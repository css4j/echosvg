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

package io.sf.carte.echosvg.transcoder;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.XMLReader;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This test validates that the various configurations of TranscoderInput are
 * supported by the XMLAbstractTranscoder class.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TranscoderInputTest {

	private static final String TEST_URI =  TestLocations.getRootBuildURL() + "samples/anne.svg";

	@Test
	public void test() throws Exception {
		TestTranscoder t = new TestTranscoder();

		TranscoderOutput out = new TranscoderOutput(new StringWriter());

		// XMLReader
		{
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			saxFactory.setValidating(false);
			SAXParser parser = saxFactory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			TranscoderInput ti = new TranscoderInput(xmlReader);
			ti.setURI(TEST_URI);
			t.transcode(ti, out);
			assertTrue(t.passed);
		}

		// Input Stream
		{
			URL uri = new URL(TEST_URI);
			InputStream is = uri.openStream();
			TranscoderInput ti = new TranscoderInput(is);
			ti.setURI(TEST_URI);
			t = new TestTranscoder();
			t.transcode(ti, out);
			assertTrue(t.passed);
		}

		// Reader
		{
			URL uri = new URL(TEST_URI);
			InputStream is = uri.openStream();
			Reader r = new InputStreamReader(is);
			TranscoderInput ti = new TranscoderInput(r);
			ti.setURI(TEST_URI);
			t = new TestTranscoder();
			t.transcode(ti, out);
			assertTrue(t.passed);
		}
		// Document
		{
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory();
			Document doc = f.createDocument(TEST_URI);
			TranscoderInput ti = new TranscoderInput(doc);
			ti.setURI(TEST_URI);
			t = new TestTranscoder();
			t.transcode(ti, out);
			assertTrue(t.passed);
		}

		// Generic Document
		{
			DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
			SAXDocumentFactory f = new SAXDocumentFactory(impl);
			Document doc = f.createDocument(TEST_URI);
			TranscoderInput ti = new TranscoderInput(doc);
			ti.setURI(TEST_URI);
			t = new TestTranscoder();
			t.transcode(ti, out);
			assertTrue(t.passed);
		}

		// URI only
		{
			TranscoderInput ti = new TranscoderInput(TEST_URI);
			t = new TestTranscoder();
			t.transcode(ti, out);
			assertTrue(t.passed);
		}

	}

	static class TestTranscoder extends XMLAbstractTranscoder {
		boolean passed = false;

		public TestTranscoder() {
			addTranscodingHint(KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
			addTranscodingHint(KEY_DOCUMENT_ELEMENT, SVGConstants.SVG_SVG_TAG);
			addTranscodingHint(KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
		}

		@Override
		protected void transcode(Document document, String uri, TranscoderOutput output) {
			passed = (document != null);
		}
	}

}
