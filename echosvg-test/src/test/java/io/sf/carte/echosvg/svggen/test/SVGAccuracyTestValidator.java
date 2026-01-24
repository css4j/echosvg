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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

/**
 * Validates the operation of the <code>SVGAccuractyTest</code> class
 *
 * <p>
 * Original author: <a href="mailto:vhardy@apache.org">Vincent Hardy</a>. For
 * later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGAccuracyTestValidator {

	/**
	 * Checks that test fails if Rendering sequence generates an exception.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testPainterException() throws IOException {
		new PainterWithException().test();
	}

	/**
	 * Checks that test fails if the reference URL is null
	 * 
	 * @throws IOException
	 */
	@Test
	public void testNullReferenceURL() throws IOException {
		new NullReferenceURL().test();
	}

	/**
	 * Checks that test fails if the reference URL points to nothing.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testNonexistentReferenceURL() throws IOException {
		new InexistantReferenceURL().test();
	}

	/**
	 * Checks that test fails if Reference SVG differs from the generated SVG
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDiffWithReferenceImage() throws IOException {
		new DiffWithReferenceImage().test();
	}

	/**
	 * Checks that test works if SVG and reference SVG are identical
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSameAsReferenceImage() throws IOException {
		new SameAsReferenceImage().test();
	}

	static class PainterWithException implements Painter {

		@Override
		public void paint(Graphics2D g) {
			g.setComposite(null); // Will cause the exception
			g.fillRect(0, 0, 20, 20);
		}

		public void test() throws IOException {
			Painter painter = this;
			URL refURL = new URL("http", "dummyHost", "dummyFile.svg");
			SVGAccuracyTest t = new SVGAccuracyTest(painter, refURL);
			assertThrows(NullPointerException.class, () -> t.runTest(SVGAccuracyTest.FAIL_IF_NO_ERROR));
		}

	}

	static class ValidPainterTest implements Painter {

		@Override
		public void paint(Graphics2D g) {
			g.setPaint(Color.red);
			g.fillRect(0, 0, 40, 40);
		}

	}

	static class NullReferenceURL extends ValidPainterTest {

		public void test() throws IOException {
			SVGAccuracyTest t = new SVGAccuracyTest(this, null);
			assertThrows(NullPointerException.class, () -> t.runTest(SVGAccuracyTest.FAIL_IF_NO_ERROR));
		}

	}

	static class InexistantReferenceURL extends ValidPainterTest {

		public void test() throws IOException {
			SVGAccuracyTest t = new SVGAccuracyTest(this, new URL("http", "dummyHost", "dummyFile.svg"));
			assertThrows(UnknownHostException.class, () -> t.runTest(SVGAccuracyTest.FAIL_IF_NO_ERROR));
		}

	}

	static class DiffWithReferenceImage extends ValidPainterTest {

		public void test() throws IOException {
			File tmpFile = Files.createTempFile("EmptySVGReference", null).toFile();
			tmpFile.deleteOnExit();

			SVGAccuracyTest t = new SVGAccuracyTest(this, tmpFile.toURI().toURL());
			t.runTest(SVGAccuracyTest.FAIL_IF_NO_ERROR);
		}

	}

	static class SameAsReferenceImage extends ValidPainterTest {

		public void test() throws IOException {
			File tmpFile = Files.createTempFile("SVGReference", null).toFile();
			tmpFile.deleteOnExit();

			SVGAccuracyTest t = new SVGAccuracyTest(this, tmpFile.toURI().toURL());

			t.setSaveSVG(tmpFile);

			// This first run should fail but it should
			// have created the reference image in tmpFile
			t.runTest(SVGAccuracyTest.FAIL_IF_NO_ERROR);

			// Second run should work because the reference
			// image should match
			t.runTest(SVGAccuracyTest.FAIL_ON_ERROR);
		}

	}

}
