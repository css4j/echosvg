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
package io.sf.carte.echosvg.util;

import java.lang.reflect.Constructor;
import java.net.URL;

/**
 * This is a helper class which helps applications enforce secure script
 * execution if a Security Manager is available. <br>
 * It is used by the rasterizer, also by tests. <br>
 * This class may install a <code>SecurityManager</code> for an application and
 * resolves whether the application runs in a development environment or from a
 * jar file (in other words, it resolves code-base issues for the application).
 * <br>
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@Deprecated
public class ApplicationSecurityEnforcer {

	/**
	 * Message for the NullPointerException thrown when no policy file can be found.
	 */
	static final String EXCEPTION_NO_POLICY_FILE = "ApplicationSecurityEnforcer.message.null.pointer.exception.no.policy.file";

	/**
	 * System property for App's development base directory
	 */
	public static final String PROPERTY_APP_DEV_BASE = "app.dev.base";

	/**
	 * System property for App's development class directory
	 */
	public static final String PROPERTY_APP_DEV_CLASS_DIR = "app.dev.classdir";

	/**
	 * System property for App's development test classes directory
	 */
	public static final String PROPERTY_APP_DEV_TEST_CLASS_DIR = "app.dev.testdir";

	/**
	 * System property for IDE's class directory (e.g. Eclipse's OSGI directory)
	 */
	public static final String PROPERTY_IDE_CLASS_DIR = "app.ide.classdir";

	/**
	 * System property for App's jars base directory
	 */
	public static final String PROPERTY_APP_JAR_BASE = "app.jar.base";

	/**
	 * The application's main entry point
	 */
	private final Class<?> appMainClass;

	/**
	 * The application's security policy
	 */
	private final String securityPolicy;

	/**
	 * Creates a new ApplicationSecurityEnforcer.
	 * 
	 * @param appMainClass   class of the applications's main entry point
	 * @param securityPolicy resource for the security policy which should be
	 *                       enforced for the application.
	 */
	ApplicationSecurityEnforcer(Class<?> appMainClass, String securityPolicy) {
		this.appMainClass = appMainClass;
		this.securityPolicy = securityPolicy;
	}

	public static ApplicationSecurityEnforcer createSecurityEnforcer(Class<?> appMainClass, String securityPolicy) {
		ApplicationSecurityEnforcer ase;
		try {
			Class<?> cl = Class.forName("io.sf.carte.echosvg.util.SMApplicationSecurityEnforcer");
			Constructor<?> ctor = cl.getConstructor(Class.class, String.class);
			ase = (ApplicationSecurityEnforcer) ctor.newInstance(appMainClass, securityPolicy);
		} catch (Exception e) {
			ase = new ApplicationSecurityEnforcer(appMainClass, securityPolicy);
		}
		return ase;
	}

	protected Class<?> getApplicationMainClass() {
		return appMainClass;
	}

	protected String getSecurityPolicy() {
		return securityPolicy;
	}

	/**
	 * Enforces security by installing a <code>SecurityManager</code>. This will
	 * throw a <code>SecurityException</code> if installing a
	 * <code>SecurityManager</code> requires overriding an existing
	 * <code>SecurityManager</code>. In other words, this method will not install a
	 * new <code>SecurityManager</code> if there is already one it did not install
	 * in place.
	 */
	public void enforceSecurity(boolean enforce) {
	}

	/**
	 * Returns the url for the default policy. This never returns null, but it may
	 * throw a NullPointerException
	 */
	public URL getPolicyURL() {
		ClassLoader cl = appMainClass.getClassLoader();
		URL policyURL = cl.getResource(securityPolicy);

		if (policyURL == null) {
			throw new NullPointerException(
					Messages.formatMessage(EXCEPTION_NO_POLICY_FILE, new Object[] { securityPolicy }));
		}

		return policyURL;
	}

	/**
	 * Installs a SecurityManager on behalf of the application
	 */
	public void installSecurityManager() {
	}

}
