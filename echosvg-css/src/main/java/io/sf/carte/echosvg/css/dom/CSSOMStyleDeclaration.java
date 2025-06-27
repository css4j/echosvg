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

import io.sf.carte.echosvg.css.engine.value.AbstractValueModificationHandler;
import io.sf.carte.echosvg.css.engine.value.CSSVal;
import io.sf.carte.echosvg.css.engine.value.Value;

/**
 * This class represents a style declaration.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class CSSOMStyleDeclaration implements CSSStyleDeclaration {

	/**
	 * The associated value.
	 */
	protected ValueProvider valueProvider;

	/**
	 * The modifications handler.
	 */
	protected ModificationHandler handler;

	/**
	 * The parent rule.
	 */
	protected CSSRule parentRule;

	/**
	 * The values.
	 */
	protected Map<String, CSSVal> values;

	/**
	 * Creates a new style declaration.
	 */
	public CSSOMStyleDeclaration(ValueProvider vp, CSSRule parent) {
		valueProvider = vp;
		parentRule = parent;
	}

	/**
	 * Sets the modification handler of this value.
	 */
	public void setModificationHandler(ModificationHandler h) {
		handler = h;
	}

	@Override
	public String getCssText() {
		return valueProvider.getText();
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		if (handler == null) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
		} else {
			values = null;
			handler.textChanged(cssText);
		}
	}

	@Override
	public String getPropertyValue(String propertyName) {
		Value value = valueProvider.getValue(propertyName);
		if (value == null) {
			return "";
		}
		return value.getCssText();
	}

	@Override
	public CSSVal getCSSStyleValue(String propertyName) {
		Value value = valueProvider.getValue(propertyName);
		if (value == null) {
			return null;
		}
		return getCSSValue(propertyName);
	}

	@Override
	public String removeProperty(String propertyName) throws DOMException {
		String result = getPropertyValue(propertyName);
		if (result.length() > 0) {
			if (handler == null) {
				throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
			} else {
				if (values != null) {
					values.remove(propertyName);
				}
				handler.propertyRemoved(propertyName);
			}
		}
		return result;
	}

	@Override
	public String getPropertyPriority(String propertyName) {
		return (valueProvider.isImportant(propertyName)) ? "important" : "";
	}

	@Override
	public void setProperty(String propertyName, String value, String prio) throws DOMException {
		if (handler == null) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
		} else {
			handler.propertyChanged(propertyName, value, prio);
		}
	}

	@Override
	public int getLength() {
		return valueProvider.getLength();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.css.CSSStyleDeclaration#item(int)}.
	 */
	@Override
	public String item(int index) {
		return valueProvider.item(index);
	}

	@Override
	public CSSRule getParentRule() {
		return parentRule;
	}

	/**
	 * Gets the CSS value associated with the given property.
	 */
	protected CSSVal getCSSValue(String name) {
		CSSVal result = null;
		if (values != null) {
			result = values.get(name);
		}
		if (result == null) {
			result = createCSSValue(name);
			if (values == null) {
				values = new HashMap<>(11);
			}
			values.put(name, result);
		}
		return result;
	}

	/**
	 * Creates the CSS value associated with the given property.
	 */
	protected CSSVal createCSSValue(String name) {
		return new StyleDeclarationValue(name);
	}

	/**
	 * To provides the values.
	 */
	public interface ValueProvider {

		/**
		 * Returns the current value associated with this object.
		 */
		Value getValue(String name);

		/**
		 * Tells whether the given property is important.
		 */
		boolean isImportant(String name);

		/**
		 * Returns the text of the declaration.
		 */
		String getText();

		/**
		 * Returns the length of the declaration.
		 */
		int getLength();

		/**
		 * Returns the value at the given.
		 */
		String item(int idx);

	}

	/**
	 * To manage the modifications on a CSS style declaration.
	 */
	public interface ModificationHandler {

		/**
		 * Called when the declaration text has changed.
		 */
		void textChanged(String text) throws DOMException;

		/**
		 * Called when a property was removed.
		 */
		void propertyRemoved(String name) throws DOMException;

		/**
		 * Called when a property was changed.
		 */
		void propertyChanged(String name, String value, String prio) throws DOMException;

	}

	/**
	 * This class represents a CSS value returned by this declaration.
	 */
	public class StyleDeclarationValue extends CSSOMValue implements CSSOMValue.ValueProvider {

		private static final long serialVersionUID = 1L;

		/**
		 * The property name.
		 */
		protected String property;

		/**
		 * Creates a new StyleDeclarationValue.
		 */
		public StyleDeclarationValue(String prop) {
			super(null);
			this.valueProvider = this;
			this.setModificationHandler(new AbstractValueModificationHandler() {
				@Override
				protected Value getValue() {
					return StyleDeclarationValue.this.getValue();
				}

				@Override
				public void setPropertyText(String text) throws DOMException {
					if (values == null || StyleDeclarationValue.this.handler == null) {
						throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
					}
					String prio = getPropertyPriority(property);
					CSSOMStyleDeclaration.this.handler.propertyChanged(property, text, prio);
				}
			});

			property = prop;
		}

		// ValueProvider ///////////////////////////////

		/**
		 * Returns the current value associated with this object.
		 */
		@Override
		public Value getValue() {
			return CSSOMStyleDeclaration.this.valueProvider.getValue(property);
		}

	}

}
