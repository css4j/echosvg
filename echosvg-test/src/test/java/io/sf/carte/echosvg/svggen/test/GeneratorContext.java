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
package io.sf.carte.echosvg.svggen.test;

import java.awt.Font;
import java.net.URL;
import java.util.Map;

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.svggen.CachedImageHandlerBase64Encoder;
import io.sf.carte.echosvg.svggen.DefaultStyleHandler;
import io.sf.carte.echosvg.svggen.GenericImageHandler;
import io.sf.carte.echosvg.svggen.SVGGeneratorContext;
import io.sf.carte.echosvg.svggen.SVGGraphics2D;
import io.sf.carte.echosvg.svggen.SVGIDGenerator;
import io.sf.carte.echosvg.svggen.SVGGeneratorContext.GraphicContextDefaults;
import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Testing customization of the SVGGeneratorContext and generation of SVG Fonts.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GeneratorContext extends SVGAccuracyTest {

	public static class TestIDGenerator extends SVGIDGenerator {

		@Override
		public String generateID(String prefix) {
			return "test" + super.generateID(prefix);
		}

	}

	public static class TestStyleHandler extends DefaultStyleHandler {

		private CDATASection styleSheet;

		public TestStyleHandler(CDATASection styleSheet) {
			this.styleSheet = styleSheet;
		}

		@Override
		public void setStyle(Element element, Map<String, String> styleMap, SVGGeneratorContext generatorContext) {
			// create a new class id in the style sheet
			String id = generatorContext.getIDGenerator().generateID("C");
			styleSheet.appendData("." + id + " {");
			// append each key/value pairs
			for (String key : styleMap.keySet()) {
				String value = styleMap.get(key);
				styleSheet.appendData(key + ":" + value + ";");
			}
			styleSheet.appendData("}\n");
			// reference the class id of the style sheet on the element to be styled
			element.setAttribute("class", id);
		}

	}

	private Element topLevelGroup = null;

	public GeneratorContext(Painter painter, URL refURL) {
		super(painter, refURL);
	}

	@Override
	protected SVGGraphics2D buildSVGGraphics2D() {
		// Use EchoSVG's DOM implementation to create a Document
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String namespaceURI = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document domFactory = impl.createDocument(namespaceURI, SVGConstants.SVG_SVG_TAG, null);

		// Create a default context from our Document instance
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(domFactory);

		// Set ID generator
		ctx.setIDGenerator(new TestIDGenerator());

		// Extension Handler to be done

		// Image Handler to be done
		GenericImageHandler ihandler = new CachedImageHandlerBase64Encoder();
		ctx.setGenericImageHandler(ihandler);

		// Set Style handler
		CDATASection styleSheet = domFactory.createCDATASection("");
		ctx.setStyleHandler(new TestStyleHandler(styleSheet));

		// Set the generator comment
		ctx.setComment("Generated by the EchoSVG Test Framework. Test:\u00e9j");

		// Turn SVG Font embedding on.
		ctx.setEmbeddedFontsOn(true);

		// Set the default font to use
		GraphicContextDefaults defaults = new GraphicContextDefaults();
		defaults.setFont(new Font(TestFonts.FONT_FAMILY_SANS1, Font.PLAIN, 12));
		ctx.setGraphicContextDefaults(defaults);

		//
		// Build SVGGraphics2D with our customized context
		//
		SVGGraphics2D g2d = new SVGGraphics2D(ctx, false);

		// Append our stylesheet to the top level group.
		topLevelGroup = g2d.getTopLevelGroup();
		Element style = domFactory.createElementNS(SVGConstants.SVG_NAMESPACE_URI,
				SVGConstants.SVG_STYLE_TAG);
		style.setAttributeNS(null, SVGConstants.SVG_TYPE_ATTRIBUTE, "text/css");
		style.appendChild(styleSheet);
		topLevelGroup.appendChild(style);

		return g2d;
	}

	@Override
	protected void configureSVGGraphics2D(SVGGraphics2D g2d) {
		topLevelGroup.appendChild(g2d.getTopLevelGroup());
		g2d.setTopLevelGroup(topLevelGroup);
	}

}