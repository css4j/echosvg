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

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/**
 * This class implements the {@link org.w3c.dom.CharacterData} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractCharacterData extends AbstractChildNode implements CharacterData {

	private static final long serialVersionUID = 1L;
	/**
	 * The value of this node.
	 */
	protected String nodeValue = "";

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeValue()}.
	 * 
	 * @return {@link #nodeValue}.
	 */
	@Override
	public String getNodeValue() throws DOMException {
		return nodeValue;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#setNodeValue(String)}.
	 */
	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		if (isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) getNodeType(), getNodeName() });
		}
		// Node modification
		String val = this.nodeValue;
		this.nodeValue = (nodeValue == null) ? "" : nodeValue;

		// Mutation event
		fireDOMCharacterDataModifiedEvent(val, this.nodeValue);
		if (getParentNode() != null) {
			((AbstractParentNode) getParentNode()).fireDOMSubtreeModifiedEvent();
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.CharacterData#getData()}.
	 * 
	 * @return {@link #getNodeValue()}.
	 */
	@Override
	public String getData() throws DOMException {
		return getNodeValue();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.CharacterData#setData(String)}.
	 */
	@Override
	public void setData(String data) throws DOMException {
		setNodeValue(data);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.CharacterData#getLength()}.
	 * 
	 * @return {@link #nodeValue}.length().
	 */
	@Override
	public int getLength() {
		return nodeValue.length();
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.CharacterData#substringData(int,int)}.
	 */
	@Override
	public String substringData(int offset, int count) throws DOMException {
		checkOffsetCount(offset, count);

		String v = getNodeValue();
		return v.substring(offset, Math.min(v.length(), offset + count));
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.CharacterData#appendData(String)}.
	 */
	@Override
	public void appendData(String arg) throws DOMException {
		if (isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) getNodeType(), getNodeName() });
		}
		setNodeValue(getNodeValue() + ((arg == null) ? "" : arg));
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.CharacterData#insertData(int,String)}.
	 */
	@Override
	public void insertData(int offset, String arg) throws DOMException {
		if (isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) getNodeType(), getNodeName() });
		}
		if (offset < 0 || offset > getLength()) {
			throw createDOMException(DOMException.INDEX_SIZE_ERR, "offset", new Object[] { offset });
		}
		String v = getNodeValue();
		setNodeValue(v.substring(0, offset) + arg + v.substring(offset));
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.CharacterData#deleteData(int,int)}.
	 */
	@Override
	public void deleteData(int offset, int count) throws DOMException {
		if (isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) getNodeType(), getNodeName() });
		}
		checkOffsetCount(offset, count);

		String v = getNodeValue();
		setNodeValue(v.substring(0, offset) + v.substring(Math.min(v.length(), offset + count)));
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.CharacterData#replaceData(int,int,String)}.
	 */
	@Override
	public void replaceData(int offset, int count, String arg) throws DOMException {
		if (isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) getNodeType(), getNodeName() });
		}
		checkOffsetCount(offset, count);

		String v = getNodeValue();
		setNodeValue(v.substring(0, offset) + arg + v.substring(Math.min(v.length(), offset + count)));
	}

	/**
	 * Checks the given offset and count validity.
	 */
	protected void checkOffsetCount(int offset, int count) throws DOMException {
		if (offset < 0 || offset >= getLength()) {
			throw createDOMException(DOMException.INDEX_SIZE_ERR, "offset", new Object[] { offset });
		}
		if (count < 0) {
			throw createDOMException(DOMException.INDEX_SIZE_ERR, "negative.count", new Object[] { count });
		}
	}

	/**
	 * Exports this node to the given document.
	 */
	@Override
	protected Node export(Node n, AbstractDocument d) {
		super.export(n, d);
		AbstractCharacterData cd = (AbstractCharacterData) n;
		cd.nodeValue = nodeValue;
		return n;
	}

	/**
	 * Deeply exports this node to the given document.
	 */
	@Override
	protected Node deepExport(Node n, AbstractDocument d) {
		super.deepExport(n, d);
		AbstractCharacterData cd = (AbstractCharacterData) n;
		cd.nodeValue = nodeValue;
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
		AbstractCharacterData cd = (AbstractCharacterData) n;
		cd.nodeValue = nodeValue;
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
		AbstractCharacterData cd = (AbstractCharacterData) n;
		cd.nodeValue = nodeValue;
		return n;
	}

}
