/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.dom.DOMException;

/**
 * This class represents a computed property value.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ComputedValue implements Value {

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

	/**
	 * Implements {@link Value#getCssValueType()}.
	 */
	@Override
	public short getCssValueType() {
		return computedValue.getCssValueType();
	}

	/**
	 * Implements {@link Value#getPrimitiveType()}.
	 */
	@Override
	public short getPrimitiveType() {
		return computedValue.getPrimitiveType();
	}

	/**
	 * Implements {@link Value#getFloatValue()}.
	 */
	@Override
	public float getFloatValue() throws DOMException {
		return computedValue.getFloatValue();
	}

	/**
	 * Implements {@link Value#getStringValue()}.
	 */
	@Override
	public String getStringValue() throws DOMException {
		return computedValue.getStringValue();
	}

	/**
	 * Implements {@link Value#getRed()}.
	 */
	@Override
	public Value getRed() throws DOMException {
		return computedValue.getRed();
	}

	/**
	 * Implements {@link Value#getGreen()}.
	 */
	@Override
	public Value getGreen() throws DOMException {
		return computedValue.getGreen();
	}

	/**
	 * Implements {@link Value#getBlue()}.
	 */
	@Override
	public Value getBlue() throws DOMException {
		return computedValue.getBlue();
	}

	@Override
	public Value getAlpha() throws DOMException {
		return computedValue.getAlpha();
	}

	/**
	 * Implements {@link Value#getLength()}.
	 */
	@Override
	public int getLength() throws DOMException {
		return computedValue.getLength();
	}

	/**
	 * Implements {@link Value#item(int)}.
	 */
	@Override
	public Value item(int index) throws DOMException {
		return computedValue.item(index);
	}

	/**
	 * Implements {@link Value#getTop()}.
	 */
	@Override
	public Value getTop() throws DOMException {
		return computedValue.getTop();
	}

	/**
	 * Implements {@link Value#getRight()}.
	 */
	@Override
	public Value getRight() throws DOMException {
		return computedValue.getRight();
	}

	/**
	 * Implements {@link Value#getBottom()}.
	 */
	@Override
	public Value getBottom() throws DOMException {
		return computedValue.getBottom();
	}

	/**
	 * Implements {@link Value#getLeft()}.
	 */
	@Override
	public Value getLeft() throws DOMException {
		return computedValue.getLeft();
	}

	/**
	 * Implements {@link Value#getIdentifier()}.
	 */
	@Override
	public String getIdentifier() throws DOMException {
		return computedValue.getIdentifier();
	}

	/**
	 * Implements {@link Value#getListStyle()}.
	 */
	@Override
	public String getListStyle() throws DOMException {
		return computedValue.getListStyle();
	}

	/**
	 * Implements {@link Value#getSeparator()}.
	 */
	@Override
	public String getSeparator() throws DOMException {
		return computedValue.getSeparator();
	}
}
