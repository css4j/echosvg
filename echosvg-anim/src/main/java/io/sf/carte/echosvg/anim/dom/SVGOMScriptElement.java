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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGScriptElement;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGScriptElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMScriptElement extends SVGOMURIReferenceElement implements SVGScriptElement {

	private static final long serialVersionUID = 1L;
	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGOMURIReferenceElement.xmlTraitInformation);
		t.put(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_BOOLEAN));
//         t.put(null, SVG_TYPE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
		xmlTraitInformation = t;
	}

	/**
	 * The attribute initializer.
	 */
	protected static final AttributeInitializer attributeInitializer;
	static {
		attributeInitializer = new AttributeInitializer(1);
		attributeInitializer.addAttribute(XMLConstants.XMLNS_NAMESPACE_URI, null, "xmlns:xlink",
				XMLConstants.XLINK_NAMESPACE_URI);
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "type", "simple");
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "show", "other");
		attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI, "xlink", "actuate", "onLoad");
	}

	/**
	 * The 'externalResourcesRequired' attribute value.
	 */
	protected SVGOMAnimatedBoolean externalResourcesRequired;

	/**
	 * Creates a new SVGOMScriptElement object.
	 */
	protected SVGOMScriptElement() {
	}

	/**
	 * Creates a new SVGOMScriptElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMScriptElement(String prefix, AbstractDocument owner) {
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
		externalResourcesRequired = createLiveAnimatedBoolean(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, false);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_SCRIPT_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGScriptElement#getType()}.
	 */
	@Override
	public String getType() {
		return getAttributeNS(null, SVG_TYPE_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGScriptElement#setType(String)}.
	 */
	@Override
	public void setType(String type) throws DOMException {
		setAttributeNS(null, SVG_TYPE_ATTRIBUTE, type);
	}

	// SVGExternalResourcesRequired support /////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGExternalResourcesRequired}.
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
		return new SVGOMScriptElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
