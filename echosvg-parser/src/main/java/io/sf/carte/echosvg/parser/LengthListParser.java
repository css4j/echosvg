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

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSMathFunctionValue;

/**
 * This class implements an event-based parser for the SVG length list values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class LengthListParser extends LengthParser {

	boolean lengthStarted = false;

	/**
	 * Creates a new LengthListParser.
	 * 
	 * @param handler The length list handler.
	 */
	public LengthListParser(LengthListHandler handler) {
		super(handler);
	}

	/**
	 * Allows an application to register a length list handler.
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
	 * @param handler The length list handler.
	 */
	public void setLengthListHandler(LengthListHandler handler) {
		lengthHandler = handler;
	}

	/**
	 * Returns the length list handler in use.
	 */
	public LengthListHandler getLengthListHandler() {
		return (LengthListHandler) lengthHandler;
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
				if (current == -1) {
					break;
				}
			}
		} catch (CalcParseException e) {
			cssParse();
		} catch (NumberFormatException e) {
			reportUnexpectedCharacterError(current);
		}

		((LengthListHandler) lengthHandler).endLengthList();
	}

	@Override
	protected void handleNumber(short unitType, float floatValue) throws ParseException {
		if (!lengthStarted) {
			lengthHandler.startLength();
		}

		super.handleNumber(unitType, floatValue);

		lengthHandler.endLength();
		lengthStarted = false;
	}

	@Override
	protected void handleMathExpression(CSSExpressionValue value) throws ParseException {
		if (!lengthStarted) {
			lengthHandler.startLength();
		}

		super.handleMathExpression(value);

		lengthHandler.endLength();
		lengthStarted = false;
	}

	@Override
	protected void handleMathFunction(CSSMathFunctionValue value) throws ParseException {
		if (!lengthStarted) {
			lengthHandler.startLength();
		}

		super.handleMathFunction(value);

		lengthHandler.endLength();
		lengthStarted = false;
	}

}
