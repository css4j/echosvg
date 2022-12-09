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

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files, generally under the {@code samples}
 * directory, and compares the result with a reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * If you want to test a non-conformant SVG document, run a non-validating test
 * with {@link #testNV(String)}.
 * </p>
 */
public class SamplesRenderingTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	@Test
	public void testLogo() throws TranscoderException, IOException {
		test("samples/asf-logo.svg");
	}

	@Test
	public void testBarChart() throws TranscoderException, IOException {
		test("samples/barChart.svg");
	}

	@Test
	public void test3D() throws TranscoderException, IOException {
		test("samples/batik3D.svg");
	}

	@Test
	public void test70() throws TranscoderException, IOException {
		test("samples/batik70.svg");
	}

	@Test
	public void testBatikBatik() throws TranscoderException, IOException {
		test("samples/batikBatik.svg");
	}

	@Test
	public void testFX() throws TranscoderException, IOException {
		test("samples/batikFX.svg");
	}

	@Ignore
	@Test
	public void testBatikLogo() throws TranscoderException, IOException {
		test("samples/batikLogo.svg");
	}

	@Test
	public void testYin() throws TranscoderException, IOException {
		test("samples/batikYin.svg");
	}

	/* The file does not exist
	@Test
	public void testBookOfKells() throws TranscoderException, IOException {
		test("samples/bookOfKells.svgz");
	} */

	@Test
	public void testChessboard() throws TranscoderException, IOException {
		test("samples/chessboard.svg");
	}

	@Test
	public void testEllipses() throws TranscoderException, IOException {
		test("samples/ellipses.svg");
	}

	@Test
	public void testGradients() throws TranscoderException, IOException {
		test("samples/gradients.svg");
	}

	@Test
	public void testGVT() throws TranscoderException, IOException {
		test("samples/GVT.svg");
	}

	@Test
	public void testHenryV() throws TranscoderException, IOException {
		test("samples/henryV.svg");
	}

	@Test
	public void testLogoShadowOffset() throws TranscoderException, IOException {
		test("samples/logoShadowOffset.svg");
	}

	@Test
	public void testLogoTexture() throws TranscoderException, IOException {
		test("samples/logoTexture.svg");
	}

	@Test
	public void testMapSpain() throws TranscoderException, IOException {
		test("samples/mapSpain.svg");
	}

	@Test
	public void testMapWaadt() throws TranscoderException, IOException {
		test("samples/mapWaadt.svg");
	}

	@Test
	public void testMathMetal() throws TranscoderException, IOException {
		test("samples/mathMetal.svg");
	}

	@Test
	public void testMoonPhases() throws TranscoderException, IOException {
		test("samples/moonPhases.svg");
	}

	@Test
	public void testSizeOfSun() throws TranscoderException, IOException {
		test("samples/sizeOfSun.svg");
	}

	@Test
	public void testSunRise() throws TranscoderException, IOException {
		test("samples/sunRise.svg");
	}

	@Test
	public void testSydney() throws TranscoderException, IOException {
		test("samples/sydney.svg");
	}

	@Test
	public void testTextRotate() throws TranscoderException, IOException {
		test("samples/textRotate.svg");
	}

	@Test
	public void testTextRotateShadows() throws TranscoderException, IOException {
		test("samples/textRotateShadows.svg");
	}

	@Test
	public void testUnsupportedRules() throws TranscoderException, IOException {
		testNV("samples/unsupportedRules.svg");
	}

	/*
	 * SVG 1.2
	 */
	@Test
	public void testFlowBidi() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowBidi.svg");
	}

	@Test
	public void testFlowText() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText.svg");
	}

	@Test
	public void testFlowText2() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText2.svg");
	}

	@Test
	public void testFlowText3() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText3.svg");
	}

	@Test
	public void testFlowText4() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText4.svg");
	}

	@Test
	public void testFlowText5() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowText5.svg");
	}

	@Test
	public void testFlowRegionBreak() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/flowRegionBreak.svg");
	}

	@Test
	public void testLineHeightFontShorthand() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/text/lineHeightFontShorthand.svg");
	}

	@Test
	public void testStructureMulti() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/multi.svg");
	}

	@Test
	public void testStructureMulti2() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/multi2.svg");
	}

	@Test
	public void testOperaSubImage() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/opera/opera-subImage.svg");
	}

	@Test
	public void testOperaSubImageRef() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/structure/opera/opera-subImageRef.svg");
	}

	@Test
	public void testPaintsSolidColor() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/paints/solidColor.svg");
	}

	@Test
	public void testPaintsSolidColor2() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/paints/solidColor2.svg");
	}

	// See issue #27
	@Ignore
	@Test
	public void testFilterRegion() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/filters/filterRegion.svg");
	}

	@Test
	public void testFilterRegionDetailed() throws TranscoderException, IOException {
		testNV("samples/tests/spec12/filters/filterRegionDetailed.svg");
	}

	/*
	 * Extensions
	 */
	@Test
	public void testExtColorSwitch() throws TranscoderException, IOException {
		testNV("samples/extensions/colorSwitch.svg");
	}

	@Test
	public void testExtHistogramNormalization() throws TranscoderException, IOException {
		testNV("samples/extensions/histogramNormalization.svg");
	}

	@Test
	public void testExtRegularPolygon() throws TranscoderException, IOException {
		testNV("samples/extensions/regularPolygon.svg");
	}

	@Test
	public void testExtStar() throws TranscoderException, IOException {
		testNV("samples/extensions/star.svg");
	}

	@Test
	public void testExtFlowText() throws TranscoderException, IOException {
		testNV("samples/extensions/flowText.svg");
	}

	@Test
	public void testExtFlowTextAlign() throws TranscoderException, IOException {
		testNV("samples/extensions/flowTextAlign.svg");
	}

	/*
	 * Color
	 */
	@Test
	public void testColorProfile() throws TranscoderException, IOException {
		test("samples/tests/spec/color/colorProfile.svg");
	}

	@Test
	public void testColors() throws TranscoderException, IOException {
		test("samples/tests/spec/color/colors.svg");
	}

	@Test
	public void testSystemColors() throws TranscoderException, IOException {
		test("samples/tests/spec/color/systemColors.svg");
	}

	/*
	 * Coordinates
	 */
	@Test
	public void testPercentagesAndUnits() throws TranscoderException, IOException {
		test("samples/tests/spec/coordinates/percentagesAndUnits.svg");
	}

	@Test
	public void testEm() throws TranscoderException, IOException {
		test("samples/tests/spec/coordinates/em.svg");
	}

	/*
	 * Filters
	 */
	@Test
	public void testFilterEnableBackground() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/enableBackground.svg");
	}

	@Test
	public void testFilterFeColorMatrix() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feColorMatrix.svg");
	}

	@Test
	public void testFilterFeComponentTransfer() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feComponentTransfer.svg");
	}

	@Test
	public void testFilterFeComponentTransfer2() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feComponentTransfer2.svg");
	}

	@Test
	public void testFilterFeComposite() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feComposite.svg");
	}

	// Issue #28: "Unable to convolve src image", see also BATIK-1280
	@Ignore
	@Test
	public void testFilterFeConvolveMatrix() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feConvolveMatrix.svg");
	}

	@Test
	public void testFilterFeDisplacementMap() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feDisplacementMap.svg");
	}

	@Test
	public void testFilterFeGaussianDefault() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feGaussianDefault.svg");
	}

	@Test
	public void testFilterFeImage() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feImage.svg");
	}

	@Test
	public void testFilterFeImage2() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feImage2.svg");
	}

	@Test
	public void testFilterFeMerge() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feMerge.svg");
	}

	@Test
	public void testFilterFeMorphology() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feMorphology.svg");
	}

	@Test
	public void testFilterFeTile() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feTile.svg");
	}

	@Test
	public void testFilterFeTileTarget() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feTileTarget.svg");
	}

	@Test
	public void testFilterFeTurbulence() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feTurbulence.svg");
	}

	@Test
	public void testFilterRegions() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/filterRegions.svg");
	}

	@Test
	public void testFilterSvgEnableBackground() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/svgEnableBackground.svg");
	}

	/*
	 * Fonts
	 */
	@Ignore
	@Test
	public void testFont() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/echosvgFont.svg");
	}

	/*
	 * This test depends on the Batik Font, which is licensing-problematic.
	 * The license only allows usage of ASF trademarks for attribution purposes.
	 */
	@Test
	public void testFontAltGlyph() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontAltGlyph.svg");
	}

	@Test
	public void testFontAltGlyph2() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontAltGlyph2.svg");
	}

	@Test
	public void testFontArabic() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontArabic.svg");
	}

	@Test
	public void testFontBounds() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontBounds.svg");
	}

	@Test
	public void testFontChoice() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontChoice.svg");
	}

	@Test
	public void testFontDecorations() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontDecorations.svg");
	}

	@Test
	public void testFontExternalFont() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontExternalFont.svg");
	}

	/*
	 * Can't make this one work
	 */
	@Ignore
	@Test
	public void testFontFace() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontFace.svg");
	}

	@Test
	public void testFontGlyphChoice() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontGlyphChoice.svg");
	}

	@Test
	public void testFontGlyphsBoth() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontGlyphsBoth.svg");
	}

	@Test
	public void testFontGlyphsChildSVG() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontGlyphsChildSVG.svg");
	}

	@Test
	public void testFontGlyphsD() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontGlyphsD.svg");
	}

	@Ignore
	@Test
	public void testFontKerning() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontKerning.svg");
	}

	@Test
	public void testFontOnPath() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontOnPath.svg");
	}

	@Test
	public void testFontStyling() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/fontStyling.svg");
	}

	/*
	 * Linking
	 */
	@Test
	public void testAnchorInsideText() throws TranscoderException, IOException {
		testNV("samples/tests/spec/linking/anchorInsideText.svg");
	}

	@Test
	public void testAnchor() throws TranscoderException, IOException {
		test("samples/tests/spec/linking/anchor.svg");
	}

	@Test
	public void testLinkingTransform() throws TranscoderException, IOException {
		test("samples/tests/spec/linking/linkingTransform.svg");
	}

	@Test
	public void testLinkingViewBox() throws TranscoderException, IOException {
		test("samples/tests/spec/linking/linkingViewBox.svg");
	}

	@Test
	public void testPointerEvents() throws TranscoderException, IOException {
		test("samples/tests/spec/linking/pointerEvents.svg");
	}

	@Test
	public void testPointerEvents2() throws TranscoderException, IOException {
		test("samples/tests/spec/linking/pointerEvents2.svg");
	}

	/*
	 * Masking
	 */
	@Test
	public void testMaskClip() throws TranscoderException, IOException {
		test("samples/tests/spec/masking/clip.svg");
	}

	@Test
	public void testMaskRegions() throws TranscoderException, IOException {
		test("samples/tests/spec/masking/maskRegions.svg");
	}

	@Test
	public void testMaskClipTransform() throws TranscoderException, IOException {
		test("samples/tests/spec/masking/clipTransform.svg");
	}

	/*
	 * Painting
	 */
	@Test
	public void testPaintBboxOnText() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/bboxOnText.svg");
	}

	@Test
	public void testPaintDisplay() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/display.svg");
	}

	@Test
	public void testPaintImageRendering() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/image-rendering.svg");
	}

	@Test
	public void testPaintMarkersExt() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/markersExt.svg");
	}

	@Test
	public void testPaintMarkersMisc() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/markersMisc.svg");
	}

	@Test
	public void testPaintMarkersOrientA() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/markersOrientA.svg");
	}

	@Test
	public void testPaintMarkersOrientB() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/markersOrientB.svg");
	}

	@Test
	public void testPaintMarkersPreserveAspectRatio() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/markersPreserveAspectRatio.svg");
	}

	@Test
	public void testPaintMarkersShapes() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/markersShapes.svg");
	}

	@Test
	public void testPaintMarkersStrokeRendering() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/stroke-rendering.svg");
	}

	@Test
	public void testPaintMarkersShapeRendering() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/shape-rendering.svg");
	}

	@Test
	public void testPaintMarkersTextRendering() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/text-rendering.svg");
	}

	@Test
	public void testPaintMarkersTextRendering2() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/text-rendering2.svg");
	}

	@Test
	public void testPaintMarkersVisibility() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/visibility.svg");
	}

	/*
	 * Paints
	 */
	@Test
	public void testPaintsExternalPaints() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/externalPaints.svg");
	}

	@Test
	public void testPaintsGradientLimit() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/gradientLimit.svg");
	}

	@Test
	public void testPaintsLinearGradientOrientation() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/linearGradientOrientation.svg");
	}

	@Test
	public void testPaintsLinearGradientLine() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/linearGradientLine.svg");
	}

	@Test
	public void testPaintsLinearGradientRepeat() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/linearGradientRepeat.svg");
	}

	@Test
	public void testPaintsRadialGradientLine() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/radialGradientLine.svg");
	}

	@Test
	public void testPaintsGradientPoint() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/gradientPoint.svg");
	}

	@Test
	public void testPaintsPatternPreserveAspectRatioA() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/patternPreserveAspectRatioA.svg");
	}

	@Test
	public void testPaintsPatternRegionA() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/patternRegionA.svg");
	}

	@Test
	public void testPaintsPatternRegionB() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/patternRegionB.svg");
	}

	@Test
	public void testPaintsPatternRegions() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/patternRegions.svg");
	}

	@Test
	public void testPaintsRadialGradient() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/radialGradient.svg");
	}

	@Test
	public void testPaintsRadialGradient2() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/radialGradient2.svg");
	}

	@Test
	public void testPaintsRadialGradient3() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/radialGradient3.svg");
	}

	@Test
	public void testPaintsRadialGradientOrientation() throws TranscoderException, IOException {
		test("samples/tests/spec/paints/radialGradientOrientation.svg");
	}

	/*
	 * Rendering
	 */
	@Test
	public void testRenderingOpacity() throws TranscoderException, IOException {
		test("samples/tests/spec/rendering/opacity.svg");
	}

	@Test
	public void testRenderingOpacity2() throws TranscoderException, IOException {
		test("samples/tests/spec/rendering/opacity2.svg");
	}

	@Test
	public void testRenderingPaintOpacity() throws TranscoderException, IOException {
		test("samples/tests/spec/rendering/paintOpacity.svg");
	}

	/*
	 * Shapes
	 */
	@Test
	public void testShapesZero() throws TranscoderException, IOException {
		test("samples/tests/spec/shapes/zero.svg");
	}

	@Test
	public void testShapesEmptyShape() throws TranscoderException, IOException {
		test("samples/tests/spec/shapes/emptyShape.svg");
	}

	/*
	 * Structure
	 */
	@Test
	public void testDataProtocol() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/dataProtocol.svg");
	}

	@Test
	public void testExternalUseCascading() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/externalUseCascading.svg");
	}

	@Test
	public void testStructureImage() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/image.svg");
	}

	@Test
	public void testRasterImageViewBox() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/rasterImageViewBox.svg");
	}

	@Test
	public void testRasterImageViewBoxClip() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/rasterImageViewBoxClip.svg");
	}

	@Test
	public void testRasterImageViewBoxOverflow() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/rasterImageViewBoxOverflow.svg");
	}

	@Test
	public void testSvgImageViewBox() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/svgImageViewBox.svg");
	}

	@Test
	public void testSvgImageViewBoxClip() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/svgImageViewBoxClip.svg");
	}

	@Test
	public void testSvgImageViewBoxOverflow() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/svgImageViewBoxOverflow.svg");
	}

	@Test
	public void testSymbolViewBox() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/symbolViewBox.svg");
	}

	@Test
	public void testSymbolViewBoxClip() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/symbolViewBoxClip.svg");
	}

	@Test
	public void testSymbolViewBoxOverflow() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/symbolViewBoxOverflow.svg");
	}

	@Test
	public void testStructureTiff() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/tiff.svg");
	}

	@Test
	public void testStructureToolTips() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/toolTips.svg");
	}

	@Test
	public void testStructureUseMultiple() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/useMultiple.svg");
	}

	@Test
	public void testStructureUseMultipleURI() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/useMultipleURI.svg");
	}

	@Test
	public void testStructureUseStylesheet() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/useStylesheet.svg");
	}

	@Test
	public void testStructureUseStyling() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/useStyling.svg");
	}

	@Test
	public void testStructureUseStylingURI() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/useStylingURI.svg");
	}

	@Test
	public void testStructureUseTargets() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/useTargets.svg");
	}

	@Test
	public void testStructureUseTargets2() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/useTargets2.svg");
	}

	@Test
	public void testStructureXmlBase() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/xmlBase.svg");
	}

	@Test
	public void testStructureXmlBaseStyling() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/xmlBaseStyling.svg");
	}

	@Test
	public void testStructureRequiredFeatures() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/requiredFeatures.svg");
	}

	@Test
	public void testStructureRequiredFeaturesCombo() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/requiredFeaturesCombo.svg");
	}

	@Test
	public void testStructureSwitch() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/switch.svg");
	}

	@Test
	public void testStructureSystemLanguage() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/systemLanguage.svg", "fr");
	}

	@Test
	public void testStructureSystemLanguageDialect() throws TranscoderException, IOException {
		test("samples/tests/spec/structure/systemLanguageDialect.svg", "en-UK");
	}

	/*
	 * Styling
	 */
	@Test
	public void testAlternateStylesheet() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/alternateStylesheet.svg");
	}

	@Test
	public void testCssMedia() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/cssMedia.svg");
	}

	@Test
	public void testCssMediaList() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/cssMediaList.svg");
	}

	@Test
	public void testEmptyStyle() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/emptyStyle.svg");
	}

	@Test
	public void testFontShorthand() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/fontShorthand.svg");
	}

	@Test
	public void testImportant() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/important.svg");
	}

	@Test
	public void testSmiley() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/smiley.svg");
	}

	@Test
	public void testStyleElement() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/styleElement.svg");
	}

	@Test
	public void testXHTMLEmbed() throws TranscoderException, IOException {
		testXHTML("samples/tests/spec/styling/css2.xhtml");
	}

	/*
	 * Text
	 */
	@Test
	public void testLongTextOnPath() throws TranscoderException, IOException {
		test("samples/tests/spec/text/longTextOnPath.svg");
	}

	@Test
	public void testSmallFonts() throws TranscoderException, IOException {
		test("samples/tests/spec/text/smallFonts.svg");
	}

	@Test
	public void testTextAnchor() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textAnchor.svg");
	}

	@Test
	public void testTextAnchor2() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textAnchor2.svg");
	}

	@Test
	public void testTextAnchor3() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textAnchor3.svg");
	}

	@Test
	public void testTextBiDi() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textBiDi.svg");
	}

	@Test
	public void testTextBiDi2() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textBiDi2.svg");
	}

	@Test
	public void testTextDecoration() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textDecoration.svg");
	}

	// See issue #31
	@Ignore
	@Test
	public void testTextDecoration2() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textDecoration2.svg");
	}

	@Test
	public void testTextEffect() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textEffect.svg");
	}

	@Test
	public void testTextEffect2() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textEffect2.svg");
	}

	@Test
	public void testTextEffect3() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textEffect3.svg");
	}

	@Test
	public void testTextFeatures() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textFeatures.svg");
	}

	@Test
	public void testTextLayout() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textLayout.svg");
	}

	@Test
	public void testTextLayout2() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textLayout2.svg");
	}

	@Test
	public void testTextLength() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textLength.svg");
	}

	@Test
	public void testTextOnPath() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textOnPath.svg");
	}

	@Test
	public void testTextOnPath2() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textOnPath2.svg");
	}

	@Test
	public void testTextOnPath3() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textOnPath3.svg");
	}

	@Test
	public void testTextOnPathSpaces() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textOnPathSpaces.svg");
	}

	@Test
	public void testTextPCDATA() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textPCDATA.svg");
	}

	@Test
	public void testTextProperties() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textProperties.svg");
	}

	@Test
	public void testTextProperties2() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textProperties2.svg");
	}

	@Test
	public void testTextStyles() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textStyles.svg");
	}

	@Test
	public void testVerticalText() throws TranscoderException, IOException {
		test("samples/tests/spec/text/verticalText.svg");
	}

	@Test
	public void testVerticalTextOnPath() throws TranscoderException, IOException {
		test("samples/tests/spec/text/verticalTextOnPath.svg");
	}

	@Test
	public void testTextPosition() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textPosition.svg");
	}

	@Test
	public void testTextGlyphOrientationHorizontal() throws TranscoderException, IOException {
		test("samples/tests/spec/text/textGlyphOrientationHorizontal.svg");
	}

	@Test
	public void testXmlSpace() throws TranscoderException, IOException {
		testNV("samples/tests/spec/text/xmlSpace.svg");
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
	@Ignore
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
		test("samples/tests/spec/scripting/paintType.svg");
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
	public void testScriptRemoveOnclick() throws TranscoderException, IOException {
		test("samples/tests/spec/scripting/removeOnclick.svg");
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
	 * Bugs
	 */
	@Test
	public void testBug19363() throws TranscoderException, IOException {
		test("test-resources/io/sf/carte/echosvg/test/svg/bug19363.svg");
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

	@Ignore
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

	private void test(String file) throws TranscoderException, IOException {
		test(file, true);
	}

	/**
	 * A non-validating test.
	 * 
	 * @param file the SVG file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	private void testNV(String file) throws TranscoderException, IOException {
		test(file, false);
	}

	/**
	 * Test the rendering of a file with the given user language.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the SVG file to test.
	 * @param lang the language.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	private void test(String file, String lang) throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setUserLanguage(lang);
		runner.setFile(file);
		runner.runTest(0.00001f, 0.00001f);
	}

	/**
	 * Test the rendering of a SVG file.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the SVG file to test.
	 * @param validating if true, the SVG is validated.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	private void test(String file, boolean validating) throws TranscoderException, IOException {
		RenderingTest runner = new RenderingTest();
		runner.setValidating(validating);
		runner.setFile(file);
		runner.runTest(0.00001f, 0.00001f);
	}

	/**
	 * Test the rendering of a SVG image inside an XHTML document.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the HTML file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	private void testXHTML(String file) throws TranscoderException, IOException {
		RenderingTest runner = new XHTMLRenderingAccuracyTest();
		runner.setFile(file);
		runner.runTest(0.000001f, 0.000001f);
	}

	/**
	 * Dynamic test of the rendering of a SVG file.
	 * 
	 * <p>
	 * A small percentage of different pixels is allowed during the comparison to a
	 * reference image.
	 * </p>
	 * 
	 * @param file the SVG file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	private void testDynamicUpdate(String file) throws TranscoderException, IOException {
		JSVGRenderingAccuracyTest runner = new JSVGRenderingAccuracyTest();
		runner.setFile(file);
		runner.runTest(0.00001f, 0.00001f);
	}

}
