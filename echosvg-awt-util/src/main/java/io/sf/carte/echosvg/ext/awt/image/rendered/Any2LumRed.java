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
package io.sf.carte.echosvg.ext.awt.image.rendered;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import io.sf.carte.echosvg.ext.awt.ColorSpaceHintKey;
import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;

/**
 * This function will tranform an image from any colorspace into a luminance
 * image. The alpha channel if any will be copied to the new image.
 *
 * @author <a href="mailto:Thomas.DeWeeese@Kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class Any2LumRed extends AbstractRed {

	boolean isColorConvertOpAplhaSupported;

	/**
	 * Construct a luminace image from src.
	 *
	 * @param src The image to convert to a luminance image
	 */
	public Any2LumRed(CachableRed src) {
		super(src, src.getBounds(), fixColorModel(src), fixSampleModel(src), src.getTileGridXOffset(),
				src.getTileGridYOffset(), null);

		isColorConvertOpAplhaSupported = getColorConvertOpAplhaSupported();

		props.put(ColorSpaceHintKey.PROPERTY_COLORSPACE, ColorSpaceHintKey.VALUE_COLORSPACE_GREY);
	}

	@Override
	public WritableRaster copyData(WritableRaster wr) {
		// Get my source.
		CachableRed src = (CachableRed) getSources().get(0);

		SampleModel sm = src.getSampleModel();
		ColorModel srcCM = src.getColorModel();
		Raster srcRas = src.getData(wr.getBounds());
		if (srcCM == null) {
			// We don't really know much about this source.

			float[][] matrix = null;
			if (sm.getNumBands() == 2) {
				matrix = new float[2][2];
				matrix[0][0] = 1;
				matrix[1][1] = 1;
			} else {
				matrix = new float[sm.getNumBands()][1];
				matrix[0][0] = 1;
			}

			BandCombineOp op = new BandCombineOp(matrix, null);
			op.filter(srcRas, wr);
		} else {
			WritableRaster srcWr = (WritableRaster) srcRas;

			// Divide out alpha if we have it. We need to do this since
			// the color convert may not be a linear operation which may
			// lead to out of range values.
			if (srcCM.hasAlpha())
				GraphicsUtil.coerceData(srcWr, srcCM, false);

			BufferedImage srcBI, dstBI;
			srcBI = new BufferedImage(srcCM, srcWr.createWritableTranslatedChild(0, 0), false, null);
			ColorModel dstCM = getColorModel();

			if (dstCM.hasAlpha() && !isColorConvertOpAplhaSupported) {

				// All this nonsense is to work around the fact that the
				// Color convert op doesn't properly copy the Alpha from
				// src to dst:
				// https://bugs.openjdk.java.net/browse/JDK-8005930

				PixelInterleavedSampleModel dstSM;
				dstSM = (PixelInterleavedSampleModel) wr.getSampleModel();
				SampleModel smna = new PixelInterleavedSampleModel(dstSM.getDataType(), dstSM.getWidth(),
						dstSM.getHeight(), dstSM.getPixelStride(), dstSM.getScanlineStride(), new int[] { 0 });

				WritableRaster dstWr;
				dstWr = Raster.createWritableRaster(smna, wr.getDataBuffer(), new Point(0, 0));
				dstWr = dstWr.createWritableChild(wr.getMinX() - wr.getSampleModelTranslateX(),
						wr.getMinY() - wr.getSampleModelTranslateY(), wr.getWidth(), wr.getHeight(), 0, 0, null);

				ColorModel cmna = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8 },
						false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

				dstBI = new BufferedImage(cmna, dstWr, false, null);
			} else {
				// No alpha ao we don't have to work around the bug
				// in the color convert op.
				dstBI = new BufferedImage(dstCM, wr.createWritableTranslatedChild(0, 0), dstCM.isAlphaPremultiplied(),
						null);
			}

			ColorConvertOp op = new ColorConvertOp(null);
			op.filter(srcBI, dstBI);

			// Have to 'fix' alpha premult
			if (dstCM.hasAlpha()) {
				copyBand(srcWr, sm.getNumBands() - 1, wr, getSampleModel().getNumBands() - 1);
				if (dstCM.isAlphaPremultiplied())
					GraphicsUtil.multiplyAlpha(wr);
			}
		}
		return wr;
	}

	/**
	 * This function 'fixes' the source's color model. Right now it just selects if
	 * it should have one or two bands based on if the source had an alpha channel.
	 */
	protected static ColorModel fixColorModel(CachableRed src) {
		ColorModel cm = src.getColorModel();
		if (cm != null) {
			if (cm.hasAlpha())
				return new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8, 8 }, true,
						cm.isAlphaPremultiplied(), Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

			return new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8 }, false, false,
					Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		} else {
			// No ColorModel so try to make some intelligent
			// decisions based just on the number of bands...
			// 1 bands -> lum
			// 2 bands -> lum (Band 0) & alpha (Band 1)
			// >2 bands -> lum (Band 0) - No color conversion...
			SampleModel sm = src.getSampleModel();

			if (sm.getNumBands() == 2)
				return new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8, 8 }, true,
						true, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

			return new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8 }, false, false,
					Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		}
	}

	/**
	 * This function 'fixes' the source's sample model. Right now it just selects if
	 * it should have one or two bands based on if the source had an alpha channel.
	 */
	protected static SampleModel fixSampleModel(CachableRed src) {
		SampleModel sm = src.getSampleModel();

		int width = sm.getWidth();
		int height = sm.getHeight();

		ColorModel cm = src.getColorModel();
		if (cm != null) {
			if (cm.hasAlpha())
				return new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 2, 2 * width,
						new int[] { 0, 1 });

			return new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 1, width, new int[] { 0 });
		} else {
			// No ColorModel so try to make some intelligent
			// decisions based just on the number of bands...
			// 1 bands -> lum
			// 2 bands -> lum (Band 0) & alpha (Band 1)
			// >2 bands -> lum (Band 0) - No color conversion...
			if (sm.getNumBands() == 2)
				return new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 2, 2 * width,
						new int[] { 0, 1 });

			return new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 1, width, new int[] { 0 });
		}
	}

	protected static boolean getColorConvertOpAplhaSupported() {
		int size = 50;

		// create source image filled with an opaque color
		BufferedImage srcImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		Graphics2D srcGraphics = srcImage.createGraphics();
		srcGraphics.setColor(Color.red);
		srcGraphics.fillRect(0, 0, size, size);
		srcGraphics.dispose();

		// create clear (transparent black) destination image
		BufferedImage dstImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		Graphics2D dstGraphics = dstImage.createGraphics();
		dstGraphics.setComposite(AlphaComposite.Clear);
		dstGraphics.fillRect(0, 0, size, size);
		dstGraphics.dispose();

		ColorSpace grayColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(grayColorSpace, null);
		op.filter(srcImage, dstImage);

		return getAlpha(srcImage) == getAlpha(dstImage);
	}

	protected static int getAlpha(BufferedImage bufferedImage) {
		int x = bufferedImage.getWidth() / 2;
		int y = bufferedImage.getHeight() / 2;
		return 0xff & (bufferedImage.getRGB(x, y) >> 24);
	}
}
