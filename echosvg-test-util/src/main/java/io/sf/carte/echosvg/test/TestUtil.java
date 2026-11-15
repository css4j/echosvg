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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
	public static URI getRootProjectURI(Class<?> cl, String projectDirname) {
		URI url = rootProjectURI(cl);
		String sUrl = url.getPath();
		int testDirIdx = sUrl.lastIndexOf(projectDirname);
		if (testDirIdx != -1) {
			sUrl = sUrl.substring(0, testDirIdx);
		} else {
			// If no projectDirname, we probably got the root via CWD
			return url;
		}

		try {
			return new URI(url.getScheme(), url.getHost(), sUrl, null);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Get the root project directory.
	 * 
	 * @param cl             a class provided by the project.
	 * @param projectDirname the directory name of the subproject from which this is
	 *                       executed.
	 * @return the directory.
	 */
	public static Path getRootProjectPath(Class<?> cl, String projectDirname) {
		URI url = rootProjectURI(cl);
		String sUrl = url.getPath();
		int testDirIdx = sUrl.lastIndexOf(projectDirname);
		if (testDirIdx != -1) {
			sUrl = sUrl.substring(0, testDirIdx);
		} else {
			// If no projectDirname, we probably got the root via CWD
			return Paths.get(url);
		}

		try {
			return Paths.get(new java.net.URI(url.getScheme(), url.getHost(), sUrl, null));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static URI rootProjectURI(Class<?> cl) {
		String resName = cl.getName().replace(".", "/") + ".class";
		URL url = ResourceLoader.getInstance().getResource(cl, resName);
		if (url == null) {
			return cwdURI().toUri();
		}
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static Path cwdURI() {
		return Paths.get(".").toAbsolutePath().normalize();
	}

	/**
	 * Get the URL of the Gradle-style project build directory.
	 * 
	 * @param cl             a class provided by the project.
	 * @param projectDirname the directory name of the subproject from which this is
	 *                       executed.
	 * @return the URL.
	 */
	public static Path getProjectBuildPath(Class<?> cl, String projectDirname) {
		String resName = cl.getName().replace(".", "/") + ".class";
		URL url = ResourceLoader.getInstance().getResource(cl, resName);
		URI uri;
		if (url == null) {
			Path cwd = cwdURI();
			Path f = cwd.resolve(projectDirname);
			if (!Files.exists(f)) {
				// CWD is the project directory instead of root
				if (cwd.toString().lastIndexOf(projectDirname) == -1) {
					return null;
				}
			} // CWD is root directory
			uri = f.toUri();
		} else {
			try {
				uri = url.toURI();
			} catch (URISyntaxException e) {
				throw new IllegalStateException(e);
			}
		}
		String classPath = uri.getPath();
		int testDirIdx = classPath.lastIndexOf(projectDirname);
		String projectDir = classPath.substring(0, testDirIdx + projectDirname.length());
		try {
			uri = new URI(uri.getScheme(), projectDir + "/build", null);
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
		return Paths.get(uri);
	}

	/**
	 * Save {@code data} to a destination file.
	 * 
	 * @param data            the data to save.
	 * @param destinationFile the file where data has to be stored.
	 * @throws IOException
	 */
	public static void save(byte[] data, Path destinationFile) throws IOException {
		if (destinationFile == null) {
			return;
		}

		Path parentDir = destinationFile.getParent();
		if (!Files.exists(parentDir)) {
			Files.createDirectories(parentDir);
		}

		try (OutputStream os = Files.newOutputStream(destinationFile)) {
			os.write(data);
		}
	}

}
