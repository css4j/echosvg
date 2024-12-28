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
package io.sf.carte.echosvg.test.misc;

import java.net.MalformedURLException;
import java.net.URL;

import io.sf.carte.echosvg.test.ResourceLoader;
import io.sf.carte.echosvg.test.TestUtil;

public class TestLocations {

	/**
	 * The name of this project
	 */
	public static final String TEST_DIRNAME = "echosvg-test";

	/**
	 * The project's root directory URL, with the last '/' included.
	 */
	public static final String PROJECT_ROOT_URL = TestUtil.getRootProjectURL(TestLocations.class, TEST_DIRNAME);

	/**
	 * Test location utility.
	 */
	private TestLocations() {
		super();
	}

	public static String getTestProjectBuildURL() {
		String resName = TestLocations.class.getName().replace(".", "/") + ".class";
		URL url = ResourceLoader.getInstance().getResource(TestLocations.class, resName);
		if (url == null) {
			return null;
		}
		String classUrl = url.toExternalForm();
		int testDirIdx = classUrl.lastIndexOf(TEST_DIRNAME);
		String buildDir = classUrl.substring(5, testDirIdx + TEST_DIRNAME.length()) + "/build/";
		return buildDir;
	}

	public static String getProjectClassURL(Class<?> projectClass) {
		String resName = projectClass.getName().replace(".", "/") + ".class";
		URL url = ResourceLoader.getInstance().getResource(projectClass, resName);
		if (url == null) {
			return null;
		}
		String strUrl = url.toExternalForm();
		String classdir;
		if (strUrl.endsWith(resName)) {
			classdir = strUrl.substring(0, strUrl.length() - resName.length());
		} else {
			classdir = null;
		}
		return classdir;
	}

	/**
	 * Resolves the input string as  a URL.
	 * 
	 * @throws MalformedURLException if the argument is not recognized as a URL.
	 */
	public static URL resolveURL(String url) throws MalformedURLException {
		if (url.startsWith("file:")) {
			return new URL(url);
		}
		return new URL(PROJECT_ROOT_URL + url);
	}

}
