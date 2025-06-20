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

import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class represents a @font-face CSS rule.
 *
 * This mostly exists to give us a place to store the URI to be used for 'src'
 * URI resolution.
 *
 * <p>
 * Original author: <a href="mailto:deweese@apache.org">l449433</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class FontFaceRule implements Rule {

	/**
	 * The type constant.
	 */
	public static final short TYPE = CSSRule.FONT_FACE_RULE;

	StyleMap sm;
	ParsedURL purl;

	public FontFaceRule(StyleMap sm, ParsedURL purl) {
		this.sm = sm;
		this.purl = purl;
	}

	/**
	 * Returns a constant identifying the rule type.
	 */
	@Override
	public short getType() {
		return TYPE;
	}

	/**
	 * Returns the URI of the @font-face rule.
	 */
	public ParsedURL getURL() {
		return purl;
	}

	/**
	 * Returns the StyleMap from the @font-face rule.
	 */
	public StyleMap getStyleMap() {
		return sm;
	}

	/**
	 * Returns a printable representation of this rule.
	 */
	@Override
	public String toString(CSSEngine eng) {
		StringBuilder sb = new StringBuilder();
		sb.append("@font-face { ");
		sb.append(sm.toString(eng));
		sb.append(" }\n");
		return sb.toString();
	}

}
