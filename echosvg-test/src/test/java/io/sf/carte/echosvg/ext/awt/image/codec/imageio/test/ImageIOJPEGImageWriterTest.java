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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOJPEGImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.test.image.ImageComparator;
import io.sf.carte.echosvg.test.image.ImageFileBuilder;
import io.sf.carte.echosvg.test.image.TempImageFiles;

/**
 * This test validates the ImageIOJPEGImageWriter operation.
 *
 * <p>The drawing is based on PNGEncoderTest by Vincent Hardy.</p>
 * 
 * @version $Id$
 */
public class ImageIOJPEGImageWriterTest {

	private static final int width = 200;

	private static final int height = 150;

	@Test
	public void test3B_BGR() throws IOException {
		BufferedImage image = drawImage(new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR));
		testEncoding(image, "3bBgr");
	}

	@Test
	public void testICC() throws IOException {
		ICC_Profile prof;
		try (InputStream iccStream = ImageIOJPEGImageWriterTest.class.getResourceAsStream(
				"/io/sf/carte/echosvg/css/color/profiles/Display P3.icc")) {
			prof = ICC_Profile.getInstance(iccStream);
		}
		ICC_ColorSpace cs = new ICC_ColorSpace(prof);

		int[] bits = { 8, 8, 8 };
		int[] offsets = { 2, 1, 0 };

		ComponentColorModel cm = new ComponentColorModel(cs, bits, false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		ComponentSampleModel sm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, width, height,
				3, width * 3, offsets);
		Point loc = new Point(0, 0);
		WritableRaster raster = Raster.createWritableRaster(sm, loc);

		BufferedImage raw = new BufferedImage(cm, raster, false, null);
		BufferedImage image = drawImage(raw);

		testEncoding(image, "icc");
	}

	BufferedImage drawImage(BufferedImage image) {
		// Create a BufferedImage to be encoded
		Graphics2D ig = image.createGraphics();
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

		return image;
	}

	void testEncoding(final BufferedImage image, String baseName) throws IOException {
		// Create an output stream where the JPEG data
		// will be stored.
		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
		// Now, try to encode image
		ImageWriter writer = new ImageIOJPEGImageWriter();
		ImageWriterParams params = new ImageWriterParams();
		params.setJPEGQuality(1f, false);

		writer.writeImage(image, bos, params);

		// Now compare with reference
		assertTrue(checkIdentical(bos.toByteArray(), image, baseName),
				"Encoded file does not match reference.");
	}

	InputStream openRefStream(String baseName) {
		String name = resourcePath(baseName);
		return getClass().getResourceAsStream(name);
	}

	String resourcePath(String baseName) {
		return '/' + getClass().getPackageName().replace('.', '/') + '/' + baseName + getDotExtension();
	}

	protected String getDotExtension() {
		return ".jpg";
	}

	protected String getMIMEType() {
		return "image/jpeg";
	}

	/**
	 * Template method for building the JPEG input stream.
	 */
	protected InputStream buildInputStream(ByteArrayOutputStream bos) {
		return new ByteArrayInputStream(bos.toByteArray());
	}

	/**
	 * Compares the streams of the two images
	 * 
	 * @param cand     byte array with the candidate image.
	 * @param baseName the reference file basename.
	 * @throws IOException if an I/O error happens.
	 */
	private boolean checkIdentical(byte[] cand, BufferedImage imgCand, String baseName)
			throws IOException {
		if (equalStreams(cand, baseName)) {
			return true;
		}

		// We are in error (images are different: produce an image
		// with the two images side by side as well as a diff image).

		// First, create a reference image (which may be inaccurate due to
		// ImageIO transcoding issues, but enough to give an idea).
		BufferedImage imgRef;
		try (InputStream isRef = openRefStream(baseName)) {
			imgRef = decodeStream(isRef);
		}

		BufferedImage diff = ImageComparator.createDiffImage(imgRef, imgCand);
		BufferedImage cmp = ImageComparator.createCompareImage(imgRef, imgCand);

		ImageFileBuilder tmpUtil = new TempImageFiles(
				TestUtil.getProjectBuildURL(getClass(), TestLocations.TEST_DIRNAME));

		bytesToFile(cand, tmpUtil, baseName, "_candidate");
		imageToFile(diff, tmpUtil, baseName, "_diff");
		imageToFile(cmp, tmpUtil, baseName, "_cmp");

		return false;
	}

	protected BufferedImage decodeStream(InputStream is) throws IOException {
		return ImageIO.read(is);
	}

	private boolean equalStreams(byte[] cand, String baseName) throws IOException {
		byte[] bufRef = new byte[4096];
		byte[] bufCand = new byte[4096];

		InputStream isCand = new ByteArrayInputStream(cand);
		try (InputStream isRef = openRefStream(baseName)) {
			if (isRef == null) {
				// No reference
				ImageFileBuilder tmpUtil = new TempImageFiles(
						TestUtil.getProjectBuildURL(getClass(), TestLocations.TEST_DIRNAME));
				bytesToFile(cand, tmpUtil, baseName, "_candidate");
				throw new FileNotFoundException("Cannot find reference resource at " + resourcePath(baseName));
			}

			int count;
			while ((count = isRef.read(bufRef)) != -1) {
				int candCount = isCand.read(bufCand, 0, count);
				// We are reading the candidate from memory, count must be equal
				if (candCount != count || !Arrays.equals(bufRef, 0, count, bufCand, 0, count)) {
					return false;
				}
			}

			// Check that the candidate stream is empty
			if (isCand.read() != -1) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Creates a temporary File into which the bytes are saved.
	 */
	private File bytesToFile(byte[] cand, ImageFileBuilder fileBuilder, String name, String imageSuffix)
			throws IOException {

		File imageFile = obtainDiffCmpFilename(fileBuilder, name, imageSuffix);

		try (OutputStream out = new FileOutputStream(imageFile)) {
			out.write(cand);
		}

		return imageFile;

	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File imageToFile(BufferedImage img, ImageFileBuilder fileBuilder, String name, String imageSuffix)
			throws IOException {

		File imageFile = obtainDiffCmpFilename(fileBuilder, name, imageSuffix);

		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor(getMIMEType());

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
		return fileBuilder.createImageFile(name + imageSuffix + getDotExtension());
	}

}
