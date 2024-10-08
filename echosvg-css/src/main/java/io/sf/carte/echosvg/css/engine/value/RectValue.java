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

import org.w3c.api.DOMSyntaxException;
import org.w3c.css.om.typed.CSSRectValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

/**
 * This class represents CSS rect values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class RectValue extends ComponentValue implements CSSRectValue {

	/**
	 * The top value.
	 */
	protected Value top;

	/**
	 * The right value.
	 */
	protected Value right;

	/**
	 * The bottom value.
	 */
	protected Value bottom;

	/**
	 * The left value.
	 */
	protected Value left;

	/**
	 * Creates a new Rect value.
	 */
	public RectValue(Value t, Value r, Value b, Value l) {
		setTop(t);
		setRight(r);
		setBottom(b);
		setLeft(l);
	}

	/**
	 * The type of the value.
	 */
	@Override
	public Type getPrimitiveType() {
		return Type.RECT;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return "rect(" + top.getCssText() + ", " + right.getCssText() + ", " + bottom.getCssText() + ", "
				+ left.getCssText() + ')';
	}

	@Override
	public Value getTop() throws DOMException {
		return top;
	}

	@Override
	public Value getRight() throws DOMException {
		return right;
	}

	@Override
	public Value getBottom() throws DOMException {
		return bottom;
	}

	@Override
	public Value getLeft() throws DOMException {
		return left;
	}

	void setTop(Value top) {
		this.top = component(top);
	}

	/**
	 * Initialize a component of this value.
	 * 
	 * @param c the component.
	 * @return the initialized component.
	 * @throws DOMSyntaxException if the value is inadequate for a component.
	 */
	Value component(Value c) throws DOMSyntaxException {
		short unit = c.getUnitType();
		if (unit != CSSUnit.CSS_PERCENTAGE && unit != CSSUnit.CSS_NUMBER
				&& !CSSUnit.isLengthUnitType(unit) && c.getPrimitiveType() != Type.IDENT) {
			throw new DOMSyntaxException("rect() component must be a length or percentage.");
		}

		if (c.getModificationHandler() != null) {
			c = ((AbstractValue) c).clone();
		}
		componentize(c);

		return c;
	}

	void setRight(Value right) {
		this.right = component(right);
	}

	void setBottom(Value bottom) {
		this.bottom = component(bottom);
	}

	void setLeft(Value left) {
		this.left = component(left);
	}

	@Override
	public RectValue getRectValue() throws DOMException {
		return this;
	}

	@Override
	public int getLength() throws DOMException {
		return 4;
	}

	@Override
	public Value item(int index) throws DOMException {
		switch (index) {
		case 0:
			return getTop();
		case 1:
			return getRight();
		case 2:
			return getBottom();
		case 3:
			return getLeft();
		default:
			return null;
		}
	}

	@Override
	public RectValue clone() {
		RectValue clon = new RectValue(top, right, bottom, left);
		return clon;
	}

}
