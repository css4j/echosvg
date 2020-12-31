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
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGFEPointLightElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFEPointLightElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMFEPointLightElement
    extends    SVGOMElement
    implements SVGFEPointLightElement {

    /**
     * Table mapping XML attribute names to TraitInformation objects.
     */
    protected static DoublyIndexedTable xmlTraitInformation;
    static {
        DoublyIndexedTable t =
            new DoublyIndexedTable(SVGOMElement.xmlTraitInformation);
        t.put(null, SVG_X_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        t.put(null, SVG_Y_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        t.put(null, SVG_Z_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        xmlTraitInformation = t;
    }

    /**
     * The 'x' attribute value.
     */
    protected SVGOMAnimatedNumber x;

    /**
     * The 'y' attribute value.
     */
    protected SVGOMAnimatedNumber y;

    /**
     * The 'z' attribute value.
     */
    protected SVGOMAnimatedNumber z;

    /**
     * Creates a new SVGOMFEPointLightElement object.
     */
    protected SVGOMFEPointLightElement() {
    }

    /**
     * Creates a new SVGOMFEPointLightElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFEPointLightElement(String prefix,
                                    AbstractDocument owner) {
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
        x = createLiveAnimatedNumber(null, SVG_X_ATTRIBUTE, 0f);
        y = createLiveAnimatedNumber(null, SVG_Y_ATTRIBUTE, 0f);
        z = createLiveAnimatedNumber(null, SVG_Z_ATTRIBUTE, 0f);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return SVG_FE_POINT_LIGHT_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEPointLightElement#getX()}.
     */
    @Override
    public SVGAnimatedNumber getX() {
        return x;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEPointLightElement#getY()}.
     */
    @Override
    public SVGAnimatedNumber getY() {
        return y;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEPointLightElement#getZ()}.
     */
    @Override
    public SVGAnimatedNumber getZ() {
        return z;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGOMFEPointLightElement();
    }

    /**
     * Returns the table of TraitInformation objects for this element.
     */
    @Override
    protected DoublyIndexedTable getTraitInformationTable() {
        return xmlTraitInformation;
    }
}
