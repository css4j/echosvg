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

import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.PointsHandler;
import io.sf.carte.echosvg.parser.PointsParser;

/**
 * Check that there is no performance degradation in the points parser.
 *
 * @version $Id$
 */
@Threads(4)
@Fork(value = 2)
@Measurement(iterations = 4, time = 10)
@Warmup(iterations = 4, time = 10)
public class PointsParserMark {

	@Benchmark
	public void markParser() {
		runPointsParser("0,10 5,3 5,8  10,0");
		runPointsParser("0.0,10.0 5.0,3.0 5.0,8.0  10.0,0.0");
	}

	@Benchmark
	public void markParserCalc() {
		runPointsParser("0,10 5,calc(2 + 1) 5,8  10,0");
	}

	/**
	 * Parse the input.
	 * 
	 * @param sourcePoints The points to parse.
	 */
	private void runPointsParser(String sourcePoints) throws ParseException {
		PointsHandler handler = new PointsHandlerImpl();
		PointsParser pp = new PointsParser(handler);

		pp.parse(new StringReader(sourcePoints));
	}

	private static class PointsHandlerImpl implements PointsHandler {

		StringBuilder buffer;

		PointsHandlerImpl() {
		}

		@Override
		public void startPoints() {
			buffer = new StringBuilder(48);
		}

		@Override
		public void point(float x, float y) {
			buffer.append(x).append(',').append(y);
		}

		@Override
		public void endPoints() {
		}

	}

}
