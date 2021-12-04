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

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.PrivilegedAction;

import org.mozilla.javascript.WrappedException;

import io.sf.carte.echosvg.script.rhino.RhinoClassLoader;

@SuppressWarnings("removal")
class SMSecurityHelper extends SecurityHelper {

	/**
	 * Default constructor
	 */
	public SMSecurityHelper() {
		super();
	}

	@SuppressWarnings("deprecation")
	@Override
	Object runPrivilegedAction(PrivilegedAction<?> action) {
		try {
			return AccessController.doPrivileged(action);
		} catch (Exception e) {
			throw new WrappedException(e);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	Object runPrivilegedAction(PrivilegedAction<?> action, Object securityDomain) {
		AccessControlContext acc;
		if (securityDomain instanceof AccessControlContext)
			acc = (AccessControlContext) securityDomain;
		else {
			RhinoClassLoader loader = (RhinoClassLoader) securityDomain;
			acc = (AccessControlContext) loader.getAccessControlObject();
		}

		try {
			return AccessController.doPrivileged(action, acc);
		} catch (Exception e) {
			throw new WrappedException(e);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	PermissionCollection getPermissions(CodeSource codesource) {
		Policy p = Policy.getPolicy();

		PermissionCollection pc = null;
		if (p != null) {
			pc = p.getPermissions(codesource);
		}

		return pc;
	}

}
