/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

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
	protected String inputURI;
	/** The URI of the reference image. */
	protected String refImageURI;
	/** The maximum width of the image. */
	protected Float maxWidth = Float.NaN;
	/** The maximum height of the image. */
	protected Float maxHeight = Float.NaN;
	/** The width of the image. */
	protected Float width = Float.NaN;
	/** The height of the image. */
	protected Float height = Float.NaN;

	// -- Constructors --------------------------------------------------------
	/**
	 * Constructs a new <code>MaxDimensionTest</code>.
	 *
	 * @param inputURI    URI of the input image.
	 * @param refImageURI URI of the reference image.
	 * @param maxWidth    Maximum image width (KEY_MAX_WIDTH value).
	 * @param maxHeight   Maximum image height (KEY_MAX_HEIGHT value).
	 */
	public MaxDimensionTest(String inputURI, String refImageURI, Float maxWidth, Float maxHeight) {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
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
	 */
	public MaxDimensionTest(String inputURI, String refImageURI, Float maxWidth, Float maxHeight, Float width,
			Float height) {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.width = width;
		this.height = height;
	}

	// -- Methods -------------------------------------------------------------
	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	@Override
	protected TranscoderInput createTranscoderInput() {
		return new TranscoderInput(resolveURL(inputURI).toString());
	}

	/**
	 * Creates a Map that contains additional transcoding hints.
	 *
	 * @return Transcoding hint values.
	 */
	@Override
	protected Map<Key, Object> createTranscodingHints() {
		Map<Key, Object> hints = new HashMap<>(7);
		if (!width.isNaN() && width > 0) {
			hints.put(SVGAbstractTranscoder.KEY_WIDTH, width);
		}
		if (!height.isNaN() && height > 0) {
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
		return createBufferedImageData(resolveURL(refImageURI));
	}
}
