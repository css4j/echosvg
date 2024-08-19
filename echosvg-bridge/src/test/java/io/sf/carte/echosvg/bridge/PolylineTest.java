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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPointList;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGOMPolylineElement;
import io.sf.carte.echosvg.dom.svg.SVGPointItem;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Check polyline bounding boxes.
 */
public class PolylineTest {

	BridgeContext context;

	int errorCount;

	@BeforeEach
	public void setupBeforeEach() {
		context = createBridgeContext();
		errorCount = 0;
	}

	@Test
	public void testMissingPoints() {
		Element poly = createDocumentWithPoly();
		Document document = poly.getOwnerDocument();
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(0, errorCount);
	}

	@Test
	public void testEmptyPoints() {
		Element poly = createDocumentWithPoly();
		Document document = poly.getOwnerDocument();
		poly.setAttribute(SVGConstants.SVG_POINTS_ATTRIBUTE, "");
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(0, errorCount);
	}

	@Test
	public void testInvalidPoints() {
		Element poly = createDocumentWithPoly();
		Document document = poly.getOwnerDocument();
		poly.setAttribute(SVGConstants.SVG_POINTS_ATTRIBUTE, "5, 5 10, 10 20 - 1");
		assertBounds(document, 5, 5, 195, 195);
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
	public void testPolyOperations() {
		context.setDynamic(true);
		SVGOMPolylineElement poly = (SVGOMPolylineElement) createDocumentWithPoly();
		Document document = poly.getOwnerDocument();
		poly.setAttribute(SVGConstants.SVG_POINTS_ATTRIBUTE, "5,5 10,20");

		SVGPointList ptsList = poly.getPoints();
		assertEquals(2, ptsList.getNumberOfItems());

		SVGPointItem point = new SVGPointItem(14, 4);
		ptsList.appendItem(point);
		assertEquals(3, ptsList.getNumberOfItems());
		assertBounds(document, 5, 4, 195, 196);

		poly.setAttribute(CSSConstants.CSS_VISIBILITY_PROPERTY, "hidden");
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		poly.removeAttribute(CSSConstants.CSS_VISIBILITY_PROPERTY);
		assertBounds(document, 5, 4, 195, 196);

		ptsList.removeItem(1);
		assertEquals(2, ptsList.getNumberOfItems());
		assertEquals("5.0,5.0 14.0,4.0", ptsList.toString());

		ptsList.clear();
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(0, errorCount);
	}

	private GraphicsNode createGraphicsNode(Document document) {
		return new GVTBuilder().build(context, document);
	}

	private static Element createDocumentWithPoly() {
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

		Element poly = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_POLYLINE_TAG);
		svg.appendChild(poly);

		return poly;
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
