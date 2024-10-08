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

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.RGBAColor;
import io.sf.carte.doc.style.css.property.NumberValue;

/**
 * A gateway value between Typed OM and its internal representation.
 *
 * @version $Id$
 */
public interface TypedValue extends Value, io.sf.carte.doc.style.css.CSSTypedValue {

	@Override
	default short getUnitType() {
		return CSSUnit.CSS_INVALID;
	}

	@Override
	default void setExpectInteger() throws DOMException {
		throw new DOMException(DOMException.TYPE_MISMATCH_ERR,
				"Expected an integer, found type " + getPrimitiveType());
	}

	@Override
	default void setFloatValue(short unitType, float floatValue) throws DOMException {
		setFloatValue(NumberValue.floatValueConversion(floatValue, unitType, getUnitType()));
	}

	@Override
	default float getFloatValue(short unitType) throws DOMException {
		return NumberValue.floatValueConversion(getFloatValue(), getUnitType(), unitType);
	}

	@Override
	default void setStringValue(Type stringType, String stringValue) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Please use setValue()");
	}

	@Override
	default RGBAColor toRGBColor() throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Please use getColorValue()");
	}

	@Override
	default boolean isCalculatedNumber() {
		return false;
	}

	@Override
	default boolean isNumberZero() {
		return false;
	}

	@Override
	TypedValue clone();

}
