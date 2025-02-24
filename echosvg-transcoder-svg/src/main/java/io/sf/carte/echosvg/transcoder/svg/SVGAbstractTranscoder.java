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
package io.sf.carte.echosvg.transcoder.svg;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.anim.dom.SVG12DOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGOMDocument;
import io.sf.carte.echosvg.bridge.BaseScriptingEnvironment;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.BridgeException;
import io.sf.carte.echosvg.bridge.DefaultScriptSecurity;
import io.sf.carte.echosvg.bridge.ExternalResourceSecurity;
import io.sf.carte.echosvg.bridge.GVTBuilder;
import io.sf.carte.echosvg.bridge.NoLoadScriptSecurity;
import io.sf.carte.echosvg.bridge.RelaxedExternalResourceSecurity;
import io.sf.carte.echosvg.bridge.RelaxedScriptSecurity;
import io.sf.carte.echosvg.bridge.SVGUtilities;
import io.sf.carte.echosvg.bridge.ScriptSecurity;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.bridge.ViewBox;
import io.sf.carte.echosvg.bridge.svg12.SVG12BridgeContext;
import io.sf.carte.echosvg.dom.AbstractParentNode;
import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.gvt.CanvasGraphicsNode;
import io.sf.carte.echosvg.gvt.CompositeGraphicsNode;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.impl.SizingHelper;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This class may be the base class of transcoders which take an SVG document as
 * input and which need to build a GVT tree and have a user agent.
 *
 * <p>
 * Original author: <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry
 * Kormann</a>. For later modifications, see Git history.
 * </p>
 * 
 * @version $Id$
 */
public abstract class SVGAbstractTranscoder extends io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder {

	/**
	 * Current GVT Tree, i.e., the GVT tree representing the page being printed
	 * currently.
	 */
	private GraphicsNode root;

	/**
	 * Current bridge context
	 */
	private BridgeContext ctx;

	/**
	 * Current gvt builder
	 */
	private GVTBuilder builder;

	/**
	 * The user agent dedicated to an SVG Transcoder.
	 */
	private UserAgent userAgent;

	protected SVGAbstractTranscoder() {
		super();
		userAgent = createUserAgent();
	}

	protected UserAgent createUserAgent() {
		return new SVGAbstractTranscoderUserAgent();
	}

	/**
	 * Gets the bridge context.
	 * 
	 * @return the {@code BridgeContext}, or {@code null} if no context was set.
	 */
	protected BridgeContext getBridgeContext() {
		return ctx;
	}

	/**
	 * Set the bridge context.
	 * 
	 * @param ctx the {@code BridgeContext}.
	 */
	protected void setBridgeContext(BridgeContext ctx) {
		this.ctx = ctx;
	}

	protected GraphicsNode getCurrentGVTree() {
		return root;
	}

	/**
	 * Set the root node of the current GVT tree.
	 * 
	 * @param root the root node.
	 */
	protected void setCurrentGVTree(GraphicsNode root) {
		this.root = root;
	}

	public UserAgent getUserAgent() {
		return userAgent;
	}

	@Override
	protected DocumentFactory createSVGDocumentFactory() {
		return new SAXSVGDocumentFactory();
	}

	@Override
	public void transcode(TranscoderInput input, TranscoderOutput output) throws TranscoderException {

		super.transcode(input, output);

		if (ctx != null)
			ctx.dispose();
	}

	/**
	 * Transcodes the specified Document as an image in the specified output.
	 *
	 * @param document the document to transcode. Cannot be {@code null}.
	 * @param uri      the uri of the document or {@code null} if any.
	 * @param output   the ouput where to transcode.
	 * @exception TranscoderException if an error occured while transcoding
	 */
	@Override
	protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
		SVGOMDocument svgDoc;
		// document is assumed to be non-null here
		Element root = document.getDocumentElement();
		if (!(document instanceof SVGOMDocument)
				|| root.getNamespaceURI() != SVGConstants.SVG_NAMESPACE_URI) {
			svgDoc = importAsSVGDocument(document, uri);
		} else {
			svgDoc = (SVGOMDocument) document;
		}

