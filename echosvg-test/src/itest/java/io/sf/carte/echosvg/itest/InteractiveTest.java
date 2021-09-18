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
package io.sf.carte.echosvg.itest;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.test.svg.JSVGRenderingAccuracyTest;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * samples and samples/test interactive rendering tests
 */
public class InteractiveTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	@Test
	public void testCursor() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/interactivity/cursor.svg", true);
	}

	@Test
	public void testFocus() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/interactivity/focus.svg", true);
	}

	@Test
	public void testKeyEvents() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/interactivity/keyEvents.svg", false);
	}

	@Test
	public void testPointerEvents() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/interactivity/pointerEvents.svg", true);
	}

	private void testDynamicUpdate(String file, boolean validating) throws TranscoderException, IOException {
		JSVGRenderingAccuracyTest runner = new JSVGRenderingAccuracyTest();
		runner.setFile(file);
		runner.setValidating(validating);
		runner.runTest(0f, 0f);
	}

}
