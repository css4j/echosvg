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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GraphicAttribute;
import java.awt.font.ImageGraphicAttribute;
import java.awt.font.ShapeGraphicAttribute;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.text.AttributedString;

/**
 * This test validates the convertion of Java 2D AffineTransform into SVG
 * Shapes.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AttributedCharacterIterator implements Painter {
	@Override
	public void paint(Graphics2D g) {
		String fontName = "Arial";
		int fontSize = 15;

		String text = "Attributed Strings are fun !";
		AttributedString styledText = new AttributedString(text);

		//
		// Set font family for the whole string
		//
		Font font = new Font(fontName, Font.PLAIN, fontSize);
		styledText.addAttribute(TextAttribute.FAMILY, font.getFamily());
		styledText.addAttribute(TextAttribute.SIZE, (float) font.getSize());
		styledText.addAttribute(TextAttribute.FOREGROUND, Color.black);

		//
		// Set font style attributes for different part of the string
		//

		// "Attributed" is in Bold
		styledText.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, 0, 10);

		// "String" is italic
		styledText.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, 11, 18);

		// fun is underlined and strike through
		styledText.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 23, 28);
		styledText.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 23, 28);

		//
		// Set text color
		//

		// "Attributed" is in dark red
		styledText.addAttribute(TextAttribute.FOREGROUND, new Color(128, 0, 0), 0, 10);

		// "String" is blue
		styledText.addAttribute(TextAttribute.FOREGROUND, new Color(70, 107, 132), 11, 18);

		// "fun" is yellow
		styledText.addAttribute(TextAttribute.FOREGROUND, new Color(236, 214, 70), 23, 28);

		java.text.AttributedCharacterIterator iter = styledText.getIterator();
		g.drawString(iter, 10, 100);

		styledText.addAttribute(TextAttribute.SWAP_COLORS, TextAttribute.SWAP_COLORS_ON);

		iter = styledText.getIterator();
		g.drawString(iter, 10, 130);

		styledText = new AttributedString(text);

		styledText.addAttribute(TextAttribute.FAMILY, font.getFamily());
		styledText.addAttribute(TextAttribute.SIZE, (float) font.getSize());
		styledText.addAttribute(TextAttribute.FOREGROUND, Color.black);

		// "Attributed" is in red
		styledText.addAttribute(TextAttribute.FOREGROUND, new Color(160, 0, 0), 0, 10);

		// "Attributed" is now Ultra Bold
		styledText.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD, 0, 10);

		// "String" is blue-green
		styledText.addAttribute(TextAttribute.FOREGROUND, new Color(20, 90, 120), 11, 18);

		// "String" is underline
		styledText.addAttribute(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED, 11, 18);

		// "fun" is yellow
		styledText.addAttribute(TextAttribute.FOREGROUND, new Color(100, 140, 90), 23, 28);
		// "fun" on a blue background
		styledText.addAttribute(TextAttribute.BACKGROUND, new Color(70, 107, 132), 23, 28);

		// "fun" is Bold
		styledText.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD, 23, 28);

		// Character replacement
		FontRenderContext frc = g.getFontRenderContext();
		TextLayout aLayout = new TextLayout("A", font, frc);
		Shape aShape = aLayout.getOutline(null);

		ShapeGraphicAttribute aReplacement = new ShapeGraphicAttribute(aShape, GraphicAttribute.ROMAN_BASELINE, true);
		styledText.addAttribute(TextAttribute.CHAR_REPLACEMENT, aReplacement, 0, 1);

		// Create a BufferedImage to decorate the Shape
		aLayout = new TextLayout("A", font, frc);
		aShape = aLayout.getOutline(null);
		Rectangle bounds = aShape.getBounds();

		int blurWidth = 6;
		BufferedImage image = new BufferedImage(bounds.width + blurWidth * 4, bounds.height + blurWidth * 4,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		int w = image.getWidth(), h = image.getHeight();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(Color.black);
		g2.translate(-bounds.x + (w - bounds.width) / 2, -bounds.y + (h - bounds.height) / 2);
		g2.fill(aShape);
		g2.setStroke(new BasicStroke(blurWidth / 2));
		g2.draw(aShape);
		g2.dispose();

		float k[] = new float[blurWidth * blurWidth];
		for (int i = 0; i < k.length; i++)
			k[i] = 1 / (float) k.length;
		Kernel kernel = new Kernel(blurWidth, blurWidth, k);
		ConvolveOp blur = new ConvolveOp(kernel);
		image = blur.filter(image, null);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(-bounds.x + (w - bounds.width) / 2, -bounds.y + (h - bounds.height) / 2);
		g2.setComposite(AlphaComposite.Clear);
		g2.fill(aShape);

		image = image.getSubimage(blurWidth, blurWidth, image.getWidth() - 2 * blurWidth,
				image.getHeight() - 2 * blurWidth);

		ImageGraphicAttribute aImageReplacement = new ImageGraphicAttribute(image, GraphicAttribute.ROMAN_BASELINE,
				blurWidth, blurWidth + bounds.height);
		styledText.addAttribute(TextAttribute.CHAR_REPLACEMENT, aImageReplacement, 0, 1);


		iter = styledText.getIterator();

		TextLayout layout = new TextLayout(iter, frc);
		
		bounds = layout.getBounds().getBounds();
		bounds.width += 50;
		bounds.height += 50;
		
		layout.draw(g, 20, layout.getAscent() + 25);

		g.drawString(iter, 10, 160);

		styledText.addAttribute(TextAttribute.SWAP_COLORS, TextAttribute.SWAP_COLORS_ON);

		iter = styledText.getIterator();
		g.drawString(iter, 10, 190);

	}

}
