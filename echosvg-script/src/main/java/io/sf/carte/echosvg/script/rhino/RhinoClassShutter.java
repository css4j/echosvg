/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.script.rhino;

import org.mozilla.javascript.ClassShutter;

/**
 * Class shutter that restricts access to EchoSVG internals from script.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RhinoClassShutter implements ClassShutter {

	/*
	 * public RhinoClassShutter() { // I suspect that we might want to initialize
	 * this // from a resource file. // test(); }
	 * 
	 * public void test() { test("org.mozilla.javascript.Context");
	 * test("org.mozilla.javascript");
	 * test("io.sf.carte.echosvg.dom.SVGOMDocument");
	 * test("io.sf.carte.echosvg.script.rhino.RhinoInterpreter");
	 * test("io.sf.carte.echosvg.apps.svgbrowser.JSVGViewerFrame");
	 * test("io.sf.carte.echosvg.bridge.BridgeContext");
	 * test("io.sf.carte.echosvg.bridge.BaseScriptingEnvironment");
	 * test("io.sf.carte.echosvg.bridge.ScriptingEnvironment"); } public void
	 * test(String cls) { System.err.println("Test '" + cls + "': " +
	 * visibleToScripts(cls)); }
	 */

	/**
	 * Returns whether the given class is visible to scripts.
	 */
	@Override
	public boolean visibleToScripts(String fullClassName) {
		// Don't let them mess with script engine's internals.
		if (fullClassName.startsWith("org.mozilla.javascript"))
			return false;

		if (fullClassName.startsWith("io.sf.carte.echosvg.")) {
			// Just get package within this implementation.
			String implPkg = fullClassName.substring(17);

			// Don't let them mess with this implementation's script internals.
			if (implPkg.startsWith("script"))
				return false;

			// Don't let them get global structures.
			if (implPkg.startsWith("apps"))
				return false;

			// Don't let them get scripting stuff from bridge, but specifically
			// allow access to:
			//
			// i.s.c.e.bridge.ScriptingEnvironment$Window$IntervalScriptTimerTask
			// i.s.c.e.bridge.ScriptingEnvironment$Window$IntervalRunnableTimerTask
			// i.s.c.e.bridge.ScriptingEnvironment$Window$TimeoutScriptTimerTask
			// i.s.c.e.bridge.ScriptingEnvironment$Window$TimeoutRunnableTimerTask
			//
			// since objects of these classes are returned by setInterval() and
			// setTimeout().
			if (implPkg.startsWith("bridge.")) {
				String implBridgeClass = implPkg.substring(7);
				if (implBridgeClass.startsWith("ScriptingEnvironment")) {
					if (implBridgeClass.startsWith("$Window$", 20)) {
						String c = implBridgeClass.substring(28);
						if (c.equals("IntervalScriptTimerTask") || c.equals("IntervalRunnableTimerTask")
								|| c.equals("TimeoutScriptTimerTask") || c.equals("TimeoutRunnableTimerTask")) {
							return true;
						}
					}
					return false;
				}
				if (implBridgeClass.startsWith("BaseScriptingEnvironment")) {
					return false;
				}
			}
		}

		return true;
	}
}
