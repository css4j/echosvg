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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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

import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.test.image.ImageComparator;
import io.sf.carte.echosvg.test.image.ImageFileBuilder;
import io.sf.carte.echosvg.test.image.TempImageFiles;

/**
 * This test validates the operation of ImageIOImageWriter implementations.
 *
 * <p>
 * The drawing is based on PNGEncoderTest by Vincent Hardy.
 * </p>
 * 
 * @version $Id$
 */
public abstract class AbstractImageWriterCheck {

	protected BufferedImage drawImage(BufferedImage image) {
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

	protected void testEncoding(final BufferedImage image, String baseName) throws IOException {
		// Create an output stream where the image data will be stored.
		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);

		// Now, try to encode image
		ImageWriter writer = createImageWriter();
		ImageWriterParams params = createImageWriterParams();
		configureImageWriterParams(params);

		writer.writeImage(image, bos, params);

		// Now compare with reference
		assertTrue(checkIdentical(bos.toByteArray(), image, baseName),
				"Encoded file does not match reference.");
	}

	protected ImageWriterParams createImageWriterParams() {
		return new ImageWriterParams();
	}

	protected abstract ImageWriter createImageWriter();

	protected void configureImageWriterParams(ImageWriterParams params) {
	}

	protected InputStream openRefStream(String baseName) {
		String name = resourcePath(baseName);
		return getClass().getResourceAsStream(name);
	}

	String resourcePath(String baseName) {
		return '/' + getClass().getPackageName().replace('.', '/') + '/' + baseName + getImageSuffix()
				+ getDotExtension();
	}

	protected abstract String getDotExtension();

	protected abstract String getMIMEType();

	/**
	 * Compares the streams of the two images
	 * 
	 * @param cand     byte array with the candidate image.
	 * @param baseName the reference file basename.
	 * @throws IOException if an I/O error happens.
	 */
	protected boolean checkIdentical(byte[] cand, BufferedImage imgCand, String baseName)
			throws IOException {
		if (equalStreams(cand, baseName)) {
			return true;
		}

		// We are in error (images are different). Save candidate.
		ImageFileBuilder tmpUtil = new TempImageFiles(
				TestUtil.getProjectBuildURL(getClass(), TestLocations.TEST_DIRNAME));

		bytesToFile(cand, tmpUtil, baseName, "_candidate");
		// Now produce an image with the two images side by side
		// as well as a diff image.

		// First, create a reference image (which may be inaccurate due to
		// ImageIO transcoding issues, but enough to give an idea).
		BufferedImage imgRef;
		try (InputStream isRef = openRefStream(baseName)) {
			imgRef = decodeStream(isRef);
		}

		BufferedImage diff = ImageComparator.createDiffImage(imgRef, imgCand);
		BufferedImage cmp = ImageComparator.createCompareImage(imgRef, imgCand);

		imageToFile(diff, tmpUtil, baseName, "_diff");
		imageToFile(cmp, tmpUtil, baseName, "_cmp");

		return false;
	}

	protected BufferedImage decodeStream(InputStream is) throws IOException {
		return ImageIO.read(is);
	}

	protected boolean equalStreams(byte[] cand, String baseName) throws IOException {
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
	private File bytesToFile(byte[] cand, ImageFileBuilder fileBuilder, String name, String imageQualifier)
			throws IOException {

		File imageFile = obtainImageFilename(fileBuilder, name, imageQualifier);

		try (OutputStream out = new FileOutputStream(imageFile)) {
			out.write(cand);
		}

		return imageFile;

	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File imageToFile(BufferedImage img, ImageFileBuilder fileBuilder, String name, String imageQualifier)
			throws IOException {

		File imageFile = obtainImageFilename(fileBuilder, name, imageQualifier);

		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor(getMIMEType());

		try (OutputStream out = new FileOutputStream(imageFile)) {
			writer.writeImage(img, out);
		}

		return imageFile;

	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File obtainImageFilename(ImageFileBuilder fileBuilder, String name, String imageQualifier)
			throws IOException {
		return fileBuilder.createImageFile(name + getImageSuffix() + imageQualifier + getDotExtension());
	}

	protected String getImageSuffix() {
		return "";
	}

}
