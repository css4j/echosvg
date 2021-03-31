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
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGSymbolElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.svg.SVGZoomAndPanSupport;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGSymbolElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMSymbolElement extends SVGStylableElement implements SVGSymbolElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGStylableElement.xmlTraitInformation);
		t.put(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_BOOLEAN));
		t.put(null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE,
				new TraitInformation(true, SVGTypes.TYPE_PRESERVE_ASPECT_RATIO_VALUE));
		t.put(null, SVG_VIEW_BOX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER_LIST));
		xmlTraitInformation = t;
	}

	/**
	 * The 'preserveAspectRatio' attribute value.
	 */
	protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;

	/**
	 * The attribute initializer.
	 */
	protected static final AttributeInitializer attributeInitializer;
	static {
		attributeInitializer = new AttributeInitializer(1);
		attributeInitializer.addAttribute(null, null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE, "xMidYMid meet");
	}

	/**
	 * The 'externalResourcesRequired' attribute value.
	 */
	protected SVGOMAnimatedBoolean externalResourcesRequired;

	/**
	 * Creates a new SVGOMSymbolElement object.
	 */
	protected SVGOMSymbolElement() {
	}

	/**
	 * Creates a new SVGOMSymbolElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMSymbolElement(String prefix, AbstractDocument owner) {
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
		preserveAspectRatio = createLiveAnimatedPreserveAspectRatio();
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_SYMBOL_TAG;
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

	// SVGZoomAndPan support ///////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGZoomAndPan#getZoomAndPan()}.
	 */
	public short getZoomAndPan() {
		return SVGZoomAndPanSupport.getZoomAndPan(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGZoomAndPan#getZoomAndPan()}.
	 */
	public void setZoomAndPan(short val) {
		SVGZoomAndPanSupport.setZoomAndPan(this, val);
	}

	// SVGFitToViewBox support ////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGFitToViewBox#getViewBox()}.
	 */
	@Override
	public SVGAnimatedRect getViewBox() {
		throw new UnsupportedOperationException("SVGFitToViewBox.getViewBox is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFitToViewBox#getPreserveAspectRatio()}.
	 */
	@Override
	public SVGAnimatedPreserveAspectRatio getPreserveAspectRatio() {
		return preserveAspectRatio;
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
		return new SVGOMSymbolElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
