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

import io.sf.carte.echosvg.anim.dom.AnimationTarget;

/**
 * A number-or-percentage value in the animation system.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AnimatableNumberOrPercentageValue extends AnimatableNumberValue {

	/**
	 * Whether the number is a percentage.
	 */
	protected boolean isPercentage;

	/**
	 * Creates a new, uninitialized AnimatableNumberOrPercentageValue.
	 */
	protected AnimatableNumberOrPercentageValue(AnimationTarget target) {
		super(target);
	}

	/**
	 * Creates a new AnimatableNumberOrPercentageValue with a number.
	 */
	public AnimatableNumberOrPercentageValue(AnimationTarget target, float n) {
		super(target, n);
	}

	/**
	 * Creates a new AnimatableNumberOrPercentageValue with either a number or a
	 * percentage.
	 */
	public AnimatableNumberOrPercentageValue(AnimationTarget target, float n, boolean isPercentage) {
		super(target, n);
		this.isPercentage = isPercentage;
	}

	/**
	 * Performs interpolation to the given value.
	 */
	@Override
	public AnimatableValue interpolate(AnimatableValue result, AnimatableValue to, float interpolation,
			AnimatableValue accumulation, int multiplier) {
		AnimatableNumberOrPercentageValue res;
		if (result == null) {
			res = new AnimatableNumberOrPercentageValue(target);
		} else {
			res = (AnimatableNumberOrPercentageValue) result;
		}

		float newValue;
		boolean newIsPercentage;

		AnimatableNumberOrPercentageValue toValue = (AnimatableNumberOrPercentageValue) to;
		AnimatableNumberOrPercentageValue accValue = (AnimatableNumberOrPercentageValue) accumulation;

		if (to != null) {
			if (toValue.isPercentage == isPercentage) {
				newValue = value + interpolation * (toValue.value - value);
				newIsPercentage = isPercentage;
			} else {
				if (interpolation >= 0.5) {
					newValue = toValue.value;
					newIsPercentage = toValue.isPercentage;
				} else {
					newValue = value;
					newIsPercentage = isPercentage;
				}
			}
		} else {
			newValue = value;
			newIsPercentage = isPercentage;
		}

		if (accumulation != null && accValue.isPercentage == newIsPercentage) {
			newValue += multiplier * accValue.value;
		}

		if (res.value != newValue || res.isPercentage != newIsPercentage) {
			res.value = newValue;
			res.isPercentage = newIsPercentage;
			res.hasChanged = true;
		}
		return res;
	}

	/**
	 * Returns whether the value is a percentage.
	 */
	public boolean isPercentage() {
		return isPercentage;
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
		return new AnimatableNumberOrPercentageValue(target, 0, isPercentage);
	}

	/**
	 * Returns the CSS text representation of the value.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder();
		sb.append(formatNumber(value));
		if (isPercentage) {
			sb.append('%');
		}
		return sb.toString();
	}

}
