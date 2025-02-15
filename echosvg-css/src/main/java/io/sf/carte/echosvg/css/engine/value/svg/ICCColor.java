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
package io.sf.carte.echosvg.css.engine.value.svg;

import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.css.engine.value.AbstractValue;

/**
 * This class represents an ICC color value.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ICCColor extends AbstractValue {

	private static final long serialVersionUID = 1L;

	public static final String ICC_COLOR_FUNCTION = "icc-color";

	/**
	 * The color profile.
	 */
	protected String colorProfile;

	/**
	 * The color count.
	 */
	protected int count;

	/**
	 * The colors.
	 */
	protected float[] colors = new float[5];

	/**
	 * Creates a new ICCColor.
	 */
	public ICCColor(String name) {
		colorProfile = name;
	}

	/**
	 * Returns the color name.
	 */
	public String getColorProfile() throws DOMException {
		return colorProfile;
	}

	/**
	 * Returns the number of colors.
	 */
	public int getNumberOfColors() throws DOMException {
		return count;
	}

	/**
	 * Returns the color at the given index.
	 */
	public float getColor(int i) throws DOMException {
		return colors[i];
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder(count * 8);
		sb.append(ICC_COLOR_FUNCTION).append('(');
		sb.append(colorProfile);
		for (int i = 0; i < count; i++) {
			sb.append(", ");
			sb.append(colors[i]);
		}
		sb.append(')');
		return sb.toString();
	}

	/**
	 * Appends a color to the list.
	 */
	public void append(float c) {
		if (count == colors.length) {
			float[] t = new float[count * 2];
			System.arraycopy(colors, 0, t, 0, count);
			colors = t;
		}
		colors[count++] = c;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getCssText();
	}

	@Override
	public Type getPrimitiveType() {
		return Type.COLOR;
	}

	@Override
	public ICCColor clone() {
		ICCColor icc = new ICCColor(colorProfile);
		icc.colors = colors.clone();
		icc.count = count;
		return icc;
	}

}
