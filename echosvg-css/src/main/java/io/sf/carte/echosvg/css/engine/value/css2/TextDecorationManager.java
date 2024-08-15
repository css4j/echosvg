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
package io.sf.carte.echosvg.css.engine.value.css2;

import java.util.Locale;

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.dom.CSSValue.Type;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'text-decoration' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class TextDecorationManager extends AbstractValueManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_BLINK_VALUE, ValueConstants.BLINK_VALUE);
		values.put(CSSConstants.CSS_LINE_THROUGH_VALUE, ValueConstants.LINE_THROUGH_VALUE);
		values.put(CSSConstants.CSS_OVERLINE_VALUE, ValueConstants.OVERLINE_VALUE);
		values.put(CSSConstants.CSS_UNDERLINE_VALUE, ValueConstants.UNDERLINE_VALUE);
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
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
		return SVGTypes.TYPE_IDENT_LIST;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_TEXT_DECORATION_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.NONE_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case INHERIT:
			return ValueConstants.INHERIT_VALUE;

		case IDENT:
			if (lu.getStringValue().equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
				return ValueConstants.NONE_VALUE;
			}
			ListValue lv = new ListValue(' ');
			do {
				if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.IDENT) {
					String s = lu.getStringValue().toLowerCase(Locale.ROOT).intern();
					Object obj = values.get(s);
					if (obj == null) {
						throw createInvalidIdentifierDOMException(lu.getStringValue());
					}
					lv.append((Value) obj);
					lu = lu.getNextLexicalUnit();
				} else {
					throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
				}

			} while (lu != null);
			return lv;

		default:
			break;
		}
		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

	@Override
	public Value createStringValue(Type type, String value, CSSEngine engine) throws DOMException {
		if (type != Type.IDENT) {
			throw createInvalidStringTypeDOMException(type);
		}
		if (!value.equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
			throw createInvalidIdentifierDOMException(value);
		}
		return ValueConstants.NONE_VALUE;
	}

}
