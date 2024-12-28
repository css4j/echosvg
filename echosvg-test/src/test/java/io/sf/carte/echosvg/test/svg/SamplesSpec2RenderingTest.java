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
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files, generally under the {@code samples}
 * directory, and compares the result with a reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * There is no DTD for SVG version 2 documents, so the tests are run as
 * non-validating with {@link #testNV(String)}.
 * </p>
 */
public class SamplesSpec2RenderingTest extends AbstractSamplesRendering {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	/*
	 * Namespaceless href
	 */
	@Test
	public void testHref() throws TranscoderException, IOException {
		testNV("samples/tests/spec2/linking/href.svg");
	}

	/*
	 * CSS3 Styling
	 */
	@Test
	public void testAttrValues() throws TranscoderException, IOException {
		testNV("samples/tests/spec2/styling/attrValues.svg");
	}

	@Test
	public void testConditionalRules() throws TranscoderException, IOException {
		testNV("samples/tests/spec2/styling/conditionalRules.svg");
	}

	@Test
	public void testConditionalRulesDark() throws TranscoderException, IOException {
		testDarkMode("samples/tests/spec2/styling/conditionalRules.svg");
	}

	@Test
	public void testConditionalRulesAlternate() throws TranscoderException, IOException {
		testAlternateSheet("samples/tests/spec2/styling/conditionalRules.svg", "Gray", false);
	}

	@Test
	public void testConditionalRulesPrint() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec2/styling/conditionalRules.svg", PRINT_MEDIA, 0);
	}

	@Test
	public void testMermaidColor4() throws TranscoderException, IOException {
		testNV("samples/tests/spec2/styling/mermaid-color4.svg");
	}

}
