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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.w3c.dom.Document;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;
import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

/**
 * To test the Java serialization.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SerializationTest extends AbstractTest {

	protected String testFileName;
	protected String rootTag;
	protected String parserClassName = XMLResourceDescriptor.getXMLParserClassName();

	public SerializationTest(String file, String root) {
		testFileName = file;
		rootTag = root;
	}

	@Override
	public TestReport runImpl() throws Exception {
		DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parserClassName);

		File f = (new File(testFileName));
		URL url = f.toURI().toURL();
		Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());

		File ser1 = File.createTempFile("doc1", "ser");
		File ser2 = File.createTempFile("doc2", "ser");

		try {
			// Serialization 1
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(new FileOutputStream(ser1));
			oos.writeObject(doc);
			oos.close();

			// Deserialization 1
			ObjectInputStream ois;
			ois = new ObjectInputStream(new FileInputStream(ser1));
			doc = (Document) ois.readObject();
			ois.close();

			// Serialization 2
			oos = new ObjectOutputStream(new FileOutputStream(ser2));
			oos.writeObject(doc);
			oos.close();
		} catch (IOException e) {
			DefaultTestReport report = new DefaultTestReport(this);
			report.setErrorCode("io.error");
			report.addDescriptionEntry("message", e.getClass().getName() + ": " + e.getMessage());
			report.addDescriptionEntry("file.name", testFileName);
			report.setPassed(false);
			return report;
		}

		// Binary diff
		InputStream is1 = new FileInputStream(ser1);
		InputStream is2 = new FileInputStream(ser2);

		try {
			for (;;) {
				int i1 = is1.read();
				int i2 = is2.read();
				if (i1 == -1 && i2 == -1) {
					return reportSuccess();
				}
				if (i1 != i2) {
					DefaultTestReport report = new DefaultTestReport(this);
					report.setErrorCode("difference.found");
					report.addDescriptionEntry("file.name", testFileName);
					report.setPassed(false);
					return report;
				}
			}
		} finally {
			try {
				is1.close();
				is2.close();
			} catch (IOException e) {
			}
		}
	}
}
