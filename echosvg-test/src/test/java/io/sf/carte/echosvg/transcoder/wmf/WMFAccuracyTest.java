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

import static org.junit.Assert.fail;

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Assume;
import org.junit.Test;

import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.wmf.tosvg.WMFTranscoder;

/**
 * This test validates that a given WMF file is properly converted to an SVG
 * document by comparing the generated SVG document to a known, valid SVG
 * reference.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class WMFAccuracyTest {

	private static final String GENERATOR_REFERENCE_BASE = TestLocations.PROJECT_ROOT_URL
			+ "test-references/io/sf/carte/echosvg/transcoder/wmf";

	private static final String CANDIDATE_REF_DIR = "candidate";

	private static final String WMF_EXTENSION = ".wmf";
	private static final String SVG_EXTENSION = ".svg";
	private static final char PATH_SEPARATOR = '/';

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

	private boolean hasArial = isFontAvailable("Arial");

	private boolean hasCourierNew = isFontAvailable("Courier New");

	private static boolean isFontAvailable(String familyName) {
		List<String> fonts = Arrays
				.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		return fonts.contains(familyName);
	}

	@Test
	public void testBlackShapes() throws IOException, TranscoderException {
		Assume.assumeTrue("'Courier' font is not available", isFontAvailable("Courier"));
		runTest("samples/tests/resources/wmf/black_shapes.wmf");
	}

	@Test
	public void testImageWMF() throws IOException, TranscoderException {
		runTest("samples/tests/resources/wmf/imageWMF.wmf");
	}

	@Test
	public void testNegApmText1() throws IOException, TranscoderException {
		Assume.assumeTrue("Arial font is not available", hasArial);
		runTest("samples/tests/resources/wmf/negApmText1.wmf");
	}

	@Test
	public void testNegApmText2() throws IOException, TranscoderException {
		Assume.assumeTrue("Arial font is not available", hasArial);
		runTest("samples/tests/resources/wmf/negApmText2.wmf");
	}

	@Test
	public void testChart() throws IOException, TranscoderException {
		Assume.assumeTrue("Arial font is not available", hasArial);
		Assume.assumeTrue("'Courier New' font is not available", hasCourierNew);
		runTest("samples/tests/resources/wmf/testChart.wmf");
	}

	@Test
	public void testTextGreek() throws IOException, TranscoderException {
		Assume.assumeTrue("'Courier New' font is not available", hasCourierNew);
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
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		WMFTranscoder wmft = new WMFTranscoder();
		TranscoderInput input;
		input = new TranscoderInput(wmfURL.toString());

		OutputStreamWriter outW;
		outW = new OutputStreamWriter(out, "UTF-8");
		TranscoderOutput output = new TranscoderOutput(outW);

		wmft.transcode(input, output);

		byte[] data = out.toByteArray();
		String failMessage = TestUtil.xmlDiff(refURL, data, saveSVG);

		if (failMessage != null) {
			fail(failMessage);
		}
	}

	private void setFile(String id) throws MalformedURLException {
		String wmfFile = id;

		String[] dirNfile = breakWMFFile(wmfFile);

		wmfURL = new URL(TestLocations.PROJECT_ROOT_URL + dirNfile[0]+ dirNfile[1] + dirNfile[2]);
		refURL = new URL(GENERATOR_REFERENCE_BASE + '/' + dirNfile[1] + SVG_EXTENSION);
		String saveURL = new URL(GENERATOR_REFERENCE_BASE + '/' + CANDIDATE_REF_DIR + "/" + dirNfile[1] + SVG_EXTENSION).getFile();
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

}
