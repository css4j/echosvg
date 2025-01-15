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

import io.sf.carte.echosvg.parser.LengthArrayProducer;
import io.sf.carte.echosvg.parser.LengthListParser;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * Check that there is no performance degradation in the length list parser.
 *
 * @version $Id$
 */
@Threads(4)
@Fork(value = 2)
@Measurement(iterations = 4, time = 10)
@Warmup(iterations = 4, time = 10)
public class LengthListParserMark {

	@Benchmark
	public void markParser() {
		runParser("3mm 27mm 8pt 43px");
	}

	@Benchmark
	public void markParserCalc() {
		runParser("3mm 27mm calc(2*4pc) 43q");
	}

	/**
	 * Parse the input.
	 * 
	 * @param source The source to parse.
	 */
	private void runParser(String source) throws ParseException {
		HandlerImpl handler = new HandlerImpl();
		LengthListParser pp = new LengthListParser(handler);

		pp.parse(new StringReader(source));
	}

	private class HandlerImpl extends LengthArrayProducer {

		private StringBuilder buffer;

		HandlerImpl() {
		}

		@Override
		public void startLengthList() {
			super.startLengthList();
			buffer = new StringBuilder(32);
		}

		@Override
		public void lengthValue(float v) {
			buffer.append(' ').append(v);
		}

		@Override
		public void em() {
			buffer.append("em");
		}

		@Override
		public void ex() {
			buffer.append("ex");
		}

		@Override
		public void lh() {
			buffer.append("lh");
		}

		@Override
		public void in() {
			buffer.append("in");
		}

		@Override
		public void cm() {
			buffer.append("cm");
		}

		@Override
		public void mm() {
			buffer.append("mm");
		}

		@Override
		public void q() {
			buffer.append("Q");
		}

		@Override
		public void pc() {
			buffer.append("pc");
		}

		@Override
		public void pt() {
			buffer.append("pt");
		}

		@Override
		public void px() {
			buffer.append("px");
		}

		@Override
		public void percentage() {
			buffer.append("%");
		}

		@Override
		public void rem() {
			buffer.append("rem");
		}

		@Override
		public void rex() {
			buffer.append("rex");
		}

		@Override
		public void rlh() {
			buffer.append("rlh");
		}

		@Override
		public void vh() {
			buffer.append("vh");
		}

		@Override
		public void vw() {
			buffer.append("vw");
		}

		@Override
		public void vmax() {
			buffer.append("vmax");
		}

		@Override
		public void vmin() {
			buffer.append("vmin");
		}

		@Override
		protected void setUnit(short unit) {
		}

	}

}
