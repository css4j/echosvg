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
 * A value meaning that a longhand property is pending the PROXY substitution of
 * a shorthand value.
 *
 * @version $Id$
 */
public class PendingValue extends LexicalValue {

	private String shorthandName;

	/**
	 * Creates a new PendingValue object.
	 * 
	 * @param shorthandName the shorthand name.
	 * @param the lexical unit that is pending substitution.
	 */
	public PendingValue(String shorthandName, LexicalUnit lunit) throws IllegalArgumentException {
		super(lunit);
		this.shorthandName = shorthandName;
	}

	@Override
	public Type getPrimitiveType() {
		return Type.INTERNAL;
	}

	public String getShorthandName() {
		return shorthandName;
	}

	@Override
	public String getCssText() {
		return "";
	}

	@Override
	public PendingValue clone() {
		PendingValue clon = (PendingValue) super.clone();
		clon.shorthandName = shorthandName;
		return clon;
	}

}
