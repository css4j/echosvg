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

import java.util.Iterator;

import org.w3c.dom.DOMException;

/**
 * This class represents a list of values.
 *
 * @author See Git history.
 * @version $Id$
 */
public class ListValue extends AbstractValueList<Value> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a ListValue.
	 */
	public ListValue() {
		super(',');
	}

	/**
	 * Creates a ListValue with the given separator.
	 * 
	 * @param s the separator.
	 */
	public ListValue(char s) {
		super(s);
	}

	/**
	 * Creates a ListValue with the given separator and initial capacity.
	 * 
	 * @param s               the separator.
	 * @param initialCapacity the initial capacity.
	 */
	public ListValue(char s, int initialCapacity) {
		super(s, initialCapacity);
	}

	/**
	 * Appends an item to the list.
	 */
	public void append(Value v) {
		add(v);
	}

	/**
	 * Create an unmodifiable view of this list value.
	 * 
	 * @return the unmodifiable view.
	 */
	public ListValue createUnmodifiableView() {
		return new UnmodifiableListValue();
	}

	private class UnmodifiableListValue extends ListValue {

		private static final long serialVersionUID = 1L;

		UnmodifiableListValue() {
			super(ListValue.this.getSeparatorChar(), 1);
		}

		@Override
		public char getSeparatorChar() {
			return ListValue.this.getSeparatorChar();
		}

		@Override
		public String getCssText() {
			return ListValue.this.getCssText();
		}

		@Override
		public Value item(int index) {
			return ListValue.this.item(index);
		}

		@Override
		public int getLength() {
			return ListValue.this.getLength();
		}

		@Override
		public Iterator<Value> iterator() {
			return new Iterator<Value>() {
				Iterator<Value> iter = ListValue.this.items.iterator();

				@Override
				public boolean hasNext() {
					return iter.hasNext();
				}

				@Override
				public Value next() {
					return iter.next();
				}

			};
		}

		@Override
		public boolean isEmpty() {
			return ListValue.this.isEmpty();
		}

		@Override
		public AbstractValueList<Value> clone() {
			return ListValue.this.clone();
		}

		@Override
		public boolean add(Value value) {
			throw createNoModificationDOMException();
		}

		@Override
		public void clear() {
			throw createNoModificationDOMException();
		}

		@Override
		public Value remove(int index) {
			throw createNoModificationDOMException();
		}

		@Override
		public Value set(int index, Value value) {
			throw createNoModificationDOMException();
		}

		/**
		 * Creates a NO_MODIFICATION_ALLOWED_ERR exception.
		 */
		private DOMException createNoModificationDOMException() {
			return new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
					"Cannot modify this list.");
		}

	}

}
