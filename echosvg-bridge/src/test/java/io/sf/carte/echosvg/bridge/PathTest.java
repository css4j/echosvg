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
import org.w3c.dom.svg.SVGPathSegList;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGOMPathElement;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * See issue #113
 */
public class PathTest {

	BridgeContext context;

	int errorCount;

	@BeforeEach
	public void setupBeforeEach() {
		context = createBridgeContext();
		errorCount = 0;
	}

	@Test
	public void testPathMissingD() {
		Element path = createDocumentWithPath();
		Document document = path.getOwnerDocument();
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(0, errorCount);
	}

	@Test
	public void testPathEmptyD() {
		Element path = createDocumentWithPath();
		Document document = path.getOwnerDocument();
		Attr d = document.createAttribute(SVGConstants.SVG_D_ATTRIBUTE);
		path.setAttributeNode(d);
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(0, errorCount);
	}

	@Test
	public void testPathDNone() {
		Element path = createDocumentWithPath();
		Document document = path.getOwnerDocument();
		Attr d = document.createAttribute(SVGConstants.SVG_D_ATTRIBUTE);
		d.setValue("none");
		path.setAttributeNode(d);
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(0, errorCount);
	}

	@Test
	public void testPathInvalidD() {
		Element path = createDocumentWithPath();
		Document document = path.getOwnerDocument();
		path.setAttribute(SVGConstants.SVG_D_ATTRIBUTE, "M 5 5, 10 10, 20 - 1");
		assertBounds(document, 4.65, 4.65, 195.35, 195.35);
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
	public void testPathOperations() {
		context.setDynamic(true);
		SVGOMPathElement path = (SVGOMPathElement) createDocumentWithPath();
		Document document = path.getOwnerDocument();
		path.setAttribute(SVGConstants.SVG_D_ATTRIBUTE, "M 5 5, 10 10");
		SVGPathSegList segList = path.getPathSegList();
		assertEquals(2, segList.getNumberOfItems());

		segList.appendItem(path.createSVGPathSegMovetoRel(10, 5));
		assertEquals(3, segList.getNumberOfItems());
		assertBounds(document, 4.65, 4.65, 195.35, 195.35);

		segList.insertItemBefore(path.createSVGPathSegMovetoAbs(20, 25), 2);
		assertEquals(4, segList.getNumberOfItems());
		assertBounds(document, 4.65, 4.65, 195.35, 195.35);

		path.setAttribute(CSSConstants.CSS_VISIBILITY_PROPERTY, "hidden");
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		path.removeAttribute(CSSConstants.CSS_VISIBILITY_PROPERTY);
		assertBounds(document, 4.65, 4.65, 195.35, 195.35);


		segList.replaceItem(path.createSVGPathSegMovetoAbs(35, 35), 0);
		segList.replaceItem(path.createSVGPathSegMovetoAbs(45, 45), 1);
		segList.removeItem(2);
		assertEquals(3, segList.getNumberOfItems());
		assertEquals("M 35.0 35.0 M 45.0 45.0 m 10.0 5.0", segList.toString());

		segList.clear();
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(0, errorCount);
	}

	private GraphicsNode createGraphicsNode(Document document) {
		return new GVTBuilder().build(context, document);
	}

	private static Element createDocumentWithPath() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		DocumentType dtd = impl.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		SVGDocument doc = (SVGDocument) impl.createDocument(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_SVG_TAG, dtd);

		SVGSVGElement svg = doc.getRootElement();
		svg.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "200");
		svg.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "200");
		Element rect = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_RECT_TAG);
		rect.setAttribute(SVGConstants.SVG_X_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_Y_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, "#107");
		svg.appendChild(rect);

		Element path = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_PATH_TAG);
		path.setAttribute(XMLConstants.XML_ID_ATTRIBUTE, "path1");
		path.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, "#111");
		svg.appendChild(path);

		return path;
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
