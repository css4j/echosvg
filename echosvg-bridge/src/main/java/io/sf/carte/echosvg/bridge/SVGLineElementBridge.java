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

import java.awt.geom.Line2D;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.AbstractSVGAnimatedLength;
import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMLineElement;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.gvt.ShapePainter;

/**
 * Bridge class for the &lt;line&gt; element.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGLineElementBridge extends SVGDecoratedShapeElementBridge {

	/**
	 * Constructs a new bridge for the &lt;line&gt; element.
	 */
	public SVGLineElementBridge() {
	}

	/**
	 * Returns 'line'.
	 */
	@Override
	public String getLocalName() {
		return SVG_LINE_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGLineElementBridge();
	}

	/**
	 * Creates the shape painter associated to the specified element. This
	 * implementation creates a shape painter considering the various fill and
	 * stroke properties.
	 *
	 * @param ctx       the bridge context to use
	 * @param e         the element that describes the shape painter to use
	 * @param shapeNode the shape node that is interested in its shape painter
	 */
	@Override
	protected ShapePainter createFillStrokePainter(BridgeContext ctx, Element e, ShapeNode shapeNode) {
		// 'fill' - ignored
		// 'fill-opacity' - ignored
		// 'stroke'
		// 'stroke-opacity',
		// 'stroke-width'
		// 'stroke-linecap'
		// 'stroke-linejoin'
		// 'stroke-miterlimit'
		// 'stroke-dasharray'
		// 'stroke-dashoffset'
		return PaintServer.convertStrokePainter(e, shapeNode, ctx);
	}

	/**
	 * Constructs a line according to the specified parameters.
	 *
	 * @param ctx       the bridge context to use
	 * @param e         the element that describes a rect element
	 * @param shapeNode the shape node to initialize
	 */
	@Override
	protected void buildShape(BridgeContext ctx, Element e, ShapeNode shapeNode)
			throws BridgeException {
		SVGOMLineElement le = (SVGOMLineElement) e;

		// 'x1' attribute - default is 0
		AbstractSVGAnimatedLength _x1 = (AbstractSVGAnimatedLength) le.getX1();
		float x1 = safeAnimatedLength(_x1, 0f);

		// 'y1' attribute - default is 0
		AbstractSVGAnimatedLength _y1 = (AbstractSVGAnimatedLength) le.getY1();
		float y1 = safeAnimatedLength(_y1, 0f);

		// 'x2' attribute - default is 0
		AbstractSVGAnimatedLength _x2 = (AbstractSVGAnimatedLength) le.getX2();
		float x2 = safeAnimatedLength(_x2, 0f);

		// 'y2' attribute - default is 0
		AbstractSVGAnimatedLength _y2 = (AbstractSVGAnimatedLength) le.getY2();
		float y2 = safeAnimatedLength(_y2, 0f);

		try {
			shapeNode.setShape(new Line2D.Float(x1, y1, x2, y2));
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
			if (ln.equals(SVG_X1_ATTRIBUTE) || ln.equals(SVG_Y1_ATTRIBUTE) || ln.equals(SVG_X2_ATTRIBUTE)
					|| ln.equals(SVG_Y2_ATTRIBUTE)) {
				buildShape(ctx, e, (ShapeNode) node);
				handleGeometryChanged();
				return;
			}
		}
		super.handleAnimatedAttributeChanged(alav);
	}

}
