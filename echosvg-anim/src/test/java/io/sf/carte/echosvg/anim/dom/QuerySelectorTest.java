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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import io.sf.carte.echosvg.dom.AbstractElement;
import io.sf.carte.echosvg.dom.AbstractParentNode;

/**
 * Test the matches, querySelector and querySelectorAll methods.
 *
 * @version $Id$
 */
public class QuerySelectorTest {

	@Test
	public void testTypeSelectors() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "elt2", "elt2", "elt2",
				1);
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "elt4", "elt4", "", 1);
	}

	@Test
	public void testAttributeSelectors() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "[attr='value1']", "doc",
				"root", 1);
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "[attr2='value2']",
				"elt2", "elt2", 1);
	}

	@Test
	public void testIdSelectors() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "elt2#elt2", "elt2",
				"elt2", 1);
	}

	@Test
	public void testPseudoClassEmpty() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", ":empty", "elt1", "", 2);
	}

	@Test
	public void testPseudoClassRoot() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", ":root", "doc", "root",
				1);
	}

	@Test
	public void testPseudoClassLastChild() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", ":last-child", "doc",
				"root", 3);
	}

	@Test
	public void testPseudoClassFirstLastChild() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml",
				":first-child,:last-child", "doc", "root", 4);
	}

	@Test
	public void testPseudoClassNthChild() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", ":nth-child(3)", "elt3",
				"", 1);
	}

	@Test
	public void testDescendant() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "doc elt4", "elt4", "",
				1);
	}

	@Test
	public void testChild() throws SAXException, IOException {
		testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "elt3>elt4", "elt4", "",
				1);
	}

	@Test
	public void testSyntaxError() throws IOException {
		DOMException ex = assertThrows(DOMException.class,
				() -> testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "! . !", "",
						"", 0));
		assertEquals(DOMException.SYNTAX_ERR, ex.code);
	}

	@Test
	public void testNSError() throws IOException {
		DOMException ex = assertThrows(DOMException.class,
				() -> testQuerySelector("io/sf/carte/echosvg/anim/dom/dummyXML4.xml", "n|doc", "",
						"", 0));
		assertEquals(DOMException.NAMESPACE_ERR, ex.code);
	}

	void testQuerySelector(String testFileName, String selector, String tagName,
			String nodeID, int listCount) throws SAXException, IOException {
		DocumentBuilder df = new SAXSVGDocumentFactory();

		// Load the document
		Document doc;
		URL url = getClass().getClassLoader().getResource(testFileName);
		InputSource source = new InputSource(url.toString());
		try (InputStream is = url.openStream()) {
			source.setByteStream(is);
			doc = df.parse(source);
		}

		AbstractParentNode docNode = (AbstractParentNode) doc;

		// querySelector
		Element elt = docNode.querySelector(selector);

		assertNotNull(elt);
		assertEquals(tagName, elt.getTagName());
		assertEquals(nodeID, elt.getAttribute("id"));
		assertTrue(((AbstractElement) elt).matches(selector));

		// querySelectorAll
		NodeList list = docNode.querySelectorAll(selector);

		assertNotNull(list);
		assertEquals(listCount, list.getLength());
		assertSame(elt, list.item(0));
	}

}
