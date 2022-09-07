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
package io.sf.carte.echosvg.transcoder.util;

import io.sf.carte.doc.agent.DeviceFactory;
import io.sf.carte.doc.dom.*;
import io.sf.carte.doc.style.css.*;
import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.om.AbstractCSSCanvas;
import io.sf.carte.doc.style.css.om.AbstractStyleDatabase;
import io.sf.carte.doc.style.css.om.ComputedCSSStyle;
import io.sf.carte.doc.style.css.om.RGBStyleFormattingFactory;
import io.sf.carte.doc.style.css.property.ColorIdentifiers;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.doc.xml.dtd.DefaultEntityResolver;
import io.sf.carte.echosvg.anim.dom.SVG12DOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.transcoder.ErrorHandler;
import io.sf.carte.echosvg.transcoder.*;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;
import io.sf.carte.echosvg.util.SVGConstants;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.*;

import static io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder.*;
import static java.lang.String.format;

/**
 * Utility for transcoding documents that use modern CSS, bypassing the EchoSVG
 * style computations.
 * 
 * <p>
 * To obtain the best results, your document should define style properties in a
 * style sheet or the {@code style} attribute, instead of using style-like
 * attributes like {@code font-size}.
 * </p>
 * <p>
 * For example it is preferable:
 * </p>
 * <code>
 * &lt;text style="font-size: 20px;"&gt;
 * </code>
 * <p>
 * to:
 * </p>
 * <code>
 * &lt;text font-size="20"&gt;
 * </code>
 * <h2>Supported CSS</h2>
 * <p>
 * Modern CSS is allowed, with most of the following specifications being
 * supported:
 * </p>
 * <ul>
 * <li>Selectors Level 4.</li>
 * <li>Values and Units Level 4 ({@code calc()}, viewport-based units).</li>
 * <li>Values and Units Level 5 (advanced {@code attr()}).</li>
 * <li>Color Level 4 ({@code color(display-p3 -0.61 1.09 -0.22)}).</li>
 * <li>Custom properties Level 1 ({@code var()}).</li>
 * <li>Properties and Values API Level 1 ({@code @property} rule).</li>
 * <li>Media Queries Level 4
 * ({@code @media screen and (400px <= width <= 700px)}).</li>
 * <li>Conditional Rules Level 3 ({@code @supports (color: lch(45% 30 60))}).
 * </li>
 * </ul>
 * <h3>Configuring for Media Queries</h3>
 * <p>
 * Media Queries use the SVG viewport dimensions by default, but you can set the
 * dimensions used by queries by setting the
 * {@link SVGAbstractTranscoder#KEY_WIDTH} and
 * {@link SVGAbstractTranscoder#KEY_HEIGHT} transcoding hints. And the target
 * medium ({@code screen}, {@code print}, etc.) can be set via the
 * {@link SVGAbstractTranscoder#KEY_MEDIA} hint.
 * </p>
 * <p>
 * For example:
 * </p>
 *
 * <pre>{@code
 * Transcoder transcoder = new PNGTranscoder();
 *
 * CSSTranscodingHelper helper = new CSSTranscodingHelper(transcoder);
 *
 * helper.getTranscoder().addTranscodingHint(SVGAbstractTranscoder.KEY_MEDIA, "screen");
 * helper.getTranscoder().addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, 450);
 * helper.getTranscoder().addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, 500);
 *
 * String uri = "https://www.example.com/my_image.svg";
 * java.io.Reader re = ... [SVG document reader]
 * java.io.OutputStream os = ... [where to write the image]
 *
 * TranscoderOutput dst = new TranscoderOutput(os);
 *
 * helper.transcode(re, uri, dst, null);
 * }</pre>
 * 
 * <h3>Dark Mode</h3>
 * <p>
 * Dark Mode can be enabled with {@link #setDarkMode(boolean)}.
 * </p>
 * 
 * <h2>Rendering SVG inside HTML</h2>
 * <p>
 * You can also render {@code <svg>} elements that are located inside an HTML
 * document. By default, the first {@code <svg>} element (in document order)
 * will be used, but you can point to a specific one using a CSS selector. For
 * example:
 * </p>
 * 
 * <pre>
 * helper.transcode(re, uri, dst, "#mySvg");
 * </pre>
 * 
 * <p>
 * And instead of the default XML parser (perfectly valid for XHTML) you can use
 * an HTML one. For example, to use the validator.nu HTML parser (one of the
 * parsers used by the Firefox browser):
 * </p>
 * 
 * <pre>
 * HtmlParser parser = new HtmlParser(XmlViolationPolicy.ALTER_INFOSET);
 * parser.setCommentPolicy(XmlViolationPolicy.ALLOW);
 * parser.setXmlnsPolicy(XmlViolationPolicy.ALLOW);
 * helper.setXMLReader(parser);
 * helper.setHTMLEmbedding(true);
 * </pre>
 * <p>
 * Note that calling {@link #setHTMLEmbedding(boolean)} is not required if the
 * document {@code uri} ends with {@code .html}.
 * </p>
 * 
 * <h2>Foreign Elements</h2>
 * <p>
 * The processing of foreign elements is performed via SVG 1.2 features.
 * Therefore, if a document contains foreign elements the {@code <svg>} element
 * should either not have a {@code version} attribute, or that attribute's value
 * must be {@code 1.2} or {@code 2}.
 * </p>
 */
