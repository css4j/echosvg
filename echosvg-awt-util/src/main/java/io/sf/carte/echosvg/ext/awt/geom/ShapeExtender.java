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

package io.sf.carte.echosvg.ext.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class wraps a normal path into an extended path.
 * 
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ShapeExtender implements ExtendedShape {
	Shape shape;

	public ShapeExtender(Shape shape) {
		this.shape = shape;
	}

	@Override
	public boolean contains(double x, double y) {
		return shape.contains(x, y);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return shape.contains(x, y, w, h);
	}

	@Override
	public boolean contains(Point2D p) {
		return shape.contains(p);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return shape.contains(r);
	}

	@Override
	public Rectangle getBounds() {
		return shape.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return shape.getBounds2D();
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return shape.getPathIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return shape.getPathIterator(at, flatness);
	}

	@Override
	public ExtendedPathIterator getExtendedPathIterator() {
		return new EPIWrap(shape.getPathIterator(null));
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return shape.intersects(x, y, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return shape.intersects(r);
	}

	public static class EPIWrap implements ExtendedPathIterator {
		PathIterator pi = null;

		public EPIWrap(PathIterator pi) {
			this.pi = pi;
		}

		@Override
		public int currentSegment() {
			float[] coords = new float[6];
			return pi.currentSegment(coords);
		}

		@Override
		public int currentSegment(double[] coords) {
			return pi.currentSegment(coords);
		}

		@Override
		public int currentSegment(float[] coords) {
			return pi.currentSegment(coords);
		}

		@Override
		public int getWindingRule() {
			return pi.getWindingRule();
		}

		@Override
		public boolean isDone() {
			return pi.isDone();
		}

		@Override
		public void next() {
			pi.next();
		}
	}
}
