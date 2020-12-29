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

import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFontFaceNameElement;

/**
 * This class implements {@link SVGFontFaceNameElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMFontFaceNameElement
    extends    SVGOMElement
    implements SVGFontFaceNameElement {

//     /**
//      * Table mapping XML attribute names to TraitInformation objects.
//      */
//     protected static DoublyIndexedTable xmlTraitInformation;
//     static {
//         DoublyIndexedTable t =
//             new DoublyIndexedTable(SVGOMElement.xmlTraitInformation);
//         t.put(null, SVG_NAME_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         xmlTraitInformation = t;
//     }

    /**
     * Creates a new SVGOMFontFaceNameElement object.
     */
    protected SVGOMFontFaceNameElement() {
    }

    /**
     * Creates a new SVGOMFontFaceNameElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFontFaceNameElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_FONT_FACE_NAME_TAG;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMFontFaceNameElement();
    }

//     /**
//      * Returns the table of TraitInformation objects for this element.
//      */
//     protected DoublyIndexedTable getTraitInformationTable() {
//         return xmlTraitInformation;
//     }
}
