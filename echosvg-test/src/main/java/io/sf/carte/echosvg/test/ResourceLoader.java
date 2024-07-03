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
package io.sf.carte.echosvg.test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;

import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Load resources with/without a SecurityManager.
 *
 * @version $Id$
 */
public class ResourceLoader {

	private static final ResourceLoader singleton = createResourceLoader();

	private static ResourceLoader createResourceLoader() {
		ResourceLoader loader;
		try {
			Class<?> cl = Class.forName("io.sf.carte.echosvg.test.SMResourceLoader");
			Constructor<?> ctor = cl.getConstructor();
			loader = (ResourceLoader) ctor.newInstance();
		} catch (Exception e) {
			loader = new ResourceLoader();
		}
		return loader;
	}

	public static ResourceLoader getInstance() {
		return singleton;
	}

	public URL getResource(Class<?> cl, String resourceName) {
		return cl.getClassLoader().getResource(resourceName);
	}

	public InputStream openStream(ParsedURL purl) throws IOException {
		return purl.openStream();
	}

	public InputStream getResourceAsStream(Class<?> cl, String resourceName) {
		return cl.getResourceAsStream(resourceName);
	}

}
