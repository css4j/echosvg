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

import org.w3c.css.om.unit.CSSUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.css.dom.CSSValue.Type;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;

/**
 * This class provides a manager for the property with support for length
 * values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
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
			return new FloatValue(CSSUnit.CSS_NUMBER, lu.getIntegerValue());

		case REAL:
			return new FloatValue(CSSUnit.CSS_NUMBER, lu.getFloatValue());

		case PERCENTAGE:
			return new FloatValue(CSSUnit.CSS_PERCENTAGE, lu.getFloatValue());
		default:
			break;
		}
		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

	static FloatValue createLength(LexicalUnit lu) {
		if (CSSUnit.isLengthUnitType(lu.getCssUnit())) {
			return new FloatValue(lu.getCssUnit(), lu.getFloatValue());
		}
		return null;
	}

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	@Override
	public Value createFloatValue(short type, float floatValue) throws DOMException {
		if (type == CSSUnit.CSS_NUMBER || type == CSSUnit.CSS_PERCENTAGE || CSSUnit.isLengthUnitType(type)) {
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
		if (value.getPrimitiveType() != Type.NUMERIC) {
			return value;
		}

		switch (value.getCSSUnit()) {
		case CSSUnit.CSS_NUMBER:
		case CSSUnit.CSS_PX:
			return value;

		case CSSUnit.CSS_MM:
			CSSContext ctx = engine.getCSSContext();
			float v = value.getFloatValue();
			return new FloatValue(CSSUnit.CSS_NUMBER, v / ctx.getPixelUnitToMillimeter());

		case CSSUnit.CSS_CM:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSUnit.CSS_NUMBER, v * 10f / ctx.getPixelUnitToMillimeter());

		case CSSUnit.CSS_IN:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSUnit.CSS_NUMBER, v * 25.4f / ctx.getPixelUnitToMillimeter());

		case CSSUnit.CSS_PT:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSUnit.CSS_NUMBER, v * 25.4f / (72f * ctx.getPixelUnitToMillimeter()));

		case CSSUnit.CSS_PC:
			ctx = engine.getCSSContext();
			v = value.getFloatValue();
			return new FloatValue(CSSUnit.CSS_NUMBER, (v * 25.4f / (6f * ctx.getPixelUnitToMillimeter())));

		case CSSUnit.CSS_EM:
			sm.putFontSizeRelative(idx, true);

			v = value.getFloatValue();
			int fsidx = engine.getFontSizeIndex();
			Value cv = engine.getComputedStyle(elt, pseudo, fsidx);
			float fs = lengthValue(cv);
			return new FloatValue(CSSUnit.CSS_NUMBER, v * fs);

		case CSSUnit.CSS_EX:
			sm.putFontSizeRelative(idx, true);

			v = value.getFloatValue();
			fsidx = engine.getFontSizeIndex();
			cv = engine.getComputedStyle(elt, pseudo, fsidx);
			fs = lengthValue(cv);
			return new FloatValue(CSSUnit.CSS_NUMBER, v * fs * 0.5f);

		case CSSUnit.CSS_PERCENTAGE:
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
			return new FloatValue(CSSUnit.CSS_NUMBER, fs);
		case CSSUnit.CSS_INVALID:
			break;
		default:
			// Maybe it is one of the new absolute length units
			try {
				value = new FloatValue(CSSUnit.CSS_NUMBER,
						NumberValue.floatValueConversion(value.getFloatValue(), value.getCSSUnit(), CSSUnit.CSS_PX));
			} catch (DOMException e) {
			}
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
