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

import java.util.HashMap;
import java.util.Map;

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;

/**
 * Test the ImageTranscoder with the KEY_ALTERNATE_STYLESHEET transcoding hint.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AlternateStylesheetTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	protected String inputURI;

	/** The URI of the reference image. */
	protected String refImageURI;

	/** The alternate stylesheet to use. */
	protected String alternateStylesheet;

	/**
	 * Constructs a new <code>AlternateStylesheetTest</code>.
	 *
	 * @param inputURI            the URI of the input image
	 * @param refImageURI         the URI of the reference image
	 * @param alternateStylesheet the alternate stylesheet CSS media
	 */
	public AlternateStylesheetTest(String inputURI, String refImageURI, String alternateStylesheet) {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.alternateStylesheet = alternateStylesheet;
	}

	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	@Override
	protected TranscoderInput createTranscoderInput() {
		return new TranscoderInput(resolveURL(inputURI).toString());
	}

	/**
	 * Creates a Map that contains additional transcoding hints.
	 */
	@Override
	protected Map<Key, Object> createTranscodingHints() {
		Map<Key, Object> hints = new HashMap<>(3);
		hints.put(SVGAbstractTranscoder.KEY_ALTERNATE_STYLESHEET, alternateStylesheet);
		return hints;
	}

	/**
	 * Returns the reference image for this test.
	 */
	@Override
	protected byte[] getReferenceImageData() {
		return createBufferedImageData(resolveURL(refImageURI));
	}
}
