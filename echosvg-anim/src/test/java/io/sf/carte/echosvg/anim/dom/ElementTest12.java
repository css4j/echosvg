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

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ElementTest12 {

	@Test
	public void testNoPrefix() throws IOException {
		testCreateElement("http://www.example.com/examplens", "doc", "doc");
	}

	@Test
	public void testPrefix() throws IOException {
		testCreateElement("http://www.example.com/examplens", "ex:doc", "doc");
	}

	void testCreateElement(String namespaceURI, String qName, String localName) {
		DOMImplementation domImpl = SVG12DOMImplementation.getDOMImplementation();

		Document doc = domImpl.createDocument(namespaceURI, qName, null);

		Element elm = doc.getDocumentElement();

		assertNotNull(elm);

		assertEquals(qName, elm.getNodeName());
		assertEquals(qName, elm.getTagName());
		assertEquals(namespaceURI, elm.getNamespaceURI());
		assertEquals(localName, elm.getLocalName());
	}

}
