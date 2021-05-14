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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

/**
 * Tests the {@link Node#isEqualNode(Node)} method.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NodeIsEqualNodeTest {

	private String DOC =
			"<a><b class=\"foo\" lang=\"en\"/><b lang=\"en\" class=\"foo\"/><c class=\"bar\">.<?x?>.</c><c class=\"bar\">.<?x?>..</c></a>";

	@Test
	public void test() throws DOMException, IOException {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXDocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);
		Document doc = df.createDocument("http://example.org/", new StringReader(DOC));

		AbstractElement a = (AbstractElement) doc.getDocumentElement();
		Node b1 = a.getFirstChild();
		Node b2 = b1.getNextSibling();
		Node c1 = b2.getNextSibling();
		Node c2 = c1.getNextSibling();
		Node dotTextC1 = c1.getFirstChild();
		Node piC1 = dotTextC1.getNextSibling();
		Node piC2 = c2.getFirstChild().getNextSibling();

		assertEquals(Node.PROCESSING_INSTRUCTION_NODE, piC1.getNodeType());
		assertEquals("x", piC1.getNodeName());
		assertEquals(Node.PROCESSING_INSTRUCTION_NODE, piC2.getNodeType());
		assertEquals("x", piC2.getNodeName());

		// Identity test
		assertTrue(a.isEqualNode(a));
		assertTrue(a.isSameNode(a));
		assertTrue(c1.isEqualNode(c1));
		assertTrue(c1.isSameNode(c1));
		assertTrue(dotTextC1.isEqualNode(dotTextC1));
		assertTrue(piC1.isEqualNode(piC1));

		// Node type
		assertFalse(a.isEqualNode(dotTextC1));
		assertFalse(a.isEqualNode(piC1));
		assertFalse(piC1.isEqualNode(dotTextC1));
		assertFalse(piC1.isEqualNode(a));

		// Same node name
		assertTrue(piC1.isEqualNode(piC2));
		assertFalse(piC1.isSameNode(piC2));

		// Different node name
		assertFalse(a.isEqualNode(b1));

		// Equal elements
		assertTrue(b1.isEqualNode(b2));
		assertTrue(b2.isEqualNode(b1));

		// Different child nodes
		assertFalse(c1.isEqualNode(c2));
		assertFalse(c2.isEqualNode(c1));
	}

}
