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
package io.sf.carte.echosvg.gvt.text;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.color.ColorSpace;

import io.sf.carte.echosvg.ext.awt.color.ColorContext;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TextPaintInfo {

	public boolean visible;
	public Paint fillPaint;
	public Paint strokePaint;
	public Stroke strokeStroke;
	public Composite composite;

	public Paint underlinePaint;
	public Paint underlineStrokePaint;
	public Stroke underlineStroke;

	public Paint overlinePaint;
	public Paint overlineStrokePaint;
	public Stroke overlineStroke;

	public Paint strikethroughPaint;
	public Paint strikethroughStrokePaint;
	public Stroke strikethroughStroke;

	public int startChar, endChar;

	/**
	 * The color context.
	 */
	private ColorContext context;

	public TextPaintInfo(ColorContext context) {
		this.context = context;
	}

	public TextPaintInfo(TextPaintInfo pi) {
		set(pi);
	}

	public void set(TextPaintInfo pi) {
		if (pi == null) {
			this.fillPaint = null;
			this.strokePaint = null;
			this.strokeStroke = null;
			this.composite = null;

			this.underlinePaint = null;
			this.underlineStrokePaint = null;
			this.underlineStroke = null;

			this.overlinePaint = null;
			this.overlineStrokePaint = null;
			this.overlineStroke = null;

			this.strikethroughPaint = null;
			this.strikethroughStrokePaint = null;
			this.strikethroughStroke = null;

			this.visible = false;
			this.context = null;
		} else {
			this.fillPaint = pi.fillPaint;
			this.strokePaint = pi.strokePaint;
			this.strokeStroke = pi.strokeStroke;
			this.composite = pi.composite;

			this.underlinePaint = pi.underlinePaint;
			this.underlineStrokePaint = pi.underlineStrokePaint;
			this.underlineStroke = pi.underlineStroke;

			this.overlinePaint = pi.overlinePaint;
			this.overlineStrokePaint = pi.overlineStrokePaint;
			this.overlineStroke = pi.overlineStroke;

			this.strikethroughPaint = pi.strikethroughPaint;
			this.strikethroughStrokePaint = pi.strikethroughStrokePaint;
			this.strikethroughStroke = pi.strikethroughStroke;

			this.visible = pi.visible;
			this.context = pi.context;
		}
	}

	public Paint getFillPaint() {
		if (fillPaint instanceof Color) {
			Color color = (Color) fillPaint;
			ColorSpace space = color.getColorSpace();
			ColorSpace targetSpace = context.getColorSpace();
			if (targetSpace == null) {
				// sRGB
				if (!space.isCS_sRGB()) {
					float[] comp = color.getRGBComponents(null);
					color = new Color(comp[0], comp[1], comp[2], comp[3]);
				}
			} else {
				color = new Color(targetSpace, color.getColorComponents(targetSpace, null),
						color.getAlpha() / 255f);
			}
			return color;
		}
		return fillPaint;
	}

	public void setFillPaint(Paint fillPaint) {
		this.fillPaint = fillPaint;
	}

	public Paint getStrokePaint() {
		return strokePaint;
	}

	public void setStrokePaint(Paint strokePaint) {
		this.strokePaint = strokePaint;
	}

	public Paint getUnderlinePaint() {
		return underlinePaint;
	}

	public void setUnderlinePaint(Paint underlinePaint) {
		this.underlinePaint = underlinePaint;
	}

	public Paint getUnderlineStrokePaint() {
		return underlineStrokePaint;
	}

	public void setUnderlineStrokePaint(Paint underlineStrokePaint) {
		this.underlineStrokePaint = underlineStrokePaint;
	}

	public Paint getOverlinePaint() {
		return overlinePaint;
	}

	public void setOverlinePaint(Paint overlinePaint) {
		this.overlinePaint = overlinePaint;
	}

	public Paint getOverlineStrokePaint() {
		return overlineStrokePaint;
	}

	public void setOverlineStrokePaint(Paint overlineStrokePaint) {
		this.overlineStrokePaint = overlineStrokePaint;
	}

	public Paint getStrikethroughPaint() {
		return strikethroughPaint;
	}

	public void setStrikethroughPaint(Paint strikethroughPaint) {
		this.strikethroughPaint = strikethroughPaint;
	}

	public Paint getStrikethroughStrokePaint() {
		return strikethroughStrokePaint;
	}

	public void setStrikethroughStrokePaint(Paint strikethroughStrokePaint) {
		this.strikethroughStrokePaint = strikethroughStrokePaint;
	}

	public static boolean equivilent(TextPaintInfo tpi1, TextPaintInfo tpi2) {
		if (tpi1 == null) {
			if (tpi2 == null)
				return true;
			return false;
		} else if (tpi2 == null)
			return false;

		if ((tpi1.fillPaint == null) != (tpi2.fillPaint == null))
			return false;

		if (tpi1.visible != tpi2.visible)
			return false;

		boolean tpi1Stroke = ((tpi1.strokePaint != null) && (tpi1.strokeStroke != null));

		boolean tpi2Stroke = ((tpi2.strokePaint != null) && (tpi2.strokeStroke != null));

		return (tpi1Stroke == tpi2Stroke);

	}

}
