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
 * A class to handle offset SMIL timing specifiers.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class OffsetTimingSpecifier extends TimingSpecifier {

	/**
	 * The offset value.
	 */
	protected float offset;

	/**
	 * Creates a new OffsetTimingSpecifier object.
	 */
	public OffsetTimingSpecifier(TimedElement owner, boolean isBegin, float offset) {
		super(owner, isBegin);
		this.offset = offset;
	}

	/**
	 * Returns a string representation of this timing specifier.
	 */
	@Override
	public String toString() {
		return (offset >= 0 ? "+" : "") + offset;
	}

	/**
	 * Initializes this timing specifier by adding the initial instance time to the
	 * owner's instance time list or setting up any event listeners.
	 */
	@Override
	public void initialize() {
		InstanceTime instance = new InstanceTime(this, offset, false);
		owner.addInstanceTime(instance, isBegin);
	}

	/**
	 * Returns whether this timing specifier is event-like (i.e., if it is an
	 * eventbase, accesskey or a repeat timing specifier).
	 */
	@Override
	public boolean isEventCondition() {
		return false;
	}

}
