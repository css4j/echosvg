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

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import io.sf.carte.echosvg.ext.awt.color.StandardColorSpaces;
import io.sf.carte.echosvg.ext.awt.image.rendered.IndexImage;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.ext.awt.image.spi.PNGImageWriterParams;
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
public class PNGTranscoderImageIOWriteAdapter implements PNGTranscoder.WriteAdapter {

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
		PNGImageWriterParams params = new PNGImageWriterParams();

		// If they specify GAMMA key with a value of '0' then omit
		// gamma chunk. If they do not provide a GAMMA then just
		// generate an sRGB chunk. Otherwise suppress the sRGB chunk
		// and just generate gamma and chroma chunks.
		if (hints.containsKey(PNGTranscoder.KEY_GAMMA)) {
			float gamma = (Float) hints.get(PNGTranscoder.KEY_GAMMA);
			if (gamma > 0f) {
				params.setGamma(gamma);
			}
			ColorSpace cs = img.getColorModel().getColorSpace();
			setDefaultChromaticity(params, cs);
		} else {
			// We generally want an sRGB chunk and our encoding intent
			// is perceptual
			params.setSRGBIntent(PNGImageWriterParams.INTENT_PERCEPTUAL);
		}

		Integer level = (Integer) hints.get(PNGTranscoder.KEY_COMPRESSION_LEVEL);
		if (level != null) {
			params.setCompressionLevel(level);
		}

		// tEXt
		String[] text = (String[]) hints.get(PNGTranscoder.KEY_KEYWORD_TEXT);
		if (text != null) {
			params.setText(text);
		}

		// iTXt
		text = (String[]) hints.get(PNGTranscoder.KEY_INTERNATIONAL_TEXT);
		if (text != null) {
			params.setInternationalText(text);
		}

		// zTXt
		text = (String[]) hints.get(PNGTranscoder.KEY_COMPRESSED_TEXT);
		if (text != null) {
			params.setCompressedText(text);
		}

		float resol = transcoder.getUserAgent().getResolution();
		params.setResolution(Math.round(resol));

		try {
			OutputStream ostream = output.getOutputStream();
			writer.writeImage(img, ostream, params);
			ostream.flush();
		} catch (IOException ex) {
			throw new TranscoderException(ex);
		}
	}

	private void setDefaultChromaticity(PNGImageWriterParams params, ColorSpace cs) {
		/* @formatter:off
		 * 
		 * For sRGB, set the sRGB chunk. For the other spaces
		 * the iCCP chunk should suffice, but in case that the
		 * decoder doesn't support that we set the gamma and the
		 * chromaticities (which have a lower chunk precedence).
		 * 
		 * cICP > iCCP > sRGB > cHRM/gAMA
		 * 
		 * @formatter:on
		 */
		if (cs == StandardColorSpaces.getA98RGB()) {
			float[] chroma = { 0.31270f, 0.329f, 0.64f, 0.33f, 0.21f, 0.71f, 0.15f, 0.06f };
			params.setChromaticity(chroma);
		} else if (cs == StandardColorSpaces.getDisplayP3()) {
			float[] chroma = { 0.31270f, 0.329f, 0.68f, 0.32f, 0.265f, 0.69f, 0.15f, 0.06f };
			params.setChromaticity(chroma);
		} else if (cs == StandardColorSpaces.getRec2020()) {
			float[] chroma = { 0.31270f, 0.329f, 0.708f, 0.292f, 0.170f, 0.797f, 0.131f, 0.046f };
			params.setChromaticity(chroma);
		} else if (cs == StandardColorSpaces.getProphotoRGB()) {
			float[] chroma = { 0.34570f, 0.3585f, 0.734699f, 0.265301f, 0.159597f, 0.840403f, 0.036598f,
					0.000105f };
			params.setChromaticity(chroma);
		} else {
			params.setChromaticity(PNGTranscoder.DEFAULT_CHROMA);
		}
	}

}
