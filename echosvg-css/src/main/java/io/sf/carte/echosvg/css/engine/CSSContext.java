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

import org.w3c.dom.Element;

import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This interface allows the user of a CSSEngine to provide contextual
 * informations.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface CSSContext {

	/**
	 * Returns the Value corresponding to the given system color.
	 */
	Value getSystemColor(String ident);

	/**
	 * Returns the value corresponding to the default font-family.
	 */
	Value getDefaultFontFamily();

	/**
	 * Returns a lighter font-weight.
	 */
	float getLighterFontWeight(float f);

	/**
	 * Returns a bolder font-weight.
	 */
	float getBolderFontWeight(float f);

	/**
	 * Returns the size of a px CSS unit in millimeters.
	 */
	float getPixelUnitToMillimeter();

	/**
	 * Returns the size of a px CSS unit in millimeters. This will be removed after
	 * next release.
	 * 
	 * @see #getPixelUnitToMillimeter()
	 */
	@Deprecated(forRemoval = true)
	default float getPixelToMillimeter() {
		return getPixelUnitToMillimeter();
	}

	/**
	 * Returns the medium font size.
	 */
	float getMediumFontSize();

	/**
	 * Returns the width of the block which directly contains the given element.
	 */
	float getBlockWidth(Element elt);

	/**
	 * Returns the height of the block which directly contains the given element.
	 */
	float getBlockHeight(Element elt);

	/**
	 * This method should throw a SecurityException if the resource found at url and
	 * referenced from docURL should not be loaded.
	 *
	 * @param resourceURL url for the resource, as defined in the resource's
	 *                    xlink:href attribute. If that attribute was empty, then
	 *                    this parameter should be null
	 * @param docURL      url for the document into which the resource was found.
	 */
	void checkLoadExternalResource(ParsedURL resourceURL, ParsedURL docURL) throws SecurityException;

	/**
	 * Returns true if the document is dynamic, false otherwise.
	 */
	boolean isDynamic();

	/**
	 * Returns true if the document is interactive, false otherwise.
	 */
	boolean isInteractive();

	/**
	 * Returns the CSS engine associated with given element.
	 */
	CSSEngine getCSSEngineForElement(Element e);

}
