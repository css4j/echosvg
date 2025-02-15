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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.w3c.api.DOMSyntaxException;
import org.w3c.css.om.typed.CSSNumericValue;
import org.w3c.css.om.typed.CSSRGB;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.css.engine.value.svg.SVGValueConstants;

/**
 * RGB colors.
 *
 * @author See Git history.
 * @version $Id$
 */
public class RGBColorValue extends ColorValue implements CSSRGB {

	private static final long serialVersionUID = 1L;

	/**
	 * The red component.
	 */
	NumericValue red;

	/**
	 * The green component.
	 */
	NumericValue green;

	/**
	 * The blue component.
	 */
	NumericValue blue;

	/**
	 * Whether components were specified as percentages
	 */
	private transient boolean pcntSpecified;

	private transient boolean alphaPcntSpecified;

	/**
	 * Creates a new, opaque RGBColorValue.
	 * 
	 * @param r the r component as a percentage or a number in the [0, 1] range.
	 * @param g the g component as a percentage or a number in the [0, 1] range.
	 * @param b the b component as a percentage or a number in the [0, 1] range.
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public RGBColorValue(NumericValue r, NumericValue g, NumericValue b) throws DOMSyntaxException {
		// Clone the alpha so it can be modified via Typed OM
		this(r, g, b, SVGValueConstants.NUMBER_1.clone());
	}

	/**
	 * Creates a new RGBColorValue.
	 * 
	 * @param r the r component as a percentage or a number in the [0, 1] range.
	 * @param g the g component as a percentage or a number in the [0, 1] range.
	 * @param b the b component as a percentage or a number in the [0, 1] range.
	 * @param a the alpha component as a percentage or a number in the [0, 1] range.
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public RGBColorValue(NumericValue r, NumericValue g, NumericValue b, NumericValue a)
			throws DOMSyntaxException {
		super(a);
		setRGB(r, g, b);
	}

	void setRGB(NumericValue r, NumericValue g, NumericValue b) {
		setR(r);
		setG(g);
		setB(b);
	}

	/**
	 * Creates a new RGBColorValue.
	 * 
	 * @param comp the components as an array of numbers in the [0, 1] range, with alpha at the end.
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public RGBColorValue(float[] comp) {
		super(new FloatValue(CSSUnit.CSS_NUMBER, comp[3]));
		red = new FloatValue(CSSUnit.CSS_NUMBER, comp[0]);
		componentize(red);
		green = new FloatValue(CSSUnit.CSS_NUMBER, comp[1]);
		componentize(green);
		blue = new FloatValue(CSSUnit.CSS_NUMBER, comp[2]);
		componentize(blue);
	}

	/**
	 * Creates a constant color value from legacy RGB components.
	 * <p>
	 * If you want to have a modifiable copy of the returned value, clone it.
	 * </p>
	 * 
	 * @param r the r component as a percentage or a number in the [0, 255] range.
	 * @param g the g component as a percentage or a number in the [0, 255] range.
	 * @param b the b component as a percentage or a number in the [0, 255] range.
	 * @return an immutable color value.
	 * @throws DOMSyntaxException if the values aren't valid color components.
	 * @throws IllegalArgumentException if a component is a mathematical expression.
	 */
	public static RGBColorValue createConstant(NumericValue r, NumericValue g, NumericValue b)
			throws DOMSyntaxException, IllegalArgumentException {
		r = constantLegacyRange(r);
		g = constantLegacyRange(g);
		b = constantLegacyRange(b);
		return new ImmutableRGBColorValue(r, g, b);
	}

	/**
	 * Creates a constant color value from legacy RGB components.
	 * <p>
	 * If you want to have a modifiable copy of the returned value, clone it.
	 * </p>
	 * 
	 * @param r the r component as a percentage or a number in the [0, 255] range.
	 * @param g the g component as a percentage or a number in the [0, 255] range.
	 * @param b the b component as a percentage or a number in the [0, 255] range.
	 * @param a the alpha component as an immutable percentage or a number in the
	 *          [0, 1] range.
	 * @return an immutable color value.
	 * @throws DOMSyntaxException if the values aren't valid color components.
	 * @throws IllegalArgumentException if a component is a mathematical expression.
	 */
	public static RGBColorValue createConstant(NumericValue r, NumericValue g, NumericValue b,
			NumericValue a) throws DOMSyntaxException, IllegalArgumentException {
		r = constantLegacyRange(r);
		g = constantLegacyRange(g);
		b = constantLegacyRange(b);
		return new ImmutableRGBColorValue(r, g, b, a);
	}

