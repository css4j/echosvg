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

import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'alignment-baseline' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AlignmentBaselineManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected static final StringMap<Value> values = new StringMap<>(12);
	static {
		values.put(CSSConstants.CSS_AFTER_EDGE_VALUE, SVGValueConstants.AFTER_EDGE_VALUE);
		values.put(CSSConstants.CSS_ALPHABETIC_VALUE, SVGValueConstants.ALPHABETIC_VALUE);
		values.put(CSSConstants.CSS_AUTO_VALUE, ValueConstants.AUTO_VALUE);
		values.put(CSSConstants.CSS_BASELINE_VALUE, SVGValueConstants.BASELINE_VALUE);
		values.put(CSSConstants.CSS_BEFORE_EDGE_VALUE, SVGValueConstants.BEFORE_EDGE_VALUE);
		values.put(CSSConstants.CSS_HANGING_VALUE, SVGValueConstants.HANGING_VALUE);
		values.put(CSSConstants.CSS_IDEOGRAPHIC_VALUE, SVGValueConstants.IDEOGRAPHIC_VALUE);
		values.put(CSSConstants.CSS_MATHEMATICAL_VALUE, SVGValueConstants.MATHEMATICAL_VALUE);
		values.put(CSSConstants.CSS_MIDDLE_VALUE, SVGValueConstants.MIDDLE_VALUE);
		values.put(CSSConstants.CSS_TEXT_AFTER_EDGE_VALUE, SVGValueConstants.TEXT_AFTER_EDGE_VALUE);
		values.put(CSSConstants.CSS_TEXT_BEFORE_EDGE_VALUE, SVGValueConstants.TEXT_BEFORE_EDGE_VALUE);
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return false;
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
		return CSSConstants.CSS_ALIGNMENT_BASELINE_PROPERTY;
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return ValueConstants.AUTO_VALUE;
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap<Value> getIdentifiers() {
		return values;
	}

}
