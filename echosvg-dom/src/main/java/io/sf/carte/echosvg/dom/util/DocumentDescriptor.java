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

import java.util.concurrent.locks.ReentrantLock;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.util.CleanerThread;

/**
 * This class contains informations about a document.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DocumentDescriptor {

	/**
	 * The table initial capacity
	 */
	protected static final int INITIAL_CAPACITY = 101;

	/**
	 * The table lock
	 */
	private final ReentrantLock tableLock = new ReentrantLock();

	/**
	 * The underlying array
	 */
	protected Entry[] table;

	/**
	 * The number of entries
	 */
	protected int count;

	/**
	 * Creates a new table.
	 */
	public DocumentDescriptor() {
		table = new Entry[INITIAL_CAPACITY];
	}

	/**
	 * Returns the number of elements in the document.
	 */
	public int getNumberOfElements() {
		return count;
	}

	/**
	 * Returns the location line in the source file of the end element.
	 * 
	 * @return zero if the information is unknown.
	 */
	public int getLocationLine(Element elt) {
		tableLock.lock();
		try {
			int hash = elt.hashCode() & 0x7FFFFFFF;
			int index = hash % table.length;

			for (Entry e = table[index]; e != null; e = e.next) {
				if (e.hash != hash)
					continue;
				Object o = e.get();
				if (o == elt)
					return e.locationLine;
			}
		} finally {
			tableLock.unlock();
		}
		return 0;
	}

	/**
	 * Returns the location column in the source file of the end element.
	 * 
	 * @return zero if the information is unknown.
	 */
	public int getLocationColumn(Element elt) {
		tableLock.lock();
		try {
			int hash = elt.hashCode() & 0x7FFFFFFF;
			int index = hash % table.length;

			for (Entry e = table[index]; e != null; e = e.next) {
				if (e.hash != hash)
					continue;
				Object o = e.get();
				if (o == elt)
					return e.locationColumn;
			}
		} finally {
			tableLock.unlock();
		}
		return 0;
	}

	/**
	 * Sets the location in the source file of the end element.
	 */
	public void setLocation(Element elt, int line, int col) {
		tableLock.lock();
		try {
			int hash = elt.hashCode() & 0x7FFFFFFF;
			int index = hash % table.length;

			for (Entry e = table[index]; e != null; e = e.next) {
				if (e.hash != hash)
					continue;
				Object o = e.get();
				if (o == elt)
					e.locationLine = line;
			}

			// The key is not in the hash table
			int len = table.length;
			if (count++ >= (len - (len >> 2))) {
				// more than 75% loaded: grow
				rehash();
				index = hash % table.length;
			}

			Entry e = new Entry(hash, elt, line, col, table[index]);
			table[index] = e;
		} finally {
			tableLock.unlock();
		}
	}

	/**
	 * Rehash the table
	 */
	protected void rehash() {
		Entry[] oldTable = table;

		table = new Entry[oldTable.length * 2 + 1];

		for (int i = oldTable.length - 1; i >= 0; i--) {
			for (Entry old = oldTable[i]; old != null;) {
				Entry e = old;
				old = old.next;

				int index = e.hash % table.length;
				e.next = table[index];
				table[index] = e;
			}
		}
	}

	protected void removeEntry(Entry e) {
		tableLock.lock();
		try {
			int hash = e.hash;
			int index = hash % table.length;
			Entry curr = table[index];
			if (curr == null)
				return; // already removed???

			Entry prev = null;
			while (curr != e) {
				prev = curr;
				curr = curr.next;
			}

			if (prev == null)
				// First entry.
				table[index] = curr.next;
			else
				prev.next = curr.next;
			count--;
		} finally {
			tableLock.unlock();
		}
	}

	/**
	 * To manage collisions
	 */
	protected class Entry extends CleanerThread.WeakReferenceCleared<Element> {

		/**
		 * The hash code
		 */
		public int hash;

		/**
		 * The line number.
		 */
		public int locationLine;

		/**
		 * The column number.
		 */
		public int locationColumn;

		/**
		 * The next entry
		 */
		public Entry next;

		/**
		 * Creates a new entry
		 */
		public Entry(int hash, Element element, int locationLine, int locationColumn, Entry next) {
			super(element);
			this.hash = hash;
			this.locationLine = locationLine;
			this.locationColumn = locationColumn;
			this.next = next;
		}

		@Override
		public void cleared() {
			removeEntry(this);
		}

	}

}
