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

package io.sf.carte.echosvg.css.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * Test the SVG CSS Engine.
 */
public class SVGCSSEngineTest {

	@Test
	public void testPropertyIndexes() throws Exception {
		CSSEngine eng = TestCSSContext.createCSSEngine();

		assertEquals(CSSConstants.CSS_LINE_HEIGHT_PROPERTY, eng.getPropertyName(SVGCSSEngine.LINE_HEIGHT_INDEX));
		assertEquals(CSSConstants.CSS_FONT_SIZE_PROPERTY, eng.getPropertyName(SVGCSSEngine.FONT_SIZE_INDEX));
		assertEquals(CSSConstants.CSS_COLOR_PROPERTY, eng.getPropertyName(SVGCSSEngine.COLOR_INDEX));

		assertEquals(SVGCSSEngine.LINE_HEIGHT_INDEX, eng.getLineHeightIndex());
		assertEquals(SVGCSSEngine.FONT_SIZE_INDEX, eng.getFontSizeIndex());
		assertEquals(SVGCSSEngine.COLOR_INDEX, eng.getColorIndex());

		// Check for property name duplicates
		ValueManager[] vms = eng.getValueManagers();
		HashSet<String> pnames = new HashSet<>(vms.length);
		for (ValueManager vm : vms) {
			String s = vm.getPropertyName();
			if (!pnames.add(s)) {
				fail("Duplicate property name: " + s);
			}
		}
	}

}
