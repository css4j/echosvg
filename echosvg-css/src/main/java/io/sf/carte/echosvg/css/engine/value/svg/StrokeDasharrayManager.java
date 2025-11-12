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

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSEngineUserAgent;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.CSSProxyValueException;
import io.sf.carte.echosvg.css.engine.value.LengthManager;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.RevertValue;
import io.sf.carte.echosvg.css.engine.value.UnsetValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a factory for the 'stroke-dasharray' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
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
	public Value createValue(final LexicalUnit lunit, CSSEngine engine) throws DOMException {
		switch (lunit.getLexicalUnitType()) {
		case IDENT:
			if (lunit.getStringValue().equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
				return ValueConstants.NONE_VALUE;
			}
			throw createInvalidIdentifierDOMException(lunit.getStringValue());

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
			/*
			 * "A list of comma and/or white space separated <length>s and <percentage>s
			 * that specify the lengths of alternating dashes and gaps."
			 */
			ListValue lv = new ListValue(' ');
			LexicalUnit lu = lunit;
			try {
				do {
					Value v = super.createValue(lu, engine);
					lv.append(v);
					lu = lu.getNextLexicalUnit();
					if (lu != null
							&& lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA) {
						lu = lu.getNextLexicalUnit();
					}
				} while (lu != null);
			} catch (CSSProxyValueException e) {
				return createLexicalValue(lunit);
			}
			return lv;
		}
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx,
			StyleMap sm, Value value) {
		switch (value.getCssValueType()) {
		case TYPED:
			return value;
		case LIST:
			ListValue result = new ListValue(' ');
			for (int i = 0; i < value.getLength(); i++) {
				// <dasharray> = [ [ <length-percentage> | <number> ]+ ]#
				Value v = computeTypedValue(elt, pseudo, engine, idx, sm, value.item(i));
				// "If any value in the list is negative, the <dasharray> value is invalid"
				if (v == null || v.getFloatValue() < 0f) {
					CSSEngineUserAgent ua = engine.getCSSEngineUserAgent();
					if (ua != null) {
						ua.displayMessage(
								"Invalid " + getPropertyName() + " value: " + value.getCssText());
					}
					return null;
				}
				result.append(v);
			}
			/*
			 * If the list has an odd number of values, then it is repeated to yield an even
			 * number of values.
			 */
			int len = result.getLength();
			if ((len & 1) != 0) {
				for (int i = 0; i < len; i++) {
					result.add(result.item(i).clone());
				}
			}
			return result;
		default:
			throw createDOMException();
		}

	}

	@Override
	public Value computeTypedValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx,
			StyleMap sm, Value value) {
		return super.computeValue(elt, pseudo, engine, idx, sm, value);
	}

	/**
	 * Indicates the orientation of the property associated with this manager.
	 */
	@Override
	protected int getOrientation() {
		return BOTH_ORIENTATION;
	}

}
