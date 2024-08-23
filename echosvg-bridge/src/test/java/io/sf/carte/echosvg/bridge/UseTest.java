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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Test the <code>&lt;use&gt;</code> element.
 * <p>
 * Show that invalid <code>&lt;use&gt;</code> elements are handled according to
 * the specification, and do not contribute to the bounding box.
 * </p>
 */
public class UseTest {

	BridgeContext context;

	int errorCount;

	@BeforeEach
	public void setupBeforeEach() {
		context = createBridgeContext();
		errorCount = 0;
	}

	@Test
	public void testMissingHref() {
		Element use = createDocument();
		Document document = use.getOwnerDocument();
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		/*
		 * Not an error according to the specification, but useful anyway.
		 */
		assertEquals(1, errorCount);
	}

	@Test
	public void testEmptyHref() {
		Element use = createDocument();
		Document document = use.getOwnerDocument();
		Attr d = document.createAttribute(XMLConstants.XLINK_HREF_ATTRIBUTE);
		d.setValue("");
		use.setAttributeNode(d);
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		/*
		 * Not an error according to the specification, but useful anyway.
		 */
		assertEquals(1, errorCount);
	}

	@Timeout(1)
	@Test
	public void testHrefItself() {
		Element use = createDocument();
		Document document = use.getOwnerDocument();
		use.setAttribute(XMLConstants.XLINK_HREF_ATTRIBUTE, "#use1");
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(1, errorCount);
	}

	@Timeout(1)
	@Test
	public void testHrefParent() {
		Element use = createDocument();
		Document document = use.getOwnerDocument();
		use.setAttribute(XMLConstants.XLINK_HREF_ATTRIBUTE, "#root");
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(1, errorCount);
	}

	@Test
	public void testHrefNonSVG() {
		Element use = createDocument();
		Document document = use.getOwnerDocument();

		Element root = document.getDocumentElement();
		Element fo = document.createElementNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_FOREIGN_OBJECT_TAG);
		Element div = document.createElementNS("http://www.w3.org/1999/xhtml", "div");
		div.setAttribute(XMLConstants.XML_ID_ATTRIBUTE, "div1");
		div.setTextContent("Hi");
		fo.appendChild(div);
		root.appendChild(fo);

		use.setAttribute(XMLConstants.XLINK_HREF_ATTRIBUTE, "#div1");
		assertBounds(document, 99.5, 99.5, 100.5, 100.5);
		assertEquals(1, errorCount);
	}

	@Test
	public void testInvalidHref() {
		Element use = createDocument();
		Document document = use.getOwnerDocument();
		use.setAttribute(XMLConstants.XLINK_HREF_ATTRIBUTE, "/:::");
		/*
		 * Generally users want this kind of exception to be thrown, instead of
		 * just being reported by the user agent.
		 */
		BridgeException be = assertThrows(BridgeException.class, () -> createGraphicsNode(document));
		assertEquals("uri.unsecure", be.getCode());
	}

	private void assertBounds(Document document, double x, double y, double width, double height) {
		GraphicsNode node = createGraphicsNode(document);
		Rectangle2D bnds = node.getBounds();
		assertEquals(x, bnds.getX(), 0.01, "Wrong x");
		assertEquals(y, bnds.getY(), 0.01, "Wrong y");
		assertEquals(width, bnds.getWidth(), 0.01, "Wrong width");
		assertEquals(height, bnds.getHeight(), 0.01, "Wrong height");
	}

	private GraphicsNode createGraphicsNode(Document document) {
		return new GVTBuilder().build(context, document);
	}

	private static Element createDocument() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		DocumentType dtd = impl.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		SVGDocument doc = (SVGDocument) impl.createDocument(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_SVG_TAG, dtd);

		SVGSVGElement svg = doc.getRootElement();
		svg.setAttribute(XMLConstants.XML_ID_ATTRIBUTE, "root");
		svg.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "200");
		svg.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "200");

		Element g = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_G_TAG);
		g.setAttribute(XMLConstants.XML_ID_ATTRIBUTE, "g1");
		svg.appendChild(g);

		Element rect = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_RECT_TAG);
		rect.setAttribute(XMLConstants.XML_ID_ATTRIBUTE, "rect1");
		rect.setAttribute(SVGConstants.SVG_X_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_Y_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "100");
		rect.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, "#107");
		g.appendChild(rect);

		Element use = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_USE_TAG);
		use.setAttribute(XMLConstants.XML_ID_ATTRIBUTE, "use1");
		use.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "120");
		use.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "120");
		svg.appendChild(use);

		return use;
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
