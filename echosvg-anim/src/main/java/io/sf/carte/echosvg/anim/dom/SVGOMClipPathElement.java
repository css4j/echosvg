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
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGClipPathElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGClipPathElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMClipPathElement extends SVGGraphicsElement implements SVGClipPathElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGGraphicsElement.xmlTraitInformation);
		t.put(null, SVG_CLIP_PATH_UNITS_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_IDENT));
		xmlTraitInformation = t;
	}

	/**
	 * The clipPathUnits values.
	 */
	protected static final String[] CLIP_PATH_UNITS_VALUES = { "", SVG_USER_SPACE_ON_USE_VALUE,
			SVG_OBJECT_BOUNDING_BOX_VALUE };

	/**
	 * The 'clipPathUnits' attribute value.
	 */
	protected SVGOMAnimatedEnumeration clipPathUnits;

	/**
	 * Creates a new SVGOMClipPathElement object.
	 */
	protected SVGOMClipPathElement() {
	}

	/**
	 * Creates a new SVGOMClipPathElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMClipPathElement(String prefix, AbstractDocument owner) {
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
		clipPathUnits = createLiveAnimatedEnumeration(null, SVG_CLIP_PATH_UNITS_ATTRIBUTE, CLIP_PATH_UNITS_VALUES,
				(short) 1);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_CLIP_PATH_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGClipPathElement#getClipPathUnits()}.
	 */
	@Override
	public SVGAnimatedEnumeration getClipPathUnits() {
		return clipPathUnits;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMClipPathElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
