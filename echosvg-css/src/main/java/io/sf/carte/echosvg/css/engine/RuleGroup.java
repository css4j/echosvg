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

import org.w3c.dom.DOMException;

/**
 * This class represents a group of rules.
 */
public abstract class RuleGroup {

	/**
	 * The rules.
	 */
	protected Rule[] rules = createRuleList();

	/**
	 * The number of rules.
	 */
	protected int size;

	/**
	 * The parent rule group, if any.
	 */
	protected RuleGroup parent;

	protected RuleGroup() {
		super();
	}

	protected Rule[] createRuleList() {
		return new Rule[16];
	}

	abstract short getType();

	/**
	 * Returns the parent group.
	 */
	public RuleGroup getParent() {
		return parent;
	}

	/**
	 * Sets the parent group.
	 */
	public void setParent(RuleGroup ss) {
		parent = ss;
	}

	/**
	 * Returns the number of rules.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the rule at the given index.
	 */
	public Rule getRule(int i) {
		return rules[i];
	}

	/**
	 * Removes a CSS rule from the CSS rule list at <code>index</code>.
	 * 
	 * @param index the rule list index at which the rule must be removed.
	 * @throws DOMException INDEX_SIZE_ERR if <code>index</code> is greater than or
	 *                      equal to {@link #getSize()}.
	 */
	public void deleteRule(int index) throws DOMException {
		if (index < 0 || index >= size) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, "Invalid index: " + index + '.');
		}
		for (int i = index; i < size - 1; i++) {
			rules[i] = rules[i + 1];
		}
		size--;
		rules[size] = null;
	}

	/**
	 * Clears the content.
	 */
	public void clear() {
		size = 0;
		rules = new Rule[10];
	}

	/**
	 * Appends a rule to the stylesheet.
	 */
	public void append(Rule r) {
		if (size == rules.length) {
			Rule[] t = new Rule[size * 2];
			System.arraycopy(rules, 0, t, 0, size);
			rules = t;
		}
		rules[size++] = r;
	}

	/**
	 * Returns a printable representation of this style-sheet.
	 */
	public String toString(CSSEngine eng) {
		StringBuilder sb = new StringBuilder(size * 8);
		for (int i = 0; i < size; i++) {
			sb.append(rules[i].toString(eng));
		}
		return sb.toString();
	}

}