	/**
	 * Creates a color value from legacy RGB components.
	 * <p>
	 * If you want to have a modifiable copy of the returned value, clone it.
	 * </p>
	 * 
	 * @param r the r component as a percentage or a number in the [0, 255] range.
	 * @param g the g component as a percentage or a number in the [0, 255] range.
	 * @param b the b component as a percentage or a number in the [0, 255] range.
	 * @return an immutable color value.
	 * @throws DOMSyntaxException       if the values aren't valid color components.
	 * @throws IllegalArgumentException if a component is a mathematical expression.
	 */
	public static RGBColorValue createLegacy(NumericValue r, NumericValue g, NumericValue b)
			throws DOMSyntaxException, IllegalArgumentException {
		r = legacyRange(r);
		g = legacyRange(g);
		b = legacyRange(b);
		RGBColorValue rgb = new RGBColorValue(r, g, b);
		rgb.pcntSpecified = r.getUnitType() == CSSUnit.CSS_PERCENTAGE
				|| g.getUnitType() == CSSUnit.CSS_PERCENTAGE
				|| b.getUnitType() == CSSUnit.CSS_PERCENTAGE;
		return rgb;
	}

	/**
	 * Initialize a constant legacy component of this value.
	 * 
	 * @param c the component (0 &le; c &le; 255).
	 * @return the (0 &le; c &le; 1) constant component.
	 * @throws DOMSyntaxException if the value is inadequate for a component.
	 * @throws IllegalArgumentException if the value is a mathematical expression.
	 */
	private static NumericValue constantLegacyRange(NumericValue ch)
			throws DOMSyntaxException, IllegalArgumentException {
		if (ch.getUnitType() == CSSUnit.CSS_NUMBER) {
			if (ch.getPrimitiveType() == Type.NUMERIC) {
				ch = new ImmutableUnitValue(CSSUnit.CSS_NUMBER, ch.getFloatValue() / 255f);
			} else {
				throw new IllegalArgumentException("Cannot normalize value to [0,1] now: " + ch.getCssText());
			}
		} else if (ch.getUnitType() != CSSUnit.CSS_PERCENTAGE) {
			throw new DOMSyntaxException("RGB component must be a number or percentage, not a "
					+ CSSUnit.dimensionUnitString(ch.getUnitType()) + '.');
		}
		if (ch.handler != null) {
			ch = new ImmutableUnitValue(ch.getUnitType(), ch.getFloatValue());
		}
		return ch;
	}

	/**
	 * Initialize a legacy component of this value.
	 * 
	 * @param c the component (0 &le; c &le; 255).
	 * @return the (0 &le; c &le; 1) component.
	 * @throws DOMSyntaxException if the value is inadequate for a component.
	 * @throws IllegalArgumentException if the value is a mathematical expression.
	 */
	private static NumericValue legacyRange(NumericValue ch)
			throws DOMSyntaxException, IllegalArgumentException {
		if (ch.getUnitType() == CSSUnit.CSS_NUMBER) {
			if (ch.getPrimitiveType() == Type.NUMERIC) {
				ch.setFloatValue(ch.getFloatValue() / 255f);
			} else {
				throw new IllegalArgumentException("Cannot normalize value to [0,1] now: " + ch.getCssText());
			}
		} else if (ch.getUnitType() != CSSUnit.CSS_PERCENTAGE) {
			throw new DOMSyntaxException("RGB component must be a number or percentage, not a "
					+ CSSUnit.dimensionUnitString(ch.getUnitType()) + '.');
		}
		return ch;
	}

	void setSpecifiedAsPercentage(boolean spec) {
		pcntSpecified = spec;
	}

	void setAlphaSpecifiedAsPercentage(boolean spec) {
		alphaPcntSpecified = spec;
	}

	@Override
	public String getCSSColorSpace() {
		return ColorValue.RGB_FUNCTION;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		DecimalFormat df = null;
		if (!pcntSpecified || !alphaPcntSpecified) {
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
			df = new DecimalFormat("#.#", dfs);
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(3);
		}

		String sr = rgbComponentText(red, df);
		String sg = rgbComponentText(green, df);
		String sb = rgbComponentText(blue, df);

		StringBuilder buf = new StringBuilder(sr.length() + sg.length() + sb.length() + 16);
		boolean opaque = isOpaque();
		if (opaque) {
			buf.append("rgb(");
		} else {
			buf.append("rgba(");
		}
		buf.append(sr).append(", ").append(sg).append(", ").append(sb);
		if (!opaque) {
			buf.append(", ").append(alphaComponentText(alpha, df));
		}
		buf.append(')');
		return buf.toString();
	}

