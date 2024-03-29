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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

/**
 * This test validates the PNGEncoder operation. It creates a BufferedImage,
 * then encodes it with the PNGEncoder, then decodes it and compares the decoded
 * image with the original one.
 *
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PNGEncoderTest {

	@Test
	public void test() throws Exception {
		// Create a BufferedImage to be encoded
		BufferedImage image = new BufferedImage(100, 75, BufferedImage.TYPE_INT_ARGB);
		Graphics2D ig = image.createGraphics();
		ig.scale(.5, .5);
		ig.setPaint(new Color(128, 0, 0));
		ig.fillRect(0, 0, 100, 50);
		ig.setPaint(Color.orange);
		ig.fillRect(100, 0, 100, 50);
		ig.setPaint(Color.yellow);
		ig.fillRect(0, 50, 100, 50);
		ig.setPaint(Color.red);
		ig.fillRect(100, 50, 100, 50);
		ig.setPaint(new Color(255, 127, 127));
		ig.fillRect(0, 100, 100, 50);
		ig.setPaint(Color.black);
		ig.draw(new Rectangle2D.Double(0.5, 0.5, 199, 149));
		ig.dispose();

		image = image.getSubimage(50, 0, 50, 25);

		// Create an output stream where the PNG data
		// will be stored.
		ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
		OutputStream os = buildOutputStream(bos);

		// Now, try to encode image
		PNGEncodeParam params = PNGEncodeParam.getDefaultEncodeParam(image);
		PNGImageEncoder pngImageEncoder = new PNGImageEncoder(os, params);

		pngImageEncoder.encode(image);
		os.close();

		// Now, try to decode image
		InputStream is = buildInputStream(bos);

		PNGImageDecoder pngImageDecoder = new PNGImageDecoder(is, new PNGDecodeParam());

		RenderedImage decodedRenderedImage = pngImageDecoder.decodeAsRenderedImage(0);

		BufferedImage decodedImage = null;
		if (decodedRenderedImage instanceof BufferedImage) {
			decodedImage = (BufferedImage) decodedRenderedImage;
		} else {
			decodedImage = new BufferedImage(decodedRenderedImage.getWidth(), decodedRenderedImage.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			ig = decodedImage.createGraphics();
			ig.drawRenderedImage(decodedRenderedImage, new AffineTransform());
			ig.dispose();
		}

		// Compare images
		assertTrue(checkIdentical(image, decodedImage), "Images are not identical");
	}

	/**
	 * Template method for building the PNG output stream. This gives a chance to
	 * sub-classes (e.g., Base64PNGEncoderTest) to add an additional encoding.
	 */
	protected OutputStream buildOutputStream(ByteArrayOutputStream bos) {
		return bos;
	}

	/**
	 * Template method for building the PNG input stream. This gives a chance to
	 * sub-classes (e.g., Base64PNGEncoderTest) to add an additional decoding.
	 */
	protected InputStream buildInputStream(ByteArrayOutputStream bos) {
		return new ByteArrayInputStream(bos.toByteArray());
	}

	/**
	 * Compares the data for the two images
	 */
	private static boolean checkIdentical(BufferedImage imgA, BufferedImage imgB) {
		boolean identical = true;
		if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
			int w = imgA.getWidth();
			int h = imgA.getHeight();
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					if (imgA.getRGB(j, i) != imgB.getRGB(j, i)) {
						identical = false;
						break;
					}
				}
				if (!identical) {
					break;
				}
			}
		}

		return identical;
	}

}
