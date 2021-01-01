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
package io.sf.carte.echosvg.test.xml;

/**
 * Contains constants for the XML Test Report (XTR) syntax.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface XTRConstants extends XMLReflectConstants{
    String XTR_NAMESPACE_URI 
        = "http://xml.apache.org/xml-batik/test/xtr";

    
    /////////////////////////////////////////////////////////////////////////
    // XTR tags
    /////////////////////////////////////////////////////////////////////////
    String XTR_DESCRIPTION_TAG = "description";
    String XTR_FILE_ENTRY_TAG = "fileEntry";
    String XTR_GENERIC_ENTRY_TAG = "genericEntry";
    String XTR_TEST_REPORT_TAG = "testReport";
    String XTR_TEST_SUITE_REPORT_TAG = "testSuiteReport";
    String XTR_URI_ENTRY_TAG = "uriEntry";
    
    /////////////////////////////////////////////////////////////////////////
    // XTR attributes
    /////////////////////////////////////////////////////////////////////////
    String XTR_CLASS_ATTRIBUTE = "class";
    String XTR_DATE_ATTRIBUTE = "date";
    String XTR_KEY_ATTRIBUTE = "key";
    String XTR_ERROR_CODE_ATTRIBUTE = "errorCode";
    String XTR_ID_ATTRIBUTE = "id";
    String XTR_STATUS_ATTRIBUTE = "status";
    String XTR_TEST_NAME_ATTRIBUTE = "testName";
    String XTR_VALUE_ATTRIBUTE     = "value";

    /////////////////////////////////////////////////////////////////////////
    // XTR values
    /////////////////////////////////////////////////////////////////////////
    String XTR_PASSED_VALUE = "passed";
    String XTR_FAILED_VALUE = "failed";
}
