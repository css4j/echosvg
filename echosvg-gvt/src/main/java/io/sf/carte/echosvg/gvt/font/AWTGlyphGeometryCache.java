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
package io.sf.carte.echosvg.gvt.font;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents a doubly indexed hash table, which holds soft
 * references to the contained glyph geometry informations.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author <a href="mailto:tkormann@ilog.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AWTGlyphGeometryCache {

	/**
	 * The initial capacity
	 */
	private static final int INITIAL_CAPACITY = 71;

	/**
	 * The underlying array
	 */
	private Entry[] table;

	/**
	 * The number of entries
	 */
	private AtomicInteger count = new AtomicInteger();

	/**
	 * The reference queue.
	 */
	private ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();

	/**
	 * The table lock
	 */
	private final ReentrantLock tableLock = new ReentrantLock();

	/**
	 * Creates a new AWTGlyphGeometryCache.
	 */
	public AWTGlyphGeometryCache() {
		table = new Entry[INITIAL_CAPACITY];
	}

	/**
	 * Creates a new AWTGlyphGeometryCache.
	 * 
	 * @param c The inital capacity.
	 */
	public AWTGlyphGeometryCache(int c) {
		table = new Entry[c];
	}

	/**
	 * Returns the size of this table.
	 */
	public int size() {
		return count.get();
	}

	/**
	 * Gets the value of a variable
	 * 
	 * @return the value or null
	 */
	public Value get(char c) {
		int hash = hashCode(c) & 0x7FFFFFFF;
		int index = hash % table.length;

		for (Entry e = table[index]; e != null; e = e.next) {
			if ((e.hash == hash) && e.match(c)) {
				return e.get();
			}
		}
		return null;
	}

	/**
	 * Sets a new value for the given variable
	 * 
	 * @return the old value or null
	 */
	public Value put(char c, Value value) {
		int hash = hashCode(c) & 0x7FFFFFFF;

		tableLock.lock();

		int index;
		try {
			removeClearedEntries();

			index = hash % table.length;

			Entry e = table[index];
			if (e != null) {
				if ((e.hash == hash) && e.match(c)) {
					Value old = e.get();
					table[index] = new Entry(hash, c, value, e.next);
					return old;
				}
				Entry o = e;
				e = e.next;
				while (e != null) {
					if ((e.hash == hash) && e.match(c)) {
						Value old = e.get();
						e = new Entry(hash, c, value, e.next);
						o.next = e;
						return old;
					}

					o = e;
					e = e.next;
				}
			}

			// The key is not in the hash table
			int len = table.length;
			if (count.getAndIncrement() >= (len - (len >> 2))) {
				// more than 75% loaded: grow
				rehash();
				index = hash % table.length;
			}

			table[index] = new Entry(hash, c, value, table[index]);
		} finally {
			tableLock.unlock();
		}

		return null;
	}

	/**
	 * Clears the table.
	 */
	public void clear() {
		tableLock.lock();
		try {
			table = new Entry[INITIAL_CAPACITY];
		} finally {
			tableLock.unlock();
		}
		/*
		 * The clear() method is intended to help GC AFAIK and is currently never used,
		 * so let's use a lazySet.
		 */
		count.lazySet(0);
		referenceQueue = new ReferenceQueue<>();
	}

	/**
	 * Rehash the table.
	 * <p>
	 * This method should be called under a lock.
	 * </p>
	 */
	protected void rehash() {
		Entry[] oldTable = table;
		int olen = oldTable.length;

		int rehlen = olen * 2 + 1;
		Entry[] rehTable = new Entry[rehlen];

		for (int i = olen - 1; i >= 0; i--) {
			Entry old = oldTable[i];
			while (old != null) {
				Entry e = old;
				old = old.next;

				int index = e.hash % rehlen;
				e.next = rehTable[index];
				rehTable[index] = e;
			}
		}

		table = rehTable;
	}

	/**
	 * Computes a hash code corresponding to the given objects.
	 */
	protected int hashCode(char c) {
		return c;
	}

	/**
	 * Removes the cleared entries.
	 */
	protected void removeClearedEntries() {
		Entry e;
		while ((e = (Entry) referenceQueue.poll()) != null) {
			int index = e.hash % table.length;
			Entry t = table[index];
			if (t == e) {
				table[index] = e.next;
			} else {
				loop: for (; t != null;) {
					Entry c = t.next;
					if (c == e) {
						t.next = e.next;
						break loop;
					}
					t = c;
				}
			}
			count.decrementAndGet();
		}
	}

	/**
	 * The object that holds glyph geometry.
	 */
	public static class Value {

		private final Shape outline;
		private final Rectangle2D gmB;
		private final Rectangle2D outlineBounds;

		/**
		 * Constructs a new Value with the specified parameter.
		 */
		public Value(Shape outline, Rectangle2D gmB) {
			this.outline = outline;
			this.outlineBounds = outline.getBounds2D();
			this.gmB = gmB;
		}

		/**
		 * Returns the outline of the glyph.
		 */
		public Shape getOutline() {
			return outline;
		}

		/**
		 * Returns the bounds of the glyph according to its glyph metrics.
		 */
		public Rectangle2D getBounds2D() {
			return gmB;
		}

		/**
		 * Returns the bounds of the outline.
		 */
		public Rectangle2D getOutlineBounds2D() {
			return outlineBounds;
		}

	}

	/**
	 * To manage collisions
	 */
	protected class Entry extends SoftReference<Value> {

		/**
		 * The hash code
		 */
		private final int hash;

		/**
		 * The character
		 */
		private final char c;

		/**
		 * The next entry
		 */
		private Entry next;

		/**
		 * Creates a new entry
		 */
		public Entry(int hash, char c, Value value, Entry next) {
			super(value, referenceQueue);
			this.hash = hash;
			this.c = c;
			this.next = next;
		}

		/**
		 * Whether this entry match the given keys.
		 */
		public boolean match(char o2) {
			return c == o2;
		}

	}

}
