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

import java.util.LinkedList;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedInteger;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGFitToViewBox;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.echosvg.anim.values.AnimatableNumberOptionalNumberValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSNavigableNode;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.AbstractStylableDocument;
import io.sf.carte.echosvg.dom.svg.ExtendedTraitAccess;
import io.sf.carte.echosvg.dom.svg.LiveAttributeValue;
import io.sf.carte.echosvg.dom.svg.SVGContext;
import io.sf.carte.echosvg.dom.svg.SVGOMException;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.parser.UnitProcessor;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements the {@link SVGElement} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGOMElement extends AbstractElement implements SVGElement, ExtendedTraitAccess, AnimationTarget {

	private static final long serialVersionUID = 1L;

	/**
	 * Table mapping XML attribute names to TraitInformation objects.
	 */
	protected static DoublyIndexedTable<String, String> xmlTraitInformation;
	static {
		DoublyIndexedTable<String, String> t = new DoublyIndexedTable<>();
		t.put(null, SVG_ID_ATTRIBUTE, new TraitInformation(false, SVGTypes.TYPE_CDATA));
		t.put(XML_NAMESPACE_URI, XML_BASE_ATTRIBUTE, new TraitInformation(false, SVGTypes.TYPE_URI));
		t.put(XML_NAMESPACE_URI, XML_SPACE_ATTRIBUTE, new TraitInformation(false, SVGTypes.TYPE_IDENT));
		t.put(XML_NAMESPACE_URI, XML_ID_ATTRIBUTE, new TraitInformation(false, SVGTypes.TYPE_CDATA));
		t.put(XML_NAMESPACE_URI, XML_LANG_ATTRIBUTE, new TraitInformation(false, SVGTypes.TYPE_LANG));
		xmlTraitInformation = t;
	}

	/**
	 * Is this element immutable?
	 */
	protected transient boolean readonly;

	/**
	 * The element prefix.
	 */
	protected String prefix;

	/**
	 * The SVG context to get SVG specific informations.
	 */
	protected transient SVGContext svgContext;

	/**
	 * Table mapping namespaceURI/local name pairs to {@link LinkedList}s of
	 * {@link AnimationTargetListener}s.
	 */
	protected DoublyIndexedTable<String, String> targetListeners;

	/**
	 * The context used to resolve the units.
	 */
	protected UnitProcessor.Context unitContext;

	/**
	 * Creates a new Element object.
	 */
	protected SVGOMElement() {
	}

	/**
	 * Creates a new Element object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	protected SVGOMElement(String prefix, AbstractDocument owner) {
		super(prefix, owner);
		// initializeLiveAttributes();
	}

	/**
	 * Initializes all live attributes for this element.
	 */
	protected void initializeAllLiveAttributes() {
		// initializeLiveAttributes();
	}

//     /**
//       * Initializes the live attribute values of this element.
//       */
//      private void initializeLiveAttributes() {
//          // If live attributes are added here, make sure to uncomment the
//          // call to initializeLiveAttributes in the constructor and
//          // initializeAllLiveAttributes method above.
//      }

	/**
	 * <b>DOM</b>: Implements {@link SVGElement#getId()}.
	 */
	@Override
	public String getId() {
		if (((SVGOMDocument) ownerDocument).isSVG12) {
			Attr a = getAttributeNodeNS(XML_NAMESPACE_URI, SVG_ID_ATTRIBUTE);
			if (a != null) {
				return a.getNodeValue();
			}
		}
		return getAttributeNS(null, SVG_ID_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGElement#setId(String)}.
	 */
	@Override
	public void setId(String id) {
		if (((SVGOMDocument) ownerDocument).isSVG12) {
			setAttributeNS(XML_NAMESPACE_URI, XML_ID_QNAME, id);
			Attr a = getAttributeNodeNS(null, SVG_ID_ATTRIBUTE);
			if (a != null) {
				a.setNodeValue(id);
			}
		} else {
			setAttributeNS(null, SVG_ID_ATTRIBUTE, id);
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGElement#getXMLbase()}.
	 */
	@Override
	public String getXMLbase() {
		return getAttributeNS(XML_NAMESPACE_URI, XML_BASE_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGElement#setXMLbase(String)}.
	 */
	@Override
	public void setXMLbase(String xmlbase) throws DOMException {
		setAttributeNS(XML_NAMESPACE_URI, XML_BASE_QNAME, xmlbase);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGElement#getOwnerSVGElement()}.
	 */
	@Override
	public SVGSVGElement getOwnerSVGElement() {
		for (Element e = CSSEngine.getParentCSSStylableElement(this); e != null; e = CSSEngine
				.getParentCSSStylableElement(e)) {
			if (e instanceof SVGSVGElement) {
				return (SVGSVGElement) e;
			}
		}
		return null;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGElement#getViewportElement()}.
	 */
	@Override
	public SVGElement getViewportElement() {
		for (Element e = CSSEngine.getParentCSSStylableElement(this); e != null; e = CSSEngine
				.getParentCSSStylableElement(e)) {
			if (e instanceof SVGFitToViewBox) {
				return (SVGElement) e;
			}
		}
		return null;
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getNodeName()}.
	 */
	@Override
	public String getNodeName() {
		if (prefix == null || prefix.equals("")) {
			return getLocalName();
		}

		return prefix + ':' + getLocalName();
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getNamespaceURI()}.
	 */
	@Override
	public String getNamespaceURI() {
		return SVGDOMImplementation.SVG_NAMESPACE_URI;
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#setPrefix(String)}.
	 */
	@Override
	public void setPrefix(String prefix) throws DOMException {
		if (isReadonly()) {
			throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.node",
					new Object[] { (int) getNodeType(), getNodeName() });
		}
		if (prefix != null && !prefix.equals("") && !DOMUtilities.isValidName(prefix)) {
			throw createDOMException(DOMException.INVALID_CHARACTER_ERR, "prefix",
					new Object[] { (int) getNodeType(), getNodeName(), prefix });
		}
		this.prefix = prefix;
	}

	/**
	 * Returns the xml:base attribute value of the given element, resolving any
	 * dependency on parent bases if needed. Follows shadow trees when moving to
	 * parent nodes.
	 */
	@Override
	protected String getCascadedXMLBase(Node node) {
		String base = null;
		Node n = node.getParentNode();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				base = getCascadedXMLBase(n);
				break;
			}
			if (n instanceof CSSNavigableNode) {
				n = ((CSSNavigableNode) n).getCSSParentNode();
			} else {
				n = n.getParentNode();
			}
		}
		if (base == null) {
			AbstractDocument doc;
			if (node.getNodeType() == Node.DOCUMENT_NODE) {
				doc = (AbstractDocument) node;
			} else {
				doc = (AbstractDocument) node.getOwnerDocument();
			}
			base = doc.getDocumentURI();
		}
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getParentNode();
		}
		if (node == null) {
			return base;
		}
		Element e = (Element) node;
		Attr attr = e.getAttributeNodeNS(XML_NAMESPACE_URI, XML_BASE_ATTRIBUTE);
		if (attr != null) {
			if (base == null) {
				base = attr.getNodeValue();
			} else {
				base = new ParsedURL(base, attr.getNodeValue()).toString();
			}
		}
		return base;
	}

	// SVGContext ////////////////////////////////////////////////////

	/**
	 * Sets the SVG context to use to get SVG specific informations.
	 *
	 * @param ctx the SVG context
	 */
	public void setSVGContext(SVGContext ctx) {
		svgContext = ctx;
	}

	/**
	 * Returns the SVG context used to get SVG specific informations.
	 */
	public SVGContext getSVGContext() {
		return svgContext;
	}

	// ExtendedNode //////////////////////////////////////////////////

	/**
	 * Creates an SVGException with the appropriate error message.
	 */
	public SVGException createSVGException(short type, String key, Object[] args) {
		try {
			return new SVGOMException(type, getCurrentDocument().formatMessage(key, args));
		} catch (Exception e) {
			return new SVGOMException(type, key);
		}
	}

	/**
	 * Tests whether this node is readonly.
	 */
	@Override
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * Sets this node readonly attribute.
	 */
	@Override
	public void setReadonly(boolean v) {
		readonly = v;
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedTransformList} and stores it in this
	 * element's LiveAttributeValue table.
	 */
	protected SVGOMAnimatedTransformList createLiveAnimatedTransformList(String ns, String ln, String def) {
		SVGOMAnimatedTransformList v = new SVGOMAnimatedTransformList(this, ns, ln, def);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedBoolean} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedBoolean createLiveAnimatedBoolean(String ns, String ln, boolean def) {
		SVGOMAnimatedBoolean v = new SVGOMAnimatedBoolean(this, ns, ln, def);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedString} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedString createLiveAnimatedString(String ns, String ln) {
		SVGOMAnimatedString v = new SVGOMAnimatedString(this, ns, ln);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedPreserveAspectRatio} and stores it in this
	 * element's LiveAttributeValue table.
	 */
	protected SVGOMAnimatedPreserveAspectRatio createLiveAnimatedPreserveAspectRatio() {
		SVGOMAnimatedPreserveAspectRatio v = new SVGOMAnimatedPreserveAspectRatio(this);
		liveAttributeValues.put(null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedMarkerOrientValue} and stores it in this
	 * element's LiveAttributeValue table.
	 */
	protected SVGOMAnimatedMarkerOrientValue createLiveAnimatedMarkerOrientValue(String ns, String ln) {
		SVGOMAnimatedMarkerOrientValue v = new SVGOMAnimatedMarkerOrientValue(this, ns, ln);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedPathData} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedPathData createLiveAnimatedPathData(String ns, String ln, String def) {
		SVGOMAnimatedPathData v = new SVGOMAnimatedPathData(this, ns, ln, def);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedNumber} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedNumber createLiveAnimatedNumber(String ns, String ln, float def) {
		return createLiveAnimatedNumber(ns, ln, def, false);
	}

	/**
	 * Creates a new {@link SVGOMAnimatedNumber} that can be parsed as a percentage
	 * and stores it in this element's LiveAttributeValue table.
	 */
	protected SVGOMAnimatedNumber createLiveAnimatedNumber(String ns, String ln, float def, boolean allowPercentage) {
		SVGOMAnimatedNumber v = new SVGOMAnimatedNumber(this, ns, ln, def, allowPercentage);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedNumberList} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedNumberList createLiveAnimatedNumberList(String ns, String ln, String def, boolean canEmpty) {
		SVGOMAnimatedNumberList v = new SVGOMAnimatedNumberList(this, ns, ln, def, canEmpty);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedPoints} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedPoints createLiveAnimatedPoints(String ns, String ln, String def) {
		SVGOMAnimatedPoints v = new SVGOMAnimatedPoints(this, ns, ln, def);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedLengthList} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedLengthList createLiveAnimatedLengthList(String ns, String ln, String def,
			boolean emptyAllowed, short dir) {
		SVGOMAnimatedLengthList v = new SVGOMAnimatedLengthList(this, ns, ln, def, emptyAllowed, dir);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedInteger} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedInteger createLiveAnimatedInteger(String ns, String ln, int def) {
		SVGOMAnimatedInteger v = new SVGOMAnimatedInteger(this, ns, ln, def);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedEnumeration} and stores it in this
	 * element's LiveAttributeValue table.
	 */
	protected SVGOMAnimatedEnumeration createLiveAnimatedEnumeration(String ns, String ln, String[] val, short def) {
		SVGOMAnimatedEnumeration v = new SVGOMAnimatedEnumeration(this, ns, ln, val, def);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedLength} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedLength createLiveAnimatedLength(String ns, String ln, String val, short dir,
			boolean nonneg) {
		SVGOMAnimatedLength v = new SVGOMAnimatedLength(this, ns, ln, val, dir, nonneg);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	/**
	 * Creates a new {@link SVGOMAnimatedRect} and stores it in this element's
	 * LiveAttributeValue table.
	 */
	protected SVGOMAnimatedRect createLiveAnimatedRect(String ns, String ln, String value) {
		SVGOMAnimatedRect v = new SVGOMAnimatedRect(this, ns, ln, value);
		liveAttributeValues.put(ns, ln, v);
		v.addAnimatedAttributeListener(((SVGOMDocument) ownerDocument).getAnimatedAttributeListener());
		return v;
	}

	// ExtendedTraitAccess ///////////////////////////////////////////////////

	/**
	 * Returns whether the given CSS property is available on this element.
	 */
	@Override
	public boolean hasProperty(String pn) {
		AbstractStylableDocument doc = (AbstractStylableDocument) ownerDocument;
		CSSEngine eng = doc.getCSSEngine();
		return eng.getPropertyIndex(pn) != -1 || eng.getShorthandIndex(pn) != -1;
	}

	/**
	 * Returns whether the given trait is available on this element.
	 */
	@Override
	public boolean hasTrait(String ns, String ln) {
		// XXX no traits yet
		return false;
	}

	/**
	 * Returns whether the given CSS property is animatable.
	 */
	@Override
	public boolean isPropertyAnimatable(String pn) {
		AbstractStylableDocument doc = (AbstractStylableDocument) ownerDocument;
		CSSEngine eng = doc.getCSSEngine();
		int idx = eng.getPropertyIndex(pn);
		if (idx != -1) {
			ValueManager[] vms = eng.getValueManagers();
			return vms[idx].isAnimatableProperty();
		}
		idx = eng.getShorthandIndex(pn);
		if (idx != -1) {
			ShorthandManager[] sms = eng.getShorthandManagers();
			return sms[idx].isAnimatableProperty();
		}
		return false;
	}

	/**
	 * Returns whether the given XML attribute is animatable.
	 */
	@Override
	public final boolean isAttributeAnimatable(String ns, String ln) {
		DoublyIndexedTable<String, String> t = getTraitInformationTable();
		TraitInformation ti = (TraitInformation) t.get(ns, ln);
		if (ti != null) {
			return ti.isAnimatable();
		}
		return false;
	}

	/**
	 * Returns whether the given CSS property is additive.
	 */
	@Override
	public boolean isPropertyAdditive(String pn) {
		AbstractStylableDocument doc = (AbstractStylableDocument) ownerDocument;
		CSSEngine eng = doc.getCSSEngine();
		int idx = eng.getPropertyIndex(pn);
		if (idx != -1) {
			ValueManager[] vms = eng.getValueManagers();
			return vms[idx].isAdditiveProperty();
		}
		idx = eng.getShorthandIndex(pn);
		if (idx != -1) {
			ShorthandManager[] sms = eng.getShorthandManagers();
			return sms[idx].isAdditiveProperty();
		}
		return false;
	}

	/**
	 * Returns whether the given XML attribute is additive.
	 */
	@Override
	public boolean isAttributeAdditive(String ns, String ln) {
		return true;
	}

	/**
	 * Returns whether the given trait is animatable.
	 */
	@Override
	public boolean isTraitAnimatable(String ns, String tn) {
		// XXX no traits yet
		return false;
	}

	/**
	 * Returns whether the given trait is additive.
	 */
	@Override
	public boolean isTraitAdditive(String ns, String tn) {
		// XXX no traits yet
		return false;
	}

	/**
	 * Returns the type of the given property.
	 */
	@Override
	public int getPropertyType(String pn) {
		AbstractStylableDocument doc = (AbstractStylableDocument) ownerDocument;
		CSSEngine eng = doc.getCSSEngine();
		int idx = eng.getPropertyIndex(pn);
		if (idx != -1) {
			ValueManager[] vms = eng.getValueManagers();
			return vms[idx].getPropertyType();
		}
		return SVGTypes.TYPE_UNKNOWN;
	}

	/**
	 * Returns the type of the given attribute.
	 */
	@Override
	public final int getAttributeType(String ns, String ln) {
		DoublyIndexedTable<String, String> t = getTraitInformationTable();
		TraitInformation ti = (TraitInformation) t.get(ns, ln);
		if (ti != null) {
			return ti.getType();
		}
		return SVGTypes.TYPE_UNKNOWN;
	}

	// AnimationTarget ///////////////////////////////////////////////////////

	/**
	 * Returns the element.
	 */
	@Override
	public Element getElement() {
		return this;
	}

	/**
	 * Updates a property value in this target. Ignored for non-stylable elements.
	 * Overridden in {@link SVGStylableElement} to actually update properties.
	 */
	@Override
	public void updatePropertyValue(String pn, AnimatableValue val) {
	}

	/**
	 * Updates an attribute value in this target.
	 */
	@Override
	public void updateAttributeValue(String ns, String ln, AnimatableValue val) {
		LiveAttributeValue a = getLiveAttributeValue(ns, ln);
		((AbstractSVGAnimatedValue) a).updateAnimatedValue(val);
		// XXX Override this for NumberOptionalNumber values
	}

	/**
	 * Updates a 'other' animation value in this target.
	 */
	@Override
	public void updateOtherValue(String type, AnimatableValue val) {
	}

	/**
	 * Returns the underlying value of an animatable XML attribute.
	 */
	@Override
	public AnimatableValue getUnderlyingValue(String ns, String ln) {
		LiveAttributeValue a = getLiveAttributeValue(ns, ln);
		if (!(a instanceof AnimatedLiveAttributeValue)) {
			return null;
		}
		return ((AnimatedLiveAttributeValue) a).getUnderlyingValue(this);
		// XXX Override this for NumberOptionalNumber values
	}

	/**
	 * Returns an AnimatableNumberOptionalNumberValue for the base value of the
	 * given two SVGAnimatedIntegers.
	 */
	protected AnimatableValue getBaseValue(SVGAnimatedInteger n, SVGAnimatedInteger on) {
		return new AnimatableNumberOptionalNumberValue(this, n.getBaseVal(), on.getBaseVal());
	}

	/**
	 * Returns an AnimatableNumberOptionalNumberValue for the base value of the
	 * given two SVGAnimatedNumbers.
	 */
	protected AnimatableValue getBaseValue(SVGAnimatedNumber n, SVGAnimatedNumber on) {
		return new AnimatableNumberOptionalNumberValue(this, n.getBaseVal(), on.getBaseVal());
	}

	/**
	 * Gets how percentage values are interpreted by the given attribute or
	 * property.
	 */
	@Override
	public short getPercentageInterpretation(String ns, String an, boolean isCSS) {
		if ((isCSS || ns == null) && (CSSConstants.CSS_BASELINE_SHIFT_PROPERTY.equals(an)
				|| CSSConstants.CSS_FONT_SIZE_PROPERTY.equals(an))) {
			return PERCENTAGE_FONT_SIZE;
		}
		if (!isCSS) {
			DoublyIndexedTable<String, String> t = getTraitInformationTable();
			TraitInformation ti = (TraitInformation) t.get(ns, an);
			if (ti != null) {
				return ti.getPercentageInterpretation();
			}
			return PERCENTAGE_VIEWPORT_SIZE;
		}
		// Default for properties.
		return PERCENTAGE_VIEWPORT_SIZE;
	}

	/**
	 * Gets how percentage values are interpreted by the given attribute.
	 */
	protected final short getAttributePercentageInterpretation(String ns, String ln) {
		return PERCENTAGE_VIEWPORT_SIZE;
	}

	/**
	 * Returns whether color interpolations should be done in linear RGB color space
	 * rather than sRGB. Overriden in {@link SVGStylableElement} to actually look up
	 * the 'color-interpolation' property.
	 */
	@Override
	public boolean useLinearRGBColorInterpolation() {
		return false;
	}

	/**
	 * Converts the given SVG length into user units.
	 * 
	 * @param v        the SVG length value
	 * @param type     the CSS unit (one of the
	 *                 {@link CSSUnit}.CSS_* constants)
	 * @param pcInterp how to interpretet percentage values (one of the
	 *                 {@link SVGContext}.PERCENTAGE_* constants)
	 * @return the SVG value in user units
	 */
	@Override
	public float svgToUserSpace(float v, short type, short pcInterp) {
		if (unitContext == null) {
			unitContext = new UnitContext();
		}
		if (pcInterp == PERCENTAGE_FONT_SIZE && type == CSSUnit.CSS_PERCENTAGE) {
			// XXX
			return 0f;
		} else {
			return UnitProcessor.cssToUserSpace(v, type, (short) (3 - pcInterp), unitContext);
		}
	}

	/**
	 * Adds a listener for changes to the given attribute value.
	 */
	@Override
	public void addTargetListener(String ns, String an, boolean isCSS, AnimationTargetListener l) {
		if (!isCSS) {
			if (targetListeners == null) {
				targetListeners = new DoublyIndexedTable<>();
			}
			@SuppressWarnings("unchecked")
			LinkedList<AnimationTargetListener> ll = (LinkedList<AnimationTargetListener>) targetListeners.get(ns, an);
			if (ll == null) {
				ll = new LinkedList<>();
				targetListeners.put(ns, an, ll);
			}
			ll.add(l);
		}
	}

	/**
	 * Removes a listener for changes to the given attribute value.
	 */
	@Override
	public void removeTargetListener(String ns, String an, boolean isCSS, AnimationTargetListener l) {
		if (!isCSS) {
			@SuppressWarnings("unchecked")
			LinkedList<AnimationTargetListener> ll = (LinkedList<AnimationTargetListener>) targetListeners.get(ns, an);
			ll.remove(l);
		}
	}

	/**
	 * Fires the listeners registered for changes to the base value of the given
	 * attribute.
	 */
	void fireBaseAttributeListeners(String ns, String ln) {
		if (targetListeners != null) {
			@SuppressWarnings("unchecked")
			LinkedList<AnimationTargetListener> ll = (LinkedList<AnimationTargetListener>) targetListeners.get(ns, ln);
			for (AnimationTargetListener l : ll) {
				l.baseValueChanged(this, ns, ln, false);
			}
		}
	}

	// Importation/Cloning ///////////////////////////////////////////

	/**
	 * Exports this node to the given document.
	 */
	@Override
	protected Node export(Node n, AbstractDocument d) {
		super.export(n, d);
		SVGOMElement e = (SVGOMElement) n;
		e.prefix = prefix;
		e.initializeAllLiveAttributes();
		return n;
	}

	/**
	 * Deeply exports this node to the given document.
	 */
	@Override
	protected Node deepExport(Node n, AbstractDocument d) {
		super.deepExport(n, d);
		SVGOMElement e = (SVGOMElement) n;
		e.prefix = prefix;
		e.initializeAllLiveAttributes();
		return n;
	}

	/**
	 * Copy the fields of the current node into the given node.
	 * 
	 * @param n a node of the type of this.
	 */
	@Override
	protected Node copyInto(Node n) {
		super.copyInto(n);
		SVGOMElement e = (SVGOMElement) n;
		e.prefix = prefix;
		e.initializeAllLiveAttributes();
		return n;
	}

	/**
	 * Deeply copy the fields of the current node into the given node.
	 * 
	 * @param n a node of the type of this.
	 */
	@Override
	protected Node deepCopyInto(Node n) {
		super.deepCopyInto(n);
		SVGOMElement e = (SVGOMElement) n;
		e.prefix = prefix;
		e.initializeAllLiveAttributes();
		return n;
	}

	/**
	 * To resolve the units.
	 */
	protected class UnitContext implements UnitProcessor.Context {

		/**
		 * Returns the element.
		 */
		@Override
		public Element getElement() {
			return SVGOMElement.this;
		}

		@Override
		public float getResolution() {
			return getSVGContext().getResolution();
		}

		/**
		 * Returns the font-size value.
		 */
		@Override
		public float getFontSize() {
			return getSVGContext().getFontSize();
		}

		@Override
		public float getLineHeight() {
			return getSVGContext().getLineHeight();
		}

		@Override
		public float getRootFontSize() {
			return getSVGContext().getRootFontSize();
		}

		@Override
		public float getRootLineHeight() {
			return getSVGContext().getRootLineHeight();
		}

		/**
		 * Returns the x-height value.
		 */
		@Override
		public float getXHeight() {
			return 0.5f;
		}

		@Override
		public float getRootXHeight() {
			return 0.5f;
		}

		/**
		 * Returns the viewport width used to compute units.
		 */
		@Override
		public float getViewportWidth() {
			return getSVGContext().getViewportWidth();
		}

		/**
		 * Returns the viewport height used to compute units.
		 */
		@Override
		public float getViewportHeight() {
			return getSVGContext().getViewportHeight();
		}

	}

}
