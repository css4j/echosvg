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

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;

/**
 * Test the ImageTranscoder with the KEY_AOI transcoding hint.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AOITest extends AbstractImageTranscoderTest {

	/** The URI of the input image. */
	private String inputURI;

	/** The URI of the reference image. */
	private String refImageURI;

	/** The area of interest. */
	private Rectangle2D aoi;

	/** The width of the image. */
	private float imgWidth;

	/** The height of the image. */
	private float imgHeight;

	@org.junit.Test
	public void test() throws TranscoderException {
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneNW.png", 0f, 0f, 225f,
				250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneNE.png", 225f, 0f, 225f,
				250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneSW.png", 0f, 250f, 225f,
				250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneSE.png", 225f, 250f, 225f,
				250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneC.png", 125f, 150f, 225f,
				250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneWNW.png", 0f, 0f, 225f,
				250f, 225f, 250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneWNE.png", 225f, 0f, 225f,
				250f, 225f, 250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneWSW.png", 0f, 250f, 225f,
				250f, 225f, 250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneWSE.png", 225f, 250f,
				225f, 250f, 225f, 250f);
		testAOI("samples/anne.svg", "test-references/io/sf/carte/echosvg/transcoder/image/anneWC.png", 125f, 150f, 225f,
				250f, 225f, 250f);

		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyNW.png", 0f, 0f, 212.5f, 150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyNE.png", 212.5f, 0f, 212.5f, 150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflySW.png", 0f, 150f, 212.5f, 150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflySE.png", 212.5f, 150f, 212.5f, 150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyC.png", 125f, 150f, 212.5f, 150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyWNW.png", 0f, 0f, 212.5f, 150f, 212.5f,
				150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyWNE.png", 212.5f, 0f, 212.5f, 150f,
				212.5f, 150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyWSW.png", 0f, 150f, 212.5f, 150f, 212.5f,
				150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyWSE.png", 212.5f, 150f, 212.5f, 150f,
				212.5f, 150f);
		testAOI("test-resources/io/sf/carte/echosvg/transcoder/image/resources/butterfly.svg",
				"test-references/io/sf/carte/echosvg/transcoder/image/butterflyWC.png", 125f, 150f, 212.5f, 150f,
				212.5f, 150f);
	}

	/**
	 * Runs a new <code>AOITest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @param x           the x coordinate of the area of interest
	 * @param y           the y coordinate of the area of interest
	 * @param width       the width of the area of interest
	 * @param height      the height of the area of interest
	 */
	private void testAOI(String inputURI, String refImageURI, float x, float y, float width, float height) {
		testAOI(inputURI, refImageURI, x, y, width, height, -1, -1);
	}

	/**
	 * Runs a new <code>AOITest</code>.
	 *
	 * @param inputURI    the URI of the input image
	 * @param refImageURI the URI of the reference image
	 * @param x           the x coordinate of the area of interest
	 * @param y           the y coordinate of the area of interest
	 * @param width       the width of the area of interest
	 * @param height      the height of the area of interest
	 * @param imgWidth    the width of the image to generate
	 * @param imgHeight   the height of the image to generate
	 */
	private void testAOI(String inputURI, String refImageURI, float x, float y, float width, float height,
			float imgWidth, float imgHeight) {
		this.inputURI = inputURI;
		this.refImageURI = refImageURI;
		this.aoi = new Rectangle2D.Float(x, y, width, height);
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
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
		Map<Key, Object> hints = new HashMap<>(11);
		hints.put(SVGAbstractTranscoder.KEY_AOI, aoi);
		if (imgWidth > 0) {
			hints.put(SVGAbstractTranscoder.KEY_WIDTH, imgWidth);
		}
		if (imgHeight > 0) {
			hints.put(SVGAbstractTranscoder.KEY_HEIGHT, imgHeight);
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
