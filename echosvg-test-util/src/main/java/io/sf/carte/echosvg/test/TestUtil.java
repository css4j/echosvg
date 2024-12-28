/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.sf.carte.echosvg.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class TestUtil {

	private TestUtil() {
	}

	/**
	 * Get the URL of the root project directory.
	 * 
	 * @param cl             a class provided by the project.
	 * @param projectDirname the directory name of the subproject from which this is
	 *                       executed.
	 * @return the URL.
	 */
	public static String getRootProjectURL(Class<?> cl, String projectDirname) {
		String resName = cl.getName().replace(".", "/") + ".class";
		URL url = ResourceLoader.getInstance().getResource(cl, resName);
		if (url == null) {
			url = cwdURL();
		}
		String sUrl = url.toExternalForm();
		int testDirIdx = sUrl.lastIndexOf(projectDirname);
		if (testDirIdx != -1) {
			sUrl = sUrl.substring(0, testDirIdx);
		} // If no projectDirname, we probably got the root via CWD
		return sUrl;
	}

	private static URL cwdURL() {
		try {
			return Paths.get(".").toAbsolutePath().normalize().toUri().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Get the URL of the Gradle-style project build directory.
	 * 
	 * @param cl             a class provided by the project.
	 * @param projectDirname the directory name of the subproject from which this is
	 *                       executed.
	 * @return the URL.
	 */
	public static String getProjectBuildURL(Class<?> cl, String projectDirname) {
		String resName = cl.getName().replace(".", "/") + ".class";
		URL url = ResourceLoader.getInstance().getResource(cl, resName);
		String classUrl;
		if (url == null) {
			url = cwdURL();
			File f = new File(url.getFile(), projectDirname);
			if (f.exists()) {
				// CWD is root directory
				try {
					url = new URL(url.getProtocol(), url.getHost(), url.getPort(), f.getAbsolutePath());
				} catch (MalformedURLException e) {
					return null;
				}
				classUrl = url.toExternalForm();
			} else {
				// CWD is the project directory instead of root
				classUrl = url.toExternalForm();
				if (classUrl.lastIndexOf(projectDirname) == -1) {
					return null;
				}
			}
		} else {
			classUrl = url.toExternalForm();
		}
		int testDirIdx = classUrl.lastIndexOf(projectDirname);
		String buildDir = classUrl.substring(5, testDirIdx + projectDirname.length()) + "/build/";
		return buildDir;
	}

}
