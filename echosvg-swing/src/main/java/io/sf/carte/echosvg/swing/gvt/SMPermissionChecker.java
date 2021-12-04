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

import java.security.Permission;

/**
 * Check permissions with the Security Manager.
 */
class SMPermissionChecker extends PermissionChecker {

	/**
	 * Default constructor
	 */
	public SMPermissionChecker() {
		super();
	}

	/**
	 * Check for the permission.
	 * 
	 * @param perm the Permission.
	 * @return true if permitted.
	 */
	@SuppressWarnings("removal")
	@Override
	boolean checkPermission(Permission perm) {
		SecurityManager securityManager = System.getSecurityManager();
		if (securityManager != null) {
			try {
				securityManager.checkPermission(perm);
			} catch (SecurityException e) {
				return false;
			}
		}
		return true;
	}

}
