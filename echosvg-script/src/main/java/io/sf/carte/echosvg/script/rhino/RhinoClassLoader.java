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

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

import org.mozilla.javascript.GeneratedClassLoader;

/**
 * This class loader implementation will work whether or not the documentURL is
 * null.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RhinoClassLoader extends URLClassLoader implements GeneratedClassLoader {

	/**
	 * URL for the document referencing the script.
	 */
	protected URL documentURL;

	/**
	 * CodeSource for classes defined by this loader
	 */
	protected CodeSource codeSource;

	/**
	 * The AccessControlContext which can be associated with code loaded by this
	 * class loader if it was running stand-alone (i.e., not invoked by code with
	 * lesser priviledges).
	 */
	private Object rhinoAccessControlContext;

	/**
	 * Constructor.
	 * 
	 * @param documentURL the URL from which to load classes and resources
	 * @param parent      the parent class loader for delegation
	 */
	public RhinoClassLoader(URL documentURL, ClassLoader parent) {
		super(documentURL != null ? new URL[] { documentURL } : new URL[] {}, parent);
		this.documentURL = documentURL;
		if (documentURL != null) {
			codeSource = new CodeSource(documentURL, (Certificate[]) null);
		}

		//
		// Create the Rhino ProtectionDomain
		// and AccessControlContext
		//
		ProtectionDomain rhinoProtectionDomain = new ProtectionDomain(codeSource, getPermissions(codeSource));

		rhinoAccessControlContext = EchoSVGSecurityController.getInstance()
				.getAccessControlObject(rhinoProtectionDomain);
	}

	/**
	 * Helper, returns the URL array from the parent loader
	 */
	static URL[] getURL(ClassLoader parent) {
		if (parent instanceof RhinoClassLoader) {
			URL documentURL = ((RhinoClassLoader) parent).documentURL;
			if (documentURL != null) {
				return new URL[] { documentURL };
			} else {
				return new URL[] {};
			}
		} else {
			return new URL[] {};
		}
	}

	/**
	 * Define and load a Java class
	 */
	@Override
	public Class<?> defineClass(String name, byte[] data) {
		return super.defineClass(name, data, 0, data.length, codeSource);
	}

	/**
	 * Links the Java class.
	 */
	@Override
	public void linkClass(Class<?> clazz) {
		super.resolveClass(clazz);
	}

	/**
	 * Returns the access control object which should be associated with RhinoCode.
	 * 
	 * @return the access control object, or {@code null} if the JVM has no concept
	 *         of access control objects.
	 */
	public Object getAccessControlObject() {
		return rhinoAccessControlContext;
	}

	/**
	 * Returns the permissions for the given CodeSource object. Compared to
	 * URLClassLoader, this adds a FilePermission so that files under the same root
	 * directory as the document can be read.
	 */
	@Override
	protected PermissionCollection getPermissions(CodeSource codesource) {
		PermissionCollection perms = null;

		if (codesource != null) {
			perms = super.getPermissions(codesource);
		}

		if (documentURL != null && perms != null) {
			Permission p = null;
			Permission dirPerm = null;
			try {
				p = documentURL.openConnection().getPermission();
			} catch (IOException e) {
				p = null;
			}

			if (p instanceof FilePermission) {
				String path = p.getName();
				if (!path.endsWith(File.separator)) {
					// We are dealing with a file, as we would expect
					// from a document file URL
					int dirEnd = path.lastIndexOf(File.separator);
					if (dirEnd != -1) {
						// Include trailing file separator
						path = path.substring(0, dirEnd + 1);
						path += "-";
						dirPerm = new FilePermission(path, "read");
						perms.add(dirPerm);
					}
				}
			}
		}

		return perms;
	}

}
