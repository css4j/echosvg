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
package io.sf.carte.echosvg.anim.dom.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGRect;

import io.sf.carte.echosvg.anim.dom.AbstractElement;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGOMAnimatedRect;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * To test the SVGOMAnimatedRect.
 *
 * @author github.com/carlosame
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMAnimatedRectTest {

	// Floating point equality tolerance
	private static final float TOL = 1e-5f;

	private AbstractElement element;

	@BeforeEach
	public void setUpBeforeEach() throws DOMException {
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
		Document document = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SVG_TAG,
				null);
		element = (AbstractElement) document.getDocumentElement();
	}

	@Test
	public void testFloatValues() {
		SVGOMAnimatedRect animRect = createAnimatedRect("-10 -20 500 450");
		SVGRect rect = animRect.getBaseVal();
		assertNotNull(rect);

		assertEquals(-10f, rect.getX(), TOL);
		assertEquals(-20f, rect.getY(), TOL);
		assertEquals(500f, rect.getWidth(), TOL);
		assertEquals(450f, rect.getHeight(), TOL);
	}

	@Test
	public void testFloatValuesComma() {
		SVGOMAnimatedRect animRect = createAnimatedRect("-1.1e1, -20.2, 5e2, 450.07");
		SVGRect rect = animRect.getBaseVal();
		assertNotNull(rect);

		assertEquals(-11f, rect.getX(), TOL);
		assertEquals(-20.2f, rect.getY(), TOL);
		assertEquals(500f, rect.getWidth(), TOL);
		assertEquals(450.07f, rect.getHeight(), TOL);
	}

	@Test
	public void testUnitValues() throws ParseException {
		SVGOMAnimatedRect animRect = createAnimatedRect("-10 -20 500 450mm");
		SVGRect rect = animRect.getBaseVal();

		assertThrows(ParseException.class, () -> rect.getX());
	}

	@Test
	public void test3Values() throws LiveAttributeException {
		SVGOMAnimatedRect animRect = createAnimatedRect("-10 -20 500");
		SVGRect rect = animRect.getBaseVal();

		assertThrows(LiveAttributeException.class, () -> rect.getX());
	}

	@Test
	public void test2Values() throws LiveAttributeException {
		SVGOMAnimatedRect animRect = createAnimatedRect("-10 -20 ");
		SVGRect rect = animRect.getBaseVal();

		assertThrows(LiveAttributeException.class, () -> rect.getX());
	}

	@Test
	public void test1Value() throws LiveAttributeException {
		SVGOMAnimatedRect animRect = createAnimatedRect(" 500 ");
		SVGRect rect = animRect.getBaseVal();

		assertThrows(LiveAttributeException.class, () -> rect.getX());
	}

	/**
	 * Test rectangles with calc() values.
	 * <p>
	 * From the CSS Values Level 3 specification:
	 * </p>
	 * <p>
	 * [<code>calc()</code>] "can be used wherever &lt;length&gt;,
	 * &lt;frequency&gt;, &lt;angle&gt;, &lt;time&gt;, &lt;percentage&gt;,
	 * &lt;number&gt;, or &lt;integer&gt; values are allowed."
	 * </p>
	 */
	@Test
	public void testCalcValue() {
		SVGOMAnimatedRect animRect = createAnimatedRect("-10 -2e1 calc(5*1e2) 450");
		SVGRect rect = animRect.getBaseVal();
		assertNotNull(rect);

		assertEquals(-10f, rect.getX(), TOL);
		assertEquals(-20f, rect.getY(), TOL);
		assertEquals(500f, rect.getWidth(), TOL);
		assertEquals(450f, rect.getHeight(), TOL);
	}

	@Test
	public void testCalcMixedCase() {
		SVGOMAnimatedRect animRect = createAnimatedRect(
				"Calc(10 - 20) CaLc(10 - 30) CALC(5*100) cALC(400 + 50)");
		SVGRect rect = animRect.getBaseVal();
		assertNotNull(rect);

		assertEquals(-10f, rect.getX(), TOL);
		assertEquals(-20f, rect.getY(), TOL);
		assertEquals(500f, rect.getWidth(), TOL);
		assertEquals(450f, rect.getHeight(), TOL);
	}

	@Test
	public void testCalcValueUnitError() throws LiveAttributeException {
		SVGOMAnimatedRect animRect = createAnimatedRect("0 0 calc(10mm) 500");
		SVGRect rect = animRect.getBaseVal();

		assertThrows(LiveAttributeException.class, () -> rect.getX());
	}

	private SVGOMAnimatedRect createAnimatedRect(String attrValue) {
		element.setAttributeNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_VIEW_BOX_ATTRIBUTE,
				attrValue);
		SVGOMAnimatedRect animRect = new SVGOMAnimatedRect(element, SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_VIEW_BOX_ATTRIBUTE, null);
		return animRect;
	}

}
