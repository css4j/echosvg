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
package io.sf.carte.echosvg.svggen;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;

/**
 * GenericImageHandler which caches JPEG images.
 *
 * @author <a href="mailto:paul_evenblij@compuware.com">Paul Evenblij</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class CachedImageHandlerJPEGEncoder extends DefaultCachedImageHandler {

	public static final String CACHED_JPEG_PREFIX = "jpegImage";
	public static final String CACHED_JPEG_SUFFIX = ".jpg";

	protected String refPrefix = "";

	/**
	 * @param imageDir directory where this handler should generate images. If null,
	 *                 an IllegalArgumentException is thrown.
	 * @param urlRoot  root for the urls that point to images created by this image
	 *                 handler. If null, then the url corresponding to imageDir is
	 *                 used.
	 */
	public CachedImageHandlerJPEGEncoder(String imageDir, String urlRoot) throws SVGGraphics2DIOException {
		refPrefix = urlRoot + "/";
		setImageCacher(new ImageCacher.External(imageDir, CACHED_JPEG_PREFIX, CACHED_JPEG_SUFFIX));
	}

	/**
	 * Uses JPEG encoding.
	 */
	@Override
	public void encodeImage(BufferedImage buf, OutputStream os) throws IOException {
		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/jpeg");
		ImageWriterParams params = new ImageWriterParams();
		params.setJPEGQuality(1, false);
		writer.writeImage(buf, os, params);
	}

	@Override
	public int getBufferedImageType() {
		return BufferedImage.TYPE_INT_RGB;
	}

	@Override
	public String getRefPrefix() {
		return refPrefix;
	}

}
