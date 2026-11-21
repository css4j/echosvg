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

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGGeometryProperty;
import io.sf.carte.echosvg.anim.dom.SVGOMRectElement;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.gvt.ShapePainter;

/**
 * Bridge class for the &lt;rect&gt; element.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGRectElementBridge extends SVGShapeElementBridge {

	/**
	 * Constructs a new bridge for the &lt;rect&gt; element.
	 */
	public SVGRectElementBridge() {
	}

	/**
	 * Returns 'rect'.
	 */
	@Override
	public String getLocalName() {
		return SVG_RECT_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGRectElementBridge();
	}

	/**
	 * Constructs a rectangle according to the specified parameters.
	 *
	 * @param ctx       the bridge context to use
	 * @param e         the element that describes a rect element
	 * @param shapeNode the shape node to initialize
	 */
	@Override
	protected void buildShape(BridgeContext ctx, Element e, ShapeNode shapeNode)
			throws BridgeException {
		SVGOMRectElement re = (SVGOMRectElement) e;

		StyleMap sm = safeComputedStyleMap(re);

		// 'x' attribute - default is 0
		float x = ((SVGGeometryProperty) re.getX()).getAnimatedComputedValue(sm);

		// 'y' attribute - default is 0
		float y = ((SVGGeometryProperty) re.getY()).getAnimatedComputedValue(sm);

		// 'width' attribute - default is 0
		float w = ((SVGGeometryProperty) re.getWidth()).getAnimatedComputedValue(sm);

		// 'height' attribute - default is 0
		float h = ((SVGGeometryProperty) re.getHeight()).getAnimatedComputedValue(sm);

		// 'rx' attribute - default is 0
		boolean rxAuto = false;
		SVGGeometryProperty _rx = (SVGGeometryProperty) re.getRx();
		float rx;
		Value css = _rx.computeAnimatedValue(sm);
		if (css != null) {
			rx = css.getFloatValue();
		} else {
			rx = 0f;
			rxAuto = true;
		}

		if (rx > w / 2f) {
			rx = w / 2f;
		}

		// 'ry' attribute - default is rx
		SVGGeometryProperty _ry = (SVGGeometryProperty) re.getRy();
		float ry;
		css = _ry.computeAnimatedValue(sm);
		if (css != null) {
			ry = css.getFloatValue();
		} else {
			ry = rx;
		}

		if (ry > h / 2f) {
			ry = h / 2f;
		}

		// Check whether rx was auto
		/*
		 * SVG2 §7.4: "When the computed value of ‘rx’ is auto, the used radius is equal
		 * to the absolute length used for ry, creating a circular arc. If both ‘rx’ and
		 * ‘ry’ have a computed value of auto, the used value is 0."
		 */
		if (rxAuto) {
			rx = ry;
		}

		// Report negative values for rx & ry, for the SVG linter use case
		// rx & ry could come from calc(), so allow some tolerance
		if (rx < TOLERANCE || ry < TOLERANCE) {
			if (rx < 0f) {
				if (!rxAuto && rx < -TOLERANCE) {
					BridgeException be = new BridgeException(ctx, e, ERR_LENGTH_NEGATIVE,
							new Object[] { SVG_RX_ATTRIBUTE });
					displayErrorOrThrow(ctx, be);
				}
				rxAuto = true;
				rx = ry;
			}
			if (ry < 0f) {
				if (ry < -TOLERANCE) {
					BridgeException be = new BridgeException(ctx, e, ERR_LENGTH_NEGATIVE,
							new Object[] { SVG_RY_ATTRIBUTE });
					displayErrorOrThrow(ctx, be);
				}
				if (rxAuto) {
					ry = 0f;
					rx = 0f;
				} else {
					ry = rx;
				}
			}
		}

		Shape shape;
		if (rx <= 0f || ry <= 0f) {
			shape = new Rectangle2D.Float(x, y, w, h);
		} else {
			shape = new RoundRectangle2D.Float(x, y, w, h, rx * 2f, ry * 2f);
		}

		try {
			shapeNode.setShape(shape);
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
			if (ln.equals(SVG_X_ATTRIBUTE) || ln.equals(SVG_Y_ATTRIBUTE) || ln.equals(SVG_WIDTH_ATTRIBUTE)
					|| ln.equals(SVG_HEIGHT_ATTRIBUTE) || ln.equals(SVG_RX_ATTRIBUTE) || ln.equals(SVG_RY_ATTRIBUTE)) {
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
		case SVGCSSEngine.X_INDEX:
		case SVGCSSEngine.Y_INDEX:
		case SVGCSSEngine.WIDTH_INDEX:
		case SVGCSSEngine.HEIGHT_INDEX:
		case SVGCSSEngine.RX_INDEX:
		case SVGCSSEngine.RY_INDEX: {
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
		Shape shape = shapeNode.getShape();
		Rectangle2D r2d = shape.getBounds2D();
		if (r2d.getWidth() < TOLERANCE || r2d.getHeight() < TOLERANCE) {
			return null;
		}
		return super.createShapePainter(ctx, e, shapeNode);
	}

}
