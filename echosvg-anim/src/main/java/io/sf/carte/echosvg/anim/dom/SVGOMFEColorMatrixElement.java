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
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEColorMatrixElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFEColorMatrixElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFEColorMatrixElement extends SVGOMFilterPrimitiveStandardAttributes
		implements SVGFEColorMatrixElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(
				SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
		t.put(null, SVG_IN_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_CDATA));
		t.put(null, SVG_TYPE_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_IDENT));
		t.put(null, SVG_VALUES_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER_LIST));
		xmlTraitInformation = t;
	}

	/**
	 * The 'type' attribute values.
	 */
	protected static final String[] TYPE_VALUES = { "", SVG_MATRIX_VALUE, SVG_SATURATE_VALUE, SVG_HUE_ROTATE_VALUE,
			SVG_LUMINANCE_TO_ALPHA_VALUE };

	/**
	 * The 'in' attribute value.
	 */
	protected SVGOMAnimatedString in;

	/**
	 * The 'type' attribute value.
	 */
	protected SVGOMAnimatedEnumeration type;

//     /**
//      * The 'values' attribute value.
//      */
//     protected SVGOMAnimatedNumberList values;

	/**
	 * Creates a new SVGOMFEColorMatrixElement object.
	 */
	protected SVGOMFEColorMatrixElement() {
	}

	/**
	 * Creates a new SVGOMFEColorMatrixElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMFEColorMatrixElement(String prefix, AbstractDocument owner) {
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
		type = createLiveAnimatedEnumeration(null, SVG_TYPE_ATTRIBUTE, TYPE_VALUES, (short) 1);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_FE_COLOR_MATRIX_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEColorMatrixElement#getIn1()}.
	 */
	@Override
	public SVGAnimatedString getIn1() {
		return in;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEColorMatrixElement#getType()}.
	 */
	@Override
	public SVGAnimatedEnumeration getType() {
		return type;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEColorMatrixElement#getValues()}.
	 */
	@Override
	public SVGAnimatedNumberList getValues() {
		throw new UnsupportedOperationException("SVGFEColorMatrixElement.getValues is not implemented"); // XXX
		// return values;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMFEColorMatrixElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

}
