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

import java.io.IOException;

/**
 * This class implements an event-based parser for semicolon separated length
 * pair lists, as used in the 'values' attribute of the 'animateMotion' element.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class LengthPairListParser extends LengthListParser {

	/**
	 * Creates a new LengthPairListParser.
	 * 
	 * @param lap The length array producer.
	 */
	public LengthPairListParser(LengthArrayProducer lap) {
		super(lap);
	}

	/**
	 * Parses the given reader.
	 */
	@Override
	protected void doParse() throws ParseException, IOException {
		((LengthListHandler) lengthHandler).startLengthList();

		current = reader.read();
		skipSpaces();

		try {
			for (;;) {
				lengthHandler.startLength();
				lengthStarted = true;
				parseLength();
				lengthHandler.endLength();
				lengthStarted = false;
				skipCommaSpaces();
				lengthHandler.startLength();
				lengthStarted = true;
				parseLength();
				lengthHandler.endLength();
				lengthStarted = false;
				skipSpaces();
				if (current == -1) {
					break;
				}
				if (current != ';') {
					reportUnexpectedCharacterError(current);
				}
				current = reader.read();
				skipSpaces();
			}
		} catch (NumberFormatException e) {
			reportUnexpectedCharacterError(current);
		} catch (CalcParseException e) {
			cssParse();
		}
		((LengthListHandler) lengthHandler).endLengthList();
	}

}
