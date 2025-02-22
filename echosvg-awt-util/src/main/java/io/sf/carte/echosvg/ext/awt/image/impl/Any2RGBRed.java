/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.ext.awt.image.impl;

import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

import io.sf.carte.echosvg.ext.awt.color.StandardColorSpaces;
import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.rendered.AbstractRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.CachableRed;

/**
 * This class is used when transforming an image from any colorspace into a RGB
 * image in the given color space.
 * <p>
 * The alpha channel if any will be copied to the new image.
 * </p>
 * <p>
 * This is an implementation class and is not part of the official EchoSVG API.
 * </p>
 *
 * <p>
 * Based on <code>Any2sRGBRed</code> by Thomas DeWeese.
 * </p>
 * 
 * @version $Id$
 */
public class Any2RGBRed extends AbstractRed {

	/**
	 * Construct a <code>colorSpace</code> image from src.
	 *
	 * @param src        The image to convert to a luminance image
	 * @param colorSpace The current working RGB color space, or {@code null} if
	 *                   sRGB. Must be of type {@code TYPE_RGB}.
	 */
	public Any2RGBRed(CachableRed src, ColorSpace colorSpace) {
		super(src, src.getBounds(), mergeColorModel(src, colorSpace), fixSampleModel(src),
				src.getTileGridXOffset(), src.getTileGridYOffset(), null);
	}

	private static boolean is_INT_PACK_COMP(SampleModel sm) {
		if (!(sm instanceof SinglePixelPackedSampleModel))
			return false;

		// Check transfer types
		if (sm.getDataType() != DataBuffer.TYPE_INT)
			return false;

		SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel) sm;

		int[] masks = sppsm.getBitMasks();
		if ((masks.length != 3) && (masks.length != 4))
			return false;
		if (masks[0] != 0x00ff0000)
			return false;
		if (masks[1] != 0x0000ff00)
			return false;
		if (masks[2] != 0x000000ff)
			return false;
		if ((masks.length == 4) && (masks[3] != 0xff000000))
			return false;

