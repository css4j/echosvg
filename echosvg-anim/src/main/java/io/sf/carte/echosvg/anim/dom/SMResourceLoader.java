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
package io.sf.carte.echosvg.anim.dom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.PrivilegedActionException;

import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Load resources with/without a SecurityManager.
 *
 * @version $Id$
 */
class SMResourceLoader extends ResourceLoader {

	@Override
	@SuppressWarnings({ "deprecation", "removal" })
	URL getResource(Class<?> cl, String resourceName) {
		return java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<URL>() {
			@Override
			public URL run() {
				return cl.getResource(resourceName);
			}
		});
	}

	@Override
	@SuppressWarnings({ "deprecation", "removal" })
	InputStream openStream(ParsedURL purl) throws IOException, DOMException {
		try {
			return java.security.AccessController
					.doPrivileged(new java.security.PrivilegedExceptionAction<InputStream>() {
						@Override
						public InputStream run() throws IOException {
							return purl.openStream();
						}
					});
		} catch (PrivilegedActionException e) {
			DOMException ex = new DOMException(DOMException.INVALID_STATE_ERR, e.getMessage());
			ex.initCause(e);
			throw ex;
		}
	}

}
