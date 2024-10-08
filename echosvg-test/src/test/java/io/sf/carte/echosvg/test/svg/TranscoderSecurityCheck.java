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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.sf.carte.echosvg.transcoder.ErrorHandler;
import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;
import io.sf.carte.echosvg.transcoder.test.DummyErrorHandler;

/**
 * Check transcoder security.
 * 
 * <p>
 * Use it to transcode an image that is expected to throw a SecurityException.
 * No attempt is made to compare the result with a reference.
 * </p>
 * 
 * @version $Id$
 */
public class TranscoderSecurityCheck {

	private final int expectedErrorCount;

	public TranscoderSecurityCheck() {
		this(0);
	}

	public TranscoderSecurityCheck(int expectedErrorCount) {
		super();
		this.expectedErrorCount = expectedErrorCount;
	}

	public void runTest(String file) throws TranscoderException, IOException {
		SVGAbstractTranscoder transcoder = getTestImageTranscoder();

		ErrorHandler errHandler = createErrorHandler();
		transcoder.setErrorHandler(errHandler);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TranscoderOutput dst = new TranscoderOutput(os);

		InputStream is = getSourceStream(file);
		if (is == null) {
			throw new IOException("Null stream for " + file);
		}
		TranscoderInput inp = new TranscoderInput(is);
		inp.setEncoding("utf-8");

		try {
			transcoder.transcode(inp, dst);
		} finally {
			is.close();
			checkErrorHandler(errHandler);
		}
	}

	protected InputStream getSourceStream(String file) {
		return getClass().getResourceAsStream(file);
	}

	private SVGAbstractTranscoder getTestImageTranscoder() {
		SVGAbstractTranscoder t = createTestImageTranscoder();
		t.addTranscodingHint(ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE, Boolean.FALSE);
		t.addTranscodingHint(SVGAbstractTranscoder.KEY_EXECUTE_ONLOAD, Boolean.TRUE);
		return t;
	}

	SVGAbstractTranscoder createTestImageTranscoder() {
		return new PNGTranscoder();
	}

	private ErrorHandler createErrorHandler() {
		return new DummyErrorHandler();
	}

	private void checkErrorHandler(ErrorHandler errHandler) {
		((DummyErrorHandler) errHandler).assertErrorCount(expectedErrorCount);
	}

}
