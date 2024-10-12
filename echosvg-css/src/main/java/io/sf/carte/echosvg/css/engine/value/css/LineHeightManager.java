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
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a factory for the 'line-height' property values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class LineHeightManager extends LengthManager {

	public LineHeightManager() {
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
		return SVGTypes.TYPE_LINE_HEIGHT_VALUE;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_LINE_HEIGHT_PROPERTY;
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

		switch (lu.getLexicalUnitType()) {
		case IDENT: {
			String s = lu.getStringValue().toLowerCase(Locale.ROOT);
			if (CSSConstants.CSS_NORMAL_VALUE.equals(s))
				return ValueConstants.NORMAL_VALUE;
			throw createInvalidIdentifierDOMException(lu.getStringValue());
		}

		case INHERIT:
			return ValueConstants.INHERIT_VALUE;

		default:
			return super.createValue(lu, engine);
		}
	}

	/**
	 * Indicates the orientation of the property associated with this manager.
	 */
	@Override
	protected int getOrientation() {
		// Not really used.
		return VERTICAL_ORIENTATION;
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {
		if (value.getCssValueType() != Value.CssType.TYPED)
			return value;

		if (value.getPrimitiveType() == Type.IDENT) {
			// Can only be 'normal'
			return new FloatValue(CSSUnit.CSS_NUMBER, DEFAULT_LINE_HEIGHT);
		}

		switch (value.getUnitType()) {
		case CSSUnit.CSS_NUMBER:
			// The computed value of a <number> line-height is itself
			return value;

		case CSSUnit.CSS_PERCENTAGE:
			return fontSizeRelative(elt, pseudo, engine, sm, value.getFloatValue() * 0.01f);

		case CSSUnit.CSS_LH:
			float f = value.getFloatValue();
			CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
			if (p != null) {
				int lhidx = engine.getLineHeightIndex();
				Value v = engine.getComputedStyle(p, null, lhidx);
				float lh = v.getFloatValue();
				return new FloatValue(v.getUnitType(), f * lh);
			} else {
				return new FloatValue(CSSUnit.CSS_NUMBER, f * DEFAULT_LINE_HEIGHT);
			}

		case CSSUnit.CSS_RLH:
			f = value.getFloatValue();
			CSSStylableElement root = (CSSStylableElement) elt.getOwnerDocument().getDocumentElement();
			if (root != elt) {
				int lhidx = engine.getLineHeightIndex();
				Value v = engine.getComputedStyle(root, null, lhidx);
				float lh = v.getFloatValue();
				return new FloatValue(v.getUnitType(), f * lh);
			} else {
				return new FloatValue(CSSUnit.CSS_NUMBER, f * DEFAULT_LINE_HEIGHT);
			}

		default:
			return super.computeValue(elt, pseudo, engine, idx, sm, value);
		}
	}

	private Value fontSizeRelative(CSSStylableElement elt, String pseudo, CSSEngine engine, StyleMap sm,
			float lineHeight) {
		int fsidx = engine.getFontSizeIndex();
		Value v = engine.getComputedStyle(elt, pseudo, fsidx);
		float fs = v.getFloatValue();
		return new FloatValue(CSSUnit.CSS_PX, lineHeight * fs);
	}

}
