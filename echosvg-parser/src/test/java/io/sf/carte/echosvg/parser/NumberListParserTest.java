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
 * Test the number list parser.
 */
public class NumberListParserTest {

	private static DecimalFormat numFormat;

	@BeforeAll
	public static void setUpBeforeClass() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
		numFormat = new DecimalFormat("#.#", dfs);
		numFormat.setMinimumFractionDigits(0);
		numFormat.setMaximumFractionDigits(5);
	}

	/**
	 * Parse the input and compare with the expected output.
	 * 
	 * @param sourcePoints      The points to parse.
	 * @param destinationPoints The points after serialization.
	 * @param numCount          the count of numbers.
	 */
	private void testParser(String sourcePoints, String destinationPoints, int numCount) throws ParseException {
		TestHandler handler = new TestHandler();
		NumberListParser pp = new NumberListParser(handler);

		pp.parse(new StringReader(sourcePoints));

		assertEquals(destinationPoints, handler.result);
		assertEquals(numCount, handler.starts);
		assertEquals(numCount, handler.ends);
	}

	@Test
	public void testDoParse() {
		testParser("0 10 5 3 5 8  10 0", "0 10 5 3 5 8 10 0", 8);
		testParser("0.0,10.0,5.0,3,5,8,  10, 0", "0 10 5 3 5 8 10 0", 8);
	}

	@Test
	public void testDoParseCalc() {
		testParser("0 10 5 calc(2 + 1) 5 8  10 0", "0 10 5 3 5 8 10 0", 8);
		testParser("0 10  calc(2*2 + 1) 3 5.0 calc(2*4)  10,0", "0 10 5 3 5 8 10 0", 8);
	}

	/**
	 * Make sure that invalid units are detected.
	 */
	@Test
	public void testDoParseError() {
		ParseException ex = assertThrows(ParseException.class, () -> testParser("0 10 5 3 5dpi 8 10 0", "", 0));
		assertEquals(11, ex.getColumnNumber());
	}

	/**
	 * Once the first calc() is found, input is processed via CSS parser. Make sure
	 * that invalid units are detected.
	 */
	@Test
	public void testDoParseErrorCSSProcessingInvalidUnit() {
		assertThrows(ParseException.class, () -> testParser("0 calc(5 + 5) 5 3 5 8dpi 10 0", "", 0));
	}

	/**
	 * Once the first calc() is found, input is processed via CSS parser. Make sure
	 * that invalid identifiers are detected.
	 */
	@Test
	public void testDoParseErrorCSSProcessingIdent() {
		assertThrows(ParseException.class, () -> testParser("0 calc(2*5) 5 3 5 foo 10 0", "", 0));
	}

	/**
	 * Once the first calc() is found, input is processed via CSS parser. Make sure
	 * that invalid units located inside a calc() are detected.
	 */
	@Test
	public void testDoParseCalcInvalidUnit() {
		assertThrows(ParseException.class, () -> testParser("0 10 5 3 calc(5dpi*2) 8 10 0", "", 0));
	}

	private class TestHandler implements NumberListHandler {

		StringBuilder buffer;
		String result;

		int starts;
		int ends;

		TestHandler() {
		}

		@Override
		public void startNumberList() throws ParseException {
			buffer = new StringBuilder(32);
		}

		@Override
		public void endNumberList() throws ParseException {
			result = buffer.toString();
		}

		@Override
		public void startNumber() throws ParseException {
			starts++;
		}

		@Override
		public void endNumber() throws ParseException {
			ends++;
		}

		@Override
		public void numberValue(float v) throws ParseException {
			if (buffer.length() > 0) {
				buffer.append(' ');
			}
			buffer.append(numFormat.format(v));
		}

	}

}
