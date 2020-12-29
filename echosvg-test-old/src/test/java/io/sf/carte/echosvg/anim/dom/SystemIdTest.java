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

package io.sf.carte.echosvg.anim.dom;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.MissingResourceException;

import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.test.DefaultTestReport;

/**
 * This class tests that there is System Id for each public Id
 * in the dtdids.properties resource file.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author $Id$
 */
public class SystemIdTest extends AbstractTest {
    public static final String ERROR_MISSING_SYSTEM_ID
        = "error.missing.system.id";

    public static final String KEY_MISSING_IDS
        = "key.missing.ids";

    public SystemIdTest() {
    }

    public TestReport runImpl() throws Exception {
        ResourceBundle rb = 
            ResourceBundle.getBundle(SAXSVGDocumentFactory.DTDIDS,
                                     Locale.getDefault());
        String dtdids = rb.getString(SAXSVGDocumentFactory.KEY_PUBLIC_IDS);
        
        StringTokenizer st = new StringTokenizer(dtdids, "-");
        int nIds = st.countTokens();
        String missingIds = "";
        for (int i=0; i<nIds; i++) {
            String publicId = st.nextToken();
            publicId = "-" + publicId.trim();
            System.out.println("Testing public id: " + publicId);
            try {
                rb.getString(SAXSVGDocumentFactory.KEY_SYSTEM_ID 
                              + publicId.replace(' ', '_'));
            } catch (MissingResourceException e) {
                missingIds += "[" + publicId + "]  -- ";
            }
        }
        
        if (!"".equals(missingIds)) {
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode(ERROR_MISSING_SYSTEM_ID);
            report.addDescriptionEntry(KEY_MISSING_IDS, missingIds);
            report.setPassed(false);
            return report;
        }

        return reportSuccess();
    }
}
