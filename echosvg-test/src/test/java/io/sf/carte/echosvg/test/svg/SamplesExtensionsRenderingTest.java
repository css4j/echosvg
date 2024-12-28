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
 * This test renders a number of SVG files under the {@code samples/extensions}
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
public class SamplesExtensionsRenderingTest extends AbstractSamplesRendering {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	/*
	 * Extensions
	 */
	@Test
	public void testExtColorSwitch() throws TranscoderException, IOException {
		testNV("samples/extensions/colorSwitch.svg");
	}

	@Test
	public void testExtHistogramNormalization() throws TranscoderException, IOException {
		testNV("samples/extensions/histogramNormalization.svg");
	}

	@Test
	public void testExtRegularPolygon() throws TranscoderException, IOException {
		testNV("samples/extensions/regularPolygon.svg");
	}

	@Test
	public void testExtStar() throws TranscoderException, IOException {
		testNV("samples/extensions/star.svg");
	}

	@Test
	public void testExtFlowText() throws TranscoderException, IOException {
		testNV("samples/extensions/flowText.svg");
	}

	@Test
	public void testExtFlowTextAlign() throws TranscoderException, IOException {
		testNV("samples/extensions/flowTextAlign.svg");
	}

}
