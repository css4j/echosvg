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
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;

/**
 * Test the ImageTranscoder with the KEY_DEFAULT_FONT_FAMILY transcoding hint.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultFontFamilyTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The default font-family to use. */
	private String defaultFontFamily;

	@org.junit.Test
	public void test() throws TranscoderException {
		testDefaultFontFamily("samples/tests/spec/styling/defaultFontFamily.svg",
				"test-references/samples/tests/spec/styling/defaultFontFamilyArial.png", "Arial");
		testDefaultFontFamily("samples/tests/spec/styling/defaultFontFamily.svg",
				"test-references/samples/tests/spec/styling/defaultFontFamilyTimes.png", "Times");
		testDefaultFontFamily("samples/tests/spec/styling/defaultFontFamily.svg",
				"test-references/samples/tests/spec/styling/defaultFontFamilyTimes.png", "toto, Times");
	}

	/**
	 * Runs a new <code>DefaultFontFamilyTest</code>.
	 *
	 * @param inputURI          the URI of the input image
	 * @param refImageURI       the URI of the reference image
	 * @param defaultFontFamily the default font-family to use
	 */
	private void testDefaultFontFamily(String inputURI, String refImageURI, String defaultFontFamily) {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.defaultFontFamily = defaultFontFamily;
	}

	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	@Override
	protected TranscoderInput createTranscoderInput() {
		return new TranscoderInput(resolveURI(inputURI).toString());
	}

	/**
	 * Creates a Map that contains additional transcoding hints.
	 */
	@Override
	protected Map<Key, Object> createTranscodingHints() {
		Map<Key, Object> hints = new HashMap<>(3);
		hints.put(SVGAbstractTranscoder.KEY_DEFAULT_FONT_FAMILY, defaultFontFamily);
		return hints;
	}

	/**
	 * Returns the reference image for this test.
	 */
	@Override
	protected byte[] getReferenceImageData() {
		return createBufferedImageData(resolveURI(refImageURI));
	}

}
