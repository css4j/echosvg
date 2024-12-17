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
 * Test the angle parser.
 */
public class AngleParserTest {

	private static DecimalFormat numFormat;

	@BeforeAll
	static void setUpBeforeClass() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
		numFormat = new DecimalFormat("#.#", dfs);
		numFormat.setMinimumFractionDigits(0);
		numFormat.setMaximumFractionDigits(5);
	}

	/**
	 * Test the parser.
	 * 
	 * @param source      The number to parse.
	 * @param destination The number after serialization.
	 */
	private void testParser(String source, String destination) {
		TestHandler handler = new TestHandler();
		AngleParser pp = new AngleParser(handler);

		pp.parse(new StringReader(source));

		assertEquals(destination, handler.result);
	}

	@Test
	public void test() {
		testParser("123.456", "123.456");
		testParser("123.456deg", "123.456deg");
		testParser("123.456grad", "123.456grad");
		testParser("1.2rad", "1.2rad");
		testParser(".87turn", "0.87turn");
	}

	@Test
	public void testCalc() {
		testParser("calc(1.2rad*2)", "2.4rad");
	}

	@Test
	void testError() {
		assertThrows(ParseException.class, () -> testParser("5dpi", ""));
	}

	@Test
	void testCalcError() {
		assertThrows(ParseException.class, () -> testParser("calc(5dpi*2)", ""));
	}

	private class TestHandler implements AngleHandler {

		private StringBuilder buffer;
		String result;

		TestHandler() {
		}

		@Override
		public void startAngle() throws ParseException {
			buffer = new StringBuilder();
		}

		@Override
		public void angleValue(float v) throws ParseException {
			buffer.append(numFormat.format(v));
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
		public void endAngle() throws ParseException {
			result = buffer.toString();
		}

	}

}
