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

import org.w3c.dom.svg.SVGAnimatedPoints;
import org.w3c.dom.svg.SVGPointList;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a common superclass for shape elements that are defined
 * with a 'points' attribute (i.e., polygon and polyline).
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGPointShapeElement extends SVGGraphicsElement implements SVGAnimatedPoints {

	private static final long serialVersionUID = 1L;
	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGGraphicsElement.xmlTraitInformation);
		t.put(null, SVG_POINTS_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_POINTS_VALUE));
		xmlTraitInformation = t;
	}

	/**
	 * The 'points' attribute value.
	 */
	protected SVGOMAnimatedPoints points;

	/**
	 * Creates a new SVGPointShapeElement object.
	 */
	protected SVGPointShapeElement() {
	}

	/**
	 * Creates a new SVGPointShapeElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGPointShapeElement(String prefix, AbstractDocument owner) {
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
		points = createLiveAnimatedPoints(null, SVG_POINTS_ATTRIBUTE, "");
	}

	/**
	 * Gets the {@link SVGOMAnimatedPoints} object that manages the point list for
	 * this element.
	 */
	public SVGOMAnimatedPoints getSVGOMAnimatedPoints() {
		return points;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGAnimatedPoints#getPoints()}.
	 */
	@Override
	public SVGPointList getPoints() {
		return points.getPoints();
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGAnimatedPoints#getAnimatedPoints()}.
	 */
	@Override
	public SVGPointList getAnimatedPoints() {
		return points.getAnimatedPoints();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

}
