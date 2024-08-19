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
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGPathSegList;

import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMAnimatedPathData;
import io.sf.carte.echosvg.anim.dom.SVGOMPathElement;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.dom.svg.SVGAnimatedPathDataSupport;
import io.sf.carte.echosvg.dom.svg.SVGPathContext;
import io.sf.carte.echosvg.ext.awt.geom.PathLength;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.parser.AWTPathProducer;

/**
 * Bridge class for the &lt;path&gt; element.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGPathElementBridge extends SVGDecoratedShapeElementBridge implements SVGPathContext {

	/**
	 * default shape for the update of 'd' when the value is the empty string.
	 */
	protected static final Shape DEFAULT_SHAPE = new GeneralPath();

	/**
	 * Constructs a new bridge for the &lt;path&gt; element.
	 */
	public SVGPathElementBridge() {
	}

	/**
	 * Returns 'path'.
	 */
	@Override
	public String getLocalName() {
		return SVG_PATH_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGPathElementBridge();
	}

	/**
	 * Constructs a path according to the specified parameters.
	 *
	 * @param ctx       the bridge context to use
	 * @param e         the element that describes a rect element
	 * @param shapeNode the shape node to initialize
	 */
	@Override
	protected void buildShape(BridgeContext ctx, Element e, ShapeNode shapeNode) {
		SVGOMPathElement pe = (SVGOMPathElement) e;
		AWTPathProducer app = new AWTPathProducer();
		// 'd' attribute - ignore shape if not present
		SVGOMAnimatedPathData _d = pe.getAnimatedPathData();

		short check = _d.check();
		if (check >= 0) { // Either 'none' or new errors detected
			if (check == LiveAttributeException.ERR_ATTRIBUTE_MISSING) {
				// none
				/*
				 * SVG 2.0 § 9.3.2. "The value none indicates that there is no path data for the
				 * element. For ‘path’ elements, this means that the element does not render or
				 * contribute to the bounding box of ancestor container elements."
				 */

				// We could just return, but in case that we are updating the
				// ShapeNode, we want to set the shape.
				shapeNode.setShape(EMPTY_SHAPE);
				shapeNode.setVisible(false);
				return;
			}
			// Must be LiveAttributeException.ERR_ATTRIBUTE_MALFORMED:
			LiveAttributeException lex = new LiveAttributeException(e, e.getLocalName(), check,
					_d.getPathSegList().toString());
			BridgeException be = new BridgeException(ctx, lex);
			displayErrorOrThrow(ctx, be);
		}

		if (_d.getPathSegList().getNumberOfItems() == 0) {
			// § 9.3.2. "If the path data string contains no valid commands,
			// then the behavior is the same as the none value."
			//
			// We could just return, but in case that we are updating the
			// ShapeNode, we want to set the shape.
			shapeNode.setShape(EMPTY_SHAPE);
			shapeNode.setVisible(false);
			return;
		}

		SVGPathSegList p = _d.getAnimatedPathSegList();
		try {
			app.setWindingRule(CSSUtilities.convertFillRule(e));
			shapeNode.setVisible(CSSUtilities.convertVisibility(e));
			SVGAnimatedPathDataSupport.handlePathSegList(p, app);
		} catch (LiveAttributeException ex) {
			// Probably not thrown here
			throw new BridgeException(ctx, ex);
		} finally {
			shapeNode.setShape(app.getShape());
		}
	}

	// BridgeUpdateHandler implementation //////////////////////////////////

	/**
	 * Invoked when the animated value of an animatable attribute has changed.
	 */
	@Override
	public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue alav) {
		if (alav.getNamespaceURI() == null && alav.getLocalName().equals(SVG_D_ATTRIBUTE)) {
			buildShape(ctx, e, (ShapeNode) node);
			handleGeometryChanged();
		} else {
			super.handleAnimatedAttributeChanged(alav);
		}
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

	// SVGPathContext ////////////////////////////////////////////////////////

	/**
	 * The cached Shape used for computing the path length.
	 */
	protected Shape pathLengthShape;

	/**
	 * The cached PathLength object used for computing the path length.
	 */
	protected PathLength pathLength;

	/**
	 * Returns the PathLength object that tracks the length of the path.
	 */
	protected PathLength getPathLengthObj() {
		Shape s = ((ShapeNode) node).getShape();
		if (pathLengthShape != s) {
			pathLength = new PathLength(s);
			pathLengthShape = s;
		}
		return pathLength;
	}

	/**
	 * Returns the total length of the path.
	 */
	@Override
	public float getTotalLength() {
		PathLength pl = getPathLengthObj();
		return pl.lengthOfPath();
	}

	/**
	 * Returns the point at the given distance along the path.
	 */
	@Override
	public Point2D getPointAtLength(float distance) {
		PathLength pl = getPathLengthObj();
		return pl.pointAtLength(distance);
	}

	/**
	 * Returns the index of the path segment at the given distance along the path.
	 */
	@Override
	public int getPathSegAtLength(float distance) {
		PathLength pl = getPathLengthObj();
		return pl.segmentAtLength(distance);
	}

}
