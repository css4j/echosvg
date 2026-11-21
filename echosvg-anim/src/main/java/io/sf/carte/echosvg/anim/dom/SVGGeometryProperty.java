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
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.svg.SVGAnimatedLength;

import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;

/**
 * Represents an SVG geometry property.
 */
public interface SVGGeometryProperty extends SVGAnimatedLength {

	/**
	 * Get the current animated float computed value, applying defaults if the value
	 * was invalid or not set.
	 * 
	 * @param sm the style map that applies.
	 * @return the float computed value.
	 */
	float getAnimatedComputedValue(StyleMap sm);

	/**
	 * Compute the animated value of this property, or its base value according to
	 * the given style map.
	 * 
	 * @param sm the style map that applies.
	 * @return the computed value, or {@code null} if it is not animated, or no
	 *         value was set or it is invalid.
	 */
	Value computeAnimatedValue(StyleMap sm);

	/**
	 * Compute the value of this property according to the given style map.
	 * <p>
	 * Ignores animated values.
	 * </p>
	 * 
	 * @param sm the style map that applies.
	 * @return the computed value, or {@code null} if no value was set or it is
	 *         invalid.
	 */
	Value computeValue(StyleMap sm);

	/**
	 * Gets the current animated length value, or the computed property value if not
	 * animated.
	 * 
	 * @return the current animated length value, expressed in user space.
	 * @throws LiveAttributeException if the property value is invalid.
	 */
	float getCheckedValue() throws LiveAttributeException;

}
