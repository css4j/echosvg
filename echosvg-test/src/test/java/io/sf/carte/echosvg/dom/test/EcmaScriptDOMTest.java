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
package io.sf.carte.echosvg.dom.test;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.svg.SelfContainedSVGOnLoadTest;

/**
 * DOM scripting tests.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public class EcmaScriptDOMTest extends SelfContainedSVGOnLoadTest {

	@Test
	public void testBug18143() throws Exception {
		testSVGOnLoad("io/sf/carte/echosvg/dom/bug18143.svg");
	}

	@Test
	public void testBug20331() throws Exception {
		testSVGOnLoad("io/sf/carte/echosvg/dom/bug20331.svg");
	}

	@Test
	public void testBug20332() throws Exception {
		testSVGOnLoad("io/sf/carte/echosvg/dom/bug20332.svg");
	}

}
