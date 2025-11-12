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

package io.sf.carte.echosvg.css.engine.value.svg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.AbstractManagerTestSetup;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;

/**
 * Test the stroke-dasharray manager.
 */
public class StrokeDasharrayManagerTest extends AbstractManagerTestSetup {

	private ValueManager manager;

	@BeforeEach
	public void setupBeforeEach() {
		manager = new StrokeDasharrayManager();
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
	public void testInitial() throws Exception {
		LexicalUnit lu = parsePropertyValue("initial");
		Value v = manager.createValue(lu, null);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getCssText();
		assertEquals("none", s);
	}

	@Test
	public void testNone() throws Exception {
		LexicalUnit lu = parsePropertyValue("none");
		Value v = manager.createValue(lu, null);
		assertEquals(Type.IDENT, v.getPrimitiveType());
		String s = v.getIdentifierValue();
		assertEquals("none", s);
	}

	@Test
	public void testNumbers() throws Exception {
		LexicalUnit lu = parsePropertyValue("5,3");
		Value list = manager.createValue(lu, null);
		assertEquals(CssType.LIST, list.getCssValueType());

		Value v = list.item(0);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(5f, v.getFloatValue());
		v = list.item(1);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(3f, v.getFloatValue());

		assertEquals(2, list.getLength());
	}

	@Test
	public void testLength() throws Exception {
		LexicalUnit lu = parsePropertyValue("5em,3ex");
		Value list = manager.createValue(lu, null);
		assertEquals(CssType.LIST, list.getCssValueType());

		Value v = list.item(0);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_EM, v.getUnitType());
		assertEquals(5f, v.getFloatValue());
		v = list.item(1);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_EX, v.getUnitType());
		assertEquals(3f, v.getFloatValue());

		assertEquals(2, list.getLength());
	}

	@Test
	public void testPercentage() throws Exception {
		LexicalUnit lu = parsePropertyValue("5%,3%");
		Value list = manager.createValue(lu, null);
		assertEquals(CssType.LIST, list.getCssValueType());

		Value v = list.item(0);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_PERCENTAGE, v.getUnitType());
		assertEquals(5f, v.getFloatValue());
		v = list.item(1);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_PERCENTAGE, v.getUnitType());
		assertEquals(3f, v.getFloatValue());

		assertEquals(2, list.getLength());
	}

	@Test
	public void testVar() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"var(--x) ,2");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testVarAfter() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"5 var(--x), 2");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testAttr() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"attr(data-dash type(<length-percentage> | <integer>)), 10");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testAttrAfter() throws Exception {
		LexicalUnit lu = parsePropertyValue(
				"20,attr(data-dash type(<length-percentage> | <integer>))");
		CSSEngine engine = createCSSEngine();
		Value v = manager.createValue(lu, engine);
		assertEquals(Type.LEXICAL, v.getPrimitiveType());
	}

	@Test
	public void testCalc() throws Exception {
		LexicalUnit lu = parsePropertyValue("7 calc(3 + 2) 3 1");
		CSSEngine engine = createCSSEngine();
		Value list = manager.createValue(lu, engine);
		assertEquals(CssType.LIST, list.getCssValueType());
		Value v = list.item(0);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(7f, v.getFloatValue());
		v = list.item(1);
		assertEquals(Type.EXPRESSION, v.getPrimitiveType());
		assertEquals("calc(3 + 2)", v.getCssText());
		v = list.item(2);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(3f, v.getFloatValue());
		v = list.item(3);
		assertEquals(Type.NUMERIC, v.getPrimitiveType());
		assertEquals(CSSUnit.CSS_NUMBER, v.getUnitType());
		assertEquals(1f, v.getFloatValue());
		assertEquals(4, list.getLength());
	}

	@Test
	public void testInvalid_Ident() throws Exception {
		LexicalUnit lu = parsePropertyValue("auto");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testInvalid_URL() throws Exception {
		LexicalUnit lu = parsePropertyValue("url(foo.png)");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

	@Test
	public void testInvalid_Abs() throws Exception {
		LexicalUnit lu = parsePropertyValue("6, abs(-5deg), 3");
		CSSEngine engine = createCSSEngine();
		assertThrows(DOMException.class, () -> manager.createValue(lu, engine));
	}

}
