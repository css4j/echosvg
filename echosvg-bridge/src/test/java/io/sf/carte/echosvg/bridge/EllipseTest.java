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
package io.sf.carte.echosvg.bridge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Test ellipse bounding boxes.
 */
public class EllipseTest {

	BridgeContext context;

	int errorCount;

	@BeforeEach
	public void setupBeforeEach() {
		context = createBridgeContext();
		errorCount = 0;
	}

	@Test
	public void testMissingR() {
		Element ellipse = createDocumentWithEllipse();
		Document document = ellipse.getOwnerDocument();
		assertBounds(document, 40, 50, 160, 150);
		assertEquals(0, errorCount);
	}

	@Test
	public void testEmptyRX() {
		Element ellipse = createDocumentWithEllipse();
		Document document = ellipse.getOwnerDocument();
		Attr d = document.createAttribute(SVGConstants.SVG_RX_ATTRIBUTE);
		ellipse.setAttributeNode(d);
		assertBounds(document, 40, 50, 160, 150);
		assertEquals(1, errorCount);
	}

	@Test
	public void testInvalidRX() {
		Element ellipse = createDocumentWithEllipse();
		Document document = ellipse.getOwnerDocument();
		ellipse.setAttribute(SVGConstants.SVG_RX_ATTRIBUTE, ",");
		assertBounds(document, 40, 50, 160, 150);
		assertEquals(1, errorCount);
	}

	private void assertBounds(Document document, double x, double y, double width, double height) {
		GraphicsNode node = createGraphicsNode(document);
		Rectangle2D bnds = node.getBounds();
		assertEquals(x, bnds.getX(), 0.01, "Wrong x");
		assertEquals(y, bnds.getY(), 0.01, "Wrong y");
		assertEquals(width, bnds.getWidth(), 0.01, "Wrong width");
		assertEquals(height, bnds.getHeight(), 0.01, "Wrong height");
	}

	@Test
	public void testOperations() {
		context.setDynamic(true);
		Element ellipse = createDocumentWithEllipse();
		Document document = ellipse.getOwnerDocument();
		ellipse.setAttribute(SVGConstants.SVG_RX_ATTRIBUTE, "20");
		ellipse.setAttribute(SVGConstants.SVG_RY_ATTRIBUTE, "35");
		assertBounds(document, 20, 15, 180, 185);

		ellipse.setAttribute(CSSConstants.CSS_VISIBILITY_PROPERTY, "hidden");
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		ellipse.removeAttribute(CSSConstants.CSS_VISIBILITY_PROPERTY);
		assertBounds(document, 20, 15, 180, 185);

		assertEquals(0, errorCount);
	}

	private GraphicsNode createGraphicsNode(Document document) {
		return new GVTBuilder().build(context, document);
	}

	private static Element createDocumentWithEllipse() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		DocumentType dtd = impl.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		SVGDocument doc = (SVGDocument) impl.createDocument(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_SVG_TAG, dtd);

		SVGSVGElement svg = doc.getRootElement();
		svg.setAttribute("width", "200");
		svg.setAttribute("height", "200");
		Element rect = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_RECT_TAG);
		rect.setAttribute(SVGConstants.SVG_X_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_Y_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, "#107");
		svg.appendChild(rect);

		Element ellipse = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_ELLIPSE_TAG);
		ellipse.setAttribute(SVGConstants.SVG_CX_ATTRIBUTE, "40");
		ellipse.setAttribute(SVGConstants.SVG_CY_ATTRIBUTE, "50");
		svg.appendChild(ellipse);

		return ellipse;
	}

	private BridgeContext createBridgeContext() {
		return new BridgeContext(new UserAgentAdapter() {

			@Override
			public Dimension2D getViewportSize() {
				return new Dimension(200, 200);
			}

			@Override
			public void displayError(String message) {
				errorCount++;
			}

		});
	}

}