		if (svgDoc == null) {
			throw new TranscoderException("Document contains no valid SVG element.");
		}

		if (hints.containsKey(KEY_WIDTH))
			width = (Float) hints.get(KEY_WIDTH);
		if (hints.containsKey(KEY_HEIGHT))
			height = (Float) hints.get(KEY_HEIGHT);

		ctx = createBridgeContext(svgDoc);

		// build the GVT tree
		builder = new GVTBuilder();
		// flag that indicates if the document is dynamic
		boolean isDynamic = hints.containsKey(KEY_EXECUTE_ONLOAD) && (Boolean) hints.get(KEY_EXECUTE_ONLOAD);

		GraphicsNode gvtRoot;
		try {
			if (isDynamic)
				ctx.setDynamicState(BridgeContext.DYNAMIC);

			gvtRoot = builder.build(ctx, svgDoc);

			if (gvtRoot == null) {
				throw new TranscoderException("Could not build a bridge for the document at uri "
						+ Objects.toString(uri, "<unknown>"));
			}

			// dispatch an 'onload' event if needed
			if (ctx.isDynamic()) {
				BaseScriptingEnvironment se;
				se = new BaseScriptingEnvironment(ctx);
				se.loadScripts();
				se.dispatchSVGLoadEvent();
				if (hints.containsKey(KEY_SNAPSHOT_TIME)) {
					float t = (Float) hints.get(KEY_SNAPSHOT_TIME);
					ctx.getAnimationEngine().setCurrentTime(t);
				} else if (ctx.isSVG12()) {
					float t = SVGUtilities.convertSnapshotTime(root, null);
					ctx.getAnimationEngine().setCurrentTime(t);
				}
			}
		} catch (BridgeException ex) {
			try {
				ctx.close();
			} catch (RuntimeException e) {
			}
			throw new TranscoderException(ex);
		}

		// get the 'width' and 'height' attributes of the SVG document
		float docWidth = (float) ctx.getDocumentSize().getWidth();
		float docHeight = (float) ctx.getDocumentSize().getHeight();

		setImageSize(docWidth, docHeight);

		// compute the preserveAspectRatio matrix
		AffineTransform Px;

		// take the AOI into account if any
		if (hints.containsKey(KEY_AOI)) {
			Rectangle2D aoi = (Rectangle2D) hints.get(KEY_AOI);
			// transform the AOI into the image's coordinate system
			Px = new AffineTransform();
			double sx = width / aoi.getWidth();
			double sy = height / aoi.getHeight();
			double scale = Math.min(sx, sy);
			Px.scale(scale, scale);
			double tx = -aoi.getX() + (width / scale - aoi.getWidth()) / 2;
			double ty = -aoi.getY() + (height / scale - aoi.getHeight()) / 2;
			Px.translate(tx, ty);
			// take the AOI transformation matrix into account
			// we apply first the preserveAspectRatio matrix
			setCurrentAOI(aoi);
		} else {
			String ref = new ParsedURL(uri).getRef();

			// XXX Update this to use the animated value of 'viewBox' and
			// 'preserveAspectRatio'.
			String viewBox = root.getAttributeNS(null, SVGConstants.SVG_VIEW_BOX_ATTRIBUTE).trim();

			if (ref != null && !ref.isEmpty()) {
				Px = ViewBox.getViewTransform(ref, root, width, height, ctx);
			} else if (!viewBox.isEmpty()) {
				String aspectRatio = root.getAttributeNS(null, SVGConstants.SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE);
				Px = ViewBox.getPreserveAspectRatioTransform(root, viewBox, aspectRatio, width, height, ctx);
				// check for null, which means zero width or height
				if (Px == null) {
					Px = AffineTransform.getTranslateInstance(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
				}
			} else {
				// no viewBox has been specified, create a scale transform
				float xscale, yscale;
				xscale = width / docWidth;
				yscale = height / docHeight;
				float scale = Math.min(xscale, yscale);
				Px = AffineTransform.getScaleInstance(scale, scale);
			}

			setCurrentAOI(new Rectangle2D.Float(0, 0, width, height));
		}

		CanvasGraphicsNode cgn = getCanvasGraphicsNode(gvtRoot);
		if (cgn != null) {
			cgn.setViewingTransform(Px);
			setCurrentAOITransform(new AffineTransform());
		} else {
			setCurrentAOITransform(Px);
		}

		this.root = gvtRoot;
	}

