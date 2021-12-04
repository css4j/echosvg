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

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import io.sf.carte.echosvg.bridge.DefaultScriptSecurity;
import io.sf.carte.echosvg.bridge.NoLoadScriptSecurity;
import io.sf.carte.echosvg.bridge.RelaxedScriptSecurity;
import io.sf.carte.echosvg.bridge.ScriptSecurity;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.test.svg.SelfContainedSVGOnLoadTest;
import io.sf.carte.echosvg.util.ApplicationSecurityEnforcer;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Helper class to simplify writing the unitTesting.xml file for scripting. The
 * "id" for the test needs to be the path of the selft contained SVG test,
 * starting from: {@literal $}rootDir/test-resources/io/sf/carte/echosvg/
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

@SuppressWarnings({ "deprecation", "removal" })
public class ScriptSelfTest extends SelfContainedSVGOnLoadTest {

	private boolean secure = true;
	private boolean constrain = true;
	private String scripts = "text/ecmascript, application/java-archive";
	private String id = null;
	private TestUserAgent userAgent = new TestUserAgent();

	public void runTest() throws Exception {
		ApplicationSecurityEnforcer ase = ApplicationSecurityEnforcer.createSecurityEnforcer(
				this.getClass(), "io/sf/carte/echosvg/apps/svgbrowser/svgbrowser.policy");

		if (secure) {
			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
					@Override
					public Void run() throws Exception {
						ase.enforceSecurity(true);
						return null;
					}
				});
			} catch (PrivilegedActionException pae) {
				throw pae.getException();
			}
		}

		try {
			testSVGOnLoad("io/sf/carte/echosvg/" + id + ".svg");
		} finally {
			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
					@Override
					public Void run() throws Exception {
						ase.enforceSecurity(false);
						return null;
					}
				});
			} catch (PrivilegedActionException pae) {
				throw pae.getException();
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSecure(Boolean secure) {
		this.secure = secure;
	}

	public Boolean getSecure() {
		return secure ? Boolean.TRUE : Boolean.FALSE;
	}

	public void setConstrain(Boolean constrain) {
		this.constrain = constrain;
	}

	public Boolean getConstrain() {
		return constrain ? Boolean.TRUE : Boolean.FALSE;
	}

	public void setScripts(String scripts) {
		this.scripts = scripts;
	}

	public String getScripts() {
		return scripts;
	}

	@Override
	protected UserAgent buildUserAgent() {
		return userAgent;
	}

	private class TestUserAgent extends UserAgentAdapter {
		@Override
		public ScriptSecurity getScriptSecurity(String scriptType, ParsedURL scriptPURL, ParsedURL docPURL) {
			if (scripts.indexOf(scriptType) == -1) {
				return new NoLoadScriptSecurity(scriptType);
			} else {
				if (constrain) {
					return new DefaultScriptSecurity(scriptType, scriptPURL, docPURL);
				} else {
					return new RelaxedScriptSecurity(scriptType, scriptPURL, docPURL);
				}
			}
		}
	}

}
