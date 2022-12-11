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

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Document;

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.XMLAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;

/**
 * Checks for regressions in rendering a specific SVG document. The
 * <code>Test</code> will rasterize and SVG document and compare it to a
 * reference image. The test passes if the rasterized SVG and the reference
 * image match exactly (i.e., all pixel values are the same).
 *
 * @author <a href="mailto:vhardy@apache.lorg">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGRenderingAccuracyTest extends AbstractRenderingAccuracyTest {

	/**
	 * Controls whether or not the SVG file should be validated. By default, no
	 * validation is used.
	 */
	private boolean validate = false;

	/**
	 * The userLanguage for which the document should be tested.
	 */
	private String userLanguage;

	/**
	 * Constructor.
	 * 
	 * @param svgURL    the URL String for the SVG document being tested.
	 * @param refImgURL the URL for the reference image.
	 * @throws MalformedURLException 
	 */
	public SVGRenderingAccuracyTest(String svgURL, String refImgURL) throws MalformedURLException {
		super(svgURL, refImgURL);
	}

	/**
	 * For subclasses
	 */
	protected SVGRenderingAccuracyTest() {
	}

	/**
	 * If true, this test will use validation
	 */
	public void setValidating(Boolean validate) {
		if (validate == null) {
			throw new IllegalArgumentException();
		}
		this.validate = validate;
	}

	public boolean getValidating() {
		return validate;
	}

	/**
	 * Sets the userLanguage
	 */
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public String getUserLanguage() {
		return this.userLanguage;
	}

	/**
	 * Template method which subclasses can override if they need to manipulate the
	 * DOM in some way before running the accuracy test. For example, this can be
	 * useful to test the alternate stylesheet support.
	 */
	protected Document manipulateSVGDocument(Document doc) {
		return doc;
	}

	@Override
	protected void encode(URL srcURL, FileOutputStream fos) throws TranscoderException, IOException {
		ImageTranscoder transcoder = getTestImageTranscoder();
		TranscoderInput src = getTranscoderInput();
		TranscoderOutput dst = new TranscoderOutput(fos);
		transcoder.transcode(src, dst);
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = new InternalPNGTranscoder();
		t.addTranscodingHint(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE, Boolean.FALSE);
		t.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, new Color(0, 0, 0, 0));
		t.addTranscodingHint(SVGAbstractTranscoder.KEY_EXECUTE_ONLOAD, Boolean.TRUE);

		if (validate) {
			t.addTranscodingHint(XMLAbstractTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.TRUE);
		}

		if (userLanguage != null) {
			t.addTranscodingHint(SVGAbstractTranscoder.KEY_LANGUAGE, userLanguage);
		}
		return t;
	}

	/**
	 * Returns the <code>TranscoderInput</code> that the Test should use,
	 * 
	 * @return the <code>TranscoderInput</code>.
	 * @throws IOException if an I/O error happened creating the input.
	 */
	TranscoderInput getTranscoderInput() throws IOException {
		return new TranscoderInput(getURI());
	}

	/**
	 * Inner class which derives from the PNGTranscoder and calls the
	 * manipulateSVGDocument just before encoding happens.
	 */
	protected class InternalPNGTranscoder extends PNGTranscoder {
		/**
		 * Transcodes the specified Document as an image in the specified output.
		 *
		 * @param document the document to transcode
		 * @param uri      the uri of the document or null if any
		 * @param output   the ouput where to transcode
		 * @exception TranscoderException if an error occured while transcoding
		 */
		@Override
		protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
			SVGRenderingAccuracyTest.this.manipulateSVGDocument(document);
			super.transcode(document, uri, output);
		}
	}
}
