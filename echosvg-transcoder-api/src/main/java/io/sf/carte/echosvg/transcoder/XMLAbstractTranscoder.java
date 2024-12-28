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

package io.sf.carte.echosvg.transcoder;

import java.io.IOException;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.transcoder.keys.BooleanKey;
import io.sf.carte.echosvg.transcoder.keys.DOMImplementationKey;
import io.sf.carte.echosvg.transcoder.keys.StringKey;

/**
 * This class may be the base class of all transcoders which take an XML
 * document as input and which need to build a DOM tree. In order to take
 * advantage of this class, you have to specify the following transcoding hints:
 *
 * <ul>
 * <li><code>KEY_DOM_IMPLEMENTATION</code>: the DOM Implementation to use. If no
 * hint is provided a SVG 1.1 implementation shall be used, or a 1.2 if the
 * input is a DOM <code>Document</code> where the SVG root has a 1.2
 * <code>version</code> attribute.</li>
 *
 * <li><code>KEY_DOCUMENT_ELEMENT_NAMESPACE_URI</code>: the namespace URI of the
 * document to create. By default it is the SVG namespace URI. If you want to
 * process a <code>svg</code> element embedded into a XHTML document, it should
 * be the XHTML namespace URI.</li>
 *
 * <li><code>KEY_DOCUMENT_ELEMENT</code>: the qualified name of the document
 * element to create. By default it is the <code>svg</code> element. If you want
 * to process a <code>svg</code> element embedded into a XHTML document, it must
 * be the <code>html</code> element.</li>
 * </ul>
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class XMLAbstractTranscoder extends AbstractTranscoder {

	/**
	 * Constructs a new <code>XMLAbstractTranscoder</code>.
	 */
	protected XMLAbstractTranscoder() {
		hints.put(KEY_XML_PARSER_VALIDATING, Boolean.FALSE);
	}

	/**
	 * Transcodes the specified XML input in the specified output. All
	 * <code>TranscoderException</code> exceptions not catched previously are tagged
	 * as fatal errors (ie. call the <code>fatalError</code> method of the
	 * <code>ErrorHandler</code>).
	 *
	 * @param input  the XML input to transcode
	 * @param output the ouput where to transcode
	 * @exception TranscoderException if an error occured while transcoding
	 */
	@Override
	public void transcode(TranscoderInput input, TranscoderOutput output) throws TranscoderException {
		Document document = null;
		if (input.getDocument() != null) {
			document = input.getDocument();
		} else {
			document = loadDocument(input);
		}
		// call the dedicated transcode method
		if (document != null) {
			String uri = input.getURI();
			try {
				transcode(document, uri, output);
			} catch (TranscoderException ex) {
				// at this time, all TranscoderExceptions are fatal errors
				handler.fatalError(ex);
				return;
			}
		}
	}

	/**
	 * Retrieve the document from the URI, {@code InputStream} or reader given by
	 * {@code input}.
	 * 
	 * @param input the transcoder input.
	 * @return the document.
	 * @throws TranscoderException
	 */
	private Document loadDocument(TranscoderInput input) throws TranscoderException {
		String namespaceURI = (String) hints.get(KEY_DOCUMENT_ELEMENT_NAMESPACE_URI);
		String documentElement = (String) hints.get(KEY_DOCUMENT_ELEMENT);

		DocumentFactory f = createDocumentFactory(documentElement);

		if (namespaceURI == null) {
			handler.fatalError(
					new TranscoderException("Unspecified transcoding hints: KEY_DOCUMENT_ELEMENT_NAMESPACE_URI"));
			return null;
		}
		if (documentElement == null) {
			handler.fatalError(new TranscoderException("Unspecified transcoding hints: KEY_DOCUMENT_ELEMENT"));
			return null;
		}

		// parse the XML document
		Object xmlParserValidating = hints.get(KEY_XML_PARSER_VALIDATING);
		boolean validating = xmlParserValidating != null && (Boolean) xmlParserValidating;
		f.setValidating(validating);

		f.setXMLReader(input.getXMLReader());

		String uri = input.getURI();

		Document document = null;
		try {
			if (input.getInputStream() != null) {
				document = f.createDocument(namespaceURI, documentElement, uri, input.getInputStream(),
						input.getEncoding());
			} else if (input.getReader() != null) {
				document = f.createDocument(namespaceURI, documentElement, uri, input.getReader());
			} else if (uri != null) {
				document = f.createDocument(namespaceURI, documentElement, uri, input.getEncoding());
			}
		} catch (DOMException ex) {
			handler.fatalError(new TranscoderException(ex));
		} catch (IOException ex) {
			handler.fatalError(new TranscoderException(ex));
		}

		return document;
	}

	/**
	 * Create a {@code DocumentFactory} appropriate for the given document element.
	 * 
	 * @param documentElement the document element name.
	 * @return the {@code DocumentFactory}.
	 */
	protected DocumentFactory createDocumentFactory(String documentElement) {
		DOMImplementation domImpl = (DOMImplementation) hints.get(KEY_DOM_IMPLEMENTATION);
		if (domImpl == null) {
			domImpl = GenericDOMImplementation.getDOMImplementation();
		}
		return createDocumentFactory(domImpl);
	}

	/**
	 * Creates the <code>DocumentFactory</code> used to create the SVG DOM tree.
	 * <p>
	 * Override this method if you have to use another implementation of the
	 * <code>DocumentFactory</code> (ie. for SVG, you have to use the
	 * <code>SAXSVGDocumentFactory</code>).
	 * </p>
	 *
	 * @param domImpl the DOM Implementation to use
	 */
	protected DocumentFactory createDocumentFactory(DOMImplementation domImpl) {
		return new SAXDocumentFactory(domImpl);
	}

	/**
	 * Transcodes the specified Document in the specified output.
	 *
	 * @param document the document to transcode
	 * @param uri      the uri of the document or null if any
	 * @param output   the ouput where to transcode
	 * @exception TranscoderException if an error occured while transcoding
	 */
	protected abstract void transcode(Document document, String uri, TranscoderOutput output)
			throws TranscoderException;

	// --------------------------------------------------------------------
	// Keys definition
	// --------------------------------------------------------------------

	/**
	 * The validation mode of the XML parser.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_XML_PARSER_VALIDATING</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">false</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the validation mode of the XML parser.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_XML_PARSER_VALIDATING = new BooleanKey();

	/**
	 * Document element key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_DOCUMENT_ELEMENT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">Yes</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the qualified name of the document type to be
	 * created.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_DOCUMENT_ELEMENT = new StringKey();

	/**
	 * Document element namespace URI key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_DOCUMENT_ELEMENT_NAMESPACE_URI</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">Yes</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the namespace URI of the document element.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_DOCUMENT_ELEMENT_NAMESPACE_URI = new StringKey();

	/**
	 * DOM Implementation key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_DOM_IMPLEMENTATION</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the DOM Implementation to use.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_DOM_IMPLEMENTATION = new DOMImplementationKey();
}
