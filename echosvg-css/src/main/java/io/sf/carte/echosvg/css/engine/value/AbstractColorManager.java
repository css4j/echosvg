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

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSColor;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSValue;
import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.doc.style.css.property.PercentageEvaluator;
import io.sf.carte.doc.style.css.property.StyleValue;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.svg.SVGValueConstants;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class provides a manager for the property with support for CSS color
 * values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class AbstractColorManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap<Value> values = new StringMap<>(50);
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
	protected static final StringMap<Value> computedValues = new StringMap<>(18);
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
		// Maximum number of fraction digits when serializing a color component.
		final int COMP_MAX_FRACTION_DIGITS = 7;

		switch (lunit.getLexicalUnitType()) {
		case LABCOLOR:
		case LCHCOLOR:
		case OKLABCOLOR:
		case OKLCHCOLOR:
		case COLOR_MIX: {
			ValueFactory vf = new ValueFactory();
			String colorSerialization;
			try {
				StyleValue css4jValue = vf.createCSSValue(lunit);
				if (css4jValue.getCssValueType() != CssType.TYPED) {
					throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
				}
				CSSColor color = ((io.sf.carte.doc.style.css.CSSColorValue) css4jValue).getColor();
				if (color.isInGamut(io.sf.carte.doc.style.css.ColorSpace.srgb)) {
					color = color.toColorSpace(io.sf.carte.doc.style.css.ColorSpace.srgb);
					setComponentsMaximumFractionDigits(color, COMP_MAX_FRACTION_DIGITS);
					colorSerialization = color.toString();
					// Now parse the result
					lunit = reparseColor(colorSerialization);
					return createRGBColor(lunit);
				} else if (color.isInGamut(io.sf.carte.doc.style.css.ColorSpace.display_p3)) {
					color = color.toColorSpace(io.sf.carte.doc.style.css.ColorSpace.display_p3);
				} else if (color.isInGamut(io.sf.carte.doc.style.css.ColorSpace.a98_rgb)) {
					color = color.toColorSpace(io.sf.carte.doc.style.css.ColorSpace.a98_rgb);
				} else if (color.isInGamut(io.sf.carte.doc.style.css.ColorSpace.rec2020)) {
					color = color.toColorSpace(io.sf.carte.doc.style.css.ColorSpace.rec2020);
				} else {
					// Prophoto is the largest supported RGB space
					color = color.toColorSpace(io.sf.carte.doc.style.css.ColorSpace.prophoto_rgb);
				}
				setComponentsMaximumFractionDigits(color, COMP_MAX_FRACTION_DIGITS);
				colorSerialization = color.toString();
			} catch (DOMException e) {
				throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
			}
			// Now parse the result
			lunit = reparseColor(colorSerialization);
		}
		case COLOR_FUNCTION:
			return createColorFunction(lunit);
		case HSLCOLOR:
		case HWBCOLOR: {
			ValueFactory vf = new ValueFactory();
			String rgbSerialization;
			try {
				StyleValue css4jValue = vf.createCSSValue(lunit);
				if (css4jValue.getCssValueType() != CssType.TYPED) {
					throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
				}
				CSSColor rgb = ((CSSTypedValue) css4jValue).toRGBColor();
				setComponentsMaximumFractionDigits(rgb, COMP_MAX_FRACTION_DIGITS);
				rgbSerialization = rgb.toString();
			} catch (DOMException e) {
				throw createInvalidLexicalUnitDOMException(lunit.getLexicalUnitType());
			}
			// Now parse the result
			lunit = reparseColor(rgbSerialization);
		}
		case RGBCOLOR:
			return createRGBColor(lunit);
		case IDENT:
			// Clone so colors can be modified
			return createIdentValue(lunit.getStringValue(), engine).clone();
		default:
			return super.createValue(lunit, engine);
		}
	}

	private static void setComponentsMaximumFractionDigits(CSSColor color, int maxFractionDigits) {
		((NumberValue) color.item(1)).setMaximumFractionDigits(maxFractionDigits);
		((NumberValue) color.item(2)).setMaximumFractionDigits(maxFractionDigits);
		((NumberValue) color.item(3)).setMaximumFractionDigits(maxFractionDigits);
	}

	private LexicalUnit reparseColor(String colorSerialization) throws DOMException {
		CSSParser parser = new CSSParser();
		parser.setFlag(Parser.Flag.VALUE_COMMENTS_IGNORE);
		try {
			return parser.parsePropertyValue(new StringReader(colorSerialization));
		} catch (CSSParseException | IOException e) {
			throw createMalformedLexicalUnitDOMException();
		}
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.getPrimitiveType() == CSSValue.Type.IDENT) {
			String ident = ((AbstractStringValue) value).getValue();
			// Search for a direct computed value.
			// 'ident' must come from a constant, so no need to call intern()
			Value v = computedValues.get(ident);
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

	private Value createRGBColor(LexicalUnit lunit) {
		LexicalUnit lu = lunit.getParameters();

		boolean pcntSpecified = lu.getLexicalUnitType() == LexicalType.PERCENTAGE;
		boolean alphaPcntSpecified = false;

		NumericValue red, green, blue, alpha;
		try {
			red = createRGBColorComponent(lu);
			lu = lu.getNextLexicalUnit();
			if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA) {
				lu = lu.getNextLexicalUnit();
			}

			pcntSpecified = pcntSpecified || lu.getLexicalUnitType() == LexicalType.PERCENTAGE;
			green = createRGBColorComponent(lu);
			lu = lu.getNextLexicalUnit();
			if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA) {
				lu = lu.getNextLexicalUnit();
			}

			pcntSpecified = pcntSpecified || lu.getLexicalUnitType() == LexicalType.PERCENTAGE;
			blue = createRGBColorComponent(lu);

			// Alpha channel
			lu = lu.getNextLexicalUnit();
			if (lu != null) {
				if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_COMMA
						|| lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_SLASH) {
					lu = lu.getNextLexicalUnit();
					if (lu == null) {
						throw new DOMException(DOMException.SYNTAX_ERR, "Invalid color: " + lunit.getCssText());
					}
				}
				alphaPcntSpecified = lu.getLexicalUnitType() == LexicalType.PERCENTAGE;
				alpha = createColorComponent(lu);
			} else {
				alpha = null;
			}
		} catch (CSSProxyValueException e) {
			return createLexicalValue(lunit);
		}

		return createRGBColor(red, green, blue, pcntSpecified, alpha, alphaPcntSpecified);
	}

	/**
	 * Creates an RGB(A) color.
	 */
	protected ColorValue createRGBColor(NumericValue r, NumericValue g, NumericValue b,
			boolean pcntSpecified, NumericValue a, boolean alphaPcntSpecified) {
		RGBColorValue c = a == null ? new RGBColorValue(r, g, b) : new RGBColorValue(r, g, b, a);
		c.setSpecifiedAsPercentage(pcntSpecified);
		c.setAlphaSpecifiedAsPercentage(alphaPcntSpecified);
		return c;
	}

	private Value createColorFunction(LexicalUnit lunit) {
		LexicalUnit lu = lunit.getParameters();
		AbstractValueList<NumericValue> components = new AbstractValueList<>(' ', 4);

		// Color space
		if (lu.getLexicalUnitType() != LexicalUnit.LexicalType.IDENT) {
			throw new DOMException(DOMException.TYPE_MISMATCH_ERR,
					"Color space must be identifier: " + lunit.toString());
		}

		String colorSpace = lu.getStringValue();
		lu = lu.getNextLexicalUnit();
		if (lu == null) {
			throw new DOMException(DOMException.SYNTAX_ERR, "Wrong value: " + lunit.toString());
		}

		// Components
		NumericValue alpha = null;
		NumericValue primi;
		try {
			while (true) {
				primi = createColorComponent(lu);
				components.add(primi);
				lu = lu.getNextLexicalUnit();

				if (lu == null) {
					break;
				}
				if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.OPERATOR_SLASH) {
					lu = lu.getNextLexicalUnit(); // Alpha
					alpha = createColorComponent(lu);
					lu = lu.getNextLexicalUnit();
					if (lu != null) {
						throw new DOMException(DOMException.SYNTAX_ERR, "Wrong value: " + lunit.toString());
					}
					break;
				}
			}
		} catch (CSSProxyValueException e) {
			return createLexicalValue(lunit);
		}

		ColorFunction color = new ColorFunction(colorSpace, components);

		if (alpha == null) {
			alpha = SVGValueConstants.NUMBER_1;
		}
		color.setAlpha(alpha);

		return color;
	}

	/**
	 * Creates an RGB color component from a lexical unit.
	 */
	protected NumericValue createRGBColorComponent(LexicalUnit lu) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case INTEGER:
			return new FloatValue(CSSUnit.CSS_PERCENTAGE, lu.getIntegerValue() / 2.55f);

		case REAL:
			return new FloatValue(CSSUnit.CSS_PERCENTAGE, lu.getFloatValue() / 2.55f);

		case PERCENTAGE:
			return new FloatValue(CSSUnit.CSS_PERCENTAGE, lu.getFloatValue());

		case VAR:
		case ATTR:
			throw new CSSProxyValueException();

		case CALC:
			Value calc = createCalc(lu);
			if (calc.getCssValueType() == CSSValue.CssType.PROXY) {
				throw new CSSProxyValueException();
			} else if (calc.getPrimitiveType() != Type.EXPRESSION) {
				break;
			}
			return evaluateComponentExpression((CalcValue) calc);

		case MATH_FUNCTION:
			Value v;
			try {
				v = createMathFunction(lu, "<number|percentage>");
			} catch (Exception e) {
				DOMException ife = createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
				ife.initCause(e);
				throw ife;
			}
			if (v.getCssValueType() == CSSValue.CssType.PROXY) {
				throw new CSSProxyValueException();
			} else if (v.getPrimitiveType() != Type.MATH_FUNCTION) {
				break;
			}
			return evaluateComponentFunction((MathFunctionValue) v);

		default:
		}
		throw createInvalidRGBComponentUnitDOMException(lu.getLexicalUnitType());
	}

	/**
	 * Creates a color component from a lexical unit.
	 */
	protected NumericValue createColorComponent(LexicalUnit lu) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case INTEGER:
			return new FloatValue(CSSUnit.CSS_NUMBER, lu.getIntegerValue());

		case REAL:
			return new FloatValue(CSSUnit.CSS_NUMBER, lu.getFloatValue());

		case PERCENTAGE:
			return new FloatValue(CSSUnit.CSS_PERCENTAGE, lu.getFloatValue());

		case VAR:
		case ATTR:
			throw new CSSProxyValueException();

		case CALC:
			Value calc = createCalc(lu);
			if (calc.getCssValueType() == CSSValue.CssType.PROXY) {
				throw new CSSProxyValueException();
			} else if (calc.getPrimitiveType() != Type.EXPRESSION) {
				break;
			}
			return evaluateComponentExpression((CalcValue) calc);

		case MATH_FUNCTION:
			Value v;
			try {
				v = createMathFunction(lu, "<number|percentage>");
			} catch (Exception e) {
				DOMException ife = createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
				ife.initCause(e);
				throw ife;
			}
			if (v.getCssValueType() == CSSValue.CssType.PROXY) {
				throw new CSSProxyValueException();
			} else if (v.getPrimitiveType() != Type.MATH_FUNCTION) {
				break;
			}
			return evaluateComponentFunction((MathFunctionValue) v);

		default:
		}
		throw createInvalidComponentUnitDOMException(lu.getLexicalUnitType());
	}

	private FloatValue evaluateComponentExpression(CalcValue calc) {
		PercentageEvaluator eval = new PercentageEvaluator();
		CSSTypedValue result = calc.evaluate(eval);
		return new FloatValue(CSSUnit.CSS_NUMBER, result.getFloatValue(CSSUnit.CSS_NUMBER));
	}

	private FloatValue evaluateComponentFunction(MathFunctionValue calc) {
		PercentageEvaluator eval = new PercentageEvaluator();
		CSSTypedValue result = calc.evaluate(eval);
		return new FloatValue(CSSUnit.CSS_NUMBER, result.getFloatValue(CSSUnit.CSS_NUMBER));
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap<Value> getIdentifiers() {
		return values;
	}

	private DOMException createInvalidRGBComponentUnitDOMException(LexicalType lexicalType) {
		Object[] p = { getPropertyName(), lexicalType.toString() };
		String s = Messages.formatMessage("invalid.rgb.component.unit", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

	private DOMException createInvalidComponentUnitDOMException(LexicalType lexicalType) {
		Object[] p = { getPropertyName(), lexicalType.toString() };
		String s = Messages.formatMessage("invalid.color.component.unit", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

}
