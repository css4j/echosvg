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

import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.AbstractValueFactory;
import io.sf.carte.echosvg.css.engine.value.PendingValue;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class represents an object which provide support for the 'marker'
 * shorthand properties.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class MarkerShorthandManager extends AbstractValueFactory implements ShorthandManager {

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_MARKER_PROPERTY;
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
	public void setValues(CSSEngine eng, ShorthandManager.PropertyHandler ph, LexicalUnit lu, boolean imp)
			throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case VAR:
			setPendingLonghands(eng, ph, lu, imp);
			/* fall-through */
		case INHERIT:
		case UNSET:
			return;
		case INITIAL:
			// none
			try {
				lu = new CSSParser().parsePropertyValue(new StringReader(CSSConstants.CSS_NONE_VALUE));
			} catch (CSSParseException | IOException e) {
			}
		default:
			break;
		}

		ph.property(CSSConstants.CSS_MARKER_END_PROPERTY, lu, imp);
		ph.property(CSSConstants.CSS_MARKER_MID_PROPERTY, lu, imp);
		ph.property(CSSConstants.CSS_MARKER_START_PROPERTY, lu, imp);
	}

	private void setPendingLonghands(CSSEngine eng, PropertyHandler ph, LexicalUnit lu, boolean imp) {
		PendingValue pending = new PendingValue(getPropertyName(), lu);
		ph.pendingValue(CSSConstants.CSS_MARKER_END_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_MARKER_MID_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_MARKER_START_PROPERTY, pending, imp);
	}

}
