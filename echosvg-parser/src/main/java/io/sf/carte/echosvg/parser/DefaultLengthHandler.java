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
package io.sf.carte.echosvg.parser;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSMathFunctionValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.property.Evaluator;
import io.sf.carte.doc.style.css.property.NumberValue;

/**
 * This class provides an adapter for LengthHandler
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class DefaultLengthHandler implements LengthHandler {

	public DefaultLengthHandler() {
	}

	/**
	 * Implements {@link LengthHandler#startLength()}.
	 */
	@Override
	public void startLength() throws ParseException {
	}

	/**
	 * Implements {@link LengthHandler#lengthValue(float)}.
	 */
	@Override
	public void lengthValue(float v) throws ParseException {
	}

	@Override
	public void em() throws ParseException {
		setUnit(CSSUnit.CSS_EM);
	}

	@Override
	public void ex() throws ParseException {
		setUnit(CSSUnit.CSS_EX);
	}

	@Override
	public void lh() throws ParseException {
		setUnit(CSSUnit.CSS_LH);
	}

	@Override
	public void in() throws ParseException {
		setUnit(CSSUnit.CSS_IN);
	}

	@Override
	public void cm() throws ParseException {
		setUnit(CSSUnit.CSS_CM);
	}

	@Override
	public void mm() throws ParseException {
		setUnit(CSSUnit.CSS_MM);
	}

	@Override
	public void q() throws ParseException {
		setUnit(CSSUnit.CSS_QUARTER_MM);
	}

	@Override
	public void pc() throws ParseException {
		setUnit(CSSUnit.CSS_PC);
	}

	@Override
	public void pt() throws ParseException {
		setUnit(CSSUnit.CSS_PT);
	}

	@Override
	public void px() throws ParseException {
		setUnit(CSSUnit.CSS_PX);
	}

	@Override
	public void percentage() throws ParseException {
		setUnit(CSSUnit.CSS_PERCENTAGE);
	}

	@Override
	public void rem() throws ParseException {
		setUnit(CSSUnit.CSS_REM);
	}

	@Override
	public void rex() throws ParseException {
		setUnit(CSSUnit.CSS_REX);
	}

	@Override
	public void rlh() throws ParseException {
		setUnit(CSSUnit.CSS_RLH);
	}

	@Override
	public void vh() throws ParseException {
		setUnit(CSSUnit.CSS_VH);
	}

	@Override
	public void vw() throws ParseException {
		setUnit(CSSUnit.CSS_VW);
	}

	@Override
	public void vmax() throws ParseException {
		setUnit(CSSUnit.CSS_VMAX);
	}

	@Override
	public void vmin() throws ParseException {
		setUnit(CSSUnit.CSS_VMIN);
	}

	@Override
	public void mathExpression(CSSExpressionValue value, short pcInterp) throws ParseException {
		Evaluator eval = new LengthEvaluator(pcInterp);
		float floatValue;
		short unitType;

		try {
			CSSTypedValue typed = eval.evaluateExpression(value);
			unitType = typed.getUnitType();
			if (unitType != CSSUnit.CSS_NUMBER) {
				unitType = CSSUnit.CSS_PX;
			}
			floatValue = typed.getFloatValue(unitType);
		} catch (RuntimeException e) {
			throw new ParseException(e);
		}

		lengthValue(floatValue);
		setUnit(unitType);
	}

	@Override
	public void mathFunction(CSSMathFunctionValue value, short pcInterp) throws ParseException {
		Evaluator eval = new LengthEvaluator(pcInterp);
		float floatValue;
		short unitType;

		try {
			CSSTypedValue typed = eval.evaluateFunction(value);
			unitType = typed.getUnitType();
			if (unitType != CSSUnit.CSS_NUMBER) {
				unitType = CSSUnit.CSS_PX;
			}
			floatValue = typed.getFloatValue(unitType);
		} catch (RuntimeException e) {
			throw new ParseException(e);
		}

		lengthValue(floatValue);
		setUnit(unitType);
	}

	protected class LengthEvaluator extends Evaluator {

		private short percentageInterpretation;

		/**
		 * Instantiate a length evaluator.
		 * 
		 * @param pcInterp the percentage interpretation.
		 */
		public LengthEvaluator(short pcInterp) {
			super(CSSUnit.CSS_PX);
			this.percentageInterpretation = pcInterp;
		}

		@Override
		protected CSSTypedValue absoluteTypedValue(CSSTypedValue typed) {
			short unitType = typed.getUnitType();
			if (CSSUnit.isRelativeLengthUnitType(unitType)) {
				float f = unitToPixels(unitType, typed.getFloatValue(unitType), percentageInterpretation);
				return NumberValue.createCSSNumberValue(CSSUnit.CSS_PX, f);
			} else {
				return typed;
			}
		}

		@Override
		protected float percentage(CSSTypedValue typed, short resultType) throws DOMException {
			float f = unitToPixels(CSSUnit.CSS_PERCENTAGE, typed.getFloatValue(CSSUnit.CSS_PERCENTAGE),
					percentageInterpretation);
			return NumberValue.floatValueConversion(f, CSSUnit.CSS_PX, resultType);
		}

		/**
		 * Gets the percentage interpretation that was passed by the handler caller.
		 * 
		 * @return the percentage interpretation.
		 */
		protected short getPercentageInterpretation() {
			return percentageInterpretation;
		}

	}

	protected float unitToPixels(short unitType, float floatValue, short percentageInterpretation) {
		throw new ParseException("Do not know how to convert to " + CSSUnit.dimensionUnitString(unitType), -1, -1);
	}

	protected void setUnit(short unit) {
	}

	/**
	 * Implements {@link LengthHandler#endLength()}.
	 */
	@Override
	public void endLength() throws ParseException {
	}

}
