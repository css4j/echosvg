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

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests Node.baseURI.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NodeBaseURITest extends DOM3Test {

	@Test
	public void test() throws DOMException {
		Document doc = newSVGDoc();
		doc.setDocumentURI("http://example.com/blah");
		Element e = doc.createElementNS(SVG_NAMESPACE_URI, "g");
		doc.getDocumentElement().appendChild(e);
		e.setAttributeNS(XML_NAMESPACE_URI, "xml:base", "http://example.org/base");
		Element e2 = doc.createElementNS(SVG_NAMESPACE_URI, "g");
		e.appendChild(e2);
		e2.setAttributeNS(XML_NAMESPACE_URI, "xml:base", "/somewhere");
		assertEquals("http://example.com/blah", doc.getBaseURI());
		assertEquals("http://example.org/base", e.getBaseURI());
		assertEquals("http://example.org/somewhere", e2.getBaseURI());
	}

}
