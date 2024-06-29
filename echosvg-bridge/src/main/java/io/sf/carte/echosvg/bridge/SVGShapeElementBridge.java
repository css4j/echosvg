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

import java.awt.RenderingHints;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.AbstractSVGAnimatedLength;
import io.sf.carte.echosvg.css.engine.CSSEngineEvent;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.gvt.ShapePainter;

/**
 * The base bridge class for shapes. Subclasses bridge <code>ShapeNode</code>.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGShapeElementBridge extends AbstractGraphicsNodeBridge {

	/**
	 * Constructs a new bridge for SVG shapes.
	 */
	protected SVGShapeElementBridge() {
	}

	/**
	 * Creates a graphics node using the specified BridgeContext and for the
	 * specified element.
	 *
	 * @param ctx the bridge context to use
	 * @param e   the element that describes the graphics node to build
	 * @return a graphics node that represents the specified element
	 */
	@Override
	public GraphicsNode createGraphicsNode(BridgeContext ctx, Element e) {
		ShapeNode shapeNode = (ShapeNode) super.createGraphicsNode(ctx, e);
		if (shapeNode == null) {
			return null;
		}

		associateSVGContext(ctx, e, shapeNode);

		// delegates to subclasses the shape construction
		buildShape(ctx, e, shapeNode);

		// 'shape-rendering' and 'color-rendering'
		RenderingHints hints = null;
		hints = CSSUtilities.convertColorRendering(e, hints);
		hints = CSSUtilities.convertShapeRendering(e, hints);
		if (hints != null)
			shapeNode.setRenderingHints(hints);

		return shapeNode;
	}

	/**
	 * Creates a <code>ShapeNode</code>.
	 */
	@Override
	protected GraphicsNode instantiateGraphicsNode() {
		return new ShapeNode();
	}

	/**
	 * Builds using the specified BridgeContext and element, the specified graphics
	 * node.
	 *
	 * @param ctx  the bridge context to use
	 * @param e    the element that describes the graphics node to build
	 * @param node the graphics node to build
	 */
	@Override
	public void buildGraphicsNode(BridgeContext ctx, Element e, GraphicsNode node) {
		ShapeNode shapeNode = (ShapeNode) node;
		shapeNode.setShapePainter(createShapePainter(ctx, e, shapeNode));
		super.buildGraphicsNode(ctx, e, node);
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
	protected ShapePainter createShapePainter(BridgeContext ctx, Element e, ShapeNode shapeNode) {
		// 'fill'
		// 'fill-opacity'
		// 'stroke'
		// 'stroke-opacity',
		// 'stroke-width'
		// 'stroke-linecap'
		// 'stroke-linejoin'
		// 'stroke-miterlimit'
		// 'stroke-dasharray'
		// 'stroke-dashoffset'
		return PaintServer.convertFillAndStroke(e, shapeNode, ctx);
	}

	/**
	 * Initializes the specified ShapeNode's shape defined by the specified Element
	 * and using the specified bridge context.
	 *
	 * @param ctx  the bridge context to use
	 * @param e    the element that describes the shape node to build
	 * @param node the shape node to initialize
	 */
	protected abstract void buildShape(BridgeContext ctx, Element e, ShapeNode node);

	/**
	 * Give a safe value for an animated length, regardless of exceptions.
	 * 
	 * @param animValue the animated length.
	 * @param defValue  the default value.
	 * @return the value.
	 */
	float safeAnimatedCheckedValue(AbstractSVGAnimatedLength animValue, float defValue) {
		float value;
		try {
			value = animValue.getCheckedValue();
		} catch (LiveAttributeException ex) {
			value = defValue;
			BridgeException be = new BridgeException(ctx, ex);
			if (ctx.userAgent == null) {
				throw be;
			}
			ctx.userAgent.displayError(be);
		}
		return value;
	}

	/**
	 * Returns false as shapes are not a container.
	 */
	@Override
	public boolean isComposite() {
		return false;
	}

	// BridgeUpdateHandler implementation //////////////////////////////////

	/**
	 * Invoked when the geometry of an graphical element has changed.
	 */
	@Override
	protected void handleGeometryChanged() {
		super.handleGeometryChanged();
		ShapeNode shapeNode = (ShapeNode) node;
		shapeNode.setShapePainter(createShapePainter(ctx, e, shapeNode));
	}

	/**
	 * This flag bit indicates if a new shape painter has already been created.
	 * Avoid creating one ShapePainter per CSS property change
	 */
	protected boolean hasNewShapePainter;

	/**
	 * Invoked when CSS properties have changed on an element.
	 *
	 * @param evt the CSSEngine event that describes the update
	 */
	@Override
	public void handleCSSEngineEvent(CSSEngineEvent evt) {
		hasNewShapePainter = false;
		super.handleCSSEngineEvent(evt);
	}

	/**
	 * Invoked for each CSS property that has changed.
	 */
	@Override
	protected void handleCSSPropertyChanged(int property) {
		switch (property) {
		case SVGCSSEngine.FILL_INDEX:
		case SVGCSSEngine.FILL_OPACITY_INDEX:
		case SVGCSSEngine.STROKE_INDEX:
		case SVGCSSEngine.STROKE_OPACITY_INDEX:
			// Opportunity to just 'update' the existing shape painters...
		case SVGCSSEngine.STROKE_WIDTH_INDEX:
		case SVGCSSEngine.STROKE_LINECAP_INDEX:
		case SVGCSSEngine.STROKE_LINEJOIN_INDEX:
		case SVGCSSEngine.STROKE_MITERLIMIT_INDEX:
		case SVGCSSEngine.STROKE_DASHARRAY_INDEX:
		case SVGCSSEngine.STROKE_DASHOFFSET_INDEX: {
			if (!hasNewShapePainter) {
				hasNewShapePainter = true;
				ShapeNode shapeNode = (ShapeNode) node;
				shapeNode.setShapePainter(createShapePainter(ctx, e, shapeNode));
			}
			break;
		}
		case SVGCSSEngine.SHAPE_RENDERING_INDEX: {
			RenderingHints hints = node.getRenderingHints();
			hints = CSSUtilities.convertShapeRendering(e, hints);
			if (hints != null) {
				node.setRenderingHints(hints);
			}
			break;
		}
		case SVGCSSEngine.COLOR_RENDERING_INDEX: {
			RenderingHints hints = node.getRenderingHints();
			hints = CSSUtilities.convertColorRendering(e, hints);
			if (hints != null) {
				node.setRenderingHints(hints);
			}
			break;
		}
		default:
			super.handleCSSPropertyChanged(property);
		}
	}

}
