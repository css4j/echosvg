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

import org.w3c.css.om.typed.CSSCounterValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

/**
 * This class provides an abstract implementation of the Value interface.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * </p>
 * @version $Id$
 */
public abstract class AbstractValue implements Value, Cloneable {

	ValueModificationHandler handler;

	@Override
	public CssType getCssValueType() {
		return Value.CssType.TYPED;
	}

	@Override
	public Type getPrimitiveType() {
		throw createDOMException();
	}

	@Override
	public short getCSSUnit() {
		return CSSUnit.CSS_INVALID;
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		throw createDOMException();
	}

	@Override
	public String toString() {
		return getCssText();
	}

	/**
	 * Implements {@link Value#getFloatValue()}.
	 */
	@Override
	public float getFloatValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public void setFloatValue(float value) throws DOMException {
		throw createDOMException();
	}

	@Override
	public String getStringValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public int getLength() throws DOMException {
		return 0;
	}

	@Override
	public String getIdentifierValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public String getURIValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public CSSCounterValue getCounterValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public RectValue getRectValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public ColorValue getColorValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public void setModificationHandler(ValueModificationHandler handler) {
		this.handler = handler;
	}

	@Override
	public ValueModificationHandler getModificationHandler() {
		return handler;
	}

	/**
	 * Creates an INVALID_ACCESS_ERR exception.
	 */
	protected DOMException createDOMException() {
		Object[] p = { getPrimitiveType() };
		String s = Messages.formatMessage("invalid.value.access", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	@Override
	public int hashCode() {
		return getPrimitiveType().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Value))
			return false;
		Value other = (Value) obj;
		return getPrimitiveType() == other.getPrimitiveType();
	}

	@Override
	public AbstractValue clone() {
		try {
			return (AbstractValue) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
