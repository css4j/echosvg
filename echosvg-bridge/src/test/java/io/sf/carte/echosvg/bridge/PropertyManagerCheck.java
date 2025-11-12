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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.view.ViewCSS;
import org.w3c.dom.views.DocumentView;

import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.css.engine.value.CSSVal;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Generic property manager checker.
 */
public class PropertyManagerCheck extends AbstractManagerCheck {

	static String documentURI;

	static String propertyName;

	protected String svgStyle = "";

	@BeforeAll
	public static void setupBeforeAll() {
		documentURI = Paths.get(".").toAbsolutePath().normalize().toUri().toString();
	}

	protected void checkComputedValue(String value) {
		checkComputedValue(value, value);
	}

	protected void checkComputedValue(String declValue, String computedValue) {
		Element elm = createDocument(declValue);
		assertNotNull(elm);

		Document doc = elm.getOwnerDocument();
		GraphicsNode gn = createGraphicsNode(doc);
		assertNotNull(gn);

		ViewCSS view = (ViewCSS) ((DocumentView) doc).getDefaultView();
		CSSStyleDeclaration cs = view.getComputedStyle(elm, null);
		CSSVal val = (CSSVal) cs.getCSSStyleValue(propertyName);

		assertNotNull(val);

		CssType cssType = val.getCssValueType();
		assertTrue(cssType == CssType.TYPED || cssType == CssType.LIST);
		assertEquals(computedValue, val.getCssText());
	}

	protected void checkComputedValueAttr(String declValue, String computedValue, String attrName,
			String attrValue) {
		Element elm = createDocument(declValue);
		assertNotNull(elm);
		elm.setAttribute(attrName, attrValue);

		Document doc = elm.getOwnerDocument();
		GraphicsNode gn = createGraphicsNode(doc);
		assertNotNull(gn);

		ViewCSS view = (ViewCSS) ((DocumentView) doc).getDefaultView();
		CSSStyleDeclaration cs = view.getComputedStyle(elm, null);
		CSSVal val = (CSSVal) cs.getCSSStyleValue(propertyName);

		assertNotNull(val);

		CssType cssType = val.getCssValueType();
		assertTrue(cssType == CssType.TYPED || cssType == CssType.LIST);
		assertEquals(computedValue, val.getCssText());
	}

	protected Element createDocument(String value) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		DocumentType dtd = impl.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		SVGDocument doc = (SVGDocument) impl.createDocument(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_SVG_TAG, dtd);

		doc.setDocumentURI(documentURI);

		SVGSVGElement svg = doc.getRootElement();
		svg.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "200");
		svg.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "200");

		String cssText = svgStyle + "circle{" + propertyName + ':' + value + '}';
		CDATASection text = doc.createCDATASection(cssText);
		Element style = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_STYLE_TAG);
		style.appendChild(text);
		svg.appendChild(style);

		Element circle = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_CIRCLE_TAG);
		circle.setAttribute(SVGConstants.SVG_CX_ATTRIBUTE, "40");
		circle.setAttribute(SVGConstants.SVG_CY_ATTRIBUTE, "50");
		svg.appendChild(circle);

		context.setDocument(doc);
		context.initializeDocument(doc);

		return circle;
	}

	protected GraphicsNode createGraphicsNode(Document document) {
		return new GVTBuilder().build(context, document);
	}

}
