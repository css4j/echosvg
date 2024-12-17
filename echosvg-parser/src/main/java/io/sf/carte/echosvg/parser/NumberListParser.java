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

import org.w3c.css.om.unit.CSSUnit;

/**
 * This class implements an event-based parser for the SVG Number list values.
 *
 * <p>
 * Original author: tonny@kiyut.com.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class NumberListParser extends NumberParser {

	/**
	 * The number list handler used to report parse events.
	 */
	private NumberListHandler numberListHandler;

	private boolean numberStarted = false;

	/**
	 * Creates a new instance of NumberListParser
	 * 
	 * @param handler The number list handler.
	 */
	public NumberListParser(NumberListHandler handler) {
		numberListHandler = handler;
	}

	/**
	 * Allows an application to register a number list handler.
	 *
	 * <p>
	 * If the application does not register a handler, all events reported by the
	 * parser will be silently ignored.
	 *
	 * <p>
	 * Applications may register a new or different handler in the middle of a
	 * parse, and the parser must begin using the new handler immediately.
	 * </p>
	 * 
	 * @param handler The number list handler.
	 */
	public void setNumberListHandler(NumberListHandler handler) {
		numberListHandler = handler;
	}

	/**
	 * Returns the number list handler in use.
	 */
	public NumberListHandler getNumberListHandler() {
		return numberListHandler;
	}

	/**
	 * Parses the given reader.
	 */
	@Override
	protected void doParse() throws ParseException, IOException {
		numberListHandler.startNumberList();

		current = reader.read();
		skipSpaces();

		try {
			for (;;) {
				numberListHandler.startNumber();
				numberStarted = true;
				float f = parseFloat();
				numberListHandler.numberValue(f);
				numberListHandler.endNumber();
				numberStarted = false;
				skipCommaSpaces();
				if (current == -1) {
					break;
				}
			}
		} catch (NumberFormatException e) {
			reportUnexpectedCharacterError(current);
		}
		numberListHandler.endNumberList();
	}

	@Override
	protected void handleNumber(short unitType, float floatValue) throws ParseException {
		if (unitType != CSSUnit.CSS_NUMBER) {
			throw new ParseException(createErrorMessage("dimension.not.number",
					new Object[] { CSSUnit.dimensionUnitString(unitType) }), -1, -1);
		}

		if (!numberStarted) {
			numberListHandler.startNumber();
		}

		numberListHandler.numberValue(floatValue);
		numberListHandler.endNumber();
		numberStarted = false;
	}

}
