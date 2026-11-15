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
public class SamplesResvgFiltersTest extends SamplesResvgRendering {

	@Test
	public void testEnableBackgroundAccumulateWithNew() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/accumulate-with-new.svg");
	}

	@Test
	public void testEnableBackgroundAccumulate() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/accumulate.svg");
	}

	@Test
	public void testEnableBackgroundFilterOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/filter-on-shape.svg");
	}

	@Disabled
	@Test
	public void testEnableBackgroundInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/inherit.svg");
	}

	@Test
	public void testEnableBackgroundNewWithInvalidRegion_1()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/enable-background/new-with-invalid-region-1.svg", 1);
	}

	@Test
	public void testEnableBackgroundNewWithInvalidRegion_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/new-with-invalid-region-2.svg");
	}

	@Test
	public void testEnableBackgroundNewWithInvalidRegion_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/new-with-invalid-region-3.svg");
	}

	@Test
	public void testEnableBackgroundNewWithRegion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/new-with-region.svg");
	}

	@Test
	public void testEnableBackgroundNew() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/new.svg");
	}

	@Test
	public void testEnableBackgroundShapesAfterFilter() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/shapes-after-filter.svg");
	}

	@Test
	public void testEnableBackgroundStopOnTheFirstNew_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/stop-on-the-first-new-1.svg");
	}

	@Test
	public void testEnableBackgroundStopOnTheFirstNew_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/stop-on-the-first-new-2.svg");
	}

	@Test
	public void testEnableBackgroundWithClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-clip-path.svg");
	}

	@Test
	public void testEnableBackgroundWithFilterOnTheSameElement()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-filter-on-the-same-element.svg");
	}

	@Test
	public void testEnableBackgroundWithFilter() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-filter.svg");
	}

	@Test
	public void testEnableBackgroundWithMask() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-mask.svg");
	}

	@Test
	public void testEnableBackgroundWithOpacity_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-opacity-1.svg");
	}

	@Test
	public void testEnableBackgroundWithOpacity_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-opacity-2.svg");
	}

	@Test
	public void testEnableBackgroundWithOpacity_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-opacity-3.svg");
	}

	@Test
	public void testEnableBackgroundWithOpacity_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-opacity-4.svg");
	}

	@Test
	public void testEnableBackgroundWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/enable-background/with-transform.svg");
	}

	@Test
	public void testFeBlendEmpty() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feBlend/empty.svg", 1);
	}

	@Disabled
	@Test
	public void testFeBlendModeEqColorBurn() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/mode=color-burn.svg");
	}

	@Test
	public void testFeBlendModeEqDarken() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/mode=darken.svg");
	}

	@Disabled
	@Test
	public void testFeBlendModeEqHue() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/mode=hue.svg");
	}

	@Test
	public void testFeBlendModeEqLighten() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/mode=lighten.svg");
	}

	@Test
	public void testFeBlendModeEqMultiply() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/mode=multiply.svg");
	}

	@Test
	public void testFeBlendModeEqNormal() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/mode=normal.svg");
	}

	@Test
	public void testFeBlendModeEqScreen() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/mode=screen.svg");
	}

	@Test
	public void testFeBlendWithSubregionOnInput_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/with-subregion-on-input-1.svg");
	}

	@Test
	public void testFeBlendWithSubregionOnInput_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feBlend/with-subregion-on-input-2.svg");
	}

	@Test
	public void testFeColorMatrixInvalidType() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feColorMatrix/invalid-type.svg", 1);
	}

	@Test
	public void testFeColorMatrixTypeEqHueRotateWithoutAnAngle()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=hueRotate-without-an-angle.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqHueRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=hueRotate.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqLuminanceToAlpha() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=luminanceToAlpha.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqMatrixWithEmptyValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=matrix-with-empty-values.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqMatrixWithNonNormalizedValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=matrix-with-non-normalized-values.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqMatrixWithNotEnoughValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feColorMatrix/type=matrix-with-not-enough-values.svg",
				1);
	}

	@Test
	public void testFeColorMatrixTypeEqMatrixWithTooManyValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feColorMatrix/type=matrix-with-too-many-values.svg",
				1);
	}

	@Test
	public void testFeColorMatrixTypeEqMatrixWithoutValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=matrix-without-values.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqMatrix() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=matrix.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqSaturateWithALargeCoefficient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=saturate-with-a-large-coefficient.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqSaturateWithNegativeCoefficient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=saturate-with-negative-coefficient.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqSaturateWithoutACoefficient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=saturate-without-a-coefficient.svg");
	}

	@Test
	public void testFeColorMatrixTypeEqSaturate() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/type=saturate.svg");
	}

	@Test
	public void testFeColorMatrixWithoutAType() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/without-a-type.svg");
	}

	@Test
	public void testFeColorMatrixWithoutAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feColorMatrix/without-attributes.svg");
	}

	@Test
	public void testFeComponentTransferInvalidType() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feComponentTransfer/invalid-type.svg", 1);
	}

	@Test
	public void testFeComponentTransferMixedTypes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/mixed-types.svg");
	}

	@Test
	public void testFeComponentTransferNoChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/no-children.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqDiscreteOnBlue()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=discrete-on-blue.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqGammaOnBlue() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=gamma-on-blue.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqGammaWithAnInvalidOffset()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feComponentTransfer/type=gamma-with-an-invalid-offset.svg",
				1);
	}

	@Disabled
	@Test
	public void testFeComponentTransferTypeEqGammaWithInvalidValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feComponentTransfer/type=gamma-with-invalid-values.svg",
				3);
	}

	@Test
	public void testFeComponentTransferTypeEqIdentityOnAll()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=identity-on-all.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqLinearOnBlue()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=linear-on-blue.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqLinearWithInvalidValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feComponentTransfer/type=linear-with-invalid-values.svg",
				2);
	}

	@Test
	public void testFeComponentTransferTypeEqLinearWithLargeValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=linear-with-large-values.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqTableAndTableValuesEq1_0_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-and-tableValues=1-0-1.svg");
	}

	@Disabled
	@Test
	public void testFeComponentTransferTypeEqTableAndTableValuesEq1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-and-tableValues=1.svg");
	}

	@Disabled
	@Test
	public void testFeComponentTransferTypeEqTableAndTableValuesEq100__100()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-and-tableValues=100--100.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqTableAndTableValuesEq1px()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feComponentTransfer/type=table-and-tableValues=1px.svg",
				1);
	}

	@Disabled
	@Test
	public void testFeComponentTransferTypeEqTableOnAlpha()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-on-alpha.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqTableOnBlueTwice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-on-blue-twice.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqTableOnBlueWithSRGBInterpolation()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-on-blue-with-sRGB-interpolation.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqTableOnBlue() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-on-blue.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqTableWithAnEmptyTableValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-with-an-empty-tableValues.svg");
	}

	@Disabled
	@Test
	public void testFeComponentTransferTypeEqTableWithLargeValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-with-large-values.svg");
	}

	@Test
	public void testFeComponentTransferTypeEqTableWithoutTableValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComponentTransfer/type=table-without-tableValues.svg");
	}

	@Test
	public void testFeCompositeDefaultOperator() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/default-operator.svg");
	}

	@Test
	public void testFeCompositeEmpty() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feComposite/empty.svg", 1);
	}

	@Disabled
	@Test
	public void testFeCompositeInvalidOperator() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feComposite/invalid-operator.svg", 1);
	}

	@Test
	public void testFeCompositeOperatorEqArithmeticAndInvalidK1_4()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic-and-invalid-k1-4.svg");
	}

	@Test
	public void testFeCompositeOperatorEqArithmeticOnSRGB()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic-on-sRGB.svg");
	}

	@Test
	public void testFeCompositeOperatorEqArithmeticWithLargeK1_4()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic-with-large-k1-4.svg");
	}

	@Test
	public void testFeCompositeOperatorEqArithmeticWithOpacityOnSRGB()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic-with-opacity-on-sRGB.svg");
	}

	@Test
	public void testFeCompositeOperatorEqArithmeticWithOpacity()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic-with-opacity.svg");
	}

	@Test
	public void testFeCompositeOperatorEqArithmeticWithSomeK1_4()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic-with-some-k1-4.svg");
	}

	@Test
	public void testFeCompositeOperatorEqArithmeticWithoutK1_4()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic-without-k1-4.svg");
	}

	@Test
	public void testFeCompositeOperatorEqArithmetic() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=arithmetic.svg");
	}

	@Test
	public void testFeCompositeOperatorEqAtop() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=atop.svg");
	}

	@Test
	public void testFeCompositeOperatorEqIn() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=in.svg");
	}

	@Test
	public void testFeCompositeOperatorEqOut() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=out.svg");
	}

	@Test
	public void testFeCompositeOperatorEqOver() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=over.svg");
	}

	@Test
	public void testFeCompositeOperatorEqXor() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/operator=xor.svg");
	}

	@Test
	public void testFeCompositeWithSubregionOnInput_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/with-subregion-on-input-1.svg");
	}

	@Test
	public void testFeCompositeWithSubregionOnInput_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feComposite/with-subregion-on-input-2.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixBiasEqMinus0_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/bias=-0.5.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixBiasEq0_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/bias=0.5.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixBiasEq9999() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/bias=9999.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixCustomDivisor() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/custom-divisor.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixDivisorEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/divisor=0.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixEdgeModeEqNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/edgeMode=none.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixEdgeModeEqWrapWithMatrixLargerThanTarget()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/edgeMode=wrap-with-matrix-larger-than-target.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixEdgeModeEqWrap() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/edgeMode=wrap.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixEmptyKernelMatrix() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feConvolveMatrix/empty-kernelMatrix.svg", 1);
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixKernelMatrixWithNotEnoughValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feConvolveMatrix/kernelMatrix-with-not-enough-values.svg",
				1);
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixKernelMatrixWithTooManyValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feConvolveMatrix/kernelMatrix-with-too-many-values.svg",
				1);
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixKernelMatrixWithZeroSumAndNoDivisor()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/kernelMatrix-with-zero-sum-and-no-divisor.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixNoKernelMatrix() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feConvolveMatrix/no-kernelMatrix.svg", 1);
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixOrderWithANegativeValue_1()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feConvolveMatrix/order-with-a-negative-value-1.svg",
				2);
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixOrderWithANegativeValue_2()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feConvolveMatrix/order-with-a-negative-value-2.svg",
				2);
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixOrderEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/order=0.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixOrderEq4_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/order=4-2.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixOrderEq4_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/order=4-4.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixOrderEq4() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/order=4.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixPreserveAlphaEqTrue() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/preserveAlpha=true.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixTargetXEqMinus1() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feConvolveMatrix/targetX=-1.svg", 1);
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixTargetXEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/targetX=0.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixTargetXEq2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/targetX=2.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixTargetXEq3() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/targetX=3.svg");
	}

	@Disabled
	@Test
	public void testFeConvolveMatrixUnsetOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feConvolveMatrix/unset-order.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/complex-transform.svg");
	}

	@Test
	public void testFeDiffuseLightingDiffuseConstantEqMinus1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/diffuseConstant=-1.svg");
	}

	@Test
	public void testFeDiffuseLightingDiffuseConstantEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/diffuseConstant=0.svg");
	}

	@Test
	public void testFeDiffuseLightingDiffuseConstantEq5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/diffuseConstant=5.svg");
	}

	@Test
	public void testFeDiffuseLightingLightingColorEqCurrentColorWithoutColor()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/lighting-color=currentColor-without-color.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingLightingColorEqCurrentColor()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/lighting-color=currentColor.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingLightingColorEqHsla() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/lighting-color=hsla.svg");
	}

	@Test
	public void testFeDiffuseLightingLightingColorEqInherit()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/lighting-color=inherit.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingLightingColorEqSeagreen()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/lighting-color=seagreen.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingLinearRGBColorInterpolation()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/linearRGB-color-interpolation.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingMultipleLightSources()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/multiple-light-sources.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingNoLightSource() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feDiffuseLighting/no-light-source.svg", 1);
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSingleLightSourceWithComment()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/single-light-source-with-comment.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSingleLightSourceWithDesc()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/single-light-source-with-desc.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSingleLightSourceWithInvalidChild()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/single-light-source-with-invalid-child.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSingleLightSourceWithTitleAndDesc()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/single-light-source-with-title-and-desc.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSingleLightSourceWithTitle()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/single-light-source-with-title.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSingleLightSource() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/single-light-source.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSurfaceScaleEqMinus10()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/surfaceScale=-10.svg");
	}

	@Test
	public void testFeDiffuseLightingSurfaceScaleEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/surfaceScale=0.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSurfaceScaleEq1_33() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/surfaceScale=1.33.svg");
	}

	@Disabled
	@Test
	public void testFeDiffuseLightingSurfaceScaleEq5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDiffuseLighting/surfaceScale=5.svg");
	}

	@Test
	public void testFeDisplacementMapSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDisplacementMap/simple-case.svg");
	}

	@Disabled
	@Test
	public void testFeDistantLightDefaultAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDistantLight/default-attributes.svg");
	}

	@Test
	public void testFeDistantLightNegativeAzimuthAndElevation()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDistantLight/negative-azimuth-and-elevation.svg");
	}

	@Disabled
	@Test
	public void testFeDistantLightOnlyAzimuth() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDistantLight/only-azimuth.svg");
	}

	@Disabled
	@Test
	public void testFeDistantLightOnlyElevation() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDistantLight/only-elevation.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowHslaColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/hsla-color.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowOnlyStdDeviation() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/only-stdDeviation.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowStdDeviationEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/stdDeviation=0.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowWithFloodColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/with-flood-color.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowWithFloodOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/with-flood-opacity.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowWithOffsetClipped() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/with-offset-clipped.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowWithOffset() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/with-offset.svg");
	}

	@Disabled
	@Test
	public void testFeDropShadowWithPercentOffset() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feDropShadow/with-percent-offset.svg");
	}

	@Disabled
	@Test
	public void testFeFloodComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/complex-transform.svg");
	}

	@Test
	public void testFeFloodDefaultValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/default-values.svg");
	}

	@Test
	public void testFeFloodPartialSubregion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/partial-subregion.svg");
	}

	@Test
	public void testFeFloodSeagreen() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/seagreen.svg");
	}

	@Test
	public void testFeFloodSubregionInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/subregion-inheritance.svg");
	}

	@Test
	public void testFeFloodSubregionWithPrimitiveUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/subregion-with-primitiveUnits=objectBoundingBox.svg");
	}

	@Test
	public void testFeFloodWithOpacityOnTargetElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/with-opacity-on-target-element.svg");
	}

	@Test
	public void testFeFloodWithOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feFlood/with-opacity.svg");
	}

	@Test
	public void testFeGaussianBlurComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/complex-transform.svg");
	}

	@Test
	public void testFeGaussianBlurEmptyStdDeviation() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/empty-stdDeviation.svg");
	}

	@Test
	public void testFeGaussianBlurHugeStdDeviation() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/huge-stdDeviation.svg");
	}

	@Disabled
	@Test
	public void testFeGaussianBlurNegativeStdDeviation() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feGaussianBlur/negative-stdDeviation.svg", 1);
	}

	@Test
	public void testFeGaussianBlurNoStdDeviation() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/no-stdDeviation.svg");
	}

	@Test
	public void testFeGaussianBlurSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/simple-case.svg");
	}

	@Test
	public void testFeGaussianBlurSmallStdDeviation() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/small-stdDeviation.svg");
	}

	@Test
	public void testFeGaussianBlurStdDeviationWithMultipleValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/feGaussianBlur/stdDeviation-with-multiple-values.svg",
				1);
	}

	@Test
	public void testFeGaussianBlurStdDeviationWithTwoDifferentValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/stdDeviation-with-two-different-values.svg");
	}

	@Test
	public void testFeGaussianBlurStdDeviationWithTwoValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/stdDeviation-with-two-values.svg");
	}

	@Test
	public void testFeGaussianBlurStdDeviationEq0_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/stdDeviation=0-5.svg");
	}

	@Test
	public void testFeGaussianBlurStdDeviationEq5_0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/stdDeviation=5-0.svg");
	}

	@Test
	public void testFeGaussianBlurTinyStdDeviation() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feGaussianBlur/tiny-stdDeviation.svg");
	}

	@Test
	public void testFeImageChainedFeImage() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/chained-feImage.svg");
	}

	@Test
	public void testFeImageEmbeddedPng() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/embedded-png.svg");
	}

	@Disabled
	@Test
	public void testFeImageEmpty() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feImage/empty.svg", 1);
	}

	@Test
	public void testFeImageLinkOnAnElementWithComplexTransform()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-on-an-element-with-complex-transform.svg");
	}

	@Test
	public void testFeImageLinkOnAnElementWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-on-an-element-with-transform.svg");
	}

	@Test
	public void testFeImageLinkToAnElementOutsideDefs_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-an-element-outside-defs-1.svg");
	}

	@Test
	public void testFeImageLinkToAnElementOutsideDefs_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-an-element-outside-defs-2.svg");
	}

	@Test
	public void testFeImageLinkToAnElementWithOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-an-element-with-opacity.svg");
	}

	@Test
	public void testFeImageLinkToAnElementWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-an-element-with-transform.svg");
	}

	@Test
	public void testFeImageLinkToAnElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-an-element.svg");
	}

	@Test
	public void testFeImageLinkToAnInvalidElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-an-invalid-element.svg");
	}

	@Test
	public void testFeImageLinkToG() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-g.svg");
	}

	@Test
	public void testFeImageLinkToUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/link-to-use.svg");
	}

	@Disabled
	@Test
	public void testFeImagePreserveAspectRatioEqNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/preserveAspectRatio=none.svg");
	}

	@Disabled
	@Test
	public void testFeImageRecursiveLinks_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/recursive-links-1.svg");
	}

	@Disabled
	@Test
	public void testFeImageRecursiveLinks_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/recursive-links-2.svg");
	}

	@Disabled
	@Test
	public void testFeImageSelfRecursive() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feImage/self-recursive.svg", 1);
	}

	@Test
	public void testFeImageSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/simple-case.svg");
	}

	@Test
	public void testFeImageSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/svg.svg");
	}

	@Disabled
	@Test
	public void testFeImageWithSubregion_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-subregion-1.svg");
	}

	@Disabled
	@Test
	public void testFeImageWithSubregion_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-subregion-2.svg");
	}

	@Test
	public void testFeImageWithSubregion_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-subregion-3.svg");
	}

	@Test
	public void testFeImageWithSubregion_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-subregion-4.svg");
	}

	@Test
	public void testFeImageWithSubregion_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-subregion-5.svg");
	}

	@Test
	public void testFeImageWithXYAndProtrudingSubregion_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-x-y-and-protruding-subregion-1.svg");
	}

	@Test
	public void testFeImageWithXYAndProtrudingSubregion_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-x-y-and-protruding-subregion-2.svg");
	}

	@Test
	public void testFeImageWithXY() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feImage/with-x-y.svg");
	}

	@Test
	public void testFeMergeColorInterpolationFiltersEqLinearRGB()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMerge/color-interpolation-filters=linearRGB.svg");
	}

	@Test
	public void testFeMergeColorInterpolationFiltersEqSRGB()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMerge/color-interpolation-filters=sRGB.svg");
	}

	@Test
	public void testFeMergeComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMerge/complex-transform.svg");
	}

	@Test
	public void testFeMorphologyEmptyRadius() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/empty-radius.svg");
	}

	@Test
	public void testFeMorphologyHugeRadius() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/huge-radius.svg");
	}

	@Test
	public void testFeMorphologyNegativeRadius() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feMorphology/negative-radius.svg", 1);
	}

	@Test
	public void testFeMorphologyNoRadius() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/no-radius.svg");
	}

	@Test
	public void testFeMorphologyOperatorEqDilate() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/operator=dilate.svg");
	}

	@Disabled
	@Test
	public void testFeMorphologyRadiusWithTooManyValues() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feMorphology/radius-with-too-many-values.svg",
				1);
	}

	@Disabled
	@Test
	public void testFeMorphologyRadiusEq0_5WithObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/radius=0.5-with-objectBoundingBox.svg");
	}

	@Test
	public void testFeMorphologyRadiusEq0_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/radius=0.5.svg");
	}

	@Test
	public void testFeMorphologyRadiusEq1_10() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/radius=1-10.svg");
	}

	@Disabled
	@Test
	public void testFeMorphologyRadiusEq10_0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/radius=10-0.svg");
	}

	@Test
	public void testFeMorphologyRadiusEq10_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/radius=10-1.svg");
	}

	@Test
	public void testFeMorphologySimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/simple-case.svg");
	}

	@Disabled
	@Test
	public void testFeMorphologySourceWithOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/source-with-opacity.svg");
	}

	@Test
	public void testFeMorphologyZeroRadius() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feMorphology/zero-radius.svg");
	}

	@Test
	public void testFeOffsetComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/complex-transform.svg");
	}

	@Test
	public void testFeOffsetFractionalOffset() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/fractional-offset.svg");
	}

	@Test
	public void testFeOffsetNegativeOffset() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/negative-offset.svg");
	}

	@Test
	public void testFeOffsetNoOffset() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/no-offset.svg");
	}

	@Test
	public void testFeOffsetOnlyDx() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/only-dx.svg");
	}

	@Test
	public void testFeOffsetOnlyDy() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/only-dy.svg");
	}

	@Test
	public void testFeOffsetPercentageValues() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feOffset/percentage-values.svg", 2);
	}

	@Test
	public void testFeOffsetSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/simple-case.svg");
	}

	@Disabled
	@Test
	public void testFeOffsetWithPrimitiveUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feOffset/with-primitiveUnits=objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testFePointLightComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/fePointLight/complex-transform.svg");
	}

	@Disabled
	@Test
	public void testFePointLightCustomAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/fePointLight/custom-attributes.svg");
	}

	@Disabled
	@Test
	public void testFePointLightDefaultAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/fePointLight/default-attributes.svg");
	}

	@Disabled
	@Test
	public void testFePointLightPrimitiveUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/fePointLight/primitiveUnits=objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testFeSpecularLightingLightingColorEqHsla()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpecularLighting/lighting-color=hsla.svg");
	}

	@Disabled
	@Test
	public void testFeSpecularLightingSpecularExponentEq0()
			throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feSpecularLighting/specularExponent=0.svg", 1);
	}

	@Disabled
	@Test
	public void testFeSpecularLightingSpecularExponentEq256()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpecularLighting/specularExponent=256.svg");
	}

	@Disabled
	@Test
	public void testFeSpecularLightingWithFeDistantLight() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpecularLighting/with-feDistantLight.svg");
	}

	@Disabled
	@Test
	public void testFeSpecularLightingWithFePointLight() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpecularLighting/with-fePointLight.svg");
	}

	@Disabled
	@Test
	public void testFeSpecularLightingWithFeSpotLightAndSpecularAndExponent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpecularLighting/with-feSpotLight-and-specular-and-exponent.svg");
	}

	@Disabled
	@Test
	public void testFeSpecularLightingWithFeSpotLightAndSpecularConstantEq5()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpecularLighting/with-feSpotLight-and-specularConstant=5.svg");
	}

	@Test
	public void testFeSpecularLightingWithFeSpotLight() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpecularLighting/with-feSpotLight.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/complex-transform.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightCustomAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/custom-attributes.svg");
	}

	@Test
	public void testFeSpotLightDefaultAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/default-attributes.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightLimitingConeAngleAntiAliasing()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/limitingConeAngle-anti-aliasing.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightLimitingConeAngleEqMinus30()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/limitingConeAngle=-30.svg");
	}

	@Test
	public void testFeSpotLightLimitingConeAngleEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/limitingConeAngle=0.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightLimitingConeAngleEq30() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/limitingConeAngle=30.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightPrimitiveUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/primitiveUnits=objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightSpecularExponentEqMinus10() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/specularExponent=-10.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightSpecularExponentEq0_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/specularExponent=0.5.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightSpecularExponentEq10() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/specularExponent=10.svg");
	}

	@Disabled
	@Test
	public void testFeSpotLightWithAllPointsAt() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feSpotLight/with-all-pointsAt.svg");
	}

	@Test
	public void testFeTileComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTile/complex-transform.svg");
	}

	@Test
	public void testFeTileEmptyRegion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTile/empty-region.svg");
	}

	@Test
	public void testFeTileSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTile/simple-case.svg");
	}

	@Test
	public void testFeTileWithRegion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTile/with-region.svg");
	}

	@Disabled
	@Test
	public void testFeTileWithSubregion_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTile/with-subregion-1.svg");
	}

	@Disabled
	@Test
	public void testFeTileWithSubregion_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTile/with-subregion-2.svg");
	}

	@Disabled
	@Test
	public void testFeTileWithSubregion_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTile/with-subregion-3.svg");
	}

	@Disabled
	@Test
	public void testFeTurbulenceBaseFrequencyEqMinus0_05() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feTurbulence/baseFrequency=-0.05.svg", 1);
	}

	@Test
	public void testFeTurbulenceBaseFrequencyEq0_01() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/baseFrequency=0.01.svg");
	}

	@Disabled
	@Test
	public void testFeTurbulenceBaseFrequencyEq0_05__0_01()
			throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feTurbulence/baseFrequency=0.05--0.01.svg", 1);
	}

	@Test
	public void testFeTurbulenceBaseFrequencyEq0_05_0_01() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/baseFrequency=0.05-0.01.svg");
	}

	@Test
	public void testFeTurbulenceBaseFrequencyEq0_05_0_05() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/baseFrequency=0.05-0.05.svg");
	}

	@Disabled
	@Test
	public void testFeTurbulenceBaseFrequencyEq0_05_0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/baseFrequency=0.05-0.svg");
	}

	@Test
	public void testFeTurbulenceColorInterpolationFiltersEqSRGB()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/color-interpolation-filters=sRGB.svg");
	}

	@Test
	public void testFeTurbulenceComplexTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/complex-transform.svg");
	}

	@Disabled
	@Test
	public void testFeTurbulenceNoAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/no-attributes.svg");
	}

	@Test
	public void testFeTurbulenceNumOctavesEqMinus1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/numOctaves=-1.svg");
	}

	@Test
	public void testFeTurbulenceNumOctavesEq0() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/numOctaves=0.svg");
	}

	@Test
	public void testFeTurbulenceNumOctavesEq5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/numOctaves=5.svg");
	}

	@Test
	public void testFeTurbulencePrimitiveUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/primitiveUnits=objectBoundingBox.svg");
	}

	@Test
	public void testFeTurbulenceSeedEqMinus20() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/seed=-20.svg");
	}

	@Test
	public void testFeTurbulenceSeedEq1_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/seed=1.5.svg");
	}

	@Test
	public void testFeTurbulenceSeedEq20() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/seed=20.svg");
	}

	@Test
	public void testFeTurbulenceStitchTilesEqStitch() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/stitchTiles=stitch.svg");
	}

	@Test
	public void testFeTurbulenceTypeEqFractalNoise() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/feTurbulence/type=fractalNoise.svg");
	}

	@Disabled
	@Test
	public void testFeTurbulenceTypeEqInvalid() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/feTurbulence/type=invalid.svg", 1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsBlurFunctionMmValue() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/blur-function-mm-value.svg");
	}

	@Test
	public void testFilterFunctionsBlurFunctionNegativeValue()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/blur-function-negative-value.svg", 1);
	}

	@Test
	public void testFilterFunctionsBlurFunctionNoValues() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter-functions/blur-function-no-values.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsBlurFunctionPercentValue()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/blur-function-percent-value.svg");
	}

	@Test
	public void testFilterFunctionsBlurFunctionTwoValues() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter-functions/blur-function-two-values.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsBlurFunction() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/blur-function.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsColorAdjustFunctions_0percent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/color-adjust-functions-0percent.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsColorAdjustFunctions_100percent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/color-adjust-functions-100percent.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsColorAdjustFunctions_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/color-adjust-functions-2.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsColorAdjustFunctions_200percent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/color-adjust-functions-200percent.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsColorAdjustFunctions_50percent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/color-adjust-functions-50percent.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsColorAdjustFunctionsDefaultValue()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/color-adjust-functions-default-value.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsColorAdjustFunctionsNegative()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/color-adjust-functions-negative.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionColorAsAttribute()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function-color-as-attribute.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionColorLast()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function-color-last.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionCommaSeparated()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/drop-shadow-function-comma-spearated.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionCurrentColor()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function-currentColor.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionEmValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function-em-values.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionExtraValue()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/drop-shadow-function-extra-value.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionFilterRegion()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function-filter-region.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionMmValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function-mm-values.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionNoColor()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/drop-shadow-function-no-color.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionNoValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/drop-shadow-function-no-values.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionOnly_XOffset()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function-only-X-offset.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionOnlyOffset()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/drop-shadow-function-only-offset.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunctionPercentValues()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/drop-shadow-function-percent-values.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsDropShadowFunction() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/drop-shadow-function.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsGrayscaleAndOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/grayscale-and-opacity.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunction_0_25turn()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-0.25turn.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunction_45() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-45.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunction_45deg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-45deg.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunction_45grad()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-45grad.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunction_45rad()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-45rad.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunction_999deg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-999deg.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunctionDefaultValue()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-default-value.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsHueRotateFunctionZero() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/hue-rotate-function-zero.svg");
	}

	@Test
	public void testFilterFunctionsNestedFilters() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/nested-filters.svg");
	}

	@Test
	public void testFilterFunctionsOneInvalidFunctionInList()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/filters/filter-functions/one-invalid-function-in-list.svg", 1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsOneInvalidUrlInList() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter-functions/one-invalid-url-in-list.svg",
				1);
	}

	@Disabled
	@Test
	public void testFilterFunctionsTwoDropShadowFunction() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/two-drop-shadow-function.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsTwoExactUrls() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/two-exact-urls.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsTwoUrls() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/two-urls.svg");
	}

	@Disabled
	@Test
	public void testFilterFunctionsUrlAndGrayscale() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter-functions/url-and-grayscale.svg");
	}

	@Test
	public void testFilterColorInterpolationFiltersEqSRGB()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/color-interpolation-filters=sRGB.svg");
	}

	@Test
	public void testFilterComplexOrderAndXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/complex-order-and-xlink-href.svg");
	}

	@Test
	public void testFilterContentOutsideTheCanvas_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/content-outside-the-canvas-2.svg");
	}

	@Test
	public void testFilterContentOutsideTheCanvas() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/content-outside-the-canvas.svg");
	}

	@Test
	public void testFilterDefaultColorInterpolationFilters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/default-color-interpolation-filters.svg");
	}

	@Test
	public void testFilterEverythingViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/everything-via-xlink-href.svg");
	}

	@Test
	public void testFilterGlobalTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/global-transform.svg");
	}

	@Test
	public void testFilterHugeRegion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/huge-region.svg");
	}

	@Disabled
	@Test
	public void testFilterInToInvalid_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in-to-invalid-1.svg");
	}

	@Disabled
	@Test
	public void testFilterInToInvalid_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in-to-invalid-2.svg");
	}

	@Test
	public void testFilterInEqBackgroundAlphaWithEnableBackground()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=BackgroundAlpha-with-enable-background.svg");
	}

	@Disabled
	@Test
	public void testFilterInEqBackgroundAlpha() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=BackgroundAlpha.svg");
	}

	@Test
	public void testFilterInEqBackgroundImageWithEnableBackground()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=BackgroundImage-with-enable-background.svg");
	}

	@Disabled
	@Test
	public void testFilterInEqBackgroundImage() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=BackgroundImage.svg");
	}

	@Test
	public void testFilterInEqFillPaintOnGWithoutChildren()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=FillPaint-on-g-without-children.svg");
	}

	@Test
	public void testFilterInEqFillPaintWithGradient() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=FillPaint-with-gradient.svg");
	}

	@Test
	public void testFilterInEqFillPaintWithPattern() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=FillPaint-with-pattern.svg");
	}

	@Test
	public void testFilterInEqFillPaintWithTargetOnG() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=FillPaint-with-target-on-g.svg");
	}

	@Test
	public void testFilterInEqFillPaint() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=FillPaint.svg");
	}

	@Test
	public void testFilterInEqSourceAlpha() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=SourceAlpha.svg");
	}

	@Test
	public void testFilterInEqStrokePaint() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/in=StrokePaint.svg");
	}

	@Test
	public void testFilterInitialTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/initial-transform.svg");
	}

	@Disabled
	@Test
	public void testFilterInvalid_FuncIRI() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/invalid-FuncIRI.svg", 1);
	}

	@Test
	public void testFilterInvalidFilterUnits() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/invalid-filterUnits.svg", 1);
	}

	@Disabled
	@Test
	public void testFilterInvalidPrimitive_1() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/invalid-primitive-1.svg", 1);
	}

	@Disabled
	@Test
	public void testFilterInvalidPrimitive_2() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/invalid-primitive-2.svg", 1);
	}

	@Disabled
	@Test
	public void testFilterInvalidRegion() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/invalid-region.svg", 1);
	}

	@Disabled
	@Test
	public void testFilterInvalidSubregion() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/invalid-subregion.svg", 1);
	}

	@Disabled
	@Test
	public void testFilterInvalidXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/invalid-xlink-href.svg");
	}

	@Test
	public void testFilterMultiplePrimitives_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/multiple-primitives-1.svg");
	}

	@Test
	public void testFilterMultiplePrimitives_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/multiple-primitives-2.svg");
	}

	@Test
	public void testFilterMultiplePrimitives_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/multiple-primitives-3.svg");
	}

	@Test
	public void testFilterMultiplePrimitives_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/multiple-primitives-4.svg");
	}

	@Disabled
	@Test
	public void testFilterNegativeSubregion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/negative-subregion.svg");
	}

	@Disabled
	@Test
	public void testFilterNoChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/no-children.svg");
	}

	@Test
	public void testFilterNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/none.svg");
	}

	@Test
	public void testFilterOnAThinRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/on-a-thin-rect.svg");
	}

	@Test
	public void testFilterOnAVerticalLine() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/on-a-vertical-line.svg");
	}

	@Test
	public void testFilterOnAnEmptyGroup_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/on-an-empty-group-1.svg");
	}

	@Test
	public void testFilterOnAnEmptyGroup_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/on-an-empty-group-2.svg");
	}

	@Test
	public void testFilterOnGroupWithChildOutsideOfCanvas()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/on-group-with-child-outside-of-canvas.svg");
	}

	@Test
	public void testFilterOnTheRootSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/on-the-root-svg.svg");
	}

	@Test
	public void testFilterOnZeroSizedShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/on-zero-sized-shape.svg");
	}

	@Disabled
	@Test
	public void testFilterPathBbox() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/path-bbox.svg");
	}

	@Disabled
	@Test
	public void testFilterPrimitiveUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/primitiveUnits=objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testFilterRecursiveXlinkHref() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/recursive-xlink-href.svg", 1);
	}

	@Test
	public void testFilterRegionWithStroke() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/region-with-stroke.svg");
	}

	@Disabled
	@Test
	public void testFilterSelfRecursiveXlinkHref() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/filters/filter/self-recursive-xlink-href.svg", 1);
	}

	@Test
	public void testFilterSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/simple-case.svg");
	}

	@Test
	public void testFilterSomeAttributesViaXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/some-attributes-via-xlink-href.svg");
	}

	@Disabled
	@Test
	public void testFilterSubregionAndPrimitiveUnitsEqObjectBoundingBox_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/subregion-and-primitiveUnits=objectBoundingBox-1.svg");
	}

	@Disabled
	@Test
	public void testFilterSubregionAndPrimitiveUnitsEqObjectBoundingBox_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/subregion-and-primitiveUnits=objectBoundingBox-2.svg");
	}

	@Test
	public void testFilterSubregionBiggerThatRegion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/subregion-bigger-that-region.svg");
	}

	@Test
	public void testFilterTransformOnFilter() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/transform-on-filter.svg");
	}

	@Test
	public void testFilterTransformOnShapeWithFilterRegion()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/transform-on-shape-with-filter-region.svg");
	}

	@Test
	public void testFilterTransformOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/transform-on-shape.svg");
	}

	@Test
	public void testFilterUnresolvedXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/unresolved-xlink-href.svg");
	}

	@Disabled
	@Test
	public void testFilterWithClipPathAndMask() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-clip-path-and-mask.svg");
	}

	@Test
	public void testFilterWithClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-clip-path.svg");
	}

	@Test
	public void testFilterWithMaskOnParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-mask-on-parent.svg");
	}

	@Test
	public void testFilterWithMask() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-mask.svg");
	}

	@Test
	public void testFilterWithMultipleTransforms_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-multiple-transforms-1.svg");
	}

	@Test
	public void testFilterWithMultipleTransforms_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-multiple-transforms-2.svg");
	}

	@Test
	public void testFilterWithRegionAndFilterUnitsEqUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-region-and-filterUnits=userSpaceOnUse.svg");
	}

	@Disabled
	@Test
	public void testFilterWithRegionAndSubregion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-region-and-subregion.svg");
	}

	@Test
	public void testFilterWithRegionOutsideTheCanvas() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-region-outside-the-canvas.svg");
	}

	@Test
	public void testFilterWithRegionOutsideTheViewbox() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-region-outside-the-viewbox.svg");
	}

	@Test
	public void testFilterWithRegion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-region.svg");
	}

	@Disabled
	@Test
	public void testFilterWithSubregion_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-subregion-1.svg");
	}

	@Disabled
	@Test
	public void testFilterWithSubregion_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-subregion-2.svg");
	}

	@Test
	public void testFilterWithSubregion_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-subregion-3.svg");
	}

	@Test
	public void testFilterWithTransformOutsideOfCanvas() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/with-transform-outside-of-canvas.svg");
	}

	@Test
	public void testFilterWithoutRegionAndFilterUnitsEqUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/without-region-and-filterUnits=userSpaceOnUse.svg");
	}

	@Test
	public void testFilterZeroSizedSubregion() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/filter/zero-sized-subregion.svg");
	}

	@Test
	public void testFloodColorHslaColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-color/hsla-color.svg");
	}

	@Test
	public void testFloodColorInheritance_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-color/inheritance-1.svg");
	}

	@Test
	public void testFloodColorInheritance_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-color/inheritance-2.svg");
	}

	@Test
	public void testFloodColorInheritance_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-color/inheritance-3.svg");
	}

	@Test
	public void testFloodColorInheritance_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-color/inheritance-4.svg");
	}

	@Test
	public void testFloodColorInheritance_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-color/inheritance-5.svg");
	}

	@Test
	public void testFloodColorSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-color/simple-case.svg");
	}

	@Disabled
	@Test
	public void testFloodOpacity50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-opacity/50percent.svg");
	}

	@Test
	public void testFloodOpacitySimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/filters/flood-opacity/simple-case.svg");
	}

}
