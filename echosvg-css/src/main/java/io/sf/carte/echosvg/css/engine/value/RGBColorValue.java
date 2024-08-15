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

	/**
	 * The red component.
	 */
	private NumericValue red;

	/**
	 * The green component.
	 */
	private NumericValue green;

	/**
	 * The blue component.
	 */
	private NumericValue blue;

	/**
	 * Whether components were specified as percentages
	 */
	private transient boolean pcntSpecified;

	private transient boolean alphaPcntSpecified;

	/**
	 * Creates a new, opaque RGBColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public RGBColorValue(NumericValue r, NumericValue g, NumericValue b) throws DOMSyntaxException {
		this(r, g, b, SVGValueConstants.NUMBER_1);
	}

	/**
	 * Creates a new RGBColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public RGBColorValue(NumericValue r, NumericValue g, NumericValue b, NumericValue a)
			throws DOMSyntaxException {
		super(a);
		setR(r);
		setG(g);
		setB(b);
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
			df.setMaximumFractionDigits(4);
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
		if (pcntSpecified || comp.getCSSUnit() == CSSUnit.CSS_NUMBER) {
			return comp.getCssText();
		}

		return df.format(comp.getFloatValue() * 2.55f);
	}

	private String alphaComponentText(NumericValue alpha, DecimalFormat df) {
		if (alphaPcntSpecified || alpha.getCSSUnit() == CSSUnit.CSS_NUMBER) {
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
	public Value getRed() {
		return red;
	}

	/**
	 * Get the green component.
	 * 
	 * @return the green component.
	 */
	public Value getGreen() {
		return green;
	}

	/**
	 * Get the blue component.
	 * 
	 * @return the blue component.
	 */
	public Value getBlue() {
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
		pcntSpecified = red.getCSSUnit() == CSSUnit.CSS_PERCENTAGE;
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
		if (ch.getCSSUnit() != CSSUnit.CSS_PERCENTAGE && ch.getCSSUnit() != CSSUnit.CSS_NUMBER) {
			throw new DOMSyntaxException("RGB component must be a number or percentage, not a "
					+ CSSUnit.dimensionUnitString(ch.getCSSUnit()) + '.');
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
	public Value item(int index) throws DOMException {
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
		RGBColorValue clon;
		try {
			clon = new RGBColorValue(red, green, blue, alpha);
		} catch (DOMSyntaxException e) {
			clon = null;
		}
		return clon;
	}

}
