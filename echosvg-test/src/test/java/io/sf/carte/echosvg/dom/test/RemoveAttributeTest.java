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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;

/**
 * This class tests the removeAttribute method.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RemoveAttributeTest {

	@Test
	public void test() throws Exception {
		testRemoveAttribute("io/sf/carte/echosvg/dom/dummyXML3.xml", "root", "attr");
		testRemoveAttribute("io/sf/carte/echosvg/dom/dummyXML3.xml", "root", "attr2");
	}

	void testRemoveAttribute(String testFileName, String targetId, String targetAttr)
			throws IOException, SAXException {
		DocumentBuilder df = new SAXSVGDocumentFactory();

		Document doc;
		URL url = getClass().getClassLoader().getResource(testFileName);
		InputSource source = new InputSource(url.toString());
		try (InputStream is = url.openStream()) {
			source.setByteStream(is);
			doc = df.parse(source);
		}

		Element e = doc.getElementById(targetId);

		assertNotNull(e);

		e.removeAttribute(targetAttr);
	}

}
