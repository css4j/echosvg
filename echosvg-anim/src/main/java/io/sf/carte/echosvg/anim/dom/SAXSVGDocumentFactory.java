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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.svg.SVGDocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.util.MimeTypeConstants;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class contains methods for creating SVGDocument instances from an URI
 * using SAX2.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SAXSVGDocumentFactory extends SAXDocumentFactory implements SVGDocumentFactory {

	/**
	 * Constant for HTTP content type header charset field.
	 */
	private static final String HTTP_CHARSET = "charset";

	/**
	 * Creates a new SVGDocumentFactory object with a default parser.
	 * 
	 */
	public SAXSVGDocumentFactory() {
		this(null);
	}

	/**
	 * Creates a new SVGDocumentFactory object.
	 * 
	 * @param reader The SAX2 reader. If {@code null}, a default one shall be
	 *               created.
	 */
	public SAXSVGDocumentFactory(XMLReader reader) {
		this(reader, false);
	}

	/**
	 * Creates a new SVGDocumentFactory object.
	 * 
	 * @param reader The SAX2 reader. If {@code null}, a default one shall be
	 *               created.
	 * @param dd     Whether a document descriptor must be generated.
	 */
	public SAXSVGDocumentFactory(XMLReader reader, boolean dd) {
		super(SVGDOMImplementation.getDOMImplementation(), reader, dd);
	}

	@Override
	public SVGDocument createSVGDocument(String uri) throws IOException {
		return createDocument(uri);
	}

	/**
	 * Creates a SVG Document instance.
	 * 
	 * @param uri The document URI.
	 * @param inp The document input stream.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createSVGDocument(String uri, InputStream inp) throws IOException {
		return createDocument(uri, inp, null);
	}

	/**
	 * Creates a SVG Document instance.
	 * 
	 * @param uri The document URI.
	 * @param r   The document reader.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createSVGDocument(String uri, Reader r) throws IOException {
		return createDocument(uri, r);
	}

	/**
	 * Creates a SVG Document instance. This method supports gzipped sources.
	 * 
	 * @param uri The document URI.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createDocument(String uri) throws IOException {
		ParsedURL purl = new ParsedURL(uri);

		InputStream is = purl.openStream(MimeTypeConstants.MIME_TYPES_SVG_LIST.iterator());
		uri = purl.getPostConnectionURL();

		InputSource isrc = new InputSource(is);

		// now looking for a charset encoding in the content type such
		// as "image/svg+xml; charset=iso8859-1" this is not official
		// for image/svg+xml yet! only for text/xml and maybe
		// for application/xml
		String contentType = purl.getContentType();
		int cindex = -1;
		if (contentType != null) {
			contentType = contentType.toLowerCase();
			cindex = contentType.indexOf(HTTP_CHARSET);
		}

		String charset = null;
		if (cindex != -1) {
			int i = cindex + HTTP_CHARSET.length();
			int eqIdx = contentType.indexOf('=', i);
			if (eqIdx != -1) {
				eqIdx++; // no one is interested in the equals sign...

				// The patch had ',' as the terminator but I suspect
				// that is the delimiter between possible charsets,
				// but if another 'attribute' were in the accept header
				// charset would be terminated by a ';'. So I look
				// for both and take to closer of the two.
				int idx = contentType.indexOf(',', eqIdx);
				int semiIdx = contentType.indexOf(';', eqIdx);
				if ((semiIdx != -1) && ((semiIdx < idx) || (idx == -1)))
					idx = semiIdx;
				if (idx != -1)
					charset = contentType.substring(eqIdx, idx);
				else
					charset = contentType.substring(eqIdx);
				charset = charset.trim();
				isrc.setEncoding(charset);
			}
		}

		isrc.setSystemId(uri);

		SVGOMDocument doc = (SVGOMDocument) super.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", uri,
				isrc);
		doc.setParsedURL(new ParsedURL(uri));
		doc.setDocumentInputEncoding(charset);
		doc.setXmlStandalone(isStandalone());
		doc.setXmlVersion(getXmlVersion());

		is.close();

		return doc;
	}

	/**
	 * Creates a SVG Document instance.
	 * 
	 * @param uri The document URI.
	 * @param inp The document input stream.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createDocument(String uri, InputStream inp) throws IOException {
		return createDocument(uri, inp, null);
	}

	/**
	 * Creates a SVG Document instance.
	 * 
	 * @param uri The document URI.
	 * @param inp The document input stream.
	 * @param encoding The document encoding, {@code null} if not known.
	 * @exception IOException if an error occurred while reading the document.
	 */
	public SVGDocument createDocument(String uri, InputStream inp, String encoding) throws IOException {
		SVGOMDocument doc;
		InputSource is = new InputSource(inp);
		is.setSystemId(uri);
		is.setEncoding(encoding);

		try {
			doc = (SVGOMDocument) super.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", uri, is);
			if (uri != null) {
				doc.setParsedURL(new ParsedURL(uri));
			}

			AbstractDocument d = doc;
			d.setDocumentURI(uri);
			d.setXmlStandalone(isStandalone());
			d.setXmlVersion(getXmlVersion());
		} catch (MalformedURLException e) {
			throw new IOException(e.getMessage());
		}
		return doc;
	}

	/**
	 * Creates a SVG Document instance.
	 * 
	 * @param uri The document URI.
	 * @param r   The document reader.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createDocument(String uri, Reader r) throws IOException {
		SVGOMDocument doc;
		InputSource is = new InputSource(r);
		is.setSystemId(uri);

		try {
			doc = (SVGOMDocument) super.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", uri, is);
			if (uri != null) {
				doc.setParsedURL(new ParsedURL(uri));
			}

			AbstractDocument d = doc;
			d.setDocumentURI(uri);
			d.setXmlStandalone(isStandalone());
			d.setXmlVersion(getXmlVersion());
		} catch (MalformedURLException e) {
			throw new IOException(e.getMessage());
		}
		return doc;
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createDocument(String ns, String root, String uri) throws IOException {
		return createDocument(uri);
	}

	@Override
	protected void checkRootElement(String ns, String root) {
		if (!SVGDOMImplementation.SVG_NAMESPACE_URI.equals(ns) || !"svg".equals(root)) {
			throw new IllegalArgumentException("Bad root element");
		}
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param is   The document input stream.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createDocument(String ns, String root, String uri, InputStream is) throws IOException {
		return createDocument(uri, is, null);
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param is   The document input stream.
	 * @param encoding The document source encoding, {@code null} if not known.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createDocument(String ns, String root, String uri, InputStream is, String encoding)
			throws IOException {
		return createDocument(uri, is, encoding);
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param r    The document reader.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public SVGDocument createDocument(String ns, String root, String uri, Reader r) throws IOException {
		return createDocument(uri, r);
	}

	@Override
	public DOMImplementation getDOMImplementation(String ver) {
		if (ver == null || ver.length() == 0 || ver.equals("1.0") || ver.equals("1.1")) {
			return SVGDOMImplementation.getDOMImplementation();
		} else if (ver.equals("1.2")) {
			return SVG12DOMImplementation.getDOMImplementation();
		}
		throw new RuntimeException("Unsupport SVG version '" + ver + "'");
	}

	/**
	 * <b>SAX</b>: Implements {@link org.xml.sax.ContentHandler#startDocument()}.
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		// Do not assume namespace declarations when no DTD has been specified.
		// namespaces.put("", SVGDOMImplementation.SVG_NAMESPACE_URI);
		// namespaces.put("xlink", XLinkSupport.XLINK_NAMESPACE_URI);
	}

}
