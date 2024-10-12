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

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.IdentValue;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.RevertValue;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.StringValue;
import io.sf.carte.echosvg.css.engine.value.UnsetValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a factory for the 'font-family' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class FontFamilyManager extends AbstractValueManager {

	/**
	 * The default value.
	 */
	protected static final ListValue DEFAULT_VALUE = createDefaultValue();

	private static ListValue createDefaultValue() {
		ListValue def = new ListValue(',', 4);
		def.append(new StringValue("Arial"));
		def.append(new StringValue("Helvetica"));
		def.append(IdentValue.createConstant(CSSConstants.CSS_SANS_SERIF_VALUE));
		return def.createUnmodifiableView();
	}

	/**
	 * The identifier values.
	 */
	protected static final StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_CURSIVE_VALUE, ValueConstants.CURSIVE_VALUE);
		values.put(CSSConstants.CSS_FANTASY_VALUE, ValueConstants.FANTASY_VALUE);
		values.put(CSSConstants.CSS_MONOSPACE_VALUE, ValueConstants.MONOSPACE_VALUE);
		values.put(CSSConstants.CSS_SERIF_VALUE, ValueConstants.SERIF_VALUE);
		values.put(CSSConstants.CSS_SANS_SERIF_VALUE, ValueConstants.SANS_SERIF_VALUE);
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
		return SVGTypes.TYPE_FONT_FAMILY_VALUE;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_FAMILY_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return DEFAULT_VALUE;
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
		}
		ListValue result = new ListValue();
		LexicalUnit lu = lunit;
		for (;;) {
			switch (lu.getLexicalUnitType()) {
			case STRING:
				result.append(new StringValue(lu.getStringValue()));
				lu = lu.getNextLexicalUnit();
				break;

			case IDENT:
				StringBuilder sb = new StringBuilder(lu.getStringValue());
				lu = lu.getNextLexicalUnit();
				if (lu != null && isIdentOrNumber(lu)) {
					do {
						sb.append(' ');
						switch (lu.getLexicalUnitType()) {
						case IDENT:
							sb.append(lu.getStringValue());
							break;
						case INTEGER:
							// Some font names contain integer values but are not quoted!
							// Example: "Univers 45 Light"
							sb.append(Integer.toString(lu.getIntegerValue()));
						default:
							break;
						}
						lu = lu.getNextLexicalUnit();
					} while (lu != null && isIdentOrNumber(lu));
					result.append(new StringValue(sb.toString()));
				} else {
					String id = sb.toString();
					String s = id.toLowerCase(Locale.ROOT).intern();
					Value v = (Value) values.get(s);
					result.append((v != null) ? v : new StringValue(id));
				}
				break;

			case VAR:
			case ATTR:
				return createLexicalValue(lunit);

			default:
			}
			if (lu == null) {
				return result;
			}
			if (lu.getLexicalUnitType() != LexicalUnit.LexicalType.OPERATOR_COMMA) {
				if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.VAR) {
					return createLexicalValue(lunit);
				}
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			}
			lu = lu.getNextLexicalUnit();
			if (lu == null)
				throw createMalformedLexicalUnitDOMException();
		}
	}

	private boolean isIdentOrNumber(LexicalUnit lu) {
		LexicalType type = lu.getLexicalUnitType();
		return type == LexicalType.IDENT || type == LexicalType.INTEGER;
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value == DEFAULT_VALUE) {
			CSSContext ctx = engine.getCSSContext();
			value = ctx.getDefaultFontFamily();
		}
		return value;
	}

}
