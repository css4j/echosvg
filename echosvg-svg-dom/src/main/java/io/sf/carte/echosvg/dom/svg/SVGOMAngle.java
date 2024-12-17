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
package io.sf.carte.echosvg.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAngle;

import io.sf.carte.echosvg.parser.AngleParser;
import io.sf.carte.echosvg.parser.DefaultAngleHandler;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * Implementation of an {@link SVGAngle} not associated with any attribute.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMAngle implements SVGAngle {

	/**
	 * The type of this angle.
	 */
	private short unitType;

	/**
	 * The value of this angle.
	 */
	protected float value;

	/**
	 * The unit string representations.
	 */
	protected static final String[] UNITS = { "", "", "deg", "rad", "grad" };

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#getUnitType()}.
	 */
	@Override
	public short getUnitType() {
		revalidate();
		return unitType;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#getValue()}.
	 */
	@Override
	public float getValue() {
		revalidate();
		return toUnit(getUnitType(), value, SVG_ANGLETYPE_DEG);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#setValue(float)}.
	 */
	@Override
	public void setValue(float value) throws DOMException {
		revalidate();
		this.setUnitType(SVG_ANGLETYPE_DEG);
		this.value = value;
		reset();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#getValueInSpecifiedUnits()}.
	 */
	@Override
	public float getValueInSpecifiedUnits() {
		revalidate();
		return value;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#setValueInSpecifiedUnits(float)}.
	 */
	@Override
	public void setValueInSpecifiedUnits(float value) throws DOMException {
		revalidate();
		this.value = value;
		reset();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#getValueAsString()}.
	 */
	@Override
	public String getValueAsString() {
		revalidate();
		return Float.toString(value) + UNITS[getUnitType()];
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#setValueAsString(String)}.
	 */
	@Override
	public void setValueAsString(String value) throws DOMException {
		parse(value);
		reset();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#newValueSpecifiedUnits(short,float)}.
	 */
	@Override
	public void newValueSpecifiedUnits(short unit, float value) {
		setUnitType(unit);
		this.value = value;
		reset();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAngle#convertToSpecifiedUnits(short)}.
	 */
	@Override
	public void convertToSpecifiedUnits(short unit) {
		value = toUnit(getUnitType(), value, unit);
		setUnitType(unit);
	}

	/**
	 * Resets the associated attribute value according to the current value. This
	 * should be overridden in descendant classes that associate the angle object
	 * with an attribute.
	 */
	protected void reset() {
	}

	/**
	 * Updates the values in this object according to the associated attribute
	 * value. This should be overridden in descendant classes that associate the
	 * angle object with an attribute.
	 */
	protected void revalidate() {
	}

	/**
	 * Parse a String value as an SVGAngle.
	 */
	protected void parse(String s) {
		try {
			AngleParser angleParser = new AngleParser(new DefaultAngleHandler() {
				@Override
				public void angleValue(float v) throws ParseException {
					value = v;
				}

				@Override
				public void deg() throws ParseException {
					setUnitType(SVG_ANGLETYPE_DEG);
				}

				@Override
				public void rad() throws ParseException {
					setUnitType(SVG_ANGLETYPE_RAD);
				}

				@Override
				public void grad() throws ParseException {
					setUnitType(SVG_ANGLETYPE_GRAD);
				}

				@Override
				public void turn() {
					value *= 360f;
					setUnitType(SVG_ANGLETYPE_DEG);
				}

			});

			setUnitType(SVG_ANGLETYPE_UNSPECIFIED);
			angleParser.parse(s);
		} catch (ParseException e) {
			setUnitType(SVG_ANGLETYPE_UNKNOWN);
			value = 0;
		}
	}

	/**
	 * Table of multipliers for angle unit conversion.
	 */
	protected static double[][] K = { { 1, Math.PI / 180, Math.PI / 200 }, { 180 / Math.PI, 1, 1800 / (9 * Math.PI) },
			{ 0.9, 9 * Math.PI / 1800, 1 } };

	/**
	 * Converts an angle from one unit to another.
	 */
	public static float toUnit(short fromUnit, float value, short toUnit) {
		if (fromUnit == 1) {
			fromUnit = 2;
		}
		if (toUnit == 1) {
			toUnit = 2;
		}
		return (float) (K[fromUnit - 2][toUnit - 2] * value);
	}

	public void setUnitType(short unitType) {
		this.unitType = unitType;
	}

}
