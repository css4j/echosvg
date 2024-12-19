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

import io.sf.carte.echosvg.transcoder.ErrorHandler;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.svg.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.test.DummyErrorHandler;

/**
 * Checks for regressions in rendering of a document with animations.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAnimationRenderingAccuracyTest extends RenderingTest {

	private final int expectedErrorCount;

	private float time;

	public SVGAnimationRenderingAccuracyTest(float time, int expectedErrorCount) {
		super();
		this.time = time;
		this.expectedErrorCount = expectedErrorCount;
	}

	@Override
	protected CharSequence getImageSuffix() {
		CharSequence up = super.getImageSuffix();
		if (time == 0f) {
			return up;
		}
		String stime = Float.toString(time);
		StringBuilder buf = new StringBuilder(up.length() + stime.length() + 2);
		buf.append(up).append("-t").append(stime);
		return buf;
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	@Override
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = super.getTestImageTranscoder();
		t.addTranscodingHint(SVGAbstractTranscoder.KEY_SNAPSHOT_TIME, time);
		t.setErrorHandler(new DummyErrorHandler());
		return t;
	}

	@Override
	ImageTranscoder createTestImageTranscoder() {
		return new ErrIgnoreTranscoder();
	}

	@Override
	protected void checkErrorHandler(ErrorHandler errorHandler) {
		DummyErrorHandler handler = (DummyErrorHandler) errorHandler;
		handler.assertErrorCount(expectedErrorCount);
		super.checkErrorHandler(errorHandler);
	}

}
