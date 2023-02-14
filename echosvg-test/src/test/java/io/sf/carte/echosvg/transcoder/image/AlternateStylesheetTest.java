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

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
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
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The alternate stylesheet to use. */
	private String alternateStylesheet;

	@BeforeAll
	public static void beforeClass() throws FontFormatException, IOException {
		TestFonts.loadTestFonts();
	}

	@Test
	public void test() throws TranscoderException {
		testAlternateStylesheet("samples/tests/spec/styling/smiley.svg",
				"test-references/samples/tests/spec/styling/smileySmiling.png", "Smiling");
	}

	@Test
	public void testSad() throws TranscoderException {
		testAlternateStylesheet("samples/tests/spec/styling/smiley.svg",
				"test-references/samples/tests/spec/styling/smileyBasic Sad.png", "Basic Sad");
	}

	@Test
	public void testWow() throws TranscoderException {
		testAlternateStylesheet("samples/tests/spec/styling/smiley.svg",
				"test-references/samples/tests/spec/styling/smileyWow!.png", "Wow!");
	}

	@Test
	public void testGrim() throws TranscoderException {
		testAlternateStylesheet("samples/tests/spec/styling/smiley.svg",
				"test-references/samples/tests/spec/styling/smileyGrim.png", "Grim");
	}

	@Test
	public void testOups() throws TranscoderException {
		testAlternateStylesheet("samples/tests/spec/styling/smiley.svg",
				"test-references/samples/tests/spec/styling/smileyOups.png", "Oups");
	}

	/**
	 * Runs <code>AlternateStylesheetTest</code>.
	 *
	 * @param inputURI            the URI of the input image
	 * @param refImageURI         the URI of the reference image
	 * @param alternateStylesheet the alternate stylesheet CSS media
	 * @throws TranscoderException 
	 */
	private void testAlternateStylesheet(String inputURI, String refImageURI, String alternateStylesheet)
			throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.alternateStylesheet = alternateStylesheet;
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
		Map<Key, Object> hints = new HashMap<>(3);
		hints.put(SVGAbstractTranscoder.KEY_ALTERNATE_STYLESHEET, alternateStylesheet);
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
