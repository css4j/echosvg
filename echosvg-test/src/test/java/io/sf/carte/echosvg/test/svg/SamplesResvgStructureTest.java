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
public class SamplesResvgStructureTest extends SamplesResvgRendering {

	/*
	 * a
	 */

	public void testAInsideText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/inside-text.svg");
	}

	@Test
	public void testAInsideTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/inside-tspan.svg");
	}

	@Test
	public void testAOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/on-shape.svg");
	}

	@Test
	public void testAOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/on-text.svg");
	}

	@Test
	public void testAOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/on-tspan.svg");
	}

	/*
	 * defs
	 */

	@Test
	public void testDefsIgnoreShapesInsideDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/ignore-shapes-inside-defs.svg");
	}

	@Test
	public void testDefsMultipleDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/multiple-defs.svg");
	}

	@Test
	public void testDefsNestedDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/nested-defs.svg");
	}

	@Test
	public void testDefsOutOfOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/out-of-order.svg");
	}

	@Test
	public void testDefsSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/simple-case.svg");
	}

	@Test
	public void testDefsStyleInheritanceOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/style-inheritance-on-text.svg");
	}

	@Test
	public void testDefsStyleInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/style-inheritance.svg");
	}

	/*
	 * g
	 */

	@Test
	public void testGDeeplyNestedGroups() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/g/deeply-nested-groups.svg");
	}

	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testGRecursiveInheritance() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/g/recursive-inheritance.svg", 1);
	}

	/*
	 * image
	 */

	@Disabled
	@Test
	public void testImageEmbedded_16bitPng() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-16bit-png.svg");
	}

	@Disabled
	@Test
	public void testImageEmbeddedGif() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-gif.svg");
	}

	@Test
	public void testImageEmbeddedJpegAsImageJpeg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-jpeg-as-image-jpeg.svg");
	}

	@Test
	public void testImageEmbeddedJpegAsImageJpg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-jpeg-as-image-jpg.svg");
	}

	@Test
	public void testImageEmbeddedJpegWithoutMime()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-jpeg-without-mime.svg");
	}

	@Test
	public void testImageEmbeddedPng() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-png.svg");
	}

	@Test
	public void testImageEmbeddedSvgWithText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svg-with-text.svg");
	}

	@Test
	public void testImageEmbeddedSvgWithoutMime() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svg-without-mime.svg");
	}

	@Test
	public void testImageEmbeddedSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svg.svg");
	}

	@Test
	public void testImageEmbeddedSvgz() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svgz.svg");
	}

	@Disabled
	@Test
	public void testImageExternalGif() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-gif.svg");
	}

	@Test
	public void testImageExternalJpeg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-jpeg.svg");
	}

	@Test
	public void testImageExternalPng() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-png.svg");
	}

	@Test
	public void testImageExternalSvgWithTransform()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-svg-with-transform.svg");
	}

	@Test
	public void testImageExternalSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-svg.svg");
	}

	@Test
	public void testImageExternalSvgz() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-svgz.svg");
	}

	@Test
	public void testImageFloatSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/float-size.svg");
	}

	@Test
	public void testImageImageWithFloatSizeScaling()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/image-with-float-size-scaling.svg");
	}

	@Test
	public void testImageNoHeightNonSquare() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-height-non-square.svg");
	}

	@Test
	public void testImageNoHeightOnSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-height-on-svg.svg");
	}

	@Test
	public void testImageNoHeight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-height.svg");
	}

	@Test
	public void testImageNoWidthAndHeightOnSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-width-and-height-on-svg.svg");
	}

	@Test
	public void testImageNoWidthAndHeight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-width-and-height.svg");
	}

	@Test
	public void testImageNoWidthOnSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-width-on-svg.svg");
	}

	@Test
	public void testImageNoWidth() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-width.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqNoneOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=none-on-svg.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqNone()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=none.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMaxYMaxMeetOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-meet-on-svg.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMaxYMaxMeet()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-meet.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMaxYMaxSliceOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-slice-on-svg.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMaxYMaxSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-slice.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMidYMidMeetOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-meet-on-svg.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMidYMidMeet()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-meet.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMidYMidSliceOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-slice-on-svg.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMidYMidSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-slice.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMinYMinMeetOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-meet-on-svg.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMinYMinMeet()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-meet.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMinYMinSliceOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-slice-on-svg.svg");
	}

	@Test
	public void testImagePreserveAspectRatioEqXMinYMinSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-slice.svg");
	}

	@Test
	public void testImageRasterImageAndSizeWithOddNumbers()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/raster-image-and-size-with-odd-numbers.svg");
	}

	@Disabled
	@Test
	public void testImageRecursive_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/recursive-1.svg");
	}

	@Disabled
	@Test
	public void testImageRecursive_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/recursive-2.svg");
	}

	@Test
	public void testImageUrlToPng() throws TranscoderException, IOException {
		testAllowExternal("samples/resvg/tests/structure/image/url-to-png.svg", false);
	}

	@Test
	public void testImageUrlToSvg() throws TranscoderException, IOException {
		testAllowExternal("samples/resvg/tests/structure/image/url-to-svg.svg", false);
	}

	@Test
	public void testImageWidthAndHeightSetToAuto()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/width-and-height-set-to-auto.svg");
	}

	@Test
	public void testImageWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/with-transform.svg");
	}

	@Test
	public void testImageWithZeroWidthAndHeight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/with-zero-width-and-height.svg");
	}

	@Test
	public void testImageZeroHeight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/zero-height.svg");
	}

	@Test
	public void testImageZeroWidth() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/zero-width.svg");
	}

	/*
	 * style-attribute
	 */

	@Test
	public void testStyleAttributeComments() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/comments.svg");
	}

	@Test
	public void testStyleAttributeNonPresentationalAttribute()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/non-presentational-attribute.svg");
	}

	@Test
	public void testStyleAttributeSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/simple-case.svg");
	}

	@Disabled
	@Test
	public void testStyleAttributeTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/transform.svg");
	}

	/*
	 * style
	 */

	@Test
	public void testStyleAttributeSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/attribute-selector.svg");
	}

	@Test
	public void testStyleClassSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/class-selector.svg");
	}

	@Test
	public void testStyleCombinedSelectors() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/combined-selectors.svg");
	}

	@Test
	public void testStyleExternal_CSS() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/external-CSS.svg");
	}

	@Test
	public void testStyleIDSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/iD-selector.svg");
	}

	@Test
	public void testStyleImportant() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/important.svg");
	}

	@Test
	public void testStyleInvalidType() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/invalid-type.svg");
	}

	@Test
	public void testStyleNonPresentationalAttribute()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/non-presentational-attribute.svg");
	}

	@Test
	public void testStyleResolveOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/resolve-order.svg");
	}

	@Test
	public void testStyleRuleSpecificity() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/rule-specificity.svg");
	}

	@Test
	public void testStyleStyleAfterUsage() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/style-after-usage.svg");
	}

	@Test
	public void testStyleStyleInside_CDATA() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/style-inside-CDATA.svg");
	}

	@Disabled
	@Test
	public void testStyleTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/transform.svg");
	}

	@Test
	public void testStyleTypeSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/type-selector.svg");
	}

	@Test
	public void testStyleUniversalSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/universal-selector.svg");
	}

	@Test
	public void testStyleUnresolvedClassSelector()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/unresolved-class-selector.svg");
	}

	/*
	 * svg
	 */

	@Test
	public void testSvgAttributeValueVia_ENTITYReference()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/attribute-value-via-ENTITY-reference.svg");
	}

	@Test
	public void testSvgDeeplyNestedSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/deeply-nested-svg.svg");
	}

	@Test
	public void testSvgElementsVia_ENTITYReference_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/elements-via-ENTITY-reference-1.svg");
	}

	@Test
	public void testSvgElementsVia_ENTITYReference_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/elements-via-ENTITY-reference-2.svg");
	}

	@Test
	public void testSvgElementsVia_ENTITYReference_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/elements-via-ENTITY-reference-3.svg");
	}

	@Test
	public void testSvgExplicitSvgNamespace() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/explicit-svg-namespace.svg");
	}

	@Disabled
	@Test
	public void testSvgFuncIRIParsing() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/funcIRI-parsing.svg");
	}

	@Test
	public void testSvgFuncIRIWithInvalidCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/funcIRI-with-invalid-characters.svg");
	}

	@Test
	public void testSvgFuncIRIWithQuotes() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/funcIRI-with-quotes.svg");
	}

	@Test
	public void testSvgInvalidIdAttribute_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/invalid-id-attribute-1.svg");
	}

	@Test
	public void testSvgInvalidIdAttribute_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/invalid-id-attribute-2.svg");
	}

	@Test
	public void testSvgMixedNamespaces() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/mixed-namespaces.svg");
	}

	@Test
	public void testSvgNegativeSize() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/svg/negative-size.svg", 2);
	}

	@Test
	public void testSvgNestedSvgOneWithRectAndOneWithViewBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-one-with-rect-and-one-with-viewBox.svg");
	}

	@Test
	public void testSvgNestedSvgWithOverflowAuto()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-overflow-auto.svg");
	}

	@Test
	public void testSvgNestedSvgWithOverflowVisible()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-overflow-visible.svg");
	}

	@Test
	public void testSvgNestedSvgWithRectAndPercentValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-percent-values.svg");
	}

	@Test
	public void testSvgNestedSvgWithRectAndViewBox_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-1.svg");
	}

	@Test
	public void testSvgNestedSvgWithRectAndViewBox_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-2.svg");
	}

	@Test
	public void testSvgNestedSvgWithRectAndViewBox_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-3.svg");
	}

	@Test
	public void testSvgNestedSvgWithRectAndViewBoxAndPercentValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-and-percent-values.svg");
	}

	@Test
	public void testSvgNestedSvgWithRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect.svg");
	}

	@Test
	public void testSvgNestedSvgWithRelativeWidthAndHeight()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-relative-width-and-height.svg");
	}

	@Test
	public void testSvgNestedSvgWithViewBoxAndPercentValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-viewBox-and-percent-values.svg");
	}

	@Test
	public void testSvgNestedSvgWithViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-viewBox.svg");
	}

	@Test
	public void testSvgNestedSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg.svg");
	}

	@Test
	public void testSvgNoChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/no-children.svg");
	}

	@Test
	public void testSvgNoSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/no-size.svg");
	}

	@Disabled
	@Test
	public void testSvgNot_UTF_8Encoding() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/not-UTF-8-encoding.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioWithViewBoxNotAtZeroPos()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio-with-viewBox-not-at-zero-pos.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioEqNone()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=none.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioEqXMaxYMaxSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMaxYMax-slice.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioEqXMaxYMax()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMaxYMax.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioEqXMidYMidSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMidYMid-slice.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioEqXMidYMid()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMidYMid.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioEqXMinYMinSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMinYMin-slice.svg");
	}

	@Test
	public void testSvgPreserveAspectRatioEqXMinYMin()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMinYMin.svg");
	}

	@Test
	public void testSvgProportionalViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/proportional-viewBox.svg");
	}

	@Test
	public void testSvgRectInsideANon_SVGElement()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/rect-inside-a-non-SVG-element.svg");
	}

	@Test
	public void testSvgViewBoxNotAtZeroPos() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/viewBox-not-at-zero-pos.svg");
	}

	@Test
	public void testSvgXmlnsValidation() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/xmlns-validation.svg");
	}

	@Disabled
	@Test
	public void testSvgZeroSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/zero-size.svg");
	}

	/*
	 * switch
	 */

	@Test
	public void testSwitchCommentAsFirstChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/comment-as-first-child.svg");
	}

	@Disabled
	@Test
	public void testSwitchDisplayNoneOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/display-none-on-child.svg");
	}

	@Test
	public void testSwitchNon_SVGChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/non-SVG-child.svg");
	}

	@Test
	public void testSwitchRequiredFeatures() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/requiredFeatures.svg");
	}

	@Test
	public void testSwitchSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/simple-case.svg");
	}

	@Test
	public void testSwitchSingleChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/single-child.svg");
	}

	@Test
	public void testSwitchSystemLanguage() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage.svg");
	}

	@Test
	public void testSwitchSystemLanguageEqEn_GB() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=en-GB.svg");
	}

	@Test
	public void testSwitchSystemLanguageEqEn_US() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=en-US.svg");
	}

	@Test
	public void testSwitchSystemLanguageEqEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=en.svg");
	}

	@Test
	public void testSwitchSystemLanguageEqRu_Ru() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=ru-Ru.svg");
	}

	@Test
	public void testSwitchSystemLanguageEqRuEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=ru-en.svg");
	}

	@Test
	public void testSwitchWithAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/with-attributes.svg");
	}

	/*
	 * symbol
	 */

	@Test
	public void testSymbolContentOutsideTheViewbox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/content-outside-the-viewbox.svg");
	}

	@Test
	public void testSymbolIndirectSymbolReference()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/indirect-symbol-reference.svg");
	}

	@Test
	public void testSymbolOpacityOnSymbolWithViewBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-symbol-with-viewBox.svg");
	}

	@Test
	public void testSymbolOpacityOnSymbol() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-symbol.svg");
	}

	@Test
	public void testSymbolOpacityOnUseAndSymbol() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-use-and-symbol.svg");
	}

	@Test
	public void testSymbolOpacityOnUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-use.svg");
	}

	@Test
	public void testSymbolSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/simple-case.svg");
	}

	@Test
	public void testSymbolUnusedSymbol() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/unused-symbol.svg");
	}

	@Test
	public void testSymbolWithCustomUseSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-custom-use-size.svg");
	}

	@Test
	public void testSymbolWithOverflowVisible() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-overflow-visible.svg");
	}

	@Test
	public void testSymbolWithTransformOnUseNoSize()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-transform-on-use-no-size.svg");
	}

	@Test
	public void testSymbolWithTransformOnUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-transform-on-use.svg");
	}

	@Test
	public void testSymbolWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-transform.svg");
	}

	@Test
	public void testSymbolWithViewBoxAndCustomUseRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-viewBox-and-custom-use-rect.svg");
	}

	@Test
	public void testSymbolWithViewBoxAndCustomUseSize()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-viewBox-and-custom-use-size.svg");
	}

	@Test
	public void testSymbolWithViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-viewBox.svg");
	}

	/*
	 * systemLanguage
	 */

	@Test
	public void testSystemLanguageEn_GB() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/en-GB.svg");
	}

	@Test
	public void testSystemLanguageEn_US() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/en-US.svg");
	}

	@Test
	public void testSystemLanguageEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/en.svg");
	}

	@Test
	public void testSystemLanguageOnClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-clipPath.svg");
	}

	@Test
	public void testSystemLanguageOnDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-defs.svg");
	}

	@Test
	public void testSystemLanguageOnLinearGradient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-linearGradient.svg");
	}

	@Disabled
	@Test
	public void testSystemLanguageOnSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-svg.svg");
	}

	@Test
	public void testSystemLanguageOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-tspan.svg");
	}

	@Test
	public void testSystemLanguageRu_Ru() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/ru-Ru.svg");
	}

	@Test
	public void testSystemLanguageRuEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/ru-en.svg");
	}

	/*
	 * transform-origin
	 */

	@Disabled
	@Test
	public void testTransformOriginBottom() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/bottom.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginCenter() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/center.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginKeywordLength()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/keyword-length.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginLeft() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/left.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginLengthPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/length-percent.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginLengthPx() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/length-px.svg");
	}

	@Test
	public void testTransformOriginNoTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/no-transform.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnClippathObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-clippath-objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnClippath() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-clippath.svg");
	}

	@Test
	public void testTransformOriginOnGradientObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-gradient-object-bounding-box.svg");
	}

	@Test
	public void testTransformOriginOnGradientUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-gradient-user-space-on-use.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnGroup() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-group.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnImage() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-image.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnPatternObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-pattern-object-bounding-box.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnPatternUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-pattern-user-space-on-use.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-shape.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnTextPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-text-path.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-text.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginRightBottom() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/right-bottom.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginRight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/right.svg");
	}

	@Test
	public void testTransformOriginTopLeft() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/top-left.svg");
	}

	@Disabled
	@Test
	public void testTransformOriginTop() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/top.svg");
	}

	@Test
	public void testTransformOriginTransformOnParent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/transform-on-parent.svg");
	}

	/*
	 * transform
	 */

	@Test
	public void testTransformDefault() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/default.svg");
	}

	@Test
	public void testTransformDirectTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/direct-transform.svg");
	}

	@Test
	public void testTransformEmpty() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/empty.svg");
	}

	@Test
	public void testTransformExtraSpaces() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/extra-spaces.svg");
	}

	@Test
	public void testTransformMatrixNoCommas() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/matrix-no-commas.svg");
	}

	@Test
	public void testTransformMatrix() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/matrix.svg");
	}

	@Test
	public void testTransformNestedTransforms_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/nested-transforms-1.svg");
	}

	@Test
	public void testTransformNestedTransforms_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/nested-transforms-2.svg");
	}

	@Test
	public void testTransformNumericCharacterReferences()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/numeric-character-references.svg");
	}

	@Test
	public void testTransformRotateAtPosition() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/rotate-at-position.svg");
	}

	@Test
	public void testTransformRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/rotate.svg");
	}

	@Test
	public void testTransformScaleWithout_Y() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/scale-without-Y.svg");
	}

	@Test
	public void testTransformScale() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/scale.svg");
	}

	@Test
	public void testTransformSkewX() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/skewX.svg");
	}

	@Test
	public void testTransformSkewY() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/skewY.svg");
	}

	@Test
	public void testTransformTransformList() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/transform-list.svg");
	}

	@Test
	public void testTransformTranslateWithout_Y() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/translate-without-Y.svg");
	}

	@Test
	public void testTransformTranslate() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/translate.svg");
	}

	@Test
	public void testTransformZeroedMatrix() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/zeroed-matrix.svg");
	}

	/*
	 * use
	 */

	@Test
	public void testUseCSSRules() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/cSS-rules.svg");
	}

	@Test
	public void testUseComplexStyleResolvingOrder()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/complex-style-resolving-order.svg");
	}

	@Test
	public void testUseDisplayInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/display-inheritance.svg");
	}

	@Test
	public void testUseDuplicated_IDs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/duplicated-IDs.svg");
	}

	@Test
	public void testUseFillOpacityInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/fill-opacity-inheritance.svg");
	}

	@Test
	public void testUseFromDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/from-defs.svg");
	}

	@Test
	public void testUseHrefWithoutTheXlinkNamespace()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/href-without-the-xlink-namespace.svg");
	}

	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testUseIndirectRecursive_1() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/indirect-recursive-1.svg", 2);
	}

	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testUseIndirectRecursive_2() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/indirect-recursive-2.svg", 1);
	}

	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	@Test
	public void testUseIndirectRecursive_3() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/indirect-recursive-3.svg", 2);
	}

	@Test
	public void testUseIndirect() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/indirect.svg");
	}

	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testUseNestedRecursive_1() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/nested-recursive-1.svg", 1);
	}

	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testUseNestedRecursive_2() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/nested-recursive-2.svg", 1);
	}

	@Test
	public void testUseNestedXlinkToSvgElementWithRectAndSize()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/nested-xlink-to-svg-element-with-rect-and-size.svg");
	}

	@Test
	public void testUseNonLinearOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/non-linear-order.svg");
	}

	@Test
	public void testUseOpacityInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/opacity-inheritance.svg");
	}

	@Test
	public void testUsePositionInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/position-inheritance.svg");
	}

	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testUseRecursive() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/recursive.svg", 2);
	}

	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testUseSelfRecursive() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/self-recursive.svg", 1);
	}

	@Test
	public void testUseSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/simple-case.svg");
	}

	@Test
	public void testUseStrokeOpacityInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/stroke-opacity-inheritance.svg");
	}

	@Test
	public void testUseStyleInheritance_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/style-inheritance-1.svg");
	}

	@Test
	public void testUseStyleInheritance_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/style-inheritance-2.svg");
	}

	@Test
	public void testUseStyleInheritance_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/style-inheritance-3.svg");
	}

	@Test
	public void testUseTransformAttribute_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/transform-attribute-1.svg");
	}

	@Test
	public void testUseTransformAttribute_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/transform-attribute-2.svg");
	}

	@Test
	public void testUseTransformInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/transform-inheritance.svg");
	}

	@Test
	public void testUseWithCurrentColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/with-currentColor.svg");
	}

	@Test
	public void testUseWithSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/with-size.svg");
	}

	@Test
	public void testUseXlinkToAChildOfANon_SVGElement()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-a-child-of-a-non-SVG-element.svg");
	}

	@Test
	public void testUseXlinkToAChildOfAnInvalidElement()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-a-child-of-an-invalid-element.svg");
	}

	@Test
	public void testUseXlinkToAnExternalFile() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-external-file.svg");
	}

	@Test
	public void testUseXlinkToAnInvalidElement_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-invalid-element-1.svg");
	}

	@Test
	public void testUseXlinkToAnInvalidElement_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-invalid-element-2.svg");
	}

	@Test
	public void testUseXlinkToAnInvalidElement_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-invalid-element-3.svg");
	}

	@Test
	public void testUseXlinkToSvgElementWithRectOnlyWidth()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-rect-only-width.svg");
	}

	@Test
	public void testUseXlinkToSvgElementWithRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-rect.svg");
	}

	@Test
	public void testUseXlinkToSvgElementWithViewBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-viewBox.svg");
	}

	@Test
	public void testUseXlinkToSvgElementWithWidthHeightOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-width-height-on-use.svg");
	}

	@Test
	public void testUseXlinkToSvgElementWithXYOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-x-y-on-use.svg");
	}

	@Test
	public void testUseXlinkToSvgElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element.svg");
	}

}
