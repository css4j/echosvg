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
package io.sf.carte.echosvg.anim.dom;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGLength;

import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.parser.LengthParser;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.UnitProcessor;

/**
 * Default implementation for SVGLength.
 *
 * This implementation provides the basic functionalities of SVGLength. To have
 * a complete implementation, an element is required to resolve the units.
 *
 * According to the usage of this AbstractSVGLength, the <code>reset()</code>
 * method is after changes being made to the unitType or the value of this
 * length. Before any values are return to the user of the AbstractSVGLength,
 * the <code>revalidate()</code> method is being called to insure the validity
 * of the value and unit type held by this object.
 *
 * @author nicolas.socheleau@bitflash.com
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractSVGLength implements SVGLength {

	/**
	 * This constant represents horizontal lengths.
	 */
	public static final short HORIZONTAL_LENGTH = UnitProcessor.HORIZONTAL_LENGTH;

	/**
	 * This constant represents vertical lengths.
	 */
	public static final short VERTICAL_LENGTH = UnitProcessor.VERTICAL_LENGTH;

	/**
	 * This constant represents other lengths.
	 */
	public static final short OTHER_LENGTH = UnitProcessor.OTHER_LENGTH;

	/**
	 * The unit of this length, as one of the constants in {@link CSSUnit}.
	 */
	protected short unitType;

	/**
	 * The value of this length.
	 */
	protected float value;

	/**
	 * This length's direction.
	 */
	protected short direction;

	/**
	 * The context used to resolve the units.
	 */
	protected UnitProcessor.Context context;

	/**
	 * Return the SVGElement associated to this length.
	 */
	protected abstract SVGOMElement getAssociatedElement();

	/**
	 * Creates a new AbstractSVGLength.
	 */
	public AbstractSVGLength(short direction) {
		context = new DefaultContext();
		this.direction = direction;
		this.value = 0.0f;
		this.unitType = CSSUnit.CSS_NUMBER;
	}

	@Override
	public short getCSSUnitType() {
		revalidate();
		return unitType;
	}

	@Override
	public short getUnitType() {
		return getSVGUnitType(getCSSUnitType());
	}

	private static short getSVGUnitType(short cssUnit) {
		short svgUnit;
		switch (cssUnit) {
		case CSSUnit.CSS_NUMBER:
			svgUnit = SVGLength.SVG_LENGTHTYPE_NUMBER;
			break;
		case CSSUnit.CSS_PX:
			svgUnit = SVGLength.SVG_LENGTHTYPE_PX;
			break;
		case CSSUnit.CSS_EM:
			svgUnit = SVGLength.SVG_LENGTHTYPE_EMS;
			break;
		case CSSUnit.CSS_EX:
			svgUnit = SVGLength.SVG_LENGTHTYPE_EXS;
			break;
		case CSSUnit.CSS_IN:
			svgUnit = SVGLength.SVG_LENGTHTYPE_IN;
			break;
		case CSSUnit.CSS_CM:
			svgUnit = SVGLength.SVG_LENGTHTYPE_CM;
			break;
		case CSSUnit.CSS_MM:
			svgUnit = SVGLength.SVG_LENGTHTYPE_MM;
			break;
		case CSSUnit.CSS_PC:
			svgUnit = SVGLength.SVG_LENGTHTYPE_PC;
			break;
		case CSSUnit.CSS_PT:
			svgUnit = SVGLength.SVG_LENGTHTYPE_PT;
			break;
		case CSSUnit.CSS_PERCENTAGE:
			svgUnit = SVGLength.SVG_LENGTHTYPE_PERCENTAGE;
			break;
		default:
			svgUnit = SVGLength.SVG_LENGTHTYPE_UNKNOWN;
			break;
		}
		return svgUnit;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLength#getValue()}.
	 */
	@Override
	public float getValue() {
		revalidate();
		try {
			return UnitProcessor.cssToUserSpace(value, unitType, direction, context);
		} catch (IllegalArgumentException ex) {
			// XXX Should we throw an exception here when the length
			// type is unknown?
			return 0f;
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLength#setValue(float)}.
	 */
	@Override
	public void setValue(float value) throws DOMException {
		this.value = UnitProcessor.userSpaceToCSS(value, unitType, direction, context);
		reset();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLength#getValueInSpecifiedUnits()}.
	 */
	@Override
	public float getValueInSpecifiedUnits() {
		revalidate();
		return value;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLength#setValueInSpecifiedUnits(float)}.
	 */
	@Override
	public void setValueInSpecifiedUnits(float value) throws DOMException {
		revalidate();
		this.value = value;
		reset();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLength#getValueAsString()}.
	 */
	@Override
	public String getValueAsString() {
		revalidate();
		if (unitType == CSSUnit.CSS_INVALID) {
			return "";
		}
		return Float.toString(value) + CSSUnit.dimensionUnitString(unitType);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLength#setValueAsString(String)}.
	 */
	@Override
	public void setValueAsString(String value) throws DOMException {
		parse(value);
		reset();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLength#newValueSpecifiedUnits(short,float)}.
	 */
	@Override
	public void newValueSpecifiedCSSUnits(short unit, float value) {
		unitType = unit;
		this.value = value;
		reset();
	}

	@Override
	public void newValueSpecifiedUnits(short unitType, float valueInSpecifiedUnits) throws DOMException {
		newValueSpecifiedCSSUnits(getCSSUnitType(unitType), valueInSpecifiedUnits);
	}

	private static short getCSSUnitType(short svgUnit) throws DOMException {
		short cssUnit;
		switch (svgUnit) {
		case SVGLength.SVG_LENGTHTYPE_NUMBER:
			cssUnit = CSSUnit.CSS_NUMBER;
			break;
		case SVGLength.SVG_LENGTHTYPE_PX:
			cssUnit = CSSUnit.CSS_PX;
			break;
		case SVGLength.SVG_LENGTHTYPE_EMS:
			cssUnit = CSSUnit.CSS_EM;
			break;
		case SVGLength.SVG_LENGTHTYPE_EXS:
			cssUnit = CSSUnit.CSS_EX;
			break;
		case SVGLength.SVG_LENGTHTYPE_IN:
			cssUnit = CSSUnit.CSS_IN;
			break;
		case SVGLength.SVG_LENGTHTYPE_CM:
			cssUnit = CSSUnit.CSS_CM;
			break;
		case SVGLength.SVG_LENGTHTYPE_MM:
			cssUnit = CSSUnit.CSS_MM;
			break;
		case SVGLength.SVG_LENGTHTYPE_PC:
			cssUnit = CSSUnit.CSS_PC;
			break;
		case SVGLength.SVG_LENGTHTYPE_PT:
			cssUnit = CSSUnit.CSS_PT;
			break;
		case SVGLength.SVG_LENGTHTYPE_PERCENTAGE:
			cssUnit = CSSUnit.CSS_PERCENTAGE;
			break;
		default:
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Unit not supported: " + svgUnit);
		}
		return cssUnit;
	}

	@Override
	public void convertToSpecifiedUnits(short unit) throws DOMException {
		float v = getValue();
		v = NumberValue.floatValueConversion(v, unitType, getCSSUnitType(unit));
		unitType = unit;
		setValue(v);
	}

	/**
	 * Callback method after changes made to this length.
	 *
	 * The default implementation does nothing.
	 */
	protected void reset() {
	}

	/**
	 * Callback method before any value is return from this length.
	 *
	 * The default implementation does nothing.
	 */
	protected void revalidate() {
	}

	/**
	 * Parse a String value as a SVGLength.
	 *
	 * Initialize this length with the result of the parsing of this value.
	 * 
	 * @param s String representation of a SVGlength.
	 */
	protected void parse(String s) {
		try {
			LengthParser lengthParser = new LengthParser();
			UnitProcessor.UnitResolver ur = new UnitProcessor.UnitResolver();
			lengthParser.setLengthHandler(ur);
			lengthParser.parse(s);
			unitType = ur.unit;
			value = ur.value;
		} catch (ParseException e) {
			unitType = CSSUnit.CSS_INVALID;
			value = 0;
		}
	}

	/**
	 * To resolve the units.
	 */
	protected class DefaultContext implements UnitProcessor.Context {

		/**
		 * Returns the element.
		 */
		@Override
		public Element getElement() {
			return getAssociatedElement();
		}

		@Override
		public float getResolution() {
			return getAssociatedElement().getSVGContext().getResolution();
		}

		/**
		 * Returns the font-size value.
		 */
		@Override
		public float getFontSize() {
			return getAssociatedElement().getSVGContext().getFontSize();
		}

		/**
		 * Returns the x-height value.
		 */
		@Override
		public float getXHeight() {
			return 0.5f;
		}

		@Override
		public float getLineHeight() {
			return getAssociatedElement().getSVGContext().getLineHeight();
		}

		/**
		 * Returns the :root font-size value.
		 */
		@Override
		public float getRootFontSize() {
			return getAssociatedElement().getSVGContext().getRootFontSize();
		}

		@Override
		public float getRootLineHeight() {
			return getAssociatedElement().getSVGContext().getRootLineHeight();
		}

		/**
		 * Returns the viewport width used to compute units.
		 */
		@Override
		public float getViewportWidth() {
			return getAssociatedElement().getSVGContext().getViewportWidth();
		}

		/**
		 * Returns the viewport height used to compute units.
		 */
		@Override
		public float getViewportHeight() {
			return getAssociatedElement().getSVGContext().getViewportHeight();
		}

	}

}
