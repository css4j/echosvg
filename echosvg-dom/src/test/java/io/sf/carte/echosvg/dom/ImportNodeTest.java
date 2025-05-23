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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class ImportNodeTest {

	@Test
	public void test() throws IOException {
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		String nsURI = "http://www.example.com/examplens";
		DocumentType docType = domImpl.createDocumentType("html", null, null);
		Document imported = domImpl.createDocument(nsURI, "doc", docType);

		Element elm = imported.getDocumentElement();

		assertNotNull(elm);

		elm.setAttribute("class", "cls");

		Element elm2 = imported.createElementNS(nsURI, "e2");

		elm.appendChild(elm2);

		Document doc = domImpl.createDocument("", null, null);

		Element impElm = (Element) doc.importNode(elm, true);
		assertNotNull(impElm);

		doc.appendChild(impElm);
		Element oelm = doc.getDocumentElement();
		assertNotNull(oelm);
		assertSame(impElm, oelm);

		assertEquals("doc", oelm.getNodeName());
		assertEquals("doc", oelm.getTagName());
		assertNull(oelm.getPrefix());
		assertEquals(nsURI, oelm.getNamespaceURI());
		assertEquals("doc", elm.getLocalName());
		assertEquals("cls", elm.getAttribute("class"));

		Element oelm2 = (Element) oelm.getFirstChild();
		assertNotNull(oelm2);
		assertEquals("e2", oelm2.getNodeName());
		assertEquals("e2", oelm2.getTagName());
		assertNull(oelm2.getPrefix());
		assertEquals(nsURI, oelm2.getNamespaceURI());
		assertEquals("e2", elm2.getLocalName());
		assertNotSame(elm2, oelm2);
	}

	@Test
	public void testPrefix() throws IOException {
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		String nsURI = "http://www.example.com/examplens";
		DocumentType docType = domImpl.createDocumentType("html", null, null);
		Document imported = domImpl.createDocument(nsURI, "p:doc", docType);

		Element elm = imported.getDocumentElement();

		assertNotNull(elm);

		elm.setAttribute("class", "cls");

		Element elm2 = imported.createElementNS(nsURI, "p:e2");

		elm.appendChild(elm2);

		Document doc = domImpl.createDocument("", null, null);

		Element impElm = (Element) doc.importNode(elm, true);
		assertNotNull(impElm);

		doc.appendChild(impElm);
		Element oelm = doc.getDocumentElement();
		assertNotNull(oelm);
		assertSame(impElm, oelm);

		assertEquals("p:doc", oelm.getNodeName());
		assertEquals("p:doc", oelm.getTagName());
		assertEquals("p", oelm.getPrefix());
		assertEquals(nsURI, oelm.getNamespaceURI());
		assertEquals("doc", elm.getLocalName());
		assertEquals("cls", elm.getAttribute("class"));

		Element oelm2 = (Element) oelm.getFirstChild();
		assertNotNull(oelm2);
		assertEquals("p:e2", oelm2.getNodeName());
		assertEquals("p:e2", oelm2.getTagName());
		assertEquals(nsURI, oelm2.getNamespaceURI());
		assertEquals("e2", elm2.getLocalName());
		assertNotSame(elm2, oelm2);
	}

}
