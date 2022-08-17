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

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.MutationEvent;

import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMDocument;
import io.sf.carte.echosvg.css.engine.CSSEngineEvent;
import io.sf.carte.echosvg.dom.svg.SVGContext;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.gvt.RootGraphicsNode;

/**
 * Bridge class for an SVGDocument node.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGDocumentBridge implements DocumentBridge, BridgeUpdateHandler, SVGContext {

	/**
	 * The document node this bridge is associated with.
	 */
	protected Document document;

	/**
	 * The graphics node constructed by this bridge.
	 */
	protected RootGraphicsNode node;

	/**
	 * The bridge context.
	 */
	protected BridgeContext ctx;

	/**
	 * Constructs a new bridge the SVG document.
	 */
	public SVGDocumentBridge() {
	}

	// Bridge ////////////////////////////////////////////////////////////////

	/**
	 * Returns the namespace URI of the element this <code>Bridge</code> is
	 * dedicated to. Returns <code>null</code>, as a Document node has no namespace
	 * URI.
	 */
	@Override
	public String getNamespaceURI() {
		return null;
	}

	/**
	 * Returns the local name of the element this <code>Bridge</code> is dedicated
	 * to. Returns <code>null</code>, as a Document node has no local name.
	 */
	@Override
	public String getLocalName() {
		return null;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGDocumentBridge();
	}

	// DocumentBridge ////////////////////////////////////////////////////////

	/**
	 * Creates a <code>GraphicsNode</code> according to the specified parameters.
	 * This is called before children have been added to the returned GraphicsNode
	 * (obviously since you construct and return it).
	 *
	 * @param ctx the bridge context to use
	 * @param doc the document node that describes the graphics node to build
	 * @return a graphics node that represents the specified document node
	 */
	@Override
	public RootGraphicsNode createGraphicsNode(BridgeContext ctx, Document doc) {
		RootGraphicsNode gn = new RootGraphicsNode();
		this.document = doc;
		this.node = gn;
		this.ctx = ctx;
		((SVGOMDocument) doc).setSVGContext(this);
		return gn;
	}

	/**
	 * Builds using the specified BridgeContext and element, the specified graphics
	 * node. This is called after all the children of the node have been constructed
	 * and added, so it is safe to do work that depends on being able to see your
	 * children nodes in this method.
	 *
	 * @param ctx  the bridge context to use
	 * @param doc  the document node that describes the graphics node to build
	 * @param node the graphics node to build
	 */
	@Override
	public void buildGraphicsNode(BridgeContext ctx, Document doc, RootGraphicsNode node) {
		if (ctx.isDynamic()) {
			ctx.bind(doc, node);
		}
	}

	// BridgeUpdateHandler ///////////////////////////////////////////////////

	/**
	 * Invoked when an MutationEvent of type 'DOMAttrModified' is fired.
	 */
	@Override
	public void handleDOMAttrModifiedEvent(MutationEvent evt) {
	}

	/**
	 * Invoked when an MutationEvent of type 'DOMNodeInserted' is fired.
	 */
	@Override
	public void handleDOMNodeInsertedEvent(MutationEvent evt) {
		if (evt.getTarget() instanceof Element) {
			Element childElt = (Element) evt.getTarget();

			GVTBuilder builder = ctx.getGVTBuilder();
			GraphicsNode childNode = builder.build(ctx, childElt);
			if (childNode == null) {
				return;
			}

			// There can only be one document element.
			node.add(childNode);
		}
	}

	/**
	 * Invoked when an MutationEvent of type 'DOMNodeRemoved' is fired.
	 */
	@Override
	public void handleDOMNodeRemovedEvent(MutationEvent evt) {
	}

	/**
	 * Invoked when an MutationEvent of type 'DOMCharacterDataModified' is fired.
	 */
	@Override
	public void handleDOMCharacterDataModified(MutationEvent evt) {
	}

	/**
	 * Invoked when an CSSEngineEvent is fired.
	 */
	@Override
	public void handleCSSEngineEvent(CSSEngineEvent evt) {
	}

	/**
	 * Invoked when the animated value of an animated attribute has changed.
	 */
	@Override
	public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue alav) {
	}

	/**
	 * Invoked when an 'other' animation value has changed.
	 */
	@Override
	public void handleOtherAnimationChanged(String type) {
	}

	/**
	 * Disposes this BridgeUpdateHandler and releases all resources.
	 */
	@Override
	public void dispose() {
		((SVGOMDocument) document).setSVGContext(null);
		ctx.unbind(document);
	}

	// SVGContext //////////////////////////////////////////////////////////

	/**
	 * Returns the size of a px CSS unit in millimeters.
	 */
	@Override
	public float getPixelUnitToMillimeter() {
		return ctx.getUserAgent().getPixelUnitToMillimeter();
	}

	/**
	 * Returns the size of a px CSS unit in millimeters. This will be removed after
	 * next release.
	 * 
	 * @see #getPixelUnitToMillimeter()
	 */
	@Override
	@Deprecated(forRemoval=true)
	public float getPixelToMM() {
		return getPixelUnitToMillimeter();
	}

	@Override
	public Rectangle2D getBBox() {
		return null;
	}

	@Override
	public AffineTransform getScreenTransform() {
		return ctx.getUserAgent().getTransform();
	}

	@Override
	public void setScreenTransform(AffineTransform at) {
		ctx.getUserAgent().setTransform(at);
	}

	@Override
	public AffineTransform getCTM() {
		return null;
	}

	@Override
	public AffineTransform getGlobalTransform() {
		return null;
	}

	@Override
	public float getViewportWidth() {
		return 0f;
	}

	@Override
	public float getViewportHeight() {
		return 0f;
	}

	@Override
	public float getFontSize() {
		return 0;
	}
}
