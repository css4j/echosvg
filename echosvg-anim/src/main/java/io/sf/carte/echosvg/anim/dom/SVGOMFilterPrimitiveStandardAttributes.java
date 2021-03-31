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

import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class represents a SVGElement with support for standard filter
 * attributes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGOMFilterPrimitiveStandardAttributes extends SVGStylableElement
		implements SVGFilterPrimitiveStandardAttributes {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGStylableElement.xmlTraitInformation);
		t.put(null, SVG_X_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_Y_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_WIDTH_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_HEIGHT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_RESULT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_CDATA));
		xmlTraitInformation = t;
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
	 * The 'result' attribute value.
	 */
	protected SVGOMAnimatedString result;

	/**
	 * Creates a new SVGOMFilterPrimitiveStandardAttributes object.
	 */
	protected SVGOMFilterPrimitiveStandardAttributes() {
	}

	/**
	 * Creates a new SVGOMFilterPrimitiveStandardAttributes object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected SVGOMFilterPrimitiveStandardAttributes(String prefix, AbstractDocument owner) {
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
		x = createLiveAnimatedLength(null, SVG_X_ATTRIBUTE, SVG_FILTER_PRIMITIVE_X_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
		y = createLiveAnimatedLength(null, SVG_Y_ATTRIBUTE, SVG_FILTER_PRIMITIVE_Y_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
		width = createLiveAnimatedLength(null, SVG_WIDTH_ATTRIBUTE, SVG_FILTER_PRIMITIVE_WIDTH_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, true);
		height = createLiveAnimatedLength(null, SVG_HEIGHT_ATTRIBUTE, SVG_FILTER_PRIMITIVE_HEIGHT_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, true);
		result = createLiveAnimatedString(null, SVG_RESULT_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getX()}.
	 */
	@Override
	public SVGAnimatedLength getX() {
		return x;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getY()}.
	 */
	@Override
	public SVGAnimatedLength getY() {
		return y;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getWidth()}.
	 */
	@Override
	public SVGAnimatedLength getWidth() {
		return width;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getHeight()}.
	 */
	@Override
	public SVGAnimatedLength getHeight() {
		return height;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getResult()}.
	 */
	@Override
	public SVGAnimatedString getResult() {
		return result;
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
