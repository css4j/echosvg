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

package io.sf.carte.echosvg.css.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Test the CSS Engine.
 */
public class CSSEngineTest {

	private Parser cssParser;

	@BeforeEach
	public void setupBeforeEach() {
		cssParser = new CSSParser();
	}

	@Test
	public void testPropertyIndexes() throws Exception {
		CSSEngine eng = createCSSEngine();

		assertEquals(CSSConstants.CSS_LINE_HEIGHT_PROPERTY,
				eng.getPropertyName(SVGCSSEngine.LINE_HEIGHT_INDEX));
		assertEquals(CSSConstants.CSS_FONT_SIZE_PROPERTY,
				eng.getPropertyName(SVGCSSEngine.FONT_SIZE_INDEX));
		assertEquals(CSSConstants.CSS_COLOR_PROPERTY,
				eng.getPropertyName(SVGCSSEngine.COLOR_INDEX));

		assertEquals(SVGCSSEngine.LINE_HEIGHT_INDEX, eng.getLineHeightIndex());
		assertEquals(SVGCSSEngine.FONT_SIZE_INDEX, eng.getFontSizeIndex());
		assertEquals(SVGCSSEngine.COLOR_INDEX, eng.getColorIndex());
	}

	private CSSEngine createCSSEngine() {
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

		CSSContext ctx = new TestCSSContext(doc, purl, cssParser) {

			@Override
			protected CSSEngine createCSSEngine(Document doc, ParsedURL purl, Parser cssParser) {
				return new CSSEngine(doc, purl, cssParser, SVGCSSEngine.SVG_VALUE_MANAGERS,
						SVGCSSEngine.SVG_SHORTHAND_MANAGERS, null, null, "style", null, "class",
						true, null, this) {

					@Override
					public ParsedURL getCSSBaseURI() {
						return purl;
					}

				};
			}

		};

		return ctx.getCSSEngineForElement(null);
	}

}
