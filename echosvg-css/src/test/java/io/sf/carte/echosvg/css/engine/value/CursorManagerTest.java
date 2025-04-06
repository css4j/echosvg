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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.echosvg.css.Viewport;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.css.engine.value.css.CursorManager;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Test the cursor manager.
 */
public class CursorManagerTest {

	private static Parser cssParser;

	private ValueManager manager;

	@BeforeAll
	public static void setupBeforeAll() {
		cssParser = new CSSParser();
	}

	@BeforeEach
	public void setupBeforeEach() {
		manager = new CursorManager();
	}

	@Test
	public void testInherit() throws Exception {
		LexicalUnit lu = parsePropertyValue("inherit");
		Value v = manager.createValue(lu, null);
		assertEquals(Type.INHERIT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("inherit", s);
	}

	@Test
	public void testVarX() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"url('/img/cursor.ico') var(--x) 8,url('/img/cursor.png'),pointer");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testVarY() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"url('/img/cursor.ico') 5 var(--y),url('/img/cursor.png'),pointer");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testVarIdent() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"url('/img/cursor.ico') 5 7,url('/img/cursor.png'),var(--pointer)");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testAttrIdent() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"url('/img/cursor.ico') 5 7,url('/img/cursor.png'),attr(data-pointer type(<custom-ident>))");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testPointer() throws Exception {
		LexicalUnit lu = parsePropertyValue("crosshair");
		Value v = manager.createValue(lu, null);
		assertEquals(CssType.LIST, v.getCssValueType());
		v = v.item(0);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("crosshair", s);
	}

	@Test
	public void testURL() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico'),default");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.ico", v.getURIValue());
		v = list.item(1);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("default", s);
		assertEquals(2, list.getLength());
	}

	@Test
	public void testURLx() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') 5,help");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.ico", v.getURIValue());
		v = list.item(1);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(5f, v.getFloatValue());
		v = list.item(2);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("help", s);
		assertEquals(3, list.getLength());
	}

	@Test
	public void testURLxy() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') 5 6,move");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.ico", v.getURIValue());
		v = list.item(1);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(5f, v.getFloatValue());
		v = list.item(2);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(6f, v.getFloatValue());
		v = list.item(3);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("move", s);
		assertEquals(4, list.getLength());
	}

	@Test
	public void testURL_Calc_xy() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') calc(3 + 2) 6,move");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.ico", v.getURIValue());
		v = list.item(1);
		assertEquals(Type.EXPRESSION, v.getPrimitiveType());
		assertEquals("calc(3 + 2)", v.getCssText());
		v = list.item(2);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(6f, v.getFloatValue());
		v = list.item(3);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("move", s);
		assertEquals(4, list.getLength());
	}

	@Test
	public void testURLxyURL() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"url('/img/cursor.ico') 5 6,url('/img/cursor.png'),pointer");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.ico", v.getURIValue());
		v = list.item(1);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(5f, v.getFloatValue());
		v = list.item(2);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(6f, v.getFloatValue());
		v = list.item(3);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.png", v.getURIValue());
		v = list.item(4);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("pointer", s);
		assertEquals(5, list.getLength());
	}

	@Test
	public void testURLxURL() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"url('/img/cursor.ico') 5,url('/img/cursor.png'),pointer");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.ico", v.getURIValue());
		v = list.item(1);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(5f, v.getFloatValue());
		v = list.item(2);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/img/cursor.png", v.getURIValue());
		v = list.item(3);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("pointer", s);
		assertEquals(4, list.getLength());
	}

	@Test
	public void testURL_Invalid_NoIdent() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') 5 6");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testURL_Invalid_x() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') 5deg 6,move");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testURL_Invalid_y() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') 5 6dpi,move");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testURL_Invalid_Calc_x() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') calc(5deg) 6,move");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testURL_Invalid_Calc_y() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/cursor.ico') 2 calc(3deg),move");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	private static LexicalUnit parsePropertyValue(String value) throws CSSParseException {
		try {
			return cssParser.parsePropertyValue(new StringReader(value));
		} catch (IOException e) {
			return null;
		}
	}

	private static CSSEngine createCSSEngine() {
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
		CSSContext ctx = new TestCSSContext(doc, purl);
		CSSEngine engine = ctx.getCSSEngineForElement(null);
		return engine;
	}

	static class TestCSSContext implements CSSContext {

		private CSSEngine engine;

		public TestCSSContext(Document doc, ParsedURL purl) {
			this.engine = new SVGCSSEngine(doc, purl, cssParser, this) {

				@Override
				public ParsedURL getCSSBaseURI() {
					return purl;
				}

			};
		}

		@Override
		public Value getSystemColor(String ident) {
			return null;
		}

		@Override
		public Value getDefaultFontFamily() {
			return null;
		}

		@Override
		public float getLighterFontWeight(float f) {
			return 0;
		}

		@Override
		public float getBolderFontWeight(float f) {
			return f * 1.2f;
		}

		@Override
		public float getResolution() {
			return 96f;
		}

		@Override
		public float getMediumFontSize() {
			return 12f;
		}

		@Override
		public float getBlockWidth(Element elt) {
			return 100;
		}

		@Override
		public float getBlockHeight(Element elt) {
			return 50;
		}

		@Override
		public Viewport getViewport(Element e) {
			return new Viewport() {

				@Override
				public float getWidth() {
					return 400f;
				}

				@Override
				public float getHeight() {
					return 300f;
				}

			};
		}

		@Override
		public void checkLoadExternalResource(ParsedURL resourceURL, ParsedURL docURL) throws SecurityException {
		}

		@Override
		public String getPrefersColorScheme() {
			return "light";
		}

		@Override
		public boolean isDynamic() {
			return false;
		}

		@Override
		public boolean isInteractive() {
			return false;
		}

		@Override
		public CSSEngine getCSSEngineForElement(Element e) {
			return engine;
		}

	}

}
