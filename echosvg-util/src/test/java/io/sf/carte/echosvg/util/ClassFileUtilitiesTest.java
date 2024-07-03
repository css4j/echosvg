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
package io.sf.carte.echosvg.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.util.ClassFileUtilities.ClassFile;
import io.sf.carte.echosvg.util.ClassFileUtilities.Jar;

/**
 * This test validates that the ClassFileUtilities work properly.
 *
 * @version $Id$
 */
public class ClassFileUtilitiesTest {

	@Test
	public void testCollectJars() throws Exception {
		URL url = ClassFileUtilitiesTest.class.getResource("/io/sf/carte/echosvg/bridge/IWasLoaded.jar");
		String s = new File(url.toURI()).getParent();
		collectJarsTest(s, 2, 2);
	}

	private void collectJarsTest(String dir, int minJarCount, int minClassCount)
			throws SecurityException, IOException {
		File f = new File(dir);
		Map<String, Jar> jars = new HashMap<>();
		Map<String, ClassFile> classFiles = new HashMap<>();
		ClassFileUtilities.collectJars(f, jars, classFiles);

		assertTrue(minJarCount <= jars.size(), "Found less than " + minJarCount + " jar files.");
		assertTrue(minClassCount <= classFiles.size(),
				"Found less than " + minClassCount + " class files.");
	}

}
