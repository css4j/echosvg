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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files under the {@code samples/resvg}
 * directory, and compares the result with a reference image. See issue #140.
 * <p>
 * Images were obtained from
 * {@linkplain https://github.com/linebender/resvg-test-suite}.
 * </p>
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 */
public class SamplesResvgShapesTest extends SamplesResvgRendering {

	@Test
	public void testCircleMissingCxAndCyAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/circle/missing-cx-and-cy-attributes.svg");
	}

	@Test
	public void testCircleMissingCxAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/circle/missing-cx-attribute.svg");
	}

	@Test
	public void testCircleMissingCyAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/circle/missing-cy-attribute.svg");
	}

	@Test
	public void testCircleMissingRAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/circle/missing-r-attribute.svg");
	}

	@Test
	public void testCircleNegativeRAttribute() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/circle/negative-r-attribute.svg", 1);
	}

	@Test
	public void testCircleSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/circle/simple-case.svg");
	}

	@Test
	public void testEllipseMissingCxAndCyAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/missing-cx-and-cy-attributes.svg");
	}

	@Test
	public void testEllipseMissingCxAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/missing-cx-attribute.svg");
	}

	@Test
	public void testEllipseMissingCyAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/missing-cy-attribute.svg");
	}

	@Test
	public void testEllipseMissingRxAndRyAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/missing-rx-and-ry-attributes.svg");
	}

	@Test
	public void testEllipseMissingRxAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/missing-rx-attribute.svg");
	}

	@Test
	public void testEllipseMissingRyAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/missing-ry-attribute.svg");
	}

	@Test
	public void testEllipseNegativeRxAndRyAttributes() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/ellipse/negative-rx-and-ry-attributes.svg", 2);
	}

	@Test
	public void testEllipseNegativeRxAttribute() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/ellipse/negative-rx-attribute.svg", 1);
	}

	@Test
	public void testEllipseNegativeRyAttribute() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/ellipse/negative-ry-attribute.svg", 1);
	}

	@Test
	public void testEllipsePercentValuesMissingRy() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/percent-values-missing-ry.svg");
	}

	@Test
	public void testEllipsePercentValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/percent-values.svg");
	}

	@Test
	public void testEllipseSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/ellipse/simple-case.svg");
	}

	@Test
	public void testLineNoCoordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/no-coordinates.svg");
	}

	@Test
	public void testLineNoX1AndY1Coordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/no-x1-and-y1-coordinates.svg");
	}

	@Test
	public void testLineNoX1Coordinate() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/no-x1-coordinate.svg");
	}

	@Test
	public void testLineNoX2AndY2Coordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/no-x2-and-y2-coordinates.svg");
	}

	@Test
	public void testLineNoX2Coordinate() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/no-x2-coordinate.svg");
	}

	@Test
	public void testLineNoY1Coordinate() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/no-y1-coordinate.svg");
	}

	@Test
	public void testLineNoY2Coordinate() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/no-y2-coordinate.svg");
	}

	@Test
	public void testLinePercentUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/percent-units.svg");
	}

	@Test
	public void testLineSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/simple-case.svg");
	}

	@Disabled
	@Test
	public void testLineWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/line/with-transform.svg");
	}

	@Disabled
	@Test
	public void testPathA() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/A.svg");
	}

	@Test
	public void testPathM_AS() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-A-s.svg");
	}

	@Test
	public void testPathM_AT() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-A-t.svg");
	}

	@Test
	public void testPathM_ATrimmed() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-A-trimmed.svg");
	}

	@Test
	public void testPathM_A() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-A.svg");
	}

	@Test
	public void testPathM_C_S() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-C-S.svg");
	}

	@Test
	public void testPathM_C() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-C.svg");
	}

	@Test
	public void testPathM_H_HImplicit() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-H-H-implicit.svg");
	}

	@Test
	public void testPathM_H_H() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-H-H.svg");
	}

	@Test
	public void testPathM_H() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-H.svg");
	}

	@Test
	public void testPathM_L_L_ZRel() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-L-Z-rel.svg");
	}

	@Test
	public void testPathM_L_L_Z() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-L-Z.svg");
	}

	@Test
	public void testPathM_L_LImplicit() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-L-implicit.svg");
	}

	@Test
	public void testPathM_L_M_L() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-M-L.svg");
	}

	@Test
	public void testPathM_L_M_Z() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-M-Z.svg");
	}

	@Test
	public void testPathM_L_M() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-M.svg");
	}

	@Test
	public void testPathM_L_Z_A() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-Z-A.svg");
	}

	@Test
	public void testPathM_L_Z_L_L() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L-Z-L-L.svg");
	}

	@Test
	public void testPathM_L() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-L.svg");
	}

	@Test
	public void testPathM_MImplicit_MImplicit() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-M-implicit-M-implicit.svg");
	}

	@Test
	public void testPathM_MRel() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-M-rel.svg");
	}

	@Test
	public void testPathM_M() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-M.svg");
	}

	@Test
	public void testPathM_Q_TRel() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-Q-T-rel.svg");
	}

	@Test
	public void testPathM_Q_T() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-Q-T.svg");
	}

	@Test
	public void testPathM_QRel_TRel() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-Q-rel-T-rel.svg");
	}

	@Test
	public void testPathM_Q() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-Q.svg");
	}

	@Test
	public void testPathM_S_S() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-S-S.svg");
	}

	@Test
	public void testPathM_S() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-S.svg");
	}

	@Test
	public void testPathM_T_QRel() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-T-Q-rel.svg");
	}

	@Test
	public void testPathM_T_Q() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-T-Q.svg");
	}

	@Disabled
	@Test
	public void testPathM_T_SRel() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-T-S-rel.svg");
	}

	@Disabled
	@Test
	public void testPathM_T_S() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-T-S.svg");
	}

	@Test
	public void testPathM_T_TRel() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-T-T-rel.svg");
	}

	@Test
	public void testPathM_T_T() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-T-T.svg");
	}

	@Disabled
	@Test
	public void testPathM_T() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-T.svg");
	}

	@Test
	public void testPathM_V_VImplicit() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-V-V-implicit.svg");
	}

	@Test
	public void testPathM_V_V() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-V-V.svg");
	}

	@Test
	public void testPathM_V() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-V.svg");
	}

	@Test
	public void testPathM_Z() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-Z.svg");
	}

	@Test
	public void testPathMRel_MRelImplicit_MRelImplicit() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-rel-M-rel-implicit-M-rel-implicit.svg");
	}

	@Test
	public void testPathMRel_M() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M-rel-M.svg");
	}

	@Test
	public void testPathM() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/M.svg");
	}

	@Test
	public void testPathEmpty() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/empty.svg");
	}

	@Test
	public void testPathExtraSpaces() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/extra-spaces.svg");
	}

	@Test
	public void testPathInvalidDataIn_L() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/invalid-data-in-L.svg", 1);
	}

	@Test
	public void testPathInvalidTransform() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/invalid-transform.svg", 1);
	}

	@Test
	public void testPathMissingCoordinateIn_L() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/missing-coordinate-in-L.svg", 1);
	}

	@Test
	public void testPathMultiLineData() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/multi-line-data.svg");
	}

	@Test
	public void testPathNegativeLargeArcFlagValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/negative-large-arc-flag-value.svg", 1);
	}

	@Test
	public void testPathNegativeSweepFlagValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/negative-sweep-flag-value.svg", 1);
	}

	@Test
	public void testPathNoCommawspAfterSweepFlag() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/no-commawsp-after-sweep-flag.svg");
	}

	@Test
	public void testPathNoCommawspBeforeArcFlags() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/no-commawsp-before-arc-flags.svg", 1);
	}

	@Test
	public void testPathNoCommawspBetweenAndAfterArcFlags()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/no-commawsp-between-and-after-arc-flags.svg");
	}

	@Test
	public void testPathNoCommawspBetweenArcFlags() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/no-commawsp-between-arc-flags.svg");
	}

	@Test
	public void testPathNumericCharacterReferences() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/path/numeric-character-references.svg");
	}

	@Test
	public void testPathOutOfRangeLargeArcFlagValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/out-of-range-large-arc-flag-value.svg", 1);
	}

	@Test
	public void testPathOutOfRangeSweepFlagValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/path/out-of-range-sweep-flag-value.svg", 1);
	}

	@Test
	public void testPolygonIgnoreOddPoints() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/polygon/ignore-odd-points.svg", 1);
	}

	@Test
	public void testPolygonMissingPointsAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/polygon/missing-points-attribute.svg");
	}

	@Test
	public void testPolygonNotEnoughPoints() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/polygon/not-enough-points.svg", 1);
	}

	@Test
	public void testPolygonSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/polygon/simple-case.svg");
	}

	@Test
	public void testPolygonStopProcessingOnInvalidData() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/polygon/stop-processing-on-invalid-data.svg",
				1);
	}

	@Test
	public void testPolylineIgnoreOddPoints() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/polyline/ignore-odd-points.svg", 1);
	}

	@Test
	public void testPolylineMissingPointsAttribute() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/polyline/missing-points-attribute.svg");
	}

	@Test
	public void testPolylineNotEnoughPoints() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/polyline/not-enough-points.svg", 1);
	}

	@Test
	public void testPolylineSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/polyline/simple-case.svg");
	}

	@Test
	public void testPolylineStopProcessingOnInvalidData() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/polyline/stop-processing-on-invalid-data.svg",
				1);
	}

	@Disabled
	@Test
	public void testRectCapValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/cap-values.svg");
	}

	@Disabled
	@Test
	public void testRectChValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/ch-values.svg");
	}

	@Test
	public void testRectEmValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/em-values.svg");
	}

	@Test
	public void testRectExValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/ex-values.svg");
	}

	@Disabled
	@Test
	public void testRectIcValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/ic-values.svg");
	}

	@Test
	public void testRectInvalidCoordinates() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/rect/invalid-coordinates.svg", 1);
	}

	@Test
	public void testRectInvalidLength() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/rect/invalid-length.svg", 1);
	}

	@Test
	public void testRectLhValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/lh-values.svg");
	}

	@Test
	public void testRectMissingHeightAttributeProcessing() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/missing-height-attribute-processing.svg");
	}

	@Test
	public void testRectMissingWidthAttributeProcessing() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/missing-width-attribute-processing.svg");
	}

	@Test
	public void testRectMmValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/mm-values.svg");
	}

	@Test
	public void testRectNegativeHeightAttributeProcessing()
			throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/rect/negative-height-attribute-processing.svg",
				1);
	}

	@Test
	public void testRectNegativeRxAndRyAttributesResolving()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/shapes/rect/negative-rx-and-ry-attributes-resolving.svg", 2);
	}

	@Test
	public void testRectNegativeRxAttributeResolving() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/rect/negative-rx-attribute-resolving.svg", 1);
	}

	@Test
	public void testRectNegativeRyAttributeResolving() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/rect/negative-ry-attribute-resolving.svg", 1);
	}

	@Test
	public void testRectNegativeWidthAttributeProcessing() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/shapes/rect/negative-width-attribute-processing.svg",
				1);
	}

	@Test
	public void testRectPercentageValues_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/percentage-values-1.svg");
	}

	@Test
	public void testRectPercentageValues_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/percentage-values-2.svg");
	}

	@Test
	public void testRectQValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/q-values.svg");
	}

	@Test
	public void testRectRemValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/rem-values.svg");
	}

	@Test
	public void testRectRlhValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/rlh-values.svg");
	}

	@Test
	public void testRectRoundedRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/rounded-rect.svg");
	}

	@Test
	public void testRectRxAndRyAttributesClampingOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/rx-and-ry-attributes-clamping-order.svg");
	}

	@Test
	public void testRectRxAttributeClamping() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/rx-attribute-clamping.svg");
	}

	@Test
	public void testRectRxAttributeResolving() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/rx-attribute-resolving.svg");
	}

	@Test
	public void testRectRyAttributeClamping() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/ry-attribute-clamping.svg");
	}

	@Test
	public void testRectRyAttributeResolving() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/ry-attribute-resolving.svg");
	}

	@Test
	public void testRectSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/simple-case.svg");
	}

	@Disabled
	@Test
	public void testRectViAndVbValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/vi-and-vb-values.svg");
	}

	@Disabled
	@Test
	public void testRectVminAndVmaxValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/vmin-and-vmax-values.svg");
	}

	@Disabled
	@Test
	public void testRectVwAndVhValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/vw-and-vh-values.svg");
	}

	@Test
	public void testRectWithChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/with-child.svg");
	}

	@Test
	public void testRectXAttributeResolving() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/x-attribute-resolving.svg");
	}

	@Test
	public void testRectYAttributeResolving() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/y-attribute-resolving.svg");
	}

	@Test
	public void testRectZeroHeightAttributeProcessing() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/zero-height-attribute-processing.svg");
	}

	@Test
	public void testRectZeroRxAttributeResolving() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/zero-rx-attribute-resolving.svg");
	}

	@Test
	public void testRectZeroRyAttributeResolving() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/zero-ry-attribute-resolving.svg");
	}

	@Test
	public void testRectZeroWidthAttributeProcessing() throws TranscoderException, IOException {
		test("samples/resvg/tests/shapes/rect/zero-width-attribute-processing.svg");
	}

}
