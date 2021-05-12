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
package io.sf.carte.echosvg.swing.test;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.test.svg.JSVGRenderingAccuracyTest;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Test setDocument on JSVGComponent with non-EchoSVG SVGOMDocument.
 *
 * This test constructs a generic Document with SVG content then it ensures that
 * when this is passed to JSVGComponet.setDocument it is properly imported to an
 * SVGOMDocument and rendered from there.
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SetSVGDocumentTest extends JSVGRenderingAccuracyTest {
	public SetSVGDocumentTest() {
	}

	/* JSVGCanvasHandler.Delegate Interface */
	@Override
	public boolean canvasInit(JSVGCanvas canvas) {
		DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
		Document doc = impl.createDocument(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SVG_TAG, null);
		Element e = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_RECT_TAG);
		e.setAttribute("x", "10");
		e.setAttribute("y", "10");
		e.setAttribute("width", "100");
		e.setAttribute("height", "50");
		e.setAttribute("fill", "crimson");
		doc.getDocumentElement().appendChild(e);

		e = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_CIRCLE_TAG);
		e.setAttribute("cx", "55");
		e.setAttribute("cy", "35");
		e.setAttribute("r", "30");
		e.setAttribute("fill", "gold");
		doc.getDocumentElement().appendChild(e);

		canvas.setDocument(doc);
		return false; // We didn't trigger a load event.
	}

	@Override
	public boolean canvasUpdated(JSVGCanvas canvas) {
		return true;
	}
}
