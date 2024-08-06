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
package io.sf.carte.echosvg.bridge;

import java.awt.geom.AffineTransform;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGPreserveAspectRatio;

import io.sf.carte.doc.style.css.CSSUnit;
import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.property.Evaluator;
import io.sf.carte.doc.style.css.property.ExpressionValue;
import io.sf.carte.doc.style.css.property.StyleValue;
import io.sf.carte.doc.style.css.property.TypedValue;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.doc.style.css.property.ValueList;
import io.sf.carte.echosvg.anim.dom.SVGOMAnimatedRect;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.dom.svg.SVGOMRect;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.parser.AWTTransformProducer;
import io.sf.carte.echosvg.parser.FragmentIdentifierHandler;
import io.sf.carte.echosvg.parser.FragmentIdentifierParser;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.PreserveAspectRatioParser;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This class provides convenient methods to handle viewport.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class ViewBox implements SVGConstants, ErrorConstants {

	/**
	 * No instance of this class is required.
	 */
	protected ViewBox() {
	}

	/**
	 * Parses the specified reference (from a URI) and returns the appropriate
	 * transform.
	 *
	 * @param ref the reference of the URI that may specify additional attribute
	 *            values such as the viewBox, preserveAspectRatio or a transform
	 * @param e   the element interested in its view transform
	 * @param w   the width of the effective viewport
	 * @param h   The height of the effective viewport
	 * @param ctx The BridgeContext to use for error information
	 * @exception BridgeException if an error occured while computing the
	 *                            preserveAspectRatio transform
	 */
	public static AffineTransform getViewTransform(String ref, Element e, float w, float h, BridgeContext ctx) {

		// no reference has been specified, no extra viewBox is defined
		if (ref == null || ref.length() == 0) {
			return getPreserveAspectRatioTransform(e, w, h, ctx);
		}

		ViewHandler vh = new ViewHandler();
		FragmentIdentifierParser p = new FragmentIdentifierParser();
		p.setFragmentIdentifierHandler(vh);
		p.parse(ref);

		// Determine the 'view' element that ref refers to.
		Element viewElement = e;
		if (vh.hasId) {
			Document document = e.getOwnerDocument();
			viewElement = document.getElementById(vh.id);
		}
		if (viewElement == null) {
			throw new BridgeException(ctx, e, ERR_URI_MALFORMED, new Object[] { ref });
		}

		Element ancestorSVG = getClosestAncestorSVGElement(e);
		if (!(viewElement.getNamespaceURI().equals(SVG_NAMESPACE_URI)
				&& viewElement.getLocalName().equals(SVG_VIEW_TAG))) {
			viewElement = ancestorSVG;
		}

		// 'viewBox'
		float[] vb;
		if (vh.hasViewBox) {
			vb = vh.viewBox;
		} else {
			Element elt;
			if (DOMUtilities.isAttributeSpecifiedNS(viewElement, null, SVG_VIEW_BOX_ATTRIBUTE)) {
				elt = viewElement;
			} else {
				elt = ancestorSVG;
			}
			String viewBoxStr = elt.getAttributeNS(null, SVG_VIEW_BOX_ATTRIBUTE).trim();
			vb = parseViewBoxAttribute(elt, viewBoxStr, ctx);
		}

		// 'preserveAspectRatio'
		short align;
		boolean meet;
		if (vh.hasPreserveAspectRatio) {
			align = vh.align;
			meet = vh.meet;
		} else {
			Element elt;
			if (DOMUtilities.isAttributeSpecifiedNS(viewElement, null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE)) {
				elt = viewElement;
			} else {
				elt = ancestorSVG;
			}
			String aspectRatio = elt.getAttributeNS(null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE);
			PreserveAspectRatioParser pp = new PreserveAspectRatioParser();
			ViewHandler ph = new ViewHandler();
			pp.setPreserveAspectRatioHandler(ph);
			try {
				pp.parse(aspectRatio);
			} catch (ParseException pEx) {
				throw new BridgeException(ctx, elt, pEx, ERR_ATTRIBUTE_VALUE_MALFORMED,
						new Object[] { SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE, aspectRatio, pEx });
			}
			align = ph.align;
			meet = ph.meet;
		}

		// the additional transform that may appear on the URI
		AffineTransform transform = getPreserveAspectRatioTransform(vb, align, meet, w, h);
		if (transform != null && vh.hasTransform) {
			transform.concatenate(vh.getAffineTransform());
		}

		return transform;
	}

	/**
	 * Returns the closest svg element ancestor of the specified element.
	 *
	 * @param e the element on which to start the svg element lookup
	 */
	private static Element getClosestAncestorSVGElement(Element e) {
		for (Node n = e; n != null && n.getNodeType() == Node.ELEMENT_NODE; n = n.getParentNode()) {
			Element tmp = (Element) n;
			if (tmp.getNamespaceURI().equals(SVG_NAMESPACE_URI) && tmp.getLocalName().equals(SVG_SVG_TAG)) {
				return tmp;
			}
		}
		return null;
	}

	/**
	 * Returns the transformation matrix to apply to initalize a viewport or null if
	 * the specified viewBox disables the rendering of the element.
	 *
	 * @deprecated Replaced by
	 *             {@link #getPreserveAspectRatioTransform(Element,float,float,BridgeContext)},
	 *             which has more accurate error reporting.
	 * @param e the element with a viewbox
	 * @param w the width of the effective viewport
	 * @param h The height of the effective viewport
	 */
	@Deprecated
	public static AffineTransform getPreserveAspectRatioTransform(Element e, float w, float h) {
		return getPreserveAspectRatioTransform(e, w, h, null);
	}

	/**
	 * Returns the transformation matrix to apply to initalize a viewport or null if
	 * the specified viewBox disables the rendering of the element.
	 *
	 * @param e   the element with a viewbox
	 * @param w   the width of the effective viewport
	 * @param h   The height of the effective viewport
	 * @param ctx The BridgeContext to use for error information
	 */
	public static AffineTransform getPreserveAspectRatioTransform(Element e, float w, float h,
			BridgeContext ctx) throws BridgeException {
		String viewBox = e.getAttributeNS(null, SVG_VIEW_BOX_ATTRIBUTE);

		String aspectRatio = e.getAttributeNS(null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE);

		return getPreserveAspectRatioTransform(e, viewBox, aspectRatio, w, h, ctx);
	}

	/**
	 * Returns the transformation matrix to apply to initalize a viewport or null if
	 * the specified viewBox disables the rendering of the element.
	 *
	 * @param e       the element with a viewbox
	 * @param viewBox the viewBox definition
	 * @param w       the width of the effective viewport
	 * @param h       The height of the effective viewport
	 * @param ctx     The BridgeContext to use for error information
	 */
	public static AffineTransform getPreserveAspectRatioTransform(Element e, String viewBox, String aspectRatio,
			float w, float h, BridgeContext ctx) throws BridgeException {

		// no viewBox specified
		if (viewBox.length() == 0) {
			return new AffineTransform();
		}
		float[] vb;
		try {
			vb = parseViewBoxAttribute(e, viewBox, ctx);
		} catch (BridgeException be) {
			if (ctx.userAgent != null) {
				ctx.userAgent.displayError(be);
			} else {
				throw be;
			}
			return new AffineTransform();
		}

		// 'preserveAspectRatio' attribute
		PreserveAspectRatioParser p = new PreserveAspectRatioParser();
		ViewHandler ph = new ViewHandler();
		p.setPreserveAspectRatioHandler(ph);
		try {
			p.parse(aspectRatio);
		} catch (ParseException pEx) {
			BridgeException be = new BridgeException(ctx, e, pEx, ERR_ATTRIBUTE_VALUE_MALFORMED,
					new Object[] { SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE, aspectRatio, pEx });
			if (ctx.userAgent != null) {
				ctx.userAgent.displayError(be);
			} else {
				throw be;
			}
			return new AffineTransform();
		}

		return getPreserveAspectRatioTransform(vb, ph.align, ph.meet, w, h);
	}

	/**
	 * Returns the transformation matrix to apply to initalize a viewport or null if
	 * the specified viewBox disables the rendering of the element.
	 *
	 * @param e   the element with a viewbox
	 * @param vb  the viewBox definition as float
	 * @param w   the width of the effective viewport
	 * @param h   The height of the effective viewport
	 * @param ctx The BridgeContext to use for error information
	 */
	public static AffineTransform getPreserveAspectRatioTransform(Element e, float[] vb, float w, float h,
			BridgeContext ctx) throws BridgeException {

		String aspectRatio = e.getAttributeNS(null, SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE);

		// 'preserveAspectRatio' attribute
		PreserveAspectRatioParser p = new PreserveAspectRatioParser();
		ViewHandler ph = new ViewHandler();
		p.setPreserveAspectRatioHandler(ph);
		try {
			p.parse(aspectRatio);
		} catch (ParseException pEx) {
			throw new BridgeException(ctx, e, pEx, ERR_ATTRIBUTE_VALUE_MALFORMED,
					new Object[] { SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE, aspectRatio, pEx });
		}

		return getPreserveAspectRatioTransform(vb, ph.align, ph.meet, w, h);
	}

	/**
	 * Returns the transformation matrix to apply to initalize a viewport or null if
	 * the specified viewBox disables the rendering of the element.
	 *
	 * @param e    the element with a viewbox
	 * @param vb   the viewBox definition as float
	 * @param w    the width of the effective viewport
	 * @param h    The height of the effective viewport
	 * @param aPAR The animated preserveAspectRatio value
	 * @param ctx  The BridgeContext to use for error information
	 */
	public static AffineTransform getPreserveAspectRatioTransform(Element e, float[] vb, float w, float h,
			SVGAnimatedPreserveAspectRatio aPAR, BridgeContext ctx) throws BridgeException {

		// 'preserveAspectRatio' attribute
		try {
			SVGPreserveAspectRatio pAR = aPAR.getAnimVal();
			short align = pAR.getAlign();
			boolean meet = pAR.getMeetOrSlice() == SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;
			return getPreserveAspectRatioTransform(vb, align, meet, w, h);
		} catch (LiveAttributeException ex) {
			throw new BridgeException(ctx, ex);
		}
	}

	/**
	 * Returns the transformation matrix to apply to initialize a viewport or null
	 * if the specified viewBox disables the rendering of the element.
	 *
	 * @param e        the element with a viewbox
	 * @param aViewBox the viewBox definition
	 * @param aPAR     the preserveAspectRatio definition
	 * @param w        the width of the effective viewport
	 * @param h        the height of the effective viewport
	 * @param ctx      the BridgeContext to use for error information
	 */
	public static AffineTransform getPreserveAspectRatioTransform(Element e, SVGAnimatedRect aViewBox,
			SVGAnimatedPreserveAspectRatio aPAR, float w, float h, BridgeContext ctx)
					throws BridgeException {
		SVGOMAnimatedRect rectVB = (SVGOMAnimatedRect) aViewBox;
		if (!rectVB.isSpecified()) {
			// no viewBox specified
			return new AffineTransform();
		}

		float[] vb;
		try {
			vb = ((SVGOMRect) rectVB.getAnimVal()).toArray();
		} catch (LiveAttributeException ex) {
			if (ctx.userAgent != null) {
				ctx.userAgent.displayError(ex);
			} else {
				throw new BridgeException(ctx, ex);
			}
			return new AffineTransform();
		}

		AffineTransform art;
		try {
			art = getPreserveAspectRatioTransform(e, vb, w, h, aPAR, ctx);
		} catch (BridgeException ex) {
			if (ctx.userAgent != null) {
				ctx.userAgent.displayError(ex);
			} else {
				throw ex;
			}
			art = new AffineTransform();
		}
		return art;
	}

	/**
	 * Parses a viewBox attribute.
	 *
	 * @param e     the element whose viewBox attribute value is being parsed
	 * @param value the viewBox, cannot be null nor empty.
	 * @param ctx   the BridgeContext to use for error information
	 * @return The 4 viewbox components or null.
	 */
	public static float[] parseViewBoxAttribute(Element e, String value, BridgeContext ctx)
			throws BridgeException {
		if (value.isEmpty()) {
			/*
			 * Cannot return anything meaningful, so return null
			 */
			return null;
		}

		float[] vb = new float[4];
		ValueFactory factory = new ValueFactory();
		try {
			StyleValue sv = factory.parseProperty(value);
			if (!computeRectangle(sv, vb)) {
				throw new BridgeException(ctx, e, ERR_ATTRIBUTE_VALUE_MALFORMED,
						new Object[] { SVG_VIEW_BOX_ATTRIBUTE, value });
			}
		} catch (Exception ex) {
			throw new BridgeException(ctx, e, ex, ERR_ATTRIBUTE_VALUE_MALFORMED,
					new Object[] { SVG_VIEW_BOX_ATTRIBUTE, value, ex });
		}

		// A negative value for <width> or <height> is an error
		if (vb[2] < 0 || vb[3] < 0) {
			throw new BridgeException(ctx, e, ERR_ATTRIBUTE_VALUE_MALFORMED,
					new Object[] { SVG_VIEW_BOX_ATTRIBUTE, value });
		}

		// A value of zero for width or height disables rendering of the element
		if (vb[2] == 0 || vb[3] == 0) {
			return null; // disable rendering
		}

		return vb;
	}

	static boolean computeRectangle(StyleValue value, float[] numbers) throws DOMException {
		if (value.getCssValueType() != CssType.LIST) {
			return false;
		}
		ValueList list = (ValueList) value;
		if (list.getLength() != 4) {
			return false;
		}

		for (int i = 0; i < 4; i++) {
			StyleValue item = list.item(i);
			if (item.getCssValueType() != CssType.TYPED) {
				return false;
			}
			TypedValue typed;
			switch (item.getPrimitiveType()) {
			case NUMERIC:
				typed = (TypedValue) item;
				if (typed.getUnitType() != CSSUnit.CSS_NUMBER) {
					return false;
				}
				break;
			case EXPRESSION:
				Evaluator eval = new Evaluator();
				typed = eval.evaluateExpression((ExpressionValue) item);
				if (typed.getUnitType() != CSSUnit.CSS_NUMBER) {
					return false;
				}
				break;
			default:
				return false;
			}
			numbers[i] = typed.getFloatValue(CSSUnit.CSS_NUMBER);
		}
		return true;
	}

	/**
	 * Returns the preserveAspectRatio transform according to the specified
	 * parameters.
	 *
	 * @param vb    the viewBox definition
	 * @param align the alignment definition
	 * @param meet  true means 'meet', false means 'slice'
	 * @param w     the width of the region in which the document has to fit into
	 * @param h     the height of the region in which the document has to fit into
	 */
	public static AffineTransform getPreserveAspectRatioTransform(float[] vb, short align, boolean meet, float w,
			float h) {
		if (vb == null) {
			return null;
		}

		AffineTransform result = new AffineTransform();
		float vpar = vb[2] / vb[3];
		float svgar = w / h;

		if (align == SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_NONE) {
			result.scale(w / vb[2], h / vb[3]);
			result.translate(-vb[0], -vb[1]);
		} else if (vpar < svgar && meet || vpar >= svgar && !meet) {
			float sf = h / vb[3];
			result.scale(sf, sf);
			switch (align) {
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMIN:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMID:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMAX:
				result.translate(-vb[0], -vb[1]);
				break;
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMIN:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMAX:
				result.translate(-vb[0] - (vb[2] - w * vb[3] / h) / 2, -vb[1]);
				break;
			default:
				result.translate(-vb[0] - (vb[2] - w * vb[3] / h), -vb[1]);
			}
		} else {
			float sf = w / vb[2];
			result.scale(sf, sf);
			switch (align) {
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMIN:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMIN:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMIN:
				result.translate(-vb[0], -vb[1]);
				break;
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMID:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID:
			case SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMID:
				result.translate(-vb[0], -vb[1] - (vb[3] - h * vb[2] / w) / 2);
				break;
			default:
				result.translate(-vb[0], -vb[1] - (vb[3] - h * vb[2] / w));
			}
		}
		return result;
	}

	/**
	 * This class can be used to store the value of the attribute viewBox or can
	 * also be used to store the various attribute value that can be specified on a
	 * SVG URI fragments.
	 */
	protected static class ViewHandler extends AWTTransformProducer implements FragmentIdentifierHandler {

		/**
		 * Constructs a new <code>ViewHandler</code> instance.
		 */
		protected ViewHandler() {
		}

		//////////////////////////////////////////////////////////////////////
		// TransformListHandler
		//////////////////////////////////////////////////////////////////////

		public boolean hasTransform;

		@Override
		public void endTransformList() throws ParseException {
			super.endTransformList();
			hasTransform = true;
		}

		//////////////////////////////////////////////////////////////////////
		// FragmentIdentifierHandler
		//////////////////////////////////////////////////////////////////////

		public boolean hasId;
		public boolean hasViewBox;
		public boolean hasViewTargetParams;
		public boolean hasZoomAndPanParams;

		public String id;
		public float[] viewBox;
		public String viewTargetParams;
		public boolean isMagnify;

		/**
		 * Invoked when the fragment identifier starts.
		 * 
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void startFragmentIdentifier() throws ParseException {
		}

		/**
		 * Invoked when an ID has been parsed.
		 * 
		 * @param s The string that represents the parsed ID.
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void idReference(String s) throws ParseException {
			id = s;
			hasId = true;
		}

		/**
		 * Invoked when 'viewBox(x,y,width,height)' has been parsed.
		 * 
		 * @param x      the viewbox x coordinate
		 * @param y      the viewbox y coordinate
		 * @param width  the viewbox width
		 * @param height the viewbox height
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void viewBox(float x, float y, float width, float height) throws ParseException {

			hasViewBox = true;
			viewBox = new float[4];
			viewBox[0] = x;
			viewBox[1] = y;
			viewBox[2] = width;
			viewBox[3] = height;
		}

		/**
		 * Invoked when a view target specification starts.
		 * 
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void startViewTarget() throws ParseException {
		}

		/**
		 * Invoked when a identifier has been parsed within a view target specification.
		 * 
		 * @param name the target name.
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void viewTarget(String name) throws ParseException {
			viewTargetParams = name;
			hasViewTargetParams = true;
		}

		/**
		 * Invoked when a view target specification ends.
		 * 
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void endViewTarget() throws ParseException {
		}

		/**
		 * Invoked when a 'zoomAndPan' specification has been parsed.
		 * 
		 * @param magnify true if 'magnify' has been parsed.
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void zoomAndPan(boolean magnify) {
			isMagnify = magnify;
			hasZoomAndPanParams = true;
		}

		/**
		 * Invoked when the fragment identifier ends.
		 * 
		 * @exception ParseException if an error occured while processing the fragment
		 *                           identifier
		 */
		@Override
		public void endFragmentIdentifier() throws ParseException {
		}

		//////////////////////////////////////////////////////////////////////
		// PreserveAspectRatioHandler
		//////////////////////////////////////////////////////////////////////

		public boolean hasPreserveAspectRatio;

		public short align;
		public boolean meet = true;

		/**
		 * Invoked when the PreserveAspectRatio parsing starts.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void startPreserveAspectRatio() throws ParseException {
		}

		/**
		 * Invoked when 'none' been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void none() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_NONE;
		}

		/**
		 * Invoked when 'xMaxYMax' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMaxYMax() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMAX;
		}

		/**
		 * Invoked when 'xMaxYMid' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMaxYMid() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMID;
		}

		/**
		 * Invoked when 'xMaxYMin' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMaxYMin() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMIN;
		}

		/**
		 * Invoked when 'xMidYMax' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMidYMax() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMAX;
		}

		/**
		 * Invoked when 'xMidYMid' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMidYMid() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;
		}

		/**
		 * Invoked when 'xMidYMin' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMidYMin() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMIN;
		}

		/**
		 * Invoked when 'xMinYMax' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMinYMax() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMAX;
		}

		/**
		 * Invoked when 'xMinYMid' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMinYMid() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMID;
		}

		/**
		 * Invoked when 'xMinYMin' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMinYMin() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMIN;
		}

		/**
		 * Invoked when 'meet' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void meet() throws ParseException {
			meet = true;
		}

		/**
		 * Invoked when 'slice' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void slice() throws ParseException {
			meet = false;
		}

		/**
		 * Invoked when the PreserveAspectRatio parsing ends.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void endPreserveAspectRatio() throws ParseException {
			hasPreserveAspectRatio = true;
		}

	}

}
