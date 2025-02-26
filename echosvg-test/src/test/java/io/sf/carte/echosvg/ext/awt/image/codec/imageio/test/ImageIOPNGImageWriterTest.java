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

package io.sf.carte.echosvg.ext.awt.image.codec.imageio.test;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ICC_ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.color.StandardColorSpaces;
import io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOPNGImageWriter;
import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGDecodeParam;
import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGEncodeParam;
import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGImageDecoder;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.PNGImageWriterParams;

/**
 * This test validates the ImageIOPNGImageWriter operation.
 * 
 * @version $Id$
 */
public class ImageIOPNGImageWriterTest extends AbstractImageWriterCheck<PNGDecodeParam> {

	private static final int width = 200;

	private static final int height = 150;

	@Test
	public void testRGBa() throws Exception {
		BufferedImage image = drawImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		testEncoding(image, "rgba");
	}

	@Test
	public void testICC() throws IOException {
		ICC_ColorSpace cs = StandardColorSpaces.getDisplayP3();

		int[] bits = { 16, 16, 16, 16 };
		int[] offsets = { 0, 1, 2, 3 };

		ComponentColorModel cm = new ComponentColorModel(cs, bits, true, false,
				Transparency.TRANSLUCENT, DataBuffer.TYPE_USHORT);
		ComponentSampleModel sm = new ComponentSampleModel(DataBuffer.TYPE_USHORT, width, height,
				bits.length, width * bits.length, offsets);
		Point loc = new Point(0, 0);
		WritableRaster raster = Raster.createWritableRaster(sm, loc);

		BufferedImage raw = new BufferedImage(cm, raster, false, null);
		BufferedImage image = drawImage(raw);

		PNGImageWriterParams params = new PNGImageWriterParams();

		// Not really the P3 gamma but this is a test
		params.setGamma(1f / 2.2f);

		// P3 chromaticities
		params.setChromaticity(0.31270f, 0.329f, 0.68f, 0.32f, 0.265f, 0.69f, 0.15f, 0.06f);

		testEncoding(image, "icc", params);
	}

	@Override
	protected PNGImageWriterParams createImageWriterParams() {
		return new PNGImageWriterParams();
	}

	@Override
	protected ImageWriter createImageWriter() {
		return new ImageIOPNGImageWriter();
	}

