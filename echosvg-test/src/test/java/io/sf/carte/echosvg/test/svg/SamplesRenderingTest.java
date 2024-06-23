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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files, generally under the {@code samples}
 * directory, and compares the result with a reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * If you want to test a non-conformant SVG document, run a non-validating test
 * with {@link #testNV(String)}.
 * </p>
 */
public class SamplesRenderingTest extends AbstractSamplesRendering {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	@Test
	public void testLogo() throws TranscoderException, IOException {
		test("samples/asf-logo.svg");
	}

	@Test
	public void testBarChart() throws TranscoderException, IOException {
		test("samples/barChart.svg");
	}

	@Test
	public void test3D() throws TranscoderException, IOException {
		test("samples/batik3D.svg");
	}

	@Test
	public void test70() throws TranscoderException, IOException {
		test("samples/batik70.svg");
	}

	@Test
	public void testBatikBatik() throws TranscoderException, IOException {
		test("samples/batikBatik.svg");
	}

	@Test
	public void testFX() throws TranscoderException, IOException {
		test("samples/batikFX.svg");
	}

	@Disabled
	@Test
	public void testBatikLogo() throws TranscoderException, IOException {
		test("samples/batikLogo.svg");
	}

	@Test
	public void testYin() throws TranscoderException, IOException {
		test("samples/batikYin.svg");
	}

	/* @formatter:off
	 * 
	 * The file does not exist
	 * 
	 * @Test
	 * public void testBookOfKells() throws TranscoderException, IOException {
	 *     test("samples/bookOfKells.svgz");
	 * }
	 * 
	 * @formatter:on
	 */

	@Test
	public void testChessboard() throws TranscoderException, IOException {
		test("samples/chessboard.svg");
	}

	@Test
	public void testEllipses() throws TranscoderException, IOException {
		test("samples/ellipses.svg");
	}

	@Test
	public void testGradients() throws TranscoderException, IOException {
		test("samples/gradients.svg");
	}

	@Test
	public void testGVT() throws TranscoderException, IOException {
		test("samples/GVT.svg");
	}

	@Test
	public void testHenryV() throws TranscoderException, IOException {
		test("samples/henryV.svg");
	}

	@Test
	public void testLogoShadowOffset() throws TranscoderException, IOException {
		test("samples/logoShadowOffset.svg");
	}

	@Test
	public void testLogoTexture() throws TranscoderException, IOException {
		test("samples/logoTexture.svg");
	}

	@Test
	public void testMapSpain() throws TranscoderException, IOException {
		test("samples/mapSpain.svg");
	}

	@Test
	public void testMapWaadt() throws TranscoderException, IOException {
		test("samples/mapWaadt.svg");
	}

	@Test
	public void testMathMetal() throws TranscoderException, IOException {
		test("samples/mathMetal.svg");
	}

	@Test
	public void testMoonPhases() throws TranscoderException, IOException {
		test("samples/moonPhases.svg");
	}

	@Test
	public void testSizeOfSun() throws TranscoderException, IOException {
		test("samples/sizeOfSun.svg");
	}

	@Test
	public void testSunRise() throws TranscoderException, IOException {
		test("samples/sunRise.svg");
	}

	@Test
	public void testSydney() throws TranscoderException, IOException {
		test("samples/sydney.svg");
	}

	@Test
	public void testTextRotate() throws TranscoderException, IOException {
		test("samples/textRotate.svg");
	}

	@Test
	public void testTextRotateShadows() throws TranscoderException, IOException {
		test("samples/textRotateShadows.svg");
	}

	@Test
	public void testUnsupportedRules() throws TranscoderException, IOException {
		testNV("samples/unsupportedRules.svg");
	}

	/*
	 * Bugs
	 */
	@Test
	public void testBug19363() throws TranscoderException, IOException {
		test("test-resources/io/sf/carte/echosvg/test/svg/bug19363.svg");
	}

	@Override
	float getBelowThresholdAllowed() {
		return 0.5f;
	}

	@Override
	float getOverThresholdAllowed() {
		return 0.5f;
	}

}
