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
 * Test the ImageTranscoder with the KEY_PIXEL_UNIT_TO_MILLIMETER transcoding
 * hint.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PixelToMMTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The pixel to mm factor. */
	private float px2mm;

	@org.junit.Test
	public void test96dpi() throws TranscoderException {
		testPixelToMM("test-resources/io/sf/carte/echosvg/transcoder/image/resources/px2mm.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/px2mm96dpi.png", 0.2645833f);
	}

	@org.junit.Test
	public void test72dpi() throws TranscoderException {
		testPixelToMM("test-resources/io/sf/carte/echosvg/transcoder/image/resources/px2mm.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/px2mm72dpi.png", 0.3528f);
	}

	/**
	 * Runs a new <code>PixelToMMTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @param px2mm       the pixel to mm conversion factor
	 * @throws TranscoderException 
	 */
	private void testPixelToMM(String inputURI, String refImageURI, float px2mm) throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.px2mm = px2mm;
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
		hints.put(SVGAbstractTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, px2mm);
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
