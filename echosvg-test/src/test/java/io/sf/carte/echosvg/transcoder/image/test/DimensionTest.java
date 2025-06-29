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
 * Test the ImageTranscoder with the KEY_WIDTH and/or the KEY_HEIGHT transcoding
 * hint.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DimensionTest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The width of the image. */
	private Float width;

	/** The height of the image. */
	private Float height;

	@Test
	public void test()
			throws TranscoderException {
		testDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneW200.png", 200, -1);
		testDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneH200.png", -1, 200);
		testDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneWH200.png", 200,
				200);
		testDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneW600.png", 600, -1);
		testDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneH600.png", -1, 600);
		testDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneWH600.png", 600,
				600);

		testDimension("samples/tests/transcoder/image/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyW200.png", 200, -1);
		testDimension("samples/tests/transcoder/image/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyH200.png", -1, 200);
		testDimension("samples/tests/transcoder/image/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyWH200.png", 200, 200);
		testDimension("samples/tests/transcoder/image/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyW600.png", 600, -1);
		testDimension("samples/tests/transcoder/image/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyH600.png", -1, 600);
		testDimension("samples/tests/transcoder/image/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyWH600.png", 600, 600);
	}

	/**
	 * Runs a <code>DimensionTest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @param width       the image width
	 * @param height      the image height
	 * @throws TranscoderException
	 */
	private void testDimension(String inputURI, String refImageURI, float width, float height)
			throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.width = width;
		this.height = height;
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
		if (width > 0) {
			hints.put(SVGAbstractTranscoder.KEY_WIDTH, width);
		}
		if (height > 0) {
			hints.put(SVGAbstractTranscoder.KEY_HEIGHT, height);
		}
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
