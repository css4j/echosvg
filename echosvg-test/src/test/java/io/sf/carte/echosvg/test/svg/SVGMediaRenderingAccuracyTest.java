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

import java.net.MalformedURLException;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.transcoder.SVGAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;

/**
 * Checks for regressions in rendering of a document with a given media.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGMediaRenderingAccuracyTest extends ParameterizedRenderingAccuracyTest {

	@Test
	public void test() throws MalformedURLException {
		runTest("samples/tests/spec/styling/cssMedia.svg", "Screen");
		runTest("samples/tests/spec/styling/cssMedia.svg", "Print");
		runTest("samples/tests/spec/styling/cssMedia.svg", "Projection");
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	@Override
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = super.getTestImageTranscoder();
		t.addTranscodingHint(SVGAbstractTranscoder.KEY_MEDIA, parameter);
		return t;
	}

}
