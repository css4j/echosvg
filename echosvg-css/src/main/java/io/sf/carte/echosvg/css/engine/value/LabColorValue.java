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
import org.w3c.css.om.typed.CSSLab;
import org.w3c.css.om.typed.CSSNumericValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.css.engine.value.svg.SVGValueConstants;

/**
 * Lab colors.
 *
 * @version $Id$
 */
public class LabColorValue extends ColorValue implements CSSLab {

	/**
	 * The lightness component.
	 */
	protected NumericValue l;

	/**
	 * The a component.
	 */
	protected NumericValue a;

	/**
	 * The b component.
	 */
	protected NumericValue b;

	/**
	 * Creates a new, opaque LabColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public LabColorValue(NumericValue l, NumericValue a, NumericValue b) throws DOMSyntaxException {
		this(l, a, b, SVGValueConstants.NUMBER_1);
	}

	/**
	 * Creates a new LabColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public LabColorValue(NumericValue l, NumericValue a, NumericValue b, NumericValue alpha)
			throws DOMSyntaxException {
		super(alpha);
		setL(l);
		setA(a);
		setB(b);
	}

	@Override
	public String getCSSColorSpace() {
		return ColorValue.LAB;
	}

	@Override
	public String getCssText() {
		StringBuilder buf = new StringBuilder();
		buf.append("lab(").append(l.getCssText()).append(' ').append(a.getCssText()).append(' ')
			.append(b.getCssText());
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
	public NumericValue getA() {
		return a;
	}

	@Override
	public NumericValue getB() {
		return b;
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
		if (ch.getCSSUnit() != CSSUnit.CSS_PERCENTAGE && ch.getCSSUnit() != CSSUnit.CSS_NUMBER) {
			throw new DOMSyntaxException("Lab component must be a number or percentage.");
		}
		if (ch.handler != null) {
			ch = ch.clone();
		}
		componentize(ch);
		return ch;
	}

	@Override
	public void setA(double a) {
		this.a = new FloatValue(CSSUnit.CSS_NUMBER, (float) a);
		componentize(this.a);
		componentChanged(this.a);
	}

	@Override
	public void setA(CSSNumericValue a) throws DOMSyntaxException {
		this.a = component(a);
		componentChanged(this.a);
	}

	@Override
	public void setB(double b) {
		this.b = new FloatValue(CSSUnit.CSS_NUMBER, (float) b);
		componentize(this.b);
		componentChanged(this.b);
	}

	@Override
	public void setB(CSSNumericValue b) throws DOMSyntaxException {
		this.b = component(b);
		componentChanged(this.b);
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
			return getA();
		case 2:
			return getB();
		case 3:
			return getAlpha();
		default:
			return null;
		}
	}

	@Override
	public LabColorValue clone() {
		LabColorValue clon;
		try {
			clon = new LabColorValue(l, a, b, alpha);
		} catch (DOMSyntaxException e) {
			clon = null;
		}
		return clon;
	}

}
