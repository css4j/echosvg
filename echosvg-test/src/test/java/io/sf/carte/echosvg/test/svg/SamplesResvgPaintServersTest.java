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
public class SamplesResvgPaintServersTest extends SamplesResvgRendering {

	/*
	 * linearGradient
	 */

	@Test
	public void testLinearGradientAttributesViaXlinkHrefComplexOrder()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/attributes-via-xlink-href-complex-order.svg");
	}

	@Disabled
	@Test
	public void testLinearGradientAttributesViaXlinkHrefFromRadialGradient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/attributes-via-xlink-href-from-radialGradient.svg");
	}

	@Disabled
	@Test
	public void testLinearGradientAttributesViaXlinkHrefFromRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/attributes-via-xlink-href-from-rect.svg");
	}

	@Test
	public void testLinearGradientAttributesViaXlinkHrefOnlyRequired()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/attributes-via-xlink-href-only-required.svg");
	}

	@Test
	public void testLinearGradientAttributesViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/attributes-via-xlink-href.svg");
	}

	@Test
	public void testLinearGradientDefaultAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/default-attributes.svg");
	}

	@Test
	public void testLinearGradientGradientTransformAndTransform()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/gradientTransform-and-transform.svg");
	}

	@Test
	public void testLinearGradientGradientTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/gradientTransform.svg");
	}

	@Test
	public void testLinearGradientGradientUnitsEqObjectBoundingBoxWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/gradientUnits=objectBoundingBox-with-percent.svg");
	}

	@Test
	public void testLinearGradientGradientUnitsEqUserSpaceOnUseWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/gradientUnits=userSpaceOnUse-with-percent.svg");
	}

	@Test
	public void testLinearGradientGradientUnitsEqUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/gradientUnits=userSpaceOnUse.svg");
	}

	@Test
	public void testLinearGradientHslaColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/hsla-color.svg");
	}

	@Test
	public void testLinearGradientInvalidChild_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/invalid-child-1.svg");
	}

	@Test
	public void testLinearGradientInvalidChild_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/invalid-child-2.svg");
	}

	@Test
	public void testLinearGradientInvalidChild_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/invalid-child-3.svg");
	}

	@Disabled
	@Test
	public void testLinearGradientInvalidGradientTransform()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/linearGradient/invalid-gradientTransform.svg",
				1);
	}

	@Disabled
	@Test
	public void testLinearGradientInvalidGradientUnits() throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/linearGradient/invalid-gradientUnits.svg", 1);
	}

	@Disabled
	@Test
	public void testLinearGradientInvalidSpreadMethod() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/linearGradient/invalid-spreadMethod.svg",
				1);
	}

	@Test
	public void testLinearGradientInvalidXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/invalid-xlink-href.svg");
	}

	@Test
	public void testLinearGradientManyStops() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/many-stops.svg");
	}

	@Test
	public void testLinearGradientNoStops() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/no-stops.svg");
	}

	@Disabled
	@Test
	public void testLinearGradientRecursiveXlinkHref_1() throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/linearGradient/recursive-xlink-href-1.svg", 1);
	}

	@Disabled
	@Test
	public void testLinearGradientRecursiveXlinkHref_2() throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/linearGradient/recursive-xlink-href-2.svg", 1);
	}

	@Disabled
	@Test
	public void testLinearGradientRecursiveXlinkHref_3() throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/linearGradient/recursive-xlink-href-3.svg", 1);
	}

	@Disabled
	@Test
	public void testLinearGradientSelfRecursiveXlinkHref() throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/linearGradient/self-recursive-xlink-href.svg",
				1);
	}

	@Test
	public void testLinearGradientSingleStopWithOpacityUsedByFillAndStroke()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/single-stop-with-opacity-used-by-fill-and-stroke.svg");
	}

	@Test
	public void testLinearGradientSingleStopWithOpacityUsedByFill()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/single-stop-with-opacity-used-by-fill.svg");
	}

	@Test
	public void testLinearGradientSingleStopWithOpacityUsedByStroke()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/single-stop-with-opacity-used-by-stroke.svg");
	}

	@Test
	public void testLinearGradientSingleStop() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/single-stop.svg");
	}

	@Test
	public void testLinearGradientSpreadMethodEqPad() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/spreadMethod=pad.svg");
	}

	@Test
	public void testLinearGradientSpreadMethodEqReflect() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/spreadMethod=reflect.svg");
	}

	@Test
	public void testLinearGradientSpreadMethodEqRepeat() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/spreadMethod=repeat.svg");
	}

	@Test
	public void testLinearGradientStopsViaXlinkHrefComplexOrder_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/stops-via-xlink-href-complex-order-1.svg");
	}

	@Test
	public void testLinearGradientStopsViaXlinkHrefComplexOrder_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/stops-via-xlink-href-complex-order-2.svg");
	}

	@Test
	public void testLinearGradientStopsViaXlinkHrefFromRadialGradient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/stops-via-xlink-href-from-radialGradient.svg");
	}

	@Disabled
	@Test
	public void testLinearGradientStopsViaXlinkHrefFromRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/stops-via-xlink-href-from-rect.svg");
	}

	@Test
	public void testLinearGradientStopsViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/stops-via-xlink-href.svg");
	}

	@Test
	public void testLinearGradientUnresolvedXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/linearGradient/unresolved-xlink-href.svg");
	}

	/*
	 * pattern
	 */

	@Test
	public void testPatternAttributesViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/attributes-via-xlink-href.svg");
	}

	@Test
	public void testPatternChildWithInvalid_FuncIRI() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/pattern/child-with-invalid-FuncIRI.svg",
				1);
	}

	@Test
	public void testPatternChildrenViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/children-via-xlink-href.svg");
	}

	@Test
	public void testPatternDisplayEqNoneOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/display=none-on-child.svg");
	}

	@Test
	public void testPatternEverythingViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/everything-via-xlink-href.svg");
	}

	@Disabled
	@Test
	public void testPatternInvalidPatternTransform() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/pattern/invalid-patternTransform.svg",
				1);
	}

	@Disabled
	@Test
	public void testPatternInvalidPatternUnitsAndPatternContentUnits()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/pattern/invalid-patternUnits-and-patternContentUnits.svg",
				2);
	}

	@Disabled
	@Test
	public void testPatternMissingHeight() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/pattern/missing-height.svg", 1);
	}

	@Disabled
	@Test
	public void testPatternMissingWidth() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/pattern/missing-width.svg", 1);
	}

	@Disabled
	@Test
	public void testPatternNestedObjectBoundingBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/nested-objectBoundingBox.svg");
	}

	@Test
	public void testPatternNoChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/no-children.svg");
	}

	@Disabled
	@Test
	public void testPatternOutOfOrderReferencing() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/out-of-order-referencing.svg");
	}

	@Disabled
	@Test
	public void testPatternOverflowEqVisible() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/overflow=visible.svg");
	}

	@Disabled
	@Test
	public void testPatternPatternOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/pattern-on-child.svg");
	}

	@Test
	public void testPatternPatternContentUnitsWithViewBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/patternContentUnits-with-viewBox.svg");
	}

	@Disabled
	@Test
	public void testPatternPatternContentUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/patternContentUnits=objectBoundingBox.svg");
	}

	@Test
	public void testPatternPatternUnitsEqObjectBoundingBoxWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/patternUnits=objectBoundingBox-with-percent.svg");
	}

	@Test
	public void testPatternPatternUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/patternUnits=objectBoundingBox.svg");
	}

	@Test
	public void testPatternPatternUnitsEqUserSpaceOnUseWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/patternUnits=userSpaceOnUse-with-percent.svg");
	}

	@Test
	public void testPatternPreserveAspectRatio() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/preserveAspectRatio.svg");
	}

	@Disabled
	@Test
	public void testPatternRecursiveOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/recursive-on-child.svg");
	}

	@Disabled
	@Test
	public void testPatternSelfRecursiveOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/self-recursive-on-child.svg");
	}

	@Disabled
	@Test
	public void testPatternSelfRecursive() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/self-recursive.svg");
	}

	@Test
	public void testPatternSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/simple-case.svg");
	}

	@Test
	public void testPatternTextChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/text-child.svg");
	}

	@Test
	public void testPatternTinyPatternUpscaled() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/tiny-pattern-upscaled.svg");
	}

	@Test
	public void testPatternTransformAndPatternTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/transform-and-patternTransform.svg");
	}

	@Disabled
	@Test
	public void testPatternViewBoxViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/viewBox-via-xlink-href.svg");
	}

	@Test
	public void testPatternWithPatternTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/with-patternTransform.svg");
	}

	@Test
	public void testPatternWithViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/with-viewBox.svg");
	}

	@Test
	public void testPatternWithXAndY() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/pattern/with-x-and-y.svg");
	}

	/*
	 * radialGradient
	 */

	@Test
	public void testRadialGradientAttributesViaXlinkHrefComplexOrder()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/attributes-via-xlink-href-complex-order.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientAttributesViaXlinkHrefFromLinearGradient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/attributes-via-xlink-href-from-linearGradient.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientAttributesViaXlinkHrefFromRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/attributes-via-xlink-href-from-rect.svg");
	}

	@Test
	public void testRadialGradientAttributesViaXlinkHrefOnlyRequired()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/attributes-via-xlink-href-only-required.svg");
	}

	@Test
	public void testRadialGradientAttributesViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/attributes-via-xlink-href.svg");
	}

	@Test
	public void testRadialGradientDefaultAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/default-attributes.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientFocalPointCorrection() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/focal-point-correction.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientFrEqMinus1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fr=-1.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientFrEq0dot2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fr=0.2.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientFrEq0dot5() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fr=0.5.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientFrEq0dot7() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fr=0.7.svg");
	}

	@Test
	public void testRadialGradientFxResolving_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fx-resolving-1.svg");
	}

	@Test
	public void testRadialGradientFxResolving_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fx-resolving-2.svg");
	}

	@Test
	public void testRadialGradientFxResolving_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fx-resolving-3.svg");
	}

	@Test
	public void testRadialGradientFyResolving_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fy-resolving-1.svg");
	}

	@Test
	public void testRadialGradientFyResolving_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fy-resolving-2.svg");
	}

	@Test
	public void testRadialGradientFyResolving_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/fy-resolving-3.svg");
	}

	@Test
	public void testRadialGradientGradientTransformAndTransform()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/gradientTransform-and-transform.svg");
	}

	@Test
	public void testRadialGradientGradientTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/gradientTransform.svg");
	}

	@Test
	public void testRadialGradientGradientUnitsEqObjectBoundingBoxWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/gradientUnits=objectBoundingBox-with-percent.svg");
	}

	@Test
	public void testRadialGradientGradientUnitsEqUserSpaceOnUseWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/gradientUnits=userSpaceOnUse-with-percent.svg");
	}

	@Test
	public void testRadialGradientGradientUnitsEqUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/gradientUnits=userSpaceOnUse.svg");
	}

	@Test
	public void testRadialGradientHslaColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/hsla-color.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientInvalidGradientTransform()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/radialGradient/invalid-gradientTransform.svg",
				1);
	}

	@Disabled
	@Test
	public void testRadialGradientInvalidGradientUnits() throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/radialGradient/invalid-gradientUnits.svg", 1);
	}

	@Disabled
	@Test
	public void testRadialGradientInvalidSpreadMethod() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/radialGradient/invalid-spreadMethod.svg",
				1);
	}

	@Test
	public void testRadialGradientInvalidXlinkHref() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/radialGradient/invalid-xlink-href.svg",
				8);
	}

	@Test
	public void testRadialGradientManyStops() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/many-stops.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientNegativeR() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/radialGradient/negative-r.svg", 1);
	}

	@Test
	public void testRadialGradientNoStops() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/no-stops.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientRecursiveXlinkHref() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/radialGradient/recursive-xlink-href.svg",
				1);
	}

	@Disabled
	@Test
	public void testRadialGradientSelfRecursiveXlinkHref() throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/paint-servers/radialGradient/self-recursive-xlink-href.svg",
				1);
	}

	@Test
	public void testRadialGradientSingleStop() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/single-stop.svg");
	}

	@Test
	public void testRadialGradientSpreadMethodEqPad() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/spreadMethod=pad.svg");
	}

	@Test
	public void testRadialGradientSpreadMethodEqReflect() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/spreadMethod=reflect.svg");
	}

	@Test
	public void testRadialGradientSpreadMethodEqRepeat() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/spreadMethod=repeat.svg");
	}

	@Test
	public void testRadialGradientStopsViaXlinkHrefComplexOrder()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/stops-via-xlink-href-complex-order.svg");
	}

	@Test
	public void testRadialGradientStopsViaXlinkHrefFromLinearGradient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/stops-via-xlink-href-from-linearGradient.svg");
	}

	@Disabled
	@Test
	public void testRadialGradientStopsViaXlinkHrefFromRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/stops-via-xlink-href-from-rect.svg");
	}

	@Test
	public void testRadialGradientStopsViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/stops-via-xlink-href.svg");
	}

	@Test
	public void testRadialGradientUnresolvedXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/unresolved-xlink-href.svg");
	}

	@Test
	public void testRadialGradientXlinkHrefNotToGradient() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/xlink-href-not-to-gradient.svg");
	}

	@Test
	public void testRadialGradientZeroRWithStopOpacity_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/zero-r-with-stop-opacity-1.svg");
	}

	@Test
	public void testRadialGradientZeroRWithStopOpacity_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/zero-r-with-stop-opacity-2.svg");
	}

	@Test
	public void testRadialGradientZeroR() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/radialGradient/zero-r.svg");
	}

	/*
	 * stop-color
	 */

	@Test
	public void testStopColorSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop-color/simple-case.svg");
	}

	/*
	 * stop-opacity
	 */

	@Disabled
	@Test
	public void testStopOpacity50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop-opacity/50percent.svg");
	}

	@Test
	public void testStopOpacitySimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop-opacity/simple-case.svg");
	}

	/*
	 * stop
	 */

	@Test
	public void testStopEqualStopColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/equal-stop-color.svg");
	}

	@Test
	public void testStopHslaColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/hsla-color.svg");
	}

	@Disabled
	@Test
	public void testStopInvalidOffset_1() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/stop/invalid-offset-1.svg", 1);
	}

	@Disabled
	@Test
	public void testStopInvalidOffset_2() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/paint-servers/stop/invalid-offset-2.svg", 1);
	}

	@Test
	public void testStopMissingOffset_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/missing-offset-1.svg");
	}

	@Test
	public void testStopMissingOffset_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/missing-offset-2.svg");
	}

	@Test
	public void testStopMissingOffset_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/missing-offset-3.svg");
	}

	@Test
	public void testStopMissingOffset_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/missing-offset-4.svg");
	}

	@Test
	public void testStopMissingOffset_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/missing-offset-5.svg");
	}

	@Test
	public void testStopMissingOffset_6() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/missing-offset-6.svg");
	}

	@Test
	public void testStopMissingOffset_7() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/missing-offset-7.svg");
	}

	@Test
	public void testStopNoStopColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/no-stop-color.svg");
	}

	@Test
	public void testStopOffsetClampingWithPercent() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/offset-clamping-with-percent.svg");
	}

	@Test
	public void testStopOffsetClamping() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/offset-clamping.svg");
	}

	@Test
	public void testStopOffsetWithPercent() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/offset-with-percent.svg");
	}

	@Test
	public void testStopStopColorWithCurrentColor_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-currentColor-1.svg");
	}

	@Test
	public void testStopStopColorWithCurrentColor_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-currentColor-2.svg");
	}

	@Test
	public void testStopStopColorWithCurrentColor_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-currentColor-3.svg");
	}

	@Test
	public void testStopStopColorWithCurrentColor_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-currentColor-4.svg");
	}

	@Test
	public void testStopStopColorWithInherit_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-inherit-1.svg");
	}

	@Test
	public void testStopStopColorWithInherit_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-inherit-2.svg");
	}

	@Test
	public void testStopStopColorWithInherit_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-inherit-3.svg");
	}

	@Test
	public void testStopStopColorWithInherit_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-inherit-4.svg");
	}

	@Test
	public void testStopStopColorWithInherit_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-color-with-inherit-5.svg");
	}

	@Test
	public void testStopStopWithSmallerOffset() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stop-with-smaller-offset.svg");
	}

	@Test
	public void testStopStopsWithEqualOffset_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stops-with-equal-offset-1.svg");
	}

	@Test
	public void testStopStopsWithEqualOffset_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stops-with-equal-offset-2.svg");
	}

	@Test
	public void testStopStopsWithEqualOffset_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stops-with-equal-offset-3.svg");
	}

	@Test
	public void testStopStopsWithEqualOffset_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stops-with-equal-offset-4.svg");
	}

	@Test
	public void testStopStopsWithEqualOffset_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stops-with-equal-offset-5.svg");
	}

	@Test
	public void testStopStopsWithEqualOffset_6() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/stops-with-equal-offset-6.svg");
	}

	@Test
	public void testStopZeroOffsetInTheMiddle() throws TranscoderException, IOException {
		test("samples/resvg/tests/paint-servers/stop/zero-offset-in-the-middle.svg");
	}

}
