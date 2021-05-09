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

import java.net.URL;
import java.security.Policy;
import java.util.StringTokenizer;

/**
 * This is a helper class which helps applications enforce secure script
 * execution. <br>
 * It is used by the rasterizer, also by tests. <br>
 * This class can install a <code>SecurityManager</code> for an application and
 * resolves whether the application runs in a development environment or from a
 * jar file (in other words, it resolves code-base issues for the application).
 * <br>
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ApplicationSecurityEnforcer {
	/**
	 * Message for the SecurityException thrown when there is already a
	 * SecurityManager installed at the time the rasterizer tries to install its
	 * own security settings.
	 */
	private static final String EXCEPTION_ALIEN_SECURITY_MANAGER = "ApplicationSecurityEnforcer.message.security.exception.alien.security.manager";

	/**
	 * Message for the NullPointerException thrown when no policy file can be found.
	 */
	private static final String EXCEPTION_NO_POLICY_FILE = "ApplicationSecurityEnforcer.message.null.pointer.exception.no.policy.file";

	/**
	 * System property for specifying a policy file.
	 */
	private static final String PROPERTY_JAVA_SECURITY_POLICY = "java.security.policy";

	/**
	 * Files in a jar file have a URL with the jar protocol
	 */
	private static final String JAR_PROTOCOL = "jar:";

	/**
	 * Used in jar file urls to separate the jar file name from the referenced file
	 */
	private static final String JAR_URL_FILE_SEPARATOR = "!/";

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
	 * Directories where classes are in the development version
	 */
	private static final String DEV_GRADLE_CLASS_DIR = "/build/classes/java/main/";
	private static final String DEV_GRADLE_TEST_CLASS_DIR = "/build/classes/java/test/";
	private static final String DEV_ECLIPSE_CLASS_DIR = "/bin/main/";
	private static final String DEV_ECLIPSE_TEST_CLASS_DIR = "/bin/test/";
