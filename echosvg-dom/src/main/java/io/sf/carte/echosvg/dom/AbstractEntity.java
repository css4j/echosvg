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
import org.w3c.dom.Entity;
import org.w3c.dom.Node;

/**
 * This class implements the {@link org.w3c.dom.Entity} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractEntity extends AbstractParentNode implements Entity {

	private static final long serialVersionUID = 1L;

	/**
	 * The node name.
	 */
	protected String nodeName;

	/**
	 * The public id.
	 */
	protected String publicId;

	/**
	 * The system id.
	 */
	protected String systemId;

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeType()}.
	 * 
	 * @return {@link org.w3c.dom.Node#ENTITY_NODE}
	 */
	@Override
	public short getNodeType() {
		return ENTITY_NODE;
	}

	/**
	 * Sets the name of this node.
	 */
	@Override
	public void setNodeName(String v) {
		nodeName = v;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
	 * 
	 * @return {@link #nodeName}.
	 */
	@Override
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Entity#getPublicId()}.
	 * 
	 * @return {@link #publicId}.
	 */
	@Override
	public String getPublicId() {
		return publicId;
	}

	/**
	 * Sets the public id.
	 */
	public void setPublicId(String id) {
		publicId = id;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Entity#getSystemId()}.
	 * 
	 * @return {@link #systemId}.
	 */
	@Override
	public String getSystemId() {
		return systemId;
	}

	/**
	 * Sets the system id.
	 */
	public void setSystemId(String id) {
		systemId = id;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Entity#getNotationName()}.
	 * 
	 * @return {@link #getNodeName()}.
	 */
	@Override
	public String getNotationName() {
		return getNodeName();
	}

	/**
	 * Sets the notation name.
	 */
	public void setNotationName(String name) {
		setNodeName(name);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Entity#getInputEncoding()}.
	 */
	@Override
	public String getInputEncoding() {
		return null;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Entity#getXmlEncoding()}.
	 */
	@Override
	public String getXmlEncoding() {
		return null;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Entity#getXmlVersion()}.
	 */
	@Override
	public String getXmlVersion() {
		return null;
	}

	/**
	 * Exports this node to the given document.
	 */
	@Override
	protected Node export(Node n, AbstractDocument d) {
		super.export(n, d);
		AbstractEntity ae = (AbstractEntity) n;
		ae.nodeName = nodeName;
		ae.publicId = publicId;
		ae.systemId = systemId;
		return n;
	}

	/**
	 * Deeply exports this node to the given document.
	 */
	@Override
	protected Node deepExport(Node n, AbstractDocument d) {
		super.deepExport(n, d);
		AbstractEntity ae = (AbstractEntity) n;
		ae.nodeName = nodeName;
		ae.publicId = publicId;
		ae.systemId = systemId;
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
		AbstractEntity ae = (AbstractEntity) n;
		ae.nodeName = nodeName;
		ae.publicId = publicId;
		ae.systemId = systemId;
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
		AbstractEntity ae = (AbstractEntity) n;
		ae.nodeName = nodeName;
		ae.publicId = publicId;
		ae.systemId = systemId;
		return n;
	}

	/**
	 * Checks the validity of a node to be inserted.
	 */
	@Override
	protected void checkChildType(Node n, boolean replace) {
		switch (n.getNodeType()) {
		case ELEMENT_NODE:
		case PROCESSING_INSTRUCTION_NODE:
		case COMMENT_NODE:
		case TEXT_NODE:
		case CDATA_SECTION_NODE:
		case ENTITY_REFERENCE_NODE:
		case DOCUMENT_FRAGMENT_NODE:
			break;
		default:
			throw createDOMException(DOMException.HIERARCHY_REQUEST_ERR, "child.type",
					new Object[] { (int) getNodeType(), getNodeName(), (int) n.getNodeType(), n.getNodeName() });
		}
	}

}
