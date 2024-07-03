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
package io.sf.carte.echosvg.ext.awt.image.spi.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.color.ColorSpace;
import java.awt.image.RenderedImage;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.bridge.SVGBrokenLinkProvider;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.AbstractRegistryEntry;
import io.sf.carte.echosvg.ext.awt.image.spi.ErrorConstants;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;

public class ImageTagRegistryTest {

	@Test
	public void testMimeTypes() {
		ImageTagRegistry ir = new ImageTagRegistry();
		// Add a new registry entry with a HIGHER priority first
		ir.register(new AbstractRegistryEntry("Unit test", 100, "working", "application/working") {
		});
		// Ensure the first one is present:
		assertTrue(ir.getRegisteredMimeTypes().contains("application/working"));
		// Ensure the second is NOT YET present:
		assertTrue(!ir.getRegisteredMimeTypes().contains("application/missing"));
		// Add a new registry entry with a LOW priority later
		ir.register(new AbstractRegistryEntry("Unit test", 1, "missing", "application/missing") {
		});
		// This one still works - this is expected:
		assertTrue(ir.getRegisteredMimeTypes().contains("application/working"));
		// The second was not added because of BATIK-1203.
		assertTrue(ir.getRegisteredMimeTypes().contains("application/missing"));
	}

	@Test
	public void testDefaultBrokenLinkProvider() {
		ImageTagRegistry.setBrokenLinkProvider(null);
		ImageTagRegistry reg = ImageTagRegistry.getRegistry();

		Filter filt = ImageTagRegistry.getBrokenLinkImage(reg, ErrorConstants.ERR_STREAM_UNREADABLE, null);
		RenderedImage red = filt.createDefaultRendering();
		assertNotNull(red);

		assertEquals(100, red.getWidth());
		assertEquals(100, red.getHeight());
		assertEquals(0, red.getMinX());
		assertEquals(0, red.getMinY());
	}

	@Test
	public void testSVGBrokenLinkProvider() {
		ImageTagRegistry.setBrokenLinkProvider(new SVGBrokenLinkProvider());
		ImageTagRegistry reg = ImageTagRegistry.getRegistry();

		Filter filt = ImageTagRegistry.getBrokenLinkImage(reg, ErrorConstants.ERR_STREAM_UNREADABLE, null);
		assertNotNull(filt);
		assertEquals(100d, filt.getWidth(), 1e-7);
		assertEquals(100d, filt.getHeight(), 1e-7);

		RenderedImage red = filt.createDefaultRendering();
		assertNotNull(red);

		assertEquals(100, red.getWidth());
		assertEquals(100, red.getHeight());
		assertEquals(0, red.getMinX());
		assertEquals(0, red.getMinY());

		assertEquals(ColorSpace.TYPE_RGB, red.getColorModel().getColorSpace().getType());
	}

}
