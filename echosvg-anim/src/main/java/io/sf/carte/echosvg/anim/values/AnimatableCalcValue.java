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
package io.sf.carte.echosvg.anim.values;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSMathFunctionValue;
import io.sf.carte.doc.style.css.CSSMathValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.property.Evaluator;
import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.anim.dom.AnimationTarget;

/**
 * A {@code calc()}-based SVG length value in the animation system.
 *
 * @version $Id$
 */
public class AnimatableCalcValue extends AnimatableNumericValue {

	private CSSMathValue calc;

	/**
	 * Create a new AnimatableCalcValue.
	 * 
	 * @param target   the animation target.
	 * @param type     the desired final unit, according to {@link CSSUnit}.
	 * @param calc     the {@code calc()} or mathematical function value.
	 * @param pcInterp One of the {@link AnimationTarget}.PERCENTAGE_* constants.
	 */
	public AnimatableCalcValue(AnimationTarget target, short type, CSSMathValue calc, short pcInterp) {
		super(target, type, pcInterp);
		this.calc = calc;
	}

	/**
	 * Returns the magnitude of this length value.
	 */
	public float getLengthValue() {
		Evaluator eval = new Evaluator(lengthType) {

			@Override
			protected float percentage(CSSTypedValue value, short resultType) throws DOMException {
				return target.svgToUserSpace(value.getFloatValue(CSSUnit.CSS_PERCENTAGE), lengthType,
						percentageInterpretation);
			}

		};

		CSSTypedValue result;
		switch (calc.getPrimitiveType()) {
		case EXPRESSION:
			result = eval.evaluateExpression((CSSExpressionValue) calc);
			break;
		case MATH_FUNCTION:
			result = eval.evaluateFunction((CSSMathFunctionValue) calc);
			break;
		default:
			throw new RuntimeException("Invalid result: " + calc.getCssText());
		}

		float f = result.getFloatValue(result.getUnitType());
		if (lengthType != result.getUnitType() && result.getUnitType() != CSSUnit.CSS_NUMBER) {
			f = NumberValue.floatValueConversion(f, result.getUnitType(), lengthType);
		}

		return f;
	}

}
