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

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;

/**
 * @author <a href="mailto:shillion@ilog.fr">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SetAttributeTest {

	@Test
	public void test() throws IOException {
		testSetAttribute("io/sf/carte/echosvg/dom/dummyXML.xml", "doc", "root",
				"targetAttribute", "targetValue");
	}

	void testSetAttribute(String testFileName, String rootTag, String targetId, String targetAttribute,
			String targetValue) throws IOException {
		DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), null);

		URL url = getClass().getClassLoader().getResource(testFileName);
		Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());

		Element e = doc.getElementById(targetId);

		assertNotNull(e);

		e.setAttribute(targetAttribute, targetValue);
		assertEquals(targetValue, e.getAttribute(targetAttribute));

		Attr attr = e.getAttributeNode(targetAttribute);

		assertNotNull(attr);
		assertEquals(targetValue, attr.getValue());
		assertEquals(targetValue, attr.getNodeValue());
	}

}
