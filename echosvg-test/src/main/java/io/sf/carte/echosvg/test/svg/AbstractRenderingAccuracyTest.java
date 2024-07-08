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
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.test.image.ImageComparator;
import io.sf.carte.echosvg.test.image.TempImageFiles;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Checks for regressions in rendering a specific SVG document. The
 * <code>Test</code> will rasterize and SVG document and compare it to a
 * reference image. The test passes if the rasterized SVG and the reference
 * image match exactly (i.e., all pixel values are the same).
 *
 * @author <a href="mailto:vhardy@apache.lorg">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractRenderingAccuracyTest {

	/**
	 * Messages expressing that an image could not be loaded. {0} : URL for the
	 * reference image.
	 */
	private static final String COULD_NOT_LOAD_IMAGE = "SVGRenderingAccuracyTest.message.error.could.not.load.image";

	/**
	 * Message expressing that the variation URL could not be open {0} : URL
	 */
	private static final String COULD_NOT_OPEN_VARIATION_URL = "SVGRenderingAccuracyTest.message.warning.could.not.open.variation.url";

	/**
	 * The gui resources file name
	 */
	private static final String CONFIGURATION_RESOURCES = "io.sf.carte.echosvg.test.svg.resources.Configuration";

	/**
	 * Suffix used for comparison images
	 */
	private static final String IMAGE_TYPE_COMPARISON = "_cmp";

	/**
	 * Suffix used for diff images
	 */
	private static final String IMAGE_TYPE_DIFF = "_diff";

	/**
	 * Suffix used for saved images (e.g., ".png")
	 */
	private static final String IMAGE_FILE_DOT_EXTENSION = ".png";

	/**
	 * The configuration resource bundle
	 */
	static ResourceBundle configuration;

	static {
		configuration = ResourceBundle.getBundle(CONFIGURATION_RESOURCES, Locale.getDefault());
	}

	private final String PROJECT_ROOT_URL = TestUtil.getRootProjectURL(getClass(), getProjectName());

	private final TempImageFiles tmpUtil = new TempImageFiles(
			TestUtil.getProjectBuildURL(getClass(), getProjectName()));

	/**
	 * The URL where the SVG can be found.
	 */
	private URL svgURL;

	/**
	 * The URL for the reference image
	 */
	protected URL refImgURL;

	/**
	 * Threshold to apply when comparing different pixels with
	 * {@link ImageComparator}.
	 */
	private final int pixelThreshold;

	/**
	 * A list of {@link URL}s of files containing an 'accepted' variation from the
	 * reference image.
	 */
	protected List<URL> variationURLs;

	/**
	 * The file where the newly computed range variation should be saved.
	 */
	private String saveRangeVariation;

	/**
	 * The file where the newly computed platform variation should be saved.
	 */
	private String savePlatformVariation;

	/**
	 * The File where the candidate reference should be saved if there is not
	 * candidate reference or if it cannot be opened.
	 */
	private File candidateReference;

	/**
	 * Temporary directory
	 */
	protected static File tempDirectory;

	/**
	 * Returns the temporary directory
	 */
	public static File getTempDirectory() {
		if (tempDirectory == null) {
			String tmpDir = System.getProperty("java.io.tmpdir");
			if (tmpDir == null) {
				throw new RuntimeException();
			}

			synchronized (AbstractRenderingAccuracyTest.class) {
				// Double-check for null to prevent race
				if (tempDirectory == null) {
					tempDirectory = new File(tmpDir);
				}
			}

			if (!tempDirectory.exists()) {
				throw new RuntimeException();
			}
		}
		return tempDirectory;
	}

	/**
	 * Constructor.
	 * 
	 * @param svgURL    the URL String for the SVG document being tested.
	 * @param refImgURL the URL for the reference image.
	 * @throws MalformedURLException 
	 */
	public AbstractRenderingAccuracyTest(int pixelThreshold, String svgURL, String refImgURL) throws MalformedURLException {
		this.pixelThreshold = pixelThreshold;
		setInputAndRefURL(svgURL, refImgURL);
	}

	/**
	 * For subclasses
	 */
	protected AbstractRenderingAccuracyTest(int pixelThreshold) {
		this.pixelThreshold = pixelThreshold;
	}

	/**
	 * Sets the URLs for the input file and the reference image.
	 * 
	 * @throws MalformedURLException if any of the URLs are malformed.
	 */
	public void setInputAndRefURL(String svgURL, String refImgURL) throws MalformedURLException {
		if (svgURL == null) {
			throw new IllegalArgumentException();
		}

		if (refImgURL == null) {
			throw new IllegalArgumentException();
		}

		this.svgURL = resolveURL(svgURL);
		this.refImgURL = resolveURL(refImgURL);
	}

	/**
	 * Resolves the input string as a URL.
	 * 
	 * @throws MalformedURLException if the argument is not recognized as a URL.
	 */
	public URL resolveURL(String url) throws MalformedURLException {
		if (url.startsWith("file:")) {
			return new URL(url);
		}
		return new URL(PROJECT_ROOT_URL + url);
	}

	/**
	 * Get the name of the project directory.
	 * 
	 * @return the name of the project directory.
	 */
	protected abstract String getProjectName();

	/**
	 * Sets the File where the range variation from the reference image should be stored
	 */
	public void setSaveRangeVariation(String saveVariation) {
		this.saveRangeVariation = saveVariation;
	}

	private String getSaveRangeVariation() {
		return saveRangeVariation;
	}

	/**
	 * Sets the File where the platform variation from the reference image should be stored
	 */
	public void setSavePlatformVariation(String saveVariation) {
		this.savePlatformVariation = saveVariation;
	}

	private String getSavePlatformVariation() {
		return savePlatformVariation;
	}

	public String[] getVariationURLs() {
		if (variationURLs != null) {
			return variationURLs.toArray(new String[0]);
		}
		return null;
	}

	/**
	 * Adds a URL for an acceptable variation from the reference image.
	 */
	public void addVariationURL(String variationURL) {
		if (this.variationURLs == null) {
			this.variationURLs = new LinkedList<>();
		}
		try {
			this.variationURLs.add(resolveURL(variationURL));
		} catch (MalformedURLException e) {
			// no worries
		}
	}

	/**
	 * See {@link #candidateReference}
	 */
	public void setCandidateReference(File candidateReference) {
		this.candidateReference = candidateReference;
	}

	public File getCandidateReference() {
		return candidateReference;
	}

	/**
	 * Get the image suffix, for example {@code -dark}.
	 * 
	 * @return the image suffix, generally the empty string.
	 */
	protected String getImageSuffix() {
		return "";
	}

	/**
	 * Returns the URL of the SVG being rendered.
	 */
	public String getURI() {
		return svgURL.toString();
	}

	protected URL getURL() {
		return svgURL;
	}

	/**
	 * Requests this <code>Test</code> to run.
	 * 
	 * @param allowedPercentBelowThreshold the allowed percentage of different
	 *                                     pixels where the difference does not
	 *                                     exceed a fixed threshold of
	 *                                     {@code PIXEL_THRESHOLD}.
	 * @param allowedPercentOverThreshold  the allowed percentage of different
	 *                                     pixels where the difference exceeds a
	 *                                     fixed threshold of
	 *                                     {@code PIXEL_THRESHOLD}.
	 * @throws TranscoderException
	 * @throws IOException
	 *
	 */
	public void runTest(float allowedPercentBelowThreshold, float allowedPercentOverThreshold)
			throws TranscoderException, IOException {
		//
		// First, do clean-up
		//
		if (candidateReference != null) {
			if (candidateReference.exists()) {
				candidateReference.delete();
			}
		}

		//
		// Render the SVG image into a raster. We call an
		// abstract method to convert the src into a raster in
		// a temporary file.
		File tmpFile = null;

		if (candidateReference != null) {
			tmpFile = candidateReference;
			if (!tmpFile.exists()) {
				File parentDir = tmpFile.getParentFile();
				if (!parentDir.exists()) {
					if (!parentDir.mkdir()) {
						throw new IOException("Could not create directory: " + parentDir.getAbsolutePath());
					}
				}
			}
		} else {
			tmpFile = tmpUtil.createImageFile(svgURL, getImageSuffix(), IMAGE_FILE_DOT_EXTENSION);
		}

		try (FileOutputStream tmpFileOS = new FileOutputStream(tmpFile)) {

			// Call abstract method to encode svgURL to tmpFileOS as a
			// raster.
			encode(svgURL, tmpFileOS);

		}

		BufferedImage ref = getImage(refImgURL);
		BufferedImage gen = getImage(tmpFile);

		short result = compareImages(ref, gen, allowedPercentBelowThreshold, allowedPercentOverThreshold);

		if (result == ImageComparator.MATCH) {
			//
			// Everything worked out well.
			//
			tmpFile.delete();
			return;
		}

		//
		// If there are accepted variations, check if it covers the
		// computed difference.
		//
		Variants variants = null;
		if (variationURLs != null) {
			// Reload the images, may have been changed during alpha pre-mult
			ref = getImage(refImgURL);
			gen = getImage(tmpFile);
			variants = new Variants();
			short variantResult = ImageComparator.compareVariantImages(ref, gen, pixelThreshold,
					allowedPercentBelowThreshold, allowedPercentOverThreshold, variants);

			if (variantResult == ImageComparator.MATCH) {
				// Everything worked out well, at least with variation.
				tmpFile.delete();
				return;
			} else if (variantResult == ImageComparator.VARIANT_ERROR) {
				// If there is an error in any variant file, we want to know that
				result = variantResult;
			}
		}

		//
		// If the files still differ here, it means that even the
		// variation does not account for the difference return an
		// error
		//

		// Rendering is not accurate
		if (getSaveRangeVariation() != null) {
			// Reload the images, to operate on exact file contents
			ref = getImage(refImgURL);
			gen = getImage(tmpFile);

			saveRangeDiff(ref, gen, variants);
		}

		// Reload the images
		ref = getImage(refImgURL);
		gen = getImage(tmpFile);

		BufferedImage diff = ImageComparator.createDiffImage(ref, gen);
		if (getSavePlatformVariation() != null) {
			// There is a computed platform variation different from the
			// referenced variation and there is a place where the new
			// variation should be saved.
			saveImage(diff, new File(getSavePlatformVariation()));
		}

		// Save two images:
		// a. One with the reference image and the newly generated image
		// b. One with the difference between the two images and the set
		// of different pixels.
		BufferedImage cmp = ImageComparator.createCompareImage(ref, gen);
		File cmpFile = imageToFile(cmp, IMAGE_TYPE_COMPARISON);
		File diffFile = imageToFile(diff, IMAGE_TYPE_DIFF);

		if (candidateReference == null) {
			tmpFile.delete();
		}

		failTest("Rendering not accurate [" + ImageComparator.getResultDescription(result)
				+ "], see generated images: " + diffFile.getAbsolutePath() + ", "
				+ cmpFile.getAbsolutePath());
	}

	/**
	 * Fail the test with the given message.
	 * 
	 * @param message
	 */
	protected abstract void failTest(String message);

	private void saveRangeDiff(BufferedImage ref, BufferedImage gen, Variants variants) throws IOException {
		BufferedImage exactDiff;
		if (variants != null) {
			// Attempt to update the range variant, if available
			variants.setTrace(false);
			BufferedImage rangeDiff = variants.getVariantImage(0);
			if (rangeDiff != null) {
				exactDiff = ImageComparator.createMergedDiffImage(ref, gen, rangeDiff);
				saveImage(exactDiff, new File(getSaveRangeVariation()));
				return;
			}
		}

		exactDiff = ImageComparator.createExactDiffImage(ref, gen);
		// There is a computed range variation different from the
		// referenced variation and there is a place where the new
		// variation should be saved.
		saveImage(exactDiff, new File(getSaveRangeVariation()));
	}

	private class Variants implements ImageComparator.ImageVariants {

		private boolean trace = true;

		private Variants() {
			super();
		}

		void setTrace(boolean trace) {
			this.trace = trace;
		}

		@Override
		public int getVariantCount() {
			return variationURLs.size();
		}

		@Override
		public BufferedImage getVariantImage(int index) {
			URL variationURL;
			try {
				variationURL = variationURLs.get(index);
			} catch (IndexOutOfBoundsException e) {
				return null;
			}

			InputStream variationURLStream;
			try {
				variationURLStream = variationURL.openStream();
			} catch (IOException e) {
				// Could not open variationURL stream. Just trace that
				if (trace) {
					System.err.println(Messages.formatMessage(COULD_NOT_OPEN_VARIATION_URL,
							new Object[] { variationURL.toString() }));
				}
				return null;
			}

			ImageTagRegistry reg = ImageTagRegistry.getRegistry();
			Filter filt = reg.readStream(variationURLStream);
			if (filt == null) {
				if (trace) {
					System.err.println(Messages.formatMessage(COULD_NOT_OPEN_VARIATION_URL,
							new Object[] { variationURL.toString() }));
				}
				try {
					variationURLStream.close();
				} catch (IOException e) {
				}
				return null;
			}

			RenderedImage red = filt.createDefaultRendering();
			if (red == null) {
				if (trace) {
					System.err.println(Messages.formatMessage(COULD_NOT_OPEN_VARIATION_URL,
							new Object[] { variationURL.toString() }));
				}
				try {
					variationURLStream.close();
				} catch (IOException e) {
				}
				return null;
			}

			BufferedImage img = new BufferedImage(red.getWidth(), red.getHeight(), BufferedImage.TYPE_INT_ARGB);
			red.copyData(img.getRaster());

			try {
				variationURLStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return img;
		}

	}

	short compareImages(BufferedImage imageA, BufferedImage imageB, float allowedPercentBelowThreshold,
			float allowedPercentOverThreshold) {
		return ImageComparator.compareImages(imageA, imageB, pixelThreshold, allowedPercentBelowThreshold,
				allowedPercentOverThreshold);
	}

	protected abstract void encode(URL srcURL, FileOutputStream fos) throws TranscoderException, IOException;

	/**
	 * Saves an image in a given File
	 */
	private void saveImage(BufferedImage img, File imgFile) throws IOException {
		if (!imgFile.exists()) {
			File parentDir = imgFile.getParentFile();
			if (!parentDir.exists()) {
				if (!parentDir.mkdir()) {
					return;
				}
			}
			imgFile.createNewFile();
		}

		try (OutputStream out = new FileOutputStream(imgFile)) {
			saveImage(img, out);
		}
	}

	/**
	 * Saves an image in a given File
	 */
	void saveImage(BufferedImage img, OutputStream os) throws IOException {
		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/png");
		writer.writeImage(img, os);
	}

	/**
	 * Loads an image from a File
	 */
	private BufferedImage getImage(File file) throws IOException {
		return getImage(file.toURI().toURL());
	}

	/**
	 * Loads an image from a URL
	 */
	private BufferedImage getImage(URL url) throws IOException {
		ImageTagRegistry reg = ImageTagRegistry.getRegistry();
		Filter filt = reg.readURL(new ParsedURL(url));
		if (filt == null)
			throw new IOException(Messages.formatMessage(COULD_NOT_LOAD_IMAGE, new Object[] { url.toString() }));

		RenderedImage red = filt.createDefaultRendering();
		if (red == null)
			throw new IOException(Messages.formatMessage(COULD_NOT_LOAD_IMAGE, new Object[] { url.toString() }));

		BufferedImage img = new BufferedImage(red.getWidth(), red.getHeight(), BufferedImage.TYPE_INT_ARGB);
		red.copyData(img.getRaster());

		return img;
	}

	/**
	 * Creates a File into which the input image is saved. If there is a "file"
	 * component in the SVG url, then a temporary file is created with that name and
	 * the imageType suffix in the temp directory of the test-reports directory.
	 */
	private File imageToFile(BufferedImage img, String imageType) throws IOException {
		File imageFile = tmpUtil.createImageFile(svgURL, getImageSuffix() + imageType, IMAGE_FILE_DOT_EXTENSION);

		saveImage(img, imageFile);

		return imageFile;
	}

}
