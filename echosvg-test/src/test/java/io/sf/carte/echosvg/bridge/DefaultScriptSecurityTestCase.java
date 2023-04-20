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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVGConstants;

/*
 * Test based on commits 3f937cfea7 and 905f368b5 by S. Steiner
 */
public class DefaultScriptSecurityTestCase {

	@Test
	public void testSameHost() {
		ParsedURL docUrl = new ParsedURL("https://www.example.com/svg.svg");
		ParsedURL scriptUrl = new ParsedURL("https://www.example.com/js/script.js");
		new DefaultScriptSecurity(null, scriptUrl, docUrl).checkLoadScript();
	}

	@Test
	public void testDifferentHost() {
		ParsedURL docUrl = new ParsedURL("https://192.168.1.10/svg.svg");
		ParsedURL scriptUrl = new ParsedURL("https://192.168.1.20/js/script.js");
		SecurityException e = assertThrows(SecurityException.class,
				() -> new DefaultScriptSecurity(null, scriptUrl, docUrl).checkLoadScript());

		String ex = e.getMessage();
		assertEquals("The document references a script file (https://192.168.1.20/js/script.js) "
				+ "which comes from different location than the document itself. This is not allowed with the current "
				+ "security settings and that script will not be loaded.", ex);
	}

	@Test
	public void testUrls() {
		ParsedURL docUrl = new ParsedURL("");
		ParsedURL scriptUrl = new ParsedURL("jar:http://192.168.1.10/poc.jar!/");
		SecurityException e = assertThrows(SecurityException.class,
				() -> new DefaultScriptSecurity(null, scriptUrl, docUrl).checkLoadScript());

		String ex = e.getMessage();
		assertEquals("The document references a script file (jar:http://192.168.1.10/poc.jar!/) "
				+ "which comes from different location than the document itself. This is not allowed with the current "
				+ "security settings and that script will not be loaded.", ex);
	}

	@Test
	public void testJarFile() {
		ParsedURL docUrl = new ParsedURL("");
		ParsedURL scriptUrl = new ParsedURL("poc.jar");
		SecurityException e = assertThrows(SecurityException.class,
				() -> new DefaultScriptSecurity(SVGConstants.SVG_SCRIPT_TYPE_JAVA, scriptUrl,
						docUrl).checkLoadScript());

		String ex = e.getMessage();
		assertEquals("Could not access the current document URL when trying to load script file "
				+ "file:poc.jar. Script will not be loaded as it is not possible to verify it comes from the same location "
				+ "as the document.", ex);
	}

	@Test
	public void testJsFile() {
		ParsedURL docUrl = new ParsedURL("");
		ParsedURL scriptUrl = new ParsedURL("script.js");
		new DefaultScriptSecurity(SVGConstants.SVG_SCRIPT_TYPE_JAVASCRIPT, scriptUrl, docUrl)
				.checkLoadScript();
	}

}
