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
package io.sf.carte.echosvg.bridge;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.TestUtil;

/**
 * Checks that JAR Scripts which should be loaded are indeed loaded.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@Tag("SecMan")
@SuppressWarnings({ "deprecation", "removal" })
public class JarLoadTest {

	private static final String scriptsType = "application/java-archive";

	@BeforeAll
	public static void setUpBeforeAll() {
		TestUtil.defaultRhinoShutter();
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
	public void testJarCheckLoadAny() throws Exception {
		String scriptSource = "jarCheckLoadAny";

		ScriptSelfTest t = buildTest(scriptsType, scriptSource, "any", true);
		t.runTest();

		t = buildTest(scriptsType, scriptSource, "any", false);
		t.runTest();
	}

	@Test
	public void testJarCheckLoadSameAsDocument() throws Exception {
		String scriptSource = "jarCheckLoadSameAsDocument";
		// Note: base64 encoding of jar content is not supported.

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
		ScriptSelfTest t = buildTest(scriptsType, scriptSource, "any", true);
		t.runTest();

		t = buildTest(scriptsType, scriptSource, "any", false);
		t.runTest();
	}

	private ScriptSelfTest buildTest(String scripts, String id, String origin, boolean secure) {
		ScriptSelfTest t = new ScriptSelfTest();
		String desc = "(scripts=" + scripts + ")(scriptOrigin=" + origin + ")(secure=" + secure + ")";

		t.setId(id);
		t.setDescription(id + desc);
		t.setScriptOrigin(origin);
		t.setSecure(secure);
		t.setScripts(scripts);

		return t;
	}

}
