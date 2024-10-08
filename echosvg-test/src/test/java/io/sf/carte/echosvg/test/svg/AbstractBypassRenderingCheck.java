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

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.w3c.dom.Document;

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.test.DummyErrorHandler;
import io.sf.carte.echosvg.transcoder.util.CSSTranscodingHelper;

/**
 * Base class for tests that check the <code>CSSTranscodingHelper</code>.
 * <p>
 * It bypasses EchoSVG's style computations and uses CSS4J instead.
 * </p>
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * If you want to test a non-conformant SVG document, run a non-validating test
 * with {@link #testNV(String)}.
 * </p>
 */
public class AbstractBypassRenderingCheck {

	static final String BROWSER_MEDIA = "screen";

	static final String PRINT_MEDIA = "print";

	void test(String file) throws TranscoderException, IOException {
		test(file, 0);
	}

	void test(String file, int expectedErrorCount) throws TranscoderException, IOException {
		test(file, expectedErrorCount, true);
	}

	void test(String file, int expectedErrorCount, boolean validating)
			throws TranscoderException, IOException {
		test(file, SVGRenderingAccuracyTest.DEFAULT_MEDIUM, false, null, null, validating,
				expectedErrorCount);
	}

	/**
	 * A {@code print} medium test.
	 * 
	 * @param file the SVG file to test.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testPrint(String file, int expectedErrorCount)
			throws TranscoderException, IOException {
		test(file, PRINT_MEDIA, false, null, null, true, expectedErrorCount);
	}

	/**
	 * A dark mode test.
	 * 
	 * @param file               the SVG file to test.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testDark(String file, int expectedErrorCount)
			throws TranscoderException, IOException {
		test(file, SVGRenderingAccuracyTest.DEFAULT_MEDIUM, true, null, null, true, expectedErrorCount);
	}

	/**
	 * A non-validating test that expects no errors.
	 * 
	 * @param file the SVG file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testNV(String file)
			throws TranscoderException, IOException {
		testNV(file, 0);
	}

	/**
	 * A non-validating test.
	 * 
	 * @param file               the SVG file to test.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testNV(String file, int expectedErrorCount)
			throws TranscoderException, IOException {
		test(file, SVGRenderingAccuracyTest.DEFAULT_MEDIUM, false, null, null, false, expectedErrorCount);
	}

	/**
	 * A non-validating test.
	 * 
	 * @param file               the SVG file to test.
	 * @param expectedErrorCount the expected error count.
	 * @param backgroundColor    the background color, or {@code null} if
	 *                           transparent.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testNV(String file, int expectedErrorCount, Color backgroundColor) throws TranscoderException,
			IOException {
		test(file, SVGRenderingAccuracyTest.DEFAULT_MEDIUM, false, backgroundColor, null, false,
				expectedErrorCount);
	}

	float getBelowThresholdAllowed() {
		return 0.5f;
	}

	float getOverThresholdAllowed() {
		return 0.2f;
	}

	/**
	 * Test the rendering of a file with the given user language.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file               the SVG file to test.
	 * @param medium             the target medium ({@code screen}, {@code print},
	 *                           etc).
	 * @param lang               the language.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void test(String file, String medium, String lang, int expectedErrorCount)
			throws TranscoderException, IOException {
		RenderingTest runner = new BypassRenderingTest(medium, expectedErrorCount);
		runner.setUserLanguage(lang);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test rendering with a user style sheet.
	 * 
	 * @param file               the SVG file to test.
	 * @param alt                the alternate style sheet name.
	 * @param validating         validate if true.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testAlternate(String file, String alt, boolean validating, int expectedErrorCount)
			throws TranscoderException, IOException {
		BypassRenderingTest runner = new BypassRenderingTest(
				SVGRenderingAccuracyTest.DEFAULT_MEDIUM, expectedErrorCount);
		runner.setValidating(validating);
		runner.setAlternateSheet(alt);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test rendering with a user style sheet.
	 * 
	 * @param file               the SVG file to test.
	 * @param validating         validate if true.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testUserSheet(String file, boolean validating, int expectedErrorCount)
			throws TranscoderException, IOException {
		BypassRenderingTest runner = new BypassRenderingTest(
				SVGRenderingAccuracyTest.DEFAULT_MEDIUM, expectedErrorCount);
		runner.setValidating(validating);
		runner.setUserSheetClasspath(AltUserSheetRenderingTest.DEFAULT_USER_SHEET);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the rendering of a SVG file.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file                 the SVG file to test.
	 * @param medium               the target medium ({@code screen}, {@code print},
	 *                             etc).
	 * @param darkMode             if true, dark mode is enabled in CSS.
	 * @param backgroundColor      the background color.
	 * @param selector             the selector to find the SVG element.
	 * @param validating           if true, the SVG is validated.
	 * @param expectedErrorCount   the expected number of errors.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void test(String file, String medium, boolean darkMode, Color backgroundColor, String selector,
			boolean validating, int expectedErrorCount) throws TranscoderException, IOException {
		BypassRenderingTest runner = new BypassRenderingTest(medium, expectedErrorCount);
		configureAndRun(runner, file, darkMode, backgroundColor, selector, validating);
	}

	void configureAndRun(BypassRenderingTest runner, String file, boolean darkMode, Color backgroundColor,
			String selector, boolean validating) throws TranscoderException, IOException {
		runner.setDarkMode(darkMode);
		runner.setBackgroundColor(backgroundColor);
		runner.setSelector(selector);
		runner.setValidating(validating);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the rendering of a SVG file, with all the supported input source
	 * variants.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file               the SVG file to test.
	 * @param medium             the target medium ({@code screen}, {@code print},
	 *                           etc).
	 * @param darkMode           if true, dark mode is enabled in CSS.
	 * @param backgroundColor    the background color, or {@code null} if
	 *                           transparent.
	 * @param selector           the selector to find the SVG element.
	 * @param validating         if true, the SVG is validated.
	 * @param expectedErrorCount the expected number of errors.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testAllInputSources(String file, String medium, boolean darkMode, Color backgroundColor,
			String selector, boolean validating, int expectedErrorCount)
					throws TranscoderException, IOException {
		BypassRenderingTest runner = new BypassRenderingTest(medium, expectedErrorCount);
		configureAndRun(runner, file, darkMode, backgroundColor, selector, validating);

		Document doc = runner.getRenderDocument();
		runner = new DocumentInputHelperRenderingTest(medium, expectedErrorCount);
		runner.setRenderDocument(doc);
		configureAndRun(runner, file, darkMode, backgroundColor, selector, validating);

		runner = new TIDocumentInputHelperRenderingTest(medium, expectedErrorCount);
		runner.setRenderDocument(doc);
		configureAndRun(runner, file, darkMode, backgroundColor, selector, validating);

		runner = new TIInputStreamHelperRenderingTest(medium, expectedErrorCount);
		configureAndRun(runner, file, darkMode, backgroundColor, selector, validating);

		runner = new TIReaderInputHelperRenderingTest(medium, expectedErrorCount);
		configureAndRun(runner, file, darkMode, backgroundColor, selector, validating);

		runner = new TIURIInputHelperRenderingTest(medium, expectedErrorCount);
		configureAndRun(runner, file, darkMode, backgroundColor, selector, validating);
	}

	private class BypassRenderingTest extends RenderingTest {

		private final int expectedErrorCount;

		/**
		 * dark mode toggle.
		 */
		private boolean darkMode = false;

