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

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.ScriptUtil;
import io.sf.carte.echosvg.test.svg.SVGOnLoadExceptionTest;

/**
 * Checks the permissions applied to ECMA Scripts.
 *
 * @version $Id$
 */
@SuppressWarnings({ "deprecation", "removal" })
public class LoadPermissionsTest {

	@BeforeAll
	public static void setUpBeforeAll() {
		ScriptUtil.defaultRhinoShutter();
	}

	@AfterEach
	public void tearDown() throws Exception {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {

				@Override
				public Void run() throws Exception {
					try {
						System.setSecurityManager(null);
						System.setProperty("java.security.policy", "");
					} catch (UnsupportedOperationException e) {
					}
					return null;
				}

			});
		} catch (PrivilegedActionException pae) {
			throw pae.getException();
		}
	}

	@Test
	public void testNoEmbed() throws Exception {
		boolean secure = false;
		runTest("application/java-archive", "bridge/test/ecmaCheckNoEmbed", "NONE", secure, false,
			"java.lang.SecurityException");
	}

	@Test
	@Tag("SecMan")
	public void testPermissionsDeniedScript() throws Exception {
		runTest("application/java-archive", "bridge/test/ecmaCheckPermissionsDeniedScript", "NONE",
			true, false, "java.lang.SecurityException");
		runTest("application/java-archive", "bridge/test/ecmaCheckPermissionsDeniedScript", "NONE",
			false, false, "java.lang.SecurityException");
	}

	@Test
	public void testLoadEmbed() throws Exception {
		runTest("application/java-archive", "bridge/test/jarCheckLoadEmbed", "ANY", false, false,
			"java.net.MalformedURLException");
	}

	@Test
	public void testExceptionNegativeWidth() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-width", "ANY", false,
			false, "io.sf.carte.echosvg.dom.svg.LiveAttributeException");
	}

	@Test
	public void testExceptionInvalidClipPathUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/clipPath-clipPathUnits-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidClipPathSubelement() throws Exception {
		runTest("application/java-archive", "bridge/test/error/clipPath-subelement-invalid", "ANY",
			false, false, "org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionIllegalClipPathUri() throws Exception {
		runTest("application/java-archive", "bridge/test/error/clipPath-uri-illegal", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	/**
	 * The rendering can proceed despite the CSS error, but the exception is
	 * reported to the user agent and is detected by this test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExceptionInvalidCSS() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/css-invalid", "ANY", false, false,
			"org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionEllipseWrongUnitRx() throws Exception {
		runTest("text/javascript", "bridge/test/error/ellipse-wrong-unit-rx", "ANY", false, false,
			"org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionEllipseWrongUnitRy() throws Exception {
		runTest("text/javascript", "bridge/test/error/ellipse-wrong-unit-ry", "ANY", false, false,
			"org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionEllipseNegativeRx() throws Exception {
		runTest("application/java-archive", "bridge/test/error/ellipse-negative-rx", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionEllipseNegativeRy() throws Exception {
		runTest("application/java-archive", "bridge/test/error/ellipse-negative-ry", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidColorMatrixType() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feColorMatrix-type-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidColorMatrixValue() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feColorMatrix-value-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidColorMatrixValues() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feColorMatrix-values-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidComponentTransfertType() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComponentTransfert-type-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	// Valid in SVG2
	@Test
	public void testMissingComponentTransfertType() throws Exception {
		runTest("text/ecmascript", "bridge/test/feComponentTransfert-type-missing",
			"ANY", false, false, null);
	}

	@Test
	public void testExceptionInvalidComponentTransfertValue() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComponentTransfert-value-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionMissingCompositeIn2() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComposite-in2-missing", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidCompositeOperator() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComposite-operator-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidDMChannelSelector() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/feDisplacementMap-channelSelector-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionMissingDMIn2() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feDisplacementMap-in2-missing",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidGBStdDevX() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/feGaussianBlur-stdDeviationX-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidGBStdDevY() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/feGaussianBlur-stdDeviationY-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionFeImageBadURL() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/feImage-badurl", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidMergeNode() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/feMerge-feMergeNode-invalid", "ANY", false,
			false, null);
	}

	@Test
	public void testExceptionInvalidMorphologyOperator() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feMorphology-operator-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionMorphologyNegativeRadiusX() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feMorphology-radiusX-negative",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionMorphologyNegativeRadiusY() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feMorphology-radiusY-negative",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidTurbulenceStitchTiles() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feTurbulence-stitchTiles.invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidTurbulenceType() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feTurbulence-type-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionEmptyFilter() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/filter-empty", "ANY", false, false, null);
	}

	@Test
	public void testExceptionInvalidFilterPrimitive() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/filter-filterPrimitive-invalid", "ANY", false,
			false, null);
	}

	@Test
	public void testExceptionFilterNegativeResX() throws Exception {
		runTest("application/java-archive", "bridge/test/error/filter-filterResX-negative", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionFilterNegativeResY() throws Exception {
		runTest("application/java-archive", "bridge/test/error/filter-filterResY-negative", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidFilterUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/filter-filterUnits-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionIllegalFilterUri() throws Exception {
		runTest("application/java-archive", "bridge/test/error/filter-uri-illegal", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	/**
	 * There is a method intended to supply broken-link images but the user agent
	 * used here just throws an exception. For an actual broken link image test, see
	 * {@link io.sf.carte.echosvg.test.svg.SamplesRenderingTest#testImageBadUrl
	 * SamplesRenderingTest.testImageBadUrl}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExceptionImageBadURL() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/image-badurl", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testImageMissingHeight() throws Exception {
		runTest("text/ecmascript", "bridge/test/image-missing-height", "ANY", false,
			false, null);
	}

	@Test
	public void testImageMissingWidth() throws Exception {
		runTest("text/ecmascript", "bridge/test/image-missing-width", "ANY", false,
			false, null);
	}

	@Test
	public void testExceptionImageNegativeHeight() throws Exception {
		runTest("application/java-archive", "bridge/test/error/image-negative-height", "ANY", false,
			false, "io.sf.carte.echosvg.dom.svg.LiveAttributeException");
	}

	@Test
	public void testExceptionImageNegativeWidth() throws Exception {
		runTest("application/java-archive", "bridge/test/error/image-negative-width", "ANY", false,
			false, "io.sf.carte.echosvg.dom.svg.LiveAttributeException");
	}

	@Test
	public void testExceptionEmptyLinearGradient() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/linearGradient-empty", "ANY", false, false,
			null);
	}

	@Test
	public void testExceptionInvalidGradientUnits() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/linearGradient-gradientUnits-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionMissingGradientOffset() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/linearGradient-missing-offset", "ANY", false,
			false, null);
	}

	@Test
	public void testExceptionInvalidGradientSpreadMethod() throws Exception {
		runTest("application/java-archive", "bridge/test/error/linearGradient-spreadMethod-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidGradientUri() throws Exception {
		runTest("application/java-archive", "bridge/test/error/linearGradient-uri-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionEmptyMask() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/mask-empty", "ANY", false, false, null);
	}

	@Test
	public void testExceptionInvalidMaskUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/mask-maskUnits-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidMaskSubelement() throws Exception {
		runTest("application/java-archive", "bridge/test/error/mask-subelement-invalid", "ANY",
			false, false, "org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionIllegalMaskUri() throws Exception {
		runTest("application/java-archive", "bridge/test/error/mask-uri-illegal", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidPathD() throws Exception {
		runTest("application/java-archive", "bridge/test/error/path-invalid-d", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionEmptyPattern() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/pattern-empty", "ANY", false, false, null);
	}

	@Test
	public void testExceptionInvalidPatternUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/pattern-patternUnits-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidPatternSubelement() throws Exception {
		runTest("application/java-archive", "bridge/test/error/pattern-subelement-invalid", "ANY",
			false, false, "org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionInvalidPolygonPoints() throws Exception {
		runTest("application/java-archive", "bridge/test/error/polygon-invalid-points", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidPolylinePoints() throws Exception {
		runTest("application/java-archive", "bridge/test/error/polyline-invalid-points", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	// This is not an error according to 13.2.4
	@Test
	public void testExceptionEmptyRadialGradient() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/radialGradient-empty", "ANY", false, false,
			null);
	}

	@Test
	public void testExceptionInvalidRadialGradientUnits() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/radialGradient-gradientUnits-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionMissingRadialGradientOffset() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/radialGradient-missing-offset", "ANY", false,
			false, null);
	}

	@Test
	public void testExceptionRadialGradientRNegative() throws Exception {
		runTest("application/java-archive", "bridge/test/error/radialGradient-r-negative", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	// This is not an error according to 13.2.3
	@Test
	public void testExceptionRadialGradientRZero() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/radialGradient-r-zero", "ANY", false, false,
			null);
	}

	@Test
	public void testExceptionInvalidRadialGradientSpreadMethod() throws Exception {
		runTest("application/java-archive", "bridge/test/error/radialGradient-spreadMethod-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidRectHeight() throws Exception {
		runTest("text/javascript", "bridge/test/error/rect-invalid-height", "ANY", false, false,
			"org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionInvalidRectWidth() throws Exception {
		runTest("text/javascript", "bridge/test/error/rect-invalid-width", "ANY", false, false,
			"org.w3c.dom.DOMException");
	}

	@Test
	public void testExceptionNegativeRectHeight() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-height", "ANY", false,
			false, "io.sf.carte.echosvg.dom.svg.LiveAttributeException");
	}

	@Test
	public void testExceptionNegativeRectRx() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-rx", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionNegativeRectRy() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-ry", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionNegativeRectWidth() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-width", "ANY", false,
			false, "io.sf.carte.echosvg.dom.svg.LiveAttributeException");
	}

	@Test
	public void testExceptionMalformedTransform() throws Exception {
		runTest("application/java-archive", "bridge/test/error/transform", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testExceptionInvalidXML() throws Exception {
		runTest("application/java-archive", "bridge/test/error/xml-invalid", "ANY", false, false,
			"java.io.IOException");
	}

	private void runTest(String scripts, String scriptId, String origin, boolean secure,
		boolean restricted, String expectedException) throws Exception {
		SVGOnLoadExceptionTest t = new SVGOnLoadExceptionTest();
		String desc = "(scripts=" + scripts + ")(scriptOrigin=" + origin + ")(secure=" + secure
			+ ")(restricted=" + restricted + ")";

		t.setId(scriptId);
		t.setDescription(scriptId + ' ' + desc);
		t.setScriptOrigin(origin);
		t.setSecure(secure);
		t.setScripts(scripts);
		t.setExpectedExceptionClass(expectedException);
		t.setRestricted(restricted);

		t.runTest();
	}

}
