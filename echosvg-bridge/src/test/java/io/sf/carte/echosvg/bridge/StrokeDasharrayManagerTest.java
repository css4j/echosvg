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
 * Test stroke-dasharray manager.
 */
public class StrokeDasharrayManagerTest extends PropertyManagerCheck {

	@BeforeAll
	public static void setupBeforeAll() {
		propertyName = "stroke-dasharray";
	}

	@Test
	public void testNumber() {
		checkComputedValue("5,2", "5 2");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testNumberOdd() {
		checkComputedValue("7,5,2", "7 5 2 7 5 2");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testLength() {
		checkComputedValue("0.5em,2px", "6px 2px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testLengthOdd() {
		checkComputedValue("0.5em,2vw,2px", "6px 4px 2px 6px 4px 2px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testPercentage() {
		checkComputedValue("3vw,2%", "6px 4px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCalcNumber() {
		checkComputedValue("5,calc(3 - 1)", "5 2");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCalcPercentage() {
		checkComputedValue("calc(6vw/2),calc(2% - 1px)", "6px 3px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testAbsPercentage() {
		checkComputedValue("abs(6vw/2),abs(2% - 1px)", "6px 3px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testInitial() {
		checkComputedValue("initial", "none");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testNone() {
		checkComputedValue("none");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testNegativeItem() {
		checkComputedValue("5 -2", "none");

		assertEquals(0, errorCount);
		assertEquals(1, messageCount);
	}

	@Test
	public void testVar() {
		svgStyle = "svg{--da1:5px;--da2:,3px;}";
		checkComputedValue("1ex,var(--da1),3pt var(--da2)", "6px 5px 4px 3px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testAttr() {
		checkComputedValueAttr("1% attr(data-width type(<length>))", "2px 6px", "data-width",
				"6px");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

}
