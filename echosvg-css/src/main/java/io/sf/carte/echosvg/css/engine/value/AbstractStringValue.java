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

import java.util.Objects;

import org.w3c.api.DOMTypeException;

/**
 * Abstract class for string values.
 *
 * @version $Id$
 */
public abstract class AbstractStringValue extends AbstractValue {

	/**
	 * The value of the string
	 */
	String value;

	/**
	 * Creates a new StringValue.
	 */
	public AbstractStringValue(String s) {
		value = s;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPrimitiveType(), value);
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj the reference object with which to compare.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractStringValue)
				|| getPrimitiveType() != ((AbstractStringValue) obj).getPrimitiveType()) {
			return false;
		}
		AbstractStringValue v = (AbstractStringValue) obj;
		return value.equals(v.value);
	}

	/**
	 * This method is used to get the string value.
	 * 
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String value) throws DOMTypeException {
		if (value == null || value.isEmpty()) {
			throw new DOMTypeException("Value is null or empty.");
		}
		this.value = value;

		if (handler != null) {
			handler.valueChanged(this);
		}
	}

	@Override
	public abstract AbstractStringValue clone();

}