public class CSSTranscodingHelper {

	/**
	 * Do not compute styles for the following elements
 	 */
	private static final Set<String> NO_STYLE = Set.of(
		"style", "script", "base", "link", "head", "title", "meta"
	);

	/**
	 * The transcoder.
	 */
	private final Transcoder transcoder;

	/**
	 * Dark mode toggle ({@code false} by default).
	 */
	private boolean darkMode;

	/**
	 * dark mode initial value for the CSS {@code color} property.
	 */
	private static final CSSTypedValue darkmodeInitialColor = (CSSTypedValue) new ValueFactory().parseProperty("#fff");

	/**
	 * Toggle for HTML processing ({@code false} by default).
	 */
	private boolean htmlEmbed;

	/**
	 * Optional {@code XMLReader} ({@code null} by default).
	 */
	private XMLReader xmlReader;

	/**
	 * Constructs a helper for transcoding to PNG.
	 */
	public CSSTranscodingHelper() {
		this(new PNGTranscoder());
	}

	/**
	 * Constructs a helper for transcoding with the given transcoder.
	 * 
	 * @param trans the transcoder.
	 */
	public CSSTranscodingHelper(Transcoder trans) {
		this.transcoder = trans;
	}

	/**
	 * Enables or disables dark mode.
	 * 
	 * @param darkMode if {@code true}, dark mode will be enabled.
	 */
	public void setDarkMode(boolean darkMode) {
		this.darkMode = darkMode;
	}

	/**
	 * Enables or disables the processing of HTML.
	 * <p>
	 * It is useful for svg images embedded in HTML documents. With HTML-specific
	 * processing, {@code <svg>} elements are automatically put in its own namespace
	 * (XHTML documents do this explicitly).
	 * </p>
	 * <p>
	 * If the document {@code uri} ends with {@code .html}, HTML processing is
	 * enabled automatically.
	 * </p>
	 * <p>
	 * To parse arbitrary HTML you may want to set an actual HTML parser with
	 * {@link #setXMLReader(XMLReader)}.
	 * </p>
	 * 
	 * @param htmlEmbed {@code true} to enable HTML processing.
	 */
	public void setHTMLEmbedding(boolean htmlEmbed) {
		this.htmlEmbed = htmlEmbed;
	}

	/**
	 * Get the transcoder.
	 * 
	 * @return the transcoder.
	 */
	public Transcoder getTranscoder() {
		return transcoder;
	}

