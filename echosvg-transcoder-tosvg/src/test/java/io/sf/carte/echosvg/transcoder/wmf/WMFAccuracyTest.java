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
package io.sf.carte.echosvg.transcoder.wmf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.test.xml.XmlUtil;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.wmf.tosvg.WMFTranscoder;

/**
 * This test validates that a given WMF file is properly converted to an SVG
 * document by comparing the generated SVG document to a known, valid SVG
 * reference.
 * <p>
 * Unlike the test with the same name in the echosvg-test module, this test
 * uses the JDK DOM implementation.
 * </p>
 *
 * <p>
 * Original author: <a href="mailto:deweese@apache.org">Thomas DeWeese</a>.
 * For later modifications, see Git history.
 * </p>
 * 
 * @version $Id$
 */
public class WMFAccuracyTest {

	private static final String PROJECT_ROOT_URL = TestUtil.getRootProjectURL(WMFAccuracyTest.class,
			"echosvg-transcoder-tosvg");

	private static final String GENERATOR_REFERENCE_BASE = PROJECT_ROOT_URL
			+ "test-references/io/sf/carte/echosvg/transcoder/wmf";

	private static final String CANDIDATE_REF_DIR = "candidate";

	private static final String WMF_EXTENSION = ".wmf";
	private static final String SVG_EXTENSION = ".svg";
	private static final char PATH_SEPARATOR = '/';

	private static final List<String> fonts = Arrays
			.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

	private static boolean hasArial = isFontAvailable("Arial");

	private static boolean hasCourierNew = isFontAvailable("Courier New");

	/**
	 * Painter which performs an arbitrary rendering sequence.
	 */
	private URL wmfURL;

	/**
	 * Reference SVG URL
	 */
	private URL refURL;

	/**
	 * File where the generated SVG might be saved
	 */
	private File saveSVG;

	@Test
	public void testBlackShapes() throws IOException, TranscoderException {
		assumeTrue(isFontAvailable("Courier"), "'Courier' font is not available");
		assumeTrue(isFontAvailable("System"), "'System' font is not available");
		runTest("samples/tests/resources/wmf/black_shapes.wmf");
	}

	@Test
	public void testImageWMF() throws IOException, TranscoderException {
		runTest("samples/tests/resources/wmf/imageWMF.wmf");
	}

	@Test
	public void testNegApmText1() throws IOException, TranscoderException {
		assumeTrue(hasArial, "Arial font is not available");
		runTest("samples/tests/resources/wmf/negApmText1.wmf");
	}

	@Test
	public void testNegApmText2() throws IOException, TranscoderException {
		assumeTrue(hasArial, "Arial font is not available");
		runTest("samples/tests/resources/wmf/negApmText2.wmf");
	}

	@Test
	public void testChart() throws IOException, TranscoderException {
		assumeTrue(hasArial, "Arial font is not available");
		assumeTrue(hasCourierNew, "'Courier New' font is not available");
		runTest("samples/tests/resources/wmf/testChart.wmf");
	}

	@Test
	public void testTextGreek() throws IOException, TranscoderException {
		assumeTrue(hasCourierNew, "'Courier New' font is not available");
		runTest("samples/tests/resources/wmf/textGreek.wmf");
	}

	/**
	 * Run the test.
	 * 
	 * @param filename
	 * @throws IOException
	 * @throws TranscoderException
	 */
	private void runTest(String filename) throws IOException, TranscoderException {
		setFile(filename);
		ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
		WMFTranscoder wmft = new WMFTranscoder();
		TranscoderInput input;
		input = new TranscoderInput(wmfURL.toString());

		OutputStreamWriter outW;
		outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
		TranscoderOutput output = new TranscoderOutput(outW);

		wmft.transcode(input, output);

		byte[] data = out.toByteArray();
		String failMessage = XmlUtil.xmlDiff(refURL, data, saveSVG,
				new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation()));

		if (failMessage != null) {
			fail(failMessage);
		}
	}

	private void setFile(String id) throws MalformedURLException {
		String wmfFile = id;

		String[] dirNfile = breakWMFFile(wmfFile);

		wmfURL = new URL(PROJECT_ROOT_URL + dirNfile[0] + dirNfile[1] + dirNfile[2]);
		refURL = new URL(GENERATOR_REFERENCE_BASE + '/' + dirNfile[1] + SVG_EXTENSION);
		String saveURL = new URL(GENERATOR_REFERENCE_BASE + '/' + CANDIDATE_REF_DIR + "/" + dirNfile[1]
				+ SVG_EXTENSION).getFile();
		saveSVG = new File(saveURL);
	}

	private String[] breakWMFFile(String wmfFile) {
		if (wmfFile == null) {
			throw new IllegalArgumentException("Null WMF file given");
		}

		String[] ret = new String[3];

		if (wmfFile.endsWith(WMF_EXTENSION)) {
			ret[2] = WMF_EXTENSION;
		} else {
			throw new IllegalArgumentException("WMF File must end with '.wmf': " + wmfFile);
		}

		wmfFile = wmfFile.substring(0, wmfFile.length() - ret[2].length());

		int fileNameStart = wmfFile.lastIndexOf(PATH_SEPARATOR);
		String wmfDir = "";
		if (fileNameStart != -1) {
			if (wmfFile.length() < fileNameStart + 2) {
				// Nothing after PATH_SEPARATOR
				throw new IllegalArgumentException("Nothing after '" + PATH_SEPARATOR + "': " + wmfFile);
			}
			wmfDir = wmfFile.substring(0, fileNameStart + 1);
			wmfFile = wmfFile.substring(fileNameStart + 1);
		}
		ret[0] = wmfDir;
		ret[1] = wmfFile;
		return ret;
	}

	/**
	 * Check whether the given font is available.
	 * 
	 * @param familyName the font family name.
	 * @return true if the font is available.
	 */
	private static boolean isFontAvailable(String familyName) {
		return fonts.contains(familyName);
	}

}
