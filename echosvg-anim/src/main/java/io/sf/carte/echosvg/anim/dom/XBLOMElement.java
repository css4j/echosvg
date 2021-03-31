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
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.util.XBLConstants;

/**
 * Base class for all XBL elements to inherit from.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class XBLOMElement extends SVGOMElement implements XBLConstants {

	private static final long serialVersionUID = 1L;
	/**
	 * The element prefix.
	 */
	protected String prefix;

	/**
	 * Creates a new XBLOMElement.
	 */
	protected XBLOMElement() {
	}

	/**
	 * Creates a new XBLOMElement.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected XBLOMElement(String prefix, AbstractDocument owner) {
		ownerDocument = owner;
		setPrefix(prefix);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getNodeName()}.
	 */
	@Override
	public String getNodeName() {
		if (prefix == null || prefix.equals("")) {
			return getLocalName();
		}

		return prefix + ':' + getLocalName();
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getNamespaceURI()}.
	 */
	@Override
	public String getNamespaceURI() {
		return XBL_NAMESPACE_URI;
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#setPrefix(String)}.
	 */
	@Override
	public void setPrefix(String prefix) throws DOMException {
		if (isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) getNodeType(), getNodeName() });
		}
		if (prefix != null && !prefix.equals("") && !DOMUtilities.isValidName(prefix)) {
			throw createDOMException(DOMException.INVALID_CHARACTER_ERR, "prefix",
					new Object[] { (int) getNodeType(), getNodeName(), prefix });
		}
		this.prefix = prefix;
	}

	/**
	 * Exports this node to the given document.
	 */
	@Override
	protected Node export(Node n, AbstractDocument d) {
		super.export(n, d);
		XBLOMElement e = (XBLOMElement) n;
		e.prefix = prefix;
		return n;
	}

	/**
	 * Deeply exports this node to the given document.
	 */
	@Override
	protected Node deepExport(Node n, AbstractDocument d) {
		super.deepExport(n, d);
		XBLOMElement e = (XBLOMElement) n;
		e.prefix = prefix;
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
		XBLOMElement e = (XBLOMElement) n;
		e.prefix = prefix;
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
		XBLOMElement e = (XBLOMElement) n;
		e.prefix = prefix;
		return n;
	}
}
