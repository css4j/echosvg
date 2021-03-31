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
package io.sf.carte.echosvg.bridge;

import java.util.HashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.AnimationTarget;
import io.sf.carte.echosvg.anim.dom.AnimationTargetListener;
import io.sf.carte.echosvg.anim.dom.SVGAnimationTargetContext;

/**
 * Abstract bridge class for those elements that can be animated.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AnimatableSVGBridge extends AbstractSVGBridge implements SVGAnimationTargetContext {

	/**
	 * The element that has been handled by this bridge.
	 */
	protected Element e;

	/**
	 * The bridge context to use for dynamic updates.
	 */
	protected BridgeContext ctx;

	/**
	 * Map of CSS property names to {@link LinkedList}s of
	 * {@link AnimationTargetListener}s.
	 */
	protected HashMap<String, LinkedList<AnimationTargetListener>> targetListeners;

	// SVGAnimationTargetContext /////////////////////////////////////////////

	/**
	 * Adds a listener for changes to the given attribute value.
	 */
	@Override
	public void addTargetListener(String pn, AnimationTargetListener l) {
		if (targetListeners == null) {
			targetListeners = new HashMap<>();
		}
		LinkedList<AnimationTargetListener> ll = targetListeners.get(pn);
		if (ll == null) {
			ll = new LinkedList<>();
			targetListeners.put(pn, ll);
		}
		ll.add(l);
	}

	/**
	 * Removes a listener for changes to the given attribute value.
	 */
	@Override
	public void removeTargetListener(String pn, AnimationTargetListener l) {
		LinkedList<AnimationTargetListener> ll = targetListeners.get(pn);
		ll.remove(l);
	}

	/**
	 * Fires the listeners registered for changes to the base value of the given CSS
	 * property.
	 */
	protected void fireBaseAttributeListeners(String pn) {
		if (targetListeners != null) {
			LinkedList<AnimationTargetListener> ll = targetListeners.get(pn);
			if (ll != null) {
				for (AnimationTargetListener l : ll) {
					l.baseValueChanged((AnimationTarget) e, null, pn, true);
				}
			}
		}
	}
}
