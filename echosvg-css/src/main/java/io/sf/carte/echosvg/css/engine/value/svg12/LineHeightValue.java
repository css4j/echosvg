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

import io.sf.carte.echosvg.css.engine.value.FloatValue;

/**
 * This class represents line-height values. These are basically FloatValues
 * except that it may be 'font-size' relative.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class LineHeightValue extends FloatValue {

	/**
	 * True if the line-height is relative to the font-size
	 */
	protected boolean fontSizeRelative;

	/**
	 * Creates a new value.
	 */
	public LineHeightValue(short unitType, float floatValue, boolean fontSizeRelative) {
		super(unitType, floatValue);
		this.fontSizeRelative = fontSizeRelative;
	}

	/**
	 * The type of the value.
	 */
	public boolean getFontSizeRelative() {
		return fontSizeRelative;
	}

}
