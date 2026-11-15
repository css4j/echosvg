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
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

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
public class SamplesResvgMaskingTest extends SamplesResvgRendering {

	@Test
	public void testClipRuleClipRuleEqEvenodd() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clip-rule/clip-rule=evenodd.svg");
	}

	@Test
	public void testClipSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clip/simple-case.svg");
	}

	@Disabled
	@Test
	public void testClipPathCircleFunctionWithStrokeBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/circle-shorthand-with-stroke-box.svg");
	}

	@Disabled
	@Test
	public void testClipPathCircleFunctionWithViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/circle-shorthand-with-view-box.svg");
	}

	@Disabled
	@Test
	public void testClipPathCircleFunction() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/circle-shorthand.svg");
	}

	@Disabled
	@Test
	public void testClipPathClipPathOnChildWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-path-on-child-with-transform.svg");
	}

	@Disabled
	@Test
	public void testClipPathClipPathOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-path-on-child.svg");
	}

	@Disabled
	@Test
	public void testClipPathClipPathOnChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-path-on-children.svg");
	}

	@Disabled
	@Test
	public void testClipPathClipPathOnSelf_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-path-on-self-2.svg");
	}

	@Disabled
	@Test
	public void testClipPathClipPathOnSelf() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-path-on-self.svg");
	}

	@Test
	public void testClipPathClipPathWithTransformOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-path-with-transform-on-text.svg");
	}

	@Test
	public void testClipPathClipPathWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-path-with-transform.svg");
	}

	@Test
	public void testClipPathClipRuleFromParentNode() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-rule-from-parent-node.svg");
	}

	@Test
	public void testClipPathClipRuleEqEvenodd() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clip-rule=evenodd.svg");
	}

	@Test
	public void testClipPathClipPathUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clipPathUnits=objectBoundingBox.svg");
	}

	@Test
	public void testClipPathClippingWithComplexText_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clipping-with-complex-text-1.svg");
	}

	@Test
	public void testClipPathClippingWithComplexText_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clipping-with-complex-text-2.svg");
	}

	@Disabled
	@Test
	public void testClipPathClippingWithComplexTextAndClipRule()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clipping-with-complex-text-and-clip-rule.svg");
	}

	@Test
	public void testClipPathClippingWithText() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/clipping-with-text.svg");
	}

	@Test
	public void testClipPathFillHasNoEffect() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/fill-has-no-effect.svg");
	}

	@Test
	public void testClipPathFilterHasNoEffect() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/filter-has-no-effect.svg");
	}

	@Disabled
	@Test
	public void testClipPathGIsNotAValidChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/g-is-not-a-valid-child.svg");
	}

	@Disabled
	@Test
	public void testClipPathImageIsNotAValidChild() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/masking/clipPath/image-is-not-a-valid-child.svg", 1);
	}

	@Test
	public void testClipPathInvalid_FuncIRI() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/masking/clipPath/invalid-FuncIRI.svg", 1);
	}

	@Disabled
	@Test
	public void testClipPathInvalidClipPathOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/invalid-clip-path-on-child.svg");
	}

	@Disabled
	@Test
	public void testClipPathInvalidClipPathOnSelf() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/invalid-clip-path-on-self.svg");
	}

	@Test
	public void testClipPathInvalidTransformOnClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/invalid-transform-on-clipPath.svg");
	}

	@Disabled
	@Test
	public void testClipPathInvisibleChild_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/invisible-child-1.svg");
	}

	@Disabled
	@Test
	public void testClipPathInvisibleChild_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/invisible-child-2.svg");
	}

	@Test
	public void testClipPathLineIsNotAValidChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/line-is-not-a-valid-child.svg");
	}

	@Test
	public void testClipPathMalformedPathChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/malformed-path-child.svg");
	}

	@Test
	public void testClipPathMaskHasNoEffect() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/mask-has-no-effect.svg");
	}

	@Test
	public void testClipPathMixedClipRule() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/mixed-clip-rule.svg");
	}

	@Test
	public void testClipPathMultipleChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/multiple-children.svg");
	}

	@Test
	public void testClipPathNestedClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/nested-clip-path.svg");
	}

	@Disabled
	@Test
	public void testClipPathNoChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/no-children.svg");
	}

	@Test
	public void testClipPathNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/none.svg");
	}

	@Test
	public void testClipPathOnAHorizontalLine() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/on-a-horizontal-line.svg");
	}

	@Disabled
	@Test
	public void testClipPathOnTheRootSvgWithSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/on-the-root-svg-with-size.svg");
	}

	@Disabled
	@Test
	public void testClipPathOnTheRootSvgWithoutSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/on-the-root-svg-without-size.svg");
	}

	@Test
	public void testClipPathOpacityHasNoEffect() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/opacity-has-no-effect.svg");
	}

	@Test
	public void testClipPathOverlappedShapesWithEvenodd() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/overlapped-shapes-with-evenodd.svg");
	}

	@Disabled
	@Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testClipPathRecursiveOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/recursive-on-child.svg");
	}

	@Disabled
	@Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testClipPathRecursiveOnSelf() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/recursive-on-self.svg");
	}

	@Disabled
	@Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testClipPathRecursive() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/recursive.svg");
	}

	@Disabled
	@Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testClipPathSelfRecursive() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/self-recursive.svg");
	}

	@Test
	public void testClipPathSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/simple-case.svg");
	}

	@Test
	public void testClipPathStrokeHasNoEffect() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/stroke-has-no-effect.svg");
	}

	@Disabled
	@Test
	public void testClipPathSwitchIsNotAValidChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/switch-is-not-a-valid-child.svg");
	}

	@Disabled
	@Test
	public void testClipPathSymbolViaUseIsNotAValidChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/symbol-via-use-is-not-a-valid-child.svg");
	}

	@Disabled
	@Test
	public void testClipPathTransformOnClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/transform-on-clipPath.svg");
	}

	@Disabled
	@Test
	public void testClipPathWithInvalidChildViaUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/with-invalid-child-via-use.svg");
	}

	@Test
	public void testClipPathWithMarkerOnClip() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/with-marker-on-clip.svg");
	}

	@Test
	public void testClipPathWithUseChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/clipPath/with-use-child.svg");
	}

	@Test
	public void testMaskColorInterpolationEqLinearRGB() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/color-interpolation=linearRGB.svg");
	}

	@Test
	public void testMaskHalfWidthRegionWithRotation() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/half-width-region-with-rotation.svg");
	}

	@Test
	public void testMaskInvalid_FuncIRI() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/masking/mask/invalid-FuncIRI.svg", 1);
	}

	@Test
	public void testMaskInvalidChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/invalid-child.svg");
	}

	@Disabled
	@Test
	public void testMaskInvisibleChild_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/invisible-child-1.svg");
	}

	@Test
	public void testMaskInvisibleChild_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/invisible-child-2.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-on-child.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskOnSelfWithMaskTypeEqAlpha() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-on-self-with-mask-type=alpha.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskOnSelfWithMixedMaskType() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-on-self-with-mixed-mask-type.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskOnSelf() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-on-self.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskTypeInStyle() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-type-in-style.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskTypeEqAlpha() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-type=alpha.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskTypeEqInvalid() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-type=invalid.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskTypeEqLuminance() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/mask-type=luminance.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskContentUnitsEqObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/maskContentUnits=objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskUnitsEqObjectBoundingBoxWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/maskUnits=objectBoundingBox-with-percent.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskUnitsEqUserSpaceOnUseWithPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/maskUnits=userSpaceOnUse-with-percent.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskUnitsEqUserSpaceOnUseWithRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/maskUnits=userSpaceOnUse-with-rect.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskUnitsEqUserSpaceOnUseWithWidthOnly()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/maskUnits=userSpaceOnUse-with-width-only.svg");
	}

	@Disabled
	@Test
	public void testMaskMaskUnitsEqUserSpaceOnUseWithoutRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/maskUnits=userSpaceOnUse-without-rect.svg");
	}

	@Disabled
	@Test
	public void testMaskNestedObjectBoundingBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/nested-objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testMaskNoChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/no-children.svg");
	}

	@Test
	public void testMaskNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/none.svg");
	}

	@Test
	public void testMaskOnAHorizontalLine() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/on-a-horizontal-line.svg");
	}

	@Disabled
	@Test
	public void testMaskOnASmallObject() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/on-a-small-object.svg");
	}

	@Test
	public void testMaskOnGroupWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/on-group-with-transform.svg");
	}

	@Disabled
	@Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testMaskRecursiveOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/recursive-on-child.svg");
	}

	@Disabled
	@Test
	public void testMaskRecursiveOnSelf() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/recursive-on-self.svg");
	}

	@Disabled
	@Test
	public void testMaskRecursive() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/recursive.svg");
	}

	@Disabled
	@Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testMaskSelfRecursive() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/self-recursive.svg");
	}

	@Disabled
	@Test
	public void testMaskSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/simple-case.svg");
	}

	@Disabled
	@Test
	public void testMaskTransformHasNoEffect() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/transform-has-no-effect.svg");
	}

	@Disabled
	@Test
	public void testMaskTransformOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/transform-on-shape.svg");
	}

	@Disabled
	@Test
	public void testMaskWithClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/with-clip-path.svg");
	}

	@Disabled
	@Test
	public void testMaskWithGrayscaleImage() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/with-grayscale-image.svg");
	}

	@Disabled
	@Test
	public void testMaskWithImage() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/with-image.svg");
	}

	@Disabled
	@Test
	public void testMaskWithOpacity_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/with-opacity-1.svg");
	}

	@Disabled
	@Test
	public void testMaskWithOpacity_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/with-opacity-2.svg");
	}

	@Disabled
	@Test
	public void testMaskWithOpacity_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/masking/mask/with-opacity-3.svg");
	}

}
