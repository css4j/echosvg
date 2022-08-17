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

import org.w3c.dom.Element;
import org.w3c.dom.events.MutationEvent;

import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMElement;
import io.sf.carte.echosvg.css.engine.CSSEngineEvent;
import io.sf.carte.echosvg.dom.svg.SVGContext;

/**
 * Base class for 'descriptive' elements, mostly title and desc.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGDescriptiveElementBridge extends AbstractSVGBridge
		implements GenericBridge, BridgeUpdateHandler, SVGContext {

	Element theElt;
	Element parent;
	BridgeContext theCtx;

	public SVGDescriptiveElementBridge() {
	}

	/**
	 * Invoked to handle an <code>Element</code> for a given
	 * <code>BridgeContext</code>. For example, see the
	 * <code>SVGDescElementBridge</code>.
	 *
	 * @param ctx the bridge context to use
	 * @param e   the element to be handled
	 */
	@Override
	public void handleElement(BridgeContext ctx, Element e) {
		UserAgent ua = ctx.getUserAgent();
		ua.handleElement(e, Boolean.TRUE);

		if (ctx.isDynamic()) {
			SVGDescriptiveElementBridge b;
			b = (SVGDescriptiveElementBridge) getInstance();
			b.theElt = e;
			b.parent = (Element) e.getParentNode();
			b.theCtx = ctx;
			((SVGOMElement) e).setSVGContext(b);
		}

	}

	// BridgeUpdateHandler implementation ////////////////////////////////////

	@Override
	public void dispose() {
		UserAgent ua = theCtx.getUserAgent();
		((SVGOMElement) theElt).setSVGContext(null);
		ua.handleElement(theElt, parent);
		theElt = null;
		parent = null;
	}

	@Override
	public void handleDOMNodeInsertedEvent(MutationEvent evt) {
		UserAgent ua = theCtx.getUserAgent();
		ua.handleElement(theElt, Boolean.TRUE);
	}

	@Override
	public void handleDOMCharacterDataModified(MutationEvent evt) {
		UserAgent ua = theCtx.getUserAgent();
		ua.handleElement(theElt, Boolean.TRUE);
	}

	@Override
	public void handleDOMNodeRemovedEvent(MutationEvent evt) {
		dispose();
	}

	@Override
	public void handleDOMAttrModifiedEvent(MutationEvent evt) {
	}

	@Override
	public void handleCSSEngineEvent(CSSEngineEvent evt) {
	}

	@Override
	public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue alav) {
	}

	@Override
	public void handleOtherAnimationChanged(String type) {
	}

	// SVGContext implementation ///////////////////////////////////////////

	/**
	 * Returns the size of a px CSS unit in millimeters.
	 */
	@Override
	public float getPixelUnitToMillimeter() {
		return theCtx.getUserAgent().getPixelUnitToMillimeter();
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
		return theCtx.getUserAgent().getTransform();
	}

	@Override
	public void setScreenTransform(AffineTransform at) {
		theCtx.getUserAgent().setTransform(at);
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
		return theCtx.getBlockWidth(theElt);
	}

	@Override
	public float getViewportHeight() {
		return theCtx.getBlockHeight(theElt);
	}

	@Override
	public float getFontSize() {
		return 0;
	}
}
