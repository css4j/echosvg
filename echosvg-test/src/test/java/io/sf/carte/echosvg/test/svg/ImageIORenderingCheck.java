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
package io.sf.carte.echosvg.test.svg;

import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoderImageIOWriteAdapter;

/**
 * Checks for regressions in rendering of SVG with ImageIO writer.
 * 
 * @version $Id$
 */
class ImageIORenderingCheck extends RenderingTest {

	// ImageIO default is 4
	private static final int IMAGEIO_DEFAULT_COMPRESSION_LEVEL = 4;

	/**
	 * Construct a new test.
	 */
	public ImageIORenderingCheck() {
		super();
	}

	@Override
	protected int getDefaultCompressionLevel() {
		return IMAGEIO_DEFAULT_COMPRESSION_LEVEL;
	}

	@Override
	protected CharSequence getImageSuffix() {
		CharSequence suf = super.getImageSuffix();
		StringBuilder buf = new StringBuilder(suf.length() + 3);
		buf.append("-io").append(suf);
		return buf;
	}

	@Override
	ImageTranscoder createTestImageTranscoder() {
		return new ImageIOTestTranscoder();
	}

	class ImageIOTestTranscoder extends InternalPNGTranscoder {

		@Override
		protected WriteAdapter createWriteAdapter() {
			return new PNGTranscoderImageIOWriteAdapter();
		}

	}

}
