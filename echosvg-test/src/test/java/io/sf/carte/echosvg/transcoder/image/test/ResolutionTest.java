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

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;

/**
 * Test the ImageTranscoder with the KEY_RESOLUTION_DPI transcoding hint.
 *
 * <p>
 * Based on PixelToMM test by
 * <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>. For
 * later modifications, see Git history.
 * </p>
 * 
 * @version $Id$
 */
public class ResolutionTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The resolution. */
	private float resolution;

	@Test
	public void test96dpi() throws TranscoderException {
		testResolution("test-resources/io/sf/carte/echosvg/transcoder/image/resources/resolution.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/resolution96dpi.png", 96f);
	}

	@Test
	public void test72dpi() throws TranscoderException {
		testResolution("test-resources/io/sf/carte/echosvg/transcoder/image/resources/resolution.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/resolution72dpi.png", 72f);
	}

	/**
	 * Runs a new <code>ResolutionTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @param resolution  the resolution
	 * @throws TranscoderException
	 */
	private void testResolution(String inputURI, String refImageURI, float resolution) throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.resolution = resolution;
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
		hints.put(SVGAbstractTranscoder.KEY_RESOLUTION_DPI, resolution);
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
