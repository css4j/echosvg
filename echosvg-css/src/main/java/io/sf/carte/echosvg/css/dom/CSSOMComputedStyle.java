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

import java.util.HashMap;
import java.util.Map;

import org.w3c.css.om.CSSRule;
import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.value.CSSVal;
import io.sf.carte.echosvg.css.engine.value.Value;

/**
 * This class represents the computed style of an element.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class CSSOMComputedStyle implements CSSStyleDeclaration {

	/**
	 * The CSS engine used to compute the values.
	 */
	protected CSSEngine cssEngine;

	/**
	 * The associated element.
	 */
	protected CSSStylableElement element;

	/**
	 * The optional pseudo-element.
	 */
	protected String pseudoElement;

	/**
	 * The CSS values.
	 */
	protected Map<String, CSSVal> values = new HashMap<>();

	/**
	 * Creates a new computed style.
	 */
	public CSSOMComputedStyle(CSSEngine e, CSSStylableElement elt, String pseudoElt) {
		cssEngine = e;
		element = elt;
		pseudoElement = pseudoElt;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#getCssText()}.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cssEngine.getNumberOfProperties(); i++) {
			sb.append(cssEngine.getPropertyName(i));
			sb.append(": ");
			sb.append(cssEngine.getComputedStyle(element, pseudoElement, i).getCssText());
			sb.append(";\n");
		}
		return sb.toString();
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#setCssText(String)}. Throws a
	 * NO_MODIFICATION_ALLOWED_ERR {@link org.w3c.dom.DOMException}.
	 */
	@Override
	public void setCssText(String cssText) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#getPropertyValue(String)}.
	 */
	@Override
	public String getPropertyValue(String propertyName) {
		int idx = cssEngine.getPropertyIndex(propertyName);
		if (idx == -1) {
			return "";
		}
		Value v = cssEngine.getComputedStyle(element, pseudoElement, idx);
		return v.getCssText();
	}

	@Override
	public CSSVal getCSSStyleValue(String propertyName) {
		return getCSSValue(propertyName);
	}

	public CSSVal getCSSValue(String propertyName) {
		CSSVal result = values.get(propertyName);
		if (result == null) {
			int idx = cssEngine.getPropertyIndex(propertyName);
			if (idx != -1) {
				result = createCSSValue(idx);
				values.put(propertyName, result);
			}
		}
		return result;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#removeProperty(String)}.
	 */
	@Override
	public String removeProperty(String propertyName) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#getPropertyPriority(String)}.
	 */
	@Override
	public String getPropertyPriority(String propertyName) {
		return "";
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#setProperty(String,String,String)}.
	 */
	@Override
	public void setProperty(String propertyName, String value, String prio) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#getLength()}.
	 */
	@Override
	public int getLength() {
		return cssEngine.getNumberOfProperties();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.css.CSSStyleDeclaration#item(int)}.
	 */
	@Override
	public String item(int index) {
		if (index < 0 || index >= cssEngine.getNumberOfProperties()) {
			return "";
		}
		return cssEngine.getPropertyName(index);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.CSSStyleDeclaration#getParentRule()}.
	 * 
	 * @return null.
	 */
	@Override
	public CSSRule getParentRule() {
		return null;
	}

	/**
	 * Creates a CSSValue to manage the value at the given index.
	 */
	protected CSSVal createCSSValue(int idx) {
		return new ComputedCSSValue(idx);
	}

	/**
	 * To manage a computed CSSValue.
	 */
	public class ComputedCSSValue extends CSSOMValue implements CSSOMValue.ValueProvider {

		private static final long serialVersionUID = 1L;

		/**
		 * The index of the associated value.
		 */
		protected int index;

		/**
		 * Creates a new ComputedCSSValue.
		 */
		public ComputedCSSValue(int idx) {
			super(null);
			valueProvider = this;
			index = idx;
		}

		/**
		 * Returns the Value associated with this object.
		 */
		@Override
		public Value getValue() {
			return cssEngine.getComputedStyle(element, pseudoElement, index);
		}

	}

}
