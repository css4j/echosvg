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

package io.sf.carte.echosvg.test.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This utility does a pixel comparison of two images and passes if the two
 * images are identical within the specified allowance margins. It fails
 * otherwise, producing a comparison and a diff image.
 * <p>
 * Based on Batik's <code>ImageCompareTest</code> by Vincent Hardy (ASF).
 * </p>
 * 
 * @version $Id$
 */
public class ImageCompareUtil {

	private static final String ERROR_COULD_NOT_LOAD_IMAGE = "ImageCompareUtil error: could not load image ";

	private static final String IMAGE_TYPE_DIFFERENCE = "_diff";

	private static final String IMAGE_TYPE_COMPARISON = "_cmp";

	/**
	 * URL for the first image to be compared.
	 */
	private String urlAStr;
	private URL urlA;

	/**
	 * URL for the second image to be compared
	 */
	private String urlBStr;
	private URL urlB;

	private final ImageFileBuilder filenameUtil;

	/**
	 * Threshold to apply when comparing different pixels with
	 * {@link ImageComparator}.
	 */
	private final int pixelThreshold;

	/**
	 * This test makes a comparison of the two images. If the images are different,
	 * the test generates a report containing the two images and a delta images to
	 * help the user visualize the difference.
	 *
	 * @param fileUtil       the object that produces filenames for the comparison
	 *                       images.
	 * @param pixelThreshold the pixel threshold to be used in comparisons.
	 */
	public ImageCompareUtil(ImageFileBuilder fileUtil, int pixelThreshold) {
		this.filenameUtil = fileUtil;
		this.pixelThreshold = pixelThreshold;
	}

	/**
	 * This test makes a comparison of the two images. If the images are different,
	 * the test generates a report containing the two images and a delta images to
	 * help the user visualize the difference.
	 *
	 * @param fileUtil       the object that produces filenames for the comparison
	 *                       images.
	 * @param pixelThreshold the pixel threshold to be used in comparisons.
	 * @param urlA           the URL to the first image.
	 * @param urlB           the URL to the second image.
	 * @throws MalformedURLException if any of the URLs are malformed.
	 */
	public ImageCompareUtil(ImageFileBuilder fileUtil, int pixelThreshold, String urlA, String urlB)
			throws MalformedURLException {
		this.filenameUtil = fileUtil;
		this.pixelThreshold = pixelThreshold;
		urlAStr = urlA;
		urlBStr = urlB;
		setImageURLs(urlA, urlB);
	}

	/**
	 * Sets the URLs for the two images to compare.
	 * 
	 * @param urlA the URL to the first image.
	 * @param urlB the URL to the second image.
	 * @throws MalformedURLException if any of the URLs are malformed.
	 */
	public void setImageURLs(String urlA, String urlB) throws MalformedURLException {
		if (urlA == null) {
			throw new IllegalArgumentException();
		}

		if (urlB == null) {
			throw new IllegalArgumentException();
		}

		this.urlA = resolveURL(urlAStr);
		this.urlB = resolveURL(urlBStr);
	}

	/**
	 * Resolves the input string as follows. First, the string is interpreted as a
	 * file description. If the file exists, then the file name is turned into a
	 * URL. Otherwise, the string is supposed to be a URL.
	 * 
	 * @param urlStr the input string.
	 * @throws MalformedURLException if no URL could be formed.
	 */
	private URL resolveURL(String urlStr) throws MalformedURLException {
		URL url;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			// Maybe a file
			File file = new File(urlStr);
			if (file.exists()) {
				url = file.toURI().toURL();
			} else {
				throw e;
			}
		}

		return url;
	}

	/**
	 * Compare two images
	 * 
	 * @param allowedPercentBelowThreshold the allowed percentage of different
	 *                                     pixels below the threshold.
	 * @param allowedPercentBelowThreshold the allowed percentage of different
	 *                                     pixels over the threshold.
	 * @return a string describing the differences between the images, null if no
	 *         differences.
	 * @throws IOException if the comparison images could not be written.
	 */
	public String compare(float allowedPercentBelowThreshold, float allowedPercentOverThreshold)
			throws IOException {
		BufferedImage imageA = getImage(urlA);
		if (imageA == null) {
			throw new IllegalStateException(ERROR_COULD_NOT_LOAD_IMAGE + urlA.toString());
		}

		BufferedImage imageB = getImage(urlB);
		if (imageB == null) {
			throw new IllegalStateException(ERROR_COULD_NOT_LOAD_IMAGE + urlB.toString());
		}

		short result = compareImages(imageA, imageB, allowedPercentBelowThreshold,
				allowedPercentOverThreshold);

		if (result == ImageComparator.MATCH) {
			return null;
		}

		// We are in error (images are different: produce an image
		// with the two images side by side as well as a diff image)
		BufferedImage diff = ImageComparator.createDiffImage(imageA, imageB);
		BufferedImage cmp = ImageComparator.createCompareImage(imageA, imageB);

		File tmpDiff = imageToFile(diff, IMAGE_TYPE_DIFFERENCE);
		File tmpCmp = imageToFile(cmp, IMAGE_TYPE_COMPARISON);

		return "Images are different [" + ImageComparator.getResultDescription(result) + "]. Comp: "
				+ tmpCmp.getAbsolutePath() + " diff: " + tmpDiff.getAbsolutePath();

	}

	public short compareImages(BufferedImage imageA, BufferedImage imageB, float allowedPercentBelowThreshold,
			float allowedPercentOverThreshold) {
		return ImageComparator.compareImages(imageA, imageB, pixelThreshold, allowedPercentBelowThreshold,
				allowedPercentOverThreshold);
	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File imageToFile(BufferedImage img, String imageType) throws IOException {

		File imageFile = obtainDiffCmpFilename(urlA, imageType);
		//imageFile.deleteOnExit();

		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/png");

		try (OutputStream out = new FileOutputStream(imageFile)) {
			writer.writeImage(img, out);
		}

		return imageFile;

	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File obtainDiffCmpFilename(URL pngURL, String imageType) throws IOException {
		return filenameUtil.createImageFile(pngURL, imageType, ".png");
	}

	/**
	 * Loads an image from a URL
	 */
	private BufferedImage getImage(URL url) {
		ImageTagRegistry reg = ImageTagRegistry.getRegistry();
		Filter filt = reg.readURL(new ParsedURL(url));
		if (filt == null) {
			return null;
		}

		RenderedImage red = filt.createDefaultRendering();
		if (red == null) {
			return null;
		}

		BufferedImage img = new BufferedImage(red.getWidth(), red.getHeight(), BufferedImage.TYPE_INT_ARGB);
		red.copyData(img.getRaster());

		return img;
	}

}
