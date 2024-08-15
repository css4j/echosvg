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
package io.sf.carte.echosvg.css.dom;

import org.w3c.css.om.typed.CSSKeywordValue;
import org.w3c.css.om.typed.CSSStringValue;
import org.w3c.css.om.typed.CSSStyleValue;
import org.w3c.dom.DOMException;

import org.w3c.css.om.unit.CSSUnit;
import io.sf.carte.doc.style.css.property.KeywordValue;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.NumericValue;
import io.sf.carte.echosvg.css.engine.value.StringValue;

/**
 * A gateway value, useful in the transition to Typed OM.
 *
 * @author See Git history.
 * @version $Id$
 */
public interface CSSValue extends CSSStyleValue {

	/**
	 * The main categories of values.
	 */
	enum CssType {

		/**
		 * A CSS-wide keyword like {@code inherit}.
		 */
		KEYWORD,

		/**
		 * <p>
		 * A vehicle towards a final value, of a CSS type that cannot be anticipated.
		 * </p>
		 * <p>
		 * Example: {@code var()} or {@code attr()}.
		 * </p>
		 * <p>
		 * <i>(note that </i>{@code attr()}<i> has two components, a main one whose type
		 * could be anticipated, and a fallback that could be of a different type)</i>.
		 * </p>
		 */
		PROXY,

		/**
		 * A typed primitive value, includes numbers and identifiers.
		 */
		TYPED,

		/**
		 * A list of values.
		 * <p>
		 * You can cast to {@link ListValue}.
		 * </p>
		 */
		LIST,

		/**
		 * A shorthand property.
		 */
		SHORTHAND
	}

	/**
	 * The type of value. For keywords, it is the keyword.
	 */
	enum Type {
		/**
		 * Unknown type, probably a system default or a compat value.
		 */
		UNKNOWN,

		/**
		 * {@code inherit} keyword.
		 */
		INHERIT,

		/**
		 * {@code initial} keyword.
		 */
		INITIAL,

		/**
		 * {@code unset} keyword.
		 */
		UNSET,

		/**
		 * {@code revert} keyword.
		 */
		REVERT,

		/**
		 * Numeric type (excludes {@code calc()} which is an {@link #EXPRESSION}).
		 * <p>
		 * Cast to {@link NumericValue}.
		 * </p>
		 */
		NUMERIC,

		/**
		 * String.
		 * <p>
		 * Cast to {@link StringValue}.
		 * </p>
		 */
		STRING,

		/**
		 * Identifier.
		 * <p>
		 * Cast to {@link KeywordValue}.
		 * </p>
		 */
		IDENT,

		/**
		 * Color.
		 */
		COLOR,

		/**
		 * Color-Mix function.
		 */
		COLOR_MIX,

		/**
		 * URI ({@code url()}).
		 * <p>
		 * Cast to {@link CSSStringValue}.
		 * </p>
		 */
		URI,

		/**
		 * {@code rect()} function.
		 */
		RECT,

		/**
		 * An expression with algebraic syntax (i.e. <code>calc()</code>).
		 */
		EXPRESSION,

		/**
		 * Gradient function.
		 */
		GRADIENT,

		/**
		 * CSS <code>counter()</code> function.
		 */
		COUNTER,

		/**
		 * CSS <code>counters()</code> function.
		 */
		COUNTERS,

		/**
		 * <code>cubic-bezier()</code> easing function.
		 */
		CUBIC_BEZIER,

		/**
		 * <code>steps()</code> easing function.
		 */
		STEPS,

		/**
		 * Function.
		 */
		FUNCTION,

		/**
		 * Mathematical function (as defined by CSS Values and Units).
		 */
		MATH_FUNCTION,

		/**
		 * Unicode range.
		 */
		UNICODE_RANGE,

		/**
		 * Unicode character.
		 */
		UNICODE_CHARACTER,

		/**
		 * Unicode wildcard.
		 */
		UNICODE_WILDCARD,

		/**
		 * Element reference.
		 * <p>
		 * Cast to {@link CSSKeywordValue}.
		 * </p>
		 */
		ELEMENT_REFERENCE,

		/**
		 * Ratio value.
		 */
		RATIO,

		/**
		 * {@code attr()} function.
		 */
		ATTR,

		/**
		 * Custom property reference.
		 */
		VAR,

		/**
		 * Environment variable.
		 */
		ENV,

		/**
		 * Lexical value.
		 */
		LEXICAL,

		/**
		 * For this library's internal use.
		 */
		INTERNAL,

		/**
		 * Invalid (non-primitive and non-keyword) value.
		 * <p>
		 * The value is either a list or a shorthand.
		 * </p>
		 */
		INVALID

	}

	/**
	 * Get the general category to which this value belongs.
	 * 
	 * @return the general value type.
	 */
	CssType getCssValueType();

	/**
	 * Get the primitive type.
	 * 
	 * @return the primitive type.
	 */
	Type getPrimitiveType();

	/**
	 * Gets the css unit as in CSS4J's {@code CSSUnit}.
	 * <p>
	 * If the value has no valid CSS unit, returns {@code CSSUnit.CSS_INVALID}.
	 * </p>
	 * 
	 * @return the css unit as in CSS4J's {@code CSSUnit}.
	 */
	default short getCSSUnit() {
		return CSSUnit.CSS_INVALID;
	}

	/**
	 * Set this value to the result of parsing the argument.
	 * 
	 * @param cssText a CSS serialization to set this value.
	 * @throws DOMException
	 */
	void setCssText(String cssText) throws DOMException;

	/**
	 * Get a parsable representation of this value.
	 * 
	 * @return the CSS serialization of this value.
	 */
	String getCssText();

	/**
	 * Convenience method that either returns the float value, if the value is
	 * numeric, or throws an exception.
	 * 
	 * @return the float value.
	 */
	float getFloatValue();

	/**
	 * Convenience method that either returns an identifier or throws an exception.
	 * 
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain an identifier value.
	 */
	String getIdentifierValue() throws DOMException;

	/**
	 * If this value can be used where a string is expected, get the value.
	 * 
	 * @return the string value, without the commas.
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain a String.
	 */
	String getStringValue() throws DOMException;

	/**
	 * Convenience method that either returns a String or URI or throws an exception.
	 * 
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain a String nor a URI value.
	 */
	String getURIValue() throws DOMException;

	/**
	 * If this value is a list or contains components, the number of
	 * <code>CSSStyleValue</code>s in the list. The range of valid values of the
	 * indices is <code>0</code> to <code>length-1</code> inclusive.
	 * 
	 * @return the number of components, or {@code 0} if this value is not a list
	 *         nor does it contain components.
	 */
	int getLength();

	/**
	 * If this value is a list, give the item corresponding to the requested index.
	 * If there is no item at such index, return {@code null} If this object is not
	 * a list and the index is {@code 0}, return itself.
	 * 
	 * @param index the index on the list.
	 * @return the item, or {@code null} if there is no item on that index.
	 */
	default CSSValue item(int index) {
		return index == 0 ? this : null;
	}

}
