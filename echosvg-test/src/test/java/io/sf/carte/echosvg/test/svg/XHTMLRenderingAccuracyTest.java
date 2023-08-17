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

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.XMLAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;

/**
 * Checks for regressions in rendering of SVG inside an XHTML document.
 *
 * @author See Git history.
 * @version $Id$
 */
public class XHTMLRenderingAccuracyTest extends RenderingTest {

	private final String selector;

	public XHTMLRenderingAccuracyTest() {
		this(null);
	}

	public XHTMLRenderingAccuracyTest(String selector) {
		super();
		this.selector = selector;
		setValidating(false);
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	@Override
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = super.getTestImageTranscoder();
		t.addTranscodingHint(XMLAbstractTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI,
				"http://www.w3.org/1999/xhtml");
		t.addTranscodingHint(XMLAbstractTranscoder.KEY_DOCUMENT_ELEMENT, "html");
		if (selector != null) {
			t.addTranscodingHint(SVGAbstractTranscoder.KEY_SVG_SELECTOR, selector);
		}
		return t;
	}

}
