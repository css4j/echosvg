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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.util.ParsedURL;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.sax.HtmlParser;

/**
 * Checks for regressions in rendering of SVG inside an HTML document.
 *
 * @author See Git history.
 * @version $Id$
 */
class HTMLRenderingAccuracyTest extends XHTMLRenderingAccuracyTest {

	private final HtmlParser parser;

	public HTMLRenderingAccuracyTest() {
		super();
		setValidating(false);
		parser = new HtmlParser(XmlViolationPolicy.ALTER_INFOSET);
		parser.setCommentPolicy(XmlViolationPolicy.ALLOW);
		parser.setXmlnsPolicy(XmlViolationPolicy.ALLOW);
	}

	/**
	 * Returns the <code>TranscoderInput</code> that the Test should use,
	 * 
	 * @return the <code>TranscoderInput</code>.
	 * @throws IOException if an I/O error happened creating the input.
	 */
	@Override
	TranscoderInput getTranscoderInput() throws IOException {
		ParsedURL purl = new ParsedURL(getURI());

		InputStream is = purl.openStream();

		TranscoderInput src = new TranscoderInput(new InputStreamReader(is, StandardCharsets.UTF_8));
		src.setURI(purl.getPostConnectionURL());

		src.setXMLReader(parser);

		return src;
	}

}
