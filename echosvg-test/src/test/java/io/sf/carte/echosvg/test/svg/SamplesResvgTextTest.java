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
public class SamplesResvgTextTest extends SamplesResvgRendering {

	/*
	 * alignment-baseline
	 */

	@Disabled
	@Test
	public void testAlignmentBaselineAfterEdge() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/after-edge.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineAlphabetic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/alphabetic.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineAuto() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/auto.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineBaseline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/baseline.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineBeforeEdge() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/before-edge.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineCentral() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/central.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineHangingAndBaselineShiftEq_20OnTspan()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/hanging-and-baseline-shift-eq-20-on-tspan.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineHangingOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/hanging-on-tspan.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineHangingOnVertical() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/hanging-on-vertical.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineHangingWithUnderline()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/hanging-with-underline.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineHanging() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/hanging.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineIdeographic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/ideographic.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/inherit.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineMathematical() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/mathematical.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineMiddleOnTextPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/middle-on-textPath.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineMiddle() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/middle.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineTextAfterEdge() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/text-after-edge.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineTextBeforeEdge() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/text-before-edge.svg");
	}

	@Disabled
	@Test
	public void testAlignmentBaselineTwoTextPathWithMiddleOnFirst()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/alignment-baseline/two-textPath-with-middle-on-first.svg");
	}

	/*
	 * baseline-shift
	 */

	@Test
	public void testBaselineShiftMinus10() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/-10.svg");
	}

	@Test
	public void testBaselineShiftMinus50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/-50percent.svg");
	}

	@Test
	public void testBaselineShift0() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/0.svg");
	}

	@Test
	public void testBaselineShift10() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/10.svg");
	}

	@Test
	public void testBaselineShift2mm() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/2mm.svg");
	}

	@Test
	public void testBaselineShift50percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/50percent.svg");
	}

	@Test
	public void testBaselineShiftBaseline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/baseline.svg");
	}

	@Disabled
	@Test
	public void testBaselineShiftDeeplyNestedSuper() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/deeply-nested-super.svg");
	}

	@Disabled
	@Test
	public void testBaselineShiftInheritance_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/inheritance-1.svg");
	}

	@Test
	public void testBaselineShiftInheritance_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/inheritance-2.svg");
	}

	@Disabled
	@Test
	public void testBaselineShiftInheritance_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/inheritance-3.svg");
	}

	@Test
	public void testBaselineShiftInheritance_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/inheritance-4.svg");
	}

	@Test
	public void testBaselineShiftInheritance_5() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/inheritance-5.svg");
	}

	@Test
	public void testBaselineShiftInvalidValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/baseline-shift/invalid-value.svg", 1);
	}

	@Disabled
	@Test
	public void testBaselineShiftMixedNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/mixed-nested.svg");
	}

	@Disabled
	@Test
	public void testBaselineShiftNestedLength() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/nested-length.svg");
	}

	@Disabled
	@Test
	public void testBaselineShiftNestedSuper() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/nested-super.svg");
	}

	@Disabled
	@Test
	public void testBaselineShiftNestedWithBaseline_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/nested-with-baseline-1.svg");
	}

	@Disabled
	@Test
	public void testBaselineShiftNestedWithBaseline_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/nested-with-baseline-2.svg");
	}

	@Test
	public void testBaselineShiftSub() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/sub.svg");
	}

	@Test
	public void testBaselineShiftSuper() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/super.svg");
	}

	@Test
	public void testBaselineShiftWithRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/baseline-shift/with-rotate.svg");
	}

	/*
	 * direction
	 */

	@Disabled
	@Test
	public void testDirectionRtlWithVerticalWritingMode() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/direction/rtl-with-vertical-writing-mode.svg");
	}

	@Disabled
	@Test
	public void testDirectionRtl() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/direction/rtl.svg");
	}

	/*
	 * dominant-baseline
	 */

	@Disabled
	@Test
	public void testDominantBaselineAlignmentBaselineAndBaselineShiftOnTspans()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/alignment-baseline-and-baseline-shift-on-tspans.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineAlignmentBaselineEqBaselineOnTspan()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/alignment-baseline=baseline-on-tspan.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineAlphabetic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/alphabetic.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineAuto() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/auto.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineCentral() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/central.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineComplex() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/complex.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineDifferentAlignmentBaselineOnTspan()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/different-alignment-baseline-on-tspan.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineDummyTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/dummy-tspan.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineEqualAlignmentBaselineOnTspan()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/equal-alignment-baseline-on-tspan.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineHanging() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/hanging.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineIdeographic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/ideographic.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/inherit.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineMathematical() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/mathematical.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineMiddle() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/middle.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/nested.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineNoChange() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/no-change.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineResetSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/reset-size.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineSequential() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/sequential.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineTextAfterEdge() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/text-after-edge.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineTextBeforeEdge() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/text-before-edge.svg");
	}

	@Disabled
	@Test
	public void testDominantBaselineUseScript() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/dominant-baseline/use-script.svg");
	}

	/*
	 * font-family
	 */

	@Test
	public void testFontFamilyBoldSansSerif() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/bold-sans-serif.svg");
	}

	@Disabled
	@Test
	public void testFontFamilyCursive() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/cursive.svg");
	}

	@Test
	public void testFontFamilyDoubleQuoted() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/double-quoted.svg");
	}

	@Test
	public void testFontFamilyFallback_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/fallback-1.svg");
	}

	@Test
	public void testFontFamilyFallback_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/fallback-2.svg");
	}

	@Disabled
	@Test
	public void testFontFamilyFantasy() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/fantasy.svg");
	}

	@Test
	public void testFontFamilyFontList() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/font-list.svg");
	}

	@Test
	public void testFontFamilyMonospace() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/monospace.svg");
	}

	@Test
	public void testFontFamilyNotoSans() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/noto-sans.svg");
	}

	@Test
	public void testFontFamilySansSerif() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/sans-serif.svg");
	}

	@Test
	public void testFontFamilySerif() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/serif.svg");
	}

	@Test
	public void testFontFamilySourceSansPro() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-family/source-sans-pro.svg");
	}

	/*
	 * font-kerning
	 */

	@Disabled
	@Test
	public void testFontKerningArabicScript() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-kerning/arabic-script.svg");
	}

	@Disabled
	@Test
	public void testFontKerningAsProperty() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-kerning/as-property.svg");
	}

	@Disabled
	@Test
	public void testFontKerningNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-kerning/none.svg");
	}

	/*
	 * font-size-adjust
	 */

	@Disabled
	@Test
	public void testFontSizeAdjustSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size-adjust/simple-case.svg");
	}

	/*
	 * font-size
	 */

	@Test
	public void testFontSizeEmNestedAndMixed() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/em-nested-and-mixed.svg");
	}

	@Test
	public void testFontSizeEmOnTheRootElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/em-on-the-root-element.svg");
	}

	@Test
	public void testFontSizeEm() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/em.svg");
	}

	@Test
	public void testFontSizeExNestedAndMixed() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/ex-nested-and-mixed.svg");
	}

	@Test
	public void testFontSizeExOnTheRootElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/ex-on-the-root-element.svg");
	}

	@Test
	public void testFontSizeEx() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/ex.svg");
	}

	@Test
	public void testFontSizeInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/inheritance.svg");
	}

	@Test
	public void testFontSizeMixedValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/mixed-values.svg");
	}

	@Test
	public void testFontSizeNamedValueWithoutAParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/named-value-without-a-parent.svg");
	}

	@Test
	public void testFontSizeNamedValue() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/named-value.svg");
	}

	@Test
	public void testFontSizeNegativeSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/negative-size.svg");
	}

	@Test
	public void testFontSizeNestedPercentValues_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/nested-percent-values-1.svg");
	}

	@Test
	public void testFontSizeNestedPercentValues_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/nested-percent-values-2.svg");
	}

	@Test
	public void testFontSizePercentValueWithoutAParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/percent-value-without-a-parent.svg");
	}

	@Test
	public void testFontSizePercentValue() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/percent-value.svg");
	}

	@Test
	public void testFontSizeSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/simple-case.svg");
	}

	@Test
	public void testFontSizeZeroSizeOnParent_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/zero-size-on-parent-1.svg");
	}

	@Test
	public void testFontSizeZeroSizeOnParent_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/zero-size-on-parent-2.svg");
	}

	@Test
	public void testFontSizeZeroSizeOnParent_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/zero-size-on-parent-3.svg");
	}

	@Test
	public void testFontSizeZeroSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-size/zero-size.svg");
	}

	/*
	 * font-stretch
	 */

	@Test
	public void testFontStretchExtraCondensed() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-stretch/extra-condensed.svg");
	}

	@Test
	public void testFontStretchInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-stretch/inherit.svg");
	}

	@Disabled
	@Test
	public void testFontStretchNarrower() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-stretch/narrower.svg");
	}

	/*
	 * font-style
	 */

	@Test
	public void testFontStyleInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-style/inherit.svg");
	}

	@Test
	public void testFontStyleItalic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-style/italic.svg");
	}

	@Test
	public void testFontStyleOblique() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-style/oblique.svg");
	}

	/*
	 * font-variant
	 */

	@Disabled
	@Test
	public void testFontVariantInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-variant/inherit.svg");
	}

	@Disabled
	@Test
	public void testFontVariantSmallCaps() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-variant/small-caps.svg");
	}

	/*
	 * font-weight
	 */

	@Test
	public void testFontWeight650() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/650.svg");
	}

	@Test
	public void testFontWeight700() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/700.svg");
	}

	@Test
	public void testFontWeightBold() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/bold.svg");
	}

	@Test
	public void testFontWeightBolderWithClamping() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/bolder-with-clamping.svg");
	}

	@Disabled
	@Test
	public void testFontWeightBolderWithoutParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/bolder-without-parent.svg");
	}

	@Disabled
	@Test
	public void testFontWeightBolder() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/bolder.svg");
	}

	@Test
	public void testFontWeightInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/inherit.svg");
	}

	@Test
	public void testFontWeightInvalidNumber_1() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/font-weight/invalid-number-1.svg", 1);
	}

	@Test
	public void testFontWeightLighterWithClamping() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/lighter-with-clamping.svg");
	}

	@Test
	public void testFontWeightLighterWithoutParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/lighter-without-parent.svg");
	}

	@Test
	public void testFontWeightLighter() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/lighter.svg");
	}

	@Test
	public void testFontWeightNormal() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font-weight/normal.svg");
	}

	@Test
	public void testFontFontShorthand() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font/font-shorthand.svg");
	}

	@Test
	public void testFontSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/font/simple-case.svg");
	}

	/*
	 * glyph-orientation-horizontal
	 */

	@Test
	public void testGlyphOrientationHorizontalSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/glyph-orientation-horizontal/simple-case.svg");
	}

	/*
	 * glyph-orientation-vertical
	 */

	@Test
	public void testGlyphOrientationVerticalSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/glyph-orientation-vertical/simple-case.svg");
	}

	/*
	 * kerning
	 */

	@Disabled
	@Test
	public void testKerning0() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/kerning/0.svg");
	}

	@Test
	public void testKerning10percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/kerning/10percent.svg");
	}

	/*
	 * lengthAdjust
	 */

	@Test
	public void testLengthAdjustSpacingAndGlyphs() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/lengthAdjust/spacingAndGlyphs.svg");
	}

	@Test
	public void testLengthAdjustTextOnPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/lengthAdjust/text-on-path.svg");
	}

	@Disabled
	@Test
	public void testLengthAdjustVertical() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/lengthAdjust/vertical.svg");
	}

	@Test
	public void testLengthAdjustWithUnderline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/lengthAdjust/with-underline.svg");
	}

	/*
	 * letter-spacing
	 */

	@Test
	public void testLetterSpacingMinus3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/-3.svg");
	}

	@Test
	public void testLetterSpacing0() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/0.svg");
	}

	@Test
	public void testLetterSpacing1mm() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/1mm.svg");
	}

	@Test
	public void testLetterSpacing3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/3.svg");
	}

	@Test
	public void testLetterSpacing5percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/5percent.svg");
	}

	@Disabled
	@Test
	public void testLetterSpacingFilterBbox() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/filter-bbox.svg");
	}

	@Test
	public void testLetterSpacingLargeNegative() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/large-negative.svg");
	}

	// Close to passing
	@Disabled
	@Test
	public void testLetterSpacingMixedScripts() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/mixed-scripts.svg");
	}

	@Test
	public void testLetterSpacingMixedSpacing() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/mixed-spacing.svg");
	}

	@Disabled
	@Test
	public void testLetterSpacingNon_ASCIICharacter() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/non-ASCII-character.svg");
	}

	@Test
	public void testLetterSpacingNormal() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/normal.svg");
	}

	@Disabled
	@Test
	public void testLetterSpacingOn_Arabic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/letter-spacing/on-Arabic.svg");
	}

	/*
	 * text-anchor
	 */

	@Disabled
	@Test
	public void testTextAnchorCoordinatesList() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/coordinates-list.svg");
	}

	@Test
	public void testTextAnchorEndOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/end-on-text.svg");
	}

	@Disabled
	@Test
	public void testTextAnchorEndWithLetterSpacing() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/end-with-letter-spacing.svg");
	}

	@Test
	public void testTextAnchorInheritance_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/inheritance-1.svg");
	}

	@Test
	public void testTextAnchorInheritance_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/inheritance-2.svg");
	}

	@Test
	public void testTextAnchorInheritance_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/inheritance-3.svg");
	}

	@Test
	public void testTextAnchorInvalidValueOnText() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/text-anchor/invalid-value-on-text.svg", 1);
	}

	@Test
	public void testTextAnchorMiddleOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/middle-on-text.svg");
	}

	@Test
	public void testTextAnchorOnTheFirstTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/on-the-first-tspan.svg");
	}

	@Disabled
	@Test
	public void testTextAnchorOnTspanWithArabic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/on-tspan-with-arabic.svg");
	}

	@Test
	public void testTextAnchorOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/on-tspan.svg");
	}

	@Test
	public void testTextAnchorStartOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/start-on-text.svg");
	}

	@Test
	public void testTextAnchorTextAnchorNotOnTextChunk() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-anchor/text-anchor-not-on-text-chunk.svg");
	}

	/*
	 * text-decoration
	 */

	@Test
	public void testTextDecorationAllTypesInlineCommaSeparated()
			throws TranscoderException, IOException {
		testNVErrIgnore(
				"samples/resvg/tests/text/text-decoration/all-types-inline-comma-separated.svg", 1);
	}

	@Test
	public void testTextDecorationAllTypesInlineNoSpaces() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/text-decoration/all-types-inline-no-spaces.svg",
				1);
	}

	@Test
	public void testTextDecorationAllTypesInline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/all-types-inline.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationAllTypesNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/all-types-nested.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationIndirectWithMultipleColors()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/indirect-with-multiple-colors.svg");
	}

	@Test
	public void testTextDecorationIndirect() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/indirect.svg");
	}

	@Test
	public void testTextDecorationLineThrough() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/line-through.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationOutsideTheTextElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/outside-the-text-element.svg");
	}

	@Test
	public void testTextDecorationOverline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/overline.svg");
	}

	@Test
	public void testTextDecorationStyleResolving_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/style-resolving-1.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationStyleResolving_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/style-resolving-2.svg");
	}

	@Test
	public void testTextDecorationStyleResolving_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/style-resolving-3.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationStyleResolving_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/style-resolving-4.svg");
	}

	@Test
	public void testTextDecorationTspanDecoration() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/tspan-decoration.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationUnderlineWithDyList_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/underline-with-dy-list-1.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationUnderlineWithDyList_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/underline-with-dy-list-2.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationUnderlineWithRotateList_3()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/underline-with-rotate-list-3.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationUnderlineWithRotateList_4()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/underline-with-rotate-list-4.svg");
	}

	@Disabled
	@Test
	public void testTextDecorationUnderlineWithYList() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/underline-with-y-list.svg");
	}

	@Test
	public void testTextDecorationUnderline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/underline.svg");
	}

	@Test
	public void testTextDecorationWithTextLengthOnASingleCharacter()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-decoration/with-textLength-on-a-single-character.svg");
	}

	/*
	 * text-rendering
	 */

	@Test
	public void testTextRenderingGeometricPrecision() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-rendering/geometricPrecision.svg");
	}

	@Test
	public void testTextRenderingOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-rendering/on-tspan.svg");
	}

	@Test
	public void testTextRenderingOptimizeLegibility() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-rendering/optimizeLegibility.svg");
	}

	@Test
	public void testTextRenderingOptimizeSpeed() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-rendering/optimizeSpeed.svg");
	}

	@Test
	public void testTextRenderingWithUnderline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text-rendering/with-underline.svg");
	}

	/*
	 * text
	 */

	@Disabled
	@Test
	public void testTextBidiReordering() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/bidi-reordering.svg");
	}

	@Disabled
	@Test
	public void testTextComplexGraphemeSplitByTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/complex-grapheme-split-by-tspan.svg");
	}

	@Disabled
	@Test
	public void testTextComplexGraphemesAndCoordinatesList()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/complex-graphemes-and-coordinates-list.svg");
	}

	@Disabled
	@Test
	public void testTextComplexGraphemes() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/complex-graphemes.svg");
	}

	@Disabled
	@Test
	public void testTextCompoundEmojisAndCoordinatesList() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/compound-emojis-and-coordinates-list.svg");
	}

	@Disabled
	@Test
	public void testTextCompoundEmojis() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/compound-emojis.svg");
	}

	@Test
	public void testTextDxAndDyInsteadOfXAndY() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/dx-and-dy-instead-of-x-and-y.svg");
	}

	@Test
	public void testTextDxAndDyWithLessValuesThanCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/dx-and-dy-with-less-values-than-characters.svg");
	}

	@Test
	public void testTextDxAndDyWithMoreValuesThanCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/dx-and-dy-with-more-values-than-characters.svg");
	}

	@Test
	public void testTextDxAndDyWithMultipleValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/dx-and-dy-with-multiple-values.svg");
	}

	@Test
	public void testTextEmAndExCoordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/em-and-ex-coordinates.svg");
	}

	@Disabled
	@Test
	public void testTextEmojis() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/emojis.svg");
	}

	@Test
	public void testTextEscapedText_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/escaped-text-1.svg");
	}

	@Test
	public void testTextEscapedText_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/escaped-text-2.svg");
	}

	@Test
	public void testTextEscapedText_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/escaped-text-3.svg");
	}

	@Test
	public void testTextEscapedText_4() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/escaped-text-4.svg");
	}

	@Disabled
	@Test
	public void testTextFillRuleEqEvenodd() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/fill-rule=evenodd.svg");
	}

	@Disabled
	@Test
	public void testTextFilterBbox() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/filter-bbox.svg");
	}

	@Disabled
	@Test
	public void testTextLigaturesHandlingInMixedFonts_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/ligatures-handling-in-mixed-fonts-1.svg");
	}

	@Disabled
	@Test
	public void testTextLigaturesHandlingInMixedFonts_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/ligatures-handling-in-mixed-fonts-2.svg");
	}

	@Test
	public void testTextMmCoordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/mm-coordinates.svg");
	}

	@Test
	public void testTextNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/nested.svg");
	}

	@Test
	public void testTextNoCoordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/no-coordinates.svg");
	}

	@Test
	public void testTextPercentValueOnDxAndDy() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/percent-value-on-dx-and-dy.svg");
	}

	@Test
	public void testTextPercentValueOnXAndY() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/percent-value-on-x-and-y.svg");
	}

	@Disabled
	@Test
	public void testTextRealTextHeight() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/real-text-height.svg");
	}

	// Undefined behavior
	@Test
	public void testTextRotateOn_Arabic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/rotate-on-Arabic.svg");
	}

	@Test
	public void testTextRotateWithAnInvalidAngle() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/text/rotate-with-an-invalid-angle.svg", 1);
	}

	@Disabled
	@Test
	public void testTextRotateWithLessValuesThanCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/rotate-with-less-values-than-characters.svg");
	}

	@Test
	public void testTextRotateWithMoreValuesThanCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/rotate-with-more-values-than-characters.svg");
	}

	@Disabled
	@Test
	public void testTextRotateWithMultipleValuesAndComplexText()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/rotate-with-multiple-values-and-complex-text.svg");
	}

	@Disabled
	@Test
	public void testTextRotateWithMultipleValuesUnderlineAndPattern()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/rotate-with-multiple-values-underline-and-pattern.svg");
	}

	@Test
	public void testTextRotateWithMultipleValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/rotate-with-multiple-values.svg");
	}

	@Test
	public void testTextRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/rotate.svg");
	}

	@Test
	public void testTextSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/simple-case.svg");
	}

	@Test
	public void testTextTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/transform.svg");
	}

	@Test
	public void testTextXAndYWithDxAndDyLists() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/x-and-y-with-dx-and-dy-lists.svg");
	}

	@Test
	public void testTextXAndYWithDxAndDy() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/x-and-y-with-dx-and-dy.svg");
	}

	@Test
	public void testTextXAndYWithLessValuesThanCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/x-and-y-with-less-values-than-characters.svg");
	}

	@Test
	public void testTextXAndYWithMoreValuesThanCharacters()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/x-and-y-with-more-values-than-characters.svg");
	}

	@Disabled
	@Test
	public void testTextXAndYWithMultipleValuesAndArabicText()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/x-and-y-with-multiple-values-and-arabic-text.svg");
	}

	@Test
	public void testTextXAndYWithMultipleValuesAndTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/x-and-y-with-multiple-values-and-tspan.svg");
	}

	@Test
	public void testTextXAndYWithMultipleValues() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/x-and-y-with-multiple-values.svg");
	}

	// https://heistak.github.io/your-code-displays-japanese-wrong/
	@Disabled
	@Test
	public void testTextXmlLangEqJa() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/xml-lang=ja.svg");
	}

	@Test
	public void testTextXmlSpace() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/xml-space.svg");
	}

	@Disabled
	@Test
	public void testTextZalgo() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/text/zalgo.svg");
	}

	/*
	 * textLength
	 */

	@Test
	public void testTextLength150OnParent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/150-on-parent.svg");
	}

	@Test
	public void testTextLength150OnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/150-on-tspan.svg");
	}

	@Test
	public void testTextLength150() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/150.svg");
	}

	@Test
	public void testTextLength40mm() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/40mm.svg");
	}

	@Test
	public void testTextLength75percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/75percent.svg");
	}

	/*
	 * Unclear that this one passes
	 */
	@Test
	public void testTextLengthArabicWithLengthAdjust() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/arabic-with-lengthAdjust.svg");
	}

	@Disabled
	@Test
	public void testTextLengthArabic() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/arabic.svg");
	}

	@Disabled
	@Test
	public void testTextLengthInherit() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/inherit.svg");
	}

	@Test
	public void testTextLengthNegative() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/textLength/negative.svg", 1);
	}

	@Disabled
	@Test
	public void testTextLengthOnASingleTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/on-a-single-tspan.svg");
	}

	// Should pass according to website
	@Disabled
	@Test
	public void testTextLengthOnTextAndTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/on-text-and-tspan.svg");
	}

	@Test
	public void testTextLengthZero() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textLength/zero.svg");
	}

	/*
	 * textPath
	 */

	@Test
	public void testTextPathClosedPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/closed-path.svg");
	}

	@Test
	public void testTextPathComplex() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/textPath/complex.svg", 1);
	}

	@Disabled
	@Test
	public void testTextPathDyWithTinyCoordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/dy-with-tiny-coordinates.svg");
	}

	@Test
	public void testTextPathInvalidLink() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/textPath/invalid-link.svg", 1);
	}

	@Test
	public void testTextPathInvalidTextPathInTheMiddle() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/textPath/invalid-textPath-in-the-middle.svg", 1);
	}

	@Disabled
	@Test
	public void testTextPathLinkToRect() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/link-to-rect.svg");
	}

	@Test
	public void testTextPathM_APath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/m-A-path.svg");
	}

	@Test
	public void testTextPathM_L_ZPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/m-L-Z-path.svg");
	}

	@Test
	public void testTextPathMethodEqStretch() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/method=stretch.svg");
	}

	@Test
	public void testTextPathMixedChildren_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/mixed-children-1.svg");
	}

	@Test
	public void testTextPathMixedChildren_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/mixed-children-2.svg");
	}

	@Disabled
	@Test
	public void testTextPathNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/nested.svg");
	}

	@Test
	public void testTextPathNoLink() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/textPath/no-link.svg", 1);
	}

	@Test
	public void testTextPathPathWith_ClosePath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/path-with-ClosePath.svg");
	}

	@Test
	public void testTextPathPathWithSubpathsAndStartOffset()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/path-with-subpaths-and-startOffset.svg");
	}

	@Test
	public void testTextPathPathWithSubpaths() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/path-with-subpaths.svg");
	}

	@Disabled
	@Test
	public void testTextPathSideEqRight() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/side=right.svg");
	}

	@Test
	public void testTextPathSimpleCase() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/simple-case.svg");
	}

	// resvg web site says it fails
	@Test
	public void testTextPathSpacingEqAuto() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/spacing=auto.svg");
	}

	@Disabled
	@Test
	public void testTextPathStartOffsetEqMinus100() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/startOffset=-100.svg");
	}

	@Test
	public void testTextPathStartOffsetEq10percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/startOffset=10percent.svg");
	}

	@Test
	public void testTextPathStartOffsetEq30() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/startOffset=30.svg");
	}

	@Test
	public void testTextPathStartOffsetEq5mm() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/startOffset=5mm.svg");
	}

	@Test
	public void testTextPathStartOffsetEq9999() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/startOffset=9999.svg");
	}

	@Test
	public void testTextPathTspanWithAbsolutePosition() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/tspan-with-absolute-position.svg");
	}

	@Test
	public void testTextPathTspanWithRelativePosition() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/tspan-with-relative-position.svg");
	}

	@Disabled
	@Test
	public void testTextPathTwoPaths() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/two-paths.svg");
	}

	@Test
	public void testTextPathVeryLongText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/very-long-text.svg");
	}

	@Test
	public void testTextPathWithBaselineShiftAndRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-baseline-shift-and-rotate.svg");
	}

	@Test
	public void testTextPathWithBaselineShift() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-baseline-shift.svg");
	}

	@Test
	public void testTextPathWithBigLetterSpacing() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-big-letter-spacing.svg");
	}

	@Test
	public void testTextPathWithCoordinatesOnText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-coordinates-on-text.svg");
	}

	@Test
	public void testTextPathWithCoordinatesOnTextPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-coordinates-on-textPath.svg");
	}

	@Disabled
	@Test
	public void testTextPathWithFilter() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-filter.svg");
	}

	@Disabled
	@Test
	public void testTextPathWithInvalidPathAndXlinkHref() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/textPath/with-invalid-path-and-xlink-href.svg",
				1);
	}

	@Test
	public void testTextPathWithLetterSpacing() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-letter-spacing.svg");
	}

	@Test
	public void testTextPathWithPathAndXlinkHref() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-path-and-xlink-href.svg");
	}

	@Test
	public void testTextPathWithPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-path.svg");
	}

	// Misses the initial letter
	@Disabled
	@Test
	public void testTextPathWithRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-rotate.svg");
	}

	@Test
	public void testTextPathWithTextAnchor() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-text-anchor.svg");
	}

	@Test
	public void testTextPathWithTransformOnAReferencedPath()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-transform-on-a-referenced-path.svg");
	}

	@Test
	public void testTextPathWithTransformOutsideAReferencedPath()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-transform-outside-a-referenced-path.svg");
	}

	@Disabled
	@Test
	public void testTextPathWithUnderline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/with-underline.svg");
	}

	@Test
	public void testTextPathWritingModeEqTb() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/textPath/writing-mode=tb.svg");
	}

	/*
	 * tref
	 */

	@Test
	public void testTrefLinkToAComplexText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/link-to-a-complex-text.svg");
	}

	@Disabled
	@Test
	public void testTrefLinkToANon_SVGElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/link-to-a-non-SVG-element.svg");
	}

	@Test
	public void testTrefLinkToANonTextElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/link-to-a-non-text-element.svg");
	}

	@Test
	public void testTrefLinkToAnExternalFileElement() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/link-to-an-external-file-element.svg");
	}

	@Test
	public void testTrefLinkToText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/link-to-text.svg");
	}

	@Test
	public void testTrefNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/nested.svg");
	}

	@Test
	public void testTrefPositionAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/position-attributes.svg");
	}

	@Test
	public void testTrefStyleAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/style-attributes.svg");
	}

	@Test
	public void testTrefWithATitleChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/with-a-title-child.svg");
	}

	@Test
	public void testTrefWithText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/with-text.svg");
	}

	@Disabled
	@Test
	public void testTrefXmlSpace() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tref/xml-space.svg");
	}

	/*
	 * tspan
	 */

	@Test
	public void testTspanBidiReordering() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/bidi-reordering.svg");
	}

	@Test
	public void testTspanMixedFontSize() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/mixed-font-size.svg");
	}

	@Test
	public void testTspanMixedXmlSpace_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/mixed-xml-space-1.svg");
	}

	@Test
	public void testTspanMixedXmlSpace_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/mixed-xml-space-2.svg");
	}

	@Test
	public void testTspanMixedXmlSpace_3() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/mixed-xml-space-3.svg");
	}

	@Test
	public void testTspanMixed() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/mixed.svg");
	}

	@Test
	public void testTspanMultipleCoordinates() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/multiple-coordinates.svg");
	}

	@Disabled
	@Test
	public void testTspanNestedRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/nested-rotate.svg");
	}

	@Test
	public void testTspanNestedWhitespaces() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/nested-whitespaces.svg");
	}

	@Test
	public void testTspanNested() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/nested.svg");
	}

	@Test
	public void testTspanOnlyWithY() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/only-with-y.svg");
	}

	@Test
	public void testTspanOutsideTheText() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/outside-the-text.svg");
	}

	@Test
	public void testTspanPseudoMultiLine() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/pseudo-multi-line.svg");
	}

	@Test
	public void testTspanRotateAndDisplayNone() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/rotate-and-display-none.svg");
	}

	@Test
	public void testTspanRotateOnChild() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/rotate-on-child.svg");
	}

	@Test
	public void testTspanSequential() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/sequential.svg");
	}

	@Test
	public void testTspanStyleOverride() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/style-override.svg");
	}

	@Test
	public void testTspanTextShapingAcrossMultipleTspan_1()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/text-shaping-across-multiple-tspan-1.svg");
	}

	@Test
	public void testTspanTextShapingAcrossMultipleTspan_2()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/text-shaping-across-multiple-tspan-2.svg");
	}

	@Test
	public void testTspanTransform() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/transform.svg");
	}

	@Test
	public void testTspanTspanBbox_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/tspan-bbox-1.svg");
	}

	@Disabled
	@Test
	public void testTspanTspanBbox_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/tspan-bbox-2.svg");
	}

	@Disabled
	@Test
	public void testTspanWithClipPath() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/with-clip-path.svg");
	}

	@Test
	public void testTspanWithDy() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/with-dy.svg");
	}

	@Disabled
	@Test
	public void testTspanWithFilter() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/with-filter.svg");
	}

	@Disabled
	@Test
	public void testTspanWithMask() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/with-mask.svg");
	}

	@Test
	public void testTspanWithOpacity() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/with-opacity.svg");
	}

	@Test
	public void testTspanWithXAndY() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/with-x-and-y.svg");
	}

	@Test
	public void testTspanWithoutAttributes() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/without-attributes.svg");
	}

	@Test
	public void testTspanXmlSpace_1() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/xml-space-1.svg");
	}

	@Test
	public void testTspanXmlSpace_2() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/tspan/xml-space-2.svg");
	}

	/*
	 * unicode-bidi
	 */

	@Test
	public void testUnicodeBidiBidiOverride() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/unicode-bidi/bidi-override.svg");
	}

	/*
	 * word-spacing
	 */

	@Test
	public void testWordSpacingMinus5() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/word-spacing/-5.svg");
	}

	@Test
	public void testWordSpacing0() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/word-spacing/0.svg");
	}

	@Test
	public void testWordSpacing10() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/word-spacing/10.svg");
	}

	@Test
	public void testWordSpacing2mm() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/word-spacing/2mm.svg");
	}

	@Test
	public void testWordSpacing5percent() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/word-spacing/5percent.svg");
	}

	@Test
	public void testWordSpacingLargeNegative() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/word-spacing/large-negative.svg");
	}

	@Test
	public void testWordSpacingNormal() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/word-spacing/normal.svg");
	}

	/*
	 * writing-mode
	 */

	@Disabled
	@Test
	public void testWritingModeArabicWithRl() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/arabic-with-rl.svg");
	}

	// resvg web site claims Batik passes this despite not supporting value
	@Disabled
	@Test
	public void testWritingModeHorizontalTb() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/horizontal-tb.svg");
	}

	@Test
	public void testWritingModeInheritance() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/inheritance.svg");
	}

	@Test
	public void testWritingModeInvalidValue() throws TranscoderException, IOException {
		testNVErrIgnore("samples/resvg/tests/text/writing-mode/invalid-value.svg", 1);
	}

	@Test
	public void testWritingModeJapaneseWithTb() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/japanese-with-tb.svg");
	}

	@Test
	public void testWritingModeLrTb() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/lr-tb.svg");
	}

	@Test
	public void testWritingModeLr() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/lr.svg");
	}

	@Disabled
	@Test
	public void testWritingModeMixedLanguagesWithTbAndUnderline()
			throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/mixed-languages-with-tb-and-underline.svg");
	}

	@Test
	public void testWritingModeMixedLanguagesWithTb() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/mixed-languages-with-tb.svg");
	}

	@Disabled
	@Test
	public void testWritingModeOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/on-tspan.svg");
	}

	@Disabled
	@Test
	public void testWritingModeRlTb() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/rl-tb.svg");
	}

	@Disabled
	@Test
	public void testWritingModeRl() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/rl.svg");
	}

	@Disabled
	@Test
	public void testWritingModeTbAndPunctuation() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-and-punctuation.svg");
	}

	@Test
	public void testWritingModeTbRl() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-rl.svg");
	}

	@Test
	public void testWritingModeTbWithAlignment() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-with-alignment.svg");
	}

	@Test
	public void testWritingModeTbWithDxOnSecondTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-with-dx-on-second-tspan.svg");
	}

	@Test
	public void testWritingModeTbWithDxOnTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-with-dx-on-tspan.svg");
	}

	@Test
	public void testWritingModeTbWithDyOnSecondTspan() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-with-dy-on-second-tspan.svg");
	}

	@Disabled
	@Test
	public void testWritingModeTbWithRotateAndUnderline() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-with-rotate-and-underline.svg");
	}

	@Test
	public void testWritingModeTbWithRotate() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb-with-rotate.svg");
	}

	@Test
	public void testWritingModeTb() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/tb.svg");
	}

	@Disabled
	@Test
	public void testWritingModeVerticalLr() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/vertical-lr.svg");
	}

	@Disabled
	@Test
	public void testWritingModeVerticalRl() throws TranscoderException, IOException {
		test("samples/resvg/tests/text/writing-mode/vertical-rl.svg");
	}

}
