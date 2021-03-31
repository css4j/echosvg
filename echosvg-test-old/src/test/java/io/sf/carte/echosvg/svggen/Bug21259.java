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
package io.sf.carte.echosvg.svggen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.TestReport;

/**
 * Checks that the streamed root is not removed from its parent as shown by bug
 * report 21259.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class Bug21259 extends AbstractTest {
	@Override
	public TestReport runImpl() throws Exception {
		Document document = SVGDOMImplementation.getDOMImplementation()
				.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		ctx.setComment("Test");
		SVGGraphics2D graphics = new SVGGraphics2D(ctx, false);
		graphics.setSVGCanvasSize(new Dimension(600, 400));

		graphics.setColor(Color.red);
		graphics.setBackground(Color.black);
		graphics.fill(new Rectangle(0, 0, 100, 100));

		// Populate the Document's root with the content of the tree
		Element root = document.getDocumentElement();
		graphics.getRoot(root);
		Writer writer = new StringWriter();
		graphics.stream(root, writer);

		assertTrue(root.getParentNode() == document);
		return reportSuccess();
	}
}
