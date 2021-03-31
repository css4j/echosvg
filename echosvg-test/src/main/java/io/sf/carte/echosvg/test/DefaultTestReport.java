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
 * Simple, default implementation for the <code>TestReport</code> interface.
 * 
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultTestReport implements TestReport {
	private boolean passed = true;

	protected Entry[] description = null;

	protected Test test;

	private String errorCode;

	/**
	 * Parent report, in case this report is part of a <code>TestSuiteReport</code>
	 */
	protected TestSuiteReport parent;

	public DefaultTestReport(Test test) {
		if (test == null) {
			throw new IllegalArgumentException();
		}

		this.test = test;
	}

	@Override
	public TestSuiteReport getParentReport() {
		return parent;
	}

	@Override
	public void setParentReport(TestSuiteReport parent) {
		this.parent = parent;
	}

	@Override
	public Test getTest() {
		return test;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		if (!passed && errorCode == null) {
			/**
			 * Error code should be set first
			 */
			throw new IllegalArgumentException();
		}

		this.errorCode = errorCode;
	}

	@Override
	public boolean hasPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		if (!passed && (errorCode == null)) {
			/**
			 * Error Code should be set first
			 */
			throw new IllegalArgumentException();
		}
		this.passed = passed;
	}

	@Override
	public Entry[] getDescription() {
		return description;
	}

	public void setDescription(Entry[] description) {
		this.description = description;
	}

	@Override
	public void addDescriptionEntry(String key, Object value) {
		addDescriptionEntry(new Entry(key, value));
	}

	protected void addDescriptionEntry(Entry entry) {
		if (description == null) {
			description = new Entry[1];
			description[0] = entry;
		} else {
			Entry[] oldDescription = description;
			description = new Entry[description.length + 1];
			System.arraycopy(oldDescription, 0, description, 0, oldDescription.length);
			description[oldDescription.length] = entry;
		}
	}

}
