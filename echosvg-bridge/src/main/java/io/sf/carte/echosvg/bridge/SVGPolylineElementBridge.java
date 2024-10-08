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

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGPointList;

import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMAnimatedPoints;
import io.sf.carte.echosvg.anim.dom.SVGOMPolylineElement;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.parser.AWTPolylineProducer;

/**
 * Bridge class for the &lt;polyline&gt; element.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGPolylineElementBridge extends SVGDecoratedShapeElementBridge {

	/**
	 * Constructs a new bridge for the &lt;polyline&gt; element.
	 */
	public SVGPolylineElementBridge() {
	}

	/**
	 * Returns 'polyline'.
	 */
	@Override
	public String getLocalName() {
		return SVG_POLYLINE_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGPolylineElementBridge();
	}

	/**
	 * Constructs a polyline according to the specified parameters.
	 *
	 * @param ctx       the bridge context to use
	 * @param e         the element that describes a rect element
	 * @param shapeNode the shape node to initialize
	 */
	@Override
	protected void buildShape(BridgeContext ctx, Element e, ShapeNode shapeNode) {
		SVGOMPolylineElement pe = (SVGOMPolylineElement) e;
		SVGOMAnimatedPoints _points = pe.getSVGOMAnimatedPoints();

		short check = _points.check();
		if (check >= 0) { // Either (none) or errors detected
			if (check == LiveAttributeException.ERR_ATTRIBUTE_MISSING) {
				// "The initial value, (none), indicates that the polyline element
				// is valid, but does not render."
				//
				// We could just return, but in case that we are updating the
				// ShapeNode, we want to set the shape.
				shapeNode.setShape(EMPTY_SHAPE);
				shapeNode.setVisible(false);
				return;
			}
			// Must be LiveAttributeException.ERR_ATTRIBUTE_MALFORMED:
			LiveAttributeException lex = new LiveAttributeException(e, e.getLocalName(), check,
					_points.getPoints().toString());
			BridgeException be = new BridgeException(ctx, lex);
			displayErrorOrThrow(ctx, be);
		}

		try {
			SVGPointList pl = _points.getAnimatedPoints();
			int size = pl.getNumberOfItems();
			if (size == 0) {
				shapeNode.setShape(EMPTY_SHAPE);
				shapeNode.setVisible(false);
			} else {
				AWTPolylineProducer app = new AWTPolylineProducer();
				app.setWindingRule(CSSUtilities.convertFillRule(e));
				app.startPoints();
				for (int i = 0; i < size; i++) {
					SVGPoint p = pl.getItem(i);
					app.point(p.getX(), p.getY());
				}
				app.endPoints();
				shapeNode.setVisible(CSSUtilities.convertVisibility(e));
				shapeNode.setShape(app.getShape());
			}
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
			if (ln.equals(SVG_POINTS_ATTRIBUTE)) {
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
		case SVGCSSEngine.FILL_RULE_INDEX:
			buildShape(ctx, e, (ShapeNode) node);
			handleGeometryChanged();
			break;
		default:
			super.handleCSSPropertyChanged(property);
		}
	}

}
