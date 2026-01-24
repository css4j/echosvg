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
package io.sf.carte.echosvg.test.image;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Produce image-based filenames.
 */
public class TempImageFiles implements ImageFileBuilder {

	private static final String TEST_IMAGES_PATH = "/reports/tests/test/images";

	private final String projectBuildURL;

	private final String imageSubpath;

	/**
	 * Instantiates a new temporary file utility, defaulting to a Gradle layout.
	 * 
	 * @param projectBuildURL the url to the project's build directory. You
	 *                        generally have to call
	 *                        {@link io.sf.carte.echosvg.test.TestUtil#getProjectBuildURL(Class, String)
	 *                        TestUtil.getProjectBuildURL(Class, String)} to obtain
	 *                        that. If {@code null}, then OS-supplied temporary
	 *                        files will be used.
	 */
	public TempImageFiles(String projectBuildURL) {
		this(projectBuildURL, TEST_IMAGES_PATH);
	}

	/**
	 * Instantiates a new temporary file utility.
	 * 
	 * @param projectBuildURL the url to the project's build directory. You
	 *                        generally have to call
	 *                        {@link io.sf.carte.echosvg.test.TestUtil#getProjectBuildURL(Class, String)
	 *                        TestUtil.getProjectBuildURL(Class, String)} to obtain
	 *                        that. If {@code null}, then OS-supplied temporary
	 *                        files will be used.
	 * @param imageSubpath    the subpath to images (for example in Gradle layout it
	 *                        would be <code>/reports/tests/test/images</code>).
	 */
	public TempImageFiles(String projectBuildURL, String imageSubpath) {
		super();
		this.projectBuildURL = projectBuildURL;
		this.imageSubpath = imageSubpath;
	}

	@Override
	public File createImageFile(URL imageUrl, CharSequence fileSuffix, CharSequence dotExtension)
			throws IOException {
		String path = imageUrl.getPath();
		int idx = path.lastIndexOf('/');
		if (idx != -1) {
			int dotIndex = path.lastIndexOf('.');
			if (dotIndex == -1) {
				dotIndex = path.length();
			}
			CharSequence imageName = path.subSequence(idx + 1, dotIndex);
			StringBuilder buf = new StringBuilder(
					imageName.length() + fileSuffix.length() + dotExtension.length());
			buf.append(imageName).append(fileSuffix).append(dotExtension);
			return createImageFile(buf.toString());
		}

		StringBuilder buf = new StringBuilder(path.length() + fileSuffix.length() + dotExtension.length());
		buf.append(path).append(fileSuffix).append(dotExtension);

		return Files.createTempFile("TempImageFiles", buf.toString()).toFile();
	}

	@Override
	public File createImageFile(String imageNameWithExtension) throws IOException {
		if (projectBuildURL != null) {
			File buildDir = new File(projectBuildURL);
			if (buildDir.exists()) {
				File imgDir = new File(buildDir + imageSubpath);
				if (imgDir.exists() || imgDir.mkdirs()) {
					return new File(imgDir, imageNameWithExtension);
				}
			}
		}

		return Files.createTempFile("TempImageFiles", imageNameWithExtension).toFile();
	}

}
