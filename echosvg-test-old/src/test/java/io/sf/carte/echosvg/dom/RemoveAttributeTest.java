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

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

/**
 * This class tests the removeAttribute method.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RemoveAttributeTest {

	@Test
	public void test() throws IOException {
		testRemoveAttribute("io/sf/carte/echosvg/dom/dummyXML3.xml", "doc", "root", "attr");
		testRemoveAttribute("io/sf/carte/echosvg/dom/dummyXML3.xml", "doc", "root", "attr2");
	}

	void testRemoveAttribute(String testFileName, String rootTag, String targetId, String targetAttr)
			throws IOException {
		String parser = XMLResourceDescriptor.getXMLParserClassName();

		DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);

		URL url = getClass().getClassLoader().getResource(testFileName);
		Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());

		Element e = doc.getElementById(targetId);

		assertNotNull(e);

		e.removeAttribute(targetAttr);
	}

}
