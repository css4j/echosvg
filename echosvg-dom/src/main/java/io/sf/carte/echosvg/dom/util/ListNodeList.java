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

package io.sf.carte.echosvg.dom.util;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A simple class that implements the DOM NodeList interface by wrapping an Java
 * List instace.
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ListNodeList implements NodeList {

	protected List<Element> list;

	public ListNodeList(List<Element> list) {
		this.list = list;
	}

	/**
	 * <b>DOM</b>: Implements {@link NodeList#item(int)}.
	 */
	@Override
	public Node item(int index) {
		if ((index < 0) || (index > list.size()))
			return null;
		return list.get(index);
	}

	/**
	 * <b>DOM</b>: Implements {@link NodeList#getLength()}.
	 */
	@Override
	public int getLength() {
		return list.size();
	}

}
