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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test the length list parser.
 *
 * @version $Id$
 */
public class LengthListParserTest {

	private static DecimalFormat numFormat;

	@BeforeAll
	static void setUpBeforeClass() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
		numFormat = new DecimalFormat("#.#", dfs);
		numFormat.setMinimumFractionDigits(0);
		numFormat.setMaximumFractionDigits(5);
	}

	/**
	 * 
	 * @param sourceLength      The length to parse.
	 * @param destinationLength The length after serialization.
	 */
	private void testLengthParser(String sourceLength, String destinationLength) {
		TestHandler handler = new TestHandler();
		LengthListParser pp = new LengthListParser(handler);

		pp.parse(new StringReader(sourceLength));

		assertEquals(destinationLength, handler.resultLength);
	}

	@Test
	public void test() {
		testLengthParser("3mm 27mm 8pt 43px", " 3mm 27mm 8pt 43px");
	}

	@Test
	public void testQuirks() {
		testLengthParser("3 27 8 43", " 3 27 8 43");
	}

	@Test
	public void testCalc() {
		testLengthParser("3mm 27mm calc(2*4pc) 43q", " 3mm 27mm 128px 43Q");
	}

	@Test
	public void testCalcQuirks() {
		testLengthParser("3 27 calc(2*4) 43", " 3 27 8 43");
		testLengthParser("3 27 8 calc(40 + 3)", " 3 27 8 43");
	}

	@Test
	public void testInvalidUnit() {
		assertThrows(ParseException.class, () -> testLengthParser("3 27dpi 8 5", ""));
	}

	@Test
	public void testCalcInvalidUnit() {
		assertThrows(ParseException.class, () -> testLengthParser("3 calc(7dpi*2) 8 5", ""));
	}

	private class TestHandler extends LengthArrayProducer {

		private StringBuilder buffer;
		String resultLength;

		TestHandler() {
		}

		@Override
		public void startLengthList() {
			super.startLengthList();
			buffer = new StringBuilder();
		}

		@Override
		public void lengthValue(float v) {
			buffer.append(' ').append(numFormat.format(v));
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
		public void endLengthList() {
			super.endLengthList();
			resultLength = buffer.toString();
		}

	}

}
