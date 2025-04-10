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
package io.sf.carte.echosvg.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 * Tests EventTarget.addEventListenerNS.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class EventTargetAddEventListenerNSTest extends DOM3Test {

	@Test
	public void test() throws DOMException {
		Listener1 l1 = new Listener1();
		Listener2 l2 = new Listener2();

		Document doc = GenericDOMImplementation.getDOMImplementation().createDocument(null, null, null);
		Element e = doc.createElementNS(null, "test");
		AbstractNode et = (AbstractNode) e;
		doc.appendChild(e);
		et.addEventListenerNS(XML_EVENTS_NAMESPACE_URI, "DOMAttrModified", l1, false, null);
		et.addEventListenerNS(null, "DOMAttrModified", l1, false, null);
		e.setAttributeNS(null, "test", "abc");
		assertEquals(2, l1.getCount());
		et.addEventListenerNS(XML_EVENTS_NAMESPACE_URI, "DOMAttrModified", l2, false, "g1");
		et.addEventListenerNS(null, "DOMAttrModified", l2, false, "g1");
		e.setAttributeNS(null, "test", "def");

		assertEquals(2, l2.getCount());
	}

	private static class Listener1 implements EventListener {

		int count = 0;

		@Override
		public void handleEvent(Event e) {
			count++;
		}

		int getCount() {
			int c = count;
			count = 0;
			return c;
		}

	}

	private static class Listener2 implements EventListener {

		int count = 0;

		@Override
		public void handleEvent(Event e) {
			count++;
			e.stopPropagation();
		}

		int getCount() {
			int c = count;
			count = 0;
			return c;
		}

	}

}
