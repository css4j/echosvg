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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

/**
 * This test validates that the ParsedURL class properly parses and cascades
 * URLs.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ParsedURLDataTest {

	@Test
	public void testBase64Data() throws IOException {
		runTest("data:image/png;base64,R0lGODdhMAAw",
				"CT: image/png CE: base64 DATA: 47 49 46 38 37 URL: data:image/png;base64,R0lGODdhMAAw");
		runTest("data:text/plain;charset=US-ASCII;base64,R0lGODdhMAAw",
				"CT: text/plain;charset=US-ASCII CE: base64 DATA: 47 49 46 38 37 URL: data:text/plain;charset=US-ASCII;base64,R0lGODdhMAAw");
		runTest("data:image/gif;base64,R0lGODdhMAAw",
				"CT: image/gif CE: base64 DATA: 47 49 46 38 37 URL: data:image/gif;base64,R0lGODdhMAAw");
		runTest("data:text/plain;charset=iso-8859-7,%be%fb%be",
				"CT: text/plain;charset=iso-8859-7 CE: null DATA: be fb be URL: data:text/plain;charset=iso-8859-7,%be%fb%be");
		runTest("data:,A%20brief%20note", "CT: null CE: null DATA: 2c 41 20 62 72 URL: data:,,A%20brief%20note");
		runTest("data:;=;,A%20brief%20note", "CT: ;= CE:  DATA: 41 20 62 72 69 URL: data:;=;,A%20brief%20note");
	}

	/**
	 * Run test
	 * 
	 * @param base The url to parse
	 * @param ref  The expected result.
	 * @throws IOException 
	 */
	private void runTest(String url, String ref) throws IOException {
		ParsedURL purl = new ParsedURL(url);

		byte[] data = new byte[5];
		int num = 0;
		InputStream is = purl.openStream();
		num = is.read(data);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			int val = data[i] & 0xFF;
			if (val < 16) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val) + " ");
		}

		String info = ("CT: " + purl.getContentType() + " CE: " + purl.getContentEncoding() + " DATA: " + sb + "URL: "
				+ purl);

		assertEquals(ref, info, "Bad URL: ");
	}

}
