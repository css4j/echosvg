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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.anim.dom.SVGOMDocument;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.FontFaceRule;
import io.sf.carte.echosvg.gvt.font.GVTFontFace;
import io.sf.carte.echosvg.gvt.font.GVTFontFamily;
import io.sf.carte.echosvg.gvt.font.UnresolvedFontFamily;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Utility class for SVG fonts.
 *
 * @author <a href="mailto:bella.robinson@cmis.csiro.au">Bella Robinson</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGFontUtilities implements SVGConstants {

	public static List<FontFace> getFontFaces(Document doc, BridgeContext ctx) {
		// check fontFamilyMap to see if we have already created an
		// FontFamily that matches
		Map<Object, Object> fontFamilyMap = ctx.getFontFamilyMap();
		@SuppressWarnings("unchecked")
		List<FontFace> ret = (List<FontFace>) fontFamilyMap.get(doc);
		if (ret != null)
			return ret;

		ret = new LinkedList<>();

		NodeList fontFaceElements = doc.getElementsByTagNameNS(SVG_NAMESPACE_URI, SVG_FONT_FACE_TAG);

		SVGFontFaceElementBridge fontFaceBridge;
		fontFaceBridge = (SVGFontFaceElementBridge) ctx.getBridge(SVG_NAMESPACE_URI, SVG_FONT_FACE_TAG);

		for (int i = 0; i < fontFaceElements.getLength(); i++) {
			Element fontFaceElement = (Element) fontFaceElements.item(i);
			ret.add(fontFaceBridge.createFontFace(ctx, fontFaceElement));
		}

		CSSEngine engine = ((SVGOMDocument) doc).getCSSEngine();
		List<FontFaceRule> sms = engine.getFontFaces();
		for (FontFaceRule ffr : sms) {
			ret.add(CSSFontFace.createCSSFontFace(engine, ffr));
		}
		return ret;
	}

	/**
	 * Given a font family name tries to find a matching SVG font object. If finds
	 * one, returns an SVGFontFamily otherwise returns an UnresolvedFontFamily.
	 *
	 * @param textElement    The text element that the font family will be attached
	 *                       to.
	 * @param ctx            The bridge context, used to search for a matching SVG
	 *                       font element.
	 * @param fontFamilyName The name of the font family to search for.
	 * @param fontWeight     The weight of the font to use when trying to match an
	 *                       SVG font family.
	 * @param fontStyle      The style of the font to use when trying to match as
	 *                       SVG font family.
	 *
	 * @return A GVTFontFamily for the specified font attributes. This will be
	 *         unresolved unless a matching SVG font was found.
	 */
	public static GVTFontFamily getFontFamily(Element textElement, BridgeContext ctx, String fontFamilyName,
			String fontWeight, String fontStyle) {

		// TODO: should match against font-variant as well
		String fontKeyName = fontFamilyName.toLowerCase() + " " + // todo locale??
				fontWeight + " " + fontStyle;

		// check fontFamilyMap to see if we have already created an
		// FontFamily that matches
		Map<Object, Object> fontFamilyMap = ctx.getFontFamilyMap();
		GVTFontFamily fontFamily = (GVTFontFamily) fontFamilyMap.get(fontKeyName);
		if (fontFamily != null) {
			return fontFamily;
		}

		// try to find a matching SVGFontFace element
		Document doc = textElement.getOwnerDocument();

		@SuppressWarnings("unchecked")
		List<FontFace> fontFaces = (List<FontFace>) fontFamilyMap.get(doc);

		if (fontFaces == null) {
			fontFaces = getFontFaces(doc, ctx);
			fontFamilyMap.put(doc, fontFaces);
		}

		List<GVTFontFamily> svgFontFamilies = new LinkedList<>();
		for (FontFace fontFace : fontFaces) {
			if (!fontFace.hasFamilyName(fontFamilyName)) {
				continue;
			}

			String fontFaceStyle = fontFace.getFontStyle();
			if (fontFaceStyle.equals(SVG_ALL_VALUE) || fontFaceStyle.indexOf(fontStyle) != -1) {
				GVTFontFamily ffam = fontFace.getFontFamily(ctx);
				if (ffam != null)
					svgFontFamilies.add(ffam);
			}
		}

		if (svgFontFamilies.size() == 1) {
			// only found one matching svg font family
			fontFamilyMap.put(fontKeyName, svgFontFamilies.get(0));
			return svgFontFamilies.get(0);

		} else if (svgFontFamilies.size() > 1) {
			// need to find font face that matches the font-weight closest
			String fontWeightNumber = getFontWeightNumberString(fontWeight);

			// create lists of font weight numbers for each font family
			List<String> fontFamilyWeights = new ArrayList<>(svgFontFamilies.size());
			for (GVTFontFamily svgFontFamily : svgFontFamilies) {
				GVTFontFace fontFace;
				fontFace = svgFontFamily.getFontFace();
				String fontFaceWeight = fontFace.getFontWeight();
				fontFaceWeight = getFontWeightNumberString(fontFaceWeight);
				fontFamilyWeights.add(fontFaceWeight);
			}

			// make sure that each possible font-weight has been
			// assigned to a font-face, if not then need to "fill the
			// holes"

			List<String> newFontFamilyWeights = new ArrayList<>(fontFamilyWeights);
			for (int i = 100; i <= 900; i += 100) {
				String weightString = String.valueOf(i);
				boolean matched = false;
				int minDifference = 1000;
				int minDifferenceIndex = 0;
				for (int j = 0; j < fontFamilyWeights.size(); j++) {
					String fontFamilyWeight = fontFamilyWeights.get(j);
					if (fontFamilyWeight.indexOf(weightString) > -1) {
						matched = true;
						break;
					}
					StringTokenizer st = new StringTokenizer(fontFamilyWeight, " ,");
					while (st.hasMoreTokens()) {
						int weightNum = Integer.parseInt(st.nextToken());
						int difference = Math.abs(weightNum - i);
						if (difference < minDifference) {
							minDifference = difference;
							minDifferenceIndex = j;
						}
					}
				}
				if (!matched) {
					String newFontFamilyWeight = newFontFamilyWeights.get(minDifferenceIndex) + ", " + weightString;
					newFontFamilyWeights.set(minDifferenceIndex, newFontFamilyWeight);
				}
			}

			// now find matching font weight
			for (int i = 0; i < svgFontFamilies.size(); i++) {
				String fontFaceWeight = newFontFamilyWeights.get(i);
				if (fontFaceWeight.indexOf(fontWeightNumber) > -1) {
					fontFamilyMap.put(fontKeyName, svgFontFamilies.get(i));
					return svgFontFamilies.get(i);
				}
			}
			// should not get here, just return the first svg font family
			fontFamilyMap.put(fontKeyName, svgFontFamilies.get(0));
			return svgFontFamilies.get(0);

		} else {
			// couldn't find one so return an UnresolvedFontFamily object
			GVTFontFamily gvtFontFamily = new UnresolvedFontFamily(fontFamilyName);
			fontFamilyMap.put(fontKeyName, gvtFontFamily);
			return gvtFontFamily;
		}
	}

	/**
	 * Returns a string that contains all of the font weight numbers for the
	 * specified font weight attribute value.
	 *
	 * @param fontWeight The font-weight attribute value.
	 *
	 * @return The font weight expressed as font weight numbers. e.g. "normal"
	 *         becomes "400".
	 */
	protected static String getFontWeightNumberString(String fontWeight) {
		if (fontWeight.equals(SVG_NORMAL_VALUE)) {
			return SVG_400_VALUE;
		} else if (fontWeight.equals(SVG_BOLD_VALUE)) {
			return SVG_700_VALUE;
		} else if (fontWeight.equals(SVG_ALL_VALUE)) {
			return "100, 200, 300, 400, 500, 600, 700, 800, 900";
		}
		return fontWeight;
	}

}
