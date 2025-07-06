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
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.css.om.typed.CSSUnitValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.DOMInvalidAccessException;
import io.sf.carte.doc.style.css.CSSMathValue;
import io.sf.carte.doc.style.css.CSSNumberValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSValueSyntax;
import io.sf.carte.doc.style.css.CSSValueSyntax.Match;
import io.sf.carte.doc.style.css.property.Evaluator;
import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;

/**
 * Base class for numeric function or expression values.
 *
 * @author See Git history.
 * @version $Id$
 */
public abstract class NumericDelegateValue<D extends CSSMathValue> extends NumericValue {

	private static final long serialVersionUID = 1L;

	private D delegate;

	/**
	 * Creates a new value.
	 */
	protected NumericDelegateValue(D expr) {
		super();
		this.delegate = expr;
	}

	@Override
	public String getCssText() {
		return delegate.getCssText();
	}

	@Override
	short getCSSUnit() {
		return delegate.computeUnitType();
	}

	public D getNumericDelegate() {
		return delegate;
	}

	@Override
	public Match matches(CSSValueSyntax syntax) {
		return delegate.matches(syntax);
	}

	public FloatValue evaluate(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			final short unit) throws DOMException {
		Evaluator eval = new Evaluator(unit) {

			@Override
			protected CSSNumberValue absoluteTypedValue(CSSTypedValue typed) {
				if (typed.getPrimitiveType() == Type.NUMERIC) {
					short unitType = typed.getUnitType();
					if (CSSUnit.isRelativeLengthUnitType(unitType)) {
						FloatValue relative = new FloatValue(unitType, typed.getFloatValue());
						FloatValue abs = NumericDelegateValue.this.absoluteValue(elt, pseudo, engine, idx, sm,
								relative);
						short u;
						if (abs.getUnitType() != CSSUnit.CSS_NUMBER) {
							u = abs.getUnitType();
						} else {
							u = unit;
						}
						return NumberValue.createCSSNumberValue(u, abs.getFloatValue());
					}
					return (CSSNumberValue) typed;
				}
				throw new DOMInvalidAccessException("Unexpected value: " + typed.getCssText());
			}

			@Override
			protected float percentage(CSSNumberValue typed, short resultType) throws DOMException {
				FloatValue relative = new FloatValue(typed.getUnitType(), typed.getFloatValue(typed.getUnitType()));
				FloatValue abs = NumericDelegateValue.this.absoluteValue(elt, pseudo, engine, idx, sm, relative);
				return NumberValue.floatValueConversion(abs.getFloatValue(), abs.getUnitType(), resultType);
			}

		};

		CSSTypedValue typed = evaluate(eval);
		if (typed.getPrimitiveType() != Type.NUMERIC) {
			throw new DOMException(DOMException.INVALID_STATE_ERR,
					"Unexpected calc() result: " + typed.getCssText());
		}

		float f;
		short u;
		if (typed.getUnitType() == CSSUnit.CSS_NUMBER) {
			// Plain number can be interpreted as px or deg according to context
			u = CSSUnit.CSS_NUMBER;
			f = typed.getFloatValue(CSSUnit.CSS_NUMBER);
		} else {
			u = unit;
			f = typed.getFloatValue(unit);
		}

		return new FloatValue(u, f);
	}

	protected abstract CSSTypedValue evaluate(Evaluator eval) throws DOMException;

	protected FloatValue absoluteValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx,
			StyleMap sm, FloatValue relative) throws DOMException {
		return relative;
	}

	public Value floatValue(Evaluator eval) throws DOMException {
		CSSTypedValue typed = evaluate(eval);
		return new FloatValue(typed.getUnitType(), typed.getFloatValue());
	}

	@Override
	public CSSUnitValue to(String unit) {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Not supported.");
	}

}
