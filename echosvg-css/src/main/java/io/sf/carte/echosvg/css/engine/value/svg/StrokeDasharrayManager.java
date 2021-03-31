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
package io.sf.carte.echosvg.css.engine.value.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.LengthManager;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a factory for the 'stroke-dasharray' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class StrokeDasharrayManager extends LengthManager {

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
		return SVGTypes.TYPE_LENGTH_LIST_OR_IDENT;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_STROKE_DASHARRAY_PROPERTY;
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
			throw createInvalidIdentifierDOMException(lu.getStringValue());

		default:
			ListValue lv = new ListValue(' ');
			do {
				Value v = super.createValue(lu, engine);
				lv.append(v);
				lu = lu.getNextLexicalUnit();
				if (lu != null && lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA) {
					lu = lu.getNextLexicalUnit();
				}
			} while (lu != null);
			return lv;
		}
	}

	/**
	 * Implements {@link ValueManager#createStringValue(short,String,CSSEngine)}.
	 */
	@Override
	public Value createStringValue(short type, String value, CSSEngine engine) throws DOMException {
		if (type != CSSPrimitiveValue.CSS_IDENT) {
			throw createInvalidStringTypeDOMException(type);
		}
		if (value.equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
			return ValueConstants.NONE_VALUE;
		}
		throw createInvalidIdentifierDOMException(value);
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		switch (value.getCssValueType()) {
		case CSSValue.CSS_PRIMITIVE_VALUE:
			return value;
		}

		ListValue lv = (ListValue) value;
		ListValue result = new ListValue(' ');
		for (int i = 0; i < lv.getLength(); i++) {
			result.append(super.computeValue(elt, pseudo, engine, idx, sm, lv.item(i)));
		}
		return result;
	}

	/**
	 * Indicates the orientation of the property associated with this manager.
	 */
	@Override
	protected int getOrientation() {
		return BOTH_ORIENTATION;
	}
}
