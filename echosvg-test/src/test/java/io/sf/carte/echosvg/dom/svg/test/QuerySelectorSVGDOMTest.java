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
package io.sf.carte.echosvg.dom.svg.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.dom.AbstractElement;
import io.sf.carte.echosvg.dom.AbstractParentNode;
import io.sf.carte.echosvg.dom.ArrayNodeList;

/**
 * This class tests the querySelector method in the SVG DOM implementation.
 *
 * @version $Id$
 */
public class QuerySelectorSVGDOMTest {

	@Test
	public void test() throws IOException {
		runTest("io/sf/carte/echosvg/dom/svg/test.svg", "#nodeID");
		runTest("io/sf/carte/echosvg/dom/svg/test.svg", "svg");
	}

	void runTest(String testFileName, String selector) throws IOException {
		SAXSVGDocumentFactory df = new SAXSVGDocumentFactory();

		// Load the document
		URL url = getClass().getClassLoader().getResource(testFileName);
		Document doc = df.createDocument(url.toString(), url.openStream(), "utf-8");

		AbstractParentNode docNode = (AbstractParentNode) doc;

		// querySelector
		Element elt = docNode.querySelector(selector);

		assertNotNull(elt);
		assertEquals("svg", elt.getLocalName());
		assertEquals("nodeID", elt.getAttribute("id"));

		// querySelectorAll
		NodeList list = docNode.querySelectorAll(selector);

		assertNotNull(list);
		assertEquals(1, list.getLength());
		assertSame(elt, list.item(0));

		// Test the iterator
		Iterator<Node> it = ((ArrayNodeList) list).iterator();
		Node n = it.next();
		assertTrue(((AbstractElement) n).matches(selector));
		assertFalse(it.hasNext());
		assertThrows(UnsupportedOperationException.class, () -> it.remove());
	}

}
