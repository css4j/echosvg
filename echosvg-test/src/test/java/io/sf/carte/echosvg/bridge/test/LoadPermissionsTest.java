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
	public void testBridgeExceptionNegativeWidth() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-width", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidClipPathUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/clipPath-clipPathUnits-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidClipPathSubelement() throws Exception {
		runTest("application/java-archive", "bridge/test/error/clipPath-subelement-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionIllegalClipPathUri() throws Exception {
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
	public void testBridgeExceptionInvalidCSS() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/css-invalid", "ANY", false, false,
			"org.w3c.dom.DOMException");
	}

	@Test
	public void testBridgeExceptionEllipseWrongUnitRx() throws Exception {
		runTest("text/javascript", "bridge/test/error/ellipse-wrong-unit-rx", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionEllipseWrongUnitRy() throws Exception {
		runTest("text/javascript", "bridge/test/error/ellipse-wrong-unit-ry", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionEllipseNegativeRx() throws Exception {
		runTest("application/java-archive", "bridge/test/error/ellipse-negative-rx", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionEllipseNegativeRy() throws Exception {
		runTest("application/java-archive", "bridge/test/error/ellipse-negative-ry", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidColorMatrixType() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feColorMatrix-type-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidColorMatrixValue() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feColorMatrix-value-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidColorMatrixValues() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feColorMatrix-values-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidComponentTransfertType() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComponentTransfert-type-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMissingComponentTransfertType() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComponentTransfert-type-missing",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidComponentTransfertValue() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComponentTransfert-value-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMissingCompositeIn2() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComposite-in2-missing", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidCompositeOperator() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feComposite-operator-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidDMChannelSelector() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/feDisplacementMap-channelSelector-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMissingDMIn2() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feDisplacementMap-in2-missing",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidGBStdDevX() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/feGaussianBlur-stdDeviationX-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidGBStdDevY() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/feGaussianBlur-stdDeviationY-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionFeImageBadURL() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/feImage-badurl", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidMergeNode() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/feMerge-feMergeNode-invalid", "ANY", false,
			false, null);
	}

	@Test
	public void testBridgeExceptionInvalidMorphologyOperator() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feMorphology-operator-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMorphologyNegativeRadiusX() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feMorphology-radiusX-negative",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMorphologyNegativeRadiusY() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feMorphology-radiusY-negative",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidTurbulenceStitchTiles() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feTurbulence-stitchTiles.invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidTurbulenceType() throws Exception {
		runTest("application/java-archive", "bridge/test/error/feTurbulence-type-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionEmptyFilter() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/filter-empty", "ANY", false, false, null);
	}

	@Test
	public void testBridgeExceptionInvalidFilterPrimitive() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/filter-filterPrimitive-invalid", "ANY", false,
			false, null);
	}

	@Test
	public void testBridgeExceptionFilterNegativeResX() throws Exception {
		runTest("application/java-archive", "bridge/test/error/filter-filterResX-negative", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionFilterNegativeResY() throws Exception {
		runTest("application/java-archive", "bridge/test/error/filter-filterResY-negative", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidFilterUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/filter-filterUnits-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionIllegalFilterUri() throws Exception {
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
	public void testBridgeExceptionImageBadURL() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/image-badurl", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionImageMissingHeight() throws Exception {
		runTest("application/java-archive", "bridge/test/error/image-missing-height", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionImageMissingWidth() throws Exception {
		runTest("application/java-archive", "bridge/test/error/image-missing-width", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionImageNegativeHeight() throws Exception {
		runTest("application/java-archive", "bridge/test/error/image-negative-height", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionImageNegativeWidth() throws Exception {
		runTest("application/java-archive", "bridge/test/error/image-negative-width", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionEmptyLinearGradient() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/linearGradient-empty", "ANY", false, false,
			null);
	}

	@Test
	public void testBridgeExceptionInvalidGradientUnits() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/linearGradient-gradientUnits-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMissingGradientOffset() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/linearGradient-missing-offset", "ANY", false,
			false, null);
	}

	@Test
	public void testBridgeExceptionInvalidGradientSpreadMethod() throws Exception {
		runTest("application/java-archive", "bridge/test/error/linearGradient-spreadMethod-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidGradientUri() throws Exception {
		runTest("application/java-archive", "bridge/test/error/linearGradient-uri-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionEmptyMask() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/mask-empty", "ANY", false, false, null);
	}

	@Test
	public void testBridgeExceptionInvalidMaskUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/mask-maskUnits-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidMaskSubelement() throws Exception {
		runTest("application/java-archive", "bridge/test/error/mask-subelement-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionIllegalMaskUri() throws Exception {
		runTest("application/java-archive", "bridge/test/error/mask-uri-illegal", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidPathD() throws Exception {
		runTest("application/java-archive", "bridge/test/error/path-invalid-d", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionEmptyPattern() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/pattern-empty", "ANY", false, false, null);
	}

	@Test
	public void testBridgeExceptionInvalidPatternUnits() throws Exception {
		runTest("application/java-archive", "bridge/test/error/pattern-patternUnits-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidPatternSubelement() throws Exception {
		runTest("application/java-archive", "bridge/test/error/pattern-subelement-invalid", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidPolygonPoints() throws Exception {
		runTest("application/java-archive", "bridge/test/error/polygon-invalid-points", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidPolylinePoints() throws Exception {
		runTest("application/java-archive", "bridge/test/error/polyline-invalid-points", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	// This is not an error according to 13.2.4
	@Test
	public void testBridgeExceptionEmptyRadialGradient() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/radialGradient-empty", "ANY", false, false,
			null);
	}

	@Test
	public void testBridgeExceptionInvalidRadialGradientUnits() throws Exception {
		runTest("application/java-archive",
			"bridge/test/error/radialGradient-gradientUnits-invalid", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMissingRadialGradientOffset() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/radialGradient-missing-offset", "ANY", false,
			false, null);
	}

	@Test
	public void testBridgeExceptionRadialGradientRNegative() throws Exception {
		runTest("application/java-archive", "bridge/test/error/radialGradient-r-negative", "ANY",
			false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	// This is not an error according to 13.2.3
	@Test
	public void testBridgeExceptionRadialGradientRZero() throws Exception {
		runTest("text/ecmascript", "bridge/test/error/radialGradient-r-zero", "ANY", false, false,
			null);
	}

	@Test
	public void testBridgeExceptionInvalidRadialGradientSpreadMethod() throws Exception {
		runTest("application/java-archive", "bridge/test/error/radialGradient-spreadMethod-invalid",
			"ANY", false, false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidRectHeight() throws Exception {
		runTest("text/javascript", "bridge/test/error/rect-invalid-height", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidRectWidth() throws Exception {
		runTest("text/javascript", "bridge/test/error/rect-invalid-width", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionNegativeRectHeight() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-height", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionNegativeRectRx() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-rx", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionNegativeRectRy() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-ry", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionNegativeRectWidth() throws Exception {
		runTest("application/java-archive", "bridge/test/error/rect-negative-width", "ANY", false,
			false, "io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionMalformedTransform() throws Exception {
		runTest("application/java-archive", "bridge/test/error/transform", "ANY", false, false,
			"io.sf.carte.echosvg.bridge.BridgeException");
	}

	@Test
	public void testBridgeExceptionInvalidXML() throws Exception {
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
