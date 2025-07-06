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
import io.sf.carte.doc.style.css.CSSMathFunctionValue;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.CSSValueSyntax;
import io.sf.carte.doc.style.css.CSSValueSyntax.Match;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.doc.style.css.parser.SyntaxParser;
import io.sf.carte.doc.style.css.property.StyleValue;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSEngineUserAgent;
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

	protected Value createLexicalValue(LexicalUnit lu) throws CSSProxyValueException {
		if (lu.getPreviousLexicalUnit() != null || lu.isParameter()) {
			throw new CSSProxyValueException();
		}
		return new LexicalValue(lu);
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

			private static final long serialVersionUID = 1L;

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
	 * Create a mathematical function from the given lexical unit.
	 * 
	 * @param lu     the lexical unit.
	 * @param syntax the syntax that the function must match.
	 * @return the mathematical function.
	 * @throws DOMException if not a valid mathematical function for the given
	 *                      syntax.
	 */
	protected Value createMathFunction(LexicalUnit lu, String syntax) throws DOMException {
		LexicalUnit lunit;
		if (lu.getNextLexicalUnit() != null) {
			lunit = lu.shallowClone();
		} else {
			lunit = lu;
		}
		ValueFactory vf = new ValueFactory();
		StyleValue cssValue = vf.createCSSValue(lunit);

		Type pType = cssValue.getPrimitiveType();
		if (pType != Type.MATH_FUNCTION) {
			if (pType == Type.LEXICAL) {
				if (lunit.getPreviousLexicalUnit() != null || lunit.isParameter()) {
					throw new CSSProxyValueException();
				}
				return createLexicalValue(lunit);
			}
			createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}

		MathFunctionValue calc = new MathFunctionValue((CSSMathFunctionValue) cssValue) {

			private static final long serialVersionUID = 1L;

			@Override
			protected FloatValue absoluteValue(CSSStylableElement elt, String pseudo, CSSEngine engine,
					int idx, StyleMap sm, FloatValue relative) {
				return (FloatValue) AbstractValueManager.this.computeValue(elt, pseudo, engine, idx, sm,
						relative);
			}

		};

		CSSValueSyntax syn = new SyntaxParser().parseSyntax(syntax);
		if (calc.matches(syn) == Match.FALSE) {
			throw createDOMException(calc);
		}

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
			if (sm.isAttrTainted(idx)) {
				CSSEngineUserAgent ua = engine.getCSSEngineUserAgent();
				if (ua != null) {
					ua.warn("attr()-tainted value: " + value.getCssText());
				}
				return null;
			}

			// For performance, set the parsed value as the cssText now.
			String uri = value.getURIValue();
			return new URIValue(uri, uri);
		}

		return value;
	}

	protected Value evaluateMath(NumericDelegateValue<?> value, CSSStylableElement elt, String pseudo,
			CSSEngine engine, int idx, StyleMap sm, short destUnit) throws DOMException {
		return value.evaluate(elt, pseudo, engine, idx, sm, destUnit);
	}

	/**
	 * Obtain the float value, making sure that it is a <number> or a <length>.
	 * 
	 * @param cv the value.
	 * @return the float value
	 */
	protected float lengthValue(Value cv) {
		short unit = cv.getUnitType();
		if (!CSSUnit.isLengthUnitType(unit) && unit != CSSUnit.CSS_NUMBER) {
			throw createDOMException(cv);
		}
		return cv.getFloatValue();
	}

	protected Value createURIValue(LexicalUnit lu, CSSEngine engine) {
		String sv = lu.getStringValue();
		return new URIValue(sv, resolveURI(engine.getCSSBaseURI(), sv));
	}

	static LexicalUnit nextLexicalUnitNonNull(LexicalUnit lu, LexicalUnit firstUnit)
			throws DOMException {
		lu = lu.getNextLexicalUnit();
		if (lu == null) {
			throw createDOMSyntaxException(firstUnit);
		}
		LexicalType type = lu.getLexicalUnitType();
		if (type == LexicalType.VAR || type == LexicalType.ATTR) {
			throw new CSSProxyValueException();
		}
		return lu;
	}

	/**
	 * Creates an INVALID_ACCESS_ERR exception.
	 * @param cv the value.
	 */
	protected DOMException createDOMException(Value cv) {
		Object[] p = { CSSUnit.dimensionUnitString(cv.getUnitType()), cv.getCssText() };
		String s = Messages.formatMessage("invalid.value.access", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	/**
	 * Creates a SYNTAX_ERR exception.
	 * @param lu the value.
	 */
	static DOMException createDOMSyntaxException(LexicalUnit lu) {
		Object[] p = { lu.toString() };
		String s = Messages.formatMessage("invalid.value.syntax", p);
		return new DOMException(DOMException.SYNTAX_ERR, s);
	}

}
