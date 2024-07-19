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
package io.sf.carte.echosvg.ext.awt.image.codec.imageio;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.Deflater;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import io.sf.carte.echosvg.ext.awt.image.codec.impl.ColorUtil;

/**
 * ImageWriter that encodes PNG images using Image I/O.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ImageIOPNGImageWriter extends ImageIOImageWriter {

	/**
	 * Main constructor.
	 */
	public ImageIOPNGImageWriter() {
		super("image/png");
	}

	@Override
	protected void updateColorMetadata(IIOMetadata meta, ColorSpace colorSpace) {
		if (!ColorUtil.isBuiltInColorSpace(colorSpace) && colorSpace instanceof ICC_ColorSpace
				&& meta.isStandardMetadataFormatSupported()) {
			final String metaName = "javax_imageio_png_1.0";
			IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(metaName);
			IIOMetadataNode iCCP = getChildNode(root, "iCCP");
			if (iCCP == null) {
				iCCP = new IIOMetadataNode("iCCP");
				root.appendChild(iCCP);
			}
			ICC_Profile profile = ((ICC_ColorSpace) colorSpace).getProfile();
			iCCP.setAttribute("profileName", profileName(profile));
			iCCP.setAttribute("compressionMethod", "deflate");
			iCCP.setUserObject(compressProfile(profile.getData()));
			try {
				meta.mergeTree(metaName, root);
			} catch (IIOInvalidTreeException e) {
				throw new RuntimeException("Cannot update image metadata.", e);
			}
		}
	}

	static String profileName(ICC_Profile profile) {
		byte[] bdesc = profile.getData(ICC_Profile.icSigProfileDescriptionTag);
		/*
		 * The profile description tag is of type multiLocalizedUnicodeType which starts
		 * with a 'mluc' (see paragraph 10.15 of ICC specification
		 * https://www.color.org/specification/ICC.1-2022-05.pdf).
		 */
		final byte[] mluc = { 'm', 'l', 'u', 'c' };
		if (bdesc != null && Arrays.equals(bdesc, 0, 4, mluc, 0, 4)) {
			int numrec = uInt32Number(bdesc, 8);
			if (numrec > 0) {
				int len = uInt32Number(bdesc, 20);
				int offset = uInt32Number(bdesc, 24);
				int maxlen = bdesc.length - offset;
				if (maxlen > 0) {
					len = Math.min(len, maxlen);
					return new String(bdesc, offset, len, StandardCharsets.UTF_16BE).trim();
				}
			}
		}
		return profile.getClass().getSimpleName();
	}

	/**
	 * Convert four bytes into a big-endian unsigned 32-bit integer.
	 * 
	 * @param bytes the array of bytes.
	 * @param offset the offset at which to start the conversion.
	 * @return the 32-bit integer.
	 */
	private static int uInt32Number(byte[] bytes, int offset) {
		// Computation is carried out as a long integer, to avoid potential overflows
		long value = (bytes[offset + 3] & 0xFF) | ((bytes[offset + 2] & 0xFF) << 8)
				| ((bytes[offset + 1] & 0xFF) << 16) | ((long) (bytes[offset] & 0xFF) << 24);
		return (int) value;
	}

	private static byte[] compressProfile(byte[] data) {
		// ByteArrayOutputStream for the compressed output
		ByteArrayOutputStream out = new ByteArrayOutputStream(data.length + 16);
		// Compress profile
		// Buffer size should be at least 6
		byte[] buffer = new byte[Math.min(data.length + 5, 1024)];
		Deflater defl = new Deflater(Deflater.BEST_COMPRESSION);
		defl.setInput(data);
		defl.finish();
		do {
			int cmprLen = defl.deflate(buffer);
			out.write(buffer, 0, cmprLen);
		} while (!defl.finished());
		defl.end();

		return out.toByteArray();
	}

}
