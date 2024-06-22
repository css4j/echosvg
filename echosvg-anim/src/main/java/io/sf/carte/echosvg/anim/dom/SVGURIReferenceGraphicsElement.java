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

import org.w3c.dom.svg.SVGAnimatedString;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides support for Xlink to a graphics element.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGURIReferenceGraphicsElement extends SVGGraphicsElement {

	private static final long serialVersionUID = 2L;
	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGGraphicsElement.xmlTraitInformation);
		t.put(null, XLINK_HREF_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_URI));
		t.put(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_URI));
		xmlTraitInformation = t;
	}

	/**
	 * The namespaceless 'href' attribute value.
	 */
	private SVGOMAnimatedString href;

	/**
	 * The 'xlink:href' attribute value.
	 */
	private SVGOMAnimatedString xlinkhref;

	/**
	 * Creates a new SVGURIReferenceGraphicsElement object.
	 */
	protected SVGURIReferenceGraphicsElement() {
	}

	/**
	 * Creates a new SVGURIReferenceGraphicsElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected SVGURIReferenceGraphicsElement(String prefix, AbstractDocument owner) {
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
		href = createLiveAnimatedString(null, XLINK_HREF_ATTRIBUTE);
		xlinkhref = createLiveAnimatedString(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGURIReference#getHref()}.
	 */
	public SVGAnimatedString getHref() {
		return href.element.hasAttribute(XLINK_HREF_ATTRIBUTE) ? href : xlinkhref;
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
