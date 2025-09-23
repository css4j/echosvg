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
import java.io.Reader;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This interface represents an object which can build a Document.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface DocumentFactory {

	/**
	 * Sets whether or not the XML stream has to be validate, depending on the
	 * specified parameter.
	 *
	 * @param isValidating true implies the XML stream will be validated
	 */
	void setValidating(boolean isValidating);

	/**
	 * Returns true if the XML stream has to be validated, false otherwise.
	 */
	boolean isValidating();

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @exception IOException if an error occured while reading the document.
	 */
	Document createDocument(String ns, String root, String uri) throws IOException;

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param encoding The default content encoding, {@code null} if not known.
	 * @exception IOException if an error occured while reading the document.
	 */
	Document createDocument(String ns, String root, String uri, String encoding) throws IOException;

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param is   The document input stream.
	 * @exception IOException if an error occured while reading the document.
	 */
	Document createDocument(String ns, String root, String uri, InputStream is) throws IOException;

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns       The namespace URI of the root element of the document.
	 * @param root     The name of the root element of the document.
	 * @param uri      The document URI.
	 * @param is       The document input stream.
	 * @param encoding The document source encoding, {@code null} if not known.
	 * @exception IOException if an error occured while reading the document.
	 */
	Document createDocument(String ns, String root, String uri, InputStream is, String encoding)
			throws IOException;

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param r    An XMLReader instance
	 * @exception IOException if an error occured while reading the document.
	 * @deprecated see {@link #setXMLReader(XMLReader)}.
	 */
	@Deprecated
	Document createDocument(String ns, String root, String uri, XMLReader r) throws IOException;

	/**
	 * Creates a Document instance.
	 * 
	 * @param ns   The namespace URI of the root element of the document.
	 * @param root The name of the root element of the document.
	 * @param uri  The document URI.
	 * @param r    The document reader.
	 * @exception IOException if an error occured while reading the document.
	 */
	Document createDocument(String ns, String root, String uri, Reader r) throws IOException;

	/**
	 * Parse the content of the given input source as an XML document and return a
	 * new DOM {@link Document} object.
	 *
	 * @param is InputSource containing the content to be parsed.
	 *
	 * @return a new DOM Document object.
	 *
	 * @throws IOException              if any IO errors occur.
	 * @throws SAXException             if any parse errors occur.
	 * @throws IllegalArgumentException when <code>is</code> is <code>null</code>
	 */
	Document parse(InputSource is) throws SAXException, IOException;

	/**
	 * Returns the document descriptor associated with the latest created document.
	 * 
	 * @return null if no document or descriptor was previously generated.
	 */
	DocumentDescriptor getDocumentDescriptor();

	/**
	 * Get the reader to be used in the parsing.
	 * 
	 * @return the XML reader.
	 */
	XMLReader getXMLReader();

	/**
	 * Set the factory to parse with the given {@code XMLReader}.
	 * 
	 * @param reader the reader.
	 */
	void setXMLReader(XMLReader reader);

}
