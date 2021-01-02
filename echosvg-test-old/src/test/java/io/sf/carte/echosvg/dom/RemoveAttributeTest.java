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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

/**
 * This class tests the removeAttribute method.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RemoveAttributeTest extends AbstractTest {
	public static String ERROR_GET_ELEMENT_BY_ID_FAILED = "error.get.element.by.id.failed";

	public static String ENTRY_KEY_ID = "entry.key.id";

	protected String testFileName;
	protected String rootTag;
	protected String targetId;
	protected String targetAttr;

	public RemoveAttributeTest(String file, String root, String id, String attr) {
		testFileName = file;
		rootTag = root;
		targetId = id;
		targetAttr = attr;
	}

	@Override
	public TestReport runImpl() throws Exception {
		String parser = XMLResourceDescriptor.getXMLParserClassName();

		DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);

		File f = (new File(testFileName));
		URL url = f.toURI().toURL();
		Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());

		Element e = doc.getElementById(targetId);

		if (e == null) {
			DefaultTestReport report = new DefaultTestReport(this);
			report.setErrorCode(ERROR_GET_ELEMENT_BY_ID_FAILED);
			report.addDescriptionEntry(ENTRY_KEY_ID, targetId);
			report.setPassed(false);
			return report;
		}

		try {
			e.removeAttribute(targetAttr);
		} catch (DOMException ex) {
			DefaultTestReport report = new DefaultTestReport(this);
			report.setErrorCode(TestReport.ERROR_TEST_FAILED);
			report.addDescriptionEntry("exception.message", ex.getMessage());
			report.setPassed(false);
			return report;
		}
		return reportSuccess();
	}
}
