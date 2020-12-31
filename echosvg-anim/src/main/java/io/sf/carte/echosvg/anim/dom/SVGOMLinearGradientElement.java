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
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGLinearGradientElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGLinearGradientElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMLinearGradientElement
    extends    SVGOMGradientElement
    implements SVGLinearGradientElement {

    private static final long serialVersionUID = 1L;

    /**
     * Table mapping XML attribute names to TraitInformation objects.
     */
    protected static DoublyIndexedTable xmlTraitInformation;
    static {
        DoublyIndexedTable t =
            new DoublyIndexedTable(SVGOMGradientElement.xmlTraitInformation);
        t.put(null, SVG_X_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
        t.put(null, SVG_Y_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
        t.put(null, SVG_WIDTH_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
        t.put(null, SVG_HEIGHT_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
        xmlTraitInformation = t;
    }

    /**
     * The 'x1' attribute value.
     */
    protected SVGOMAnimatedLength x1;

    /**
     * The 'y1' attribute value.
     */
    protected SVGOMAnimatedLength y1;

    /**
     * The 'x2' attribute value.
     */
    protected SVGOMAnimatedLength x2;

    /**
     * The 'y2' attribute value.
     */
    protected SVGOMAnimatedLength y2;

    /**
     * Creates a new SVGOMLinearGradientElement object.
     */
    protected SVGOMLinearGradientElement() {
    }

    /**
     * Creates a new SVGOMLinearGradientElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMLinearGradientElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
        initializeLiveAttributes();
    }

    /**
     * Initializes all live attributes for this element.
     */
    @Override
    protected void initializeAllLiveAttributes() {
        super.initializeAllLiveAttributes();
        initializeLiveAttributes();
    }

    /**
     * Initializes the live attribute values of this element.
     */
    private void initializeLiveAttributes() {
        x1 = createLiveAnimatedLength
            (null, SVG_X1_ATTRIBUTE, SVG_LINEAR_GRADIENT_X1_DEFAULT_VALUE,
             AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
        y1 = createLiveAnimatedLength
            (null, SVG_Y1_ATTRIBUTE, SVG_LINEAR_GRADIENT_Y1_DEFAULT_VALUE,
             AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
        x2 = createLiveAnimatedLength
            (null, SVG_X2_ATTRIBUTE, SVG_LINEAR_GRADIENT_X2_DEFAULT_VALUE,
             AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
        y2 = createLiveAnimatedLength
            (null, SVG_Y2_ATTRIBUTE, SVG_LINEAR_GRADIENT_Y2_DEFAULT_VALUE,
             AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return SVG_LINEAR_GRADIENT_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLinearGradientElement#getX1()}.
     */
    @Override
    public SVGAnimatedLength getX1() {
        return x1;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLinearGradientElement#getY1()}.
     */
    @Override
    public SVGAnimatedLength getY1() {
        return y1;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLinearGradientElement#getX2()}.
     */
    @Override
    public SVGAnimatedLength getX2() {
        return x2;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLinearGradientElement#getY2()}.
     */
    @Override
    public SVGAnimatedLength getY2() {
        return y2;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGOMLinearGradientElement();
    }

    /**
     * Returns the table of TraitInformation objects for this element.
     */
    @Override
    protected DoublyIndexedTable getTraitInformationTable() {
        return xmlTraitInformation;
    }
}