	private String rgbComponentText(NumericValue comp, DecimalFormat df) {
		if (pcntSpecified) {
			return comp.getCssText();
		}

		float f;
		if (comp.getUnitType() == CSSUnit.CSS_NUMBER) {
			f = comp.getFloatValue() * 255f;
		} else {
			f = comp.getFloatValue() * 2.55f;
		}
		return df.format(f);
	}

	private String alphaComponentText(NumericValue alpha, DecimalFormat df) {
		if (alphaPcntSpecified || alpha.getUnitType() == CSSUnit.CSS_NUMBER) {
			return alpha.getCssText();
		}

		return df.format(alpha.getFloatValue() * 0.01f);
	}

	@Override
	public RGBColorValue getColorValue() {
		return this;
	}

	@Override
	public NumericValue getR() {
		return red;
	}

	@Override
	public NumericValue getG() {
		return green;
	}

	@Override
	public NumericValue getB() {
		return blue;
	}

	/**
	 * Get the red component.
	 * 
	 * @return the red component.
	 */
	public NumericValue getRed() {
		return red;
	}

	/**
	 * Get the green component.
	 * 
	 * @return the green component.
	 */
	public NumericValue getGreen() {
		return green;
	}

	/**
	 * Get the blue component.
	 * 
	 * @return the blue component.
	 */
	public NumericValue getBlue() {
		return blue;
	}

	@Override
	public void setR(double r) {
		red = new FloatValue(CSSUnit.CSS_PERCENTAGE, (float) r * 100);
		pcntSpecified = true;
		componentize(red);
		componentChanged(red);
	}

	@Override
	public void setR(CSSNumericValue r) throws DOMSyntaxException {
		red = component(r);
		componentChanged(red);
		pcntSpecified = red.getUnitType() == CSSUnit.CSS_PERCENTAGE;
	}

	/**
	 * Initialize a component of this value.
	 * 
	 * @param c the component.
	 * @return the initialized component.
	 * @throws DOMSyntaxException if the value is inadequate for a component.
	 */
	private NumericValue component(CSSNumericValue c) throws DOMSyntaxException {
		NumericValue ch = (NumericValue) c;
		if (ch.getUnitType() != CSSUnit.CSS_PERCENTAGE && ch.getUnitType() != CSSUnit.CSS_NUMBER) {
			throw new DOMSyntaxException("RGB component must be a number or percentage, not a "
					+ CSSUnit.dimensionUnitString(ch.getUnitType()) + '.');
		}
		if (ch.handler != null) {
			ch = ch.clone();
		}
		componentize(ch);
		return ch;
	}

	@Override
	public void setG(double g) {
		green = new FloatValue(CSSUnit.CSS_PERCENTAGE, (float) g * 100);
		pcntSpecified = true;
		componentize(green);
		componentChanged(green);
	}

	@Override
	public void setG(CSSNumericValue g) throws DOMSyntaxException {
		green = component(g);
		componentChanged(green);
	}

	@Override
	public void setB(double b) {
		blue = new FloatValue(CSSUnit.CSS_PERCENTAGE, (float) b * 100);
		pcntSpecified = true;
		componentize(blue);
		componentChanged(blue);
	}

	@Override
	public void setB(CSSNumericValue b) throws DOMSyntaxException {
		blue = component(b);
		componentChanged(blue);
	}

	@Override
	public int getLength() throws DOMException {
		return 4;
	}

	@Override
	public NumericValue item(int index) throws DOMException {
		switch (index) {
		case 0:
			return getR();
		case 1:
			return getG();
		case 2:
			return getB();
		case 3:
			return getAlpha();
		default:
			return null;
		}
	}

	@Override
	public RGBColorValue clone() {
		RGBColorValue clon = (RGBColorValue) super.clone();
		clon.red = red.clone();
		clon.green = green.clone();
		clon.blue = blue.clone();
		clon.pcntSpecified = pcntSpecified;
		clon.alphaPcntSpecified = alphaPcntSpecified;
		return clon;
	}

}
