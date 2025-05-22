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

import io.sf.carte.doc.style.css.nsac.SelectorList;

/**
 * This class represents a style rule.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class StyleRule extends RuleGroup implements DeclarationsRule, GroupingRule {

	/**
	 * The type constant.
	 */
	public static final short TYPE = CSSRule.STYLE_RULE;

	/**
	 * The selector list.
	 */
	protected SelectorList selectorList;

	/**
	 * The absolute selector list.
	 */
	SelectorList absSelectorList;

	/**
	 * The style declaration.
	 */
	protected StyleDeclaration styleDeclaration;

	@Override
	protected Rule[] createRuleList() {
		return new Rule[1];
	}

	/**
	 * Returns a constant identifying the rule type.
	 */
	@Override
	public short getType() {
		return TYPE;
	}

	/**
	 * Sets the selector list.
	 */
	void setSelectorList(SelectorList sl) {
		selectorList = sl;
	}

	/**
	 * Returns the selector list.
	 */
	public SelectorList getSelectorList() {
		return selectorList;
	}

	/**
	 * Returns the absolute selector list.
	 */
	SelectorList getAbsoluteSelectorList() {
		return absSelectorList;
	}

	void setAbsoluteSelectorList(SelectorList absoluteSelector) {
		this.absSelectorList = absoluteSelector;
	}

	/**
	 * Sets the style map.
	 */
	public void setStyleDeclaration(StyleDeclaration sd) {
		styleDeclaration = sd;
	}

	/**
	 * Returns the style declaration.
	 */
	public StyleDeclaration getStyleDeclaration() {
		return styleDeclaration;
	}

	/**
	 * Returns a printable representation of this style rule.
	 */
	@Override
	public String toString(CSSEngine eng) {
		StringBuilder sb = new StringBuilder();
		if (selectorList != null) {
			sb.append(selectorList.item(0));
			for (int i = 1; i < selectorList.getLength(); i++) {
				sb.append(", ");
				sb.append(selectorList.item(i));
			}
		}
		sb.append(" {\n");
		if (styleDeclaration != null) {
			sb.append(styleDeclaration.toString(eng));
		}
		sb.append("}\n");
		return sb.toString();
	}

}
