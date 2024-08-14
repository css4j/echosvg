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
package io.sf.carte.echosvg.bridge;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.AbstractSVGAnimatedLength;
import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMCircleElement;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.gvt.ShapePainter;

/**
 * Bridge class for the &lt;circle&gt; element.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGCircleElementBridge extends SVGShapeElementBridge {

	/**
	 * Constructs a new bridge for the &lt;circle&gt; element.
	 */
	public SVGCircleElementBridge() {
	}

	/**
	 * Returns 'circle'.
	 */
	@Override
	public String getLocalName() {
		return SVG_CIRCLE_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGCircleElementBridge();
	}

	/**
	 * Constructs a circle according to the specified parameters.
	 *
	 * @param ctx       the bridge context to use
	 * @param e         the element that describes a rect element
	 * @param shapeNode the shape node to initialize
	 */
	@Override
	protected void buildShape(BridgeContext ctx, Element e, ShapeNode shapeNode) {
		try {
			SVGOMCircleElement ce = (SVGOMCircleElement) e;

			// 'cx' attribute - default is 0
			AbstractSVGAnimatedLength _cx = (AbstractSVGAnimatedLength) ce.getCx();
			float cx = safeAnimatedLength(_cx, 0f);

			// 'cy' attribute - default is 0
			AbstractSVGAnimatedLength _cy = (AbstractSVGAnimatedLength) ce.getCy();
			float cy = safeAnimatedLength(_cy, 0f);

			// 'r' attribute - default is 0 (SVG2)
			AbstractSVGAnimatedLength _r = (AbstractSVGAnimatedLength) ce.getR();
			float r = safeAnimatedLength(_r, 0f);

			float x = cx - r;
			float y = cy - r;
			float w = r * 2f;
			shapeNode.setShape(new Ellipse2D.Float(x, y, w, w));
		} catch (LiveAttributeException ex) {
			throw new BridgeException(ctx, ex);
		}
	}

	// BridgeUpdateHandler implementation //////////////////////////////////

	/**
	 * Invoked when the animated value of an animatable attribute has changed.
	 */
	@Override
	public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue alav) {
		if (alav.getNamespaceURI() == null) {
			String ln = alav.getLocalName();
			if (ln.equals(SVG_CX_ATTRIBUTE) || ln.equals(SVG_CY_ATTRIBUTE) || ln.equals(SVG_R_ATTRIBUTE)) {
				buildShape(ctx, e, (ShapeNode) node);
				handleGeometryChanged();
				return;
			}
		}
		super.handleAnimatedAttributeChanged(alav);
	}

	@Override
	protected ShapePainter createShapePainter(BridgeContext ctx, Element e, ShapeNode shapeNode) {
		Rectangle2D r2d = shapeNode.getShape().getBounds2D();
		if ((r2d.getWidth() == 0) || (r2d.getHeight() == 0))
			return null;
		return super.createShapePainter(ctx, e, shapeNode);
	}

}
