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
import org.w3c.dom.css.CSSValue;

/**
 * This class provides an abstract implementation of the Value interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractValue implements Value {

	/**
	 * Implements {@link Value#getCssValueType()}.
	 */
	@Override
	public short getCssValueType() {
		return CSSValue.CSS_PRIMITIVE_VALUE;
	}

	/**
	 * Implements {@link Value#getPrimitiveType()}.
	 */
	@Override
	public short getPrimitiveType() {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getFloatValue()}.
	 */
	@Override
	public float getFloatValue() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getStringValue()}.
	 */
	@Override
	public String getStringValue() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getRed()}.
	 */
	@Override
	public Value getRed() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getGreen()}.
	 */
	@Override
	public Value getGreen() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getBlue()}.
	 */
	@Override
	public Value getBlue() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getAlpha()}.
	 */
	@Override
	public Value getAlpha() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getLength()}.
	 */
	@Override
	public int getLength() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#item(int)}.
	 */
	@Override
	public Value item(int index) throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getTop()}.
	 */
	@Override
	public Value getTop() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getRight()}.
	 */
	@Override
	public Value getRight() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getBottom()}.
	 */
	@Override
	public Value getBottom() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getLeft()}.
	 */
	@Override
	public Value getLeft() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getIdentifier()}.
	 */
	@Override
	public String getIdentifier() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getListStyle()}.
	 */
	@Override
	public String getListStyle() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link Value#getSeparator()}.
	 */
	@Override
	public String getSeparator() throws DOMException {
		throw createDOMException();
	}

	/**
	 * Creates an INVALID_ACCESS_ERR exception.
	 */
	protected DOMException createDOMException() {
		Object[] p = { (int) getCssValueType() };
		String s = Messages.formatMessage("invalid.value.access", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}
}
