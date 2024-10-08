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

import java.util.ArrayList;
import java.util.List;

/**
 * A class for receiving notification of parsed list items.
 */
public class ListBuilder implements ListHandler {

	/**
	 * 
	 */
	private final AbstractSVGList abstractSVGList;

	/**
	 * @param abstractSVGList
	 */
	public ListBuilder(AbstractSVGList abstractSVGList) {
		this.abstractSVGList = abstractSVGList;
	}

	/**
	 * The list being built.
	 */
	protected List<SVGItem> list;

	/**
	 * Returns the newly created list.
	 */
	public List<SVGItem> getList() {
		return list;
	}

	/**
	 * Begins the construction of the list.
	 */
	@Override
	public void startList() {
		list = new ArrayList<>();
	}

	/**
	 * Adds an item to the list.
	 */
	@Override
	public void item(SVGItem item) {
		item.setParent(this.abstractSVGList);
		list.add(item);
	}

	/**
	 * Ends the construction of the list.
	 */
	@Override
	public void endList() {
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		for (SVGItem item : list) {
			buf.append(item.getValueAsString()).append('\n');
		}
		return buf.toString();
	}

}
