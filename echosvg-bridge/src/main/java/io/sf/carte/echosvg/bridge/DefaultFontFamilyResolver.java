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

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import io.sf.carte.echosvg.gvt.font.AWTFontFamily;
import io.sf.carte.echosvg.gvt.font.AWTGVTFont;
import io.sf.carte.echosvg.gvt.font.GVTFontFace;
import io.sf.carte.echosvg.gvt.font.GVTFontFamily;

/**
 * The is a utility class that is used for resolving UnresolvedFontFamilies.
 *
 * @author <a href="mailto:bella.robinson@cmis.csiro.au">Bella Robinson</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public final class DefaultFontFamilyResolver implements FontFamilyResolver {

	public static final DefaultFontFamilyResolver SINGLETON = new DefaultFontFamilyResolver();

	private DefaultFontFamilyResolver() {
	}

	/**
	 * The default font. This will be used when no font families can be resolved for
	 * a particular text chunk/run.
	 */
	private static final AWTFontFamily DEFAULT_FONT_FAMILY = new AWTFontFamily("SansSerif");

	/**
	 * List of all available fonts on the current system, plus a few common
	 * alternatives.
	 */
	protected static final Map<String, String> fonts = new HashMap<>();

	protected static final List<AWTFontFamily> awtFontFamilies = new ArrayList<>();

	protected static final List<AWTGVTFont> awtFonts = new ArrayList<>();

	/**
	 * This sets up the list of available fonts.
	 */
	static {
		fonts.put("sans-serif", "SansSerif");
		fonts.put("serif", "Serif");
		fonts.put("times", "Serif");
		fonts.put("times new roman", "Serif");
		fonts.put("cursive", "Dialog");
		fonts.put("fantasy", "Symbol");
		fonts.put("monospace", "Monospaced");
		fonts.put("monospaced", "Monospaced");
		fonts.put("courier", "Monospaced");

		//
		// Load all fonts. Work around
		//

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = env.getAvailableFontFamilyNames();

		int nFonts = fontNames != null ? fontNames.length : 0;
		for (int i = 0; i < nFonts; i++) {
			String lcFontName = fontNames[i].toLowerCase();
			fonts.put(lcFontName, fontNames[i]);

			// also add the font name with the spaces removed
			StringTokenizer st = new StringTokenizer(lcFontName);
			StringBuilder fontNameWithoutSpaces = new StringBuilder(lcFontName.length());
			while (st.hasMoreTokens()) {
				fontNameWithoutSpaces.append(st.nextToken());
			}
			if (fontNameWithoutSpaces.length() != lcFontName.length()) {
				fonts.put(fontNameWithoutSpaces.toString(), fontNames[i]);

				// also add the font name with spaces replaced by dashes
				String fontNameWithDashes = lcFontName.replace(' ', '-');
				fonts.put(fontNameWithDashes, fontNames[i]);
			}
		}

		// Also register all font names, not just font families.
		// Example: Font Family: "Univers", but Font Name: "Univers 45 Light"
		// Without this, matching "Univers 45 Light" is not possible.
		Font[] allFonts = env.getAllFonts();
		for (Font f : allFonts) {
			fonts.put(f.getFontName().toLowerCase(), f.getFontName());
		}

		// first add the default font
		awtFontFamilies.add(DEFAULT_FONT_FAMILY);
		awtFonts.add(new AWTGVTFont(DEFAULT_FONT_FAMILY.getFamilyName(), 0, 12));

		Collection<String> fontValues = fonts.values();
		for (String fontFamily : fontValues) {
			AWTFontFamily awtFontFamily = new AWTFontFamily(fontFamily);
			awtFontFamilies.add(awtFontFamily);
			AWTGVTFont font = new AWTGVTFont(fontFamily, 0, 12);
			awtFonts.add(font);
		}

	}

	/**
	 * This keeps track of all the resolved font families. This is to hopefully
	 * reduce the number of font family objects used.
	 */
	protected static final Map<String, GVTFontFamily> resolvedFontFamilies = new HashMap<>();

	/**
	 * Get the singleton instance of {@code DefaultFontFamilyResolver}.
	 * 
	 * @return the singleton instance.
	 */
	public static DefaultFontFamilyResolver getInstance() {
		return SINGLETON;
	}

	/**
	 * Register a font with this resolver.
	 * 
	 * @param font the font.
	 */
	public void registerFont(Font font) {
		String fontName = font.getFontName();
		String lcFontName = fontName.toLowerCase();
		fonts.putIfAbsent(lcFontName, fontName);

		// Now the font family
		String fontFamily = font.getFamily();
		String lcFontFamily = fontFamily.toLowerCase();

		if (!fonts.containsKey(lcFontFamily)) {
			fonts.put(lcFontFamily, fontFamily);

			// also add the font name with the spaces removed
			StringTokenizer st = new StringTokenizer(lcFontFamily);
			StringBuilder fontNameWithoutSpaces = new StringBuilder(lcFontFamily.length());
			while (st.hasMoreTokens()) {
				fontNameWithoutSpaces.append(st.nextToken());
			}
			if (fontNameWithoutSpaces.length() != lcFontFamily.length()) {
				fonts.put(fontNameWithoutSpaces.toString(), fontFamily);
				// also add the font name with spaces replaced by dashes
				String fontNameWithDashes = lcFontFamily.replace(' ', '-');
				fonts.put(fontNameWithDashes, fontFamily);
			}

			AWTFontFamily awtFontFamily = new AWTFontFamily(fontFamily);
			awtFontFamilies.add(awtFontFamily);
			AWTGVTFont gvtFont = new AWTGVTFont(fontFamily, 0, 12);
			awtFonts.add(gvtFont);
		}
	}

	@Override
	public AWTFontFamily resolve(String familyName, FontFace fontFace) {
		String fontName = fonts.get(fontFace.getFamilyName().toLowerCase());
		if (fontName == null) {
			return null;
		} else {
			GVTFontFace face = FontFace.createFontFace(fontName, fontFace);
			return new AWTFontFamily(face);
		}
	}

	@Override
	public GVTFontFamily loadFont(InputStream in, FontFace ff) throws Exception {
		Font font = Font.createFont(Font.TRUETYPE_FONT, in);
		return new AWTFontFamily(ff, font);
	}

	/** {@inheritDoc} */
	@Override
	public GVTFontFamily resolve(String familyName) {

		familyName = familyName.toLowerCase();

		// first see if this font family has already been resolved
		GVTFontFamily resolvedFF = resolvedFontFamilies.get(familyName);

		if (resolvedFF == null) { // hasn't been resolved yet
			// try to find a matching family name in the list of
			// available fonts
			String awtFamilyName = fonts.get(familyName);
			if (awtFamilyName != null) {
				resolvedFF = new AWTFontFamily(awtFamilyName);
			}

			resolvedFontFamilies.put(familyName, resolvedFF);
		}

		return resolvedFF;
	}

	/** {@inheritDoc} */
	@Override
	public GVTFontFamily getFamilyThatCanDisplay(char c) {
		for (int i = 0; i < awtFontFamilies.size(); i++) {
			AWTFontFamily fontFamily = awtFontFamilies.get(i);
			AWTGVTFont font = awtFonts.get(i);
			if (font.canDisplay(c) && fontFamily.getFamilyName().indexOf("Song") == -1) {
				// the awt font for "MS Song" doesn't display chinese glyphs correctly
				return fontFamily;
			}
		}

		return null;
	}

	/** {@inheritDoc} */
	@Override
	public GVTFontFamily getDefault() {
		return DEFAULT_FONT_FAMILY;
	}

}
