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
import org.w3c.dom.DOMException;

/**
 * This class represents a computed property value.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>. See Git history.
 * </p>
 * @version $Id$
 */
public class ComputedValue implements Value {

	private static final long serialVersionUID = 1L;

	/**
	 * The cascaded value.
	 */
	protected Value cascadedValue;

	/**
	 * The computed value.
	 */
	protected Value computedValue;

	/**
	 * Creates a new ComputedValue object.
	 * 
	 * @param cv The cascaded value.
	 */
	public ComputedValue(Value cv) {
		cascadedValue = cv;
	}

	/**
	 * Returns the computed value.
	 */
	public Value getComputedValue() {
		return computedValue;
	}

	/**
	 * Returns the cascaded value.
	 */
	public Value getCascadedValue() {
		return cascadedValue;
	}

	/**
	 * Sets the computed value.
	 */
	public void setComputedValue(Value v) {
		computedValue = v;
	}

	/**
	 * Implements {@link Value#getCssText()}.
	 */
	@Override
	public String getCssText() {
		return computedValue.getCssText();
	}

	@Override
	public CssType getCssValueType() {
		return computedValue.getCssValueType();
	}


	@Override
	public Type getPrimitiveType() {
		return computedValue.getPrimitiveType();
	}

	@Override
	public short getUnitType() {
		return computedValue.getUnitType();
	}

	@Override
	public void setFloatValue(float value) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Cannot modify computed value.");
	}

	@Override
	public void setModificationHandler(ValueModificationHandler handler) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Cannot modify computed value.");
	}

	@Override
	public ValueModificationHandler getModificationHandler() {
		return computedValue.getModificationHandler();
	}

	@Override
	public float getFloatValue() throws DOMException {
		return computedValue.getFloatValue();
	}

	@Override
	public String getIdentifierValue() throws DOMException {
		return computedValue.getIdentifierValue();
	}

	@Override
	public boolean isIdentifier(String internedIdent) {
		return computedValue.isIdentifier(internedIdent);
	}

	/**
	 * Implements {@link Value#getStringValue()}.
	 */
	@Override
	public String getStringValue() throws DOMException {
		return computedValue.getStringValue();
	}

	@Override
	public String getURIValue() throws DOMException {
		return computedValue.getURIValue();
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Cannot modify computed value.");
	}

	@Override
	public int getLength() throws DOMException {
		return computedValue.getLength();
	}

	@Override
	public Value item(int index) throws DOMException {
		return computedValue.item(index);
	}

	@Override
	public Iterator<? extends Value> iterator() throws UnsupportedOperationException {
		return computedValue.iterator();
	}

	@Override
	public CSSCounterValue getCounterValue() throws DOMException {
		return computedValue.getCounterValue();
	}

	@Override
	public RectValue getRectValue() throws DOMException {
		return computedValue.getRectValue();
	}

	@Override
	public ColorValue getColorValue() throws DOMException {
		return computedValue.getColorValue();
	}

	@Override
	public String toString() {
		return computedValue.toString();
	}

	@Override
	public ComputedValue clone() {
		ComputedValue cv = new ComputedValue(cascadedValue);
		cv.setComputedValue(computedValue);
		return cv;
	}

}
