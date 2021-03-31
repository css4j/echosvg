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

import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGComponentTransferFunctionElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class represents the component transfer function elements.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGOMComponentTransferFunctionElement extends SVGOMElement
		implements SVGComponentTransferFunctionElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGOMElement.xmlTraitInformation);
		t.put(null, SVG_TYPE_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_IDENT));
		t.put(null, SVG_TABLE_VALUES_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER_LIST));
		t.put(null, SVG_SLOPE_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		t.put(null, SVG_INTERCEPT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		t.put(null, SVG_AMPLITUDE_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		t.put(null, SVG_EXPONENT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		t.put(null, SVG_OFFSET_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		xmlTraitInformation = t;
	}

	/**
	 * The 'type' attribute values.
	 */
	protected static final String[] TYPE_VALUES = { "", SVG_IDENTITY_VALUE, SVG_TABLE_VALUE, SVG_DISCRETE_VALUE,
			SVG_LINEAR_VALUE, SVG_GAMMA_VALUE };

	/**
	 * The 'type' attribute value.
	 */
	protected SVGOMAnimatedEnumeration type;

	/**
	 * The 'tableValues' attribute value.
	 */
	protected SVGOMAnimatedNumberList tableValues;

	/**
	 * The 'slope' attribute value.
	 */
	protected SVGOMAnimatedNumber slope;

	/**
	 * The 'intercept' attribute value.
	 */
	protected SVGOMAnimatedNumber intercept;

	/**
	 * The 'amplitude' attribute value.
	 */
	protected SVGOMAnimatedNumber amplitude;

	/**
	 * The 'exponent' attribute value.
	 */
	protected SVGOMAnimatedNumber exponent;

	/**
	 * The 'offset' attribute value.
	 */
	protected SVGOMAnimatedNumber offset;

	/**
	 * Creates a new Element object.
	 */
	protected SVGOMComponentTransferFunctionElement() {
	}

	/**
	 * Creates a new Element object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected SVGOMComponentTransferFunctionElement(String prefix, AbstractDocument owner) {
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
		type = createLiveAnimatedEnumeration(null, SVG_TYPE_ATTRIBUTE, TYPE_VALUES, (short) 1);
		tableValues = createLiveAnimatedNumberList(null, SVG_TABLE_VALUES_ATTRIBUTE,
				SVG_COMPONENT_TRANSFER_FUNCTION_TABLE_VALUES_DEFAULT_VALUE, false);
		slope = createLiveAnimatedNumber(null, SVG_SLOPE_ATTRIBUTE, 1f);
		intercept = createLiveAnimatedNumber(null, SVG_INTERCEPT_ATTRIBUTE, 0f);
		amplitude = createLiveAnimatedNumber(null, SVG_AMPLITUDE_ATTRIBUTE, 1f);
		exponent = createLiveAnimatedNumber(null, SVG_EXPONENT_ATTRIBUTE, 1f);
		offset = createLiveAnimatedNumber(null, SVG_EXPONENT_ATTRIBUTE, 0f);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGComponentTransferFunctionElement#getType()}.
	 */
	@Override
	public SVGAnimatedEnumeration getType() {
		return type;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGComponentTransferFunctionElement#getTableValues()}.
	 */
	@Override
	public SVGAnimatedNumberList getTableValues() {
		// XXX
		throw new UnsupportedOperationException(
				"SVGComponentTransferFunctionElement.getTableValues is not implemented");
		// return tableValues;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGComponentTransferFunctionElement#getSlope()}.
	 */
	@Override
	public SVGAnimatedNumber getSlope() {
		return slope;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGComponentTransferFunctionElement#getIntercept()}.
	 */
	@Override
	public SVGAnimatedNumber getIntercept() {
		return intercept;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGComponentTransferFunctionElement#getAmplitude()}.
	 */
	@Override
	public SVGAnimatedNumber getAmplitude() {
		return amplitude;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGComponentTransferFunctionElement#getExponent()}.
	 */
	@Override
	public SVGAnimatedNumber getExponent() {
		return exponent;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGComponentTransferFunctionElement#getOffset()}.
	 */
	@Override
	public SVGAnimatedNumber getOffset() {
		return offset;
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
