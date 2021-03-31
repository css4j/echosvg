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
import org.w3c.dom.svg.SVGEllipseElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGEllipseElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMEllipseElement extends SVGGraphicsElement implements SVGEllipseElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGGraphicsElement.xmlTraitInformation);
		t.put(null, SVG_CX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_CY_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_RX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_RY_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		xmlTraitInformation = t;
	}

	/**
	 * The 'cx' attribute value.
	 */
	protected SVGOMAnimatedLength cx;

	/**
	 * The 'cy' attribute value.
	 */
	protected SVGOMAnimatedLength cy;

	/**
	 * The 'rx' attribute value.
	 */
	protected SVGOMAnimatedLength rx;

	/**
	 * The 'ry' attribute value.
	 */
	protected SVGOMAnimatedLength ry;

	/**
	 * Creates a new SVGOMEllipseElement object.
	 */
	protected SVGOMEllipseElement() {
	}

	/**
	 * Creates a new SVGOMEllipseElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMEllipseElement(String prefix, AbstractDocument owner) {
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
		cx = createLiveAnimatedLength(null, SVG_CX_ATTRIBUTE, SVG_ELLIPSE_CX_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
		cy = createLiveAnimatedLength(null, SVG_CY_ATTRIBUTE, SVG_ELLIPSE_CY_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
		rx = createLiveAnimatedLength(null, SVG_RX_ATTRIBUTE, null, AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, true);
		ry = createLiveAnimatedLength(null, SVG_RY_ATTRIBUTE, null, AbstractSVGAnimatedLength.VERTICAL_LENGTH, true);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_ELLIPSE_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGEllipseElement#getCx()}.
	 */
	@Override
	public SVGAnimatedLength getCx() {
		return cx;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGEllipseElement#getCy()}.
	 */
	@Override
	public SVGAnimatedLength getCy() {
		return cy;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGEllipseElement#getRx()}.
	 */
	@Override
	public SVGAnimatedLength getRx() {
		return rx;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGEllipseElement#getRy()}.
	 */
	@Override
	public SVGAnimatedLength getRy() {
		return ry;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMEllipseElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
