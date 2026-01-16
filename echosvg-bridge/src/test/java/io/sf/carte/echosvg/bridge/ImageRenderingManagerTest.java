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
 * Test {@code image-rendering} manager.
 */
public class ImageRenderingManagerTest extends PropertyManagerCheck {

	@BeforeAll
	public static void setupBeforeAll() {
		propertyName = "image-rendering";
	}

	@Test
	public void testNumber() {
		checkComputedValue("2", "auto");

		assertEquals(1, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testInitial() {
		checkComputedValue("initial", "auto");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testOptimizeSpeed() {
		checkComputedValue("optimizespeed", "optimizespeed");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testPixelated() {
		checkComputedValue("pixelated", "optimizespeed");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testSmooth() {
		checkComputedValue("smooth", "optimizequality");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testNone() {
		checkComputedValue("none", "auto");

		assertEquals(1, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testVar() {
		svgStyle = "svg{--imgRend:pixelated;}";
		checkComputedValue("var(--imgRend)", "optimizespeed");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testAttr() {
		checkComputedValueAttr("attr(data-imgrend type(<custom-ident>))", "optimizequality",
				"data-imgrend", "smooth");

		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

}
