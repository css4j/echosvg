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
package io.sf.carte.echosvg.css.engine.value;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.css.om.typed.CSSStyleValueList;

/**
 * This class represents a list of values.
 *
 * @author See Git history.
 * @version $Id$
 */
public class AbstractValueList<V extends Value> extends ComponentValue implements CSSStyleValueList<V> {

	private static final long serialVersionUID = 1L;

	/**
	 * The items.
	 */
	ArrayList<V> items;

	/**
	 * The list separator.
	 */
	private char separator;

	/**
	 * Creates a ListValue.
	 */
	public AbstractValueList() {
		this(',');
	}

	/**
	 * Creates a ListValue with the given separator.
	 * 
	 * @param s the separator.
	 */
	public AbstractValueList(char s) {
		this(s, 5);
	}

	/**
	 * Creates a ListValue with the given separator and an initial capacity.
	 * 
	 * @param s               the separator.
	 * @param initialCapacity the initial capacity.
	 */
	public AbstractValueList(char s, int initialCapacity) {
		separator = s;
		items = new ArrayList<>(initialCapacity);
	}

	/**
	 * Returns a deep copy of this instance.
	 * 
	 * @return a copy of this instance.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AbstractValueList<V> clone() {
		AbstractValueList<V> clon = (AbstractValueList<V>) super.clone();
		clon.items = new ArrayList<>(clon.getLength());
		for (V item : items) {
			clon.items.add((V) item.clone());
		}
		clon.separator = separator;
		return clon;
	}

	/**
	 * Returns the separator used for this list.
	 */
	public char getSeparatorChar() {
		return separator;
	}

	@Override
	public CssType getCssValueType() {
		return CssType.LIST;
	}

	@Override
	public Type getPrimitiveType() {
		return Type.INVALID; // Not a primitive
	}

	/**
	 * Appends an item to the list.
	 */
	public boolean add(V value) {
		boolean b = items.add(value);
		componentAdded(value);
		return b;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		int length = items.size();
		StringBuilder sb = new StringBuilder(length * 8);
		if (length > 0) {
			sb.append(items.get(0).toString());
		}
		for (int i = 1; i < length; i++) {
			sb.append(separator);
			sb.append(items.get(i).toString());
		}
		return sb.toString();
	}

	@Override
	public V item(int index) {
		return items.get(index);
	}

	@Override
	public int getLength() {
		return items.size();
	}

	@Override
	public Iterator<V> iterator() {
		return items.iterator();
	}

	public void clear() {
		items.clear();
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public V remove(int index) {
		V item = items.remove(index);
		componentRemoved(item);
		return item;
	}

	public V set(int index, V value) {
		V v = items.set(index, value);
		getComponentHandler().listValueChanged(index, value);
		return v;
	}

}
