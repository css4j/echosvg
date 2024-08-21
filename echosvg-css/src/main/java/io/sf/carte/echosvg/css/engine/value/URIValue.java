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
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.css.om.typed.CSSStringValue;

/**
 * This class represents URI values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class URIValue extends AbstractStringValue implements CSSStringValue {

	private String cssText;

	/**
	 * Creates a new URIValue.
	 */
	public URIValue(String cssText, String s) {
		super(s);
		this.cssText = cssText;
	}

	/**
	 * The type of the value.
	 */
	@Override
	public Type getPrimitiveType() {
		return Type.URI;
	}

	@Override
	public String getURIValue() {
		return value;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return "url(" + cssText + ')';
	}

	@Override
	public URIValue clone() {
		return new URIValue(cssText, value);
	}

}
