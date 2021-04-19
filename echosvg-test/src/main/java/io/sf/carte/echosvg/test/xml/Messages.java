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
package io.sf.carte.echosvg.test.xml;

import java.util.Locale;
import java.util.MissingResourceException;

import io.sf.carte.echosvg.i18n.LocalizableSupport;

/**
 * This class manages the message for the test.xml module.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class Messages {

	/**
	 * This class does not need to be instantiated.
	 */
	protected Messages() {
	}

	/**
	 * The error messages bundle class name.
	 */
	protected static final String RESOURCES = "io.sf.carte.echosvg.test.xml.resources.Messages";

	/**
	 * The localizable support for the error messages.
	 */
	protected static LocalizableSupport localizableSupport = new LocalizableSupport(RESOURCES);

	/**
	 * Implements {@link io.sf.carte.echosvg.i18n.Localizable#setLocale(Locale)}.
	 */
	public static void setLocale(Locale l) {
		localizableSupport.setLocale(l);
	}

	/**
	 * Implements {@link io.sf.carte.echosvg.i18n.Localizable#getLocale()}.
	 */
	public static Locale getLocale() {
		return localizableSupport.getLocale();
	}

	/**
	 * Implements
	 * {@link io.sf.carte.echosvg.i18n.Localizable#formatMessage(String,Object[])}.
	 */
	public static String formatMessage(String key, Object[] args) throws MissingResourceException {
		return localizableSupport.formatMessage(key, args);
	}
}
