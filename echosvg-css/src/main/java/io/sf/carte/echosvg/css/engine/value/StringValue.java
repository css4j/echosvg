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

import io.sf.carte.doc.style.css.parser.ParseHelper;

/**
 * This class represents string values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class StringValue extends AbstractStringValue implements CSSStringValue {

	/**
	 * Creates a new StringValue.
	 */
	public StringValue(String s) {
		super(s);
	}

	/**
	 * The type of the value.
	 */
	@Override
	public Type getPrimitiveType() {
		return Type.STRING;
	}

	@Override
	public String getStringValue() {
		return value;
	}

	@Override
	public String getURIValue() {
		return value;
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
		return v.getPrimitiveType() == Type.STRING && value.equals(v.getStringValue());
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return ParseHelper.quote(value, '\'');
	}

	@Override
	public StringValue clone() {
		return new StringValue(value);
	}

}
