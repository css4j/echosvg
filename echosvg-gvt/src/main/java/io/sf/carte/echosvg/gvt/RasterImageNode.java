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
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;

/**
 * A graphics node that represents a raster image.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author <a href="mailto:Thomas.DeWeese@Kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RasterImageNode extends AbstractGraphicsNode {

	/**
	 * The renderable image that represents this image node.
	 */
	protected Filter image;

	/**
	 * Constructs a new empty <code>RasterImageNode</code>.
	 */
	public RasterImageNode() {
	}

	//
	// Properties methods
	//

	/**
	 * Sets the raster image of this raster image node.
	 *
	 * @param newImage the new raster image of this raster image node
	 */
	public void setImage(Filter newImage) {
		fireGraphicsNodeChangeStarted();
		invalidateGeometryCache();
		this.image = newImage;
		fireGraphicsNodeChangeCompleted();
	}

	/**
	 * Returns the raster image of this raster image node.
	 *
	 * @return the raster image of this raster image node
	 */
	public Filter getImage() {
		return image;
	}

	/**
	 * Returns the bounds of this raster image node.
	 *
	 * @return the bounds of this raster image node
	 */
	public Rectangle2D getImageBounds() {
		if (image == null)
			return null;
		return (Rectangle2D) image.getBounds2D().clone();
	}

	/**
	 * Returns the RenderableImage for this node. The returned RenderableImage this
	 * node before any of the filter operations have been applied.
	 */
	public Filter getGraphicsNodeRable() {
		return image;
	}

	//
	// Drawing methods
	//

	/**
	 * Paints this node without applying Filter, Mask, Composite and clip.
	 *
	 * @param g2d the Graphics2D to use
	 */
	@Override
	public void primitivePaint(Graphics2D g2d) {
		if (image == null)
			return;

		GraphicsUtil.drawImage(g2d, image);
	}

	//
	// Geometric methods
	//

	/**
	 * Returns the bounds of the area covered by this node's primitive paint.
	 */
	@Override
	public Rectangle2D getPrimitiveBounds() {
		if (image == null)
			return null;
		return image.getBounds2D();
	}

	/**
	 * Returns the bounds of the area covered by this node, without taking any of
	 * its rendering attribute into account. That is, exclusive of any clipping,
	 * masking, filtering or stroking, for example.
	 */
	@Override
	public Rectangle2D getGeometryBounds() {
		if (image == null)
			return null;
		return image.getBounds2D();
	}

	/**
	 * Returns the bounds of the sensitive area covered by this node, This includes
	 * the stroked area but does not include the effects of clipping, masking or
	 * filtering.
	 */
	@Override
	public Rectangle2D getSensitiveBounds() {
		if (image == null)
			return null;
		return image.getBounds2D();
	}

	/**
	 * Returns the outline of this node.
	 */
	@Override
	public Shape getOutline() {
		if (image == null)
			return null;
		return image.getBounds2D();
	}
}
