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

package io.sf.carte.echosvg.css.dom.test;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.svg.SelfContainedSVGOnLoadTest;

/**
 * CSSOM scripting tests.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public class EcmaScriptCSSDOMTest extends SelfContainedSVGOnLoadTest {

	private static final String PATH_PREFIX = "io/sf/carte/echosvg/css/dom/test/";

	@Test
	public void testRgb() throws Exception {
		testSVGOnLoad(PATH_PREFIX + "rgbTest.svg");
	}

	@Test
	public void testRgbUpdate() throws Exception {
		testSVGOnLoad(PATH_PREFIX + "rgbUpdateTest.svg");
	}

	@Test
	public void testRgbPresentation() throws Exception {
		testSVGOnLoad(PATH_PREFIX + "rgbPresentationTest.svg");
	}

	@Test
	public void testBug9740() throws Exception {
		testSVGOnLoad(PATH_PREFIX + "bug9740.svg");
	}

	@Test
	public void testBug9779() throws Exception {
		testSVGOnLoad(PATH_PREFIX + "bug9779.svg");
	}

	@Test
	public void testBug11670() throws Exception {
		testSVGOnLoad(PATH_PREFIX + "bug11670.svg");
	}

}
