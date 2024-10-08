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

import io.sf.carte.echosvg.css.engine.value.IdentValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.svg.SVGValueConstants;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVG12CSSConstants;

/**
 * This interface provides constants for SVG 1.2 values.
 *
 * <p>
 * Original author: <a href="mailto:deweese@apache.org">deweese</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public interface SVG12ValueConstants extends SVGValueConstants {

	/** The 'start' keyword. */
	Value START_VALUE = IdentValue.createConstant(CSSConstants.CSS_START_VALUE);
	/** The 'center' keyword. */
	Value CENTER_VALUE = IdentValue.createConstant(CSSConstants.CSS_CENTER_VALUE);
	/** The 'end' keyword. */
	Value END_VALUE = IdentValue.createConstant(CSSConstants.CSS_END_VALUE);
	/** The 'full' keyword. */
	Value FULL_VALUE = IdentValue.createConstant(SVG12CSSConstants.CSS_FULL_VALUE);

}
