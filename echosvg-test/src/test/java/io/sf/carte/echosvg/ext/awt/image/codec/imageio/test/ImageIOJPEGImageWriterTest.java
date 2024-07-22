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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.image.codec.imageio.ImageIOJPEGImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.test.image.ImageFileBuilder;
import io.sf.carte.echosvg.test.image.TempImageFiles;

/**
 * This test validates the ImageIOJPEGImageWriter operation.
 * 
 * @version $Id$
 */
public class ImageIOJPEGImageWriterTest extends AbstractImageWriterCheck {

	private static final int width = 200;

	private static final int height = 150;

	@Test
	public void test3B_BGR() throws IOException {
		BufferedImage image = drawImage(new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR));
		testEncoding(image, "3bBgr");
	}

	@Test
	public void testICC() throws IOException {
		ICC_Profile prof;
		try (InputStream iccStream = ImageIOJPEGImageWriterTest.class.getResourceAsStream(
				"/io/sf/carte/echosvg/css/color/profiles/Display P3.icc")) {
			prof = ICC_Profile.getInstance(iccStream);
		}
		ICC_ColorSpace cs = new ICC_ColorSpace(prof);

		int[] bits = { 8, 8, 8 };
		int[] offsets = { 2, 1, 0 };

		ComponentColorModel cm = new ComponentColorModel(cs, bits, false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		ComponentSampleModel sm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, width, height,
				3, width * 3, offsets);
		Point loc = new Point(0, 0);
		WritableRaster raster = Raster.createWritableRaster(sm, loc);

		BufferedImage raw = new BufferedImage(cm, raster, false, null);
		BufferedImage image = drawImage(raw);

		testEncoding(image, "icc");
	}

	@Override
	protected ImageWriter createImageWriter() {
		return new ImageIOJPEGImageWriter();
	}

	@Override
	protected void configureImageWriterParams(ImageWriterParams params) {
		params.setJPEGQuality(1f, false);
	}

	@Override
	protected boolean equalStreams(byte[] cand, String baseName) throws IOException {
		/*
		 * Do a loose comparison here
		 */

		// First read the reference stream
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
		try (InputStream isRef = openRefStream(baseName)) {
			if (isRef == null) {
				// No reference
				ImageFileBuilder tmpUtil = new TempImageFiles(
						TestUtil.getProjectBuildURL(getClass(), TestLocations.TEST_DIRNAME));
				bytesToFile(cand, tmpUtil, baseName, "_candidate");
				throw new FileNotFoundException("Cannot find reference resource at " + resourcePath(baseName));
			}

			byte[] buffer = new byte[512];
			int count;
			while ((count = isRef.read(buffer)) != -1) {
				bos.write(buffer, 0, count);
			}
		}

		byte[] ref = bos.toByteArray();

		// Check the differences
		int lendiff = Math.abs(ref.length - cand.length);

		if (lendiff == 0 && Arrays.equals(ref, 0, ref.length, cand, 0, ref.length)) {
			return true; // Exact equality
		}

		int allowance = ref.length / 2048;

		if (lendiff > allowance) {
			System.err.println("Difference in file lengths is too big: " + lendiff + " (allowed "
					+ allowance + ").");
			return false;
		}

		// Check the first 42 bytes
		int len = Math.min(ref.length - allowance, 42);
		if (Arrays.equals(ref, 0, len, cand, 0, len)) {
			return true;
		}

		System.err.println("The first " + len + " bytes do not match.");
		return false;
	}

	@Override
	protected String getDotExtension() {
		return ".jpg";
	}

	@Override
	protected String getMIMEType() {
		return "image/jpeg";
	}

}
