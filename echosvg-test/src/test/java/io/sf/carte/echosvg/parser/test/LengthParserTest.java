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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.parser.DefaultLengthHandler;
import io.sf.carte.echosvg.parser.LengthParser;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * To test the length parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class LengthParserTest {

	/**
	 * 
	 * @param sourceLength      The length to parse.
	 * @param destinationLength The length after serialization.
	 */
	private void testLengthParser(String sourceLength, String destinationLength) {
		LengthParser pp = new LengthParser();
		TestHandler handler = new TestHandler();
		pp.setLengthHandler(handler);

		pp.parse(new StringReader(sourceLength));

		assertEquals(destinationLength, handler.resultLength);
	}

	@Test
	public void test() throws Exception {
		testLengthParser("123.456", "123.456");

		testLengthParser("123em", "123.0em");

		testLengthParser(".456ex", "0.456ex");

		testLengthParser("-.456789in", "-0.456789in");

		testLengthParser("-456789.cm", "-456789.0cm");

		testLengthParser("-4567890.mm", "-4567890.0mm");

		testLengthParser("-000456789.pc", "-456789.0pc");

		testLengthParser("-0.00456789pt", "-0.00456789pt");

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

	private class TestHandler extends DefaultLengthHandler {

		private StringBuilder buffer;
		String resultLength;

		TestHandler() {
		}

		@Override
		public void startLength() throws ParseException {
			buffer = new StringBuilder();
		}

		@Override
		public void lengthValue(float v) throws ParseException {
			buffer.append(v);
		}

		@Override
		public void em() throws ParseException {
			buffer.append("em");
		}

		@Override
		public void ex() throws ParseException {
			buffer.append("ex");
		}

		@Override
		public void lh() throws ParseException {
			buffer.append("lh");
		}

		@Override
		public void in() throws ParseException {
			buffer.append("in");
		}

		@Override
		public void cm() throws ParseException {
			buffer.append("cm");
		}

		@Override
		public void mm() throws ParseException {
			buffer.append("mm");
		}

		@Override
		public void pc() throws ParseException {
			buffer.append("pc");
		}

		@Override
		public void pt() throws ParseException {
			buffer.append("pt");
		}

		@Override
		public void px() throws ParseException {
			buffer.append("px");
		}

		@Override
		public void percentage() throws ParseException {
			buffer.append("%");
		}

		@Override
		public void rem() throws ParseException {
			buffer.append("rem");
		}

		@Override
		public void rex() throws ParseException {
			buffer.append("rex");
		}

		@Override
		public void rlh() throws ParseException {
			buffer.append("rlh");
		}

		@Override
		public void vh() throws ParseException {
			buffer.append("vh");
		}

		@Override
		public void vw() throws ParseException {
			buffer.append("vw");
		}

		@Override
		public void vmax() throws ParseException {
			buffer.append("vmax");
		}

		@Override
		public void vmin() throws ParseException {
			buffer.append("vmin");
		}

		@Override
		public void endLength() throws ParseException {
			resultLength = buffer.toString();
		}

	}

}
