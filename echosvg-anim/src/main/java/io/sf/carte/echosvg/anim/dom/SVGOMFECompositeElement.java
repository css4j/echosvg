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
import org.w3c.dom.svg.SVGFECompositeElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFECompositeElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFECompositeElement
    extends    SVGOMFilterPrimitiveStandardAttributes
    implements SVGFECompositeElement {

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
        t.put(null, SVG_OPERATOR_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_IDENT));
        t.put(null, SVG_K1_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        t.put(null, SVG_K2_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        t.put(null, SVG_K3_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        t.put(null, SVG_K4_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER));
        xmlTraitInformation = t;
    }

    /**
     * The 'operator' attribute values.
     */
    protected static final String[] OPERATOR_VALUES = {
        "",
        SVG_OVER_VALUE,
        SVG_IN_VALUE,
        SVG_OUT_VALUE,
        SVG_ATOP_VALUE,
        SVG_XOR_VALUE,
        SVG_ARITHMETIC_VALUE
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
     * The 'operator' attribute value.
     */
    protected SVGOMAnimatedEnumeration operator;

    /**
     * The 'k1' attribute value.
     */
    protected SVGOMAnimatedNumber k1;

    /**
     * The 'k2' attribute value.
     */
    protected SVGOMAnimatedNumber k2;

    /**
     * The 'k3' attribute value.
     */
    protected SVGOMAnimatedNumber k3;

    /**
     * The 'k4' attribute value.
     */
    protected SVGOMAnimatedNumber k4;

    /**
     * Creates a new SVGOMFECompositeElement object.
     */
    protected SVGOMFECompositeElement() {
    }

    /**
     * Creates a new SVGOMFECompositeElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFECompositeElement(String prefix, AbstractDocument owner) {
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
        operator =
            createLiveAnimatedEnumeration
                (null, SVG_OPERATOR_ATTRIBUTE, OPERATOR_VALUES, (short) 1);
        k1 = createLiveAnimatedNumber(null, SVG_K1_ATTRIBUTE, 0f);
        k2 = createLiveAnimatedNumber(null, SVG_K2_ATTRIBUTE, 0f);
        k3 = createLiveAnimatedNumber(null, SVG_K3_ATTRIBUTE, 0f);
        k4 = createLiveAnimatedNumber(null, SVG_K4_ATTRIBUTE, 0f);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return SVG_FE_COMPOSITE_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFECompositeElement#getIn1()}.
     */
    @Override
    public SVGAnimatedString getIn1() {
        return in;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFECompositeElement#getIn2()}.
     */
    @Override
    public SVGAnimatedString getIn2() {
        return in2;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFECompositeElement#getOperator()}.
     */
    @Override
    public SVGAnimatedEnumeration getOperator() {
        return operator;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFECompositeElement#getK1()}.
     */
    @Override
    public SVGAnimatedNumber getK1() {
        return k1;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFECompositeElement#getK2()}.
     */
    @Override
    public SVGAnimatedNumber getK2() {
        return k2;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFECompositeElement#getK3()}.
     */
    @Override
    public SVGAnimatedNumber getK3() {
        return k3;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFECompositeElement#getK4()}.
     */
    @Override
    public SVGAnimatedNumber getK4() {
        return k4;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGOMFECompositeElement();
    }

    /**
     * Returns the table of TraitInformation objects for this element.
     */
    @Override
    protected DoublyIndexedTable<String,String> getTraitInformationTable() {
        return xmlTraitInformation;
    }
}
