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
package io.sf.carte.echosvg.test;

/**
 * Interface for classes that can process <code>TestReport</code> instances This
 * allows different applications to use the same <code>TestReport</code> for
 * different purposes, such as generating an XML output or emailing a test
 * result summary.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface TestReportProcessor {
	/**
	 * Generic error code. Takes no parameter.
	 */
	String INTERNAL_ERROR = "TestReportProcessor.error.code.internal.error";

	/**
	 * Requests the processor to process the input <code>TestReport</code>
	 * instances. Note that a processor should make its own copy of any resource
	 * described by a <code>TestReport</code> such as files, as these may be
	 * transient resources. In particular, a processor should not keep a reference
	 * to the input <code>TestReport</code>
	 */
	void processReport(TestReport report) throws TestException;
}
