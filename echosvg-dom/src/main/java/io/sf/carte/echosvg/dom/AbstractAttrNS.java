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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.util.DOMUtilities;

/**
 * This class implements the {@link org.w3c.dom.Attr} interface with support for
 * namespaces.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractAttrNS extends AbstractAttr {

	private static final long serialVersionUID = 1L;
	/**
	 * The namespace URI
	 */
	protected String namespaceURI;

	/**
	 * Creates a new Attr object.
	 */
	protected AbstractAttrNS() {
	}

	/**
	 * Creates a new Attr object.
	 * 
	 * @param nsURI The element namespace URI.
	 * @param qname The attribute qualified name for validation purposes.
	 * @param owner The owner document.
	 * @exception DOMException INVALID_CHARACTER_ERR: Raised if the specified
	 *                         qualified name contains an illegal character. <br>
	 *                         NAMESPACE_ERR: Raised if the
	 *                         <code>qualifiedName</code> is malformed, if the
	 *                         <code>qualifiedName</code> has a prefix and the
	 *                         <code>namespaceURI</code> is <code>null</code> or an
	 *                         empty string, if the <code>qualifiedName</code> has a
	 *                         prefix that is "xml" and the
	 *                         <code>namespaceURI</code> is different from
	 *                         "http://www.w3.org/XML/1998/namespace", if the
	 *                         <code>qualifiedName</code> has a prefix that is
	 *                         "xmlns" and the <code>namespaceURI</code> is
	 *                         different from "http://www.w3.org/2000/xmlns/", or if
	 *                         the <code>qualifiedName</code> is "xmlns" and the
	 *                         <code>namespaceURI</code> is different from
	 *                         "http://www.w3.org/2000/xmlns/".
	 */
	protected AbstractAttrNS(String nsURI, String qname, AbstractDocument owner) throws DOMException {
		super(qname, owner);
		if (nsURI != null && nsURI.length() == 0) {
			nsURI = null;
		}
		namespaceURI = nsURI;
		String prefix = DOMUtilities.getPrefix(qname);
		if (!owner.getStrictErrorChecking()) {
			return;
		}
		if (prefix != null) {
			if (nsURI == null || ("xml".equals(prefix) && !XMLConstants.XML_NAMESPACE_URI.equals(nsURI))
					|| ("xmlns".equals(prefix) && !XMLConstants.XMLNS_NAMESPACE_URI.equals(nsURI))) {
				throw createDOMException(DOMException.NAMESPACE_ERR, "namespace.uri",
						new Object[] { (int) getNodeType(), getNodeName(), nsURI });
			}
		} else if ("xmlns".equals(qname) && !XMLConstants.XMLNS_NAMESPACE_URI.equals(nsURI)) {
			throw createDOMException(DOMException.NAMESPACE_ERR, "namespace.uri",
					new Object[] { (int) getNodeType(), getNodeName(), nsURI });
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNamespaceURI()}.
	 * 
	 * @return {@link #namespaceURI}.
	 */
	@Override
	public String getNamespaceURI() {
		return namespaceURI;
	}

	/**
	 * Exports this node to the given document.
	 */
	@Override
	protected Node export(Node n, AbstractDocument d) {
		super.export(n, d);
		AbstractAttrNS aa = (AbstractAttrNS) n;
		aa.namespaceURI = namespaceURI;
		return n;
	}

	/**
	 * Deeply exports this node to the given document.
	 */
	@Override
	protected Node deepExport(Node n, AbstractDocument d) {
		super.deepExport(n, d);
		AbstractAttrNS aa = (AbstractAttrNS) n;
		aa.namespaceURI = namespaceURI;
		return n;
	}

	/**
	 * Copy the fields of the current node into the given node.
	 * 
	 * @param n a node of the type of this.
	 */
	@Override
	protected Node copyInto(Node n) {
		super.copyInto(n);
		AbstractAttrNS aa = (AbstractAttrNS) n;
		aa.namespaceURI = namespaceURI;
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
		AbstractAttrNS aa = (AbstractAttrNS) n;
		aa.namespaceURI = namespaceURI;
		return n;
	}

}
