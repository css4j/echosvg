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
package io.sf.carte.echosvg.dom.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.util.HaltingThread;

/**
 * This class contains methods for creating Document instances from an URI using
 * SAX2.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SAXDocumentFactory implements LexicalHandler, DocumentFactory, ContentHandler, ErrorHandler {

	/**
	 * The DOM implementation used to create the document.
	 */
	private DOMImplementation implementation;

	/**
	 * The SAX2 parser object.
	 */
	private XMLReader parser;

	/**
	 * The created document.
	 */
	private Document document;

	/**
	 * The created document descriptor.
	 */
	private DocumentDescriptor documentDescriptor;

	/**
	 * Whether a document descriptor must be generated.
	 */
	private boolean createDocumentDescriptor;

	/**
	 * The current node.
	 */
	private Node currentNode;

	/**
	 * The locator.
	 */
	private Locator locator;

	/**
	 * Contains collected string data. May be Text, CDATA or Comment.
	 */
	private StringBuilder stringBuffer = new StringBuilder();

	/**
	 * The DTD to use when the document is created.
	 */
	private DocumentType doctype;

	/**
	 * Indicates if stringBuffer has content, needed in case of zero sized "text"
	 * content.
	 */
	private boolean stringContent;

	/**
	 * True if the parser is currently parsing a DTD.
	 */
	private boolean inDTD;

	/**
	 * True if the parser is currently parsing a CDATA section.
	 */
	private boolean inCDATA;

	/**
	 * Whether the parser still hasn't read the document element's opening tag.
	 */
	private boolean inProlog;

	/**
	 * Whether the parser is in validating mode.
	 */
	private boolean isValidating;

	/**
	 * Whether the document just parsed was standalone.
	 */
	private boolean isStandalone;

	/**
	 * XML version of the document just parsed.
	 */
	private String xmlVersion;

	/**
	 * The stack used to store the namespace URIs.
	 */
	private HashTableStack namespaces;

	/**
	 * The error handler.
	 */
	protected ErrorHandler errorHandler;

	protected interface PreInfo {

		Node createNode(Document doc);

	}

	private static class ProcessingInstructionInfo implements PreInfo {

		public String target, data;

		public ProcessingInstructionInfo(String target, String data) {
			this.target = target;
			this.data = data;
		}

		@Override
		public Node createNode(Document doc) {
			return doc.createProcessingInstruction(target, data);
		}

	}

	private static class CommentInfo implements PreInfo {

		public String comment;

		public CommentInfo(String comment) {
			this.comment = comment;
		}

		@Override
		public Node createNode(Document doc) {
			return doc.createComment(comment);
		}

	}

	private static class CDataInfo implements PreInfo {

		public String cdata;

		public CDataInfo(String cdata) {
			this.cdata = cdata;
		}

		@Override
		public Node createNode(Document doc) {
			return doc.createCDATASection(cdata);
		}

	}

	private static class TextInfo implements PreInfo {

		public String text;

		public TextInfo(String text) {
			this.text = text;
		}

		@Override
		public Node createNode(Document doc) {
			return doc.createTextNode(text);
		}

	}

	/**
	 * Various elements encountered prior to real document root element. List of
	 * PreInfo objects.
	 */
	private List<PreInfo> preInfo;

	/**
	 * Creates a new SAXDocumentFactory object with a default parser. No document
	 * descriptor will be created while generating a document.
	 * 
	 * @param impl The DOM implementation to use for building the DOM tree.
	 */
	public SAXDocumentFactory(DOMImplementation impl) {
		this(impl, null);
	}

	/**
	 * Creates a new SAXDocumentFactory object. No document descriptor will be
	 * created while generating a document.
	 * 
	 * @param impl   The DOM implementation to use for building the DOM tree.
	 * @param reader The SAX2 reader. If {@code null}, a default one shall be
	 *               created.
	 */
	public SAXDocumentFactory(DOMImplementation impl, XMLReader reader) {
		this(impl, reader, false);
	}

	/**
	 * Creates a new SAXDocumentFactory object.
	 * 
	 * @param impl   The DOM implementation to use for building the DOM tree.
	 * @param reader The SAX2 reader. If {@code null}, a default one shall be
	 *               created.
	 * @param dd     Whether a document descriptor must be generated.
	 */
	public SAXDocumentFactory(DOMImplementation impl, XMLReader reader, boolean dd) {
		this.implementation = impl;
		this.parser = getXMLReader(reader);
		this.createDocumentDescriptor = dd;
	}

	private static XMLReader getXMLReader(XMLReader reader) {
		if (reader == null) {
			ServiceLoader<XMLReader> loader = ServiceLoader.load(XMLReader.class);
			Iterator<XMLReader> it = loader.iterator();
			if (it.hasNext()) {
				reader = it.next();
				try {
					reader.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
				} catch (SAXNotRecognizedException | SAXNotSupportedException e) {
				}
				try {
					reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
					reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				} catch (SAXNotRecognizedException | SAXNotSupportedException e) {
				}
			} else {
				SAXParser saxParser;
				try {
					saxParser = saxFactory.newSAXParser();
					reader = saxParser.getXMLReader();
				} catch (ParserConfigurationException | SAXException e) {
					e.printStackTrace();
					return null; // That should never happen
				}
			}
		}

		return reader;
	}

	/**
	 * Gets the document that is being processed.
	 * 
	 * @return the document.
	 */
	protected Document getDocument() {
		return document;
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
	public Document createDocument(String ns, String root, String uri) throws IOException {
		return parseDocument(ns, root, uri, new InputSource(uri));
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param encoding The default content encoding, {@code null} if not known.
	 * @exception IOException if an error occured while reading the document.
	 */
	@Override
	public Document createDocument(String ns, String root, String uri, String encoding)
			throws IOException {
		InputSource inp = new InputSource(uri);
		inp.setEncoding(encoding);
		return parseDocument(ns, root, uri, inp);
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param uri The document URI.
	 * @exception IOException if an error occurred while reading the document.
	 */
	public Document createDocument(String uri) throws IOException {
		return parseDocument(new InputSource(uri));
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param uri The document URI.
	 * @param encoding The default content encoding, {@code null} if not known.
	 * @exception IOException if an error occurred while reading the document.
	 */
	public Document createDocument(String uri, String encoding) throws IOException {
		InputSource inp = new InputSource(uri);
		inp.setEncoding(encoding);
		return parseDocument(inp);
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
	public Document createDocument(String ns, String root, String uri, InputStream is) throws IOException {
		InputSource inp = new InputSource(is);
		inp.setSystemId(uri);
		return parseDocument(ns, root, uri, inp);
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns       The namespace URI of the root element of the document.
	 * @param root     The name of the root element of the document.
	 * @param uri      The document URI.
	 * @param is       The document input stream.
	 * @param encoding The document source encoding, {@code null} if not known.
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Override
	public Document createDocument(String ns, String root, String uri, InputStream is, String encoding)
			throws IOException {
		InputSource inp = new InputSource(is);
		inp.setSystemId(uri);
		inp.setEncoding(encoding);
		return parseDocument(ns, root, uri, inp);
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param uri The document URI.
	 * @param is  The document input stream.
	 * @exception IOException if an error occurred while reading the document.
	 */
	public Document createDocument(String uri, InputStream is) throws IOException {
		InputSource inp = new InputSource(is);
		inp.setSystemId(uri);
		return parseDocument(inp);
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
	public Document createDocument(String ns, String root, String uri, Reader r) throws IOException {
		InputSource inp = new InputSource(r);
		inp.setSystemId(uri);
		return parseDocument(ns, root, uri, inp);
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param r    an XMLReaderInstance
	 * @exception IOException if an error occurred while reading the document.
	 */
	@Deprecated
	@Override
	public Document createDocument(String ns, String root, String uri, XMLReader r) throws IOException {
		r.setContentHandler(this);
		r.setEntityResolver(createEntityResolver());
		try {
			r.parse(uri);
		} catch (SAXException e) {
			Exception ex = e.getException();
			if (ex instanceof InterruptedIOException) {
				throw (InterruptedIOException) ex;
			}
			throw new SAXIOException(e);
		}
		currentNode = null;
		Document ret = document;
		document = null;
		doctype = null;
		ret.setDocumentURI(uri);
		return ret;
	}

	private EntityResolver createEntityResolver() {
		return new ResourceEntityResolver();
	}

	/**
	 * Creates a Document instance.
	 * 
	 * @param uri The document URI.
	 * @param r   The document reader.
	 * @exception IOException if an error occurred while reading the document.
	 */
	public Document createDocument(String uri, Reader r) throws IOException {
		InputSource inp = new InputSource(r);
		inp.setSystemId(uri);
		return parseDocument(inp);
	}

	/**
	 * Parses a Document and sets the document URI.
	 * 
	 * @param ns   The namespace URI of the root element.
	 * @param root The name of the root element.
	 * @param uri  The document URI.
	 * @param is   The document input source.
	 * @exception IOException if an error occurred while reading the document.
	 */
	private Document parseDocument(String ns, String root, String uri, InputSource is) throws IOException {
		Document doc = createDocument(ns, root, uri, is);
		doc.setDocumentURI(uri);
		return doc;
	}

	/**
	 * Creates a Document.
	 * 
	 * @param ns   The namespace URI of the root element.
	 * @param root The name of the root element.
	 * @param uri  The document URI.
	 * @param is   The document input source.
	 * @exception IOException if an error occurred while reading the document.
	 */
	protected Document createDocument(String ns, String root, String uri, InputSource is)
			throws IOException {
		checkRootElement(ns, root);

		Document ret = createDocument(is);
		Element docElem = ret.getDocumentElement();

		String lname = root;
		String nsURI = ns;
		if (ns == null) {
			int idx = lname.indexOf(':');
			String nsp = (idx == -1 || idx == lname.length() - 1) ? "" : lname.substring(0, idx);
			nsURI = namespaces.get(nsp);
			if (idx != -1 && idx != lname.length() - 1) {
				lname = lname.substring(idx + 1);
			}
		}

		String docElemNS = docElem.getNamespaceURI();
		if (docElemNS != nsURI && docElemNS != null && !docElemNS.equals(nsURI)) {
			throw new IOException("Root element namespace does not match that requested:\n"
					+ "Requested: " + nsURI + "\n" + "Found: " + docElemNS);
		}

		if (docElemNS != null) {
			if (!docElem.getLocalName().equals(lname))
				throw new IOException("Root element does not match that requested:\n"
						+ "Requested: " + lname + "\n" + "Found: " + docElem.getLocalName());
		} else {
			if (!docElem.getNodeName().equals(lname))
				throw new IOException("Root element does not match that requested:\n"
						+ "Requested: " + lname + "\n" + "Found: " + docElem.getNodeName());
		}

		return ret;
	}

	protected void checkRootElement(String ns, String root) {
	}

	private static final SAXParserFactory saxFactory;

	static {
		saxFactory = SAXParserFactory.newInstance();
		try {
			saxFactory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
			// saxFactory.setFeature("http://xml.org/sax/features/namespaces", true); // Default
			saxFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
			saxFactory.setFeature("http://xml.org/sax/features/xmlns-uris", true);
			saxFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			saxFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		} catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
		}
	}

	/**
	 * Parses a Document.
	 * 
	 * @param is The document input source.
	 * @exception IOException if an error occurred while reading the document.
	 */
	private Document parseDocument(InputSource is) throws IOException {
		Document doc = createDocument(is);
		doc.setDocumentURI(is.getSystemId());
		return doc;
	}

	/**
	 * Creates a Document.
	 * 
	 * @param is The document input source.
	 * @exception IOException if an error occurred while reading the document.
	 */
	protected Document createDocument(InputSource is) throws IOException {
		try {
			parser.setContentHandler(this);
			parser.setEntityResolver(createEntityResolver());
			parser.setErrorHandler((errorHandler == null) ? this : errorHandler);

			parser.setFeature("http://xml.org/sax/features/validation", isValidating);
			parser.setProperty("http://xml.org/sax/properties/lexical-handler", this);
			parser.parse(is);
		} catch (SAXException e) {
			Exception ex = e.getException();
			if (ex instanceof InterruptedIOException) {
				throw (InterruptedIOException) ex;
			}
			throw new SAXIOException(e);
		}

		currentNode = null;
		Document ret = document;
		document = null;
		doctype = null;
		locator = null;
		return ret;
	}

	/**
	 * Returns the document descriptor associated with the latest created document.
	 * 
	 * @return null if no document or descriptor was previously generated.
	 */
	@Override
	public DocumentDescriptor getDocumentDescriptor() {
		return documentDescriptor;
	}

	@Override
	public void setXMLReader(XMLReader reader) {
		this.parser = getXMLReader(reader);
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ContentHandler#setDocumentLocator(Locator)}.
	 */
	@Override
	public void setDocumentLocator(Locator l) {
		locator = l;
	}

	/**
	 * Sets whether or not the XML parser will validate the XML document depending
	 * on the specified parameter.
	 *
	 * @param isValidating indicates that the XML parser will validate the XML
	 *                     document
	 */
	@Override
	public void setValidating(boolean isValidating) {
		this.isValidating = isValidating;
	}

	/**
	 * Returns true if the XML parser validates the XML stream, false otherwise.
	 */
	@Override
	public boolean isValidating() {
		return isValidating;
	}

	protected boolean isStandalone() {
		return isStandalone;
	}

	protected String getXmlVersion() {
		return xmlVersion;
	}

	/**
	 * Sets a custom error handler.
	 */
	public void setErrorHandler(ErrorHandler eh) {
		errorHandler = eh;
	}

	public DOMImplementation getDOMImplementation(String ver) {
		return implementation;
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ErrorHandler#fatalError(SAXParseException)}.
	 */
	@Override
	public void fatalError(SAXParseException ex) throws SAXException {
		throw ex;
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ErrorHandler#error(SAXParseException)}.
	 */
	@Override
	public void error(SAXParseException ex) throws SAXException {
		throw ex;
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ErrorHandler#warning(SAXParseException)}.
	 */
	@Override
	public void warning(SAXParseException ex) throws SAXException {
	}

	/**
	 * <b>SAX</b>: Implements {@link org.xml.sax.ContentHandler#startDocument()}.
	 */
	@Override
	public void startDocument() throws SAXException {
		initPreInfo();

		namespaces = new HashTableStack();
		namespaces.put("xml", XMLConstants.XML_NAMESPACE_URI);
		namespaces.put("xmlns", XMLConstants.XMLNS_NAMESPACE_URI);
		namespaces.put("", null);

		inDTD = false;
		inCDATA = false;
		inProlog = true;
		currentNode = null;
		document = null;
		doctype = null;
		isStandalone = false;
		xmlVersion = XMLConstants.XML_VERSION_10;

		stringBuffer.setLength(0);
		stringContent = false;

		if (createDocumentDescriptor) {
			documentDescriptor = new DocumentDescriptor();
		} else {
			documentDescriptor = null;
		}
	}

	protected void initPreInfo() {
		preInfo = new LinkedList<>();
	}

	@Override
	public void endDocument() throws SAXException {
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ContentHandler#startElement(String,String,String,Attributes)}.
	 */
	@Override
	public void startElement(String uri, String localName, String rawName, Attributes attributes) throws SAXException {
		// Check If we should halt early.
		if (HaltingThread.hasBeenHalted()) {
			throw new SAXException(new InterruptedIOException());
		}

		if (inProlog) {
			inProlog = false;
			if (parser != null) {
				try {
					isStandalone = parser.getFeature("http://xml.org/sax/features/is-standalone");
				} catch (SAXNotRecognizedException ex) {
				}
				try {
					xmlVersion = (String) parser.getProperty("http://xml.org/sax/properties/document-xml-version");
				} catch (SAXNotRecognizedException ex) {
				}
			}
		}

		// Namespaces resolution
		int len = attributes.getLength();
		namespaces.push();
		String version = null;
		for (int i = 0; i < len; i++) {
			String aname = attributes.getQName(i);
			int slen = aname.length();
			if (slen < 5)
				continue;
			if (aname.equals("version")) {
				version = attributes.getValue(i);
				continue;
			}
			if (!aname.startsWith("xmlns"))
				continue;
			if (slen == 5) {
				String ns = attributes.getValue(i);
				if (ns.length() == 0)
					ns = null;
				namespaces.put("", ns);
			} else if (aname.charAt(5) == ':') {
				String ns = attributes.getValue(i);
				if (ns.length() == 0) {
					ns = null;
				}
				namespaces.put(aname.substring(6), ns);
			}
		}

		// Add any collected String Data before element.
		appendStringData();

		// Element creation
		Element e;
		int idx = rawName.indexOf(':');
		String nsp = (idx == -1 || idx == rawName.length() - 1) ? "" : rawName.substring(0, idx);
		String nsURI = namespaces.get(nsp);
		if (currentNode == null) {
			implementation = getDOMImplementation(version);
			document = implementation.createDocument(nsURI, rawName, doctype);
			Iterator<PreInfo> i = preInfoIterator();
			currentNode = e = document.getDocumentElement();
			while (i.hasNext()) {
				PreInfo pi = i.next();
				Node n = pi.createNode(document);
				document.insertBefore(n, e);
			}
			preInfo = null;
		} else {
			e = document.createElementNS(nsURI, rawName);
			currentNode.appendChild(e);
			currentNode = e;
		}

		// Storage of the line number.
		if (createDocumentDescriptor && locator != null) {
			documentDescriptor.setLocation(e, locator.getLineNumber(), locator.getColumnNumber());
		}

		// Attributes creation
		for (int i = 0; i < len; i++) {
			String aname = attributes.getQName(i);
			if (aname.equals("xmlns")) {
				e.setAttributeNS(XMLConstants.XMLNS_NAMESPACE_URI, aname, attributes.getValue(i));
			} else {
				idx = aname.indexOf(':');
				nsURI = (idx == -1) ? null : namespaces.get(aname.substring(0, idx));
				e.setAttributeNS(nsURI, aname, attributes.getValue(i));
			}
		}
	}

	protected Iterator<PreInfo> preInfoIterator() {
		return preInfo.iterator();
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ContentHandler#endElement(String,String,String)}.
	 */
	@Override
	public void endElement(String uri, String localName, String rawName) throws SAXException {
		appendStringData(); // add string data if any.

		if (currentNode != null)
			currentNode = currentNode.getParentNode();
		namespaces.pop();
	}

	public void appendStringData() {
		if (!stringContent)
			return;

		String str = stringBuffer.toString();
		stringBuffer.setLength(0); // reuse buffer.
		stringContent = false;
		if (currentNode == null) {
			if (inCDATA)
				addPreInfo(new CDataInfo(str));
			else
				addPreInfo(new TextInfo(str));
		} else {
			Node n;
			if (inCDATA)
				n = document.createCDATASection(str);
			else
				n = document.createTextNode(str);
			currentNode.appendChild(n);
		}
	}

	protected void addPreInfo(PreInfo node) {
		preInfo.add(node);
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ContentHandler#characters(char[],int,int)}.
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		stringBuffer.append(ch, start, length);
		stringContent = true;
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ContentHandler#ignorableWhitespace(char[],int,int)}.
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		stringBuffer.append(ch, start, length);
		stringContent = true;
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ContentHandler#processingInstruction(String,String)}.
	 */
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		if (inDTD)
			return;

		appendStringData(); // Add any collected String Data before PI

		if (currentNode == null)
			addPreInfo(new ProcessingInstructionInfo(target, data));
		else
			currentNode.appendChild(document.createProcessingInstruction(target, data));
	}

	// LexicalHandler /////////////////////////////////////////////////////////

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ext.LexicalHandler#startDTD(String,String,String)}.
	 */
	@Override
	public void startDTD(String name, String publicId, String systemId) throws SAXException {
		appendStringData(); // Add collected string data before entering DTD
		doctype = implementation.createDocumentType(name, publicId, systemId);
		inDTD = true;
	}

	/**
	 * <b>SAX</b>: Implements {@link org.xml.sax.ext.LexicalHandler#endDTD()}.
	 */
	@Override
	public void endDTD() throws SAXException {
		inDTD = false;
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ext.LexicalHandler#startEntity(String)}.
	 */
	@Override
	public void startEntity(String name) throws SAXException {
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ext.LexicalHandler#endEntity(String)}.
	 */
	@Override
	public void endEntity(String name) throws SAXException {
	}

	/**
	 * <b>SAX</b>: Implements {@link org.xml.sax.ext.LexicalHandler#startCDATA()}.
	 */
	@Override
	public void startCDATA() throws SAXException {
		appendStringData(); // Add any collected String Data before CData
		inCDATA = true;
		stringContent = true; // always create CDATA even if empty.
	}

	/**
	 * <b>SAX</b>: Implements {@link org.xml.sax.ext.LexicalHandler#endCDATA()}.
	 */
	@Override
	public void endCDATA() throws SAXException {
		appendStringData(); // Add the CDATA section
		inCDATA = false;
	}

	/**
	 * <b>SAX</b>: Implements
	 * {@link org.xml.sax.ext.LexicalHandler#comment(char[],int,int)}.
	 */
	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		if (inDTD)
			return;
		appendStringData();

		String str = new String(ch, start, length);
		if (currentNode == null) {
			addPreInfo(new CommentInfo(str));
		} else {
			currentNode.appendChild(document.createComment(str));
		}
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

}
