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
package io.sf.carte.echosvg.bridge.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

import org.junit.jupiter.api.Test;
import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.css.om.typed.CSSStyleValue;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.view.ViewCSS;
import org.w3c.dom.views.DocumentView;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.anim.dom.SVGStylableElement;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.GVTBuilder;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.css.dom.CSSValue;
import io.sf.carte.echosvg.css.dom.CSSValue.CssType;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Test value modifications.
 */
public class ModificationsTest {

	@Test
	void testFillRGB() {
		CSSStyleValue value = updateValue(CSSConstants.CSS_FILL_PROPERTY, "rgb(120, 30, 200)",
				"color(display-p3 0.6 0.2 0.7)");
		assertNotNull(value);
	}

	@Test
	void testFillColorFunction() {
		CSSStyleValue value = updateValue(CSSConstants.CSS_FILL_PROPERTY, "color(display-p3 0.6 0.2 0.7)",
				"rgb(120, 30, 200)");
		assertNotNull(value);
	}

	static CSSStyleValue updateValue(String ptyName, String ptyValue, String newValue) {
		BridgeContext ctx = createBridgeContext();
		SVGDocument doc = createDocumentWithStyle(ptyName + ':' + ptyValue);
		GraphicsNode gn = createGraphicsNode(ctx, doc);
		assertNotNull(gn);
		SVGStylableElement rect = (SVGStylableElement) doc.getElementById("rect1");
		assertNotNull(rect);

		ViewCSS view = (ViewCSS) ((DocumentView) doc).getDefaultView();
		CSSStyleDeclaration cs = view.getComputedStyle(rect, null);
		CSSValue val = (CSSValue) cs.getCSSStyleValue(ptyName);

		assertNotNull(val);
		assertEquals(CssType.TYPED, val.getCssValueType());
		assertEquals(ptyValue, val.getCssText());

		rect.getStyle().setProperty(ptyName, newValue, null);
		String styleDecl = ptyName + ": " + newValue + ';';
		assertEquals(styleDecl, rect.getStyle().getCssText().replace("\n", ""));
		assertEquals(styleDecl, rect.getAttribute("style").replace("\n", ""));

		cs = view.getComputedStyle(rect, null);
		CSSStyleValue modval = cs.getCSSStyleValue(ptyName);

		assertNotNull(modval);
		assertEquals(newValue, modval.toString());

		return modval;
	}

	private static SVGDocument createDocumentWithStyle(String rectStyle) {
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
		BridgeContext ctx = new BridgeContext(new UserAgentAdapter() {

			@Override
			public Dimension2D getViewportSize() {
				return new Dimension(200, 200);
			}

		});
		ctx.setDynamic(true);

		return ctx;
	}

	private static GraphicsNode createGraphicsNode(BridgeContext ctx, Document document) {
		return new GVTBuilder().build(ctx, document);
	}

}
