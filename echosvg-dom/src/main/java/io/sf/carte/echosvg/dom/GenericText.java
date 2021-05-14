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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import io.sf.carte.echosvg.dom.util.DOMUtilities;

/**
 * This class provides a generic implementation of the {@link org.w3c.dom.Text}
 * interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public class GenericText extends AbstractText {
	private static final long serialVersionUID = 1L;
	/**
	 * Is this element immutable?
	 */
	protected boolean readonly;

	/**
	 * Creates a new uninitialized Text object.
	 */
	protected GenericText() {
	}

	/**
	 * Creates a new Text object.
	 */
	public GenericText(String value, AbstractDocument owner) {
		ownerDocument = owner;
		setNodeValue(value);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
	 * 
	 * @return {@link #getNodeName()}.
	 */
	@Override
	public String getNodeName() {
		return "#text";
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeType()}.
	 * 
	 * @return {@link org.w3c.dom.Node#TEXT_NODE}
	 */
	@Override
	public short getNodeType() {
		return TEXT_NODE;
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
	 * Creates a text node of the current type.
	 */
	@Override
	protected Text createTextNode(String text) {
		return getOwnerDocument().createTextNode(text);
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new GenericText();
	}

	@Override
	public String toString() {
		Document d = getOwnerDocument();
		try {
			return DOMUtilities.contentToString(getData(), d == null || "1.1".equals(d.getXmlVersion()));
		} catch (Exception e) {
			return "";
		}
	}

}
