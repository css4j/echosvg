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

package io.sf.carte.echosvg.css.engine.value;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeAll;

import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.parser.CSSParser;

/**
 * Manager test infrastructure.
 */
public class AbstractManagerTestSetup {

	private static CSSParser cssParser;

	@BeforeAll
	public static void setupBeforeAll() {
		cssParser = new CSSParser();
	}

	protected static LexicalUnit parsePropertyValue(String value) throws CSSParseException {
		try {
			return cssParser.parsePropertyValue(new StringReader(value));
		} catch (IOException e) {
			return null;
		}
	}

}
