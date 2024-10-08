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
package io.sf.carte.echosvg.css.engine.value.svg;

import java.util.Locale;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.LengthManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'baseline-shift' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class BaselineShiftManager extends LengthManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_BASELINE_VALUE, SVGValueConstants.BASELINE_VALUE);
		values.put(CSSConstants.CSS_SUB_VALUE, SVGValueConstants.SUB_VALUE);
		values.put(CSSConstants.CSS_SUPER_VALUE, SVGValueConstants.SUPER_VALUE);
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return false;
	}

	/**
	 * Implements {@link ValueManager#isAnimatableProperty()}.
	 */
	@Override
	public boolean isAnimatableProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#isAdditiveProperty()}.
	 */
	@Override
	public boolean isAdditiveProperty() {
		return false;
	}

	/**
	 * Implements {@link ValueManager#getPropertyType()}.
	 */
	@Override
	public int getPropertyType() {
		return SVGTypes.TYPE_BASELINE_SHIFT_VALUE;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_BASELINE_SHIFT_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return SVGValueConstants.BASELINE_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case IDENT:
			Object v = values.get(lu.getStringValue().toLowerCase(Locale.ROOT).intern());
			if (v == null) {
				throw createInvalidIdentifierDOMException(lu.getStringValue());
			}
			return (Value) v;

		case INHERIT:
			return ValueConstants.INHERIT_VALUE;

		default:
			break;
		}

		return super.createValue(lu, engine);
	}

	@Override
	public Value createStringValue(Type type, String value, CSSEngine engine) throws DOMException {
		if (type != Type.IDENT) {
			throw createInvalidStringTypeDOMException(type);
		}
		Object v = values.get(value.toLowerCase(Locale.ROOT).intern());
		if (v == null) {
			throw createInvalidIdentifierDOMException(value);
		}
		return (Value) v;
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.getUnitType() == CSSUnit.CSS_PERCENTAGE) {
			sm.putLineHeightRelative(idx, true);

			int fsi = engine.getLineHeightIndex();
			CSSStylableElement parent;
			parent = (CSSStylableElement) elt.getParentNode();
			if (parent == null) {
				// Hmmm somthing pretty odd - can't happen accordint to spec,
				// should always have text parent.
				// http://www.w3.org/TR/SVG11/text.html#BaselineShiftProperty
				parent = elt;
			}
			Value fs = engine.getComputedStyle(parent, pseudo, fsi);
			float fsv = lengthValue(fs);
			float v = value.getFloatValue();
			return new FloatValue(CSSUnit.CSS_NUMBER, (fsv * v) / 100f);
		}
		return super.computeValue(elt, pseudo, engine, idx, sm, value);
	}

	/**
	 * Indicates the orientation of the property associated with this manager.
	 */
	@Override
	protected int getOrientation() {
		return BOTH_ORIENTATION; // Not used
	}

}
