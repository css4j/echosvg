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

package io.sf.carte.echosvg.ext.awt.image.codec.png.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGDecodeParam;
import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGEncodeParam;
import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGImageDecoder;
import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGImageEncoder;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.test.image.ImageComparator;
import io.sf.carte.echosvg.test.image.ImageFileBuilder;
import io.sf.carte.echosvg.test.image.TempImageFiles;

/**
 * This test validates the PNGEncoder operation. It creates a BufferedImage,
 * then encodes it with the PNGEncoder, then decodes it and compares the decoded
 * image with the original one.
 *
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PNGEncoderTest {

	private static final String[] loremIpsum = { "LoremIpsum 1",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, ", "LoremIpsum 2",
			"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "LoremIpsum 3",
			" Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi", "LoremIpsum 4",
			" ut aliquip ex ea commodo consequat." };

	private static final String[] grouchoContract = { "Groucho contract scene 1",
			"The first part of the party of the first part shall be known in this contract",
			"Groucho contract scene 2", " as the first part of the party of the first part.",
			"Groucho contract scene 3",
			"The party of the second part shall be know in this contract as the party of the second part." };

	@Test
	public void testRGBa() throws Exception {
		BufferedImage image = drawImage(new BufferedImage(100, 75, BufferedImage.TYPE_INT_ARGB));
		testEncoding(image);
	}

	@Test
	public void testTEXtZTEXt() throws Exception {
		BufferedImage image = drawImage(new BufferedImage(100, 75, BufferedImage.TYPE_INT_ARGB));
		PNGEncodeParam params = PNGEncodeParam.getDefaultEncodeParam(image);
		params.setText(loremIpsum);
		params.setCompressedText(grouchoContract);
		testEncoding(image, params);
	}

	@Test
	public void testICC() throws Exception {
		ICC_Profile prof;
		try (InputStream iccStream = PNGEncoderTest.class.getResourceAsStream(
				"/io/sf/carte/echosvg/css/color/profiles/Display P3.icc")) {
			prof = ICC_Profile.getInstance(iccStream);
		}
		ICC_ColorSpace cs = new ICC_ColorSpace(prof);

		int[] bits = { 16, 16, 16, 16 };
		ComponentColorModel cm = new ComponentColorModel(cs, bits, true, false,
				Transparency.TRANSLUCENT, DataBuffer.TYPE_USHORT);
		ComponentSampleModel sm = new ComponentSampleModel(DataBuffer.TYPE_USHORT, 100, 75, 4, 100 * 4,
				new int[] { 0, 1, 2, 3 });
		Point loc = new Point(0, 0);
		WritableRaster raster = Raster.createWritableRaster(sm, loc);

		BufferedImage raw = new BufferedImage(cm, raster, false, null);
		BufferedImage image = drawImage(raw);

		testEncoding(image);
	}

	BufferedImage drawImage(BufferedImage image) {
		// Create a BufferedImage to be encoded
		Graphics2D ig = image.createGraphics();
		ig.scale(.5, .5);
		ig.setPaint(new Color(128, 0, 0));
		ig.fillRect(0, 0, 100, 50);
		ig.setPaint(Color.orange);
		ig.fillRect(100, 0, 100, 50);
		ig.setPaint(Color.yellow);
		ig.fillRect(0, 50, 100, 50);
		ig.setPaint(Color.red);
		ig.fillRect(100, 50, 100, 50);
		ig.setPaint(new Color(255, 127, 127));
		ig.fillRect(0, 100, 100, 50);
		ig.setPaint(Color.black);
		ig.draw(new Rectangle2D.Double(0.5, 0.5, 199, 149));
		ig.dispose();

		return image.getSubimage(50, 0, 50, 25);
	}

	void testEncoding(final BufferedImage image) throws Exception {
		PNGEncodeParam params = PNGEncodeParam.getDefaultEncodeParam(image);
		testEncoding(image, params);
	}

	void testEncoding(final BufferedImage image, PNGEncodeParam params) throws Exception {
		// Create an output stream where the PNG data
		// will be stored.
		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
		try (OutputStream os = buildOutputStream(bos)) {
			// Now, try to encode image
			PNGImageEncoder pngImageEncoder = new PNGImageEncoder(os, params);

			pngImageEncoder.encode(image);
		}

		PNGDecodeParam param = new PNGDecodeParam();

		String[] text = null;
		if (params.isTextSet()) {
			text = params.getText();
			param.setGenerateEncodeParam(true);
		}

		String[] zText = null;
		if (params.isCompressedTextSet()) {
			zText = params.getCompressedText();
			param.setGenerateEncodeParam(true);
		}

		// Now, try to decode image
		InputStream is = buildInputStream(bos);

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

		// Check text
		if (text != null) {
			PNGEncodeParam encodeParams = param.getEncodeParam();
			assertNotNull(encodeParams, "EncodeParam should be generated.");
			assertTrue(encodeParams.isTextSet());
			String[] decText = encodeParams.getText();
			assertTrue(Arrays.equals(text, decText), "tEXt does not match.");
		}

		// Check zText
		if (zText != null) {
			PNGEncodeParam encodeParams = param.getEncodeParam();
			assertNotNull(encodeParams, "EncodeParam should be generated.");
			assertTrue(encodeParams.isCompressedTextSet());
			String[] decZText = encodeParams.getCompressedText();
			assertTrue(Arrays.equals(zText, decZText), "zTXt does not match.");
		}

		// Compare images
		assertTrue(checkIdentical(image, decodedImage), "Images are not identical");
	}

	/**
	 * Template method for building the PNG output stream. This gives a chance to
	 * sub-classes (e.g., Base64PNGEncoderTest) to add an additional encoding.
	 */
	protected OutputStream buildOutputStream(ByteArrayOutputStream bos) {
		return bos;
	}

	/**
	 * Template method for building the PNG input stream. This gives a chance to
	 * sub-classes (e.g., Base64PNGEncoderTest) to add an additional decoding.
	 */
	protected InputStream buildInputStream(ByteArrayOutputStream bos) {
		return new ByteArrayInputStream(bos.toByteArray());
	}

	/**
	 * Compares the data for the two images
	 * @throws IOException 
	 */
	private boolean checkIdentical(BufferedImage imgA, BufferedImage imgB) throws IOException {
		short result = ImageComparator.compareImages(imgA, imgB, 8, 0, 0);

		if (result == ImageComparator.MATCH) {
			return true;
		}

		// We are in error (images are different: produce an image
		// with the two images side by side as well as a diff image)
		BufferedImage diff = ImageComparator.createDiffImage(imgA, imgB);
		BufferedImage cmp = ImageComparator.createCompareImage(imgA, imgB);

		ImageFileBuilder tmpUtil = new TempImageFiles(
				TestUtil.getProjectBuildURL(getClass(), TestLocations.TEST_DIRNAME));

		imageToFile(diff, tmpUtil, "PNGEncoderTest", "_diff");
		imageToFile(cmp, tmpUtil, "PNGEncoderTest", "_cmp");

		System.err.println(ImageComparator.getResultDescription(result));

		return false;
	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File imageToFile(BufferedImage img, ImageFileBuilder fileBuilder, String name, String imageSuffix)
			throws IOException {

		File imageFile = obtainDiffCmpFilename(fileBuilder, name, imageSuffix);

		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/png");

		try (OutputStream out = new FileOutputStream(imageFile)) {
			writer.writeImage(img, out);
		}

		return imageFile;

	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File obtainDiffCmpFilename(ImageFileBuilder fileBuilder, String name, String imageSuffix)
			throws IOException {
		return fileBuilder.createImageFile(name + imageSuffix + ".png");
	}

}
