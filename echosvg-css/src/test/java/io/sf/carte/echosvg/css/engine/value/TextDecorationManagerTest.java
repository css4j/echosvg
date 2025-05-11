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
import io.sf.carte.echosvg.css.engine.value.css.TextDecorationManager;

/**
 * Test the text-decoration manager.
 */
public class TextDecorationManagerTest extends AbstractManagerTestSetup {

	private ValueManager manager;

	@BeforeEach
	public void setupBeforeEach() {
		manager = new TextDecorationManager();
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
		LexicalUnit lu = parsePropertyValue("var(--text-decor)");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testVar2() throws Exception {
		LexicalUnit lu = parsePropertyValue("overline var(--text-decor)");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
		assertEquals("overline var(--text-decor)", v.getCssText());
	}

	@Test
	public void testAttr() throws Exception {
		LexicalUnit lu = parsePropertyValue("attr(data-text-decor type(<custom-ident>))");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testAttr2() throws Exception {
		LexicalUnit lu = parsePropertyValue("overline attr(data-text-decor type(<custom-ident>))");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testNone() throws Exception {
		LexicalUnit lu = parsePropertyValue("none");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("none", v.getStringValue());
	}

	@Test
	public void testIdent() throws Exception {
		LexicalUnit lu = parsePropertyValue("underline");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("underline", v.getStringValue());
		assertEquals(1, list.getLength());
	}

	@Test
	public void testList() throws Exception {
		LexicalUnit lu = parsePropertyValue("underline line-through");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("underline", v.getStringValue());
		v = list.item(1);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("line-through", v.getStringValue());
		assertEquals(2, list.getLength());
	}

	@Test
	public void testInvalid() throws Exception {
		LexicalUnit lu = parsePropertyValue("nonexistent-ident");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

}
