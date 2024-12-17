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
 * Test the points parser.
 */
public class PointsParserTest {

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
	 */
	private void testPointsParser(String sourcePoints, String destinationPoints) throws ParseException {
		TestHandler handler = new TestHandler();
		PointsParser pp = new PointsParser(handler);

		pp.parse(new StringReader(sourcePoints));

		assertEquals(destinationPoints, handler.resultPoints);
	}

	@Test
	public void testDoParse() {
		testPointsParser("0,10 5,3 5,8  10,0", "0,10 5,3 5,8 10,0");
	}

	@Test
	public void testDoParseCalc() {
		testPointsParser("0,10 5,calc(2 + 1) 5,8  10,0", "0,10 5,3 5,8 10,0");
		testPointsParser("0,10  calc(2*2 + 1),3 5,calc(2*4)  10,0", "0,10 5,3 5,8 10,0");
	}

	@Test
	public void testDoParseError() {
		ParseException ex = assertThrows(ParseException.class,
				() -> testPointsParser("0,10 5,3 5dpi,8 10,0", ""));
		assertEquals(11, ex.getColumnNumber());
	}

	/**
	 * Once the first calc() is found, input is processed via CSS parser. Make sure
	 * that invalid units are detected.
	 */
	@Test
	public void testDoParseErrorCSSProcessingInvalidUnit() {
		assertThrows(ParseException.class, () -> testPointsParser("0,calc(5 + 5) 5,3 5,8dpi 10,0", ""));
	}

	/**
	 * Once the first calc() is found, input is processed via CSS parser. Make sure
	 * that invalid identifiers are detected.
	 */
	@Test
	public void testDoParseErrorCSSProcessingIdent() {
		assertThrows(ParseException.class, () -> testPointsParser("0,calc(2*5) 5,3 5,foo 10,0", ""));
	}

	@Test
	public void testDoParseCalcInvalidUnit() {
		assertThrows(ParseException.class, () -> testPointsParser("0,10 5,3 calc(5dpi*2),8 10,0", ""));
	}

	private class TestHandler implements PointsHandler {

		StringBuilder buffer;
		String resultPoints;

		TestHandler() {
		}

		@Override
		public void startPoints() {
			buffer = new StringBuilder(32);
		}

		@Override
		public void point(float x, float y) {
			if (buffer.length() > 0) {
				buffer.append(' ');
			}
			buffer.append(numFormat.format(x)).append(',').append(numFormat.format(y));
		}

		@Override
		public void endPoints() {
			resultPoints = buffer.toString();
		}

	}

}
