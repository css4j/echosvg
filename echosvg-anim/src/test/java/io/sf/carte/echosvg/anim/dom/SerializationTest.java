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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * To test the Java serialization.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SerializationTest {

	@Test
	public void test() throws Exception {
		testSerialization("io/sf/carte/echosvg/anim/dom/dummyXML.xml");
		testSerialization("io/sf/carte/echosvg/anim/dom/dummyXML2.xml");
	}

	void testSerialization(String testFileName) throws Exception {
		SAXSVGDocumentFactory df = new SAXSVGDocumentFactory();

		Document doc;
		URL url = getClass().getClassLoader().getResource(testFileName);
		InputSource source = new InputSource(url.toString());
		try (InputStream is = url.openStream()) {
			source.setByteStream(is);
			doc = df.parse(source);
		}

		File ser1 = Files.createTempFile("doc1", "ser").toFile();
		File ser2 = Files.createTempFile("doc2", "ser").toFile();
		ser1.deleteOnExit();
		ser2.deleteOnExit();

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

		// Binary diff
		try (InputStream is1 = new FileInputStream(ser1);
				InputStream is2 = new FileInputStream(ser2)) {
			for (;;) {
				int i1 = is1.read();
				int i2 = is2.read();
				assertEquals(i1, i2);
				if (i1 == -1) {
					break;
				}
			}
		}
	}

}
