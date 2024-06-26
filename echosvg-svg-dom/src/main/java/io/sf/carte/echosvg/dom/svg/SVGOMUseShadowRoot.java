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
package io.sf.carte.echosvg.dom.svg;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.css.engine.CSSNavigableNode;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.AbstractDocumentFragment;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;

/**
 * This class implements {@link org.w3c.dom.DocumentFragment} interface. It is
 * used to implement the SVG use element behavioUr.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMUseShadowRoot extends AbstractDocumentFragment implements CSSNavigableNode, IdContainer {

	private static final long serialVersionUID = 1L;

	/**
	 * The parent CSS element.
	 */
	protected Element cssParentElement;

	/**
	 * Indicates if the imported css element is from this document.
	 */
	protected boolean isLocal;

	/**
	 * Creates a new DocumentFragment object.
	 */
	protected SVGOMUseShadowRoot() {
	}

	/**
	 * Creates a new DocumentFragment object.
	 */
	public SVGOMUseShadowRoot(AbstractDocument owner, Element parent, boolean isLocal) {
		ownerDocument = owner;
		cssParentElement = parent;
		this.isLocal = isLocal;
	}

	/**
	 * Tests whether this node is readonly.
	 */
	@Override
	public boolean isReadonly() {
		return false;
	}

	/**
	 * Sets this node readonly attribute.
	 */
	@Override
	public void setReadonly(boolean v) {
	}

	// IdContainer ///////////////////////////////////////////////////////////

	@Override
	public Element getElementById(String id) {
		return ownerDocument.getChildElementById(this, id);
	}

	// CSSNavigableNode //////////////////////////////////////////////////////

	/**
	 * Returns the CSS parent node of this node.
	 */
	@Override
	public Node getCSSParentNode() {
		return cssParentElement;
	}

	/**
	 * Returns the CSS previous sibling node of this node.
	 */
	@Override
	public Node getCSSPreviousSibling() {
		return null;
	}

	/**
	 * Returns the CSS next sibling node of this node.
	 */
	@Override
	public Node getCSSNextSibling() {
		return null;
	}

	/**
	 * Returns the CSS first child node of this node.
	 */
	@Override
	public Node getCSSFirstChild() {
		return getFirstChild();
	}

	/**
	 * Returns the CSS last child of this node.
	 */
	@Override
	public Node getCSSLastChild() {
		return getLastChild();
	}

	/**
	 * Returns whether this node is the root of a (conceptual) hidden tree that
	 * selectors will not work across.
	 */
	@Override
	public boolean isHiddenFromSelectors() {
		return false;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.dom.events.NodeEventTarget#getParentNodeEventTarget()}.
	 */
	@Override
	public NodeEventTarget getParentNodeEventTarget() {
		return (NodeEventTarget) getCSSParentNode();
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMUseShadowRoot();
	}

}
