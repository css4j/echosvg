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

import org.w3c.api.DOMSyntaxException;
import org.w3c.css.om.typed.CSSColorValue;
import org.w3c.css.om.typed.CSSNumericValue;
import org.w3c.css.om.unit.CSSUnit;

/**
 * CSS colors.
 *
 * @author See Git history.
 * @version $Id$
 */
public abstract class ColorValue extends ComponentValue implements TypedValue, CSSColorValue {

	public static final String RGB_FUNCTION = "rgb";

	public static final String CS_SRGB = "srgb";

	public static final String LAB = "lab";

	public static final String LCH = "lch";

	public static final String CS_DISPLAY_P3 = "display-p3";

	public static final String CS_SRGB_LINEAR = "srgb-linear";

	public static final String CS_A98_RGB = "a98-rgb";

	public static final String CS_PROPHOTO_RGB = "prophoto-rgb";

	public static final String CS_REC2020 = "rec2020";

	/**
	 * The {@code xyz-d50} color space.
	 */
	public static final String CS_XYZ_D50 = "xyz-d50";

	/**
	 * The {@code xyz-d65} color space (same as {@code xyz}).
	 */
	public static final String CS_XYZ_D65 = "xyz-d65";

	/**
	 * The {@code xyz} color space (same as {@code xyz-d65}).
	 */
	public static final String CS_XYZ = "xyz";

	/**
	 * The alpha channel.
	 */
	protected NumericValue alpha;

	/**
	 * Creates a new RGBColorValue.
	 */
	protected ColorValue() {
	}

	/**
	 * Creates a new RGBColorValue.
	 * 
	 * @param a the alpha channel.
	 * @throws DOMSyntaxException if the value is an invalid opacity.
	 */
	protected ColorValue(NumericValue a) throws DOMSyntaxException {
		super();
		setAlphaChannel(a);
	}

	/**
	 * The type of the value.
	 */
	@Override
	public Type getPrimitiveType() {
		return Type.COLOR;
	}

	/**
	 * Get the color space as defined by the Typed OM specification.
	 * 
	 * @return the color space.
	 */
	public abstract String getCSSColorSpace();

	/**
	 * Gets the value of the {@code alpha} component.
	 * 
	 * @return the {@code alpha} component.
	 */
	public NumericValue getAlpha() {
		return alpha;
	}

	/**
	 * Sets the alpha channel as a percentage.
	 * 
	 * @param alpha the alpha channel as a percentage.
	 */
	public void setAlpha(double alpha) {
		this.alpha = new FloatValue(CSSUnit.CSS_PERCENTAGE, (float) alpha * 100);
		componentize(this.alpha);

		componentChanged(this.alpha);
	}

	/**
	 * Sets the alpha channel.
	 * 
	 * @param alpha the alpha channel.
	 * @throws DOMSyntaxException if alpha is not a percentage.
	 */
	public void setAlpha(CSSNumericValue alpha) throws DOMSyntaxException {
		setAlphaChannel(alpha);
	}

	private void setAlphaChannel(CSSNumericValue alpha) throws DOMSyntaxException {
		NumericValue a = (NumericValue) alpha;
		if (a.getUnitType() != CSSUnit.CSS_PERCENTAGE && a.getUnitType() != CSSUnit.CSS_NUMBER) {
			throw new DOMSyntaxException("Alpha channel must be a number or percentage.");
		}
		componentize(a);
		this.alpha = a;

		componentChanged(a);
	}

	boolean isOpaque() {
		switch (alpha.getUnitType()) {
		case CSSUnit.CSS_NUMBER:
			return alpha.getFloatValue() == 1f;
		case CSSUnit.CSS_PERCENTAGE:
			return alpha.getFloatValue() == 100f;
		}
		return false;
	}

	@Override
	public ColorValue getColorValue() {
		return this;
	}

	@Override
	public ColorValue clone() {
		ColorValue c = (ColorValue) super.clone();
		c.alpha = alpha.clone();
		return c;
	}

}
