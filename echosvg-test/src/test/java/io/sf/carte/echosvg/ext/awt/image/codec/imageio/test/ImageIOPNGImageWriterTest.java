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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
	protected void matchDecodeMetadata(PNGDecodeParam refParam, PNGDecodeParam candParam) {
		PNGEncodeParam refEnc = refParam.getEncodeParam();
		PNGEncodeParam candEnc = candParam.getEncodeParam();

		if (refEnc.getICCProfileName() != null) {
			if (candEnc.getICCProfileName() == null) {
				fail("Candidate is missing ICC profile data.");
			}
			assertEquals(refEnc.getICCProfileName(), candEnc.getICCProfileName(), "ICC profile name mismatch.");
		} else if (candEnc.getICCProfileName() != null) {
			fail("Candidate has unexpected ICC profile data.");
		}

		if (refEnc.isSRGBIntentSet()) {
			if (!candEnc.isSRGBIntentSet()) {
				fail("Candidate is missing sRGB chunk.");
			}
			assertEquals(refEnc.getSRGBIntent(), candEnc.getSRGBIntent(), "sRGB mismatch.");
		} else if (candEnc.isSRGBIntentSet()) {
			fail("Candidate has unexpected sRGB data.");
		} else {
			// sRGB intent is not set

			if (refEnc.isGammaSet()) {
				if (!candEnc.isGammaSet()) {
					fail("Candidate is missing gAMA chunk.");
				}
				assertEquals(refEnc.getGamma(), candEnc.getGamma(), 3e-5f, "gAMA mismatch.");
			} else if (candEnc.isGammaSet()) {
				fail("Candidate has unexpected gAMA data.");
			}

			if (refEnc.isChromaticitySet()) {
				if (!candEnc.isChromaticitySet()) {
					fail("Candidate is missing cHRM chunk.");
				}
				float[] refC = refEnc.getChromaticity();
				float[] candC = candEnc.getChromaticity();
				assertEquals(refC[0], candC[0], 3e-5f, "White X mismatch.");
				assertEquals(refC[1], candC[1], 3e-5f, "White Y mismatch.");
				assertEquals(refC[2], candC[2], 3e-5f, "Red X mismatch.");
				assertEquals(refC[3], candC[3], 3e-5f, "Red Y mismatch.");
				assertEquals(refC[4], candC[4], 3e-5f, "Green X mismatch.");
				assertEquals(refC[5], candC[5], 3e-5f, "Green Y mismatch.");
				assertEquals(refC[6], candC[6], 3e-5f, "Blue X mismatch.");
				assertEquals(refC[7], candC[7], 3e-5f, "Blue Y mismatch.");
			} else if (candEnc.isChromaticitySet()) {
				fail("Candidate has unexpected cHRM data.");
			}
		}

		if (refEnc.isTextSet()) {
			if (!candEnc.isTextSet()) {
				fail("Candidate is missing tEXt chunk.");
			}
			assertTrue(Arrays.equals(refEnc.getText(), candEnc.getText()), "tEXt data mismatch.");
		} else if (candEnc.isTextSet()) {
			fail("Candidate has unexpected tEXt data.");
		}

		if (refEnc.isInternationalTextSet()) {
			if (!candEnc.isInternationalTextSet()) {
				fail("Candidate is missing iTXt chunk.");
			}
			assertTrue(Arrays.equals(refEnc.getInternationalText(), candEnc.getInternationalText()),
					"iTXt data mismatch.");
		} else if (candEnc.isInternationalTextSet()) {
			fail("Candidate has unexpected iTXt data.");
		}

		if (refEnc.isCompressedTextSet()) {
			if (!candEnc.isCompressedTextSet()) {
				fail("Candidate is missing zTXt chunk.");
			}
			assertTrue(Arrays.equals(refEnc.getCompressedText(), candEnc.getCompressedText()),
					"zTXt data mismatch.");
		} else if (candEnc.isCompressedTextSet()) {
			fail("Candidate has unexpected zTXt data.");
		}
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
