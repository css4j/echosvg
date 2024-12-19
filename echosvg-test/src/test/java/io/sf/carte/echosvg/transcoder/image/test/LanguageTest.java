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
package io.sf.carte.echosvg.transcoder.image.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;
import io.sf.carte.echosvg.transcoder.svg.SVGAbstractTranscoder;

/**
 * Test the ImageTranscoder with the KEY_LANGUAGE transcoding hint.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class LanguageTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The preferred language. */
	private String language;

	/**
	 * Runs a new <code>LanguageTest</code>.
	 */
	@Test
	public void testLanguage() throws TranscoderException {
		testLanguage("test-resources/io/sf/carte/echosvg/transcoder/image/resources/language.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/languageEn.png", "en");
		testLanguage("test-resources/io/sf/carte/echosvg/transcoder/image/resources/language.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/languageFr.png", "fr");
		testLanguage("test-resources/io/sf/carte/echosvg/transcoder/image/resources/language.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/language.png", "");
	}

	/**
	 * Runs a new <code>LanguageTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @param language    the preferred language
	 * @throws TranscoderException 
	 */
	private void testLanguage(String inputURI, String refImageURI, String language) throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.language = language;
		runTest();
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
		Map<Key, Object> hints = new HashMap<>(7);
		hints.put(SVGAbstractTranscoder.KEY_LANGUAGE, language);
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
