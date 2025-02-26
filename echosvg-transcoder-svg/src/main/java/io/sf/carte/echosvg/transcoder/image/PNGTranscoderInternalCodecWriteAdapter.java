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

import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGEncodeParam;
import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGImageEncoder;
import io.sf.carte.echosvg.ext.awt.image.rendered.IndexImage;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.impl.ColorHelper;

/**
 * This class is a helper to <code>PNGTranscoder</code> that writes PNG images
 * through the internal PNG codec.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
class PNGTranscoderInternalCodecWriteAdapter implements PNGTranscoder.WriteAdapter {

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

		PNGEncodeParam params = PNGEncodeParam.getDefaultEncodeParam(img);
		if (params instanceof PNGEncodeParam.RGB) {
			((PNGEncodeParam.RGB) params).setBackgroundRGB(new int[] { 255, 255, 255 });
		}

		/*
		 * KEY_GAMMA is generally not needed because the encoders just look at the image
		 * and generate color-related chunks automatically. If KEY_GAMMA is specified
		 * with a value larger than '0', a gamma chunk (with a default chromaticity) is
		 * written. In that case, even if the space is sRGB no sRGB chunk is
		 * automatically generated.
		 */
		if (hints.containsKey(PNGTranscoder.KEY_GAMMA)) {
			float gamma = (Float) hints.get(PNGTranscoder.KEY_GAMMA);
			if (gamma > 0f) {
				params.setGamma(gamma);
				/* @formatter:off
				 * 
				 * Given that we set the gamma, let's do an attempt with the
				 * chromaticities (which have a lower chunk precedence than
				 * the sRGB and iCCP chunks).
				 * 
				 * cICP > iCCP > sRGB > cHRM/gAMA
				 * 
				 * @formatter:on
				 */
				ColorSpace cs = img.getColorModel().getColorSpace();
				params.setChromaticity(ColorHelper.defaultChromaticity(cs));
			}
		}

		// Compression level
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
		// number of pixels in 1 Meter
		int numPix = Math.round(resol / 0.0254f);
		params.setPhysicalDimension(numPix, numPix, 1); // 1 means 'pixels/meter'

		try {
			OutputStream ostream = output.getOutputStream();
			PNGImageEncoder pngEncoder = new PNGImageEncoder(ostream, params);
			pngEncoder.encode(img);
			ostream.flush();
		} catch (IOException ex) {
			throw new TranscoderException(ex);
		}
	}

}
