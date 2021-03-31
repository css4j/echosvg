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

import org.w3c.dom.svg.SVGAnimatedLengthList;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGTextPositioningElement;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGTextPositioningElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGOMTextPositioningElement extends SVGOMTextContentElement implements SVGTextPositioningElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGOMTextContentElement.xmlTraitInformation);
		t.put(null, SVG_X_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH_LIST, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_Y_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH_LIST, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_DX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH_LIST, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_DY_ATTRIBUTE,
				new TraitInformation(true, SVGTypes.TYPE_LENGTH_LIST, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_ROTATE_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_NUMBER_LIST));
		xmlTraitInformation = t;
	}

	/**
	 * The 'x' attribute value.
	 */
	protected SVGOMAnimatedLengthList x;

	/**
	 * The 'y' attribute value.
	 */
	protected SVGOMAnimatedLengthList y;

	/**
	 * The 'dx' attribute value.
	 */
	protected SVGOMAnimatedLengthList dx;

	/**
	 * The 'dy' attribute value.
	 */
	protected SVGOMAnimatedLengthList dy;

	/**
	 * The 'rotate' attribute value.
	 */
	protected SVGOMAnimatedNumberList rotate;

	/**
	 * Creates a new SVGOMTextPositioningElement object.
	 */
	protected SVGOMTextPositioningElement() {
	}

	/**
	 * Creates a new SVGOMTextPositioningElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected SVGOMTextPositioningElement(String prefix, AbstractDocument owner) {
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
		x = createLiveAnimatedLengthList(null, SVG_X_ATTRIBUTE, getDefaultXValue(), true,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH);
		y = createLiveAnimatedLengthList(null, SVG_Y_ATTRIBUTE, getDefaultYValue(), true,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH);
		dx = createLiveAnimatedLengthList(null, SVG_DX_ATTRIBUTE, "", true,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH);
		dy = createLiveAnimatedLengthList(null, SVG_DY_ATTRIBUTE, "", true, AbstractSVGAnimatedLength.VERTICAL_LENGTH);
		rotate = createLiveAnimatedNumberList(null, SVG_ROTATE_ATTRIBUTE, "", true);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getX()}.
	 */
	@Override
	public SVGAnimatedLengthList getX() {
		return x;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getY()}.
	 */
	@Override
	public SVGAnimatedLengthList getY() {
		return y;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getDx()}.
	 */
	@Override
	public SVGAnimatedLengthList getDx() {
		return dx;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getDy()}.
	 */
	@Override
	public SVGAnimatedLengthList getDy() {
		return dy;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getRotate()}.
	 */
	@Override
	public SVGAnimatedNumberList getRotate() {
		return rotate;
	}

	/**
	 * Returns the default value of the 'x' attribute.
	 */
	protected String getDefaultXValue() {
		return "";
	}

	/**
	 * Returns the default value of the 'y' attribute.
	 */
	protected String getDefaultYValue() {
		return "";
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}
}
