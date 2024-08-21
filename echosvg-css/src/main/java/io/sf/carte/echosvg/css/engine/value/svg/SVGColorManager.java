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

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the SVGColor property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGColorManager extends ColorManager {

	/**
	 * The name of the handled property.
	 */
	protected String property;

	/**
	 * The default value.
	 */
	protected Value defaultValue;

	/**
	 * Creates a new SVGColorManager. The default value is black.
	 */
	public SVGColorManager(String prop) {
		this(prop, ValueConstants.BLACK_RGB_VALUE);
	}

	/**
	 * Creates a new SVGColorManager.
	 */
	public SVGColorManager(String prop, Value v) {
		property = prop;
		defaultValue = v;
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return false;
	}

	/**
	 * Implements {@link ValueManager#isAnimatableProperty()}.
	 */
	@Override
	public boolean isAnimatableProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#isAdditiveProperty()}.
	 */
	@Override
	public boolean isAdditiveProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#getPropertyType()}.
	 */
	@Override
	public int getPropertyType() {
		return SVGTypes.TYPE_COLOR;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return property;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return defaultValue.clone();
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.IDENT) {
			if (lu.getStringValue().equalsIgnoreCase(CSSConstants.CSS_CURRENTCOLOR_VALUE)) {
				return SVGValueConstants.CURRENTCOLOR_VALUE;
			}
		}
		Value v = super.createValue(lu, engine);
		lu = lu.getNextLexicalUnit();
		if (lu == null) {
			return v;
		}

		// If we have more content here, there is a color function after the sRGB color.
		if (lu.getLexicalUnitType() != LexicalUnit.LexicalType.FUNCTION) {
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}

		ListValue result = new ListValue(' ');
		result.append(v);

		Value colorValue = parseColorFunction(lu, v);
		if (colorValue != null) {
			result.append(colorValue);
		} else {
			return v; // use sRGB fallback if an unsupported color function is encountered
		}
		return result;
	}

	private Value parseColorFunction(LexicalUnit lu, Value v) {
		String functionName = lu.getFunctionName();
		if (functionName.equalsIgnoreCase(ICCColor.ICC_COLOR_FUNCTION)) {
			return createICCColorValue(lu, v);
		}
		return parseColor12Function(lu, v);
	}

	private Value parseColor12Function(LexicalUnit lu, Value v) {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Deprecated color.");
		/*
		 * @formatter: off
		String functionName = lu.getFunctionName();
		if (functionName.equalsIgnoreCase(ICCNamedColor.ICC_NAMED_COLOR_FUNCTION)) {
			return createICCNamedColorValue(lu, v);
		} else if (functionName.equalsIgnoreCase(CIELabColor.CIE_LAB_COLOR_FUNCTION)) {
			return createCIELabColorValue(lu, v);
		} else if (functionName.equalsIgnoreCase(CIELCHColor.CIE_LCH_COLOR_FUNCTION)) {
			return createCIELCHColorValue(lu, v);
		} else if (functionName.equalsIgnoreCase(DeviceColor.DEVICE_CMYK_COLOR_FUNCTION)) {
			return createDeviceColorValue(lu, v, 4);
		} else if (functionName.equalsIgnoreCase(DeviceColor.DEVICE_RGB_COLOR_FUNCTION)) {
			return createDeviceColorValue(lu, v, 3);
		} else if (functionName.equalsIgnoreCase(DeviceColor.DEVICE_GRAY_COLOR_FUNCTION)) {
			return createDeviceColorValue(lu, v, 1);
		} else if (functionName.equalsIgnoreCase(DeviceColor.DEVICE_NCHANNEL_COLOR_FUNCTION)) {
			return createDeviceColorValue(lu, v, 0);
		}
		return null;
		* @formatter: on
		*/
	}

	private Value createICCColorValue(LexicalUnit lu, Value v) {
		lu = lu.getParameters();
		expectIdent(lu);

		ICCColor icc = new ICCColor(lu.getStringValue());

		lu = lu.getNextLexicalUnit();
		while (lu != null) {
			expectComma(lu);
			lu = lu.getNextLexicalUnit();
			icc.append(getColorValue(lu));
			lu = lu.getNextLexicalUnit();
		}
		return icc;
	}

	private void expectIdent(LexicalUnit lu) {
		if (lu.getLexicalUnitType() != LexicalUnit.LexicalType.IDENT) {
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}
	}

	private void expectComma(LexicalUnit lu) {
		if (lu.getLexicalUnitType() != LexicalUnit.LexicalType.OPERATOR_COMMA) {
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}
	}

	private void expectNonNull(LexicalUnit lu) {
		if (lu == null) {
			throw createInvalidLexicalUnitDOMException(LexicalUnit.LexicalType.UNKNOWN);
		}
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.isIdentifier(CSSConstants.CSS_CURRENTCOLOR_VALUE)) {
			sm.putColorRelative(idx, true);

			int ci = engine.getColorIndex();
			return engine.getComputedStyle(elt, pseudo, ci);
		}
		if (value.getCssValueType() == Value.CssType.LIST) {
			Value v = value.item(0);
			Value t = super.computeValue(elt, pseudo, engine, idx, sm, v);
			if (t != v) {
				ListValue result = new ListValue(' ');
				result.append(t);
				result.append(value.item(1));
				return result;
			}
			return value;
		}
		return super.computeValue(elt, pseudo, engine, idx, sm, value);
	}

	/**
	 * Creates a float value usable as a component of an RGBColor.
	 */
	protected float getColorValue(LexicalUnit lu) {
		expectNonNull(lu);
		switch (lu.getLexicalUnitType()) {
		case INTEGER:
			return lu.getIntegerValue();
		case REAL:
			return lu.getFloatValue();
		default:
			break;
		}
		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

}
