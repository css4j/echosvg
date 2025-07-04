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
package io.sf.carte.echosvg.css.engine.value.css;

import java.util.Locale;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.css.Viewport;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.LengthManager;
import io.sf.carte.echosvg.css.engine.value.NumericDelegateValue;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'font-size' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class FontSizeManager extends LengthManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap<Value> values = new StringMap<>();
	static {
		values.put(CSSConstants.CSS_ALL_VALUE, ValueConstants.ALL_VALUE);
		values.put(CSSConstants.CSS_LARGE_VALUE, ValueConstants.LARGE_VALUE);
		values.put(CSSConstants.CSS_LARGER_VALUE, ValueConstants.LARGER_VALUE);
		values.put(CSSConstants.CSS_MEDIUM_VALUE, ValueConstants.MEDIUM_VALUE);
		values.put(CSSConstants.CSS_SMALL_VALUE, ValueConstants.SMALL_VALUE);
		values.put(CSSConstants.CSS_SMALLER_VALUE, ValueConstants.SMALLER_VALUE);
		values.put(CSSConstants.CSS_X_LARGE_VALUE, ValueConstants.X_LARGE_VALUE);
		values.put(CSSConstants.CSS_X_SMALL_VALUE, ValueConstants.X_SMALL_VALUE);
		values.put(CSSConstants.CSS_XX_LARGE_VALUE, ValueConstants.XX_LARGE_VALUE);
		values.put(CSSConstants.CSS_XX_SMALL_VALUE, ValueConstants.XX_SMALL_VALUE);
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	public StringMap<Value> getIdentifiers() {
		return values;
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return true;
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
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_SIZE_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getPropertyType()}.
	 */
	@Override
	public int getPropertyType() {
		return SVGTypes.TYPE_FONT_SIZE_VALUE;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.MEDIUM_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case IDENT:
			return createIdentValue(lu.getStringValue(), engine);

		case INHERIT:
			return ValueConstants.INHERIT_VALUE;

		default:
			break;
		}

		return super.createValue(lu, engine);
	}

	protected Value createIdentValue(String value, CSSEngine engine) throws DOMException {
		String s = value.toLowerCase(Locale.ROOT).intern();
		Value v = values.get(s);
		if (v == null) {
			throw createInvalidIdentifierDOMException(value);
		}
		return v;
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			final Value value) {
		float scale = 1.0f;
		boolean doParentRelative = false;

		Type pType = value.getPrimitiveType();
		if (pType == Type.NUMERIC) {
			switch (value.getUnitType()) {
			case CSSUnit.CSS_NUMBER:
			case CSSUnit.CSS_PX:
				return value;

			case CSSUnit.CSS_MM:
				float v = value.getFloatValue();
				return new FloatValue(CSSUnit.CSS_PX, v * 3.779527559055f);

			case CSSUnit.CSS_CM:
				v = value.getFloatValue();
				return new FloatValue(CSSUnit.CSS_PX, v * 37.79527559055f);

			case CSSUnit.CSS_IN:
				v = value.getFloatValue();
				return new FloatValue(CSSUnit.CSS_PX, v * 96f);

			case CSSUnit.CSS_PT:
				v = value.getFloatValue();
				return new FloatValue(CSSUnit.CSS_PX, v / 0.75f);

			case CSSUnit.CSS_PC:
				v = value.getFloatValue();
				return new FloatValue(CSSUnit.CSS_PX, v * 16f);

			case CSSUnit.CSS_EM:
				doParentRelative = true;
				scale = value.getFloatValue();
				break;
			case CSSUnit.CSS_EX:
				doParentRelative = true;
				scale = value.getFloatValue() * 0.5f; // !!! x-height
				break;
			case CSSUnit.CSS_PERCENTAGE:
				doParentRelative = true;
				scale = value.getFloatValue() / 100f;
				break;
			case CSSUnit.CSS_LH:
				sm.putLineHeightRelative(idx, true);
				scale = value.getFloatValue();
				int lhidx = engine.getLineHeightIndex();
				CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
				float lh;
				if (p == null) {
					lh = DEFAULT_LINE_HEIGHT * engine.getCSSContext().getMediumFontSize();
				} else {
					Value cs = engine.getComputedStyle(p, null, lhidx);
					lh = lineHeightValue(p, null, engine, cs);
				}
				return new FloatValue(CSSUnit.CSS_PX, lh * scale);
			case CSSUnit.CSS_REM:
				sm.putRootFontSizeRelative(idx, true);
				scale = value.getFloatValue();
				return rootRelative(elt, engine, idx, scale);
			case CSSUnit.CSS_REX:
				sm.putRootFontSizeRelative(idx, true);
				scale = value.getFloatValue() * 0.5f;
				return rootRelative(elt, engine, idx, scale);
			case CSSUnit.CSS_RLH:
				sm.putLineHeightRelative(idx, true);
				scale = value.getFloatValue();
				lhidx = engine.getLineHeightIndex();
				CSSStylableElement root = (CSSStylableElement) elt.getOwnerDocument().getDocumentElement();
				if (elt == root) {
					lh = DEFAULT_LINE_HEIGHT * engine.getCSSContext().getMediumFontSize();
				} else {
					Value cs = engine.getComputedStyle(root, null, lhidx);
					lh = lineHeightValue(root, null, engine, cs);
				}
				return new FloatValue(CSSUnit.CSS_PX, lh * scale);
			case CSSUnit.CSS_VW:
				sm.putViewportRelative(idx, true);
				v = value.getFloatValue();
				return new FloatValue(CSSUnit.CSS_PX,
						v * engine.getCSSContext().getViewport(elt).getWidth() / 100f);
			case CSSUnit.CSS_VH:
				sm.putViewportRelative(idx, true);
				v = value.getFloatValue();
				return new FloatValue(CSSUnit.CSS_PX,
						v * engine.getCSSContext().getViewport(elt).getHeight() / 100f);
			case CSSUnit.CSS_VMIN:
				sm.putViewportRelative(idx, true);
				v = value.getFloatValue();
				Viewport vp = engine.getCSSContext().getViewport(elt);
				float w = vp.getWidth();
				float h = vp.getHeight();
				float min = Math.min(w, h);
				return new FloatValue(CSSUnit.CSS_PX, v * min / 100f);
			case CSSUnit.CSS_VMAX:
				sm.putViewportRelative(idx, true);
				v = value.getFloatValue();
				vp = engine.getCSSContext().getViewport(elt);
				w = vp.getWidth();
				h = vp.getHeight();
				float max = Math.max(w, h);
				return new FloatValue(CSSUnit.CSS_PX, v * max / 100f);
			default:
				// Maybe it is one of the new absolute length units
				return new FloatValue(CSSUnit.CSS_PX,
						NumberValue.floatValueConversion(value.getFloatValue(), value.getUnitType(),
								CSSUnit.CSS_PX));
			}
		} else if (pType == Type.EXPRESSION || pType == Type.MATH_FUNCTION) {
			try {
				Value calc = evaluateMath((NumericDelegateValue<?>) value, elt, pseudo, engine, idx, sm,
						CSSUnit.CSS_PX);
				return new FloatValue(CSSUnit.CSS_PX, calc.getFloatValue());
			} catch (Exception e) {
				return isInheritedProperty() ? null : getDefaultValue();
			}
		}

		if (value.isIdentifier(CSSConstants.CSS_LARGER_VALUE)) {
			doParentRelative = true;
			scale = 1.2f;
		} else if (value.isIdentifier(CSSConstants.CSS_SMALLER_VALUE)) {
			doParentRelative = true;
			scale = 1f / 1.2f;
		}

		if (doParentRelative) {
			sm.putParentRelative(idx, true);

			CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
			float fs;
			if (p == null) {
				CSSContext ctx = engine.getCSSContext();
				fs = ctx.getMediumFontSize();
			} else {
				Value cs = engine.getComputedStyle(p, null, idx);
				fs = cs.getFloatValue();
			}
			return new FloatValue(CSSUnit.CSS_PX, fs * scale);
		}

		// absolute identifiers
		CSSContext ctx = engine.getCSSContext();
		float fs = ctx.getMediumFontSize();

		if (pType == Type.IDENT) {
			String s = value.getIdentifierValue();
			switch (s.charAt(0)) {
			case 'm':
				break;

			case 's':
				fs = fs / 1.2f;
				break;

			case 'l':
				fs = fs * 1.2f;
				break;

			default: // 'x'
				switch (s.charAt(1)) {
				case 'x':
					switch (s.charAt(3)) {
					case 's':
						fs = fs / (float) (1.2 * 1.2 * 1.2);
						break;

					default: // 'l'
						fs = fs * (float) (1.2 * 1.2 * 1.2);
					}
					break;

				default: // '-'
					switch (s.charAt(2)) {
					case 's':
						fs = fs / (float) (1.2 * 1.2);
						break;

					default: // 'l'
						fs = fs * (float) (1.2 * 1.2);
					}
				}
			}
		}

		return new FloatValue(CSSUnit.CSS_PX, fs);
	}

	private Value rootRelative(CSSStylableElement elt, CSSEngine engine, int idx, float scale) {
		CSSStylableElement root = (CSSStylableElement) elt.getOwnerDocument().getDocumentElement();
		if (elt == root) {
			CSSContext ctx = engine.getCSSContext();
			float f = ctx.getMediumFontSize() * scale;
			return new FloatValue(CSSUnit.CSS_PX, f);
		}

		Value cs = engine.getComputedStyle(root, null, idx);
		float f = cs.getFloatValue() * scale;
		return new FloatValue(CSSUnit.CSS_PX, f);
	}

	/**
	 * Indicates the orientation of the property associated with this manager.
	 */
	@Override
	protected int getOrientation() {
		return VERTICAL_ORIENTATION; // Not used
	}

}
