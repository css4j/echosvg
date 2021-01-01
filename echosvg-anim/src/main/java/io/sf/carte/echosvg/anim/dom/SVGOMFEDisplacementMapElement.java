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
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEDisplacementMapElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGFEDisplacementMapElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFEDisplacementMapElement
    extends    SVGOMFilterPrimitiveStandardAttributes
    implements SVGFEDisplacementMapElement {

    private static final long serialVersionUID = 1L;

    /**
     * Table mapping XML attribute names to TraitInformation objects.
     */
    protected static DoublyIndexedTable<String,String> xmlTraitInformation;
    static {
        DoublyIndexedTable<String,String> t =
            new DoublyIndexedTable<>(SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
        t.put(null, SVG_IN_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_CDATA));
        t.put(null, SVG_IN2_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_CDATA));
        t.put(null, SVG_SCALE_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        t.put(null, SVG_X_CHANNEL_SELECTOR_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_IDENT));
        t.put(null, SVG_Y_CHANNEL_SELECTOR_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_IDENT));
        xmlTraitInformation = t;
    }

    /**
     * The 'xChannelSelector' and 'yChannelSelector' attributes values.
     */
    protected static final String[] CHANNEL_SELECTOR_VALUES = {
        "",
        SVG_R_VALUE,
        SVG_G_VALUE,
        SVG_B_VALUE,
        SVG_A_VALUE
    };

    /**
     * The 'in' attribute value.
     */
    protected SVGOMAnimatedString in;

    /**
     * The 'in2' attribute value.
     */
    protected SVGOMAnimatedString in2;

    /**
     * The 'scale' attribute value.
     */
    protected SVGOMAnimatedNumber scale;

    /**
     * The 'xChannelSelector' attribute value.
     */
    protected SVGOMAnimatedEnumeration xChannelSelector;

    /**
     * The 'yChannelSelector' attribute value.
     */
    protected SVGOMAnimatedEnumeration yChannelSelector;

    /**
     * Creates a new SVGOMFEDisplacementMap object.
     */
    protected SVGOMFEDisplacementMapElement() {
    }

    /**
     * Creates a new SVGOMFEDisplacementMapElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFEDisplacementMapElement(String prefix,
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
        in = createLiveAnimatedString(null, SVG_IN_ATTRIBUTE);
        in2 = createLiveAnimatedString(null, SVG_IN2_ATTRIBUTE);
        scale = createLiveAnimatedNumber(null, SVG_SCALE_ATTRIBUTE, 0f);
        xChannelSelector =
            createLiveAnimatedEnumeration
                (null, SVG_X_CHANNEL_SELECTOR_ATTRIBUTE,
                 CHANNEL_SELECTOR_VALUES, (short) 4);
        yChannelSelector =
            createLiveAnimatedEnumeration
                (null, SVG_Y_CHANNEL_SELECTOR_ATTRIBUTE,
                 CHANNEL_SELECTOR_VALUES, (short) 4);
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return SVG_FE_DISPLACEMENT_MAP_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFEDisplacementMapElement#getIn1()}.
     */
    @Override
    public SVGAnimatedString getIn1() {
        return in;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFEDisplacementMapElement#getIn2()}.
     */
    @Override
    public SVGAnimatedString getIn2() {
        return in2;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGFEDisplacementMapElement#getScale()}.
     */
    @Override
    public SVGAnimatedNumber getScale() {
        return scale;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFEDisplacementMapElement#getXChannelSelector()}.
     */
    @Override
    public SVGAnimatedEnumeration getXChannelSelector() {
        return xChannelSelector;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFEDisplacementMapElement#getYChannelSelector()}.
     */
    @Override
    public SVGAnimatedEnumeration getYChannelSelector() {
        return yChannelSelector;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGOMFEDisplacementMapElement();
    }

    /**
     * Returns the table of TraitInformation objects for this element.
     */
    @Override
    protected DoublyIndexedTable<String,String> getTraitInformationTable() {
        return xmlTraitInformation;
    }
}
