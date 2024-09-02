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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import io.sf.carte.echosvg.ext.awt.color.ColorContext;

/**
 * A shape painter that can be used to fill a shape.
 *
 * <p>
 * Original author: <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class FillShapePainter extends PaintShapePainter {

	/**
	 * Constructs a new <code>FillShapePainter</code> that can be used to fill a
	 * <code>Shape</code>.
	 *
	 * @param shape Shape to be painted by this painter Should not be null.
	 * @param ctx   the color context. Cannot be {@code null}.
	 */
	public FillShapePainter(Shape shape, ColorContext ctx) {
		super(shape, ctx);
	}

	/**
	 * Sets the paint used to fill a shape.
	 *
	 * @param newPaint the paint object used to fill the shape.
	 */
	public void setPaint(Paint newPaint) {
		super.setPaint(newPaint);
	}

	/**
	 * Gets the paint used to fill the shape.
	 */
	public Paint getPaint() {
		return super.getPaint();
	}

	/**
	 * Paints the specified shape using the specified Graphics2D.
	 *
	 * @param g2d the Graphics2D to use
	 */
	@Override
	public void paint(Graphics2D g2d) {
		if (paint != null) {
			g2d.setPaint(getPaint());
			g2d.fill(shape);
		}
	}

	/**
	 * Returns the area painted by this shape painter.
	 */
	@Override
	public Shape getPaintedArea() {
		if (paint == null)
			return null;
		return shape;
	}

	/**
	 * Returns the bounds of the area painted by this shape painter
	 */
	@Override
	public Rectangle2D getPaintedBounds2D() {
		if ((paint == null) || (shape == null))
			return null;

		return shape.getBounds2D();
	}

	/**
	 * Returns true if pt is in the area painted by this shape painter
	 */
	@Override
	public boolean inPaintedArea(Point2D pt) {
		if ((paint == null) || (shape == null))
			return false;

		return shape.contains(pt);
	}

	/**
	 * Returns the area covered by this shape painter (even if not painted).
	 * 
	 */
	@Override
	public Shape getSensitiveArea() {
		return shape;
	}

	/**
	 * Returns the bounds of the area covered by this shape painte (even if not
	 * painted).
	 */
	@Override
	public Rectangle2D getSensitiveBounds2D() {
		if (shape == null)
			return null;
		return shape.getBounds2D();
	}

	/**
	 * Returns true if pt is in the area painted by this shape painter
	 */
	@Override
	public boolean inSensitiveArea(Point2D pt) {
		if (shape == null)
			return false;
		return shape.contains(pt);
	}

}
