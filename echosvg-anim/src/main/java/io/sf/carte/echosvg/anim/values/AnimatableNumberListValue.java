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
 * A number list in the animation system.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AnimatableNumberListValue extends AnimatableValue {

	/**
	 * The numbers.
	 */
	protected float[] numbers;

	/**
	 * Creates a new, uninitialized AnimatableNumberListValue.
	 */
	protected AnimatableNumberListValue(AnimationTarget target) {
		super(target);
	}

	/**
	 * Creates a new AnimatableNumberListValue.
	 */
	public AnimatableNumberListValue(AnimationTarget target, float[] numbers) {
		super(target);
		this.numbers = numbers;
	}

	/**
	 * Performs interpolation to the given value. Number list values cannot be
	 * interpolated.
	 */
	@Override
	public AnimatableValue interpolate(AnimatableValue result, AnimatableValue to, float interpolation,
			AnimatableValue accumulation, int multiplier) {
		AnimatableNumberListValue toNumList = (AnimatableNumberListValue) to;
		AnimatableNumberListValue accNumList = (AnimatableNumberListValue) accumulation;

		boolean hasTo = to != null;
		boolean hasAcc = accumulation != null;
		boolean canInterpolate = !(hasTo && toNumList.numbers.length != numbers.length)
				&& !(hasAcc && accNumList.numbers.length != numbers.length);

		float[] baseValues;
		if (!canInterpolate && hasTo && interpolation >= 0.5) {
			baseValues = toNumList.numbers;
		} else {
			baseValues = numbers;
		}
		int len = baseValues.length;

		AnimatableNumberListValue res;
		if (result == null) {
			res = new AnimatableNumberListValue(target);
			res.numbers = new float[len];
		} else {
			res = (AnimatableNumberListValue) result;
			if (res.numbers == null || res.numbers.length != len) {
				res.numbers = new float[len];
			}
		}

		for (int i = 0; i < len; i++) {
			float newValue = baseValues[i];
			if (canInterpolate) {
				if (hasTo) {
					newValue += interpolation * (toNumList.numbers[i] - newValue);
				}
				if (hasAcc) {
					newValue += multiplier * accNumList.numbers[i];
				}
			}
			if (res.numbers[i] != newValue) {
				res.numbers[i] = newValue;
				res.hasChanged = true;
			}
		}

		return res;
	}

	/**
	 * Gets the numbers.
	 */
	public float[] getNumbers() {
		return numbers;
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
		float[] ns = new float[numbers.length];
		return new AnimatableNumberListValue(target, ns);
	}

	/**
	 * Returns the CSS text representation of the value.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder();
		sb.append(numbers[0]);
		for (int i = 1; i < numbers.length; i++) {
			sb.append(' ');
			sb.append(numbers[i]);
		}
		return sb.toString();
	}

}
