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

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGLineElement;

/**
 * This class implements {@link SVGLineElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMLineElement
    extends    SVGGraphicsElement
    implements SVGLineElement {

    /**
     * Table mapping XML attribute names to TraitInformation objects.
     */
    protected static DoublyIndexedTable xmlTraitInformation;
    static {
        DoublyIndexedTable t =
            new DoublyIndexedTable(SVGGraphicsElement.xmlTraitInformation);
        t.put(null, SVG_X1_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
        t.put(null, SVG_Y1_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
        t.put(null, SVG_X2_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
        t.put(null, SVG_Y2_ATTRIBUTE,
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
     * Creates a new SVGOMLineElement object.
     */
    protected SVGOMLineElement() {
    }

    /**
     * Creates a new SVGOMLineElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMLineElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
        initializeLiveAttributes();
    }

    /**
     * Initializes all live attributes for this element.
     */
    protected void initializeAllLiveAttributes() {
        super.initializeAllLiveAttributes();
        initializeLiveAttributes();
    }

    /**
     * Initializes the live attribute values of this element.
     */
    private void initializeLiveAttributes() {
        x1 = createLiveAnimatedLength
            (null, SVG_X1_ATTRIBUTE, SVG_LINE_X1_DEFAULT_VALUE,
             SVGOMAnimatedLength.HORIZONTAL_LENGTH, false);
        y1 = createLiveAnimatedLength
            (null, SVG_Y1_ATTRIBUTE, SVG_LINE_Y1_DEFAULT_VALUE,
             SVGOMAnimatedLength.VERTICAL_LENGTH, false);
        x2 = createLiveAnimatedLength
            (null, SVG_X2_ATTRIBUTE, SVG_LINE_X2_DEFAULT_VALUE,
             SVGOMAnimatedLength.HORIZONTAL_LENGTH, false);
        y2 = createLiveAnimatedLength
            (null, SVG_Y2_ATTRIBUTE, SVG_LINE_Y2_DEFAULT_VALUE,
             SVGOMAnimatedLength.VERTICAL_LENGTH, false);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_LINE_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getX1()}.
     */
    public SVGAnimatedLength getX1() {
        return x1;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getY1()}.
     */
    public SVGAnimatedLength getY1() {
        return y1;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getX2()}.
     */
    public SVGAnimatedLength getX2() {
        return x2;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getY2()}.
     */
    public SVGAnimatedLength getY2() {
        return y2;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMLineElement();
    }

    /**
     * Returns the table of TraitInformation objects for this element.
     */
    protected DoublyIndexedTable getTraitInformationTable() {
        return xmlTraitInformation;
    }
}
