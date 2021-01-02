/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

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
import org.w3c.dom.svg.SVGFEDistantLightElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGFEDistantLightElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMFEDistantLightElement extends SVGOMElement implements SVGFEDistantLightElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGOMElement.xmlTraitInformation);
		t.put(null, SVG_AZIMUTH_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		t.put(null, SVG_ELEVATION_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER));
		xmlTraitInformation = t;
	}

	/**
	 * The 'azimuth' attribute value.
	 */
	protected SVGOMAnimatedNumber azimuth;

	/**
	 * The 'elevation' attribute value.
	 */
	protected SVGOMAnimatedNumber elevation;

	/**
	 * Creates a new SVGOMFEDistantLightElement object.
	 */
	protected SVGOMFEDistantLightElement() {
	}

	/**
	 * Creates a new SVGOMFEDistantLightElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMFEDistantLightElement(String prefix, AbstractDocument owner) {
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
		azimuth = createLiveAnimatedNumber(null, SVG_AZIMUTH_ATTRIBUTE, 0f);
		elevation = createLiveAnimatedNumber(null, SVG_ELEVATION_ATTRIBUTE, 0f);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_FE_DISTANT_LIGHT_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEDistantLightElement#getAzimuth()}.
	 */
	@Override
	public SVGAnimatedNumber getAzimuth() {
		return azimuth;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGFEDistantLightElement#getElevation()}.
	 */
	@Override
	public SVGAnimatedNumber getElevation() {
		return elevation;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMFEDistantLightElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
