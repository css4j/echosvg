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
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.BooleanCondition;
import io.sf.carte.doc.style.css.nsac.CSSBudgetException;
import io.sf.carte.doc.style.css.nsac.CSSException;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.om.CSSOMParser;
import io.sf.carte.doc.style.css.parser.CSSParser;

/**
 * This class represents a {@code @supports} CSS rule.
 *
 * @version $Id$
 */
public class SupportsRule extends RuleGroup implements GroupingRule {

	/**
	 * The type constant.
	 */
	public static final short TYPE = CSSRule.SUPPORTS_RULE;

	/**
	 * The media list.
	 */
	private BooleanCondition condition;

	boolean supports;

	public SupportsRule(BooleanCondition condition) {
		super();
		this.condition = condition;
	}

	/**
	 * Returns a constant identifying the rule type.
	 */
	@Override
	public short getType() {
		return TYPE;
	}

	public BooleanCondition getCondition() {
		return condition;
	}

	public String getConditionText() {
		return condition != null ? condition.toString() : "";
	}

	public void setConditionText(String conditionText) throws DOMException {
		parseConditionText(conditionText);
	}

	/**
	 * Parse the condition text.
	 * 
	 * @param conditionText the condition text.
	 */
	private void parseConditionText(String conditionText) throws DOMException {
		CSSParser parser = new CSSOMParser();
		parser.setFlag(Parser.Flag.VALUE_COMMENTS_IGNORE);
		try {
			condition = parser.parseSupportsCondition(conditionText, null);
		} catch (CSSBudgetException e) {
			DOMException ex = new DOMException(DOMException.NOT_SUPPORTED_ERR,
					"Limit found while parsing condition " + conditionText);
			ex.initCause(e);
			throw ex;
		} catch (CSSException e) {
			DOMException ex = new DOMException(DOMException.SYNTAX_ERR,
					"Error parsing condition: " + conditionText);
			ex.initCause(e);
			throw ex;
		}
	}

	/**
	 * Returns a printable representation of this media rule.
	 */
	@Override
	public String toString(CSSEngine eng) {
		StringBuilder sb = new StringBuilder();
		sb.append("@supports");
		if (condition != null) {
			sb.append(condition.toString());
		}
		sb.append(" {\n");
		for (int i = 0; i < size; i++) {
			sb.append(rules[i].toString(eng));
		}
		sb.append("}\n");
		return sb.toString();
	}

}
