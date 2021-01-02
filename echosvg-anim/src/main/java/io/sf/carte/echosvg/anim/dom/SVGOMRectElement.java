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

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGRectElement;

import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link SVGRectElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMRectElement extends SVGGraphicsElement implements SVGRectElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>(SVGGraphicsElement.xmlTraitInformation);
		t.put(null, SVG_X_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_Y_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_RX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_RY_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
		t.put(null, SVG_WIDTH_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_WIDTH));
		t.put(null, SVG_HEIGHT_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_LENGTH, PERCENTAGE_VIEWPORT_HEIGHT));
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
	 * The 'rx' attribute value.
	 */
	protected AbstractSVGAnimatedLength rx;

	/**
	 * The 'ry' attribute value.
	 */
	protected AbstractSVGAnimatedLength ry;

	/**
	 * The 'width' attribute value.
	 */
	protected SVGOMAnimatedLength width;

	/**
	 * The 'height' attribute value.
	 */
	protected SVGOMAnimatedLength height;

	/**
	 * Creates a new SVGOMRectElement object.
	 */
	protected SVGOMRectElement() {
	}

	/**
	 * Creates a new SVGOMRectElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMRectElement(String prefix, AbstractDocument owner) {
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
		x = createLiveAnimatedLength(null, SVG_X_ATTRIBUTE, SVG_RECT_X_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
		y = createLiveAnimatedLength(null, SVG_Y_ATTRIBUTE, SVG_RECT_Y_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
		width = createLiveAnimatedLength(null, SVG_WIDTH_ATTRIBUTE, null, AbstractSVGAnimatedLength.HORIZONTAL_LENGTH,
				true);
		height = createLiveAnimatedLength(null, SVG_HEIGHT_ATTRIBUTE, null, AbstractSVGAnimatedLength.VERTICAL_LENGTH,
				true);
		rx = new AbstractSVGAnimatedLength(this, null, SVG_RX_ATTRIBUTE, AbstractSVGAnimatedLength.HORIZONTAL_LENGTH,
				true) {
			@Override
			protected String getDefaultValue() {
				Attr attr = getAttributeNodeNS(null, SVG_RY_ATTRIBUTE);
				if (attr == null) {
					return "0";
				}
				return attr.getValue();
			}

			@Override
			protected void attrChanged() {
				super.attrChanged();
				AbstractSVGAnimatedLength ry = (AbstractSVGAnimatedLength) getRy();
				if (isSpecified() && !ry.isSpecified()) {
					ry.attrChanged();
				}
			}
		};
		ry = new AbstractSVGAnimatedLength(this, null, SVG_RY_ATTRIBUTE, AbstractSVGAnimatedLength.VERTICAL_LENGTH,
				true) {
			@Override
			protected String getDefaultValue() {
				Attr attr = getAttributeNodeNS(null, SVG_RX_ATTRIBUTE);
				if (attr == null) {
					return "0";
				}
				return attr.getValue();
			}

			@Override
			protected void attrChanged() {
				super.attrChanged();
				AbstractSVGAnimatedLength rx = (AbstractSVGAnimatedLength) getRx();
				if (isSpecified() && !rx.isSpecified()) {
					rx.attrChanged();
				}
			}
		};

		liveAttributeValues.put(null, SVG_RX_ATTRIBUTE, rx);
		liveAttributeValues.put(null, SVG_RY_ATTRIBUTE, ry);
		AnimatedAttributeListener l = ((SVGOMDocument) ownerDocument).getAnimatedAttributeListener();
		rx.addAnimatedAttributeListener(l);
		ry.addAnimatedAttributeListener(l);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_RECT_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGRectElement#getX()}.
	 */
	@Override
	public SVGAnimatedLength getX() {
		return x;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGRectElement#getY()}.
	 */
	@Override
	public SVGAnimatedLength getY() {
		return y;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGRectElement#getWidth()}.
	 */
	@Override
	public SVGAnimatedLength getWidth() {
		return width;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGRectElement#getHeight()}.
	 */
	@Override
	public SVGAnimatedLength getHeight() {
		return height;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGRectElement#getRx()}.
	 */
	@Override
	public SVGAnimatedLength getRx() {
		return rx;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGRectElement#getRy()}.
	 */
	@Override
	public SVGAnimatedLength getRy() {
		return ry;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMRectElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

	// AnimationTarget ///////////////////////////////////////////////////////

	/**
	 * Updates an attribute value in this target.
	 */
	@Override
	public void updateAttributeValue(String ns, String ln, AnimatableValue val) {
		if (ns == null) {
			if (ln.equals(SVG_RX_ATTRIBUTE)) {
				super.updateAttributeValue(ns, ln, val);
				AbstractSVGAnimatedLength ry = (AbstractSVGAnimatedLength) getRy();
				if (!ry.isSpecified()) {
					super.updateAttributeValue(ns, SVG_RY_ATTRIBUTE, val);
				}
				return;
			} else if (ln.equals(SVG_RY_ATTRIBUTE)) {
				super.updateAttributeValue(ns, ln, val);
				AbstractSVGAnimatedLength rx = (AbstractSVGAnimatedLength) getRx();
				if (!rx.isSpecified()) {
					super.updateAttributeValue(ns, SVG_RX_ATTRIBUTE, val);
				}
				return;
			}
		}
		super.updateAttributeValue(ns, ln, val);
	}
}
