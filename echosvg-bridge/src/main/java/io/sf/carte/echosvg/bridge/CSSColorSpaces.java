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
package io.sf.carte.echosvg.bridge;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InputStream;

/**
 * Predefined ICC color spaces.
 */
class CSSColorSpaces {

	private static ICC_ColorSpace a98rgb;

	private static ICC_ColorSpace prophotoRGB;

	private static ICC_ColorSpace displayP3;

	private static ICC_ColorSpace rec2020;

	CSSColorSpaces() {
		super();
	}

	public static ICC_ColorSpace getA98RGB() {
		ICC_Profile prof;
		if (a98rgb == null) {
			try (InputStream is = CSSColorSpaces.class.getResourceAsStream(
					"color/profiles/A98RGBCompat-v4.icc")) {
				prof = ICC_Profile.getInstance(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			a98rgb = new ICC_ColorSpace(prof);
		}
		return a98rgb;
	}

	public static ICC_ColorSpace getProphotoRGB() {
		ICC_Profile prof;
		if (prophotoRGB == null) {
			try (InputStream is = CSSColorSpaces.class.getResourceAsStream(
					"color/profiles/WideGamutPhoto-v4.icc")) {
				prof = ICC_Profile.getInstance(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			prophotoRGB = new ICC_ColorSpace(prof);
		}
		return prophotoRGB;
	}

	public static ICC_ColorSpace getDisplayP3() {
		ICC_Profile prof;
		if (displayP3 == null) {
			try (InputStream is = CSSColorSpaces.class.getResourceAsStream(
					"color/profiles/Display P3.icc")) {
				prof = ICC_Profile.getInstance(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			displayP3 = new ICC_ColorSpace(prof);
		}
		return displayP3;
	}

	public static ICC_ColorSpace getRec2020() {
		ICC_Profile prof;
		if (rec2020 == null) {
			try (InputStream is = CSSColorSpaces.class.getResourceAsStream(
					"color/profiles/ITU-R_BT2020.icc")) {
				prof = ICC_Profile.getInstance(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			rec2020 = new ICC_ColorSpace(prof);
		}
		return rec2020;
	}

	/**
	 * Merge the two color spaces.
	 * 
	 * @param colorSpace1 the first color space, or {@code null}.
	 * @param colorSpace2 the second color space. Cannot be equal to colorSpace1
	 *                    (check that before calling).
	 * @param color       the color that needs to be represented by the merged
	 *                    space.
	 * @return the recommended merged color space, or {@code null} if sRGB is recommended.
	 */
	static ColorSpace mergeColorSpace(ColorSpace colorSpace1, ColorSpace colorSpace2, Color color) {
		// For a reasoning, you may want to look at
		// https://upload.wikimedia.org/wikipedia/commons/b/b3/CIE1931xy_gamut_comparison_of_sRGB_P3_Rec2020.svg
		if (colorSpace1 == null) {
			if (isInGamut(color, ColorSpace.getInstance(ColorSpace.CS_sRGB))
					|| colorSpace2 == ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB)) {
				return null;
			}
			return colorSpace2;
		}

		if (colorSpace2 == getProphotoRGB()) {
			return colorSpace2;
		}

		// Prophoto contains nearly all of the other gamuts, first check for it.
		if (colorSpace1 == getProphotoRGB() || isInGamut(color, colorSpace1)) {
			return colorSpace1;
		}

		// colorSpace1 is either rec2020, A98RGB or P3.
		// colorSpace2 must be rec2020, A98RGB or P3.
		// rec2020 is the best, smallest merge.
		ICC_ColorSpace rec = getRec2020();
		if (colorSpace1 != rec) {
			if (isInGamut(color, rec)) {
				return rec;
			}
		}

		return getProphotoRGB();
	}

	/**
	 * Determine which color space is the most adequate to represent the given
	 * color, starting with sRGB and then giving precedence to the supplied color
	 * space over others.
	 * 
	 * @param xyz        the color to check, often in the XYZ color space.
	 * @param colorSpace the suggested color space.
	 * @return the recommended color space, or {@code null} if it is sRGB.
	 */
	static ColorSpace containerRGBSpace(Color xyz, ColorSpace colorSpace) {
		// Verify whether xyz is in sRGB gamut, otherwise check P3, A98, rec2020, prophoto.
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		if (isInGamut(xyz, cs)) {
			return null;
		}
		if (colorSpace != null) {
			if (isInGamut(xyz, colorSpace)) {
				return colorSpace;
			}
		}
		// Check P3
		cs = getDisplayP3();
		if (colorSpace != cs) {
			if (isInGamut(xyz, cs)) {
				return cs;
			}
		}
		// Check A98
		cs = getA98RGB();
		if (colorSpace != cs) {
			if (isInGamut(xyz, cs)) {
				return cs;
			}
		}
		// Check rec2020
		cs = getRec2020();
		if (colorSpace != cs) {
			if (isInGamut(xyz, cs)) {
				return cs;
			}
		}
		return getProphotoRGB();
	}

	/**
	 * Check whether the color is in the gamut of the given color space.
	 * <p>
	 * This method is only approximate.
	 * </p>
	 * 
	 * @param color the color to check.
	 * @param space the color space. Cannot be {@code null}.
	 * @return {@code false} if the color may not be representable in the given
	 *         color space.
	 */
	private static boolean isInGamut(Color color, ColorSpace space) {
		float[] comps = color.getColorComponents(space, null);
		float min = space.getMinValue(0);
		float max = space.getMaxValue(0);

		for (float comp : comps) {
			if (comp <= min || comp >= max) {
				// Symptom that the color is at the edge of the gamut
				return false;
			}
		}

		return true;
	}

}
