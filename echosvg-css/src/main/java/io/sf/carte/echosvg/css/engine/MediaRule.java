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
 * This class represents a @media CSS rule.
 *
 *<p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class MediaRule extends MediaGroup implements GroupingRule {

	/**
	 * The type constant.
	 */
	public static final short TYPE = CSSRule.MEDIA_RULE;

	/**
	 * Returns a constant identifying the rule type.
	 */
	@Override
	public short getType() {
		return TYPE;
	}

	/**
	 * Returns a printable representation of this media rule.
	 */
	@Override
	public String toString(CSSEngine eng) {
		StringBuilder sb = new StringBuilder();
		sb.append("@media");
		if (mediaList != null) {
			for (int i = 0; i < mediaList.getLength(); i++) {
				sb.append(' ');
				sb.append(mediaList.item(i));
			}
		}
		sb.append(" {\n");
		for (int i = 0; i < size; i++) {
			sb.append(rules[i].toString(eng));
		}
		sb.append("}\n");
		return sb.toString();
	}

}
