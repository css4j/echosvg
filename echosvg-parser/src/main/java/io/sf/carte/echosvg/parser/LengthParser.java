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

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSMathFunctionValue;

/**
 * This class implements an event-based parser for the SVG length values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class LengthParser extends AbstractParser {

	/**
	 * The length handler used to report parse events.
	 */
	protected LengthHandler lengthHandler;

	/**
	 * Creates a new LengthParser.
	 * 
	 * @param handler The length handler.
	 */
	public LengthParser(LengthHandler handler) {
		lengthHandler = handler;
	}

	/**
	 * Allows an application to register a length handler.
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
	 * @param handler The length handler.
	 */
	public void setLengthHandler(LengthHandler handler) {
		lengthHandler = handler;
	}

	/**
	 * Returns the length handler in use.
	 */
	public LengthHandler getLengthHandler() {
		return lengthHandler;
	}

	@Override
	protected void doParse() throws ParseException, IOException {
		lengthHandler.startLength();

		current = reader.read();
		skipSpaces();

		try {
			parseLength();
		} catch (CalcParseException e) {
			cssParse(e);
		}

		skipSpaces();
		if (current != -1) {
			reportError("end.of.stream.expected", new Object[] { current });
		}
		lengthHandler.endLength();
	}

	/**
	 * Parses a length value.
	 */
	protected void parseLength() throws ParseException, IOException {
		int mant = 0;
		int mantDig = 0;
		boolean mantPos = true;
		boolean mantRead = false;

		int exp = 0;
		int expDig = 0;
		int expAdj = 0;
		boolean expPos = true;

		int unitState = 0;

		switch (current) {
		case '-':
			mantPos = false;
		case '+':
			current = reader.read();
			break;
		case 'c':
		case 'C':
			// calc() ?
			handleCalc();
			return;
		}

		m1: switch (current) {
		default:
			reportUnexpectedCharacterError(current);
			return;

		case '.':
			break;

		case '0':
			mantRead = true;
			l: for (;;) {
				current = reader.read();
				switch (current) {
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					break l;
				default:
					break m1;
				case '0':
				}
			}

		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			mantRead = true;
			l: for (;;) {
				if (mantDig < 9) {
					mantDig++;
					mant = mant * 10 + (current - '0');
				} else {
					expAdj++;
				}
				current = reader.read();
				switch (current) {
				default:
					break l;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				}
			}
		}

		if (current == '.') {
			current = reader.read();
			m2: switch (current) {
			default:
			case 'e':
			case 'E':
				if (!mantRead) {
					reportUnexpectedCharacterError(current);
					return;
				}
				break;

			case '0':
				if (mantDig == 0) {
					l: for (;;) {
						current = reader.read();
						expAdj--;
						switch (current) {
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							break l;
						default:
							break m2;
						case '0':
						}
					}
				}
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				l: for (;;) {
					if (mantDig < 9) {
						mantDig++;
						mant = mant * 10 + (current - '0');
						expAdj--;
					}
					current = reader.read();
					switch (current) {
					default:
						break l;
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
					}
				}
			}
		}

		boolean le = false;
		es: switch (current) {
		case 'e':
			le = true;
		case 'E':
			current = reader.read();
			switch (current) {
			default:
				reportUnexpectedCharacterError(current);
				return;
			case 'm':
				if (!le) {
					reportUnexpectedCharacterError(current);
					return;
				}
				unitState = 1;
				break es;
			case 'x':
				if (!le) {
					reportUnexpectedCharacterError(current);
					return;
				}
				unitState = 2;
				break es;
			case '-':
				expPos = false;
			case '+':
				current = reader.read();
				switch (current) {
				default:
					reportUnexpectedCharacterError(current);
					return;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				}
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			}

			en: switch (current) {
			case '0':
				l: for (;;) {
					current = reader.read();
					switch (current) {
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						break l;
					default:
						break en;
					case '0':
					}
				}

			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				l: for (;;) {
					if (expDig < 3) {
						expDig++;
						exp = exp * 10 + (current - '0');
					}
					current = reader.read();
					switch (current) {
					default:
						break l;
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
					}
				}
			}
		default:
		}

		if (!expPos) {
			exp = -exp;
		}
		exp += expAdj;
		if (!mantPos) {
			mant = -mant;
		}

		lengthHandler.lengthValue(NumberParser.buildFloat(mant, exp));

		switch (unitState) {
		case 1:
			lengthHandler.em();
			current = reader.read();
			return;
		case 2:
			lengthHandler.ex();
			current = reader.read();
			return;
		}

		switch (current) {
		case 'e':
			current = reader.read();
			switch (current) {
			case 'm':
				lengthHandler.em();
				current = reader.read();
				break;
			case 'x':
				lengthHandler.ex();
				current = reader.read();
				break;
			default:
				reportUnexpectedCharacterError(current);
			}
			break;

		case 'p':
			current = reader.read();
			switch (current) {
			case 'c':
				lengthHandler.pc();
				current = reader.read();
				break;
			case 't':
				lengthHandler.pt();
				current = reader.read();
				break;
			case 'x':
				lengthHandler.px();
				current = reader.read();
				break;
			default:
				reportUnexpectedCharacterError(current);
			}
			break;

		case 'i':
			current = reader.read();
			if (current != 'n') {
				reportCharacterExpectedError('n', current);
				break;
			}
			lengthHandler.in();
			current = reader.read();
			break;
		case 'c':
			current = reader.read();
			if (current != 'm') {
				reportCharacterExpectedError('m', current);
				break;
			}
			lengthHandler.cm();
			current = reader.read();
			break;
		case 'l':
			current = reader.read();
			if (current != 'h') {
				reportCharacterExpectedError('l', current);
				break;
			}
			lengthHandler.lh();
			current = reader.read();
			break;
		case 'm':
			current = reader.read();
			if (current != 'm') {
				reportCharacterExpectedError('m', current);
				break;
			}
			lengthHandler.mm();
			current = reader.read();
			break;
		case '%':
			lengthHandler.percentage();
			current = reader.read();
			break;
		case 'Q':
		case 'q':
			lengthHandler.q();
			current = reader.read();
			break;
		case 'r':
			current = reader.read();
			switch (current) {
			case 'e':
				current = reader.read();
				switch (current) {
				case 'm':
					lengthHandler.rem();
					current = reader.read();
					break;
				case 'x':
					lengthHandler.rex();
					current = reader.read();
					break;
				default:
					reportUnexpectedCharacterError(current);
				}
				break;
			case 'l':
				current = reader.read();
				if (current != 'h') {
					reportCharacterExpectedError('h', current);
					break;
				}
				lengthHandler.rlh();
				current = reader.read();
				break;
			default:
				reportUnexpectedCharacterError(current);
			}
			break;
		case 'v':
			current = reader.read();
			switch (current) {
			case 'w':
				lengthHandler.vw();
				current = reader.read();
				break;
			case 'h':
				lengthHandler.vh();
				current = reader.read();
				break;
			case 'm':
				current = reader.read();
				switch (current) {
				case 'i':
					current = reader.read();
					if (current != 'n') {
						reportCharacterExpectedError('n', current);
						break;
					}
					lengthHandler.vmin();
					current = reader.read();
					break;
				case 'a':
					current = reader.read();
					if (current != 'x') {
						reportCharacterExpectedError('x', current);
						break;
					}
					lengthHandler.vmax();
					current = reader.read();
					break;
				default:
					reportUnexpectedCharacterError(current);
				}
				break;
			default:
				reportUnexpectedCharacterError(current);
			}
			break;
		}
	}

	@Override
	protected void handleNumber(short unitType, float floatValue) throws ParseException {
		if (unitType != CSSUnit.CSS_NUMBER && !CSSUnit.isLengthUnitType(unitType)) {
			throw new ParseException("Not a length: " + CSSUnit.dimensionUnitString(unitType), -1, -1);
		}

		lengthHandler.lengthValue(floatValue);

		if (!handleUnit(unitType, lengthHandler)) {
			reportError("character.unexpected", new Object[] { CSSUnit.dimensionUnitString(unitType) });
		}
	}

	static boolean handleUnit(short unitType, LengthHandler lengthHandler) throws ParseException {
		switch (unitType) {
		case CSSUnit.CSS_PX:
			lengthHandler.px();
			break;
		case CSSUnit.CSS_NUMBER:
			// Quirks mode
			break;
		case CSSUnit.CSS_PERCENTAGE:
			lengthHandler.percentage();
			break;
		case CSSUnit.CSS_IN:
			lengthHandler.in();
			break;
		case CSSUnit.CSS_PC:
			lengthHandler.pc();
			break;
		case CSSUnit.CSS_PT:
			lengthHandler.pt();
			break;
		case CSSUnit.CSS_CM:
			lengthHandler.cm();
			break;
		case CSSUnit.CSS_MM:
			lengthHandler.mm();
			break;
		case CSSUnit.CSS_QUARTER_MM:
			lengthHandler.q();
			break;
		case CSSUnit.CSS_EM:
			lengthHandler.em();
			break;
		case CSSUnit.CSS_EX:
			lengthHandler.ex();
			break;
		case CSSUnit.CSS_LH:
			lengthHandler.lh();
			break;
		case CSSUnit.CSS_REM:
			lengthHandler.rem();
			break;
		case CSSUnit.CSS_RLH:
			lengthHandler.rlh();
			break;
		case CSSUnit.CSS_REX:
			lengthHandler.rex();
			break;
		case CSSUnit.CSS_VH:
			lengthHandler.vh();
			break;
		case CSSUnit.CSS_VMAX:
			lengthHandler.vmax();
			break;
		case CSSUnit.CSS_VMIN:
			lengthHandler.vmin();
			break;
		case CSSUnit.CSS_VW:
			lengthHandler.vw();
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	protected short getPreferredUnit() {
		return CSSUnit.CSS_PX;
	}

	@Override
	protected void handleMathExpression(CSSExpressionValue value) throws ParseException {
		lengthHandler.mathExpression(value, getPercentageInterpretation());
	}

	@Override
	protected void handleMathFunction(CSSMathFunctionValue value) throws ParseException {
		lengthHandler.mathFunction(value, getPercentageInterpretation());
	}

	/**
	 * Gets the percentage interpretation that applies, if any.
	 * 
	 * @return the percentage interpretation.
	 */
	protected short getPercentageInterpretation() {
		return UnitProcessor.HORIZONTAL_LENGTH;
	}

}
