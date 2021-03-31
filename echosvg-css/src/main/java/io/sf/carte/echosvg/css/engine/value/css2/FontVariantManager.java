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

import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'font-variant' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class FontVariantManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NORMAL_VALUE, ValueConstants.NORMAL_VALUE);
		values.put(CSSConstants.CSS_SMALL_CAPS_VALUE, ValueConstants.SMALL_CAPS_VALUE);
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
		return CSSConstants.CSS_FONT_VARIANT_PROPERTY;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.NORMAL_VALUE;
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap getIdentifiers() {
		return values;
	}
}
