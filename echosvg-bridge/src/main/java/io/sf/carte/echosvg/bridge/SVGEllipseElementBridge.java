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

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.AbstractSVGAnimatedLength;
import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.anim.dom.SVGOMEllipseElement;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.gvt.ShapePainter;

/**
 * Bridge class for the &lt;ellipse&gt; element.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @version $Id$
 */
public class SVGEllipseElementBridge extends SVGShapeElementBridge {

    /**
     * Constructs a new bridge for the &lt;ellipse&gt; element.
     */
    public SVGEllipseElementBridge() {}

    /**
     * Returns 'ellipse'.
     */
    public String getLocalName() {
        return SVG_ELLIPSE_TAG;
    }

    /**
     * Returns a new instance of this bridge.
     */
    public Bridge getInstance() {
        return new SVGEllipseElementBridge();
    }

    /**
     * Constructs an ellipse according to the specified parameters.
     *
     * @param ctx the bridge context to use
     * @param e the element that describes a rect element
     * @param shapeNode the shape node to initialize
     */
    protected void buildShape(BridgeContext ctx,
                              Element e,
                              ShapeNode shapeNode) {
        try {
            SVGOMEllipseElement ee = (SVGOMEllipseElement) e;

            // 'cx' attribute - default is 0
            AbstractSVGAnimatedLength _cx =
                (AbstractSVGAnimatedLength) ee.getCx();
            float cx = _cx.getCheckedValue();

            // 'cy' attribute - default is 0
            AbstractSVGAnimatedLength _cy =
                (AbstractSVGAnimatedLength) ee.getCy();
            float cy = _cy.getCheckedValue();

            // 'rx' attribute - required
            AbstractSVGAnimatedLength _rx =
                (AbstractSVGAnimatedLength) ee.getRx();
            float rx = _rx.getCheckedValue();

            // 'ry' attribute - required
            AbstractSVGAnimatedLength _ry =
                (AbstractSVGAnimatedLength) ee.getRy();
            float ry = _ry.getCheckedValue();

            shapeNode.setShape(new Ellipse2D.Float(cx - rx, cy - ry,
                                                   rx * 2, ry * 2));
        } catch (LiveAttributeException ex) {
            throw new BridgeException(ctx, ex);
        }
    }

    // BridgeUpdateHandler implementation //////////////////////////////////

    /**
     * Invoked when the animated value of an animatable attribute has changed.
     */
    public void handleAnimatedAttributeChanged
            (AnimatedLiveAttributeValue alav) {
        if (alav.getNamespaceURI() == null) {
            String ln = alav.getLocalName();
            if (ln.equals(SVG_CX_ATTRIBUTE)
                    || ln.equals(SVG_CY_ATTRIBUTE)
                    || ln.equals(SVG_RX_ATTRIBUTE)
                    || ln.equals(SVG_RY_ATTRIBUTE)) {
                buildShape(ctx, e, (ShapeNode)node);
                handleGeometryChanged();
                return;
            }
        }
        super.handleAnimatedAttributeChanged(alav);
    }

    protected ShapePainter createShapePainter(BridgeContext ctx,
                                              Element e,
                                              ShapeNode shapeNode) {
        Rectangle2D r2d = shapeNode.getShape().getBounds2D();
        if ((r2d.getWidth() == 0) || (r2d.getHeight() == 0))
            return null;
        return super.createShapePainter(ctx, e, shapeNode);
    }
}
