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

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.events.DOMKeyEvent;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;
import io.sf.carte.echosvg.w3c.dom.events.KeyboardEvent;

/**
 * A class to handle SMIL access key timing specifiers.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AccesskeyTimingSpecifier extends EventLikeTimingSpecifier implements EventListener {

	/**
	 * The accesskey.
	 */
	protected char accesskey;

	/**
	 * Whether this access key specifier uses SVG 1.2 syntax.
	 */
	protected boolean isSVG12AccessKey;

	/**
	 * The DOM 3 key name for SVG 1.2 access key specifiers.
	 */
	protected String keyName;

	/**
	 * Creates a new AccesskeyTimingSpecifier object using SVG 1.1 or SMIL syntax.
	 */
	public AccesskeyTimingSpecifier(TimedElement owner, boolean isBegin, float offset, char accesskey) {
		super(owner, isBegin, offset);
		this.accesskey = accesskey;
	}

	/**
	 * Creates a new AccesskeyTimingSpecifier object using SVG 1.2 syntax.
	 */
	public AccesskeyTimingSpecifier(TimedElement owner, boolean isBegin, float offset, String keyName) {
		super(owner, isBegin, offset);
		this.isSVG12AccessKey = true;
		this.keyName = keyName;
	}

	/**
	 * Returns a string representation of this timing specifier.
	 */
	@Override
	public String toString() {
		if (isSVG12AccessKey) {
			return "accessKey(" + keyName + ")" + (offset != 0 ? super.toString() : "");
		}
		return "accesskey(" + accesskey + ")" + (offset != 0 ? super.toString() : "");
	}

	/**
	 * Initializes this timing specifier by adding the initial instance time to the
	 * owner's instance time list or setting up any event listeners.
	 */
	@Override
	public void initialize() {
		if (isSVG12AccessKey) {
			NodeEventTarget eventTarget = (NodeEventTarget) owner.getRootEventTarget();
			eventTarget.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "keydown", this, false, null);
		} else {
			EventTarget eventTarget = owner.getRootEventTarget();
			eventTarget.addEventListener("keypress", this, false);
		}
	}

	/**
	 * Deinitializes this timing specifier by removing any event listeners.
	 */
	@Override
	public void deinitialize() {
		if (isSVG12AccessKey) {
			NodeEventTarget eventTarget = (NodeEventTarget) owner.getRootEventTarget();
			eventTarget.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "keydown", this, false);
		} else {
			EventTarget eventTarget = owner.getRootEventTarget();
			eventTarget.removeEventListener("keypress", this, false);
		}
	}

	// EventListener /////////////////////////////////////////////////////////

	/**
	 * Handles key events fired by the eventbase element.
	 */
	@Override
	public void handleEvent(Event e) {
		boolean matched;
		if (e.getType().charAt(3) == 'p') {
			// DOM 2 key draft keypress
			DOMKeyEvent evt = (DOMKeyEvent) e;
			matched = evt.getCharCode() == accesskey;
		} else {
			// DOM 3 keydown
			KeyboardEvent evt = (KeyboardEvent) e;
			matched = evt.getKeyIdentifier().equals(keyName);
		}
		if (matched) {
			owner.eventOccurred(this, e);
		}
	}

	/**
	 * Invoked to resolve an event-like timing specifier into an instance time.
	 */
	@Override
	public void resolve(Event e) {
		float time = owner.getRoot().convertEpochTime(e.getTimeStamp());
		InstanceTime instance = new InstanceTime(this, time + offset, true);
		owner.addInstanceTime(instance, isBegin);
	}
}
