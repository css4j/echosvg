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
import java.awt.color.ColorSpace;
import java.awt.geom.Dimension2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.value.ColorValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.ext.awt.color.StandardColorSpaces;
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
	public void testConvertRelativeRGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"rgb(from rgb(50 200 180) r calc(g - 90) b / alpha)", null);
		assertNotNull(color);
		assertEquals(50, color.getRed());
		assertEquals(110, color.getGreen());
		assertEquals(180, color.getBlue());
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertRelativeRGBIdent() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"rgb(from lime 120 calc(g - 120) 200)", null);
		assertNotNull(color);
		assertEquals(120, color.getRed());
		assertEquals(135, color.getGreen());
		assertEquals(200, color.getBlue());
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertRelativeRGB_ColorFunction() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"rgb(from color(srgb 0.196078431 0.784313725 0.705882353) r calc(g - 90) b / alpha)",
			null);
		// rgb(50 200 180)
		assertNotNull(color);
		assertEquals(50, color.getRed());
		assertEquals(110, color.getGreen());
		assertEquals(180, color.getBlue());
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

		assertNull(context.getColorSpace());

		assertEquals("#a628b9", toHexString(color.getRGBComponents(null)));
	}

	private static String toHexString(float[] comp) {
		String s = '#' + hexComp(comp[0]) + hexComp(comp[1]) + hexComp(comp[2]);
		if (comp[3] != 1f) {
			s += hexComp(comp[3]);
		}
		return s;
	}

	private static String hexComp(float f) {
		String s = Integer.toHexString(Math.round(f * 255f));
		if (s.length() == 1) {
			s = '0' + s;
		}
		return s;
	}

	@Test
	public void testConvertRelativeColorFunctionSRGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(from rgb(100 60 200) srgb r calc(g + 0.1) b/alpha)", null);
		// Reference color is color(srgb 0.392156863 0.235294118 0.784313725)
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.392156863f, comp[0], 1e-4f);
		assertEquals(0.335294118f, comp[1], 1e-4f);
		assertEquals(0.784313725f, comp[2], 1e-4f);
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertRelativeColorFunctionSRGB_SRGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(from color(srgb 0.980392 0.980392 0.0705882) srgb r calc(g - 0.1) b/alpha)",
			null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.980392f, comp[0], 1e-4f);
		assertEquals(0.880392f, comp[1], 1e-4f);
		assertEquals(0.0705882f, comp[2], 1e-4f);
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionP3() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "color(display-p3 0.02 0.9 0.7)",
			ColorValue.CS_DISPLAY_P3);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.02f, comp[0]);
		assertEquals(.9f, comp[1]);
		assertEquals(.7f, comp[2]);
		assertEquals(255, color.getAlpha());

		assertSame(StandardColorSpaces.getDisplayP3(), context.getColorSpace());
	}

	@Test
	public void testConvertRelativeColorFunctionP3() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(from rgb(100 60 200) display-p3 r calc(g + 0.1) b)", null);
		// Reference color is color(display-p3 0.37005 0.24248 0.75564)
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.37f, comp[0], 1e-4f);
		assertEquals(.34248f, comp[1], 1e-4f);
		assertEquals(.75564f, comp[2], 1e-4f);
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionLinearSRGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(srgb-linear 0.14 0.002 0.064)", ColorValue.CS_SRGB_LINEAR);
		assertNotNull(color);
		assertSame(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB), color.getColorSpace());

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.14f, comp[0]);
		assertEquals(.002f, comp[1]);
		assertEquals(.064f, comp[2]);
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());

		assertEquals("#690748", toHexString(color.getRGBComponents(null)));
	}

	@Test
	public void testConvertColorFunctionXYZD50_RGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(xyz-d50 0.3636655 0.6739725 0.0938054)", ColorValue.CS_XYZ_D50);
		assertNotNull(color);
		assertSame(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), color.getColorSpace());

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.3636655f, comp[0], 1e-7f);
		assertEquals(0.6739725f, comp[1], 1e-7f);
		assertEquals(0.0938054f, comp[2], 1e-7f);
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());

		assertEquals("#0bf80c", toHexString(color.getRGBComponents(null)));
	}

	@Test
	public void testConvertColorFunctionXYZD50_P3() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(xyz-d50 0.07 0.08 0.19 / 0.969)", ColorValue.CS_XYZ_D50);
		assertNotNull(color);

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.07f, comp[0], 1e-5f);
		assertEquals(.08f, comp[1], 1e-5f);
		assertEquals(.19f, comp[2], 1e-5f);
		assertEquals(247, color.getAlpha());

		assertSame(StandardColorSpaces.getDisplayP3(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD50_A98() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(xyz-d50 0.07 0.08 0.23 / 0.969)", ColorValue.CS_XYZ_D50);
		assertNotNull(color);

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.07f, comp[0], 1e-5f);
		assertEquals(.08f, comp[1], 1e-5f);
		assertEquals(.23f, comp[2], 1e-5f);
		assertEquals(247, color.getAlpha());

		assertSame(StandardColorSpaces.getA98RGB(), context.getColorSpace());
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

		assertSame(StandardColorSpaces.getRec2020(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD50_ProPhoto() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(xyz-d50 0.2 0.08 0.3 / 0.969)", ColorValue.CS_XYZ_D50);
		assertNotNull(color);

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.2f, comp[0], 1e-5f);
		assertEquals(.08f, comp[1], 1e-5f);
		assertEquals(.3f, comp[2], 1e-5f);
		assertEquals(247, color.getAlpha());

		assertSame(StandardColorSpaces.getProphotoRGB(), context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZD65() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(xyz-d65 0.065 0.033 0.06 / 0.2)", ColorValue.CS_XYZ_D65);
		assertNotNull(color);
		assertSame(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), color.getColorSpace());

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.06586f, comp[0], 1e-5f); // D50
		assertEquals(.0335833f, comp[1], 1e-5f); // D50
		assertEquals(.045024f, comp[2], 1e-5f); // D50
		assertEquals(51, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertColorFunctionXYZ() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(xyz 0.065 0.033 0.06 / 0.2)", ColorValue.CS_XYZ);
		assertNotNull(color);
		assertSame(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), color.getColorSpace());

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(.06586f, comp[0], 1e-5f); // D50
		assertEquals(.0335833f, comp[1], 1e-5f); // D50
		assertEquals(.045024f, comp[2], 1e-5f); // D50
		assertEquals(51, color.getAlpha());

		assertNull(context.getColorSpace());

		assertEquals("#65054533", toHexString(color.getRGBComponents(null)));
	}

	@Test
	public void testConvertColorFunctionXYZ_2() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"color(xyz 0.33801785 0.67248034 0.1155108)", ColorValue.CS_XYZ);
		assertNotNull(color);
		assertSame(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), color.getColorSpace());

		float[] comp = color.getColorComponents(null);
		assertEquals(0.3637795f, comp[0], 5e-5f); // D50
		assertEquals(0.6740978f, comp[1], 1e-5f); // D50
		assertEquals(0.09387444f, comp[2], 3e-5f); // D50
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());

		assertEquals("#0bf80c", toHexString(color.getRGBComponents(null)));
	}

	@Test
	public void testConvertLab_p3() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "lab(20.6% 48.8 -13.8 / 0.2)",
			null);
		assertNotNull(color);

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.3713666f, comp[0], 1e-5f); // Display P3
		assertEquals(0f, comp[1], 1e-5f); // Display P3
		assertEquals(0.2716182f, comp[2], 1e-4f); // Display P3
		assertEquals(51, color.getAlpha());

		assertSame(StandardColorSpaces.getDisplayP3(), context.getColorSpace());
	}

	@Test
	public void testConvertLab_rec2020() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "lab(51.3% 104.5 -28 / 0.2)",
			null);
		assertNotNull(color);

		assertSame(StandardColorSpaces.getRec2020(), context.getColorSpace());

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.7942925f, comp[0], 1e-5f); // ITU Rec. 2020
		assertEquals(0.009957f, comp[1], 1e-5f); // ITU Rec. 2020
		assertEquals(0.6187307f, comp[2], 1e-5f); // ITU Rec. 2020
		assertEquals(51, color.getAlpha());
	}

	@Test
	public void testConvertLabSRGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "lab(83% 21.5 20.08)", null);
		assertNotNull(color);

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.999577f, comp[0], 1e-5f); // R
		assertEquals(0.750328f, comp[1], 1e-5f); // G
		assertEquals(0.667233f, comp[2], 1e-5f); // B
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertLCh_p3() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"lch(20.6% 50.71371 344.2098 / 0.2)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.3713666f, comp[0], 1e-5f); // Display P3
		assertEquals(0f, comp[1], 1e-5f); // Display P3
		assertEquals(0.2716182f, comp[2], 1e-4f); // Display P3
		assertEquals(51, color.getAlpha());

		assertSame(StandardColorSpaces.getDisplayP3(), context.getColorSpace());
	}

	@Test
	public void testConvertLCh_rec2020() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "lch(51.3% 108.2 345 / 0.2)",
			null);
		assertNotNull(color);

		assertSame(StandardColorSpaces.getRec2020(), context.getColorSpace());

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.794338f, comp[0], 1e-5f); // ITU Rec. 2020
		assertEquals(0.0098127f, comp[1], 1e-5f); // ITU Rec. 2020
		assertEquals(0.61876f, comp[2], 1e-4f); // ITU Rec. 2020
		assertEquals(51, color.getAlpha());
	}

	@Test
	public void testConvertLCh_sRGB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "lch(48% 77 33)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.835736f, comp[0], 1e-5f); // sRGB
		assertEquals(0.17189053f, comp[1], 1e-5f); // sRGB
		assertEquals(0.18918473f, comp[2], 1e-5f); // sRGB
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertLCh_var() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"var(--color);--color:lch(48% 77 33)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.835736f, comp[0], 1e-5f); // sRGB
		assertEquals(0.17189053f, comp[1], 1e-5f); // sRGB
		assertEquals(0.18918473f, comp[2], 1e-5f); // sRGB
		assertEquals(255, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertLCh_99Pcnt() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "lch(99% 110 60)", null);
		assertNotNull(color);
		assertSame(StandardColorSpaces.getProphotoRGB(), color.getColorSpace());

		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(1f, comp[0], 1e-3f); // Prophoto
		assertEquals(0.975576f, comp[1], 1e-3f); // Prophoto
		assertEquals(0.9142f, comp[2], 1e-3f); // Prophoto
		assertEquals(255, color.getAlpha());

		assertSame(StandardColorSpaces.getProphotoRGB(), context.getColorSpace());
	}

	@Test
	public void testConvertOkLab() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"oklab(33.5% 0.154 -0.0386 / 0.8)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.27054f, comp[0], 1e-5f); // Rec 2020
		assertEquals(0.02655f, comp[1], 1e-5f); // Rec 2020
		assertEquals(0.208f, comp[2], 1e-3f); // Rec 2020
		assertEquals(204, color.getAlpha());

		assertSame(StandardColorSpaces.getRec2020(), context.getColorSpace());
	}

	@Test
	public void testConvertOkLCh() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"oklch(33.5% 0.1588 345.93 / 0.8)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.27056f, comp[0], 1e-5f); // Rec 2020
		assertEquals(0.026513f, comp[1], 1e-5f); // Rec 2020
		assertEquals(0.208f, comp[2], 1e-3f); // Rec 2020
		assertEquals(204, color.getAlpha());

		assertSame(StandardColorSpaces.getRec2020(), context.getColorSpace());
	}

	@Test
	public void testConvertOkLChRelative() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY,
			"oklch(from lch(20.620797% 50.824408 344.25594 / 0.8) l c h / alpha)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.27055f, comp[0], 1e-5f); // Rec 2020
		assertEquals(0.02653f, comp[1], 1e-5f); // Rec 2020
		assertEquals(0.208f, comp[2], 1e-3f); // Rec 2020
		assertEquals(204, color.getAlpha());

		assertSame(StandardColorSpaces.getRec2020(), context.getColorSpace());
	}

	@Test
	public void testConvertHSL() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "hsl(210 94% 29% / 0.2)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.0174f, comp[0], 1e-4f); // R
		assertEquals(0.29f, comp[1], 1e-4f); // G
		assertEquals(0.5626f, comp[2], 1e-4f); // B
		assertEquals(51, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertHWB() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "hwb(210 1.7% 43.7%/0.8)", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.017f, comp[0], 1e-3f); // R
		assertEquals(0.29f, comp[1], 1e-3f); // G
		assertEquals(0.563f, comp[2], 1e-3f); // B
		assertEquals(204, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	@Test
	public void testConvertCurrentColor() {
		Color color = convertPaint(CSSConstants.CSS_FILL_PROPERTY, "hwb(210 1.7% 43.7%/0.8)",
			"color:hwb(210 1.7% 43.7%/0.8);fill:currentColor", null);
		assertNotNull(color);
		float[] comp = new float[3];
		color.getColorComponents(comp);
		assertEquals(0.017f, comp[0], 1e-3f); // R
		assertEquals(0.29f, comp[1], 1e-3f); // G
		assertEquals(0.563f, comp[2], 1e-3f); // B
		assertEquals(204, color.getAlpha());

		assertNull(context.getColorSpace());
	}

	private Color convertPaint(String ptyName, String paintDef, String cssSpace) {
		return convertPaint(ptyName, paintDef, ptyName + ':' + paintDef, cssSpace);
	}

	private Color convertPaint(String ptyName, String paintDef, String styleDecl, String cssSpace) {
		SVGDocument doc = createDocumentWithPaint(styleDecl);
		GraphicsNode gn = createGraphicsNode(context, doc);
		CSSStylableElement rect = (CSSStylableElement) doc.getElementById("rect1");
		assertNotNull(rect);

		CSSEngine cssEng = CSSUtilities.getCSSEngine(rect);
		int index = cssEng.getPropertyIndex(ptyName);
		Value val = cssEng.getComputedStyle(rect, null, index);

		assertNotNull(val);
		assertEquals(Type.COLOR, val.getPrimitiveType());
		ColorValue cssColor = val.getColorValue();
		if (cssSpace != null) {
			assertEquals(cssSpace, cssColor.getCSSColorSpace());
			assertEquals(paintDef, cssColor.getCssText());
		}

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
		svg.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "200");
		svg.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "200");
		Element rect = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI,
			SVGConstants.SVG_RECT_TAG);
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
