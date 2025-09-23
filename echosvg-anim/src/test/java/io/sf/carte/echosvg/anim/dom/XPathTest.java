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
package io.sf.carte.echosvg.anim.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathResult;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;

/**
 * XPath tests.
 */
public class XPathTest {

	private static final String SVG_DOCUMENT = "<?xml version=\"1.0\" standalone=\"no\"?>\n"
			+ "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n"
			+ "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n"
			+ "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"200\" height=\"200\">\n"
			+ "<rect x=\"5\" y=\"5\" width=\"190\" height=\"190\" fill=\"#daf\" />\n"
			+ "<text x=\"20\" y=\"30\">foo</text><text x=\"120\" y=\"70\">bar</text>\n</svg>";

	@Test
	void testXPathIterator() {
		Document doc = createDocument(SVG_DOCUMENT);

		XPathResult res = (XPathResult) ((XPathEvaluator) doc).evaluate(".//*[local-name()=\"text\"]",
				doc.getDocumentElement(), null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);

		Node node = res.iterateNext();

		assertNotNull(node, "Node with content 'foo' not found.");
		assertEquals("foo", node.getTextContent().trim());

		node = res.iterateNext();
		assertNotNull(node, "Node with content 'bar' not found.");
		assertEquals("bar", node.getTextContent().trim());

		assertNull(res.iterateNext(), "Unexpected node.");
	}

	@Test
	void testXPathSnapshot() {
		Document doc = createDocument(SVG_DOCUMENT);

		XPathResult res = (XPathResult) ((XPathEvaluator) doc).evaluate(".//*[local-name()=\"text\"]",
				doc.getDocumentElement(), null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);

		assertEquals(2, res.getSnapshotLength());

		Node node = res.snapshotItem(0);

		assertNotNull(node, "Node with content 'foo' not found.");
		assertEquals("foo", node.getTextContent().trim());

		node = res.snapshotItem(1);
		assertNotNull(node, "Node with content 'bar' not found.");
		assertEquals("bar", node.getTextContent().trim());

		assertNull(res.snapshotItem(2), "Unexpected node.");
	}

	private static Document createDocument(String xml) {
		StringReader re = new StringReader(xml);
		SAXDocumentFactory f = new SAXSVGDocumentFactory();
		f.setValidating(false);
		XMLReader reader = f.getXMLReader();
		try {
			reader.setProperty("jdk.xml.maxParameterEntitySizeLimit", 0xffff);
		} catch (SAXNotRecognizedException | SAXNotSupportedException e) {
			throw new IllegalStateException(e);
		}

		try {
			return f.createDocument("file:///memory", re);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
