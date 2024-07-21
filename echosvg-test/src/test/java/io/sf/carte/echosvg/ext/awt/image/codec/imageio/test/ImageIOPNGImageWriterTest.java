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

package io.sf.carte.echosvg.ext.awt.image.codec.imageio.test;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOPNGImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.PNGImageWriterParams;

/**
 * This test validates the ImageIOPNGImageWriter operation.
 * 
 * @version $Id$
 */
public class ImageIOPNGImageWriterTest extends AbstractImageWriterCheck {

	private static final int width = 200;

	private static final int height = 150;

	@Test
	public void testRGBa() throws Exception {
		BufferedImage image = drawImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		testEncoding(image, "rgba");
	}

	@Test
	public void testICC() throws IOException {
		ICC_Profile prof;
		try (InputStream iccStream = ImageIOPNGImageWriterTest.class.getResourceAsStream(
				"/io/sf/carte/echosvg/css/color/profiles/Display P3.icc")) {
			prof = ICC_Profile.getInstance(iccStream);
		}
		ICC_ColorSpace cs = new ICC_ColorSpace(prof);

		int[] bits = { 16, 16, 16, 16 };
		int[] offsets = { 0, 1, 2, 3 };

		ComponentColorModel cm = new ComponentColorModel(cs, bits, true, false,
				Transparency.TRANSLUCENT, DataBuffer.TYPE_USHORT);
		ComponentSampleModel sm = new ComponentSampleModel(DataBuffer.TYPE_USHORT, width, height,
				bits.length, width * bits.length, offsets);
		Point loc = new Point(0, 0);
		WritableRaster raster = Raster.createWritableRaster(sm, loc);

		BufferedImage raw = new BufferedImage(cm, raster, false, null);
		BufferedImage image = drawImage(raw);

		testEncoding(image, "icc");
	}

	@Override
	protected PNGImageWriterParams createImageWriterParams() {
		return new PNGImageWriterParams();
	}

	@Override
	protected ImageWriter createImageWriter() {
		return new ImageIOPNGImageWriter();
	}

	@Override
	protected String getDotExtension() {
		return ".png";
	}

	@Override
	protected String getMIMEType() {
		return "image/png";
	}

}
