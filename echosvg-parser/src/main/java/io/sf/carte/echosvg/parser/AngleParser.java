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
 * This class implements an event-based parser for the SVG angle values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AngleParser extends NumberParser {

	/**
	 * The angle handler used to report parse events.
	 */
	private AngleHandler angleHandler;

	public AngleParser(AngleHandler handler) {
		super();
		angleHandler = handler;
	}

	/**
	 * Allows an application to register an angle handler.
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
	 * @param handler The transform list handler.
	 */
	public void setAngleHandler(AngleHandler handler) {
		angleHandler = handler;
	}

	/**
	 * Returns the angle handler in use.
	 */
	public AngleHandler getAngleHandler() {
		return angleHandler;
	}

	/**
	 * Parses the current reader representing an angle.
	 */
	@Override
	protected void doParse() throws ParseException, IOException {
		angleHandler.startAngle();

		current = reader.read();
		skipSpaces();

		try {
			float f = parseFloat();

			angleHandler.angleValue(f);

			s: if (current != -1) {
				switch (current) {
				case 0xD:
				case 0xA:
				case 0x20:
				case 0x9:
					break s;
				}

				switch (current) {
				case 'd':
					current = reader.read();
					if (current != 'e') {
						reportCharacterExpectedError('e', current);
						break;
					}
					current = reader.read();
					if (current != 'g') {
						reportCharacterExpectedError('g', current);
						break;
					}
					angleHandler.deg();
					current = reader.read();
					break;
				case 'g':
					current = reader.read();
					if (current != 'r') {
						reportCharacterExpectedError('r', current);
						break;
					}
					current = reader.read();
					if (current != 'a') {
						reportCharacterExpectedError('a', current);
						break;
					}
					current = reader.read();
					if (current != 'd') {
						reportCharacterExpectedError('d', current);
						break;
					}
					angleHandler.grad();
					current = reader.read();
					break;
				case 'r':
					current = reader.read();
					if (current != 'a') {
						reportCharacterExpectedError('a', current);
						break;
					}
					current = reader.read();
					if (current != 'd') {
						reportCharacterExpectedError('d', current);
						break;
					}
					angleHandler.rad();
					current = reader.read();
					break;
				case 't':
					current = reader.read();
					if (current != 'u') {
						reportCharacterExpectedError('u', current);
						break;
					}
					current = reader.read();
					if (current != 'r') {
						reportCharacterExpectedError('r', current);
						break;
					}
					current = reader.read();
					if (current != 'n') {
						reportCharacterExpectedError('n', current);
						break;
					}
					angleHandler.turn();
					current = reader.read();
					break;
				default:
					reportUnexpectedCharacterError(current);
				}
			}

			skipSpaces();
			if (current != -1) {
				reportError("end.of.stream.expected", new Object[] { current });
			}
		} catch (NumberFormatException e) {
			reportUnexpectedCharacterError(current);
		} catch (CalcParseException e) {
			cssParse(e);
		}
		angleHandler.endAngle();
	}

	@Override
	protected void handleNumber(short unitType, float floatValue) throws ParseException {
		if (!CSSUnit.isAngleUnitType(unitType)) {
			throw new ParseException("Not an angle: " + CSSUnit.dimensionUnitString(unitType), -1, -1);
		}

		angleHandler.angleValue(floatValue);

		switch (unitType) {
		case CSSUnit.CSS_DEG:
			angleHandler.deg();
			break;
		case CSSUnit.CSS_GRAD:
			angleHandler.grad();
			break;
		case CSSUnit.CSS_RAD:
			angleHandler.rad();
			break;
		case CSSUnit.CSS_TURN:
			angleHandler.turn();
			break;
		default:
			reportError("character.unexpected", new Object[] { CSSUnit.dimensionUnitString(unitType) });
		}
	}

	@Override
	protected short getPreferredUnit() {
		return CSSUnit.CSS_DEG;
	}

}
