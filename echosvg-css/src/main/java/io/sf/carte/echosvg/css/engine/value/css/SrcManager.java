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

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSEngineUserAgent;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.RevertValue;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.StringValue;
import io.sf.carte.echosvg.css.engine.value.URIValue;
import io.sf.carte.echosvg.css.engine.value.UnsetValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 * <p>
 * Original author: <a href="mailto:deweese@apache.org">l449433</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SrcManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap<Value> values = new StringMap<>(3);
	static {
		values.put(CSSConstants.CSS_NONE_VALUE, ValueConstants.NONE_VALUE);
	}

	public SrcManager() {
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return false;
	}

	/**
	 * Implements {@link ValueManager#isAnimatableProperty()}.
	 */
	@Override
	public boolean isAnimatableProperty() {
		return false;
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
		return SVGTypes.TYPE_FONT_DESCRIPTOR_SRC_VALUE;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_SRC_PROPERTY;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.NONE_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(final LexicalUnit lunit, CSSEngine engine) throws DOMException {

		switch (lunit.getLexicalUnitType()) {
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

		case IDENT:
		case STRING:
		case URI:
		case SRC:
		}

		ListValue result = new ListValue();
		LexicalUnit lu = lunit;
		for (;;) {
			switch (lu.getLexicalUnitType()) {
			case STRING:
				result.append(new StringValue(lu.getStringValue()));
				lu = lu.getNextLexicalUnit();
				break;

			case URI:
				String sv = lu.getStringValue();
				String uri = resolveURI(engine.getCSSBaseURI(), sv);
				result.append(new URIValue(sv, uri));
				lu = lu.getNextLexicalUnit();
				if (lu != null) {
					switch (lu.getLexicalUnitType()) {
					case FUNCTION:
						if (!lu.getFunctionName().equalsIgnoreCase("format")) {
							break;
						}
						// Format really does us no good so just ignore it.

						// TODO: Should probably turn this into a ListValue
						// and append the format function CSS Value.
						lu = lu.getNextLexicalUnit();
						break;
					case VAR:
						// Skip ATTR just in case
						return createLexicalValue(lunit);
					default:
						break;
					}
				}
				break;

			case SRC:
				LexicalUnit param = lu.getParameters();
				paramLoop: while (param != null) {
					switch (param.getLexicalUnitType()) {
					case STRING:
						String s = param.getStringValue();
						uri = resolveURI(engine.getCSSBaseURI(), s);
						result.append(new URIValue(s, uri));
						break paramLoop;
					case VAR:
						// Skip ATTR just in case
						return createLexicalValue(lunit);
					case FUNCTION:
					case IDENT:
						// url-modifiers
						break;
					default:
						throw createInvalidLexicalUnitDOMException(param.getLexicalUnitType());
					}
					param = param.getNextLexicalUnit();
				}
				lu = lu.getNextLexicalUnit();
				break;

			case IDENT:
				StringBuilder sb = new StringBuilder(lu.getStringValue());
				lu = lu.getNextLexicalUnit();
				if (lu != null) {
					switch (lu.getLexicalUnitType()) {
					case IDENT:
						do {
							sb.append(' ');
							sb.append(lu.getStringValue());
							lu = lu.getNextLexicalUnit();
						} while (lu != null && lu.getLexicalUnitType() == LexicalType.IDENT);
						result.append(new StringValue(sb.toString()));
						break;
					case VAR:
					case ATTR:
						return createLexicalValue(lunit);
					default:
						String id = sb.toString();
						String s = id.toLowerCase(Locale.ROOT).intern();
						Value v = values.get(s);
						result.append(v != null ? v : new StringValue(id));
					}
				}
				break;

			case VAR:
			case ATTR:
				return createLexicalValue(lunit);

			default:
				break;

			}

			if (lu == null) {
				return result;
			}

			LexicalType type = lu.getLexicalUnitType();
			if (type != LexicalType.OPERATOR_COMMA) {
				if (type == LexicalType.VAR || type == LexicalType.ATTR) {
					return createLexicalValue(lunit);
				}
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			}

			lu = lu.getNextLexicalUnit();

			if (lu == null) {
				throw createMalformedLexicalUnitDOMException();
			}
		}
	}

	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (sm.isAttrTainted(idx) && value.getCssValueType() == CSSValue.CssType.LIST) {
			for (Value v : (ListValue) value) {
				if (v.getPrimitiveType() == Type.URI) {
					CSSEngineUserAgent ua = engine.getCSSEngineUserAgent();
					if (ua != null) {
						ua.displayMessage("attr()-tainted value: " + value.getCssText());
					}
					return null;
				}
			}
		}
		return super.computeValue(elt, pseudo, engine, idx, sm, value);
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap<Value> getIdentifiers() {
		return values;
	}

}
