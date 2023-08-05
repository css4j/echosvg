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
 * An SVG rect value in the animation system.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AnimatableRectValue extends AnimatableValue {

	/**
	 * The x coordinate.
	 */
	protected float x;

	/**
	 * The y coordinate.
	 */
	protected float y;

	/**
	 * The width.
	 */
	protected float width;

	/**
	 * The height.
	 */
	protected float height;

	/**
	 * Creates a new, uninitialized AnimatableRectValue.
	 */
	protected AnimatableRectValue(AnimationTarget target) {
		super(target);
	}

	/**
	 * Creates a new AnimatableRectValue with one number.
	 */
	public AnimatableRectValue(AnimationTarget target, float x, float y, float w, float h) {
		super(target);
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	/**
	 * Performs interpolation to the given value. Rect values cannot be
	 * interpolated.
	 */
	@Override
	public AnimatableValue interpolate(AnimatableValue result, AnimatableValue to, float interpolation,
			AnimatableValue accumulation, int multiplier) {
		AnimatableRectValue res;
		if (result == null) {
			res = new AnimatableRectValue(target);
		} else {
			res = (AnimatableRectValue) result;
		}

		float newX = x, newY = y, newWidth = width, newHeight = height;
		if (to != null) {
			AnimatableRectValue toValue = (AnimatableRectValue) to;
			newX += interpolation * (toValue.x - x);
			newY += interpolation * (toValue.y - y);
			newWidth += interpolation * (toValue.width - width);
			newHeight += interpolation * (toValue.height - height);
		}
		if (accumulation != null && multiplier != 0) {
			AnimatableRectValue accValue = (AnimatableRectValue) accumulation;
			newX += multiplier * accValue.x;
			newY += multiplier * accValue.y;
			newWidth += multiplier * accValue.width;
			newHeight += multiplier * accValue.height;
		}
		if (res.x != newX || res.y != newY || res.width != newWidth || res.height != newHeight) {
			res.x = newX;
			res.y = newY;
			res.width = newWidth;
			res.height = newHeight;
			res.hasChanged = true;
		}
		return res;
	}

	/**
	 * Returns the x coordinate.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the y coordinate.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the width.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the height.
	 */
	public float getHeight() {
		return height;
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
		return new AnimatableRectValue(target, 0f, 0f, 0f, 0f);
	}

	/**
	 * Returns a string representation of this object.
	 */
	@Override
	public String toStringRep() {
		StringBuilder sb = new StringBuilder();
		sb.append(x);
		sb.append(',');
		sb.append(y);
		sb.append(',');
		sb.append(width);
		sb.append(',');
		sb.append(height);
		return sb.toString();
	}
}
