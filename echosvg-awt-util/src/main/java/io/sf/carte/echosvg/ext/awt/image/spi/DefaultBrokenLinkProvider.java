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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.RedRable;

/**
 * Default broken link provider.
 * 
 * @version $Id$
 */
public class DefaultBrokenLinkProvider extends AbstractBrokenLinkProvider {

	private static final Color BROKEN_LINK_COLOR = new Color(255, 255, 255, 190);

	@Override
	public Filter getBrokenLinkImage(Object base, String code, Object[] params) {
		BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

		String message = formatMessage(base, code, params);
		// Put the broken link property in the image so people know
		// This isn't the "real" image.
		Hashtable<String, String> ht = new Hashtable<>();
		ht.put(BROKEN_LINK_PROPERTY, message);
		bi = new BufferedImage(bi.getColorModel(), bi.getRaster(), bi.isAlphaPremultiplied(), ht);

		Graphics2D g2d = bi.createGraphics();

		g2d.setColor(BROKEN_LINK_COLOR);
		g2d.fillRect(0, 0, 100, 100);
		g2d.setColor(Color.black);
		g2d.drawRect(2, 2, 96, 96);
		Font hdrfont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		g2d.setFont(hdrfont);
		g2d.drawString("Broken Image", 6, 30);
		Font font = new Font(Font.SERIF, Font.PLAIN, 10);
		g2d.setFont(font);
		g2d.drawString(message, 5, 60);
		g2d.dispose();

		return new RedRable(GraphicsUtil.wrap(bi));
	}

}
