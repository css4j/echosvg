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
package io.sf.carte.echosvg.test.swing;

import io.sf.carte.echosvg.swing.JSVGCanvas;

/**
 * This test makes sure that setting the canvas's document uri to null does not
 * cause a NullPointerException
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NullURITest extends NullSetSVGDocumentTest {

	@Override
	public String getName() {
		return "NullURITest." + getFilename();
	}

	@Override
	public Runnable getRunnable(final JSVGCanvas canvas) {
		return new Runnable() {
			@Override
			public void run() {
				canvas.setURI(null);
			}
		};
	}

}
