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
package io.sf.carte.echosvg.svggen;

import static org.junit.Assert.fail;

import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.BeforeClass;

import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.svg.ImageCompareTest;
import io.sf.carte.echosvg.test.svg.SVGRenderingAccuracyTest;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test validates that a given rendering sequence, modeled by a
 * <code>Painter</code> by doing several subtests for a single input class: +
 * SVGAccuracyTest with several configurations + SVGRenderingAccuracyTest +
 * ImageComparisonTest between the rendering of the generated SVG for various
 * configurations.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGGeneratorTests {
	private static final String GENERATOR_REFERENCE_BASE;

	private static final String RENDERING_DIR = "rendering";

	private static final String ACCEPTED_VARIATION_DIR = "accepted-variation";

	private static final String[] VARIATION_PLATFORMS = io.sf.carte.echosvg.test.svg.PreconfiguredRenderingTest.DEFAULT_VARIATION_PLATFORMS;

	private static final String ACCEPTED_REF_DIR = "accepted-ref";

	private static final String CANDIDATE_VARIATION_DIR = "candidate-variation";

	private static final String CANDIDATE_REF_DIR = "candidate-ref";

	private static final String RENDERING_CANDIDATE_REF_DIR = "candidate-reference";

	private static final String PNG_EXTENSION = ".png";

	private static final String SVG_EXTENSION = ".svg";

	private static final String PLAIN_GENERATION_PREFIX = "";

	private static final String CUSTOM_CONTEXT_GENERATION_PREFIX = "Context";

	static {
		GENERATOR_REFERENCE_BASE = TestLocations.getRootBuildURL() + "test-references/io/sf/carte/echosvg/svggen/";
	}

	@BeforeClass
	public static void beforeClass() throws FontFormatException, IOException {
		TestFonts.loadTestFonts();
	}

	@org.junit.Test
	public void testATransform() throws Exception {
		runTests("ATransform");
	}

	@org.junit.Test
	public void testAttributedCharacterIterator() throws Exception {
		runTests("AttributedCharacterIterator");
	}

	@org.junit.Test
	public void testBasicShapes() throws Exception {
		runTests("BasicShapes");
	}

	@org.junit.Test
	public void testBasicShapes2() throws Exception {
		runTests("BasicShapes2");
	}

	@org.junit.Test
	public void testBStroke() throws Exception {
		runTests("BStroke");
	}

	@org.junit.Test
	public void testBug4389() throws Exception {
		runTests("Bug4389");
	}

	@org.junit.Test
	public void testBug4945() throws Exception {
		runTests("Bug4945");
	}

	@org.junit.Test
	public void testBug6535() throws Exception {
		runTests("Bug6535");
	}

	@org.junit.Test
	public void testBug17965() throws Exception {
		runTests("Bug17965");
	}

	@org.junit.Test
	public void testClip() throws Exception {
		runTests("Clip");
	}

	@org.junit.Test
	public void testColor1() throws Exception {
		runTests("Color1");
	}

	@org.junit.Test
	public void testColor2() throws Exception {
		runTests("Color2");
	}

	@org.junit.Test
	public void testDrawImage() throws Exception {
		runTests("DrawImage");
	}

	@org.junit.Test
	public void testFont1() throws Exception {
		runTests("Font1");
	}

	@org.junit.Test
	public void testFontDecoration() throws Exception {
		runTests("FontDecoration");
	}

	@org.junit.Test
	public void testGVector() throws Exception {
		runTests("GVector");
	}

	@org.junit.Test
	public void testGradient() throws Exception {
		runTests("Gradient");
	}

	@org.junit.Test
	public void testGraphicObjects() throws Exception {
		runTests("GraphicObjects");
	}

	@org.junit.Test
	public void testIdentityTest() throws Exception {
		runTests("IdentityTest");
	}

	@org.junit.Test
	public void testLookup() throws Exception {
		runTests("Lookup");
	}

	@org.junit.Test
	public void testNegativeLengths() throws Exception {
		runTests("NegativeLengths");
	}

	@org.junit.Test
	public void testPaints() throws Exception {
		runTests("Paints");
	}

	@org.junit.Test
	public void testRHints() throws Exception {
		runTests("RHints");
	}

	@org.junit.Test
	public void testRescale() throws Exception {
		runTests("Rescale");
	}

	@org.junit.Test
	public void testShearTest() throws Exception {
		runTests("ShearTest");
	}

	@org.junit.Test
	public void testTexture() throws Exception {
		runTests("Texture");
	}

	@org.junit.Test
	public void testTextSpacePreserve() throws Exception {
		runTests("TextSpacePreserve");
	}

	@org.junit.Test
	public void testTransformCollapse() throws Exception {
		runTests("TransformCollapse");
	}

	/**
	 * The id should be the Painter's class name prefixed with the package name
	 * defined in getPackageName
	 * 
	 * @throws IOException
	 * @throws TranscoderException
	 * @throws FontFormatException 
	 */
	void runTests(String painterClassname) throws IOException, TranscoderException, FontFormatException {
		String clName = getClass().getPackageName() + "." + painterClassname;
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
		acctest.runTest(false);

		GeneratorContext genctxt = makeGeneratorContext(painter, painterClassname);
		genctxt.runTest(false);

		SVGRenderingAccuracyTest t = makeSVGRenderingAccuracyTest(painter, painterClassname, PLAIN_GENERATION_PREFIX);
		t.runTest();

		t = makeSVGRenderingAccuracyTest(painter, painterClassname, CUSTOM_CONTEXT_GENERATION_PREFIX);
		t.runTest();

		ImageCompareTest ict = makeImageCompareTest(painter, painterClassname, PLAIN_GENERATION_PREFIX,
				CUSTOM_CONTEXT_GENERATION_PREFIX);
		String err = ict.compare(0.005d);
		if (err != null) {
			fail(err);
		}
	}

	private ImageCompareTest makeImageCompareTest(Painter painter, String id, String prefixA, String prefixB) {
		String cl = getNonQualifiedClassName(painter);
		String clA = prefixA + cl;
		String clB = prefixB + cl;
		String testReferenceA = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + clA + PNG_EXTENSION;
		String testReferenceB = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + clB + PNG_EXTENSION;
		ImageCompareTest t = new ImageCompareTest(testReferenceA, testReferenceB);
		return t;
	}

	private SVGRenderingAccuracyTest makeSVGRenderingAccuracyTest(Painter painter, String id, String prefix) throws MalformedURLException {
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
		String saveVariation = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + CANDIDATE_VARIATION_DIR + "/" + cl
				+ PNG_EXTENSION;
		String candidateReference = GENERATOR_REFERENCE_BASE + RENDERING_DIR + "/" + RENDERING_CANDIDATE_REF_DIR + "/"
				+ cl + PNG_EXTENSION;

		SVGRenderingAccuracyTest test = new SVGRenderingAccuracyTest(testSource, testReference);
		for (String variationURL : variationURLs) {
			test.addVariationURL(variationURL);
		}

		test.setSaveVariation(new File(new URL(saveVariation).getFile()));
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

	private String getNonQualifiedClassName(Painter painter) {
		String cl = painter.getClass().getName();
		int n = cl.lastIndexOf('.');
		return cl.substring(n + 1);
	}

	private URL getReferenceURL(Painter painter, String prefix) throws MalformedURLException {
		String suffix = prefix + getNonQualifiedClassName(painter) + SVG_EXTENSION;
		URL refUrl = new URL(GENERATOR_REFERENCE_BASE + ACCEPTED_REF_DIR + '/' + suffix);
		File acceptedReference = new File(refUrl.getFile());
		if (!acceptedReference.exists()) {
			refUrl = new URL(GENERATOR_REFERENCE_BASE + suffix);
		}
		return refUrl;
	}

}
