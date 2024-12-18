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
 * This class represents a parser with support for numbers.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class NumberParser extends AbstractParser {

	/**
	 * Parses the content of the buffer and converts it to a float.
	 */
	protected float parseFloat() throws ParseException, IOException {
		int mant = 0;
		int mantDig = 0;
		boolean mantPos = true;
		boolean mantRead = false;

		int exp = 0;
		int expDig = 0;
		int expAdj = 0;
		boolean expPos = true;

		switch (current) {
		case 'c':
		case 'C':
			// calc() ?
			return handleCalc();
		case '-':
			mantPos = false;
			// fallthrough
		case '+':
			current = reader.read();
		}

		m1: switch (current) {
		default:
			reportUnexpectedCharacterError(current);
			return 0.0f;

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
				case '.':
				case 'e':
				case 'E':
					break m1;
				default:
					return 0.0f;
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
					return 0.0f;
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
							if (!mantRead) {
								return 0.0f;
							}
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

		switch (current) {
		case 'e':
		case 'E':
			current = reader.read();
			switch (current) {
			default:
				reportUnexpectedCharacterError(current);
				return 0f;
			case '-':
				expPos = false;
			case '+':
				current = reader.read();
				switch (current) {
				default:
					reportUnexpectedCharacterError(current);
					return 0f;
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

		return buildFloat(mant, exp);
	}

	/**
	 * Computes a float from mantissa and exponent.
	 */
	public static float buildFloat(int mant, int exp) {
		if (exp < -125 || mant == 0) {
			return 0.0f;
		}

		if (exp >= 128) {
			return (mant > 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
		}

		if (exp == 0) {
			return mant;
		}

		if (mant >= (1 << 26)) {
			mant++; // round up trailing bits if they will be dropped.
		}

		return (float) ((exp > 0) ? mant * pow10[exp] : mant / pow10[-exp]);
	}

	/**
	 * Array of powers of ten. Using double instead of float gives a tiny bit more
	 * precision.
	 */
	private static final double[] pow10 = new double[128];

	static {
		for (int i = 0; i < pow10.length; i++) {
			pow10[i] = Math.pow(10, i);
		}
	}

	/**
	 * Handle a possible {@code calc()} value.
	 * <p>
	 * Any handling of numbers must deal with calc().
	 * </p>
	 * <p>
	 * From https://www.w3.org/TR/css3-values/#funcdef-calc
	 * </p>
	 * <p>
	 * [calc()] "can be used wherever [...] &lt;number&gt; or &lt;integer&gt; values
	 * are allowed."
	 * </p>
	 * 
	 * @return 0.
	 * @throws IOException if an I/O error occurs.
	 */
	private float handleCalc() throws IOException {
		int line = reader.getLine();
		int column = reader.getColumn();

		char[] calcLCRef = { 'a', 'l', 'c', '(' };
		char[] calcUCRef = { 'A', 'L', 'C', '(' };
		char[] calcBuf = new char[4];

		// For performance, ignore the number of bytes read
		reader.read(calcBuf);

		if (equalsAny(calcLCRef, calcUCRef, calcBuf)) {
			handleCalc(line, column);
		} else {
			reportError("character.unexpected", new Object[] { current }, line, column);
		}

		return 0.0f;
	}

	private static boolean equalsAny(char[] lcRef, char[] ucRef, char[] buf) {
		// The caller must make sure that buf, lcRef and ucRef have the same length
		assert lcRef.length == buf.length;

		for (int i = 0; i < lcRef.length; i++) {
			char c = buf[i];
			if (c != lcRef[i] && c != ucRef[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Report that we found a calc() value.
	 * 
	 * @param line   the line number.
	 * @param column the column number.
	 */
	protected void handleCalc(int line, int column) {
		reportError("character.unexpected", new Object[] { 'c' }, line, column);
	}

	private void reportError(String key, Object[] args, int line, int column) {
		errorHandler.error(new ParseException(createErrorMessage(key, args), line, column));
	}

}
