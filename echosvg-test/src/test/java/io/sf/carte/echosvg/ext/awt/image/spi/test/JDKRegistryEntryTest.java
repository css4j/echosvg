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

import java.awt.color.ColorSpace;
import java.awt.image.RenderedImage;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.bridge.SVGBrokenLinkProvider;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.ErrorConstants;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;
import io.sf.carte.echosvg.ext.awt.image.spi.JDKRegistryEntry;

public class JDKRegistryEntryTest {

	@Test
	public void testDefaultBrokenLinkProvider() {
		ImageTagRegistry.setBrokenLinkProvider(null);
		JDKRegistryEntry re = new JDKRegistryEntry();

		Object[] errParam = { "JDK", "foo" };
		Filter filt = ImageTagRegistry.getBrokenLinkImage(re, ErrorConstants.ERR_URL_FORMAT_UNREADABLE, errParam);
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
		JDKRegistryEntry re = new JDKRegistryEntry();

		Object[] errParam = { "JDK", "foo" };
		Filter filt = ImageTagRegistry.getBrokenLinkImage(re, ErrorConstants.ERR_URL_FORMAT_UNREADABLE, errParam);
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
