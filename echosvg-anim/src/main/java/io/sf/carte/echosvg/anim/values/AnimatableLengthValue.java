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
 * An SVG length value in the animation system.
 *
 * <p>
 * Original author: <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class AnimatableLengthValue extends AnimatableNumericValue {

	/**
	 * The length value.
	 */
	protected float lengthValue;

	/**
	 * Creates a new AnimatableLengthValue with no length.
	 */
	protected AnimatableLengthValue(AnimationTarget target) {
		super(target);
	}

	/**
	 * Creates a new AnimatableLengthValue.
	 */
	public AnimatableLengthValue(AnimationTarget target, short type, float v, short pcInterp) {
		super(target, type, pcInterp);
		lengthValue = v;
	}

	/**
	 * Returns the magnitude of this length value.
	 */
	@Override
	public float getLengthValue() {
		return lengthValue;
	}

}
