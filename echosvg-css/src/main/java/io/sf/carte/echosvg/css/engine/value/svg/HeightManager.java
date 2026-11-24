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
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * Manager for the height property.
 */
public class HeightManager extends VerticalLengthManager {

	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_HEIGHT_PROPERTY;
	}

	@Override
	protected Value createIdentValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		if (!CSSConstants.CSS_AUTO_VALUE.equalsIgnoreCase(lu.getStringValue())) {
			return super.createIdentValue(lu, engine);
		}
		// The 'auto' value matches the default
		return getDefaultValue();
	}

	@Override
	public Value getDefaultValue() {
		// Depends on the element
		return null;
	}

}
