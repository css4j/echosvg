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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Protocol Handler for the 'jar' protocol. This appears to have the format:
 * jar:&lt;URL for jar file&gt;!&lt;path in jar file&gt;
 *
 * <p>
 * Original author: <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class ParsedURLJarProtocolHandler extends ParsedURLDefaultProtocolHandler {

	public static final String JAR = "jar";

	public ParsedURLJarProtocolHandler() {
		super(JAR);
	}

	// We mostly use the base class parse methods (that leverage
	// java.net.URL. But we take care to ignore the baseURL if urlStr
	// is an absolute URL.
	@Override
	public ParsedURLData parseURL(ParsedURL baseURL, String urlStr) {
		// Check whether urlStr is absolute...
		if (startsWithJarColon(urlStr))
			return parseURL(urlStr);

		// It's relative so base it off baseURL.
		try {
			URL context = new URL(baseURL.toString());
			URL url = new URL(context, urlStr);
			return constructParsedURLData(url);
		} catch (MalformedURLException mue) {
			return super.parseURL(baseURL, urlStr);
		}
	}

	private boolean startsWithJarColon(String url) {
		char c;
		return url.length() > 3 && ((c = url.charAt(0)) == 'j' || c == 'J')
				&& ((c = url.charAt(1)) == 'a' || c == 'A')
				&& ((c = url.charAt(2)) == 'r' || c == 'R') && url.charAt(3) == ':';
	}

}
