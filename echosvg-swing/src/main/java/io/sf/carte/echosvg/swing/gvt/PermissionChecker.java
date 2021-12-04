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
package io.sf.carte.echosvg.swing.gvt;

import java.lang.reflect.Constructor;
import java.security.Permission;

/**
 * Check permissions.
 */
class PermissionChecker {

	private static final PermissionChecker singleton = createPermissionChecker();

	private static PermissionChecker createPermissionChecker() {
		PermissionChecker pc;
		try {
			Class<?> cl = Class.forName("io.sf.carte.echosvg.swing.gvt.SMPermissionChecker");
			Constructor<?> ctor = cl.getConstructor();
			pc = (PermissionChecker) ctor.newInstance();
		} catch (Exception e) {
			pc = new PermissionChecker();
		}
		return pc;
	}

	static PermissionChecker getInstance() {
		return singleton;
	}

	/**
	 * Default constructor
	 */
	PermissionChecker() {
		super();
	}

	/**
	 * Check for the permission.
	 * 
	 * @param perm the Permission.
	 * @return true if permitted.
	 */
	boolean checkPermission(Permission perm) {
		return true;
	}

}
