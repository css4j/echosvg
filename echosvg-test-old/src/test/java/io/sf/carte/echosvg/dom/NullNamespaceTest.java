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

import org.w3c.dom.*;

import java.io.*;
import java.net.*;
import io.sf.carte.echosvg.dom.util.*;
import io.sf.carte.echosvg.util.*;

import io.sf.carte.echosvg.test.*;

/**
 * This class tests the empty string value for an xmlns attribute.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class NullNamespaceTest extends AbstractTest {
    public static String ERROR_GET_ELEMENT_BY_ID_FAILED 
        = "error.get.element.by.id.failed";

    public static String ENTRY_KEY_ID 
        = "entry.key.id";

    protected String testFileName;
    protected String rootTag;
    protected String targetId;

    public NullNamespaceTest(String file,
                             String root,
                             String id) {
        testFileName = file;
        rootTag = root;
        targetId = id;
    }

    public TestReport runImpl() throws Exception {
        String parser =
            XMLResourceDescriptor.getXMLParserClassName();

        DocumentFactory df 
            = new SAXDocumentFactory
            (GenericDOMImplementation.getDOMImplementation(), parser);

        File f = (new File(testFileName));
        URL url = f.toURI().toURL();
        Document doc = df.createDocument(null,
                                         rootTag,
                                         url.toString(),
                                         url.openStream());

        
        Element e = doc.getElementById(targetId);

        if (e == null){
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode(ERROR_GET_ELEMENT_BY_ID_FAILED);
            report.addDescriptionEntry(ENTRY_KEY_ID,
                                       targetId);
            report.setPassed(false);
            return report;
        }
            
            
        if (e.getNamespaceURI() == null) {
            return reportSuccess();
        }
        DefaultTestReport report = new DefaultTestReport(this);
        report.setErrorCode(TestReport.ERROR_TEST_FAILED);
        report.setPassed(false);
        return report;
    }
}