	/**
	 * Set the {@code XMLReader} to be used when parsing.
	 * <p>
	 * If no {@code XMLReader} is set, one will be created by the
	 * {@code SAXParserFactory}.
	 * </p>
	 * <p>
	 * For example, to use the validator.nu HTML parser:
	 * </p>
	 * 
	 * <pre>
	 * HtmlParser parser = new HtmlParser(XmlViolationPolicy.ALTER_INFOSET);
	 * parser.setCommentPolicy(XmlViolationPolicy.ALLOW);
	 * parser.setXmlnsPolicy(XmlViolationPolicy.ALLOW);
	 * helper.setXMLReader(parser);
	 * </pre>
	 * 
	 * @param xmlReader the XMLReader.
	 */
	public void setXMLReader(XMLReader xmlReader) {
		this.xmlReader = xmlReader;
	}

	/**
	 * Transcode an SVG document styled with CSS 3 using the given transcoder.
	 * <p>
	 * This method attempts to convert advanced CSS into something that EchoSVG can
	 * understand. It may or may not succeed.
	 * </p>
	 * 
	 * @param reader      the {@code Reader} containing the character stream of the
	 *                    SVG document.
	 * @param documentURI the URI of the document. If the URI ends with
	 *                    {@code .html}, HTML processing is enabled.
	 * @param out         the stream to write the result.
	 * 
	 * @throws TranscoderException If an error occured while transcoding.
	 * @throws IOException         If any I/O error occurs.
	 */
	public void transcode(Reader reader, String documentURI, OutputStream out) throws TranscoderException, IOException {
		TranscoderOutput output = new TranscoderOutput(out);
		transcode(reader, documentURI, output, null);
	}

	/**
	 * Transcode an SVG document styled with CSS 3 using the given transcoder.
	 *
	 * <p>
	 * This method attempts to convert advanced CSS into something that EchoSVG
	 * can understand. It may or may not succeed.
	 * </p>
	 *
	 * @param reader      the {@code Reader} containing the character stream of
	 *                    the document that contains the SVG tree.
	 * @param documentURI the URI of the document. If the URI ends with a common
	 *                    HTML file name extension, HTML processing is enabled.
	 * @param output      the {@code TranscoderOutput} to write the result.
	 * @param selector    the selector to find the topmost {@code svg} element
	 *                    containing the subtree to be transcoded. If {@code
	 *                    null}, it is assumed that it is the document element
	 *                    (the whole document being an SVG document). If not
	 *                    {@code null}, HTML processing is used.
	 * @throws TranscoderException If an error occurred while transcoding.
	 */
	public void transcode(
		final Reader reader,
		final String documentURI,
		final TranscoderOutput output,
		final String selector)
			throws TranscoderException {
		final XMLDocumentBuilder builder = createXMLDocumentBuilder();
		final boolean isHtml = isHtml(documentURI);

		if (htmlEmbed || selector != null || isHtml) {
			// Prepare builder to process HTML
			builder.setHTMLProcessing(true);
		}

		// Parse
		final InputSource source = new InputSource(reader);
		source.setSystemId(documentURI);
		final DOMDocument document;

		try {
			document = (DOMDocument) builder.parse(source);
		} catch (final SAXException | IOException e) {
			throw new TranscoderException(e);
		}

		DocumentType docType = null;

		// Determine the SVG root element
		DOMElement svgRoot = null;
		String cssSelector = selector == null ? null : selector.trim();

		if (cssSelector != null && !cssSelector.isEmpty()) {
			svgRoot = document.querySelectorAll(cssSelector).item(0);
		}

		if (svgRoot == null) {
			svgRoot = document.getDocumentElement();
			docType = document.getDoctype();
		}

		// The svg root must be a SVG element
		if (!"svg".equals(svgRoot.getLocalName())) {
			docType = null;
			if (cssSelector == null) {
				svgRoot = document.getElementsByTagNameNS("*", "svg").item(0);
				if (svgRoot == null) {
					String msg = "Document contains no <svg> elements.";
					throw new TranscoderException(msg);
				}
			} else {
				// Before throwing the exception, sanitize possibly untrusted
				// selector by removing control characters ('Other, Control'
				// Unicode category).
				cssSelector = cssSelector.replaceAll("\\p{Cc}", "*CTRL*");
				final String msg = format("Element selected by '%s' is not a <svg>.", cssSelector);
				throw new TranscoderException(msg);
			}
		}

		final DOMImplementation svgImpl = createDOMImplementation(svgRoot, docType);

		// Create the DOCTYPE if appropriate, then the SVG document
		DocumentType svgDocType = null;
		if (docType != null) {
			svgDocType = svgImpl.createDocumentType(docType.getName(), docType.getPublicId(), docType.getSystemId());
		}
		final Document svgDoc = svgImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, null, svgDocType);
		svgDoc.setDocumentURI(documentURI);

