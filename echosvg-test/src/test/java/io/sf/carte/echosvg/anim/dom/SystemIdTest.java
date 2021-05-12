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

package io.sf.carte.echosvg.anim.dom;

import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.junit.Test;

/**
 * This class tests that there is System Id for each public Id in the
 * dtdids.properties resource file.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SystemIdTest {

	@Test
	public void test() throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle(SAXSVGDocumentFactory.DTDIDS, Locale.US);
		String dtdids = rb.getString(SAXSVGDocumentFactory.KEY_PUBLIC_IDS);

		StringTokenizer st = new StringTokenizer(dtdids, "-");
		int nIds = st.countTokens();
		String missingIds = "";
		for (int i = 0; i < nIds; i++) {
			String publicId = st.nextToken();
			publicId = "-" + publicId.trim();
			try {
				rb.getString(SAXSVGDocumentFactory.KEY_SYSTEM_ID + publicId.replace(' ', '_'));
			} catch (MissingResourceException e) {
				missingIds += "[" + publicId + "]  -- ";
			}
		}

		if (missingIds.length() != 0) {
			fail("Missing publicIds:" + missingIds);
		}

	}
}
