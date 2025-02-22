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
package io.sf.carte.echosvg.svggen.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.sf.carte.echosvg.ext.awt.color.StandardColorSpaces;
import io.sf.carte.echosvg.test.misc.TestFonts;

/**
 * This test validates the convertion of Java 2D text with profiled colors into
 * SVG Shapes, one of the capabilities of the SVGGraphics2D.
 *
 * @author See Git history.
 * @version $Id$
 */
public class FontDecoration implements Painter {

	@Override
	public void paint(Graphics2D g) {
		// Set anti-aliasing
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Set a background color
		Color backgroundColor = new Color(0x08081a);
		g.setBackground(backgroundColor);

		// Set default font
		g.setFont(new Font(TestFonts.FONT_FAMILY_SANS1, Font.BOLD, 12));

		// Create a font with the desired attributes, including STRIKETHROUGH
		Map<TextAttribute, Object> attributes = new HashMap<>();
		attributes.put(TextAttribute.FAMILY, TestFonts.FONT_FAMILY_SANS1);
		attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
		attributes.put(TextAttribute.SIZE, 20);
		attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		Font fontST = new Font(attributes);

		// A similar font but with UNDERLINE instead of STRIKETHROUGH
		Map<TextAttribute, Object> attributes2 = new HashMap<>(attributes);
		attributes2.remove(TextAttribute.STRIKETHROUGH);
		attributes2.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		Font fontUL = new Font(attributes2);

		// Prepare a color
		Color stColor;
		// Load a color profile
		try (InputStream is = getClass().getResourceAsStream(
				"/io/sf/carte/echosvg/css/color/profiles/WideGamutPhoto-v4.icc")) {
			ICC_Profile profile = ICC_Profile.getInstance(is);
			ICC_ColorSpace cs = new ICC_ColorSpace(profile);
			float[] comps = { 0.33f, 0.34f, 0.43f };
			stColor = new Color(cs, comps, 1f);
		} catch (IOException e) {
			e.printStackTrace();
			stColor = new Color(0x666699);
		}

		// Set the STRIKETHROUGH font and a color
		g.setFont(fontST);
		g.setPaint(stColor);
		// Draw a string
		g.drawString("Strike Through", 10, 40);

		/*
		 * Draw some figure with another non-sRGB color
		 */
		Color lColor;
		// Use the ITU Rec bt.2020 color space
		ICC_ColorSpace cs = StandardColorSpaces.getRec2020();
		float[] comps = { .55f, .6f, .34f };
		lColor = new Color(cs, comps, 1f);

		// Now draw with the new color
		g.setPaint(lColor);
		g.draw(new Line2D.Float(60, 46, 60, 80));
		g.fill(new Ellipse2D.Float(56, 53, 8, 20));

		// Prepare a new color
		Color ulColor;
		// Get the P3 color space
		ICC_ColorSpace csP3 = StandardColorSpaces.getDisplayP3();
		comps = new float[] { .36f, .35f, .33f };
		ulColor = new Color(csP3, comps, 1f);

		// Now draw with the new color and the UNDERLINE font
		g.setPaint(ulColor);
		g.setFont(fontUL);
		g.translate(0, 30);
		// Draw a new string
		g.drawString("Underline", 10, 70);
	}

}
