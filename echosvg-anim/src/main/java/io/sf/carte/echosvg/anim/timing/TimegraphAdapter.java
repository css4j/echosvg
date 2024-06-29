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

/**
 * An adapter class for {@link TimegraphListener}s.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TimegraphAdapter implements TimegraphListener {

	/**
	 * Invoked to indicate that a timed element has been added to the document.
	 */
	@Override
	public void elementAdded(TimedElement e) {
	}

	/**
	 * Invoked to indicate that a timed element has been removed from the document.
	 */
	@Override
	public void elementRemoved(TimedElement e) {
	}

	/**
	 * Invoked to indicate that a timed element has become active.
	 * 
	 * @param e the TimedElement that became active
	 * @param t the time (in parent simple time) that the element became active
	 */
	@Override
	public void elementActivated(TimedElement e, float t) {
	}

	/**
	 * Invoked to indicate that a timed element has become inactive and is filling.
	 */
	@Override
	public void elementFilled(TimedElement e, float t) {
	}

	/**
	 * Invoked to indicate that a timed element has become inactive and is not
	 * filling.
	 */
	@Override
	public void elementDeactivated(TimedElement e, float t) {
	}

	/**
	 * Invoked to indivate that an interval was created for the given timed element.
	 */
	@Override
	public void intervalCreated(TimedElement e, Interval i) {
	}

	/**
	 * Invoked to indivate that an interval was removed for the given timed element.
	 */
	@Override
	public void intervalRemoved(TimedElement e, Interval i) {
	}

	/**
	 * Invoked to indivate that an interval's endpoints were changed.
	 */
	@Override
	public void intervalChanged(TimedElement e, Interval i) {
	}

	/**
	 * Invoked to indivate that the given interval began.
	 * 
	 * @param i the Interval that began, or null if no interval is active for the
	 *          given timed element.
	 */
	@Override
	public void intervalBegan(TimedElement e, Interval i) {
	}

	/**
	 * Invoked to indicate that the given timed element began a repeat iteration at
	 * the specified time.
	 */
	@Override
	public void elementRepeated(TimedElement e, int i, float t) {
	}

	/**
	 * Invoked to indicate that the list of instance times for the given timed
	 * element has been updated.
	 */
	@Override
	public void elementInstanceTimesChanged(TimedElement e, float isBegin) {
	}

}
