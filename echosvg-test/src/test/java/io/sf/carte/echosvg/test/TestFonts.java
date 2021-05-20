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

	public static void loadTestFonts() throws FontFormatException, IOException {
		String fontPathUrl = TestLocations.getRootBuildURL() + FONT_PATH;

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

		registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-Regular.ttf");
		registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-Bold.ttf");
		registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-BoldItalic.ttf");
		registerFont(Font.TRUETYPE_FONT, fontPathUrl, "UbuntuMono-Italic.ttf");
	}

	public static void registerFont(int truetypeFont, String fontFilename)
			throws FontFormatException, IOException {
		registerFont(Font.TRUETYPE_FONT, TestLocations.getRootBuildURL() + FONT_PATH, fontFilename);
	}

	private static void registerFont(int truetypeFont, String fontPathUrl, String fontFile)
			throws FontFormatException, IOException {
		URL url = new URL(fontPathUrl + fontFile);
		File f = new File(url.getPath());
		Font baseFont = Font.createFont(Font.TRUETYPE_FONT, f);

		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(baseFont);
	}

}
