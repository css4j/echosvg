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
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGAnimatedTransformList;
import org.w3c.dom.svg.SVGGradientElement;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGGradientElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGOMGradientElement extends SVGStylableElement implements SVGGradientElement {

	private static final long serialVersionUID = 2L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGStylableElement.xmlTraitInformation);
		t.put(null, SVG_GRADIENT_UNITS_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_IDENT));
		t.put(null, SVG_SPREAD_METHOD_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_IDENT));
		t.put(null, SVG_GRADIENT_TRANSFORM_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_TRANSFORM_LIST));
		t.put(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_BOOLEAN));
		t.put(null, XLINK_HREF_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_URI));
		t.put(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_URI));
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
	 * The 'spreadMethod' attribute values.
	 */
	protected static final String[] SPREAD_METHOD_VALUES = { "", SVG_PAD_VALUE, SVG_REFLECT_VALUE, SVG_REPEAT_VALUE };

	/**
	 * The 'gradientUnits' attribute value.
	 */
	protected SVGOMAnimatedEnumeration gradientUnits;

	/**
	 * The 'spreadMethod' attribute value.
	 */
	protected SVGOMAnimatedEnumeration spreadMethod;

	/**
	 * The namespaceless 'href' attribute value.
	 */
	private SVGOMAnimatedString href;

	/**
	 * The 'xlink:href' attribute value. Note that this attribute not actually
	 * animatable, according to SVG 1.1.
	 */
	private SVGOMAnimatedString xlinkhref;

	/**
	 * The 'externalResourcesRequired' attribute value.
	 */
	protected SVGOMAnimatedBoolean externalResourcesRequired;

	/**
	 * Creates a new SVGOMGradientElement object.
	 */
	protected SVGOMGradientElement() {
	}

	/**
	 * Creates a new SVGOMGradientElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected SVGOMGradientElement(String prefix, AbstractDocument owner) {
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
		gradientUnits = createLiveAnimatedEnumeration(null, SVG_GRADIENT_UNITS_ATTRIBUTE, UNITS_VALUES, (short) 2);
		spreadMethod = createLiveAnimatedEnumeration(null, SVG_SPREAD_METHOD_ATTRIBUTE, SPREAD_METHOD_VALUES,
				(short) 1);
		href = createLiveAnimatedString(null, XLINK_HREF_ATTRIBUTE);
		xlinkhref = createLiveAnimatedString(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE);
		externalResourcesRequired = createLiveAnimatedBoolean(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, false);
	}

	/**
	 * To implement
	 * {@link org.w3c.dom.svg.SVGGradientElement#getGradientTransform()}.
	 */
	@Override
	public SVGAnimatedTransformList getGradientTransform() {
		throw new UnsupportedOperationException("SVGGradientElement.getGradientTransform is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGGradientElement#getGradientUnits()}.
	 */
	@Override
	public SVGAnimatedEnumeration getGradientUnits() {
		return gradientUnits;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGGradientElement#getSpreadMethod()}.
	 */
	@Override
	public SVGAnimatedEnumeration getSpreadMethod() {
		return spreadMethod;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGURIReference#getHref()}.
	 */
	@Override
	public SVGAnimatedString getHref() {
		return hasAttribute(XLINK_HREF_ATTRIBUTE) ? href : xlinkhref;
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
		return new SVGOMAElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
