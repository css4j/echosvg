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

import java.util.Iterator;

import org.w3c.css.om.typed.CSSCounterValue;
import org.w3c.css.om.typed.CSSStyleValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

/**
 * This interface represents a property value.
 *
 * @author See Git history.
 * @version $Id$
 */
public interface Value extends CSSVal, CSSStyleValue, java.io.Serializable {

	/**
	 * Gets the css unit as in CSS4J's {@code CSSUnit}.
	 * <p>
	 * If the value has no valid CSS unit, returns {@code CSSUnit.CSS_INVALID}.
	 * </p>
	 * 
	 * @return the css unit as in CSS4J's {@code CSSUnit}.
	 */
	default short getUnitType() {
		return CSSUnit.CSS_INVALID;
	}

	/**
	 * Set the modification handler.
	 * 
	 * @param handler the modification handler.
	 */
	void setModificationHandler(ValueModificationHandler handler);

	/**
	 * Get the modification handler.
	 * 
	 * @return the modification handler, or {@code null} if there is no handler.
	 */
	ValueModificationHandler getModificationHandler();

	/**
	 * Is this value a component?
	 * 
	 * @return {@code true} if the value is a component.
	 */
	default boolean isComponent() {
		return false;
	}

	/**
	 * Do this value represent the given identifier?
	 * 
	 * @param internedIdent the interned identifier string.
	 * @return {@code true} if the value is a component.
	 */
	default boolean isIdentifier(String internedIdent) {
		return false;
	}

	/**
	 * Convenience method that either returns an identifier or throws an exception.
	 * 
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain an identifier value.
	 * @Deprecated use {@link #getgetIdentifierValue()} instead.
	 */
	@Deprecated
	default String getIdentifier() throws DOMException {
		return getIdentifierValue();
	}

	/**
	 * If this value is a unit value, set the float value.
	 * 
	 * @param value the new value, in the current unit.
	 * @throws DOMException if the value is not a unit value.
	 */
	void setFloatValue(float value) throws DOMException;

	/**
	 * If this value is a list, give the item corresponding to the requested index.
	 * If there is no item at such index, return {@code null} If this object is not
	 * a list and the index is {@code 0}, return itself.
	 * 
	 * @param index the index on the list.
	 * @return the item, or {@code null} if there is no item on that index.
	 */
	@Override
	default Value item(int index) {
		return index == 0 ? this : null;
	}

	/**
	 * If this value is a list, return an iterator.
	 * 
	 * @return the iterator.
	 * @throws UnsupportedOperationException if this value is not a list.
	 */
	default Iterator<? extends Value> iterator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * If this value is a counter() or counters() value, get it.
	 * 
	 * @return the counter() value.
	 * @throws DOMException INVALID_ACCESS_ERR if this is not a counter() or
	 *                      counters() value.
	 */
	CSSCounterValue getCounterValue() throws DOMException;

	/**
	 * If this value is a rect() value, get it.
	 * 
	 * @return the rect() value.
	 * @throws DOMException INVALID_ACCESS_ERR if this is not a rect() value.
	 */
	RectValue getRectValue() throws DOMException;

	/**
	 * If this value is a color value, get it.
	 * 
	 * @return the color value.
	 * @throws DOMException INVALID_ACCESS_ERR if this is not a color value.
	 */
	ColorValue getColorValue() throws DOMException;

	/**
	 * Create and return a copy of this object.
	 * <p>
	 * If this object is unmodifiable, the clone will be modifiable.
	 * </p>
	 * 
	 * @return a modifiable copy of this object.
	 */
	@Override
	Value clone();

}
