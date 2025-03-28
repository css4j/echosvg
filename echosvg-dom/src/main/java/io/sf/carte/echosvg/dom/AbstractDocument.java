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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMLocator;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.events.DocumentEventSupport;
import io.sf.carte.echosvg.dom.events.EventSupport;
import io.sf.carte.echosvg.dom.traversal.TraversalSupport;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.dom.xbl.GenericXBLManager;
import io.sf.carte.echosvg.dom.xbl.XBLManager;
import io.sf.carte.echosvg.i18n.Localizable;
import io.sf.carte.echosvg.i18n.LocalizableSupport;
import io.sf.carte.echosvg.util.CleanerThread;
import io.sf.carte.echosvg.util.DOMConstants;
import io.sf.carte.echosvg.util.SoftDoublyIndexedTable;
import io.sf.carte.echosvg.w3c.dom.events.MutationNameEvent;
import io.sf.carte.echosvg.xml.XMLUtilities;

/**
 * This class implements the {@link org.w3c.dom.Document} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractDocument extends AbstractParentNode
		implements Document, DocumentEvent, DocumentTraversal, Localizable, XPathEvaluator {

	private static final long serialVersionUID = 1L;

	/**
	 * The error messages bundle class name.
	 */
	protected static final String RESOURCES = "io.sf.carte.echosvg.dom.resources.Messages";

	/**
	 * The localizable support for the error messages.
	 */
	protected transient LocalizableSupport localizableSupport = new LocalizableSupport(RESOURCES,
			getClass().getClassLoader());

	/**
	 * The DOM implementation.
	 */
	protected transient DOMImplementation implementation;

	/**
	 * The traversal support.
	 */
	protected transient TraversalSupport traversalSupport;

	/**
	 * The DocumentEventSupport.
	 */
	protected transient DocumentEventSupport documentEventSupport;

	/**
	 * Whether the event dispatching must be done.
	 */
	protected transient boolean eventsEnabled;

	/**
	 * The ElementsByTagName lists.
	 */
	protected transient WeakHashMap<Node, SoftDoublyIndexedTable<String, String>> elementsByTagNames;

	/**
	 * The ElementsByTagNameNS lists.
	 */
	protected transient WeakHashMap<Node, SoftDoublyIndexedTable<String, String>> elementsByTagNamesNS;

	/**
	 * Input encoding of this document.
	 */
	protected String inputEncoding;

	/**
	 * XML encoding of this document.
	 */
	protected String xmlEncoding;

	/**
	 * XML version of this document.
	 */
	protected String xmlVersion = XMLConstants.XML_VERSION_10;

	/**
	 * Whether this document is standalone.
	 */
	protected boolean xmlStandalone;

	/**
	 * The document URI.
	 */
	protected String documentURI;

	/**
	 * Whether strict error checking is in force.
	 */
	protected boolean strictErrorChecking = true;

	/**
	 * The DOMConfiguration object for this document.
	 */
	protected DocumentConfiguration domConfig;

	/**
	 * The XBL manager for this document.
	 */
	protected transient XBLManager xblManager = new GenericXBLManager();

	/**
	 * The elementsById lists. This is keyed on 'id'. the entry is either a
	 * IdSoftReference to the element or a List of IdSoftReferences (if there is
	 * more than one element owned by this document with a particular 'id').
	 */
	protected transient Map<String, Object> elementsById;

	/**
	 * The ID lock.
	 * <p>
	 * A lock is used to handle elementsById instead of using a
	 * {@code ConcurrentHashMap} because we have to operate inside the Map values.
	 * </p>
	 */
	private transient ReentrantLock idLock = new ReentrantLock();

	/**
	 * Creates a new document.
	 */
	protected AbstractDocument() {
	}

	/**
	 * Creates a new document.
	 */
	public AbstractDocument(DocumentType dt, DOMImplementation impl) {
		implementation = impl;
		if (dt != null) {
			if (dt instanceof GenericDocumentType) {
				GenericDocumentType gdt = (GenericDocumentType) dt;
				if (gdt.getOwnerDocument() == null)
					gdt.setOwnerDocument(this);
			}
			appendChild(dt);
		}
	}

	/**
	 * Sets the input encoding that was used when the document was being parsed.
	 */
	public void setDocumentInputEncoding(String ie) {
		inputEncoding = ie;
	}

	/**
	 * Sets the XML encoding that was found in the XML prolog.
	 */
	public void setDocumentXmlEncoding(String xe) {
		xmlEncoding = xe;
	}

	/**
	 * Implements {@link io.sf.carte.echosvg.i18n.Localizable#setLocale(Locale)}.
	 */
	@Override
	public void setLocale(Locale l) {
		localizableSupport.setLocale(l);
	}

	/**
	 * Implements {@link io.sf.carte.echosvg.i18n.Localizable#getLocale()}.
	 */
	@Override
	public Locale getLocale() {
		return localizableSupport.getLocale();
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.i18n.Localizable#formatMessage(String,Object[])}.
	 */
	@Override
	public String formatMessage(String key, Object[] args) throws MissingResourceException {
		return localizableSupport.formatMessage(key, args);
	}

	/**
	 * Tests whether the event dispatching must be done.
	 */
	public boolean getEventsEnabled() {
		return eventsEnabled;
	}

	/**
	 * Sets the eventsEnabled property.
	 */
	public void setEventsEnabled(boolean b) {
		eventsEnabled = b;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
	 * 
	 * @return "#document".
	 */
	@Override
	public String getNodeName() {
		return "#document";
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeType()}.
	 * 
	 * @return {@link org.w3c.dom.Node#DOCUMENT_NODE}
	 */
	@Override
	public short getNodeType() {
		return DOCUMENT_NODE;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getDoctype()}.
	 */
	@Override
	public DocumentType getDoctype() {
		for (Node n = getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() == DOCUMENT_TYPE_NODE) {
				return (DocumentType) n;
			}
		}
		return null;
	}

	/**
	 * Sets the document type node.
	 */
	public void setDoctype(DocumentType dt) {
		if (dt != null) {
			appendChild(dt);
			((ExtendedNode) dt).setReadonly(true);
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getImplementation()}.
	 * 
	 * @return {@link #implementation}
	 */
	@Override
	public DOMImplementation getImplementation() {
		return implementation;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getDocumentElement()}.
	 */
	@Override
	public Element getDocumentElement() {
		for (Node n = getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() == ELEMENT_NODE) {
				return (Element) n;
			}
		}
		return null;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#importNode(Node,boolean)}.
	 */
	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		return importNode(importedNode, deep, false);
	}

	/**
	 * Imports the given node into this document. It does so deeply if
	 * <code>deep</code> is set to true. It will not mark ID attributes as IDs if
	 * <code>trimId</code> is set to true. This is used primarily for the shadow
	 * trees of the 'use' elements so they don't clutter the hash table.
	 *
	 * @param importedNode The node to import into this document.
	 * @param deep         Whether to perform a deep importation.
	 * @param trimId       Whether to make all cloned attributes not be ID
	 *                     attributes.
	 */
	public Node importNode(Node importedNode, boolean deep, boolean trimId) {
		/*
		 * The trimming of id's is used by the 'use' element to keep down the amount of
		 * 'bogus' id's in the hashtable.
		 */
		Node result;
		switch (importedNode.getNodeType()) {
		case ELEMENT_NODE:
			result = importElement(importedNode, trimId);
			break;

		case ATTRIBUTE_NODE:
			result = createAttributeNS(importedNode.getNamespaceURI(), importedNode.getNodeName());
			result.setNodeValue(importedNode.getNodeValue());
			deep = false;
			break;

		case TEXT_NODE:
			result = createTextNode(importedNode.getNodeValue());
			deep = false;
			break;

		case CDATA_SECTION_NODE:
			result = createCDATASection(importedNode.getNodeValue());
			deep = false;
			break;

		case ENTITY_REFERENCE_NODE:
			result = createEntityReference(importedNode.getNodeName());
			break;

		case PROCESSING_INSTRUCTION_NODE:
			result = createProcessingInstruction(importedNode.getNodeName(), importedNode.getNodeValue());
			deep = false;
			break;

		case COMMENT_NODE:
			result = createComment(importedNode.getNodeValue());
			deep = false;
			break;

		case DOCUMENT_FRAGMENT_NODE:
			result = createDocumentFragment();
			break;

		case DOCUMENT_TYPE_NODE:
			DocumentType docType = (DocumentType) importedNode;
			GenericDocumentType copy = new GenericDocumentType(docType.getName(), docType.getPublicId(),
					docType.getSystemId());
			copy.ownerDocument = this;
			result = copy;
			break;

		default:
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "import.node", new Object[] {});
		}

		if (importedNode instanceof AbstractNode) {
			// Only fire the UserDataHandler if the imported node is from
			// EchoSVG's DOM implementation.
			fireUserDataHandlers(UserDataHandler.NODE_IMPORTED, importedNode, result);
		}

		if (deep) {
			for (Node n = importedNode.getFirstChild(); n != null; n = n.getNextSibling()) {
				result.appendChild(importNode(n, true));
			}
		}
		return result;
	}

	protected Element importElement(Node importMe, boolean trimId) {
		Element e = createElementNS(importMe.getNamespaceURI(), importMe.getNodeName());
		if (importMe.hasAttributes()) {
			NamedNodeMap attr = importMe.getAttributes();
			int len = attr.getLength();
			for (int i = 0; i < len; i++) {
				Attr a = (Attr) attr.item(i);
				if (!a.getSpecified())
					continue;
				AbstractAttr aa = (AbstractAttr) importNode(a, true);
				if (trimId && aa.isId())
					aa.setIsId(false); // don't consider this an Id.
				e.setAttributeNodeNS(aa);
			}
		}
		return e;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#cloneNode(boolean)}.
	 */
	@Override
	public Node cloneNode(boolean deep) {
		Document n = (Document) newNode();
		copyInto(n);
		fireUserDataHandlers(UserDataHandler.NODE_CLONED, this, n);
		if (deep) {
			for (Node c = getFirstChild(); c != null; c = c.getNextSibling()) {
				n.appendChild(n.importNode(c, deep));
			}
		}
		return n;
	}

	/**
	 * Returns whether the given attribute node is an ID attribute.
	 */
	public abstract boolean isId(Attr node);

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getElementById(String)}.
	 */
	@Override
	public Element getElementById(String id) {
		return getChildElementById(getDocumentElement(), id);
	}

	/**
	 * Finds an element that is in the same document fragment as 'requestor' that
	 * has 'id'.
	 */
	public Element getChildElementById(Node requestor, String id) {
		if (id == null || id.length() == 0)
			return null;
		if (elementsById == null)
			return null;

		Node root = getRoot(requestor);

		Object o = elementsById.get(id);
		if (o == null)
			return null;
		if (o instanceof IdSoftRef) {
			o = ((IdSoftRef) o).get();
			if (o == null) {
				idLock.lock();
				try {
					elementsById.remove(id);
				} finally {
					idLock.unlock();
				}
				return null;
			}
			Element e = (Element) o;
			if (getRoot(e) == root)
				return e;
			return null;
		}

		// Not a IdSoftRef so it must be a list.
		@SuppressWarnings("unchecked")
		List<IdSoftRef> l = (List<IdSoftRef>) o;
		Iterator<IdSoftRef> li = l.iterator();
		while (li.hasNext()) {
			IdSoftRef sr = li.next();
			o = sr.get();
			if (o == null) {
				try {
					li.remove();
				} catch (IllegalStateException e) {
					// Prevent (unlikely) race condition
				}
			} else {
				Element e = (Element) o;
				if (getRoot(e) == root)
					return e;
			}
		}
		return null;
	}

	protected Node getRoot(Node n) {
		Node r = n;
		while (n != null) {
			r = n;
			n = n.getParentNode();
		}
		return r;
	}

	protected class IdSoftRef extends CleanerThread.SoftReferenceCleared<Object> {

		String id;
		List<IdSoftRef> list;

		IdSoftRef(Object o, String id) {
			super(o);
			this.id = id;
		}

		IdSoftRef(Object o, String id, List<IdSoftRef> list) {
			super(o);
			this.id = id;
			this.list = list;
		}

		public void setList(List<IdSoftRef> list) {
			this.list = list;
		}

		@Override
		public void cleared() {
			if (elementsById == null)
				return;
			idLock.lock();
			try {
				if (list != null)
					list.remove(this);
				else {
					Object o = elementsById.remove(id);
					if (o != this) // oops not us!
						elementsById.put(id, o);
				}
			} finally {
				idLock.unlock();
			}
		}

	}

	/**
	 * Remove the mapping for <code>element</code> to <code>id</code>
	 */
	public void removeIdEntry(Element e, String id) {
		// Remove old Id mapping if we have one.
		if (id == null)
			return;
		if (elementsById == null)
			return;

		idLock.lock();
		try {
			Object o = elementsById.get(id);
			if (o == null)
				return;

			if (o instanceof IdSoftRef) {
				elementsById.remove(id);
				return;
			}

			@SuppressWarnings("unchecked")
			List<IdSoftRef> l = (List<IdSoftRef>) o;
			Iterator<IdSoftRef> li = l.iterator();
			while (li.hasNext()) {
				IdSoftRef ip = li.next();
				o = ip.get();
				if (o == null) {
					try {
						li.remove();
					} catch (IllegalStateException ex) {
						/*
						 * Prevent unlikely race condition: void soft refs in lists can be modified
						 * outside of the lock.
						 */
					}
				} else if (e == o) {
					li.remove();
					break;
				}
			}

			if (l.isEmpty()) {
				elementsById.remove(id);
			}
		} finally {
			idLock.unlock();
		}
	}

	public void addIdEntry(Element e, String id) {
		if (id == null)
			return;

		idLock.lock();
		try {
			if (elementsById == null) {
				elementsById = new HashMap<>();
				elementsById.put(id, new IdSoftRef(e, id));
				return;
			}

			// Add new Id mapping.
			Object o = elementsById.get(id);
			if (o == null) {
				elementsById.put(id, new IdSoftRef(e, id));
				return;
			}
			if (o instanceof IdSoftRef) {
				IdSoftRef ip = (IdSoftRef) o;
				Object r = ip.get();
				if (r == null) { // reference is gone so replace it.
					elementsById.put(id, new IdSoftRef(e, id));
					return;
				}

				// Create new List for this id.
				List<IdSoftRef> l = new ArrayList<>(4);
				ip.setList(l);
				l.add(ip);
				l.add(new IdSoftRef(e, id, l));
				elementsById.put(id, l);
				return;
			}

			@SuppressWarnings("unchecked")
			List<IdSoftRef> l = (List<IdSoftRef>) o;
			l.add(new IdSoftRef(e, id, l));
		} finally {
			idLock.unlock();
		}
	}

	public void updateIdEntry(Element e, String oldId, String newId) {
		if (oldId == newId || (oldId != null && oldId.equals(newId)))
			return;

		removeIdEntry(e, oldId);

		addIdEntry(e, newId);
	}

	/**
	 * Returns an ElementsByTagName object from the cache, if any.
	 */
	public ElementsByTagName getElementsByTagName(Node n, String ln) {
		if (elementsByTagNames == null) {
			return null;
		}
		SoftDoublyIndexedTable<String, String> t = elementsByTagNames.get(n);
		if (t == null) {
			return null;
		}
		return (ElementsByTagName) t.get(null, ln);
	}

	/**
	 * Puts an ElementsByTagName object in the cache.
	 */
	public void putElementsByTagName(Node n, String ln, ElementsByTagName l) {
		if (elementsByTagNames == null) {
			elementsByTagNames = new WeakHashMap<>(11);
		}
		SoftDoublyIndexedTable<String, String> t;
		t = elementsByTagNames.get(n);
		if (t == null) {
			elementsByTagNames.put(n, t = new SoftDoublyIndexedTable<>());
		}
		t.put(null, ln, l);
	}

	/**
	 * Returns an ElementsByTagNameNS object from the cache, if any.
	 */
	public ElementsByTagNameNS getElementsByTagNameNS(Node n, String ns, String ln) {
		if (elementsByTagNamesNS == null) {
			return null;
		}
		SoftDoublyIndexedTable<String, String> t;
		t = elementsByTagNamesNS.get(n);
		if (t == null) {
			return null;
		}
		return (ElementsByTagNameNS) t.get(ns, ln);
	}

	/**
	 * Puts an ElementsByTagNameNS object in the cache.
	 */
	public void putElementsByTagNameNS(Node n, String ns, String ln, ElementsByTagNameNS l) {
		if (elementsByTagNamesNS == null) {
			elementsByTagNamesNS = new WeakHashMap<>(11);
		}
		SoftDoublyIndexedTable<String, String> t;
		t = elementsByTagNamesNS.get(n);
		if (t == null) {
			elementsByTagNamesNS.put(n, t = new SoftDoublyIndexedTable<>());
		}
		t.put(ns, ln, l);
	}

	// DocumentEvent /////////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.events.DocumentEvent#createEvent(String)}.
	 */
	@Override
	public Event createEvent(String eventType) throws DOMException {
		if (documentEventSupport == null) {
			documentEventSupport = ((AbstractDOMImplementation) implementation).createDocumentEventSupport();
		}
		return documentEventSupport.createEvent(eventType);
	}

	/**
	 * <b>DOM</b>: Implements
	 * org.w3c.dom.events.DocumentEvent#canDispatch(String,String).
	 */
	public boolean canDispatch(String ns, String eventType) {
		if (eventType == null) {
			return false;
		}
		if (ns != null && ns.length() == 0) {
			ns = null;
		}
		if (ns == null || ns.equals(XMLConstants.XML_EVENTS_NAMESPACE_URI)) {
			return eventType.equals("Event") || eventType.equals("MutationEvent")
					|| eventType.equals("MutationNameEvent") || eventType.equals("UIEvent")
					|| eventType.equals("MouseEvent") || eventType.equals("KeyEvent")
					|| eventType.equals("KeyboardEvent") || eventType.equals("TextEvent")
					|| eventType.equals("CustomEvent");
		}
		return false;
	}

	// DocumentTraversal /////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link DocumentTraversal#createNodeIterator(Node,int,NodeFilter,boolean)}.
	 */
	@Override
	public NodeIterator createNodeIterator(Node root, int whatToShow, NodeFilter filter,
			boolean entityReferenceExpansion) throws DOMException {
		if (traversalSupport == null) {
			traversalSupport = new TraversalSupport();
		}
		return traversalSupport.createNodeIterator(this, root, whatToShow, filter, entityReferenceExpansion);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link DocumentTraversal#createTreeWalker(Node,int,NodeFilter,boolean)}.
	 */
	@Override
	public TreeWalker createTreeWalker(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion)
			throws DOMException {
		return TraversalSupport.createTreeWalker(this, root, whatToShow, filter, entityReferenceExpansion);
	}

	/**
	 * Detaches the given node iterator from this document.
	 */
	public void detachNodeIterator(NodeIterator it) {
		traversalSupport.detachNodeIterator(it);
	}

	/**
	 * Notifies this document that a node will be removed.
	 */
	@Override
	public void nodeToBeRemoved(Node node) {
		if (traversalSupport != null) {
			traversalSupport.nodeToBeRemoved(node);
		}
	}

	/**
	 * Returns the current document.
	 */
	@Override
	protected AbstractDocument getCurrentDocument() {
		return this;
	}

	/**
	 * Exports this node to the given document.
	 * 
	 * @param n The clone node.
	 * @param d The destination document.
	 */
	protected Node export(Node n, Document d) {
		throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "import.document", new Object[] {});
	}

	/**
	 * Deeply exports this node to the given document.
	 * 
	 * @param n The clone node.
	 * @param d The destination document.
	 */
	protected Node deepExport(Node n, Document d) {
		throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "import.document", new Object[] {});
	}

	/**
	 * Copy the fields of the current node into the given node.
	 * 
	 * @param n a node of the type of this.
	 */
	@Override
	protected Node copyInto(Node n) {
		super.copyInto(n);
		AbstractDocument ad = (AbstractDocument) n;
		ad.implementation = implementation;
		ad.localizableSupport = new LocalizableSupport(RESOURCES, getClass().getClassLoader());
		ad.inputEncoding = inputEncoding;
		ad.xmlEncoding = xmlEncoding;
		ad.xmlVersion = xmlVersion;
		ad.xmlStandalone = xmlStandalone;
		ad.documentURI = documentURI;
		ad.strictErrorChecking = strictErrorChecking;
		// XXX clone DocumentConfiguration?
		return n;
	}

	/**
	 * Deeply copy the fields of the current node into the given node.
	 * 
	 * @param n a node of the type of this.
	 */
	@Override
	protected Node deepCopyInto(Node n) {
		super.deepCopyInto(n);
		AbstractDocument ad = (AbstractDocument) n;
		ad.implementation = implementation;
		ad.localizableSupport = new LocalizableSupport(RESOURCES, getClass().getClassLoader());
		return n;
	}

	/**
	 * Checks the validity of a node to be inserted.
	 */
	@Override
	protected void checkChildType(Node n, boolean replace) {
		short t = n.getNodeType();
		switch (t) {
		case ELEMENT_NODE:
		case PROCESSING_INSTRUCTION_NODE:
		case COMMENT_NODE:
		case DOCUMENT_TYPE_NODE:
		case DOCUMENT_FRAGMENT_NODE:
			break;
		default:
			throw createDOMException(DOMException.HIERARCHY_REQUEST_ERR, "child.type",
					new Object[] { (int) getNodeType(), getNodeName(), (int) t, n.getNodeName() });
		}
		if (!replace && (t == ELEMENT_NODE && getDocumentElement() != null)
				|| (t == DOCUMENT_TYPE_NODE && getDoctype() != null)) {
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "document.child.already.exists",
					new Object[] { (int) t, n.getNodeName() });
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getInputEncoding()}.
	 */
	@Override
	public String getInputEncoding() {
		return inputEncoding;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getXmlEncoding()}.
	 */
	@Override
	public String getXmlEncoding() {
		return xmlEncoding;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getXmlStandalone()}.
	 */
	@Override
	public boolean getXmlStandalone() {
		return xmlStandalone;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#setXmlStandalone(boolean)}.
	 */
	@Override
	public void setXmlStandalone(boolean b) throws DOMException {
		xmlStandalone = b;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getXmlVersion()}.
	 */
	@Override
	public String getXmlVersion() {
		return xmlVersion;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#setXmlVersion(String)}.
	 */
	@Override
	public void setXmlVersion(String v) throws DOMException {
		if (v == null || !v.equals(XMLConstants.XML_VERSION_10) && !v.equals(XMLConstants.XML_VERSION_11)) {
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "xml.version", new Object[] { v });
		}
		xmlVersion = v;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getStrictErrorChecking()}.
	 */
	@Override
	public boolean getStrictErrorChecking() {
		return strictErrorChecking;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#setStrictErrorChecking(boolean)}.
	 */
	@Override
	public void setStrictErrorChecking(boolean b) {
		strictErrorChecking = b;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getDocumentURI()}.
	 */
	@Override
	public String getDocumentURI() {
		return documentURI;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#setDocumentURI(String)}.
	 */
	@Override
	public void setDocumentURI(String uri) {
		documentURI = uri;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#getDomConfig()}.
	 */
	@Override
	public DOMConfiguration getDomConfig() {
		if (domConfig == null) {
			domConfig = new DocumentConfiguration();
		}
		return domConfig;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#adoptNode(Node)}.
	 */
	@Override
	public Node adoptNode(Node n) throws DOMException {
		if (!(n instanceof AbstractNode)) {
			return null;
		}
		switch (n.getNodeType()) {
		case Node.DOCUMENT_NODE:
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "adopt.document", new Object[] {});
		case Node.DOCUMENT_TYPE_NODE:
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "adopt.document.type", new Object[] {});
		case Node.ENTITY_NODE:
		case Node.NOTATION_NODE:
			return null;
		}
		AbstractNode an = (AbstractNode) n;
		if (an.isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) an.getNodeType(), an.getNodeName() });
		}
		Node parent = n.getParentNode();
		if (parent != null) {
			parent.removeChild(n);
		}
		adoptNode1((AbstractNode) n);
		return n;
	}

	/**
	 * Helper function for {@link #adoptNode(Node)}.
	 */
	protected void adoptNode1(AbstractNode n) {
		n.ownerDocument = this;
		switch (n.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			AbstractAttr attr = (AbstractAttr) n;
			attr.ownerElement = null;
			attr.unspecified = false;
			break;
		case Node.ELEMENT_NODE:
			NamedNodeMap nnm = n.getAttributes();
			int len = nnm.getLength();
			for (int i = 0; i < len; i++) {
				attr = (AbstractAttr) nnm.item(i);
				if (attr.getSpecified()) {
					adoptNode1(attr);
				}
			}
			break;
		case Node.ENTITY_REFERENCE_NODE:
			while (n.getFirstChild() != null) {
				n.removeChild(n.getFirstChild());
			}
			break;
		}

		fireUserDataHandlers(UserDataHandler.NODE_ADOPTED, n, null);

		for (Node m = n.getFirstChild(); m != null; m = m.getNextSibling()) {
			switch (m.getNodeType()) {
			case Node.DOCUMENT_TYPE_NODE:
			case Node.ENTITY_NODE:
			case Node.NOTATION_NODE:
				return;
			}
			adoptNode1((AbstractNode) m);
		}
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#renameNode(Node,String,String)}.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Node renameNode(Node n, String ns, String qn) {
		AbstractNode an = (AbstractNode) n;
		if (an == getDocumentElement()) {
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "rename.document.element", new Object[] {});
		}
		int nt = n.getNodeType();
		if (nt != Node.ELEMENT_NODE && nt != Node.ATTRIBUTE_NODE) {
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "rename.node",
					new Object[] { nt, n.getNodeName() });
		}
		if (xmlVersion.equals(XMLConstants.XML_VERSION_11) && !DOMUtilities.isValidName11(qn)
				|| !DOMUtilities.isValidName(qn)) {
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "wf.invalid.name", new Object[] { qn });
		}
		if (n.getOwnerDocument() != this) {
			throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "node.from.wrong.document",
					new Object[] { nt, n.getNodeName() });
		}
		int i = qn.indexOf(':');
		if (i == 0 || i == qn.length() - 1) {
			throw createDOMException(DOMException.NAMESPACE_ERR, "qname", new Object[] { nt, n.getNodeName(), qn });
		}
		String prefix = DOMUtilities.getPrefix(qn);
		if (ns != null && ns.length() == 0) {
			ns = null;
		}
		if (prefix != null && ns == null) {
			throw createDOMException(DOMException.NAMESPACE_ERR, "prefix",
					new Object[] { nt, n.getNodeName(), prefix });
		}
		if (strictErrorChecking) {
			if (XMLConstants.XML_PREFIX.equals(prefix) && !XMLConstants.XML_NAMESPACE_URI.equals(ns)
					|| XMLConstants.XMLNS_PREFIX.equals(prefix) && !XMLConstants.XMLNS_NAMESPACE_URI.equals(ns)) {
				throw createDOMException(DOMException.NAMESPACE_ERR, "namespace",
						new Object[] { nt, n.getNodeName(), ns });
			}
		}

		String prevNamespaceURI = n.getNamespaceURI();
		String prevNodeName = n.getNodeName();
		if (nt == Node.ELEMENT_NODE) {
			Node parent = n.getParentNode();
			AbstractElement e = (AbstractElement) createElementNS(ns, qn);

			// Move event handlers across
			EventSupport es1 = an.getEventSupport();
			if (es1 != null) {
				EventSupport es2 = e.getEventSupport();
				if (es2 == null) {
					AbstractDOMImplementation di = (AbstractDOMImplementation) implementation;
					es2 = di.createEventSupport(e);
					setEventsEnabled(true);
					e.eventSupport = es2;
				}
				es1.moveEventListeners(e.getEventSupport());
			}

			// Move user data across
			e.userData = e.userData == null ? null : (HashMap<String, Object>) an.userData.clone();
			e.userDataHandlers = e.userDataHandlers == null ? null
					: (HashMap<String, Object>) an.userDataHandlers.clone();

			// Remove from parent
			Node next = null;
			if (parent != null) {
				next = n.getNextSibling();
				parent.removeChild(n);
			}

			// Move child nodes across
			while (n.getFirstChild() != null) {
				e.appendChild(n.getFirstChild());
			}

			// Move attributes across
			NamedNodeMap nnm = n.getAttributes();
			for (int j = 0; j < nnm.getLength(); j++) {
				Attr a = (Attr) nnm.item(j);
				e.setAttributeNodeNS(a);
			}

			// Reinsert into parent
			if (parent != null) {
				if (next == null) {
					parent.appendChild(e);
				} else {
					parent.insertBefore(next, e);
				}
			}

			fireUserDataHandlers(UserDataHandler.NODE_RENAMED, n, e);
			if (getEventsEnabled()) {
				MutationNameEvent ev = (MutationNameEvent) createEvent("MutationNameEvent");
				ev.initMutationNameEventNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMElementNameChanged",
						true, // canBubbleArg
						false, // cancelableArg
						null, // relatedNodeArg
						prevNamespaceURI, prevNodeName);
				dispatchEvent(ev);
			}
			return e;
		} else {
			if (n instanceof AbstractAttrNS) {
				AbstractAttrNS a = (AbstractAttrNS) n;
				Element e = a.getOwnerElement();

				// Remove attribute from element
				if (e != null) {
					e.removeAttributeNode(a);
				}

				// Update name
				a.namespaceURI = ns;
				a.nodeName = qn;

				// Reinsert attribute into element
				if (e != null) {
					e.setAttributeNodeNS(a);
				}

				fireUserDataHandlers(UserDataHandler.NODE_RENAMED, a, null);
				if (getEventsEnabled()) {
					MutationNameEvent ev = (MutationNameEvent) createEvent("MutationNameEvent");
					ev.initMutationNameEventNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrNameChanged",
							true, // canBubbleArg
							false, // cancelableArg
							a, // relatedNodeArg
							prevNamespaceURI, prevNodeName);
					dispatchEvent(ev);
				}
				return a;
			} else {
				AbstractAttr a = (AbstractAttr) n;
				Element e = a.getOwnerElement();

				// Remove attribute from element and create new one
				if (e != null) {
					e.removeAttributeNode(a);
				}
				AbstractAttr a2 = (AbstractAttr) createAttributeNS(ns, qn);

				// Move attribute value across
				a2.setNodeValue(a.getNodeValue());

				// Move user data across
				a2.userData = a.userData == null ? null : (HashMap<String, Object>) a.userData.clone();
				a2.userDataHandlers = a.userDataHandlers == null ? null
						: (HashMap<String, Object>) a.userDataHandlers.clone();

				// Reinsert attribute into parent
				if (e != null) {
					e.setAttributeNodeNS(a2);
				}

				fireUserDataHandlers(UserDataHandler.NODE_RENAMED, a, a2);
				if (getEventsEnabled()) {
					MutationNameEvent ev = (MutationNameEvent) createEvent("MutationNameEvent");
					ev.initMutationNameEventNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrNameChanged",
							true, // canBubbleArg
							false, // cancelableArg
							a2, // relatedNodeArg
							prevNamespaceURI, prevNodeName);
					dispatchEvent(ev);
				}
				return a2;
			}
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#normalizeDocument()}. XXX
	 * Does not handle the 'entities' parameter yet.
	 */
	@Override
	public void normalizeDocument() {
		if (domConfig == null) {
			domConfig = new DocumentConfiguration();
		}
		boolean cdataSections = domConfig.getBooleanParameter(DOMConstants.DOM_CDATA_SECTIONS_PARAM);
		boolean comments = domConfig.getBooleanParameter(DOMConstants.DOM_COMMENTS_PARAM);
		boolean elementContentWhitespace = domConfig
				.getBooleanParameter(DOMConstants.DOM_ELEMENT_CONTENT_WHITESPACE_PARAM);
		boolean namespaceDeclarations = domConfig.getBooleanParameter(DOMConstants.DOM_NAMESPACE_DECLARATIONS_PARAM);
		boolean namespaces = domConfig.getBooleanParameter(DOMConstants.DOM_NAMESPACES_PARAM);
		boolean splitCdataSections = domConfig.getBooleanParameter(DOMConstants.DOM_SPLIT_CDATA_SECTIONS_PARAM);
		DOMErrorHandler errorHandler = (DOMErrorHandler) domConfig.getParameter(DOMConstants.DOM_ERROR_HANDLER_PARAM);
		normalizeDocument(getDocumentElement(), cdataSections, comments, elementContentWhitespace,
				namespaceDeclarations, namespaces, splitCdataSections, errorHandler);
	}

	/**
	 * Helper function for {@link #normalizeDocument()}.
	 */
	protected boolean normalizeDocument(Element e, boolean cdataSections, boolean comments,
			boolean elementContentWhitepace, boolean namespaceDeclarations, boolean namespaces,
			boolean splitCdataSections, DOMErrorHandler errorHandler) {
		AbstractElement ae = (AbstractElement) e;
		Node n = e.getFirstChild();
		while (n != null) {
			int nt = n.getNodeType();
			if (nt == Node.TEXT_NODE || !cdataSections && nt == Node.CDATA_SECTION_NODE) {
				// coalesce text nodes
				Node t = n;
				StringBuilder sb = new StringBuilder();
				sb.append(t.getNodeValue());
				n = n.getNextSibling();
				while (n != null && (n.getNodeType() == Node.TEXT_NODE
						|| !cdataSections && n.getNodeType() == Node.CDATA_SECTION_NODE)) {
					sb.append(n.getNodeValue());
					Node next = n.getNextSibling();
					e.removeChild(n);
					n = next;
				}
				String s = sb.toString();
				if (s.length() == 0) {
					Node next = n.getNextSibling(); // todo: Jlint says: n can be NULL
					e.removeChild(n);
					n = next;
					continue;
				}
				if (!s.equals(t.getNodeValue())) {
					if (!cdataSections && nt == Node.TEXT_NODE) {
						n = createTextNode(s);
						e.replaceChild(n, t);
					} else {
						n = t;
						t.setNodeValue(s);
					}
				} else {
					n = t;
				}
				if (!elementContentWhitepace) {
					// remove element content whitespace text nodes
					nt = n.getNodeType();
					if (nt == Node.TEXT_NODE) {
						AbstractText tn = (AbstractText) n;
						if (tn.isElementContentWhitespace()) {
							Node next = n.getNextSibling();
							e.removeChild(n);
							n = next;
							continue;
						}
					}
				}
				if (nt == Node.CDATA_SECTION_NODE && splitCdataSections) {
					if (!splitCdata(e, n, errorHandler)) {
						return false;
					}
				}
			} else if (nt == Node.CDATA_SECTION_NODE && splitCdataSections) {
				// split CDATA sections
				if (!splitCdata(e, n, errorHandler)) {
					return false;
				}
			} else if (nt == Node.COMMENT_NODE && !comments) {
				// remove comments
				Node next = n.getPreviousSibling();
				if (next == null) {
					next = n.getNextSibling();
				}
				e.removeChild(n);
				n = next;
				continue;
			}

			n = n.getNextSibling();
		}

		NamedNodeMap nnm = e.getAttributes();
		LinkedList<Attr> toRemove = new LinkedList<>();
		HashMap<String, String> names = new HashMap<>(); // todo names is not used ?
		for (int i = 0; i < nnm.getLength(); i++) {
			Attr a = (Attr) nnm.item(i);
			// Check for possible race with 'a'
			if (a != null) {
				String prefix = a.getPrefix();
				if (XMLConstants.XMLNS_PREFIX.equals(prefix)
						|| a.getNodeName().equals(XMLConstants.XMLNS_PREFIX)) {
					if (!namespaceDeclarations) {
						// remove namespace declarations
						toRemove.add(a);
					} else {
						// namespace normalization
						String ns = a.getNodeValue();
						if (a.getNodeValue().equals(XMLConstants.XMLNS_NAMESPACE_URI)
								|| !ns.equals(XMLConstants.XMLNS_NAMESPACE_URI)) {
							// XXX report error
						} else {
							names.put(prefix, ns);
						}
					}
				}
			}
		}

		if (!namespaceDeclarations) {
			// remove namespace declarations
			for (Attr aToRemove : toRemove) {
				e.removeAttributeNode(aToRemove);
			}
		} else {
			if (namespaces) {
				// normalize element namespace
				String ens = e.getNamespaceURI();
				if (ens != null) {
					String eprefix = e.getPrefix();
					if (!compareStrings(ae.lookupNamespaceURI(eprefix), ens)) {
						e.setAttributeNS(XMLConstants.XMLNS_NAMESPACE_URI,
								eprefix == null ? XMLConstants.XMLNS_PREFIX : "xmlns:" + eprefix, ens);
					}
				} else {
					if (e.getLocalName() == null) {
						// report error
					} else {
						if (ae.lookupNamespaceURI(null) == null) {
							e.setAttributeNS(XMLConstants.XMLNS_NAMESPACE_URI, XMLConstants.XMLNS_PREFIX, "");
						}
					}
				}
				// normalize attribute namespaces
				nnm = e.getAttributes();
				for (int i = 0; i < nnm.getLength(); i++) {
					Attr a = (Attr) nnm.item(i);
					String ans = a.getNamespaceURI();
					if (ans != null) {
						String apre = a.getPrefix();
						if (apre != null
								&& (apre.equals(XMLConstants.XML_PREFIX) || apre.equals(XMLConstants.XMLNS_PREFIX))
								|| ans.equals(XMLConstants.XMLNS_NAMESPACE_URI)) {
							continue;
						}
						String aprens = apre == null ? null : ae.lookupNamespaceURI(apre);
						if (apre == null || aprens == null || !aprens.equals(ans)) {
							String newpre = ae.lookupPrefix(ans);
							if (newpre != null) {
								a.setPrefix(newpre);
							} else {
								if (apre != null && ae.lookupNamespaceURI(apre) == null) {
									e.setAttributeNS(XMLConstants.XMLNS_NAMESPACE_URI,
											XMLConstants.XMLNS_PREFIX + ':' + apre, ans);
								} else {
									int index = 1;
									for (;;) {
										newpre = "NS" + index;
										if (ae.lookupPrefix(newpre) == null) {
											e.setAttributeNS(XMLConstants.XMLNS_NAMESPACE_URI,
													XMLConstants.XMLNS_PREFIX + ':' + newpre, ans);
											a.setPrefix(newpre);
											break;
										}
									}
								}
							}
						}
					} else {
						if (a.getLocalName() == null) {
							// report error
						}
					}
				}
			}
		}

		// check well-formedness
		nnm = e.getAttributes();
		for (int i = 0; i < nnm.getLength(); i++) {
			Attr a = (Attr) nnm.item(i);
			if (!checkName(a.getNodeName())) {
				if (errorHandler != null) {
					if (!errorHandler.handleError(createDOMError(DOMConstants.DOM_INVALID_CHARACTER_IN_NODE_NAME_ERROR,
							DOMError.SEVERITY_ERROR, "wf.invalid.name", new Object[] { a.getNodeName() }, a, null))) {
						return false;
					}
				}
			}
			if (!checkChars(a.getNodeValue())) {
				if (errorHandler != null) {
					if (!errorHandler.handleError(createDOMError(DOMConstants.DOM_INVALID_CHARACTER_ERROR,
							DOMError.SEVERITY_ERROR, "wf.invalid.character",
							new Object[] { (int) Node.ATTRIBUTE_NODE, a.getNodeName(), a.getNodeValue() }, a, null))) {
						return false;
					}
				}
			}
		}
		for (Node m = e.getFirstChild(); m != null; m = m.getNextSibling()) {
			int nt = m.getNodeType();
			String s;
			switch (nt) {
			case Node.TEXT_NODE:
				s = m.getNodeValue();
				if (!checkChars(s)) {
					if (errorHandler != null) {
						if (!errorHandler.handleError(createDOMError(DOMConstants.DOM_INVALID_CHARACTER_ERROR,
								DOMError.SEVERITY_ERROR, "wf.invalid.character",
								new Object[] { (int) m.getNodeType(), m.getNodeName(), s }, m, null))) {
							return false;
						}
					}
				}
				break;
			case Node.COMMENT_NODE:
				s = m.getNodeValue();
				if (!checkChars(s) || s.indexOf(XMLConstants.XML_DOUBLE_DASH) != -1
						|| s.charAt(s.length() - 1) == '-') {
					if (errorHandler != null) {
						if (!errorHandler.handleError(createDOMError(DOMConstants.DOM_INVALID_CHARACTER_ERROR,
								DOMError.SEVERITY_ERROR, "wf.invalid.character",
								new Object[] { (int) m.getNodeType(), m.getNodeName(), s }, m, null))) {
							return false;
						}
					}
				}
				break;
			case Node.CDATA_SECTION_NODE:
				s = m.getNodeValue();
				if (!checkChars(s) || s.indexOf(XMLConstants.XML_CDATA_END) != -1) {
					if (errorHandler != null) {
						if (!errorHandler.handleError(createDOMError(DOMConstants.DOM_INVALID_CHARACTER_ERROR,
								DOMError.SEVERITY_ERROR, "wf.invalid.character",
								new Object[] { (int) m.getNodeType(), m.getNodeName(), s }, m, null))) {
							return false;
						}
					}
				}
				break;
			case Node.PROCESSING_INSTRUCTION_NODE:
				if (m.getNodeName().equalsIgnoreCase(XMLConstants.XML_PREFIX)) {
					if (errorHandler != null) {
						if (!errorHandler.handleError(createDOMError(
								DOMConstants.DOM_INVALID_CHARACTER_IN_NODE_NAME_ERROR, DOMError.SEVERITY_ERROR,
								"wf.invalid.name", new Object[] { m.getNodeName() }, m, null))) {
							return false;
						}
					}
				}
				s = m.getNodeValue();
				if (!checkChars(s) || s.indexOf(XMLConstants.XML_PROCESSING_INSTRUCTION_END) != -1) {
					if (errorHandler != null) {
						if (!errorHandler.handleError(createDOMError(DOMConstants.DOM_INVALID_CHARACTER_ERROR,
								DOMError.SEVERITY_ERROR, "wf.invalid.character",
								new Object[] { (int) m.getNodeType(), m.getNodeName(), s }, m, null))) {
							return false;
						}
					}
				}
				break;
			case Node.ELEMENT_NODE:
				if (!checkName(m.getNodeName())) {
					if (errorHandler != null) {
						if (!errorHandler.handleError(createDOMError(
								DOMConstants.DOM_INVALID_CHARACTER_IN_NODE_NAME_ERROR, DOMError.SEVERITY_ERROR,
								"wf.invalid.name", new Object[] { m.getNodeName() }, m, null))) {
							return false;
						}
					}
				}
				if (!normalizeDocument((Element) m, cdataSections, comments, elementContentWhitepace,
						namespaceDeclarations, namespaces, splitCdataSections, errorHandler)) {
					return false;
				}
				break;
			}
		}
		return true;
	}

	/**
	 * Splits the given CDATA node if required.
	 */
	protected boolean splitCdata(Element e, Node n, DOMErrorHandler errorHandler) {
		String s2 = n.getNodeValue();
		int index = s2.indexOf(XMLConstants.XML_CDATA_END);
		if (index != -1) {
			String before = s2.substring(0, index + 2);
			String after = s2.substring(index + 2);
			n.setNodeValue(before);
			Node next = n.getNextSibling();
			if (next == null) {
				e.appendChild(createCDATASection(after));
			} else {
				e.insertBefore(createCDATASection(after), next);
			}
			if (errorHandler != null) {
				if (!errorHandler.handleError(createDOMError(DOMConstants.DOM_CDATA_SECTIONS_SPLITTED_ERROR,
						DOMError.SEVERITY_WARNING, "cdata.section.split", new Object[] {}, n, null))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks that the characters in the given string are all valid content
	 * characters.
	 */
	protected boolean checkChars(String s) {
		int len = s.length();
		if (xmlVersion.equals(XMLConstants.XML_VERSION_11)) {
			for (int i = 0; i < len; i++) {
				if (!XMLUtilities.isXML11Character(s.charAt(i))) {
					return false;
				}
			}
		} else {
			// assume XML 1.0
			for (int i = 0; i < len; i++) {
				if (!XMLUtilities.isXMLCharacter(s.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks that the given string is a valid XML name.
	 */
	protected boolean checkName(String s) {
		if (xmlVersion.equals(XMLConstants.XML_VERSION_11)) {
			return DOMUtilities.isValidName11(s);
		}
		// assume XML 1.0
		return DOMUtilities.isValidName(s);
	}

	/**
	 * Creates a DOMError object with the given parameters.
	 */
	protected DOMError createDOMError(String type, short severity, String key, Object[] args, Node related,
			Exception e) {
		try {
			return new DocumentError(type, severity, getCurrentDocument().formatMessage(key, args), related, e);
		} catch (Exception ex) {
			return new DocumentError(type, severity, key, related, e);
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#setTextContent(String)}.
	 */
	@Override
	public void setTextContent(String s) throws DOMException {
	}

	/**
	 * Sets the XBLManager used for this document.
	 */
	public void setXBLManager(XBLManager m) {
		boolean wasProcessing = xblManager.isProcessing();
		xblManager.stopProcessing();
		if (m == null) {
			m = new GenericXBLManager();
		}
		xblManager = m;
		if (wasProcessing) {
			xblManager.startProcessing();
		}
	}

	/**
	 * Returns the XBLManager used for this document.
	 */
	public XBLManager getXBLManager() {
		return xblManager;
	}

	/**
	 * DOMError implementation.
	 */
	protected static class DocumentError implements DOMError {

		/**
		 * The error type.
		 */
		protected String type;

		/**
		 * The error severity.
		 */
		protected short severity;

		/**
		 * The error message.
		 */
		protected String message;

		/**
		 * The error related data.
		 */
		protected Node relatedNode;

		/**
		 * The exception which cuased this error.
		 */
		protected Object relatedException;

		/**
		 * The DOMLocator for this error.
		 */
		protected DOMLocator domLocator;

		/**
		 * Creates a new DocumentError object.
		 */
		public DocumentError(String type, short severity, String message, Node relatedNode,
				Exception relatedException) {
			this.type = type;
			this.severity = severity;
			this.message = message;
			this.relatedNode = relatedNode;
			this.relatedException = relatedException;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public short getSeverity() {
			return severity;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public Object getRelatedData() {
			return relatedNode;
		}

		@Override
		public Object getRelatedException() {
			return relatedException;
		}

		@Override
		public DOMLocator getLocation() {
			if (domLocator == null) {
				domLocator = new ErrorLocation(relatedNode);
			}
			return domLocator;
		}

		/**
		 * The DOMLocator implementation.
		 */
		protected static class ErrorLocation implements DOMLocator {

			/**
			 * The node that caused the error.
			 */
			protected Node node;

			/**
			 * Create a new ErrorLocation object.
			 */
			public ErrorLocation(Node n) {
				node = n;
			}

			/**
			 * Get the line number of the error node.
			 */
			@Override
			public int getLineNumber() {
				return -1;
			}

			/**
			 * Get the column number of the error node.
			 */
			@Override
			public int getColumnNumber() {
				return -1;
			}

			/**
			 * Get the byte offset of the error node.
			 */
			@Override
			public int getByteOffset() {
				return -1;
			}

			/**
			 * Get the UTF-16 offset of the error node.
			 */
			@Override
			public int getUtf16Offset() {
				return -1;
			}

			/**
			 * Get the node.
			 */
			@Override
			public Node getRelatedNode() {
				return node;
			}

			/**
			 * Get the document URI.
			 */
			@Override
			public String getUri() {
				AbstractDocument doc = (AbstractDocument) node.getOwnerDocument();
				return doc.getDocumentURI();
			}

		}

	}

	/**
	 * DOMConfiguration for this document.
	 */
	protected class DocumentConfiguration implements DOMConfiguration {

		/**
		 * The boolean parameter names.
		 */
		protected String[] booleanParamNames = { DOMConstants.DOM_CANONICAL_FORM_PARAM,
				DOMConstants.DOM_CDATA_SECTIONS_PARAM, DOMConstants.DOM_CHECK_CHARACTER_NORMALIZATION_PARAM,
				DOMConstants.DOM_COMMENTS_PARAM, DOMConstants.DOM_DATATYPE_NORMALIZATION_PARAM,
				DOMConstants.DOM_ELEMENT_CONTENT_WHITESPACE_PARAM, DOMConstants.DOM_ENTITIES_PARAM,
				DOMConstants.DOM_INFOSET_PARAM, DOMConstants.DOM_NAMESPACES_PARAM,
				DOMConstants.DOM_NAMESPACE_DECLARATIONS_PARAM, DOMConstants.DOM_NORMALIZE_CHARACTERS_PARAM,
				DOMConstants.DOM_SPLIT_CDATA_SECTIONS_PARAM, DOMConstants.DOM_VALIDATE_PARAM,
				DOMConstants.DOM_VALIDATE_IF_SCHEMA_PARAM, DOMConstants.DOM_WELL_FORMED_PARAM };

		/**
		 * The boolean parameter values.
		 */
		protected boolean[] booleanParamValues = { false, // canonical-form
				true, // cdata-sections
				false, // check-character-normalization
				true, // comments
				false, // datatype-normalization
				false, // element-content-whitespace
				true, // entities
				false, // infoset
				true, // namespaces
				true, // namespace-declarations
				false, // normalize-characters
				true, // split-cdata-sections
				false, // validate
				false, // validate-if-schema
				true // well-formed
		};

		/**
		 * The read-onlyness of the boolean parameters.
		 */
		protected boolean[] booleanParamReadOnly = { true, // canonical-form
				false, // cdata-sections
				true, // check-character-normalization
				false, // comments
				true, // datatype-normalization
				false, // element-content-whitespace
				false, // entities
				false, // infoset
				false, // namespaces
				false, // namespace-declarations
				true, // normalize-characters
				false, // split-cdata-sections
				true, // validate
				true, // validate-if-schema
				false // well-formed
		};

		/**
		 * Map of parameter names to array indexes.
		 */
		protected Map<String, Integer> booleanParamIndexes = new HashMap<>();
		{
			for (int i = 0; i < booleanParamNames.length; i++) {
				booleanParamIndexes.put(booleanParamNames[i], i);
			}
		}

		/**
		 * Value of the 'error-handler' parameter.
		 */
		protected Object errorHandler;

		/**
		 * The DOMStringList object containing the parameter names.
		 */
		protected ParameterNameList paramNameList;

		/**
		 * Sets the given parameter.
		 */
		@Override
		public void setParameter(String name, Object value) {
			if (DOMConstants.DOM_ERROR_HANDLER_PARAM.equals(name)) {
				if (value != null && !(value instanceof DOMErrorHandler)) {
					throw createDOMException((short) 17 /* DOMException.TYPE_MISMATCH_ERR */, "domconfig.param.type",
							new Object[] { name });
				}
				errorHandler = value;
				return;
			}
			Integer i = booleanParamIndexes.get(name);
			if (i == null) {
				throw createDOMException(DOMException.NOT_FOUND_ERR, "domconfig.param.not.found",
						new Object[] { name });
			}
			if (value == null) {
				throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "domconfig.param.value",
						new Object[] { name });
			}
			if (!(value instanceof Boolean)) {
				throw createDOMException((short) 17 /* DOMException.TYPE_MISMATCH_ERR */, "domconfig.param.type",
						new Object[] { name });
			}
			int index = i;
			boolean val = (Boolean) value;
			if (booleanParamReadOnly[index] && booleanParamValues[index] != val) {
				throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "domconfig.param.value",
						new Object[] { name });
			}
			booleanParamValues[index] = val;
			if (name.equals(DOMConstants.DOM_INFOSET_PARAM)) {
				setParameter(DOMConstants.DOM_VALIDATE_IF_SCHEMA_PARAM, Boolean.FALSE);
				setParameter(DOMConstants.DOM_ENTITIES_PARAM, Boolean.FALSE);
				setParameter(DOMConstants.DOM_DATATYPE_NORMALIZATION_PARAM, Boolean.FALSE);
				setParameter(DOMConstants.DOM_CDATA_SECTIONS_PARAM, Boolean.FALSE);
				setParameter(DOMConstants.DOM_WELL_FORMED_PARAM, Boolean.TRUE);
				setParameter(DOMConstants.DOM_ELEMENT_CONTENT_WHITESPACE_PARAM, Boolean.TRUE);
				setParameter(DOMConstants.DOM_COMMENTS_PARAM, Boolean.TRUE);
				setParameter(DOMConstants.DOM_NAMESPACES_PARAM, Boolean.TRUE);
			}
		}

		/**
		 * Gets the value of the given parameter.
		 */
		@Override
		public Object getParameter(String name) {
			if (DOMConstants.DOM_ERROR_HANDLER_PARAM.equals(name)) {
				return errorHandler;
			}
			Integer index = booleanParamIndexes.get(name);
			if (index == null) {
				throw createDOMException(DOMException.NOT_FOUND_ERR, "domconfig.param.not.found",
						new Object[] { name });
			}
			return booleanParamValues[index] ? Boolean.TRUE : Boolean.FALSE;
		}

		/**
		 * Gets the boolean value of the given parameter.
		 */
		public boolean getBooleanParameter(String name) {
			Boolean b = (Boolean) getParameter(name);
			return b;
		}

		/**
		 * Returns whether the given parameter can be set to the given value.
		 */
		@Override
		public boolean canSetParameter(String name, Object value) {
			if (name.equals(DOMConstants.DOM_ERROR_HANDLER_PARAM)) {
				return value == null || value instanceof DOMErrorHandler;
			}
			Integer i = booleanParamIndexes.get(name);
			if (i == null || value == null || !(value instanceof Boolean)) {
				return false;
			}
			int index = i;
			boolean val = (Boolean) value;
			return !booleanParamReadOnly[index] || booleanParamValues[index] == val;
		}

		/**
		 * Returns a DOMStringList of parameter names.
		 */
		@Override
		public DOMStringList getParameterNames() {
			if (paramNameList == null) {
				paramNameList = new ParameterNameList();
			}
			return paramNameList;
		}

		/**
		 * Class to expose the parameter names.
		 */
		protected class ParameterNameList implements DOMStringList {

			/**
			 * Returns the parameter name at the given index.
			 */
			@Override
			public String item(int index) {
				if (index < 0) {
					return null;
				}
				if (index < booleanParamNames.length) {
					return booleanParamNames[index];
				}
				if (index == booleanParamNames.length) {
					return DOMConstants.DOM_ERROR_HANDLER_PARAM;
				}
				return null;
			}

			/**
			 * Returns the number of parameter names in the list.
			 */
			@Override
			public int getLength() {
				return booleanParamNames.length + 1;
			}

			/**
			 * Returns whether the given parameter name is in the list.
			 */
			@Override
			public boolean contains(String s) {
				if (DOMConstants.DOM_ERROR_HANDLER_PARAM.equals(s)) {
					return true;
				}
				for (String booleanParamName : booleanParamNames) {
					if (booleanParamName.equals(s)) {
						return true;
					}
				}
				return false;
			}

		}

	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.xpath.XPathEvaluator#createExpression(String,XPathNSResolver)}.
	 */
	@Override
	public XPathExpression createExpression(String expression, XPathNSResolver resolver)
			throws DOMException, XPathException {
		return new XPathExpr(expression, resolver);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.xpath.XPathEvaluator#createNSResolver(Node)}.
	 */
	@Override
	public XPathNSResolver createNSResolver(Node n) {
		return new XPathNodeNSResolver(n);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.xpath.XPathEvaluator#evaluate(String,Node,XPathNSResolver,short,Object)}.
	 */
	@Override
	public Object evaluate(String expression, Node contextNode, XPathNSResolver resolver, short type, Object result)
			throws XPathException, DOMException {
		XPathExpression xpath = createExpression(expression, resolver);
		return xpath.evaluate(contextNode, type, result);
	}

	/**
	 * Creates an exception with the appropriate error message.
	 */
	public XPathException createXPathException(short type, String key, Object[] args) {
		try {
			return new XPathException(type, formatMessage(key, args));
		} catch (Exception e) {
			return new XPathException(type, key);
		}
	}

	/**
	 * A compiled XPath expression.
	 */
	protected class XPathExpr implements XPathExpression {

		/**
		 * The compiled XPath expression.
		 */
		protected javax.xml.xpath.XPathExpression xpath;

		/**
		 * The namespace resolver.
		 */
		protected XPathNSResolver resolver;

		/**
		 * The Xalan prefix resolver.
		 */
		protected NSPrefixResolver prefixResolver;

		/**
		 * Creates a new XPathExpr object.
		 */
		public XPathExpr(String expr, XPathNSResolver res) throws DOMException, XPathException {
			resolver = res;
			prefixResolver = new NSPrefixResolver();
			try {
				XPath xPathAPI = XPathFactory.newInstance().newXPath();
				xPathAPI.setNamespaceContext(prefixResolver);
				xpath = xPathAPI.compile(expr);
			} catch (XPathExpressionException te) {
				throw createXPathException(XPathException.INVALID_EXPRESSION_ERR,
						"xpath.invalid.expression", new Object[] { expr, te.getMessage() });
			}
		}

		/**
		 * <b>DOM</b>: Implements
		 * {@link org.w3c.dom.xpath.XPathExpression#evaluate(Node,short,Object)}.
		 */
		@Override
		public Object evaluate(Node contextNode, short type, Object res) throws XPathException, DOMException {
			if (contextNode.getNodeType() != DOCUMENT_NODE && contextNode.getOwnerDocument() != AbstractDocument.this
					|| contextNode.getNodeType() == DOCUMENT_NODE && contextNode != AbstractDocument.this) {
				throw createDOMException(DOMException.WRONG_DOCUMENT_ERR, "node.from.wrong.document",
						new Object[] { (int) contextNode.getNodeType(), contextNode.getNodeName() });
			}
			if (type < 0 || type > 9) {
				throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "xpath.invalid.result.type",
						new Object[] { (int) type });
			}
			switch (contextNode.getNodeType()) {
			case ENTITY_REFERENCE_NODE:
			case ENTITY_NODE:
			case DOCUMENT_TYPE_NODE:
			case DOCUMENT_FRAGMENT_NODE:
			case NOTATION_NODE:
				throw createDOMException(DOMException.NOT_SUPPORTED_ERR, "xpath.invalid.context.node",
						new Object[] { (int) contextNode.getNodeType(), contextNode.getNodeName() });
			}

			try {
				switch (type) {
				case XPathResult.ANY_UNORDERED_NODE_TYPE:
				case XPathResult.FIRST_ORDERED_NODE_TYPE:
					return new Result((Node) xpath.evaluate(contextNode, XPathConstants.NODE), type);
				case XPathResult.BOOLEAN_TYPE:
					return new Result((Boolean) xpath.evaluate(contextNode, XPathConstants.BOOLEAN));
				case XPathResult.NUMBER_TYPE:
					return new Result((Double) xpath.evaluate(contextNode, XPathConstants.NUMBER));
				case XPathResult.ORDERED_NODE_ITERATOR_TYPE:
				case XPathResult.UNORDERED_NODE_ITERATOR_TYPE:
				case XPathResult.ORDERED_NODE_SNAPSHOT_TYPE:
				case XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE:
					return new Result((NodeList) xpath.evaluate(contextNode, XPathConstants.NODESET), type);
				case XPathResult.STRING_TYPE:
					return new Result((String) xpath.evaluate(contextNode, XPathConstants.STRING));
				}
			} catch (XPathExpressionException | TransformerException te) {
				throw createXPathException(XPathException.TYPE_ERR, "xpath.cannot.convert.result",
						new Object[] { (int) type, te.getMessage() });
			}
			return null;
		}

		/**
		 * XPathResult implementation. XXX Namespace nodes are not handled correctly,
		 * since Xalan returns namespace nodes as simply the attribute node that caused
		 * the namespace to be in scope on the element in question. Thus it is
		 * impossible to tell the difference between a selected attribute that begins
		 * with 'xmlns' and an XPath namespace node.
		 */
		public class Result implements XPathResult {

			/**
			 * The result type.
			 */
			protected short resultType;

			/**
			 * The number value.
			 */
			protected double numberValue;

			/**
			 * The string value.
			 */
			protected String stringValue;

			/**
			 * The boolean value.
			 */
			protected boolean booleanValue;

			/**
			 * The single node value.
			 */
			protected Node singleNodeValue;

			/**
			 * The NodeList for iterators.
			 */
			protected NodeList iterator;

			/**
			 * The position of the iterator.
			 */
			protected int iteratorPosition;

			/**
			 * Creates a new single node Result object.
			 */
			public Result(Node n, short type) {
				resultType = type;
				singleNodeValue = n;
			}

			/**
			 * Creates a new boolean Result object.
			 */
			public Result(boolean b) throws javax.xml.transform.TransformerException {
				resultType = BOOLEAN_TYPE;
				booleanValue = b;
			}

			/**
			 * Creates a new number Result object.
			 */
			public Result(double d) throws javax.xml.transform.TransformerException {
				resultType = NUMBER_TYPE;
				numberValue = d;
			}

			/**
			 * Creates a new string Result object.
			 */
			public Result(String s) {
				resultType = STRING_TYPE;
				stringValue = s;
			}

			/**
			 * Creates a new node iterator Result object.
			 */
			public Result(NodeList nl, short type) {
				resultType = type;
				iterator = nl;
			}

			/**
			 * Gets the result type.
			 */
			@Override
			public short getResultType() {
				return resultType;
			}

			/**
			 * Gets the boolean value.
			 */
			@Override
			public boolean getBooleanValue() {
				if (resultType != BOOLEAN_TYPE) {
					throw createXPathException(XPathException.TYPE_ERR, "xpath.invalid.result.type",
							new Object[] { (int) resultType });
				}
				return booleanValue;
			}

			/**
			 * Gets the number value.
			 */
			@Override
			public double getNumberValue() {
				if (resultType != NUMBER_TYPE) {
					throw createXPathException(XPathException.TYPE_ERR, "xpath.invalid.result.type",
							new Object[] { (int) resultType });
				}
				return numberValue;
			}

			/**
			 * Gets the string value.
			 */
			@Override
			public String getStringValue() {
				if (resultType != STRING_TYPE) {
					throw createXPathException(XPathException.TYPE_ERR, "xpath.invalid.result.type",
							new Object[] { (int) resultType });
				}
				return stringValue;
			}

			/**
			 * Gets the single node value.
			 */
			@Override
			public Node getSingleNodeValue() {
				if (resultType != ANY_UNORDERED_NODE_TYPE && resultType != FIRST_ORDERED_NODE_TYPE) {
					throw createXPathException(XPathException.TYPE_ERR, "xpath.invalid.result.type",
							new Object[] { (int) resultType });
				}
				return singleNodeValue;
			}

			/**
			 * Returns whether the iterator has been invalidated by document modifications.
			 */
			@Override
			public boolean getInvalidIteratorState() {
				return false;
			}

			/**
			 * Returns the length of the snapshot.
			 */
			@Override
			public int getSnapshotLength() {
				if (resultType != UNORDERED_NODE_SNAPSHOT_TYPE && resultType != ORDERED_NODE_SNAPSHOT_TYPE) {
					throw createXPathException(XPathException.TYPE_ERR, "xpath.invalid.result.type",
							new Object[] { (int) resultType });
				}
				return iterator.getLength();
			}

			/**
			 * <b>DOM</b>: Implement {@link org.w3c.dom.xpath.XPathResult#iterateNext()}.
			 */
			@Override
			public Node iterateNext() {
				if (resultType != UNORDERED_NODE_ITERATOR_TYPE && resultType != ORDERED_NODE_ITERATOR_TYPE) {
					throw createXPathException(XPathException.TYPE_ERR, "xpath.invalid.result.type",
							new Object[] { (int) resultType });
				}
				return iterator.item(iteratorPosition++);
			}

			/**
			 * Returns the <code>i</code>th item in the snapshot.
			 */
			@Override
			public Node snapshotItem(int i) {
				if (resultType != UNORDERED_NODE_SNAPSHOT_TYPE && resultType != ORDERED_NODE_SNAPSHOT_TYPE) {
					throw createXPathException(XPathException.TYPE_ERR, "xpath.invalid.result.type",
							new Object[] { (int) resultType });
				}
				return iterator.item(i);
			}

		}

		/**
		 * Prefix resolver.
		 */
		protected class NSPrefixResolver implements NamespaceContext {

			/**
			 * Resolves the given namespace prefix.
			 */
			@Override
			public String getNamespaceURI(String prefix) {
				if (resolver == null) {
					return null;
				}
				return resolver.lookupNamespaceURI(prefix);
			}

			@Override
			public String getPrefix(String namespaceURI) {
				return null;
			}

			@Override
			public Iterator<String> getPrefixes(String namespaceURI) {
				return null;
			}

		}

	}

	/**
	 * An XPathNSResolver that uses Node.lookupNamespaceURI.
	 */
	protected static class XPathNodeNSResolver implements XPathNSResolver {

		/**
		 * The context node for namespace prefix resolution.
		 */
		protected Node contextNode;

		/**
		 * Creates a new XPathNodeNSResolver object.
		 */
		public XPathNodeNSResolver(Node n) {
			contextNode = n;
		}

		/**
		 * <b>DOM</b>: Implements
		 * {@link org.w3c.dom.xpath.XPathNSResolver#lookupNamespaceURI(String)}.
		 */
		@Override
		public String lookupNamespaceURI(String prefix) {
			return contextNode.lookupNamespaceURI(prefix);
		}

	}

	// NodeXBL //////////////////////////////////////////////////////////////

	/**
	 * Get the parent of this node in the fully flattened tree.
	 */
	@Override
	public Node getXblParentNode() {
		return xblManager.getXblParentNode(this);
	}

	/**
	 * Get the list of child nodes of this node in the fully flattened tree.
	 */
	@Override
	public NodeList getXblChildNodes() {
		return xblManager.getXblChildNodes(this);
	}

	/**
	 * Get the list of child nodes of this node in the fully flattened tree that are
	 * within the same shadow scope.
	 */
	@Override
	public NodeList getXblScopedChildNodes() {
		return xblManager.getXblScopedChildNodes(this);
	}

	/**
	 * Get the first child node of this node in the fully flattened tree.
	 */
	@Override
	public Node getXblFirstChild() {
		return xblManager.getXblFirstChild(this);
	}

	/**
	 * Get the last child node of this node in the fully flattened tree.
	 */
	@Override
	public Node getXblLastChild() {
		return xblManager.getXblLastChild(this);
	}

	/**
	 * Get the node which directly precedes the current node in the xblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Node getXblPreviousSibling() {
		return xblManager.getXblPreviousSibling(this);
	}

	/**
	 * Get the node which directly follows the current node in the xblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Node getXblNextSibling() {
		return xblManager.getXblNextSibling(this);
	}

	/**
	 * Get the first element child of this node in the fully flattened tree.
	 */
	@Override
	public Element getXblFirstElementChild() {
		return xblManager.getXblFirstElementChild(this);
	}

	/**
	 * Get the last element child of this node in the fully flattened tree.
	 */
	@Override
	public Element getXblLastElementChild() {
		return xblManager.getXblLastElementChild(this);
	}

	/**
	 * Get the first element that precedes the current node in the xblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Element getXblPreviousElementSibling() {
		return xblManager.getXblPreviousElementSibling(this);
	}

	/**
	 * Get the first element that follows the current node in the xblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Element getXblNextElementSibling() {
		return xblManager.getXblNextElementSibling(this);
	}

	/**
	 * Get the bound element whose shadow tree this current node resides in.
	 */
	@Override
	public Element getXblBoundElement() {
		return xblManager.getXblBoundElement(this);
	}

	/**
	 * Get the shadow tree of this node.
	 */
	@Override
	public Element getXblShadowTree() {
		return xblManager.getXblShadowTree(this);
	}

	/**
	 * Get the xbl:definition elements currently binding this element.
	 */
	@Override
	public NodeList getXblDefinitions() {
		return xblManager.getXblDefinitions(this);
	}

	// Serializable /////////////////////////////////////////////////

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();

		s.writeObject(implementation.getClass().getName());
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();

		localizableSupport = new LocalizableSupport(RESOURCES, getClass().getClassLoader());

		Class<?> c = Class.forName((String) s.readObject());

		try {
			Method m = c.getMethod("getDOMImplementation", (Class[]) null);
			implementation = (DOMImplementation) m.invoke(null, (Object[]) null);
		} catch (Exception e) {
			if (DOMImplementation.class.isAssignableFrom(c)) {
				try {
					implementation = (DOMImplementation) c.getDeclaredConstructor().newInstance();
				} catch (Exception ex) {
				}
			} else {
				throw new SecurityException("Trying to create object that is not a DOMImplementation.");
			}
		}
	}

}
