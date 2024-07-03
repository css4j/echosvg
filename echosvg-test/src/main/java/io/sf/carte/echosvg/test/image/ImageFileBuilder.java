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

/**
 * Create files derived from image filenames, according to provided prefixes and
 * suffixes.
 */
public interface ImageFileBuilder {

	/**
	 * Create a file with a name derived from the URL of a file, according to the
	 * given prefixes and suffixes.
	 * 
	 * @param imageUrl     the base file URL.
	 * @param filePrefix   the file prefix.
	 * @param fileSuffix   the file suffix.
	 * @param imageType    the image type (for example <code>_diff</code>).
	 * @param dotExtension the file extension, preceded by a dot (example:
	 *                     <code>.png</code>)
	 * @return the file.
	 * @throws IOException if it was determined that a file could not be created at
	 *                     the given place.
	 */
	File createImageFile(URL imageUrl, String filePrefix, String fileSuffix, String imageType,
			String dotExtension) throws IOException;

}
