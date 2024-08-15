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

import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.css.engine.value.ColorValue;

/**
 * This class represents an ICC named color value.
 *
 * @version $Id$
 */
@Deprecated
public class ICCNamedColor extends ColorValue {

	public static final String ICC_NAMED_COLOR_FUNCTION = "icc-named-color";

	/**
	 * The color profile.
	 */
	protected String colorProfile;

	/**
	 * The color name.
	 */
	protected String colorName;

	/**
	 * Creates a new ICCNamedColor.
	 */
	public ICCNamedColor(String profileName, String colorName) {
		this.colorProfile = profileName;
		this.colorName = colorName;
	}

	@Override
	public String getCSSColorSpace() {
		return colorProfile;
	}

	/**
	 * Returns the color name.
	 */
	public String getColorProfile() throws DOMException {
		return colorProfile;
	}

	/**
	 * Returns the color name
	 */
	public String getColorName() throws DOMException {
		return colorName;
	}

	@Override
	public ColorValue clone() {
		ICCNamedColor c = (ICCNamedColor) super.clone();
		c.colorName = colorName;
		c.colorProfile = colorProfile;
		return c;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder(ICC_NAMED_COLOR_FUNCTION);
		sb.append('(');
		sb.append(colorProfile);
		sb.append(", ");
		sb.append(colorName);
		sb.append(')');
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getCssText();
	}

}
