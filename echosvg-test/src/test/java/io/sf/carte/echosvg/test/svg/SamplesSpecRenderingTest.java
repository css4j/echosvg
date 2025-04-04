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

import static io.sf.carte.echosvg.test.misc.TestFonts.isFontAvailable;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.ScriptUtil;
import io.sf.carte.echosvg.test.misc.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files under the {@code samples/spec}
 * directory (except the {@code scripting} subdirectory), and compares the
 * result with a reference image.
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 * <p>
 * If you want to test a non-conformant SVG document, run a non-validating test
 * with {@link #testNV(String)}.
 * </p>
 */
public class SamplesSpecRenderingTest extends AbstractSamplesRendering {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
		ScriptUtil.defaultRhinoShutter();
	}

	/*
	 * Color
	 */
	@Test
	public void testColorProfiles() throws TranscoderException, IOException {
		testNV("samples/tests/spec/color/colorProfiles.svg");
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
	public void testAnimRotateOrigin() throws TranscoderException, IOException {
		float[] times = { 0.5f, 1.5f, 1.52f, 3.5f };
		testAnim("samples/tests/spec/coordinates/animRotateOrigin.svg", times);
	}

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
	@Disabled
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
	public void testFilterFeTurbulenceDivZero() throws TranscoderException, IOException {
		test("samples/tests/spec/filters/feTurbulenceDivZero.svg");
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
	@Disabled
	@Test
	public void testFont() throws TranscoderException, IOException {
		test("samples/tests/spec/fonts/echosvgFont.svg");
	}

	/*
	 * This test depends on the Batik Font, which is licensing-problematic. The
	 * license only allows usage of ASF trademarks for attribution purposes.
	 */
	@Test
	public void testFontAltGlyph() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/fonts/fontAltGlyph.svg", null, 4);
	}

	@Test
	public void testFontAltGlyph2() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/fonts/fontAltGlyph2.svg", null, 5);
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
	@Disabled
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

	@Disabled
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

	/**
	 * Render a document which contains an image with a broken link.
	 * <p>
	 * Another image is used as a broken link image.
	 * </p>
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testImageBadUrl() throws TranscoderException, IOException {
		/*
		 * Admits errors (the broken link and the invalid x,y being reported).
		 */
		testNVErrIgnore("samples/tests/spec/linking/imageBadUrl.svg", null, 5);
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
	public void testMissingRef() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/linking/missingRef.svg", null, 16);
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

	@Test
	public void testPaintMarkersVisibilityUnset() throws TranscoderException, IOException {
		test("samples/tests/spec/painting/visibilityUnset.svg");
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
	public void testRenderingInvalidViewbox() throws TranscoderException, IOException {
		testErrIgnore("samples/tests/spec/rendering/invalidViewbox.svg", BROWSER_MEDIA, true, 3);
	}

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

	@Test
	public void testRenderingResolutionPxMM() throws TranscoderException, IOException {
		testResolutionPxMM("samples/tests/spec/rendering/resolution.svg", 0.25f);
	}

	@Test
	public void testRenderingZeroWidthViewbox() throws TranscoderException, IOException {
		test("samples/tests/spec/rendering/zeroWidthViewbox.svg");
	}

	/*
	 * Shapes
	 */
	@Test
	public void testShapesPolygons() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/shapes/polygons.svg", BROWSER_MEDIA, 2);
	}

	@Test
	public void testShapesWrongAttr() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/shapes/wrongAttr.svg", null, 10);
	}

	@Test
	public void testShapesMissingAttr() throws TranscoderException, IOException {
		testNVErrIgnore("samples/tests/spec/shapes/missingAttr.svg", null, 1);
	}

	@Test
	public void testShapesZero() throws TranscoderException, IOException {
		test("samples/tests/spec/shapes/zero.svg");
	}

	@Test
	public void testShapesEmptyShape() throws TranscoderException, IOException {
		test("samples/tests/spec/shapes/emptyShape.svg", true);
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
	public void testStructureUseAnimEvent() throws TranscoderException, IOException {
		float[] times = { 0f, 0.3f };
		int[] errors = { 4, 2 };
		testAnim("samples/tests/spec/structure/useAnimEvent.svg", times, errors);
	}

	@Test
	public void testStructureUseAnimEventXlink() throws TranscoderException, IOException {
		float[] times = { 0f, 0.3f };
		int[] errors = { 6, 4 };
		testAnim("samples/tests/spec/structure/useAnimEventXlink.svg", times, errors);
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
	public void testAlternateStylesheet_Hot() throws TranscoderException, IOException {
		testAlternateSheet("samples/tests/spec/styling/alternateStylesheet.svg", "Hot", true);
	}

	@Test
	public void testAlternateStylesheet_Cold() throws TranscoderException, IOException {
		testAlternateSheet("samples/tests/spec/styling/alternateStylesheet.svg", "Cold", true);
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
	public void testDefaultFontFamily() throws TranscoderException, IOException {
		test("samples/tests/spec/styling/defaultFontFamily.svg");
	}

	@Test
	public void testUserSheet() throws TranscoderException, IOException {
		testUserSheet("samples/tests/spec/styling/userStylesheet.svg", true);
	}

	@Test
	public void testHTMLEmbed() throws TranscoderException, IOException {
		testHTML("samples/tests/spec/styling/css3.html");
	}

	@Test
	public void testHTMLEmbedPrint() throws TranscoderException, IOException {
		testHTML("samples/tests/spec/styling/css3.html", null, PRINT_MEDIA);
	}

	@Test
	public void testHTMLEmbedSelector() throws TranscoderException, IOException {
		testHTML("samples/tests/spec/styling/css3.html", "#theSVG", null);
	}

	@Test
	public void testXHTMLEmbed() throws TranscoderException, IOException {
		testXHTML("samples/tests/spec/styling/css3.xhtml");
	}

	@Test
	public void testXHTMLErrorRecovery() throws TranscoderException, IOException {
		testXHTMLErrIgnore("samples/tests/spec/styling/error-recovery.xhtml", null, 6);
	}

	@Test
	public void testXHTMLErrorRecoveryPrintMedia() throws TranscoderException, IOException {
		testXHTMLErrIgnore("samples/tests/spec/styling/error-recovery.xhtml", "print", 6);
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
	public void testTextArabicCharacters() throws TranscoderException, IOException {
		testNV("samples/tests/spec/text/arabicCharacters.svg");
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

	@Test
	public void testTextDecoration2() throws TranscoderException, IOException {
		assumeTrue(isFontAvailable("Lucida Sans Typewriter"),
				"'Lucida Sans Typewriter' font is not available");
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

}
