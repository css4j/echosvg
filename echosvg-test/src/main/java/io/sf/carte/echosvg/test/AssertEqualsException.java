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

/**
 * Exception which Tests can throw when a specific <code>assertEquals</code>
 * fails.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AssertEqualsException extends AssertException {
	private static final long serialVersionUID = 1L;

	public static final String ENTRY_KEY_REF_OBJECT = "AssertEqualsException.entry.key.ref.object";

	public static final String ENTRY_KEY_CMP_OBJECT = "AssertEqualsException.entry.key.cmp.object";

	public static final String ASSERTION_TYPE = "assertEquals";

	/**
	 * Objects which should have be equal
	 */
	protected Object ref, cmp;

	public AssertEqualsException(Object ref, Object cmp) {
		this.ref = ref;
		this.cmp = cmp;
	}

	/**
	 * Requests that the exception populates the TestReport with the relevant
	 * information.
	 */
	@Override
	public void addDescription(TestReport report) {
		report.addDescriptionEntry(ENTRY_KEY_REF_OBJECT, ref);
		report.addDescriptionEntry(ENTRY_KEY_CMP_OBJECT, cmp);
	}

	@Override
	public String getAssertionType() {
		return ASSERTION_TYPE;
	}
}
