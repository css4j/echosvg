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

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Document;

import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.test.image.ImageComparator;
import io.sf.carte.echosvg.transcoder.ErrorHandler;
import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.XMLAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Checks for regressions in rendering a specific SVG document. The
 * <code>Test</code> will rasterize and SVG document and compare it to a
 * reference image. The test passes if the rasterized SVG and the reference
 * image match exactly (i.e., all pixel values are the same).
 *
 * <p>
 * Original author: <a href="mailto:vhardy@apache.lorg">Vincent Hardy</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGRenderingAccuracyTest extends AbstractRenderingAccuracyTest {

	static final String DEFAULT_MEDIUM = SVGConstants.SVG_SCREEN_VALUE;

	/**
	 * Threshold to apply when comparing different pixels with
	 * {@link ImageComparator}.
	 */
	private static final int PIXEL_THRESHOLD = 8;

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
	 * The media for which the document should be tested.
	 */
	private String media;

	/*
	 * Enable or disable dark mode.
	 */
	private boolean darkMode = false;

	// Batik uses 9
	private static final int DEFAULT_COMPRESSION_LEVEL = 9;

	/**
	 * The PNG compression level.
	 */
	private int comprLevel = getDefaultCompressionLevel();

	/**
	 * To set the tEXt chunk.
	 */
	private String[] tEXt;

	/**
	 * To set the iTXt chunk.
	 */
	private String[] iTXt;

	/**
	 * To set the zTXt chunk.
	 */
	private String[] zTXt;

	/**
	 * Constructor.
	 * 
	 * @param svgURL    the URL String for the SVG document being tested.
	 * @param refImgURL the URL for the reference image.
	 * @throws MalformedURLException 
	 */
	public SVGRenderingAccuracyTest(String svgURL, String refImgURL) throws MalformedURLException {
		super(PIXEL_THRESHOLD, svgURL, refImgURL);
	}

	/**
	 * For subclasses
	 */
	protected SVGRenderingAccuracyTest() {
		super(PIXEL_THRESHOLD);
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

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	/**
	 * Enable or disable the dark mode.
	 * 
	 * @param darkMode {@code true} to enable the dark mode.
	 */
	public void setDarkMode(boolean darkMode) {
		this.darkMode = darkMode;
	}

	/**
	 * Set the compression level.
	 * 
	 * @param comprLevel the compression level.
	 */
	public void setCompressionLevel(int comprLevel) {
		this.comprLevel = comprLevel;
	}

	/**
	 * 
	 * @return the compression level.
	 */
	protected int getCompressionLevel() {
		return comprLevel;
	}

	protected int getDefaultCompressionLevel() {
		return DEFAULT_COMPRESSION_LEVEL;
	}

	public void setText(String[] tEXt) {
		this.tEXt = tEXt;
	}

	protected String[] getText() {
		return tEXt;
	}

	public void setInternationalText(String[] iTXt) {
		this.iTXt = iTXt;
	}

	protected String[] getInternationalText() {
		return iTXt;
	}

	public void setCompressedText(String[] zTXt) {
		this.zTXt = zTXt;
	}

	protected String[] getCompressedText() {
		return zTXt;
	}

	@Override
	protected CharSequence getImageSuffix() {
		boolean nonDefCompr = getCompressionLevel() != getDefaultCompressionLevel();
		boolean nonDefMedia = media != null && !DEFAULT_MEDIUM.equals(media);

		if (nonDefCompr && nonDefMedia && !darkMode && tEXt == null && iTXt == null && zTXt == null) {
			return "";
		}

		StringBuilder buf = new StringBuilder();
		if (nonDefMedia) {
			buf.append("-").append(media);
		}

		if (darkMode) {
			buf.append("-dark");
		}

		if (nonDefCompr) {
			buf.append("-z").append(getCompressionLevel());
		}

		if (tEXt != null) {
			buf.append("-text");
			if (iTXt != null) {
				buf.append("-i");
			}
			if (zTXt != null) {
				buf.append("-z");
			}
		} else {
			if (iTXt != null) {
				if (zTXt != null) {
					buf.append("-iztext");
				} else {
					buf.append("-itext");
				}
			} else if (zTXt != null) {
				buf.append("-ztext");
			}
		}

		return buf;
	}

	@Override
	protected void failTest(String message) {
		fail(message);
	}

	@Override
	protected String getProjectName() {
		return TestLocations.TEST_DIRNAME;
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
		checkErrorHandler(transcoder.getErrorHandler());
		fos.getChannel().force(false);
	}

	protected void checkErrorHandler(ErrorHandler errorHandler) {
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = createTestImageTranscoder();
		t.addTranscodingHint(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE, Boolean.FALSE);

		if (darkMode) {
			// Opaque background for dark mode
			t.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, new Color(0, 0, 0, 255));
		} else {
			t.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, new Color(0, 0, 0, 0));
		}

		t.addTranscodingHint(SVGAbstractTranscoder.KEY_EXECUTE_ONLOAD, Boolean.TRUE);

		if (validate) {
			t.addTranscodingHint(XMLAbstractTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.TRUE);
		}

		if (userLanguage != null) {
			t.addTranscodingHint(SVGAbstractTranscoder.KEY_LANGUAGE, userLanguage);
		}

		if (media != null) {
			t.addTranscodingHint(SVGAbstractTranscoder.KEY_MEDIA, media);
		}

		if (darkMode) {
			t.addTranscodingHint(SVGAbstractTranscoder.KEY_PREFERS_COLOR_SCHEME, "dark");
		}

		if (getCompressionLevel() != getDefaultCompressionLevel()) {
			t.addTranscodingHint(PNGTranscoder.KEY_COMPRESSION_LEVEL, getCompressionLevel());
		}

		if (tEXt != null) {
			t.addTranscodingHint(PNGTranscoder.KEY_KEYWORD_TEXT, tEXt);
		}

		if (iTXt != null) {
			t.addTranscodingHint(PNGTranscoder.KEY_INTERNATIONAL_TEXT, iTXt);
		}

		if (zTXt != null) {
			t.addTranscodingHint(PNGTranscoder.KEY_COMPRESSED_TEXT, zTXt);
		}

		return t;
	}

	ImageTranscoder createTestImageTranscoder() {
		return new InternalPNGTranscoder();
	}

	/**
	 * Returns the <code>TranscoderInput</code> that the Test should use,
	 * 
	 * @return the <code>TranscoderInput</code>.
	 * @throws IOException if an I/O error happened creating the input.
	 */
	TranscoderInput getTranscoderInput() throws IOException {
		TranscoderInput inp = new TranscoderInput(getURI());
		inp.setEncoding("utf-8");
		return inp;
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

		@Override
		protected UserAgent createUserAgent() {
			return new FailOnErrorTranscoderUserAgent();
		}

		/**
		 * A Transcoder user agent that does not print a stack trace.
		 */
		class TestTranscoderUserAgent
				extends SVGAbstractTranscoder.SVGAbstractTranscoderUserAgent {
		}

		/**
		 * A Transcoder user agent that does not print a stack trace.
		 */
		class FailOnErrorTranscoderUserAgent extends TestTranscoderUserAgent {

			@Override
			public void displayError(String message) {
				super.displayError(message);
				throw new RuntimeException(message);
			}

			/**
			 * Displays the specified error using the <code>ErrorHandler</code>.
			 * <p>
			 * And does not print a stack trace.
			 * </p>
			 */
			@Override
			public void displayError(Exception e) {
				super.displayError(e);
				throw new RuntimeException(e);
			}

		}

	}

	class ErrIgnoreTranscoder extends InternalPNGTranscoder {

		@Override
		protected UserAgent createUserAgent() {
			return new InternalPNGTranscoder.TestTranscoderUserAgent();
		}

	}

}
