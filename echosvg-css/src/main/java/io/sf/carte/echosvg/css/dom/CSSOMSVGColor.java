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
package io.sf.carte.echosvg.css.dom;

import org.w3c.css.om.typed.CSSColorValue;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.svg.SVGColor;
import org.w3c.dom.svg.SVGICCColor;
import org.w3c.dom.svg.SVGNumber;
import org.w3c.dom.svg.SVGNumberList;

import io.sf.carte.echosvg.css.engine.value.CSSVal;
import io.sf.carte.echosvg.css.engine.value.ColorFunction;
import io.sf.carte.echosvg.css.engine.value.ColorValue;
import io.sf.carte.echosvg.css.engine.value.NumericValue;
import io.sf.carte.echosvg.css.engine.value.RGBColorValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueModificationHandler;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class implements the {@link SVGColor} interface.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
@SuppressWarnings("removal")
public class CSSOMSVGColor implements CSSVal, CSSColorValue, SVGICCColor, SVGNumberList {

	/**
	 * The associated value.
	 */
	protected ValueProvider valueProvider;

	/**
	 * The modifications handler.
	 */
	protected ValueModificationHandler handler;

	/**
	 * Creates a new CSSOMSVGColor.
	 */
	public CSSOMSVGColor(ValueProvider vp) {
		valueProvider = vp;
	}

	/**
	 * Sets the modification handler of this value.
	 */
	public void setModificationHandler(ValueModificationHandler h) {
		handler = h;
	}

