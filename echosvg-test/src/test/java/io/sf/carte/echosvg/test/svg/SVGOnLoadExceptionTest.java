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
package io.sf.carte.echosvg.test.svg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.bridge.BaseScriptingEnvironment;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.DefaultExternalResourceSecurity;
import io.sf.carte.echosvg.bridge.DefaultScriptSecurity;
import io.sf.carte.echosvg.bridge.EmbededExternalResourceSecurity;
import io.sf.carte.echosvg.bridge.EmbededScriptSecurity;
import io.sf.carte.echosvg.bridge.ExternalResourceSecurity;
import io.sf.carte.echosvg.bridge.GVTBuilder;
import io.sf.carte.echosvg.bridge.NoLoadExternalResourceSecurity;
import io.sf.carte.echosvg.bridge.NoLoadScriptSecurity;
import io.sf.carte.echosvg.bridge.RelaxedExternalResourceSecurity;
import io.sf.carte.echosvg.bridge.RelaxedScriptSecurity;
import io.sf.carte.echosvg.bridge.ScriptSecurity;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.util.ApplicationSecurityEnforcer;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This test takes an SVG file as an input. It processes the input SVG (meaning
 * it turns it into a GVT tree) and then dispatches the 'onload' event.
 *
 * In that process, the test checks for the occurence of a specific exception
 * type and, for BridgeExceptions, for a given error code.
 *
 * If an exception of the given type (and, optionally, code) happens, then the
 * test passes. If an exception of an unexpected type (or code, for
 * BridgeExceptions) happens, or if no exception happens, the test fails.
 *
 * The following properties control the test's operation: - Scripts: list of
 * allowed script types (e.g., "application/java-archive") - ScriptOrigin:
 * "ANY", "DOCUMENT", "EMBEDED", "NONE" - ResourceOrigin: "ANY", "DOCUMENT",
 * "EMBEDED", "NONE" - ExpectedExceptionClass (e.g.,
 * "java.lang.SecurityException") - ExpectedErrorCode (e.g., "err.uri.unsecure")
 * - Validate (e.g., "true")
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@SuppressWarnings({ "deprecation", "removal" })
public class SVGOnLoadExceptionTest {
	/**
	 * Value for the script having successfully run.
	 */
	private static final String RAN = "ran";

	/**
	 * Test Namespace
	 */
	public static final String testNS = "https://css4j.github.io/echosvg/test";

	/**
	 * Location of test files in filesystem.
	 */
	private static final String FILE_DIR = "test-resources/io/sf/carte/echosvg/";

	private static final String SVG_EXTENSION = ".svg";

	/**
	 * The URL for the input SVG document to be tested
	 */
	protected String svgURL;

	/**
	 * The allowed script types
	 */
	protected String scripts = "text/ecmascript, application/java-archive";

	/**
	 * Name of the expected exception class
	 */
	protected String expectedExceptionClass = "io.sf.carte.echosvg.bridge.Exception";

	/**
	 * Expected error code (for BridgeExceptions)
	 */
	protected String expectedErrorCode = "none";

	/**
	 * The allowed script origin
	 */
	protected String scriptOrigin = "ANY";

	/**
	 * The allowed external resource origin
	 */
	protected String resourceOrigin = "ANY";

	/**
	 * True if the scripts are run securely (i.e., with a security manager)
	 */
	protected boolean secure = false;

	/**
	 * Controls whether or not the input SVG document should be validated
	 */
	protected Boolean validate = Boolean.FALSE;

	/**
	 * The name of the test file
	 */
	protected String fileName;

	private String description;

	/**
	 * Controls whether on not the document should be processed from a 'restricted'
	 * context, one with no createClassLoader permission.
	 */
	protected boolean restricted = false;

