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

package io.sf.carte.echosvg.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class represents a list of Java classes/packages to import into a
 * scripting environment.
 * <p>
 * It initializes itself by reading a file from the classpath
 * ({@code META_INF/imports/script.txt}).
 * </p>
 * <p>
 * The format of the file is as follows:
 * </p>
 * <ul>
 * <li>Anything after a '#' on a line is ignored.</li>
 *
 * <li>The first space delimited token on a line must be either '{@code class}'
 * or '{@code package}'.</li>
 *
 * <li>The remainder of a line is a whitespace delimited, fully qualified Java
 * class/package name (<i>e.g.</i> {@code java.lang.System}).</li>
 * </ul>
 * 
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ImportInfo {

	/**
	 * Default file to read imports from, can be overridden by setting the
	 * '{@code io.sf.carte.echosvg.script.imports'} System property.
	 */
	static final String defaultFile = "META-INF/imports/script.txt";

	static String importFile;
	static {
		importFile = defaultFile;
		try {
			importFile = System.getProperty("io.sf.carte.echosvg.script.imports", defaultFile);
		} catch (SecurityException se) {
		} catch (NumberFormatException nfe) {
		}
	}

	static ImportInfo defaultImports = null;

	/**
	 * Returns the default ImportInfo instance.
	 *
	 * This instance is initialized by reading the file identified by 'importFile'.
	 * 
	 * @return the default ImportInfo instance.
	 */
	public static ImportInfo getImports() {
		if (defaultImports == null)
			defaultImports = readImports();
		return defaultImports;
	}

	static ImportInfo readImports() {
		ImportInfo ret = new ImportInfo();

		// Can always request your own class loader. But it might be 'null'.
		ClassLoader cl = ImportInfo.class.getClassLoader();

		// No class loader so we can't find 'serviceFile'.
		if (cl == null)
			return ret;

		Enumeration<URL> e;
		try {
			e = cl.getResources(importFile);
		} catch (IOException ioe) {
			return ret;
		}

		while (e.hasMoreElements()) {
			try {
				URL url = e.nextElement();
				ret.addImports(url);
			} catch (Exception ex) {
				// Just try the next file...
				// ex.printStackTrace();
			}
		}

		return ret;
	}

	protected Set<String> classes;
	protected Set<String> packages;

	/**
	 * Construct an empty ImportInfo instance
	 */
	public ImportInfo() {
		classes = new HashSet<>();
		packages = new HashSet<>();
	}

	/**
	 * Return an unmodifiable iterator over the list of classes.
	 * 
	 * @return the classname iterator.
	 */
	public Iterator<String> getClasses() {
		return Collections.unmodifiableSet(classes).iterator();
	}

	/**
	 * Return an unmodifiable iterator over the list of packages
	 * 
	 * @return the package name iterator.
	 */
	public Iterator<String> getPackages() {
		return Collections.unmodifiableSet(packages).iterator();
	}

	/**
	 * Add a class to the set of classes to import.
	 * 
	 * @param cls the fully qualified class name (like {@code java.lang.System}).
	 */
	public void addClass(String cls) {
		classes.add(cls);
	}

	/**
	 * Add a package to the set of packages to import.
	 * 
	 * @param pkg the fully qualified package name (like {@code java.lang}).
	 */
	public void addPackage(String pkg) {
		packages.add(pkg);
	}

	/**
	 * Remove a class from the set of classes to import.
	 * 
	 * @param cls the fully qualified class name (like {@code java.lang.System}).
	 * @return true if the class was present.
	 */
	public boolean removeClass(String cls) {
		return classes.remove(cls);
	}

	/**
	 * Remove a package from the set of packages to import.
	 * 
	 * @param pkg the fully qualified package name (like {@code java.lang}).
	 * @return true if the package was present.
	 */
	public boolean removePackage(String pkg) {
		return packages.remove(pkg);
	}

	static final String classStr = "class";
	static final String packageStr = "package";

	/**
	 * Read from a URL and add imports into this {@code ImportInfo} instance.
	 * 
	 * <p>
	 * See the class documentation for the expected format of the file.
	 * </p>
	 * 
	 * @param src the URL of the {@code utf-8} encoded file to read from.
	 * @throws IOException
	 */
	public void addImports(URL src) throws IOException {
		try (InputStream is = src.openStream();
				Reader r = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(r)) {

			String line;
			while ((line = br.readLine()) != null) {
				// First strip any comment...
				int idx = line.indexOf('#');
				if (idx != -1)
					line = line.substring(0, idx);

				// Trim whitespace.
				line = line.trim();
				// If nothing left then loop around...
				if (line.length() == 0)
					continue;

				// Line must start with 'class ' or 'package '.
				idx = line.indexOf(' ');
				if (idx == -1)
					continue;

				String prefix = line.substring(0, idx);
				line = line.substring(idx + 1);
				boolean isPackage = packageStr.equals(prefix);
				boolean isClass = classStr.equals(prefix);

				if (!isPackage && !isClass)
					continue;

				while (line.length() != 0) {
					idx = line.indexOf(' ');
					String id;
					if (idx == -1) {
						id = line;
						line = "";
					} else {
						id = line.substring(0, idx);
						line = line.substring(idx + 1);
					}
					if (id.length() == 0)
						continue;

					if (isClass)
						addClass(id);
					else
						addPackage(id);
				}
			}
		}
	}

}
