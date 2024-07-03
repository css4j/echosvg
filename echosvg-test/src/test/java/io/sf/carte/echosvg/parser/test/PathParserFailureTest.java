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
package io.sf.carte.echosvg.parser.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.parser.PathParser;

/**
 * To test the path parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PathParserFailureTest {

	/**
	 * 
	 * @param sourcePath The path to parse.
	 */
	private void testPathParserFailure(String sourcePath) {
		PathParser pp = new PathParser();
		try {
			pp.parse(new StringReader(sourcePath));
			fail("Must throw exception for: " + sourcePath);
		} catch (Exception e) {
		}
	}

	@Test
	public void test() throws Exception {
		testPathParserFailure("m 1ee2 3");

		testPathParserFailure("m 1e4e2 3");

		testPathParserFailure("m 1e+ 2");

		testPathParserFailure("m 1 l 3 4");

		testPathParserFailure("m 1.5.6.7 l 3 4");

		testPathParserFailure("m 1.5,6.7,l 3 4");

		testPathParserFailure("m 1.5,6.7,L 3 4");

		testPathParserFailure("m 1.5,6.7,h 3");

		testPathParserFailure("m 1.5,6.7,H 3");

		testPathParserFailure("m 1.5,6.7,v 3");

		testPathParserFailure("m 1.5,6.7,V 3");

		testPathParserFailure("m 1.5,6.7,c 1,2 3,4 5,6");

		testPathParserFailure("m 1.5,6.7,C 1,2 3,4 5,6");

		testPathParserFailure("m 1.5,6.7,s 1,2 3,4");

		testPathParserFailure("m 1.5,6.7,S 1,2 3,4");

		testPathParserFailure("m 1.5,6.7,q 1,2 3,4");

		testPathParserFailure("m 1.5,6.7,Q 1,2 3,4");

		testPathParserFailure("m 1.5,6.7,t 1,2");

		testPathParserFailure("m 1.5,6.7,T 1,2");

		testPathParserFailure("m 1.5,6.7,a 2,2 0 1 1 2 2");

		testPathParserFailure("m 1.5,6.7,A 4,4 0 1 1 2 2");

		// Check for double path commands
		testPathParserFailure("m m 1,2");

		testPathParserFailure("M M 1,2");

		testPathParserFailure("m 1,2 l l 3,4 z");

		testPathParserFailure("m 1,2 L L 3,4 z");

		testPathParserFailure("m 1,2 h h 3 z");

		testPathParserFailure("m 1,2 H H 3 z");

		testPathParserFailure("m 1,2 v v 3 z");

		testPathParserFailure("m 1,2 V V 3 z");

		testPathParserFailure("m 1,2 c c 1,2 3,4 5,6z");

		testPathParserFailure("m 1,2 C C 1,2 3,4 5,6 z");

		testPathParserFailure("m 1,2 s s 1,2 3,4 z");

		testPathParserFailure("m 1,2 S S 1,2 3,4 z");

		testPathParserFailure("m 1,2 q q 1,2 3,4 z");

		testPathParserFailure("m 1,2 Q Q 1,2 3,4 z");

		testPathParserFailure("m 1,2 t t 1,2 z");

		testPathParserFailure("m 1,2 T T 1,2 z");

		testPathParserFailure("m 1.5,6.7 a a 2,2 0 1 1 2 2");

		testPathParserFailure("m 1.5,6.7 A A 4,4 0 1 1 2 2");
	}

}
