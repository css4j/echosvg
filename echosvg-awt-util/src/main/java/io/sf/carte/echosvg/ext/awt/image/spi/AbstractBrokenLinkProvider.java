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

import io.sf.carte.echosvg.i18n.LocalizableSupport;

/**
 * Abstract broken link provider.
 * 
 * @version $Id$
 */
public abstract class AbstractBrokenLinkProvider implements BrokenLinkProvider {

	private static final String MESSAGE_RSRC = "resources.Messages";

	protected static String formatMessage(Object base, String code, Object[] params) {
		ClassLoader cl = null;
		try {
			// Should work always
			cl = AbstractBrokenLinkProvider.class.getClassLoader();
			// may not work (depends on security and relationship
			// of base's class loader to this class's class loader.
			cl = base.getClass().getClassLoader();
		} catch (SecurityException se) {
		}
		LocalizableSupport ls;
		ls = new LocalizableSupport(MESSAGE_RSRC, base.getClass(), cl);
		return ls.formatMessage(code, params);
	}

}
