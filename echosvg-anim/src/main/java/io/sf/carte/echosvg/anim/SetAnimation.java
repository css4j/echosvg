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
package io.sf.carte.echosvg.anim;

import io.sf.carte.echosvg.anim.dom.AnimatableElement;
import io.sf.carte.echosvg.anim.timing.TimedElement;
import io.sf.carte.echosvg.anim.values.AnimatableValue;

/**
 * An animation class for 'set' animations.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SetAnimation extends AbstractAnimation {

	/**
	 * The set animation value.
	 */
	protected AnimatableValue to;

	/**
	 * Creates a new SetAnimation.
	 */
	public SetAnimation(TimedElement timedElement, AnimatableElement animatableElement, AnimatableValue to) {
		super(timedElement, animatableElement);
		this.to = to;
	}

	/**
	 * Called when the element is sampled at the given time.
	 */
	@Override
	protected void sampledAt(float simpleTime, float simpleDur, int repeatIteration) {
		if (value == null) {
			value = to;
			markDirty();
		}
	}

	/**
	 * Called when the element is sampled for its "last" value.
	 */
	@Override
	protected void sampledLastValue(int repeatIteration) {
		if (value == null) {
			value = to;
			markDirty();
		}
	}

}
