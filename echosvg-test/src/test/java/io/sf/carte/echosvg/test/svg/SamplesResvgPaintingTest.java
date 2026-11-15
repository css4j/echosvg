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
public class SamplesResvgPaintingTest extends SamplesResvgRendering {

	@Test
	public void testColorInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/color/inherit.svg");
	}

	@Disabled
	@Test
	public void testColorRecursiveNestedContextWithoutColor()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/color/recursive-nested-context-without-color.svg");
	}

	@Disabled
	@Test
	public void testColorRecursiveNestedContext() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/color/recursive-nested-context.svg");
	}

	@Test
	public void testColorSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/color/simple-case.svg");
	}

	@Disabled
	@Test
	public void testContextInMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/in-marker.svg");
	}

	@Disabled
	@Test
	public void testContextInNestedMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/in-nested-marker.svg");
	}

	@Disabled
	@Test
	public void testContextInNestedUseAndMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/in-nested-use-and-marker.svg");
	}

	@Disabled
	@Test
	public void testContextInNestedUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/in-nested-use.svg");
	}

	@Disabled
	@Test
	public void testContextInUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/in-use.svg");
	}

	@Disabled
	@Test
	public void testContextOnShapeWithZeroSizeBbox() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/on-shape-with-zero-size-bbox.svg");
	}

	@Disabled
	@Test
	public void testContextWithGradientAndGradientTransform()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-gradient-and-gradient-transform.svg");
	}

	@Disabled
	@Test
	public void testContextWithGradientInUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-gradient-in-use.svg");
	}

	@Disabled
	@Test
	public void testContextWithGradientOnMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-gradient-on-marker.svg");
	}

	@Disabled
	@Test
	public void testContextWithPatternAndTransformInUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-pattern-and-transform-in-use.svg");
	}

	@Disabled
	@Test
	public void testContextWithPatternInUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-pattern-in-use.svg");
	}

	@Disabled
	@Test
	public void testContextWithPatternObjectBoundingBoxInUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-pattern-objectBoundingBox-in-use.svg");
	}

	@Disabled
	@Test
	public void testContextWithPatternOnMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-pattern-on-marker.svg");
	}

	@Disabled
	@Test
	public void testContextWithText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/with-text.svg");
	}

	@Disabled
	@Test
	public void testContextWithoutContextElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/context/without-context-element.svg");
	}

	@Test
	public void testDisplayBBoxImpact() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/bBox-impact.svg");
	}

	@Test
	public void testDisplayNoneOnClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-clipPath.svg");
	}

	@Test
	public void testDisplayNoneOnDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-defs.svg");
	}

	@Test
	public void testDisplayNoneOnLinearGradient() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-linearGradient.svg");
	}

	@Test
	public void testDisplayNoneOnRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-rect.svg");
	}

	@Disabled
	@Test
	public void testDisplayNoneOnSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-svg.svg");
	}

	@Disabled
	@Test
	public void testDisplayNoneOnTref() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-tref.svg");
	}

	@Test
	public void testDisplayNoneOnTspan_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-tspan-1.svg");
	}

	@Test
	public void testDisplayNoneOnTspan_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/display/none-on-tspan-2.svg");
	}

	@Disabled
	@Test
	public void testFillOpacity50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/50percent.svg");
	}

	@Test
	public void testFillOpacityHalfOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/half-opacity.svg");
	}

	@Test
	public void testFillOpacityNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/nested.svg");
	}

	@Test
	public void testFillOpacityOnParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/on-parent.svg");
	}

	@Test
	public void testFillOpacityOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/on-text.svg");
	}

	@Test
	public void testFillOpacityWithLinearGradient() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/with-linearGradient.svg");
	}

	@Test
	public void testFillOpacityWithOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/with-opacity.svg");
	}

	@Test
	public void testFillOpacityWithPattern() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-opacity/with-pattern.svg");
	}

	@Test
	public void testFillRuleEvenodd() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-rule/evenodd.svg");
	}

	@Test
	public void testFillRuleNonzero() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill-rule/nonzero.svg");
	}

	@Test
	public void testFillHexRGBColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/#RGB-color.svg");
	}

	@Test
	public void testFillHexRGBA() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/#RGBA.svg");
	}

	@Test
	public void testFillHexRRGGBBColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/#RRGGBB-color.svg");
	}

	@Test
	public void testFillHexRRGGBBUppercaseColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/#RRGGBB-uppercase-color.svg");
	}

	@Test
	public void testFillHexRRGGBBAA() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/#RRGGBBAA.svg");
	}

	@Test
	public void testFillCurrentColorWithoutParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/currentColor-without-parent.svg");
	}

	@Test
	public void testFillCurrentColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/currentColor.svg");
	}

	@Test
	public void testFillDoubleInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/double-inherit.svg");
	}

	@Test
	public void testFillFuncIRIToAMissingElementWithAFallbackColor()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/painting/fill/funcIRI-to-a-missing-element-with-a-fallback-color.svg",
				1);
	}

	@Test
	public void testFillFuncIRIToAMissingElementWithANoneFallback()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/painting/fill/funcIRI-to-a-missing-element-with-a-none-fallback.svg",
				1);
	}

	@Test
	public void testFillFuncIRIToAnInvalidElementWithANoneFallback()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/funcIRI-to-an-invalid-element-with-a-none-fallback.svg");
	}

	@Test
	public void testFillFuncIRIToAnUnsupportedElement() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/fill/funcIRI-to-an-unsupported-element.svg",
				1);
	}

	@Test
	public void testFillFuncIRIWithAFallbackColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/funcIRI-with-a-fallback-color.svg");
	}

	@Test
	public void testFillHsl_120_100percent_25percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/hsl-120-100percent-25percent.svg");
	}

	@Test
	public void testFillHsl_120_200percent_25percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/hsl-120-200percent-25percent.svg");
	}

	@Test
	public void testFillHsl_360_100percent_25percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/hsl-360-100percent-25percent.svg");
	}

	@Test
	public void testFillHsl_999_100percent_25percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/hsl-999-100percent-25percent.svg");
	}

	@Test
	public void testFillHslWithAlpha() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/hsl-with-alpha.svg");
	}

	@Test
	public void testFillHslaWithPercentageSAndLValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/hsla-with-percentage-s-and-l-values.svg");
	}

	@Test
	public void testFillInheritWithoutParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/inherit-without-parent.svg");
	}

	@Test
	public void testFillInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/inherit.svg");
	}

	@Test
	public void testFillInvalidHexRRGGBB_1() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/fill/invalid-#RRGGBB-1.svg", 1);
	}

	@Test
	public void testFillInvalidHexRRGGBB_2() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/fill/invalid-#RRGGBB-2.svg", 1);
	}

	@Test
	public void testFillInvalidHexRRGGBB_3() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/fill/invalid-#RRGGBB-3.svg", 1);
	}

	@Test
	public void testFillInvalid_FuncIRIWithACurrentColorFallback()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/invalid-FuncIRI-with-a-currentColor-fallback.svg");
	}

	@Test
	public void testFillInvalid_FuncIRIWithAFallbackColor()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/invalid-FuncIRI-with-a-fallback-color.svg");
	}

	@Test
	public void testFillLinearGradientOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/linear-gradient-on-shape.svg");
	}

	@Test
	public void testFillLinearGradientOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/linear-gradient-on-text.svg");
	}

	@Test
	public void testFillMissing_FuncIRIWithACurrentColorFallback()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/painting/fill/missing-FuncIRI-with-a-currentColor-fallback.svg",
				1);
	}

	@Test
	public void testFillNamedColorInMixedcase() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/named-color-in-mixedcase.svg");
	}

	@Test
	public void testFillNamedColorInUppercase() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/named-color-in-uppercase.svg");
	}

	@Test
	public void testFillNamedColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/named-color.svg");
	}

	@Test
	public void testFillNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/none.svg");
	}

	@Test
	public void testFillNotTrimmedAttributeValue() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/not-trimmed-attribute-value.svg");
	}

	@Test
	public void testFillPatternOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/pattern-on-shape.svg");
	}

	@Test
	public void testFillPatternOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/pattern-on-text.svg");
	}

	@Test
	public void testFillRadialGradientOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/radial-gradient-on-shape.svg");
	}

	@Test
	public void testFillRadialGradientOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/radial-gradient-on-text.svg");
	}

	@Test
	public void testFillRandomValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/fill/random-value.svg", 1);
	}

	@Test
	public void testFillRgb_0_127_0_0dot5() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-0-127-0-0.5.svg");
	}

	@Test
	public void testFillRgbColorWithABigFractionPart() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-color-with-a-big-fraction-part.svg");
	}

	@Test
	public void testFillRgbColorWithExtraSpaces() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-color-with-extra-spaces.svg");
	}

	@Test
	public void testFillRgbColorWithFloatPercentageValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-color-with-float-percentage-values.svg");
	}

	@Test
	public void testFillRgbColorWithFloats() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-color-with-floats.svg");
	}

	@Test
	public void testFillRgbColorWithPercentageOverflow() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-color-with-percentage-overflow.svg");
	}

	@Test
	public void testFillRgbColorWithPercentageValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-color-with-percentage-values.svg");
	}

	@Test
	public void testFillRgbColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-color.svg");
	}

	@Test
	public void testFillRgbIntIntInt() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgb-int-int-int.svg");
	}

	@Test
	public void testFillRgba_0_127_0__1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgba-0-127-0--1.svg");
	}

	@Test
	public void testFillRgba_0_127_0_0dot5() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgba-0-127-0-0.5.svg");
	}

	@Test
	public void testFillRgba_0_127_0_0() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgba-0-127-0-0.svg");
	}

	@Test
	public void testFillRgba_0_127_0_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgba-0-127-0-1.svg");
	}

	@Test
	public void testFillRgba_0_127_0_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgba-0-127-0-2.svg");
	}

	@Test
	public void testFillRgba_0_127_0_50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgba-0-127-0-50percent.svg");
	}

	@Test
	public void testFillRgba_0_50percent_0_0dot5() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/fill/rgba-0-50percent-0-0.5.svg", 1);
	}

	@Test
	public void testFillRgba_0percent_50percent_0percent_0dot5()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/rgba-0percent-50percent-0percent-0.5.svg");
	}

	@Test
	public void testFillTransparent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/transparent.svg");
	}

	@Test
	public void testFillUppercaseRgbColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/fill/uppercase-rgb-color.svg");
	}

	@Disabled
	@Test
	public void testImageRenderingOnFeImage() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/image-rendering/on-feImage.svg");
	}

	@Test
	public void testImageRenderingOptimizeSpeedOn_SVG() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/image-rendering/optimizeSpeed-on-SVG.svg");
	}

	@Test
	public void testImageRenderingOptimizeSpeed() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/image-rendering/optimizeSpeed.svg");
	}

	@Disabled
	@Test
	public void testIsolationAsProperty() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/isolation/as-property.svg");
	}

	@Test
	public void testIsolationIsolate() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/isolation/isolate.svg");
	}

	@Test
	public void testMarkerDefaultClip() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/default-clip.svg");
	}

	@Test
	public void testMarkerEmpty() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/empty.svg");
	}

	@Test
	public void testMarkerInheritance_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/inheritance-1.svg");
	}

	@Test
	public void testMarkerInheritance_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/inheritance-2.svg");
	}

	@Test
	public void testMarkerInvalidChild() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/marker/invalid-child.svg", 3);
	}

	@Disabled
	@Test
	public void testMarkerMarkerOnCircle() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/marker-on-circle.svg");
	}

	@Test
	public void testMarkerMarkerOnLine() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/marker-on-line.svg");
	}

	@Test
	public void testMarkerMarkerOnPolygon() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/marker-on-polygon.svg");
	}

	@Test
	public void testMarkerMarkerOnPolyline() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/marker-on-polyline.svg");
	}

	@Disabled
	@Test
	public void testMarkerMarkerOnRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/marker-on-rect.svg");
	}

	@Disabled
	@Test
	public void testMarkerMarkerOnRoundedRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/marker-on-rounded-rect.svg");
	}

	@Test
	public void testMarkerMarkerOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/marker-on-text.svg");
	}

	@Disabled
	@Test
	public void testMarkerMarkerWithANegativeSize() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/marker/marker-with-a-negative-size.svg", 1);
	}

	@Test
	public void testMarkerNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/nested.svg");
	}

	@Test
	public void testMarkerNoStrokeOnTarget() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/no-stroke-on-target.svg");
	}

	@Test
	public void testMarkerOn_ArcTo() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/on-ArcTo.svg");
	}

	@Test
	public void testMarkerOnlyMarkerEnd() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/only-marker-end.svg");
	}

	@Test
	public void testMarkerOnlyMarkerMid() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/only-marker-mid.svg");
	}

	@Test
	public void testMarkerOnlyMarkerStart() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/only-marker-start.svg");
	}

	@Test
	public void testMarkerOrientEqMinus45() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=-45.svg");
	}

	@Disabled
	@Test
	public void testMarkerOrientEq0dot25turn() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=0.25turn.svg");
	}

	@Disabled
	@Test
	public void testMarkerOrientEq1dot5rad() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=1.5rad.svg");
	}

	@Test
	public void testMarkerOrientEq30() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=30.svg");
	}

	@Disabled
	@Test
	public void testMarkerOrientEq40grad() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=40grad.svg");
	}

	@Test
	public void testMarkerOrientEq9999() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=9999.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-1.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-2.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-3.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-4.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-5.svg");
	}

	@Disabled
	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_6() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-6.svg");
	}

	@Disabled
	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_7() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-7.svg");
	}

	@Disabled
	@Test
	public void testMarkerOrientEqAutoOn_M_C_C_8() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-C-8.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_C_L() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-L.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_C_M_L() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-C-M-L.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_L_C() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-L-C.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_L_L_Z_Z_Z() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-L-L-Z-Z-Z.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_L_L() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-L-L.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_L_M_C() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-L-M-C.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_L_Z() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-L-Z.svg");
	}

	@Test
	public void testMarkerOrientEqAutoOn_M_L() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-on-M-L.svg");
	}

	@Disabled
	@Test
	public void testMarkerOrientEqAutoStartReverse() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/orient=auto-start-reverse.svg");
	}

	@Test
	public void testMarkerPercentValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/percent-values.svg");
	}

	@Disabled
	@Test
	public void testMarkerRecursive_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/recursive-1.svg");
	}

	@Disabled
	@Test
	public void testMarkerRecursive_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/recursive-2.svg");
	}

	@Disabled
	@Test
	public void testMarkerRecursive_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/recursive-3.svg");
	}

	@Disabled
	@Test
	public void testMarkerRecursive_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/recursive-4.svg");
	}

	@Disabled
	@Test
	public void testMarkerRecursive_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/recursive-5.svg");
	}

	@Test
	public void testMarkerTargetWithSubpaths_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/target-with-subpaths-1.svg");
	}

	@Test
	public void testMarkerTargetWithSubpaths_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/target-with-subpaths-2.svg");
	}

	@Test
	public void testMarkerTheMarkerPropertyIn_CSS() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/the-marker-property-in-CSS.svg");
	}

	@Disabled
	@Test
	public void testMarkerTheMarkerProperty() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/the-marker-property.svg");
	}

	@Test
	public void testMarkerWithALargeStroke() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/with-a-large-stroke.svg");
	}

	@Test
	public void testMarkerWithATextChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/with-a-text-child.svg");
	}

	@Test
	public void testMarkerWithAnImageChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/with-an-image-child.svg");
	}

	@Test
	public void testMarkerWithInvalidMarkerUnits() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/marker/with-invalid-markerUnits.svg", 3);
	}

	@Disabled
	@Test
	public void testMarkerWithMarkerUnitsEqUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/with-markerUnits=userSpaceOnUse.svg");
	}

	@Test
	public void testMarkerWithViewBox_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/with-viewBox-1.svg");
	}

	@Test
	public void testMarkerWithViewBox_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/with-viewBox-2.svg");
	}

	@Test
	public void testMarkerZeroLengthPath_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/zero-length-path-1.svg");
	}

	@Test
	public void testMarkerZeroLengthPath_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/zero-length-path-2.svg");
	}

	@Test
	public void testMarkerZeroSizedStroke() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/zero-sized-stroke.svg");
	}

	@Test
	public void testMarkerZeroSized() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/marker/zero-sized.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeAsProperty() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/as-property.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeColorBurn() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/color-burn.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeColorDodge() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/color-dodge.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/color.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeDarken() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/darken.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeDifference() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/difference.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeExclusion() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/exclusion.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeHardLight() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/hard-light.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeHue() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/hue.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeLighten() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/lighten.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeLuminosity() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/luminosity.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeMultiply() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/multiply.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeNormal() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/normal.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeOpacityOnElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/opacity-on-element.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeOpacityOnGroup() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/opacity-on-group.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeOverlay() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/overlay.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeSaturation() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/saturation.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeScreen() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/screen.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeSoftLight() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/soft-light.svg");
	}

	@Disabled
	@Test
	public void testMixBlendModeXor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/mix-blend-mode/xor.svg");
	}

	@Disabled
	@Test
	public void testOpacity50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/50percent.svg");
	}

	@Test
	public void testOpacityBBoxImpact() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/bBox-impact.svg");
	}

	@Test
	public void testOpacityClampValue_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/clamp-value-1.svg");
	}

	@Test
	public void testOpacityClampValue_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/clamp-value-2.svg");
	}

	@Test
	public void testOpacityGroupOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/group-opacity.svg");
	}

	@Test
	public void testOpacityInvalidValue_2() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/opacity/invalid-value-2.svg", 1);
	}

	@Test
	public void testOpacityMixedGroupOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/mixed-group-opacity.svg");
	}

	@Test
	public void testOpacityOnAnInvalidElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/on-an-invalid-element.svg");
	}

	@Test
	public void testOpacityOnTheRootSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/opacity/on-the-root-svg.svg");
	}

	@Test
	public void testOverflowAutoOnMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/overflow/auto-on-marker.svg");
	}

	@Disabled
	@Test
	public void testOverflowInheritOnMarkerWithoutParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/overflow/inherit-on-marker-without-parent.svg");
	}

	@Test
	public void testOverflowInheritOnMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/overflow/inherit-on-marker.svg");
	}

	@Test
	public void testOverflowScrollOnMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/overflow/scroll-on-marker.svg");
	}

	@Test
	public void testOverflowVisibleOnMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/overflow/visible-on-marker.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderDuplicates() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/duplicates.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderFillMarkersStroke() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/fill-markers-stroke.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderFill() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/fill.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderInvalid() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/invalid.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderMarkersStroke() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/markers-stroke.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderMarkers() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/markers.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderNormal() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/normal.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/on-text.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/on-tspan.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderStrokeInvalid() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/stroke-invalid.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderStrokeMarkersFill() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/stroke-markers-fill.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderStrokeMarkers() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/stroke-markers.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderStroke() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/stroke.svg");
	}

	@Disabled
	@Test
	public void testPaintOrderTrailingData() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/paint-order/trailing-data.svg");
	}

	@Test
	public void testShapeRenderingAutoOnCircle() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/auto-on-circle.svg");
	}

	@Test
	public void testShapeRenderingCrispEdgesOnCircle() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/crispEdges-on-circle.svg");
	}

	@Test
	public void testShapeRenderingGeometricPrecisionOnCircle()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/geometricPrecision-on-circle.svg");
	}

	@Test
	public void testShapeRenderingInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/inheritance.svg");
	}

	@Test
	public void testShapeRenderingOnHorizontalLine() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/on-horizontal-line.svg");
	}

	@Test
	public void testShapeRenderingOptimizeSpeedOnCircle() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/optimizeSpeed-on-circle.svg");
	}

	@Test
	public void testShapeRenderingOptimizeSpeedOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/optimizeSpeed-on-text.svg");
	}

	@Test
	public void testShapeRenderingPathWithMarker() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/shape-rendering/path-with-marker.svg");
	}

	@Test
	public void testStrokeDasharray0NWithButtCaps() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/0-n-with-butt-caps.svg");
	}

	@Test
	public void testStrokeDasharray0NWithRoundCaps() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/0-n-with-round-caps.svg");
	}

	@Test
	public void testStrokeDasharray0NWithSquareCaps() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/0-n-with-square-caps.svg");
	}

	@Test
	public void testStrokeDasharrayCommaWsSeparator() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/comma-ws-separator.svg");
	}

	@Test
	public void testStrokeDasharrayEmUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/em-units.svg");
	}

	@Test
	public void testStrokeDasharrayEvenCount() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/even-count.svg");
	}

	@Test
	public void testStrokeDasharrayMmUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/mm-units.svg");
	}

	@Test
	public void testStrokeDasharrayMultipleSubpaths() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/multiple-subpaths.svg");
	}

	@Test
	public void testStrokeDasharrayN_0() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/n-0.svg");
	}

	@Test
	public void testStrokeDasharrayNegativeSum() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/negative-sum.svg");
	}

	@Test
	public void testStrokeDasharrayNegativeValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/negative-values.svg");
	}

	@Test
	public void testStrokeDasharrayNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/none.svg");
	}

	@Test
	public void testStrokeDasharrayOddCount() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/odd-count.svg");
	}

	@Test
	public void testStrokeDasharrayOnACircle() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/on-a-circle.svg");
	}

	@Test
	public void testStrokeDasharrayPercentUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/percent-units.svg");
	}

	@Test
	public void testStrokeDasharrayWsSeparator() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/ws-separator.svg");
	}

	@Test
	public void testStrokeDasharrayZeroSum() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dasharray/zero-sum.svg");
	}

	@Test
	public void testStrokeDashoffsetDefault() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dashoffset/default.svg");
	}

	@Test
	public void testStrokeDashoffsetEmUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dashoffset/em-units.svg");
	}

	@Test
	public void testStrokeDashoffsetMmUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dashoffset/mm-units.svg");
	}

	@Test
	public void testStrokeDashoffsetNegativeValue() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dashoffset/negative-value.svg");
	}

	@Test
	public void testStrokeDashoffsetPercentUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dashoffset/percent-units.svg");
	}

	@Test
	public void testStrokeDashoffsetPxUnits() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-dashoffset/px-units.svg");
	}

	@Test
	public void testStrokeLinecapButt() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/butt.svg");
	}

	@Test
	public void testStrokeLinecapOpenPathWithButt() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/open-path-with-butt.svg");
	}

	@Test
	public void testStrokeLinecapOpenPathWithRound() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/open-path-with-round.svg");
	}

	@Test
	public void testStrokeLinecapOpenPathWithSquare() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/open-path-with-square.svg");
	}

	@Test
	public void testStrokeLinecapRound() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/round.svg");
	}

	@Test
	public void testStrokeLinecapSquare() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/square.svg");
	}

	@Test
	public void testStrokeLinecapZeroLengthPathWithButt() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/zero-length-path-with-butt.svg");
	}

	@Test
	public void testStrokeLinecapZeroLengthPathWithRound() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/zero-length-path-with-round.svg");
	}

	@Test
	public void testStrokeLinecapZeroLengthPathWithSquare()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linecap/zero-length-path-with-square.svg");
	}

	@Disabled
	@Test
	public void testStrokeLinejoinArcs() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linejoin/arcs.svg");
	}

	@Test
	public void testStrokeLinejoinBevel() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linejoin/bevel.svg");
	}

	@Disabled
	@Test
	public void testStrokeLinejoinMiterClip() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linejoin/miter-clip.svg");
	}

	@Test
	public void testStrokeLinejoinMiter() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linejoin/miter.svg");
	}

	@Test
	public void testStrokeLinejoinRound() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-linejoin/round.svg");
	}

	// stroke-linecap="miter" is not valid
	@Test
	public void testStrokeMiterlimitDefault() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/stroke-miterlimit/default.svg", 1);
	}

	@Test
	public void testStrokeMiterlimitInvalidValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/stroke-miterlimit/invalid-value.svg", 1);
	}

	// stroke-linecap="miter" is not valid
	@Test
	public void testStrokeMiterlimitValidValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/stroke-miterlimit/valid-value.svg", 1);
	}

	@Disabled
	@Test
	public void testStrokeMiterlimitValueWithMm() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-miterlimit/value-with-mm.svg");
	}

	@Disabled
	@Test
	public void testStrokeMiterlimitValueWithPercent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-miterlimit/value-with-percent.svg");
	}

	@Disabled
	@Test
	public void testStrokeOpacity50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/50percent.svg");
	}

	@Test
	public void testStrokeOpacityHalfOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/half-opacity.svg");
	}

	@Test
	public void testStrokeOpacityNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/nested.svg");
	}

	@Test
	public void testStrokeOpacityOnParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/on-parent.svg");
	}

	@Test
	public void testStrokeOpacityOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/on-text.svg");
	}

	@Test
	public void testStrokeOpacityWithLinearGradient() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/with-linearGradient.svg");
	}

	@Test
	public void testStrokeOpacityWithOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/with-opacity.svg");
	}

	@Test
	public void testStrokeOpacityWithPattern() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-opacity/with-pattern.svg");
	}

	@Test
	public void testStrokeWidthBold() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-width/bold.svg");
	}

	@Test
	public void testStrokeWidthDefault() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-width/default.svg");
	}

	@Test
	public void testStrokeWidthNegative() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-width/negative.svg");
	}

	@Test
	public void testStrokeWidthPercentage() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-width/percentage.svg");
	}

	@Test
	public void testStrokeWidthZero() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke-width/zero.svg");
	}

	@Disabled
	@Test
	public void testStrokeControlPointsClamping_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/control-points-clamping-1.svg");
	}

	@Test
	public void testStrokeControlPointsClamping_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/control-points-clamping-2.svg");
	}

	@Test
	public void testStrokeCurrentColorWithoutAParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/currentColor-without-a-parent.svg");
	}

	@Test
	public void testStrokeFuncIRIToUnsupportedElement() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/painting/stroke/funcIRI-to-unsupported-element.svg",
				1);
	}

	@Test
	public void testStrokeGradientWithObjectBoundingBoxAndFallbackOnLines()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/gradient-with-objectBoundingBox-and-fallback-on-lines.svg");
	}

	@Test
	public void testStrokeGradientWithObjectBoundingBoxOnPathWithoutABbox_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/gradient-with-objectBoundingBox-on-path-without-a-bbox-1.svg");
	}

	@Test
	public void testStrokeGradientWithObjectBoundingBoxOnPathWithoutABbox_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/gradient-with-objectBoundingBox-on-path-without-a-bbox-2.svg");
	}

	@Test
	public void testStrokeGradientWithObjectBoundingBoxOnShapeWithoutABbox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/gradient-with-objectBoundingBox-on-shape-without-a-bbox.svg");
	}

	@Disabled
	@Test
	public void testStrokeLineAsCurve_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/line-as-curve-1.svg");
	}

	@Disabled
	@Test
	public void testStrokeLineAsCurve_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/line-as-curve-2.svg");
	}

	@Test
	public void testStrokeLinearGradientOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/linear-gradient-on-text.svg");
	}

	@Test
	public void testStrokeLinearGradient() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/linear-gradient.svg");
	}

	@Test
	public void testStrokeNamedColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/named-color.svg");
	}

	@Test
	public void testStrokeNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/none.svg");
	}

	@Test
	public void testStrokePatternOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/pattern-on-text.svg");
	}

	@Test
	public void testStrokePatternWithObjectBoundingBoxFallbackOnZeroBboxShape()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/painting/stroke/pattern-with-objectBoundingBox-fallback-on-zero-bbox-shape.svg",
				2);
	}

	@Disabled
	@Test
	public void testStrokePatternWithObjectBoundingBoxOnZeroBboxShape()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/painting/stroke/pattern-with-objectBoundingBox-on-zero-bbox-shape.svg",
				1);
	}

	@Test
	public void testStrokePattern() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/pattern.svg");
	}

	@Test
	public void testStrokeRadialGradientOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/radial-gradient-on-text.svg");
	}

	@Test
	public void testStrokeRadialGradient() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/stroke/radial-gradient.svg");
	}

	@Test
	public void testVisibilityBboxImpact_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/visibility/bbox-impact-1.svg");
	}

	@Test
	public void testVisibilityBboxImpact_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/visibility/bbox-impact-2.svg");
	}

	@Test
	public void testVisibilityBboxImpact_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/visibility/bbox-impact-3.svg");
	}

	@Test
	public void testVisibilityCollapseOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/visibility/collapse-on-tspan.svg");
	}

	@Test
	public void testVisibilityHiddenOnGroup() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/visibility/hidden-on-group.svg");
	}

	@Test
	public void testVisibilityHiddenOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/visibility/hidden-on-shape.svg");
	}

	@Test
	public void testVisibilityHiddenOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/painting/visibility/hidden-on-tspan.svg");
	}

}
