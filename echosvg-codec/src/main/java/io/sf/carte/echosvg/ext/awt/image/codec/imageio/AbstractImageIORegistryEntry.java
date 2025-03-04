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
package io.sf.carte.echosvg.ext.awt.image.codec.imageio;

import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.renderable.DeferRable;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.RedRable;
import io.sf.carte.echosvg.ext.awt.image.rendered.CachableRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.FormatRed;
import io.sf.carte.echosvg.ext.awt.image.spi.MagicNumberRegistryEntry;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This is the base class for all ImageIO-based RegistryEntry implementations.
 * <p>
 * They have a slightly lower priority than the RegistryEntry implementations
 * using the internal codecs, so these take precedence if they are available.
 * </p>
 *
 * @version $Id$
 */
public abstract class AbstractImageIORegistryEntry extends MagicNumberRegistryEntry {

	/**
	 * Constructor
	 * 
	 * @param name         Format Name
	 * @param exts         Standard set of extensions
	 * @param magicNumbers array of magic numbers any of which can match.
	 */
	public AbstractImageIORegistryEntry(String name, String[] exts, String[] mimeTypes, MagicNumber[] magicNumbers) {
		super(name, PRIORITY + 100, exts, mimeTypes, magicNumbers);
	}

	/**
	 * Constructor, simplifies construction of entry when only one extension and one
	 * magic number is required.
	 * 
	 * @param name        Format Name
	 * @param ext         Standard extension
	 * @param offset      Offset of magic number
	 * @param magicNumber byte array to match.
	 */
	public AbstractImageIORegistryEntry(String name, String ext, String mimeType, int offset, byte[] magicNumber) {
		super(name, PRIORITY + 100, ext, mimeType, offset, magicNumber);
	}

	/**
	 * Decode the Stream into a RenderableImage
	 *
	 * @param inIS        The input stream that contains the image.
	 * @param origURL     The original URL, if any, for documentation purposes only.
	 *                    This may be null.
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
					Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(getMimeTypes().get(0));
					if (!iter.hasNext()) {
						throw new UnsupportedOperationException(
								"No image reader for " + getFormatName() + " available!");
					}
					ImageReader reader = iter.next();
					ImageInputStream imageIn = ImageIO.createImageInputStream(is);
					reader.setInput(imageIn, true);

					int imageIndex = 0;
					dr.setBounds(
							new Rectangle2D.Double(0, 0, reader.getWidth(imageIndex), reader.getHeight(imageIndex)));
					CachableRed cr;
					// Naive approach possibly wasting lots of memory
					// and ignoring the gamma correction done by PNGRed :-(
					// Matches the code used by the former JPEGRegistryEntry, though.
					BufferedImage bi = reader.read(imageIndex);
					cr = GraphicsUtil.wrap(bi);
					cr = GraphicsUtil.convertToRGB(cr, colorSpace);
					ColorSpace cs = cr.getColorModel().getColorSpace();
					ColorModel cm_Unpre;
					if (cs.isCS_sRGB() || cs.getType() != ColorSpace.TYPE_RGB) {
						cm_Unpre = GraphicsUtil.sRGB_Unpre;
					} else {
						cm_Unpre = new DirectColorModel(cr.getColorModel().getColorSpace(), 32,
								0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000, false, DataBuffer.TYPE_INT);
					}
					cr = new FormatRed(cr, cm_Unpre);
					WritableRaster wr = (WritableRaster) cr.getData();
					ColorModel cm = cr.getColorModel();
					BufferedImage image = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
					cr = GraphicsUtil.wrap(image);
					filt = new RedRable(cr);
				} catch (IOException ioe) {
					// Something bad happened here...
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
