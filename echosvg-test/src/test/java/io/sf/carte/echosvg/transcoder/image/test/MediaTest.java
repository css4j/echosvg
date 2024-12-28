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

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;
import io.sf.carte.echosvg.transcoder.svg.SVGAbstractTranscoder;

/**
 * Test the ImageTranscoder with the KEY_MEDIA transcoding hint.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class MediaTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The CSS media to use. */
	private String media;

	@BeforeAll
	public static void beforeClass() throws FontFormatException, IOException {
		TestFonts.loadTestFonts();
	}

	@Test
	public void testScreen() throws TranscoderException {
		testMedia("samples/tests/spec/styling/cssMedia.svg",
				"test-references/samples/tests/spec/styling/cssMediaScreen.png", "screen");
	}

	@Test
	public void testProjection() throws TranscoderException {
		testMedia("samples/tests/spec/styling/cssMedia.svg",
				"test-references/samples/tests/spec/styling/cssMediaProjection.png", "projection");
	}

	@Test
	public void testPrint() throws TranscoderException {
		testMedia("samples/tests/spec/styling/cssMedia.svg",
				"test-references/samples/tests/spec/styling/cssMediaPrint.png", "print");
	}

	/**
	 * Runs a new <code>MediaTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @param media       the CSS media
	 * @throws TranscoderException 
	 */
	private void testMedia(String inputURI, String refImageURI, String media) throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.media = media;
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
		hints.put(SVGAbstractTranscoder.KEY_MEDIA, media);
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
