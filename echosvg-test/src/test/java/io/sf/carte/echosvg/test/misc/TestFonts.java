/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.sf.carte.echosvg.test.misc;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import io.sf.carte.echosvg.bridge.DefaultFontFamilyResolver;

/**
 * Free fonts used by tests.
 *
 */
public class TestFonts {

	public static final String FONT_PATH = "samples/tests/resources/ttf/";

	public static final String FONT_FAMILY_SANS1 = "Roboto";

	public static final String FONT_FAMILY_SANS2 = "Open Sans";

	public static final String FONT_FAMILY_SERIF1 = "Newsreader";

	public static final String FONT_FAMILY_MONOSPACED1 = "Ubuntu Mono";

	private static boolean fontsLoaded = false;

	public static void loadTestFonts() throws FontFormatException, IOException {
		if (!fontsLoaded) {
			// Replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, "OpenSans-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "OpenSans-SemiBoldItalic.ttf");

			// Alternative replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, "Roboto-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "Roboto-MediumItalic.ttf");

			// Alternative replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, "Raleway-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-SemiBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-Thin.ttf");
			registerFont(Font.TRUETYPE_FONT, "Raleway-ThinItalic.ttf");

			// Replacement for 'Verdana'
			registerFont(Font.TRUETYPE_FONT, "Sansation_Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Sansation_Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Sansation_Bold_Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Sansation_Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Sansation_Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "Sansation_Light_Italic.ttf");

			// Alternative replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, "Dosis-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Dosis-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Dosis-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Dosis-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, "Dosis-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "Dosis-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "Dosis-SemiBold.ttf");

			// Replacement for 'AvantGarde'
			registerFont(Font.TRUETYPE_FONT, "Poppins-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-SemiBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-Thin.ttf");
			registerFont(Font.TRUETYPE_FONT, "Poppins-ThinItalic.ttf");

			// Replacement for 'Times New Roman'
			registerFont(Font.TRUETYPE_FONT, "Newsreader-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Newsreader-SemiBoldItalic.ttf");

			// Alternative replacement for 'Times New Roman'
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "PlayfairDisplay-SemiBoldItalic.ttf");

			// Replacement for other serif fonts
			registerFont(Font.TRUETYPE_FONT, "Bitter-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-SemiBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-Thin.ttf");
			registerFont(Font.TRUETYPE_FONT, "Bitter-ThinItalic.ttf");

			// Replacement for 'Courier New'
			registerFont(Font.TRUETYPE_FONT, "UbuntuMono-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "UbuntuMono-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "UbuntuMono-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, "UbuntuMono-Italic.ttf");

			// Replacement for other Monospaced
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, "Inconsolata-SemiBold.ttf");

			// Replacement for 'Lucida Sans Typewriter'
			registerFont(Font.TRUETYPE_FONT, "NotCourierSans.ttf");
			registerFont(Font.TRUETYPE_FONT, "NotCourierSans-Bold.ttf");

			// Replacement for 'Impact'
			registerFont(Font.TRUETYPE_FONT, "Anton-Regular.ttf");

			// Replacement for 'BlockSpace'
			registerFont(Font.TRUETYPE_FONT, "Notable-Regular.ttf");

			fontsLoaded = true;
		}
	}

	public static void registerFont(int fontFormat, String fontFilename)
			throws FontFormatException, IOException {
		registerFont(fontFormat, TestLocations.PROJECT_ROOT_URL + FONT_PATH, fontFilename);
	}

	public static void registerFont(int fontFormat, String fontPathUrl, String fontFile)
			throws FontFormatException, IOException {
		URL url = new URL(fontPathUrl + fontFile);
		File f = new File(url.getPath());
		Font baseFont = Font.createFont(fontFormat, f);

		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(baseFont);
		DefaultFontFamilyResolver.getInstance().registerFont(baseFont);
	}

	/**
	 * Check whether the given font is available.
	 * 
	 * @param familyName the font family name.
	 * @return true if the font is available.
	 */
	public static boolean isFontAvailable(String familyName) {
		List<String> fonts = Arrays.asList(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		return fonts.contains(familyName);
	}

}
