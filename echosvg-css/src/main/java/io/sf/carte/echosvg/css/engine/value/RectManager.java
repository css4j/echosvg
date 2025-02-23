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

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class provides a manager for the property with support for rect values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * 
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
	public Value createValue(final LexicalUnit lunit, CSSEngine engine) throws DOMException {
		switch (lunit.getLexicalUnitType()) {
		case FUNCTION:
			// This case could be removed
			if (!lunit.getFunctionName().equalsIgnoreCase("rect")) {
				break;
			}
		case RECT_FUNCTION:
			LexicalUnit lu = lunit.getParameters();
			try {
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
			} catch (CSSProxyValueException e) {
				return createLexicalValue(lunit);
			}

		case UNSET:
			return UnsetValue.getInstance();

		case REVERT:
			return RevertValue.getInstance();

		case INITIAL:
			return getDefaultValue();

		case VAR:
		case ATTR:
			return createLexicalValue(lunit);

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
			return createLength(lu);

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
			return createCalc(lu);

		case MATH_FUNCTION:
			Value v;
			try {
				v = createMathFunction(lu, "<length-percentage>");
			} catch (Exception e) {
				DOMException mrd = createMalformedRectDOMException();
				mrd.initCause(e);
				throw mrd;
			}
			return v;

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
		if (value.getPrimitiveType() != Type.RECT) {
			return value;
		}

		RectValue rect = value.getRectValue();

		orientation = VERTICAL_ORIENTATION;
		Value top = super.computeValue(elt, pseudo, engine, idx, sm, rect.getTop());
		Value bottom = super.computeValue(elt, pseudo, engine, idx, sm, rect.getBottom());
		orientation = HORIZONTAL_ORIENTATION;
		Value left = super.computeValue(elt, pseudo, engine, idx, sm, rect.getLeft());
		Value right = super.computeValue(elt, pseudo, engine, idx, sm, rect.getRight());
		if (top != rect.getTop() || right != rect.getRight() || bottom != rect.getBottom()
				|| left != rect.getLeft()) {
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
