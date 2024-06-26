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
package io.sf.carte.echosvg.extension;

import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.util.DOMUtilities;

/**
 * This class implements a simple method for handling the node 'prefix'.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas Deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class PrefixableStylableExtensionElement extends StylableExtensionElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new EchoSVGStarElement object.
	 */
	protected PrefixableStylableExtensionElement() {
	}

	/**
	 * Creates a new EchoSVGStarElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public PrefixableStylableExtensionElement(String prefix, AbstractDocument owner) {
		super(prefix, owner);
		setPrefix(prefix);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
	 */
	@Override
	public String getNodeName() {
		return (prefix == null || prefix.equals("")) ? getLocalName() : prefix + ':' + getLocalName();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#setPrefix(String)}.
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

}
