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
package io.sf.carte.echosvg.parser.jmh;

import java.io.StringReader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.sf.carte.echosvg.parser.DefaultPathHandler;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.PathParser;

/**
 * Check that there is no performance degradation in the path parser.
 *
 * @version $Id$
 */
@Threads(4)
@Fork(value = 2)
@Measurement(iterations = 4, time = 10)
@Warmup(iterations = 4, time = 10)
public class PathParserMark {

	@Benchmark
	public void markParser() {
		runParser("M1 2a3 4 5 0,1 6 7");
		runParser("M1.0 2.0C3.1 4.1 5.2 6.1 7.6 8.7");
	}

	/**
	 * Parse the input.
	 * 
	 * @param source The source to parse.
	 */
	private void runParser(String source) throws ParseException {
		HandlerImpl handler = new HandlerImpl();
		PathParser pp = new PathParser(handler);

		pp.parse(new StringReader(source));
	}

	private class HandlerImpl extends DefaultPathHandler {

		private StringBuilder buffer;

		HandlerImpl() {
		}

		@Override
		public void startPath() {
			buffer = new StringBuilder(64);
		}

		@Override
		public void movetoRel(float x, float y) {
			buffer.append('m');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void movetoAbs(float x, float y) {
			buffer.append('M');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void closePath() {
			buffer.append('Z');
		}

		@Override
		public void linetoRel(float x, float y) {
			buffer.append('l');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void linetoAbs(float x, float y) {
			buffer.append('L');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void linetoHorizontalRel(float x) {
			buffer.append('h');
			buffer.append(x);
		}

		@Override
		public void linetoHorizontalAbs(float x) {
			buffer.append('H');
			buffer.append(x);
		}

		@Override
		public void linetoVerticalRel(float y) {
			buffer.append('v');
			buffer.append(y);
		}

		@Override
		public void linetoVerticalAbs(float y) {
			buffer.append('V');
			buffer.append(y);
		}

		@Override
		public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y) {
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
		public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) {
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
		public void curvetoCubicSmoothRel(float x2, float y2, float x, float y) {
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
		public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y) {
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
		public void curvetoQuadraticRel(float x1, float y1, float x, float y) {
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
		public void curvetoQuadraticAbs(float x1, float y1, float x, float y) {
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
		public void curvetoQuadraticSmoothRel(float x, float y) {
			buffer.append('t');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void curvetoQuadraticSmoothAbs(float x, float y) {
			buffer.append('T');
			buffer.append(x);
			buffer.append(' ');
			buffer.append(y);
		}

		@Override
		public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x,
				float y) {
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
				float y) {
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
