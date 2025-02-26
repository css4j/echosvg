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
package io.sf.carte.echosvg.transcoder.impl;

import java.awt.color.ColorSpace;

import io.sf.carte.echosvg.ext.awt.color.StandardColorSpaces;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;

/**
 * Color Helper.
 */
public class ColorHelper {

	private static final float[] chroma_A98 = { 0.31270f, 0.329f, 0.64f, 0.33f, 0.21f, 0.71f, 0.15f, 0.06f };

	private static final float[] chroma_DisplayP3 = { 0.31270f, 0.329f, 0.68f, 0.32f, 0.265f, 0.69f, 0.15f,
			0.06f };

	private static final float[] chroma_Rec2020 = { 0.31270f, 0.329f, 0.708f, 0.292f, 0.170f, 0.797f, 0.131f,
			0.046f };

	private static final float[] chroma_ProphotoRGB = { 0.34570f, 0.3585f, 0.734699f, 0.265301f, 0.159597f,
			0.840403f, 0.036598f, 0.000105f };

	// Prevent instantiation
	private ColorHelper() {
		super();
	}

	/**
	 * Obtain the default chromaticity for the given color space, as expected by the
	 * PNG encoders.
	 * 
	 * @param cs the color space.
	 * @return the default chromaticity for that space, or the sRGB if that couldn't
	 *         be found.
	 */
	public static float[] defaultChromaticity(ColorSpace cs) {
		float[] chroma;

		if (cs == StandardColorSpaces.getA98RGB()) {
			chroma = ColorHelper.chroma_A98;
		} else if (cs == StandardColorSpaces.getDisplayP3()) {
			chroma = ColorHelper.chroma_DisplayP3;
		} else if (cs == StandardColorSpaces.getRec2020()) {
			chroma = ColorHelper.chroma_Rec2020;
		} else if (cs == StandardColorSpaces.getProphotoRGB()) {
			chroma = ColorHelper.chroma_ProphotoRGB;
		} else {
			chroma = PNGTranscoder.DEFAULT_CHROMA;
		}

		return chroma;
	}

}