	@Override
	protected BufferedImage decodeStream(InputStream is, PNGDecodeParam param) throws IOException {
		PNGImageDecoder pngImageDecoder = new PNGImageDecoder(is, param);

		RenderedImage decodedRenderedImage = pngImageDecoder.decodeAsRenderedImage(0);

		BufferedImage decodedImage = null;
		if (decodedRenderedImage instanceof BufferedImage) {
			decodedImage = (BufferedImage) decodedRenderedImage;
		} else {
			ColorModel cm = decodedRenderedImage.getColorModel();
			if (cm.getColorSpace().isCS_sRGB()) {
				decodedImage = new BufferedImage(decodedRenderedImage.getWidth(),
						decodedRenderedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			} else {
				Point loc = new Point(0, 0);
				WritableRaster raster = Raster.createWritableRaster(decodedRenderedImage.getSampleModel(),
						loc);
				decodedImage = new BufferedImage(cm, raster, false, null);
			}
			Graphics2D ig = decodedImage.createGraphics();
			ig.drawRenderedImage(decodedRenderedImage, new AffineTransform());
			ig.dispose();
		}

		return decodedImage;
	}

	@Override
	protected PNGDecodeParam createDecodeParam() {
		PNGDecodeParam param = new PNGDecodeParam();
		param.setGenerateEncodeParam(true);
		return param;
	}

	@Override
	protected String matchDecodeMetadata(PNGDecodeParam refParam, PNGDecodeParam candParam) {
		PNGEncodeParam refEnc = refParam.getEncodeParam();
		PNGEncodeParam candEnc = candParam.getEncodeParam();

		String msg;

		if (refEnc.getICCProfileName() != null) {
			if (candEnc.getICCProfileName() == null) {
				return "Candidate is missing ICC profile data.";
			}
			msg = notEqualsMsg(refEnc.getICCProfileName(), candEnc.getICCProfileName(),
					"ICC profile name mismatch.");
			if (msg != null) {
				return msg;
			}
		} else if (candEnc.getICCProfileName() != null) {
			return "Candidate has unexpected ICC profile data.";
		}

		if (refEnc.isSRGBIntentSet()) {
			if (!candEnc.isSRGBIntentSet()) {
				return "Candidate is missing sRGB chunk.";
			}
			msg = notEqualsMsg(refEnc.getSRGBIntent(), candEnc.getSRGBIntent(), "sRGB mismatch.");
			if (msg != null) {
				return msg;
			}
		} else if (candEnc.isSRGBIntentSet()) {
			return "Candidate has unexpected sRGB data.";
		} else {
			// sRGB intent is not set

			if (refEnc.isGammaSet()) {
				if (!candEnc.isGammaSet()) {
					return "Candidate is missing gAMA chunk.";
				}
				msg = notEqualsMsg(refEnc.getGamma(), candEnc.getGamma(), 3e-5f, "gAMA mismatch.");
				if (msg != null) {
					return msg;
				}
			} else if (candEnc.isGammaSet()) {
				return "Candidate has unexpected gAMA data.";
			}

			if (refEnc.isChromaticitySet()) {
				if (!candEnc.isChromaticitySet()) {
					return "Candidate is missing cHRM chunk.";
				}
				float[] refC = refEnc.getChromaticity();
				float[] candC = candEnc.getChromaticity();
				msg = notEqualsMsg(refC[0], candC[0], 3e-5f, "White X mismatch.");
				if (msg != null) {
					return msg;
				}
				msg = notEqualsMsg(refC[1], candC[1], 3e-5f, "White Y mismatch.");
				if (msg != null) {
					return msg;
				}
				msg = notEqualsMsg(refC[2], candC[2], 3e-5f, "Red X mismatch.");
				if (msg != null) {
					return msg;
				}
				msg = notEqualsMsg(refC[3], candC[3], 3e-5f, "Red Y mismatch.");
				if (msg != null) {
					return msg;
				}
				msg = notEqualsMsg(refC[4], candC[4], 3e-5f, "Green X mismatch.");
				if (msg != null) {
					return msg;
				}
				msg = notEqualsMsg(refC[5], candC[5], 3e-5f, "Green Y mismatch.");
				if (msg != null) {
					return msg;
				}
				msg = notEqualsMsg(refC[6], candC[6], 3e-5f, "Blue X mismatch.");
				if (msg != null) {
					return msg;
				}
				msg = notEqualsMsg(refC[7], candC[7], 3e-5f, "Blue Y mismatch.");
				if (msg != null) {
					return msg;
				}
			} else if (candEnc.isChromaticitySet()) {
				return "Candidate has unexpected cHRM data.";
			}
		}

		if (refEnc.isTextSet()) {
			if (!candEnc.isTextSet()) {
				return "Candidate is missing tEXt chunk.";
			}
			if (!Arrays.equals(refEnc.getText(), candEnc.getText())) {
				return "tEXt data mismatch.";
			}
		} else if (candEnc.isTextSet()) {
			return "Candidate has unexpected tEXt data.";
		}

		if (refEnc.isInternationalTextSet()) {
			if (!candEnc.isInternationalTextSet()) {
				return "Candidate is missing iTXt chunk.";
			}
			if (!Arrays.equals(refEnc.getInternationalText(), candEnc.getInternationalText())) {
				return "iTXt data mismatch.";
			}
		} else if (candEnc.isInternationalTextSet()) {
			return "Candidate has unexpected iTXt data.";
		}

		if (refEnc.isCompressedTextSet()) {
			if (!candEnc.isCompressedTextSet()) {
				return "Candidate is missing zTXt chunk.";
			}
			if (!Arrays.equals(refEnc.getCompressedText(), candEnc.getCompressedText())) {
				return "zTXt data mismatch.";
			}
		} else if (candEnc.isCompressedTextSet()) {
			return "Candidate has unexpected zTXt data.";
		}

		return null;
	}

	private static String notEqualsMsg(Object obj1, Object obj2, String prefix) {
		if (!Objects.equals(obj1, obj2)) {
			return prefix + ": expected '" + obj1.toString() + "' got '" + obj2.toString() + '\'';
		}
		return null;
	}

	private static String notEqualsMsg(int f1, int f2, String prefix) {
		if (f1 != f2) {
			return prefix + ": expected '" + f1 + "' got '" + f2 + '\'';
		}
		return null;
	}

	private static String notEqualsMsg(float f1, float f2, float tol, String prefix) {
		if (Math.abs(f1 - f2) > tol) {
			return prefix + ": expected '" + f1 + "' got '" + f2 + '\'';
		}
		return null;
	}

	@Override
	protected String getDotExtension() {
		return ".png";
	}

	@Override
	protected String getMIMEType() {
		return "image/png";
	}

}
