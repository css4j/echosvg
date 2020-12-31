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
import org.w3c.dom.svg.SVGAnimateColorElement;

import io.sf.carte.echosvg.dom.AbstractDocument;

/**
 * This class implements {@link SVGAnimateColorElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMAnimateColorElement
    extends    SVGOMAnimationElement
    implements SVGAnimateColorElement {

//     /**
//      * Table mapping XML attribute names to TraitInformation objects.
//      */
//     protected static DoublyIndexedTable xmlTraitInformation;
//     static {
//         DoublyIndexedTable t =
//             new DoublyIndexedTable(SVGOMAnimationElement.xmlTraitInformation);
//         t.put(null, SVG_ACCUMULATE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_ADDITIVE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_ATTRIBUTE_TYPE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_CALC_MODE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_FILL_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_RESTART_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_ATTRIBUTE_NAME_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_BY_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_FROM_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_MAX_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_MIN_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_TO_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_VALUES_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_BEGIN_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_TIMING_SPECIFIER_LIST));
//         t.put(null, SVG_END_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_TIMING_SPECIFIER_LIST));
//         t.put(null, SVG_DUR_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_TIME));
//         t.put(null, SVG_REPEAT_DUR_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_TIME));
//         t.put(null, SVG_KEY_SPLINES_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_NUMBER_LIST));
//         t.put(null, SVG_KEY_TIMES_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_NUMBER_LIST));
//         t.put(null, SVG_REPEAT_COUNT_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_INTEGER));
//         xmlTraitInformation = t;
//     }

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new SVGOMAnimateColorElement object.
     */
    protected SVGOMAnimateColorElement() {
    }

    /**
     * Creates a new SVGOMAnimateColorElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMAnimateColorElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return SVG_ANIMATE_COLOR_TAG;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGOMAnimateColorElement();
    }

//     /**
//      * Returns the table of TraitInformation objects for this element.
//      */
//     protected DoublyIndexedTable getTraitInformationTable() {
//         return xmlTraitInformation;
//     }
}
