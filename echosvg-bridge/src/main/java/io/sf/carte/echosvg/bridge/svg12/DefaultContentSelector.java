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
package io.sf.carte.echosvg.bridge.svg12;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.anim.dom.XBLOMContentElement;
import io.sf.carte.echosvg.dom.ArrayNodeList;

/**
 * A class to handle content selection when the includes attribute is not
 * present.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultContentSelector extends AbstractContentSelector {

	/**
	 * The selected nodes.
	 */
	protected SelectedNodes selectedContent;

	/**
	 * Creates a new XPathSubsetContentSelector object.
	 */
	public DefaultContentSelector(ContentManager cm, XBLOMContentElement content, Element bound) {
		super(cm, content, bound);
	}

	/**
	 * Returns a list of nodes that were matched by the given selector string.
	 */
	@Override
	public NodeList getSelectedContent() {
		if (selectedContent == null) {
			selectedContent = new SelectedNodes();
		}
		return selectedContent;
	}

	/**
	 * Forces this selector to update its selected nodes list. Returns true if the
	 * selected node list needed updating. This assumes that the previous content
	 * elements in this shadow tree (in document order) have up-to-date
	 * selectedContent lists.
	 */
	@Override
	boolean update() {
		if (selectedContent == null) {
			selectedContent = new SelectedNodes();
			return true;
		}
		return selectedContent.update();
	}

	/**
	 * Implementation of NodeList that contains the nodes that matched this
	 * selector.
	 */
	protected class SelectedNodes extends ArrayNodeList {

		/**
		 * Creates a new SelectedNodes object.
		 */
		public SelectedNodes() {
			update();
		}

		protected boolean update() {
			ArrayList<?> oldNodes = (ArrayList<?>) nodes.clone();
			nodes.clear();
			for (Node n = boundElement.getFirstChild(); n != null; n = n.getNextSibling()) {
				if (isSelected(n)) {
					continue;
				}
				nodes.add(n);
			}
			int nodesSize = nodes.size();
			if (oldNodes.size() != nodesSize) {
				return true;
			}
			for (int i = 0; i < nodesSize; i++) {
				if (oldNodes.get(i) != nodes.get(i)) {
					return true;
				}
			}
			return false;
		}

	}

}
