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
package io.sf.carte.echosvg.util;

/**
 * This interface defines constants for CSS. Important: Constants must not
 * contain uppercase characters.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface CSSConstants {

	//
	// The CSS mime-type string.
	//
	String CSS_MIME_TYPE = "text/css";

	//
	// The CSS property names.
	//
	String CSS_ALIGNMENT_BASELINE_PROPERTY = "alignment-baseline";
	String CSS_BASELINE_SHIFT_PROPERTY = "baseline-shift";
	String CSS_CLIP_PROPERTY = "clip";
	String CSS_CLIP_PATH_PROPERTY = "clip-path";
	String CSS_CLIP_RULE_PROPERTY = "clip-rule";
	String CSS_COLOR_PROPERTY = "color";
	String CSS_COLOR_INTERPOLATION_PROPERTY = "color-interpolation";
	String CSS_COLOR_INTERPOLATION_FILTERS_PROPERTY = "color-interpolation-filters";
	String CSS_COLOR_RENDERING_PROPERTY = "color-rendering";
	String CSS_CURSOR_PROPERTY = "cursor";
	String CSS_DIRECTION_PROPERTY = "direction";
	String CSS_DISPLAY_PROPERTY = "display";
	String CSS_DOMINANT_BASELINE_PROPERTY = "dominant-baseline";
	String CSS_ENABLE_BACKGROUND_PROPERTY = "enable-background";
	String CSS_FILL_OPACITY_PROPERTY = "fill-opacity";
	String CSS_FILL_PROPERTY = "fill";
	String CSS_FILL_RULE_PROPERTY = "fill-rule";
	String CSS_FILTER_PROPERTY = "filter";
	String CSS_FLOOD_COLOR_PROPERTY = "flood-color";
	String CSS_FLOOD_OPACITY_PROPERTY = "flood-opacity";
	String CSS_FONT_PROPERTY = "font";
	String CSS_FONT_FAMILY_PROPERTY = "font-family";
	String CSS_FONT_SIZE_PROPERTY = "font-size";
	String CSS_FONT_SIZE_ADJUST_PROPERTY = "font-size-adjust";
	String CSS_FONT_STRETCH_PROPERTY = "font-stretch";
	String CSS_FONT_STYLE_PROPERTY = "font-style";
	String CSS_FONT_VARIANT_PROPERTY = "font-variant";
	String CSS_FONT_WEIGHT_PROPERTY = "font-weight";
	String CSS_GLYPH_ORIENTATION_HORIZONTAL_PROPERTY = "glyph-orientation-horizontal";
	String CSS_GLYPH_ORIENTATION_VERTICAL_PROPERTY = "glyph-orientation-vertical";
	String CSS_IMAGE_RENDERING_PROPERTY = "image-rendering";
	String CSS_KERNING_PROPERTY = "kerning";
	String CSS_LETTER_SPACING_PROPERTY = "letter-spacing";
	String CSS_LIGHTING_COLOR_PROPERTY = "lighting-color";
	String CSS_LINE_HEIGHT_PROPERTY = "line-height";
	/** Property name for margin shorthand */
	String CSS_MARGIN_PROPERTY = "margin";
	/** Property name for top-margin */
	String CSS_MARGIN_TOP_PROPERTY = "margin-top";
	/** Property name for right-margin */
	String CSS_MARGIN_RIGHT_PROPERTY = "margin-right";
	/** Property name for bottom-margin */
	String CSS_MARGIN_BOTTOM_PROPERTY = "margin-bottom";
	/** Property name for left-margin */
	String CSS_MARGIN_LEFT_PROPERTY = "margin-left";
	String CSS_MARKER_PROPERTY = "marker";
	String CSS_MARKER_END_PROPERTY = "marker-end";
	String CSS_MARKER_MID_PROPERTY = "marker-mid";
	String CSS_MARKER_START_PROPERTY = "marker-start";
	String CSS_MASK_PROPERTY = "mask";
	String CSS_OPACITY_PROPERTY = "opacity";
	String CSS_OVERFLOW_PROPERTY = "overflow";
	String CSS_POINTER_EVENTS_PROPERTY = "pointer-events";
	String CSS_SHAPE_RENDERING_PROPERTY = "shape-rendering";
	String CSS_SRC_PROPERTY = "src";
	String CSS_STOP_COLOR_PROPERTY = "stop-color";
	String CSS_STOP_OPACITY_PROPERTY = "stop-opacity";
	String CSS_STROKE_PROPERTY = "stroke";
	String CSS_STROKE_DASHARRAY_PROPERTY = "stroke-dasharray";
	String CSS_STROKE_DASHOFFSET_PROPERTY = "stroke-dashoffset";
	String CSS_STROKE_LINECAP_PROPERTY = "stroke-linecap";
	String CSS_STROKE_LINEJOIN_PROPERTY = "stroke-linejoin";
	String CSS_STROKE_MITERLIMIT_PROPERTY = "stroke-miterlimit";
	String CSS_STROKE_OPACITY_PROPERTY = "stroke-opacity";
	String CSS_STROKE_WIDTH_PROPERTY = "stroke-width";
	String CSS_TEXT_ANCHOR_PROPERTY = "text-anchor";
	String CSS_TEXT_DECORATION_PROPERTY = "text-decoration";
	String CSS_TEXT_RENDERING_PROPERTY = "text-rendering";
	String CSS_UNICODE_BIDI_PROPERTY = "unicode-bidi";
	String CSS_VISIBILITY_PROPERTY = "visibility";
	String CSS_WORD_SPACING_PROPERTY = "word-spacing";
	String CSS_WRITING_MODE_PROPERTY = "writing-mode";

	//
	// The CSS property values.
	//
	String CSS_ACCUMULATE_VALUE = "accumulate";
	String CSS_ACTIVEBORDER_VALUE = "activeborder";
	String CSS_ACTIVECAPTION_VALUE = "activecaption";
	String CSS_AFTER_EDGE_VALUE = "after-edge";
	String CSS_ALICEBLUE_VALUE = "aliceblue";
	String CSS_ALL_VALUE = "all";
	String CSS_ALPHABETIC_VALUE = "alphabetic";
	String CSS_ANTIQUEWHITE_VALUE = "antiquewhite";
	String CSS_APPWORKSPACE_VALUE = "appworkspace";
	String CSS_AQUAMARINE_VALUE = "aquamarine";
	String CSS_AQUA_VALUE = "aqua";
	String CSS_AUTOSENSE_SCRIPT_VALUE = "autosense-script";
	String CSS_AUTO_VALUE = "auto";
	String CSS_AZURE_VALUE = "azure";
	String CSS_BACKGROUND_VALUE = "background";
	String CSS_BASELINE_VALUE = "baseline";
	String CSS_BEFORE_EDGE_VALUE = "before-edge";
	String CSS_BEIGE_VALUE = "beige";
	String CSS_BEVEL_VALUE = "bevel";
	String CSS_BIDI_OVERRIDE_VALUE = "bidi-override";
	String CSS_BISQUE_VALUE = "bisque";
	String CSS_BLACK_VALUE = "black";
	String CSS_BLANCHEDALMOND_VALUE = "blanchedalmond";
	String CSS_BLINK_VALUE = "blink";
	String CSS_BLOCK_VALUE = "block";
	String CSS_BLUEVIOLET_VALUE = "blueviolet";
	String CSS_BLUE_VALUE = "blue";
	String CSS_BOLDER_VALUE = "bolder";
	String CSS_BOLD_VALUE = "bold";
	String CSS_BOTTOM_VALUE = "bottom";
	String CSS_BROWN_VALUE = "brown";
	String CSS_BURLYWOOD_VALUE = "burlywood";
	String CSS_BUTTONFACE_VALUE = "buttonface";
	String CSS_BUTTONHIGHLIGHT_VALUE = "buttonhighlight";
	String CSS_BUTTONSHADOW_VALUE = "buttonshadow";
	String CSS_BUTTONTEXT_VALUE = "buttontext";
	String CSS_BUTT_VALUE = "butt";
	String CSS_CADETBLUE_VALUE = "cadetblue";
	String CSS_CAPTIONTEXT_VALUE = "captiontext";
	String CSS_CAPTION_VALUE = "caption";
	String CSS_CENTER_VALUE = "center";
	String CSS_CENTRAL_VALUE = "central";
	String CSS_CHARTREUSE_VALUE = "chartreuse";
	String CSS_CHOCOLATE_VALUE = "chocolate";
	String CSS_COLLAPSE_VALUE = "collapse";
	String CSS_COMPACT_VALUE = "compact";
	String CSS_CONDENSED_VALUE = "condensed";
	String CSS_CORAL_VALUE = "coral";
	String CSS_CORNFLOWERBLUE_VALUE = "cornflowerblue";
	String CSS_CORNSILK_VALUE = "cornsilk";
	String CSS_CRIMSON_VALUE = "crimson";
	String CSS_CRISPEDGES_VALUE = "crispedges";
	String CSS_CROSSHAIR_VALUE = "crosshair";
	String CSS_CURRENTCOLOR_VALUE = "currentcolor";
	String CSS_CURSIVE_VALUE = "cursive";
	String CSS_CYAN_VALUE = "cyan";
	String CSS_DARKBLUE_VALUE = "darkblue";
	String CSS_DARKCYAN_VALUE = "darkcyan";
	String CSS_DARKGOLDENROD_VALUE = "darkgoldenrod";
	String CSS_DARKGRAY_VALUE = "darkgray";
	String CSS_DARKGREEN_VALUE = "darkgreen";
	String CSS_DARKGREY_VALUE = "darkgrey";
	String CSS_DARKKHAKI_VALUE = "darkkhaki";
	String CSS_DARKMAGENTA_VALUE = "darkmagenta";
	String CSS_DARKOLIVEGREEN_VALUE = "darkolivegreen";
	String CSS_DARKORANGE_VALUE = "darkorange";
	String CSS_DARKORCHID_VALUE = "darkorchid";
	String CSS_DARKRED_VALUE = "darkred";
	String CSS_DARKSALMON_VALUE = "darksalmon";
	String CSS_DARKSEAGREEN_VALUE = "darkseagreen";
	String CSS_DARKSLATEBLUE_VALUE = "darkslateblue";
	String CSS_DARKSLATEGRAY_VALUE = "darkslategray";
	String CSS_DARKSLATEGREY_VALUE = "darkslategrey";
	String CSS_DARKTURQUOISE_VALUE = "darkturquoise";
	String CSS_DARKVIOLET_VALUE = "darkviolet";
	String CSS_DEEPPINK_VALUE = "deeppink";
	String CSS_DEEPSKYBLUE_VALUE = "deepskyblue";
	String CSS_DEFAULT_VALUE = "default";
	String CSS_DIMGRAY_VALUE = "dimgray";
	String CSS_DIMGREY_VALUE = "dimgrey";
	String CSS_DODGERBLUE_VALUE = "dodgerblue";
	String CSS_EMBED_VALUE = "embed";
	/** Value for text-align to end of region */
	String CSS_END_VALUE = "end";
	String CSS_EVENODD_VALUE = "evenodd";
	String CSS_EXPANDED_VALUE = "expanded";
	String CSS_EXTRA_CONDENSED_VALUE = "extra-condensed";
	String CSS_EXTRA_EXPANDED_VALUE = "extra-expanded";
	String CSS_E_RESIZE_VALUE = "e-resize";
	String CSS_FANTASY_VALUE = "fantasy";
	String CSS_FILLSTROKE_VALUE = "fillstroke";
	String CSS_FILL_VALUE = "fill";
	String CSS_FIREBRICK_VALUE = "firebrick";
	String CSS_FLORALWHITE_VALUE = "floralwhite";
	String CSS_FORESTGREEN_VALUE = "forestgreen";
	String CSS_FUCHSIA_VALUE = "fuchsia";
	String CSS_GAINSBORO_VALUE = "gainsboro";
	String CSS_GEOMETRICPRECISION_VALUE = "geometricprecision";
	String CSS_GHOSTWHITE_VALUE = "ghostwhite";
	String CSS_GOLDENROD_VALUE = "goldenrod";
	String CSS_GOLD_VALUE = "gold";
	String CSS_GRAYTEXT_VALUE = "graytext";
	String CSS_GRAY_VALUE = "gray";
	String CSS_GREENYELLOW_VALUE = "greenyellow";
	String CSS_GREEN_VALUE = "green";
	String CSS_GREY_VALUE = "grey";
	String CSS_HANGING_VALUE = "hanging";
	String CSS_HELP_VALUE = "help";
	String CSS_HIDDEN_VALUE = "hidden";
	String CSS_HIGHLIGHTTEXT_VALUE = "highlighttext";
	String CSS_HIGHLIGHT_VALUE = "highlight";
	String CSS_HONEYDEW_VALUE = "honeydew";
	String CSS_HOTPINK_VALUE = "hotpink";
	String CSS_ICON_VALUE = "icon";
	String CSS_IDEOGRAPHIC_VALUE = "ideographic";
	String CSS_INACTIVEBORDER_VALUE = "inactiveborder";
	String CSS_INACTIVECAPTIONTEXT_VALUE = "inactivecaptiontext";
	String CSS_INACTIVECAPTION_VALUE = "inactivecaption";
	String CSS_INDIANRED_VALUE = "indianred";
	String CSS_INDIGO_VALUE = "indigo";
	String CSS_INFOBACKGROUND_VALUE = "infobackground";
	String CSS_INFOTEXT_VALUE = "infotext";
	String CSS_INLINE_TABLE_VALUE = "inline-table";
	String CSS_INLINE_BLOCK_VALUE = "inline-block";
	String CSS_INLINE_VALUE = "inline";
	String CSS_ITALIC_VALUE = "italic";
	String CSS_IVORY_VALUE = "ivory";
	String CSS_KHAKI_VALUE = "khaki";
	String CSS_LARGER_VALUE = "larger";
	String CSS_LARGE_VALUE = "large";
	String CSS_LAVENDERBLUSH_VALUE = "lavenderblush";
	String CSS_LAVENDER_VALUE = "lavender";
	String CSS_LAWNGREEN_VALUE = "lawngreen";
	String CSS_LEMONCHIFFON_VALUE = "lemonchiffon";
	String CSS_LIGHTBLUE_VALUE = "lightblue";
	String CSS_LIGHTCORAL_VALUE = "lightcoral";
	String CSS_LIGHTCYAN_VALUE = "lightcyan";
	String CSS_LIGHTER_VALUE = "lighter";
	String CSS_LIGHTGOLDENRODYELLOW_VALUE = "lightgoldenrodyellow";
	String CSS_LIGHTGRAY_VALUE = "lightgray";
	String CSS_LIGHTGREEN_VALUE = "lightgreen";
	String CSS_LIGHTGREY_VALUE = "lightgrey";
	String CSS_LIGHTPINK_VALUE = "lightpink";
	String CSS_LIGHTSALMON_VALUE = "lightsalmon";
	String CSS_LIGHTSEAGREEN_VALUE = "lightseagreen";
	String CSS_LIGHTSKYBLUE_VALUE = "lightskyblue";
	String CSS_LIGHTSLATEGRAY_VALUE = "lightslategray";
	String CSS_LIGHTSLATEGREY_VALUE = "lightslategrey";
	String CSS_LIGHTSTEELBLUE_VALUE = "lightsteelblue";
	String CSS_LIGHTYELLOW_VALUE = "lightyellow";
	String CSS_LIMEGREEN_VALUE = "limegreen";
	String CSS_LIME_VALUE = "lime";
	String CSS_LINEARRGB_VALUE = "linearrgb";
	String CSS_LINEN_VALUE = "linen";
	String CSS_LINE_THROUGH_VALUE = "line-through";
	String CSS_LIST_ITEM_VALUE = "list-item";
	String CSS_LOWER_VALUE = "lower";
	String CSS_LR_TB_VALUE = "lr-tb";
	String CSS_LR_VALUE = "lr";
	String CSS_LTR_VALUE = "ltr";
	String CSS_MAGENTA_VALUE = "magenta";
	String CSS_MARKER_VALUE = "marker";
	String CSS_MAROON_VALUE = "maroon";
	String CSS_MATHEMATICAL_VALUE = "mathematical";
	String CSS_MEDIUMAQUAMARINE_VALUE = "mediumaquamarine";
	String CSS_MEDIUMBLUE_VALUE = "mediumblue";
	String CSS_MEDIUMORCHID_VALUE = "mediumorchid";
	String CSS_MEDIUMPURPLE_VALUE = "mediumpurple";
	String CSS_MEDIUMSEAGREEN_VALUE = "mediumseagreen";
	String CSS_MEDIUMSLATEBLUE_VALUE = "mediumslateblue";
	String CSS_MEDIUMSPRINGGREEN_VALUE = "mediumspringgreen";
	String CSS_MEDIUMTURQUOISE_VALUE = "mediumturquoise";
	String CSS_MEDIUMVIOLETRED_VALUE = "mediumvioletred";
	String CSS_MEDIUM_VALUE = "medium";
	String CSS_MENU_VALUE = "menu";
	String CSS_MENUTEXT_VALUE = "menutext";
	String CSS_MESSAGE_BOX_VALUE = "message-box";
	String CSS_MIDDLE_VALUE = "middle";
	String CSS_MIDNIGHTBLUE_VALUE = "midnightblue";
	String CSS_MINTCREAM_VALUE = "mintcream";
	String CSS_MISTYROSE_VALUE = "mistyrose";
	String CSS_MITER_VALUE = "miter";
	String CSS_MOCCASIN_VALUE = "moccasin";
	String CSS_MONOSPACE_VALUE = "monospace";
	String CSS_MOVE_VALUE = "move";
	String CSS_NARROWER_VALUE = "narrower";
	String CSS_NAVAJOWHITE_VALUE = "navajowhite";
	String CSS_NAVY_VALUE = "navy";
	String CSS_NEW_VALUE = "new";
	String CSS_NE_RESIZE_VALUE = "ne-resize";
	String CSS_NONE_VALUE = "none";
	String CSS_NONZERO_VALUE = "nonzero";
	String CSS_NORMAL_VALUE = "normal";
	String CSS_NO_CHANGE_VALUE = "no-change";
	String CSS_NW_RESIZE_VALUE = "nw-resize";
	String CSS_N_RESIZE_VALUE = "n-resize";
	String CSS_OBLIQUE_VALUE = "oblique";
	String CSS_OLDLACE_VALUE = "oldlace";
	String CSS_OLIVEDRAB_VALUE = "olivedrab";
	String CSS_OLIVE_VALUE = "olive";
	String CSS_OPTIMIZELEGIBILITY_VALUE = "optimizelegibility";
	String CSS_OPTIMIZEQUALITY_VALUE = "optimizequality";
	String CSS_OPTIMIZESPEED_VALUE = "optimizespeed";
	String CSS_ORANGERED_VALUE = "orangered";
	String CSS_ORANGE_VALUE = "orange";
	String CSS_ORCHID_VALUE = "orchid";
	String CSS_OVERLINE_VALUE = "overline";
	String CSS_PAINTED_VALUE = "painted";
	String CSS_PALEGOLDENROD_VALUE = "palegoldenrod";
	String CSS_PALEGREEN_VALUE = "palegreen";
	String CSS_PALETURQUOISE_VALUE = "paleturquoise";
	String CSS_PALEVIOLETRED_VALUE = "palevioletred";
	String CSS_PAPAYAWHIP_VALUE = "papayawhip";
	String CSS_PEACHPUFF_VALUE = "peachpuff";
	String CSS_PERU_VALUE = "peru";
	String CSS_PINK_VALUE = "pink";
	String CSS_PLUM_VALUE = "plum";
	String CSS_POINTER_VALUE = "pointer";
	String CSS_POWDERBLUE_VALUE = "powderblue";
	String CSS_PURPLE_VALUE = "purple";
	String CSS_RED_VALUE = "red";
	String CSS_RESET_SIZE_VALUE = "reset-size";
	String CSS_RESET_VALUE = "reset";
	String CSS_RL_TB_VALUE = "rl-tb";
	String CSS_RL_VALUE = "rl";
	String CSS_ROSYBROWN_VALUE = "rosybrown";
	String CSS_ROUND_VALUE = "round";
	String CSS_ROYALBLUE_VALUE = "royalblue";
	String CSS_RTL_VALUE = "rtl";
	String CSS_RUN_IN_VALUE = "run-in";
	String CSS_SADDLEBROWN_VALUE = "saddlebrown";
	String CSS_SALMON_VALUE = "salmon";
	String CSS_SANDYBROWN_VALUE = "sandybrown";
	String CSS_SANS_SERIF_VALUE = "sans-serif";
	String CSS_SCROLLBAR_VALUE = "scrollbar";
	String CSS_SCROLL_VALUE = "scroll";
	String CSS_SEAGREEN_VALUE = "seagreen";
	String CSS_SEASHELL_VALUE = "seashell";
	String CSS_SEMI_CONDENSED_VALUE = "semi-condensed";
	String CSS_SEMI_EXPANDED_VALUE = "semi-expanded";
	String CSS_SERIF_VALUE = "serif";
	String CSS_SE_RESIZE_VALUE = "se-resize";
	String CSS_SIENNA_VALUE = "sienna";
	String CSS_SILVER_VALUE = "silver";
	String CSS_SKYBLUE_VALUE = "skyblue";
	String CSS_SLATEBLUE_VALUE = "slateblue";
	String CSS_SLATEGRAY_VALUE = "slategray";
	String CSS_SLATEGREY_VALUE = "slategrey";
	String CSS_SMALLER_VALUE = "smaller";
	String CSS_SMALL_CAPS_VALUE = "small-caps";
	String CSS_SMALL_CAPTION_VALUE = "small-caption";
	String CSS_SMALL_VALUE = "small";
	String CSS_SNOW_VALUE = "snow";
	String CSS_SPRINGGREEN_VALUE = "springgreen";
	String CSS_SQUARE_VALUE = "square";
	String CSS_SRGB_VALUE = "srgb";
	/** Value for text-align to start of text on line */
	String CSS_START_VALUE = "start";
	String CSS_STATUS_BAR_VALUE = "status-bar";
	String CSS_STEELBLUE_VALUE = "steelblue";
	String CSS_STROKE_VALUE = "stroke";
	String CSS_SUB_VALUE = "sub";
	String CSS_SUPER_VALUE = "super";
	String CSS_SW_RESIZE_VALUE = "sw-resize";
	String CSS_S_RESIZE_VALUE = "s-resize";
	String CSS_TABLE_CAPTION_VALUE = "table-caption";
	String CSS_TABLE_CELL_VALUE = "table-cell";
	String CSS_TABLE_COLUMN_GROUP_VALUE = "table-column-group";
	String CSS_TABLE_COLUMN_VALUE = "table-column";
	String CSS_TABLE_FOOTER_GROUP_VALUE = "table-footer-group";
	String CSS_TABLE_HEADER_GROUP_VALUE = "table-header-group";
	String CSS_TABLE_ROW_GROUP_VALUE = "table-row-group";
	String CSS_TABLE_ROW_VALUE = "table-row";
	String CSS_TABLE_VALUE = "table";
	String CSS_TAN_VALUE = "tan";
	String CSS_TB_RL_VALUE = "tb-rl";
	String CSS_TB_VALUE = "tb";
	String CSS_TEAL_VALUE = "teal";
	String CSS_TEXT_AFTER_EDGE_VALUE = "text-after-edge";
	String CSS_TEXT_BEFORE_EDGE_VALUE = "text-before-edge";
	String CSS_TEXT_BOTTOM_VALUE = "text-bottom";
	String CSS_TEXT_TOP_VALUE = "text-top";
	String CSS_TEXT_VALUE = "text";
	String CSS_THISTLE_VALUE = "thistle";
	String CSS_THREEDDARKSHADOW_VALUE = "threeddarkshadow";
	String CSS_THREEDFACE_VALUE = "threedface";
	String CSS_THREEDHIGHLIGHT_VALUE = "threedhighlight";
	String CSS_THREEDLIGHTSHADOW_VALUE = "threedlightshadow";
	String CSS_THREEDSHADOW_VALUE = "threedshadow";
	String CSS_TOMATO_VALUE = "tomato";
	String CSS_TOP_VALUE = "top";
	String CSS_TRANSPARENT_VALUE = "transparent";
	String CSS_TURQUOISE_VALUE = "turquoise";
	String CSS_ULTRA_CONDENSED_VALUE = "ultra-condensed";
	String CSS_ULTRA_EXPANDED_VALUE = "ultra-expanded";
	String CSS_UNDERLINE_VALUE = "underline";
	String CSS_USE_SCRIPT_VALUE = "use-script";
	String CSS_VIOLET_VALUE = "violet";
	String CSS_VISIBLEFILLSTROKE_VALUE = "visiblefillstroke";
	String CSS_VISIBLEFILL_VALUE = "visiblefill";
	String CSS_VISIBLEPAINTED_VALUE = "visiblepainted";
	String CSS_VISIBLESTROKE_VALUE = "visiblestroke";
	String CSS_VISIBLE_VALUE = "visible";
	String CSS_WAIT_VALUE = "wait";
	String CSS_WHEAT_VALUE = "wheat";
	String CSS_WHITESMOKE_VALUE = "whitesmoke";
	String CSS_WHITE_VALUE = "white";
	String CSS_WIDER_VALUE = "wider";
	String CSS_WINDOWFRAME_VALUE = "windowframe";
	String CSS_WINDOWTEXT_VALUE = "windowtext";
	String CSS_WINDOW_VALUE = "window";
	String CSS_W_RESIZE_VALUE = "w-resize";
	String CSS_XX_LARGE_VALUE = "xx-large";
	String CSS_XX_SMALL_VALUE = "xx-small";
	String CSS_X_LARGE_VALUE = "x-large";
	String CSS_X_SMALL_VALUE = "x-small";
	String CSS_YELLOWGREEN_VALUE = "yellowgreen";
	String CSS_YELLOW_VALUE = "yellow";

}
