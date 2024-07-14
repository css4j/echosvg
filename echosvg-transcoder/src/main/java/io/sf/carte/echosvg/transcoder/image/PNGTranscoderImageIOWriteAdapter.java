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
package io.sf.carte.echosvg.transcoder.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import io.sf.carte.echosvg.ext.awt.image.rendered.IndexImage;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;

/**
 * This class is a helper to <code>PNGTranscoder</code> that writes PNG images
 * through the Image I/O API.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
class PNGTranscoderImageIOWriteAdapter implements PNGTranscoder.WriteAdapter {

	/**
	 * @throws TranscoderException
	 * @see io.sf.carte.echosvg.transcoder.image.PNGTranscoder.WriteAdapter#writeImage(
	 *      io.sf.carte.echosvg.transcoder.image.PNGTranscoder,
	 *      java.awt.image.BufferedImage,
	 *      io.sf.carte.echosvg.transcoder.TranscoderOutput)
	 */
	@Override
	public void writeImage(PNGTranscoder transcoder, BufferedImage img, TranscoderOutput output)
			throws TranscoderException {

		TranscodingHints hints = transcoder.getTranscodingHints();

		int n = -1;
		if (hints.containsKey(PNGTranscoder.KEY_INDEXED)) {
			n = (Integer) hints.get(PNGTranscoder.KEY_INDEXED);
			if (n == 1 || n == 2 || n == 4 || n == 8)
				// PNGEncodeParam.Palette can handle these numbers only.
				img = IndexImage.getIndexedImage(img, 1 << n);
		}

		ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/png");
		ImageWriterParams params = new ImageWriterParams();

		float PixSzMM = transcoder.getUserAgent().getPixelUnitToMillimeter();
		int PixSzInch = (int) (25.4 / PixSzMM + 0.5);
		params.setResolution(PixSzInch);

		try {
			OutputStream ostream = output.getOutputStream();
			writer.writeImage(img, ostream, params);
			ostream.flush();
		} catch (IOException ex) {
			throw new TranscoderException(ex);
		}
	}

}
