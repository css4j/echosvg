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
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import io.sf.carte.echosvg.ext.awt.color.ColorContext;

/**
 * A shape painter that can be used to draw the outline of a shape.
 *
 * <p>
 * Original author: <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class StrokeShapePainter extends PaintShapePainter {

	/**
	 * Stroked version of the shape.
	 */
	protected Shape strokedShape;

	/**
	 * The stroke attribute used to draw the outline of the shape.
	 */
	protected Stroke stroke;

	/**
	 * Constructs a new <code>ShapePainter</code> that can be used to draw the
	 * outline of a <code>Shape</code>.
	 *
	 * @param shape shape to be painted by this painter. Should not be null.
	 * @param ctx   the color context. Cannot be {@code null}.
	 */
	public StrokeShapePainter(Shape shape, ColorContext ctx) {
		super(shape, ctx);
	}

	/**
	 * Sets the stroke used to draw the outline of a shape.
	 *
	 * @param newStroke the stroke object used to draw the outline of the shape
	 */
	public void setStroke(Stroke newStroke) {
		this.stroke = newStroke;
		this.strokedShape = null;
	}

	/**
	 * Gets the stroke used to draw the outline of the shape.
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * Sets the paint used to fill a shape.
	 *
	 * @param newPaint the paint object used to draw the shape
	 */
	@Override
	public void setPaint(Paint newPaint) {
		this.paint = newPaint;
	}

	/**
	 * Gets the paint used to draw the outline of the shape.
	 */
	@Override
	public Paint getPaint() {
		return super.getPaint();
	}

	/**
	 * Paints the outline of the specified shape using the specified Graphics2D.
	 *
	 * @param g2d the Graphics2D to use
	 */
	@Override
	public void paint(Graphics2D g2d) {
		if (stroke != null && paint != null) {
			g2d.setPaint(getPaint());
			g2d.setStroke(stroke);
			g2d.draw(shape);
		}
	}

	/**
	 * Returns the area painted by this shape painter.
	 */
	@Override
	public Shape getPaintedArea() {
		if ((paint == null) || (stroke == null))
			return null;

		if (strokedShape == null)
			strokedShape = stroke.createStrokedShape(shape);

		return strokedShape;
	}

	/**
	 * Returns the bounds of the area painted by this shape painter
	 */
	@Override
	public Rectangle2D getPaintedBounds2D() {
		Shape painted = getPaintedArea();
		if (painted == null)
			return null;

		return painted.getBounds2D();
	}

	/**
	 * Returns the bounds of the area covered by this shape painter
	 */
	@Override
	public boolean inPaintedArea(Point2D pt) {
		Shape painted = getPaintedArea();
		if (painted == null)
			return false;
		return painted.contains(pt);
	}

	/**
	 * Returns the area covered by this shape painter (even if not painted).
	 */
	@Override
	public Shape getSensitiveArea() {
		if (stroke == null)
			return null;

		if (strokedShape == null)
			strokedShape = stroke.createStrokedShape(shape);

		return strokedShape;
	}

	/**
	 * Returns the bounds of the area covered by this shape painter (even if not
	 * painted).
	 */
	@Override
	public Rectangle2D getSensitiveBounds2D() {
		Shape sensitive = getSensitiveArea();
		if (sensitive == null)
			return null;

		return sensitive.getBounds2D();
	}

	/**
	 * Returns the bounds of the area covered by this shape painter (even if not
	 * painted).
	 */
	@Override
	public boolean inSensitiveArea(Point2D pt) {
		Shape sensitive = getSensitiveArea();
		if (sensitive == null)
			return false;
		return sensitive.contains(pt);
	}

	/**
	 * Sets the Shape this shape painter is associated with.
	 *
	 * @param shape new shape this painter should be associated with. Should not be
	 *              null.
	 */
	@Override
	public void setShape(Shape shape) {
		super.setShape(shape);
		this.strokedShape = null;
	}

}
