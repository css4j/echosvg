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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.svggen.SVGGeneratorContext;
import io.sf.carte.echosvg.svggen.SVGGeneratorContext.GraphicContextDefaults;
import io.sf.carte.echosvg.svggen.SVGGraphics2D;
import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Test the <code>SVGGraphics2D</code> with the JDK built-in DOM implementation.
 *
 * @version $Id$
 */
public class SVGGraphics2DDOMTest {

	@Test
	public void testFontDecoration() throws IOException {
		runTest("FontDecoration");
	}

	private void runTest(String painterClassname) throws IOException {
		String clName = getClass().getPackage().getName() + '.' + painterClassname;
		Class<?> cl = null;

		try {
			cl = Class.forName(clName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(clName);
		}

		Object o = null;

		try {
			o = cl.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(clName);
		}

		if (!(o instanceof Painter)) {
			throw new IllegalArgumentException(clName);
		}

		Painter painter = (Painter) o;

		SVGAccuracyTest acctest = makeSVGAccuracyTest(painter, painterClassname);

		acctest.runTest(false);
	}

	private SVGAccuracyTest makeSVGAccuracyTest(Painter painter, String id) throws MalformedURLException {
		String cl = SVGGeneratorTest.getNonQualifiedClassName(painter);

		SVGAccuracyTest test = new SVGAccuracyTest(painter,
				SVGGeneratorTest.getReferenceURL(painter, SVGGeneratorTest.PLAIN_GENERATION_PREFIX));

		String filename = new URL(SVGGeneratorTest.GENERATOR_REFERENCE_BASE + SVGGeneratorTest.CANDIDATE_REF_DIR
				+ '/' + cl + SVGGeneratorTest.SVG_EXTENSION).getFile();

		test.setSaveSVG(new File(filename));

		return test;
	}

	static class DOMSVGGenAccuracy extends SVGAccuracyTest {

		public DOMSVGGenAccuracy(Painter painter, URL refURL) {
			super(painter, refURL);
		}

		/**
		 * Builds an <code>SVGGraphics2D</code> with a default configuration.
		 * 
		 * @throws IllegalStateException if the parser could not be configured.
		 */
		@Override
		protected SVGGraphics2D buildSVGGraphics2D() {
			// We need a Document that holds an SVG root element.
			// First obtain a DocumentBuilder as a way to get it.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);

			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw new IllegalStateException(e);
			}

			// Now the document which is what is needed
			Document doc = builder.newDocument();

			// Create a SVG DTD and the root element
			DocumentType dtd = builder.getDOMImplementation().createDocumentType("svg", SVGConstants.SVG_PUBLIC_ID,
					SVGConstants.SVG_SYSTEM_ID);
			Element svgRoot = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SVG_TAG);
			// Append those to the document
			doc.appendChild(dtd);
			doc.appendChild(svgRoot);

			// For simplicity, create a generator context with some defaults
			SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);

			// Create the context defaults, with a default font just in case
			GraphicContextDefaults defaults = new GraphicContextDefaults();
			defaults.setFont(new Font(TestFonts.FONT_FAMILY_SANS1, Font.PLAIN, 12));
			// Set the defaults
			ctx.setGraphicContextDefaults(defaults);
			// We want enough precision
			ctx.setPrecision(12);

			return new SVGGraphics2D(ctx, false);
		}

	}

}
