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

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.property.StyleValue;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;

/**
 * This class provides an abstract implementation of the ValueManager interface.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class AbstractValueManager extends AbstractValueFactory implements ValueManager {

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	@Override
	public Value createFloatValue(short unitType, float floatValue) throws DOMException {
		throw createDOMException();
	}

	@Override
	public Value createStringValue(Type type, String value, CSSEngine engine) throws DOMException {
		throw createDOMException();
	}

	protected Value createCalc(LexicalUnit lu) throws DOMException {
		LexicalUnit lunit;
		if (lu.getNextLexicalUnit() != null) {
			lunit = lu.shallowClone();
		} else {
			lunit = lu;
		}
		ValueFactory vf = new ValueFactory();
		StyleValue cssValue = vf.createCSSValue(lunit);

		Type pType = cssValue.getPrimitiveType();
		if (pType != Type.EXPRESSION) {
			if (pType == Type.LEXICAL) {
				if (lunit.getPreviousLexicalUnit() != null || lunit.isParameter()) {
					throw new CSSProxyValueException();
				}
				return createLexicalValue(lunit);
			}
			createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}

		CalcValue calc = new CalcValue((CSSExpressionValue) cssValue) {

			@Override
			protected FloatValue absoluteValue(CSSStylableElement elt, String pseudo, CSSEngine engine,
					int idx, StyleMap sm, FloatValue relative) {
				return (FloatValue) AbstractValueManager.this.computeValue(elt, pseudo, engine, idx, sm,
						relative);
			}

		};

		return calc;
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {

		if (value.getPrimitiveType() == Type.URI) {
			// For performance, set the parsed value as the cssText now.
			String uri = value.getURIValue();
			return new URIValue(uri, uri);
		}

		return value;
	}

	protected float lengthValue(Value cv) {
		short unit = cv.getUnitType();
		if (!CSSUnit.isLengthUnitType(unit) && unit != CSSUnit.CSS_NUMBER) {
			throw createDOMException(unit);
		}
		return cv.getFloatValue();
	}

	/**
	 * Creates an INVALID_ACCESS_ERR exception.
	 * @param unit the unit.
	 */
	protected DOMException createDOMException(int unit) {
		Object[] p = { unit };
		String s = Messages.formatMessage("invalid.value.access", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	protected Value createLexicalValue(LexicalUnit lu) throws CSSProxyValueException {
		if (lu.getPreviousLexicalUnit() != null || lu.isParameter()) {
			throw new CSSProxyValueException();
		}
		return new LexicalValue(lu);
	}

}
