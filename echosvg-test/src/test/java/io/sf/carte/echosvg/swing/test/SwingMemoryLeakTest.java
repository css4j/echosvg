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
package io.sf.carte.echosvg.swing.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.svg.AbstractRenderingAccuracyTest;

/**
 * Swing Memory Leak Tests
 *
 * @author see Git history.
 * @version $Id$
 */
public class SwingMemoryLeakTest {

	@Test
	public void test() throws Exception {
		new JSVGMemoryLeakTest().runTest("samples/anne.svg");
	}

	@Test
	public void testMemoryLeak1() throws Exception {
		new JSVGMemoryLeakTest().runTest("samples/tests/spec/scripting/memoryLeak1.svg");
	}

	@Test
	public void testPrimaryDoc() throws Exception {
		new JSVGMemoryLeakTest().runTest("samples/tests/spec/scripting/primaryDoc.svg");
	}

	@Test
	public void testInterrupt() throws Exception {
		new JSVGInterruptTest().runTest("samples/anne.svg");
	}

	@Disabled
	@Test
	public void testInterrupt2() throws Exception {
		new JSVGInterruptTest().runTest("samples/sydney.svg");
	}

	@Disabled
	@Test
	public void testInterrupt3() throws Exception {
		new JSVGInterruptTest().runTest("samples/mines.svg");
	}

	@Test
	public void testSetSVGDocument() throws Exception {
		SetSVGDocumentTest test = new SetSVGDocumentTest();

		test.setFile("test-resources/io/sf/carte/echosvg/test/svg/SetSVGDocumentTest.svg");
		test.runTest(0f, 0f);
	}

	@Test
	public void testNullSetSVGDocument() throws Exception {
		URL svgURL;
		try {
			svgURL = AbstractRenderingAccuracyTest.resolveURL("samples/anne.svg");
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}

		new NullSetSVGDocumentTest().runTest(svgURL.getPath());
	}

	@Test
	public void testNullURI() throws Exception {
		URL svgURL;
		try {
			svgURL = AbstractRenderingAccuracyTest.resolveURL("samples/anne.svg");
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}

		new NullURITest().runTest(svgURL.getPath());
	}

}
