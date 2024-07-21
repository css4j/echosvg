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

import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;

/**
 * This test validates the compression levels in the ImageIOPNGImageWriter
 * operation.
 * 
 * @version $Id$
 */
public class ImageIOPNGImageWriterCompressTest extends ImageIOPNGImageWriterTest {

	@Override
	protected void configureImageWriterParams(ImageWriterParams params) {
		params.setCompressionLevel(3);
	}

	@Override
	protected String getImageSuffix() {
		return "-z3";
	}

}