		return true;
	}

	@Override
	public WritableRaster copyData(WritableRaster wr) {
		// Get my source.
		CachableRed src = (CachableRed) getSources().get(0);
		ColorModel srcCM = src.getColorModel();
		SampleModel srcSM = src.getSampleModel();

		if (srcCM == null) {
			// We don't really know much about this source, let's
			// guess based on the number of bands...

			float[][] matrix = null;
			switch (srcSM.getNumBands()) {
			case 1:
				matrix = new float[3][1];
				matrix[0][0] = 1; // Red
				matrix[1][0] = 1; // Grn
				matrix[2][0] = 1; // Blu
				break;
			case 2:
				matrix = new float[4][2];
				matrix[0][0] = 1; // Red
				matrix[1][0] = 1; // Grn
				matrix[2][0] = 1; // Blu
				matrix[3][1] = 1; // Alpha
				break;
			case 3:
				matrix = new float[3][3];
				matrix[0][0] = 1; // Red
				matrix[1][1] = 1; // Grn
				matrix[2][2] = 1; // Blu
				break;
			default:
				matrix = new float[4][srcSM.getNumBands()];
				matrix[0][0] = 1; // Red
				matrix[1][1] = 1; // Grn
				matrix[2][2] = 1; // Blu
				matrix[3][3] = 1; // Alpha
				break;
			}

			Raster srcRas = src.getData(wr.getBounds());
			BandCombineOp op = new BandCombineOp(matrix, null);
			op.filter(srcRas, wr);

			return wr;
		}

		if (srcCM.getColorSpace() == ColorSpace.getInstance(ColorSpace.CS_GRAY)) {
			// This is a little bit of a hack. There is only
			// a linear grayscale ICC profile in the JDK so
			// many things use this when the data _really_
			// has sRGB gamma applied.
			try {
				float[][] matrix = null;
				switch (srcSM.getNumBands()) {
				case 1:
					matrix = new float[3][1];
					matrix[0][0] = 1; // Red
					matrix[1][0] = 1; // Grn
					matrix[2][0] = 1; // Blu
					break;
				case 2:
				default:
					matrix = new float[4][2];
					matrix[0][0] = 1; // Red
					matrix[1][0] = 1; // Grn
					matrix[2][0] = 1; // Blu
					matrix[3][1] = 1; // Alpha
					break;
				}
				Raster srcRas = src.getData(wr.getBounds());
				BandCombineOp op = new BandCombineOp(matrix, null);
				op.filter(srcRas, wr);
			} catch (Throwable t) {
				t.printStackTrace();
			}

			return wr;
		}

		ColorModel dstCM = getColorModel();
		if (srcCM.getColorSpace() == dstCM.getColorSpace()) {
			// No transform needed, just reformat data...
			// System.out.println("Bypassing");

			if (is_INT_PACK_COMP(srcSM))
				src.copyData(wr);
			else
				GraphicsUtil.copyData(src.getData(wr.getBounds()), wr);

			return wr;
		}

		Raster srcRas = src.getData(wr.getBounds());
		WritableRaster srcWr = (WritableRaster) srcRas;

		// Divide out alpha if we have it. We need to do this since
		// the color convert may not be a linear operation which may
		// lead to out of range values.
		ColorModel srcBICM = srcCM;
		if (srcCM.hasAlpha()) {
			srcBICM = GraphicsUtil.coerceData(srcWr, srcCM, false);
		}

		BufferedImage srcBI, dstBI;
		srcBI = new BufferedImage(srcBICM, srcWr.createWritableTranslatedChild(0, 0), false, null);

		ColorConvertOp op = new ColorConvertOp(dstCM.getColorSpace(), null);
		dstBI = op.filter(srcBI, null);

		WritableRaster wr00 = wr.createWritableTranslatedChild(0, 0);
		for (int i = 0; i < dstCM.getColorSpace().getNumComponents(); i++) {
			copyBand(dstBI.getRaster(), i, wr00, i);
		}

		if (dstCM.hasAlpha()) {
			copyBand(srcWr, srcSM.getNumBands() - 1, wr, getSampleModel().getNumBands() - 1);
		}

		return wr;
	}

	/**
	 * This function merges the source's color space with the requested one.
	 */
	private static ColorModel mergeColorModel(CachableRed src, ColorSpace colorSpace) {
		ColorModel cm = src.getColorModel();
		if (cm != null) {
			ColorSpace cs = cm.getColorSpace();
			if (cs.getType() != ColorSpace.TYPE_RGB) {
				if (cm.hasAlpha()) {
					cm = GraphicsUtil.sRGB_Unpre;
				} else {
					// RGB pre-multiplied
					cm = GraphicsUtil.sRGB;
				}
			} else {
				cs = StandardColorSpaces.mergeColorSpace(colorSpace, cs);
				if (cs == null || cs.isCS_sRGB()) {
					if (cm.hasAlpha()) {
						cm = GraphicsUtil.sRGB_Unpre;
					} else {
						cm = GraphicsUtil.sRGB;
					}
				} else if (cm.hasAlpha()) {
					cm = new DirectColorModel(cs, 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, false,
							DataBuffer.TYPE_INT);
				} else {
					cm = new DirectColorModel(cs, 24, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, false,
							DataBuffer.TYPE_INT);
				}
			}
		} else {
			// No ColorModel so try to make some intelligent
			// decisions based just on the number of bands...
			// 1 bands -> replicated into RGB
			// 2 bands -> Band 0 replicated into RGB & Band 1 -> alpha premult
			// 3 bands -> sRGB (not-linear?)
			// 4 bands -> sRGB premult (not-linear?)

			int numbands = src.getSampleModel().getNumBands();

			if (colorSpace == null || colorSpace.isCS_sRGB()) {
				switch (numbands) {
				case 1:
				case 3:
					cm = GraphicsUtil.sRGB;
					break;
				default:
					cm = GraphicsUtil.sRGB_Unpre;
				}
			} else {
				switch (numbands) {
				case 1:
				case 3:
					cm = new DirectColorModel(colorSpace, 24, 0x00FF0000, 0x0000FF00, 0x000000FF, 0x0, false,
							DataBuffer.TYPE_INT);
					break;
				default:
					cm = new DirectColorModel(colorSpace, 32, 0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, false,
							DataBuffer.TYPE_INT);
				}
			}
		}

		return cm;
	}

	/**
	 * This function 'fixes' the source's sample model. Right now it just selects if
	 * it should have 3 or 4 bands based on if the source had an alpha channel.
	 */
	private static SampleModel fixSampleModel(CachableRed src) {
		SampleModel sm = src.getSampleModel();
		ColorModel cm = src.getColorModel();

		boolean alpha = false;

		if (cm != null) {
			alpha = cm.hasAlpha();
		} else {
			switch (sm.getNumBands()) {
			case 1:
			case 3:
				alpha = false;
				break;
			default:
				alpha = true;
				break;
			}
		}

		if (alpha) {
			sm = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, sm.getWidth(), sm.getHeight(),
					new int[] { 0xFF0000, 0xFF00, 0xFF, 0xFF000000 });
		} else {
			sm = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, sm.getWidth(), sm.getHeight(),
					new int[] { 0xFF0000, 0xFF00, 0xFF });
		}

		return sm;
	}

}
