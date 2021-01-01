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
package io.sf.carte.echosvg.dom.svg;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGRect;

/**
 * Context class for svg:svg elements.
 *
 * Eventually this interface will likely have a number of other
 * methods but for now it will have methods to do intersection
 * and enclosure checking.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface SVGSVGContext extends SVGContext {

    /**
     * Returns a List of all the DOM elements that intersect
     * <code>svgRect</code> that are below <code>end</code> in the
     * rendering order.
     */
    List<Element> getIntersectionList(SVGRect svgRect, Element end);

    /**
     * Returns a List of all the DOM elements that are encosed in
     * <code>svgRect</code> that are below <code>end</code> in the
     * rendering order.
     */
    List<Element> getEnclosureList(SVGRect rect, Element end);

    /**
     * Returns true if the given DOM element intersects
     * <code>svgRect</code>.
     */
    boolean checkIntersection(Element element, SVGRect rect);

    /**
     * Returns true if the given DOM element is enclosed in the
     * <code>svgRect</code>.
     */
    boolean checkEnclosure(Element element, SVGRect rect);

    /**
     * Used to inform the user agent that the text selection should be
     * cleared.
     */
    void deselectAll();

    /**
     * Suspends redrawing of the canvas for the given number of milliseconds.
     */
    int suspendRedraw(int max_wait_milliseconds);

    /**
     * Unsuspends redrawing of the canvas.
     */
    boolean unsuspendRedraw(int suspend_handle_id);

    /**
     * Unsuspends redrawing of the canvas.
     */
    void unsuspendRedrawAll();

    /**
     * Forces an immediate redraw of the canvas.
     */
    void forceRedraw();

    /**
     * Pauses animations in the document.
     */
    void pauseAnimations();

    /**
     * Unpauses animations in the document.
     */
    void unpauseAnimations();

    /**
     * Returns whether animations are currently paused.
     */
    boolean animationsPaused();

    /**
     * Returns the current document time.
     */
    float getCurrentTime();

    /**
     * Sets the current document time.
     */
    void setCurrentTime(float t);
}
