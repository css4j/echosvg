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
package io.sf.carte.echosvg.script.rhino;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.PatternSyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Test based on commits eada57c716 and 19192fa8b9 by S. Steiner
 */
public class RhinoClassShutterTest {

	RhinoClassShutter shutter;

	@BeforeEach
	public void setUpBeforeEach() {
		shutter = new RhinoClassShutter();
	}

	@Test
	public void testShutter() {
		assertFalse(shutter.visibleToScripts("org.mozilla.javascript.Context"));
		assertFalse(shutter.visibleToScripts("org.mozilla.javascript"));
		assertFalse(shutter.visibleToScripts("io.sf.carte.echosvg.apps.ttf2svg.Main"));
		assertFalse(shutter.visibleToScripts("io.sf.carte.echosvg.script.rhino.RhinoClassShutter"));
		assertTrue(shutter.visibleToScripts("io.sf.carte.echosvg.dom.SVGOMDocument"));
		assertTrue(shutter.visibleToScripts("org.w3c.dom.Document"));
		assertTrue(shutter.visibleToScripts(
				"io.sf.carte.echosvg.bridge.ScriptingEnvironment$Window$IntervalScriptTimerTask"));
		assertTrue(shutter.visibleToScripts(
				"io.sf.carte.echosvg.bridge.ScriptingEnvironment$Window$IntervalRunnableTimerTask"));
		assertTrue(shutter.visibleToScripts(
				"io.sf.carte.echosvg.bridge.ScriptingEnvironment$Window$TimeoutScriptTimerTask"));
		assertTrue(shutter.visibleToScripts(
				"io.sf.carte.echosvg.bridge.ScriptingEnvironment$Window$TimeoutRunnableTimerTask"));
		assertFalse(shutter
				.visibleToScripts("io.sf.carte.echosvg.bridge.ScriptingEnvironment$Window$Foo"));
		assertFalse(shutter.visibleToScripts("io.sf.carte.echosvg.bridge.ScriptingEnvironment"));
		assertFalse(
				shutter.visibleToScripts("io.sf.carte.echosvg.bridge.BaseScriptingEnvironment"));
		assertFalse(shutter.visibleToScripts("io.sf.carte.echosvg.bridge.BridgeContext"));
		assertFalse(shutter.visibleToScripts("io.sf.carte.echosvg.script.rhino.RhinoInterpreter"));
		assertFalse(
				shutter.visibleToScripts("io.sf.carte.echosvg.apps.svgbrowser.JSVGViewerFrame"));
	}

	@Test
	public void testWhitelist() {
		String fqcn = "io.sf.carte.chart.ChartRenderer";
		assertFalse(shutter.visibleToScripts(fqcn));
		RhinoClassShutter.addToWhitelist(fqcn);
		assertTrue(shutter.visibleToScripts(fqcn));
		RhinoClassShutter.removeFromWhitelist(fqcn);
		assertFalse(shutter.visibleToScripts(fqcn));

		// Removing again has no effect
		RhinoClassShutter.removeFromWhitelist(fqcn);
		assertFalse(shutter.visibleToScripts(fqcn));
	}

	@Test
	public void testNullEmptyEntry() {
		// Empty entry
		RhinoClassShutter.addToWhitelist(null);
		// Empty entry
		RhinoClassShutter.addToWhitelist(" ");
	}

	@Test
	public void testNoScriptingEnvironment() {
		String fqcn = "io.sf.carte.echosvg.bridge.ScriptingEnvironment";
		assertFalse(shutter.visibleToScripts(fqcn));
		RhinoClassShutter.addToWhitelist(fqcn);
		assertFalse(shutter.visibleToScripts(fqcn));
	}

	@Test
	public void testNoBaseScriptingEnvironment() {
		String fqcn = "io.sf.carte.echosvg.bridge.BaseScriptingEnvironment";
		assertFalse(shutter.visibleToScripts(fqcn));
		RhinoClassShutter.addToWhitelist(fqcn);
		assertFalse(shutter.visibleToScripts(fqcn));
	}

	@Test
	public void testLoadWhitelistReader() throws IOException, PatternSyntaxException {
		String s = "+io.sf.carte.echosvg.test.svg.*\n" + "+io.sf.carte.echosvg.test.image.*\n"
				+ "io.sf.carte.echosvg.swing.test.*\n" + "io.sf.carte.chart.ChartInfo\n"
				+ "-io.sf.carte.chart.ChartInfo\n" + "#io.sf.carte.chart.ChartInfo\n";
		StringReader re = new StringReader(s);
		RhinoClassShutter.loadWhitelist(re);

		assertTrue(shutter
				.visibleToScripts("io.sf.carte.echosvg.test.svg.SelfContainedSVGOnLoadTest"));
		assertTrue(shutter.visibleToScripts("io.sf.carte.echosvg.test.image.ImageComparatorTest"));
		assertTrue(shutter.visibleToScripts("io.sf.carte.echosvg.swing.test.SetSVGDocumentTest"));
		assertFalse(shutter.visibleToScripts("io.sf.carte.chart.ChartInfo"));
	}

}
