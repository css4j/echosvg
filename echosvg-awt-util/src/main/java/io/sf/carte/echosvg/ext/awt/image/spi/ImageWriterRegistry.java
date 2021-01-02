/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.ext.awt.image.spi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.sf.carte.echosvg.util.Service;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public final class ImageWriterRegistry {

	private static ImageWriterRegistry instance;

	private final Map<String, ImageWriter> imageWriterMap = new HashMap<>();

	private ImageWriterRegistry() {
		setup();
	}

	public static ImageWriterRegistry getInstance() {
		synchronized (ImageWriterRegistry.class) {
			if (instance == null) {
				instance = new ImageWriterRegistry();
			}
			return instance;
		}
	}

	private void setup() {
		Iterator<Object> iter = Service.providers(ImageWriter.class);
		while (iter.hasNext()) {
			ImageWriter writer = (ImageWriter) iter.next();
			// System.out.println("RE: " + writer);
			register(writer);
		}
	}

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
