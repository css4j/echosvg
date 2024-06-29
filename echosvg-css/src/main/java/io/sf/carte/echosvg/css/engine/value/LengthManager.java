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

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import io.sf.carte.doc.style.css.CSSUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;

/**
 * This class provides a manager for the property with support for length
 * values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class LengthManager extends AbstractValueManager {

	/**
	 * precomputed square-root of 2.0
	 */
	static final double SQRT2 = Math.sqrt(2.0);

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case DIMENSION:
			Value value = createLength(lu);
			if (value != null) {
				return value;
			}
			break;

		case INTEGER:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getIntegerValue());

		case REAL:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getFloatValue());

		case PERCENTAGE:
			return new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, lu.getFloatValue());
		default:
			break;
		}
		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

	static Value createLength(LexicalUnit lu) {
		if (CSSUnit.isLengthUnitType(lu.getCssUnit())) {
			short omUnit;
			switch (lu.getCssUnit()) {
			case CSSUnit.CSS_PX:
				omUnit = CSSPrimitiveValue.CSS_PX;
				break;
			case CSSUnit.CSS_IN:
				omUnit = CSSPrimitiveValue.CSS_IN;
				break;
			case CSSUnit.CSS_PC:
				omUnit = CSSPrimitiveValue.CSS_PC;
				break;
			case CSSUnit.CSS_PT:
				omUnit = CSSPrimitiveValue.CSS_PT;
				break;
			case CSSUnit.CSS_CM:
				omUnit = CSSPrimitiveValue.CSS_CM;
				break;
			case CSSUnit.CSS_MM:
				omUnit = CSSPrimitiveValue.CSS_MM;
				break;
			case CSSUnit.CSS_EM:
				omUnit = CSSPrimitiveValue.CSS_EMS;
				break;
			case CSSUnit.CSS_EX:
				omUnit = CSSPrimitiveValue.CSS_EXS;
				break;
			default:
				return null;
			}
			return new FloatValue(omUnit, lu.getFloatValue());
		}
		return null;
	}

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	@Override
	public Value createFloatValue(short type, float floatValue) throws DOMException {
		switch (type) {
		case CSSPrimitiveValue.CSS_PERCENTAGE:
		case CSSPrimitiveValue.CSS_EMS:
		case CSSPrimitiveValue.CSS_EXS:
		case CSSPrimitiveValue.CSS_PX:
		case CSSPrimitiveValue.CSS_CM:
		case CSSPrimitiveValue.CSS_MM:
		case CSSPrimitiveValue.CSS_IN:
		case CSSPrimitiveValue.CSS_PT:
		case CSSPrimitiveValue.CSS_PC:
		case CSSPrimitiveValue.CSS_NUMBER:
			return new FloatValue(type, floatValue);
		}
		throw createInvalidFloatTypeDOMException(type);
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.getCssValueType() != CSSValue.CSS_PRIMITIVE_VALUE) {
			return value;
		}

		switch (value.getPrimitiveType()) {
		case CSSPrimitiveValue.CSS_NUMBER:
		case CSSPrimitiveValue.CSS_PX:
			return value;

		case CSSPrimitiveValue.CSS_MM:
			CSSContext ctx = engine.getCSSContext();
			float v = value.getFloatValue();
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v / ctx.getPixelUnitToMillimeter());

		case CSSPrimitiveValue.CSS_CM:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * 10f / ctx.getPixelUnitToMillimeter());

		case CSSPrimitiveValue.CSS_IN:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * 25.4f / ctx.getPixelUnitToMillimeter());

		case CSSPrimitiveValue.CSS_PT:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * 25.4f / (72f * ctx.getPixelUnitToMillimeter()));

		case CSSPrimitiveValue.CSS_PC:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, (v * 25.4f / (6f * ctx.getPixelUnitToMillimeter())));

		case CSSPrimitiveValue.CSS_EMS:
			sm.putFontSizeRelative(idx, true);

			v = value.getFloatValue();
			int fsidx = engine.getFontSizeIndex();
			float fs;
			fs = engine.getComputedStyle(elt, pseudo, fsidx).getFloatValue();
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * fs);

		case CSSPrimitiveValue.CSS_EXS:
			sm.putFontSizeRelative(idx, true);

			v = value.getFloatValue();
			fsidx = engine.getFontSizeIndex();
			fs = engine.getComputedStyle(elt, pseudo, fsidx).getFloatValue();
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * fs * 0.5f);

		case CSSPrimitiveValue.CSS_PERCENTAGE:
			ctx = engine.getCSSContext();
			switch (getOrientation()) {
			case HORIZONTAL_ORIENTATION:
				sm.putBlockWidthRelative(idx, true);
				fs = value.getFloatValue() * ctx.getBlockWidth(elt) / 100f;
				break;
			case VERTICAL_ORIENTATION:
				sm.putBlockHeightRelative(idx, true);
				fs = value.getFloatValue() * ctx.getBlockHeight(elt) / 100f;
				break;
			default: // Both
				sm.putBlockWidthRelative(idx, true);
				sm.putBlockHeightRelative(idx, true);
				double w = ctx.getBlockWidth(elt);
				double h = ctx.getBlockHeight(elt);
				fs = (float) (value.getFloatValue() * (Math.sqrt(w * w + h * h) / SQRT2) / 100.0);
			}
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, fs);
		}
		return value;
	}

	//
	// Orientation enumeration
	//
	protected static final int HORIZONTAL_ORIENTATION = 0;
	protected static final int VERTICAL_ORIENTATION = 1;
	protected static final int BOTH_ORIENTATION = 2;

	/**
	 * Indicates the orientation of the property associated with this manager.
	 */
	protected abstract int getOrientation();

}
