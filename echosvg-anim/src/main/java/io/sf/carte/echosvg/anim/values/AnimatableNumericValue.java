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

import io.sf.carte.echosvg.anim.dom.AnimationTarget;

/**
 * An SVG numeric value in the animation system.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AnimatableNumericValue extends AnimatableValue {

	/**
	 * The length type according to {@link CSSUnit}.
	 */
	protected short lengthType;

	/**
	 * How to interpret percentage values. One of the
	 * {@link AnimationTarget}.PERCENTAGE_* constants.
	 */
	protected short percentageInterpretation;

	/**
	 * Creates a new AnimatableLengthValue with no length.
	 */
	protected AnimatableNumericValue(AnimationTarget target) {
		super(target);
	}

	/**
	 * Creates a new AnimatableLengthValue.
	 */
	protected AnimatableNumericValue(AnimationTarget target, short type, short pcInterp) {
		super(target);
		lengthType = type;
		percentageInterpretation = pcInterp;
	}

	/**
	 * Performs interpolation to the given value.
	 */
	@Override
	public AnimatableValue interpolate(AnimatableValue result, AnimatableValue to, float interpolation,
			AnimatableValue accumulation, int multiplier) {
		AnimatableLengthValue res;
		if (result == null) {
			res = new AnimatableLengthValue(target);
		} else {
			res = (AnimatableLengthValue) result;
		}

		short oldLengthType = res.lengthType;
		float oldLengthValue = res.getLengthValue();
		short oldPercentageInterpretation = res.percentageInterpretation;

		res.lengthType = lengthType;
		res.lengthValue = getLengthValue();
		res.percentageInterpretation = percentageInterpretation;

		if (to != null) {
			AnimatableNumericValue toLength = (AnimatableNumericValue) to;
			float toValue;
			if (!compatibleTypes(res.lengthType, res.percentageInterpretation, toLength.lengthType,
					toLength.percentageInterpretation)) {
				res.lengthValue = target.svgToUserSpace(oldLengthValue, res.lengthType, res.percentageInterpretation);
				res.lengthType = CSSUnit.CSS_NUMBER;
				toValue = toLength.target.svgToUserSpace(toLength.getLengthValue(), toLength.lengthType,
						toLength.percentageInterpretation);
			} else {
				toValue = toLength.getLengthValue();
			}
			res.lengthValue += interpolation * (toValue - res.getLengthValue());
		}

		if (accumulation != null) {
			AnimatableNumericValue accLength = (AnimatableNumericValue) accumulation;
			float accValue;
			if (!compatibleTypes(res.lengthType, res.percentageInterpretation, accLength.lengthType,
					accLength.percentageInterpretation)) {
				res.lengthValue = target.svgToUserSpace(res.getLengthValue(), res.lengthType, res.percentageInterpretation);
				res.lengthType = CSSUnit.CSS_NUMBER;
				accValue = accLength.target.svgToUserSpace(accLength.getLengthValue(), accLength.lengthType,
						accLength.percentageInterpretation);
			} else {
				accValue = accLength.getLengthValue();
			}
			res.lengthValue += multiplier * accValue;
		}

		if (oldPercentageInterpretation != res.percentageInterpretation || oldLengthType != res.lengthType
				|| oldLengthValue != res.getLengthValue()) {
			res.hasChanged = true;
		}
		return res;
	}

	/**
	 * Determines if two SVG length types are compatible.
	 * 
	 * @param t1  the first SVG length type
	 * @param pi1 the first percentage interpretation type
	 * @param t2  the second SVG length type
	 * @param pi2 the second percentage interpretation type
	 */
	public static boolean compatibleTypes(short t1, short pi1, short t2, short pi2) {
		return t1 == t2 && (t1 != CSSUnit.CSS_PERCENTAGE || pi1 == pi2)
				|| t1 == CSSUnit.CSS_NUMBER && t2 == CSSUnit.CSS_PX
				|| t1 == CSSUnit.CSS_PX && t2 == CSSUnit.CSS_NUMBER;
	}

	/**
	 * Returns the unit type of this length value.
	 */
	public int getLengthType() {
		return lengthType;
	}

	/**
	 * Returns the magnitude of this length value.
	 */
	public abstract float getLengthValue();

	/**
	 * Returns whether two values of this type can have their distance computed, as
	 * needed by paced animation.
	 */
	@Override
	public boolean canPace() {
		return true;
	}

	/**
	 * Returns the absolute distance between this value and the specified other
	 * value.
	 */
	@Override
	public float distanceTo(AnimatableValue other) {
		AnimatableNumericValue o = (AnimatableNumericValue) other;
		float v1 = target.svgToUserSpace(getLengthValue(), lengthType, percentageInterpretation);
		float v2 = target.svgToUserSpace(o.getLengthValue(), o.lengthType, o.percentageInterpretation);
		return Math.abs(v1 - v2);
	}

	/**
	 * Returns a zero value of this AnimatableValue's type.
	 */
	@Override
	public AnimatableValue getZeroValue() {
		return new AnimatableLengthValue(target, CSSUnit.CSS_NUMBER, 0f, percentageInterpretation);
	}

	/**
	 * Returns the CSS text representation of the value. This could use
	 * io.sf.carte.echosvg.css.engine.value.FloatValue.getCssText, but we don't want
	 * a dependency on the CSS package.
	 */
	@Override
	public String getCssText() {
		return formatNumber(getLengthValue()) + CSSUnit.dimensionUnitString(lengthType);
	}

}
