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
package io.sf.carte.echosvg.css.engine.value.css;

import java.util.Locale;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValueSyntax;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.CSSValueSyntax.Match;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.parser.SyntaxParser;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSEngineUserAgent;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.RevertValue;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.URIValue;
import io.sf.carte.echosvg.css.engine.value.UnsetValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'cursor' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class CursorManager extends AbstractValueManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap<Value> values = new StringMap<>(18);
	static {
		values.put(CSSConstants.CSS_AUTO_VALUE, ValueConstants.AUTO_VALUE);
		values.put(CSSConstants.CSS_CROSSHAIR_VALUE, ValueConstants.CROSSHAIR_VALUE);
		values.put(CSSConstants.CSS_DEFAULT_VALUE, ValueConstants.DEFAULT_VALUE);
		values.put(CSSConstants.CSS_E_RESIZE_VALUE, ValueConstants.E_RESIZE_VALUE);
		values.put(CSSConstants.CSS_HELP_VALUE, ValueConstants.HELP_VALUE);
		values.put(CSSConstants.CSS_MOVE_VALUE, ValueConstants.MOVE_VALUE);
		values.put(CSSConstants.CSS_N_RESIZE_VALUE, ValueConstants.N_RESIZE_VALUE);
		values.put(CSSConstants.CSS_NE_RESIZE_VALUE, ValueConstants.NE_RESIZE_VALUE);
		values.put(CSSConstants.CSS_NW_RESIZE_VALUE, ValueConstants.NW_RESIZE_VALUE);
		values.put(CSSConstants.CSS_POINTER_VALUE, ValueConstants.POINTER_VALUE);
		values.put(CSSConstants.CSS_S_RESIZE_VALUE, ValueConstants.S_RESIZE_VALUE);
		values.put(CSSConstants.CSS_SE_RESIZE_VALUE, ValueConstants.SE_RESIZE_VALUE);
		values.put(CSSConstants.CSS_SW_RESIZE_VALUE, ValueConstants.SW_RESIZE_VALUE);
		values.put(CSSConstants.CSS_TEXT_VALUE, ValueConstants.TEXT_VALUE);
		values.put(CSSConstants.CSS_W_RESIZE_VALUE, ValueConstants.W_RESIZE_VALUE);
		values.put(CSSConstants.CSS_WAIT_VALUE, ValueConstants.WAIT_VALUE);
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#isAnimatableProperty()}.
	 */
	@Override
	public boolean isAnimatableProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#isAdditiveProperty()}.
	 */
	@Override
	public boolean isAdditiveProperty() {
		return false;
	}

	/**
	 * Implements {@link ValueManager#getPropertyType()}.
	 */
	@Override
	public int getPropertyType() {
		return SVGTypes.TYPE_CURSOR_VALUE;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_CURSOR_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.AUTO_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(final LexicalUnit lunit, CSSEngine engine) throws DOMException {
		switch (lunit.getLexicalUnitType()) {
		case URI: {
			ListValue result = new ListValue();
			return addToURLList(lunit, engine, result);
		}

		case IDENT:
			Value v = createIdentValue(lunit);
			if (v == null) {
				throw createInvalidIdentifierDOMException(lunit.getStringValue());
			}
			ListValue result = new ListValue();
			result.append(v);

			LexicalUnit lu = lunit.getNextLexicalUnit();
			if (lu != null) {
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			}

			return result;

		case INHERIT:
			return ValueConstants.INHERIT_VALUE;

		case UNSET:
			return UnsetValue.getInstance();

		case REVERT:
			return RevertValue.getInstance();

		case INITIAL:
			return getDefaultValue();

		case VAR:
		case ATTR:
			return createLexicalValue(lunit);

		default:
			throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
		}
	}

	private Value addToURLList(LexicalUnit lunit, CSSEngine engine, ListValue result) {
		LexicalUnit lu = lunit;
		LexicalUnit.LexicalType type;
		topLoop: do {
			type = lu.getLexicalUnitType();
			switch (type) {
			case URI: {
				Value v = createURIValue(lu, engine);
				if (v == null) {
					throw createMalformedLexicalUnitDOMException();
				}
				if (v.getPrimitiveType() == Type.LEXICAL) {
					return createLexicalValue(lunit);
				}
				result.append(v);
				break;
			}
			case IDENT:
				Value ident = createIdentValue(lu);
				if (ident != null) {
					result.add(ident);
				} else {
					CSSEngineUserAgent ua = engine.getCSSEngineUserAgent();
					if (ua != null) {
						ua.displayMessage("Unknown identifier: " + lu.getStringValue());
					}
				}
				lu = lu.getNextLexicalUnit();
				if (lu != null) {
					throw createMalformedLexicalUnitDOMException();
				}
				break topLoop;

			case INTEGER:
				result.add(new FloatValue(CSSUnit.CSS_NUMBER, lu.getIntegerValue()));
				break;

			case REAL:
				result.add(new FloatValue(CSSUnit.CSS_NUMBER, lu.getFloatValue()));
				break;

			case DIMENSION:
				short unit = lu.getCssUnit();
				if (CSSUnit.isLengthUnitType(unit) || unit == CSSUnit.CSS_OTHER) {
					result.add(new FloatValue(unit, lu.getFloatValue()));
				} else {
					throw createMalformedLexicalUnitDOMException();
				}
				break;

			case CALC:
				Value calc = createCalc(lu);

				CSSValueSyntax syn = new SyntaxParser().parseSyntax("<number> | <length>");
				if (calc.matches(syn) == Match.FALSE) {
					throw createDOMException(calc);
				}

				result.append(calc);
				break;

			case MATH_FUNCTION: {
				Value v;
				try {
					v = createMathFunction(lu, "<number> | <length>");
				} catch (Exception e) {
					DOMException ife = createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
					ife.initCause(e);
					throw ife;
				}
				result.append(v);
				break;
			}
			case VAR:
			case ATTR:
				return createLexicalValue(lunit);

			case OPERATOR_COMMA:
				lu = lu.getNextLexicalUnit();
				if (lu == null) {
					throw createMalformedLexicalUnitDOMException();
				}
				continue;

			default:
				throw createInvalidLexicalUnitDOMException(type);
			}

			lu = lu.getNextLexicalUnit();
			if (lu == null) {
				throw createMalformedLexicalUnitDOMException();
			}
		} while (true);

		return result;
	}

	private Value createIdentValue(LexicalUnit lunit) {
		String s = lunit.getStringValue().toLowerCase(Locale.ROOT).intern();
		return values.get(s);
	}

	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.getCssValueType() == Value.CssType.LIST) {
			int len = value.getLength();
			ListValue result = new ListValue(' ');
			for (int i = 0; i < len; i++) {
				Value v = value.item(i);
				if (v.getPrimitiveType() == Type.URI) {
					if (sm.isAttrTainted(idx)) {
						CSSEngineUserAgent ua = engine.getCSSEngineUserAgent();
						if (ua != null) {
							ua.warn("attr()-tainted value: " + value.getCssText());
						}
						return null;
					}

					// For performance, use the absolute value as the cssText now.
					String uri = v.getURIValue();
					result.append(new URIValue(uri, uri));
				} else {
					result.append(v);
				}
			}
			return result;
		}

		return super.computeValue(elt, pseudo, engine, idx, sm, value);
	}

}
