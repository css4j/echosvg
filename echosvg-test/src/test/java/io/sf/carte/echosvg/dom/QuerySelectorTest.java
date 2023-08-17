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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;

/**
 * Test the matches, querySelector and querySelectorAll methods.
 *
 * @version $Id$
 */
public class QuerySelectorTest {

	@Test
	public void testTypeSelectors() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", "elt2", "elt2", "elt2",
				1);
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", "elt4", "elt4", "", 1);
	}

	@Test
	public void testAttributeSelectors() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", "[attr='value1']", "doc",
				"root", 1);
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", "[attr2='value2']",
				"elt2", "elt2", 1);
	}

	@Test
	public void testIdSelectors() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", "elt2#elt2", "elt2",
				"elt2", 1);
	}

	@Test
	public void testPseudoClassEmpty() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", ":empty", "elt1", "", 2);
	}

	@Test
	public void testPseudoClassRoot() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", ":root", "doc", "root",
				1);
	}

	@Test
	public void testPseudoClassLastChild() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", ":last-child", "doc",
				"root", 3);
	}

	@Test
	public void testPseudoClassFirstLastChild() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc",
				":first-child,:last-child", "doc", "root", 4);
	}

	@Test
	public void testPseudoClassNthChild() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", ":nth-child(3)", "elt3",
				"", 1);
	}

	@Test
	public void testDescendant() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", "doc elt4", "elt4", "",
				1);
	}

	@Test
	public void testChild() throws IOException {
		testQuerySelector("io/sf/carte/echosvg/dom/dummyXML4.xml", "doc", "elt3>elt4", "elt4", "",
				1);
	}

	void testQuerySelector(String testFileName, String rootTag, String selector, String tagName,
			String nodeID, int listCount) throws IOException {
		DocumentFactory df = new SAXDocumentFactory(
				GenericDOMImplementation.getDOMImplementation());

		// Load the document
		URL url = getClass().getClassLoader().getResource(testFileName);
		Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());

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
