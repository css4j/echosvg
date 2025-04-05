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
package io.sf.carte.echosvg.css.dom;

import org.w3c.css.om.typed.CSSCounterValue;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.property.NumberValue;
import io.sf.carte.echosvg.css.engine.value.ColorValue;
import io.sf.carte.echosvg.css.engine.value.RectValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueModificationHandler;

/**
 * This class implements the {@link org.w3c.css.om.typed.CSSStyleValue}
 * interface.
 * <p>
 * Original author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * </p>
 * 
 * @version $Id$
 */
public class CSSOMValue implements Value {

	private static final long serialVersionUID = 1L;

	/**
	 * The associated value.
	 */
	protected ValueProvider valueProvider;

	/**
	 * The modifications handler.
	 */
	protected ValueModificationHandler handler;

	/**
	 * Creates a new CSSOMValue.
	 */
	public CSSOMValue(ValueProvider vp) {
		valueProvider = vp;
	}

	/**
	 * Sets the modification handler of this value.
	 */
	@Override
	public void setModificationHandler(ValueModificationHandler h) {
		handler = h;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.css.CSSValue#getCssText()}.
	 */
	@Override
	public String getCssText() {
		return valueProvider.getValue().getCssText();
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		if (handler == null) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
		} else {
			valueProvider.getValue().setCssText(cssText);
			handler.valueChanged(valueProvider.getValue());
		}
	}

	@Override
	public CssType getCssValueType() {
		return valueProvider.getValue().getCssValueType();
	}

	@Override
	public Type getPrimitiveType() {
		return valueProvider.getValue().getPrimitiveType();
	}

