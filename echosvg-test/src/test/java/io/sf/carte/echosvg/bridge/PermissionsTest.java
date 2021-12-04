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

import org.junit.After;
import org.junit.Test;

/**
 * Checks the permissions applied to ECMA and Jar Scripts.
 *
 * @version $Id$
 */
@SuppressWarnings({ "deprecation", "removal" })
public class PermissionsTest {

	@After
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

	/*
	 * Check that permissions are denied for: scripts loaded 'normall', for scripts
	 * loaded with a Function() object and for scripts loaded with the 'eval()'
	 * function. Note that scripts loaded with the 'Script()' object are disallowed
	 * completely in secure mode.
	 */
	@Test
	public void testECMAPermissionsDenied() throws Exception {
		String scripts = "text/ecmascript";
		String[] scriptSource = { "ecmaCheckPermissionsDenied", "ecmaCheckPermissionsDeniedFunction",
				"ecmaCheckPermissionsDeniedEval", };
		boolean secure = true;
		String[][] scriptOrigin = { { "any" }, { "any", "document" }, { "any", "document", "embeded" },
				{ "any", "document", "embeded" }, };
		runTest(scripts, scriptSource, scriptOrigin, secure);
	}

	/*
	 * Check that permissions are granted for all types of scripts when in
	 * non-secure model, except for Script() objects which are not supported _at
	 * all_
	 */
	@Test
	public void testECMAPermissionsGranted() throws Exception {
		String scripts = "text/ecmascript";
		String[] scriptSource = { "ecmaCheckPermissionsGranted", "ecmaCheckPermissionsGrantedFunction",
				"ecmaCheckPermissionsGrantedEval", };
		boolean secure = false;
		String[][] scriptOrigin = { { "any" }, { "any", "document" }, { "any", "document", "embeded" },
				{ "any", "document", "embeded" }, };
		runTest(scripts, scriptSource, scriptOrigin, secure);
	}

	@Test
	public void testJarPermissionsDenied() throws Exception {
		String scripts = "application/java-archive";
		String[] scriptSource = { "jarCheckPermissionsDenied" };
		boolean secure = true;
		String[][] scriptOrigin = { { "any" }, { "any", "document" }, { "any", "document", "embeded" },
				{ "any", "document", "embeded" }, };
		runTest(scripts, scriptSource, scriptOrigin, secure);
	}

	@Test
	public void testJarPermissionsGranted() throws Exception {
		String scripts = "application/java-archive";
		String[] scriptSource = { "jarCheckPermissionsGranted" };
		boolean secure = false;
		String[][] scriptOrigin = { { "any" }, { "any", "document" }, { "any", "document", "embeded" },
				{ "any", "document", "embeded" }, };
		runTest(scripts, scriptSource, scriptOrigin, secure);
	}

	private void runTest(String scripts, String[] scriptSource, String[][] scriptOrigin, boolean secure)
			throws Exception {
		for (int i = 0; i < scriptSource.length; i++) {
			for (int j = 0; j < scriptOrigin[i].length; j++) {
				ScriptSelfTest t = buildTest(scripts, scriptSource[i], scriptOrigin[i][j], secure);
				t.runTest();
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
