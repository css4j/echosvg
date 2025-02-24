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
package io.sf.carte.echosvg.test.svg;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.ScriptUtil;
import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files under the
 * {@code samples/spec/scripting} directory, and compares the result with a
 * reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * If you want to test a non-conformant SVG document, run a non-validating test
 * with {@link #testNV(String)}.
 * </p>
 */
public class SamplesScriptingTest extends AbstractSamplesRendering {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
		ScriptUtil.defaultRhinoShutter();
	}

	/*
	 * Scripting
	 */
	@Test
	public void testScriptAdd() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/add.svg");
	}

	@Test
	public void testScriptBbox() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/bbox.svg");
	}

	@Test
	public void testScriptCircle() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/circle.svg");
	}

	@Test
	public void testScriptDisplay() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/display.svg");
	}

	@Test
	public void testScriptDomSVGColor() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/domSVGColor.svg");
	}

	@Test
	public void testScriptEllipse() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/ellipse.svg");
	}

	@Test
	public void testScriptEnclosureList() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/enclosureList.svg");
	}

	// See issue #30
	@Disabled
	@Test
	public void testScriptEnclosureList2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/enclosureList2.svg");
	}

	@Test
	public void testScriptFill() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/fill.svg");
	}

	@Test
	public void testScriptGetElementById() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/getElementById.svg");
	}

	@Test
	public void testScriptImage() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/image.svg");
	}

	@Test
	public void testScriptImageRefUpdate() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/imageRefUpdate.svg");
	}

	@Test
	public void testScriptImageraster() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/imageraster.svg");
	}

	@Test
	public void testScriptImagesvg() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/imagesvg.svg");
	}

	@Test
	public void testScriptIntersectionList() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/intersectionList.svg");
	}

	@Test
	public void testScriptIntersectionList2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/intersectionList2.svg");
	}

	@Test
	public void testScriptLine() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/line.svg");
	}

	@Test
	public void testScriptNestedsvg() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/nestedsvg.svg");
	}

	@Test
	public void testScriptNormalizedPathTest() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/normalizedPathTest.svg");
	}

	@Test
	public void testScriptNormalizedPathTest2() throws TranscoderException, IOException {
		testNV("samples/tests/spec/scripting/normalizedPathTest2.svg");
	}

	@Test
	public void testScriptPaintType() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/scripting/paintType.svg", null, 2);
	}

	@Test
	public void testScriptPath() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/path.svg");
	}

	@Test
	public void testScriptPathLength() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/pathLength.svg");
	}

	@Test
	public void testScriptPath_pathSegList_create() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/path_pathSegList_create.svg");
	}

	@Test
	public void testScriptPath_pathSegList1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/path_pathSegList1.svg");
	}

	@Test
	public void testScriptPath_pathSegList2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/path_pathSegList2.svg");
	}

	@Test
	public void testScriptPolygon() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/polygon.svg");
	}

	@Test
	public void testScriptPolygon_points1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/polygon_points1.svg");
	}

	@Test
	public void testScriptPolygon_points2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/polygon_points2.svg");
	}

	@Test
	public void testScriptPolyline() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/polyline.svg");
	}

	@Test
	public void testScriptPolyline_points1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/polyline_points1.svg");
	}

	@Test
	public void testScriptPolyline_points2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/polyline_points2.svg");
	}

	@Test
	public void testScriptRect() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/rect.svg");
	}

	@Test
	public void testScriptRelativeURI() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/relativeURI.svg");
	}

	@Test
	public void testScriptRemove() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/remove.svg");
	}

	@Test
	public void testScriptRemoveAttrECR() throws TranscoderException, IOException {
		testErrIgnore("samples/tests/spec/scripting/removeAttrECR.svg", BROWSER_MEDIA, true, 3);
	}

	@Test
	public void testScriptRemoveAttrPathPoly() throws TranscoderException, IOException {
		testErrIgnore("samples/tests/spec/scripting/removeAttrPathPoly.svg", BROWSER_MEDIA, true, 6);
	}

	@Test
	public void testScriptRemoveOnclick() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/removeOnclick.svg");
	}

	@Test
	public void testScriptSetAttributeECR() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/scripting/setAttributeECR.svg", BROWSER_MEDIA, 3);
	}

	@Test
	public void testScriptSetAttributePathPoly() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/scripting/setAttributePathPoly.svg", BROWSER_MEDIA, 6);
	}

	@Test
	public void testScriptText() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text.svg");
	}

	@Test
	public void testScriptTextAllProperties() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/textAllProperties.svg");
	}

	@Test
	public void testScriptTextProperties() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/textProperties.svg");
	}

	@Test
	public void testScriptText_children1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_children1.svg");
	}

	@Test
	public void testScriptText_children2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_children2.svg");
	}

	@Test
	public void testScriptText_children3() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_children3.svg");
	}

	@Test
	public void testScriptText_dxlist1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_dxlist1.svg");
	}

	@Test
	public void testScriptText_dxlist2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_dxlist2.svg");
	}

	@Test
	public void testScriptText_dylist1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_dylist1.svg");
	}

	@Test
	public void testScriptText_dylist2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_dylist2.svg");
	}

	@Test
	public void testScriptText_xlist1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_xlist1.svg");
	}

	@Test
	public void testScriptText_xlist2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_xlist2.svg");
	}

	@Test
	public void testScriptText_ylist1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_ylist1.svg");
	}

	@Test
	public void testScriptText_ylist2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/text_ylist2.svg");
	}

	@Test
	public void testScriptTextpathProperties() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/textpathProperties.svg");
	}

	@Test
	public void testScriptTransform() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/transform.svg");
	}

	@Test
	public void testScriptTransform2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/transform2.svg");
	}

	@Test
	public void testScriptTransform_create() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/transform_create.svg");
	}

	@Test
	public void testScriptTransform_getTransform1() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/transform_getTransform1.svg");
	}

	@Test
	public void testScriptTransform_getTransform2() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/transform_getTransform2.svg");
	}

	@Test
	public void testScriptTrefProperties() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/trefProperties.svg");
	}

	@Test
	public void testScriptTspan() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/tspan.svg");
	}

	@Test
	public void testScriptTspanProperties() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/tspanProperties.svg");
	}

	@Test
	public void testScriptVisibility() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/visibility.svg");
	}

	@Test
	public void testScriptViewBoxOnLoad() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/viewBoxOnLoad.svg");
	}

	@Test
	public void testScriptXyModifOnLoad() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/xyModifOnLoad.svg");
	}

	@Test
	public void testScriptZeroSize() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/zeroSize.svg");
	}

	/*
	 * Dynamic Update Tests
	 */
	@Test
	public void testBoundsTransformChange() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/boundsTransformChange.svg");
	}

	@Test
	public void testEventAttrAdd() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/eventAttrAdd.svg");
	}

	@Test
	public void testMarkerUpdate() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/markerUpdate.svg");
	}

	@Test
	public void testRootSizeChange() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/rootSizeChange.svg");
	}

	@Test
	public void testRectResizeOnClick() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/rectResizeOnClick.svg");
	}

	@Test
	public void testSetProperty() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/setProperty.svg");
	}

	/*
	 * This one should be with the interactive tests.
	 */
	@Disabled
	@Test
	public void testStyling() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/styling.svg");
	}

	@Test
	public void testSvgFontMove() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/svgFontMove.svg");
	}

	@Test
	public void testText_content() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/text_content.svg");
	}

	@Test
	public void testScriptTextProperties2() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/textProperties2.svg");
	}

	@Test
	public void testVisibilityOnClick() throws TranscoderException, IOException {
		testDynamicUpdate("samples/tests/spec/scripting/visibilityOnClick.svg");
	}

}
