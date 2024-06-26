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

import org.w3c.dom.Node;

import io.sf.carte.echosvg.dom.AbstractDocument;

/**
 * This class implements the xbl:handlerGroup element.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class XBLOMHandlerGroupElement extends XBLOMElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new XBLOMHandlerGroupElement.
	 */
	protected XBLOMHandlerGroupElement() {
	}

	/**
	 * Creates a new XBLOMHandlerGroupElement.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public XBLOMHandlerGroupElement(String prefix, AbstractDocument owner) {
		super(prefix, owner);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return XBL_HANDLER_GROUP_TAG;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new XBLOMHandlerGroupElement();
	}

}
