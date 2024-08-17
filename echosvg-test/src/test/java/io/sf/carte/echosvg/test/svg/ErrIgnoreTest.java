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

import java.io.IOException;
import java.net.MalformedURLException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.bridge.BridgeException;
import io.sf.carte.echosvg.bridge.ErrorConstants;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.transcoder.ErrorHandler;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.test.DummyErrorHandler;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Checks for regressions in rendering of SVG, ignoring reported errors.
 *
 * @author See Git history.
 * @version $Id$
 */
public class ErrIgnoreTest extends RenderingTest {

	private final int expectedErrorCount;

	public ErrIgnoreTest(int expectedErrorCount) {
		super();
		this.expectedErrorCount = expectedErrorCount;
		setValidating(Boolean.FALSE);
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	@Override
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = super.getTestImageTranscoder();
		t.setErrorHandler(new DummyErrorHandler());
		return t;
	}

	@Override
	ImageTranscoder createTestImageTranscoder() {
		return new ErrIgnoreTranscoder();
	}

	@Override
	protected void checkErrorHandler(ErrorHandler errorHandler) {
		DummyErrorHandler handler = (DummyErrorHandler) errorHandler;
		handler.assertErrorCount(expectedErrorCount);
		super.checkErrorHandler(errorHandler);
	}

	class ErrIgnoreTranscoder extends InternalPNGTranscoder {

		@Override
		protected UserAgent createUserAgent() {
			return new BrokenLinkUserAgent();
		}

		class BrokenLinkUserAgent
				extends InternalPNGTranscoder.TestTranscoderUserAgent {

			public BrokenLinkUserAgent() {
				super();
			}

			/**
			 * Load a broken link document.
			 *
			 * @param elem    The &lt;image&gt; element that can't be loaded.
			 * @param url     The resolved url that can't be loaded.
			 * @param message As best as can be determined the reason it can't be loaded
			 *                (not available, corrupt, unknown format,...).
			 */
			@Override
			public SVGDocument getBrokenLinkDocument(Element elem, String url, String message) {
				// Report the exception
				BridgeException be = new BridgeException(ctx, elem,
						ErrorConstants.ERR_URI_IMAGE_BROKEN, new Object[] { url, message });
				displayError(be);

				// Retrieve the broken link document
				String templateUri;
				try {
					templateUri = TestLocations.resolveURL("samples/ref-me.svg")
							.toExternalForm();
				} catch (MalformedURLException ex) {
					templateUri = "file:samples/ref-me.svg";
				}

				// Obtain a document factory
				DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
				DocumentFactory factory = new SAXDocumentFactory(domImpl);

				// Load the template
				SVGDocument doc;
				try {
					doc = (SVGDocument) factory.createDocument(SVGConstants.SVG_NAMESPACE_URI,
							SVGConstants.SVG_SVG_TAG, templateUri, "utf-8");
				} catch (IOException ex) {
					return super.getBrokenLinkDocument(elem, url, message);
				}

				/*
				 * In the future an actual template could be used, eventually filled with the
				 * message.
				 */

				return doc;
			}

		}

	}

}
