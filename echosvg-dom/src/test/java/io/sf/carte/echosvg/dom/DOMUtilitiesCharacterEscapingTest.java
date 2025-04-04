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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.Test;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;

/**
 * Checks that Text nodes can be properly written and read. This test creates a
 * Document with a CDATA section and checks that the CDATA section content can
 * be written out and then read without being altered.
 *
 */
public class DOMUtilitiesCharacterEscapingTest {

	@Test
	public void test() throws DOMException, IOException {
		DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
		Document doc = impl.createDocument("http://www.w3.org/1999/xhtml", "html", null);

		Element html = doc.getDocumentElement();
		Element p = doc.createElementNS("http://www.w3.org/1999/xhtml", "p");
		html.appendChild(p);

		p.setAttributeNS(null, "id", "myText");
		String unescapedContent = "You should not escape: & # \" ...";
		CDATASection cdata = doc.createCDATASection(unescapedContent);

		p.appendChild(cdata);

		Writer stringWriter = new StringWriter();

		DOMUtilities.writeDocument(doc, stringWriter);

		SAXDocumentFactory f = new SAXDocumentFactory(impl);
		doc = f.createDocument("https://raw.githubusercontent.com/css4j/echosvg/master/samples/foo.svg",
			new StringReader(stringWriter.toString()));

		p = doc.getElementById("myText");
		cdata = (CDATASection) p.getFirstChild();
		assertEquals(cdata.getData(), unescapedContent);
	}

}
