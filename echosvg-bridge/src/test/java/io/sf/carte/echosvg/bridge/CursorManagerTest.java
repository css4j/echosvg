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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Test cursor manager.
 */
public class CursorManagerTest {

	private static String documentURI;

	BridgeContext context;

	int errorCount;

	int messageCount;

	@BeforeAll
	public static void setupBeforeAll() {
		documentURI = Paths.get(".").toAbsolutePath().normalize().toUri().toString();
	}

	@BeforeEach
	public void setupBeforeEach() {
		context = createBridgeContext();
		errorCount = 0;
		messageCount = 0;
	}

	@Test
	public void testCursorUnsupported() {
		Element circle = createDocument(context, "unsupported");
		Cursor c = context.getCursorManager().convertCursor(circle);

		assertNotNull(c);
		assertEquals(Cursor.DEFAULT_CURSOR, c.getType());
		assertEquals(1, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCursorText() {
		Element circle = createDocument(context, "text");
		Cursor c = context.getCursorManager().convertCursor(circle);

		assertNotNull(c);
		assertEquals(Cursor.TEXT_CURSOR, c.getType());
		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCursorURI() {
		Element circle = createDocument(context,
				"url('../samples/tests/resources/images/hotSpotCenter.png') 3 4,wait");
		Cursor c = context.getCursorManager().convertCursor(circle);

		assertNotNull(c);
		assertEquals(Cursor.CUSTOM_CURSOR, c.getType());
		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCursorURI_Other_units() {
		Element circle = createDocument(context,
				"url('../samples/tests/resources/images/hotSpotCenter.png') 3pt 1mm,wait");
		Cursor c = context.getCursorManager().convertCursor(circle);

		assertNotNull(c);
		assertEquals(Cursor.CUSTOM_CURSOR, c.getType());
		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCursorURI_Calc() {
		Element circle = createDocument(context,
				"url('../samples/tests/resources/images/hotSpotCenter.png') 3 calc(2*2),wait");
		Cursor c = context.getCursorManager().convertCursor(circle);

		assertNotNull(c);
		assertEquals(Cursor.CUSTOM_CURSOR, c.getType());
		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCursorURI_Calc_Other_units() {
		Element circle = createDocument(context,
				"url('../samples/tests/resources/images/hotSpotCenter.png') calc(0.5pt + 0.001cm) calc(2*1mm),wait");
		Cursor c = context.getCursorManager().convertCursor(circle);

		assertNotNull(c);
		assertEquals(Cursor.CUSTOM_CURSOR, c.getType());
		assertEquals(0, errorCount);
		assertEquals(0, messageCount);
	}

	@Test
	public void testCursorURINotFound() {
		Element circle = createDocument(context,
				"url('../samples/nothing_here.png') 3 4,wait");
		Cursor c = context.getCursorManager().convertCursor(circle);

		assertNotNull(c);
		assertEquals(Cursor.WAIT_CURSOR, c.getType());
		assertEquals(0, errorCount);
		assertEquals(1, messageCount);
	}

	private static Element createDocument(BridgeContext context, String cursor) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		DocumentType dtd = impl.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		SVGDocument doc = (SVGDocument) impl.createDocument(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_SVG_TAG, dtd);

		doc.setDocumentURI(documentURI);

		SVGSVGElement svg = doc.getRootElement();
		svg.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "200");
		svg.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "200");

		CDATASection text = doc.createCDATASection("circle{cursor:" + cursor + "}");
		Element style = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_STYLE_TAG);
		style.appendChild(text);
		svg.appendChild(style);

		Element circle = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_CIRCLE_TAG);
		circle.setAttribute(SVGConstants.SVG_CX_ATTRIBUTE, "40");
		circle.setAttribute(SVGConstants.SVG_CY_ATTRIBUTE, "50");
		svg.appendChild(circle);

		context.setDocument(doc);
		context.initializeDocument(doc);

		return circle;
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

			@Override
			public void displayMessage(String message) {
				messageCount++;
			}

		});
	}

}
