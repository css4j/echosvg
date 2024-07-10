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
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import io.sf.carte.echosvg.test.TestFonts;

/**
 * This test validates the convertion of Java 2D text into SVG Shapes, one of
 * the options of the SVGGraphics2D constructor.
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

		// Set the STRIKETHROUGH font and a color
		g.setFont(fontST);
		g.setPaint(new Color(0x666699));
		// Draw a string
		g.drawString("Strike Through", 10, 40);

		// Now draw with a different color and the UNDERLINE font
		g.setPaint(Color.black);
		g.setFont(fontUL);
		g.translate(0, 30);
		// Draw a new string
		g.drawString("Underline", 10, 70);
	}

}
