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

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;

/**
 * Checks for regressions in rendering of a document with animations.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAnimationRenderingAccuracyTest extends RenderingTest {

	private float time;

	public SVGAnimationRenderingAccuracyTest(float time) {
		super();
		this.time = time;
	}

	@Override
	protected String getImageSuffix() {
		return (time == 0f) ? "" : "-t" + Float.toString(time);
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	@Override
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = super.getTestImageTranscoder();
		t.addTranscodingHint(SVGAbstractTranscoder.KEY_SNAPSHOT_TIME, time);
		return t;
	}

}
