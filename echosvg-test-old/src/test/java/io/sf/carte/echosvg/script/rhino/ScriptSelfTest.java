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

import io.sf.carte.echosvg.bridge.DefaultScriptSecurity;
import io.sf.carte.echosvg.bridge.NoLoadScriptSecurity;
import io.sf.carte.echosvg.bridge.RelaxedScriptSecurity;
import io.sf.carte.echosvg.bridge.ScriptSecurity;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.test.svg.SelfContainedSVGOnLoadTest;
import io.sf.carte.echosvg.util.ApplicationSecurityEnforcer;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Helper class to simplify writing the unitTesting.xml file for scripting. The
 * "id" for the test needs to be the path of the selft contained SVG test,
 * starting from: <xml-batik-dir>/test-resources/io/sf/carte/echosvg/
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public class ScriptSelfTest extends SelfContainedSVGOnLoadTest {
	boolean secure = true;
	boolean constrain = true;
	String scripts = "text/ecmascript, application/java-archive";
	TestUserAgent userAgent = new TestUserAgent();

	@Override
	public void setId(String id) {
		super.setId(id);
		svgURL = resolveURL("test-resources/io/sf/carte/echosvg/" + id + ".svg");
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
	public TestReport runImpl() throws Exception {
		ApplicationSecurityEnforcer ase = new ApplicationSecurityEnforcer(this.getClass(),
				"io/sf/carte/echosvg/apps/svgbrowser/resources/svgbrowser.policy");

		if (secure) {
			ase.enforceSecurity(true);
		}

		try {
			return super.runImpl();
		} finally {
			ase.enforceSecurity(false);
		}
	}

	@Override
	protected UserAgent buildUserAgent() {
		return userAgent;
	}

	class TestUserAgent extends UserAgentAdapter {
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
