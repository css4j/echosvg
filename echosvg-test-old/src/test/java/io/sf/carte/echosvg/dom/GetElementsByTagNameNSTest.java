/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.dom;

import java.io.File;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

/**
 * This class tests the getElementsByTagNameNS method.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GetElementsByTagNameNSTest extends AbstractTest {
	protected String testFileName;
	protected String rootTag;
	protected String tagName;

	public GetElementsByTagNameNSTest(String file, String root, String tag) {
		testFileName = file;
		rootTag = root;
		tagName = tag;
	}

	@Override
	public TestReport runImpl() throws Exception {
		String parser = XMLResourceDescriptor.getXMLParserClassName();

		DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);

		File f = (new File(testFileName));
		URL url = f.toURI().toURL();
		Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());

		Element root = doc.getDocumentElement();
		NodeList lst = root.getElementsByTagNameNS(null, tagName);

		if (lst.getLength() != 1) {
			DefaultTestReport report = new DefaultTestReport(this);
			report.setErrorCode("error.getElementByTagNameNS.failed");
			report.setPassed(false);
			return report;
		}

		Node n;
		while ((n = root.getFirstChild()) != null) {
			root.removeChild(n);
		}

		if (lst.getLength() != 0) {
			DefaultTestReport report = new DefaultTestReport(this);
			report.setErrorCode("error.getElementByTagNameNS.failed");
			report.setPassed(false);
			return report;
		}

		return reportSuccess();
	}

}
