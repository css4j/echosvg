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
package io.sf.carte.echosvg.svggen.test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.image.ImageCompareUtil;
import io.sf.carte.echosvg.test.image.TempImageFiles;
import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.test.misc.TestLocations;
import io.sf.carte.echosvg.test.svg.SVGRenderingAccuracyTest;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test validates that a given rendering sequence, modeled by a
 * <code>Painter</code> by doing several subtests for a single input class: +
 * SVGAccuracyTest with several configurations + SVGRenderingAccuracyTest +
 * ImageComparisonTest between the rendering of the generated SVG for various
 * configurations.
 *
 * <p>
 * Original author: <a href="mailto:vhardy@apache.org">Vincent Hardy</a>. For
 * later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGGeneratorTest {

	static final String GENERATOR_REFERENCE_BASE;

	private static final String RENDERING_DIR = "rendering";

	private static final String ACCEPTED_VARIATION_DIR = "accepted-variation";

	private static final String[] VARIATION_PLATFORMS = io.sf.carte.echosvg.test.svg.PreconfiguredRenderingTest.DEFAULT_VARIATION_PLATFORMS;

	private static final String CANDIDATE_VARIATION_DIR = "candidate-variation";

	private static final String PLATFORM_VARIATION_SUFFIX = io.sf.carte.echosvg.test.svg.PreconfiguredRenderingTest.PLATFORM_VARIATION_SUFFIX;

	static final String CANDIDATE_REF_DIR = "candidate-ref";

	private static final String RENDERING_CANDIDATE_REF_DIR = "candidate-reference";

	private static final String PNG_EXTENSION = ".png";

	static final String SVG_EXTENSION = ".svg";

	static final String PLAIN_GENERATION_PREFIX = "";

	private static final String CUSTOM_CONTEXT_GENERATION_PREFIX = "Context";

	/**
	 * Threshold to apply when comparing different pixels with
	 * {@link io.sf.carte.echosvg.test.image.ImageComparator ImageComparator}.
	 */
	private static final int PIXEL_THRESHOLD = 8;

	static {
		GENERATOR_REFERENCE_BASE = TestLocations.PROJECT_ROOT_URL + "test-references/io/sf/carte/echosvg/svggen/";
	}

	@BeforeAll
	public static void beforeClass() throws FontFormatException, IOException {
		TestFonts.loadTestFonts();
	}

	@Test
	public void testATransform() throws TranscoderException, IOException {
		runTests("ATransform");
	}

	@Test
	public void testAttributedCharacterIterator() throws TranscoderException, IOException {
		runTests("AttributedCharacterIterator");
	}

	@Test
	public void testBasicShapes() throws TranscoderException, IOException {
		runTests("BasicShapes");
	}

	@Test
	public void testBasicShapes2() throws TranscoderException, IOException {
		runTests("BasicShapes2");
	}

	@Test
	public void testBStroke() throws TranscoderException, IOException {
		runTests("BStroke");
	}

	@Test
	public void testBug4389() throws TranscoderException, IOException {
		runTests("Bug4389");
	}

	@Test
	public void testBug4945() throws TranscoderException, IOException {
		runTests("Bug4945");
	}

	@Test
	public void testBug6535() throws TranscoderException, IOException {
		runTests("Bug6535");
	}

	@Test
	public void testBug17965() throws TranscoderException, IOException {
		runTests("Bug17965");
	}

	@Test
	public void testClip() throws TranscoderException, IOException {
		runTests("Clip");
	}

	@Test
	public void testColor1() throws TranscoderException, IOException {
		runTests("Color1");
	}

	@Test
	public void testColor2() throws TranscoderException, IOException {
		runTests("Color2");
	}

	@Test
	public void testDrawImage() throws TranscoderException, IOException {
		runTests("DrawImage");
	}

	@Test
	public void testDrawImageICC() throws TranscoderException, IOException {
		runTests("DrawImageICC", SVGAccuracyTest.WARN_ON_ERROR, 7);
	}

	@Test
	public void testFont1() throws TranscoderException, IOException {
		assumeTrue(isWindows(), "Test uses logical fonts, reference data is for Windows");
		runTests("Font1");
	}

	private static boolean isWindows() {
		String osName = System.getProperty("os.name", "");
		return osName.startsWith("Windows");
	}

	@Test
	public void testFontDecoration() throws TranscoderException, IOException {
		runTests("FontDecoration");
	}

	@Test
	public void testGVector() throws TranscoderException, IOException {
		runTests("GVector");
	}

	@Test
	public void testGradient() throws TranscoderException, IOException {
		runTests("Gradient");
	}

	@Test
	public void testGraphicObjects() throws TranscoderException, IOException {
		runTests("GraphicObjects");
	}

	@Test
	public void testIdentityTest() throws TranscoderException, IOException {
		runTests("IdentityTest");
	}

	@Test
	public void testLookup() throws TranscoderException, IOException {
		runTests("Lookup");
	}

	@Test
	public void testNegativeLengths() throws TranscoderException, IOException {
		runTests("NegativeLengths");
	}

	@Test
	public void testPaints() throws TranscoderException, IOException {
		runTests("Paints");
	}

	@Test
	public void testRHints() throws TranscoderException, FontFormatException, IOException {
		TestFonts.registerFont(Font.TRUETYPE_FONT, "Anton-Regular.ttf");
		runTests("RHints");
	}

	@Test
	public void testRescale() throws TranscoderException, IOException {
		runTests("Rescale");
	}

	@Test
	public void testShearTest() throws TranscoderException, IOException {
		runTests("ShearTest");
	}

	@Test
	public void testTexture() throws TranscoderException, IOException {
		runTests("Texture");
	}

	@Test
	public void testTextSpacePreserve() throws TranscoderException, IOException {
		runTests("TextSpacePreserve");
	}

	@Test
	public void testTransformCollapse() throws TranscoderException, IOException {
		runTests("TransformCollapse");
	}

	/**
	 * The id should be the Painter's class name prefixed with the package name
	 * defined in getPackageName
	 * 
	 * @throws IOException         If an I/O error occurs
	 * @throws TranscoderException
	 */
	void runTests(String painterClassname) throws IOException, TranscoderException {
		runTests(painterClassname, SVGAccuracyTest.FAIL_ON_ERROR, null);
	}

	/**
	 * The id should be the Painter's class name prefixed with the package name
	 * defined in getPackageName
	 * 
	 * @throws IOException         If an I/O error occurs
	 * @throws TranscoderException
	 */
	void runTests(String painterClassname, int accuracyTestErrorHandling, Integer compressionLevel)
			throws IOException, TranscoderException {
		String clName = getClass().getPackage().getName() + "." + painterClassname;
		Class<?> cl = null;

		try {
			cl = Class.forName(clName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(clName);
		}

		Object o = null;

		try {
			o = cl.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(clName);
		}

		if (!(o instanceof Painter)) {
			throw new IllegalArgumentException(clName);
		}

		Painter painter = (Painter) o;

		SVGAccuracyTest acctest = makeSVGAccuracyTest(painter, painterClassname);
		acctest.setCompressionLevel(compressionLevel);
		acctest.runTest(accuracyTestErrorHandling);

		GeneratorContext genctxt = makeGeneratorContext(painter, painterClassname);
		genctxt.setCompressionLevel(compressionLevel);
		genctxt.runTest(accuracyTestErrorHandling);

		float allowedPercentBelowThreshold = 0.01f;
		float allowedPercentOverThreshold = 0.01f;
		SVGRenderingAccuracyTest t = makeSVGRenderingAccuracyTest(painter, painterClassname, PLAIN_GENERATION_PREFIX);
		t.runTest(allowedPercentBelowThreshold, allowedPercentOverThreshold);

		t = makeSVGRenderingAccuracyTest(painter, painterClassname, CUSTOM_CONTEXT_GENERATION_PREFIX);
		t.runTest(allowedPercentBelowThreshold, allowedPercentOverThreshold);

		ImageCompareUtil ict = makeImageCompareTest(painter, painterClassname, PLAIN_GENERATION_PREFIX,
				CUSTOM_CONTEXT_GENERATION_PREFIX);
		// Tolerances must be higher, as rendering varies a bit
		String err = ict.compare(1.1f, 0.004f);
		if (err != null) {
			fail(err);
		}
	}

	private ImageCompareUtil makeImageCompareTest(Painter painter, String id, String prefixA, String prefixB)
			throws MalformedURLException {
		String cl = getNonQualifiedClassName(painter);
		String clA = prefixA + cl;
		String clB = prefixB + cl;
		String testReferenceA = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + clA + PNG_EXTENSION;
		String testReferenceB = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + clB + PNG_EXTENSION;
		TempImageFiles fileUtil = new TempImageFiles(TestLocations.getTestProjectBuildURL());
		ImageCompareUtil t = new ImageCompareUtil(fileUtil, PIXEL_THRESHOLD, testReferenceA, testReferenceB);
		return t;
	}

	private SVGRenderingAccuracyTest makeSVGRenderingAccuracyTest(Painter painter, String id, String prefix)
			throws MalformedURLException {
		String cl = prefix + getNonQualifiedClassName(painter);
		String testSource = GENERATOR_REFERENCE_BASE + cl + SVG_EXTENSION;
		String testReference = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + cl + PNG_EXTENSION;
		String[] variationURLs = new String[VARIATION_PLATFORMS.length + 1];
		variationURLs[0] = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + ACCEPTED_VARIATION_DIR + "/" + cl
				+ PNG_EXTENSION;
		for (int i = 0; i < VARIATION_PLATFORMS.length; i++) {
			variationURLs[i + 1] = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + ACCEPTED_VARIATION_DIR + "/" + cl
					+ '_' + VARIATION_PLATFORMS[i] + PNG_EXTENSION;
		}
		String vPath = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + CANDIDATE_VARIATION_DIR + "/" + cl;
		String saveRangeVariation = vPath + PNG_EXTENSION;
		String savePlatformVariation = vPath + PLATFORM_VARIATION_SUFFIX + PNG_EXTENSION;
		String candidateReference = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + RENDERING_CANDIDATE_REF_DIR + "/"
				+ cl + PNG_EXTENSION;

		SVGRenderingAccuracyTest test = new SVGRenderingAccuracyTest(testSource, testReference);
		for (String variationURL : variationURLs) {
			test.addVariationURL(variationURL);
		}

		test.setSaveRangeVariation(new URL(saveRangeVariation).getFile());
		test.setSavePlatformVariation(new URL(savePlatformVariation).getFile());
		test.setCandidateReference(new File(new URL(candidateReference).getFile()));

		return test;
	}

	private GeneratorContext makeGeneratorContext(Painter painter, String id) throws MalformedURLException {
		String cl = CUSTOM_CONTEXT_GENERATION_PREFIX + getNonQualifiedClassName(painter);

		GeneratorContext test = new GeneratorContext(painter,
				getReferenceURL(painter, CUSTOM_CONTEXT_GENERATION_PREFIX));

		String filename = new URL(GENERATOR_REFERENCE_BASE + CANDIDATE_REF_DIR + "/" + cl + SVG_EXTENSION).getFile();
		test.setSaveSVG(new File(filename));
		return test;
	}

	private SVGAccuracyTest makeSVGAccuracyTest(Painter painter, String id) throws MalformedURLException {
		String cl = PLAIN_GENERATION_PREFIX + getNonQualifiedClassName(painter);

		SVGAccuracyTest test = new SVGAccuracyTest(painter, getReferenceURL(painter, PLAIN_GENERATION_PREFIX));

		String filename = new URL(GENERATOR_REFERENCE_BASE + CANDIDATE_REF_DIR + "/" + cl + SVG_EXTENSION).getFile();
		test.setSaveSVG(new File(filename));
		return test;
	}

	static String getNonQualifiedClassName(Painter painter) {
		String cl = painter.getClass().getName();
		int n = cl.lastIndexOf('.');
		return cl.substring(n + 1);
	}

	static URL getReferenceURL(Painter painter, String prefix) throws MalformedURLException {
		String suffix = prefix + getNonQualifiedClassName(painter) + SVG_EXTENSION;
		URL refUrl = new URL(GENERATOR_REFERENCE_BASE + suffix);
		return refUrl;
	}

}
