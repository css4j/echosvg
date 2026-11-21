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

import io.sf.carte.echosvg.css.engine.value.LengthManager;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * Generic horizontal length manager for a geometry property.
 */
public abstract class HorizontalLengthManager extends LengthManager {

	@Override
	public boolean isInheritedProperty() {
		return false;
	}

	@Override
	public boolean isAnimatableProperty() {
		return true;
	}

	@Override
	public boolean isAdditiveProperty() {
		return true;
	}

	@Override
	public int getPropertyType() {
		return SVGTypes.TYPE_LENGTH;
	}

	@Override
	public Value getDefaultValue() {
		return ValueConstants.NUMBER_0;
	}

	@Override
	protected int getOrientation() {
		return HORIZONTAL_ORIENTATION;
	}

}
