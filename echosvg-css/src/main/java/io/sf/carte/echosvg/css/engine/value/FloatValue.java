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

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class represents float values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class FloatValue extends AbstractValue {

	/**
	 * Returns the CSS text associated with the given type/value pair.
	 */
	public static String getCssText(short unit, float value) {
		if (unit < 0 || unit >= UNITS.length) {
			throw new DOMException(DOMException.SYNTAX_ERR, "");
		}
		String s = String.valueOf(value);
		if (s.endsWith(".0")) {
			s = s.substring(0, s.length() - 2);
		}
		return s + UNITS[unit - CSSPrimitiveValue.CSS_NUMBER];
	}

	/**
	 * The unit types representations
	 */
	protected static final String[] UNITS = { "", "%", "em", "ex", "px", "cm", "mm", "in", "pt", "pc", "deg", "rad",
			"grad", "ms", "s", "Hz", "kHz", "" };

	/**
	 * The float value
	 */
	protected float floatValue;

	/**
	 * The unit type
	 */
	protected short unitType;

	/**
	 * Creates a new value.
	 */
	public FloatValue(short unitType, float floatValue) {
		this.unitType = unitType;
		this.floatValue = floatValue;
	}

	/**
	 * The type of the value.
	 */
	@Override
	public short getPrimitiveType() {
		return unitType;
	}

	/**
	 * Returns the float value.
	 */
	@Override
	public float getFloatValue() {
		return floatValue;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return getCssText(unitType, floatValue);
	}

	/**
	 * Returns a printable representation of this value.
	 */
	@Override
	public String toString() {
		return getCssText();
	}
}
