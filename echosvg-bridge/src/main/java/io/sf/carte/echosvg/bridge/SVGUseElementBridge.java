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

import java.awt.Cursor;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGTransformable;
import org.w3c.dom.svg.SVGUseElement;

import io.sf.carte.echosvg.anim.dom.AbstractSVGAnimatedLength;
import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMDocument;
import io.sf.carte.echosvg.anim.dom.SVGOMUseElement;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.dom.svg.SVGOMUseShadowRoot;
import io.sf.carte.echosvg.gvt.CompositeGraphicsNode;
import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for the &lt;use&gt; element.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGUseElementBridge extends AbstractGraphicsNodeBridge {

	/**
	 * Used to handle mutation of the referenced content. This is only used in
	 * dynamic context and only for reference to local content.
	 */
	protected ReferencedElementMutationListener l;

	/**
	 * The bridge context for the referenced document.
	 */
	protected BridgeContext subCtx;

	/**
	 * Constructs a new bridge for the &lt;use&gt; element.
	 */
	public SVGUseElementBridge() {
	}

	/**
	 * Returns 'use'.
	 */
	@Override
	public String getLocalName() {
		return SVG_USE_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGUseElementBridge();
	}

	/**
	 * Creates a <code>GraphicsNode</code> according to the specified parameters.
	 *
	 * @param ctx the bridge context to use
	 * @param e   the element that describes the graphics node to build
	 * @return a graphics node that represents the specified element
	 */
	@Override
	public GraphicsNode createGraphicsNode(BridgeContext ctx, Element e) {
		// 'requiredFeatures', 'requiredExtensions' and 'systemLanguage'
		if (!SVGUtilities.matchUserAgent(e, ctx.getUserAgent()))
			return null;

		CompositeGraphicsNode gn = buildCompositeGraphicsNode(ctx, e, null);
		if (gn == null) {
			return null;
		}
		associateSVGContext(ctx, e, gn);

		return gn;
	}

	/**
	 * Creates a <code>GraphicsNode</code> from the input element and populates the
	 * input <code>CompositeGraphicsNode</code>
	 *
	 * @param ctx the bridge context to use
	 * @param e   the element that describes the graphics node to build
	 * @param gn  the CompositeGraphicsNode where the use graphical content will be
	 *            appended. The composite node is emptied before appending new
	 *            content.
	 */
	public CompositeGraphicsNode buildCompositeGraphicsNode(BridgeContext ctx, Element e, CompositeGraphicsNode gn) {
		// get the referenced element
		SVGOMUseElement ue = (SVGOMUseElement) e;
		String uri = ue.getHref().getAnimVal().trim();
		if (uri.isEmpty()) {
			BridgeException be = new BridgeException(ctx, e, ERR_ATTRIBUTE_MISSING,
					new Object[] { "href" });
			displayErrorOrThrow(ctx, be);
			return null;
		}

		Element refElement = ctx.getReferencedElement(e, uri);

		if (refElement == null) {
			return null; // Missing reference
		}

		// Check for ancestor circularity
		Node anc = e;
		do {
			if (refElement == anc) {
				BridgeException be = new BridgeException(ctx, e, ERR_XLINK_HREF_CIRCULAR_DEPENDENCIES,
						new Object[] { "href" });
				displayErrorOrThrow(ctx, be);
				return null; // Circularity
			}
			anc = anc.getParentNode();
		} while (anc != null && anc.getNodeType() == Node.ELEMENT_NODE);

		// Check that the referenced element is SVG
		if (!SVG_NAMESPACE_URI.equals(refElement.getNamespaceURI())) {
			/* @formatter:off
			 *
			 *  § 5.5:
			 * "If the referenced element that results from resolving the URL is
			 *  not an SVG element, then the reference is invalid and the ‘use’
			 *  element is in error."
			 *
			 * @formatter:on
			 */
			BridgeException be = new BridgeException(ctx, e, ERR_URI_BAD_TARGET,
					new Object[] { "href" });
			displayErrorOrThrow(ctx, be);
			return null;
		}

		SVGOMDocument document, refDocument;
		document = (SVGOMDocument) e.getOwnerDocument();
		refDocument = (SVGOMDocument) refElement.getOwnerDocument();
		boolean isLocal = (refDocument == document);

		BridgeContext theCtx = ctx;
		subCtx = null;
		if (!isLocal) {
			subCtx = (BridgeContext) refDocument.getCSSEngine().getCSSContext();
			theCtx = subCtx;
		}

		// import or clone the referenced element in current document
		Element localRefElement;
		localRefElement = (Element) document.importNode(refElement, true, true);

		if (SVG_SYMBOL_TAG.equals(localRefElement.getLocalName())) {
			// The referenced 'symbol' and its contents are deep-cloned into
			// the generated tree, with the exception that the 'symbol' is
			// replaced by an 'svg'.
			Element svgElement = document.createElementNS(SVG_NAMESPACE_URI, SVG_SVG_TAG);

			// move the attributes from <symbol> to the <svg> element
			NamedNodeMap attrs = localRefElement.getAttributes();
			int len = attrs.getLength();
			for (int i = 0; i < len; i++) {
				Attr attr = (Attr) attrs.item(i);
				svgElement.setAttributeNS(attr.getNamespaceURI(), attr.getName(), attr.getValue());
			}
			// move the children from <symbol> to the <svg> element
			for (Node n = localRefElement.getFirstChild(); n != null; n = localRefElement.getFirstChild()) {
				svgElement.appendChild(n);
			}
			localRefElement = svgElement;
		}

		if (SVG_SVG_TAG.equals(localRefElement.getLocalName())) {
			// The referenced 'svg' and its contents are deep-cloned into the
			// generated tree. If attributes width and/or height are provided
			// on the 'use' element, then these values will override the
			// corresponding attributes on the 'svg' in the generated tree.
			try {
				AbstractSVGAnimatedLength al = (AbstractSVGAnimatedLength) ue.getWidth();
				if (al.isSpecified()) {
					localRefElement.setAttributeNS(null, SVG_WIDTH_ATTRIBUTE, al.getAnimVal().getValueAsString());
				}
				al = (AbstractSVGAnimatedLength) ue.getHeight();
				if (al.isSpecified()) {
					localRefElement.setAttributeNS(null, SVG_HEIGHT_ATTRIBUTE, al.getAnimVal().getValueAsString());
				}
			} catch (LiveAttributeException ex) {
				throw new BridgeException(ctx, ex);
			}
		}

		// attach the referenced element to the current document
		SVGOMUseShadowRoot root;
		root = new SVGOMUseShadowRoot(document, e, isLocal);
		root.appendChild(localRefElement);

		if (gn == null) {
			gn = new CompositeGraphicsNode();
			associateSVGContext(ctx, e, node);
		} else {
			int s = gn.size();
			for (int i = 0; i < s; i++)
				gn.remove(0);
		}

		Node oldRoot = ue.getCSSFirstChild();
		if (oldRoot != null) {
			disposeTree(oldRoot);
		}
		ue.setUseShadowTree(root);

		Element g = localRefElement;

		// compute URIs and style sheets for the used element
		CSSUtilities.computeStyleAndURIs(refElement, localRefElement, uri);

		GVTBuilder builder = ctx.getGVTBuilder();
		GraphicsNode refNode = builder.build(ctx, g);

		if (refNode == null) {
			return null; // No bridge was built for ref. node
		}

		///////////////////////////////////////////////////////////////////////

		gn.getChildren().add(refNode);

		gn.setTransform(computeTransform((SVGTransformable) e, ctx));

		// set an affine transform to take into account the (x, y)
		// coordinates of the <use> element

		// 'visibility'
		gn.setVisible(CSSUtilities.convertVisibility(e));

		RenderingHints hints = null;
		hints = CSSUtilities.convertColorRendering(e, hints);
		if (hints != null)
			gn.setRenderingHints(hints);

		// 'enable-background'
		Rectangle2D r = CSSUtilities.convertEnableBackground(e);
		if (r != null)
			gn.setBackgroundEnable(r);

		if (l != null) {
			// Remove event listeners
			NodeEventTarget target = l.target;
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified", l, true);
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted", l, true);
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved", l, true);
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMCharacterDataModified", l, true);
			l = null;
		}

		///////////////////////////////////////////////////////////////////////

		// Handle mutations on content referenced in the same file if
		// we are in a dynamic context.
		if (isLocal && ctx.isDynamic()) {
			l = new ReferencedElementMutationListener();

			NodeEventTarget target = (NodeEventTarget) refElement;
			l.target = target;

			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified", l, true, null);
			theCtx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified", l, true);

			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted", l, true, null);
			theCtx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted", l, true);

			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved", l, true, null);
			theCtx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved", l, true);

			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMCharacterDataModified", l, true, null);
			theCtx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMCharacterDataModified", l,
					true);
		}

		return gn;
	}

	@Override
	public void dispose() {
		if (l != null) {
			// Remove event listeners
			NodeEventTarget target = l.target;
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMAttrModified", l, true);
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeInserted", l, true);
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMNodeRemoved", l, true);
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMCharacterDataModified", l, true);
			l = null;
		}

		SVGOMUseElement ue = (SVGOMUseElement) e;
		if (ue != null && ue.getCSSFirstChild() != null) {
			disposeTree(ue.getCSSFirstChild());
		}

		super.dispose();

		subCtx = null;
	}

	/**
	 * Returns an {@link AffineTransform} that is the transformation to be applied
	 * to the node.
	 */
	@Override
	protected AffineTransform computeTransform(SVGTransformable e, BridgeContext ctx)
			throws BridgeException {
		AffineTransform at = super.computeTransform(e, ctx);
		SVGUseElement ue = (SVGUseElement) e;

		// 'x' attribute - default is 0
		AbstractSVGAnimatedLength _x = (AbstractSVGAnimatedLength) ue.getX();
		float x = safeAnimatedLength(_x, 0f);

		// 'y' attribute - default is 0
		AbstractSVGAnimatedLength _y = (AbstractSVGAnimatedLength) ue.getY();
		float y = safeAnimatedLength(_y, 0f);

		AffineTransform xy = AffineTransform.getTranslateInstance(x, y);
		xy.preConcatenate(at);

		return xy;
	}

	/**
	 * Creates the GraphicsNode depending on the GraphicsNodeBridge implementation.
	 */
	@Override
	protected GraphicsNode instantiateGraphicsNode() {
		return null; // nothing to do, createGraphicsNode is fully overridden
	}

	/**
	 * Returns false as the &lt;use&gt; element is a not container.
	 */
	@Override
	public boolean isComposite() {
		return false;
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

		super.buildGraphicsNode(ctx, e, node);

		if (ctx.isInteractive()) {
			NodeEventTarget target = (NodeEventTarget) e;
			EventListener l = new CursorMouseOverListener(ctx);
			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOVER, l, false, null);
			ctx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOVER, l, false);
		}
	}

	/**
	 * To handle a mouseover on an anchor and set the cursor.
	 */
	public static class CursorMouseOverListener implements EventListener {

		protected BridgeContext ctx;

		public CursorMouseOverListener(BridgeContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void handleEvent(Event evt) {
			//
			// Only modify the cursor if the current target's (i.e., the <use>) cursor
			// property is *not* 'auto'.
			//
			Element currentTarget = (Element) evt.getCurrentTarget();

			if (!CSSUtilities.isAutoCursor(currentTarget)) {
				Cursor cursor;
				cursor = CSSUtilities.convertCursor(currentTarget, ctx);
				if (cursor != null) {
					ctx.getUserAgent().setSVGCursor(cursor);
				}
			}
		}

	}

	/**
	 * Used to handle modifications to the referenced content
	 */
	protected class ReferencedElementMutationListener implements EventListener {

		protected NodeEventTarget target;

		@Override
		public void handleEvent(Event evt) {
			// We got a mutation in the referenced content. We need to
			// build the content again, just in case.
			// Note that this is way sub-optimal, because multiple changes
			// to the referenced content will cause multiple updates to the
			// referencing <use>. However, this provides the desired behavior
			buildCompositeGraphicsNode(ctx, e, (CompositeGraphicsNode) node);
		}

	}

	// BridgeUpdateHandler implementation //////////////////////////////////

	/**
	 * Invoked when the animated value of an animatable attribute has changed.
	 */
	@Override
	public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue alav) {
		try {
			String ns = alav.getNamespaceURI();
			String ln = alav.getLocalName();
			if (ns == null) {
				if (ln.equals(SVG_X_ATTRIBUTE) || ln.equals(SVG_Y_ATTRIBUTE) || ln.equals(SVG_TRANSFORM_ATTRIBUTE)) {
					node.setTransform(computeTransform((SVGTransformable) e, ctx));
					handleGeometryChanged();
				} else if (ln.equals(SVG_WIDTH_ATTRIBUTE) || ln.equals(SVG_HEIGHT_ATTRIBUTE))
					buildCompositeGraphicsNode(ctx, e, (CompositeGraphicsNode) node);
			} else {
				if (ns.equals(XLINK_NAMESPACE_URI) && ln.equals(XLINK_HREF_ATTRIBUTE))
					buildCompositeGraphicsNode(ctx, e, (CompositeGraphicsNode) node);
			}
		} catch (LiveAttributeException ex) {
			throw new BridgeException(ctx, ex);
		}
		super.handleAnimatedAttributeChanged(alav);
	}

}
