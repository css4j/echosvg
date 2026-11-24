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

import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.gvt.GraphicsNode;

public class SVGUseElementBridgeTest implements ErrorConstants {

	private static final String CIRCULAR_SVG = "/circular.svg";
	private static final String REPEATED_USE_SVG = "/repeatedUse.svg";
	private static final String EXTERNAL_USE_SVG = "/externalUse.svg";

	@Test
	public void testCircularUseReferenceIsDetected() {
		assertCircularReferenceDetected(false);
	}

	@Test
	public void testCircularUseReferenceIsDetectedInDynamicContext() {
		assertCircularReferenceDetected(true);
	}

	@Test
	public void testRepeatedUseOfSameElementIsNotCircular() throws Exception {
		GraphicsNode root = build(REPEATED_USE_SVG, false);
		assertEquals(new Rectangle2D.Double(0, 0, 30, 10), root.getGeometryBounds(),
				"Both 'use' elements must render the referenced rect");
	}

	@Test
	public void testSameFragmentInExternalDocumentIsNotCircular() throws Exception {
		GraphicsNode root = build(EXTERNAL_USE_SVG, false);
		assertEquals(new Rectangle2D.Double(0, 0, 10, 10), root.getGeometryBounds(),
				"The rect from the external document must be rendered");
	}

	private static void assertCircularReferenceDetected(boolean dynamic) {
		BridgeException e = assertThrows(BridgeException.class, () -> build(CIRCULAR_SVG, dynamic),
				"Circular references must be detected to avoid possible denial of service attacks");
		assertEquals(ERR_XLINK_HREF_CIRCULAR_DEPENDENCIES, e.getCode(),
				"Must use the correct xlink reference");
	}

	private static GraphicsNode build(String resource, boolean dynamic) throws Exception {
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory();
		String uri = SVGUseElementBridgeTest.class.getResource(resource).toString();
		Document doc = f.createDocument(uri);

		BridgeContext ctx = new BridgeContext(new UserAgentAdapter() {

			@Override
			public void displayError(Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				}
				throw new RuntimeException(e);
			}

		});
		ctx.setDynamic(dynamic);
		return new GVTBuilder().build(ctx, doc);
	}

}
