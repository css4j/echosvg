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

import java.util.LinkedList;
import java.util.List;

/**
 * This class provides an implementation for the <code>addTest</code> method and
 * a protected member to store the children <code>Test</code> instances.
 *
 * @author <a href="mailto:vhardy@apache.lorg">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractTestSuite implements TestSuite {
	/**
	 * Stores the list of children <code>Test</code> instances.
	 */
	protected List<Test> children = new LinkedList<>();

	/**
	 * Adds a <code>Test</code> to the suite
	 */
	@Override
	public void addTest(Test test) {
		if (test != null) {
			children.add(test);
		}
	}

}
