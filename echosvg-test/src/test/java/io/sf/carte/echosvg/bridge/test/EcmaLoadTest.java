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
package io.sf.carte.echosvg.bridge.test;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.ScriptUtil;

/**
 * Checks that ECMA Scripts which should be loaded are indeed loaded.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@Tag("SecMan")
@SuppressWarnings({ "deprecation", "removal" })
public class EcmaLoadTest {

	@BeforeAll
	public static void setUpBeforeAll() {
		ScriptUtil.defaultRhinoShutter();
	}

	@AfterEach
	public void tearDown() throws Exception {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
				@Override
				public Void run() throws Exception {
					System.setSecurityManager(null);
					System.setProperty("java.security.policy", "");
					return null;
				}
			});
		} catch (PrivilegedActionException pae) {
			throw pae.getException();
		}
	}

	@Test
	public void test() throws Exception {
		String scripts = "text/ecmascript";
		String[] scriptSource = { "ecmaCheckLoadAny", "ecmaCheckLoadSameAsDocument", "ecmaCheckLoadEmbed",
				"ecmaCheckLoadEmbedAttr", };
		boolean[] secure = { true, false };
		String[][] scriptOrigin = { { "any" }, { "any", "document" }, { "any", "document", "embeded" },
				{ "any", "document", "embeded" }, };

		//
		// <!> Need to make restricted {true/false}
		//

		//
		// An ecma script can be loaded if ECMA is listed
		// as an allowed script _and_ the loaded script
		// has an origin allowed by the scriptOrigin setting.
		// All other security settings should not have an
		// influence on whether or not the script can be loaded.
		//
		for (int i = 0; i < scriptSource.length; i++) {
			for (int j = 0; j < scriptOrigin[i].length; j++) {
				for (boolean aSecure : secure) {
					ScriptSelfTest t = buildTest(scripts, scriptSource[i], scriptOrigin[i][j], aSecure);
					t.runTest();
				}
			}
		}
	}

	private ScriptSelfTest buildTest(String scripts, String id, String origin, boolean secure) {
		ScriptSelfTest t = new ScriptSelfTest();
		String desc = "(scripts=" + scripts + ")(scriptOrigin=" + origin + ")(secure=" + secure + ')';

		t.setId(id);
		t.setDescription(id + ' ' + desc);
		t.setScriptOrigin(origin);
		t.setSecure(secure);
		t.setScripts(scripts);

		return t;
	}

}
