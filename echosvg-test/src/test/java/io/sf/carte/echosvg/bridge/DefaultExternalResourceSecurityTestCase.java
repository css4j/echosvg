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
package io.sf.carte.echosvg.bridge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.util.ParsedURL;

/*
 * Test adapted from commit bfbccb6ad2 by S. Steiner
 */
public class DefaultExternalResourceSecurityTestCase {

	@Test
	public void testJarURL() {
		ParsedURL ext = new ParsedURL("jar:http://evil.com/poc!/");
		ParsedURL doc = new ParsedURL(".");
		String err = "";
		try {
			new DefaultExternalResourceSecurity(ext, doc).checkLoadExternalResource();
		} catch (SecurityException e) {
			err = e.getMessage();
		}
		Assertions.assertEquals(
				"The document references an external resource (jar:http://evil.com/poc!/) which "
				+ "comes from different location than the document itself. This is not allowed for security reasons and "
				+ "that resource will not be loaded.", err);
	}

}
