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
import java.awt.image.SinglePixelPackedSampleModel;

import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.imageio.TIFFTranscoderImageIOWriteAdapter;
import io.sf.carte.echosvg.transcoder.keys.StringKey;

/**
 * This class is an <code>ImageTranscoder</code> that produces a TIFF image.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TIFFTranscoder extends ImageTranscoder {

	/**
	 * Constructs a new transcoder that produces tiff images.
	 */
	public TIFFTranscoder() {
		hints.put(KEY_FORCE_TRANSPARENT_WHITE, Boolean.FALSE);
	}

	/**
	 * Creates a new ARGB image with the specified dimension.
	 * 
	 * @param width  the image width in pixels
	 * @param height the image height in pixels
	 */
	@Override
	public BufferedImage createImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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

		//
		// This is a trick so that viewers which do not support the alpha
		// channel will see a white background (and not a black one).
		//
		boolean forceTransparentWhite = false;

		if (hints.containsKey(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE)) {
			forceTransparentWhite = (Boolean) hints.get(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE);
		}

		if (forceTransparentWhite) {
			SinglePixelPackedSampleModel sppsm;
			sppsm = (SinglePixelPackedSampleModel) img.getSampleModel();
			forceTransparentWhite(img, sppsm);
		}

		WriteAdapter adapter = new TIFFTranscoderImageIOWriteAdapter();

		adapter.writeImage(this, img, output);
	}

	// --------------------------------------------------------------------
	// TIFF specific interfaces
	// --------------------------------------------------------------------

	/**
	 * This interface is used by <code>TIFFTranscoder</code> to write TIFF images
	 * through different codecs.
	 *
	 * @version $Id$
	 */
	public interface WriteAdapter {

		/**
		 * Writes the specified image to the specified output.
		 * 
		 * @param transcoder the calling PNGTranscoder
		 * @param img        the image to write
		 * @param output     the output where to store the image
		 * @throws TranscoderException if an error occured while storing the image
		 */
		void writeImage(TIFFTranscoder transcoder, BufferedImage img, TranscoderOutput output)
				throws TranscoderException;

	}

	// --------------------------------------------------------------------
	// Keys definition
	// --------------------------------------------------------------------

	/**
	 * The forceTransparentWhite key.
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_FORCE_TRANSPARENT_WHITE</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">false</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">It controls whether the encoder should force the image's
	 * fully transparent pixels to be fully transparent white instead of fully
	 * transparent black. This is useful when the encoded TIFF is displayed in a
	 * viewer which does not support TIFF transparency and lets the image display
	 * with a white background instead of a black background. <br>
	 * However, note that the modified image will display differently over a white
	 * background in a viewer that supports transparency.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_FORCE_TRANSPARENT_WHITE = ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE;

	/**
	 * The compression method for the image.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_COMPRESSION_METHOD</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String ("none", "packbits", "jpeg" etc.)</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">"none" (no compression)</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">Recommended</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the compression method used to encode the
	 * image.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_COMPRESSION_METHOD = new StringKey();

}