	/**
	 * Import the given document with a SVG DOM implementation.
	 * 
	 * @param document the document to import.
	 * @param uri      the document URI.
	 * @return the imported SVG document, or {@code null} if no SVG tree could be
	 *         found.
	 * @throws TranscoderException if an error occurred while importing the SVG.
	 */
	private SVGOMDocument importAsSVGDocument(Document document, String uri)
			throws TranscoderException {
		// Obtain the document element and DocumentType
		Element docElm = document.getDocumentElement();
		// Check whether the document element is a SVG element anyway
		if (!SVGConstants.SVG_NAMESPACE_URI.equals(docElm.getNamespaceURI())
				&& !"svg".equalsIgnoreCase(docElm.getTagName())) {
			// Not a SVG document, either locate KEY_SVG_SELECTOR
			// or get the first SVG element
			String selector = (String) hints.get(KEY_SVG_SELECTOR);
			if (selector != null && !(selector = selector.trim()).isEmpty()) {
				try {
					docElm = ((AbstractParentNode) docElm).querySelector(selector);
				} catch (DOMException e) {
					throw new TranscoderException("Invalid selector: " + selector + '.', e);
				}
				if (docElm == null || (!"svg".equals(docElm.getLocalName())
						&& !"svg".equals(docElm.getTagName()))) {
					// No SVG element with that selector
					throw new TranscoderException(
							"Selector " + selector + " points to no valid SVG element.");
				}
				String namespaceURI = docElm.getNamespaceURI();
				if (namespaceURI == null) {
					// Set the right namespace
					docElm = replaceSVGRoot(docElm);
				} else if (!SVGConstants.SVG_NAMESPACE_URI.equals(namespaceURI)) {
					throw new TranscoderException("Selector " + selector + " points to element in "
							+ namespaceURI + " namespace.");
				}
			} else {
				docElm = (Element) document.getElementsByTagNameNS(SVGConstants.SVG_NAMESPACE_URI,
						SVGConstants.SVG_SVG_TAG).item(0);
				if (docElm == null) {
					// If we are we in XHTML instead of plain HTML, we are done
					if (document.getDocumentElement().getNamespaceURI() != null) {
						return null;
					}
					// Check for namespaceless <svg> (acceptable inside plain HTML)
					docElm = (Element) document.getElementsByTagName(SVGConstants.SVG_SVG_TAG)
							.item(0);
					if (docElm == null) {
						// No SVG elements at all
						return null;
					}
					// Set the right namespace
					docElm = replaceSVGRoot(docElm);
				} else {
					// We are in CSS context
					SizingHelper.defaultDimensions(docElm);
				}
			}
		}

		// Obtain a DOM implementation
		DOMImplementation impl;
		impl = (DOMImplementation) hints.get(KEY_DOM_IMPLEMENTATION);
		if (impl == null) {
			// Look for the version attribute of the root element
			if ("1.2".equals(docElm.getAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE))) {
				impl = SVG12DOMImplementation.getDOMImplementation();
			} else {
				impl = SVGDOMImplementation.getDOMImplementation();
			}
		}

		// Clone the document
		SVGOMDocument svgDocument = (SVGOMDocument) deepCloneDocument(document, docElm, impl);

		if (uri != null) {
			ParsedURL url = new ParsedURL(uri);
			svgDocument.setParsedURL(url);
		}

