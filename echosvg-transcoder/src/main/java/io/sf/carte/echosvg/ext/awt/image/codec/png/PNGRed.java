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

import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.rendered.AbstractRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.CachableRed;

/**
 * PNG CachableRed.
 * 
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PNGRed extends AbstractRed {

	private final PNGImage pngImage;

	public PNGRed(InputStream stream) throws IOException {
		this(stream, null);
	}

	public PNGRed(InputStream stream, PNGDecodeParam decodeParam) throws IOException {
		pngImage = new MyPNGImage(stream, decodeParam);
	}

	class MyPNGImage extends PNGImage {

		public MyPNGImage(InputStream input, PNGDecodeParam param) throws IOException {
			super(input, param);
		}

		@Override
		protected void onEnd() {
			Rectangle bounds = new Rectangle(0, 0, width, height);
			init((CachableRed) null, bounds, colorModel, sampleModel, 0, 0, properties);
		}

	}

	@Override
	public WritableRaster copyData(WritableRaster wr) {
		GraphicsUtil.copyData(pngImage.theTile, wr);
		return wr;
	}

	// RenderedImage stuff
	@Override
	public Raster getTile(int tileX, int tileY) {
		return pngImage.getTile(tileX, tileY);
	}

}
