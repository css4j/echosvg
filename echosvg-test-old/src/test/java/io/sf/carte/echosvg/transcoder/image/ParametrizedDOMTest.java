/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.transcoder.image;

import java.io.IOException;

import org.w3c.dom.Document;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

/**
 * Test the ImageTranscoder input with a DOM tree.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ParametrizedDOMTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	protected String inputURI;

	/** The URI of the reference image. */
	protected String refImageURI;

	/**
	 * Constructs a new <code>ParametrizedDOMTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 */
	public ParametrizedDOMTest(String inputURI, String refImageURI) {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
	}

	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	@Override
	protected TranscoderInput createTranscoderInput() {
		try {
			String parser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			Document doc = f.createDocument(resolveURL(inputURI).toString());
			return new TranscoderInput(doc);
		} catch (IOException ex) {
			throw new IllegalArgumentException(inputURI);
		}
	}

	/**
	 * Returns the reference image for this test.
	 */
	@Override
	protected byte[] getReferenceImageData() {
		return createBufferedImageData(resolveURL(refImageURI));
	}
}
