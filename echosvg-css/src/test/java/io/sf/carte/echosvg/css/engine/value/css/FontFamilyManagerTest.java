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

package io.sf.carte.echosvg.css.engine.value.css;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.TestCSSContext;
import io.sf.carte.echosvg.css.engine.value.AbstractManagerTestSetup;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;

/**
 * Test the font-family manager.
 */
public class FontFamilyManagerTest extends AbstractManagerTestSetup {

	private ValueManager manager;

	@BeforeEach
	public void setupBeforeEach() {
		manager = new FontFamilyManager();
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
		LexicalUnit lu = parsePropertyValue("var(--font-family)");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testVar2() throws Exception {
		LexicalUnit lu = parsePropertyValue("Arial var(--font-family)");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
		assertEquals("Arial var(--font-family)", v.getCssText());
	}

	@Test
	public void testVarComma() throws Exception {
		LexicalUnit lu = parsePropertyValue("Arial, var(--font-family)");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
		assertEquals("Arial, var(--font-family)", v.getCssText());
	}

	@Test
	public void testAttr() throws Exception {
		LexicalUnit lu = parsePropertyValue("attr(data-font-family type(<string>))");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testAttrList() throws Exception {
		LexicalUnit lu = parsePropertyValue("Arial,attr(data-font-family type(<string>)), serif");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
		assertEquals("Arial, attr(data-font-family type(<string>)), serif", v.getCssText());
	}

	@Test
	public void testString() throws Exception {
		LexicalUnit lu = parsePropertyValue("'Times New Roman'");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Times New Roman", v.getStringValue());
		assertEquals(1, list.getLength());
	}

	@Test
	public void testStringList() throws Exception {
		LexicalUnit lu = parsePropertyValue("'Times New Roman','Playfair Display'");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Times New Roman", v.getStringValue());
		v = list.item(1);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Playfair Display", v.getStringValue());
		assertEquals(2, list.getLength());
	}

	@Test
	public void testIdent() throws Exception {
		LexicalUnit lu = parsePropertyValue("sans-serif");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("sans-serif", v.getStringValue());
		assertEquals(1, list.getLength());
	}

	@Test
	public void testIdentMulti() throws Exception {
		LexicalUnit lu = parsePropertyValue("Times New Roman");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Times New Roman", v.getStringValue());
		assertEquals(1, list.getLength());
	}

	@Test
	public void testIdentMultiList() throws Exception {
		LexicalUnit lu = parsePropertyValue("Times New Roman, serif");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Times New Roman", v.getStringValue());
		v = list.item(1);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("serif", v.getStringValue());
		assertEquals(2, list.getLength());
	}

	@Test
	public void testList() throws Exception {
		LexicalUnit lu = parsePropertyValue("Helvetica,Arial, sans-serif");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Helvetica", v.getStringValue());
		v = list.item(1);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Arial", v.getStringValue());
		v = list.item(2);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("sans-serif", v.getStringValue());
		assertEquals(3, list.getLength());
	}

	@Test
	public void testMPlus1p() throws Exception {
		LexicalUnit lu = parsePropertyValue("Mplus 1p, monospace");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Mplus 1p", v.getStringValue());
		v = list.item(1);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("monospace", v.getStringValue());
		assertEquals(2, list.getLength());
	}

	@Test
	public void testSlabo() throws Exception {
		LexicalUnit lu = parsePropertyValue("Slabo 13px, serif");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.STRING, v.getPrimitiveType());
		assertEquals("Slabo 13px", v.getStringValue());
		v = list.item(1);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("serif", v.getStringValue());
		assertEquals(2, list.getLength());
	}

	@Test
	public void testUnquotedDisallowed() throws Exception {
		LexicalUnit lu = parsePropertyValue("Mplus 1.2px, monospace");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testInvalid() throws Exception {
		LexicalUnit lu = parsePropertyValue("foo %");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

}
