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
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.css.engine.StyleMap;
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
	protected void buildShape(BridgeContext ctx, Element e, ShapeNode shapeNode)
			throws BridgeException {
		SVGOMCircleElement ce = (SVGOMCircleElement) e;

		StyleMap sm = safeComputedStyleMap(ce);

		// 'cx' attribute - default is 0
		float cx = ((AbstractSVGAnimatedLength) ce.getCx()).getAnimatedComputedValue(sm);

		// 'cy' attribute - default is 0
		float cy = ((AbstractSVGAnimatedLength) ce.getCy()).getAnimatedComputedValue(sm);

		// 'r' attribute - default is 0 (SVG2)
		float r = ((AbstractSVGAnimatedLength) ce.getR()).getAnimatedComputedValue(sm);

		if (r < 0f) {
			// Report negative values for r, for the SVG linter use case
			// r could come from calc(), so allow some tolerance
			if (r < -TOLERANCE) {
				BridgeException be = new BridgeException(ctx, e, ERR_LENGTH_NEGATIVE,
						new Object[] { SVG_R_ATTRIBUTE });
				displayErrorOrThrow(ctx, be);
			}
			r = 0f;
		}

		float x = cx - r;
		float y = cy - r;
		float w = r * 2f;
		try {
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
	protected void handleCSSPropertyChanged(int property) {
		switch (property) {
		case SVGCSSEngine.CX_INDEX:
		case SVGCSSEngine.CY_INDEX:
		case SVGCSSEngine.R_INDEX: {
			buildShape(ctx, e, (ShapeNode) node);
			handleGeometryChanged();
			break;
		}
		default:
			super.handleCSSPropertyChanged(property);
		}
	}

	@Override
	protected ShapePainter createShapePainter(BridgeContext ctx, Element e, ShapeNode shapeNode) {
		Rectangle2D r2d = shapeNode.getShape().getBounds2D();
		if (r2d.getWidth() < TOLERANCE || r2d.getHeight() < TOLERANCE) {
			return null;
		}
		return super.createShapePainter(ctx, e, shapeNode);
	}

}
