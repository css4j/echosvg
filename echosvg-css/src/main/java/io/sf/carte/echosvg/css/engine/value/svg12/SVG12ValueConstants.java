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

import org.w3c.dom.css.CSSPrimitiveValue;

import io.sf.carte.echosvg.css.engine.value.StringValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.svg.SVGValueConstants;
import io.sf.carte.echosvg.util.SVG12CSSConstants;

/**
 * This interface provides constants for SVG 1.2 values.
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface SVG12ValueConstants extends SVGValueConstants {

	/** The 'start' keyword. */
	Value START_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, SVG12CSSConstants.CSS_FULL_VALUE);
	/** The 'middle' keyword. */
	Value MIDDLE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, SVG12CSSConstants.CSS_MIDDLE_VALUE);
	/** The 'end' keyword. */
	Value END_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, SVG12CSSConstants.CSS_END_VALUE);
	/** The 'full' keyword. */
	Value FULL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, SVG12CSSConstants.CSS_FULL_VALUE);
	/** The 'normal' keyword, for 'line-height'. */
	Value NORMAL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, SVG12CSSConstants.CSS_NORMAL_VALUE);

}
