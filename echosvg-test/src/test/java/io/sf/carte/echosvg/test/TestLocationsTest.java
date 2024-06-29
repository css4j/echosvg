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
 * Detect location issues early by checking test locations.
 */
public class TestLocationsTest {

	@Test
	public void testProjectRoot() throws IOException {
		assertNotNull(TestLocations.PROJECT_ROOT_URL);
		assertTrue(TestLocations.PROJECT_ROOT_URL.length() > 2);
	}

	@Test
	public void testGetTestProjectBuildPath() throws IOException {
		String path = TestLocations.getTestProjectBuildPath();
		assertNotNull(path);
		assertTrue(path.length() > 8);
	}

}
