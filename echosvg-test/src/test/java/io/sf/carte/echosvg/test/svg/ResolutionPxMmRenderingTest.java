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
import io.sf.carte.echosvg.transcoder.svg.SVGAbstractTranscoder;

/**
 * Checks for regressions in rendering of SVG with varying resolution.
 * 
 * @see io.sf.carte.echosvg.transcoder.image.test.ResolutionTest
 *
 * @author See Git history.
 * @version $Id$
 */
class ResolutionPxMmRenderingTest extends RenderingTest {

	private final float pxToMM;

	/**
	 * Construct a new test.
	 * 
	 * @param pxToMM the pixel-to-mm ratio (default is {@code 0.2645833}.
	 */
	public ResolutionPxMmRenderingTest(float pxToMM) {
		super();
		this.pxToMM = pxToMM;
	}

	@Override
	protected CharSequence getImageSuffix() {
		String pxmm = Float.toString(pxToMM);
		CharSequence up = super.getImageSuffix();
		StringBuilder buf = new StringBuilder(up.length() + pxmm.length() + 5);
		buf.append(up).append('-').append(pxmm).append("pxmm");
		return buf;
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	@SuppressWarnings("deprecation")
	@Override
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = super.getTestImageTranscoder();
		t.addTranscodingHint(SVGAbstractTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, pxToMM);
		return t;
	}

}
