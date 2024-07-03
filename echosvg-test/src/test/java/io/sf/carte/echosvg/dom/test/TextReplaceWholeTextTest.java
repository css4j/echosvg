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
package io.sf.carte.echosvg.dom.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

/**
 * Tests Text.replaceWholeText.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TextReplaceWholeTextTest extends DOM3Test {

	@Test
	public void test() throws DOMException {
		Document doc = newSVGDoc();
		Text n1 = doc.createTextNode("abc");
		Text n2 = doc.createTextNode("def");
		Text n3 = doc.createCDATASection("ghi");
		doc.getDocumentElement().appendChild(n1);
		doc.getDocumentElement().appendChild(n2);
		doc.getDocumentElement().appendChild(n3);
		n2.replaceWholeText("xyz");

		assertEquals("xyz", doc.getDocumentElement().getFirstChild().getNodeValue());
		assertNull(doc.getDocumentElement().getFirstChild().getNextSibling());
	}

}
