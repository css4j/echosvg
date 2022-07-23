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
package io.sf.carte.echosvg.ext.awt.image.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.color.ColorSpace;
import java.awt.image.RenderedImage;

import org.junit.Test;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.util.ParsedURL;

public class JDKRegistryEntryTest {

	@Test
	public void testBrokenLink() {
		ParsedURL purl = new ParsedURL(TestLocations.PROJECT_ROOT_URL + "doesnotexist.png");
		URLRegistryEntry re = new JDKRegistryEntry();
		assertTrue(re.isCompatibleURL(purl));

		Filter filt = re.handleURL(purl, false);
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
