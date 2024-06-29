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
package io.sf.carte.echosvg.anim.timing;

import java.util.LinkedList;
import java.util.List;

/**
 * An abstract base class for time container elements.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class TimeContainer extends TimedElement {

	/**
	 * The child {@link TimedElement}s of this time container.
	 */
	protected List<TimedElement> children = new LinkedList<>();

	/**
	 * Adds a {@link TimedElement} to this container.
	 */
	public void addChild(TimedElement e) {
		if (e == this) {
			throw new IllegalArgumentException("recursive datastructure not allowed here!");
		}
		children.add(e);
		e.parent = this;
		setRoot(e, root);
		root.fireElementAdded(e);
		root.currentIntervalWillUpdate();
	}

	/**
	 * Recursively sets the {@link TimedDocumentRoot} of the given
	 * {@link TimedElement} and any of its descendants.
	 */
	protected void setRoot(TimedElement e, TimedDocumentRoot root) {
		e.root = root;
		if (e instanceof TimeContainer) {
			TimeContainer c = (TimeContainer) e;
			for (TimedElement te : c.children) {
				setRoot(te, root);
			}
		}
	}

	/**
	 * Removes a {@link TimedElement} from this container.
	 */
	public void removeChild(TimedElement e) {
		children.remove(e);
		e.parent = null;
		setRoot(e, null);
		root.fireElementRemoved(e);
		root.currentIntervalWillUpdate();
	}

	/**
	 * Returns an array of the children of this container.
	 */
	public TimedElement[] getChildren() {
		return children.toArray(new TimedElement[children.size()]);
	}

	/**
	 * Calculates the local simple time. Currently the hyperlinking parameter is
	 * ignored, so DOM timing events are fired during hyperlinking seeks. If we were
	 * following SMIL 2.1 rather than SMIL Animation, then these events would have
	 * to be surpressed.
	 *
	 * @return the number of seconds until this element becomes active again if it
	 *         currently is not, {@link Float#POSITIVE_INFINITY} if this element
	 *         will become active at some undetermined point in the future (because
	 *         of unresolved begin times, for example) or will never become active
	 *         again, or <code>0f</code> if the element is currently active.
	 */
	@Override
	protected float sampleAt(float parentSimpleTime, boolean hyperlinking) {
		super.sampleAt(parentSimpleTime, hyperlinking);
		// Maybe check the return value of the previous statement.
		return sampleChildren(parentSimpleTime, hyperlinking);
	}

	/**
	 * Samples all the child timed elements.
	 */
	protected float sampleChildren(float parentSimpleTime, boolean hyperlinking) {
		float mint = Float.POSITIVE_INFINITY;
		for (TimedElement e : children) {
			float t = e.sampleAt(parentSimpleTime, hyperlinking);
			if (t < mint) {
				mint = t;
			}
		}
		return mint;
	}

	/**
	 * Resets this element.
	 */
	@Override
	protected void reset(boolean clearCurrentBegin) {
		super.reset(clearCurrentBegin);
		for (TimedElement e : children) {
			e.reset(clearCurrentBegin);
		}
	}

	/**
	 * Returns whether this timed element is for a constant animation (i.e., a 'set'
	 * animation.
	 */
	@Override
	protected boolean isConstantAnimation() {
		return false;
	}

	/**
	 * Returns the default begin time for the given child timed element.
	 */
	public abstract float getDefaultBegin(TimedElement child);

}
