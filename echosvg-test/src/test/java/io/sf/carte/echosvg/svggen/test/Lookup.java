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
package io.sf.carte.echosvg.svggen.test;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupTable;
import java.net.URL;

/**
 * This test validates the convertion of Java 2D LookupOp into an SVG filer.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class Lookup implements Painter {

	@Override
	public void paint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//
		// Load Image
		//
		URL url = getClass().getClassLoader().getResource("io/sf/carte/echosvg/svggen/resources/vangogh.png");
		Image image = Toolkit.getDefaultToolkit().createImage(url);
		MediaTracker tracker = new MediaTracker(new Button(""));
		tracker.addImage(image, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			tracker.removeImage(image);
			image = null;
		} finally {
			if (image != null)
				tracker.removeImage(image);
			if (tracker.isErrorAny())
				image = null;
			if (image != null) {
				if (image.getWidth(null) < 0 || image.getHeight(null) < 0)
					image = null;
			}
		}

		if (image == null) {
			throw new RuntimeException("Could not load image");
		}

		BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D ig = bi.createGraphics();
		ig.drawImage(image, 0, 0, null);

		byte[] lookup = new byte[256];
		for (int i = 0; i < 256; i++)
			lookup[i] = (byte) (255 - i);

		LookupTable table = new ByteLookupTable(0, lookup);
		java.awt.image.LookupOp inverter = new java.awt.image.LookupOp(table, null);

		// Simply paint the image without and with the lookup filter
		g.setPaint(Color.black);
		g.drawString("Normal / Inverted", 10, 20);
		g.drawImage(image, 10, 30, null);
		g.drawImage(bi, inverter, 10 + bi.getWidth() + 10, 30);
	}

}
