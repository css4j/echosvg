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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Checks for regressions in rendering of a document with a given alternate
 * stylesheet.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGTextContentRenderingAccuracyTest extends ParameterizedRenderingAccuracyTest {

	private String script; // null
	private String onload; // null
	private String parameter; // null

	public void setScript(String script) {
		this.script = script;
	}

	public void setOnLoadFunction(String onload) {
		this.onload = onload;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	protected Document manipulateSVGDocument(Document doc) {

		Element root = doc.getDocumentElement();
		String function;
		if (parameter == null) {
			function = onload + "()";
		} else {
			function = onload + "(" + parameter + ")";
		}
		root.setAttributeNS(null, "onload", function);

		Element scriptElement = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SCRIPT_TAG);

		scriptElement.setAttributeNS(XMLConstants.XLINK_NAMESPACE_URI, XMLConstants.XLINK_HREF_QNAME, script);

		root.appendChild(scriptElement);

		return doc;
	}

}
