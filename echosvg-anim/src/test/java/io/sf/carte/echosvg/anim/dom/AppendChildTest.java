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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.dom.util.DocumentFactory;

/**
 * This class tests the appendChild method.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class AppendChildTest {

	@Test
	public void test() throws IOException {
		testAppendChild("io/sf/carte/echosvg/anim/dom/test.svg", "svg", "nodeID");
	}

	void testAppendChild(String testFileName, String rootTag, String targetId) throws IOException {
		DocumentFactory df = new SAXSVGDocumentFactory();

		URL url = getClass().getClassLoader().getResource(testFileName);
		Document doc = df.createDocument("http://www.w3.org/2000/svg", rootTag, url.toString(), url.openStream());

		Element e = doc.getElementById(targetId);

		assertNotNull(e);

		Document otherDocument = df.createDocument("http://www.w3.org/2000/svg", rootTag, url.toString(),
				url.openStream());

		DocumentFragment docFrag = otherDocument.createDocumentFragment();

		DOMException ex = assertThrows(DOMException.class, () -> docFrag.appendChild(doc.getDocumentElement()));
		assertEquals(DOMException.WRONG_DOCUMENT_ERR, ex.code);
	}

}
