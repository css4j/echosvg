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
import io.sf.carte.echosvg.css.engine.value.css.SrcManager;

/**
 * Test the src manager.
 */
public class SrcManagerTest extends AbstractManagerTestSetup {

	private ValueManager manager;

	@BeforeEach
	public void setupBeforeEach() {
		manager = new SrcManager();
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
	public void testVar() throws Exception {
		LexicalUnit lu = parsePropertyValue("var(--url)");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testVarModifier() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/img/font.otf') var(--modif)");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testURL() throws Exception {
		LexicalUnit lu = parsePropertyValue("url('/fonts/font.otf')");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/fonts/font.otf", v.getURIValue());
		assertEquals(1, list.getLength());
	}

	@Test
	public void testURLList() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"url(fonts/font.woff) format(\"woff\"), url('/fonts/font.otf') format(otf)");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/fonts/font.woff", v.getURIValue());
		v = list.item(1);
		assertEquals(Type.URI, v.getPrimitiveType());
		assertEquals("https://www.example.com/fonts/font.otf", v.getURIValue());
		assertEquals(2, list.getLength());
	}

	@Test
	public void testURLList_TechModif() throws Exception {
		/*
		 * tech() is not supported
		 */
		LexicalUnit lu = parsePropertyValue(
				"url(fonts/font-svg.otf) tech(color-SVG), url('/fonts/font.otf') tech(color-colrv1)");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

}