		try {
			// Check for a target medium. In case a real medium isn't present, ensure
			// the rest of the machinery will work by providing a forced default.
			final String medium = getHintString(KEY_MEDIA, "medium");
			document.setTargetMedium(medium);
		} catch (final CSSMediaException ignored) {}

		// Set a DeviceFactory for the given medium
		final MyDeviceFactory deviceFactory = new MyDeviceFactory();
		deviceFactory.setCanvasDimensions(svgRoot);
		((CSSDOMImplementation)builder.getDOMImplementation()).setDeviceFactory(deviceFactory);

		// Check for an alternate style sheet
		final String alt = getHintString(KEY_ALTERNATE_STYLESHEET, null);

		// If there is an alternate style sheet, set it
		if (alt != null) {
			document.enableStyleSheetsForSet(alt);
		}

		// Fill the SVG document with computed styles.
		copyWithComputedStyles(svgRoot, svgDoc, svgDoc);
		transcode(svgDoc, output);
	}

	private DOMImplementation createDOMImplementation(
		final DOMElement svgRoot,
		final DocumentType docType) {
		final String version = svgRoot.getAttribute("version");

		// Assume 1.2 unless doctype Public ID is 1.0
		final boolean isSVG12 = version.isBlank()
			? docType == null || !"-//W3C//DTD SVG 1.0//EN".equals(docType.getPublicId())
			: version.indexOf('2') != -1;

		// Now create the SVG document from the SVG implementation
		return isSVG12
			? SVG12DOMImplementation.getDOMImplementation()
			: SVGDOMImplementation.getDOMImplementation();
	}

	/**
	 * Transcodes the given SVG document to the given {@link TranscoderOutput}.
	 *
	 * @param svg    The document to transcode.
	 * @param output The destination for the transcoded document.
	 * @throws TranscoderException Error transcoding the document.
	 */
	private void transcode(final Document svg, final TranscoderOutput output)
		throws TranscoderException {
		TranscoderInput input = new TranscoderInput(svg);
		ErrorHandler handler = new DefaultErrorHandler();
		transcoder.setErrorHandler(handler);
		transcoder.transcode(input, output);
	}

	/**
	 * Answers whether the given document URI represents an HTML document.
	 *
	 * @param uri The URI to check.
	 * @return {@code true} if the URI isn't null and the name ends with a
	 * well-known HTML extension.
	 */
	private boolean isHtml(final String uri) {
		return uri != null && (
				uri.endsWith(".htm") ||
				uri.endsWith(".html") ||
				uri.endsWith(".xhtml")
			);
	}

	/**
	 * Returns the value for a transcoding hint, or the given value if none
	 * found. Note: This functionality belongs in the {@link TranscodingHints}
	 * class. Note: We could also provide a lambda function that returns the
	 * default value, to provide a little extra flexibility.
	 *
	 * @param key          The transcoding hint's key to use for look up.
	 * @param defaultValue The value to return if the key has no associated
	 *                      value.
	 * @return The transcoding hint value for the given key, or the default
	 * value if none found.
	 */
	private String getHintString(
			final TranscodingHints.Key key,
			final String defaultValue) {
		final TranscodingHints hints = transcoder.getTranscodingHints();
		final String hint = (String) hints.get(key);
		final String result = hint == null ? defaultValue : hint.trim();

		return result.isEmpty() ? defaultValue : result;
	}

	/**
	 * Instantiates a DOM implementation that has modern CSS support.
	 *
	 * @return An instance of {@link XMLDocumentBuilder} that has modern CSS
	 * support, including entity resolution using {@link DefaultEntityResolver}.
	 */
	private XMLDocumentBuilder createXMLDocumentBuilder() {
		final CSSDOMImplementation impl = new CSSDOMImplementation();
		impl.setStyleFormattingFactory(new RGBStyleFormattingFactory());

		final XMLDocumentBuilder builder = new XMLDocumentBuilder(impl);
		builder.setEntityResolver(new DefaultEntityResolver());
		builder.getSAXParserFactory().setXIncludeAware(true);
		builder.setXMLReader(xmlReader);

		return builder;
	}

	private static void copyWithComputedStyles(DOMNode node, Document svgDoc, Node svgParent) {
		Node svgNode;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if ("foreignObject".equals(node.getLocalName())) {
				// <foreignObject> element
				DOMElement fo = (DOMElement) node;
				Element flowRoot = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "flowRoot");
				svgParent.appendChild(flowRoot);
				flowRoot.setAttributeNS("http://www.w3.org/XML/1998/namespace", "space", "preserve");
				replaceForeignSubtree(fo, svgDoc, flowRoot);
				return;
			} else {
				svgNode = importElement((DOMElement) node, svgDoc);
			}
		} else {
			svgNode = svgDoc.importNode(node, false);
		}

		svgParent.appendChild(svgNode);

		// Repeat the process for child nodes, if any
		if (node.hasChildNodes()) {
			for(final DOMNode n : node.getChildNodes()) {
				copyWithComputedStyles(n, svgDoc, svgNode);
			}
		}
	}

	/**
	 * Import an element to SVG document.
	 * 
	 * @param elm    the element to import.
	 * @param svgDoc the document to import to.
	 * @return the imported element.
	 */
	private static Element importElement(DOMElement elm, Document svgDoc) {
		Element svgElm;
		String name = elm.getLocalName();
		if (NO_STYLE.contains(name)) {
			// This element is imported but excluded from style
			// computations or replacement
			svgElm = (Element) svgDoc.importNode(elm, false);
		} else {
			svgElm = (Element) svgDoc.importNode(elm, false);
			fillStyleAttribute(elm, svgElm);
		}
		return svgElm;
	}

	private static void fillStyleAttribute(DOMElement elm, Element svgElm) {
		// Compute the style
		ComputedCSSStyle style = elm.getComputedStyle(null);

		// If we got any property, set the 'style' attribute
		if (style.getLength() != 0) {
			// Use getMinifiedCssText() instead of getCssText
			// because EchoSVG does not support some longhands decomposed
			// from shorthands like 'mask' or 'text-decoration'.
			svgElm.setAttribute("style", style.getMinifiedCssText());
		}
	}

	private static void replaceForeignSubtree(DOMElement fo, Document svgDoc, Element flowRoot) {
		if (fo.getChildElementCount() > 1) {
			// Won't handle this
			return;
		}

		// Make sure that we use SVG 1.2
		svgDoc.getDocumentElement().setAttribute("version", "1.2");

		final Element flowDiv = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "flowDiv");
		final Element flowRegion = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "flowRegion");
		flowRoot.appendChild(flowRegion);
		flowRoot.appendChild(flowDiv);

		final DOMElement elm = fo.getFirstElementChild();
		if (SVGConstants.SVG_NAMESPACE_URI.equals(elm.getNamespaceURI())) {
			// Do not replace this one
			final Element svgElm = importElement(elm, svgDoc);
			flowRoot.appendChild(svgElm);
		} else {
			// Compute the style
			final ComputedCSSStyle style = elm.getComputedStyle(null);

			// Only compute the box model for block elements (ignore non-blocks).
			if (isBlock(style)) {
				final BoxValues box = style.getBoxValues(CSSUnit.CSS_PX);

				if (box.getWidth() > 1e-4) {
					box.fillBoxValues(style);
				}

				final Element rect = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "rect");
				copyAttributes(fo, rect);
				rect.setAttribute("visibility", "hidden");
				flowRegion.appendChild(rect);
				rect.setAttribute("style", style.getMinifiedCssText());
				replaceElement(elm, svgDoc, flowDiv, flowDiv);
			}
		}
	}

	/**
	 * Note: This belongs in {@link ComputedCSSStyle} so that we can write
	 * {@code style.isBlock()}, which upholds encapsulation.
	 *
	 * @param style The style to check as a block or inline-block style
	 * @return {@code true} if the given style is a block style.
	 */
	private static boolean isBlock(final ComputedCSSStyle style) {
		final String display = style.getDisplay();
		return "block".equals(display) || "inline-block".equals(display);
	}

	private static void copyAttributes(final DOMElement fo, final Element rect) {
		for(final Attr attr : fo.getAttributes()) {
			rect.setAttributeNS(
				attr.getNamespaceURI(),
				attr.getName(),
				attr.getValue()
			);
		}
	}

	private static void replaceElement(
			final DOMElement elm,
			final Document svgDoc,
			final Element flowDiv,
			final Element svgParent) {
		final ComputedCSSStyle style = elm.getComputedStyle(null);
		final String elemName = isBlock(style) ? "flowPara" : "flowSpan";
		final Element flowElm = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, elemName);

		if (!style.isEmpty()) {
			flowElm.setAttribute("style", style.getMinifiedCssText());
		}
		svgParent.appendChild(flowElm);

		if (elm.getFirstElementChild() == null) {
			// No child elements
			String content = elm.getTextContent();
			final String preserve = elm.getAttributeNS("http://www.w3.org/XML/1998/namespace", "space");

			if (!"preserve".equalsIgnoreCase(preserve)) {
				content = content.trim();
			}

			if (content.isBlank()) {
				return;
			}

			final Text text = svgDoc.createTextNode(content);
			flowElm.appendChild(text);
		} else {
			final Iterator<DOMNode> it = elm.iterator();

			while (it.hasNext()) {
				final DOMNode node = it.next();

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					replaceElement((DOMElement) node, svgDoc, flowDiv, flowElm);
				} else {
					final Text text = svgDoc.createTextNode(node.getNodeValue());
					flowElm.appendChild(text);
				}
			}
		}
	}

	private class MyDeviceFactory implements DeviceFactory {

		private final MyStyleDatabase STYLE_DB = new MyStyleDatabase();

		/** Default height is 0. */
		private float height;
		/** Default width is 0. */
		private float width;

		@Override
		public StyleDatabase getStyleDatabase(final String medium) {
			return STYLE_DB;
		}

		/**
		 * Set some style database information using the transcoding hints and the
		 * SVG root element. If there are neither transcoding hints or dimensions
		 * specified by the SVG document, this will assume an A4-sized canvas.
		 *
		 * @param root the SVG root element.
		 */
		void setCanvasDimensions(final DOMElement root) {
			this.width = parseFloatHint(root, "width", KEY_WIDTH, 595f);
			this.height = parseFloatHint(root, "height", KEY_HEIGHT, 842f);
		}

		/**
		 * Returns the value associated with a transcoding hint key, or the given
		 * default value if the associated value is not found.
		 *
		 * @param root          The SVG document root element.
		 * @param attributeName The SVG attribute value to use if the hint key
		 *                      is not found.
		 * @param key           The transcoding hint key to look up.
		 * @param defaultValue  The value to return if the value associated with
		 *                      the key is out of bounds or missing.
		 * @return The default value if the value associated with the key is
		 * less than or equal to zero.
		 */
		private float parseFloatHint(
			final DOMElement root,
			final String attributeName,
			final TranscodingHints.Key key,
			final float defaultValue) {
			assert defaultValue > 0;

			final TranscodingHints hints = getTranscoder().getTranscodingHints();
			final Object keyValue = hints.get(key);
			float result = keyValue instanceof Float ? (Float) keyValue : 0;

			if (keyValue == null) {
				try {
					final String attributeValue = root.getAttribute(attributeName);
					result = Float.parseFloat(attributeValue);
				} catch (final NumberFormatException ignored) {
				}
			}

			// Ensure that the resulting value is always greater than zero.
			return result <= 0 ? defaultValue : result;
		}

		@Override
		public CSSCanvas createCanvas(final String medium, final CSSDocument doc) {
			return new MyCanvas(doc.getOwnerDocument());
		}

		private class MyStyleDatabase extends AbstractStyleDatabase {

			private static final long serialVersionUID = 1L;

			private final List<String> FONTS = getAvailableFontList();

			private List<String> getAvailableFontList() {
				return Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
			}

			@Override
			public String getDefaultGenericFontFamily(String genericFamily) {
				return genericFamily;
			}

			@Override
			public boolean isFontFaceName(String requestedFamily) {
				return false;
			}

			@Override
			public int getColorDepth() {
				// We do not have the actual Graphics2D here yet, but we try
				final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
				final GraphicsConfiguration conf = env.getDefaultScreenDevice().getDefaultConfiguration();
				int bpc = 255;

				if (conf != null) {
					final int[] comp = conf.getColorModel().getComponentSize();

					for (int i = 0; i < 3; i++) {
						if (bpc > comp[i]) {
							bpc = comp[i];
						}
					}
				}

				return bpc;
			}

			@Override
			public float getDeviceHeight() {
				return height;
			}

			@Override
			public float getDeviceWidth() {
				return width;
			}

			@Override
			protected boolean isFontFamilyAvailable(final String fontFamily) {
				return FONTS.contains(fontFamily);
			}

			@Override
			public CSSTypedValue getInitialColor() {
				return darkMode ? darkmodeInitialColor : super.getInitialColor();
			}

			@Override
			public boolean supports(final String property, final CSSValue value) {
				if ("color".equalsIgnoreCase(property) || "background-color".equalsIgnoreCase(property)) {
					return supportsColor(value);
				}

				return value.getCssValueType() == CssType.TYPED && isSupportedType((CSSTypedValue) value);
			}

			private boolean supportsColor(final CSSValue value) {
				switch (value.getPrimitiveType()) {
					case COLOR:
						return true;
					case IDENT:
						final String text = value.getCssText();
						return ColorIdentifiers
							.getInstance()
							.isColorIdentifier(text.toLowerCase(Locale.ROOT));
					default:
						return false;
				}
			}

			private boolean isSupportedType(final CSSTypedValue value) {
				final short unit = value.getUnitType();

				return CSSUnit.isLengthUnitType(unit) && !CSSUnit.isRelativeLengthUnitType(unit)
						|| CSSUnit.isAngleUnitType(unit) || CSSUnit.isTimeUnitType(unit);
			}
		}

		class MyCanvas extends AbstractCSSCanvas {

			protected MyCanvas(final CSSDocument doc) {
				super(doc);
			}

			@Override
			public StyleDatabase getStyleDatabase() {
				return STYLE_DB;
			}

			@Override
			protected String getOverflowBlock() {
				return "none";
			}

			@Override
			protected String getOverflowInline() {
				return "none";
			}

			@Override
			protected String getPointerAccuracy() {
				return "none";
			}

			/**
			 * The desire for light or dark color schemes.
			 * 
			 * @return the {@code prefers-color-scheme} feature
			 */
			@Override
			protected String getPrefersColorScheme() {
				return darkMode ? "dark" : "light";
			}

			@Override
			protected float getResolution() {
				return Float.POSITIVE_INFINITY;
			}
		}
	}
}
