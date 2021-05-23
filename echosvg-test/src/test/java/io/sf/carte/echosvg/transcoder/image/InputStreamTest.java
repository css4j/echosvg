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
import java.io.InputStream;
import java.net.URL;

import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;

/**
 * Test the ImageTranscoder input with a InputStream.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class InputStreamTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	@org.junit.Test
	public void test() throws TranscoderException {
		testInputStream("samples/anne.svg", "test-references/samples/anne.png");
	}

	/**
	 * Runs <code>InputStreamTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @throws TranscoderException 
	 */
	private void testInputStream(String inputURI, String refImageURI) throws TranscoderException {
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
			InputStream istream = url.openStream();
			TranscoderInput input = new TranscoderInput(istream);
			input.setURI(url.toString()); // Needed for external resources
			return input;
		} catch (IOException ex) {
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
