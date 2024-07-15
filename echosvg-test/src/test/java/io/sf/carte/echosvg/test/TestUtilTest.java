/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.sf.carte.echosvg.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Detect testing issues early by checking test util.
 */
public class TestUtilTest {

	@Test
	public void testGetRootProjectURL() throws IOException {
		String path = TestUtil.getRootProjectURL(getClass(), TestLocations.TEST_DIRNAME);
		assertNotNull(path);
		assertTrue(path.contains("echosvg"));
	}

	@Test
	public void testGetRootProjectURLWrongClass() throws IOException {
		/*
		 * We pass a class that isn't provided by EchoSVG so it must use CWD
		 */
		String path = TestUtil.getRootProjectURL(String.class, TestLocations.TEST_DIRNAME);
		assertNotNull(path);
		assertTrue(path.contains("echosvg"));
	}

	@Test
	public void testGetProjectBuildURL() throws IOException {
		String path = TestUtil.getProjectBuildURL(getClass(), TestLocations.TEST_DIRNAME);
		assertNotNull(path);
		assertTrue(path.contains(TestLocations.TEST_DIRNAME), "Path must contain "
				+ TestLocations.TEST_DIRNAME);
	}

	@Test
	public void testGetProjectBuildURLWrongClass() throws IOException {
		/*
		 * We pass a class that isn't provided by EchoSVG so it must use CWD
		 */
		String path = TestUtil.getProjectBuildURL(String.class, TestLocations.TEST_DIRNAME);
		assertNotNull(path);
		assertTrue(path.contains(TestLocations.TEST_DIRNAME), "Path must contain "
				+ TestLocations.TEST_DIRNAME);
	}

}
