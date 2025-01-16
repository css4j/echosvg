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
import java.io.InputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSMathFunctionValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSValue;
import io.sf.carte.doc.style.css.nsac.CSSException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.doc.style.css.property.Evaluator;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.doc.style.css.property.ValueList;
import io.sf.carte.echosvg.i18n.LocalizableSupport;
import io.sf.carte.echosvg.util.io.NormalizingReader;
import io.sf.carte.echosvg.util.io.StreamNormalizingReader;
import io.sf.carte.echosvg.util.io.StringNormalizingReader;

/**
 * This class is the superclass of all parsers. It provides localization and
 * error handling methods.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * 
 * @version $Id$
 */
public abstract class AbstractParser implements Parser {

	/**
	 * The default resource bundle base name.
	 */
	public static final String BUNDLE_CLASSNAME = "io.sf.carte.echosvg.parser.resources.Messages";

	/**
	 * The error handler.
	 */
	protected ErrorHandler errorHandler = new DefaultErrorHandler();

	/**
	 * The localizable support.
	 */
	protected LocalizableSupport localizableSupport = new LocalizableSupport(BUNDLE_CLASSNAME,
			AbstractParser.class.getClassLoader());

	/**
	 * The normalizing reader.
	 */
	protected NormalizingReader reader;

	/**
	 * The current character.
	 */
	protected int current;

	/**
	 * Returns the current character value.
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * Implements {@link io.sf.carte.echosvg.i18n.Localizable#setLocale(Locale)}.
	 */
	@Override
	public void setLocale(Locale l) {
		localizableSupport.setLocale(l);
	}

