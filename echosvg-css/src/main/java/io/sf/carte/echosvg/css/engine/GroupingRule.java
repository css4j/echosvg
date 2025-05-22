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

interface GroupingRule extends Rule {

	/**
	 * Sets the parent group.
	 */
	void setParent(RuleGroup ss);

	/**
	 * Returns the number of rules.
	 */
	int getSize();

	/**
	 * Returns the rule at the given index.
	 */
	Rule getRule(int i);

	/**
	 * Removes a CSS rule from the CSS rule list at <code>index</code>.
	 * 
	 * @param index the rule list index at which the rule must be removed.
	 * @throws DOMException INDEX_SIZE_ERR if <code>index</code> is greater than or
	 *                      equal to {@link #getSize()}.
	 */
	void deleteRule(int index) throws DOMException;

	/**
	 * Clears the content.
	 */
	void clear();

	/**
	 * Appends a rule to the stylesheet.
	 */
	void append(Rule r);

}
