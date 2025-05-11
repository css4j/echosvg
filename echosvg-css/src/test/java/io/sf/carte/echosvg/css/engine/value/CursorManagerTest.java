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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.css.CursorManager;

/**
 * Test the cursor manager.
 */
public class CursorManagerTest extends AbstractManagerTestSetup {

	private ValueManager manager;

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

}
