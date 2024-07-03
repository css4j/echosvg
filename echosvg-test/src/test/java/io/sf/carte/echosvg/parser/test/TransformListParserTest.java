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

package io.sf.carte.echosvg.parser.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.parser.DefaultTransformListHandler;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.TransformListParser;

/**
 * To test the transform list parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TransformListParserTest {

	/**
	 * @param sourceTransform      The transform to parse.
	 * @param destinationTransform The transform after serialization.
	 */
	private void testTransformListParser(String sourceTransform, String destinationTransform) {
		TransformListParser pp = new TransformListParser();
		TestHandler handler = new TestHandler();
		pp.setTransformListHandler(handler);

		pp.parse(new StringReader(sourceTransform));

		assertEquals(destinationTransform, handler.resultTransform);
	}

	@Test
	public void test() throws Exception {
		testTransformListParser("matrix(1 2 3 4 5 6)", "matrix(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)");

		testTransformListParser("translate(1)", "translate(1.0)");

		testTransformListParser("translate(1e2 3e4)", "translate(100.0, 30000.0)");

		testTransformListParser("scale(1e-2)", "scale(0.01)");

		testTransformListParser("scale(-1e-2 -3e-4)", "scale(-0.01, -3.0E-4)");

		testTransformListParser("skewX(1.234)", "skewX(1.234)");

		testTransformListParser("skewY(.1)", "skewY(0.1)");

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
		public void startTransformList() throws ParseException {
			buffer = new StringBuilder();
			first = true;
		}

		@Override
		public void matrix(float a, float b, float c, float d, float e, float f) throws ParseException {
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
		public void rotate(float theta) throws ParseException {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
		}

		@Override
		public void rotate(float theta, float cx, float cy) throws ParseException {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
		}

		@Override
		public void translate(float tx) throws ParseException {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("translate(");
			buffer.append(tx);
			buffer.append(")");
		}

		@Override
		public void translate(float tx, float ty) throws ParseException {
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
		public void scale(float sx) throws ParseException {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("scale(");
			buffer.append(sx);
			buffer.append(")");
		}

		@Override
		public void scale(float sx, float sy) throws ParseException {
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
		public void skewX(float skx) throws ParseException {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("skewX(");
			buffer.append(skx);
			buffer.append(")");
		}

		@Override
		public void skewY(float sky) throws ParseException {
			if (!first) {
				buffer.append(' ');
			}
			first = false;
			buffer.append("skewY(");
			buffer.append(sky);
			buffer.append(")");
		}

		@Override
		public void endTransformList() throws ParseException {
			resultTransform = buffer.toString();
		}

	}

}
