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
 * Check the error behavior of the length parser.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class LengthParserFailureTest {

	/**
	 * Check that a given input produces the expected exception at the expected
	 * column number.
	 * 
	 * @param sourceLength   The length to parse.
	 * @param expectedColumn the expected column number.
	 */
	private void testLengthParserFailure(String sourceLength, int expectedColumn) {
		LengthParser pp = new LengthParser(new DefaultLengthHandler());
		ParseException ex = assertThrows(ParseException.class, () -> pp.parse(new StringReader(sourceLength)));
		assertEquals(expectedColumn, ex.getColumnNumber());
	}

	@Test
	public void test() {
		testLengthParserFailure("123.456.7", 8);
		testLengthParserFailure("1e+", 4);
		testLengthParserFailure("+e3", 2);
		testLengthParserFailure("1Em", 3);
		testLengthParserFailure("--1", 2);
		testLengthParserFailure("-1E--2", 5);
		testLengthParserFailure("-.E+1", 3);
		testLengthParserFailure("-.0EE+1", 5);
		testLengthParserFailure("1Eem", 3);
		testLengthParserFailure("1em%", 4);
	}

}
