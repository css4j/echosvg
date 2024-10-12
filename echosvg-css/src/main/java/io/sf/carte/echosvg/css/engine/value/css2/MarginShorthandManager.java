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

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.AbstractValueFactory;
import io.sf.carte.echosvg.css.engine.value.CSSProxyValueException;
import io.sf.carte.echosvg.css.engine.value.PendingValue;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class represents an object which provide support for the 'margin'
 * shorthand property.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class MarginShorthandManager extends AbstractValueFactory implements ShorthandManager {

	public MarginShorthandManager() {
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_MARGIN_PROPERTY;
	}

	/**
	 * Implements {@link ShorthandManager#isAnimatableProperty()}.
	 */
	@Override
	public boolean isAnimatableProperty() {
		return true;
	}

	/**
	 * Implements {@link ShorthandManager#isAdditiveProperty()}.
	 */
	@Override
	public boolean isAdditiveProperty() {
		return false;
	}

	/**
	 * Implements
	 * {@link ShorthandManager#setValues(CSSEngine,ShorthandManager.PropertyHandler,LexicalUnit,boolean)}.
	 */
	@Override
	public void setValues(CSSEngine eng, ShorthandManager.PropertyHandler ph, final LexicalUnit lunit,
			boolean imp) throws DOMException {
		switch (lunit.getLexicalUnitType()) {
		case INHERIT:
			return;

		case UNSET:
		case REVERT:
		case INITIAL:
			// Set defaults
			LexicalUnit luZero = ValueConstants.ZERO_LEXICAL_UNIT;
			ph.property(CSSConstants.CSS_MARGIN_TOP_PROPERTY, luZero, imp);
			ph.property(CSSConstants.CSS_MARGIN_RIGHT_PROPERTY, luZero, imp);
			ph.property(CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY, luZero, imp);
			ph.property(CSSConstants.CSS_MARGIN_LEFT_PROPERTY, luZero, imp);
			break;

		case VAR:
		case ATTR:
			setPendingLonghands(eng, ph, lunit, imp);
			return;

		default:
			break;
		}

		LexicalUnit lu = lunit;
		LexicalUnit[] lus = new LexicalUnit[4];
		int cnt = 0;
		while (lu != null) {
			LexicalType luType = lu.getLexicalUnitType();
			if (luType == LexicalType.VAR || luType == LexicalType.ATTR) {
				setPendingLonghands(eng, ph, lunit, imp);
				return;
			}
			if (cnt == 4)
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			lus[cnt++] = lu;
			lu = lu.getNextLexicalUnit();
		}
		switch (cnt) {
		case 1:
			lus[3] = lus[2] = lus[1] = lus[0];
			break;
		case 2:
			lus[2] = lus[0];
			lus[3] = lus[1];
			break;
		case 3:
			lus[3] = lus[1];
			break;
		default:
		}

		try {
			ph.property(CSSConstants.CSS_MARGIN_TOP_PROPERTY, lus[0], imp);
			ph.property(CSSConstants.CSS_MARGIN_RIGHT_PROPERTY, lus[1], imp);
			ph.property(CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY, lus[2], imp);
			ph.property(CSSConstants.CSS_MARGIN_LEFT_PROPERTY, lus[3], imp);
		} catch (CSSProxyValueException e) {
			// Maybe a calc() with a var() inside
			setPendingLonghands(eng, ph, lunit, imp);
		}
	}

	private void setPendingLonghands(CSSEngine eng, PropertyHandler ph, LexicalUnit lunit, boolean imp) {
		PendingValue pending = new PendingValue(getPropertyName(), lunit);
		ph.pendingValue(CSSConstants.CSS_MARGIN_TOP_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_MARGIN_RIGHT_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_MARGIN_LEFT_PROPERTY, pending, imp);
	}

}
