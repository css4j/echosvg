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

package io.sf.carte.echosvg.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

/**
 * To test the transform list parser.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class TransformListParserTest {

	/**
	 * @param sourceTransform      The transform to parse.
	 * @param destinationTransform The transform after serialization.
	 */
	private void testTransformListParser(String sourceTransform, String destinationTransform)
			throws ParseException {
		TestHandler handler = new TestHandler();
		TransformListParser pp = new TransformListParser(handler);

		pp.parse(new StringReader(sourceTransform));

		assertEquals(destinationTransform, handler.resultTransform);
	}

	@Test
	public void testMatrix() {
		testTransformListParser("matrix(1 2 3 4 5 6)", "matrix(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)");
	}

	@Test
	public void testTranslate() {
		testTransformListParser("translate(1)", "translate(1.0)");

		testTransformListParser("translate(1e2 3e4)", "translate(100.0, 30000.0)");
	}

	@Test
	public void testScale() {
		testTransformListParser("scale(1e-2)", "scale(0.01)");

		testTransformListParser("scale(-1e-2 -3e-4)", "scale(-0.01, -3.0E-4)");
	}

	@Test
	public void testSkew() {
		testTransformListParser("skewX(1.234)", "skewX(1.234)");

		testTransformListParser("skewY(.1)", "skewY(0.1)");
	}

	@Test
	public void testFunctionLists() {
		testTransformListParser("translate(1,2) skewY(.1)", "translate(1.0, 2.0) skewY(0.1)");

		testTransformListParser("scale(1,2),skewX(.1e1)", "scale(1.0, 2.0) skewX(1.0)");

		testTransformListParser("scale(1) , skewX(2) translate(3,4)", "scale(1.0) skewX(2.0) translate(3.0, 4.0)");
	}

	private class TestHandler extends DefaultTransformListHandler {

		private StringBuilder buffer;
		String resultTransform;

		private boolean first;

		TestHandler() {
		}

		@Override
		public void startTransformList() {
			buffer = new StringBuilder();
			first = true;
		}

		@Override
		public void matrix(float a, float b, float c, float d, float e, float f) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("matrix(");
			buffer.append(a);
			buffer.append(", ");
			buffer.append(b);
			buffer.append(", ");
			buffer.append(c);
			buffer.append(", ");
			buffer.append(d);
			buffer.append(", ");
			buffer.append(e);
			buffer.append(", ");
			buffer.append(f);
			buffer.append(")");
		}

		@Override
		public void rotate(float theta) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
		}

		@Override
		public void rotate(float theta, float cx, float cy) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
		}

		@Override
		public void translate(float tx) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("translate(");
			buffer.append(tx);
			buffer.append(")");
		}

		@Override
		public void translate(float tx, float ty) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("translate(");
			buffer.append(tx);
			buffer.append(", ");
			buffer.append(ty);
			buffer.append(")");
		}

		@Override
		public void scale(float sx) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("scale(");
			buffer.append(sx);
			buffer.append(")");
		}

		@Override
		public void scale(float sx, float sy) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("scale(");
			buffer.append(sx);
			buffer.append(", ");
			buffer.append(sy);
			buffer.append(")");
		}

		@Override
		public void skewX(float skx) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("skewX(");
			buffer.append(skx);
			buffer.append(")");
		}

		@Override
		public void skewY(float sky) {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("skewY(");
			buffer.append(sky);
			buffer.append(")");
		}

		@Override
		public void endTransformList() {
			resultTransform = buffer.toString();
		}

	}

}
