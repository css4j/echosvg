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

package io.sf.carte.echosvg.test.svg;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.image.ImageComparator;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This test does a pixel comparison of two images and passes if the two images
 * are identical. It fails otherwise, producing a report describing why the two
 * images are different.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ImageCompareTest {
	private static final String ERROR_COULD_NOT_LOAD_IMAGE = "ImageCompareTest error: could not load image ";

	private static final String IMAGE_TYPE_DIFFERENCE = "_diff";

	private static final String IMAGE_TYPE_COMPARISON = "_cmp";

	/**
	 * Prefix for the temporary files created by Tests of this class
	 */
	private static final String TEMP_FILE_PREFIX = "ImageCompareTest";

	/**
	 * Suffix for the temporary files created by Tests of this class
	 */
	private static final String TEMP_FILE_SUFFIX = "";

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

	/**
	 * Resolves the input string as follows. + First, the string is interpreted as a
	 * file description. If the file exists, then the file name is turned into a
	 * URL. + Otherwise, the string is supposed to be a URL. If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 */
	private URL resolveURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(url);
		}
	}

	/**
	 * This test makes a comparison of the two images. If the images are different,
	 * the test generates a report containing the two images and a delta images to
	 * help the user visualize the difference.
	 *
	 * @param urlA first image
	 * @param urlB second image
	 */
	public ImageCompareTest(String urlA, String urlB) {
		urlAStr = urlA;
		urlBStr = urlB;
	}

	private void initURLs() {
		if (urlAStr == null) {
			throw new IllegalArgumentException();
		}

		if (urlBStr == null) {
			throw new IllegalArgumentException();
		}

		this.urlA = resolveURL(urlAStr);
		this.urlB = resolveURL(urlBStr);
	}

	/**
	 * Compare two images
	 * 
	 * @param allowingPercentOfDifferentPixels the allowed percentage of different pixels.
	 * @return a string describing the differences between the images, null if no differences.
	 */
	public String compare(float allowedPercentBelowThreshold, float allowedPercentOverThreshold) throws IOException {
		initURLs();

		BufferedImage imageA = getImage(urlA);
		if (imageA == null) {
			throw new IllegalStateException(ERROR_COULD_NOT_LOAD_IMAGE + urlA.toString());
		}

		BufferedImage imageB = getImage(urlB);
		if (imageB == null) {
			throw new IllegalStateException(ERROR_COULD_NOT_LOAD_IMAGE + urlB.toString());
		}

		short result = AbstractRenderingAccuracyTest.compareImages(imageA, imageB, allowedPercentBelowThreshold,
				allowedPercentOverThreshold);

		if (result == ImageComparator.MATCH) {
			return null;
		}

		// We are in error (images are different: produce an image
		// with the two images side by side as well as a diff image)
		BufferedImage diff = buildDiffImage(imageA, imageB);
		BufferedImage cmp = ImageComparator.createCompareImage(imageA, imageB);

		File tmpDiff = imageToFile(diff, IMAGE_TYPE_DIFFERENCE);
		File tmpCmp = imageToFile(cmp, IMAGE_TYPE_COMPARISON);

		return "Images are different [code " + result + "]. Comp: " + tmpCmp.getAbsolutePath() + " diff: "
				+ tmpDiff.getAbsolutePath();

	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File imageToFile(BufferedImage img, String imageType) throws IOException {

		File imageFile = obtainDiffCmpFilename(imageType);
		//imageFile.deleteOnExit();

		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/png");
		OutputStream out = new FileOutputStream(imageFile);
		try {
			writer.writeImage(img, out);
		} finally {
			out.close();
		}

		return imageFile;

	}

	/**
	 * Creates a temporary File into which the input image is saved.
	 */
	private File obtainDiffCmpFilename(String imageType) throws IOException {
		return TestLocations.getTempFilename(urlA, TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, imageType, ".png");
	}

	/**
	 * Builds a new BufferedImage that is the difference between the two input
	 * images
	 */
	private static BufferedImage buildDiffImage(BufferedImage ref, BufferedImage gen) {
		BufferedImage diff = new BufferedImage(ref.getWidth(), ref.getHeight(), BufferedImage.TYPE_INT_ARGB);
		WritableRaster refWR = ref.getRaster();
		WritableRaster genWR = gen.getRaster();
		WritableRaster dstWR = diff.getRaster();

		boolean refPre = ref.isAlphaPremultiplied();
		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, true);
			ref = new BufferedImage(cm, refWR, true, null);
		}
		boolean genPre = gen.isAlphaPremultiplied();
		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, true);
			gen = new BufferedImage(cm, genWR, true, null);
		}

		int w = ref.getWidth();
		int h = ref.getHeight();

		int y, i, val;
		int[] refPix = null;
		int[] genPix = null;
		for (y = 0; y < h; y++) {
			refPix = refWR.getPixels(0, y, w, 1, refPix);
			genPix = genWR.getPixels(0, y, w, 1, genPix);
			for (i = 0; i < refPix.length; i++) {
				// val = ((genPix[i]-refPix[i])*5)+128;
				val = ((refPix[i] - genPix[i]) * 10) + 128;
				if ((val & 0xFFFFFF00) != 0)
					if ((val & 0x80000000) != 0)
						val = 0;
					else
						val = 255;
				genPix[i] = val;
			}
			dstWR.setPixels(0, y, w, 1, genPix);
		}

		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, false);
		}

		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, false);
		}

		return diff;
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
