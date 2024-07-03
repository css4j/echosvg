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
import java.util.Hashtable;
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
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Color backgroundColor = new Color(0x08081a);
		g.setBackground(backgroundColor);

		// Set default font
		g.setFont(new Font(TestFonts.FONT_FAMILY_SANS1, Font.BOLD, 12));

		// Colors used for labels and test output
		Color labelColor = new Color(0x666699);
		Color fontColor = Color.black;

		//
		Map<TextAttribute, Object> attributes = new Hashtable<>();
		attributes.put(TextAttribute.FAMILY, TestFonts.FONT_FAMILY_SANS1);
		attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
		attributes.put(TextAttribute.SIZE, 20);
		attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		Font font = new Font(attributes);
		g.setFont(font);
		g.setPaint(labelColor);

		g.drawString("Strike Through", 10, 40);
		g.setPaint(fontColor);
		g.translate(0, 30);
		Map<TextAttribute, Object> attributes2 = new Hashtable<>(attributes);
		attributes2.remove(TextAttribute.STRIKETHROUGH);
		attributes2.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		font = new Font(attributes2);
		g.setFont(font);
		g.drawString("Underline", 10, 70);
	}

}