		return svgDocument;
	}

	private Element replaceSVGRoot(Element docElm) {
		docElm.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
				XMLConstants.XMLNS_ATTRIBUTE, SVGConstants.SVG_NAMESPACE_URI);
		Element newRoot = replaceNamespace(docElm);
		docElm.getParentNode().replaceChild(newRoot, docElm);

		// We are in CSS context and need to apply some rules
		// see https://svgwg.org/specs/integration/#svg-css-sizing
		SizingHelper.defaultDimensions(newRoot);

		return newRoot;
	}

	private Element replaceNamespace(Element elm) {
		Element svgElm;
		if (elm.getNamespaceURI() == null) {
			svgElm = elm.getOwnerDocument().createElementNS(SVGConstants.SVG_NAMESPACE_URI,
					elm.getTagName());
			if (elm.hasAttributes()) {
				NamedNodeMap attrMap = elm.getAttributes();
				for (int i = 0; i < attrMap.getLength(); i++) {
					Node attr = attrMap.item(i);
					svgElm.setAttributeNode((Attr) attr.cloneNode(true));
				}
			}
			if (elm.hasChildNodes()) {
				NodeList nodeList = elm.getChildNodes();
				Node node;
				int i = 0;
				while ((node = nodeList.item(i)) != null) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element newNode = replaceNamespace((Element) node);
						if (newNode != node) {
							// node won't be removed from list
							i++;
							node = newNode;
						}
					}
					svgElm.appendChild(node);
				}
			}
		} else {
			// Preserve this element but check child elements
			svgElm = elm;
			if (elm.hasChildNodes()) {
				NodeList nodeList = elm.getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element newNode = replaceNamespace((Element) node);
						if (newNode != node) {
							elm.replaceChild(newNode, node);
						}
					}
				}
			}
		}
		return svgElm;
	}

	/**
	 * Deep clone the document {@code doc} with the {@code impl} DOM implementation,
	 * importing {@code root} as the document element.
	 * 
	 * @param doc  the document to be imported.
	 * @param root the element to be imported as document element.
	 * @param impl the DOM implementation.
	 * 
	 * @return the cloned document.
	 */
	private static Document deepCloneDocument(Document doc, Element root, DOMImplementation impl) {
		Document result = impl.createDocument(root.getNamespaceURI(), root.getNodeName(), null);
		Element rroot = result.getDocumentElement();

		// Let's see if the designed root node is also the origin's document root
		boolean before = root.getParentNode().getNodeType() == Node.DOCUMENT_NODE;
		Node firstNode;
		if (before) {
			firstNode = doc.getFirstChild();
		} else {
			firstNode = root;
			// Now let's see if there are HEAD-level STYLE elements
			appendHeadStyleElements(doc, result, rroot);
		}

		for (Node n = firstNode; n != null; n = n.getNextSibling()) {
			if (n == root) {
				before = false;
				if (root.hasAttributes()) {
					NamedNodeMap attr = root.getAttributes();
					int len = attr.getLength();
					for (int i = 0; i < len; i++) {
						rroot.setAttributeNode((Attr) result.importNode(attr.item(i), true));
					}
				}
				for (Node c = root.getFirstChild(); c != null; c = c.getNextSibling()) {
					rroot.appendChild(result.importNode(c, true));
				}
			} else {
				short type = n.getNodeType();
				if (type == Node.DOCUMENT_TYPE_NODE || type == Node.COMMENT_NODE
						|| type == Node.PROCESSING_INSTRUCTION_NODE) {
					if (before) {
						result.insertBefore(result.importNode(n, true), rroot);
					} else {
						result.appendChild(result.importNode(n, true));
					}
				}
			}
		}

		return result;
	}

	/**
	 * Append to {@code rroot} any {@code STYLE} elements inside {@code doc}'s
	 * {@code HEAD} element.
	 * 
	 * @param doc    the document that is being imported.
	 * @param result the resulting SVG document.
	 * @param rroot  result's document element.
	 */
	private static void appendHeadStyleElements(Document doc, Document result, Element rroot) {
		Element docElm = doc.getDocumentElement();
		for (Node n = docElm.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if ("head".equals(n.getNodeName()) || "head".equals(n.getLocalName())) {
					Element head = (Element) n;
					NodeList styleList;
					if (docElm.getNamespaceURI() != null) {
						styleList = head.getElementsByTagNameNS("*", "style");
					} else {
						styleList = head.getElementsByTagName("style");
					}
					int len = styleList.getLength();
					for (int i = 0; i < len; i++) {
						Node style = styleList.item(i);
						// Let's import this element, but with a SVG namespace
						NamedNodeMap attr = style.getAttributes();
						String qname = "style";
						String pre = rroot.getPrefix();
						if (pre != null) {
							qname = pre + ':' + qname;
						}
						Element rstyle = result.createElementNS(SVGConstants.SVG_NAMESPACE_URI, qname);
						int attrLen = attr.getLength();
						for (int j = 0; j < attrLen; j++) {
							rstyle.setAttributeNode((Attr) result.importNode(attr.item(j), true));
						}
						rstyle.setTextContent(style.getTextContent());
						rroot.appendChild(rstyle);
					}
					break;
				}
			}
		}
	}

	protected CanvasGraphicsNode getCanvasGraphicsNode(GraphicsNode gn) {
		if (!(gn instanceof CompositeGraphicsNode))
			return null;
		CompositeGraphicsNode cgn = (CompositeGraphicsNode) gn;
		List<GraphicsNode> children = cgn.getChildren();
		if (children.isEmpty())
			return null;
		gn = children.get(0);
		if (!(gn instanceof CanvasGraphicsNode))
			return null;
		return (CanvasGraphicsNode) gn;
	}

	/**
	 * Factory method for constructing an configuring a BridgeContext so subclasses
	 * can insert new/modified bridges in the context.
	 * 
	 * @param doc the SVG document to create the BridgeContext for
	 * @return the newly instantiated BridgeContext
	 */
	protected BridgeContext createBridgeContext(SVGOMDocument doc) {
		return doc.isSVG12() ? createSVG12BridgeContext() : createBridgeContext();
	}

	/**
	 * Creates the default SVG BridgeContext.
	 * <p>
	 * Subclass this method to provide customized bridges.
	 * </p>
	 * 
	 * @return the newly instantiated BridgeContext
	 */
	protected BridgeContext createBridgeContext() {
		return new BridgeContext(userAgent);
	}

	/**
	 * Creates the BridgeContext according to the version attribute.
	 * <p>
	 * Please do not subclass this method to provide customized bridges. For
	 * example, Apache FOP uses Batik's equivalent of this method to register
	 * special bridges for optimized text painting.
	 * </p>
	 * 
	 * @param svgVersion the SVG version in use (ex. "1.0", "1.x" or "1.2")
	 * @return the newly instantiated BridgeContext
	 * @deprecated SVG no longer has a version attribute.
	 * @see #createBridgeContext()
	 * @see #createSVG12BridgeContext()
	 */
	@Deprecated
	protected final BridgeContext createBridgeContext(String svgVersion) {
		if ("1.2".equals(svgVersion)) {
			return createSVG12BridgeContext();
		} else {
			return createBridgeContext();
		}
	}

	protected BridgeContext createSVG12BridgeContext() {
		return new SVG12BridgeContext(userAgent);
	}

	/**
	 * Sets document size according to the hints. Global variables width and height
	 * are modified.
	 *
	 * @param docWidth  Width of the document.
	 * @param docHeight Height of the document.
	 */
	@Override
	protected void setImageSize(float docWidth, float docHeight) {
		// Compute the image's width and height according the hints
		float imgWidth = -1;
		if (hints.containsKey(KEY_WIDTH)) {
			imgWidth = (Float) hints.get(KEY_WIDTH);
		}
		float imgHeight = -1;
		if (hints.containsKey(KEY_HEIGHT)) {
			imgHeight = (Float) hints.get(KEY_HEIGHT);
		}

		if (imgWidth > 0 && imgHeight > 0) {
			width = imgWidth;
			height = imgHeight;
		} else if (imgHeight > 0) {
			width = (docWidth * imgHeight) / docHeight;
			height = imgHeight;
		} else if (imgWidth > 0) {
			width = imgWidth;
			height = (docHeight * imgWidth) / docWidth;
		} else {
			width = docWidth;
			height = docHeight;
		}

		// Limit image size according to the maximuxm size hints.
		float imgMaxWidth = -1;
		if (hints.containsKey(KEY_MAX_WIDTH)) {
			imgMaxWidth = (Float) hints.get(KEY_MAX_WIDTH);
		}
		float imgMaxHeight = -1;
		if (hints.containsKey(KEY_MAX_HEIGHT)) {
			imgMaxHeight = (Float) hints.get(KEY_MAX_HEIGHT);
		}

		if ((imgMaxHeight > 0) && (height > imgMaxHeight)) {
			width = (docWidth * imgMaxHeight) / docHeight;
			height = imgMaxHeight;
		}
		if ((imgMaxWidth > 0) && (width > imgMaxWidth)) {
			width = imgMaxWidth;
			height = (docHeight * imgMaxWidth) / docWidth;
		}
	}

	// --------------------------------------------------------------------
	// Keys definition
	// --------------------------------------------------------------------

	/**
	 * A user agent implementation for <code>PrintTranscoder</code>.
	 */
	protected class SVGAbstractTranscoderUserAgent extends UserAgentAdapter {
		/**
		 * List containing the allowed script types
		 */
		protected List<String> scripts;

		public SVGAbstractTranscoderUserAgent() {
			addStdFeatures();
		}

		/**
		 * Return the rendering transform.
		 */
		@Override
		public AffineTransform getTransform() {
			return getCurrentAOITransform();
		}

		/**
		 * Return the rendering transform.
		 */
		@Override
		public void setTransform(AffineTransform at) {
			setCurrentAOITransform(at);
		}

		/**
		 * Returns the default size of this user agent (400x400).
		 */
		@Override
		public Dimension2D getViewportSize() {
			return new Dimension((int) SVGAbstractTranscoder.this.width, (int) SVGAbstractTranscoder.this.height);
		}

		/**
		 * Displays the specified error message using the <code>ErrorHandler</code>.
		 */
		@Override
		public void displayError(String message) {
			try {
				SVGAbstractTranscoder.this.handler.error(new TranscoderException(message));
			} catch (TranscoderException ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}

		/**
		 * Displays the specified error using the <code>ErrorHandler</code>.
		 */
		@Override
		public void displayError(Exception e) {
			try {
				SVGAbstractTranscoder.this.handler.error(new TranscoderException(e));
			} catch (TranscoderException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Displays the specified message using the <code>ErrorHandler</code>.
		 */
		@Override
		public void displayMessage(String message) {
			try {
				SVGAbstractTranscoder.this.handler.warning(new TranscoderException(message));
			} catch (TranscoderException ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}

		/**
		 * Returns the pixel to millimeter conversion factor specified in the
		 * <code>TranscodingHints</code> or 0.26458333 if not specified.
		 */
		@Override
		public float getPixelUnitToMillimeter() {
			@SuppressWarnings("deprecation")
			Object obj = SVGAbstractTranscoder.this.hints.get(KEY_PIXEL_UNIT_TO_MILLIMETER);
			if (obj != null) {
				return (Float) obj;
			}

			obj = SVGAbstractTranscoder.this.hints.get(KEY_RESOLUTION_DPI);
			if (obj != null) {
				return 25.4f / ((Float) obj).floatValue();
			}

			return super.getPixelUnitToMillimeter();
		}

		/**
		 * Returns the resolution in dpi.
		 */
		@Override
		public float getResolution() {
			Float obj = (Float) SVGAbstractTranscoder.this.hints.get(KEY_RESOLUTION_DPI);
			if (obj != null) {
				return obj.floatValue();
			}

			@SuppressWarnings("deprecation")
			Float deprec = (Float) SVGAbstractTranscoder.this.hints.get(KEY_PIXEL_UNIT_TO_MILLIMETER);
			if (deprec != null) {
				return 25.4f / deprec.floatValue();
			}

			return 96f;
		}

		/**
		 * Returns the user language specified in the <code>TranscodingHints</code> or
		 * "en" (english) if any.
		 */
		@Override
		public String getLanguages() {
			if (SVGAbstractTranscoder.this.hints.containsKey(KEY_LANGUAGE)) {
				return (String) SVGAbstractTranscoder.this.hints.get(KEY_LANGUAGE);
			}

			return super.getLanguages();
		}

		/**
		 * Returns this user agent's CSS media.
		 */
		@Override
		public String getMedia() {
			String s = (String) hints.get(KEY_MEDIA);
			if (s != null)
				return s;

			return super.getMedia();
		}

		@Override
		public String getPrefersColorScheme() {
			String s = (String) hints.get(KEY_PREFERS_COLOR_SCHEME);
			if (s != null)
				return s;

			return super.getPrefersColorScheme();
		}

		/**
		 * Returns the default font family.
		 */
		@Override
		public String getDefaultFontFamily() {
			String s = (String) hints.get(KEY_DEFAULT_FONT_FAMILY);
			if (s != null) {
				return s;
			}

			return super.getDefaultFontFamily();
		}

		/**
		 * Returns this user agent's alternate style-sheet title.
		 */
		@Override
		public String getAlternateStyleSheet() {
			String s = (String) hints.get(KEY_ALTERNATE_STYLESHEET);
			if (s != null)
				return s;

			return super.getAlternateStyleSheet();
		}

		/**
		 * Returns the user stylesheet specified in the <code>TranscodingHints</code> or
		 * null if any.
		 */
		@Override
		public String getUserStyleSheetURI() {
			String s = (String) SVGAbstractTranscoder.this.hints.get(KEY_USER_STYLESHEET_URI);
			if (s != null)
				return s;

			return super.getUserStyleSheetURI();
		}

		/**
		 * Returns true if the XML parser must be in validation mode, false otherwise.
		 */
		@Override
		public boolean isXMLParserValidating() {
			Boolean b = (Boolean) SVGAbstractTranscoder.this.hints.get(KEY_XML_PARSER_VALIDATING);
			if (b != null)
				return b;

			return super.isXMLParserValidating();
		}

		/**
		 * Returns the security settings for the given script type, script url and
		 * document url
		 *
		 * @param scriptType type of script, as found in the type attribute of the
		 *                   &lt;script&gt; element.
		 * @param scriptPURL url for the script, as defined in the script's xlink:href
		 *                   attribute. If that attribute was empty, then this parameter
		 *                   should be null
		 * @param docPURL    url for the document into which the script was found.
		 */
		@Override
		public ScriptSecurity getScriptSecurity(String scriptType, ParsedURL scriptPURL, ParsedURL docPURL) {
			if (scripts == null) {
				computeAllowedScripts();
			}

			if (!scripts.contains(scriptType)) {
				return new NoLoadScriptSecurity(scriptType);
			}

			boolean constrainOrigin = true;

			if (SVGAbstractTranscoder.this.hints.containsKey(KEY_CONSTRAIN_SCRIPT_ORIGIN)) {
				constrainOrigin = (Boolean) SVGAbstractTranscoder.this.hints.get(KEY_CONSTRAIN_SCRIPT_ORIGIN);
			}

			if (constrainOrigin) {
				return new DefaultScriptSecurity(scriptType, scriptPURL, docPURL);
			} else {
				return new RelaxedScriptSecurity(scriptType, scriptPURL, docPURL);
			}
		}

		/**
		 * Helper method. Builds a Vector containing the allowed values for the
		 * &lt;script&gt; element's type attribute.
		 */
		protected void computeAllowedScripts() {
			if (!SVGAbstractTranscoder.this.hints.containsKey(KEY_ALLOWED_SCRIPT_TYPES)) {
				scripts = Collections.emptyList();
				return;
			}

			String allowedScripts = (String) SVGAbstractTranscoder.this.hints.get(KEY_ALLOWED_SCRIPT_TYPES);

			scripts = Arrays.asList(allowedScripts.split(", "));
		}

		@Override
		public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL resourceURL, ParsedURL docURL) {
			if (isAllowExternalResources()) {
				return new RelaxedExternalResourceSecurity(resourceURL, docURL);
			}
			return super.getExternalResourceSecurity(resourceURL, docURL);
		}

		public boolean isAllowExternalResources() {
			Boolean b = (Boolean) SVGAbstractTranscoder.this.hints.get(KEY_ALLOW_EXTERNAL_RESOURCES);
			if (b != null) {
				return b;
			}
			return false;
		}
	}
}
