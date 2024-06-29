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

import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.doc.style.css.property.StyleValue;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class provides a manager for the property with support for CSS color
 * values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractColorManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_AQUA_VALUE, ValueConstants.AQUA_VALUE);
		values.put(CSSConstants.CSS_BLACK_VALUE, ValueConstants.BLACK_VALUE);
		values.put(CSSConstants.CSS_BLUE_VALUE, ValueConstants.BLUE_VALUE);
		values.put(CSSConstants.CSS_FUCHSIA_VALUE, ValueConstants.FUCHSIA_VALUE);
		values.put(CSSConstants.CSS_GRAY_VALUE, ValueConstants.GRAY_VALUE);
		values.put(CSSConstants.CSS_GREEN_VALUE, ValueConstants.GREEN_VALUE);
		values.put(CSSConstants.CSS_LIME_VALUE, ValueConstants.LIME_VALUE);
		values.put(CSSConstants.CSS_MAROON_VALUE, ValueConstants.MAROON_VALUE);
		values.put(CSSConstants.CSS_NAVY_VALUE, ValueConstants.NAVY_VALUE);
		values.put(CSSConstants.CSS_OLIVE_VALUE, ValueConstants.OLIVE_VALUE);
		values.put(CSSConstants.CSS_PURPLE_VALUE, ValueConstants.PURPLE_VALUE);
		values.put(CSSConstants.CSS_RED_VALUE, ValueConstants.RED_VALUE);
		values.put(CSSConstants.CSS_SILVER_VALUE, ValueConstants.SILVER_VALUE);
		values.put(CSSConstants.CSS_TEAL_VALUE, ValueConstants.TEAL_VALUE);
		values.put(CSSConstants.CSS_TRANSPARENT_VALUE, ValueConstants.TRANSPARENT_VALUE);
		values.put(CSSConstants.CSS_WHITE_VALUE, ValueConstants.WHITE_VALUE);
		values.put(CSSConstants.CSS_YELLOW_VALUE, ValueConstants.YELLOW_VALUE);

		values.put(CSSConstants.CSS_ACTIVEBORDER_VALUE, ValueConstants.ACTIVEBORDER_VALUE);
		values.put(CSSConstants.CSS_ACTIVECAPTION_VALUE, ValueConstants.ACTIVECAPTION_VALUE);
		values.put(CSSConstants.CSS_APPWORKSPACE_VALUE, ValueConstants.APPWORKSPACE_VALUE);
		values.put(CSSConstants.CSS_BACKGROUND_VALUE, ValueConstants.BACKGROUND_VALUE);
		values.put(CSSConstants.CSS_BUTTONFACE_VALUE, ValueConstants.BUTTONFACE_VALUE);
		values.put(CSSConstants.CSS_BUTTONHIGHLIGHT_VALUE, ValueConstants.BUTTONHIGHLIGHT_VALUE);
		values.put(CSSConstants.CSS_BUTTONSHADOW_VALUE, ValueConstants.BUTTONSHADOW_VALUE);
		values.put(CSSConstants.CSS_BUTTONTEXT_VALUE, ValueConstants.BUTTONTEXT_VALUE);
		values.put(CSSConstants.CSS_CAPTIONTEXT_VALUE, ValueConstants.CAPTIONTEXT_VALUE);
		values.put(CSSConstants.CSS_GRAYTEXT_VALUE, ValueConstants.GRAYTEXT_VALUE);
		values.put(CSSConstants.CSS_HIGHLIGHT_VALUE, ValueConstants.HIGHLIGHT_VALUE);
		values.put(CSSConstants.CSS_HIGHLIGHTTEXT_VALUE, ValueConstants.HIGHLIGHTTEXT_VALUE);
		values.put(CSSConstants.CSS_INACTIVEBORDER_VALUE, ValueConstants.INACTIVEBORDER_VALUE);
		values.put(CSSConstants.CSS_INACTIVECAPTION_VALUE, ValueConstants.INACTIVECAPTION_VALUE);
		values.put(CSSConstants.CSS_INACTIVECAPTIONTEXT_VALUE, ValueConstants.INACTIVECAPTIONTEXT_VALUE);
		values.put(CSSConstants.CSS_INFOBACKGROUND_VALUE, ValueConstants.INFOBACKGROUND_VALUE);
		values.put(CSSConstants.CSS_INFOTEXT_VALUE, ValueConstants.INFOTEXT_VALUE);
		values.put(CSSConstants.CSS_MENU_VALUE, ValueConstants.MENU_VALUE);
		values.put(CSSConstants.CSS_MENUTEXT_VALUE, ValueConstants.MENUTEXT_VALUE);
		values.put(CSSConstants.CSS_SCROLLBAR_VALUE, ValueConstants.SCROLLBAR_VALUE);
		values.put(CSSConstants.CSS_THREEDDARKSHADOW_VALUE, ValueConstants.THREEDDARKSHADOW_VALUE);
		values.put(CSSConstants.CSS_THREEDFACE_VALUE, ValueConstants.THREEDFACE_VALUE);
		values.put(CSSConstants.CSS_THREEDHIGHLIGHT_VALUE, ValueConstants.THREEDHIGHLIGHT_VALUE);
		values.put(CSSConstants.CSS_THREEDLIGHTSHADOW_VALUE, ValueConstants.THREEDLIGHTSHADOW_VALUE);
		values.put(CSSConstants.CSS_THREEDSHADOW_VALUE, ValueConstants.THREEDSHADOW_VALUE);
		values.put(CSSConstants.CSS_WINDOW_VALUE, ValueConstants.WINDOW_VALUE);
		values.put(CSSConstants.CSS_WINDOWFRAME_VALUE, ValueConstants.WINDOWFRAME_VALUE);
		values.put(CSSConstants.CSS_WINDOWTEXT_VALUE, ValueConstants.WINDOWTEXT_VALUE);
	}

	/**
	 * The computed identifier values.
	 */
	protected static final StringMap computedValues = new StringMap();
	static {
		computedValues.put(CSSConstants.CSS_BLACK_VALUE, ValueConstants.BLACK_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_SILVER_VALUE, ValueConstants.SILVER_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_GRAY_VALUE, ValueConstants.GRAY_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_WHITE_VALUE, ValueConstants.WHITE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_MAROON_VALUE, ValueConstants.MAROON_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_RED_VALUE, ValueConstants.RED_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_PURPLE_VALUE, ValueConstants.PURPLE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_FUCHSIA_VALUE, ValueConstants.FUCHSIA_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_GREEN_VALUE, ValueConstants.GREEN_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_LIME_VALUE, ValueConstants.LIME_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_OLIVE_VALUE, ValueConstants.OLIVE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_YELLOW_VALUE, ValueConstants.YELLOW_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_NAVY_VALUE, ValueConstants.NAVY_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_BLUE_VALUE, ValueConstants.BLUE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_TEAL_VALUE, ValueConstants.TEAL_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_AQUA_VALUE, ValueConstants.AQUA_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_TRANSPARENT_VALUE, ValueConstants.TRANSPARENT_RGB_VALUE);
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lunit, CSSEngine engine) throws DOMException {
		switch (lunit.getLexicalUnitType()) {
		case HSLCOLOR:
		case HWBCOLOR:
		case LABCOLOR:
		case LCHCOLOR:
		case OKLABCOLOR:
		case OKLCHCOLOR:
		case COLOR_FUNCTION:
		case COLOR_MIX:
			ValueFactory vf = new ValueFactory();
			String rgbSerialization;
			try {
				StyleValue css4jValue = vf.createCSSValue(lunit);
				if (css4jValue.getCssValueType() != CssType.TYPED) {
					throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
				}
				rgbSerialization = ((CSSTypedValue) css4jValue).toRGBColor().toString();
			} catch (DOMException e) {
				throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
			}
			// Now re-parse the result
			CSSParser parser = new CSSParser();
			try {
				lunit = parser.parsePropertyValue(new StringReader(rgbSerialization));
			} catch (CSSParseException | IOException e) {
				throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
			}
		case RGBCOLOR:
			LexicalUnit lu = lunit.getParameters();
			Value red = createColorComponent(lu);
			lu = lu.getNextLexicalUnit();
			if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA) {
				lu = lu.getNextLexicalUnit();
			}
			Value green = createColorComponent(lu);
			lu = lu.getNextLexicalUnit();
			if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA) {
				lu = lu.getNextLexicalUnit();
			}
			Value blue = createColorComponent(lu);
			// Alpha channel
			lu = lu.getNextLexicalUnit();
			Value alpha;
			if (lu != null) {
				if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA
						|| lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_SLASH) {
					lu = lu.getNextLexicalUnit();
					if (lu == null) {
						throw new DOMException(DOMException.SYNTAX_ERR, "Invalid color: " + lunit.getCssText());
					}
				}
				alpha = createColorComponent(lu);
			} else {
				alpha = null;
			}
			return createRGBColor(red, green, blue, alpha);
		default:
			return super.createValue(lunit, engine);
		}
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT) {
			String ident = value.getStringValue();
			// Search for a direct computed value.
			Value v = (Value) computedValues.get(ident);
			if (v != null) {
				return v;
			}
			// Must be a system color...
			if (values.get(ident) == null) {
				throw new IllegalStateException("Not a system-color:" + ident);
			}
			return engine.getCSSContext().getSystemColor(ident);
		}
		return super.computeValue(elt, pseudo, engine, idx, sm, value);
	}

	/**
	 * Creates an RGB(A) color.
	 */
	protected Value createRGBColor(Value r, Value g, Value b, Value a) {
		return a == null ? new RGBColorValue(r, g, b) : new RGBColorValue(r, g, b, a);
	}

	/**
	 * Creates a color component from a lexical unit.
	 */
	protected Value createColorComponent(LexicalUnit lu) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case INTEGER:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getIntegerValue());

		case REAL:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getFloatValue());

		case PERCENTAGE:
			return new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, lu.getFloatValue());

		default:
		}
		throw createInvalidRGBComponentUnitDOMException(lu.getLexicalUnitType());
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	private DOMException createInvalidRGBComponentUnitDOMException(LexicalType lexicalType) {
		Object[] p = { getPropertyName(), lexicalType.toString() };
		String s = Messages.formatMessage("invalid.rgb.component.unit", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

}
