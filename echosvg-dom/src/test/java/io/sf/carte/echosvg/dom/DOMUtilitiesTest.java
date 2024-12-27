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
package io.sf.carte.echosvg.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.util.SVGConstants;

public class DOMUtilitiesTest {

	@Test
	public void testIsValidName() throws IOException {
		assertTrue(DOMUtilities.isValidName("foo"));
		assertTrue(DOMUtilities.isValidName("foo-bar"));
		assertTrue(DOMUtilities.isValidName("_foo"));
		assertFalse(DOMUtilities.isValidName("-foo"));
		assertFalse(DOMUtilities.isValidName("9-foo"));
		assertFalse(DOMUtilities.isValidName("a\ud83d\udc49"));
	}

	@Test
	public void testIsValidName11() throws IOException {
		assertTrue(DOMUtilities.isValidName11("foo"));
		// assertTrue(DOMUtilities.isValidName11("foo-bar"));
		// assertTrue(DOMUtilities.isValidName11("_foo"));
		assertFalse(DOMUtilities.isValidName11("-foo"));
		assertFalse(DOMUtilities.isValidName11("9-foo"));
		assertFalse(DOMUtilities.isValidName11("a\ud83d\udc49"));
	}

	@Test
	public void testContentToString() throws IOException {
		String data = "\ud83d\udc49";
		String result = DOMUtilities.contentToString(data, false);
		assertEquals(data, result);
	}

	@Test
	public void testContentToString11() throws IOException {
		String data = "\ud83d\udc49";
		String result = DOMUtilities.contentToString(data, true);
		assertEquals(data, result);
	}

	@Test
	public void testWriteDocument() throws DOMException, IOException {
		DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
		Document doc = impl.createDocument(SVGConstants.SVG_NAMESPACE_URI, "svg", null);

		Element svg = doc.getDocumentElement();
		Element text = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "text");
		text.setAttributeNS(null, "id", "idText");
		svg.appendChild(text);

		String data = "\ud83d\udc49";

		Text surrogate = doc.createTextNode(data);
		text.appendChild(surrogate);
		assertEquals(data, surrogate.getData());

		Element text2 = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "text");
		text2.setAttributeNS(null, "id", "idCdata");
		svg.appendChild(text2);

		CDATASection cdata = doc.createCDATASection(data);
		text2.appendChild(cdata);
		assertEquals(data, cdata.getData());

		StringWriter stringWriter = new StringWriter();

		DOMUtilities.writeDocument(doc, stringWriter);

		SAXDocumentFactory f = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation());
		doc = f.createDocument(
				"https://raw.githubusercontent.com/css4j/echosvg/master/samples/foo.svg",
				new StringReader(stringWriter.toString()));

		text = doc.getElementById("idText");
		String content = text.getTextContent();
		Node firstChild = text.getFirstChild();
		assertEquals(data, firstChild.getNodeValue());
		assertEquals(data, content);

		text2 = doc.getElementById("idCdata");
		content = text2.getTextContent();
		firstChild = text2.getFirstChild();
		assertEquals(data, firstChild.getNodeValue());
		assertEquals(data, content);
	}

}
