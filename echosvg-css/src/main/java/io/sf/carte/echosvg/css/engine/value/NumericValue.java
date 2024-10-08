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

import org.w3c.css.om.typed.CSSNumericValue;
import org.w3c.css.om.unit.CSSUnit;

/**
 * Base class for numeric values.
 *
 * @author See Git history.
 * @version $Id$
 */
public abstract class NumericValue extends AbstractValue implements TypedValue, CSSNumericValue {

	/**
	 * Creates a new value.
	 */
	public NumericValue() {
		super();
	}

	@Override
	public NumericValue clone() {
		return (NumericValue) super.clone();
	}

	abstract short getCSSUnit();

	@Override
	public CSSNumericType type() {
		return new NumericType();
	}

	class NumericType implements CSSNumericType {

		@Override
		public int getLength() {
			return CSSUnit.isLengthUnitType(getCSSUnit()) ? 1 : 0;
		}

		@Override
		public int getAngle() {
			return CSSUnit.isAngleUnitType(getCSSUnit()) ? 1 : 0;
		}

		@Override
		public int getTime() {
			return CSSUnit.isTimeUnitType(getCSSUnit()) ? 1 : 0;
		}

		@Override
		public int getFrequency() {
			short unit = getCSSUnit();
			return unit == CSSUnit.CSS_HZ || unit == CSSUnit.CSS_KHZ ? 1 : 0;
		}

		@Override
		public int getResolution() {
			return CSSUnit.isResolutionUnitType(getCSSUnit()) ? 1 : 0;
		}

		@Override
		public int getFlex() {
			return getCSSUnit() == CSSUnit.CSS_FR ? 1 : 0;
		}

		@Override
		public int getPercent() {
			return getCSSUnit() == CSSUnit.CSS_PERCENTAGE ? 1 : 0;
		}

		@Override
		public CSSNumericBaseType getPercentHint() {
			short unitType = getCSSUnit();
			CSSNumericBaseType baseType = null;
			if (CSSUnit.isLengthUnitType(unitType)) {
				baseType = CSSNumericBaseType.length;
			} else if (unitType == CSSUnit.CSS_PERCENTAGE) {
				baseType = CSSNumericBaseType.percent;
			} else if (CSSUnit.isTimeUnitType(unitType)) {
				baseType = CSSNumericBaseType.time;
			} else if (CSSUnit.isAngleUnitType(unitType)) {
				baseType = CSSNumericBaseType.angle;
			} else if (CSSUnit.isResolutionUnitType(unitType)) {
				baseType = CSSNumericBaseType.resolution;
			} else if (unitType == CSSUnit.CSS_HZ || unitType == CSSUnit.CSS_KHZ) {
				baseType = CSSNumericBaseType.frequency;
			} else if (unitType == CSSUnit.CSS_FR) {
				baseType = CSSNumericBaseType.flex;
			}
			return baseType;
		}

	}

}
