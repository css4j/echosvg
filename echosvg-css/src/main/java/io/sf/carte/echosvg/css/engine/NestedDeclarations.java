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

import org.w3c.css.om.CSSRule;

/**
 * Nested declarations.
 * 
 * @version $Id$
 */
public class NestedDeclarations extends AbstractRule implements DeclarationsRule {

	/**
	 * The type constant.
	 */
	public static final short TYPE = CSSRule.NESTED_DECLARATIONS;

	/**
	 * The style declaration.
	 */
	StyleDeclaration styleDeclaration;

	/**
	 * Returns a constant identifying the rule type.
	 */
	@Override
	public short getType() {
		return TYPE;
	}

	/**
	 * Sets the style map.
	 */
	@Override
	public void setStyleDeclaration(StyleDeclaration sd) {
		styleDeclaration = sd;
	}

	/**
	 * Returns the style declaration.
	 */
	@Override
	public StyleDeclaration getStyleDeclaration() {
		return styleDeclaration;
	}

	/**
	 * Returns a printable representation of this rule.
	 */
	@Override
	public String toString(CSSEngine eng) {
		if (styleDeclaration != null) {
			return styleDeclaration.toString(eng);
		}
		return "";
	}

}
