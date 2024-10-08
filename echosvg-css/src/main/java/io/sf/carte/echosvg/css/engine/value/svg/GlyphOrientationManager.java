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

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.CalcValue;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.RevertValue;
import io.sf.carte.echosvg.css.engine.value.UnsetValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'glyph-orientation' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class GlyphOrientationManager extends AbstractValueManager {

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#isInheritedProperty()}.
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
		return SVGTypes.TYPE_ANGLE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case DIMENSION:
			switch (lu.getCssUnit()) {
			case CSSUnit.CSS_DEG:
				return new FloatValue(CSSUnit.CSS_DEG, lu.getFloatValue());
			case CSSUnit.CSS_RAD:
				return new FloatValue(CSSUnit.CSS_RAD, lu.getFloatValue());
			case CSSUnit.CSS_GRAD:
				return new FloatValue(CSSUnit.CSS_GRAD, lu.getFloatValue());
			case CSSUnit.CSS_TURN:
				return new FloatValue(CSSUnit.CSS_DEG, lu.getFloatValue() * 360f);
			}
			break;

		// For SVG angle properties unit defaults to 'deg'.
		case INTEGER: {
			int n = lu.getIntegerValue();
			return new FloatValue(CSSUnit.CSS_DEG, n);
		}

		case REAL: {
			float n = lu.getFloatValue();
			return new FloatValue(CSSUnit.CSS_DEG, n);
		}

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
				return calc;
			}
			FloatValue f = ((CalcValue) calc).evaluate(null, null, engine, -1, null, CSSUnit.CSS_DEG);
			if (f.getUnitType() == CSSUnit.CSS_NUMBER) {
				f = new FloatValue(CSSUnit.CSS_DEG, f.getFloatValue());
			}
			return f;

		default:
			break;
		}

		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	@Override
	public Value createFloatValue(short type, float floatValue) throws DOMException {
		switch (type) {
		case CSSUnit.CSS_DEG:
		case CSSUnit.CSS_GRAD:
		case CSSUnit.CSS_RAD:
			return new FloatValue(type, floatValue);
		default:
			float f = NumberValue.floatValueConversion(floatValue, type, CSSUnit.CSS_DEG);
			return new FloatValue(CSSUnit.CSS_DEG, f);
		}
	}

}
