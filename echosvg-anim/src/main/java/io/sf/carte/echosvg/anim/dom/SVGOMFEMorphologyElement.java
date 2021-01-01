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
import org.w3c.dom.svg.SVGFEMorphologyElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFEMorphologyElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFEMorphologyElement
    extends    SVGOMFilterPrimitiveStandardAttributes
    implements SVGFEMorphologyElement {

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
        t.put(null, SVG_OPERATOR_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_IDENT));
        t.put(null, SVG_RADIUS_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_NUMBER_OPTIONAL_NUMBER));
        xmlTraitInformation = t;
    }

    /**
     * The 'operator' attribute values.
     */
    protected static final String[] OPERATOR_VALUES = {
        "",
        SVG_ERODE_VALUE,
        SVG_DILATE_VALUE
    };

    /**
     * The 'in' attribute value.
     */
    protected SVGOMAnimatedString in;

    /**
     * The 'operator' attribute value.
     */
    protected SVGOMAnimatedEnumeration operator;

    /**
     * Creates a new SVGOMFEMorphologyElement object.
     */
    protected SVGOMFEMorphologyElement() {
    }

    /**
     * Creates a new SVGOMFEMorphologyElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFEMorphologyElement(String prefix, AbstractDocument owner) {
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
        operator =
            createLiveAnimatedEnumeration
                (null, SVG_OPERATOR_ATTRIBUTE, OPERATOR_VALUES, (short) 1);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return SVG_FE_MORPHOLOGY_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEMorphologyElement#getIn1()}.
     */
    @Override
    public SVGAnimatedString getIn1() {
        return in;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEMorphologyElement#getOperator()}.
     */
    @Override
    public SVGAnimatedEnumeration getOperator() {
        return operator;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEMorphologyElement#getRadiusX()}.
     */
    @Override
    public SVGAnimatedNumber getRadiusX() {
        throw new UnsupportedOperationException
            ("SVGFEMorphologyElement.getRadiusX is not implemented"); // XXX
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEMorphologyElement#getRadiusY()}.
     */
    @Override
    public SVGAnimatedNumber getRadiusY() {
        throw new UnsupportedOperationException
            ("SVGFEMorphologyElement.getRadiusY is not implemented"); // XXX
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGOMFEMorphologyElement();
    }

    /**
     * Returns the table of TraitInformation objects for this element.
     */
    @Override
    protected DoublyIndexedTable<String,String> getTraitInformationTable() {
        return xmlTraitInformation;
    }

    // AnimationTarget ///////////////////////////////////////////////////////

// XXX TBD
//     /**
//      * Updates an attribute value in this target.
//      */
//     public void updateAttributeValue(String ns, String ln,
//                                      AnimatableValue val) {
//         if (ns == null) {
//             if (ln.equals(SVG_RADIUS_ATTRIBUTE)) {
//                 // XXX Needs testing.
//                 if (val == null) {
//                     updateNumberAttributeValue(getRadiusX(), null);
//                     updateNumberAttributeValue(getRadiusY(), null);
//                 } else {
//                     AnimatableNumberOptionalNumberValue anonv =
//                         (AnimatableNumberOptionalNumberValue) val;
//                     SVGOMAnimatedNumber ai =
//                         (SVGOMAnimatedNumber) getRadiusX();
//                     ai.setAnimatedValue(anonv.getNumber());
//                     ai = (SVGOMAnimatedNumber) getRadiusY();
//                     if (anonv.hasOptionalNumber()) {
//                         ai.setAnimatedValue(anonv.getOptionalNumber());
//                     } else {
//                         ai.setAnimatedValue(anonv.getNumber());
//                     }
//                 }
//                 return;
//             }
//         }
//         super.updateAttributeValue(ns, ln, val);
//     }
// 
//     /**
//      * Returns the underlying value of an animatable XML attribute.
//      */
//     public AnimatableValue getUnderlyingValue(String ns, String ln) {
//         if (ns == null) {
//             if (ln.equals(SVG_RADIUS_ATTRIBUTE)) {
//                 return getBaseValue(getRadiusX(), getRadiusY());
//             }
//         }
//         return super.getUnderlyingValue(ns, ln);
//     }
}
