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
package io.sf.carte.echosvg.ext.awt.image.codec.png;

import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.renderable.DeferRable;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.RedRable;
import io.sf.carte.echosvg.ext.awt.image.rendered.CachableRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.FormatRed;
import io.sf.carte.echosvg.ext.awt.image.spi.MagicNumberRegistryEntry;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PNGRegistryEntry extends MagicNumberRegistryEntry {

	static final byte[] signature = { (byte) 0x89, 80, 78, 71, 13, 10, 26, 10 };

	public PNGRegistryEntry() {
		super("PNG", "png", "image/png", 0, signature);
	}

	/**
	 * Decode the Stream into a RenderableImage
	 *
	 * @param inIS       The input stream that contains the image.
	 * @param origURL    The original URL, if any, for documentation purposes only.
	 *                   This may be null.
	 * @param colorSpace The current working color space, or {@code null} if sRGB.
	 */
	@Override
	public Filter handleStream(InputStream inIS, ParsedURL origURL, ColorSpace colorSpace) {

		final DeferRable dr = new DeferRable();
		final InputStream is = inIS;

		Thread t = new Thread() {
			@Override
			public void run() {
				Filter filt;
				try {
					PNGDecodeParam param = new PNGDecodeParam();
					param.setExpandPalette(true);

					CachableRed cr = new PNGRed(is, param);
					dr.setBounds(new Rectangle2D.Double(0, 0, cr.getWidth(), cr.getHeight()));

					// Convert to RGB
					cr = GraphicsUtil.convertToRGB(cr, colorSpace);
					ColorSpace cs = cr.getColorModel().getColorSpace();
					ColorModel cm_Unpre;
					if (cs.isCS_sRGB() || cs.getType() != ColorSpace.TYPE_RGB) {
						cm_Unpre = GraphicsUtil.sRGB_Unpre;
					} else {
						cm_Unpre = new DirectColorModel(cs, 32,
								0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, false, DataBuffer.TYPE_INT);
					}
					cr = new FormatRed(cr, cm_Unpre);
					WritableRaster wr = (WritableRaster) cr.getData();
					ColorModel cm = cr.getColorModel();
					BufferedImage image;
					image = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
					cr = GraphicsUtil.wrap(image);
					filt = new RedRable(cr);
				} catch (IOException ioe) {
					filt = getFormatBrokenLinkImage(origURL);
				} catch (ThreadDeath td) {
					filt = getFormatBrokenLinkImage(origURL);
					dr.setSource(filt);
					throw td;
				} catch (Throwable t) {
					filt = getFormatMsgBrokenLinkImage(origURL, t);
				}

				dr.setSource(filt);
			}
		};
		t.start();

		return dr;
	}
}
