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
package io.sf.carte.echosvg.bridge.svg12;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import io.sf.carte.echosvg.anim.dom.XBLEventSupport;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.DocumentLoader;
import io.sf.carte.echosvg.bridge.Messages;
import io.sf.carte.echosvg.bridge.SVGUtilities;
import io.sf.carte.echosvg.bridge.ScriptingEnvironment;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.AbstractElement;
import io.sf.carte.echosvg.dom.events.EventSupport;
import io.sf.carte.echosvg.dom.svg12.SVGGlobal;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.dom.util.TriplyIndexedTable;
import io.sf.carte.echosvg.script.Interpreter;
import io.sf.carte.echosvg.util.SVG12Constants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Manages scripting handlers for SVG 1.2 'handler' elements.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVG12ScriptingEnvironment extends ScriptingEnvironment {

	/**
	 * Constant used to describe handler scripts. {0} - URL of document containing
	 * script. {1} - Event type {2} - Event namespace {3} - line number of element.
	 */
	public static final String HANDLER_SCRIPT_DESCRIPTION = "SVG12ScriptingEnvironment.constant.handler.script.description";

	/**
	 * Creates a new SVG12ScriptingEnvironment.
	 * 
	 * @param ctx the bridge context
	 */
	public SVG12ScriptingEnvironment(BridgeContext ctx) {
		super(ctx);
	}

	/**
	 * The listeners for XML Events style handlers. Maps (event namespace, event
	 * local name, element) to a handler.
	 */
	protected TriplyIndexedTable handlerScriptingListeners;

	/**
	 * Adds DOM listeners to the document.
	 */
	@Override
	protected void addDocumentListeners() {
		domNodeInsertedListener = new DOMNodeInsertedListener();
		domNodeRemovedListener = new DOMNodeRemovedListener();
		domAttrModifiedListener = new DOMAttrModifiedListener();
		AbstractDocument doc = (AbstractDocument) document;
		XBLEventSupport es = (XBLEventSupport) doc.initializeEventSupport();
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted",
				domNodeInsertedListener, false);
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved",
				domNodeRemovedListener, false);
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified",
				domAttrModifiedListener, false);
	}

	/**
	 * Removes DOM listeners from the document.
	 */
	@Override
	protected void removeDocumentListeners() {
		AbstractDocument doc = (AbstractDocument) document;
		XBLEventSupport es = (XBLEventSupport) doc.initializeEventSupport();
		es.removeImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted",
				domNodeInsertedListener, false);
		es.removeImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved",
				domNodeRemovedListener, false);
		es.removeImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified",
				domAttrModifiedListener, false);
	}

	/**
	 * The listener class for 'DOMNodeInserted' event.
	 */
	protected class DOMNodeInsertedListener extends ScriptingEnvironment.DOMNodeInsertedListener {

		@Override
		public void handleEvent(Event evt) {
			super.handleEvent(EventSupport.getUltimateOriginalEvent(evt));
		}

	}

	/**
	 * The listener class for 'DOMNodeRemoved' event.
	 */
	protected class DOMNodeRemovedListener extends ScriptingEnvironment.DOMNodeRemovedListener {

		@Override
		public void handleEvent(Event evt) {
			super.handleEvent(EventSupport.getUltimateOriginalEvent(evt));
		}

	}

	protected class DOMAttrModifiedListener extends ScriptingEnvironment.DOMAttrModifiedListener {

		@Override
		public void handleEvent(Event evt) {
			super.handleEvent(EventSupport.getUltimateOriginalEvent(evt));
		}

	}

	/**
	 * Adds the scripting listeners to the given element.
	 */
	@Override
	protected void addScriptingListenersOn(Element elt) {
		String eltNS = elt.getNamespaceURI();
		String eltLN = elt.getLocalName();
		if (SVGConstants.SVG_NAMESPACE_URI.equals(eltNS) && SVG12Constants.SVG_HANDLER_TAG.equals(eltLN)) {
			// For this 'handler' element, add a handler for the given
			// event type.
			AbstractElement tgt = (AbstractElement) elt.getParentNode();
			String eventType = elt.getAttributeNS(XMLConstants.XML_EVENTS_NAMESPACE_URI,
					XMLConstants.XML_EVENTS_EVENT_ATTRIBUTE);
			String eventNamespaceURI = XMLConstants.XML_EVENTS_NAMESPACE_URI;
			if (eventType.indexOf(':') != -1) {
				String prefix = DOMUtilities.getPrefix(eventType);
				eventType = DOMUtilities.getLocalName(eventType);
				eventNamespaceURI = elt.lookupNamespaceURI(prefix);
			}

			EventListener listener = new HandlerScriptingEventListener(eventNamespaceURI, eventType,
					(AbstractElement) elt);
			tgt.addEventListenerNS(eventNamespaceURI, eventType, listener, false, null);
			if (handlerScriptingListeners == null) {
				handlerScriptingListeners = new TriplyIndexedTable();
			}
			handlerScriptingListeners.put(eventNamespaceURI, eventType, elt, listener);
		}

		super.addScriptingListenersOn(elt);
	}

	/**
	 * Removes the scripting listeners from the given element.
	 */
	@Override
	protected void removeScriptingListenersOn(Element elt) {
		String eltNS = elt.getNamespaceURI();
		String eltLN = elt.getLocalName();
		if (SVGConstants.SVG_NAMESPACE_URI.equals(eltNS) && SVG12Constants.SVG_HANDLER_TAG.equals(eltLN)) {
			// For this 'handler' element, remove the handler for the given
			// event type.
			AbstractElement tgt = (AbstractElement) elt.getParentNode();
			String eventType = elt.getAttributeNS(XMLConstants.XML_EVENTS_NAMESPACE_URI,
					XMLConstants.XML_EVENTS_EVENT_ATTRIBUTE);
			String eventNamespaceURI = XMLConstants.XML_EVENTS_NAMESPACE_URI;
			if (eventType.indexOf(':') != -1) {
				String prefix = DOMUtilities.getPrefix(eventType);
				eventType = DOMUtilities.getLocalName(eventType);
				eventNamespaceURI = elt.lookupNamespaceURI(prefix);
			}

			EventListener listener = (EventListener) handlerScriptingListeners.put(eventNamespaceURI, eventType, elt,
					null);
			tgt.removeEventListenerNS(eventNamespaceURI, eventType, listener, false);
		}

		super.removeScriptingListenersOn(elt);
	}

	/**
	 * To handle a scripting event with an XML Events style handler.
	 */
	protected class HandlerScriptingEventListener implements EventListener {

		/**
		 * The namespace URI of the event type.
		 */
		protected String eventNamespaceURI;

		/**
		 * The event type.
		 */
		protected String eventType;

		/**
		 * The handler element.
		 */
		protected AbstractElement handlerElement;

		/**
		 * Creates a new HandlerScriptingEventListener.
		 * 
		 * @param ns Namespace URI of the event type.
		 * @param et The event type.
		 * @param e  The handler element.
		 */
		public HandlerScriptingEventListener(String ns, String et, AbstractElement e) {
			eventNamespaceURI = ns;
			eventType = et;
			handlerElement = e;
		}

		/**
		 * Runs the script.
		 */
		@Override
		public void handleEvent(Event evt) {
			Element elt = (Element) evt.getCurrentTarget();
			// Evaluate the script
			String script = handlerElement.getTextContent();
			if (script.length() == 0)
				return;

			DocumentLoader dl = bridgeContext.getDocumentLoader();
			AbstractDocument d = (AbstractDocument) handlerElement.getOwnerDocument();
			int line = dl.getLineNumber(handlerElement);
			final String desc = Messages.formatMessage(HANDLER_SCRIPT_DESCRIPTION,
					new Object[] { d.getDocumentURI(), eventNamespaceURI, eventType, line });

			// Find the scripting language
			String lang = handlerElement.getAttributeNS(null, SVGConstants.SVG_CONTENT_SCRIPT_TYPE_ATTRIBUTE);
			if (lang.length() == 0) {
				Element e = elt;
				while (e != null && (!SVGConstants.SVG_NAMESPACE_URI.equals(e.getNamespaceURI())
						|| !SVGConstants.SVG_SVG_TAG.equals(e.getLocalName()))) {
					e = SVGUtilities.getParentElement(e);
				}
				if (e == null)
					return;

				lang = e.getAttributeNS(null, SVGConstants.SVG_CONTENT_SCRIPT_TYPE_ATTRIBUTE);
			}

			runEventHandler(script, evt, lang, desc);
		}

	}

	/**
	 * Creates a new Window object.
	 */
	@Override
	public io.sf.carte.echosvg.bridge.Window createWindow(Interpreter interp, String lang) {
		return new Global(interp, lang);
	}

	/**
	 * The SVGGlobal object.
	 */
	protected class Global extends ScriptingEnvironment.Window implements SVGGlobal {

		/**
		 * Creates a new Global object.
		 */
		public Global(Interpreter interp, String lang) {
			super(interp, lang);
		}

		/**
		 * Implements
		 * {@link io.sf.carte.echosvg.dom.svg12.SVGGlobal#startMouseCapture(EventTarget,boolean,boolean)}.
		 */
		@Override
		public void startMouseCapture(EventTarget target, boolean sendAll, boolean autoRelease) {
			// XXX not sure if it's right to do this on the
			// primary bridge context
			((SVG12BridgeContext) bridgeContext.getPrimaryBridgeContext()).startMouseCapture(target, sendAll,
					autoRelease);
		}

		/**
		 * Stops mouse capture.
		 */
		@Override
		public void stopMouseCapture() {
			// XXX not sure if it's right to do this on the
			// primary bridge context
			((SVG12BridgeContext) bridgeContext.getPrimaryBridgeContext()).stopMouseCapture();
		}

	}

}
