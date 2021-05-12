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

import static org.junit.Assert.fail;

import java.io.StringReader;

import org.junit.Test;

/**
 * To test the length parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class LengthParserFailureTest {

	/**
	 * 
	 * 
	 * @param sourceLength The length to parse.
	 */
	private void testLengthParserFailure(String sourceLength) {
		LengthParser pp = new LengthParser();
		try {
			pp.parse(new StringReader(sourceLength));
			fail("Must throw exception for: " + sourceLength);
		} catch (ParseException e) {
		}
	}

	@Test
	public void test() throws Exception {
		testLengthParserFailure("123.456.7");
		testLengthParserFailure("1e+");
		testLengthParserFailure("+e3");
		testLengthParserFailure("1Em");
		testLengthParserFailure("--1");
		testLengthParserFailure("-1E--2");
		testLengthParserFailure("-.E+1");
		testLengthParserFailure("-.0EE+1");
		testLengthParserFailure("1Eem");
		testLengthParserFailure("1em%");
	}

}
