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

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.test.TestLocations;
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
	 * Suffix used for saved images (e.g., comparison and diff images)
	 */
	private static final String IMAGE_FILE_EXTENSION = ".png";

	/**
	 * The configuration resource bundle
	 */
	static ResourceBundle configuration;

	static {
		configuration = ResourceBundle.getBundle(CONFIGURATION_RESOURCES, Locale.getDefault());
	}

	/**
	 * Prefix for the temporary files created by Tests of this class
	 */
	public static final String TEMP_FILE_PREFIX = configuration.getString("temp.file.prefix");

	/**
	 * Suffix for the temporary files created by Tests of this class
	 */
	public static final String TEMP_FILE_SUFFIX = configuration.getString("temp.file.suffix");

	/**
	 * The URL where the SVG can be found.
	 */
	private URL svgURL;

	/**
	 * The URL for the reference image
	 */
	protected URL refImgURL;

	/**
	 * A list of {@link URL}s of files containing an 'accepted' variation from the
	 * reference image.
	 */
	protected List<URL> variationURLs;

	/**
	 * The File where the newly computed variation should be saved if different from
	 * the variationURL
	 */
	protected File saveVariation;

	/**
	 * The File where the candidate reference should be saved if there is not
	 * candidate reference or if it cannot be opened.
	 */
	protected File candidateReference;

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

			tempDirectory = new File(tmpDir);
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
	public AbstractRenderingAccuracyTest(String svgURL, String refImgURL) throws MalformedURLException {
		setConfig(svgURL, refImgURL);
	}

	/**
	 * For subclasses
	 */
	protected AbstractRenderingAccuracyTest() {
	}

	/**
	 * Sets this test's config.
	 * @throws MalformedURLException 
	 */
	public void setConfig(String svgURL, String refImgURL) throws MalformedURLException {
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
	 * Resolves the input string as  a URL. If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 * @throws MalformedURLException 
	 */
	public static URL resolveURL(String url) throws MalformedURLException {
		if (url.startsWith("file:")) {
			return new URL(url);
		}
		return new URL(TestLocations.PROJECT_ROOT_URL + url);
	}

	/**
	 * Sets the File where the variation from the reference image should be stored
	 */
	public void setSaveVariation(File saveVariation) {
		this.saveVariation = saveVariation;
	}

	public File getSaveVariation() {
		return saveVariation;
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
	 * @throws TranscoderException 
	 * @throws IOException 
	 *
	 */
	public void runTest() throws TranscoderException, IOException {
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
						return;
					}
				}
			}
		} else {
			tmpFile = TestLocations.getTempFilename(svgURL, TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, "", ".png");
		}

		FileOutputStream tmpFileOS = new FileOutputStream(tmpFile);

		// Call abstract method to encode svgURL to tmpFileOS as a
		// raster. If this returns a non-null test report then the
		// encoding failed and we should return that report.
		encode(svgURL, tmpFileOS);

		BufferedImage ref = getImage(refImgURL);
		BufferedImage gen = getImage(tmpFile);

		ImageComparisonResult result = compareImages(ref, gen);

		boolean accurate = result.getImageComparisonState() == ImageComparisonState.MATCH;

		if (accurate) {
			//
			// Yahooooooo! everything worked out well.
			//
			tmpFile.delete();
			return;
		}

		BufferedImage diff = buildDiffImage(ref, gen);

		//
		// If there is an accepted variation, check if it equals the
		// computed difference.
		//
		if (variationURLs != null) {
			for (URL variationURL : variationURLs) {
				File tmpDiff = imageToFile(diff, IMAGE_TYPE_DIFF);

				InputStream variationURLStream = null;
				try {
					variationURLStream = variationURL.openStream();
				} catch (IOException e) {
					// Could not open variationURL stream. Just trace that
					System.err.println(Messages.formatMessage(COULD_NOT_OPEN_VARIATION_URL,
							new Object[] { variationURL.toString() }));
				}

				if (variationURLStream != null) {
					InputStream refDiffStream = new BufferedInputStream(variationURLStream);

					InputStream tmpDiffStream = new BufferedInputStream(new FileInputStream(tmpDiff));

					if (compare(refDiffStream, tmpDiffStream)) {
						// We accept the generated result.
						accurate = true;
					}
				}
			}
		}

		if (accurate) {
			//
			// Yahooooooo! everything worked out well, at least
			// with variation.
			tmpFile.delete();
			return;
		}

		//
		// If the files still differ here, it means that even the
		// variation does not account for the difference return an
		// error
		//

		// Rendering is not accurate
		if (saveVariation != null) {
			// There is a computed variation different from the
			// referenced variation and there is a place where the new
			// variation should be saved.
			saveImage(diff, saveVariation);
		}

		// Build two images:
		// a. One with the reference image and the newly generated image
		// b. One with the difference between the two images and the set
		// of different pixels.
		BufferedImage cmp = makeCompareImage(ref, gen);
		File cmpFile = imageToFile(cmp, IMAGE_TYPE_COMPARISON);
		File diffFile = imageToFile(diff, IMAGE_TYPE_DIFF);

		if (candidateReference == null) {
			tmpFile.delete();
		}

		fail("Rendering not accurate, see generated images: " + diffFile.getAbsolutePath() + ", "
				+ cmpFile.getAbsolutePath());
	}

	static ImageComparisonResult compareImages(BufferedImage imageA, BufferedImage imageB) {
		return compareImages(imageA, imageB, 0d);
	}

	static ImageComparisonResult compareImages(BufferedImage imageA, BufferedImage imageB,
			double allowingPercentOfDifferentPixels) {
		ImageComparison comparison = new ImageComparison(imageA, imageB);
		comparison.setAllowingPercentOfDifferentPixels(allowingPercentOfDifferentPixels);
		ImageComparisonResult result = comparison.compareImages();
		return result;
	}

	protected abstract void encode(URL srcURL, FileOutputStream fos) throws TranscoderException, IOException;

	/**
	 * Compare the two input streams
	 */
	private boolean compare(InputStream refStream, InputStream newStream) throws IOException {
		int b, nb;
		do {
			b = refStream.read();
			nb = newStream.read();
		} while (b != -1 && nb != -1 && b == nb);
		refStream.close();
		newStream.close();
		return (b == nb);
	}

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
		OutputStream out = new FileOutputStream(imgFile);
		try {
			saveImage(img, out);
		} finally {
			out.close();
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
	 * Builds a new BufferedImage that is the difference between the two input
	 * images
	 */
	public static BufferedImage buildDiffImage(BufferedImage ref, BufferedImage gen) {
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
				val = ((genPix[i] - refPix[i]) * 10) + 128;
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
	 *
	 */
	private BufferedImage makeCompareImage(BufferedImage ref, BufferedImage gen) {
		BufferedImage cmp = new BufferedImage(ref.getWidth() * 2, ref.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = cmp.createGraphics();
		g.setPaint(Color.white);
		g.fillRect(0, 0, cmp.getWidth(), cmp.getHeight());
		g.drawImage(ref, 0, 0, null);
		g.translate(ref.getWidth(), 0);
		g.drawImage(gen, 0, 0, null);
		g.dispose();

		return cmp;
	}

	/**
	 * Creates a File into which the input image is saved. If there is a "file"
	 * component in the SVG url, then a temporary file is created with that name and
	 * the imageType suffix in the temp directory of the test-reports directory.
	 */
	private File imageToFile(BufferedImage img, String imageType) throws IOException {
		File imageFile = TestLocations.getTempFilename(svgURL, TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, imageType,
				IMAGE_FILE_EXTENSION);

		saveImage(img, imageFile);

		return imageFile;
	}

}