		private Color backgroundColor;

		/**
		 * Selector to locate SVG element
		 */
		String selector = null;

		/**
		 * Alternate sheet name.
		 */
		private String altSheet = null;

		/**
		 * Classpath to user sheet.
		 */
		private String userSheetClasspath = null;

		private transient Document renderDocument;

		BypassRenderingTest(String medium, int expectedErrorCount) {
			super();
			this.expectedErrorCount = expectedErrorCount;
			setMedia(medium);
		}

		public void setBackgroundColor(Color backgroundColor) {
			this.backgroundColor = backgroundColor;
		}

		/**
		 * Enables or disables dark mode.
		 * 
		 * @param darkMode if {@code true}, dark mode will be enabled.
		 */
		public void setDarkMode(boolean darkMode) {
			this.darkMode = darkMode;
		}

		public void setSelector(String selector) {
			this.selector = selector;
		}

		public Document getRenderDocument() {
			return renderDocument;
		}

		public void setRenderDocument(Document renderDocument) {
			this.renderDocument = renderDocument;
		}

		/**
		 * Set the name of the alternate style sheet.
		 * 
		 * @param altSheet the name of the alternate style sheet.
		 */
		public void setAlternateSheet(String altSheet) {
			this.altSheet = altSheet;
		}

		/**
		 * Set the classpath for the user style sheet.
		 * 
		 * @param userSheetClasspath the location of the user style sheet in classpath.
		 */
		public void setUserSheetClasspath(String userSheetClasspath) {
			this.userSheetClasspath = userSheetClasspath;
		}

