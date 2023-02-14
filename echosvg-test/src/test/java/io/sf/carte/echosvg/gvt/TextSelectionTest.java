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

package io.sf.carte.echosvg.gvt;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGTextContentElement;

import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.swing.svg.JSVGComponent;
import io.sf.carte.echosvg.swing.test.JSVGCanvasHandler;
import io.sf.carte.echosvg.test.svg.JSVGRenderingAccuracyTest;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test validates that the text selection API's work properly.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TextSelectionTest extends JSVGRenderingAccuracyTest {

	private String textID = null;
	private int start;
	private int end;

	@Test
	public void testTextSelectionBiDi() throws IOException, TranscoderException {
		runTextSelectionTest("samples/tests/spec/text/textBiDi.svg", "latin", 0, 20);
		runTextSelectionTest("samples/tests/spec/text/textBiDi.svg", "latin-extended", 0, 15);
		runTextSelectionTest("samples/tests/spec/text/textBiDi.svg", "cyrillic", 4, 24);
		runTextSelectionTest("samples/tests/spec/text/textBiDi.svg", "greek", 0, 35);
		runTextSelectionTest("samples/tests/spec/text/textBiDi.svg", "hebrew", 10, 20);
		runTextSelectionTest("samples/tests/spec/text/textBiDi.svg", "arabic", 5, 40);
	}

	@Test
	public void testTextSelectionFontOnPath() throws IOException, TranscoderException {
		runTextSelectionTest("samples/tests/spec/fonts/fontOnPath.svg", "middle50", 1, 13);
		runTextSelectionTest("samples/tests/spec/fonts/fontOnPath.svg", "start35", 0, 13);
	}

	@Test
	public void testTextSelectionVerticalTextOnPath() throws IOException, TranscoderException {
		runTextSelectionTest("samples/tests/spec/text/verticalTextOnPath.svg", "supersub", 3, 18);
		runTextSelectionTest("samples/tests/spec/text/verticalTextOnPath.svg", "beforeafter", 0, 28);
		runTextSelectionTest("samples/tests/spec/text/verticalTextOnPath.svg", "negpos", 3, 17);
		runTextSelectionTest("samples/tests/spec/text/verticalTextOnPath.svg", "orient0", 3, 18);
		runTextSelectionTest("samples/tests/spec/text/verticalTextOnPath.svg", "orient90", 3, 18);
		runTextSelectionTest("samples/tests/spec/text/verticalTextOnPath.svg", "orient180", 3, 18);
		runTextSelectionTest("samples/tests/spec/text/verticalTextOnPath.svg", "orient270", 3, 18);
	}

	/**
	 * Run test. ref is ignored if action == ROUND.
	 * 
	 * @param textID The element to select text from (must be a <text> element)
	 * @param start  The first character to select
	 * @param end    The last character to select
	 * @param file   The reference file.
	 * @throws MalformedURLException 
	 */
	public void runTextSelectionTest(String file, String textID, int start, int end) throws MalformedURLException {
		this.textID = textID;
		this.start = start;
		this.end = end;
		super.setFile(file);
	}

	@Override
	protected String buildRefImgURL(String svgDir, String svgFile) {
		return getRefImagePrefix() + svgDir + getRefImageSuffix() + svgFile + '-' + textID + '-' + start + '-' + end
				+ PNG_EXTENSION;
	}

	@Override
	public String[] buildVariationURLs(String svgDir, String svgFile) {
		return new String[] { getVariationPrefix() + svgDir + getVariationSuffix() + svgFile + '-' + textID + '-'
				+ start + '-' + end + PNG_EXTENSION };

	}

	@Override
	protected String buildSaveVariationPath(String svgDir, String svgFile) {
		return getSaveVariationPrefix() + svgDir + getSaveVariationSuffix() + svgFile + '-' + textID + '-' + start + '-'
				+ end;
	}

	@Override
	public String buildCandidateReferenceFile(String svgDir, String svgFile) {
		return getCandidateReferencePrefix() + svgDir + getCandidateReferenceSuffix() + svgFile + '-' + textID + '-'
				+ start + '-' + end + PNG_EXTENSION;
	}

	@Override
	protected JSVGCanvasHandler createCanvasHandler() {
		return new JSVGCanvasHandler(this, this) {
			@Override
			public JSVGCanvas createCanvas() {
				JSVGCanvas ret = new JSVGCanvas();
				ret.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);
				return ret;
			}
		};
	}

	@Override
	public void canvasRendered(JSVGCanvas canvas) {
		String errorDesc = null;
		try {
			Element e = canvas.getSVGDocument().getElementById(textID);
			if (e == null) {
				errorDesc = "Could not find element with id: " + textID;
			}
			if (!(e instanceof SVGTextContentElement)) {
				errorDesc = "Element with id " + textID + " is not a Text";
			}
			SVGTextContentElement tce = (SVGTextContentElement) e;
			tce.selectSubString(start, end);
		} finally {
			scriptDone();
		}
		if (errorDesc != null) {
			fail(errorDesc);
		}
	}

}
