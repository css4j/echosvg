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
package io.sf.carte.echosvg.dom.xbl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.dom.AbstractNode;

/**
 * An XBL manager that performs no XBL processing.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GenericXBLManager implements XBLManager {

	/**
	 * Whether XBL processing is currently taking place.
	 */
	protected boolean isProcessing;

	/**
	 * Starts XBL processing on the document.
	 */
	@Override
	public void startProcessing() {
		isProcessing = true;
	}

	/**
	 * Stops XBL processing on the document.
	 */
	@Override
	public void stopProcessing() {
		isProcessing = false;
	}

	/**
	 * Returns whether XBL processing is currently enabled.
	 */
	@Override
	public boolean isProcessing() {
		return isProcessing;
	}

	/**
	 * Get the parent of a node in the fully flattened tree.
	 */
	@Override
	public Node getXblParentNode(Node n) {
		return n.getParentNode();
	}

	/**
	 * Get the list of child nodes of a node in the fully flattened tree.
	 */
	@Override
	public NodeList getXblChildNodes(Node n) {
		return n.getChildNodes();
	}

	/**
	 * Get the list of child nodes of a node in the fully flattened tree that are
	 * within the same shadow scope.
	 */
	@Override
	public NodeList getXblScopedChildNodes(Node n) {
		return n.getChildNodes();
	}

	/**
	 * Get the first child node of a node in the fully flattened tree.
	 */
	@Override
	public Node getXblFirstChild(Node n) {
		return n.getFirstChild();
	}

	/**
	 * Get the last child node of a node in the fully flattened tree.
	 */
	@Override
	public Node getXblLastChild(Node n) {
		return n.getLastChild();
	}

	/**
	 * Get the node which directly precedes a node in the xblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Node getXblPreviousSibling(Node n) {
		return n.getPreviousSibling();
	}

	/**
	 * Get the node which directly follows a node in thexblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Node getXblNextSibling(Node n) {
		return n.getNextSibling();
	}

	/**
	 * Get the first element child of a node in the fully flattened tree.
	 */
	@Override
	public Element getXblFirstElementChild(Node n) {
		Node m = n.getFirstChild();
		while (m != null && m.getNodeType() != Node.ELEMENT_NODE) {
			m = m.getNextSibling();
		}
		return (Element) m;
	}

	/**
	 * Get the last element child of a node in the fully flattened tree.
	 */
	@Override
	public Element getXblLastElementChild(Node n) {
		Node m = n.getLastChild();
		while (m != null && m.getNodeType() != Node.ELEMENT_NODE) {
			m = m.getPreviousSibling();
		}
		return (Element) m;
	}

	/**
	 * Get the first element that precedes the a node in the xblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Element getXblPreviousElementSibling(Node n) {
		Node m = n;
		do {
			m = m.getPreviousSibling();
		} while (m != null && m.getNodeType() != Node.ELEMENT_NODE);
		return (Element) m;
	}

	/**
	 * Get the first element that follows a node in the xblParentNode's
	 * xblChildNodes list.
	 */
	@Override
	public Element getXblNextElementSibling(Node n) {
		Node m = n;
		do {
			m = m.getNextSibling();
		} while (m != null && m.getNodeType() != Node.ELEMENT_NODE);
		return (Element) m;
	}

	/**
	 * Get the bound element whose shadow tree a node resides in.
	 */
	@Override
	public Element getXblBoundElement(Node n) {
		return null;
	}

	/**
	 * Get the shadow tree of a node.
	 */
	@Override
	public Element getXblShadowTree(Node n) {
		return null;
	}

	/**
	 * Get the xbl:definition elements currently binding an element.
	 */
	@Override
	public NodeList getXblDefinitions(Node n) {
		return AbstractNode.EMPTY_NODE_LIST;
	}

}
