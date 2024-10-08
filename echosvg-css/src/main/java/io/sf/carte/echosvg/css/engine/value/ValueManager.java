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

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;

/**
 * This interface is implemented by objects which manage the values associated
 * with a property.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public interface ValueManager {

	/**
	 * Returns the name of the property handled.
	 */
	String getPropertyName();

	/**
	 * Whether the handled property is inherited or not.
	 */
	boolean isInheritedProperty();

	/**
	 * Whether the handled property can be animated.
	 */
	boolean isAnimatableProperty();

	/**
	 * Whether the handled property can be additively animated.
	 */
	boolean isAdditiveProperty();

	/**
	 * Allows URL values.
	 * <p>
	 * If this property accepts URL values, then {@code attr()} values cannot be
	 * used.
	 * </p>
	 * 
	 * @return {@code true} if URL values are allowed by this property.
	 */
	default boolean allowsURL() {
		return false;
	}

	/**
	 * Returns the type of value this manager handles. This should be one of the
	 * TYPE_* constants defined in {@link io.sf.carte.echosvg.util.SVGTypes}.
	 */
	int getPropertyType();

	/**
	 * Returns the default value for the handled property.
	 */
	Value getDefaultValue();

	/**
	 * Creates a value from a lexical unit.
	 * 
	 * @param lu     The SAC lexical unit used to create the value.
	 * @param engine The calling CSSEngine.
	 */
	Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException;

	/**
	 * Creates and returns a new float value.
	 * 
	 * @param unitType   A unit code as defined above. The unit code can only be a
	 *                   float unit type
	 * @param floatValue The new float value.
	 */
	Value createFloatValue(short unitType, float floatValue) throws DOMException;

	/**
	 * Creates and returns a new string value.
	 * 
	 * @param type   A value type as defined in Value.Type. The string code
	 *               can only be a string-like unit type.
	 * @param value  The new string value.
	 * @param engine The CSS engine.
	 */
	Value createStringValue(Type type, String value, CSSEngine engine) throws DOMException;

	/**
	 * Computes the given value.
	 * 
	 * @param elt    The owner of the value.
	 * @param pseudo The pseudo element.
	 * @param engine The CSSEngine.
	 * @param idx    The property index in the engine.
	 * @param sm     The computed style map.
	 * @param value  The value to compute.
	 */
	Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm, Value value);

}
