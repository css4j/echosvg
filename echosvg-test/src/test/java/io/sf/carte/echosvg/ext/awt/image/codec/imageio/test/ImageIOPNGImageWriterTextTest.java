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

package io.sf.carte.echosvg.ext.awt.image.codec.imageio.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.PNGImageWriterParams;

/**
 * Check text encoding in the ImageIOPNGImageWriter.
 * 
 * @version $Id$
 */
public class ImageIOPNGImageWriterTextTest extends ImageIOPNGImageWriterTest {

	private static final String[] loremIpsum = { "LoremIpsum 1",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, ", "LoremIpsum 2",
			"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "LoremIpsum 3",
			" Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi", "LoremIpsum 4",
			" ut aliquip ex ea commodo consequat." };

	private static final String[] grouchoContract = { "Groucho contract scene 1",
			"The first part of the party of the first part shall be known in this contract",
			"Groucho contract scene 2", " as the first part of the party of the first part.",
			"Groucho contract scene 3",
			"The party of the second part shall be know in this contract as the party of the second part." };

	private static final String[] iTXt = { "Foo", "la-VAT", "LoremIpsum",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
					+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." };

	@Test
	public void testTextError() {
		PNGImageWriterParams params = new PNGImageWriterParams();

		assertFalse(params.isTextSet());
		assertFalse(params.isInternationalTextSet());
		assertFalse(params.isCompressedTextSet());

		assertThrows(IllegalStateException.class, () -> params.getText());
		assertThrows(IllegalStateException.class, () -> params.getInternationalText());
		assertThrows(IllegalStateException.class, () -> params.getCompressedText());
	}

	@Override
	protected void configureImageWriterParams(ImageWriterParams params) {
		if (params instanceof PNGImageWriterParams) {
			PNGImageWriterParams pngParams = (PNGImageWriterParams) params;
			pngParams.setText(loremIpsum);
			pngParams.setInternationalText(iTXt);
			pngParams.setCompressedText(grouchoContract);
		}
	}

	@Override
	protected String getImageSuffix() {
		return "-text-i-z";
	}

}
