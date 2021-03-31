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

package io.sf.carte.echosvg.css.engine.value.svg12;

import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.SVG12CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'text-align' property values.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TextAlignManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap values = new StringMap();

	static {
		values.put(SVG12CSSConstants.CSS_START_VALUE, SVG12ValueConstants.START_VALUE);
		values.put(SVG12CSSConstants.CSS_MIDDLE_VALUE, SVG12ValueConstants.MIDDLE_VALUE);
		values.put(SVG12CSSConstants.CSS_END_VALUE, SVG12ValueConstants.END_VALUE);
		values.put(SVG12CSSConstants.CSS_FULL_VALUE, SVG12ValueConstants.FULL_VALUE);
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
		return SVG12CSSConstants.CSS_TEXT_ALIGN_PROPERTY;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.INHERIT_VALUE;
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap getIdentifiers() {
		return values;
	}
}