	public boolean getRestricted() {
		return restricted;
	}

	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}

	public void setId(String id) {
		if (id != null) {
			int i = id.indexOf("(");
			if (i != -1) {
				id = id.substring(0, i);
			}
			setDocumentURL(id);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setScripts(String scripts) {
		this.scripts = scripts;
	}

	public String getScripts() {
		return scripts;
	}

	public void setScriptOrigin(String scriptOrigin) {
		this.scriptOrigin = scriptOrigin;
	}

	public String getScriptOrigin() {
		return this.scriptOrigin;
	}

	public void setResourceOrigin(String resourceOrigin) {
		this.resourceOrigin = resourceOrigin;
	}

	public String getResourceOrigin() {
		return this.resourceOrigin;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public boolean getSecure() {
		return secure;
	}

	public void setExpectedExceptionClass(String expectedExceptionClass) {
		this.expectedExceptionClass = expectedExceptionClass;
	}

	public String getExpectedExceptionClass() {
		return this.expectedExceptionClass;
	}

	public void setExpectedErrorCode(String expectedErrorCode) {
		this.expectedErrorCode = expectedErrorCode;
	}

	public String getExpectedErrorCode() {
		return this.expectedErrorCode;
	}

	public Boolean getValidate() {
		return validate;
	}

	public void setValidate(Boolean validate) {
		this.validate = validate;
		if (this.validate == null) {
			this.validate = Boolean.FALSE;
		}
	}

	/**
	 * Default constructor
	 */
	public SVGOnLoadExceptionTest() {
	}

	/**
	 * Sets the document URI from a {@code id} If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 */
	private void setDocumentURL(String id) {
		final String resName = TestLocations.PROJECT_ROOT_URL + FILE_DIR + id + SVG_EXTENSION;
		URL urlRes;
		try {
			urlRes = new URL(resName);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(id);
		}
		fileName = urlRes.getFile();
		svgURL = urlRes.toExternalForm();
	}

	/**
	 * Resolves the input string as a URL. If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 */
	protected String resolveURL(String resourceURI) {
		final String resName = TestLocations.PROJECT_ROOT_URL + resourceURI;
		URL urlRes;
		try {
			urlRes = new URL(resName);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(resourceURI);
		}
		return urlRes.toExternalForm();
	}

	/**
	 * Run this test and produce a report. The test goes through the following
	 * steps:
	 * <ul>
	 * <li>load the input SVG into a Document</li>
	 * <li>build the GVT tree corresponding to the Document and dispatch the
	 * 'onload' event</li>
	 * </ul>
	 *
	 */
	public void runTest() throws Exception {
		ApplicationSecurityEnforcer ase = ApplicationSecurityEnforcer
				.createSecurityEnforcer(this.getClass(),
				"io/sf/carte/echosvg/apps/svgbrowser/svgbrowser.policy");

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
			if (!restricted) {
				testImpl();
				return;
			} else {
				// Emulate calling from restricted code. We create a
				// calling context with only the permission to read
				// the file.
				Policy policy = getPolicy();
				String classUri = TestLocations.getProjectClassURL(getClass());
				URL classesURL = new URL(classUri);
				CodeSource cs = new CodeSource(classesURL, (Certificate[]) null);
				PermissionCollection permissionsOrig = policy.getPermissions(cs);
				Permissions permissions = new Permissions();
				Enumeration<Permission> iter = permissionsOrig.elements();
				while (iter.hasMoreElements()) {
					Permission p = iter.nextElement();
					if (!(p instanceof RuntimePermission)) {
						if (!(p instanceof java.security.AllPermission)) {
							permissions.add(p);
						}
					} else {
						if (!"createClassLoader".equals(p.getName())) {
							permissions.add(p);
						}
					}
				}

				fileName = new File(fileName).getAbsolutePath();
				permissions.add(new FilePermission(fileName, "read"));
				permissions.add(new RuntimePermission("accessDeclaredMembers"));

				ProtectionDomain domain;
				AccessControlContext ctx;
				domain = new ProtectionDomain(null, permissions);
				ctx = new AccessControlContext(new ProtectionDomain[] { domain });

				try {
					AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
						@Override
						public Void run() throws Exception {
							testImpl();
							return null;
						}
					}, ctx);
				} catch (PrivilegedActionException pae) {
					throw pae.getException();
				}
			}
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

	private Policy getPolicy() {
		return java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<Policy>() {
			@Override
			public Policy run() {
				return Policy.getPolicy();
			}
		});
	}

	/**
	 * Implementation helper
	 * @throws Exception 
	 */
	protected void testImpl() throws IOException {
		//
		// First step:
		//
		// Load the input SVG into a Document object
		//
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory();
		f.setValidating(validate);
		Document doc;

		try {
			doc = createDocument(f);
		} catch (Exception e) {
			checkException(e);
			return;
		}

		//
		// Second step:
		//
		// Now that the SVG file has been loaded, build
		// a GVT Tree from it
		//
		TestUserAgent userAgent = buildUserAgent();
		GVTBuilder builder = new GVTBuilder();
		BridgeContext ctx = new BridgeContext(userAgent);
		ctx.setDynamic(true);
		Exception e = null;
		try {
			builder.build(ctx, doc);
			BaseScriptingEnvironment scriptEnvironment = new BaseScriptingEnvironment(ctx);
			scriptEnvironment.loadScripts();
			scriptEnvironment.dispatchSVGLoadEvent();
		} catch (RuntimeException ex) {
			e = ex;
		} finally {
			if (e == null && userAgent.e != null) {
				e = userAgent.e;
			}
		}
		if (e != null) {
			checkException(e);
			return;
		}

		//
		// If we got here, it means that an exception did not
		// happen. Check if this is expected.
		Element elem = doc.getElementById("testResult");
		String s = elem.getAttributeNS(null, "result");
		if (expectedExceptionClass == null) {
			// No error was expected then check that the script ran.
			assertEquals(RAN, s);
		} else {
			fail("Expected exception from " + fileName + ": " + expectedExceptionClass
					+ ", found no exception. Result: " + s);
		}
	}

	private Document createDocument(SAXSVGDocumentFactory f) throws Exception {
		try {
		return java.security.AccessController.doPrivileged(new java.security.PrivilegedExceptionAction<Document>() {
			@Override
			public Document run() throws IOException {
				return f.createDocument(svgURL);
			}
		});
		} catch (PrivilegedActionException e) {
			throw e.getException();
		}
	}

	private void checkException(Exception e) {
		String exname = e.getClass().getName();
		if (!exceptionMatches(e.getClass(), getExpectedExceptionClass())) {
			e.printStackTrace();
			assertEquals(getExpectedExceptionClass(), exname, "Unexpected exception from " + fileName + ',');
		}
	}

	/**
	 * Check if the input class' name (or one of its base classes) matches the input
	 * name.
	 */
	private static boolean exceptionMatches(final Class<?> cl, final String name) {
		if (cl == null) {
			return false;
		} else if (cl.getName().equals(name)) {
			return true;
		} else {
			return exceptionMatches(cl.getSuperclass(), name);
		}
	}

	/**
	 * Give subclasses a chance to build their own UserAgent
	 */
	protected TestUserAgent buildUserAgent() {
		return new TestUserAgent();
	}

	class TestUserAgent extends UserAgentAdapter {
		Exception e;

		@Override
		public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL resourceURL, ParsedURL docURL) {
			if ("ANY".equals(resourceOrigin)) {
				return new RelaxedExternalResourceSecurity(resourceURL, docURL);
			} else if ("DOCUMENT".equals(resourceOrigin)) {
				return new DefaultExternalResourceSecurity(resourceURL, docURL);
			} else if ("EMBEDED".equals(resourceOrigin)) {
				return new EmbededExternalResourceSecurity(resourceURL);
			} else {
				return new NoLoadExternalResourceSecurity();
			}
		}

		@Override
		public ScriptSecurity getScriptSecurity(String scriptType, ParsedURL scriptURL, ParsedURL docURL) {
			ScriptSecurity result = null;
			if (scripts.indexOf(scriptType) == -1) {
				result = new NoLoadScriptSecurity(scriptType);
			} else {
				if ("ANY".equals(scriptOrigin)) {
					result = new RelaxedScriptSecurity(scriptType, scriptURL, docURL);
				} else if ("DOCUMENT".equals(scriptOrigin)) {
					result = new DefaultScriptSecurity(scriptType, scriptURL, docURL);
				} else if ("EMBEDED".equals(scriptOrigin)) {
					result = new EmbededScriptSecurity(scriptType, scriptURL, docURL);
				} else {
					result = new NoLoadScriptSecurity(scriptType);
				}
			}
			return result;
		}

		@Override
		public void displayError(Exception e) {
			this.e = e;
		}
	}

}
