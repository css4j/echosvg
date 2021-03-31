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
package io.sf.carte.echosvg.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests Element.setIdAttributeNS.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ElementSetIdAttributeNSTest extends DOM3Test {

	private static final String ATTR_NAME = "blah";

	private static final String ATTR_VALUE = "abc";

	@Override
	public boolean runImplBasic() throws Exception {
		Document doc = newSVGDoc();
		doc.getDocumentElement().setAttributeNS(null, ATTR_NAME, ATTR_VALUE);
		if (!setIdAttributeWorks(doc, true)) {
			return false;
		}
		return setIdAttributeWorks(doc, false);
	}

	private boolean setIdAttributeWorks(Document doc, boolean isId) {
		doc.getDocumentElement().setIdAttributeNS(null, ATTR_NAME, isId);
		Element e = doc.getElementById(ATTR_VALUE);
		if (isId) {
			return e == doc.getDocumentElement();
		} else {
			return e == null;
		}
	}
}
