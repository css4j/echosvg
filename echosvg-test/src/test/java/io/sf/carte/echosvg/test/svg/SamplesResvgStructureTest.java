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
public class SamplesResvgStructureTest extends SamplesResvgRendering {

	/*
	 * a
	 */

	public void testStructureAInsideText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/inside-text.svg");
	}

	@Test
	public void testStructureAInsideTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/inside-tspan.svg");
	}

	@Test
	public void testStructureAOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/on-shape.svg");
	}

	@Test
	public void testStructureAOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/on-text.svg");
	}

	@Test
	public void testStructureAOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/a/on-tspan.svg");
	}

	/*
	 * defs
	 */

	@Test
	public void testStructureDefsIgnoreShapesInsideDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/ignore-shapes-inside-defs.svg");
	}

	@Test
	public void testStructureDefsMultipleDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/multiple-defs.svg");
	}

	@Test
	public void testStructureDefsNestedDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/nested-defs.svg");
	}

	@Test
	public void testStructureDefsOutOfOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/out-of-order.svg");
	}

	@Test
	public void testStructureDefsSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/simple-case.svg");
	}

	@Test
	public void testStructureDefsStyleInheritanceOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/style-inheritance-on-text.svg");
	}

	@Test
	public void testStructureDefsStyleInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/defs/style-inheritance.svg");
	}

	/*
	 * g
	 */

	@Test
	public void testStructureGDeeplyNestedGroups() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/g/deeply-nested-groups.svg");
	}

	@Disabled
	@Test
	public void testStructureGRecursiveInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/g/recursive-inheritance.svg");
	}

	/*
	 * image
	 */

	@Disabled
	@Test
	public void testStructureImageEmbedded_16bitPng() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-16bit-png.svg");
	}

	@Disabled
	@Test
	public void testStructureImageEmbeddedGif() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-gif.svg");
	}

	@Test
	public void testStructureImageEmbeddedJpegAsImageJpeg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-jpeg-as-image-jpeg.svg");
	}

	@Test
	public void testStructureImageEmbeddedJpegAsImageJpg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-jpeg-as-image-jpg.svg");
	}

	@Test
	public void testStructureImageEmbeddedJpegWithoutMime()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-jpeg-without-mime.svg");
	}

	@Test
	public void testStructureImageEmbeddedPng() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-png.svg");
	}

	@Test
	public void testStructureImageEmbeddedSvgWithText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svg-with-text.svg");
	}

	@Test
	public void testStructureImageEmbeddedSvgWithoutMime() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svg-without-mime.svg");
	}

	@Test
	public void testStructureImageEmbeddedSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svg.svg");
	}

	@Test
	public void testStructureImageEmbeddedSvgz() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/embedded-svgz.svg");
	}

	@Disabled
	@Test
	public void testStructureImageExternalGif() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-gif.svg");
	}

	@Test
	public void testStructureImageExternalJpeg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-jpeg.svg");
	}

	@Test
	public void testStructureImageExternalPng() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-png.svg");
	}

	@Test
	public void testStructureImageExternalSvgWithTransform()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-svg-with-transform.svg");
	}

	@Test
	public void testStructureImageExternalSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-svg.svg");
	}

	@Test
	public void testStructureImageExternalSvgz() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/external-svgz.svg");
	}

	@Test
	public void testStructureImageFloatSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/float-size.svg");
	}

	@Test
	public void testStructureImageImageWithFloatSizeScaling()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/image-with-float-size-scaling.svg");
	}

	@Disabled
	@Test
	public void testStructureImageNoHeightNonSquare() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/image/no-height-non-square.svg",
				BROWSER_MEDIA, 1);
	}

	@Disabled
	@Test
	public void testStructureImageNoHeightOnSvg() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/image/no-height-on-svg.svg", 1);
	}

	@Disabled
	@Test
	public void testStructureImageNoHeight() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/image/no-height.svg", 1);
	}

	@Disabled
	@Test
	public void testStructureImageNoWidthAndHeightOnSvg() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/image/no-width-and-height-on-svg.svg", 1);
	}

	@Disabled
	@Test
	public void testStructureImageNoWidthAndHeight() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/image/no-width-and-height.svg", 1);
	}

	@Disabled
	@Test
	public void testStructureImageNoWidthOnSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/no-width-on-svg.svg");
	}

	@Disabled
	@Test
	public void testStructureImageNoWidth() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/image/no-width.svg", 1);
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqNoneOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=none-on-svg.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqNone()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=none.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMaxYMaxMeetOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-meet-on-svg.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMaxYMaxMeet()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-meet.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMaxYMaxSliceOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-slice-on-svg.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMaxYMaxSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMaxYMax-slice.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMidYMidMeetOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-meet-on-svg.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMidYMidMeet()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-meet.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMidYMidSliceOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-slice-on-svg.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMidYMidSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMidYMid-slice.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMinYMinMeetOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-meet-on-svg.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMinYMinMeet()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-meet.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMinYMinSliceOnSvg()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-slice-on-svg.svg");
	}

	@Test
	public void testStructureImagePreserveAspectRatioEqXMinYMinSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/preserveAspectRatio=xMinYMin-slice.svg");
	}

	@Test
	public void testStructureImageRasterImageAndSizeWithOddNumbers()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/raster-image-and-size-with-odd-numbers.svg");
	}

	@Disabled
	@Test
	public void testStructureImageRecursive_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/recursive-1.svg");
	}

	@Disabled
	@Test
	public void testStructureImageRecursive_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/recursive-2.svg");
	}

	@Test
	public void testStructureImageUrlToPng() throws TranscoderException, IOException {
		testAllowExternal("samples/resvg/tests/structure/image/url-to-png.svg", false);
	}

	@Test
	public void testStructureImageUrlToSvg() throws TranscoderException, IOException {
		testAllowExternal("samples/resvg/tests/structure/image/url-to-svg.svg", false);
	}

	// Enable when geometry properties are supported
	@Disabled
	@Test
	public void testStructureImageWidthAndHeightSetToAuto()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/width-and-height-set-to-auto.svg");
	}

	@Test
	public void testStructureImageWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/with-transform.svg");
	}

	@Test
	public void testStructureImageWithZeroWidthAndHeight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/with-zero-width-and-height.svg");
	}

	@Test
	public void testStructureImageZeroHeight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/zero-height.svg");
	}

	@Test
	public void testStructureImageZeroWidth() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/image/zero-width.svg");
	}

	/*
	 * style-attribute
	 */

	@Test
	public void testStructureStyleAttributeComments() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/comments.svg");
	}

	@Test
	public void testStructureStyleAttributeNonPresentationalAttribute()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/non-presentational-attribute.svg");
	}

	@Test
	public void testStructureStyleAttributeSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/simple-case.svg");
	}

	@Disabled
	@Test
	public void testStructureStyleAttributeTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style-attribute/transform.svg");
	}

	/*
	 * style
	 */

	@Test
	public void testStructureStyleAttributeSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/attribute-selector.svg");
	}

	@Test
	public void testStructureStyleClassSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/class-selector.svg");
	}

	@Test
	public void testStructureStyleCombinedSelectors() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/combined-selectors.svg");
	}

	@Test
	public void testStructureStyleExternal_CSS() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/external-CSS.svg");
	}

	@Test
	public void testStructureStyleIDSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/iD-selector.svg");
	}

	@Test
	public void testStructureStyleImportant() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/important.svg");
	}

	@Test
	public void testStructureStyleInvalidType() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/invalid-type.svg");
	}

	@Test
	public void testStructureStyleNonPresentationalAttribute()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/non-presentational-attribute.svg");
	}

	@Test
	public void testStructureStyleResolveOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/resolve-order.svg");
	}

	@Test
	public void testStructureStyleRuleSpecificity() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/rule-specificity.svg");
	}

	@Test
	public void testStructureStyleStyleAfterUsage() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/style-after-usage.svg");
	}

	@Test
	public void testStructureStyleStyleInside_CDATA() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/style-inside-CDATA.svg");
	}

	@Disabled
	@Test
	public void testStructureStyleTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/transform.svg");
	}

	@Test
	public void testStructureStyleTypeSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/type-selector.svg");
	}

	@Test
	public void testStructureStyleUniversalSelector() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/universal-selector.svg");
	}

	@Test
	public void testStructureStyleUnresolvedClassSelector()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/style/unresolved-class-selector.svg");
	}

	/*
	 * svg
	 */

	@Test
	public void testStructureSvgAttributeValueVia_ENTITYReference()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/attribute-value-via-ENTITY-reference.svg");
	}

	@Test
	public void testStructureSvgDeeplyNestedSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/deeply-nested-svg.svg");
	}

	@Test
	public void testStructureSvgElementsVia_ENTITYReference_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/elements-via-ENTITY-reference-1.svg");
	}

	@Test
	public void testStructureSvgElementsVia_ENTITYReference_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/elements-via-ENTITY-reference-2.svg");
	}

	@Test
	public void testStructureSvgElementsVia_ENTITYReference_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/elements-via-ENTITY-reference-3.svg");
	}

	@Test
	public void testStructureSvgExplicitSvgNamespace() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/explicit-svg-namespace.svg");
	}

	@Disabled
	@Test
	public void testStructureSvgFuncIRIParsing() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/funcIRI-parsing.svg");
	}

	@Test
	public void testStructureSvgFuncIRIWithInvalidCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/funcIRI-with-invalid-characters.svg");
	}

	@Test
	public void testStructureSvgFuncIRIWithQuotes() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/funcIRI-with-quotes.svg");
	}

	@Test
	public void testStructureSvgInvalidIdAttribute_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/invalid-id-attribute-1.svg");
	}

	@Test
	public void testStructureSvgInvalidIdAttribute_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/invalid-id-attribute-2.svg");
	}

	@Test
	public void testStructureSvgMixedNamespaces() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/mixed-namespaces.svg");
	}

	@Test
	public void testStructureSvgNegativeSize() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/svg/negative-size.svg", 2);
	}

	@Test
	public void testStructureSvgNestedSvgOneWithRectAndOneWithViewBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-one-with-rect-and-one-with-viewBox.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithOverflowAuto()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-overflow-auto.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithOverflowVisible()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-overflow-visible.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithRectAndPercentValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-percent-values.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithRectAndViewBox_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-1.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithRectAndViewBox_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-2.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithRectAndViewBox_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-3.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithRectAndViewBoxAndPercentValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect-and-viewBox-and-percent-values.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-rect.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithRelativeWidthAndHeight()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-relative-width-and-height.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithViewBoxAndPercentValues()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-viewBox-and-percent-values.svg");
	}

	@Test
	public void testStructureSvgNestedSvgWithViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg-with-viewBox.svg");
	}

	@Test
	public void testStructureSvgNestedSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/nested-svg.svg");
	}

	@Test
	public void testStructureSvgNoChildren() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/no-children.svg");
	}

	@Test
	public void testStructureSvgNoSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/no-size.svg");
	}

	@Disabled
	@Test
	public void testStructureSvgNot_UTF_8Encoding() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/not-UTF-8-encoding.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioWithViewBoxNotAtZeroPos()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio-with-viewBox-not-at-zero-pos.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioEqNone()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=none.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioEqXMaxYMaxSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMaxYMax-slice.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioEqXMaxYMax()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMaxYMax.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioEqXMidYMidSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMidYMid-slice.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioEqXMidYMid()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMidYMid.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioEqXMinYMinSlice()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMinYMin-slice.svg");
	}

	@Test
	public void testStructureSvgPreserveAspectRatioEqXMinYMin()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/preserveAspectRatio=xMinYMin.svg");
	}

	@Test
	public void testStructureSvgProportionalViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/proportional-viewBox.svg");
	}

	@Test
	public void testStructureSvgRectInsideANon_SVGElement()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/rect-inside-a-non-SVG-element.svg");
	}

	@Test
	public void testStructureSvgViewBoxNotAtZeroPos() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/viewBox-not-at-zero-pos.svg");
	}

	@Test
	public void testStructureSvgXmlnsValidation() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/xmlns-validation.svg");
	}

	@Disabled
	@Test
	public void testStructureSvgZeroSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/svg/zero-size.svg");
	}

	/*
	 * switch
	 */

	@Test
	public void testStructureSwitchCommentAsFirstChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/comment-as-first-child.svg");
	}

	@Disabled
	@Test
	public void testStructureSwitchDisplayNoneOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/display-none-on-child.svg");
	}

	@Test
	public void testStructureSwitchNon_SVGChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/non-SVG-child.svg");
	}

	@Test
	public void testStructureSwitchRequiredFeatures() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/requiredFeatures.svg");
	}

	@Test
	public void testStructureSwitchSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/simple-case.svg");
	}

	@Test
	public void testStructureSwitchSingleChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/single-child.svg");
	}

	@Test
	public void testStructureSwitchSystemLanguage() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage.svg");
	}

	@Test
	public void testStructureSwitchSystemLanguageEqEn_GB() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=en-GB.svg");
	}

	@Test
	public void testStructureSwitchSystemLanguageEqEn_US() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=en-US.svg");
	}

	@Test
	public void testStructureSwitchSystemLanguageEqEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=en.svg");
	}

	@Test
	public void testStructureSwitchSystemLanguageEqRu_Ru() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=ru-Ru.svg");
	}

	@Test
	public void testStructureSwitchSystemLanguageEqRuEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/systemLanguage=ru-en.svg");
	}

	@Test
	public void testStructureSwitchWithAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/switch/with-attributes.svg");
	}

	/*
	 * symbol
	 */

	@Test
	public void testStructureSymbolContentOutsideTheViewbox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/content-outside-the-viewbox.svg");
	}

	@Test
	public void testStructureSymbolIndirectSymbolReference()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/indirect-symbol-reference.svg");
	}

	@Test
	public void testStructureSymbolOpacityOnSymbolWithViewBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-symbol-with-viewBox.svg");
	}

	@Test
	public void testStructureSymbolOpacityOnSymbol() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-symbol.svg");
	}

	@Test
	public void testStructureSymbolOpacityOnUseAndSymbol() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-use-and-symbol.svg");
	}

	@Test
	public void testStructureSymbolOpacityOnUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/opacity-on-use.svg");
	}

	@Test
	public void testStructureSymbolSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/simple-case.svg");
	}

	@Test
	public void testStructureSymbolUnusedSymbol() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/unused-symbol.svg");
	}

	@Test
	public void testStructureSymbolWithCustomUseSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-custom-use-size.svg");
	}

	@Test
	public void testStructureSymbolWithOverflowVisible() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-overflow-visible.svg");
	}

	@Test
	public void testStructureSymbolWithTransformOnUseNoSize()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-transform-on-use-no-size.svg");
	}

	@Test
	public void testStructureSymbolWithTransformOnUse() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-transform-on-use.svg");
	}

	@Test
	public void testStructureSymbolWithTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-transform.svg");
	}

	@Test
	public void testStructureSymbolWithViewBoxAndCustomUseRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-viewBox-and-custom-use-rect.svg");
	}

	@Test
	public void testStructureSymbolWithViewBoxAndCustomUseSize()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-viewBox-and-custom-use-size.svg");
	}

	@Test
	public void testStructureSymbolWithViewBox() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/symbol/with-viewBox.svg");
	}

	/*
	 * systemLanguage
	 */

	@Test
	public void testStructureSystemLanguageEn_GB() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/en-GB.svg");
	}

	@Test
	public void testStructureSystemLanguageEn_US() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/en-US.svg");
	}

	@Test
	public void testStructureSystemLanguageEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/en.svg");
	}

	@Test
	public void testStructureSystemLanguageOnClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-clipPath.svg");
	}

	@Test
	public void testStructureSystemLanguageOnDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-defs.svg");
	}

	@Test
	public void testStructureSystemLanguageOnLinearGradient()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-linearGradient.svg");
	}

	@Disabled
	@Test
	public void testStructureSystemLanguageOnSvg() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-svg.svg");
	}

	@Test
	public void testStructureSystemLanguageOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/on-tspan.svg");
	}

	@Test
	public void testStructureSystemLanguageRu_Ru() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/ru-Ru.svg");
	}

	@Test
	public void testStructureSystemLanguageRuEn() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/systemLanguage/ru-en.svg");
	}

	/*
	 * transform-origin
	 */

	@Disabled
	@Test
	public void testStructureTransformOriginBottom() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/bottom.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginCenter() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/center.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginKeywordLength()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/keyword-length.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginLeft() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/left.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginLengthPercent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/length-percent.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginLengthPx() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/length-px.svg");
	}

	@Test
	public void testStructureTransformOriginNoTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/no-transform.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnClippathObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-clippath-objectBoundingBox.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnClippath() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-clippath.svg");
	}

	@Test
	public void testStructureTransformOriginOnGradientObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-gradient-object-bounding-box.svg");
	}

	@Test
	public void testStructureTransformOriginOnGradientUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-gradient-user-space-on-use.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnGroup() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-group.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnImage() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-image.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnPatternObjectBoundingBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-pattern-object-bounding-box.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnPatternUserSpaceOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-pattern-user-space-on-use.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnShape() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-shape.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnTextPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-text-path.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/on-text.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginRightBottom() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/right-bottom.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginRight() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/right.svg");
	}

	@Test
	public void testStructureTransformOriginTopLeft() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/top-left.svg");
	}

	@Disabled
	@Test
	public void testStructureTransformOriginTop() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/top.svg");
	}

	@Test
	public void testStructureTransformOriginTransformOnParent()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform-origin/transform-on-parent.svg");
	}

	/*
	 * transform
	 */

	@Test
	public void testStructureTransformDefault() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/default.svg");
	}

	@Test
	public void testStructureTransformDirectTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/direct-transform.svg");
	}

	@Test
	public void testStructureTransformEmpty() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/empty.svg");
	}

	@Test
	public void testStructureTransformExtraSpaces() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/extra-spaces.svg");
	}

	@Test
	public void testStructureTransformMatrixNoCommas() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/matrix-no-commas.svg");
	}

	@Test
	public void testStructureTransformMatrix() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/matrix.svg");
	}

	@Test
	public void testStructureTransformNestedTransforms_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/nested-transforms-1.svg");
	}

	@Test
	public void testStructureTransformNestedTransforms_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/nested-transforms-2.svg");
	}

	@Test
	public void testStructureTransformNumericCharacterReferences()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/numeric-character-references.svg");
	}

	@Test
	public void testStructureTransformRotateAtPosition() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/rotate-at-position.svg");
	}

	@Test
	public void testStructureTransformRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/rotate.svg");
	}

	@Test
	public void testStructureTransformScaleWithout_Y() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/scale-without-Y.svg");
	}

	@Test
	public void testStructureTransformScale() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/scale.svg");
	}

	@Test
	public void testStructureTransformSkewX() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/skewX.svg");
	}

	@Test
	public void testStructureTransformSkewY() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/skewY.svg");
	}

	@Test
	public void testStructureTransformTransformList() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/transform-list.svg");
	}

	@Test
	public void testStructureTransformTranslateWithout_Y() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/translate-without-Y.svg");
	}

	@Test
	public void testStructureTransformTranslate() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/translate.svg");
	}

	@Test
	public void testStructureTransformZeroedMatrix() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/transform/zeroed-matrix.svg");
	}

	/*
	 * use
	 */

	@Test
	public void testStructureUseCSSRules() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/cSS-rules.svg");
	}

	@Test
	public void testStructureUseComplexStyleResolvingOrder()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/complex-style-resolving-order.svg");
	}

	@Test
	public void testStructureUseDisplayInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/display-inheritance.svg");
	}

	@Test
	public void testStructureUseDuplicated_IDs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/duplicated-IDs.svg");
	}

	@Test
	public void testStructureUseFillOpacityInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/fill-opacity-inheritance.svg");
	}

	@Test
	public void testStructureUseFromDefs() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/from-defs.svg");
	}

	@Test
	public void testStructureUseHrefWithoutTheXlinkNamespace()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/href-without-the-xlink-namespace.svg");
	}

	@Disabled
	@Test
	public void testStructureUseIndirectRecursive_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/indirect-recursive-1.svg");
	}

	@Disabled
	@Test
	public void testStructureUseIndirectRecursive_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/indirect-recursive-2.svg");
	}

	@Disabled
	@Test
	public void testStructureUseIndirectRecursive_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/indirect-recursive-3.svg");
	}

	@Test
	public void testStructureUseIndirect() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/indirect.svg");
	}

	@Disabled
	@Test
	public void testStructureUseNestedRecursive_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/nested-recursive-1.svg");
	}

	@Disabled
	@Test
	public void testStructureUseNestedRecursive_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/nested-recursive-2.svg");
	}

	@Test
	public void testStructureUseNestedXlinkToSvgElementWithRectAndSize()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/nested-xlink-to-svg-element-with-rect-and-size.svg");
	}

	@Test
	public void testStructureUseNonLinearOrder() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/non-linear-order.svg");
	}

	@Test
	public void testStructureUseOpacityInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/opacity-inheritance.svg");
	}

	@Test
	public void testStructureUsePositionInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/position-inheritance.svg");
	}

	@Disabled
	@Test
	public void testStructureUseRecursive() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/recursive.svg");
	}

	@Test
	public void testStructureUseSelfRecursive() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/structure/use/self-recursive.svg", 1);
	}

	@Test
	public void testStructureUseSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/simple-case.svg");
	}

	@Test
	public void testStructureUseStrokeOpacityInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/stroke-opacity-inheritance.svg");
	}

	@Test
	public void testStructureUseStyleInheritance_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/style-inheritance-1.svg");
	}

	@Test
	public void testStructureUseStyleInheritance_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/style-inheritance-2.svg");
	}

	@Test
	public void testStructureUseStyleInheritance_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/style-inheritance-3.svg");
	}

	@Test
	public void testStructureUseTransformAttribute_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/transform-attribute-1.svg");
	}

	@Test
	public void testStructureUseTransformAttribute_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/transform-attribute-2.svg");
	}

	@Test
	public void testStructureUseTransformInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/transform-inheritance.svg");
	}

	@Test
	public void testStructureUseWithCurrentColor() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/with-currentColor.svg");
	}

	@Test
	public void testStructureUseWithSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/with-size.svg");
	}

	@Test
	public void testStructureUseXlinkToAChildOfANon_SVGElement()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-a-child-of-a-non-SVG-element.svg");
	}

	@Test
	public void testStructureUseXlinkToAChildOfAnInvalidElement()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-a-child-of-an-invalid-element.svg");
	}

	@Test
	public void testStructureUseXlinkToAnExternalFile() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-external-file.svg");
	}

	@Test
	public void testStructureUseXlinkToAnInvalidElement_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-invalid-element-1.svg");
	}

	@Test
	public void testStructureUseXlinkToAnInvalidElement_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-invalid-element-2.svg");
	}

	@Test
	public void testStructureUseXlinkToAnInvalidElement_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-an-invalid-element-3.svg");
	}

	@Test
	public void testStructureUseXlinkToSvgElementWithRectOnlyWidth()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-rect-only-width.svg");
	}

	@Test
	public void testStructureUseXlinkToSvgElementWithRect()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-rect.svg");
	}

	@Test
	public void testStructureUseXlinkToSvgElementWithViewBox()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-viewBox.svg");
	}

	@Test
	public void testStructureUseXlinkToSvgElementWithWidthHeightOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-width-height-on-use.svg");
	}

	@Test
	public void testStructureUseXlinkToSvgElementWithXYOnUse()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element-with-x-y-on-use.svg");
	}

	@Test
	public void testStructureUseXlinkToSvgElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/structure/use/xlink-to-svg-element.svg");
	}

}
