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

import io.sf.carte.doc.style.css.nsac.LexicalUnit;

/**
 * A value that depends on the computed style.
 *
 * @version $Id$
 */
class PendingStyleValue extends AbstractValue {

	private static final long serialVersionUID = 1L;

	private LexicalUnit lunit = null;

	/**
	 * Creates a new PendingStyleValue object.
	 * 
	 * @param lunit the lexical unit with the style-depending function(s).
	 */
	public PendingStyleValue(LexicalUnit lunit) throws IllegalArgumentException {
		super();
		this.lunit = lunit;
	}

	@Override
	public Type getPrimitiveType() {
		return Type.UNKNOWN;
	}

	public LexicalUnit getLexicalUnit() {
		return lunit;
	}

	@Override
	public String getCssText() {
		return lunit.toString();
	}

	@Override
	public PendingStyleValue clone() {
		PendingStyleValue clon = (PendingStyleValue) super.clone();
		return clon;
	}

}
