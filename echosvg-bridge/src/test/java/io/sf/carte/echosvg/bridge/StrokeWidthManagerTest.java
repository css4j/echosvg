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
package io.sf.carte.echosvg.bridge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test stroke-width manager.
 */
public class StrokeWidthManagerTest extends PropertyManagerCheck {

	@BeforeAll
	public static void setupBeforeAll() {
		propertyName = "stroke-width";
	}

	@Test
	public void testNumber() {
		checkComputedValue("2", "2");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testLength() {
		checkComputedValue("2px", "2px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testPercentage() {
		checkComputedValue("2%", "4px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testInitial() {
		checkComputedValue("initial", "1");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testNone() {
		checkComputedValue("none", "1");

		assertEquals(1, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testNegative() {
		checkComputedValue("-2", "1");

		assertEquals(0, errorCount);
		assertEquals(1, messageCount);
	}

	@Test
	public void testLengthNegative() {
		checkComputedValue("-2pt", "1");

		assertEquals(0, errorCount);
		assertEquals(1, messageCount);
	}

	@Test
	public void testVar() {
		svgStyle = "svg{--width:5px;}";
		checkComputedValue("var(--width)", "5px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testAttr() {
		checkComputedValueAttr("attr(data-width type(<length>))", "6px", "data-width", "6px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

}
