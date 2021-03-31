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

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import io.sf.carte.echosvg.constants.XMLConstants;

/**
 * This class implements the {@link org.w3c.dom.Document},
 * {@link org.w3c.dom.events.DocumentEvent}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GenericDocument extends AbstractDocument {

	private static final long serialVersionUID = 1L;

	/**
	 * Local name for 'id' attributes.
	 */
	protected static final String ATTR_ID = XMLConstants.XML_ID_ATTRIBUTE;

	/**
	 * Is this document immutable?
	 */
	protected boolean readonly;

	/**
	 * Creates a new uninitialized document.
	 */
	protected GenericDocument() {
	}

	/**
	 * Creates a new uninitialized document.
	 */
	public GenericDocument(DocumentType dt, DOMImplementation impl) {
		super(dt, impl);
	}

	/**
	 * Tests whether this node is readonly.
	 */
	@Override
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * Sets this node readonly attribute.
	 */
	@Override
	public void setReadonly(boolean v) {
		readonly = v;
	}

	/**
	 * Returns true if the given Attr node represents an 'id' for this document.
	 */
	@Override
	public boolean isId(Attr node) {
		if (node.getNamespaceURI() != null)
			return false;
		return ATTR_ID.equals(node.getNodeName());
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#createElement(String)}.
	 */
	@Override
	public Element createElement(String tagName) throws DOMException {
		return new GenericElement(tagName.intern(), this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#createDocumentFragment()}.
	 */
	@Override
	public DocumentFragment createDocumentFragment() {
		return new GenericDocumentFragment(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#createTextNode(String)}.
	 */
	@Override
	public Text createTextNode(String data) {
		return new GenericText(data, this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#createComment(String)}.
	 */
	@Override
	public Comment createComment(String data) {
		return new GenericComment(data, this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#createCDATASection(String)}.
	 */
	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		return new GenericCDATASection(data, this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#createProcessingInstruction(String,String)}.
	 * 
	 * @return a {@link StyleSheetProcessingInstruction} if target is
	 *         "xml-stylesheet" or a GenericProcessingInstruction otherwise.
	 */
	@Override
	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		return new GenericProcessingInstruction(target, data, this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Document#createAttribute(String)}.
	 */
	@Override
	public Attr createAttribute(String name) throws DOMException {
		return new GenericAttr(name.intern(), this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#createEntityReference(String)}.
	 */
	@Override
	public EntityReference createEntityReference(String name) throws DOMException {
		return new GenericEntityReference(name, this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#createElementNS(String,String)}.
	 */
	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		if (namespaceURI != null && namespaceURI.length() == 0) {
			namespaceURI = null;
		}
		if (namespaceURI == null) {
			return new GenericElement(qualifiedName.intern(), this);
		} else {
			return new GenericElementNS(namespaceURI.intern(), qualifiedName.intern(), this);
		}
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.Document#createAttributeNS(String,String)}.
	 */
	@Override
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		if (namespaceURI != null && namespaceURI.length() == 0) {
			namespaceURI = null;
		}
		if (namespaceURI == null) {
			return new GenericAttr(qualifiedName.intern(), this);
		} else {
			return new GenericAttrNS(namespaceURI.intern(), qualifiedName.intern(), this);
		}
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new GenericDocument();
	}
}
