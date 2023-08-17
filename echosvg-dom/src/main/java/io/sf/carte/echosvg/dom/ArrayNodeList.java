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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An {@link ArrayList}-backed node list.
 */
public class ArrayNodeList implements NodeList, Iterable<Node> {

	/**
	 * The nodes.
	 */
	protected final ArrayList<Node> nodes;

	/**
	 * Construct a node list of the default size (10).
	 */
	public ArrayNodeList() {
		super();
		nodes = new ArrayList<>(10);
	}

	/**
	 * Construct a node list of the given size.
	 * 
	 * @param size the size.
	 */
	public ArrayNodeList(int size) {
		super();
		nodes = new ArrayList<>(size);
	}

	@Override
	public Node item(int index) {
		if (index < 0 || index >= nodes.size()) {
			return null;
		}
		return nodes.get(index);
	}

	@Override
	public int getLength() {
		return nodes.size();
	}

	/**
	 * Returns an iterator over the nodes of this list.
	 * 
	 * @return an iterator.
	 */
	@Override
	public Iterator<Node> iterator() {
		return Collections.unmodifiableList(nodes).iterator();
	}

}
