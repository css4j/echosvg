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
package io.sf.carte.echosvg.dom.svg;

import java.io.File;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;


/**
 * This class tests the cloneNode method.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class CloneNodeTest extends AbstractTest {
    protected String testFileName;
    protected String targetId;

    public CloneNodeTest(String file, String id) {
        testFileName = file;
        targetId = id;
    }
    
    public TestReport runImpl() throws Exception {
        String parser =
            XMLResourceDescriptor.getXMLParserClassName();

        SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(parser);

        File f = (new File(testFileName));
        URL url = f.toURI().toURL();
        Document doc = df.createDocument(url.toString(),
                                         url.openStream());
        
        Element e = doc.getElementById(targetId);

        if (e == null){
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode("error.get.element.by.id.failed");
            report.addDescriptionEntry("entry.key.id", targetId);
            report.setPassed(false);
            return report;
        }

        Element celt = (Element)e.cloneNode(true);

        NamedNodeMap attrs = e.getAttributes();

        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            String ns = attr.getNamespaceURI();
            String name = (ns == null)
                ? attr.getNodeName()
                : attr.getLocalName();
            String val = attr.getNodeValue();
            String val2 = celt.getAttributeNS(ns, name);
            if (!val.equals(val2)) {
                DefaultTestReport report = new DefaultTestReport(this);
                report.setErrorCode("error.attr.comparison.failed");
                report.addDescriptionEntry("entry.attr.name", name);
                report.setPassed(false);
                return report;
            }
        }

        return reportSuccess();
    }
}
