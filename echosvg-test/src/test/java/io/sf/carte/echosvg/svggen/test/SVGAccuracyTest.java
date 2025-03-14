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

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.svggen.SVGGeneratorContext;
import io.sf.carte.echosvg.svggen.SVGGeneratorContext.GraphicContextDefaults;
import io.sf.carte.echosvg.svggen.SVGGraphics2D;
import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.test.xml.XmlUtil;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This test validates that a given rendering sequence, modeled by a
 * <code>Painter</code> is properly converted to an SVG document by comparing
 * the generated SVG document to a known, valid SVG reference.
 *
 * <p>
 * Original author: <a href="mailto:vhardy@apache.org">Vincent Hardy</a>. For
 * later modifications, see Git history.
 * </p>
 * 
 * @version $Id$
 */
public class SVGAccuracyTest {

	public static final int FAIL_ON_ERROR = 0;

	public static final int WARN_ON_ERROR = 1;

	public static final int FAIL_IF_NO_ERROR = 2;

	/**
	 * Canvas size for all tests
	 */
	public static final Dimension CANVAS_SIZE = new Dimension(300, 400);

	/**
	 * Painter which performs an arbitrary rendering sequence.
	 */
	private Painter painter;

	/**
	 * Reference SVG URL
	 */
	private URL refURL;

	/**
	 * File where the generated SVG might be saved
	 */
	private File saveSVG;

	/**
	 * The compression level
	 */
	private Integer compressionLevel;

	/**
	 * Constructor
	 * 
	 * @param painter the <code>Painter</code> object which will perform an
	 *                arbitrary rendering sequence.
	 * @param refURL  the location of a reference SVG which should be exactly
	 *                identical to that generated by the painter.
	 */
	public SVGAccuracyTest(Painter painter, URL refURL) {
		this.painter = painter;
		this.refURL = refURL;
	}

	/**
	 * Get the compression level to use in embedded PNG images.
	 * 
	 * @return the compression level, or {@code null} if defaults shall be applied.
	 */
	public Integer getCompressionLevel() {
		return compressionLevel;
	}

	/**
	 * Sets the compression level which will be used in embedded PNG images.
	 * 
	 * @param compressionLevel the compression level (0-9), or {@code null} for
	 *                         default behavior.
	 */
	public void setCompressionLevel(Integer compressionLevel) {
		this.compressionLevel = compressionLevel;
	}

	void setSaveSVG(File saveSVG) {
		this.saveSVG = saveSVG;
	}

	/**
	 * This method will only throw exceptions if some aspect of the test's internal
	 * operation fails.
	 * 
	 * @param errorHandling {@code 0} if no error is accepted, {@code 1} if error
	 *                      may happen but only a warning should be printed (flaky
	 *                      test), {@code 2} to fail if no error is produced.
	 * @throws IOException If an I/O error occurs
	 */
	void runTest(int errorHandling) throws IOException {

		SVGGraphics2D g2d = buildSVGGraphics2D();
		g2d.setSVGCanvasSize(CANVAS_SIZE);

		//
		// Generate SVG content
		//
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
		OutputStreamWriter osw = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
		painter.paint(g2d);
		configureSVGGraphics2D(g2d);
		g2d.stream(osw);
		osw.flush();
		bos.flush();
		bos.close();

		//
		// Compare with reference SVG
		//
		byte[] data = bos.toByteArray();

		String failMessage = XmlUtil.xmlDiff(refURL, data, null, new SAXSVGDocumentFactory());

		if (failMessage != null) {
			save(bos.toByteArray());
			if (errorHandling == 0) {
				failTest("File: " + refURL.toExternalForm() + '\n' + failMessage);
			} else if (errorHandling == 1) {
				debug(failMessage);
			}
		} else if (errorHandling == 2) {
			failTest("Expected accuracy error but found no error, on file: " + refURL.toExternalForm());
		}

	}

	protected void failTest(String message) {
		fail(message);
	}

	protected void debug(String message) {
		System.err.println(message);
	}

	/**
	 * Saves the byte array in the "saveSVG" file.
	 */
	private void save(byte[] data) throws IOException {
		if (saveSVG == null) {
			return;
		}

		File parentDir = saveSVG.getParentFile();
		if (!parentDir.exists()) {
			if (!parentDir.mkdir()) {
				return;
			}
		}

		FileOutputStream os = new FileOutputStream(saveSVG);
		os.write(data);
		os.close();
	}

	/**
	 * Builds an <code>SVGGraphics2D</code> with a default configuration.
	 * 
	 * @return the <code>SVGGraphics2D</code>.
	 */
	protected SVGGraphics2D buildSVGGraphics2D() {
		DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
		String namespaceURI = SVGConstants.SVG_NAMESPACE_URI;
		Document domFactory = impl.createDocument(namespaceURI, SVGConstants.SVG_SVG_TAG, null);

		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(domFactory);

		GraphicContextDefaults defaults = new GraphicContextDefaults();
		defaults.setFont(new Font(TestFonts.FONT_FAMILY_SANS1, Font.PLAIN, 12));
		ctx.setGraphicContextDefaults(defaults);
		ctx.setPrecision(12);
		ctx.setCompressionLevel(compressionLevel);

		return new SVGGraphics2D(ctx, false);
	}

	/**
	 * Eventually configure the <code>SVGGraphics2D</code> after dumping in it and
	 * just before serializing the DOM Tree.
	 */
	protected void configureSVGGraphics2D(SVGGraphics2D g2d) {
	}

}
