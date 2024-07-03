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

import io.sf.carte.echosvg.test.ScriptUtil;
import io.sf.carte.echosvg.test.svg.SVGOnLoadExceptionTest;

/**
 * Checks that ECMA Scripts which should not be loaded are not loaded.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@Tag("SecMan")
@SuppressWarnings({ "deprecation", "removal" })
public class EcmaNoLoadTest {

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
		String scripts = "application/java-archive";
		String[] scriptSource = { "bridge/ecmaCheckNoLoadAny", "bridge/ecmaCheckNoLoadSameAsDocument",
				"bridge/ecmaCheckNoLoadEmbed", "bridge/ecmaCheckNoLoadEmbedAttr", };
		boolean[] secure = { true, false };
		String[] scriptOrigin = { "ANY", "DOCUMENT", "EMBEDED", "NONE" };

		//
		// If "application/ecmascript" is disallowed, scripts
		// should not be loaded, no matter their origin or the
		// other security settings.
		//
		for (String aScriptSource : scriptSource) {
			for (boolean aSecure : secure) {
				for (String aScriptOrigin : scriptOrigin) {
					SVGOnLoadExceptionTest t = buildTest(scripts, aScriptSource, aScriptOrigin, aSecure, false,
							"java.lang.SecurityException");
					t.runTest();
				}
			}
		}

		//
		// If script run in restricted mode, then there should be
		// a security exception, no matter what the other settings are
		// (if we are running code under a security manager, that is,
		// i.e., secure is true).
		scripts = "text/ecmascript";
		for (int i = 0; i < scriptSource.length; i++) {
			for (int k = 0; k < scriptOrigin.length; k++) {
				String expectedException;
				if ((i >= 2) && (k <= 2)) {
					// Success
					expectedException = null;
				} else {
					expectedException = "java.lang.SecurityException";
				}
				SVGOnLoadExceptionTest t = buildTest(scripts, scriptSource[i], scriptOrigin[k], true, true,
						expectedException);
				t.runTest();
			}
		}

		//
		// If "application/ecmascript" is allowed, but the accepted
		// script origin is lower than the candidate script, then
		// the script should not be loaded (e.g., if scriptOrigin
		// is embedded and trying to load an external script).
		//
		for (int j = 0; j < scriptOrigin.length; j++) {
			int max = j;
			if (j == scriptOrigin.length - 1) {
				max = j + 1;
			}
			for (int i = 0; i < max; i++) {
				for (boolean aSecure : secure) {
					SVGOnLoadExceptionTest t = buildTest(scripts, scriptSource[i], scriptOrigin[j], aSecure, false,
							"java.lang.SecurityException");
					t.runTest();
				}
			}
		}
	}

	private SVGOnLoadExceptionTest buildTest(String scripts, String id, String origin, boolean secure,
			boolean restricted, String expectedException) {
		SVGOnLoadExceptionTest t = new SVGOnLoadExceptionTest();
		String desc = "(scripts=" + scripts + ")(scriptOrigin=" + origin + ")(secure=" + secure + ")(restricted="
				+ restricted + ")";

		t.setId(id);
		t.setDescription(id + ' ' + desc);
		t.setScriptOrigin(origin);
		t.setSecure(secure);
		t.setScripts(scripts);
		t.setExpectedExceptionClass(expectedException);
		t.setRestricted(restricted);

		return t;
	}

}
