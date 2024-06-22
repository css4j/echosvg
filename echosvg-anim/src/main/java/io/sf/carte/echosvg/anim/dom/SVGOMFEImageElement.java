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
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEImageElement;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFEImageElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFEImageElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFEImageElement {

	private static final long serialVersionUID = 2L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(
				SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
		t.put(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_BOOLEAN));
		t.put(null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE,
				new TraitInformation(true, SVGTypes.TYPE_PRESERVE_ASPECT_RATIO_VALUE));
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
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "show", "embed");
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "actuate", "onLoad");
	}

	/**
	 * The namespaceless 'href' attribute value.
	 */
	private SVGOMAnimatedString href;

	/**
	 * The 'xlink:href' attribute value.
	 */
	private SVGOMAnimatedString xlinkhref;

	/**
	 * The 'preserveAspectRatio' attribute value.
	 */
	protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;

	/**
	 * The 'externalResourcesRequired' attribute value.
	 */
	protected SVGOMAnimatedBoolean externalResourcesRequired;

	/**
	 * Creates a new SVGOMFEImageElement object.
	 */
	protected SVGOMFEImageElement() {
	}

	/**
	 * Creates a new SVGOMFEImageElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMFEImageElement(String prefix, AbstractDocument owner) {
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
		href = createLiveAnimatedString(null, XLINK_HREF_ATTRIBUTE);
		xlinkhref = createLiveAnimatedString(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE);
		preserveAspectRatio = createLiveAnimatedPreserveAspectRatio();
		externalResourcesRequired = createLiveAnimatedBoolean(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, false);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_FE_IMAGE_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGURIReference#getHref()}.
	 */
	@Override
	public SVGAnimatedString getHref() {
		return href.element.hasAttribute(XLINK_HREF_ATTRIBUTE) ? href : xlinkhref;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEImageElement#getPreserveAspectRatio()}.
	 */
	@Override
	public SVGAnimatedPreserveAspectRatio getPreserveAspectRatio() {
		return preserveAspectRatio;
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
		return new SVGOMFEImageElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
