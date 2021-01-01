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
package io.sf.carte.echosvg.bridge;

import io.sf.carte.echosvg.test.DefaultTestSuite;

/**
 * Checks that JAR Scripts which should  be loaded are indeed
 * loaded.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JarLoadTest extends DefaultTestSuite {
    public JarLoadTest() {
        String scripts = "application/java-archive";
        // Note: base64 encoding of jar content is not supported.
        String[] scriptSource = {"jarCheckLoadAny",
                                 "jarCheckLoadSameAsDocument"};
        boolean[] secure = {true, false};
        String[] scriptOrigin = {"any", "document", "embeded"};

        //
        // <!> Need to make restricted {true/false}
        // 

        //
        // An jar script can be loaded if JAR is listed
        // as an allowed script _and_ the loaded script
        // has an origin allowed by the scriptOrigin setting.
        // All other security settings should not have an 
        // influence on whether or not the script can be loaded.
        //
        for (int i=0; i<scriptSource.length; i++) {
            for (int j=0; j<=i; j++) {
                for (boolean aSecure : secure) {
                    ScriptSelfTest t = buildTest(scripts, scriptSource[i],
                            scriptOrigin[j],
                            aSecure);
                    addTest(t);
                }
            }
        }
    }

    ScriptSelfTest buildTest(String scripts, String id, String origin, boolean secure) {
        ScriptSelfTest t = new ScriptSelfTest();
        String desc = 
            "(scripts=" + scripts + 
            ")(scriptOrigin=" + origin +
            ")(secure=" + secure + ")";
        
        t.setId(id + desc);
        t.setScriptOrigin(origin);
        t.setSecure(secure);
        t.setScripts(scripts);

        return t;
    }
                             
}
