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

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

/**
 * Tests Node.compareDocumentPosition.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NodeCompareDocumentPositionTest extends DOM3Test {

	@Test
	public void test() throws DOMException {
		Document doc = newDoc();
		AbstractNode e = (AbstractNode) doc.createElementNS(null, "test");
		doc.getDocumentElement().appendChild(e);
		AbstractNode e2 = (AbstractNode) doc.createElementNS(null, "two");
		e.appendChild(e2);
		AbstractNode e3 = (AbstractNode) doc.createElementNS(null, "three");
		e.appendChild(e3);
		AbstractNode e4 = (AbstractNode) doc.createElementNS(null, "four");
		doc.getDocumentElement().appendChild(e4);

		assertEquals(AbstractNode.DOCUMENT_POSITION_CONTAINS | AbstractNode.DOCUMENT_POSITION_PRECEDING,
				e.compareDocumentPosition(e2));
		assertEquals(AbstractNode.DOCUMENT_POSITION_CONTAINED_BY | AbstractNode.DOCUMENT_POSITION_FOLLOWING,
				e2.compareDocumentPosition(e));
		assertEquals(0, e.compareDocumentPosition(e));
		assertEquals(AbstractNode.DOCUMENT_POSITION_PRECEDING, e2.compareDocumentPosition(e3));
		assertEquals(AbstractNode.DOCUMENT_POSITION_FOLLOWING, e3.compareDocumentPosition(e2));
		assertEquals(AbstractNode.DOCUMENT_POSITION_PRECEDING, e3.compareDocumentPosition(e4));
		assertEquals(AbstractNode.DOCUMENT_POSITION_FOLLOWING, e4.compareDocumentPosition(e3));
	}

}
