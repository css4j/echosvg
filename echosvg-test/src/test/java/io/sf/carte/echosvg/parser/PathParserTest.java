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
 * To test the path parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PathParserTest {

	/**
	 * 
	 * @param sourcePath      The path to parse.
	 * @param destinationPath The path after serialization.
	 */
	private void testPathParser(String sourcePath, String destinationPath) {
		PathParser pp = new PathParser();
		TestHandler handler = new TestHandler();
		pp.setPathHandler(handler);

		pp.parse(new StringReader(sourcePath));

		assertEquals(destinationPath, handler.resultPath);
	}

	@Test
	public void test() throws Exception {
		testPathParser("M1 2", "M1.0 2.0");

		testPathParser("m1.1 2.0", "m1.1 2.0");

		testPathParser("M1 2z", "M1.0 2.0Z");

		testPathParser("M1 2e3Z", "M1.0 2000.0Z");

		testPathParser("M1 2L 3,4", "M1.0 2.0L3.0 4.0");

		testPathParser("M1 2 3,4", "M1.0 2.0L3.0 4.0");

		testPathParser("M1, 2, 3,4", "M1.0 2.0L3.0 4.0");

		testPathParser("m1, 2, 3,4", "m1.0 2.0l3.0 4.0");

		testPathParser("M1 2H3.1", "M1.0 2.0H3.1");

		testPathParser("M1 2H3.1 4", "M1.0 2.0H3.1H4.0");

		testPathParser("M1 2H3.1,4", "M1.0 2.0H3.1H4.0");

		testPathParser("M1 2h 3.1", "M1.0 2.0h3.1");

		testPathParser("M1 2h 3.1 4", "M1.0 2.0h3.1h4.0");

		testPathParser("M1 2h 3.1,4", "M1.0 2.0h3.1h4.0");

		testPathParser("M1 2H 3.1,4", "M1.0 2.0H3.1H4.0");

		testPathParser("M1 2h 3.1-4", "M1.0 2.0h3.1h-4.0");

		testPathParser("M1 2V3.1e-3", "M1.0 2.0V0.0031");

		testPathParser("M1 2V3.1", "M1.0 2.0V3.1");

		testPathParser("M1 2v3.1,.4", "M1.0 2.0v3.1v0.4");

		testPathParser("M1 2v3.1-.4", "M1.0 2.0v3.1v-0.4");

		testPathParser("M1 2C3 4 5 6 7 8", "M1.0 2.0C3.0 4.0 5.0 6.0 7.0 8.0");

		testPathParser("M1 2c.3.4.5.6.7.8", "M1.0 2.0c0.3 0.4 0.5 0.6 0.7 0.8");

		testPathParser("M1 2S3+4+5+6", "M1.0 2.0S3.0 4.0 5.0 6.0");

		testPathParser("M1 2s.3+.4+.5-.6", "M1.0 2.0s0.3 0.4 0.5 -0.6");

		testPathParser("M1 2q3. 4.+5 6", "M1.0 2.0q3.0 4.0 5.0 6.0");

		testPathParser("M1 2Q.3e0.4.5.6", "M1.0 2.0Q0.3 0.4 0.5 0.6");

		testPathParser("M1 2t+.3-.4", "M1.0 2.0t0.3 -0.4");

		testPathParser("M1 2T -.3+4", "M1.0 2.0T-0.3 4.0");

		testPathParser("M1 2a3 4 5 0,1 6 7", "M1.0 2.0a3.0 4.0 5.0 0 1 6.0 7.0");

		testPathParser("M1 2A3 4 5 0,1 6 7", "M1.0 2.0A3.0 4.0 5.0 0 1 6.0 7.0");

		testPathParser("M1 2t+.3-.4,5,6", "M1.0 2.0t0.3 -0.4t5.0 6.0");

		testPathParser("M1 2T -.3+4 5-6", "M1.0 2.0T-0.3 4.0T5.0 -6.0");
	}

	private class TestHandler extends DefaultPathHandler {

		StringBuffer buffer;
		String resultPath;

		TestHandler() {
		}

		@Override
		public void startPath() throws ParseException {
			buffer = new StringBuffer();
		}

		@Override
		public void movetoRel(float x, float y) throws ParseException {
			buffer.append('m');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void movetoAbs(float x, float y) throws ParseException {
			buffer.append('M');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void endPath() throws ParseException {
			resultPath = buffer.toString();
		}

		@Override
		public void closePath() throws ParseException {
			buffer.append('Z');
		}

		@Override
		public void linetoRel(float x, float y) throws ParseException {
			buffer.append('l');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void linetoAbs(float x, float y) throws ParseException {
			buffer.append('L');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void linetoHorizontalRel(float x) throws ParseException {
			buffer.append('h');
			buffer.append(x);
		}

		@Override
		public void linetoHorizontalAbs(float x) throws ParseException {
			buffer.append('H');
			buffer.append(x);
		}

		@Override
		public void linetoVerticalRel(float y) throws ParseException {
			buffer.append('v');
			buffer.append(y);
		}

		@Override
		public void linetoVerticalAbs(float y) throws ParseException {
			buffer.append('V');
			buffer.append(y);
		}

		@Override
		public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
			buffer.append('c');
			buffer.append(x1);
			buffer.append(' ');
			buffer.append(y1);
			buffer.append(' ');
			buffer.append(x2);
			buffer.append(' ');
			buffer.append(y2);
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
			buffer.append('C');
			buffer.append(x1);
			buffer.append(' ');
			buffer.append(y1);
			buffer.append(' ');
			buffer.append(x2);
			buffer.append(' ');
			buffer.append(y2);
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoCubicSmoothRel(float x2, float y2, float x, float y) throws ParseException {
			buffer.append('s');
			buffer.append(x2);
			buffer.append(' ');
			buffer.append(y2);
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y) throws ParseException {
			buffer.append('S');
			buffer.append(x2);
			buffer.append(' ');
			buffer.append(y2);
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoQuadraticRel(float x1, float y1, float x, float y) throws ParseException {
			buffer.append('q');
			buffer.append(x1);
			buffer.append(' ');
			buffer.append(y1);
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoQuadraticAbs(float x1, float y1, float x, float y) throws ParseException {
			buffer.append('Q');
			buffer.append(x1);
			buffer.append(' ');
			buffer.append(y1);
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoQuadraticSmoothRel(float x, float y) throws ParseException {
			buffer.append('t');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoQuadraticSmoothAbs(float x, float y) throws ParseException {
			buffer.append('T');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x,
				float y) throws ParseException {
			buffer.append('a');
			buffer.append(rx);
			buffer.append(' ');
			buffer.append(ry);
			buffer.append(' ');
			buffer.append(xAxisRotation);
			buffer.append(' ');
			buffer.append(largeArcFlag ? '1' : '0');
			buffer.append(' ');
			buffer.append(sweepFlag ? '1' : '0');
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x,
				float y) throws ParseException {
			buffer.append('A');
			buffer.append(rx);
			buffer.append(' ');
			buffer.append(ry);
			buffer.append(' ');
			buffer.append(xAxisRotation);
			buffer.append(' ');
			buffer.append(largeArcFlag ? '1' : '0');
			buffer.append(' ');
			buffer.append(sweepFlag ? '1' : '0');
			buffer.append(' ');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}
	}
}
