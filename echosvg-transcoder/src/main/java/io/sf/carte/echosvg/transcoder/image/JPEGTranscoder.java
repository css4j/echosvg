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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.image.resources.Messages;

/**
 * This class is an <code>ImageTranscoder</code> that produces a JPEG image.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JPEGTranscoder extends ImageTranscoder {

	/**
	 * Constructs a new transcoder that produces jpeg images.
	 */
	public JPEGTranscoder() {
		hints.put(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.white);
	}

	/**
	 * Creates a new ARGB image with the specified dimension.
	 * 
	 * @param width  the image width in pixels
	 * @param height the image height in pixels
	 */
	@Override
	public BufferedImage createImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Writes the specified image to the specified output.
	 * 
	 * @param img    the image to write
	 * @param output the output where to store the image
	 * @throws TranscoderException if an error occured while storing the image
	 */
	@Override
	public void writeImage(BufferedImage img, TranscoderOutput output) throws TranscoderException {
		OutputStream ostream = output.getOutputStream();
		// The outputstream wrapper protects the JPEG encoder from
		// exceptions due to stream closings. If it gets an exception
		// it nulls out the stream and just ignores any future calls.
		ostream = new OutputStreamWrapper(ostream);

		try {
			float quality;
			if (hints.containsKey(KEY_QUALITY)) {
				quality = (Float) hints.get(KEY_QUALITY);
			} else {
				TranscoderException te;
				te = new TranscoderException(Messages.formatMessage("jpeg.unspecifiedQuality", null));
				handler.error(te);
				quality = 0.75f;
			}

			ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/jpeg");
			ImageWriterParams params = new ImageWriterParams();
			params.setJPEGQuality(quality, true);
			float PixSzMM = getUserAgent().getPixelUnitToMillimeter();
			int PixSzInch = (int) (25.4 / PixSzMM + 0.5);
			params.setResolution(PixSzInch);
			writer.writeImage(img, ostream, params);
			ostream.flush();
		} catch (IOException ex) {
			throw new TranscoderException(ex);
		}
	}

	// --------------------------------------------------------------------
	// Keys definition
	// --------------------------------------------------------------------

	/**
	 * The encoder quality factor key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_QUALITY</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Float (between 0 and 1)</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">0.75 (lossy)</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">Recommended</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the JPEG image encoding quality.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_QUALITY = new QualityKey();

	/**
	 * A transcoding Key represented the JPEG image quality.
	 */
	private static class QualityKey extends TranscodingHints.Key {
		@Override
		public boolean isCompatibleValue(Object v) {
			if (v instanceof Float) {
				float q = (Float) v;
				return (q > 0 && q <= 1.0f);
			} else {
				return false;
			}
		}
	}

	/**
	 * This class will never throw an IOException, instead it eats them and then
	 * ignores any future calls to it's interface.
	 */
	private static class OutputStreamWrapper extends OutputStream {
		OutputStream os;

		/**
		 * Constructs a wrapper around <code>os</code> that will not throw IOExceptions.
		 * <@param os>The Stream to wrap.
		 */
		OutputStreamWrapper(OutputStream os) {
			this.os = os;
		}

		@Override
		public void close() throws IOException {
			if (os == null)
				return;
			try {
				os.close();
			} catch (IOException ioe) {
				os = null;
			}
		}

		@Override
		public void flush() throws IOException {
			if (os == null)
				return;
			try {
				os.flush();
			} catch (IOException ioe) {
				os = null;
			}
		}

		@Override
		public void write(byte[] b) throws IOException {
			if (os == null)
				return;
			try {
				os.write(b);
			} catch (IOException ioe) {
				os = null;
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			if (os == null)
				return;
			try {
				os.write(b, off, len);
			} catch (IOException ioe) {
				os = null;
			}
		}

		@Override
		public void write(int b) throws IOException {
			if (os == null)
				return;
			try {
				os.write(b);
			} catch (IOException ioe) {
				os = null;
			}
		}
	}
}
