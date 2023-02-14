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

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;

/**
 * Test the ImageTranscoder with the KEY_MAX_WIDTH and/or the KEY_MAX_HEIGHT
 * transcoding hint.
 *
 * @author <a href="mailto:ruini@iki.fi">Henri Ruini</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class MaxDimensionTest extends AbstractImageTranscoderTest {

	// -- Variables -----------------------------------------------------------
	/** The URI of the input image. */
	private String inputURI;
	/** The URI of the reference image. */
	private String refImageURI;
	/** The maximum width of the image. */
	private float maxWidth = Float.NaN;
	/** The maximum height of the image. */
	private float maxHeight = Float.NaN;
	/** The width of the image. */
	private float width = Float.NaN;
	/** The height of the image. */
	private float height = Float.NaN;

	@Test
	public void test() throws TranscoderException {
		testMaxDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneMaxW200.png",
				200, -1);
		testMaxDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneMaxH200.png", -1,
				200);
		testMaxDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneMaxWH200.png",
				200, 200);
		testMaxDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneMaxW200.png",
				200, -1, 300, -1);
		testMaxDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneMaxH200.png", -1,
				200, -1, 300);
		testMaxDimension("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneMaxWH200.png",
				200, 200, 300, 300);
	}

	/**
	 * Runs a new <code>MaxDimensionTest</code>.
	 *
	 * @param inputURI    URI of the input image.
	 * @param refImageURI URI of the reference image.
	 * @param maxWidth    Maximum image width (KEY_MAX_WIDTH value).
	 * @param maxHeight   Maximum image height (KEY_MAX_HEIGHT value).
	 * @throws TranscoderException 
	 */
	private void testMaxDimension(String inputURI, String refImageURI, float maxWidth, float maxHeight)
			throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		runTest();
	}

	/**
	 * Constructs a new <code>MaxDimensionTest</code>.
	 *
	 * @param inputURI    URI of the input image.
	 * @param refImageURI URI of the reference image.
	 * @param maxWidth    Maximum image width (KEY_MAX_WIDTH value).
	 * @param maxHeight   Maximum image height (KEY_MAX_HEIGHT value).
	 * @param width       Image width (KEY_WIDTH value).
	 * @param height      Image height (KEY_HEIGH value).
	 * @throws TranscoderException 
	 */
	private void testMaxDimension(String inputURI, String refImageURI, float maxWidth, float maxHeight, float width,
			float height) throws TranscoderException {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.width = width;
		this.height = height;
		runTest();
	}

	// -- Methods -------------------------------------------------------------
	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	@Override
	protected TranscoderInput createTranscoderInput() {
		return new TranscoderInput(resolveURI(inputURI).toString());
	}

	/**
	 * Creates a Map that contains additional transcoding hints.
	 *
	 * @return Transcoding hint values.
	 */
	@Override
	protected Map<Key, Object> createTranscodingHints() {
		Map<Key, Object> hints = new HashMap<>(7);
		if (!Float.isNaN(width) && width > 0) {
			hints.put(SVGAbstractTranscoder.KEY_WIDTH, width);
		}
		if (!Float.isNaN(height) && height > 0) {
			hints.put(SVGAbstractTranscoder.KEY_HEIGHT, height);
		}
		if (maxWidth > 0) {
			hints.put(SVGAbstractTranscoder.KEY_MAX_WIDTH, maxWidth);
		}
		if (maxHeight > 0) {
			hints.put(SVGAbstractTranscoder.KEY_MAX_HEIGHT, maxHeight);
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