	/**
	 * Implements {@link io.sf.carte.echosvg.i18n.Localizable#getLocale()}.
	 */
	@Override
	public Locale getLocale() {
		return localizableSupport.getLocale();
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.i18n.Localizable#formatMessage(String,Object[])}.
	 */
	@Override
	public String formatMessage(String key, Object[] args) throws MissingResourceException {
		return localizableSupport.formatMessage(key, args);
	}

	/**
	 * Allow an application to register an error event handler.
	 *
	 * <p>
	 * If the application does not register an error event handler, all error events
	 * reported by the parser will cause an exception to be thrown.
	 *
	 * <p>
	 * Applications may register a new or different handler in the middle of a
	 * parse, and the parser must begin using the new handler immediately.
	 * </p>
	 * 
	 * @param handler The error handler.
	 */
	@Override
	public void setErrorHandler(ErrorHandler handler) {
		errorHandler = handler;
	}

	/**
	 * Parses the given reader
	 */
	@Override
	public void parse(Reader r) throws ParseException {
		try {
			reader = new StreamNormalizingReader(r);
			doParse();
		} catch (IOException e) {
			errorHandler.error(new ParseException(createErrorMessage("io.exception", null), e));
		}
	}

	/**
	 * Parses the given input stream. If the encoding is null, ISO-8859-1 is used.
	 */
	public void parse(InputStream is, String enc) throws ParseException {
		try {
			reader = new StreamNormalizingReader(is, enc);
			doParse();
		} catch (IOException e) {
			errorHandler.error(new ParseException(createErrorMessage("io.exception", null), e));
		}
	}

	/**
	 * Parses the given string.
	 */
	@Override
	public void parse(String s) throws ParseException {
		try {
			reader = new StringNormalizingReader(s);
			doParse();
		} catch (IOException e) {
			errorHandler.error(new ParseException(createErrorMessage("io.exception", null), e));
		} catch (CalcParseException e) {
			// This may be handled by parsing and be unneeded
			cssParse();
		}
	}

	/**
	 * Reparse with a CSS parser.
	 * 
	 * @throws ParseException if an I/O error occurs, or the error handler throws
	 *                        one.
	 */
	void cssParse() throws ParseException {
		// Unread 'calc('
		PushbackReader pbre = new PushbackReader(reader, 5);
		char[] cbuf = { 'c', 'a', 'l', 'c', '(' };
		try {
			pbre.unread(cbuf);
		} catch (IOException e) {
			throw new ParseException(e);
		}

		/*
		 * Reparse with a CSS parser
		 */
		CSSParser parser = new CSSParser();
		LexicalUnit lunit;
		try {
			lunit = parser.parsePropertyValue(pbre);
		} catch (CSSException e) {
			// The CSSException could eventually be a budget exception (DoS)
			// but we settle for a syntax error
			DOMException ex = new DOMException(DOMException.SYNTAX_ERR, e.getMessage());
			ex.initCause(e);
			ParseException pex = new ParseException(ex);
			pex.lineNumber = reader.getLine();
			pex.columnNumber = reader.getColumn();
			errorHandler.error(pex);
			return;
		} catch (IOException e) {
			throw new ParseException(e);
		}

		try {
			CSSValue cssvalue = (new ValueFactory()).createCSSValue(lunit);
			handleStyleValue(cssvalue);
		} catch (ParseException pex) {
			pex.lineNumber = reader.getLine();
			pex.columnNumber = reader.getColumn();
			errorHandler.error(pex);
		} catch (Exception ex) {
			// Most likely a DOMException
			ParseException pex = new ParseException(ex);
			pex.lineNumber = reader.getLine();
			pex.columnNumber = reader.getColumn();
			errorHandler.error(pex);
		}

		// The parser should have consumed the stream
		current = -1;
	}

	private void handleStyleValue(CSSValue cssvalue) throws ParseException {
		switch (cssvalue.getCssValueType()) {
		case LIST:
			ValueList list = (ValueList) cssvalue;
			handleListStart(list.isCommaSeparated());
			Iterator<? extends CSSValue> it = list.iterator();
			while (it.hasNext()) {
				handleStyleValue(it.next());
			}
			handleListEnd(list.isCommaSeparated());
			break;
		case TYPED:
			handleTyped((CSSTypedValue) cssvalue);
			break;
		default:
			errorHandler.error(
					new ParseException(createErrorMessage("non.css.context",
							new Object[] { cssvalue.getCssText() }), -1, -1));
		}
	}

	/**
	 * The processing of a list begins.
	 * 
	 * @param commaSeparated {@code true} if the list is comma-separated.
	 */
	protected void handleListStart(boolean commaSeparated) {
	}

	/**
	 * The processing of a list ends.
	 * 
	 * @param commaSeparated {@code true} if the list is comma-separated.
	 */
	protected void handleListEnd(boolean commaSeparated) {
	}

	private void handleTyped(CSSTypedValue cssvalue) throws ParseException {
		switch (cssvalue.getPrimitiveType()) {
		case NUMERIC:
			handleNumber(cssvalue.getUnitType(), cssvalue.getFloatValue(cssvalue.getUnitType()));
			break;
		case EXPRESSION:
			handleMathExpression((CSSExpressionValue) cssvalue);
			break;
		case MATH_FUNCTION:
			handleMathFunction((CSSMathFunctionValue) cssvalue);
			break;
		case IDENT:
			handleIdent(cssvalue.getStringValue());
			break;
		default:
			errorHandler.error(
					new ParseException(createErrorMessage("non.css.context",
							new Object[] { cssvalue.getCssText() }), -1, -1));
		}
	}

	protected void handleNumber(short unitType, float floatValue) throws ParseException {
		errorHandler.error(new ParseException(createErrorMessage("unexpected.value",
				new Object[] { Float.toString(floatValue) + CSSUnit.dimensionUnitString(unitType) }), -1, -1));
	}

	protected void handleMathExpression(CSSExpressionValue cssvalue) throws ParseException {
		Evaluator eval = new Evaluator(getPreferredUnit());
		float floatValue;
		short unitType;

		try {
			CSSTypedValue typed = eval.evaluateExpression(cssvalue);
			unitType = typed.getUnitType();
			if (unitType != getPreferredUnit()) {
				if (getPreferredUnit() == CSSUnit.CSS_NUMBER) {
					throw new ParseException("Invalid unit.", -1, -1);
				} else if (unitType == CSSUnit.CSS_NUMBER) {
					unitType = getPreferredUnit();
				}
			}
			floatValue = typed.getFloatValue(unitType);
		} catch (RuntimeException e) {
			throw new ParseException(e);
		}

		handleNumber(unitType, floatValue);
	}

	protected void handleMathFunction(CSSMathFunctionValue cssvalue) throws ParseException {
		Evaluator eval = new Evaluator(getPreferredUnit());
		float floatValue;
		short unitType;

		try {
			CSSTypedValue typed = eval.evaluateFunction(cssvalue);
			unitType = typed.getUnitType();
			if (unitType != getPreferredUnit()) {
				if (getPreferredUnit() == CSSUnit.CSS_NUMBER) {
					throw new ParseException("Invalid unit.", -1, -1);
				} else if (unitType == CSSUnit.CSS_NUMBER) {
					unitType = getPreferredUnit();
				}
			}
			floatValue = typed.getFloatValue(unitType);
		} catch (RuntimeException e) {
			throw new ParseException(e);
		}

		handleNumber(unitType, floatValue);
	}

	protected short getPreferredUnit() {
		return CSSUnit.CSS_NUMBER;
	}

	/**
	 * Handle an identifier.
	 * <p>
	 * Path commands, for example, would be processed as identifiers.
	 * </p>
	 * 
	 * @param ident the identifier.
	 * @throws ParseException if the identifier is invalid in this parsing context.
	 */
	protected void handleIdent(String ident) throws ParseException {
		errorHandler.error(new ParseException(createErrorMessage("unexpected.identifier",
				new Object[] { ident }), -1, -1));
	}

	/**
	 * Method responsible for actually parsing data after AbstractParser has
	 * initialized itself.
	 */
	protected abstract void doParse() throws ParseException, IOException;

	/**
	 * Check for a possible {@code calc()} value.
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
	 * @throws IOException if an I/O error occurs.
	 */
	void checkForCalc() throws IOException {
		int line = reader.getLine();
		int column = reader.getColumn();

		char[] calcLCRef = { 'a', 'l', 'c', '(' };
		char[] calcUCRef = { 'A', 'L', 'C', '(' };
		char[] calcBuf = new char[4];

		// For performance, ignore how many bytes were read
		reader.read(calcBuf);

		if (equalsAny(calcLCRef, calcUCRef, calcBuf)) {
			handleCalc(line, column);
		} else {
			reportError("character.unexpected", new Object[] { current }, line, column);
		}
	}

	private static boolean equalsAny(char[] lcRef, char[] ucRef, char[] buf) {
		// It's up to the caller to make sure that buf, lcRef and ucRef have the same length
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
	 * Handle a {@code calc()} value.
	 * <p>
	 * The base implementation just throws a {@link CalcParseException}. Although
	 * subclasses may override this method to avoid the overload caused by
	 * exceptions, it is recommended to handle {@code calc()} as an exception so the
	 * associated handling cause a minimum penalty to normal (non-{@code calc()})
	 * processing.
	 * </p>
	 * 
	 * @param line   the line where {@code calc()} was found.
	 * @param column the column where {@code calc()} was found.
	 */
	protected void handleCalc(int line, int column) throws CalcParseException {
		throw new CalcParseException("Cannot handle calc().", line, column);
	}

	private void reportError(String key, Object[] objects, int line, int column) {
		errorHandler.error(new ParseException(createErrorMessage(key, new Object[] { current }),
				line, column));
	}

	/**
	 * Signals an error to the error handler.
	 * 
	 * @param key  The message key in the resource bundle.
	 * @param args The message arguments.
	 */
	protected void reportError(String key, Object[] args) throws ParseException {
		errorHandler.error(new ParseException(createErrorMessage(key, args), reader.getLine(), reader.getColumn()));
	}

	/**
	 * simple api to call often reported error. Just a wrapper for reportError().
	 *
	 * @param expectedChar what caller expected
	 * @param currentChar  what caller found
	 */
	protected void reportCharacterExpectedError(char expectedChar, int currentChar) {
		reportError("character.expected", new Object[] { expectedChar, currentChar });
	}

	/**
	 * simple api to call often reported error. Just a wrapper for reportError().
	 *
	 * @param currentChar what the caller found and didnt expect
	 */
	protected void reportUnexpectedCharacterError(int currentChar) {
		reportError("character.unexpected", new Object[] { currentChar });
	}

	/**
	 * Returns a localized error message.
	 * 
	 * @param key  The message key in the resource bundle.
	 * @param args The message arguments.
	 */
	protected String createErrorMessage(String key, Object[] args) {
		try {
			return formatMessage(key, args);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the resource bundle base name.
	 * 
	 * @return BUNDLE_CLASSNAME.
	 */
	protected String getBundleClassName() {
		return BUNDLE_CLASSNAME;
	}

	/**
	 * Skips the whitespaces in the current reader.
	 */
	protected void skipSpaces() throws IOException {
		for (;;) {
			switch (current) {
			default:
				return;
			case 0x20:
			case 0x09:
			case 0x0D:
			case 0x0A:
			}
			current = reader.read();
		}
	}

	/**
	 * Skips the whitespaces and an optional comma.
	 */
	protected void skipCommaSpaces() throws IOException {
		wsp1: for (;;) {
			switch (current) {
			default:
				break wsp1;
			case 0x20:
			case 0x9:
			case 0xD:
			case 0xA:
			}
			current = reader.read();
		}
		if (current == ',') {
			wsp2: for (;;) {
				switch (current = reader.read()) {
				default:
					break wsp2;
				case 0x20:
				case 0x9:
				case 0xD:
				case 0xA:
				}
			}
		}
	}

}
