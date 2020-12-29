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
 * @version $Id$
 */
public class RhinoClassShutter implements ClassShutter {

    /*
    public RhinoClassShutter() {
        // I suspect that we might want to initialize this
        // from a resource file.
        // test();
    }

    public void test() {
        test("org.mozilla.javascript.Context");
        test("org.mozilla.javascript");
        test("io.sf.carte.echosvg.dom.SVGOMDocument");
        test("io.sf.carte.echosvg.script.rhino.RhinoInterpreter");
        test("io.sf.carte.echosvg.apps.svgbrowser.JSVGViewerFrame");
        test("io.sf.carte.echosvg.bridge.BridgeContext");
        test("io.sf.carte.echosvg.bridge.BaseScriptingEnvironment");
        test("io.sf.carte.echosvg.bridge.ScriptingEnvironment");
    }
    public void test(String cls) {
        System.err.println("Test '" + cls + "': " +
                           visibleToScripts(cls));
    }
    */

    /**
     * Returns whether the given class is visible to scripts.
     */
    public boolean visibleToScripts(String fullClassName) {
        // Don't let them mess with script engine's internals.
        if (fullClassName.startsWith("org.mozilla.javascript"))
            return false;

        if (fullClassName.startsWith("io.sf.carte.echosvg.")) {
            // Just get package within batik.
            String batikPkg = fullClassName.substring(17);

            // Don't let them mess with EchoSVG script internals.
            if (batikPkg.startsWith("script"))
                return false;

            // Don't let them get global structures.
            if (batikPkg.startsWith("apps"))
                return false;

            // Don't let them get scripting stuff from bridge, but specifically
            // allow access to:
            //
            //   o.a.b.bridge.ScriptingEnvironment$Window$IntervalScriptTimerTask
            //   o.a.b.bridge.ScriptingEnvironment$Window$IntervalRunnableTimerTask
            //   o.a.b.bridge.ScriptingEnvironment$Window$TimeoutScriptTimerTask
            //   o.a.b.bridge.ScriptingEnvironment$Window$TimeoutRunnableTimerTask
            //
            // since objects of these classes are returned by setInterval() and
            // setTimeout().
            if (batikPkg.startsWith("bridge.")) {
                String batikBridgeClass = batikPkg.substring(7);
                if (batikBridgeClass.startsWith("ScriptingEnvironment")) {
                    if (batikBridgeClass.startsWith("$Window$", 20)) {
                        String c = batikBridgeClass.substring(28);
                        if (c.equals("IntervalScriptTimerTask")
                                || c.equals("IntervalRunnableTimerTask")
                                || c.equals("TimeoutScriptTimerTask")
                                || c.equals("TimeoutRunnableTimerTask")) {
                            return true;
                        }
                    }
                    return false;
                }
                if (batikBridgeClass.startsWith("BaseScriptingEnvironment")) {
                    return false;
                }
            }
        }

        return true;
    }
}
