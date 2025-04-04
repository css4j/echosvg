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
package io.sf.carte.echosvg.css.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.value.PendingValue;
import io.sf.carte.echosvg.css.engine.value.Value;

/**
 * This class represents a collection of CSS property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class StyleDeclaration implements Serializable {

	private static final long serialVersionUID = 1L;

	protected static final int INITIAL_LENGTH = 8;

	/**
	 * The values.
	 */
	protected Value[] values = new Value[INITIAL_LENGTH];

	/**
	 * The value indexes.
	 */
	protected int[] indexes = new int[INITIAL_LENGTH];

	/**
	 * The value priorities.
	 */
	protected boolean[] priorities = new boolean[INITIAL_LENGTH];

	/**
	 * The number of values in the declaration.
	 */
	protected int count;

	/**
	 * Custom property map.
	 */
	private Map<String, LexicalUnit> customProperties = null;

	/**
	 * Returns the number of values in the declaration.
	 */
	public int size() {
		return count;
	}

	/**
	 * Returns the value at the given index.
	 */
	public Value getValue(int idx) {
		return values[idx];
	}

	/**
	 * Returns the property index of a value.
	 */
	public int getIndex(int idx) {
		return indexes[idx];
	}

	/**
	 * Tells whether a value is important.
	 */
	public boolean getPriority(int idx) {
		return priorities[idx];
	}

	/**
	 * Removes the value at the given index.
	 */
	public void remove(int idx) {
		count--;
		int from = idx + 1;
		int to = idx;
		int nCopy = count - idx;

		System.arraycopy(values, from, values, to, nCopy);
		System.arraycopy(indexes, from, indexes, to, nCopy);
		System.arraycopy(priorities, from, priorities, to, nCopy);

		values[count] = null;
		indexes[count] = 0;
		priorities[count] = false;

//        for (int i = idx; i < count; i++) {
//            values[i] = values[i + 1];
//            indexes[i] = indexes[i + 1];
//            priorities[i] = priorities[i + 1];
//        }
	}

	/**
	 * Sets a value within the declaration.
	 */
	public void put(int idx, Value v, int i, boolean prio) {
		values[idx] = v;
		indexes[idx] = i;
		priorities[idx] = prio;
	}

	/**
	 * Appends a value to the declaration.
	 */
	public void append(Value v, int idx, boolean prio) {
		if (values.length == count) {
			Value[] newval = new Value[count * 2];
			int[] newidx = new int[count * 2];
			boolean[] newprio = new boolean[count * 2];

			System.arraycopy(values, 0, newval, 0, count);
			System.arraycopy(indexes, 0, newidx, 0, count);
			System.arraycopy(priorities, 0, newprio, 0, count);

			values = newval;
			indexes = newidx;
			priorities = newprio;
		}

		for (int i = 0; i < count; i++) {
			if (indexes[i] == idx) {
				// Replace existing property values,
				// unless they are important!
				if (prio || (priorities[i] == prio)) {
					values[i] = v;
					priorities[i] = prio;
				}
				return;
			}
		}

		values[count] = v;
		indexes[count] = idx;
		priorities[count] = prio;
		count++;
	}

	/**
	 * Set a custom property value in the declaration.
	 * 
	 * @param name      the custom property name.
	 * @param value     the custom property value.
	 * @param important the priority.
	 */
	public void setCustomProperty(String name, LexicalUnit value, boolean important) {
		if (customProperties == null) {
			customProperties = new HashMap<>();
		}

		customProperties.put(name, value);
	}

	/**
	 * Get the map of custom properties.
	 * 
	 * @return the custom property map, or {@code null} if there are no custom
	 *         properties.
	 */
	public Map<String, LexicalUnit> getCustomProperties() {
		return customProperties;
	}

	/**
	 * Returns a printable representation of this style rule.
	 */
	public String toString(CSSEngine eng) {
		Set<String> pendingShorthands = null;
		StringBuilder sb = new StringBuilder(count * 8 + 32);
		for (int i = 0; i < count; i++) {
			Value value = values[i];
			if (value.getPrimitiveType() != Type.INTERNAL) {
				sb.append(eng.getPropertyName(indexes[i]));
				sb.append(": ");
				sb.append(value);
				sb.append(";\n");
			} else {
				if (pendingShorthands == null) {
					pendingShorthands = new HashSet<>();
				}
				PendingValue pending = (PendingValue) value;
				String name = pending.getShorthandName();
				if (pendingShorthands.add(name)) {
					sb.append(name);
					sb.append(": ");
					sb.append(pending.getLexicalUnit().toString());
					sb.append(";\n");
				}
			}
		}

		if (customProperties != null) {
			for (Map.Entry<String, LexicalUnit> entry : customProperties.entrySet()) {
				sb.append(entry.getKey());
				sb.append(": ");
				sb.append(entry.getValue().toString());
				sb.append(";\n");
			}
		}

		return sb.toString();
	}

}
