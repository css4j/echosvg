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
package io.sf.carte.echosvg.css.engine.value.css2;

import io.sf.carte.echosvg.css.dom.CSSValue.Type;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'font-stretch' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class FontStretchManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_ALL_VALUE, ValueConstants.ALL_VALUE);
		values.put(CSSConstants.CSS_CONDENSED_VALUE, ValueConstants.CONDENSED_VALUE);
		values.put(CSSConstants.CSS_EXPANDED_VALUE, ValueConstants.EXPANDED_VALUE);
		values.put(CSSConstants.CSS_EXTRA_CONDENSED_VALUE, ValueConstants.EXTRA_CONDENSED_VALUE);
		values.put(CSSConstants.CSS_EXTRA_EXPANDED_VALUE, ValueConstants.EXTRA_EXPANDED_VALUE);
		values.put(CSSConstants.CSS_NARROWER_VALUE, ValueConstants.NARROWER_VALUE);
		values.put(CSSConstants.CSS_NORMAL_VALUE, ValueConstants.NORMAL_VALUE);
		values.put(CSSConstants.CSS_SEMI_CONDENSED_VALUE, ValueConstants.SEMI_CONDENSED_VALUE);
		values.put(CSSConstants.CSS_SEMI_EXPANDED_VALUE, ValueConstants.SEMI_EXPANDED_VALUE);
		values.put(CSSConstants.CSS_ULTRA_CONDENSED_VALUE, ValueConstants.ULTRA_CONDENSED_VALUE);
		values.put(CSSConstants.CSS_ULTRA_EXPANDED_VALUE, ValueConstants.ULTRA_EXPANDED_VALUE);
		values.put(CSSConstants.CSS_WIDER_VALUE, ValueConstants.WIDER_VALUE);
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return true;
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
		return SVGTypes.TYPE_IDENT;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_STRETCH_PROPERTY;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.NORMAL_VALUE;
	}

	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value != null && value.getPrimitiveType() == Type.IDENT) {
			if (value.getIdentifierValue() == CSSConstants.CSS_NARROWER_VALUE) {
				sm.putParentRelative(idx, true);

				CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
				if (p == null) {
					return ValueConstants.SEMI_CONDENSED_VALUE;
				}
				Value v = engine.getComputedStyle(p, pseudo, idx);
				if (v != null && v.getPrimitiveType() == Type.IDENT) {
					String s = v.getIdentifierValue();
					if (s == CSSConstants.CSS_NORMAL_VALUE) {
						return ValueConstants.SEMI_CONDENSED_VALUE;
					}
					if (s == CSSConstants.CSS_CONDENSED_VALUE) {
						return ValueConstants.EXTRA_CONDENSED_VALUE;
					}
					if (s == CSSConstants.CSS_EXPANDED_VALUE) {
						return ValueConstants.SEMI_EXPANDED_VALUE;
					}
					if (s == CSSConstants.CSS_SEMI_EXPANDED_VALUE) {
						return ValueConstants.NORMAL_VALUE;
					}
					if (s == CSSConstants.CSS_SEMI_CONDENSED_VALUE) {
						return ValueConstants.CONDENSED_VALUE;
					}
					if (s == CSSConstants.CSS_EXTRA_CONDENSED_VALUE) {
						return ValueConstants.ULTRA_CONDENSED_VALUE;
					}
					if (s == CSSConstants.CSS_EXTRA_EXPANDED_VALUE) {
						return ValueConstants.EXPANDED_VALUE;
					}
					if (s == CSSConstants.CSS_ULTRA_CONDENSED_VALUE) {
						return ValueConstants.ULTRA_CONDENSED_VALUE;
					}
				}
				return ValueConstants.EXTRA_EXPANDED_VALUE;
			} else if (value.getIdentifierValue() == CSSConstants.CSS_WIDER_VALUE) {
				sm.putParentRelative(idx, true);

				CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
				if (p == null) {
					return ValueConstants.SEMI_CONDENSED_VALUE;
				}
				Value v = engine.getComputedStyle(p, pseudo, idx);
				if (v != null && v.getPrimitiveType() == Type.IDENT) {
					String s = v.getIdentifierValue();
					if (s == CSSConstants.CSS_NORMAL_VALUE) {
						return ValueConstants.SEMI_EXPANDED_VALUE;
					}
					if (s == CSSConstants.CSS_CONDENSED_VALUE) {
						return ValueConstants.SEMI_CONDENSED_VALUE;
					}
					if (s == CSSConstants.CSS_EXPANDED_VALUE) {
						return ValueConstants.EXTRA_EXPANDED_VALUE;
					}
					if (s == CSSConstants.CSS_SEMI_EXPANDED_VALUE) {
						return ValueConstants.EXPANDED_VALUE;
					}
					if (s == CSSConstants.CSS_SEMI_CONDENSED_VALUE) {
						return ValueConstants.NORMAL_VALUE;
					}
					if (s == CSSConstants.CSS_EXTRA_CONDENSED_VALUE) {
						return ValueConstants.CONDENSED_VALUE;
					}
					if (s == CSSConstants.CSS_EXTRA_EXPANDED_VALUE) {
						return ValueConstants.ULTRA_EXPANDED_VALUE;
					}
					if (s == CSSConstants.CSS_ULTRA_CONDENSED_VALUE) {
						return ValueConstants.EXTRA_CONDENSED_VALUE;
					}
				}
				return ValueConstants.ULTRA_EXPANDED_VALUE;
			}
		}
		return value;
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap getIdentifiers() {
		return values;
	}

}
