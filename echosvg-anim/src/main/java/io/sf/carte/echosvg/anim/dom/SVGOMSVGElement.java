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

import java.awt.geom.AffineTransform;
import java.util.List;

import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.stylesheets.DocumentStyle;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.svg.SVGAngle;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGNumber;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.svg.SVGStringList;
import org.w3c.dom.svg.SVGTransform;
import org.w3c.dom.svg.SVGViewSpec;
import org.w3c.dom.view.DocumentCSS;
import org.w3c.dom.view.ViewCSS;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.svg.AbstractSVGMatrix;
import io.sf.carte.echosvg.dom.svg.SVGContext;
import io.sf.carte.echosvg.dom.svg.SVGOMAngle;
import io.sf.carte.echosvg.dom.svg.SVGOMPoint;
import io.sf.carte.echosvg.dom.svg.SVGOMRect;
import io.sf.carte.echosvg.dom.svg.SVGOMTransform;
import io.sf.carte.echosvg.dom.svg.SVGSVGContext;
import io.sf.carte.echosvg.dom.svg.SVGTestsSupport;
import io.sf.carte.echosvg.dom.svg.SVGZoomAndPanSupport;
import io.sf.carte.echosvg.dom.util.ListNodeList;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class implements {@link org.w3c.dom.svg.SVGSVGElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMSVGElement extends SVGStylableElement implements SVGSVGElement {

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
//         t.put(null, SVG_BASE_PROFILE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_CONTENT_SCRIPT_TYPE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_CONTENT_STYLE_TYPE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_VERSION_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
		t.put(null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE,
				new TraitInformation(true, SVGTypes.TYPE_PRESERVE_ASPECT_RATIO_VALUE));
		t.put(null, SVG_VIEW_BOX_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_RECT));
		t.put(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, new TraitInformation(true, SVGTypes.TYPE_BOOLEAN));
		xmlTraitInformation = t;
	}

	/**
	 * The attribute initializer.
	 */
	protected static final AttributeInitializer attributeInitializer;
	static {
		attributeInitializer = new AttributeInitializer(7);
		attributeInitializer.addAttribute(XMLConstants.XMLNS_NAMESPACE_URI, null, "xmlns", SVG_NAMESPACE_URI);
		attributeInitializer.addAttribute(XMLConstants.XMLNS_NAMESPACE_URI, "xmlns", "xlink",
				XMLConstants.XLINK_NAMESPACE_URI);
		attributeInitializer.addAttribute(null, null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE, "xMidYMid meet");
		attributeInitializer.addAttribute(null, null, SVG_ZOOM_AND_PAN_ATTRIBUTE, SVG_MAGNIFY_VALUE);
		attributeInitializer.addAttribute(null, null, SVG_VERSION_ATTRIBUTE, SVG_VERSION);
		attributeInitializer.addAttribute(null, null, SVG_CONTENT_SCRIPT_TYPE_ATTRIBUTE, "text/ecmascript");
		attributeInitializer.addAttribute(null, null, SVG_CONTENT_STYLE_TYPE_ATTRIBUTE, "text/css");
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
	 * The 'externalResourcesRequired' attribute value.
	 */
	protected SVGOMAnimatedBoolean externalResourcesRequired;

	/**
	 * The 'preserveAspectRatio' attribute value.
	 */
	protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;

	/**
	 * The 'viewBox' attribute value.
	 */
	protected SVGOMAnimatedRect viewBox;

	/**
	 * Creates a new SVGOMSVGElement object.
	 */
	protected SVGOMSVGElement() {
	}

	/**
	 * Creates a new SVGOMSVGElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMSVGElement(String prefix, AbstractDocument owner) {
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
		x = createLiveAnimatedLength(null, SVG_X_ATTRIBUTE, SVG_SVG_X_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, false);
		y = createLiveAnimatedLength(null, SVG_Y_ATTRIBUTE, SVG_SVG_Y_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, false);
		width = createLiveAnimatedLength(null, SVG_WIDTH_ATTRIBUTE, SVG_SVG_WIDTH_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.HORIZONTAL_LENGTH, true);
		height = createLiveAnimatedLength(null, SVG_HEIGHT_ATTRIBUTE, SVG_SVG_HEIGHT_DEFAULT_VALUE,
				AbstractSVGAnimatedLength.VERTICAL_LENGTH, true);
		externalResourcesRequired = createLiveAnimatedBoolean(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE, false);
		preserveAspectRatio = createLiveAnimatedPreserveAspectRatio();
		viewBox = createLiveAnimatedRect(null, SVG_VIEW_BOX_ATTRIBUTE, null);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_SVG_TAG;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getX()}.
	 */
	@Override
	public SVGAnimatedLength getX() {
		return x;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getY()}.
	 */
	@Override
	public SVGAnimatedLength getY() {
		return y;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getWidth()}.
	 */
	@Override
	public SVGAnimatedLength getWidth() {
		return width;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getHeight()}.
	 */
	@Override
	public SVGAnimatedLength getHeight() {
		return height;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getContentScriptType()}.
	 */
	@Override
	public String getContentScriptType() {
		return getAttributeNS(null, SVG_CONTENT_SCRIPT_TYPE_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#setContentScriptType(String)}.
	 */
	@Override
	public void setContentScriptType(String type) {
		setAttributeNS(null, SVG_CONTENT_SCRIPT_TYPE_ATTRIBUTE, type);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getContentStyleType()}.
	 */
	@Override
	public String getContentStyleType() {
		return getAttributeNS(null, SVG_CONTENT_STYLE_TYPE_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#setContentStyleType(String)}.
	 */
	@Override
	public void setContentStyleType(String type) {
		setAttributeNS(null, SVG_CONTENT_STYLE_TYPE_ATTRIBUTE, type);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getViewport()}.
	 */
	@Override
	public SVGRect getViewport() {
		SVGContext ctx = getSVGContext();
		return new SVGOMRect(0, 0, ctx.getViewportWidth(), ctx.getViewportHeight());
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getPixelUnitToMillimeterX()}.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public float getPixelUnitToMillimeterX() {
		return getSVGContext().getPixelUnitToMillimeter();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getPixelUnitToMillimeterY()}.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public float getPixelUnitToMillimeterY() {
		return getSVGContext().getPixelUnitToMillimeter();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getScreenPixelToMillimeterX()}.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public float getScreenPixelToMillimeterX() {
		return getSVGContext().getPixelUnitToMillimeter();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getScreenPixelToMillimeterY()}.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public float getScreenPixelToMillimeterY() {
		return getSVGContext().getPixelUnitToMillimeter();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getUseCurrentView()}.
	 */
	@Override
	public boolean getUseCurrentView() {
		throw new UnsupportedOperationException("SVGSVGElement.getUseCurrentView is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#setUseCurrentView(boolean)}.
	 */
	@Override
	public void setUseCurrentView(boolean useCurrentView) throws DOMException {
		throw new UnsupportedOperationException("SVGSVGElement.setUseCurrentView is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getCurrentView()}.
	 */
	@Override
	public SVGViewSpec getCurrentView() {
		throw new UnsupportedOperationException("SVGSVGElement.getCurrentView is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getCurrentView()}.
	 */
	@Override
	public float getCurrentScale() {
		AffineTransform scrnTrans = getSVGContext().getScreenTransform();
		if (scrnTrans != null) {
			return (float) Math.sqrt(scrnTrans.getDeterminant());
		}
		return 1;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#setCurrentScale(float)}.
	 */
	@Override
	public void setCurrentScale(float currentScale) throws DOMException {
		SVGContext context = getSVGContext();
		AffineTransform scrnTrans = context.getScreenTransform();
		if (scrnTrans != null) {
			float scale = (float) Math.sqrt(scrnTrans.getDeterminant());
			float delta = currentScale / scale;
			// The way currentScale, currentTranslate are defined
			// changing scale has no effect on translate.
			scrnTrans = new AffineTransform(scrnTrans.getScaleX() * delta, scrnTrans.getShearY() * delta,
					scrnTrans.getShearX() * delta, scrnTrans.getScaleY() * delta, scrnTrans.getTranslateX(),
					scrnTrans.getTranslateY());
			context.setScreenTransform(scrnTrans);
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getCurrentTranslate()}.
	 */
	@Override
	public SVGPoint getCurrentTranslate() {
		return new SVGPoint() {
			protected AffineTransform getScreenTransform() {
				SVGContext context = getSVGContext();
				return context.getScreenTransform();
			}

			@Override
			public float getX() {
				AffineTransform scrnTrans = getScreenTransform();
				return (float) scrnTrans.getTranslateX();
			}

			@Override
			public float getY() {
				AffineTransform scrnTrans = getScreenTransform();
				return (float) scrnTrans.getTranslateY();
			}

			@Override
			public void setX(float newX) {
				SVGContext context = getSVGContext();
				AffineTransform scrnTrans = context.getScreenTransform();
				scrnTrans = new AffineTransform(scrnTrans.getScaleX(), scrnTrans.getShearY(), scrnTrans.getShearX(),
						scrnTrans.getScaleY(), newX, scrnTrans.getTranslateY());
				context.setScreenTransform(scrnTrans);
			}

			@Override
			public void setY(float newY) {
				SVGContext context = getSVGContext();
				AffineTransform scrnTrans = context.getScreenTransform();
				scrnTrans = new AffineTransform(scrnTrans.getScaleX(), scrnTrans.getShearY(), scrnTrans.getShearX(),
						scrnTrans.getScaleY(), scrnTrans.getTranslateX(), newY);
				context.setScreenTransform(scrnTrans);
			}

			@Override
			public SVGPoint matrixTransform(SVGMatrix mat) {
				AffineTransform scrnTrans = getScreenTransform();
				float x = (float) scrnTrans.getTranslateX();
				float y = (float) scrnTrans.getTranslateY();
				float newX = mat.getA() * x + mat.getC() * y + mat.getE();
				float newY = mat.getB() * x + mat.getD() * y + mat.getF();
				return new SVGOMPoint(newX, newY);
			}
		};
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#suspendRedraw(int)}.
	 */
	@Override
	public int suspendRedraw(int max_wait_milliseconds) {
		if (max_wait_milliseconds > 60000) {
			max_wait_milliseconds = 60000;
		} else if (max_wait_milliseconds < 0) {
			max_wait_milliseconds = 0;
		}
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		return ctx.suspendRedraw(max_wait_milliseconds);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#unsuspendRedraw(int)}.
	 */
	@Override
	public void unsuspendRedraw(int suspend_handle_id) throws DOMException {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		if (!ctx.unsuspendRedraw(suspend_handle_id)) {
			throw createDOMException(DOMException.NOT_FOUND_ERR, "invalid.suspend.handle",
					new Object[] { suspend_handle_id });
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#unsuspendRedrawAll()}.
	 */
	@Override
	public void unsuspendRedrawAll() {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		ctx.unsuspendRedrawAll();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#forceRedraw()}.
	 */
	@Override
	public void forceRedraw() {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		ctx.forceRedraw();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#pauseAnimations()}.
	 */
	@Override
	public void pauseAnimations() {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		ctx.pauseAnimations();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#unpauseAnimations()}.
	 */
	@Override
	public void unpauseAnimations() {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		ctx.unpauseAnimations();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#animationsPaused()}.
	 */
	@Override
	public boolean animationsPaused() {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		return ctx.animationsPaused();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getCurrentTime()}.
	 */
	@Override
	public float getCurrentTime() {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		return ctx.getCurrentTime();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#setCurrentTime(float)}.
	 */
	@Override
	public void setCurrentTime(float seconds) {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		ctx.setCurrentTime(seconds);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGSVGElement#getIntersectionList(SVGRect,SVGElement)}.
	 */
	@Override
	public NodeList getIntersectionList(SVGRect rect, SVGElement referenceElement) {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		List<Element> list = ctx.getIntersectionList(rect, referenceElement);
		return new ListNodeList(list);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGSVGElement#getEnclosureList(SVGRect,SVGElement)}.
	 */
	@Override
	public NodeList getEnclosureList(SVGRect rect, SVGElement referenceElement) {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		List<Element> list = ctx.getEnclosureList(rect, referenceElement);
		return new ListNodeList(list);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGSVGElement#checkIntersection(SVGElement,SVGRect)}.
	 */
	@Override
	public boolean checkIntersection(SVGElement element, SVGRect rect) {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		return ctx.checkIntersection(element, rect);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGSVGElement#checkEnclosure(SVGElement,SVGRect)}.
	 */
	@Override
	public boolean checkEnclosure(SVGElement element, SVGRect rect) {
		SVGSVGContext ctx = (SVGSVGContext) getSVGContext();
		return ctx.checkEnclosure(element, rect);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#deselectAll()}.
	 */
	@Override
	public void deselectAll() {
		((SVGSVGContext) getSVGContext()).deselectAll();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#createSVGNumber()}.
	 */
	@Override
	public SVGNumber createSVGNumber() {
		return new SVGNumber() {
			protected float value;

			@Override
			public float getValue() {
				return value;
			}

			@Override
			public void setValue(float f) {
				value = f;
			}
		};
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#createSVGLength()}.
	 */
	@Override
	public SVGLength createSVGLength() {
		return new SVGOMLength(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#createSVGAngle()}.
	 */
	@Override
	public SVGAngle createSVGAngle() {
		return new SVGOMAngle();
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#createSVGPoint()}.
	 */
	@Override
	public SVGPoint createSVGPoint() {
		return new SVGOMPoint(0, 0);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#createSVGMatrix()}.
	 */
	@Override
	public SVGMatrix createSVGMatrix() {
		return new AbstractSVGMatrix() {
			protected AffineTransform at = new AffineTransform();

			@Override
			protected AffineTransform getAffineTransform() {
				return at;
			}
		};
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#createSVGRect()}.
	 */
	@Override
	public SVGRect createSVGRect() {
		return new SVGOMRect(0, 0, 0, 0);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#createSVGTransform()}.
	 */
	@Override
	public SVGTransform createSVGTransform() {
		SVGOMTransform ret = new SVGOMTransform();
		ret.setType(SVGTransform.SVG_TRANSFORM_MATRIX);
		return ret;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGSVGElement#createSVGTransformFromMatrix(SVGMatrix)}.
	 */
	@Override
	public SVGTransform createSVGTransformFromMatrix(SVGMatrix matrix) {
		SVGOMTransform tr = new SVGOMTransform();
		tr.setMatrix(matrix);
		return tr;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGSVGElement#getElementById(String)}.
	 */
	@Override
	public Element getElementById(String elementId) {
		return ownerDocument.getChildElementById(this, elementId);
	}

	// SVGLocatable ///////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGLocatable#getNearestViewportElement()}.
	 */
	@Override
	public SVGElement getNearestViewportElement() {
		return SVGLocatableSupport.getNearestViewportElement(this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGLocatable#getFarthestViewportElement()}.
	 */
	@Override
	public SVGElement getFarthestViewportElement() {
		return SVGLocatableSupport.getFarthestViewportElement(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGLocatable#getBBox()}.
	 */
	@Override
	public SVGRect getBBox() {
		return SVGLocatableSupport.getBBox(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGLocatable#getCTM()}.
	 */
	@Override
	public SVGMatrix getCTM() {
		return SVGLocatableSupport.getCTM(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGLocatable#getScreenCTM()}.
	 */
	@Override
	public SVGMatrix getScreenCTM() {
		return SVGLocatableSupport.getScreenCTM(this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGLocatable#getTransformToElement(SVGElement)}.
	 */
	@Override
	public SVGMatrix getTransformToElement(SVGElement element) throws SVGException {
		return SVGLocatableSupport.getTransformToElement(this, element);
	}

	// ViewCSS ////////////////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.views.AbstractView#getDocument()}.
	 */
	@Override
	public DocumentView getDocument() {
		return (DocumentView) getOwnerDocument();
	}

	@Override
	public CSSStyleDeclaration getComputedStyle(Element elt, String pseudoElt) {
		AbstractView av = ((DocumentView) getOwnerDocument()).getDefaultView();
		return ((ViewCSS) av).getComputedStyle(elt, pseudoElt);
	}

	// DocumentEvent /////////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.events.DocumentEvent#createEvent(String)}.
	 */
	@Override
	public Event createEvent(String eventType) throws DOMException {
		return ((DocumentEvent) getOwnerDocument()).createEvent(eventType);
	}

	/**
	 * <b>DOM</b>: Implements
	 * org.w3c.dom.events.DocumentEvent#canDispatch(String,String).
	 */
	public boolean canDispatch(String namespaceURI, String type) throws DOMException {
		AbstractDocument doc = (AbstractDocument) getOwnerDocument();
		return doc.canDispatch(namespaceURI, type);
	}

	// DocumentCSS ////////////////////////////////////////////////////////////

	@Override
	public StyleSheetList getStyleSheets() {
		return ((DocumentStyle) getOwnerDocument()).getStyleSheets();
	}

	@Override
	public CSSStyleDeclaration getOverrideStyle(Element elt, String pseudoElt) {
		return ((DocumentCSS) getOwnerDocument()).getOverrideStyle(elt, pseudoElt);
	}

	// SVGLangSpace support //////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Returns the xml:lang attribute value.
	 */
	@Override
	public String getXMLlang() {
		return XMLSupport.getXMLLang(this);
	}

	/**
	 * <b>DOM</b>: Sets the xml:lang attribute value.
	 */
	@Override
	public void setXMLlang(String lang) {
		setAttributeNS(XML_NAMESPACE_URI, XML_LANG_QNAME, lang);
	}

	/**
	 * <b>DOM</b>: Returns the xml:space attribute value.
	 */
	@Override
	public String getXMLspace() {
		return XMLSupport.getXMLSpace(this);
	}

	/**
	 * <b>DOM</b>: Sets the xml:space attribute value.
	 */
	@Override
	public void setXMLspace(String space) {
		setAttributeNS(XML_NAMESPACE_URI, XML_SPACE_QNAME, space);
	}

	// SVGZoomAndPan support ///////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGZoomAndPan#getZoomAndPan()}.
	 */
	@Override
	public short getZoomAndPan() {
		return SVGZoomAndPanSupport.getZoomAndPan(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGZoomAndPan#getZoomAndPan()}.
	 */
	@Override
	public void setZoomAndPan(short val) {
		SVGZoomAndPanSupport.setZoomAndPan(this, val);
	}

	// SVGFitToViewBox support ////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGFitToViewBox#getViewBox()}.
	 */
	@Override
	public SVGAnimatedRect getViewBox() {
		return viewBox;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGFitToViewBox#getPreserveAspectRatio()}.
	 */
	@Override
	public SVGAnimatedPreserveAspectRatio getPreserveAspectRatio() {
		return preserveAspectRatio;
	}

	// SVGExternalResourcesRequired support /////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGExternalResourcesRequired#getExternalResourcesRequired()}.
	 */
	@Override
	public SVGAnimatedBoolean getExternalResourcesRequired() {
		return externalResourcesRequired;
	}

	// SVGTests support ///////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGTests#getRequiredFeatures()}.
	 */
	@Override
	public SVGStringList getRequiredFeatures() {
		return SVGTestsSupport.getRequiredFeatures(this);
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGTests#getRequiredExtensions()}.
	 */
	@Override
	public SVGStringList getRequiredExtensions() {
		return SVGTestsSupport.getRequiredExtensions(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGTests#getSystemLanguage()}.
	 */
	@Override
	public SVGStringList getSystemLanguage() {
		return SVGTestsSupport.getSystemLanguage(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGTests#hasExtension(String)}.
	 */
	@Override
	public boolean hasExtension(String extension) {
		return SVGTestsSupport.hasExtension(this, extension);
	}

	/**
	 * Returns the AttributeInitializer for this element type.
	 * 
	 * @return null if this element has no attribute with a default value.
	 */
	@Override
	protected AttributeInitializer getAttributeInitializer() {
		return attributeInitializer;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMSVGElement();
	}

	/**
	 * Returns the table of TraitInformation objects for this element.
	 */
	@Override
	protected DoublyIndexedTable<String, String> getTraitInformationTable() {
		return xmlTraitInformation;
	}

}
