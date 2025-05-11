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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeAll;
import org.w3c.dom.Document;

import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Manager test infrastructure.
 */
public class AbstractManagerTestSetup {

	static Parser cssParser;

	@BeforeAll
	public static void setupBeforeAll() {
		cssParser = new CSSParser();
	}

	static LexicalUnit parsePropertyValue(String value) throws CSSParseException {
		try {
			return cssParser.parsePropertyValue(new StringReader(value));
		} catch (IOException e) {
			return null;
		}
	}

	static CSSEngine createCSSEngine() {
		ParsedURL purl = new ParsedURL("https://www.example.com/");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
		Document doc = builder.newDocument();
		doc.setDocumentURI(purl.toString());
		CSSContext ctx = new TestCSSContext(doc, purl, cssParser);
		CSSEngine engine = ctx.getCSSEngineForElement(null);
		return engine;
	}

}
