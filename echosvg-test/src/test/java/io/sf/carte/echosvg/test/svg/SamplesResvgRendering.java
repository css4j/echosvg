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
package io.sf.carte.echosvg.test.svg;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;

import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.test.misc.TestLocations;
import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;

/**
 * This test renders a number of SVG files under the {@code samples/resvg}
 * directory, and compares the result with a reference image. See issue #140.
 * <p>
 * Images were obtained from
 * {@linkplain https://github.com/linebender/resvg-test-suite}.
 * </p>
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 */
public class SamplesResvgRendering extends AbstractSamplesRendering {

	protected static final String FONT_PATH = "samples/resources/ttf/";
	protected static final float IMAGE_WIDTH = 500f;
	protected static final float IMAGE_HEIGHT = 500f;

	@Override
	protected RenderingTest createRenderingTest() {
		return new RenderingTest() {

			/**
			 * Returns the <code>ImageTranscoder</code> the Test should use
			 */
			@Override
			ImageTranscoder getTestImageTranscoder() {
				ImageTranscoder t = super.getTestImageTranscoder();

				t.addTranscodingHint(SVGAbstractTranscoder.KEY_DEFAULT_WIDTH, IMAGE_WIDTH);
				t.addTranscodingHint(SVGAbstractTranscoder.KEY_DEFAULT_HEIGHT, IMAGE_HEIGHT);

				return t;
			}

		};
	}

	@Override
	protected RenderingTest createErrIgnoreRenderingTest(int expectedErrorCount) {
		return new ErrIgnoreTest(expectedErrorCount) {

			/**
			 * Returns the <code>ImageTranscoder</code> the Test should use
			 */
			@Override
			ImageTranscoder getTestImageTranscoder() {
				ImageTranscoder t = super.getTestImageTranscoder();

				t.addTranscodingHint(SVGAbstractTranscoder.KEY_DEFAULT_WIDTH, IMAGE_WIDTH);
				t.addTranscodingHint(SVGAbstractTranscoder.KEY_DEFAULT_HEIGHT, IMAGE_HEIGHT);

				return t;
			}

		};
	}

	@Override
	protected RenderingTest createAllowExternalRenderingTest() {
		return new AllowExternalRenderingTest() {

			/**
			 * Returns the <code>ImageTranscoder</code> the Test should use
			 */
			@Override
			ImageTranscoder getTestImageTranscoder() {
				ImageTranscoder t = super.getTestImageTranscoder();

				t.addTranscodingHint(SVGAbstractTranscoder.KEY_DEFAULT_WIDTH, IMAGE_WIDTH);
				t.addTranscodingHint(SVGAbstractTranscoder.KEY_DEFAULT_HEIGHT, IMAGE_HEIGHT);

				return t;
			}

		};
	}

	@Override
	void test(String file) throws TranscoderException, IOException {
		test(file, false);
	}

	/**
	 * Test the rendering of a file with the given user language.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the SVG file to test.
	 * @param lang the language.
	 * @throws TranscoderException
	 * @throws IOException         if an I/O error occurs.
	 */
	@Override
	void testLang(String file, String lang) throws TranscoderException, IOException {
		testLang(file, lang, false);
	}

	private static boolean fontsLoaded = false;

	public static void loadTestFonts() throws FontFormatException, IOException {
		if (!fontsLoaded) {
			Path fontPathUrl = TestLocations.PROJECT_ROOT_DIR.resolve(FONT_PATH);

			TestFonts.registerFont(Font.TRUETYPE_FONT, fontPathUrl, "Amiri-Regular.ttf");
			TestFonts.registerFont(Font.TRUETYPE_FONT, fontPathUrl, "MPLUS1p-Regular.ttf");
			TestFonts.registerFont(Font.TRUETYPE_FONT, fontPathUrl, "NotoSans-Regular.ttf");
			TestFonts.registerFont(Font.TRUETYPE_FONT, fontPathUrl, "SourceSansPro-Regular.otf");

			fontsLoaded = true;
		}
	}

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
		loadTestFonts();
	}

}
