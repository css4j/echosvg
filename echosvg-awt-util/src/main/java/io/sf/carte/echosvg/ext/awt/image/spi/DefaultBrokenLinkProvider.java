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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.RedRable;
import io.sf.carte.echosvg.i18n.LocalizableSupport;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultBrokenLinkProvider extends BrokenLinkProvider {

	static Filter brokenLinkImg = null;
	static final String MESSAGE_RSRC = "resources.Messages";

	static final Color BROKEN_LINK_COLOR = new Color(255, 255, 255, 190);

	public static String formatMessage(Object base, String code, Object[] params) {
		// Should probably cache these...
		ClassLoader cl = null;
		try {
			// Should work always
			cl = DefaultBrokenLinkProvider.class.getClassLoader();
			// may not work (depends on security and relationship
			// of base's class loader to this class's class loader.
			cl = base.getClass().getClassLoader();
		} catch (SecurityException se) {
		}
		LocalizableSupport ls;
		ls = new LocalizableSupport(MESSAGE_RSRC, base.getClass(), cl);
		return ls.formatMessage(code, params);
	}

	@Override
	public Filter getBrokenLinkImage(Object base, String code, Object[] params) {
		synchronized (DefaultBrokenLinkProvider.class) {
			if (brokenLinkImg != null)
				return brokenLinkImg;

			BufferedImage bi;
			bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

			// Put the broken link property in the image so people know
			// This isn't the "real" image.
			Hashtable<String, String> ht = new Hashtable<>();
			ht.put(BROKEN_LINK_PROPERTY, formatMessage(base, code, params));
			bi = new BufferedImage(bi.getColorModel(), bi.getRaster(), bi.isAlphaPremultiplied(), ht);
			Graphics2D g2d = bi.createGraphics();

			g2d.setColor(BROKEN_LINK_COLOR);
			g2d.fillRect(0, 0, 100, 100);
			g2d.setColor(Color.black);
			g2d.drawRect(2, 2, 96, 96);
			g2d.drawString("Broken Image", 6, 50);
			g2d.dispose();

			brokenLinkImg = new RedRable(GraphicsUtil.wrap(bi));
			return brokenLinkImg;
		}
	}
}
