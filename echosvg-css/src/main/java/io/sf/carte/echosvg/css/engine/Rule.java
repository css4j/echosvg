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

/**
 * This interface represents a CSS rule.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface Rule {

	/**
	 * Returns a constant identifying the rule type.
	 */
	short getType();

	/**
	 * Returns the parent group.
	 * 
	 * @return the parent group, or {@code null} if the parent group of this rule
	 *         does not matter when processing it.
	 */
	default RuleGroup getParent() {
		return null;
	}

	/**
	 * Returns a printable representation of this rule.
	 */
	String toString(CSSEngine eng);

}
