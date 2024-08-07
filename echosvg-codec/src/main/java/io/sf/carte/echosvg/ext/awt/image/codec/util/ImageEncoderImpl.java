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

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A partial implementation of the ImageEncoder interface useful for
 * subclassing.
 *
 * @version $Id$
 */
public abstract class ImageEncoderImpl implements ImageEncoder {

	/** The OutputStream associcted with this ImageEncoder. */
	protected OutputStream output;

	/**
	 * Constructs an ImageEncoderImpl with a given OutputStream.
	 */
	protected ImageEncoderImpl(OutputStream output) {
		this.output = output;
	}

	/** Returns the OutputStream associated with this ImageEncoder. */
	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	/**
	 * Encodes a Raster with a given ColorModel and writes the output to the
	 * OutputStream associated with this ImageEncoder.
	 */
	@Override
	public void encode(Raster ras, ColorModel cm) throws IOException {
		RenderedImage im = new SingleTileRenderedImage(ras, cm);
		encode(im);
	}

	/**
	 * Encodes a <code>RenderedImage</code> and writes the output to the
	 * <code>OutputStream</code> associated with this <code>ImageEncoder</code>.
	 * <p>
	 * The stream into which the image is dumped is not closed at the end of the
	 * operation, this must be done by the caller of this method.
	 * </p>
	 * 
	 * @param im the image to encode.
	 */
	@Override
	public abstract void encode(RenderedImage im) throws IOException;
}
