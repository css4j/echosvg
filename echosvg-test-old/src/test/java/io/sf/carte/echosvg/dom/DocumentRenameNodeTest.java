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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGGElement;
import org.w3c.dom.svg.SVGSVGElement;

/**
 * Tests Document.renameNode.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DocumentRenameNodeTest extends DOM3Test {
	@Override
	public boolean runImplBasic() throws Exception {
		Document doc = newSVGDoc();
		Element e2 = doc.createElementNS(SVG_NAMESPACE_URI, "g");
		boolean pass = e2 instanceof SVGGElement;
		e2 = (Element) ((AbstractDocument) doc).renameNode(e2, SVG_NAMESPACE_URI, "svg");
		pass = pass && e2 instanceof SVGSVGElement;
		Attr a = doc.createAttributeNS(null, "test");
		a = (Attr) ((AbstractDocument) doc).renameNode(a, EX_NAMESPACE_URI, "test2");
		pass = pass && a.getNamespaceURI().equals(EX_NAMESPACE_URI) && a.getLocalName().equals("test2");
		return pass;
	}
}
