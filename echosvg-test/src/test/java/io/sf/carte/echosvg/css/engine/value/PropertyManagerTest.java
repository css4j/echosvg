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

package io.sf.carte.echosvg.css.engine.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.StringTokenizer;

import org.junit.Test;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.echosvg.css.engine.value.svg.MarkerManager;
import io.sf.carte.echosvg.css.engine.value.svg.OpacityManager;
import io.sf.carte.echosvg.css.engine.value.svg.SVGColorManager;
import io.sf.carte.echosvg.css.engine.value.svg.SVGPaintManager;
import io.sf.carte.echosvg.css.engine.value.svg.SpacingManager;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * The class to test the CSS properties's manager.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PropertyManagerTest {

	/**
	 * Runs this test.
	 */
	@Test
	public void test() throws Exception {
		// css.engine.value.svg.alignement-baseline
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.AlignmentBaselineManager", false, "auto",
				"auto | baseline | before-edge | text-before-edge | middle | after-edge | text-after-edge | ideographic | alphabetic | hanging | mathematical");

		// css.engine.value.svg.baseline-shift
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.BaselineShiftManager", false, "baseline",
				"baseline | sub | super");

		// css.engine.value.css2.clip
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.ClipManager", false, "auto", "auto");

		// css.engine.value.svg.clip-path
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ClipPathManager", false, "none", "none");

		// css.engine.value.svg.clip-rule
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ClipRuleManager", true, "nonzero",
				"nonzero | evenodd");

		// css.engine.value.svg.color
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ColorManager", true, "__USER_AGENT__", "");

		// css.engine.value.svg.color-interpolation
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ColorInterpolationManager", true, "sRGB",
				"auto | sRGB | linearRGB");

		// css.engine.value.svg.color-interpolation-filters
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ColorInterpolationFiltersManager", true,
				"linearRGB", "auto | sRGB | linearRGB");

		// css.engine.value.svg.color-profile
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ColorProfileManager", true, "auto",
				"auto | sRGB");

		// css.engine.value.svg.color-rendering
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ColorRenderingManager", true, "auto",
				"auto | optimizeSpeed | optimizeQuality");

		// css.engine.value.css2.cursor
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.CursorManager", true, "auto",
				"auto | crosshair | default | pointer | move | e-resize | ne-resize | nw-resize | n-resize | se-resize | sw-resize | s-resize | w-resize| text | wait | help ");

		// css.engine.value.css2.direction
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.DirectionManager", true, "ltr", "ltr | rtl");

		// css.engine.value.css2.display
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.DisplayManager", false, "inline",
				"inline | block | list-item | run-in | compact | marker | table | inline-table | table-row-group | table-header-group | table-footer-group | table-row | table-column-group | table-column | table-cell | table-caption | none");

		// css.engine.value.svg.dominant-baseline
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.DominantBaselineManager", false, "auto",
				"auto | use-script | no-change | reset-size | alphabetic | hanging | ideographic | mathematical | central | middle | text-after-edge | text-before-edge | text-top | text-bottom");

		// css.engine.value.svg.enable-background
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.EnableBackgroundManager", false, "accumulate",
				"accumulate");

		// css.engine.value.svg.fill
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$FillManager", true,
				"rgb(0, 0, 0)", "");

		// css.engine.value.svg.fill-opacity
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$FillOpacityManager", true, "1",
				"");

		// css.engine.value.svg.fill-rule
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.FillRuleManager", true, "nonzero",
				"nonzero | evenodd");

		// css.engine.value.svg.filter
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.FilterManager", false, "none", "none");

		// css.engine.value.svg.flood-color
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$FloodColorManager", false,
				"rgb(0, 0, 0)", "currentColor");

		// css.engine.value.svg.flood-opacity
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$FloodOpacityManager", false, "1",
				"");

		// IGNORE
		// css.engine.value.css2.font
		// testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontManager",
		// true, "1", "");

		// css.engine.value.css2.font-family
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontFamilyManager", true, "__USER_AGENT__", "");

		// css.engine.value.css2.font-size
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontSizeManager", true, "medium", "medium");

		// css.engine.value.css2.font-size-adjust
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontSizeAdjustManager", true, "none", "none");

		// css.engine.value.css2.font-stretch
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontStretchManager", true, "normal",
				"normal | wider | narrower | ultra-condensed | extra-condensed | condensed | semi-condensed | semi-expanded | expanded | extra-expanded | ultra-expanded");

		// css.engine.value.css2.font-style
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontStyleManager", true, "normal",
				"normal | italic | oblique");

		// css.engine.value.css2.font-variant
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontVariantManager", true, "normal",
				"normal | small-caps");

		// css.engine.value.css2.font-weigth
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.FontWeightManager", true, "normal",
				"normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900");

		// css.engine.value.svg.glyph-orientation-horizontal
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.GlyphOrientationHorizontalManager", true, "0deg",
				"");

		// css.engine.value.svg.glyph-orientation-vertical
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.GlyphOrientationVerticalManager", true, "auto",
				"auto");

		// css.engine.value.svg.image-rendering
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ImageRenderingManager", true, "auto",
				"auto | optimizeSpeed | optimizeQuality");

		// css.engine.value.svg.kerning
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.KerningManager", true, "auto", "auto");

		// css.engine.value.svg.letter-spacing
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$LetterSpacingManager", true,
				"normal", "normal");

		// css.engine.value.svg.lighting-color
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$LightingColorManager", false,
				"rgb(255, 255, 255)", "currentColor");

		// IGNORE
		// css.engine.value.svg.marker
		// testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.MarkerManager",
		// true, "", "");

		// css.engine.value.svg.marker-start
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$MarkerStartManager", true, "none",
				"none");

		// css.engine.value.svg.marker-mid
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$MarkerMidManager", true, "none",
				"none");

		// css.engine.value.svg.marker-end
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$MarkerEndManager", true, "none",
				"none");

		// css.engine.value.svg.mask
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.MaskManager", false, "none", "none");

		// css.engine.value.svg.opacity
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$DefaultOpacityManager", false,
				"1", "");

		// css.engine.value.css2.overflow
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.OverflowManager", false, "visible",
				"visible | hidden | scroll | auto");

		// css.engine.value.svg.pointer-events
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.PointerEventsManager", true, "visiblePainted",
				"visiblePainted | visibleFill | visibleStroke | visible | painted | fill | stroke | all | none");

		// css.engine.value.svg.shape-rendering
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.ShapeRenderingManager", true, "auto",
				"auto | optimizeSpeed | crispEdges | geometricPrecision");

		// css.engine.value.svg.stop-color
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$StopColorManager", false,
				"rgb(0, 0, 0)", "");

		// css.engine.value.svg.stop-opacity
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$StopOpacityManager", false, "1",
				"");

		// css.engine.value.svg.stroke
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$StrokeManager", true, "none",
				"none");

		// css.engine.value.svg.stroke-dasharray
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.StrokeDasharrayManager", true, "none", "none");

		// css.engine.value.svg.stroke-dashoffset
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.StrokeDashoffsetManager", true, "0", "0");

		// css.engine.value.svg.stroke-linecap
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.StrokeLinecapManager", true, "butt",
				"butt | round | square");

		// css.engine.value.svg.stroke-linejoin
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.StrokeLinejoinManager", true, "miter",
				"miter | round | bevel");

		// css.engine.value.svg.stroke-miterlimit
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.StrokeMiterlimitManager", true, "4", "");

		// css.engine.value.svg.stroke-opacity
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$StrokeOpacityManager", true, "1",
				"");

		// css.engine.value.svg.stroke-width
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.StrokeWidthManager", true, "1", "");

		// css.engine.value.svg.text-anchor
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.TextAnchorManager", true, "start",
				"start | middle | end");

		// css.engine.value.svg.text-decoration
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.TextDecorationManager", false, "none",
				"none | underline | overline | line-through | blink");

		// css.engine.value.svg.text-rendering
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.TextRenderingManager", true, "auto",
				"auto | optimizeSpeed | optimizeLegibility | geometricPrecision");

		// css.engine.value.svg.unicode-bidi
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.UnicodeBidiManager", false, "normal",
				"normal | embed | bidi-override");

		// css.engine.value.css2.visibility
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.css2.VisibilityManager", true, "visible",
				"visible | hidden | collapse");

		// css.engine.value.css2.word-spacing
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.PropertyManagerTest$WordSpacingManager", true,
				"normal", "normal");

		// css.engine.value.css2.writing-mode
		testPropertyManager("io.sf.carte.echosvg.css.engine.value.svg.WritingModeManager", true, "lr-tb",
				"lr-tb | rl-tb | tb-rl | lr | rl | tb");
	}

	/**
	 * Creates the value manager.
	 */
	private ValueManager createValueManager(String managerClassName) throws Exception {
		return (ValueManager) Class.forName(managerClassName).getDeclaredConstructor().newInstance();
	}

	/**
	 * Constructs a new test for the specified manager classname.
	 *
	 * @param managerClassName the classname of the manager to test
	 * @param isInherited      the expected flag to see if the property is inherited
	 * @param defaultValue     the default value
	 * @param identValueList   the list of possible identifiers
	 * @throws Exception
	 */
	void testPropertyManager(String managerClassName, Boolean isInherited, String defaultValue, String identValueList)
			throws Exception {

		/**
		 * The candidate values of the property.
		 */
		String[] identValues;

		StringTokenizer tokens = new StringTokenizer(identValueList, "|");
		int nbIdentValue = tokens.countTokens();
		if (nbIdentValue > 0) {
			identValues = new String[nbIdentValue];
			for (int i = 0; tokens.hasMoreTokens(); ++i) {
				identValues[i] = tokens.nextToken().trim();
			}
		} else {
			identValues = null;
		}

		ValueManager manager = createValueManager(managerClassName);

		// test default value if any
		if (!defaultValue.equals("__USER_AGENT__")) {
			String s = manager.getDefaultValue().getCssText();
			assertTrue(defaultValue.equalsIgnoreCase(s));
		}

		// test if the property is inherited or not
		assertTrue(isInherited == manager.isInheritedProperty());

		Parser cssParser = new CSSParser();
		// see if the property supports the value 'inherit'
		LexicalUnit lu = cssParser.parsePropertyValue(new StringReader("inherit"));
		Value v = manager.createValue(lu, null);
		String s = v.getCssText();
		assertEquals("inherit", s);

		// test all possible identifiers
		if (identValues != null) {
			for (String identValue : identValues) {
				lu = cssParser.parsePropertyValue(new StringReader(identValue));
				v = manager.createValue(lu, null);
				s = v.getCssText();
				if (!s.equalsIgnoreCase(identValue)) {
					assertEquals(identValue, s);
				}
			}
		}
	}

	/**
	 * Manager for 'fill'.
	 */
	public static class FillManager extends SVGPaintManager {
		public FillManager() {
			super(CSSConstants.CSS_FILL_PROPERTY);
		}
	}

	/**
	 * Manager for 'fill-opacity'.
	 */
	public static class FillOpacityManager extends OpacityManager {
		public FillOpacityManager() {
			super(CSSConstants.CSS_FILL_OPACITY_PROPERTY, true);
		}
	}

	/**
	 * Manager for 'flood-color'.
	 */
	public static class FloodColorManager extends SVGColorManager {
		public FloodColorManager() {
			super(CSSConstants.CSS_FLOOD_COLOR_PROPERTY);
		}
	}

	/**
	 * Manager for 'flood-opacity'.
	 */
	public static class FloodOpacityManager extends OpacityManager {
		public FloodOpacityManager() {
			super(CSSConstants.CSS_FLOOD_OPACITY_PROPERTY, false);
		}
	}

	/**
	 * Manager for 'letter-spacing'.
	 */
	public static class LetterSpacingManager extends SpacingManager {
		public LetterSpacingManager() {
			super(CSSConstants.CSS_LETTER_SPACING_PROPERTY);
		}
	}

	/**
	 * Manager for 'lighting-color'.
	 */
	public static class LightingColorManager extends SVGColorManager {
		public LightingColorManager() {
			super(CSSConstants.CSS_LIGHTING_COLOR_PROPERTY, ValueConstants.WHITE_RGB_VALUE);
		}
	}

	/**
	 * Manager for 'marker-end'.
	 */
	public static class MarkerEndManager extends MarkerManager {
		public MarkerEndManager() {
			super(CSSConstants.CSS_MARKER_END_PROPERTY);
		}
	}

	/**
	 * Manager for 'marker-mid'.
	 */
	public static class MarkerMidManager extends MarkerManager {
		public MarkerMidManager() {
			super(CSSConstants.CSS_MARKER_MID_PROPERTY);
		}
	}

	/**
	 * Manager for 'marker-start'.
	 */
	public static class MarkerStartManager extends MarkerManager {
		public MarkerStartManager() {
			super(CSSConstants.CSS_MARKER_START_PROPERTY);
		}
	}

	/**
	 * Manager for 'opacity'.
	 */
	public static class DefaultOpacityManager extends OpacityManager {
		public DefaultOpacityManager() {
			super(CSSConstants.CSS_OPACITY_PROPERTY, false);
		}
	}

	/**
	 * Manager for 'stop-color'.
	 */
	public static class StopColorManager extends SVGColorManager {
		public StopColorManager() {
			super(CSSConstants.CSS_STOP_COLOR_PROPERTY);
		}
	}

	/**
	 * Manager for 'stop-opacity'.
	 */
	public static class StopOpacityManager extends OpacityManager {
		public StopOpacityManager() {
			super(CSSConstants.CSS_STOP_OPACITY_PROPERTY, false);
		}
	}

	/**
	 * Manager for 'stroke'.
	 */
	public static class StrokeManager extends SVGPaintManager {
		public StrokeManager() {
			super(CSSConstants.CSS_STROKE_PROPERTY, ValueConstants.NONE_VALUE);
		}
	}

	/**
	 * Manager for 'stroke-opacity'.
	 */
	public static class StrokeOpacityManager extends OpacityManager {
		public StrokeOpacityManager() {
			super(CSSConstants.CSS_STROKE_OPACITY_PROPERTY, true);
		}
	}

	/**
	 * Manager for 'word-spacing'.
	 */
	public static class WordSpacingManager extends SpacingManager {
		public WordSpacingManager() {
			super(CSSConstants.CSS_WORD_SPACING_PROPERTY);
		}
	}
}
