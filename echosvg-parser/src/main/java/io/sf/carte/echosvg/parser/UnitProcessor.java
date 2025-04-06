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
package io.sf.carte.echosvg.parser;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.Element;

/**
 * This class provides methods to convert SVG length and coordinate to float in
 * user units.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class UnitProcessor {

	/**
	 * This constant represents horizontal lengths.
	 */
	public static final short HORIZONTAL_LENGTH = 2;

	/**
	 * This constant represents vertical lengths.
	 */
	public static final short VERTICAL_LENGTH = 1;

	/**
	 * This constant represents other lengths.
	 */
	public static final short OTHER_LENGTH = 0;

	/**
	 * precomputed square-root of 2.0
	 */
	static final double SQRT2 = Math.sqrt(2.0);

	/**
	 * No instance of this class is required.
	 */
	protected UnitProcessor() {
	}

	/**
	 * Returns the specified value with the specified direction in objectBoundingBox
	 * units.
	 *
	 * @param s    the value
	 * @param attr the attribute name that represents the value
	 * @param d    the direction of the value
	 * @param ctx  the context used to resolve relative value
	 */
	public static float cssToObjectBoundingBox(String s, String attr, short d, Context ctx) throws ParseException {
		UnitResolver ur = new UnitResolver() {

			@Override
			protected float unitToPixels(short unitType, float floatValue, short percentageInterpretation) {
				// Ignore the supplied percentage interpretation
				return cssToObjectBoundingBox(floatValue, unitType, d, ctx);
			}

		};
		LengthParser lengthParser = new LengthParser(ur);
		lengthParser.parse(s);
		return cssToObjectBoundingBox(ur.value, ur.unit, d, ctx);
	}

	/**
	 * Returns the specified value with the specified direction in objectBoundingBox
	 * units.
	 *
	 * @param value the value
	 * @param type  the type of the value according to CSSUnit
	 * @param d     the direction of the value
	 * @param ctx   the context used to resolve relative value
	 */
	public static float cssToObjectBoundingBox(float value, short type, short d, Context ctx) {
		switch (type) {
		case CSSUnit.CSS_NUMBER:
			// as is
			return value;
		case CSSUnit.CSS_PERCENTAGE:
			// If a percentage value is used, it is converted to a
			// 'bounding box' space coordinate by division by 100
			return value / 100f;
		default:
			// <!> FIXME: resolve units in userSpace but consider them
			// in the objectBoundingBox coordinate system
			return cssToUserSpace(value, type, d, ctx);
		}
	}

	/////////////////////////////////////////////////////////////////////////
	// SVG methods - userSpace
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the specified coordinate with the specified direction in user units.
	 *
	 * @param s    the 'other' coordinate
	 * @param attr the attribute name that represents the length
	 * @param d    the direction of the coordinate
	 * @param ctx  the context used to resolve relative value
	 */
	public static float cssToUserSpace(String s, String attr, short d, Context ctx) throws ParseException {
		UnitResolver ur = new UnitResolver() {

			@Override
			protected float unitToPixels(short unitType, float floatValue, short percentageInterpretation) {
				// Ignore the supplied percentage interpretation
				return cssToUserSpace(floatValue, unitType, d, ctx);
			}

		};
		LengthParser lengthParser = new LengthParser(ur);
		lengthParser.parse(s);
		return cssToUserSpace(ur.value, ur.unit, d, ctx);
	}

	/**
	 * Converts the specified value of the specified type and direction to user
	 * units.
	 *
	 * @param v    the value to convert
	 * @param type the type of the value according to CSSUnit
	 * @param d    HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx  the context used to resolve relative value
	 * @return the value in user space units.
	 * @throws IllegalArgumentException if the type is unknown.
	 */
	public static float cssToUserSpace(float v, short type, short d, Context ctx)
			throws IllegalArgumentException {
		switch (type) {
		case CSSUnit.CSS_NUMBER:
		case CSSUnit.CSS_PX:
			return v;
		case CSSUnit.CSS_PERCENTAGE:
			return percentagesToPixels(v, d, ctx);
		case CSSUnit.CSS_EM:
			return emsToPixels(v, d, ctx);
		case CSSUnit.CSS_EX:
			return exsToPixels(v, d, ctx);
		case CSSUnit.CSS_LH:
			return lhToPixels(v, d, ctx);
		case CSSUnit.CSS_REM:
			return remToPixels(v, d, ctx);
		case CSSUnit.CSS_REX:
			return rexToPixels(v, d, ctx);
		case CSSUnit.CSS_RLH:
			return rlhToPixels(v, d, ctx);
		case CSSUnit.CSS_VW:
			return vwToPixels(v, d, ctx);
		case CSSUnit.CSS_VH:
			return vhToPixels(v, d, ctx);
		case CSSUnit.CSS_VMIN:
			return vminToPixels(v, d, ctx);
		case CSSUnit.CSS_VMAX:
			return vmaxToPixels(v, d, ctx);
		case CSSUnit.CSS_MM:
			return v * 3.779527559055f; // 96 / 25.4
		case CSSUnit.CSS_CM:
			return v * 37.79527559055f; // 96 / 2.54
		case CSSUnit.CSS_IN:
			return v * 96f;
		case CSSUnit.CSS_PT:
			return v / 0.75f; // Mult. by 96 / 72
		case CSSUnit.CSS_PC:
			return v * 16f; // 96 / 6
		case CSSUnit.CSS_QUARTER_MM:
			return v * 0.94488188976f; // 96 / 25.4 / 4
		default:
			throw new IllegalArgumentException("Length has unknown type");
		}
	}

	/**
	 * Converts the specified value of the specified type and direction to SVG
	 * units.
	 *
	 * @param v    the value to convert
	 * @param type the type of the value according to CSSUnit
	 * @param d    HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx  the context used to resolve relative value
	 * @return the value in the given unit.
	 */
	public static float userSpaceToCSS(float v, short type, short d, Context ctx) {
		switch (type) {
		case CSSUnit.CSS_NUMBER:
		case CSSUnit.CSS_PX:
			return v;
		case CSSUnit.CSS_MM:
			return v * 0.26458333333333f; // 25.4/96
		case CSSUnit.CSS_CM:
			return v * 0.026458333333333f; // 2.54/96
		case CSSUnit.CSS_IN:
			return v / 96f;
		case CSSUnit.CSS_PT:
			return v * 0.75f; // 72/96
		case CSSUnit.CSS_PC:
			return v / 16f;
		case CSSUnit.CSS_QUARTER_MM:
			return v * 1.0583333333333f; // 25.4*4/96
		case CSSUnit.CSS_EM:
			return pixelsToEms(v, d, ctx);
		case CSSUnit.CSS_EX:
			return pixelsToExs(v, d, ctx);
		case CSSUnit.CSS_LH:
			return pixelsToLh(v, d, ctx);
		case CSSUnit.CSS_REM:
			return pixelsToRem(v, d, ctx);
		case CSSUnit.CSS_REX:
			return pixelsToRex(v, d, ctx);
		case CSSUnit.CSS_RLH:
			return pixelsToRlh(v, d, ctx);
		case CSSUnit.CSS_VW:
			return pixelsToVw(v, d, ctx);
		case CSSUnit.CSS_VH:
			return pixelsToVh(v, d, ctx);
		case CSSUnit.CSS_VMIN:
			return pixelsToVmin(v, d, ctx);
		case CSSUnit.CSS_VMAX:
			return pixelsToVmax(v, d, ctx);
		case CSSUnit.CSS_PERCENTAGE:
			return pixelsToPercentages(v, d, ctx);
		default:
			throw new IllegalArgumentException("Length has unknown type");
		}
	}

	/////////////////////////////////////////////////////////////////////////
	// Utilities methods for relative length
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Converts percentages to user units.
	 *
	 * @param v   the percentage to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float percentagesToPixels(float v, short d, Context ctx) {
		if (d == HORIZONTAL_LENGTH) {
			float w = ctx.getViewportWidth();
			return w * v / 100f;
		} else if (d == VERTICAL_LENGTH) {
			float h = ctx.getViewportHeight();
			return h * v / 100f;
		} else {
			double w = ctx.getViewportWidth();
			double h = ctx.getViewportHeight();
			double vpp = Math.sqrt(w * w + h * h) / SQRT2;
			return (float) (vpp * v / 100d);
		}
	}

	/**
	 * Converts user units to percentages relative to the viewport.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float pixelsToPercentages(float v, short d, Context ctx) {
		if (d == HORIZONTAL_LENGTH) {
			float w = ctx.getViewportWidth();
			return v * 100f / w;
		} else if (d == VERTICAL_LENGTH) {
			float h = ctx.getViewportHeight();
			return v * 100f / h;
		} else {
			double w = ctx.getViewportWidth();
			double h = ctx.getViewportHeight();
			double vpp = Math.sqrt(w * w + h * h) / SQRT2;
			return (float) (v * 100d / vpp);
		}
	}

	/**
	 * Converts user units to ems units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float pixelsToEms(float v, short d, Context ctx) {
		return v / ctx.getFontSize();
	}

	/**
	 * Converts ems units to user units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float emsToPixels(float v, short d, Context ctx) {
		return v * ctx.getFontSize();
	}

	/**
	 * Converts user units to exs units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float pixelsToExs(float v, short d, Context ctx) {
		float xh = ctx.getXHeight();
		return v / xh / ctx.getFontSize();
	}

	/**
	 * Converts exs units to user units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float exsToPixels(float v, short d, Context ctx) {
		float xh = ctx.getXHeight();
		return v * xh * ctx.getFontSize();
	}

	/**
	 * Converts user units to lh units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float pixelsToLh(float v, short d, Context ctx) {
		return v / ctx.getLineHeight();
	}

	/**
	 * Converts lh units to user units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float lhToPixels(float v, short d, Context ctx) {
		return v * ctx.getLineHeight();
	}

	/**
	 * Converts user units to rem units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float pixelsToRem(float v, short d, Context ctx) {
		return v / ctx.getRootFontSize();
	}

	/**
	 * Converts rem units to user units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float remToPixels(float v, short d, Context ctx) {
		return v * ctx.getRootFontSize();
	}

	/**
	 * Converts user units to rex units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float pixelsToRex(float v, short d, Context ctx) {
		float xh = ctx.getRootXHeight();
		return v / xh / ctx.getRootFontSize();
	}

	/**
	 * Converts rex units to user units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float rexToPixels(float v, short d, Context ctx) {
		float xh = ctx.getRootXHeight();
		return v * xh * ctx.getRootFontSize();
	}

	/**
	 * Converts user units to rlh units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float pixelsToRlh(float v, short d, Context ctx) {
		return v / ctx.getRootLineHeight();
	}

	/**
	 * Converts rlh units to user units.
	 *
	 * @param v   the value to convert
	 * @param d   HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
	 * @param ctx the context
	 */
	protected static float rlhToPixels(float v, short d, Context ctx) {
		return v * ctx.getRootLineHeight();
	}

	private static float pixelsToVw(float v, short d, Context ctx) {
		return v / ctx.getViewportWidth() * 100f;
	}

	private static float pixelsToVh(float v, short d, Context ctx) {
		return v / ctx.getViewportHeight() * 100f;
	}

	private static float pixelsToVmax(float v, short d, Context ctx) {
		float w = ctx.getViewportWidth();
		float h = ctx.getViewportHeight();
		return v / Math.max(w, h) * 100f;
	}

	private static float pixelsToVmin(float v, short d, Context ctx) {
		float w = ctx.getViewportWidth();
		float h = ctx.getViewportHeight();
		return v / Math.min(w, h) * 100f;
	}

	private static float vwToPixels(float v, short d, Context ctx) {
		return v * ctx.getViewportWidth() / 100f;
	}

	private static float vhToPixels(float v, short d, Context ctx) {
		return v * ctx.getViewportHeight() / 100f;
	}

	private static float vmaxToPixels(float v, short d, Context ctx) {
		float w = ctx.getViewportWidth();
		float h = ctx.getViewportHeight();
		return v * Math.max(w, h) / 100f;
	}

	private static float vminToPixels(float v, short d, Context ctx) {
		float w = ctx.getViewportWidth();
		float h = ctx.getViewportHeight();
		return v * Math.min(w, h) / 100f;
	}

	/**
	 * A LengthHandler that convert units.
	 */
	public static class UnitResolver extends DefaultLengthHandler {

		/**
		 * The length value.
		 */
		public float value;

		/**
		 * The length type.
		 */
		public short unit = CSSUnit.CSS_NUMBER;

		/**
		 * Implements {@link LengthHandler#startLength()}.
		 */
		@Override
		public void startLength() throws ParseException {
		}

		/**
		 * Implements {@link LengthHandler#lengthValue(float)}.
		 */
		@Override
		public void lengthValue(float v) throws ParseException {
			this.value = v;
		}

		@Override
		protected void setUnit(short unit) {
			this.unit = unit;
		}

		/**
		 * Implements {@link LengthHandler#endLength()}.
		 */
		@Override
		public void endLength() throws ParseException {
		}

	}

	/**
	 * Holds the informations needed to compute the units.
	 */
	public interface Context {

		/**
		 * Returns the element.
		 */
		Element getElement();

		/**
		 * Returns the resolution in {@code dpi}.
		 */
		float getResolution();

		/**
		 * Returns the font-size value.
		 */
		float getFontSize();

		/**
		 * Returns the x-height value.
		 */
		float getXHeight();

		/**
		 * Returns the line-height value.
		 */
		float getLineHeight();

		/**
		 * Returns the font-size value of the :root element.
		 */
		float getRootFontSize();

		/**
		 * Returns the x-height value of the :root element.
		 */
		float getRootXHeight();

		/**
		 * Returns the line-height value of the :root element.
		 */
		float getRootLineHeight();

		/**
		 * Returns the viewport width used to compute units.
		 */
		float getViewportWidth();

		/**
		 * Returns the viewport height used to compute units.
		 */
		float getViewportHeight();

	}

}
