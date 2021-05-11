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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests Attr.isId.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AttrIsIdTest extends DOM3Test {

	@Test
	public void test() throws DOMException {
		Document doc = newSVGDoc();
		Element g = doc.createElementNS(SVG_NAMESPACE_URI, "g");
		g.setAttributeNS(null, "id", "n1");
		doc.getDocumentElement().appendChild(g);
		Attr a = g.getAttributeNodeNS(null, "id");
		assertTrue(a.isId());
	}
}
