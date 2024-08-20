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

import org.w3c.css.om.typed.CSSKeywordValue;

import io.sf.carte.doc.style.css.parser.ParseHelper;

/**
 * Identifier values.
 *
 * @author See Git history.
 * @version $Id$
 */
public class IdentValue extends AbstractStringValue implements CSSKeywordValue {

	/**
	 * Creates a new IdentValue.
	 */
	public IdentValue(String s) {
		super(s);
	}

	/**
	 * Creates a constant identifier.
	 * <p>
	 * If you want to have a modifiable copy of the returned value, clone it.
	 * </p>
	 * 
	 * @param s the identifier.
	 * @return an immutable identifier value.
	 */
	public static IdentValue createConstant(String s) {
		return new ImmutableIdentValue(s);
	}

	/**
	 * The type of the value.
	 */
	@Override
	public Type getPrimitiveType() {
		return Type.IDENT;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj the reference object with which to compare.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Value)) {
			return false;
		}
		Value v = (Value) obj;
		return v.getPrimitiveType() == Type.IDENT && value.equals(v.getIdentifierValue());
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return ParseHelper.escape(value);
	}

	@Override
	public String getIdentifierValue() {
		return value;
	}

	@Override
	public String getStringValue() {
		return value;
	}

	@Override
	public IdentValue clone() {
		return new IdentValue(value);
	}

}
