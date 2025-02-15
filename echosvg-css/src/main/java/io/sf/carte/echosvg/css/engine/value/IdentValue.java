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

import org.w3c.api.DOMTypeException;
import org.w3c.css.om.typed.CSSKeywordValue;

import io.sf.carte.doc.style.css.parser.ParseHelper;

/**
 * Identifier values.
 *
 * @author See Git history.
 * @version $Id$
 */
public class IdentValue extends AbstractStringValue implements CSSKeywordValue {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new IdentValue.
	 * 
	 * @param s an interned identifier string.
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
		return new ImmutableIdentValue(s.intern());
	}

	/**
	 * The type of the value.
	 */
	@Override
	public Type getPrimitiveType() {
		return Type.IDENT;
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
	public void setValue(String value) throws DOMTypeException {
		if (value == null || (value = value.trim()).isEmpty()) {
			throw new DOMTypeException("Value is null or empty.");
		}
		this.value = value.intern();

		if (handler != null) {
			handler.valueChanged(this);
		}
	}

	@Override
	public boolean isIdentifier(String internedIdent) {
		return value == internedIdent;
	}

	@Override
	public IdentValue clone() {
		return (IdentValue) super.clone();
	}

}
