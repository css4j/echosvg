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
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEDiffuseLightingElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFEDiffuseLightingElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFEDiffuseLightingElement extends SVGOMFilterPrimitiveStandardAttributes
		implements SVGFEDiffuseLightingElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(
				SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
		t.put(null, SVG_IN_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_CDATA));
		t.put(null, SVG_SURFACE_SCALE_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		t.put(null, SVG_DIFFUSE_CONSTANT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		t.put(null, SVG_KERNEL_UNIT_LENGTH_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER_OPTIONAL_NUMBER));
		xmlTraitInformation = t;
	}

	/**
	 * The 'in' attribute value.
	 */
	protected SVGOMAnimatedString in;

	/**
	 * The 'surfaceScale' attribute value.
	 */
	protected SVGOMAnimatedNumber surfaceScale;

	/**
	 * The 'diffuseConstant' attribute value.
	 */
	protected SVGOMAnimatedNumber diffuseConstant;

	/**
	 * Creates a new SVGOMFEDiffuseLightingElement object.
	 */
	protected SVGOMFEDiffuseLightingElement() {
	}

	/**
	 * Creates a new SVGOMFEDiffuseLightingElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMFEDiffuseLightingElement(String prefix, AbstractDocument owner) {
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
		in = createLiveAnimatedString(null, SVG_IN_ATTRIBUTE);
		surfaceScale = createLiveAnimatedNumber(null, SVG_SURFACE_SCALE_ATTRIBUTE, 1f);
		diffuseConstant = createLiveAnimatedNumber(null, SVG_DIFFUSE_CONSTANT_ATTRIBUTE, 1f);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_FE_DIFFUSE_LIGHTING_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEDiffuseLightingElement#getIn1()}.
	 */
	@Override
	public SVGAnimatedString getIn1() {
		return in;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEDiffuseLightingElement#getSurfaceScale()}.
	 */
	@Override
	public SVGAnimatedNumber getSurfaceScale() {
		return surfaceScale;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGFEDiffuseLightingElement#getDiffuseConstant()}.
	 */
	@Override
	public SVGAnimatedNumber getDiffuseConstant() {
		return diffuseConstant;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFEDiffuseLightingElement#getKernelUnitLengthX()}.
	 */
	@Override
	public SVGAnimatedNumber getKernelUnitLengthX() {
		throw new UnsupportedOperationException("SVGFEDiffuseLightingElement.getKernelUnitLengthX is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFEDiffuseLightingElement#getKernelUnitLengthY()}.
	 */
	@Override
	public SVGAnimatedNumber getKernelUnitLengthY() {
		throw new UnsupportedOperationException("SVGFEDiffuseLightingElement.getKernelUnitLengthY is not implemented"); // XXX
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMFEDiffuseLightingElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

	// AnimationTarget ///////////////////////////////////////////////////////

// XXX TBD
//
//     /**
//      * Updates an attribute value in this target.
//      */
//     public void updateAttributeValue(String ns, String ln,
//                                      AnimatableValue val) {
//         if (ns == null) {
//             if (ln.equals(SVG_KERNEL_UNIT_LENGTH_ATTRIBUTE)) {
//                 // XXX Needs testing.
//                 if (val == null) {
//                     updateNumberAttributeValue(getKernelUnitLengthX(), null);
//                     updateNumberAttributeValue(getKernelUnitLengthY(), null);
//                 } else {
//                     AnimatableNumberOptionalNumberValue anonv =
//                         (AnimatableNumberOptionalNumberValue) val;
//                     SVGOMAnimatedNumber an =
//                         (SVGOMAnimatedNumber) getKernelUnitLengthX();
//                     an.setAnimatedValue(anonv.getNumber());
//                     an = (SVGOMAnimatedNumber) getKernelUnitLengthY();
//                     if (anonv.hasOptionalNumber()) {
//                         an.setAnimatedValue(anonv.getOptionalNumber());
//                     } else {
//                         an.setAnimatedValue(anonv.getNumber());
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
//             if (ln.equals(SVG_KERNEL_UNIT_LENGTH_ATTRIBUTE)) {
//                 return getBaseValue(getKernelUnitLengthX(),
//                                     getKernelUnitLengthY());
//             }
//         }
//         return super.getUnderlyingValue(ns, ln);
//     }
}
