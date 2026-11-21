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

import org.w3c.css.om.unit.CSSUnit;

import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;

/**
 * Implementation of a geometry property.
 * <p>
 * Unless the {@link #nonNegativeCheck(AbstractSVGLength)} and
 * {@link #nonNegativeCheck(float)} methods are overridden, negative lengths are
 * not allowed.
 * </p>
 *
 * @version $Id$
 */
class SVGOMGeometryProperty extends SVGOMAnimatedLength implements SVGGeometryProperty {

	private final int pty_idx;

	/**
	 * Creates a new SVGOMGeometryProperty.
	 * 
	 * @param elt     The associated element.
	 * @param ns      The attribute's namespace URI.
	 * @param ln      The attribute's local name.
	 * @param def     The default value if the attribute is not specified.
	 * @param dir     The length's direction.
	 * @param pty_idx The property index.
	 */
	public SVGOMGeometryProperty(AbstractElement elt, String ns, String ln, String def, short dir,
			int pty_idx) {
		super(elt, ns, ln, def, dir);
		this.pty_idx = pty_idx;
	}

	/**
	 * Get the current animated float computed value, applying defaults if the value
	 * was invalid or not set.
	 * 
	 * @param sm the style map that applies.
	 * @return the float computed value.
	 */
	@Override
	public float getAnimatedComputedValue(StyleMap sm) {
		if (hasAnimVal) {
			if (animVal == null) {
				animVal = new AnimSVGLength(getDirection());
			}
			try {
				return nonNegativeCheck(animVal);
			} catch (LiveAttributeException e) {
				reportError(e);
			}
		}
		Value v = computeValue(sm);
		if (v != null && v.getUnitType() != CSSUnit.CSS_INVALID) {
			float f = v.getFloatValue();
			return nonNegativeCheck(f);
		}
		return safeCheckedValue();
	}

	/**
	 * Compute the animated value of this property, or its base value according to
	 * the given style map.
	 * <p>
	 * If no value is set, this method does not attempt to apply any default.
	 * </p>
	 * 
	 * @param sm the style map that applies.
	 * @return the computed value, or {@code null} if it is not animated, or no
	 *         value was set or it is invalid.
	 */
	@Override
	public Value computeAnimatedValue(StyleMap sm) {
		if (hasAnimVal) {
			if (animVal == null) {
				animVal = new AnimSVGLength(getDirection());
			}
			try {
				float f = nonNegativeCheck(animVal);
				return new FloatValue(CSSUnit.CSS_NUMBER, f);
			} catch (LiveAttributeException e) {
				reportError(e);
			}
		}
		return computeValue(sm);
	}

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
	@Override
	public Value computeValue(StyleMap sm) {
		Value v = sm.getValue(pty_idx);
		short unit;
		if (v != null && (unit = v.getUnitType()) != CSSUnit.CSS_NUMBER && unit != CSSUnit.CSS_PX) {
			CSSEngine eng = ((SVGOMDocument) element.getOwnerDocument()).getCSSEngine();
			ValueManager vm = eng.getValueManagers()[pty_idx];
			v = vm.computeValue((CSSStylableElement) element, null, eng, pty_idx, sm, v);
		}

		if (v == null) {
			v = ((CSSStylableElement) element).getInitialValue(pty_idx);
			if (v != null) {
				// Maybe it is a relative unit
				sm.putValue(pty_idx, v);
				v = computeValue(sm);
			}
		}

		return v;
	}

	float nonNegativeCheck(float f) {
		if (f < 0f) {
			// Dimensions could come from calc(), so allow some tolerance
			if (f > -TOLERANCE) {
				f = 0f;
			} else {
				// Only here the exception is reported instead of just thrown
				LiveAttributeException ex = new LiveAttributeException(element, localName,
						LiveAttributeException.ERR_ATTRIBUTE_NEGATIVE, Float.toString(f));
				reportError(ex);
				f = safeCheckedValue();
			}
		}
		return f;
	}

	private float safeCheckedValue() {
		float f;
		try {
			f = getCheckedValue();
		} catch (LiveAttributeException e) {
			f = getDefault();
		}
		return f;
	}

}
