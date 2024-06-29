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

/**
 * A shape painter that can be used to fill a shape.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class FillShapePainter implements ShapePainter {

	/**
	 * The Shape to be painted.
	 */
	protected Shape shape;

	/**
	 * The paint attribute used to fill the shape.
	 */
	protected Paint paint;

	/**
	 * Constructs a new <code>FillShapePainter</code> that can be used to fill a
	 * <code>Shape</code>.
	 *
	 * @param shape Shape to be painted by this painter Should not be null.
	 */
	public FillShapePainter(Shape shape) {
		if (shape == null)
			throw new IllegalArgumentException("Shape can not be null!");

		this.shape = shape;
	}

	/**
	 * Sets the paint used to fill a shape.
	 *
	 * @param newPaint the paint object used to fill the shape
	 */
	public void setPaint(Paint newPaint) {
		this.paint = newPaint;
	}

	/**
	 * Gets the paint used to draw the outline of the shape.
	 */
	public Paint getPaint() {
		return paint;
	}

	/**
	 * Paints the specified shape using the specified Graphics2D.
	 *
	 * @param g2d the Graphics2D to use
	 */
	@Override
	public void paint(Graphics2D g2d) {
		if (paint != null) {
			g2d.setPaint(paint);
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
