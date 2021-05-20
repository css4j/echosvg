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

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Validates the operation of the <code>SVGAccuractyTest</code> class
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAccuracyTestValidator {
	/**
	 * Checks that test fails if: + Rendering sequence generates an exception +
	 * There is no reference image + Reference SVG differs from the generated SVG
	 * Checks that test works if SVG and reference SVG are identical
	 * @throws IOException 
	 */
	@org.junit.Test
	public void testSVGAccuracyValidator() throws Exception {
		new PainterWithException().test();
		new NullReferenceURL().test();
		new InexistantReferenceURL().test();
		new DiffWithReferenceImage().test();
		new SameAsReferenceImage().test();
	}

	static class PainterWithException implements Painter {

		@Override
		public void paint(Graphics2D g) {
			g.setComposite(null); // Will cause the exception
			g.fillRect(0, 0, 20, 20);
		}

		public void test() throws Exception {
			Painter painter = this;
			URL refURL = new URL("http", "dummyHost", "dummyFile.svg");
			SVGAccuracyTest t = new SVGAccuracyTest(painter, refURL);
			try {
				t.runTest(true);
				fail("Must throw exception.");
			} catch (NullPointerException e) {
			}
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
		public void test() throws Exception {
			SVGAccuracyTest t = new SVGAccuracyTest(this, null);
			try {
				t.runTest(true);
				fail("Must throw exception.");
			} catch (NullPointerException e) {
			}
		}
	}

	static class InexistantReferenceURL extends ValidPainterTest {
		public void test() throws Exception {
			SVGAccuracyTest t = new SVGAccuracyTest(this, new URL("http", "dummyHost", "dummyFile.svg"));
			try {
				t.runTest(true);
				fail("Must throw exception.");
			} catch (UnknownHostException e) {
			}
		}
	}

	static class DiffWithReferenceImage extends ValidPainterTest {
		public void test() throws Exception {
			File tmpFile = File.createTempFile("EmptySVGReference", null);
			tmpFile.deleteOnExit();

			SVGAccuracyTest t = new SVGAccuracyTest(this, tmpFile.toURI().toURL());
			t.runTest(true);
		}
	}

	static class SameAsReferenceImage extends ValidPainterTest {
		public void test() throws Exception {
			File tmpFile = File.createTempFile("SVGReference", null);
			tmpFile.deleteOnExit();

			SVGAccuracyTest t = new SVGAccuracyTest(this, tmpFile.toURI().toURL());

			t.setSaveSVG(tmpFile);

			// This first run should fail but it should
			// have created the reference image in tmpFile
			t.runTest(true);

			// Second run should work because the reference
			// image should match
			t.runTest(false);
		}
	}

}
