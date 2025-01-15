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

import io.sf.carte.echosvg.parser.AngleHandler;
import io.sf.carte.echosvg.parser.AngleParser;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * Check that there is no performance degradation in the angle parser.
 *
 * @version $Id$
 */
@Threads(4)
@Fork(value = 2)
@Measurement(iterations = 4, time = 10)
@Warmup(iterations = 4, time = 10)
public class AngleParserMark {

	@Benchmark
	public void markParser() {
		runParser("123.456deg");
	}

	@Benchmark
	public void markParserCalc() {
		runParser("calc(1.2rad*2)");
	}

	/**
	 * Parse the input.
	 * 
	 * @param source The source to parse.
	 */
	private void runParser(String source) throws ParseException {
		HandlerImpl handler = new HandlerImpl();
		AngleParser pp = new AngleParser(handler);

		pp.parse(new StringReader(source));
	}

	private class HandlerImpl implements AngleHandler {

		private StringBuilder buffer;

		HandlerImpl() {
		}

		@Override
		public void startAngle() throws ParseException {
			buffer = new StringBuilder();
		}

		@Override
		public void angleValue(float v) throws ParseException {
			buffer.append(v);
		}

		@Override
		public void deg() throws ParseException {
			buffer.append("deg");
		}

		@Override
		public void grad() throws ParseException {
			buffer.append("grad");
		}

		@Override
		public void rad() throws ParseException {
			buffer.append("rad");
		}

		@Override
		public void turn() throws ParseException {
			buffer.append("turn");
		}

		@Override
		public void endAngle() {
		}

	}

}
