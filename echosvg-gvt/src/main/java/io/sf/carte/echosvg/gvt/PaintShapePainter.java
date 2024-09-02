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
package io.sf.carte.echosvg.gvt;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.color.ColorSpace;

import io.sf.carte.echosvg.ext.awt.color.ColorContext;

/**
 * A shape painter that has a paint.
 *
 * @version $Id$
 */
abstract class PaintShapePainter implements ShapePainter {

	/**
	 * The Shape to be painted.
	 */
	protected Shape shape;

	/**
	 * The paint attribute used to fill or stroke the shape.
	 */
	protected Paint paint;

	/**
	 * The color context.
	 */
	private ColorContext context;

	/**
	 * Constructs a new <code>PaintShapePainter</code> that can be used to color a
	 * <code>Shape</code>.
	 *
	 * @param shape Shape to be painted by this painter Should not be null.
	 * @param ctx   the color context. Cannot be {@code null}.
	 */
	protected PaintShapePainter(Shape shape, ColorContext ctx) {
		if (shape == null)
			throw new IllegalArgumentException("Shape can not be null!");
		if (ctx == null)
			throw new IllegalArgumentException("Color context can not be null!");

		this.shape = shape;
		this.context = ctx;
	}

	/**
	 * Sets the paint used to fill or stroke a shape.
	 *
	 * @param newPaint the paint object used to fill the shape
	 */
	public void setPaint(Paint newPaint) {
		this.paint = newPaint;
	}

	/**
	 * Gets the paint used to fill or draw the outline of the shape.
	 */
	public Paint getPaint() {
		if (paint instanceof Color) {
			Color color = (Color) paint;
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
		return paint;
	}

	/**
	 * Sets the Shape this shape painter is associated with.
	 *
	 * @param shape new shape this painter should be associated with. Should not be
	 *              null.
	 */
	@Override
	public void setShape(Shape shape) {
		if (shape == null) {
			throw new IllegalArgumentException();
		}
		this.shape = shape;
	}

	/**
	 * Gets the Shape this shape painter is associated with.
	 *
	 * @return shape associated with this Painter.
	 */
	@Override
	public Shape getShape() {
		return shape;
	}

}
