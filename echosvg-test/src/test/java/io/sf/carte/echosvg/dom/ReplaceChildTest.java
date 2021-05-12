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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

/**
 * This class tests the hasChildNodes method.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ReplaceChildTest {

	@Test
	public void test() throws IOException {
		testReplaceChild("io/sf/carte/echosvg/dom/dummyXML3.xml", "doc", "root");
	}

	void testReplaceChild(String testFileName, String rootTag, String targetId) throws IOException {
		String parser = XMLResourceDescriptor.getXMLParserClassName();

		DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);

		URL url = getClass().getClassLoader().getResource(testFileName);
		Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());

		Element e = doc.getElementById(targetId);

		assertNotNull(e);

		Element fc = null;
		for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				fc = (Element) n;
				break;
			}
		}
		Element ne = doc.createElementNS(null, "elt4");
		e.replaceChild(ne, fc);

		assertSame(e, ne.getParentNode());
		assertNull(fc.getParentNode());
	}

}
