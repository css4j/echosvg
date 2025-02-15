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
import org.w3c.css.om.typed.CSSLCH;
import org.w3c.css.om.typed.CSSNumericValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.css.engine.value.svg.SVGValueConstants;

/**
 * LCH colors.
 *
 * @version $Id$
 */
public class LCHColorValue extends ColorValue implements CSSLCH {

	private static final long serialVersionUID = 1L;

	/**
	 * The lightness component.
	 */
	protected NumericValue l;

	/**
	 * The chroma component.
	 */
	protected NumericValue c;

	/**
	 * The hue component.
	 */
	protected NumericValue h;

	/**
	 * Creates a new, opaque LCHColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public LCHColorValue(NumericValue l, NumericValue c, NumericValue h) throws DOMSyntaxException {
		this(l, c, h, SVGValueConstants.NUMBER_1);
	}

	/**
	 * Creates a new LCHColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public LCHColorValue(NumericValue l, NumericValue c, NumericValue h, NumericValue alpha)
			throws DOMSyntaxException {
		super(alpha);
		setL(l);
		setC(c);
		setH(h);
	}

	@Override
	public String getCSSColorSpace() {
		return ColorValue.LCH;
	}

	@Override
	public String getCssText() {
		StringBuilder buf = new StringBuilder();
		buf.append("lch(").append(l.getCssText()).append(' ').append(c.getCssText()).append(' ')
			.append(h.getCssText());
		if (!isOpaque()) {
			buf.append(' ').append(getAlpha().getCssText());
		}
		buf.append(')');
		return buf.toString();
	}

	@Override
	public NumericValue getL() {
		return l;
	}

	@Override
	public NumericValue getC() {
		return c;
	}

	@Override
	public NumericValue getH() {
		return h;
	}

	@Override
	public void setL(double lightness) {
		l = new FloatValue(CSSUnit.CSS_PERCENTAGE, (float) lightness);
		componentize(l);
		componentChanged(l);
	}

	@Override
	public void setL(CSSNumericValue lightness) throws DOMSyntaxException {
		l = component(lightness);
		componentChanged(l);
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
			throw new DOMSyntaxException("LC component must be a number or percentage.");
		}
		if (ch.handler != null) {
			ch = ch.clone();
		}
		componentize(ch);
		return ch;
	}

	@Override
	public void setC(double c) {
		this.c = new FloatValue(CSSUnit.CSS_PERCENTAGE, (float) c);
		componentize(this.c);
		componentChanged(this.c);
	}

	@Override
	public void setC(CSSNumericValue c) throws DOMSyntaxException {
		this.c = component(c);
		componentChanged(this.c);
	}

	@Override
	public void setH(double h) {
		this.h = new FloatValue(CSSUnit.CSS_DEG, (float) h);
		componentize(this.h);
		componentChanged(this.h);
	}

	@Override
	public void setH(CSSNumericValue h) throws DOMSyntaxException {
		this.h = hueComponent(h);
		componentChanged(this.h);
	}

	private NumericValue hueComponent(CSSNumericValue h) {
		NumericValue ch = (NumericValue) h;
		if (ch.getUnitType() != CSSUnit.CSS_NUMBER && !CSSUnit.isAngleUnitType(ch.getUnitType())) {
			throw new DOMSyntaxException("Hue component must be a number or angle.");
		}
		if (ch.handler != null) {
			ch = ch.clone();
		}
		componentize(ch);
		return ch;
	}

	@Override
	public int getLength() throws DOMException {
		return 4;
	}

	@Override
	public Value item(int index) throws DOMException {
		switch (index) {
		case 0:
			return getL();
		case 1:
			return getC();
		case 2:
			return getH();
		case 3:
			return getAlpha();
		default:
			return null;
		}
	}

	@Override
	public LCHColorValue clone() {
		LCHColorValue clon = (LCHColorValue) super.clone();
		clon.l = l.clone();
		clon.c = c.clone();
		clon.h = h.clone();
		return clon;
	}

}
