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
 * This test renders a number of SVG files, generally under the {@code samples/wpt}
 * directory, and compares the result with a reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 */
public class SamplesWPTRenderingTest extends AbstractSamplesRendering {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	/*
	 * Render path until error
	 */
	@Test
	public void testRenderUntilError() throws TranscoderException, IOException {
		testErrIgnore("samples/wpt/path/error-handling/render-until-error.svg", BROWSER_MEDIA, false, 3);
	}

}
