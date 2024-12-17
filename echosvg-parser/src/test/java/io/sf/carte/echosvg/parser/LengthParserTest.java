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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

/**
 * To test the length parser.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class LengthParserTest {

	/**
	 * 
	 * @param sourceLength      The length to parse.
	 * @param destinationLength The length after serialization.
	 */
	private void testLengthParser(String sourceLength, String destinationLength) {
		TestHandler handler = new TestHandler();
		LengthParser pp = new LengthParser(handler);

		pp.parse(new StringReader(sourceLength));

		assertEquals(destinationLength, handler.resultLength);
	}

	@Test
	public void test() {
		testLengthParser("123.456", "123.456");

		testLengthParser("123em", "123.0em");

		testLengthParser(".456ex", "0.456ex");

		testLengthParser("-.456789in", "-0.456789in");

		testLengthParser("-456789.cm", "-456789.0cm");

		testLengthParser("-4567890.mm", "-4567890.0mm");

		testLengthParser("-000456789.pc", "-456789.0pc");

		testLengthParser("-0.00456789pt", "-0.00456789pt");

		testLengthParser("2q", "2.0Q");

		testLengthParser("-0px", "0.0px");

		testLengthParser("0000%", "0.0%");

		testLengthParser("1rem", "1.0rem");

		testLengthParser("02rex", "2.0rex");

		testLengthParser("2rlh", "2.0rlh");

		testLengthParser(".2vh", "0.2vh");

		testLengthParser("2vw", "2.0vw");

		testLengthParser("2.667vmin", "2.667vmin");

		testLengthParser("2.3vmax", "2.3vmax");
	}

	@Test
	public void testCalc() {
		testLengthParser("calc(2*2mm)", "15.118111px");
	}

	@Test
	public void testCalcInvalidUnit() {
		assertThrows(ParseException.class, () -> testLengthParser("calc(2*2s)", ""));
	}

	private class TestHandler extends DefaultLengthHandler {

		private StringBuilder buffer;
		String resultLength;

		TestHandler() {
		}

		@Override
		public void startLength() {
			buffer = new StringBuilder();
		}

		@Override
		public void lengthValue(float v) {
			buffer.append(v);
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
			if (!LengthParser.handleUnit(unit, this)) {
				throw new ParseException("Invalid unit.", -1, -1);
			}
		}

		@Override
		public void endLength() {
			resultLength = buffer.toString();
		}

	}

}
