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

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.PNGImageWriterParams;

/**
 * ImageWriter implementation that uses EchoSVG's PNG codec to write PNG files.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PNGImageWriter implements ImageWriter {

	/**
	 * @see ImageWriter#writeImage(java.awt.image.RenderedImage,
	 *      java.io.OutputStream)
	 */
	@Override
	public void writeImage(RenderedImage image, OutputStream out) throws IOException {
		writeImage(image, out, null);
	}

	@Override
	public void writeImage(RenderedImage image, OutputStream out, ImageWriterParams params)
			throws IOException {
		PNGEncodeParam param = null;

		if (params != null) {
			Integer level = params.getCompressionLevel();
			if (level != null) {
				param = PNGEncodeParam.getDefaultEncodeParam(image);
				param.setCompressionLevel(level.intValue());
			}

			if (params instanceof PNGImageWriterParams) {
				param = encodePNGParam(image, (PNGImageWriterParams) params, param);
			}
		}

		PNGImageEncoder encoder = new PNGImageEncoder(out, param);
		encoder.encode(image);
	}

	private PNGEncodeParam encodePNGParam(RenderedImage image, PNGImageWriterParams params,
			PNGEncodeParam param) {
		if (params.isTextSet()) {
			if (param == null) {
				param = PNGEncodeParam.getDefaultEncodeParam(image);
			}
			param.setText(params.getText());
		}

		if (params.isCompressedTextSet()) {
			if (param == null) {
				param = PNGEncodeParam.getDefaultEncodeParam(image);
			}
			param.setCompressedText(params.getCompressedText());
		}

		if (params.isInternationalTextSet()) {
			if (param == null) {
				param = PNGEncodeParam.getDefaultEncodeParam(image);
			}
			param.setInternationalText(params.getInternationalText());
		}

		return param;
	}

	/**
	 * @see ImageWriter#getMIMEType()
	 */
	@Override
	public String getMIMEType() {
		return "image/png";
	}
}
