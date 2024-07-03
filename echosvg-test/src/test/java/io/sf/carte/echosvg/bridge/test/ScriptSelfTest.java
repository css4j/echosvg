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

import static org.junit.jupiter.api.Assertions.fail;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import io.sf.carte.echosvg.bridge.DefaultScriptSecurity;
import io.sf.carte.echosvg.bridge.EmbededScriptSecurity;
import io.sf.carte.echosvg.bridge.NoLoadScriptSecurity;
import io.sf.carte.echosvg.bridge.RelaxedScriptSecurity;
import io.sf.carte.echosvg.bridge.ScriptSecurity;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.test.svg.SelfContainedSVGOnLoadTest;
import io.sf.carte.echosvg.util.ApplicationSecurityEnforcer;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Script test runner.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

@SuppressWarnings({ "deprecation", "removal" })
public class ScriptSelfTest extends SelfContainedSVGOnLoadTest {
	private String scripts = "text/ecmascript, application/java-archive";
	private boolean secure = true;
	private String scriptOrigin = "any";
	private String id = null;
	private String description;

	TestUserAgent userAgent = new TestUserAgent();

	public void runTest() throws Exception {
		if (id != null) {
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
				testSVGOnLoad("io/sf/carte/echosvg/bridge/" + id + ".svg", "");
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
					fail(pae.getException().getMessage());
				}
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public boolean getSecure() {
		return secure;
	}

	public String getScriptOrigin() {
		return scriptOrigin;
	}

	public void setScriptOrigin(String scriptOrigin) {
		this.scriptOrigin = scriptOrigin;
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
			ScriptSecurity scriptSecurity = null;
			if (scripts.indexOf(scriptType) == -1) {
				scriptSecurity = new NoLoadScriptSecurity(scriptType);
			} else {
				if ("any".equals(scriptOrigin)) {
					scriptSecurity = new RelaxedScriptSecurity(scriptType, scriptPURL, docPURL);
				} else if ("document".equals(scriptOrigin)) {
					scriptSecurity = new DefaultScriptSecurity(scriptType, scriptPURL, docPURL);
				} else if ("embeded".equals(scriptOrigin)) {
					scriptSecurity = new EmbededScriptSecurity(scriptType, scriptPURL, docPURL);
				} else if ("none".equals(scriptOrigin)) {
					scriptSecurity = new NoLoadScriptSecurity(scriptType);
				} else {
					throw new RuntimeException("Wrong scriptOrigin : " + scriptOrigin);
				}
			}

			return scriptSecurity;
		}
	}

}
