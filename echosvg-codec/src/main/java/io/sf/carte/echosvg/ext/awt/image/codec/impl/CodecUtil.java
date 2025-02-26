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
package io.sf.carte.echosvg.ext.awt.image.codec.impl;

import java.awt.color.ICC_Profile;
import java.nio.charset.StandardCharsets;

/**
 * Methods useful for codecs.
 *
 * @version $Id$
 */
public class CodecUtil {

	/**
	 * sRGB chromaticities, as expected by PNG encoders.
	 */
	public static final float[] SRGB_CHROMA = { 0.31270F, 0.329F, 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F };

	/**
	 * Avoid instantiation.
	 */
	private CodecUtil() {
		super();
	}

	/**
	 * Checks whether {@code checkMe} starts with the contents of {@code expect}.
	 * 
	 * @param expect    the array with the expected elements.
	 * @param checkMe   the array to check.
	 * @param fromIndex the index of {@code checkMe} at which the check should
	 *                  start.
	 * @return {@code true} if {@code checkMe} starts with {@code expect}.
	 */
	private static boolean arrayStartsWith(byte[] expect, byte[] checkMe, int fromIndex) {
		if (checkMe.length - fromIndex < expect.length) {
			return false;
		}
		for (int i = 0; i < expect.length; i++) {
			if (expect[i] != checkMe[fromIndex + i]) {
				return false;
			}
		}
		return true;
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

	public static String getProfileName(ICC_Profile profile) {
		byte[] bdesc = profile.getData(ICC_Profile.icSigProfileDescriptionTag);
		/*
		 * The profile description tag is of type multiLocalizedUnicodeType which starts
		 * with a 'mluc' (see paragraph 10.15 of ICC specification
		 * https://www.color.org/specification/ICC.1-2022-05.pdf).
		 */
		final byte[] mluc = { 'm', 'l', 'u', 'c' };
		if (bdesc != null && arrayStartsWith(mluc, bdesc, 0)) {
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
		return null;
	}

}
