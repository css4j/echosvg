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
package io.sf.carte.echosvg.anim.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPathSeg;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicAbs;
import org.w3c.dom.svg.SVGPathSegLinetoAbs;
import org.w3c.dom.svg.SVGPathSegList;
import org.w3c.dom.svg.SVGPathSegMovetoAbs;

import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.dom.util.SAXDocumentFactory;

/**
 * To test the NormalizedPathSegList.
 *
 * @author github.com/carlosame
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NormalizedPathSegListTest {

	/*
	 * @formatter:off
	 */
	String pathRef1 = "M 118.5 102.5\n"
			+ "C 118.5 80.17 136.67 62 159 62\n"
			+ "L 303 62\n"
			+ "C 325.33 62 343.5 80.17 343.5 102.5\n"
			+ "L 343.5 150.5\n"
			+ "C 343.5 158.74 327.33 167.05 313.48 171.67\n"
			+ "C 291.56 178.98 262.27 183 231 183\n"
			+ "C 199.73 183 170.44 178.98 148.52 171.67\n"
			+ "C 134.67 167.05 118.5 158.74 118.5 150.5\n"
			+ "z\n"
			+ "M 118.5 182.5\n"
			+ "L 118.5 173.98\n"
			+ "C 124.47 178.12 132.65 182.19 143.77 185.9\n"
			+ "C 167.18 193.7 198.16 198 231 198\n"
			+ "C 263.84 198 294.82 193.7 318.23 185.9\n"
			+ "C 329.35 182.19 337.53 178.12 343.5 173.98\n"
			+ "L 343.5 182.5\n"
			+ "C 343.5 204.83 325.33 223 303 223\n"
			+ "L 159 223\n"
			+ "C 136.67 223 118.5 204.83 118.5 182.5\n"
			+ "z\n";
	/*
	 * @formatter:on
	 */

	private static final NumberFormat fmt;

	static {
		fmt = NumberFormat.getInstance(Locale.ROOT);
		fmt.setMaximumFractionDigits(2);
		fmt.setMinimumFractionDigits(0);
	}

	@Test
	public void testNormalizedPath() throws IOException {
		loadAndCompare("io/sf/carte/echosvg/anim/dom/normalizedPath.svg", pathRef1);
	}

	private static void loadAndCompare(String docPath, String pathRef) throws IOException {
		SVGDocument doc = loadDocument(docPath);
		assertNotNull(doc);

		Element path1 = doc.getElementById("path1");
		assertNotNull(path1);
		assertTrue(path1 instanceof SVGOMPathElement);
		SVGOMPathElement pathElm = (SVGOMPathElement) path1;

		SVGPathSegList nSegList = pathElm.getNormalizedPathSegList();
		assertNotNull(nSegList);

		comparePathToRef(pathRef, nSegList);
	}

	private static SVGDocument loadDocument(String testFileName) throws IOException {
		DocumentFactory df = new SAXDocumentFactory(SVGDOMImplementation.getDOMImplementation(), null);

		URL url = NormalizedPathSegListTest.class.getClassLoader().getResource(testFileName);
		Document doc = df.createDocument("http://www.w3.org/2000/svg", "svg", url.toString(), url.openStream());

		return (SVGDocument) doc;
	}

	private static void comparePathToRef(String pathRef, SVGPathSegList nSegList) {
		StringBuilder buf = new StringBuilder(64);
		int num = nSegList.getNumberOfItems();

		for (int i = 0; i < num; i++) {
			SVGPathSeg pathSeg = nSegList.getItem(i);
			serializePathSeg(pathSeg, buf);
		}
		assertEquals(pathRef, buf.toString());
	}

	private static void serializePathSeg(SVGPathSeg pathSeg, StringBuilder buf) {
		buf.append(pathSeg.getPathSegTypeAsLetter());
		switch (pathSeg.getPathSegType()) {
		case SVGPathSeg.PATHSEG_MOVETO_ABS:
			buf.append(' ');
			SVGPathSegMovetoAbs m = (SVGPathSegMovetoAbs) pathSeg;
			buf.append(fmt.format(m.getX()));
			buf.append(' ');
			buf.append(fmt.format(m.getY()));
			break;
		case SVGPathSeg.PATHSEG_LINETO_ABS:
			buf.append(' ');
			SVGPathSegLinetoAbs l = (SVGPathSegLinetoAbs) pathSeg;
			buf.append(fmt.format(l.getX()));
			buf.append(' ');
			buf.append(fmt.format(l.getY()));
			break;
		case SVGPathSeg.PATHSEG_CURVETO_CUBIC_ABS:
			buf.append(' ');
			SVGPathSegCurvetoCubicAbs c = (SVGPathSegCurvetoCubicAbs) pathSeg;
			buf.append(fmt.format(c.getX1()));
			buf.append(' ');
			buf.append(fmt.format(c.getY1()));
			buf.append(' ');
			buf.append(fmt.format(c.getX2()));
			buf.append(' ');
			buf.append(fmt.format(c.getY2()));
			buf.append(' ');
			buf.append(fmt.format(c.getX()));
			buf.append(' ');
			buf.append(fmt.format(c.getY()));
			break;
		case SVGPathSeg.PATHSEG_CLOSEPATH:
			break;
		}
		buf.append('\n');
	}

}
