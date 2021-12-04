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

import java.lang.reflect.Constructor;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;

/**
 * Run actions with certain security privileges, if available.
 */
class SecurityHelper {

	private static final SecurityHelper singleton = createSecurityHelper();

	private static SecurityHelper createSecurityHelper() {
		SecurityHelper sc;
		try {
			Class<?> cl = Class.forName("io.sf.carte.echosvg.bridge.SMSecurityHelper");
			Constructor<?> ctor = cl.getConstructor();
			sc = (SecurityHelper) ctor.newInstance();
		} catch (Exception e) {
			sc = new SecurityHelper();
		}
		return sc;
	}

	static SecurityHelper getInstance() {
		return singleton;
	}

	/**
	 * Default constructor
	 */
	SecurityHelper() {
		super();
	}

	/**
	 * Run an action with the privileges of the script module, if any.
	 * 
	 * @param action the action.
	 * @return the action's result.
	 */
	Object runPrivilegedAction(PrivilegedAction<?> action) {
		return action.run();
	}

	Object runPrivilegedAction(PrivilegedAction<?> action, Object securityDomain) {
		return action.run();
	}

	/**
	 * Get the permissions of the given {@code CodeSource}.
	 * 
	 * @param codesource the code source.
	 * @return the permissions of the given {@code CodeSource}, {@code null}
	 *         if the JVM does not handle permissions.
	 */
	PermissionCollection getPermissions(CodeSource codesource) {
		return null;
	}

}
