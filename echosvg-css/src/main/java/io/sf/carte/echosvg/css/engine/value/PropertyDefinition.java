/*
 * This software adapts interfaces defined by CSS Properties and Values API Level 1
 *  (https://www.w3.org/TR/css-properties-values-api-1/).
 * Copyright © 2020 W3C® (MIT, ERCIM, Keio, Beihang).
 * https://www.w3.org/Consortium/Legal/2015/copyright-software-and-document
 */
/*
 * SPDX-License-Identifier: W3C-20150513
 */

package io.sf.carte.echosvg.css.engine.value;

import io.sf.carte.doc.style.css.CSSValueSyntax;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;

/**
 * A property definition.
 * <p>
 * See <a href=
 * "https://www.w3.org/TR/css-properties-values-api-1/#the-propertydefinition-dictionary">CSS
 * Properties and Values API Level 1</a>.
 * </p>
 */
public interface PropertyDefinition {

	/**
	 * Gets the property name.
	 * 
	 * @return the property name.
	 */
	String getName();

	/**
	 * Whether the property inherits or not.
	 * 
	 * @return {@code true} if the property inherits.
	 */
	boolean inherits();

	/**
	 * The initial value associated with the property.
	 * 
	 * @return the initial value, or {@code null} if none was specified.
	 */
	LexicalUnit getInitialValue();

	/**
	 * The syntax associated with the property.
	 * <p>
	 * If the syntax descriptor was not recognized, {@code *} will be used.
	 * </p>
	 * 
	 * @return the syntax. Cannot be {@code null}.
	 */
	CSSValueSyntax getSyntax();

}
