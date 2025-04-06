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

	static final String BROWSER_MEDIA = "screen";

	static final String PRINT_MEDIA = "print";

	/**
	 * To test the tEXt chunk.
	 */
	private static final String[] tEXt = { "Software", "EchoSVG", "LoremIpsum 1",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, ", "LoremIpsum 2",
			"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "LoremIpsum 3",
			" Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi", "LoremIpsum 4",
			" ut aliquip ex ea commodo consequat." };

	/**
	 * To test the zTXt chunk.
	 */
	private static final String[] zTXt = { "Groucho contract scene 1",
			"The first part of the party of the first part shall be known in this contract",
			"Groucho contract scene 2", " as the first part of the party of the first part.",
			"Groucho contract scene 3",
			"The party of the second part shall be know in this contract as the party of the second part." };

	/**
	 * To test the iTXt chunk.
	 */
	private static final String[] iTXt = { "Foo", "la-VAT", "LoremIpsum",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
					+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." };

	void test(String file) throws TranscoderException, IOException {
		test(file, true);
	}

	/**
	 * A non-validating test.
	 * 
	 * @param file the SVG file to test.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testNV(String file) throws TranscoderException, IOException {
		test(file, false);
	}

	float getBelowThresholdAllowed() {
		return 0.1f;
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
	 * @throws IOException         if an I/O error occurs.
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
	 * @param file                 the SVG file to test.
	 * @param validating           if true, the SVG is validated.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void test(String file, boolean validating)
			throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setValidating(validating);
		runner.setMedia(SVGRenderingAccuracyTest.DEFAULT_MEDIUM);
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
	 * @throws IOException         if an I/O error occurs.
	 */
	void testNVErrIgnore(String file, String media, int expectedErrorCount)
			throws TranscoderException, IOException {
		testErrIgnore(file, media, false, expectedErrorCount);
	}

	/**
	 * Test the rendering of an SVG image, ignoring reported errors.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file                 the SVG file to test.
	 * @param media                the media to test, or {@code null} if default
	 *                             media.
	 * @param validate             whether to validate or not.
	 * @param expectedErrorCount   the expected error count.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testErrIgnore(String file, String media, boolean validate, int expectedErrorCount)
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

	void testDarkMode(String file)
			throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setValidating(Boolean.FALSE);
		runner.setDarkMode(true);
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
	 * Test the rendering of a SVG file with varying compression levels.
	 * 
	 * @param file       the SVG file to test.
	 * @param validating if true, the SVG is validated.
	 * @param comprLevel the PNG compression level.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testCompress(String file, boolean validating, int comprLevel) throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setValidating(validating);
		runner.setCompressionLevel(comprLevel);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the rendering of a SVG file with embedded text.
	 * 
	 * @param file       the SVG file to test.
	 * @param validating if true, the SVG is validated.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testText(String file, boolean validating) throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setValidating(validating);
		runner.setText(tEXt);
		runner.setInternationalText(iTXt);
		runner.setCompressedText(zTXt);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed(), true);
	}

	/**
	 * Test the rendering of a SVG file with the ImageIO encoder and the default
	 * compression level.
	 * 
	 * @param file       the SVG file to test.
	 * @param validating if true, the SVG is validated.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testImageIO(String file, boolean validating) throws TranscoderException, IOException {
		testImageIO(file, validating, 4);
	}

	/**
	 * Test the rendering of a SVG file with the ImageIO encoder.
	 * 
	 * @param file       the SVG file to test.
	 * @param validating if true, the SVG is validated.
	 * @param comprLevel the PNG compression level.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testImageIO(String file, boolean validating, int comprLevel) throws TranscoderException, IOException {
		RenderingTest runner = new ImageIORenderingCheck();
		runner.setCompressionLevel(comprLevel);
		runner.setValidating(validating);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

	/**
	 * Test the rendering of a SVG file with the ImageIO encoder and with embedded text.
	 * 
	 * @param file       the SVG file to test.
	 * @param validating if true, the SVG is validated.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testImageIOText(String file, boolean validating) throws TranscoderException, IOException {
		RenderingTest runner = new ImageIORenderingCheck();
		runner.setValidating(validating);
		runner.setText(tEXt);
		runner.setInternationalText(iTXt);
		runner.setCompressedText(zTXt);
		runner.setFile(file);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed(), true);
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
	 * @throws IOException         if an I/O error occurs.
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
	 * @throws IOException         if an I/O error occurs.
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
	 * @throws IOException         if an I/O error occurs.
	 */
	void testHTML(String file) throws TranscoderException, IOException {
		testHTML(file, null, null);
	}

	/**
	 * Test the rendering of a SVG image inside an HTML document.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file  the HTML file to test.
	 * @param the   selector that locates the desired SVG element.
	 * @param media the media to test, or {@code null} if default media.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	void testHTML(String file, String selector, String media) throws TranscoderException, IOException {
		RenderingTest runner = new HTMLRenderingAccuracyTest(selector);
		runner.setMedia(media);
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
		for (int i = 0; i < times.length; i++) {
			RenderingTest runner = new SVGAnimationRenderingAccuracyTest(times[i], 0);
			runner.setValidating(validate);
			runner.setFile(file);
			runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
		}
	}

	void testAnim(String file, float[] times, int[] expectedErrorCount)
			throws TranscoderException, IOException {
		for (int i = 0; i < times.length; i++) {
			RenderingTest runner = new SVGAnimationRenderingAccuracyTest(times[i], expectedErrorCount[i]);
			runner.setValidating(Boolean.FALSE);
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
	 * @throws IOException         if an I/O error occurs.
	 */
	void testDynamicUpdate(String file) throws TranscoderException, IOException {
		testDynamicUpdate(file, true);
	}

	void testDynamicUpdate(String file, boolean validating) throws TranscoderException, IOException {
		JSVGRenderingAccuracyTest runner = new JSVGRenderingAccuracyTest();
		runner.setFile(file);
		runner.setValidating(validating);
		runner.runTest(getBelowThresholdAllowed(), getOverThresholdAllowed());
	}

}
