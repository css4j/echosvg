/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.sf.carte.echosvg.test.misc;

import io.sf.carte.echosvg.script.rhino.RhinoClassShutter;

public class ScriptUtil {

	private static volatile boolean shutterRan = false;

	/**
	 * Add a default set of allowed hierarchies to the Rhino class shutter.
	 */
	public static void defaultRhinoShutter() {
		if (!shutterRan) {
			RhinoClassShutter.addToWhitelist("java.*");
			RhinoClassShutter.addToWhitelist("javax.*");
			RhinoClassShutter.addToWhitelist("org.gradle.*");
			RhinoClassShutter.addToWhitelist("io.sf.carte.echosvg.test.svg.*");
			RhinoClassShutter.addToWhitelist("io.sf.carte.echosvg.test.image.*");
			RhinoClassShutter.addToWhitelist("io.sf.carte.echosvg.swing.test.*");
			shutterRan = true;
		}
	}

}
