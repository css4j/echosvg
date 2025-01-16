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

import io.sf.carte.echosvg.parser.NumberListHandler;
import io.sf.carte.echosvg.parser.NumberListParser;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * Check that there is no performance degradation in the number list parser.
 *
 * @version $Id$
 */
@Threads(4)
@Fork(value = 2)
@Measurement(iterations = 4, time = 10)
@Warmup(iterations = 4, time = 10)
public class NumberListParserMark {

	@Benchmark
	public void markParser() {
		runParser("0 10 5 3 5 8  10 0");
		runParser("0.0,10.0,5.0,3,5,8,  10, 0");
	}

	@Benchmark
	public void markParserCalc() {
		runParser("0 10 5 calc(2 + 1) 5 8  10 0");
		runParser("0 10  calc(2*2 + 1) 3 5.0 calc(2*4)  10,0");
	}

	/**
	 * Parse the input.
	 * 
	 * @param source The numbers to parse.
	 */
	private void runParser(String source) throws ParseException {
		TestHandler handler = new TestHandler();
		NumberListParser pp = new NumberListParser(handler);

		pp.parse(new StringReader(source));
	}


	private class TestHandler implements NumberListHandler {

		StringBuilder buffer;

		TestHandler() {
		}

		@Override
		public void startNumberList() throws ParseException {
			buffer = new StringBuilder(32);
		}

		@Override
		public void endNumberList() throws ParseException {
		}

		@Override
		public void startNumber() throws ParseException {
		}

		@Override
		public void endNumber() throws ParseException {
		}

		@Override
		public void numberValue(float v) throws ParseException {
			buffer.append(v);
		}

	}

}
