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

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import io.sf.carte.doc.agent.DeviceFactory;
import io.sf.carte.doc.dom.CSSDOMImplementation;
import io.sf.carte.doc.dom.DOMDocument;
import io.sf.carte.doc.dom.DOMElement;
import io.sf.carte.doc.dom.DOMNode;
import io.sf.carte.doc.dom.XMLDocumentBuilder;
import io.sf.carte.doc.style.css.BoxValues;
import io.sf.carte.doc.style.css.CSSCanvas;
import io.sf.carte.doc.style.css.CSSComputedProperties;
import io.sf.carte.doc.style.css.CSSDocument;
import io.sf.carte.doc.style.css.CSSMediaException;
import io.sf.carte.doc.style.css.CSSStyleSheetFactory;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSUnit;
import io.sf.carte.doc.style.css.CSSValue;
import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.StyleDatabase;
import io.sf.carte.doc.style.css.awt.AWTHelper;
import io.sf.carte.doc.style.css.nsac.ArgumentCondition;
import io.sf.carte.doc.style.css.nsac.CombinatorCondition;
import io.sf.carte.doc.style.css.nsac.CombinatorSelector;
import io.sf.carte.doc.style.css.nsac.Condition;
import io.sf.carte.doc.style.css.nsac.ConditionalSelector;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.Selector;
import io.sf.carte.doc.style.css.nsac.SelectorList;
import io.sf.carte.doc.style.css.om.AbstractCSSCanvas;
import io.sf.carte.doc.style.css.om.AbstractStyleDatabase;
import io.sf.carte.doc.style.css.om.ComputedCSSStyle;
import io.sf.carte.doc.style.css.om.RGBStyleFormattingFactory;
import io.sf.carte.doc.style.css.property.ColorIdentifiers;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.doc.xml.dtd.DefaultEntityResolver;
import io.sf.carte.echosvg.anim.dom.SVG12DOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.transcoder.DefaultErrorHandler;
import io.sf.carte.echosvg.transcoder.ErrorHandler;
import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.Transcoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;
import io.sf.carte.echosvg.transcoder.impl.SizingHelper;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVGConstants;
import io.sf.carte.util.agent.AgentUtil;

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
 * <pre>
   Transcoder transcoder = new PNGTranscoder();

   CSSTranscodingHelper helper = new CSSTranscodingHelper(transcoder);

   helper.getTranscoder().addTranscodingHint(SVGAbstractTranscoder.KEY_MEDIA, "screen");
   helper.getTranscoder().addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, 450);
   helper.getTranscoder().addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, 500);

   String uri = "https://www.example.com/my_image.svg";
   java.io.Reader re = ... [SVG document reader]
   java.io.OutputStream os = ... [where to write the image]

   TranscoderOutput dst = new TranscoderOutput(os);

   helper.transcode(re, uri, dst, null);
 * </pre>
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

	private static final Set<String> noStyle;

	static {
		// Do not compute styles for the following elements
		final String[] noStArray = { "style", "script", "base", "link", "head", "title", "meta" };

		noStyle = new HashSet<>(noStArray.length);
		Collections.addAll(noStyle, noStArray);
	}

	/**
	 * The transcoder.
	 */
	private final Transcoder transcoder;

	/**
	 * dark mode toggle.
	 */
	private boolean darkMode = false;

	/**
	 * dark mode initial value for the CSS {@code color} property.
	 */
	private static final CSSTypedValue darkmodeInitialColor = (CSSTypedValue) new ValueFactory()
			.parseProperty("#fff");

	/**
	 * Toggle for HTML processing.
	 */
	private boolean htmlEmbed = false;

	/**
	 * Optional {@code XMLReader}.
	 */
	private XMLReader xmlReader = null;

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
	 * Transcode a SVG document styled with CSS 3 using the given transcoder.
	 * 
	 * <p>
	 * This method attempts to convert advanced CSS into something that EchoSVG can
	 * understand. It may or may not succeed.
	 * </p>
	 * <p>
	 * If the {@code input} contains a {@code Reader} or a stream, it is closed. The
	 * reason is that this method was created to address a specific use case where
	 * it is called from a method that does not know what the input object has, and
	 * that does other things before returning. But that use case was a bad idea so
	 * this method is now deprecated.
	 * </p>
	 * 
	 * @param input    the transcoder input document. If its {@code URI} (or the
	 *                 {@code documentURI} of the document) ends with {@code .html},
	 *                 HTML processing is enabled.
	 * @param output   the {@code TranscoderOutput} to write the result.
	 * @param selector the selector to find the topmost {@code svg} element
	 *                 containing the subtree to be transcoded. If {@code null}, it
	 *                 is assumed that it is the document element (the whole
	 *                 document being a SVG document). If not {@code null}, HTML
	 *                 processing is used.
	 * 
	 * @throws TranscoderException      If an error occured while transcoding.
	 * @throws IOException              If any I/O error occurs.
	 * @throws NullPointerException     If {@code input} is {@code null}.
	 * @throws IllegalArgumentException If the {@code input} contains no input
	 *                                  sources.
	 * @deprecated use {@link #transcode(TranscoderInput, TranscoderOutput)}
	 */
	@Deprecated
	public void transcode(TranscoderInput input, TranscoderOutput output, String selector)
			throws TranscoderException, IOException {
		XMLReader oldXmlReader = xmlReader;

		XMLReader inpXmlReader = input.getXMLReader();
		if (inpXmlReader != null) {
			xmlReader = input.getXMLReader();
		}

		try {
			Document doc = input.getDocument();
			if (doc != null) {
				if (doc.getDocumentURI() == null) {
					doc.setDocumentURI(input.getURI());
				}
				transcodeDocument(doc, output, selector);
				return;
			}
		} finally {
			if (inpXmlReader != null) {
				xmlReader = oldXmlReader;
			}
		}

		final Reader reader = input.getReader();
		try {
			if (reader != null) {
				transcode(reader, input.getURI(), output, selector);
				return;
			}
		} finally {
			if (inpXmlReader != null) {
				xmlReader = oldXmlReader;
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}

		InputStream is = input.getInputStream();
		try {
			if (is != null) {
				InputStreamReader re;
				if (input.getEncoding() != null) {
					re = new InputStreamReader(is, input.getEncoding());
				} else {
					re = new InputStreamReader(is);
				}
				transcode(re, input.getURI(), output, selector);
				return;
			}
		} finally {
			if (inpXmlReader != null) {
				xmlReader = oldXmlReader;
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

		try {
			if (input.getURI() == null) {
				throw new IllegalArgumentException("No inputs found.");
			}
			transcode(null, input.getURI(), output, selector);
		} finally {
			if (inpXmlReader != null) {
				xmlReader = oldXmlReader;
			}
		}
	}

	/**
	 * Transcode a SVG document styled with CSS 3 using the given transcoder.
	 * 
	 * <p>
	 * This method attempts to convert advanced CSS into something that EchoSVG can
	 * understand. It may or may not succeed.
	 * </p>
	 * <p>
	 * Streams inside {@code input} or {@code output} aren't closed.
	 * </p>
	 * 
	 * @param input  the transcoder input document. If its {@code URI} (or the
	 *               {@code documentURI} of the document) ends with {@code .html},
	 *               HTML processing is enabled.
	 * @param output the {@code TranscoderOutput} to write the result.
	 * 
	 * @throws TranscoderException      If an error occured while transcoding.
	 * @throws IOException              If any I/O error occurs.
	 * @throws NullPointerException     If {@code input} is {@code null}.
	 * @throws IllegalArgumentException If the {@code input} contains no input
	 *                                  sources.
	 */
	public void transcode(TranscoderInput input, TranscoderOutput output)
			throws TranscoderException, IOException {
		XMLReader oldXmlReader = xmlReader;

		XMLReader inpXmlReader = input.getXMLReader();
		if (inpXmlReader != null) {
			xmlReader = input.getXMLReader();
		}

		try {
			Document doc = input.getDocument();
			if (doc != null) {
				if (doc.getDocumentURI() == null) {
					doc.setDocumentURI(input.getURI());
				}
				transcodeDocument(doc, output, null);
				return;
			}

			final Reader reader = input.getReader();
			if (reader != null) {
				transcode(reader, input.getURI(), output, null);
				return;
			}

			InputStream is = input.getInputStream();
			if (is != null) {
				InputStreamReader re;
				if (input.getEncoding() != null) {
					re = new InputStreamReader(is, input.getEncoding());
				} else {
					re = new InputStreamReader(is);
				}
				transcode(re, input.getURI(), output, null);
				return;
			}
			if (input.getURI() == null) {
				throw new IllegalArgumentException("No inputs found.");
			}
			transcode(null, input.getURI(), output, null);
		} finally {
			if (inpXmlReader != null) {
				xmlReader = oldXmlReader;
			}
		}
	}

	/**
	 * Transcode a SVG document styled with CSS 3 using the given transcoder.
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
	public void transcode(Reader reader, String documentURI, OutputStream out)
			throws TranscoderException, IOException {
		TranscoderOutput output = new TranscoderOutput(out);
		transcode(reader, documentURI, output, null);
	}

	/**
	 * Transcode a SVG document styled with CSS 3 using the given transcoder.
	 * 
	 * <p>
	 * This method attempts to convert advanced CSS into something that EchoSVG can
	 * understand. It may or may not succeed.
	 * </p>
	 * 
	 * @param reader      the {@code Reader} containing the character stream of the
	 *                    document that contains the SVG tree. If {@code null}, the
	 *                    document shall be read from the {@code documentURI}.
	 * @param documentURI the URI of the document. If the URI ends with
	 *                    {@code .html}, HTML processing is enabled.
	 * @param output      the {@code TranscoderOutput} to write the result.
	 * @param selector    the selector to find the topmost {@code svg} element
	 *                    containing the subtree to be transcoded. If {@code null},
	 *                    it is assumed that it is the document element (the whole
	 *                    document being a SVG document). If not {@code null}, HTML
	 *                    processing is used.
	 * 
	 * @throws TranscoderException If an error occured while transcoding.
	 * @throws IOException         If any I/O error occurs.
	 */
	public void transcode(Reader reader, String documentURI, TranscoderOutput output,
			String selector) throws TranscoderException, IOException {
		// Obtain a DOM implementation with modern CSS support
		CSSDOMImplementation impl = createCSSDOMImplementation();

		// The DocumentBuilder
		XMLDocumentBuilder builder = new XMLDocumentBuilder(impl);
		builder.setEntityResolver(new DefaultEntityResolver());
		builder.getSAXParserFactory().setXIncludeAware(true);
		builder.setXMLReader(xmlReader);

		if (htmlEmbed || selector != null
				|| (documentURI != null && documentURI.endsWith(".html"))) {
			// Prepare builder to process HTML
			builder.setHTMLProcessing(true);
		}

		// Parse
		InputSource source = new InputSource(reader);
		source.setSystemId(documentURI);
		DOMDocument document;
		try {
			document = (DOMDocument) builder.parse(source);
		} catch (SAXException e) {
			throw new TranscoderException(e);
		}

		transcodeDOMDocument(document, output, selector);
	}

	private CSSDOMImplementation createCSSDOMImplementation() {
		// Instantiate a DOM implementation that has modern CSS support
		CSSDOMImplementation impl = new CSSDOMImplementation();
		// Serialize colors to sRGB
		impl.setStyleFormattingFactory(new RGBStyleFormattingFactory());
		return impl;
	}

	/**
	 * Transcode a SVG document styled with CSS 3 using the given transcoder.
	 * 
	 * <p>
	 * This method attempts to convert advanced CSS into something that EchoSVG can
	 * understand. It may or may not succeed.
	 * </p>
	 * 
	 * @param document the input document. If its {@code documentURI} ends with
	 *                 {@code .html}, HTML processing is enabled.
	 * @param output   the {@code TranscoderOutput} to write the result.
	 * @param selector the selector to find the topmost {@code svg} element
	 *                 containing the subtree to be transcoded. If {@code null}, it
	 *                 is assumed that it is the document element (the whole
	 *                 document being a SVG document). If not {@code null}, HTML
	 *                 processing is used.
	 * 
	 * @throws TranscoderException      If an error occured while transcoding.
	 * @throws IOException              If any I/O error occurs.
	 * @throws NullPointerException     If {@code document} is {@code null}.
	 * @throws IllegalArgumentException If the document contains no elements.
	 */
	public void transcodeDocument(Document document, TranscoderOutput output, String selector)
			throws TranscoderException, IOException {
		DOMDocument cssDocument;

		if (document instanceof DOMDocument) {
			cssDocument = (DOMDocument) document;
		} else {
			// Get the document element
			Element inputDocElm = document.getDocumentElement();
			if (inputDocElm == null) {
				throw new IllegalArgumentException("The document has no document element.");
			}

			DocumentType inputDocType = document.getDoctype();
			// Obtain a DOM implementation with modern CSS support
			CSSDOMImplementation impl = createCSSDOMImplementation();

			// Obtain the new DocType, if necessary
			DocumentType docType = null;
			String docTypeName;
			if (inputDocType != null && (docTypeName = inputDocType.getName()) != null) {
				docType = impl.createDocumentType(docTypeName, inputDocType.getPublicId(),
						inputDocType.getSystemId());
			}

			// Instantiate a document of the correct type
			if (isHTMLDocument(inputDocElm, inputDocType)) {
				cssDocument = impl.createDocument(null, null, docType);
			} else {
				cssDocument = impl.createDocument("", null, docType);
			}

			// Set the documentURI
			cssDocument.setDocumentURI(document.getDocumentURI());

			// Import and append the document element
			Node docElm = cssDocument.importNode(inputDocElm, true);
			cssDocument.appendChild(docElm);
		}

		transcodeDOMDocument(cssDocument, output, selector);
	}

	/**
	 * Determine whether the document is HTML.
	 * 
	 * @param docElm  the document element to check.
	 * @param docType the document type of the document to check.
	 * @return {@code true} if the document is HTML or XHTML.
	 */
	private boolean isHTMLDocument(Element docElm, DocumentType docType) {
		String tagName = docElm.getTagName();
		return "html".equalsIgnoreCase(tagName) || "html".equalsIgnoreCase(docElm.getLocalName())
				|| (docType != null && "html".equalsIgnoreCase(docType.getName()));
	}

	/**
	 * Transcode a SVG document styled with CSS 3 using the given transcoder.
	 * 
	 * <p>
	 * This method attempts to convert advanced CSS into something that EchoSVG can
	 * understand. It may or may not succeed.
	 * </p>
	 * 
	 * @param document the CSS-enabled input document. If its {@code documentURI}
	 *                 ends with {@code .html}, HTML processing is enabled.
	 * @param output   the {@code TranscoderOutput} to write the result.
	 * @param selector the selector to find the topmost {@code svg} element
	 *                 containing the subtree to be transcoded. If {@code null}, it
	 *                 is assumed that it is the document element (the whole
	 *                 document being a SVG document). If not {@code null}, HTML
	 *                 processing is used.
	 * 
	 * @throws TranscoderException If an error occured while transcoding.
	 * @throws IOException         If any I/O error occurs.
	 */
	private void transcodeDOMDocument(DOMDocument document, TranscoderOutput output,
			String selector) throws TranscoderException, IOException {
		DocumentType docType = null;

		// Determine the SVG root element
		DOMElement svgRoot = null;
		if (selector == null) {
			selector = (String) transcoder.getTranscodingHints()
					.get(SVGAbstractTranscoder.KEY_SVG_SELECTOR);
		}
		if (selector != null && (selector = selector.trim()).length() != 0) {
			svgRoot = document.querySelector(selector);
		}
		if (svgRoot == null) {
			svgRoot = document.getDocumentElement();
			docType = document.getDoctype();
		}

		// The svg root must be a SVG element
		if (!SVGConstants.SVG_SVG_TAG.equals(svgRoot.getLocalName())) {
			docType = null;
			if (selector == null) {
				svgRoot = document.getElementsByTagNameNS("*", "svg").item(0);
				if (svgRoot == null) {
					String msg = "Document contains no <svg> elements.";
					throw new TranscoderException(msg);
				}
			} else {
				// Before throwing the exception, sanitize possibly untrusted
				// selector by removing control characters ('Other, Control'
				// Unicode category).
				selector = selector.replaceAll("\\p{Cc}", "*CTRL*");
				String msg = "Element selected by '" + selector + "' is not a <svg>.";
				throw new TranscoderException(msg);
			}
		}

		// We are in CSS context, need to apply some rules
		// see https://svgwg.org/specs/integration/#svg-css-sizing
		SizingHelper.defaultDimensions(svgRoot);

		boolean isSVG12;
		String version = svgRoot.getAttribute("version");
		if (version.length() == 0) {
			// Assume 1.2 unless doctype Public ID is 1.0
			isSVG12 = docType == null || !"-//W3C//DTD SVG 1.0//EN".equals(docType.getPublicId());
		} else {
			isSVG12 = version.indexOf('2') != -1;
		}

		// Now create the SVG document from the SVG implementation
		org.w3c.dom.DOMImplementation svgImpl;
		if (!isSVG12) {
			svgImpl = SVGDOMImplementation.getDOMImplementation();
		} else {
			svgImpl = SVG12DOMImplementation.getDOMImplementation();
		}

		// Create the DOCTYPE if appropriate, then the SVG document
		DocumentType svgDocType = null;
		if (docType != null) {
			svgDocType = svgImpl.createDocumentType(docType.getName(), docType.getPublicId(),
					docType.getSystemId());
		}
		org.w3c.dom.Document svgDoc = svgImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, null,
				svgDocType);
		svgDoc.setDocumentURI(document.getDocumentURI());

		// Check for an alternate style sheet
		String alt = (String) transcoder.getTranscodingHints()
				.get(SVGAbstractTranscoder.KEY_ALTERNATE_STYLESHEET);

		// Check for user style sheet
		String userSheetUri = (String) transcoder.getTranscodingHints()
				.get(SVGAbstractTranscoder.KEY_USER_STYLESHEET_URI);

		// Check for a target medium
		String medium = (String) transcoder.getTranscodingHints()
				.get(SVGAbstractTranscoder.KEY_MEDIA);
		if (medium == null || (medium = medium.trim()).length() == 0) {
			// This won't match a real medium but the rest of the machinery will work
			medium = "medium";
		}
		try {
			document.setTargetMedium(medium);
		} catch (CSSMediaException e) {
		}

		// Set a DeviceFactory for the given medium
		MyDeviceFactory devFactory = new MyDeviceFactory();
		devFactory.setHints(svgRoot);
		document.getImplementation().setDeviceFactory(devFactory);

		// Set the user style sheet, if any
		if (userSheetUri != null) {
			ParsedURL purl = new ParsedURL(userSheetUri);
			try (InputStream is = purl.openStream()) {
				String conType = purl.getContentType();
				String conEnc = purl.getContentEncoding();
				Reader userRe = AgentUtil.inputStreamToReader(is, conType, conEnc,
						StandardCharsets.UTF_8);
				((CSSStyleSheetFactory) document.getImplementation())
						.setUserStyleSheet(purl.getPostConnectionURL(), userRe);
			}
		}

		// If there is an alternate style sheet, set it
		if (alt != null && (alt = alt.trim()).length() != 0) {
			document.setSelectedStyleSheetSet(alt);
		}

		// Now fill the SVG document with computed styles
		copyWithComputedStyles(svgRoot, svgDoc, svgDoc);

		// Dispose canvas resources, if any
		MyDeviceFactory.MyCanvas canvas = (MyDeviceFactory.MyCanvas) document.getCanvas();
		canvas.dispose();

		// If no error handler is set, use the default
		if (transcoder.getErrorHandler() == null) {
			ErrorHandler handler = new DefaultErrorHandler();
			transcoder.setErrorHandler(handler);
		}

		// Transcode
		TranscoderInput input = new TranscoderInput(svgDoc);
		transcoder.transcode(input, output);

		// Set the final document as alternative output
		if (output.getDocument() == null) {
			output.setDocument(svgDoc);
		}
	}

	private static void copyWithComputedStyles(DOMNode node, Document svgDoc, Node svgParent) {
		Node svgNode;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if ("foreignObject".equals(node.getLocalName())) {
				// <foreignObject> element
				DOMElement fo = (DOMElement) node;
				Element flowRoot = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI,
						"flowRoot");
				svgParent.appendChild(flowRoot);
				flowRoot.setAttributeNS("http://www.w3.org/XML/1998/namespace", "space",
						"preserve");
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
			for (DOMNode n : node.getChildNodes()) {
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
		if (noStyle.contains(name)) {
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

		Element flowDiv = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "flowDiv");
		Element flowRegion = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "flowRegion");
		flowRoot.appendChild(flowRegion);
		flowRoot.appendChild(flowDiv);

		DOMElement elm = fo.getFirstElementChild();
		if (SVGConstants.SVG_NAMESPACE_URI.equals(elm.getNamespaceURI())) {
			// Do not replace this one
			Element svgElm = importElement(elm, svgDoc);
			flowRoot.appendChild(svgElm);
		} else {
			// Compute the style
			ComputedCSSStyle style = elm.getComputedStyle(null);
			// If a block element, compute box model
			String display = style.getDisplay();
			boolean isBlock = "block".equals(display) || "inline-block".equals(display);
			if (isBlock) {
				BoxValues box = style.getBoxValues(CSSUnit.CSS_PX);
				if (box.getWidth() > 1e-4) {
					box.fillBoxValues(style);
				}
				Element rect = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "rect");
				copyAttributes(fo, rect);
				rect.setAttribute("visibility", "hidden");
				flowRegion.appendChild(rect);
				rect.setAttribute("style", style.getMinifiedCssText());
				replaceElement(elm, svgDoc, flowDiv, flowDiv);
			} // Ignore non-blocks
		}
	}

	private static void copyAttributes(DOMElement fo, Element rect) {
		for (Attr attr : fo.getAttributes()) {
			rect.setAttributeNS(attr.getNamespaceURI(), attr.getName(), attr.getValue());
		}
	}

	private static void replaceElement(DOMElement elm, Document svgDoc, Element flowDiv,
			Element svgParent) {
		ComputedCSSStyle style = elm.getComputedStyle(null);
		String display = style.getDisplay();
		boolean isBlock = "block".equals(display) || "inline-block".equals(display);
		String elemName;
		if (isBlock) {
			elemName = "flowPara";
		} else {
			elemName = "flowSpan";
		}

		Element flowElm = svgDoc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, elemName);
		if (style.getLength() != 0) {
			flowElm.setAttribute("style", style.getMinifiedCssText());
		}
		svgParent.appendChild(flowElm);

		if (elm.getFirstElementChild() == null) {
			// No child elements
			String content = elm.getTextContent();
			String preserve = elm.getAttributeNS("http://www.w3.org/XML/1998/namespace", "space");
			if (!"preserve".equalsIgnoreCase(preserve)) {
				content = content.trim();
			}
			if (content.length() == 0) {
				return;
			}
			Text text = svgDoc.createTextNode(content);
			flowElm.appendChild(text);
		} else {
			Iterator<DOMNode> it = elm.iterator();
			while (it.hasNext()) {
				DOMNode node = it.next();
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					replaceElement((DOMElement) node, svgDoc, flowDiv, flowElm);
				} else {
					Text text = svgDoc.createTextNode(node.getNodeValue());
					flowElm.appendChild(text);
				}
			}
		}
	}

	private class MyDeviceFactory implements DeviceFactory {

		private MyStyleDatabase sdb = new MyStyleDatabase();

		private float height = 0f, width = 0f;

		@Override
		public StyleDatabase getStyleDatabase(String medium) {
			return sdb;
		}

		/**
		 * Set some style database information using the transcoding hints and the SVG
		 * root element.
		 * 
		 * @param svgRoot the SVG root element.
		 */
		void setHints(DOMElement svgRoot) {
			TranscodingHints transcodingHints = getTranscoder().getTranscodingHints();
			// Width
			Float width = (Float) transcodingHints.get(SVGAbstractTranscoder.KEY_WIDTH);
			if (width != null) {
				this.width = width.floatValue();
			} else {
				String w = svgRoot.getAttribute("width");
				if (w.length() != 0) {
					try {
						this.width = Float.parseFloat(w);
					} catch (NumberFormatException e) {
					}
				}
				if (this.width == 0) {
					this.width = 595f; // A4
				}
			}
			// Height
			Float height = (Float) transcodingHints.get(SVGAbstractTranscoder.KEY_HEIGHT);
			if (height != null) {
				this.height = height.floatValue();
			} else {
				String h = svgRoot.getAttribute("height");
				if (h.length() != 0) {
					try {
						this.height = Float.parseFloat(h);
					} catch (NumberFormatException e) {
					}
				}
				if (this.height == 0) {
					this.height = 842f; // A4
				}
			}
		}

		@Override
		public CSSCanvas createCanvas(String medium, CSSDocument doc) {
			CSSCanvas canvas;
			if (transcoder instanceof ImageTranscoder) {
				BufferedImage dest = ((ImageTranscoder) transcoder).createImage(Math.round(width),
						Math.round(height));
				Graphics2D graphics2d = dest.createGraphics();
				canvas = new Graphics2DCanvas(doc, graphics2d);
			} else {
				canvas = new MyCanvas(doc);
			}
			return canvas;
		}

		private class MyStyleDatabase extends AbstractStyleDatabase {

			private static final long serialVersionUID = 1L;

			private final List<String> fonts = getAvailableFontList();

			private List<String> getAvailableFontList() {
				return Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getAvailableFontFamilyNames());
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
				GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				java.awt.GraphicsConfiguration gConfiguration = genv.getDefaultScreenDevice()
						.getDefaultConfiguration();
				int bpc = 255;
				if (gConfiguration != null) {
					int[] comp = gConfiguration.getColorModel().getComponentSize();
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
			protected boolean isFontFamilyAvailable(String fontFamily) {
				return fonts.contains(fontFamily);
			}

			@Override
			public CSSTypedValue getInitialColor() {
				return darkMode ? darkmodeInitialColor : super.getInitialColor();
			}

			@Override
			public boolean supports(String property, LexicalUnit lunit) {
				ValueFactory valueFactory = new ValueFactory();
				CSSValue value = valueFactory.createCSSValue(lunit);
				if ("color".equalsIgnoreCase(property)
						|| "background-color".equalsIgnoreCase(property)) {
					return supportsColor(value);
				}
				return value.getCssValueType() == CssType.TYPED
						&& isSupportedType((CSSTypedValue) value);
			}

			private boolean supportsColor(CSSValue value) {
				switch (value.getPrimitiveType()) {
				case COLOR:
				case COLOR_MIX:
					return true;
				case IDENT:
					return ColorIdentifiers.getInstance()
							.isColorIdentifier(value.getCssText().toLowerCase(Locale.ROOT));
				default:
					return false;
				}
			}

			private boolean isSupportedType(CSSTypedValue value) {
				short unit = value.getUnitType();
				/*
				 * Supported units are: absolute lengths, em, ex, angles and times
				 */
				return (unit >= CSSUnit.CSS_PX && unit <= CSSUnit.CSS_EX)
						|| CSSUnit.isAngleUnitType(unit) || CSSUnit.isTimeUnitType(unit);
			}

			@Override
			public boolean supports(SelectorList selectors) {
				for (Selector selector : selectors) {
					if (!supports(selector)) {
						return false;
					}
				}
				return true;
			}

			private boolean supports(Selector selector) {
				if (selector != null) {
					switch (selector.getSelectorType()) {
					case CHILD:
					case DESCENDANT:
					case DIRECT_ADJACENT:
					case SUBSEQUENT_SIBLING:
						CombinatorSelector combSel = (CombinatorSelector) selector;
						return supports(combSel.getSelector())
								&& supports(combSel.getSecondSelector());
					case CONDITIONAL:
						ConditionalSelector condSel = (ConditionalSelector) selector;
						return supports(condSel.getSimpleSelector())
								&& supports(condSel.getCondition());
					case COLUMN_COMBINATOR:
						return false;
					default:
					}
				}
				return true;
			}

			private boolean supports(Condition condition) {
				switch (condition.getConditionType()) {
				case AND:
					CombinatorCondition combCond = (CombinatorCondition) condition;
					return supports(combCond.getFirstCondition())
							&& supports(combCond.getSecondCondition());
				case SELECTOR_ARGUMENT:
					ArgumentCondition argCond = (ArgumentCondition) condition;
					SelectorList selist = argCond.getSelectors();
					return selist == null || supports(selist);
				default:
					return true;
				}
			}

		}

		private class MyCanvas extends AbstractCSSCanvas {

			private CSSDocument document;

			protected MyCanvas(CSSDocument doc) {
				super();
				document = doc;
			}

			@Override
			public CSSDocument getDocument() {
				return document;
			}

			@Override
			public StyleDatabase getStyleDatabase() {
				return sdb;
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

			void dispose() {
			}

		}

		private class Graphics2DCanvas extends MyCanvas {

			final Graphics2D graphics2d;

			protected Graphics2DCanvas(CSSDocument doc, Graphics2D graphics2d) {
				super(doc);
				this.graphics2d = graphics2d;
			}

			@Override
			public int stringWidth(String text, CSSComputedProperties style) {
				java.awt.Font font = AWTHelper.createFont(style);
				FontMetrics fm = graphics2d.getFontMetrics(font);
				return fm.stringWidth(text);
			}

			@Override
			void dispose() {
				graphics2d.dispose();
			}

		}

	}

}
