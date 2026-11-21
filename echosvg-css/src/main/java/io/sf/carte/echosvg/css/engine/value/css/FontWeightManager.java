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

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
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
			return createFontWeight(lu.getIntegerValue());
		}
		return super.createValue(lu, engine);
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx,
			StyleMap sm, Value value) {
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
				try {
					return createFontWeight((int) ctx.getBolderFontWeight(fw));
				} catch (DOMException e) {
					reportError(engine, e);
					return null;
				}
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
				try {
					return createFontWeight((int) ctx.getLighterFontWeight(fw));
				} catch (DOMException e) {
					reportError(engine, e);
					return null;
				}
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
	protected Value createFontWeight(int i) throws DOMException {
		Value v;
		switch (i) {
		case 100:
			v = ValueConstants.NUMBER_100;
			break;
		case 200:
			v = ValueConstants.NUMBER_200;
			break;
		case 300:
			v = ValueConstants.NUMBER_300;
			break;
		case 400:
			v = ValueConstants.NUMBER_400;
			break;
		case 500:
			v = ValueConstants.NUMBER_500;
			break;
		case 600:
			v = ValueConstants.NUMBER_600;
			break;
		case 700:
			v = ValueConstants.NUMBER_700;
			break;
		case 800:
			v = ValueConstants.NUMBER_800;
			break;
		case 900:
			v = ValueConstants.NUMBER_900;
			break;
		default:
			int fw = ((i + 99) / 100) * 100 - (i < 400 ? 100 : 0);
			if (fw <= 100) {
				if (i < 1) {
					throw createInvalidFloatValueDOMException(i);
				}
				v = ValueConstants.NUMBER_100;
			} else if (fw >= 900) {
				if (i > 1000) {
					throw createInvalidFloatValueDOMException(i);
				}
				v = ValueConstants.NUMBER_900;
			} else {
				v = new FloatValue(CSSUnit.CSS_NUMBER, fw);
			}
		}
		return v;
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap<Value> getIdentifiers() {
		return values;
	}

}
