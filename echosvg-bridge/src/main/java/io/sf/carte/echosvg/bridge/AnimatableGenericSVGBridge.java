/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

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
 * Abstract bridge class for animatable elements that do not produce
 * a GraphicsNode.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id$
 */
public abstract class AnimatableGenericSVGBridge
        extends AnimatableSVGBridge
        implements GenericBridge, BridgeUpdateHandler, SVGContext {

    /**
     * Invoked to handle an <code>Element</code> for a given <code>BridgeContext</code>.
     * For example, see the <code>SVGTitleElementBridge</code>.
     *
     * @param ctx the bridge context to use
     * @param e the element being handled
     */
    @Override
    public void handleElement(BridgeContext ctx, Element e) {
        if (ctx.isDynamic()) {
            this.e = e;
            this.ctx = ctx;
            ((SVGOMElement) e).setSVGContext(this);
        }
    }

    // SVGContext ////////////////////////////////////////////////////////////

    /**
     * Returns the size of a px CSS unit in millimeters.
     */
    @Override
    public float getPixelUnitToMillimeter() {
        return ctx.getUserAgent().getPixelUnitToMillimeter();
    }

    /**
     * Returns the size of a px CSS unit in millimeters.
     * This will be removed after next release.
     * @see #getPixelUnitToMillimeter()
     */
    @Override
    public float getPixelToMM() {
        return getPixelUnitToMillimeter();
    }

    /**
     * Returns the tight bounding box in current user space (i.e.,
     * after application of the transform attribute, if any) on the
     * geometry of all contained graphics elements, exclusive of
     * stroke-width and filter effects).
     */
    @Override
    public Rectangle2D getBBox() {
        return null;
    }

    /**
     * Returns the transform from the global transform space to pixels.
     */
    @Override
    public AffineTransform getScreenTransform() {
        return ctx.getUserAgent().getTransform();
    }

    /**
     * Sets the transform to be used from the global transform space to pixels.
     */
    @Override
    public void setScreenTransform(AffineTransform at) {
        ctx.getUserAgent().setTransform(at);
    }

    /**
     * Returns the transformation matrix from current user units
     * (i.e., after application of the transform attribute, if any) to
     * the viewport coordinate system for the nearestViewportElement.
     */
    @Override
    public AffineTransform getCTM() {
        return null;
    }

    /**
     * Returns the global transformation matrix from the current
     * element to the root.
     */
    @Override
    public AffineTransform getGlobalTransform() {
        return null;
    }

    /**
     * Returns the width of the viewport which directly contains the
     * associated element.
     */
    @Override
    public float getViewportWidth() {
        return 0f;
    }

    /**
     * Returns the height of the viewport which directly contains the
     * associated element.
     */
    @Override
    public float getViewportHeight() {
        return 0f;
    }

    /**
     * Returns the font-size on the associated element.
     */
    @Override
    public float getFontSize() {
        return 0f;
    }

    // BridgeUpdateHandler ///////////////////////////////////////////////////

    @Override
    public void dispose() {
        ((SVGOMElement) e).setSVGContext(null);
    }

    @Override
    public void handleDOMNodeInsertedEvent(MutationEvent evt) { 
    }

    @Override
    public void handleDOMCharacterDataModified(MutationEvent evt) { 
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
    public void handleAnimatedAttributeChanged
            (AnimatedLiveAttributeValue alav) {
    }

    @Override
    public void handleOtherAnimationChanged(String type) {
    }
}
