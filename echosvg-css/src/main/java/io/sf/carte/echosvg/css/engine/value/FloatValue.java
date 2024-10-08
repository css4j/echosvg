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

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import org.w3c.api.DOMSyntaxException;
import org.w3c.api.DOMTypeException;
import org.w3c.css.om.typed.CSSUnitValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSNumberValue;
import io.sf.carte.doc.style.css.UnitStringToId;
import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.doc.style.css.property.NumberValue;

/**
 * This class represents float values.
 *
 * @author See Git history.
 * @version $Id$
 */
public class FloatValue extends NumericValue implements CSSNumberValue, CSSUnitValue {

	/**
	 * Returns the CSS text associated with the given type/value pair.
	 */
	public static String getCssText(short unit, float value) {
		if (unit == CSSUnit.CSS_INVALID) {
			throw new DOMException(DOMException.SYNTAX_ERR, "");
		}
		String s = String.valueOf(value);
		if (s.endsWith(".0")) {
			s = s.substring(0, s.length() - 2);
		}
		return s + CSSUnit.dimensionUnitString(unit);
	}

	/**
	 * Create a constant with the given unit and value.
	 * <p>
	 * If you want to have a modifiable copy of the returned value, clone it.
	 * </p>
	 * 
	 * @param unit  the unit.
	 * @param value the value expressed in that unit.
	 * @return the immutable value.
	 */
	public static FloatValue createConstant(short unit, float value) {
		return new ImmutableUnitValue(unit, value);
	}

	/**
	 * The float value
	 */
	private float floatValue;

	/**
	 * The unit type
	 */
	private short unitType;

	/**
	 * True if this value is the result of a calculation
	 */
	private boolean calculated = false;

	/**
	 * True if this number is in the same unit as was specified.
	 */
	private boolean specified = true;

	/**
	 * Creates a new value.
	 *
	 * @param unit  the unit.
	 * @param value the value expressed in that unit.
	 */
	public FloatValue(short unitType, float floatValue) {
		this.unitType = unitType;
		this.floatValue = floatValue;
	}

	/**
	 * The type of the value.
	 */
	@Override
	public Type getPrimitiveType() {
		return Type.NUMERIC;
	}

	@Override
	public short getUnitType() {
		return unitType;
	}

	/**
	 * Returns the float value.
	 */
	@Override
	public float getFloatValue() {
		return floatValue;
	}

	@Override
	public double getValue() {
		return floatValue;
	}

	@Override
	public void setValue(double value) {
		this.floatValue = (float) value;

		if (handler != null) {
			handler.valueChanged(this);
		}
	}

	public void setValue(String cssText) throws DOMException {
		setCssText(cssText);
	}

	@Override
	public String getUnit() {
		return CSSUnit.dimensionUnitString(unitType);
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return getCssText(unitType, floatValue);
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		CSSParser parser = new CSSParser();
		try {
			LexicalUnit lu = parser.parsePropertyValue(new StringReader(cssText));
			if (lu.getNextLexicalUnit() != null) {
				throw new DOMSyntaxException("Invalid number " + cssText);
			}
			switch (lu.getLexicalUnitType()) {
			case INTEGER:
				floatValue = lu.getIntegerValue();
				unitType = CSSUnit.CSS_NUMBER;
				break;
			case REAL:
				floatValue = lu.getFloatValue();
				unitType = CSSUnit.CSS_NUMBER;
				break;
			case PERCENTAGE:
				floatValue = lu.getFloatValue();
				unitType = CSSUnit.CSS_PERCENTAGE;
				break;
			case DIMENSION:
				floatValue = lu.getFloatValue();
				unitType = lu.getCssUnit();
				break;
			default:
				throw new DOMTypeException("Invalid unit value: " + cssText);
			}
		} catch (IOException e) {
		} catch (CSSParseException e) {
			throw new DOMSyntaxException(e);
		}

		if (handler != null) {
			handler.valueChanged(this);
		}
	}

	@Override
	public boolean isCalculatedNumber() {
		return calculated;
	}

	/**
	 * Sets whether this number is the result of a calculation.
	 * 
	 * @param calculated {@code true} if this number was calculated.
	 */
	@Override
	public void setCalculatedNumber(boolean calculated) {
		this.calculated = calculated;
		this.specified = this.specified && !calculated;
	}

	@Override
	public void setExpectInteger() throws DOMException {
		if (getUnitType() != CSSUnit.CSS_NUMBER) {
			super.setExpectInteger();
		} else if (calculated) {
			floatValue = Math.round(floatValue);
		} else if (!isInteger()) {
			super.setExpectInteger();
		}
	}

	private boolean isInteger() {
		return Math.rint(floatValue) == floatValue;
	}

	@Override
	public void roundToInteger() throws DOMException {
		setExpectInteger();
		floatValue = Math.round(floatValue);
	}

	@Override
	public boolean isNegativeNumber() {
		return floatValue < 0f;
	}

	@Override
	public boolean isNumberZero() {
		return floatValue == 0f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(floatValue, unitType);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Value))
			return false;
		Value other = (Value) obj;
		return other.getUnitType() == unitType
				&& Float.floatToIntBits(floatValue) == Float.floatToIntBits(other.getFloatValue());
	}

	@Override
	public CSSUnitValue to(String unit) {
		short destUnit = UnitStringToId.unitFromString(unit);
		float destValue = NumberValue.floatValueConversion(floatValue, unitType, destUnit);
		FloatValue toVal = new FloatValue(destUnit, destValue);
		return toVal;
	}

	@Override
	short getCSSUnit() {
		return unitType;
	}

	@Override
	public FloatValue clone() {
		return new FloatValue(unitType, floatValue);
	}

}
