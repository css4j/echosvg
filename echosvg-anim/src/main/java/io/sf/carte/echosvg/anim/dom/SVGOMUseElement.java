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
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGElementInstance;
import org.w3c.dom.svg.SVGUseElement;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.svg.SVGOMUseShadowRoot;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGUseElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMUseElement extends SVGURIReferenceGraphicsElement implements SVGUseElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(
				SVGURIReferenceGraphicsElement.xmlTraitInformation);
		t.put(null, SVG_X_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_Y_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_WIDTH_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_HEIGHT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
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
	 * Store the shadow tree of the use element.
	 */
	protected SVGOMUseShadowRoot shadowTree;

	/**
	 * Creates a new SVGOMUseElement object.
	 */
	protected SVGOMUseElement() {
	}

	/**
	 * Creates a new SVGOMUseElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMUseElement(String prefix, AbstractDocument owner) {
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
		x = createLiveAnimatedLength(null, SVG_X_ATTRIBUTE, SVG_USE_X_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
		y = createLiveAnimatedLength(null, SVG_Y_ATTRIBUTE, SVG_USE_Y_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
		width = createLiveAnimatedLength(null, SVG_WIDTH_ATTRIBUTE, null, AbstractSVGAnimatedLength.HORIZONTAL_LENGTH,
				true);
		height = createLiveAnimatedLength(null, SVG_HEIGHT_ATTRIBUTE, null, AbstractSVGAnimatedLength.VERTICAL_LENGTH,
				true);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_USE_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGUseElement#getX()}.
	 */
	@Override
	public SVGAnimatedLength getX() {
		return x;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGUseElement#getY()}.
	 */
	@Override
	public SVGAnimatedLength getY() {
		return y;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGUseElement#getWidth()}.
	 */
	@Override
	public SVGAnimatedLength getWidth() {
		return width;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGUseElement#getHeight()}.
	 */
	@Override
	public SVGAnimatedLength getHeight() {
		return height;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGUseElement#getInstanceRoot()}.
	 */
	@Override
	public SVGElementInstance getInstanceRoot() {
		throw new UnsupportedOperationException("SVGUseElement.getInstanceRoot is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGUseElement#getAnimatedInstanceRoot()}.
	 */
	@Override
	public SVGElementInstance getAnimatedInstanceRoot() {
		throw new UnsupportedOperationException("SVGUseElement.getAnimatedInstanceRoot is not implemented"); // XXX
	}

	// CSSNavigableNode ///////////////////////////////////////////////

	/**
	 * Returns the CSS first child node of this node.
	 */
	@Override
	public Node getCSSFirstChild() {
		if (shadowTree != null) {
			return shadowTree.getFirstChild();
		}
		return null;
	}

	/**
	 * Returns the CSS last child of this stylable element.
	 */
	@Override
	public Node getCSSLastChild() {
		// use element shadow trees only ever have a single element
		return getCSSFirstChild();
	}

	/**
	 * Returns whether this node is the root of a (conceptual) hidden tree that
	 * selectors will not work across. Returns true here, since CSS selectors cannot
	 * work in the conceptual cloned sub-tree of the content referenced by the 'use'
	 * element.
	 */
	@Override
	public boolean isHiddenFromSelectors() {
		return true;
	}

	/**
	 * Sets the shadow tree for this 'use' element.
	 */
	public void setUseShadowTree(SVGOMUseShadowRoot r) {
		shadowTree = r;
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
		return new SVGOMUseElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

}
