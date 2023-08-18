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

package io.sf.carte.echosvg.transcoder.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;

/**
 * Test several exceptions thrown by {@code CSSTranscodingHelper}.
 *
 * @author github.com/carlosame
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class CSSTranscodingHelperTest {

	@Test
	public void testNoReaderNoURI() throws Exception {
		CSSTranscodingHelper helper = new CSSTranscodingHelper();
		TranscoderOutput output = new TranscoderOutput();

		MalformedURLException ex = assertThrows(MalformedURLException.class,
				() -> helper.transcode(null, null, output, null));

		Throwable cause = ex.getCause();
		assertNotNull(cause);
		assertEquals(NullPointerException.class, cause.getClass());
		assertNull(cause.getCause());
	}

	@Test
	public void testNoDocument() throws Exception {
		CSSTranscodingHelper helper = new CSSTranscodingHelper();
		TranscoderOutput output = new TranscoderOutput();

		assertThrows(NullPointerException.class,
				() -> helper.transcodeDocument(null, output, null));
	}

	@Test
	public void testEmptyDocument() throws Exception {
		CSSTranscodingHelper helper = new CSSTranscodingHelper();
		TranscoderOutput output = new TranscoderOutput();
		Document document = newDocument();

		assertThrows(IllegalArgumentException.class,
				() -> helper.transcodeDocument(document, output, null));
	}

	@Test
	public void testNullTranscoderInput() throws Exception {
		CSSTranscodingHelper helper = new CSSTranscodingHelper();
		TranscoderOutput output = new TranscoderOutput();

		assertThrows(NullPointerException.class, () -> helper.transcode(null, output));
	}

	@Test
	public void testEmptyTranscoderInput() throws Exception {
		CSSTranscodingHelper helper = new CSSTranscodingHelper();
		TranscoderInput input = new TranscoderInput();
		TranscoderOutput output = new TranscoderOutput();

		assertThrows(IllegalArgumentException.class, () -> helper.transcode(input, output));
	}

	@Test
	public void testTranscoderInputEmptyDocument() throws Exception {
		CSSTranscodingHelper helper = new CSSTranscodingHelper();
		TranscoderInput input = new TranscoderInput();
		TranscoderOutput output = new TranscoderOutput();
		Document document = newDocument();
		input.setDocument(document);

		assertThrows(IllegalArgumentException.class, () -> helper.transcode(input, output));
	}

	private Document newDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}

}
