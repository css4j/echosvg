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
package io.sf.carte.echosvg.transcoder.image;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;

/**
 * Test the ImageTranscoder input with a GenericDocument.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GenericDocumentTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	@Test
	public void test() throws TranscoderException {
		testGenericDocument("samples/anne.svg", "test-references/samples/anne.png");
	}

	/**
	 * Runs a new <code>GenericDocumentTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @throws TranscoderException 
	 */
	private void testGenericDocument(String inputURI, String refImageURI) throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		runTest();
	}

	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	@Override
	protected TranscoderInput createTranscoderInput() {
		try {
			URL url = resolveURI(inputURI);
			DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
			SAXDocumentFactory f = new SAXDocumentFactory(impl);
			Document doc = f.createDocument(url.toString(), "utf-8");
			TranscoderInput input = new TranscoderInput(doc);
			input.setURI(url.toString()); // Needed for external resources
			return input;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(inputURI);
		}
	}

	/**
	 * Returns the reference image for this test.
	 */
	@Override
	protected byte[] getReferenceImageData() {
		return createBufferedImageData(resolveURI(refImageURI));
	}

}
