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
import org.junit.Ignore;
import org.junit.Test;

import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.test.svg.JSVGRenderingAccuracyTest;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * samples and samples/test interactive rendering tests (scripting)
 */
public class InteractiveScriptingTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	@Test
	public void testAddDescOnClick() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/addDescOnClick.svg");
	}

	@Test
	public void testBug12933() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/bug12933.svg");
	}

	@Test
	public void testFilterPatternUpdate() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/filterPatternUpdate.svg");
	}

	@Test
	public void testForceRedraw() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/forceRedraw.svg");
	}

	@Test
	public void testGradientsUpdate() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/gradientsUpdate.svg");
	}

	@Test
	public void testImageUpdate() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/imageUpdate.svg");
	}

	@Test
	public void testJavaBinding() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/javaBinding.svg");
	}

	@Test
	public void testMaskClipUpdate() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/maskClipUpdate.svg");
	}

	// Ignored by Batik
	@Ignore
	@Test
	public void testRemoveLast() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/removeLast.svg");
	}

	@Test
	public void testSecurity3() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/security3.svg");
	}

	@Test
	public void testSuspendRedraw() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/suspendRedraw.svg");
	}

	@Test
	public void testSvg() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/svg.svg");
	}

	@Test
	public void testSvg2() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/svg2.svg");
	}

	@Test
	public void testTextSelection() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/textSelection.svg");
	}

	@Test
	public void testUSe() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/use.svg");
	}

	private void testDynamicUpdate(String file) throws TranscoderException, IOException {
		JSVGRenderingAccuracyTest runner = new JSVGRenderingAccuracyTest();
		runner.setFile(file);
		runner.runTest();
	}

}
