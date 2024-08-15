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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Dimension2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.css.dom.CSSValue.Type;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.value.ColorValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Test PaintServer.
 */
public class PaintServerTest {

	BridgeContext context;

	@BeforeEach
	public void setupBeforeEach() {
		context = createBridgeContext();
	}

	@Test
	public void testConvertRGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "rgb(120, 30, 200)",
				ColorValue.RGB_FUNCTION);
		assertNotNull(color);
		assertEquals(120, color.getRed());
		assertEquals(30, color.getGreen());
		assertEquals(200, color.getBlue());
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertColorFunction() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "color(display-p3 0.6 0.2 0.7)",
				ColorValue.CS_DISPLAY_P3);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.6f, comp[0]);
		assertEquals(.2f, comp[1]);
		assertEquals(.7f, comp[2]);
		assertEquals(255, color.getAlpha());

		assertSame(CSSColorSpaces.getDisplayP3(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD50_P3() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "color(xyz-d50 0.07 0.08 0.19 / 0.969)",
				ColorValue.CS_XYZ_D50);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.07f, comp[0], 1e-5f);
		assertEquals(.08f, comp[1], 1e-5f);
		assertEquals(.19f, comp[2], 1e-5f);
		assertEquals(247, color.getAlpha());

		assertSame(CSSColorSpaces.getDisplayP3(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD50_A98() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "color(xyz-d50 0.07 0.08 0.23 / 0.969)",
				ColorValue.CS_XYZ_D50);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.07f, comp[0], 1e-5f);
		assertEquals(.08f, comp[1], 1e-5f);
		assertEquals(.23f, comp[2], 1e-5f);
		assertEquals(247, color.getAlpha());

		assertSame(CSSColorSpaces.getA98RGB(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD50_REC() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "color(xyz-d50 0.07 0.08 0.28)",
				ColorValue.CS_XYZ_D50);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.07f, comp[0], 1e-5f);
		assertEquals(.08f, comp[1], 1e-5f);
		assertEquals(.28f, comp[2], 1e-5f);
		assertEquals(255, color.getAlpha());

		assertSame(CSSColorSpaces.getRec2020(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD50_ProPhoto() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "color(xyz-d50 0.2 0.08 0.3 / 0.969)",
				ColorValue.CS_XYZ_D50);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.2f, comp[0], 1e-5f);
		assertEquals(.08f, comp[1], 1e-5f);
		assertEquals(.3f, comp[2], 1e-5f);
		assertEquals(247, color.getAlpha());

		assertSame(CSSColorSpaces.getProphotoRGB(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD65() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "color(xyz-d65 0.065 0.033 0.06 / 0.2)",
				ColorValue.CS_XYZ_D65);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.06586f, comp[0], 1e-5f); // D50
		assertEquals(.0335833f, comp[1], 1e-5f); // D50
		assertEquals(.045024f, comp[2], 1e-5f); // D50
		assertEquals(51, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	private Color convertPaint(String ptyName, String paintDef, String cssSpace) {
		SVGDocument doc = createDocumentWithPaint(ptyName + ':' + paintDef);
		GraphicsNode gn = createGraphicsNode(context, doc);
		CSSStylableElement rect = (CSSStylableElement) doc.getElementById("rect1");
		assertNotNull(rect);

		CSSEngine cssEng = CSSUtilities.getCSSEngine(rect);
		int index = cssEng.getPropertyIndex(ptyName);
		Value val = cssEng.getComputedStyle(rect, null, index);

		assertNotNull(val);
		assertEquals(Type.COLOR, val.getPrimitiveType());
		ColorValue cssColor = val.getColorValue();
		assertEquals(cssSpace, cssColor.getCSSColorSpace());
		assertEquals(paintDef, cssColor.getCssText());

		Paint paint = PaintServer.convertPaint(rect, gn, val, 1f, context);
		assertNotNull(paint);

		return PaintServer.convertColor(cssColor, 1f, context);
	}

	private static SVGDocument createDocumentWithPaint(String rectStyle) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		DocumentType dtd = impl.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		SVGDocument doc = (SVGDocument) impl.createDocument(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_SVG_TAG, dtd);
		SVGSVGElement svg = doc.getRootElement();
		svg.setAttribute("width", "200");
		svg.setAttribute("height", "200");
		Element rect = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_RECT_TAG);
		rect.setAttribute(XMLConstants.XML_ID_ATTRIBUTE, "rect1");
		rect.setAttribute(SVGConstants.SVG_STYLE_ATTRIBUTE, rectStyle);
		svg.appendChild(rect);
		return doc;
	}

	private static BridgeContext createBridgeContext() {
		return new BridgeContext(new UserAgentAdapter() {

			@Override
			public Dimension2D getViewportSize() {
				return new Dimension(200, 200);
			}

		});
	}

	private static GraphicsNode createGraphicsNode(BridgeContext ctx, Document document) {
		return new GVTBuilder().build(ctx, document);
	}

}
