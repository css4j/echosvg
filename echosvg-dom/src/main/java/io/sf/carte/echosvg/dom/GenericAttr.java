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

/**
 * This class implements the {@link org.w3c.dom.Attr} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GenericAttr extends AbstractAttr {

	private static final long serialVersionUID = 1L;
	/**
	 * Is this attribute immutable?
	 */
	protected boolean readonly;

	/**
	 * Creates a new Attr object.
	 */
	protected GenericAttr() {
	}

	/**
	 * Creates a new Attr object.
	 * 
	 * @param name  The attribute name for validation purposes.
	 * @param owner The owner document.
	 * @exception DOMException INVALID_CHARACTER_ERR: if name contains invalid
	 *                         characters,
	 */
	public GenericAttr(String name, AbstractDocument owner) throws DOMException {
		super(name, owner);
		setNodeName(name);
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
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new GenericAttr();
	}
}
