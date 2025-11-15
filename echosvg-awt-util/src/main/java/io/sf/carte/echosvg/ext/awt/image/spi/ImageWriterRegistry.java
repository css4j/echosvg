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
package io.sf.carte.echosvg.ext.awt.image.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * {@link ImageWriter} registry.
 * 
 * @version $Id$
 */
public final class ImageWriterRegistry {

	private static final ImageWriterRegistry instance = new ImageWriterRegistry();

	private final Map<String, ImageWriter> imageWriterMap = new HashMap<>(10);

	private ImageWriterRegistry() {
		setup();
	}

	/**
	 * Get the instance of the registry.
	 * 
	 * @return the registry instance.
	 */
	public static ImageWriterRegistry getInstance() {
		return instance;
	}

	private void setup() {
		ServiceLoader<ImageWriter> loader = ServiceLoader.load(ImageWriter.class);

		for (ImageWriter writer : loader) {
			register(writer);
		}
	}

	/**
	 * Register a new writer.
	 * 
	 * @param writer the writer.
	 */
	public void register(ImageWriter writer) {
		imageWriterMap.put(writer.getMIMEType(), writer);
	}

	/**
	 * get the ImageWriter registered for mime, or null.
	 * 
	 * @param mime used for lookup
	 * @return the registered ImageWriter (maybe null)
	 */
	public ImageWriter getWriterFor(String mime) {
		return imageWriterMap.get(mime);
	}

}
