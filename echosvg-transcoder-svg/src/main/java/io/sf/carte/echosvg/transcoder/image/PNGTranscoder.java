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

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.OutputStream;

import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.image.resources.Messages;
import io.sf.carte.echosvg.transcoder.keys.FloatKey;
import io.sf.carte.echosvg.transcoder.keys.IntegerKey;
import io.sf.carte.echosvg.transcoder.keys.StringArrayKey;

/**
 * This class is an <code>ImageTranscoder</code> that produces a PNG image.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PNGTranscoder extends ImageTranscoder {

	/**
	 * Constructs a new transcoder that produces png images.
	 */
	public PNGTranscoder() {
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
		BufferedImage image;
		BridgeContext ctx = getBridgeContext();
		ColorSpace space;
		if (ctx == null || (space = ctx.getColorSpace()) == null) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		} else {
			int[] bits = { 16, 16, 16, 16 };
			ComponentColorModel cm = new ComponentColorModel(space, bits, true, false,
					Transparency.TRANSLUCENT, DataBuffer.TYPE_USHORT);
			WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
			image = new BufferedImage(cm, raster, false, null);
		}
		return image;
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
		if (ostream == null) {
			throw new TranscoderException(Messages.formatMessage("png.badoutput", null));
		}

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

		WriteAdapter adapter = createWriteAdapter();

		adapter.writeImage(this, img, output);
	}

	protected WriteAdapter createWriteAdapter() {
		return new PNGTranscoderInternalCodecWriteAdapter();
		// return new PNGTranscoderImageIOWriteAdapter();
	}

	// --------------------------------------------------------------------
	// PNG specific interfaces
	// --------------------------------------------------------------------

	/**
	 * This interface is used by <code>PNGTranscoder</code> to write PNG images
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
		void writeImage(PNGTranscoder transcoder, BufferedImage img, TranscoderOutput output)
				throws TranscoderException;

	}

	// --------------------------------------------------------------------
	// Keys definition
	// --------------------------------------------------------------------

	/**
	 * The gamma correction key.
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_GAMMA</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Float</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">PNGEncodeParam.INTENT_PERCEPTUAL</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Controls the gamma correction of the PNG image. A value of
	 * zero for gamma disables the generation of a gamma chunk. No value causes an
	 * sRGB chunk to be generated.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_GAMMA = new FloatKey();

	/**
	 * The default Primary Chromaticities for sRGB imagery.
	 */
	public static final float[] DEFAULT_CHROMA = { 0.31270F, 0.329F, 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F };

	/**
	 * The compression level.
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption>Compression level hint</caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_COMPRESSION_LEVEL</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Integer</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">Depends on the encoder. The default one uses
	 * <code>9</code> (the maximum) for compatibility with Batik, but the ImageIO
	 * <code>WriteAdapter</code> uses <code>4</code>.</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specifies the compression level used by the
	 * encoder (in the range 0-9, 9 being the maximum).</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_COMPRESSION_LEVEL = new IntegerKey();

	/**
	 * The color indexed image key to specify number of colors used in palette.
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_INDEXED</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Integer</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">none/true color image</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Turns on the reduction of the image to index colors by
	 * specifying color bit depth, 1, 2, 4 or 8. The resultant PNG will be an
	 * indexed PNG with color bit depth specified.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_INDEXED = new IntegerKey();

	/**
	 * An array of even length, specifying keyword-text pairs.
	 * <p>
	 * Intended for PNG's {@code tEXt} chunks.
	 * </p>
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption>Keyword-text hint</caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_KEYWORD_TEXT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String[]</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">An array of even length, specifying
	 * keyword-text pairs. Strings at index {@code 0} and even indexes are
	 * <a href="https://w3c.github.io/png/#11keywords">keywords</a> like "Title" or
	 * "Description", while the strings that follow at odd indexes are text that
	 * will be embedded without compression.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_KEYWORD_TEXT = new StringArrayKey();

	/**
	 * Internationalized text to embed in the image.
	 * <p>
	 * Intended for PNG's {@code iTXt} chunks.
	 * </p>
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption>Internationalized text hint</caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_INTERNATIONAL_TEXT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String[]</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">An array of length multiple of 4, specifying
	 * chunks consisting of a keyword, an
	 * <a href="https://www.rfc-editor.org/rfc/rfc5646">RFC 5646</a> language tag, a
	 * translation of the keyword according to that language, and then a text.
	 * Strings at index {@code 0} are
	 * <a href="https://w3c.github.io/png/#11keywords">keywords</a>, at {@code 1}
	 * are the language tag, at {@code 2} the translation, at {@code 3} the text,
	 * and so on. The text will be compressed.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_INTERNATIONAL_TEXT = new StringArrayKey();

	/**
	 * An array of even length, specifying keyword-text pairs. The text will be
	 * compressed.
	 * <p>
	 * Intended for PNG's {@code zTXt} chunks.
	 * </p>
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption>Compressed text hint</caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_COMPRESSED_TEXT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String[]</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">An array of even length, specifying
	 * keyword-text pairs. Strings at index {@code 0} and even indexes are
	 * <a href="https://w3c.github.io/png/#11keywords">keywords</a>, while the
	 * strings that follow at odd indexes are text that will be embedded with
	 * compression.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_COMPRESSED_TEXT = new StringArrayKey();

}
