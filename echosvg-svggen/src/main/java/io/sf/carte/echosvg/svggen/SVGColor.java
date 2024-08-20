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
package io.sf.carte.echosvg.svggen;

import java.awt.Color;
import java.awt.Paint;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.sf.carte.echosvg.ext.awt.g2d.GraphicContext;

/**
 * Utility class that converts a Color object into a set of corresponding SVG
 * attributes.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 * @see io.sf.carte.echosvg.svggen.DOMTreeManager
 */
public class SVGColor extends AbstractSVGConverter {

	/**
	 * Predefined CSS colors
	 */
	public static final Color aqua = Color.cyan;
	public static final Color black = Color.black;
	public static final Color blue = Color.blue;
	public static final Color fuchsia = Color.magenta;
	public static final Color gray = Color.gray;
	public static final Color green = new Color(0x00, 0x80, 0x00); // NOT Color.green!
	public static final Color lime = Color.green;
	public static final Color maroon = new Color(0x80, 0x00, 0x00);
	public static final Color navy = new Color(0x00, 0x00, 0x80);
	public static final Color olive = new Color(0x80, 0x80, 0x00);
	public static final Color purple = new Color(0x80, 0x00, 0x80);
	public static final Color red = Color.red;
	public static final Color silver = new Color(0xc0, 0xc0, 0xc0);
	public static final Color teal = new Color(0x00, 0x80, 0x80);
	public static final Color white = Color.white;
	public static final Color yellow = Color.yellow;

	private static final Set<String> cssProfileNames;

	/**
	 * Color map maps Color values to HTML 4.0 color names
	 */
	private static Map<Color, String> colorMap = new HashMap<>();

	static {
		colorMap.put(black, "black");
		colorMap.put(silver, "silver");
		colorMap.put(gray, "gray");
		colorMap.put(white, "white");
		colorMap.put(maroon, "maroon");
		colorMap.put(red, "red");
		colorMap.put(purple, "purple");
		colorMap.put(fuchsia, "fuchsia");
		colorMap.put(green, "green");
		colorMap.put(lime, "lime");
		colorMap.put(olive, "olive");
		colorMap.put(yellow, "yellow");
		colorMap.put(navy, "navy");
		colorMap.put(blue, "blue");
		colorMap.put(teal, "teal");
		colorMap.put(aqua, "aqua");

		/*
		 * CSS standard color spaces.
		 */
		String[] knownProfiles = { "display-p3", "a98-rgb", "prophoto-rgb", "rec2020" };
		cssProfileNames = new HashSet<>(knownProfiles.length);
		Collections.addAll(cssProfileNames, knownProfiles);
	}

	/**
	 * @param generatorContext used by converter to handle precision or to create
	 *                         elements.
	 */
	public SVGColor(SVGGeneratorContext generatorContext) {
		super(generatorContext);
	}

	/**
	 * Converts part or all of the input GraphicContext into a set of
	 * attribute/value pairs and related definitions
	 *
	 * @param gc GraphicContext to be converted
	 * @return descriptor of the attributes required to represent some or all of the
	 *         GraphicContext state, along with the related definitions
	 * @see io.sf.carte.echosvg.svggen.SVGDescriptor
	 */
	@Override
	public SVGDescriptor toSVG(GraphicContext gc) {
		Paint paint = gc.getPaint();
		return toSVG((Color) paint, getGeneratorContext());
	}

	/**
	 * Converts a Color object to a set of two corresponding values: a CSS color
	 * string and an opacity value.
	 */
	public static SVGPaintDescriptor toSVG(Color color, SVGGeneratorContext gc) {
		//
		// First, convert the color value
		//
		String cssColor = colorMap.get(color);
		if (cssColor == null) {
			// color is not one of the predefined colors
			cssColor = serializeColor(color);
		}

		//
		// Now, convert the alpha value, if needed
		//
		float alpha = color.getAlpha() / 255f;

		String alphaString = gc.doubleString(alpha);

		return new SVGPaintDescriptor(cssColor, alphaString);
	}

	private static String serializeColor(Color color) {
		StringBuilder cssColorBuffer;
		ColorSpace cs = color.getColorSpace();
		if (!cs.isCS_sRGB()) {
			float[] comps = color.getColorComponents(null);
			String csName;
			if (!(cs instanceof ICC_ColorSpace) || (csName = lcColorProfileName((ICC_ColorSpace) cs)) == null
					|| !cssProfileNames.contains(csName)) {
				// Not a known CSS color profile, let's use XYZ
				csName = "xyz-d50";
				comps = cs.toCIEXYZ(comps);
			}
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
			DecimalFormat df = new DecimalFormat("#.#", dfs);
			df.setMaximumFractionDigits(6);
			cssColorBuffer = new StringBuilder(csName.length() + 34);
			cssColorBuffer.append("color(").append(csName);
			for (float comp : comps) {
				cssColorBuffer.append(' ').append(df.format(comp));
			}
			cssColorBuffer.append(')');
		} else {
			cssColorBuffer = new StringBuilder(16);
			cssColorBuffer.append(RGB_PREFIX);
			cssColorBuffer.append(color.getRed());
			cssColorBuffer.append(COMMA);
			cssColorBuffer.append(color.getGreen());
			cssColorBuffer.append(COMMA);
			cssColorBuffer.append(color.getBlue());
			cssColorBuffer.append(RGB_SUFFIX);
		}
		return cssColorBuffer.toString();
	}

	private static String lcColorProfileName(ICC_ColorSpace cs) {
		ICC_Profile profile = cs.getProfile();
		byte[] bdesc = profile.getData(ICC_Profile.icSigProfileDescriptionTag);
		/*
		 * The profile description tag is of type multiLocalizedUnicodeType which starts
		 * with a 'mluc' (see paragraph 10.15 of ICC specification
		 * https://www.color.org/specification/ICC.1-2022-05.pdf).
		 */
		final byte[] mluc = { 'm', 'l', 'u', 'c' };
		String iccProfileName = null;
		if (bdesc != null && Arrays.equals(bdesc, 0, 4, mluc, 0, 4)) {
			int numrec = uInt32Number(bdesc, 8);
			if (numrec > 0) {
				int len = uInt32Number(bdesc, 20);
				int offset = uInt32Number(bdesc, 24);
				int maxlen = bdesc.length - offset;
				if (maxlen > 0) {
					len = Math.min(len, maxlen);
					// This isn't always the name of the color space, but let's try
					iccProfileName = new String(bdesc, offset, len, StandardCharsets.UTF_16BE).trim();
					iccProfileName = iccProfileName.toLowerCase(Locale.ROOT).replace(' ', '-');
					if (iccProfileName.contains("bt.2020")) {
						// ITU recommendation 2020
						iccProfileName = "rec2020";
					} else if ("romm".equals(iccProfileName)
							|| "largergb-elle-v4-g18.icc".equals(iccProfileName)
							|| "prophoto".equals(iccProfileName)) {
						// Names used in "freely available profiles" for Prophoto
						iccProfileName = "prophoto-rgb";
					} else if ("a98c".equals(iccProfileName)
							|| "adobe-rgb-(1998)".equals(iccProfileName) // the 'official' name
							|| "clayrgb-elle-v4-g22.icc".equals(iccProfileName)
							|| "adobe98".equals(iccProfileName)) {
						// A98
						iccProfileName = "a98-rgb";
					}
				}
			}
		}
		return iccProfileName;
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

}
