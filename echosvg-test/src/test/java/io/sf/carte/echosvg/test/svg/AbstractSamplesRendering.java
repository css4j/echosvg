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

import java.io.IOException;

import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * Abstract base class to render SVG files, generally under the {@code samples}
 * directory, and compare the result with a reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * If you want to test a non-conformant SVG document, run a non-validating test
 * with {@link #testNV(String)}.
 * </p>
 */
public class AbstractSamplesRendering {

	void test(String file) throws TranscoderException, IOException {
		test(file, true);
	}

	/**
	 * A non-validating test.
	 * 
	 * @param file the SVG file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testNV(String file) throws TranscoderException, IOException {
		test(file, false);
	}

	float getBelowThresholdAllowed() {
		return 0.2f;
	}

	float getOverThresholdAllowed() {
		return 0.05f;
	}

	/**
	 * Test the rendering of a file with the given user language.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the SVG file to test.
	 * @param lang the language.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void test(String file, String lang) throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setUserLanguage(lang);
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
	 * @param file       the SVG file to test.
	 * @param validating if true, the SVG is validated.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void test(String file, boolean validating) throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setValidating(validating);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the non-validating rendering of an SVG image, ignoring reported errors.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file               the SVG file to test.
	 * @param media              the media to test, or {@code null} if default
	 *                           media.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testNVErrIgnore(String file, String media, int expectedErrorCount)
			throws TranscoderException, IOException {
		testErrIgnore(file, media, expectedErrorCount, false);
	}

	/**
	 * Test the rendering of an SVG image, ignoring reported errors.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file               the SVG file to test.
	 * @param media              the media to test, or {@code null} if default
	 *                           media.
	 * @param expectedErrorCount the expected error count.
	 * @param validate           whether to validate or not.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testErrIgnore(String file, String media, int expectedErrorCount, boolean validate)
			throws TranscoderException, IOException {
		RenderingTest runner = new ErrIgnoreTest(expectedErrorCount);
		runner.setValidating(validate);
		runner.setMedia(media);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	void testAlternateSheet(String file, String alt, boolean validating)
			throws TranscoderException, IOException {
		AltUserSheetRenderingTest runner = new AltUserSheetRenderingTest();
		runner.setValidating(validating);
		runner.setAlternateSheet(alt);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	void testUserSheet(String file, boolean validating) throws TranscoderException, IOException {
		AltUserSheetRenderingTest runner = new AltUserSheetRenderingTest();
		runner.setValidating(validating);
		runner.setUserSheetClasspath(AltUserSheetRenderingTest.DEFAULT_USER_SHEET);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the rendering of a SVG image inside an XHTML document.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the XHTML file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testXHTML(String file) throws TranscoderException, IOException {
		RenderingTest runner = new XHTMLRenderingAccuracyTest();
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the rendering of a SVG image inside an XHTML document, ignoring errors.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file               the XHTML file to test.
	 * @param media              the media to test, or {@code null} if default
	 *                           media.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testXHTMLErrIgnore(String file, String media, int expectedErrorCount)
			throws TranscoderException, IOException {
		RenderingTest runner = new XHTMLErrIgnoreTest(expectedErrorCount);
		runner.setMedia(media);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the rendering of a SVG image inside an HTML document.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the HTML file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testHTML(String file) throws TranscoderException, IOException {
		testHTML(file, null);
	}

	/**
	 * Test the rendering of a SVG image inside an HTML document.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the HTML file to test.
	 * @param the  selector that locates the desired SVG element.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testHTML(String file, String selector) throws TranscoderException, IOException {
		RenderingTest runner = new HTMLRenderingAccuracyTest(selector);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	void testResolutionPxMM(String file, float pxToMM) throws TranscoderException, IOException {
		RenderingTest runner = new ResolutionPxMmRenderingTest(pxToMM);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	void testAnim(String file, float[] times) throws TranscoderException, IOException {
		testAnim(file, times, true);
	}

	void testAnim(String file, float[] times, boolean validate) throws TranscoderException, IOException {
		for (float time : times) {
			RenderingTest runner = new SVGAnimationRenderingAccuracyTest(time);
			runner.setValidating(validate);
			runner.setFile(file);
			runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
		}
	}

	/**
	 * Dynamic test of the rendering of a SVG file.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the SVG file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testDynamicUpdate(String file) throws TranscoderException, IOException {
		JSVGRenderingAccuracyTest runner = new JSVGRenderingAccuracyTest();
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

}
