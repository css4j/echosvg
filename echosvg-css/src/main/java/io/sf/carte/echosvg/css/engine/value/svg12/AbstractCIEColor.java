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
package io.sf.carte.echosvg.css.engine.value.svg12;

import io.sf.carte.echosvg.css.engine.value.ColorValue;

/**
 * This is a base class for CIE Lab/LCH color values.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractCIEColor extends ColorValue {

	/** The three color values. */
	protected float[] values = new float[3];

	/**
	 * The white point in XYZ coordinates, initialized to D50.
	 * <p>
	 * Data from CIE Publication 15.2 - 1986 Page 55 Table 1.4.
	 * </p>
	 */
	protected float[] whitepoint = new float[] {96.42f, 100f, 82.49f};

	/**
	 * Creates a new CIE-based color.
	 * 
	 * @param components the color components
	 * @param whitepoint the white point in CIE XYZ coordinates
	 */
	protected AbstractCIEColor(float[] components, float[] whitepoint) {
		System.arraycopy(components, 0, this.values, 0, this.values.length);
		if (whitepoint != null) {
			System.arraycopy(whitepoint, 0, this.whitepoint, 0, this.whitepoint.length);
		}
	}

	/**
	 * Returns the color values.
	 * 
	 * @return the color values
	 */
	public float[] getColorValues() {
		float[] copy = new float[3];
		System.arraycopy(this.values, 0, copy, 0, copy.length);
		return copy;
	}

	/**
	 * Returns the white point in CIE XYZ coordinates.
	 * 
	 * @return the white point in CIE XYZ coordinates
	 */
	public float[] getWhitePoint() {
		float[] copy = new float[3];
		System.arraycopy(this.whitepoint, 0, copy, 0, copy.length);
		return copy;
	}

	public abstract String getFunctionName();

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder(getFunctionName());
		sb.append('(');
		sb.append(values[0]);
		sb.append(", ");
		sb.append(values[1]);
		sb.append(", ");
		sb.append(values[2]);
		sb.append(')');
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getCssText();
	}

}
