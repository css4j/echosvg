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
package io.sf.carte.echosvg.ext.awt.image.codec.util;

import java.util.MissingResourceException;

import io.sf.carte.echosvg.i18n.LocalizableSupport;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PropertyUtil {
	protected static final String RESOURCES = "io.sf.carte.echosvg.bridge.resources.properties";

	protected static LocalizableSupport localizableSupport = new LocalizableSupport(RESOURCES,
			PropertyUtil.class.getClassLoader());

	public static String getString(String key) {
		try {
			return localizableSupport.formatMessage(key, null);
		} catch (MissingResourceException e) {
			return key;
		}
	}
}