	@Override
	public String getCssText() {
		return valueProvider.getValue().getCssText();
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	@Override
	public String toString() {
		return getCssText();
	}

	@Override
	public CssType getCssValueType() {
		return CssType.TYPED;
	}

	@Override
	public Type getPrimitiveType() {
		return valueProvider.getValue().getPrimitiveType();
	}

	@Override
	public float getFloatValue() {
		return valueProvider.getValue().getFloatValue();
	}

	@Override
	public int getLength() {
		return valueProvider.getValue().getLength();
	}

	@Override
	public Value item(int index) {
		valueProvider.getValue().setModificationHandler(handler);
		return valueProvider.getValue().item(index);
	}

	@Override
	public String getIdentifierValue() throws DOMException {
		return valueProvider.getValue().getIdentifierValue();
	}

	@Override
	public String getStringValue() throws DOMException {
		return valueProvider.getValue().getStringValue();
	}

	@Override
	public String getURIValue() throws DOMException {
		return valueProvider.getValue().getURIValue();
	}

	@Override
	public Value clone() {
		return valueProvider.getValue().clone();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGColor#getColorType()}.
	 */
	@SuppressWarnings("deprecation")
	public short getColorType() {
		Value value = valueProvider.getValue();
		switch (value.getCssValueType()) {
		case TYPED:
			switch (value.getPrimitiveType()) {
			case IDENT: {
				if (CSSConstants.CSS_CURRENTCOLOR_VALUE.equalsIgnoreCase(value.getIdentifierValue()))
					return SVGColor.SVG_COLORTYPE_CURRENTCOLOR;
				return SVGColor.SVG_COLORTYPE_RGBCOLOR;
			}
			case COLOR:
				ColorValue color = value.getColorValue();
				switch (color.getCSSColorSpace()) {
				case ColorValue.RGB_FUNCTION:
					return SVGColor.SVG_COLORTYPE_RGBCOLOR;
				default:
					throw new IllegalStateException("Found unexpected color type:" + color.getCSSColorSpace());
				}
			default:
				// there was no case for this primitiveType, prevent throwing the other
				// exception
				throw new IllegalStateException("Found unexpected PrimitiveType:" + value.getPrimitiveType());
			}

		case LIST:
			return SVGColor.SVG_COLORTYPE_RGBCOLOR_ICCCOLOR;
		default:
			// should not happen
			throw new IllegalStateException("Found unexpected CssValueType:" + value.getCssValueType());
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGColor#getRGBColor()}.
	 */
	public RGBColor getRGBColor() {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Legacy value API not supported.");
	}

	/**
	 * Returns the CSSColorValue value for this SVGColor. For the SVG ECMAScript
	 * binding.
	 */
	public ColorValue getCSSColorValue() {
		valueProvider.getValue().setModificationHandler(handler);
		return valueProvider.getValue().getColorValue();
	}

	public String getCSSColorSpace() {
		return getCSSColorValue().getCSSColorSpace();
	}

	public NumericValue getAlpha() {
		return getCSSColorValue().getAlpha();
	}

	/**
	 * Sets the alpha channel as a percentage.
	 * 
	 * @param alpha the alpha channel as a percentage.
	 */
	public void setAlpha(double alpha) {
		getCSSColorValue().setAlpha(alpha);
	}

	public NumericValue getR() {
		return getRGBColorValue().getR();
	}

	RGBColorValue getRGBColorValue() {
		ColorValue color = getCSSColorValue();
		if (ColorValue.RGB_FUNCTION.equals(color.getCSSColorSpace())) {
			return (RGBColorValue) color;
		}
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "Not an RGB color.");
	}

	public NumericValue getG() {
		return getRGBColorValue().getG();
	}

	public NumericValue getB() {
		return getRGBColorValue().getB();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGColor#setRGBColor(String)}.
	 */
	public void setRGBColor(String color) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGColor#getICCColor()}.
	 */
	public SVGICCColor getICCColor() {
		return this;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGColor#setRGBColorICCColor(String,String)}.
	 */
	public void setRGBColorICCColor(String rgb, String icc) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGColor#setColor(short,String,String)}.
	 */
	public void setColor(short type, String rgb, String icc) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	// SVGICCColor //////////////////////////////////////////////////

	@Override
	public String getColorProfile() {
		return getCSSColorValue().getCSSColorSpace();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGICCColor#setColorProfile(String)}.
	 */
	@Override
	public void setColorProfile(String colorProfile) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGICCColor#getColors()}.
	 */
	@Override
	public SVGNumberList getColors() {
		return this;
	}

	// SVGNumberList ///////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#getNumberOfItems()}.
	 */
	@Override
	public int getNumberOfItems() {
		ColorValue value = getCSSColorValue();
		if (!(value instanceof ColorFunction)) {
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
		return ((ColorFunction) value).getChannels().getLength();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#clear()}.
	 */
	@Override
	public void clear() throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#initialize(SVGNumber)}.
	 */
	@Override
	public SVGNumber initialize(SVGNumber newItem) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#getItem(int)}.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public SVGNumber getItem(int index) throws DOMException {
		if (getColorType() != SVGColor.SVG_COLORTYPE_RGBCOLOR_ICCCOLOR) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, "");
		}
		int n = getNumberOfItems();
		if (index < 0 || index >= n) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, "");
		}
		ColorValue value = getCSSColorValue();
		if (!(value instanceof ColorFunction)) {
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
		float f = ((ColorFunction) value).getChannels().item(index).getFloatValue();
		SVGNumber result = new ColorNumber(f);
		return result;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#insertItemBefore(SVGNumber,int)}.
	 */
	@Override
	public SVGNumber insertItemBefore(SVGNumber newItem, int index) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#replaceItem(SVGNumber,int)}.
	 */
	@Override
	public SVGNumber replaceItem(SVGNumber newItem, int index) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#removeItem(int)}.
	 */
	@Override
	public SVGNumber removeItem(int index) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGNumberList#appendItem(SVGNumber)}.
	 */
	@Override
	public SVGNumber appendItem(SVGNumber newItem) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * To represent a SVGNumber which is part of a color list.
	 */
	protected class ColorNumber implements SVGNumber {

		/**
		 * The value of this number, when detached.
		 */
		protected float value;

		/**
		 * Creates a new ColorNumber.
		 */
		public ColorNumber(float f) {
			value = f;
		}

		/**
		 * Implements {@link SVGNumber#getValue()}.
		 */
		@Override
		public float getValue() {
			return value;
		}

		/**
		 * Implements {@link SVGNumber#setValue(float)}.
		 */
		@Override
		public void setValue(float f) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
		}

	}

	/**
	 * To provide the actual value.
	 */
	public interface ValueProvider {

		/**
		 * Returns the current value associated with this object.
		 */
		Value getValue();

	}

}
