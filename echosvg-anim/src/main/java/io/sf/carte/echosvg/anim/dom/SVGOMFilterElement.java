/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedInteger;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFilterElement;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFilterElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFilterElement extends SVGStylableElement implements SVGFilterElement {

	private static final long serialVersionUID = 2L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGStylableElement.xmlTraitInformation);
		t.put(null, SVG_FILTER_UNITS_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_IDENT));
		t.put(null, SVG_PRIMITIVE_UNITS_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_IDENT));
		t.put(null, SVG_X_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_Y_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_WIDTH_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_HEIGHT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_FILTER_RES_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER_OPTIONAL_NUMBER));
		xmlTraitInformation = t;
	}

	/**
	 * The attribute initializer.
	 */
	protected static final AttributeInitializer attributeInitializer;
	static {
		attributeInitializer = new AttributeInitializer(4);
		attributeInitializer.addAttribute(XMLConstants.XMLNS_NAMESPACE_URI, null, "xmlns:xlink",
				XMLConstants.XLINK_NAMESPACE_URI);
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "type", "simple");
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "show", "other");
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "actuate", "onLoad");
	}

	/**
	 * The units values.
	 */
	protected static final String[] UNITS_VALUES = { "", SVG_USER_SPACE_ON_USE_VALUE, SVG_OBJECT_BOUNDING_BOX_VALUE };

	/**
	 * The 'filterUnits' attribute value.
	 */
	protected SVGOMAnimatedEnumeration filterUnits;

	/**
	 * The 'primitiveUnits' attribute value.
	 */
	protected SVGOMAnimatedEnumeration primitiveUnits;

	/**
	 * The 'x' attribute value.
	 */
	protected SVGOMAnimatedLength x;

	/**
	 * The 'y' attribute value.
	 */
	protected SVGOMAnimatedLength y;

	/**
	 * The 'width' attribute value.
	 */
	protected SVGOMAnimatedLength width;

	/**
	 * The 'height' attribute value.
	 */
	protected SVGOMAnimatedLength height;

	/**
	 * The namespaceless 'href' attribute value.
	 */
	private SVGOMAnimatedString href;

	/**
	 * The 'xlink:href' attribute value.
	 */
	private SVGOMAnimatedString xlinkhref;

	/**
	 * The 'externalResourcesRequired' attribute value.
	 */
	protected SVGOMAnimatedBoolean externalResourcesRequired;

	/**
	 * Creates a new SVGOMFilterElement object.
	 */
	protected SVGOMFilterElement() {
	}

	/**
	 * Creates a new SVGOMFilterElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMFilterElement(String prefix, AbstractDocument owner) {
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
		filterUnits = createLiveAnimatedEnumeration(null, SVG_FILTER_UNITS_ATTRIBUTE, UNITS_VALUES, (short) 2);
		primitiveUnits = createLiveAnimatedEnumeration(null, SVG_PRIMITIVE_UNITS_ATTRIBUTE, UNITS_VALUES, (short) 1);
		x = createLiveAnimatedLength(null, SVG_X_ATTRIBUTE, SVG_FILTER_X_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
		y = createLiveAnimatedLength(null, SVG_Y_ATTRIBUTE, SVG_FILTER_Y_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
		width = createLiveAnimatedLength(null, SVG_WIDTH_ATTRIBUTE, SVG_FILTER_WIDTH_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, true);
		height = createLiveAnimatedLength(null, SVG_HEIGHT_ATTRIBUTE, SVG_FILTER_HEIGHT_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, true);
		href = createLiveAnimatedString(null, XLINK_HREF_ATTRIBUTE);
		xlinkhref = createLiveAnimatedString(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE);
		externalResourcesRequired = createLiveAnimatedBoolean(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, false);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_FILTER_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getFilterUnits()}.
	 */
	@Override
	public SVGAnimatedEnumeration getFilterUnits() {
		return filterUnits;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getPrimitiveUnits()}.
	 */
	@Override
	public SVGAnimatedEnumeration getPrimitiveUnits() {
		return primitiveUnits;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getX()}.
	 */
	@Override
	public SVGAnimatedLength getX() {
		return x;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getY()}.
	 */
	@Override
	public SVGAnimatedLength getY() {
		return y;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getWidth()}.
	 */
	@Override
	public SVGAnimatedLength getWidth() {
		return width;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getHeight()}.
	 */
	@Override
	public SVGAnimatedLength getHeight() {
		return height;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getFilterResX()}.
	 */
	@Override
	public SVGAnimatedInteger getFilterResX() {
		throw new UnsupportedOperationException("SVGFilterElement.getFilterResX is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#getFilterResY()}.
	 */
	@Override
	public SVGAnimatedInteger getFilterResY() {
		throw new UnsupportedOperationException("SVGFilterElement.getFilterResY is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFilterElement#setFilterRes(int,int)}.
	 */
	@Override
	public void setFilterRes(int filterResX, int filterResY) {
		throw new UnsupportedOperationException("SVGFilterElement.setFilterRes is not implemented"); // XXX
	}

	// SVGURIReference support /////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGURIReference#getHref()}.
	 */
	@Override
	public SVGAnimatedString getHref() {
		return href.element.hasAttribute(XLINK_HREF_ATTRIBUTE) ? href : xlinkhref;
	}

	// SVGExternalResourcesRequired support /////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGExternalResourcesRequired#getExternalResourcesRequired()}.
	 */
	@Override
	public SVGAnimatedBoolean getExternalResourcesRequired() {
		return externalResourcesRequired;
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

	// SVGLangSpace support //////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Returns the xml:lang attribute value.
	 */
	@Override
	public String getXMLlang() {
		return XMLSupport.getXMLLang(this);
	}

	/**
	 * <b>DOM</b>: Sets the xml:lang attribute value.
	 */
	@Override
	public void setXMLlang(String lang) {
		setAttributeNS(XML_NAMESPACE_URI, XML_LANG_QNAME, lang);
	}

	/**
	 * <b>DOM</b>: Returns the xml:space attribute value.
	 */
	@Override
	public String getXMLspace() {
		return XMLSupport.getXMLSpace(this);
	}

	/**
	 * <b>DOM</b>: Sets the xml:space attribute value.
	 */
	@Override
	public void setXMLspace(String space) {
		setAttributeNS(XML_NAMESPACE_URI, XML_SPACE_QNAME, space);
	}

	/**
	 * Returns the AttributeInitializer for this element type.
	 * 
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
		return new SVGOMFilterElement();
	}

	// AnimationTarget ///////////////////////////////////////////////////////

// XXX TBD
//     /**
//      * Updates an attribute value in this target.
//      */
//     public void updateAttributeValue(String ns, String ln,
//                                      AnimatableValue val) {
//         if (ns == null) {
//             if (ln.equals(SVG_FILTER_RES_ATTRIBUTE)) {
//                 // XXX Needs testing.
//                 if (val == null) {
//                     updateIntegerAttributeValue(getFilterResX(), null);
//                     updateIntegerAttributeValue(getFilterResY(), null);
//                 } else {
//                     AnimatableNumberOptionalNumberValue anonv =
//                         (AnimatableNumberOptionalNumberValue) val;
//                     SVGOMAnimatedInteger ai =
//                         (SVGOMAnimatedInteger) getFilterResX();
//                     ai.setAnimatedValue(Math.round(anonv.getNumber()));
//                     ai = (SVGOMAnimatedInteger) getFilterResY();
//                     if (anonv.hasOptionalNumber()) {
//                         ai.setAnimatedValue
//                             (Math.round(anonv.getOptionalNumber()));
//                     } else {
//                         ai.setAnimatedValue(Math.round(anonv.getNumber()));
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
//             if (ln.equals(SVG_FILTER_RES_ATTRIBUTE)) {
//                 return getBaseValue(getFilterResX(), getFilterResY());
//             }
//         }
//         return super.getUnderlyingValue(ns, ln);
//     }
}