//	private static final String DEV_MAVEN_CLASS_DIR = "/target/classes/";
//	private static final String DEV_MAVEN_TEST_CLASS_DIR = "/target/test-classes/";

	/**
	 * The application's main entry point
	 */
	private Class<?> appMainClass;

	/**
	 * The application's security policy
	 */
	private String securityPolicy;

	/**
	 * The resource name for the application's main class
	 */
	private String appMainClassRelativeURL;

	/**
	 * Keeps track of the last SecurityManager installed
	 */
	private EchoSVGSecurityManager lastSecurityManagerInstalled;

	/**
	 * Keeps track of the previous SecurityManager installed
	 */
	private SecurityManager previousSecurityManagerInstalled = null;

	/**
	 * Creates a new ApplicationSecurityEnforcer.
	 * 
	 * @param appMainClass   class of the applications's main entry point
	 * @param securityPolicy resource for the security policy which should be
	 *                       enforced for the application.
	 * @param appJarFile     the Jar file into which the application is packaged.
	 * @deprecated This constructor is now deprecated. Use the two argument
	 *             constructor instead as this version will be removed after the
	 *             1.5beta4 release.
	 */
	@Deprecated
	public ApplicationSecurityEnforcer(Class<?> appMainClass, String securityPolicy, String appJarFile) {
		this(appMainClass, securityPolicy);
	}

	/**
	 * Creates a new ApplicationSecurityEnforcer.
	 * 
	 * @param appMainClass   class of the applications's main entry point
	 * @param securityPolicy resource for the security policy which should be
	 *                       enforced for the application.
	 */
	public ApplicationSecurityEnforcer(Class<?> appMainClass, String securityPolicy) {
		this.appMainClass = appMainClass;
		this.securityPolicy = securityPolicy;
		this.appMainClassRelativeURL = appMainClass.getName().replace('.', '/') + ".class";

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
		SecurityManager sm = System.getSecurityManager();

		if (sm != null && sm != lastSecurityManagerInstalled) {
			// Throw a Security exception: we do not want to override
			// an 'alien' SecurityManager with either null or
			// a new SecurityManager.
			throw new SecurityException(Messages.getString(EXCEPTION_ALIEN_SECURITY_MANAGER));
		}

		if (enforce) {
			// We first set the security manager to null to
			// force reloading of the policy file in case there
			// has been a change since it was last enforced (this
			// may happen with dynamically generated policy files).
			System.setSecurityManager(null);
			previousSecurityManagerInstalled = sm;
			installSecurityManager();
		} else {
			if (sm != null) {
				System.setSecurityManager(previousSecurityManagerInstalled);
				lastSecurityManagerInstalled = null;
				previousSecurityManagerInstalled = null;
			}
		}
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
		Policy policy = Policy.getPolicy();
		EchoSVGSecurityManager securityManager = new EchoSVGSecurityManager();

		//
		// If there is a java.security.policy property defined,
		// it takes precedence over the one passed to this object.
		// Otherwise, we default to the one passed to the constructor
		//
		ClassLoader cl = appMainClass.getClassLoader();
		String securityPolicyProperty = System.getProperty(PROPERTY_JAVA_SECURITY_POLICY);

		if (securityPolicyProperty == null || securityPolicyProperty.equals("")) {
			// Specify app's security policy in the
			// system property.
			URL policyURL = getPolicyURL();

			System.setProperty(PROPERTY_JAVA_SECURITY_POLICY, policyURL.toString());
		}

		//
		// The following detects whether the application is running in the
		// development environment, in which case it will set the
		// app.dev.base property or if it is running in the binary
		// distribution, in which case it will set the app.jar.base
		// property. These properties are expanded in the security
		// policy files.
		// Property expansion is used to provide portability of the
		// policy files between various code bases (e.g., file base,
		// server base, etc..).
		//
		URL mainClassURL = cl.getResource(appMainClassRelativeURL);
		if (mainClassURL == null) {
			// Something is really wrong: we would be running a class
			// which can't be found....
			throw new RuntimeException(appMainClassRelativeURL);
		}

		String expandedMainClassName = mainClassURL.toString();
		if (expandedMainClassName.startsWith(JAR_PROTOCOL)) {
			setJarBase(expandedMainClassName);
		} else {
			setDevProperties(expandedMainClassName);
		}

		// Install new security manager
		System.setSecurityManager(securityManager);
		lastSecurityManagerInstalled = securityManager;

		// Forces re-loading of the security policy
		policy.refresh();

		if (securityPolicyProperty == null || securityPolicyProperty.equals("")) {
			System.setProperty(PROPERTY_JAVA_SECURITY_POLICY, "");
		}
	}

	private void setJarBase(String expandedMainClassName) {
		//
		// Only set the app.jar.base if it is not already defined
		//
		String curAppJarBase = System.getProperty(PROPERTY_APP_JAR_BASE);
		if (curAppJarBase == null) {
			expandedMainClassName = expandedMainClassName.substring(JAR_PROTOCOL.length());

			int codeBaseEnd = expandedMainClassName.indexOf(JAR_URL_FILE_SEPARATOR + appMainClassRelativeURL);

			if (codeBaseEnd == -1) {
				// Something is seriously wrong. This should *never* happen
				// as the APP_SECURITY_POLICY_URL is such that it will be
				// a substring of its corresponding URL value
				throw new RuntimeException("Unable to derive app.jar.base from: " + expandedMainClassName);
			}

			String appCodeBase = expandedMainClassName.substring(0, codeBaseEnd);

			// At this point appCodeBase contains the JAR file name
			// Now, we extract it.
			codeBaseEnd = appCodeBase.lastIndexOf('/');
			if (codeBaseEnd == -1) {
				appCodeBase = "";
			} else {
				appCodeBase = appCodeBase.substring(0, codeBaseEnd);
			}

			System.setProperty(PROPERTY_APP_JAR_BASE, appCodeBase);
		}
	}

	/**
	 * Set the development-related properties for expansion in the policy file used when
	 * App is running in its development version
	 */
	private void setDevProperties(String expandedMainClassName) {
		//
		// Only set the app.code.base property if it is not already
		// defined.
		//
		String curAppCodeBase = System.getProperty(PROPERTY_APP_DEV_BASE);
		String appClassDir = null;
		String appTestClassDir = null;
		String ideClassDir = null;

		if (curAppCodeBase == null) {
			int codeBaseEnd = expandedMainClassName.lastIndexOf(DEV_GRADLE_CLASS_DIR + appMainClassRelativeURL);

			if (codeBaseEnd == -1) {
				codeBaseEnd = expandedMainClassName.lastIndexOf(DEV_GRADLE_TEST_CLASS_DIR + appMainClassRelativeURL);
			}

			if (codeBaseEnd == -1) {
				codeBaseEnd = expandedMainClassName.lastIndexOf(DEV_ECLIPSE_CLASS_DIR + appMainClassRelativeURL);

				if (codeBaseEnd == -1) {
					codeBaseEnd = expandedMainClassName
							.lastIndexOf(DEV_ECLIPSE_TEST_CLASS_DIR + appMainClassRelativeURL);
				}
				appClassDir = DEV_ECLIPSE_CLASS_DIR;
				appTestClassDir = DEV_ECLIPSE_TEST_CLASS_DIR;
				ideClassDir = getIDEClassDir();
			} else {
				appClassDir = DEV_GRADLE_CLASS_DIR;
				appTestClassDir = DEV_GRADLE_TEST_CLASS_DIR;
			}

			if (codeBaseEnd == -1
					|| (codeBaseEnd = expandedMainClassName.substring(0, codeBaseEnd).lastIndexOf('/')) == -1) {
				throw new RuntimeException("Unable to derive app.dev.base from: " + expandedMainClassName);
			}

			String appCodeBase = expandedMainClassName.substring(0, codeBaseEnd);
			if (ideClassDir != null) {
				System.setProperty(PROPERTY_IDE_CLASS_DIR, ideClassDir);
			}
			System.setProperty(PROPERTY_APP_DEV_BASE, appCodeBase);
			System.setProperty(PROPERTY_APP_DEV_CLASS_DIR, appClassDir);
			System.setProperty(PROPERTY_APP_DEV_TEST_CLASS_DIR, appTestClassDir);
		}
	}

	private static String getIDEClassDir() {
		// Eclipse's OSGI directory
		String cp = System.getProperty("java.class.path");
		StringTokenizer st = new StringTokenizer(cp, ";");
		while (st.hasMoreTokens()) {
			String cpe = st.nextToken();
			int idx = cpe.indexOf("\\org.eclipse.osgi\\");
			if (idx != -1) {
				return cpe.substring(0, idx + 17);
			}
		}
		// If you use a different IDE, feel free to send a PR adding support for it
		return null;
	}

}
