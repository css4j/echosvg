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
package io.sf.carte.echosvg.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple implementation of the <code>TestReport</code> interface
 * for <code>TestSuite</code>
 *
 * @author <a href="mailto:vhardy@apache.lorg">Vincent Hardy</a>
 * @version $Id$
 */
public class DefaultTestSuiteReport implements TestSuiteReport {
    /**
     * Error code for a failed TestSuite
     */
    public static final String ERROR_CHILD_TEST_FAILED
        = "DefaultTestSuiteReport.error.child.test.failed";

    /**
     * Entry for a failed child test report
     */
    public static final String ENTRY_KEY_FAILED_CHILD_TEST_REPORT
        = "DefaultTestSuiteReport.entry.key.failed.child.test.report";

    /**
     * Entry for a passed child test report
     */
    public static final String ENTRY_KEY_PASSED_CHILD_TEST_REPORT
        = "DefaultTestSuiteReport.entry.key.passed.child.test.report";

    /**
     * Set of <code>TestReport</code> coming from the <code>TestSuite</code>
     */
    protected List reports = new ArrayList();

    /**
     * TestSuite that created this report
     */
    protected TestSuite testSuite;

    /**
     * Descriptions in addition to that coming from children.
     */
    protected Entry[] description;

    /**
     * Parent report in case this report is part of a bigger one.
     */
    protected TestSuiteReport parent;

    public DefaultTestSuiteReport(TestSuite testSuite){
        if(testSuite == null){
            throw new IllegalArgumentException();
        }

        this.testSuite = testSuite;
    }

    public Test getTest(){
        return testSuite;
    }

    public String getErrorCode(){
        if(hasPassed()){
            return null;
        }
        else{
            return ERROR_CHILD_TEST_FAILED;
        }
    }

    public TestSuiteReport getParentReport(){
        return parent;
    }

    public void setParentReport(TestSuiteReport parent){
        this.parent = parent;
    }

    public boolean hasPassed(){
        Iterator iter = reports.iterator();

        boolean passed = true;

        while(iter.hasNext()){
            TestReport childReport = (TestReport)iter.next();
            passed = passed && childReport.hasPassed();
        }

        return passed;
    }

    public void addDescriptionEntry(String key,
                                    Object value){
        addDescriptionEntry(new Entry(key, value));
    }

    protected void addDescriptionEntry(Entry entry){
        if(description == null){
            description = new Entry[1];
            description[0] = entry;
        }
        else{
            Entry[] oldDescription = description;
            description = new Entry[description.length + 1];
            System.arraycopy(oldDescription, 0, description, 0,
                             oldDescription.length);
            description[oldDescription.length] = entry;
        }
    }

    public Entry[] getDescription(){
        Iterator iter = reports.iterator();
        List descs = new ArrayList();

        while(iter.hasNext()){
            TestReport childReport = (TestReport)iter.next();
            if(!childReport.hasPassed()){
                TestReport.Entry entry
                    = new TestReport.Entry(Messages.formatMessage(ENTRY_KEY_FAILED_CHILD_TEST_REPORT, null),
                                           childReport);
                descs.add(entry);
            }
        }

        iter = reports.iterator();
        while(iter.hasNext()){
            TestReport childReport = (TestReport)iter.next();
            if(childReport.hasPassed()){
                TestReport.Entry entry
                    = new TestReport.Entry(Messages.formatMessage(ENTRY_KEY_PASSED_CHILD_TEST_REPORT, null),
                                           childReport);
                descs.add(entry);
            }
        }

        TestReport.Entry[] entries = null;
        if(descs.size() > 0){
            entries = new TestReport.Entry[descs.size()];
            descs.toArray(entries);
        }

        if(description != null){
            TestReport.Entry[] e = entries;
            entries = new TestReport.Entry[e.length + description.length];
            System.arraycopy(e, 0, entries, 0, e.length);
            System.arraycopy(description, 0, entries, e.length, description.length);
        }

        return entries;
    }

    public void addReport(TestReport report){
        if(report == null){
            throw new IllegalArgumentException();
        }

        report.setParentReport(this);
        reports.add(report);
    }


    public TestReport[] getChildrenReports(){
        int nReports = reports.size();
        TestReport[] r = new TestReport[nReports];
        reports.toArray(r);
        return r;
    }
}
