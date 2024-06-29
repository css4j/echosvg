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
import org.w3c.dom.svg.SVGRadialGradientElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGRadialGradientElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMRadialGradientElement extends SVGOMGradientElement implements SVGRadialGradientElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGOMGradientElement.xmlTraitInformation);
		t.put(null, SVG_CX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_CY_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_FX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_FY_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_R_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_SIZE));
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
	 * The 'fx' attribute value.
	 */
	protected AbstractSVGAnimatedLength fx;

	/**
	 * The 'fy' attribute value.
	 */
	protected AbstractSVGAnimatedLength fy;

	/**
	 * The 'r' attribute value.
	 */
	protected SVGOMAnimatedLength r;

	/**
	 * Creates a new SVGOMRadialGradientElement object.
	 */
	protected SVGOMRadialGradientElement() {
	}

	/**
	 * Creates a new SVGOMRadialGradientElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMRadialGradientElement(String prefix, AbstractDocument owner) {
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
		cx = createLiveAnimatedLength(null, SVG_CX_ATTRIBUTE, SVG_RADIAL_GRADIENT_CX_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
		cy = createLiveAnimatedLength(null, SVG_CY_ATTRIBUTE, SVG_RADIAL_GRADIENT_CY_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
		r = createLiveAnimatedLength(null, SVG_R_ATTRIBUTE, SVG_RADIAL_GRADIENT_R_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.OTHER_LENGTH, false);
		fx = new AbstractSVGAnimatedLength(this, null, SVG_FX_ATTRIBUTE, AbstractSVGAnimatedLength.HORIZONTAL_LENGTH,
				false) {
			@Override
			protected String getDefaultValue() {
				String defval = getAttribute(SVG_CX_ATTRIBUTE).trim();
				if (defval.isEmpty()) {
					defval = SVG_RADIAL_GRADIENT_CX_DEFAULT_VALUE;
				}
				return defval;
			}
		};
		fy = new AbstractSVGAnimatedLength(this, null, SVG_FY_ATTRIBUTE, AbstractSVGAnimatedLength.VERTICAL_LENGTH,
				false) {
			@Override
			protected String getDefaultValue() {
				String defval = getAttribute(SVG_CY_ATTRIBUTE).trim();
				if (defval.isEmpty()) {
					defval = SVG_RADIAL_GRADIENT_CY_DEFAULT_VALUE;
				}
				return defval;
			}
		};

		liveAttributeValues.put(null, SVG_FX_ATTRIBUTE, fx);
		liveAttributeValues.put(null, SVG_FY_ATTRIBUTE, fy);
		AnimatedAttributeListener l = ((SVGOMDocument) ownerDocument).getAnimatedAttributeListener();
		fx.addAnimatedAttributeListener(l);
		fy.addAnimatedAttributeListener(l);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_RADIAL_GRADIENT_TAG;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGRadialGradientElement#getCx()}.
	 */
	@Override
	public SVGAnimatedLength getCx() {
		return cx;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGRadialGradientElement#getCy()}.
	 */
	@Override
	public SVGAnimatedLength getCy() {
		return cy;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGRadialGradientElement#getR()}.
	 */
	@Override
	public SVGAnimatedLength getR() {
		return r;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGRadialGradientElement#getFx()}.
	 */
	@Override
	public SVGAnimatedLength getFx() {
		return fx;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGRadialGradientElement#getFy()}.
	 */
	@Override
	public SVGAnimatedLength getFy() {
		return fy;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMRadialGradientElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

}