		@Override
		protected void encode(URL srcURL, FileOutputStream fos)
				throws TranscoderException, IOException {
			ImageTranscoder transcoder = getTestImageTranscoder();
			DummyErrorHandler errorHandler = new DummyErrorHandler();
			transcoder.setErrorHandler(errorHandler);

			CSSTranscodingHelper helper = new CSSTranscodingHelper(transcoder);

			helper.setDarkMode(darkMode);

			if (darkMode) {
				// Opaque background for dark mode
				transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR,
						new Color(0, 0, 0, 255));
			} else if (backgroundColor != null) {
				transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR,
						backgroundColor);
			}

			TranscoderOutput dst = new TranscoderOutput(fos);

			encode(helper, dst);

			errorHandler.assertErrorCount(expectedErrorCount);

			checkErrorHandler(errorHandler);

			fos.getChannel().force(false);
		}

		void encode(CSSTranscodingHelper helper, TranscoderOutput dst)
				throws TranscoderException, IOException {
			String uri = getURI();
			URL url = new URL(uri);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5000);
			con.connect();
			InputStream is = con.getInputStream();
			try (Reader re = new InputStreamReader(is, StandardCharsets.UTF_8)) {
				helper.transcode(re, uri, dst, selector);
			}

			renderDocument = dst.getDocument();
		}

		/**
		 * Returns the <code>ImageTranscoder</code> the Test should use
		 */
		@Override
		ImageTranscoder getTestImageTranscoder() {
			ImageTranscoder t = super.getTestImageTranscoder();

			if (userSheetClasspath != null) {
				URL userSheet = AltUserSheetRenderingTest.class.getResource(userSheetClasspath);
				t.addTranscodingHint(SVGAbstractTranscoder.KEY_USER_STYLESHEET_URI,
						userSheet.toExternalForm());
			}

			if (altSheet != null) {
				t.addTranscodingHint(SVGAbstractTranscoder.KEY_ALTERNATE_STYLESHEET, altSheet);
			}

			return t;
		}

		@Override
		ImageTranscoder createTestImageTranscoder() {
			return new ErrIgnoreTranscoder();
		}

		@Override
		protected CharSequence getImageSuffix() {
			CharSequence suf = super.getImageSuffix();

			StringBuilder buf = null;
			if (altSheet != null) {
				buf = new StringBuilder(suf.length() + altSheet.length() + 6);
				buf.append(suf).append('_').append(altSheet);
			}

			if (darkMode) {
				if (buf == null) {
					buf = new StringBuilder();
				}
				buf.append("-dark");
			}

			if (buf == null) {
				return suf;
			}

			return buf.toString();
		}

	}

	private class DocumentInputHelperRenderingTest extends BypassRenderingTest {

		DocumentInputHelperRenderingTest(String medium, int expectedErrorCount) {
			super(medium, expectedErrorCount);
		}

		@Override
		void encode(CSSTranscodingHelper helper, TranscoderOutput dst)
				throws TranscoderException, IOException {
			helper.transcodeDocument(getRenderDocument(), dst, selector);
		}

	}

	private class TIReaderInputHelperRenderingTest extends BypassRenderingTest {

		TIReaderInputHelperRenderingTest(String medium, int expectedErrorCount) {
			super(medium, expectedErrorCount);
		}

		@Override
		void encode(CSSTranscodingHelper helper, TranscoderOutput dst)
				throws TranscoderException, IOException {
			String uri = getURI();
			URL url = new URL(uri);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5000);
			con.connect();
			try (InputStream is = con.getInputStream();
					Reader re = new InputStreamReader(is, StandardCharsets.UTF_8)) {
				TranscoderInput input = new TranscoderInput(re);
				input.setURI(uri);
				helper.transcode(input, dst);
			}
		}

	}

	private class TIInputStreamHelperRenderingTest extends BypassRenderingTest {

		TIInputStreamHelperRenderingTest(String medium, int expectedErrorCount) {
			super(medium, expectedErrorCount);
		}

		@Override
		void encode(CSSTranscodingHelper helper, TranscoderOutput dst)
				throws TranscoderException, IOException {
			String uri = getURI();
			URL url = new URL(uri);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5000);
			con.connect();
			try (InputStream is = con.getInputStream()) {
				TranscoderInput input = new TranscoderInput(is);
				input.setEncoding("utf-8");
				input.setURI(uri);
				helper.transcode(input, dst);
			}
		}

	}

	private class TIURIInputHelperRenderingTest extends BypassRenderingTest {

		TIURIInputHelperRenderingTest(String medium, int expectedErrorCount) {
			super(medium, expectedErrorCount);
		}

		@Override
		void encode(CSSTranscodingHelper helper, TranscoderOutput dst)
				throws TranscoderException, IOException {
			String uri = getURI();
			TranscoderInput input = new TranscoderInput(uri);
			helper.transcode(input, dst);
		}

	}

	private class TIDocumentInputHelperRenderingTest extends BypassRenderingTest {

		TIDocumentInputHelperRenderingTest(String medium, int expectedErrorCount) {
			super(medium, expectedErrorCount);
		}

		@Override
		void encode(CSSTranscodingHelper helper, TranscoderOutput dst)
				throws TranscoderException, IOException {
			TranscoderInput input = new TranscoderInput(getRenderDocument());
			helper.transcode(input, dst);
		}

	}

}
