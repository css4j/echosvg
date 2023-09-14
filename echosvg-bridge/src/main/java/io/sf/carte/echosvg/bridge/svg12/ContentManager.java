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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.event.EventListenerList;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

import io.sf.carte.echosvg.anim.dom.SVGOMElement;
import io.sf.carte.echosvg.anim.dom.XBLEventSupport;
import io.sf.carte.echosvg.anim.dom.XBLOMContentElement;
import io.sf.carte.echosvg.anim.dom.XBLOMShadowTreeElement;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractNode;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;
import io.sf.carte.echosvg.dom.xbl.XBLManager;
import io.sf.carte.echosvg.util.XBLConstants;

/**
 * A class to manage all XBL content elements in a shadow tree.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ContentManager {

	/**
	 * The shadow tree whose content elements this object is managing.
	 */
	protected XBLOMShadowTreeElement shadowTree;

	/**
	 * The bound element that owns the shadow tree.
	 */
	protected Element boundElement;

	/**
	 * The XBL manager.
	 */
	protected DefaultXBLManager xblManager;

	/**
	 * Map of content elements to selectors. [XBLContentElement,
	 * AbstractContentSelector]
	 */
	protected HashMap<Node, AbstractContentSelector> selectors = new HashMap<>();

	/**
	 * Map of content elements to a list of nodes that were selected by that content
	 * element. [XBLContentElement, NodeList]
	 */
	protected HashMap<Node, NodeList> selectedNodes = new HashMap<>();

	/**
	 * List of content elements. [XBLContentElement]
	 */
	protected LinkedList<SVGOMElement> contentElementList = new LinkedList<>();

	/**
	 * The recently removed node from the shadow tree.
	 */
	protected Node removedNode;

	/**
	 * Map of XBLContentElement objects to EventListenerList objects.
	 */
	protected HashMap<XBLOMContentElement, EventListenerList> listeners = new HashMap<>();

	/**
	 * DOMAttrModified listener for content elements.
	 */
	protected ContentElementDOMAttrModifiedEventListener contentElementDomAttrModifiedEventListener;

	/**
	 * DOMAttrModified listener for bound element children.
	 */
	protected DOMAttrModifiedEventListener domAttrModifiedEventListener;

	/**
	 * DOMNodeInserted listener for bound element children.
	 */
	protected DOMNodeInsertedEventListener domNodeInsertedEventListener;

	/**
	 * DOMNodeRemoved listener for bound element children.
	 */
	protected DOMNodeRemovedEventListener domNodeRemovedEventListener;

	/**
	 * DOMSubtreeModified listener for shadow tree nodes.
	 */
	protected DOMSubtreeModifiedEventListener domSubtreeModifiedEventListener;

	/**
	 * DOMNodeInserted listener for content elements in the shadow tree.
	 */
	protected ShadowTreeNodeInsertedListener shadowTreeNodeInsertedListener;

	/**
	 * DOMNodeRemoved listener for content elements in the shadow tree.
	 */
	protected ShadowTreeNodeRemovedListener shadowTreeNodeRemovedListener;

	/**
	 * DOMSubtreeModified listener for content elements in the shadow tree.
	 */
	protected ShadowTreeSubtreeModifiedListener shadowTreeSubtreeModifiedListener;

	/**
	 * Creates a new ContentManager object.
	 * 
	 * @param s  the shadow tree element whose content elements this object will be
	 *           managing
	 * @param xm the XBLManager for this document
	 */
	public ContentManager(XBLOMShadowTreeElement s, XBLManager xm) {
		shadowTree = s;
		xblManager = (DefaultXBLManager) xm;

		xblManager.setContentManager(s, this);
		boundElement = xblManager.getXblBoundElement(s);

		contentElementDomAttrModifiedEventListener = new ContentElementDOMAttrModifiedEventListener();

		XBLEventSupport es = (XBLEventSupport) shadowTree.initializeEventSupport();
		shadowTreeNodeInsertedListener = new ShadowTreeNodeInsertedListener();
		shadowTreeNodeRemovedListener = new ShadowTreeNodeRemovedListener();
		shadowTreeSubtreeModifiedListener = new ShadowTreeSubtreeModifiedListener();
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted",
				shadowTreeNodeInsertedListener, true);
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved",
				shadowTreeNodeRemovedListener, true);
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMSubtreeModified",
				shadowTreeSubtreeModifiedListener, true);

		es = (XBLEventSupport) ((AbstractNode) boundElement).initializeEventSupport();
		domAttrModifiedEventListener = new DOMAttrModifiedEventListener();
		domNodeInsertedEventListener = new DOMNodeInsertedEventListener();
		domNodeRemovedEventListener = new DOMNodeRemovedEventListener();
		domSubtreeModifiedEventListener = new DOMSubtreeModifiedEventListener();
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified",
				domAttrModifiedEventListener, true);
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted",
				domNodeInsertedEventListener, true);
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved",
				domNodeRemovedEventListener, true);
		es.addImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMSubtreeModified",
				domSubtreeModifiedEventListener, false);

		update(true);
	}

	/**
	 * Disposes this ContentManager.
	 */
	public void dispose() {
		xblManager.setContentManager(shadowTree, null);

		for (Entry<Node, NodeList> e : selectedNodes.entrySet()) {
			NodeList nl = e.getValue();
			for (int j = 0; j < nl.getLength(); j++) {
				Node n = nl.item(j);
				xblManager.getRecord(n).contentElement = null;
			}
		}

		for (NodeEventTarget n : contentElementList) {
			n.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified",
					contentElementDomAttrModifiedEventListener, false);
		}

		contentElementList.clear();
		selectedNodes.clear();

		XBLEventSupport es = (XBLEventSupport) ((AbstractNode) boundElement).getEventSupport();
		es.removeImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified",
				domAttrModifiedEventListener, true);
		es.removeImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted",
				domNodeInsertedEventListener, true);
		es.removeImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved",
				domNodeRemovedEventListener, true);
		es.removeImplementationEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMSubtreeModified",
				domSubtreeModifiedEventListener, false);
	}

	/**
	 * Returns a NodeList of the content that was selected by the given content
	 * element.
	 */
	public NodeList getSelectedContent(XBLOMContentElement e) {
		return selectedNodes.get(e);
	}

	/**
	 * Returns the content element that selected a given node.
	 */
	protected XBLOMContentElement getContentElement(Node n) {
		return xblManager.getXblContentElement(n);
	}

	/**
	 * Adds the specified ContentSelectionChangedListener to the listener list.
	 */
	public void addContentSelectionChangedListener(XBLOMContentElement e, ContentSelectionChangedListener l) {
		EventListenerList ll = listeners.get(e);
		if (ll == null) {
			ll = new EventListenerList();
			listeners.put(e, ll);
		}
		ll.add(ContentSelectionChangedListener.class, l);
	}

	/**
	 * Removes the specified ContentSelectionChangedListener from the listener list.
	 */
	public void removeContentSelectionChangedListener(XBLOMContentElement e, ContentSelectionChangedListener l) {
		EventListenerList ll = listeners.get(e);
		if (ll != null) {
			ll.remove(ContentSelectionChangedListener.class, l);
		}
	}

	/**
	 * Dispatches the ContentSelectionChangedEvent to the registered listeners.
	 */
	protected void dispatchContentSelectionChangedEvent(XBLOMContentElement e) {
		xblManager.invalidateChildNodes(e.getXblParentNode());

		ContentSelectionChangedEvent evt = new ContentSelectionChangedEvent(e);

		EventListenerList ll = listeners.get(e);
		if (ll != null) {
			Object[] ls = ll.getListenerList();
			for (int i = ls.length - 2; i >= 0; i -= 2) {
				ContentSelectionChangedListener l = (ContentSelectionChangedListener) ls[i + 1];
				l.contentSelectionChanged(evt);
			}
		}

		Object[] ls = xblManager.getContentSelectionChangedListeners();
		for (int i = ls.length - 2; i >= 0; i -= 2) {
			ContentSelectionChangedListener l = (ContentSelectionChangedListener) ls[i + 1];
			l.contentSelectionChanged(evt);
		}
	}

	/**
	 * Updates all content elements.
	 * 
	 * @param first Whether this is the first update for this ContentManager.
	 */
	protected void update(boolean first) {
		HashSet<Node> previouslySelectedNodes = new HashSet<>();
		Iterator<Entry<Node, NodeList>> i = selectedNodes.entrySet().iterator();
		while (i.hasNext()) {
			Entry<Node, NodeList> e = i.next();
			NodeList nl = e.getValue();
			for (int j = 0; j < nl.getLength(); j++) {
				Node n = nl.item(j);
				xblManager.getRecord(n).contentElement = null;
				previouslySelectedNodes.add(n);
			}
		}

		for (NodeEventTarget n : contentElementList) {
			n.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified",
					contentElementDomAttrModifiedEventListener, false);
		}

		contentElementList.clear();
		selectedNodes.clear();

		boolean updated = false;
		for (Node n = shadowTree.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (update(first, n)) {
				updated = true;
			}
		}

		if (updated) {
			HashSet<Node> newlySelectedNodes = new HashSet<>();
			i = selectedNodes.entrySet().iterator();
			while (i.hasNext()) {
				Entry<Node, NodeList> e = i.next();
				NodeList nl = e.getValue();
				for (int j = 0; j < nl.getLength(); j++) {
					Node n = nl.item(j);
					newlySelectedNodes.add(n);
				}
			}

			HashSet<Node> removed = new HashSet<>(previouslySelectedNodes);
			removed.removeAll(newlySelectedNodes);

			HashSet<Node> added = new HashSet<>();
			added.addAll(newlySelectedNodes);
			added.removeAll(previouslySelectedNodes);

			if (!first) {
				xblManager.shadowTreeSelectedContentChanged(removed, added);
			}
		}
	}

	protected boolean update(boolean first, Node n) {
		boolean updated = false;
		for (Node m = n.getFirstChild(); m != null; m = m.getNextSibling()) {
			if (update(first, m)) {
				updated = true;
			}
		}
		if (n instanceof XBLOMContentElement) {
			XBLOMContentElement e = (XBLOMContentElement) n;
			contentElementList.add(e);
			e.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified",
					contentElementDomAttrModifiedEventListener, false, null);
			AbstractContentSelector s = selectors.get(n);
			boolean changed;
			if (s == null) {
				if (e.hasAttributeNS(null, XBLConstants.XBL_INCLUDES_ATTRIBUTE)) {
					String lang = getContentSelectorLanguage(e);
					String selector = e.getAttributeNS(null, XBLConstants.XBL_INCLUDES_ATTRIBUTE);
					s = AbstractContentSelector.createSelector(lang, this, e, boundElement, selector);
				} else {
					s = new DefaultContentSelector(this, e, boundElement);
				}
				selectors.put(n, s);
				changed = true;
			} else {
				changed = s.update();
			}
			NodeList selectedContent = s.getSelectedContent();
			selectedNodes.put(n, selectedContent);
			for (int i = 0; i < selectedContent.getLength(); i++) {
				Node m = selectedContent.item(i);
				xblManager.getRecord(m).contentElement = e;
			}
			if (changed) {
				updated = true;
				dispatchContentSelectionChangedEvent(e);
			}
		}
		return updated;
	}

	/**
	 * Returns the selector language to be used for the given xbl:content element.
	 * This will look at the xbl:content element and the document element for an
	 * attribute batik:selectorLanguage.
	 */
	protected String getContentSelectorLanguage(Element e) {
		String lang = e.getAttributeNS("http://xml.apache.org/batik/ext", "selectorLanguage");
		if (lang.length() != 0) {
			return lang;
		}
		lang = e.getOwnerDocument().getDocumentElement().getAttributeNS("http://xml.apache.org/batik/ext",
				"selectorLanguage");
		if (lang.length() != 0) {
			return lang;
		}
		return null;
	}

	/**
	 * The DOM EventListener invoked when an attribute is modified, for content
	 * elements.
	 */
	protected class ContentElementDOMAttrModifiedEventListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			MutationEvent me = (MutationEvent) evt;
			Attr a = (Attr) me.getRelatedNode();
			Element e = (Element) evt.getTarget();
			if (e instanceof XBLOMContentElement) {
				String ans = a.getNamespaceURI();
				String aln = a.getLocalName();
				if (aln == null) {
					aln = a.getNodeName();
				}
				if (ans == null && XBLConstants.XBL_INCLUDES_ATTRIBUTE.equals(aln)
						|| "http://xml.apache.org/batik/ext".equals(ans) && "selectorLanguage".equals(aln)) {
					selectors.remove(e);
					update(false);
				}
			}
		}
	}

	/**
	 * The DOM EventListener invoked when an attribute is modified.
	 */
	protected class DOMAttrModifiedEventListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			if (evt.getTarget() != boundElement) {
				update(false);
			}
		}
	}

	/**
	 * The DOM EventListener invoked when a node is added.
	 */
	protected class DOMNodeInsertedEventListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			update(false);
		}
	}

	/**
	 * The DOM EventListener invoked when a node is removed.
	 */
	protected class DOMNodeRemovedEventListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			removedNode = (Node) evt.getTarget();
		}
	}

	/**
	 * The DOM EventListener invoked when a subtree has changed.
	 */
	protected class DOMSubtreeModifiedEventListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			if (removedNode != null) {
				removedNode = null;
				update(false);
			}
		}

	}

	/**
	 * The DOM EventListener invoked when a node in the shadow tree has been
	 * inserted.
	 */
	protected class ShadowTreeNodeInsertedListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			if (evt.getTarget() instanceof XBLOMContentElement) {
				update(false);
			}
		}
	}

	/**
	 * The DOM EventListener invoked when a node in the shadow tree has been
	 * removed.
	 */
	protected class ShadowTreeNodeRemovedListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			EventTarget target = evt.getTarget();
			if (target instanceof XBLOMContentElement) {
				removedNode = (Node) evt.getTarget();
			}
		}
	}

	/**
	 * The DOM EventListener invoked when a subtree of the shadow tree has changed.
	 */
	protected class ShadowTreeSubtreeModifiedListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			if (removedNode != null) {
				removedNode = null;
				update(false);
			}
		}
	}
}
