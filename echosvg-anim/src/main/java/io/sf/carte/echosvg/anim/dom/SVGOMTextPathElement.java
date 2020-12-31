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
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGTextPathElement;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.util.XLinkSupport;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGTextPathElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMTextPathElement
    extends    SVGOMTextContentElement
    implements SVGTextPathElement {

    /**
     * Table mapping XML attribute names to TraitInformation objects.
     */
    protected static DoublyIndexedTable xmlTraitInformation;
    static {
        DoublyIndexedTable t =
            new DoublyIndexedTable(SVGOMTextContentElement.xmlTraitInformation);
        t.put(null, SVG_METHOD_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_IDENT));
        t.put(null, SVG_SPACING_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_IDENT));
        t.put(null, SVG_START_OFFSET_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_LENGTH));
        t.put(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE,
                new TraitInformation(true, SVGTypes.TYPE_URI));
        xmlTraitInformation = t;
    }

    /**
     * The attribute initializer.
     */
    protected static final AttributeInitializer attributeInitializer;
    static {
        attributeInitializer = new AttributeInitializer(4);
        attributeInitializer.addAttribute(XMLConstants.XMLNS_NAMESPACE_URI,
                                          null, "xmlns:xlink",
                                          XMLConstants.XLINK_NAMESPACE_URI);
        attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI,
                                          "xlink", "type", "simple");
        attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI,
                                          "xlink", "show", "other");
        attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI,
                                          "xlink", "actuate", "onLoad");
    }

    /**
     * The 'method' attribute values.
     */
    protected static final String[] METHOD_VALUES = {
        "",
        SVG_ALIGN_VALUE,
        SVG_STRETCH_VALUE
    };

    /**
     * The 'spacing' attribute values.
     */
    protected static final String[] SPACING_VALUES = {
        "",
        SVG_AUTO_VALUE,
        SVG_EXACT_VALUE
    };

    /**
     * The 'method' attribute value.
     */
    protected SVGOMAnimatedEnumeration method;

    /**
     * The 'spacing' attribute value.
     */
    protected SVGOMAnimatedEnumeration spacing;

    /**
     * The 'startOffset' attribute value.
     */
    protected SVGOMAnimatedLength startOffset;

    /**
     * The 'xlink:href' attribute value.
     */
    protected SVGOMAnimatedString href;

    /**
     * Creates a new SVGOMTextPathElement object.
     */
    protected SVGOMTextPathElement() {
    }

    /**
     * Creates a new SVGOMTextPathElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMTextPathElement(String prefix, AbstractDocument owner) {
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
        method =
            createLiveAnimatedEnumeration
                (null, SVG_METHOD_ATTRIBUTE, METHOD_VALUES, (short) 1);
        spacing =
            createLiveAnimatedEnumeration
                (null, SVG_SPACING_ATTRIBUTE, SPACING_VALUES, (short) 2);
        startOffset =
            createLiveAnimatedLength
                (null, SVG_START_OFFSET_ATTRIBUTE,
                 SVG_TEXT_PATH_START_OFFSET_DEFAULT_VALUE,
                 AbstractSVGAnimatedLength.OTHER_LENGTH, false);
        href =
            createLiveAnimatedString(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE);
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return SVG_TEXT_PATH_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPathElement#getStartOffset()}.
     */
    @Override
    public SVGAnimatedLength getStartOffset() {
        return startOffset;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPathElement#getMethod()}.
     */
    @Override
    public SVGAnimatedEnumeration getMethod() {
        return method;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPathElement#getSpacing()}.
     */
    @Override
    public SVGAnimatedEnumeration getSpacing() {
        return spacing;
    }

    // XLink support //////////////////////////////////////////////////////

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGURIReference#getHref()}.
     */
    @Override
    public SVGAnimatedString getHref() {
        return href;
    }

    /**
     * Returns the AttributeInitializer for this element type.
     * @return null if this element has no attribute with a default value.
     */
    @Override
    protected AttributeInitializer getAttributeInitializer() {
        return attributeInitializer;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGOMTextPathElement();
    }

    /**
     * Returns the table of TraitInformation objects for this element.
     */
    @Override
    protected DoublyIndexedTable getTraitInformationTable() {
        return xmlTraitInformation;
    }
}
