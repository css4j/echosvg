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

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class provides a manager for the property with support for rect values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class RectManager extends LengthManager {

	/**
	 * The current orientation.
	 */
	protected int orientation;

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case FUNCTION:
			if (!lu.getFunctionName().equalsIgnoreCase("rect")) {
				break;
			}
		case RECT_FUNCTION:
			lu = lu.getParameters();
			Value top = createRectComponent(lu);
			lu = lu.getNextLexicalUnit();
			if (lu == null || lu.getLexicalUnitType() != LexicalUnit.LexicalType.OPERATOR_COMMA) {
				throw createMalformedRectDOMException();
			}
			lu = lu.getNextLexicalUnit();
			Value right = createRectComponent(lu);
			lu = lu.getNextLexicalUnit();
			if (lu == null || lu.getLexicalUnitType() != LexicalUnit.LexicalType.OPERATOR_COMMA) {
				throw createMalformedRectDOMException();
			}
			lu = lu.getNextLexicalUnit();
			Value bottom = createRectComponent(lu);
			lu = lu.getNextLexicalUnit();
			if (lu == null || lu.getLexicalUnitType() != LexicalUnit.LexicalType.OPERATOR_COMMA) {
				throw createMalformedRectDOMException();
			}
			lu = lu.getNextLexicalUnit();
			Value left = createRectComponent(lu);
			return new RectValue(top, right, bottom, left);
		default:
			break;
		}
		throw createMalformedRectDOMException();
	}

	private Value createRectComponent(LexicalUnit lu) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case IDENT:
			if (lu.getStringValue().equalsIgnoreCase(CSSConstants.CSS_AUTO_VALUE)) {
				return ValueConstants.AUTO_VALUE;
			}
			break;
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
		throw createMalformedRectDOMException();
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
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_RECT) {
			return value;
		}
		RectValue rect = (RectValue) value;

		orientation = VERTICAL_ORIENTATION;
		Value top = super.computeValue(elt, pseudo, engine, idx, sm, rect.getTop());
		Value bottom = super.computeValue(elt, pseudo, engine, idx, sm, rect.getBottom());
		orientation = HORIZONTAL_ORIENTATION;
		Value left = super.computeValue(elt, pseudo, engine, idx, sm, rect.getLeft());
		Value right = super.computeValue(elt, pseudo, engine, idx, sm, rect.getRight());
		if (top != rect.getTop() || right != rect.getRight() || bottom != rect.getBottom() || left != rect.getLeft()) {
			return new RectValue(top, right, bottom, left);
		} else {
			return value;
		}
	}

	/**
	 * Indicates the orientation of the property associated with this manager.
	 */
	@Override
	protected int getOrientation() {
		return orientation;
	}

	private DOMException createMalformedRectDOMException() {
		Object[] p = { getPropertyName() };
		String s = Messages.formatMessage("malformed.rect", p);
		return new DOMException(DOMException.SYNTAX_ERR, s);
	}
}
