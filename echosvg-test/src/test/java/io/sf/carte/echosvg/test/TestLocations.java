/*
 * Copyright (c) 2020-2021 Carlos Amengual
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
 */
package io.sf.carte.echosvg.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestLocations {

	/**
	 * The project's root directory URL, with the last '/' included.
	 */
	public static final String PROJECT_ROOT_URL = getRootBuildURL();

	public static final String TEST_IMAGES_PATH = "/reports/tests/test/images";

	private static final String TEST_DIRNAME = "echosvg-test";

	private static String getRootBuildURL() {
		return java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<String>() {
			@Override
			public String run() {
				String resName = TestLocations.class.getName().replace(".", "/") + ".class";
				URL url = getClass().getClassLoader().getResource(resName);
				if (url == null) {
					return null;
				}
				String classUrl = url.toExternalForm();
				int testDirIdx = classUrl.lastIndexOf(TEST_DIRNAME);
				String rootDirUrl = classUrl.substring(0, testDirIdx);
				return rootDirUrl;
			}
		});
	}

	public static String getTestProjectBuildPath() {
		return java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<String>() {
			@Override
			public String run() {
				String resName = TestLocations.class.getName().replace(".", "/") + ".class";
				URL url = getClass().getClassLoader().getResource(resName);
				if (url == null) {
					return null;
				}
				String classUrl = url.toExternalForm();
				int testDirIdx = classUrl.lastIndexOf(TEST_DIRNAME);
				String buildDir = classUrl.substring(5, testDirIdx + TEST_DIRNAME.length()) + "/build/";
				return buildDir;
			}
		});
	}

	public static File getTempFilename(URL svgUrl, String filePrefix, String fileSuffix, String imageType,
			String dotExtension) throws IOException {
		String path = svgUrl.getPath();
		int idx = path.lastIndexOf('/');
		if (idx != -1) {
			int dotIndex = path.lastIndexOf('.');
			if (dotIndex == -1) {
				dotIndex = path.length();
			}
			String buildPath = getTestProjectBuildPath();
			if (buildPath != null) {
				File buildDir = new File(buildPath);
				if (buildDir.exists()) {
					File imgDir = new File(buildDir + TEST_IMAGES_PATH);
					if (imgDir.exists() || imgDir.mkdirs()) {
						return new File(imgDir, path.subSequence(idx + 1, dotIndex) + imageType + dotExtension);
					}
				}
			}
			return File.createTempFile(filePrefix,
					fileSuffix + path.subSequence(idx + 1, dotIndex) + imageType + dotExtension, null);
		}

		return File.createTempFile(filePrefix, fileSuffix + imageType + dotExtension, null);
	}

	public static String getProjectClassURL(Class<?> projectClass) {
		return java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<String>() {
			@Override
			public String run() {
				String resName = projectClass.getName().replace(".", "/") + ".class";
				String classUrl = projectClass.getClassLoader().getResource(resName).toExternalForm();
				String classdir;
				if (classUrl.endsWith(resName)) {
					classdir = classUrl.substring(0, classUrl.length() - resName.length());
				} else {
					classdir = null;
				}
				return classdir;
			}
		});
	}

}
