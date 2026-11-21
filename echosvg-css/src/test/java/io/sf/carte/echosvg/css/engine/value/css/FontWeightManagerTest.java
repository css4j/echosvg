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
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.TestCSSContext;
import io.sf.carte.echosvg.css.engine.value.AbstractManagerTestSetup;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;

/**
 * Test the font-weight manager.
 */
public class FontWeightManagerTest extends AbstractManagerTestSetup {

	private ValueManager manager;

	@BeforeEach
	public void setupBeforeEach() {
		manager = new FontWeightManager();
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
		LexicalUnit lu = parsePropertyValue("var(--font-weight)");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testAttr() throws Exception {
		LexicalUnit lu = parsePropertyValue("attr(data-font-weight type(<number>))");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testNormal() throws Exception {
		LexicalUnit lu = parsePropertyValue("normal");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("normal", v.getStringValue());
	}

	@Test
	public void testBold() throws Exception {
		LexicalUnit lu = parsePropertyValue("bold");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("bold", v.getStringValue());
	}

	@Test
	public void testLighter() throws Exception {
		LexicalUnit lu = parsePropertyValue("lighter");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("lighter", v.getStringValue());
	}

	@Test
	public void testBolder() throws Exception {
		LexicalUnit lu = parsePropertyValue("bolder");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		assertEquals("bolder", v.getStringValue());
	}

	@Test
	public void testInteger() throws Exception {
		LexicalUnit lu = parsePropertyValue("400");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(400f, v.getFloatValue());
	}

	@Test
	public void testIntegerFallbackUnder400() throws Exception {
		LexicalUnit lu = parsePropertyValue("360");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(300f, v.getFloatValue());
	}

	@Test
	public void testIntegerFallback400500() throws Exception {
		LexicalUnit lu = parsePropertyValue("450");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(500f, v.getFloatValue());
	}

	@Test
	public void testIntegerFallbackOver400() throws Exception {
		LexicalUnit lu = parsePropertyValue("540");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(600f, v.getFloatValue());
	}

	@Test
	public void testIntegerFallbackLow() throws Exception {
		LexicalUnit lu = parsePropertyValue("40");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(100f, v.getFloatValue());
	}

	@Test
	public void testIntegerFallbackLarge() throws Exception {
		LexicalUnit lu = parsePropertyValue("1000");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(900f, v.getFloatValue());
	}

	@Test
	public void testInvalidIdent() throws Exception {
		LexicalUnit lu = parsePropertyValue("foo");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testInvalidUnit() throws Exception {
		LexicalUnit lu = parsePropertyValue("1s");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testInvalidZero() throws Exception {
		LexicalUnit lu = parsePropertyValue("0");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testInvalidNegative() throws Exception {
		LexicalUnit lu = parsePropertyValue("-1");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testInvalidOver1000() throws Exception {
		LexicalUnit lu = parsePropertyValue("1001");
		CSSEngine engine = TestCSSContext.createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

}
