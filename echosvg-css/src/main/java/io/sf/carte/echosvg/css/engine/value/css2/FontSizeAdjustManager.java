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

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.CalcValue;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.NumericDelegateValue;
import io.sf.carte.echosvg.css.engine.value.RevertValue;
import io.sf.carte.echosvg.css.engine.value.UnsetValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'font-size-adjust' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class FontSizeAdjustManager extends AbstractValueManager {

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
		return SVGTypes.TYPE_FONT_SIZE_ADJUST_VALUE;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_SIZE_ADJUST_PROPERTY;
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
		case INTEGER:
			return new FloatValue(CSSUnit.CSS_NUMBER, lu.getIntegerValue());

		case REAL:
			return new FloatValue(CSSUnit.CSS_NUMBER, lu.getFloatValue());

		case IDENT:
			if (lu.getStringValue().equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
				return ValueConstants.NONE_VALUE;
			}
			throw createInvalidIdentifierDOMException(lu.getStringValue());

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
			return createLexicalValue(lu);

		case CALC:
			Value calc = createCalc(lu);
			if (calc.getPrimitiveType() != Type.EXPRESSION) {
				// In principle this means that a var() was found
				return calc;
			}
			return ((CalcValue) calc).evaluate(null, null, engine, -1, null, CSSUnit.CSS_NUMBER);

		case FUNCTION:
			Value v;
			try {
				v = createMathFunction(lu, "<number>");
			} catch (Exception e) {
				DOMException ife = createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
				ife.initCause(e);
				throw ife;
			}
			return ((NumericDelegateValue<?>) v).evaluate(null, null, engine, -1, null, CSSUnit.CSS_NUMBER);

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
		if (value.equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
			return ValueConstants.NONE_VALUE;
		}
		throw createInvalidIdentifierDOMException(value);
	}

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	@Override
	public Value createFloatValue(short type, float floatValue) throws DOMException {
		if (type == CSSUnit.CSS_NUMBER) {
			return new FloatValue(type, floatValue);
		}
		throw createInvalidFloatTypeDOMException(type);
	}

	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		return value;
	}

}
