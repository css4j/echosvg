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
package io.sf.carte.echosvg.css.engine;

import org.w3c.dom.Document;

import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.css.engine.value.css.ClipManager;
import io.sf.carte.echosvg.css.engine.value.css.CursorManager;
import io.sf.carte.echosvg.css.engine.value.css.DirectionManager;
import io.sf.carte.echosvg.css.engine.value.css.DisplayManager;
import io.sf.carte.echosvg.css.engine.value.css.FontFamilyManager;
import io.sf.carte.echosvg.css.engine.value.css.FontShorthandManager;
import io.sf.carte.echosvg.css.engine.value.css.FontSizeAdjustManager;
import io.sf.carte.echosvg.css.engine.value.css.FontSizeManager;
import io.sf.carte.echosvg.css.engine.value.css.FontStretchManager;
import io.sf.carte.echosvg.css.engine.value.css.FontStyleManager;
import io.sf.carte.echosvg.css.engine.value.css.FontVariantManager;
import io.sf.carte.echosvg.css.engine.value.css.FontWeightManager;
import io.sf.carte.echosvg.css.engine.value.css.LineHeightManager;
import io.sf.carte.echosvg.css.engine.value.css.MarginShorthandManager;
import io.sf.carte.echosvg.css.engine.value.css.OverflowManager;
import io.sf.carte.echosvg.css.engine.value.css.SrcManager;
import io.sf.carte.echosvg.css.engine.value.css.TextDecorationManager;
import io.sf.carte.echosvg.css.engine.value.css.UnicodeBidiManager;
import io.sf.carte.echosvg.css.engine.value.css.VisibilityManager;
import io.sf.carte.echosvg.css.engine.value.svg.AlignmentBaselineManager;
import io.sf.carte.echosvg.css.engine.value.svg.BaselineShiftManager;
import io.sf.carte.echosvg.css.engine.value.svg.ClipPathManager;
import io.sf.carte.echosvg.css.engine.value.svg.ClipRuleManager;
import io.sf.carte.echosvg.css.engine.value.svg.ColorInterpolationFiltersManager;
import io.sf.carte.echosvg.css.engine.value.svg.ColorInterpolationManager;
import io.sf.carte.echosvg.css.engine.value.svg.ColorManager;
import io.sf.carte.echosvg.css.engine.value.svg.ColorProfileManager;
import io.sf.carte.echosvg.css.engine.value.svg.ColorRenderingManager;
import io.sf.carte.echosvg.css.engine.value.svg.DominantBaselineManager;
import io.sf.carte.echosvg.css.engine.value.svg.EnableBackgroundManager;
import io.sf.carte.echosvg.css.engine.value.svg.FillRuleManager;
import io.sf.carte.echosvg.css.engine.value.svg.FilterManager;
import io.sf.carte.echosvg.css.engine.value.svg.GlyphOrientationHorizontalManager;
import io.sf.carte.echosvg.css.engine.value.svg.GlyphOrientationVerticalManager;
import io.sf.carte.echosvg.css.engine.value.svg.ImageRenderingManager;
import io.sf.carte.echosvg.css.engine.value.svg.KerningManager;
import io.sf.carte.echosvg.css.engine.value.svg.MarkerManager;
import io.sf.carte.echosvg.css.engine.value.svg.MarkerShorthandManager;
import io.sf.carte.echosvg.css.engine.value.svg.MaskManager;
import io.sf.carte.echosvg.css.engine.value.svg.OpacityManager;
import io.sf.carte.echosvg.css.engine.value.svg.PointerEventsManager;
import io.sf.carte.echosvg.css.engine.value.svg.SVGColorManager;
import io.sf.carte.echosvg.css.engine.value.svg.SVGPaintManager;
import io.sf.carte.echosvg.css.engine.value.svg.ShapeRenderingManager;
import io.sf.carte.echosvg.css.engine.value.svg.SpacingManager;
import io.sf.carte.echosvg.css.engine.value.svg.StrokeDasharrayManager;
import io.sf.carte.echosvg.css.engine.value.svg.StrokeDashoffsetManager;
import io.sf.carte.echosvg.css.engine.value.svg.StrokeLinecapManager;
import io.sf.carte.echosvg.css.engine.value.svg.StrokeLinejoinManager;
import io.sf.carte.echosvg.css.engine.value.svg.StrokeMiterlimitManager;
import io.sf.carte.echosvg.css.engine.value.svg.StrokeWidthManager;
import io.sf.carte.echosvg.css.engine.value.svg.TextAnchorManager;
import io.sf.carte.echosvg.css.engine.value.svg.TextRenderingManager;
import io.sf.carte.echosvg.css.engine.value.svg.WritingModeManager;
import io.sf.carte.echosvg.css.engine.value.svg12.MarginLengthManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class provides a CSS engine initialized for SVG.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGCSSEngine extends CSSEngine {

	/**
	 * Creates a new SVGCSSEngine.
	 * 
	 * @param doc The associated document.
	 * @param uri The document URI.
	 * @param p   The CSS parser to use.
	 * @param ctx The CSS context.
	 */
	public SVGCSSEngine(Document doc, ParsedURL uri, Parser p, CSSContext ctx) {
		super(doc, uri, p, SVG_VALUE_MANAGERS, SVG_SHORTHAND_MANAGERS, null, null, "style", null, "class", true, null,
				ctx);
		lineHeightIndex = LINE_HEIGHT_INDEX;
	}

	/**
	 * Creates a new SVGCSSEngine.
	 * 
	 * @param doc The associated document.
	 * @param uri The document URI.
	 * @param p   The CSS parser to use.
	 * @param vms Extension value managers.
	 * @param sms Extension shorthand managers.
	 * @param ctx The CSS context.
	 */
	public SVGCSSEngine(Document doc, ParsedURL uri, Parser p, ValueManager[] vms, ShorthandManager[] sms,
			CSSContext ctx) {
		super(doc, uri, p, mergeArrays(SVG_VALUE_MANAGERS, vms), mergeArrays(SVG_SHORTHAND_MANAGERS, sms), null, null,
				"style", null, "class", true, null, ctx);
		lineHeightIndex = LINE_HEIGHT_INDEX;
	}

	protected SVGCSSEngine(Document doc, ParsedURL uri, Parser p, ValueManager[] vms, ShorthandManager[] sms,
			String[] pe, String sns, String sln, String cns, String cln, boolean hints, String hintsNS,
			CSSContext ctx) {
		super(doc, uri, p, mergeArrays(SVG_VALUE_MANAGERS, vms), mergeArrays(SVG_SHORTHAND_MANAGERS, sms), pe, sns, sln,
				cns, cln, hints, hintsNS, ctx);
		lineHeightIndex = LINE_HEIGHT_INDEX;
	}

	/**
	 * Merges the given arrays.
	 */
	protected static ValueManager[] mergeArrays(ValueManager[] a1, ValueManager[] a2) {
		ValueManager[] result = new ValueManager[a1.length + a2.length];
		System.arraycopy(a1, 0, result, 0, a1.length);
		System.arraycopy(a2, 0, result, a1.length, a2.length);
		return result;
	}

	/**
	 * Merges the given arrays.
	 */
	protected static ShorthandManager[] mergeArrays(ShorthandManager[] a1, ShorthandManager[] a2) {
		ShorthandManager[] result = new ShorthandManager[a1.length + a2.length];
		System.arraycopy(a1, 0, result, 0, a1.length);
		System.arraycopy(a2, 0, result, a1.length, a2.length);
		return result;
	}

	/**
	 * The value managers for SVG.
	 */
	public static final ValueManager[] SVG_VALUE_MANAGERS = { new AlignmentBaselineManager(),
			new BaselineShiftManager(), new ClipManager(), new ClipPathManager(), new ClipRuleManager(),

			new ColorManager(), new ColorInterpolationManager(), new ColorInterpolationFiltersManager(),
			new ColorProfileManager(), new ColorRenderingManager(),

			new CursorManager(), new DirectionManager(), new DisplayManager(), new DominantBaselineManager(),
			new EnableBackgroundManager(),

			new SVGPaintManager(CSSConstants.CSS_FILL_PROPERTY),
			new OpacityManager(CSSConstants.CSS_FILL_OPACITY_PROPERTY, true), new FillRuleManager(),
			new FilterManager(), new SVGColorManager(CSSConstants.CSS_FLOOD_COLOR_PROPERTY),

			new OpacityManager(CSSConstants.CSS_FLOOD_OPACITY_PROPERTY, false), new FontFamilyManager(),
			new FontSizeManager(), new FontSizeAdjustManager(), new FontStretchManager(),

			new FontStyleManager(), new FontVariantManager(), new FontWeightManager(),
			new GlyphOrientationHorizontalManager(), new GlyphOrientationVerticalManager(),

			new ImageRenderingManager(), new KerningManager(),
			new SpacingManager(CSSConstants.CSS_LETTER_SPACING_PROPERTY),
			new SVGColorManager(CSSConstants.CSS_LIGHTING_COLOR_PROPERTY, ValueConstants.WHITE_RGB_VALUE),
			new LineHeightManager(),

			new MarginLengthManager(CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY),
			new MarginLengthManager(CSSConstants.CSS_MARGIN_LEFT_PROPERTY),
			new MarginLengthManager(CSSConstants.CSS_MARGIN_RIGHT_PROPERTY),
			new MarginLengthManager(CSSConstants.CSS_MARGIN_TOP_PROPERTY),

			new MarkerManager(CSSConstants.CSS_MARKER_END_PROPERTY),
			new MarkerManager(CSSConstants.CSS_MARKER_MID_PROPERTY),
			new MarkerManager(CSSConstants.CSS_MARKER_START_PROPERTY), new MaskManager(),
			new OpacityManager(CSSConstants.CSS_OPACITY_PROPERTY, false), new OverflowManager(),

			new PointerEventsManager(), new SrcManager(), new ShapeRenderingManager(),
			new SVGColorManager(CSSConstants.CSS_STOP_COLOR_PROPERTY),
			new OpacityManager(CSSConstants.CSS_STOP_OPACITY_PROPERTY, false),
			new SVGPaintManager(CSSConstants.CSS_STROKE_PROPERTY, ValueConstants.NONE_VALUE),

			new StrokeDasharrayManager(), new StrokeDashoffsetManager(), new StrokeLinecapManager(),
			new StrokeLinejoinManager(), new StrokeMiterlimitManager(),

			new OpacityManager(CSSConstants.CSS_STROKE_OPACITY_PROPERTY, true), new StrokeWidthManager(),
			new TextAnchorManager(), new TextDecorationManager(), new TextRenderingManager(),

			new UnicodeBidiManager(), new VisibilityManager(),
			new SpacingManager(CSSConstants.CSS_WORD_SPACING_PROPERTY), new WritingModeManager(), };

	/**
	 * The shorthand managers for SVG.
	 */
	public static final ShorthandManager[] SVG_SHORTHAND_MANAGERS = { new FontShorthandManager(),
			new MarginShorthandManager(), new MarkerShorthandManager(), };

	//
	// The property indexes.
	//
	public static final int ALIGNMENT_BASELINE_INDEX = 0;
	public static final int BASELINE_SHIFT_INDEX = ALIGNMENT_BASELINE_INDEX + 1;
	public static final int CLIP_INDEX = BASELINE_SHIFT_INDEX + 1;
	public static final int CLIP_PATH_INDEX = CLIP_INDEX + 1;
	public static final int CLIP_RULE_INDEX = CLIP_PATH_INDEX + 1;

	public static final int COLOR_INDEX = CLIP_RULE_INDEX + 1;
	public static final int COLOR_INTERPOLATION_INDEX = COLOR_INDEX + 1;
	public static final int COLOR_INTERPOLATION_FILTERS_INDEX = COLOR_INTERPOLATION_INDEX + 1;
	public static final int COLOR_PROFILE_INDEX = COLOR_INTERPOLATION_FILTERS_INDEX + 1;
	public static final int COLOR_RENDERING_INDEX = COLOR_PROFILE_INDEX + 1;

	public static final int CURSOR_INDEX = COLOR_RENDERING_INDEX + 1;
	public static final int DIRECTION_INDEX = CURSOR_INDEX + 1;
	public static final int DISPLAY_INDEX = DIRECTION_INDEX + 1;
	public static final int DOMINANT_BASELINE_INDEX = DISPLAY_INDEX + 1;
	public static final int ENABLE_BACKGROUND_INDEX = DOMINANT_BASELINE_INDEX + 1;

	public static final int FILL_INDEX = ENABLE_BACKGROUND_INDEX + 1;
	public static final int FILL_OPACITY_INDEX = FILL_INDEX + 1;
	public static final int FILL_RULE_INDEX = FILL_OPACITY_INDEX + 1;
	public static final int FILTER_INDEX = FILL_RULE_INDEX + 1;
	public static final int FLOOD_COLOR_INDEX = FILTER_INDEX + 1;

	public static final int FLOOD_OPACITY_INDEX = FLOOD_COLOR_INDEX + 1;
	public static final int FONT_FAMILY_INDEX = FLOOD_OPACITY_INDEX + 1;
	public static final int FONT_SIZE_INDEX = FONT_FAMILY_INDEX + 1;
	public static final int FONT_SIZE_ADJUST_INDEX = FONT_SIZE_INDEX + 1;
	public static final int FONT_STRETCH_INDEX = FONT_SIZE_ADJUST_INDEX + 1;

	public static final int FONT_STYLE_INDEX = FONT_STRETCH_INDEX + 1;
	public static final int FONT_VARIANT_INDEX = FONT_STYLE_INDEX + 1;
	public static final int FONT_WEIGHT_INDEX = FONT_VARIANT_INDEX + 1;
	public static final int GLYPH_ORIENTATION_HORIZONTAL_INDEX = FONT_WEIGHT_INDEX + 1;
	public static final int GLYPH_ORIENTATION_VERTICAL_INDEX = GLYPH_ORIENTATION_HORIZONTAL_INDEX + 1;

	public static final int IMAGE_RENDERING_INDEX = GLYPH_ORIENTATION_VERTICAL_INDEX + 1;
	public static final int KERNING_INDEX = IMAGE_RENDERING_INDEX + 1;
	public static final int LETTER_SPACING_INDEX = KERNING_INDEX + 1;
	public static final int LIGHTING_COLOR_INDEX = LETTER_SPACING_INDEX + 1;
	public static final int LINE_HEIGHT_INDEX = LIGHTING_COLOR_INDEX + 1;

	public static final int MARGIN_BOTTOM_INDEX = LINE_HEIGHT_INDEX + 1;
	public static final int MARGIN_LEFT_INDEX = MARGIN_BOTTOM_INDEX + 1;
	public static final int MARGIN_RIGHT_INDEX = MARGIN_LEFT_INDEX + 1;
	public static final int MARGIN_TOP_INDEX = MARGIN_RIGHT_INDEX + 1;
	public static final int MARKER_END_INDEX = MARGIN_TOP_INDEX + 1;

	public static final int MARKER_MID_INDEX = MARKER_END_INDEX + 1;
	public static final int MARKER_START_INDEX = MARKER_MID_INDEX + 1;
	public static final int MASK_INDEX = MARKER_START_INDEX + 1;
	public static final int OPACITY_INDEX = MASK_INDEX + 1;
	public static final int OVERFLOW_INDEX = OPACITY_INDEX + 1;

	public static final int POINTER_EVENTS_INDEX = OVERFLOW_INDEX + 1;
	public static final int SRC_INDEX = POINTER_EVENTS_INDEX + 1;
	public static final int SHAPE_RENDERING_INDEX = SRC_INDEX + 1;
	public static final int STOP_COLOR_INDEX = SHAPE_RENDERING_INDEX + 1;
	public static final int STOP_OPACITY_INDEX = STOP_COLOR_INDEX + 1;
	public static final int STROKE_INDEX = STOP_OPACITY_INDEX + 1;

	public static final int STROKE_DASHARRAY_INDEX = STROKE_INDEX + 1;
	public static final int STROKE_DASHOFFSET_INDEX = STROKE_DASHARRAY_INDEX + 1;
	public static final int STROKE_LINECAP_INDEX = STROKE_DASHOFFSET_INDEX + 1;
	public static final int STROKE_LINEJOIN_INDEX = STROKE_LINECAP_INDEX + 1;
	public static final int STROKE_MITERLIMIT_INDEX = STROKE_LINEJOIN_INDEX + 1;

	public static final int STROKE_OPACITY_INDEX = STROKE_MITERLIMIT_INDEX + 1;
	public static final int STROKE_WIDTH_INDEX = STROKE_OPACITY_INDEX + 1;
	public static final int TEXT_ANCHOR_INDEX = STROKE_WIDTH_INDEX + 1;
	public static final int TEXT_DECORATION_INDEX = TEXT_ANCHOR_INDEX + 1;
	public static final int TEXT_RENDERING_INDEX = TEXT_DECORATION_INDEX + 1;

	public static final int UNICODE_BIDI_INDEX = TEXT_RENDERING_INDEX + 1;
	public static final int VISIBILITY_INDEX = UNICODE_BIDI_INDEX + 1;
	public static final int WORD_SPACING_INDEX = VISIBILITY_INDEX + 1;
	public static final int WRITING_MODE_INDEX = WORD_SPACING_INDEX + 1;
	public static final int FINAL_INDEX = WRITING_MODE_INDEX;

}
