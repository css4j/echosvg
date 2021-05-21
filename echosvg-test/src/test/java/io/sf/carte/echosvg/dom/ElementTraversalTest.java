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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.w3c.dom.ElementTraversal;

/**
 * Tests the {@link ElementTraversal} interface.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ElementTraversalTest {

	private String DOC = "<a><b/><c>.<?x?>.</c><d>.<?x?><e/><f/><?x?>.</d><g><h/>.<i/></g></a>";

	@Test
	public void test() throws DOMException, IOException {
		SAXDocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation());
		Document doc = df.createDocument("http://example.org/", new StringReader(DOC));

		AbstractElement a = (AbstractElement) doc.getDocumentElement();
		AbstractElement b = (AbstractElement) a.getFirstChild();
		AbstractElement c = (AbstractElement) b.getNextSibling();
		AbstractElement d = (AbstractElement) c.getNextSibling();
		AbstractElement g = (AbstractElement) d.getNextSibling();

		// (1) Test firstElementChild with no children
		assertNull(b.getFirstElementChild());

		// (2) Test firstElementChild with children but no element children
		assertNull(c.getFirstElementChild());

		// (3) Test firstElementChild with children but element child is not first
		AbstractElement e = (AbstractElement) d.getFirstElementChild();
		assertNotNull(e);
		assertEquals("e", e.getNodeName());

		// (4) Test firstElementChild with children and element child is first
		AbstractElement h = (AbstractElement) g.getFirstElementChild();
		assertNotNull(h);
		assertEquals("h", h.getNodeName());

		// (5) Test lastElementChild with no children
		assertNull(b.getLastElementChild());

		// (6) Test lastElementChild with children but no element children
		assertNull(c.getLastElementChild());

		// (7) Test lastElementChild with children but element child is not last
		AbstractElement f = (AbstractElement) d.getLastElementChild();
		assertNotNull(f);
		assertEquals("f", f.getNodeName());

		// (8) Test lastElementChild with children and element child is last
		AbstractElement i = (AbstractElement) g.getLastElementChild();
		assertNotNull(i);
		assertEquals("i", i.getNodeName());

		// (9) Test nextElementSibling with no next sibling
		assertNull(a.getNextElementSibling());

		// (10) Test nextElementSibling with next siblings but no element next sibling
		assertNull(f.getNextElementSibling());

		// (11) Test nextElementSibling with next element sibling but not first
		assertSame(i, h.getNextElementSibling());

		// (12) Test nextElementSibling with next element sibling which is first
		assertSame(f, e.getNextElementSibling());

		// (13) Test previousElementSibling with no previous sibling
		assertNull(a.getPreviousElementSibling());

		// (14) Test previousElementSibling with previous siblings but no element
		// previous sibling
		assertNull(e.getPreviousElementSibling());

		// (15) Test previousElementSibling with previous element sibling but not first
		assertSame(h, i.getPreviousElementSibling());

		// (16) Test previousElementSibling with previous element sibling which is first
		assertSame(e, f.getPreviousElementSibling());

		// (17-20) Test childElementCount for a few cases
		assertEquals(4, a.getChildElementCount());
		assertEquals(0, b.getChildElementCount());
		assertEquals(0, c.getChildElementCount());
		assertEquals(2, d.getChildElementCount());
	}

}
