/*
 * Copyright (c) 2020-2021 Carlos Amengual
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
 */
package io.sf.carte.echosvg.test;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import io.sf.carte.echosvg.bridge.DefaultFontFamilyResolver;

/**
 * Free fonts used by tests.
 *
 */
public class TestFonts {

	public static final String FONT_PATH = "samples/tests/resources/ttf/";

	public static final String FONT_FAMILY_SANS1 = "Open Sans";

	public static final String FONT_FAMILY_SANS2 = "Roboto";

	public static final String FONT_FAMILY_SERIF1 = "Newsreader";

	public static final String FONT_FAMILY_MONOSPACED1 = "Ubuntu Mono";

	private static boolean fontsLoaded = false;

	public static void loadTestFonts() throws FontFormatException, IOException {
		if (!fontsLoaded) {
			String fontPathUrl = TestLocations.PROJECT_ROOT_URL + FONT_PATH;

			// Replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "OpenSans-SemiBoldItalic.ttf");

			// Alternative replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Roboto-MediumItalic.ttf");

			// Alternative replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-SemiBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-Thin.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Raleway-ThinItalic.ttf");

			// Replacement for 'Verdana'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Sansation_Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Sansation_Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Sansation_Bold_Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Sansation_Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Sansation_Light.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Sansation_Light_Italic.ttf");

			// Alternative replacement for 'Arial', 'Helvetica'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Dosis-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Dosis-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Dosis-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Dosis-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Dosis-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Dosis-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Dosis-SemiBold.ttf");

			// Replacement for 'AvantGarde'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-SemiBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-Thin.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Poppins-ThinItalic.ttf");

			// Replacement for 'Times New Roman'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Newsreader-SemiBoldItalic.ttf");

			// Alternative replacement for 'Times New Roman'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "PlayfairDisplay-SemiBoldItalic.ttf");

			// Replacement for other serif fonts
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-Black.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-BlackItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-ExtraBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-ExtraBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-ExtraLight.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-ExtraLightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-Italic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-Light.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-LightItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-Medium.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-MediumItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-SemiBold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-SemiBoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-Thin.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Bitter-ThinItalic.ttf");

			// Replacement for 'Courier New'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-Regular.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-Bold.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-BoldItalic.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-Italic.ttf");

			// Replacement for 'Lucida Sans Typewriter'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "NotCourierSans.ttf");
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "NotCourierSans-Bold.ttf");

			// Replacement for 'Impact'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Anton-Regular.ttf");

			// Replacement for 'BlockSpace'
			registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Notable-Regular.ttf");

			fontsLoaded = true;
		}
	}

	public static void registerFont(int fontFormat, String fontFilename)
			throws FontFormatException, IOException {
		registerFont(fontFormat, TestLocations.PROJECT_ROOT_URL + FONT_PATH, fontFilename);
	}

	private static void registerFont(int fontFormat, String fontPathUrl, String fontFile)
			throws FontFormatException, IOException {
		URL url = new URL(fontPathUrl + fontFile);
		File f = new File(url.getPath());
		Font baseFont = Font.createFont(fontFormat, f);

		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(baseFont);
		DefaultFontFamilyResolver.getInstance().registerFont(baseFont);
	}

}
