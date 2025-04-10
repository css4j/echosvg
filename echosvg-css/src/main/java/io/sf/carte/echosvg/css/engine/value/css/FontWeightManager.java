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
package io.sf.carte.echosvg.css.engine.value.css;

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSContext;
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
 * This class provides a manager for the 'font-weight' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class FontWeightManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap<Value> values = new StringMap<>(7);
	static {
		values.put(CSSConstants.CSS_ALL_VALUE, ValueConstants.ALL_VALUE);
		values.put(CSSConstants.CSS_BOLD_VALUE, ValueConstants.BOLD_VALUE);
		values.put(CSSConstants.CSS_BOLDER_VALUE, ValueConstants.BOLDER_VALUE);
		values.put(CSSConstants.CSS_LIGHTER_VALUE, ValueConstants.LIGHTER_VALUE);
		values.put(CSSConstants.CSS_NORMAL_VALUE, ValueConstants.NORMAL_VALUE);
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
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
		return SVGTypes.TYPE_FONT_WEIGHT_VALUE;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_WEIGHT_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.NORMAL_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.INTEGER) {
			int i = lu.getIntegerValue();
			switch (i) {
			case 100:
				return ValueConstants.NUMBER_100;
			case 200:
				return ValueConstants.NUMBER_200;
			case 300:
				return ValueConstants.NUMBER_300;
			case 400:
				return ValueConstants.NUMBER_400;
			case 500:
				return ValueConstants.NUMBER_500;
			case 600:
				return ValueConstants.NUMBER_600;
			case 700:
				return ValueConstants.NUMBER_700;
			case 800:
				return ValueConstants.NUMBER_800;
			case 900:
				return ValueConstants.NUMBER_900;
			}
			throw createInvalidFloatValueDOMException(i);
		}
		return super.createValue(lu, engine);
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value != null && value.getPrimitiveType() == Type.IDENT) {
			if (value.getIdentifierValue() == CSSConstants.CSS_BOLDER_VALUE) {
				sm.putParentRelative(idx, true);

				CSSContext ctx = engine.getCSSContext();
				CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
				float fw;
				if (p == null) {
					fw = 400;
				} else {
					Value v = engine.getComputedStyle(p, pseudo, idx);
					fw = lengthValue(v);
				}
				return createFontWeight(ctx.getBolderFontWeight(fw));
			} else if (value.getIdentifierValue() == CSSConstants.CSS_LIGHTER_VALUE) {
				sm.putParentRelative(idx, true);

				CSSContext ctx = engine.getCSSContext();
				CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
				float fw;
				if (p == null) {
					fw = 400;
				} else {
					Value v = engine.getComputedStyle(p, pseudo, idx);
					fw = lengthValue(v);
				}
				return createFontWeight(ctx.getLighterFontWeight(fw));
			} else if (value.getIdentifierValue() == CSSConstants.CSS_NORMAL_VALUE) {
				return ValueConstants.NUMBER_400;
			} else if (value.getIdentifierValue() == CSSConstants.CSS_BOLD_VALUE) {
				return ValueConstants.NUMBER_700;
			}
		}
		return value;
	}

	/**
	 * Returns the CSS value associated with the given font-weight.
	 */
	protected Value createFontWeight(float f) {
		switch ((int) f) {
		case 100:
			return ValueConstants.NUMBER_100;
		case 200:
			return ValueConstants.NUMBER_200;
		case 300:
			return ValueConstants.NUMBER_300;
		case 400:
			return ValueConstants.NUMBER_400;
		case 500:
			return ValueConstants.NUMBER_500;
		case 600:
			return ValueConstants.NUMBER_600;
		case 700:
			return ValueConstants.NUMBER_700;
		case 800:
			return ValueConstants.NUMBER_800;
		default: // 900
			return ValueConstants.NUMBER_900;
		}
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap<Value> getIdentifiers() {
		return values;
	}

}
