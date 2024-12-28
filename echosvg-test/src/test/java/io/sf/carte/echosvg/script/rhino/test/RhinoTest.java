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
package io.sf.carte.echosvg.script.rhino.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.ScriptUtil;

/**
 * Checks the permissions applied to Rhino Scripts.
 *
 * @version $Id$
 */
public class RhinoTest {

	@BeforeAll
	public static void setUpBeforeAll() {
		ScriptUtil.defaultRhinoShutter();
	}

	@Test
	@Tag("SecMan")
	public void testECMAPermissionsDenied() throws Exception {
		runTest("script/rhino/eval", true);
	}

	private void runTest(String id, boolean secure) throws Exception {
		ScriptSelfTest t = new ScriptSelfTest();

		t.setId(id);
		t.setSecure(secure);

		t.runTest();
	}

}
