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
package io.sf.carte.echosvg.dom.svg.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;

/**
 * This class tests the cloneNode method.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class CloneNodeTest {

	@Test
	public void test() throws IOException {
		runTest("io/sf/carte/echosvg/dom/svg/test.svg", "nodeID");
	}

	void runTest(String testFileName, String targetId) throws IOException {
		SAXSVGDocumentFactory df = new SAXSVGDocumentFactory();

		URL url = getClass().getClassLoader().getResource(testFileName);
		Document doc = df.createDocument(url.toString(), url.openStream(), "utf-8");

		Element e = doc.getElementById(targetId);

		assertNotNull(e);

		Element celt = (Element) e.cloneNode(true);

		NamedNodeMap attrs = e.getAttributes();

		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String ns = attr.getNamespaceURI();
			String name = (ns == null) ? attr.getNodeName() : attr.getLocalName();
			String val = attr.getNodeValue();
			String val2 = celt.getAttributeNS(ns, name);
			assertEquals(val, val2);
		}
	}

}
