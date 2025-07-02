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

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSEngineUserAgent;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the SVGPaint property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGPaintManager extends SVGColorManager {

	/**
	 * Creates a new SVGPaintManager.
	 */
	public SVGPaintManager(String prop) {
		super(prop);
	}

	/**
	 * Creates a new SVGPaintManager.
	 * 
	 * @param prop The property name.
	 * @param v    The default value.
	 */
	public SVGPaintManager(String prop, Value v) {
		super(prop, v);
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
		return true;
	}

	/**
	 * Implements {@link ValueManager#getPropertyType()}.
	 */
	@Override
	public int getPropertyType() {
		return SVGTypes.TYPE_PAINT;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		Value uriVal = null;

		switch (lu.getLexicalUnitType()) {
		case IDENT:
			if (lu.getStringValue().equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
				return ValueConstants.NONE_VALUE;
			}
			// Fall through
		default:
			return super.createValue(lu, engine);

		case URI:
			uriVal = createURIValue(lu, engine);
			break;

		case VAR:
		case ATTR:
			return createLexicalValue(lu);
		}

		if (uriVal == null) {
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}

		lu = lu.getNextLexicalUnit();
		if (lu == null) {
			return uriVal;
		}

		ListValue result = new ListValue(' ');
		result.append(uriVal);

		if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.IDENT) {
			if (lu.getStringValue().equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
				result.append(ValueConstants.NONE_VALUE);
				return result;
			}
		}

		Value v = super.createValue(lu, engine);
		if (v instanceof ListValue) {
			for (int i = 0; i < v.getLength(); i++) {
				result.append(v.item(i));
			}
		} else {
			result.append(v);
		}

		return result;
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.isIdentifier(CSSConstants.CSS_NONE_VALUE)) {
			return value;
		}
		if (value.getCssValueType() == Value.CssType.LIST) {
			Value v = value.item(0);
			if (v.getPrimitiveType() == Value.Type.URI) {
				if (sm.isAttrTainted(idx)) {
					CSSEngineUserAgent ua = engine.getCSSEngineUserAgent();
					if (ua != null) {
						ua.warn("attr()-tainted value: " + value.getCssText());
					}
					return null;
				}
				v = value.item(1);
				if (v == null || v.isIdentifier(CSSConstants.CSS_NONE_VALUE)) {
					return value;
				}
				Value t = super.computeValue(elt, pseudo, engine, idx, sm, v);
				if (t != v) {
					ListValue result = new ListValue(' ');
					result.append(value.item(0));
					result.append(t);
					if (value.getLength() == 3) {
						result.append(value.item(1));
					}
					return result;
				}
				return value;
			}
		}
		return super.computeValue(elt, pseudo, engine, idx, sm, value);
	}

}
