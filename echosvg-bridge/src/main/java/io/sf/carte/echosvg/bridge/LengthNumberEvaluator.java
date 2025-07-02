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
package io.sf.carte.echosvg.bridge;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.DOMInvalidAccessException;
import io.sf.carte.doc.style.css.CSSNumberValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.property.Evaluator;
import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.parser.UnitProcessor.Context;

class LengthNumberEvaluator extends Evaluator {

	private short percentageInterpretation;

	private Context uctx;

	/**
	 * Instantiate a length evaluator.
	 * 
	 * @param pcInterp the percentage interpretation.
	 */
	public LengthNumberEvaluator(short pcInterp, Context uctx) {
		super(CSSUnit.CSS_PX);
		this.percentageInterpretation = pcInterp;
		this.uctx = uctx;
	}

	@Override
	protected CSSNumberValue absoluteTypedValue(CSSTypedValue typed) {
		if (typed.getPrimitiveType() == Type.NUMERIC) {
			short unitType = typed.getUnitType();
			if (CSSUnit.isRelativeLengthUnitType(unitType)) {
				float f = UnitProcessor.cssToUserSpace(typed.getFloatValue(), unitType,
						percentageInterpretation, uctx);
				return NumberValue.createCSSNumberValue(CSSUnit.CSS_PX, f);
			}
			return (CSSNumberValue) typed;
		}
		throw new DOMInvalidAccessException("Unexpected value: " + typed.getCssText());
	}

	@Override
	protected float percentage(CSSNumberValue typed, short resultType) throws DOMException {
		float f = UnitProcessor.cssToUserSpace(typed.getFloatValue(CSSUnit.CSS_PERCENTAGE),
				CSSUnit.CSS_PERCENTAGE, percentageInterpretation, uctx);
		return NumberValue.floatValueConversion(f, CSSUnit.CSS_PX, resultType);
	}

}
