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
import org.w3c.css.om.typed.CSSNumericValue;
import org.w3c.dom.DOMException;

/**
 * Immutable RGB colors.
 *
 * @author See Git history.
 * @version $Id$
 */
class ImmutableRGBColorValue extends RGBColorValue {

	/**
	 * Creates a new, opaque RGBColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public ImmutableRGBColorValue(NumericValue r, NumericValue g, NumericValue b)
			throws DOMSyntaxException {
		super(r, g, b);
	}

	/**
	 * Creates a new RGBColorValue.
	 * 
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public ImmutableRGBColorValue(NumericValue r, NumericValue g, NumericValue b, NumericValue a)
			throws DOMSyntaxException {
		super(r, g, b, a);
	}

	@Override
	void setRGB(NumericValue r, NumericValue g, NumericValue b) {
		red = r;
		green = g;
		blue = b;
	}

	@Override
	public void setR(double r) {
		immutable();
	}

	private void immutable() {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Immutable value.");
	}

	@Override
	public void setR(CSSNumericValue r) throws DOMSyntaxException {
		immutable();
	}

	@Override
	public void setG(double g) {
		immutable();
	}

	@Override
	public void setG(CSSNumericValue g) throws DOMSyntaxException {
		immutable();
	}

	@Override
	public void setB(double b) {
		immutable();
	}

	@Override
	public void setB(CSSNumericValue b) throws DOMSyntaxException {
		immutable();
	}

	@Override
	public RGBColorValue clone() {
		RGBColorValue clon;
		try {
			clon = new RGBColorValue(getR().clone(), getG().clone(), getB().clone(), getAlpha().clone());
		} catch (DOMSyntaxException e) {
			throw new IllegalStateException(e);
		}
		return clon;
	}

}
