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
 * An SVG length list value in the animation system.
 *
 * <p>
 * Original author: <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class AnimatableLengthListValue extends AnimatableValue {

	/**
	 * The length types as in CSSUnit.
	 */
	protected short[] lengthTypes;

	/**
	 * The length values.
	 */
	protected float[] lengthValues;

	/**
	 * How to interpret percentage values. These should be one of the
	 * {@link AnimationTarget}.PERCENTAGE_* constants.
	 */
	protected short percentageInterpretation;

	/**
	 * Creates a new, uninitialized AnimatableLengthListValue.
	 */
	protected AnimatableLengthListValue(AnimationTarget target) {
		super(target);
	}

	/**
	 * Creates a new AnimatableLengthListValue.
	 */
	public AnimatableLengthListValue(AnimationTarget target, short[] types, float[] values, short pcInterp) {
		super(target);
		this.lengthTypes = types;
		this.lengthValues = values;
		this.percentageInterpretation = pcInterp;
	}

	/**
	 * Performs interpolation to the given value.
	 */
	@Override
	public AnimatableValue interpolate(AnimatableValue result, AnimatableValue to, float interpolation,
			AnimatableValue accumulation, int multiplier) {
		AnimatableLengthListValue toLengthList = (AnimatableLengthListValue) to;
		AnimatableLengthListValue accLengthList = (AnimatableLengthListValue) accumulation;

		boolean hasTo = to != null;
		boolean hasAcc = accumulation != null;
		boolean canInterpolate = !(hasTo && toLengthList.lengthTypes.length != lengthTypes.length)
				&& !(hasAcc && accLengthList.lengthTypes.length != lengthTypes.length);

		short[] baseLengthTypes;
		float[] baseLengthValues;
		if (!canInterpolate && hasTo && interpolation >= 0.5) {
			baseLengthTypes = toLengthList.lengthTypes;
			baseLengthValues = toLengthList.lengthValues;
		} else {
			baseLengthTypes = lengthTypes;
			baseLengthValues = lengthValues;
		}
		int len = baseLengthTypes.length;

		AnimatableLengthListValue res;
		if (result == null) {
			res = new AnimatableLengthListValue(target);
			res.lengthTypes = new short[len];
			res.lengthValues = new float[len];
		} else {
			res = (AnimatableLengthListValue) result;
			if (res.lengthTypes == null || res.lengthTypes.length != len) {
				res.lengthTypes = new short[len];
				res.lengthValues = new float[len];
			}
		}

		res.hasChanged = percentageInterpretation != res.percentageInterpretation;
		res.percentageInterpretation = percentageInterpretation;

		for (int i = 0; i < len; i++) {
			float toV = 0, accV = 0;
			short newLengthType = baseLengthTypes[i];
			float newLengthValue = baseLengthValues[i];
			if (canInterpolate) {
				if (hasTo
						&& !AnimatableLengthValue.compatibleTypes(newLengthType, percentageInterpretation,
								toLengthList.lengthTypes[i], toLengthList.percentageInterpretation)
						|| hasAcc && !AnimatableLengthValue.compatibleTypes(newLengthType, percentageInterpretation,
								accLengthList.lengthTypes[i], accLengthList.percentageInterpretation)) {
					newLengthValue = target.svgToUserSpace(newLengthValue, newLengthType, percentageInterpretation);
					newLengthType = CSSUnit.CSS_NUMBER;
					if (hasTo) {
						toV = to.target.svgToUserSpace(toLengthList.lengthValues[i], toLengthList.lengthTypes[i],
								toLengthList.percentageInterpretation);
					}
					if (hasAcc) {
						accV = accumulation.target.svgToUserSpace(accLengthList.lengthValues[i],
								accLengthList.lengthTypes[i], accLengthList.percentageInterpretation);
					}
				} else {
					if (hasTo) {
						toV = toLengthList.lengthValues[i];
					}
					if (hasAcc) {
						accV = accLengthList.lengthValues[i];
					}
				}
				newLengthValue += interpolation * (toV - newLengthValue) + multiplier * accV;
			}
			if (!res.hasChanged) {
				res.hasChanged = newLengthType != res.lengthTypes[i] || newLengthValue != res.lengthValues[i];
			}
			res.lengthTypes[i] = newLengthType;
			res.lengthValues[i] = newLengthValue;
		}

		return res;
	}

	/**
	 * Gets the length types.
	 */
	public short[] getLengthTypes() {
		return lengthTypes;
	}

	/**
	 * Gets the length values.
	 */
	public float[] getLengthValues() {
		return lengthValues;
	}

	/**
	 * Returns whether two values of this type can have their distance computed, as
	 * needed by paced animation.
	 */
	@Override
	public boolean canPace() {
		return false;
	}

	/**
	 * Returns the absolute distance between this value and the specified other
	 * value.
	 */
	@Override
	public float distanceTo(AnimatableValue other) {
		return 0f;
	}

	/**
	 * Returns a zero value of this AnimatableValue's type.
	 */
	@Override
	public AnimatableValue getZeroValue() {
		float[] vs = new float[lengthValues.length];
		return new AnimatableLengthListValue(target, lengthTypes, vs, percentageInterpretation);
	}

	/**
	 * Returns the CSS text representation of the value. Length lists can never be
	 * used for CSS properties.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder();
		if (lengthValues.length > 0) {
			sb.append(formatNumber(lengthValues[0]));
			sb.append(CSSUnit.dimensionUnitString(lengthTypes[0]));
		}
		for (int i = 1; i < lengthValues.length; i++) {
			sb.append(',');
			sb.append(formatNumber(lengthValues[i]));
			sb.append(CSSUnit.dimensionUnitString(lengthTypes[i]));
		}
		return sb.toString();
	}

}