	@Override
	public void setFloatValue(float floatValue) throws DOMException {
		if (handler == null) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
		} else {
			handler.valueChanged(this);
		}
	}

	@Override
	public float getFloatValue() throws DOMException {
		return valueProvider.getValue().getFloatValue();
	}

	/**
	 * Converts the actual float value to the given unit type.
	 */
	public static float convertFloatValue(short unitType, Value value) {
		if (value.getUnitType() == unitType) {
			return value.getFloatValue();
		}
		switch (unitType) {
		case CSSUnit.CSS_NUMBER:
		case CSSUnit.CSS_PERCENTAGE:
		case CSSUnit.CSS_EM:
		case CSSUnit.CSS_EX:
		case CSSUnit.CSS_PX:
		case CSSUnit.CSS_REM:
		case CSSUnit.CSS_REX:
		case CSSUnit.CSS_LH:
		case CSSUnit.CSS_RLH:
		case CSSUnit.CSS_VW:
		case CSSUnit.CSS_VH:
		case CSSUnit.CSS_VMIN:
		case CSSUnit.CSS_VMAX:
		case CSSUnit.CSS_OTHER:
			break;
		case CSSUnit.CSS_CM:
			return toCentimeters(value);
		case CSSUnit.CSS_MM:
			return toMillimeters(value);
		case CSSUnit.CSS_IN:
			return toInches(value);
		case CSSUnit.CSS_PT:
			return toPoints(value);
		case CSSUnit.CSS_PC:
			return toPicas(value);
		case CSSUnit.CSS_DEG:
			return toDegrees(value);
		case CSSUnit.CSS_RAD:
			return toRadians(value);
		case CSSUnit.CSS_GRAD:
			return toGradians(value);
		case CSSUnit.CSS_MS:
			return toMilliseconds(value);
		case CSSUnit.CSS_S:
			return toSeconds(value);
		case CSSUnit.CSS_HZ:
			return toHertz(value);
		case CSSUnit.CSS_KHZ:
			return tokHertz(value);
		default:
			return NumberValue.floatValueConversion(value.getFloatValue(), value.getUnitType(),
					unitType);
		}
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
	}

	/**
	 * Converts the current value into centimeters.
	 */
	protected static float toCentimeters(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_CM:
			return value.getFloatValue();
		case CSSUnit.CSS_MM:
			return (value.getFloatValue() / 10);
		case CSSUnit.CSS_IN:
			return (value.getFloatValue() * 2.54f);
		case CSSUnit.CSS_PT:
			return (value.getFloatValue() * 2.54f / 72);
		case CSSUnit.CSS_PC:
			return (value.getFloatValue() * 2.54f / 6);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into inches.
	 */
	protected static float toInches(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_CM:
			return (value.getFloatValue() / 2.54f);
		case CSSUnit.CSS_MM:
			return (value.getFloatValue() / 25.4f);
		case CSSUnit.CSS_IN:
			return value.getFloatValue();
		case CSSUnit.CSS_PT:
			return (value.getFloatValue() / 72);
		case CSSUnit.CSS_PC:
			return (value.getFloatValue() / 6);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into millimeters.
	 */
	protected static float toMillimeters(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_CM:
			return (value.getFloatValue() * 10);
		case CSSUnit.CSS_MM:
			return value.getFloatValue();
		case CSSUnit.CSS_IN:
			return (value.getFloatValue() * 25.4f);
		case CSSUnit.CSS_PT:
			return (value.getFloatValue() * 25.4f / 72);
		case CSSUnit.CSS_PC:
			return (value.getFloatValue() * 25.4f / 6);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into points.
	 */
	protected static float toPoints(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_CM:
			return (value.getFloatValue() * 72 / 2.54f);
		case CSSUnit.CSS_MM:
			return (value.getFloatValue() * 72 / 25.4f);
		case CSSUnit.CSS_IN:
			return (value.getFloatValue() * 72);
		case CSSUnit.CSS_PT:
			return value.getFloatValue();
		case CSSUnit.CSS_PC:
			return (value.getFloatValue() * 12);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into picas.
	 */
	protected static float toPicas(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_CM:
			return (value.getFloatValue() * 6 / 2.54f);
		case CSSUnit.CSS_MM:
			return (value.getFloatValue() * 6 / 25.4f);
		case CSSUnit.CSS_IN:
			return (value.getFloatValue() * 6);
		case CSSUnit.CSS_PT:
			return (value.getFloatValue() / 12);
		case CSSUnit.CSS_PC:
			return value.getFloatValue();
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into degrees.
	 */
	protected static float toDegrees(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_DEG:
			return value.getFloatValue();
		case CSSUnit.CSS_RAD:
			return (float) Math.toDegrees(value.getFloatValue());
		case CSSUnit.CSS_GRAD:
			return (value.getFloatValue() * 9 / 5);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into radians.
	 */
	protected static float toRadians(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_DEG:
			return (value.getFloatValue() * 5 / 9); // todo ??
		case CSSUnit.CSS_RAD:
			return value.getFloatValue();
		case CSSUnit.CSS_GRAD:
			return (float) (value.getFloatValue() * 100 / Math.PI);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into gradians.
	 */
	protected static float toGradians(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_DEG:
			return (float) (value.getFloatValue() * Math.PI / 180); // todo ????
		case CSSUnit.CSS_RAD:
			return (float) (value.getFloatValue() * Math.PI / 100);
		case CSSUnit.CSS_GRAD:
			return value.getFloatValue();
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into milliseconds.
	 */
	protected static float toMilliseconds(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_MS:
			return value.getFloatValue();
		case CSSUnit.CSS_S:
			return (value.getFloatValue() * 1000);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into seconds.
	 */
	protected static float toSeconds(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_MS:
			return (value.getFloatValue() / 1000);
		case CSSUnit.CSS_S:
			return value.getFloatValue();
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into Hertz.
	 */
	protected static float toHertz(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_HZ:
			return value.getFloatValue();
		case CSSUnit.CSS_KHZ:
			return (value.getFloatValue() / 1000);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into kHertz.
	 */
	protected static float tokHertz(Value value) {
		switch (value.getUnitType()) {
		case CSSUnit.CSS_HZ:
			return (value.getFloatValue() * 1000);
		case CSSUnit.CSS_KHZ:
			return value.getFloatValue();
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	@Override
	public String getIdentifierValue() throws DOMException {
		return valueProvider.getValue().getIdentifierValue();
	}

	@Override
	public short getUnitType() {
		return valueProvider.getValue().getUnitType();
	}

	@Override
	public String getStringValue() throws DOMException {
		return valueProvider.getValue().getStringValue();
	}

	@Override
	public ValueModificationHandler getModificationHandler() {
		return handler;
	}

	@Override
	public String getURIValue() throws DOMException {
		return valueProvider.getValue().getIdentifierValue();
	}

	@Override
	public CSSCounterValue getCounterValue() throws DOMException {
		return valueProvider.getValue().getCounterValue();
	}

	@Override
	public RectValue getRectValue() throws DOMException {
		return valueProvider.getValue().getRectValue();
	}

	@Override
	public ColorValue getColorValue() throws DOMException {
		return valueProvider.getValue().getColorValue();
	}

	// CSSValueList ///////////////////////////////////////////////////////

	@Override
	public int getLength() {
		return valueProvider.getValue().getLength();
	}

	@Override
	public Value item(int index) {
		int len = valueProvider.getValue().getLength();
		if (index < 0 || index >= len) {
			return null;
		}
		Value result = valueProvider.getValue().item(index);
		if (result.getModificationHandler() == null) {
			result.setModificationHandler(handler);
		}
		return result;
	}

	// Counter /////////////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.css.Counter#getIdentifier()}.
	 */
	@Override
	public String getIdentifier() {
		return valueProvider.getValue().getIdentifierValue();
	}

	@Override
	public Value clone() {
		return valueProvider.getValue().clone();
	}

	/**
	 * To provide the actual value.
	 */
	public interface ValueProvider {

		/**
		 * Returns the current value associated with this object.
		 */
		Value getValue();

	}

}
