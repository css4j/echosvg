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
 * A number value in the animation system.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AnimatableNumberValue extends AnimatableValue {

	/**
	 * The value.
	 */
	protected float value;

	/**
	 * Creates a new, uninitialized AnimatableNumberValue.
	 */
	protected AnimatableNumberValue(AnimationTarget target) {
		super(target);
	}

	/**
	 * Creates a new AnimatableNumberValue.
	 */
	public AnimatableNumberValue(AnimationTarget target, float v) {
		super(target);
		value = v;
	}

	/**
	 * Performs interpolation to the given value.
	 */
	@Override
	public AnimatableValue interpolate(AnimatableValue result, AnimatableValue to, float interpolation,
			AnimatableValue accumulation, int multiplier) {
		AnimatableNumberValue res;
		if (result == null) {
			res = new AnimatableNumberValue(target);
		} else {
			res = (AnimatableNumberValue) result;
		}

		float v = value;
		if (to != null) {
			AnimatableNumberValue toNumber = (AnimatableNumberValue) to;
			v += interpolation * (toNumber.value - value);
		}
		if (accumulation != null) {
			AnimatableNumberValue accNumber = (AnimatableNumberValue) accumulation;
			v += multiplier * accNumber.value;
		}

		if (res.value != v) {
			res.value = v;
			res.hasChanged = true;
		}
		return res;
	}

	/**
	 * Returns the number value.
	 */
	public float getValue() {
		return value;
	}

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
		AnimatableNumberValue o = (AnimatableNumberValue) other;
		return Math.abs(value - o.value);
	}

	/**
	 * Returns a zero value of this AnimatableValue's type.
	 */
	@Override
	public AnimatableValue getZeroValue() {
		return new AnimatableNumberValue(target, 0);
	}

	/**
	 * Returns the CSS text representation of the value.
	 */
	@Override
	public String getCssText() {
		return formatNumber(value);
	}
}
