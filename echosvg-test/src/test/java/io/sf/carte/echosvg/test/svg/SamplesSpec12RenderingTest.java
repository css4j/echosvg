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

import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files under the {@code samples/spec12}
 * directory, and compares the result with a reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * Only SVG documents that are non-conformant to SVG 1.1 are tested, so they are
 * run as non-validating tests with {@link #testNV(String)}.
 * </p>
 */
public class SamplesSpec12RenderingTest extends AbstractSamplesRendering {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	/*
	 * SVG 1.2
	 */
	@Test
	public void testFlowBidi() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowBidi.svg");
	}

	@Test
	public void testFlowText() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText.svg");
	}

	@Test
	public void testFlowText2() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText2.svg");
	}

	@Test
	public void testFlowText3() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText3.svg");
	}

	@Test
	public void testFlowText4() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText4.svg");
	}

	@Test
	public void testFlowText5() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText5.svg");
	}

	@Test
	public void testFlowRegionBreak() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowRegionBreak.svg");
	}

	@Test
	public void testLineHeightFontShorthand() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/lineHeightFontShorthand.svg");
	}

	@Test
	public void testStructureMulti() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/multi.svg");
	}

	@Test
	public void testStructureMulti2() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/multi2.svg");
	}

	@Test
	public void testOperaSubImage() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/opera/opera-subImage.svg");
	}

	@Test
	public void testOperaSubImageRef() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/opera/opera-subImageRef.svg");
	}

	@Test
	public void testPaintsSolidColor() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/paints/solidColor.svg");
	}

	@Test
	public void testPaintsSolidColor2() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/paints/solidColor2.svg");
	}

	// See issue #27
	@Disabled
	@Test
	public void testFilterRegion() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/filters/filterRegion.svg");
	}

	@Test
	public void testFilterRegionDetailed() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/filters/filterRegionDetailed.svg");
	}

}
