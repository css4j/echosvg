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

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;

/**
 * This interface represents the objects which provide support for shorthand
 * properties.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public interface ShorthandManager {

	/**
	 * Returns the name of the property handled.
	 */
	String getPropertyName();

	/**
	 * Whether the handled property can be animated.
	 */
	boolean isAnimatableProperty();

	/**
	 * Whether the handled property can be additively animated.
	 */
	boolean isAdditiveProperty();

	/**
	 * Sets the properties which are affected by this shorthand property.
	 * 
	 * @param eng The current CSSEngine.
	 * @param ph  The property handler to use.
	 * @param lu  The NSAC lexical unit used to create the value.
	 * @param imp The property priority.
	 */
	void setValues(CSSEngine eng, PropertyHandler ph, LexicalUnit lu, boolean imp) throws DOMException;

	/**
	 * To handle a property value created by a ShorthandManager.
	 */
	interface PropertyHandler {

		void property(String name, LexicalUnit value, boolean important);

		/**
		 * Process a longhand value that points to a shorthand that is pending lexical
		 * substitution.
		 * 
		 * @param name      the longhand property name.
		 * @param value     the pending value that contains the shorthand's lexical
		 *                  value.
		 * @param important the priority.
		 */
		void pendingValue(String name, PendingValue value, boolean important);

	}

}
