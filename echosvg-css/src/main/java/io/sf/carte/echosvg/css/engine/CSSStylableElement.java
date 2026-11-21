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
package io.sf.carte.echosvg.css.engine;

import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This interface must be implemented by the DOM elements which needs CSS
 * support.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface CSSStylableElement extends Element {

	/**
	 * Returns the computed style of this element/pseudo-element.
	 */
	StyleMap getComputedStyleMap(String pseudoElement);

	/**
	 * Sets the computed style of this element/pseudo-element.
	 */
	void setComputedStyleMap(String pseudoElement, StyleMap sm);

	/**
	 * Returns the ID of this element.
	 */
	String getXMLId();

	/**
	 * Returns the class of this element.
	 */
	String getCSSClass();

	/**
	 * Returns the CSS base URL of this element.
	 */
	ParsedURL getCSSBase();

	/**
	 * Get the element-dependent initial value for the given property.
	 * 
	 * @param idx the property index.
	 * @return the initial value, or {@code null} if no initial value is known for
	 *         the given index.
	 */
	Value getInitialValue(int idx);

	/**
	 * Tells whether this element is an instance of the given pseudo class.
	 */
	boolean isPseudoInstanceOf(String pseudoClass);

	/**
	 * Gets the inline style of this element.
	 * 
	 * @return the inline style, or {@code null} if this element cannot have inline
	 *         style.
	 */
	CSSStyleDeclaration getStyle();

	/**
	 * Returns the object that gives access to the underlying
	 * {@link StyleDeclaration} for the override style of this element.
	 */
	StyleDeclarationProvider getOverrideStyleDeclarationProvider();

}
