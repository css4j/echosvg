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

import java.net.URL;

import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.svg.SVGAbstractTranscoder;

/**
 * Checks for regressions in rendering of SVG with alternate style sheets and/or
 * a user style sheet.
 * 
 * @author See Git history.
 * @version $Id$
 */
class AltUserSheetRenderingTest extends RenderingTest {

	static final String DEFAULT_USER_SHEET = "/io/sf/carte/echosvg/transcoder/UserAgentStyleSheet.css";

	/**
	 * Alternate sheet name.
	 */
	private String altSheet = null;

	/**
	 * Classpath to user sheet.
	 */
	private String userSheetClasspath = null;

	public AltUserSheetRenderingTest() {
		super();
	}

	/**
	 * Set the name of the alternate style sheet(s).
	 * 
	 * @param altSheet the name of the alternate style sheet.
	 */
	public void setAlternateSheet(String altSheet) {
		this.altSheet = altSheet;
	}

	/**
	 * Set the classpath for the user style sheet.
	 * 
	 * @param userSheetClasspath the location of the user style sheet in classpath.
	 */
	public void setUserSheetClasspath(String userSheetClasspath) {
		this.userSheetClasspath = userSheetClasspath;
	}

	/**
	 * Returns the <code>ImageTranscoder</code> the Test should use
	 */
	@Override
	ImageTranscoder getTestImageTranscoder() {
		ImageTranscoder t = super.getTestImageTranscoder();

		if (userSheetClasspath != null) {
			URL userSheet = AltUserSheetRenderingTest.class.getResource(userSheetClasspath);
			t.addTranscodingHint(SVGAbstractTranscoder.KEY_USER_STYLESHEET_URI,
					userSheet.toExternalForm());
		}

		if (altSheet != null) {
			t.addTranscodingHint(SVGAbstractTranscoder.KEY_ALTERNATE_STYLESHEET, altSheet);
		}

		return t;
	}

	@Override
	protected CharSequence getImageSuffix() {
		CharSequence suf = super.getImageSuffix();

		if (altSheet != null) {
			StringBuilder buf = new StringBuilder(suf.length() + altSheet.length() + 1);
			buf.append(suf).append('_').append(altSheet);
			return buf;
		}

		return suf;
	}

}
