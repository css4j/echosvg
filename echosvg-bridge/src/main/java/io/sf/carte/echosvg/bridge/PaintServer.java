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
package io.sf.carte.echosvg.bridge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.IOException;

import org.w3c.css.om.typed.CSSStyleValueList;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.css.dom.CSSValue.CssType;
import io.sf.carte.echosvg.css.dom.CSSValue.Type;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.css.engine.value.ColorFunction;
import io.sf.carte.echosvg.css.engine.value.ColorValue;
import io.sf.carte.echosvg.css.engine.value.NumericValue;
import io.sf.carte.echosvg.css.engine.value.RGBColorValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.svg.ICCColor;
import io.sf.carte.echosvg.css.engine.value.svg12.DeviceColor;
import io.sf.carte.echosvg.css.engine.value.svg12.ICCNamedColor;
import io.sf.carte.echosvg.gvt.CompositeShapePainter;
import io.sf.carte.echosvg.gvt.FillShapePainter;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.gvt.Marker;
import io.sf.carte.echosvg.gvt.MarkerShapePainter;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.gvt.ShapePainter;
import io.sf.carte.echosvg.gvt.StrokeShapePainter;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGConstants;
import io.sf.graphics.java2d.color.ColorSpaces;
import io.sf.graphics.java2d.color.ColorWithAlternatives;
import io.sf.graphics.java2d.color.DeviceCMYKColorSpace;
import io.sf.graphics.java2d.color.ICCColorSpaceWithIntent;
import io.sf.graphics.java2d.color.NamedColorSpace;
import io.sf.graphics.java2d.color.profile.NamedColorProfile;
import io.sf.graphics.java2d.color.profile.NamedColorProfileParser;

/**
 * A collection of utility methods to deliver <code>java.awt.Paint</code>,
 * <code>java.awt.Stroke</code> objects that could be used to paint a shape.
 * This class also provides additional methods the deliver SVG Paint using the
 * ShapePainter interface.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
@SuppressWarnings("deprecation")
public abstract class PaintServer implements SVGConstants, CSSConstants, ErrorConstants {

	/**
	 * No instance of this class is required.
	 */
	protected PaintServer() {
	}

	/////////////////////////////////////////////////////////////////////////
	// 'marker-start', 'marker-mid', 'marker-end' delegates to the PaintServer
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a <code>ShapePainter</code> defined on the specified element and for
	 * the specified shape node.
	 *
	 * @param e    the element with the marker CSS properties
	 * @param node the shape node
	 * @param ctx  the bridge context
	 */
	public static ShapePainter convertMarkers(Element e, ShapeNode node, BridgeContext ctx) {
		Value v;
		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.MARKER_START_INDEX);
		Marker startMarker = convertMarker(e, v, ctx);
		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.MARKER_MID_INDEX);
		Marker midMarker = convertMarker(e, v, ctx);
		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.MARKER_END_INDEX);
		Marker endMarker = convertMarker(e, v, ctx);

		if (startMarker != null || midMarker != null || endMarker != null) {
			MarkerShapePainter p = new MarkerShapePainter(node.getShape());
			p.setStartMarker(startMarker);
			p.setMiddleMarker(midMarker);
			p.setEndMarker(endMarker);
			return p;
		} else {
			return null;
		}
	}

	/////////////////////////////////////////////////////////////////////////
	// io.sf.carte.echosvg.gvt.Marker
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a <code>Marker</code> defined on the specified element by the
	 * specified value, and for the specified shape node.
	 *
	 * @param e   the painted element
	 * @param v   the CSS value describing the marker to construct
	 * @param ctx the bridge context
	 */
	public static Marker convertMarker(Element e, Value v, BridgeContext ctx) {

		if (v.getPrimitiveType() == Type.IDENT) {
			return null; // 'none'
		} else {
			String uri = v.getURIValue();
			Element markerElement = ctx.getReferencedElement(e, uri);
			if (markerElement == null) {
				return null; // Missing reference
			}
			Bridge bridge = ctx.getBridge(markerElement);
			if (bridge == null) {
				// Assume that the cause of this was already reported
				return null;
			}
			if (!(bridge instanceof MarkerBridge)) {
				BridgeException ex = new BridgeException(ctx, e, ERR_CSS_URI_BAD_TARGET, new Object[] { uri });
				UserAgent userAgent = ctx.getUserAgent();
				if (userAgent == null) {
					throw ex;
				}
				userAgent.displayError(ex);
				return null;
			}
			return ((MarkerBridge) bridge).createMarker(ctx, markerElement, e);
		}
	}

	/////////////////////////////////////////////////////////////////////////
	// 'stroke', 'fill' ... converts to ShapePainter
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a <code>ShapePainter</code> defined on the specified element and for
	 * the specified shape node, and using the specified bridge context.
	 *
	 * @param e    the element interested in a shape painter
	 * @param node the shape node
	 * @param ctx  the bridge context
	 */
	public static ShapePainter convertFillAndStroke(Element e, ShapeNode node, BridgeContext ctx) {
		Shape shape = node.getShape();
		if (shape == null)
			return null;

		Paint fillPaint = convertFillPaint(e, node, ctx);
		FillShapePainter fp = new FillShapePainter(shape);
		fp.setPaint(fillPaint);

		Stroke stroke = convertStroke(e);
		if (stroke == null)
			return fp;

		Paint strokePaint = convertStrokePaint(e, node, ctx);
		StrokeShapePainter sp = new StrokeShapePainter(shape);
		sp.setStroke(stroke);
		sp.setPaint(strokePaint);

		CompositeShapePainter cp = new CompositeShapePainter(shape);
		cp.addShapePainter(fp);
		cp.addShapePainter(sp);
		return cp;
	}

	public static ShapePainter convertStrokePainter(Element e, ShapeNode node, BridgeContext ctx) {
		Shape shape = node.getShape();
		if (shape == null)
			return null;

		Stroke stroke = convertStroke(e);
		if (stroke == null)
			return null;

		Paint strokePaint = convertStrokePaint(e, node, ctx);
		StrokeShapePainter sp = new StrokeShapePainter(shape);
		sp.setStroke(stroke);
		sp.setPaint(strokePaint);
		return sp;
	}

	/////////////////////////////////////////////////////////////////////////
	// java.awt.Paint
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Converts for the specified element, its stroke paint properties to a Paint
	 * object.
	 *
	 * @param strokedElement the element interested in a Paint
	 * @param strokedNode    the graphics node to stroke
	 * @param ctx            the bridge context
	 */
	public static Paint convertStrokePaint(Element strokedElement, GraphicsNode strokedNode, BridgeContext ctx) {
		Value v = CSSUtilities.getComputedStyle(strokedElement, SVGCSSEngine.STROKE_OPACITY_INDEX);
		float opacity = convertOpacity(v);
		v = CSSUtilities.getComputedStyle(strokedElement, SVGCSSEngine.STROKE_INDEX);

		return convertPaint(strokedElement, strokedNode, v, opacity, ctx);
	}

	/**
	 * Converts for the specified element, its fill paint properties to a Paint
	 * object.
	 *
	 * @param filledElement the element interested in a Paint
	 * @param filledNode    the graphics node to fill
	 * @param ctx           the bridge context
	 */
	public static Paint convertFillPaint(Element filledElement, GraphicsNode filledNode, BridgeContext ctx) {
		Value v = CSSUtilities.getComputedStyle(filledElement, SVGCSSEngine.FILL_OPACITY_INDEX);
		float opacity = convertOpacity(v);
		v = CSSUtilities.getComputedStyle(filledElement, SVGCSSEngine.FILL_INDEX);

		return convertPaint(filledElement, filledNode, v, opacity, ctx);
	}

	/**
	 * Converts a Paint definition to a concrete <code>java.awt.Paint</code>
	 * instance according to the specified parameters.
	 *
	 * @param paintedElement the element interested in a Paint
	 * @param paintedNode    the graphics node to paint (objectBoundingBox)
	 * @param paintDef       the paint definition
	 * @param opacity        the opacity to consider for the Paint
	 * @param ctx            the bridge context
	 */
	public static Paint convertPaint(Element paintedElement, GraphicsNode paintedNode, Value paintDef, float opacity,
			BridgeContext ctx) {
		if (paintDef.getCssValueType() == CssType.TYPED) {
			switch (paintDef.getPrimitiveType()) {
			case IDENT:
				return null; // none
			case COLOR:
				return convertColor(paintDef.getColorValue(), opacity, ctx);
			case URI:
				return convertURIPaint(paintedElement, paintedNode, paintDef, opacity, ctx);
			default:
				break;
			}
		} else { // List
			Value v = paintDef.item(0);
			switch (v.getPrimitiveType()) {
			case COLOR:
				switch ((v.getColorValue()).getCSSColorSpace()) {
				case ColorValue.RGB_FUNCTION:
					return convertRGBICCColor(paintedElement, v, paintDef.item(1), opacity, ctx);
				}
				break;

			case URI:
				Paint result = silentConvertURIPaint(paintedElement, paintedNode, v, opacity, ctx);
				if (result != null) {
					return result;
				}

				v = paintDef.item(1);
				switch (v.getPrimitiveType()) {
				case IDENT:
					return null; // none
				case COLOR:
					ColorValue color = v.getColorValue();
					switch (color.getCSSColorSpace()) {
					case ColorValue.RGB_FUNCTION:
						if (paintDef.getLength() == 2) {
							return convertColor((RGBColorValue) color, opacity);
						} else {
							return convertRGBICCColor(paintedElement, v, paintDef.item(2), opacity, ctx);
						}
					}
					break;
				default:
					break;
				}

			default:
				break;
			}
		}
		throw new IllegalArgumentException("Paint argument is not an appropriate CSS value");
	}

	/**
	 * Converts a Paint specified by URI without sending any error. if a problem
	 * occured while processing the URI, it just returns null (same effect as
	 * 'none')
	 *
	 * @param paintedElement the element interested in a Paint
	 * @param paintedNode    the graphics node to paint (objectBoundingBox)
	 * @param paintDef       the paint definition
	 * @param opacity        the opacity to consider for the Paint
	 * @param ctx            the bridge context
	 * @return the paint object or null when impossible
	 */
	public static Paint silentConvertURIPaint(Element paintedElement, GraphicsNode paintedNode, Value paintDef,
			float opacity, BridgeContext ctx) {
		Paint paint = null;
		try {
			paint = convertURIPaint(paintedElement, paintedNode, paintDef, opacity, ctx);
		} catch (BridgeException ex) {
		}
		return paint;
	}

	/**
	 * Converts a Paint specified as a URI.
	 *
	 * @param paintedElement the element interested in a Paint
	 * @param paintedNode    the graphics node to paint (objectBoundingBox)
	 * @param paintDef       the paint definition
	 * @param opacity        the opacity to consider for the Paint
	 * @param ctx            the bridge context
	 */
	public static Paint convertURIPaint(Element paintedElement, GraphicsNode paintedNode, Value paintDef, float opacity,
			BridgeContext ctx) {

		String uri = paintDef.getURIValue();
		Element paintElement = ctx.getReferencedElement(paintedElement, uri);
		if (paintElement == null) {
			return null; // Missing reference
		}

		Bridge bridge = ctx.getBridge(paintElement);
		if (bridge == null) {
			// Assume that the cause of this was already reported
			return null;
		}
		if (!(bridge instanceof PaintBridge)) {
			BridgeException ex = new BridgeException(ctx, paintedElement, ERR_CSS_URI_BAD_TARGET,
					new Object[] { uri });
			UserAgent userAgent = ctx.getUserAgent();
			if (userAgent == null) {
				throw ex;
			}
			userAgent.displayError(ex);
			return null;
		}
		return ((PaintBridge) bridge).createPaint(ctx, paintElement, paintedElement, paintedNode, opacity);
	}

	/**
	 * Returns a Color object that corresponds to the input Paint's ICC color value
	 * or an RGB color if the related color profile could not be used or loaded for
	 * any reason.
	 *
	 * @param paintedElement the element using the color
	 * @param colorDef       the color definition
	 * @param iccColor       the ICC color definition
	 * @param opacity        the opacity
	 * @param ctx            the bridge context to use
	 */
	public static Color convertRGBICCColor(Element paintedElement, Value colorDef, Value iccColor, float opacity,
			BridgeContext ctx) {
		Color color = null;
		if (iccColor != null) {
			if (iccColor instanceof ICCColor) {
				color = convertICCColor(paintedElement, (ICCColor) iccColor, opacity, ctx);
			} else if (iccColor instanceof ICCNamedColor) {
				color = convertICCNamedColor(paintedElement, (ICCNamedColor) iccColor, opacity, ctx);
			} else if (iccColor instanceof DeviceColor) {
				color = convertDeviceColor(paintedElement, colorDef, (DeviceColor) iccColor, opacity, ctx);
			}
		}
		if (color == null) {
			color = convertColor(colorDef.getColorValue(), opacity, ctx);
		}
		return color;
	}

	/**
	 * Returns a Color object that corresponds to the input Paint's ICC color value
	 * or null if the related color profile could not be used or loaded for any
	 * reason.
	 *
	 * @param e       the element using the color
	 * @param c       the ICC color definition
	 * @param opacity the opacity
	 * @param ctx     the bridge context to use
	 */
	public static Color convertICCColor(Element e, ICCColor c, float opacity, BridgeContext ctx) {
		// Get ICC Profile's name
		String iccProfileName = c.getColorProfile();
		if (iccProfileName == null) {
			return null;
		}
		// Ask the bridge to map the ICC profile name to an ICC_Profile object
		SVGColorProfileElementBridge profileBridge = (SVGColorProfileElementBridge) ctx.getBridge(
				SVG_NAMESPACE_URI, SVG_COLOR_PROFILE_TAG);
		if (profileBridge == null) {
			return null; // no bridge for color profile
		}

		ICCColorSpaceWithIntent profileCS = profileBridge.createICCColorSpaceWithIntent(ctx, e,
				iccProfileName);
		if (profileCS == null) {
			return null; // no profile
		}

		// Now, convert the colors to an array of floats
		int n = c.getNumberOfColors();
		float[] colorValue = new float[n];
		if (n == 0) {
			return null;
		}
		for (int i = 0; i < n; i++) {
			colorValue[i] = c.getColor(i);
		}

		// Convert values to RGB
		float[] rgb = profileCS.intendedToRGB(colorValue);
		// TODO Preserve original ICC color
		// value!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return new Color(rgb[0], rgb[1], rgb[2], opacity);
	}

	/**
	 * Returns a Color object that corresponds to the input Paint's ICC named color
	 * value or null if the related color profile could not be used or loaded for
	 * any reason.
	 *
	 * @param e       the element using the color
	 * @param c       the ICC named color definition
	 * @param opacity the opacity
	 * @param ctx     the bridge context to use
	 */
	public static Color convertICCNamedColor(Element e, ICCNamedColor c, float opacity, BridgeContext ctx) {
		// Get ICC Profile's name
		String iccProfileName = c.getColorProfile();
		if (iccProfileName == null) {
			return null;
		}
		// Ask the bridge to map the ICC profile name to an ICC_Profile object
		SVGColorProfileElementBridge profileBridge = (SVGColorProfileElementBridge) ctx.getBridge(
				SVG_NAMESPACE_URI, SVG_COLOR_PROFILE_TAG);
		if (profileBridge == null) {
			return null; // no bridge for color profile
		}

		ICCColorSpaceWithIntent profileCS = profileBridge.createICCColorSpaceWithIntent(ctx, e, iccProfileName);
		if (profileCS == null) {
			return null; // no profile
		}
		ICC_Profile iccProfile = profileCS.getProfile();

		String iccProfileSrc = null; // TODO Fill me!

		if (NamedColorProfileParser.isNamedColorProfile(iccProfile)) {
			NamedColorProfileParser parser = new NamedColorProfileParser();
			NamedColorProfile ncp;
			try {
				ncp = parser.parseProfile(iccProfile, iccProfileName, iccProfileSrc);
			} catch (IOException ioe) {
				return null;
			}
			NamedColorSpace ncs = ncp.getNamedColor(c.getColorName());
			if (ncs != null) {
				Color specColor = new ColorWithAlternatives(ncs, new float[] { 1.0f }, opacity, null);
				return specColor;
			} else {
				/*
				 * log.warn("Color '" + colorName + "' does not exist in named color profile: "
				 * + iccProfileSrc);
				 */
			}
		} else {
			// log.warn("ICC profile is no named color profile: " + iccProfileSrc);
		}

		return null;
	}

	/**
	 * Returns a Color object that corresponds to the input Paint's device-specific
	 * color value.
	 *
	 * @param e       the element using the color
	 * @param srgb    the sRGB fallback color
	 * @param c       the device-specific color definition
	 * @param opacity the opacity
	 * @param ctx     the bridge context to use
	 */
	public static Color convertDeviceColor(Element e, Value srgb, DeviceColor c, float opacity,
			BridgeContext ctx) {
		ColorValue color = srgb.getColorValue();
		if (c.isNChannel()) {
			return convertColor(color, opacity, ctx); // NYI
		} else {
			if (c.getNumberOfColors() == 4) {
				DeviceCMYKColorSpace cmykCs = ColorSpaces.getDeviceCMYKColorSpace();
				float[] comps = new float[4];
				for (int i = 0; i < 4; i++) {
					comps[i] = c.getColor(i);
				}
				Color cmyk = new ColorWithAlternatives(cmykCs, comps, opacity, null);
				RGBColorValue rgb = (RGBColorValue) color;
				float r = resolveColorComponent(rgb.getR());
				float g = resolveColorComponent(rgb.getG());
				float b = resolveColorComponent(rgb.getB());
				float a = resolveAlphaComponent(c.getAlpha());
				Color specColor = new ColorWithAlternatives(r, g, b, a * opacity,
						new Color[] { cmyk });
				return specColor;
			} else {
				return convertColor(color, opacity, ctx); // NYI
			}
		}
	}

	/**
	 * Converts the given Value and opacity to a Color object.
	 * 
	 * @param c       The CSS color to convert.
	 * @param opacity The opacity value (0 &lt;= o &lt;= 1).
	 * @param ctx     the bridge context.
	 */
	public static Color convertColor(ColorValue c, float opacity, BridgeContext ctx) {
		switch (c.getCSSColorSpace()) {
		case ColorValue.RGB_FUNCTION:
			return convertColor((RGBColorValue) c, opacity);
		case ColorValue.LAB:
		case ColorValue.LCH:
			throw new UnsupportedOperationException();
		default:
			// Color() function
			return convertColor((ColorFunction) c, opacity, ctx);
		}
	}

	/**
	 * Converts the given Value and opacity to a Color object.
	 * 
	 * @param c       The CSS color to convert.
	 * @param opacity The opacity value (0 &lt;= o &lt;= 1).
	 */
	public static Color convertColor(RGBColorValue c, float opacity) {
		float r = resolveColorComponent(c.getR());
		float g = resolveColorComponent(c.getG());
		float b = resolveColorComponent(c.getB());
		float a = resolveAlphaComponent(c.getAlpha());
		return new Color(r, g, b, a * opacity);
	}

	/**
	 * Converts the given color() function Value and opacity to a Color object.
	 * 
	 * @param c       The CSS color function to convert.
	 * @param opacity The opacity value (0 &lt;= o &lt;= 1).
	 * @param ctx     the bridge context.
	 */
	public static Color convertColor(ColorFunction c, float opacity, BridgeContext ctx) {
		switch (c.getCSSColorSpace()) {
		case ColorValue.CS_DISPLAY_P3:
			ICC_ColorSpace space = CSSColorSpaces.getDisplayP3();
			Color color = convert3Color(space, c, opacity);
			ctx.updateColorSpace(color, space);
			return color;
		case ColorValue.CS_A98_RGB:
			space = CSSColorSpaces.getA98RGB();
			color = convert3Color(space, c, opacity);
			ctx.updateColorSpace(color, space);
			return color;
		case ColorValue.CS_PROPHOTO_RGB:
			space = CSSColorSpaces.getProphotoRGB();
			color = convert3Color(space, c, opacity);
			ctx.updateColorSpace(color, space);
			return color;
		case ColorValue.CS_REC2020:
			space = CSSColorSpaces.getRec2020();
			color = convert3Color(space, c, opacity);
			ctx.updateColorSpace(color, space);
			return color;
		case ColorValue.CS_SRGB_LINEAR:
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
			return convert3Color(cs, c, opacity);
		case ColorValue.CS_XYZ:
		case ColorValue.CS_XYZ_D65:
			CSSStyleValueList<NumericValue> chs = c.getChannels();
			float[] ch = new float[3];
			ch[0] = resolveColorComponent(chs.item(0));
			ch[1] = resolveColorComponent(chs.item(1));
			ch[2] = resolveColorComponent(chs.item(2));
			float[] xyzd50 = d65xyzToD50(ch);
			float a = resolveAlphaComponent(c.getAlpha());
			cs = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);
			color = new Color(cs, xyzd50, a * opacity);
			cs = CSSColorSpaces.containerRGBSpace(color, ctx.getColorSpace());
			if (cs != null) {
				ctx.updateColorSpace(color, cs);
			}
			return color;
		case ColorValue.CS_XYZ_D50:
			cs = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);
			color = convert3Color(cs, c, opacity);
			cs = CSSColorSpaces.containerRGBSpace(color, ctx.getColorSpace());
			if (cs != null) {
				ctx.updateColorSpace(color, cs);
			}
			return color;
		default:
			throw new UnsupportedOperationException();
		}
	}

	/////////////////////////////////////////////////////////////////////////
	// Color utility methods
	/////////////////////////////////////////////////////////////////////////

	private static Color convert3Color(ColorSpace space, ColorFunction c, float opacity) {
		CSSStyleValueList<NumericValue> chs = c.getChannels();
		float[] ch = new float[3];
		ch[0] = resolveColorComponent(chs.item(0));
		ch[1] = resolveColorComponent(chs.item(1));
		ch[2] = resolveColorComponent(chs.item(2));
		float a = resolveAlphaComponent(c.getAlpha());
		return new Color(space, ch, a * opacity);
	}

	/**
	 * Returns the value of one color component (0 &lt;= result &lt;= 1).
	 * 
	 * @param v the value that declares the color component, either a percentage or
	 *          a number in the range (0 &lt;= v &lt;= 1).
	 * @return the value in the range (0 &lt;= v &lt;= 1).
	 */
	private static float resolveColorComponent(NumericValue item) {
		float f;
		switch (item.getCSSUnit()) {
		case CSSUnit.CSS_NUMBER:
			f = item.getFloatValue();
			if (f < 0f) {
				f = 0f;
			} else if (f > 1f) {
				f = 1f;
			}
			return f;
		case CSSUnit.CSS_PERCENTAGE:
			f = item.getFloatValue();
			if (f < 0f) {
				f = 0f;
			} else if (f > 100f) {
				f = 100f;
			}
			return f * 0.01f;
		}
		throw new IllegalArgumentException("Invalid color component: " + item.getCssText());
	}

	private static float[] d65xyzToD50(float[] xyz) {
		// Chromatic adjustment: D65 to D50, Bradford
		// See http://www.brucelindbloom.com/index.html?Eqn_ChromAdapt.html
		float[] xyzadj = new float[3];
		xyzadj[0] = (float) (1.0478112436606313d * xyz[0] + 0.022886602481693052d * xyz[1]
				- 0.05012697596852886d * xyz[2]);
		xyzadj[1] = (float) (0.029542398290574905d * xyz[0] + 0.9904844034904394d * xyz[1]
				- 0.017049095628961564d * xyz[2]);
		xyzadj[2] = (float) (-0.009234489723309473d * xyz[0] + 0.015043616793498756d * xyz[1]
				+ 0.7521316354746059d * xyz[2]);
		return xyzadj;
	}

	/////////////////////////////////////////////////////////////////////////
	// java.awt.stroke
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Converts a <code>Stroke</code> object defined on the specified element.
	 *
	 * @param e the element on which the stroke is specified
	 */
	public static Stroke convertStroke(Element e) {
		Value v;
		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.STROKE_WIDTH_INDEX);
		float width = v.getFloatValue();
		if (width == 0.0f)
			return null; // Stop here no stroke should be painted.

		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.STROKE_LINECAP_INDEX);
		int linecap = convertStrokeLinecap(v);
		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.STROKE_LINEJOIN_INDEX);
		int linejoin = convertStrokeLinejoin(v);
		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.STROKE_MITERLIMIT_INDEX);
		float miterlimit = convertStrokeMiterlimit(v);
		v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.STROKE_DASHARRAY_INDEX);
		float[] dasharray = convertStrokeDasharray(v);

		float dashoffset = 0;
		if (dasharray != null) {
			v = CSSUtilities.getComputedStyle(e, SVGCSSEngine.STROKE_DASHOFFSET_INDEX);
			dashoffset = v.getFloatValue();

			// make the dashoffset positive since BasicStroke cannot handle
			// negative values
			if (dashoffset < 0) {
				float dashpatternlength = 0;
				for (float aDasharray : dasharray) {
					dashpatternlength += aDasharray;
				}
				// if the dash pattern consists of an odd number of elements,
				// the pattern length must be doubled
				if ((dasharray.length % 2) != 0)
					dashpatternlength *= 2;

				if (dashpatternlength == 0) {
					dashoffset = 0;
				} else {
					while (dashoffset < 0)
						dashoffset += dashpatternlength;
				}
			}
		}
		return new BasicStroke(width, linecap, linejoin, miterlimit, dasharray, dashoffset);
	}

	/////////////////////////////////////////////////////////////////////////
	// Stroke utility methods
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Converts the 'stroke-dasharray' property to a list of float number in user
	 * units.
	 *
	 * @param v the CSS value describing the dasharray property
	 */
	public static float[] convertStrokeDasharray(Value v) {
		float[] dasharray = null;
		if (v.getCssValueType() == CssType.LIST) {
			int length = v.getLength();
			dasharray = new float[length];
			float sum = 0;
			for (int i = 0; i < dasharray.length; ++i) {
				dasharray[i] = v.item(i).getFloatValue();
				sum += dasharray[i];
			}
			if (sum == 0) {
				/*
				 * 11.4 - If the sum of the <length>'s is zero, then the stroke is rendered as
				 * if a value of none were specified.
				 */
				dasharray = null;
			}
		}
		return dasharray;
	}

	/**
	 * Converts the 'miterlimit' property to the appropriate float number.
	 * 
	 * @param v the CSS value describing the miterlimit property
	 */
	public static float convertStrokeMiterlimit(Value v) {
		float miterlimit = v.getFloatValue();
		return (miterlimit < 1.0f) ? 1.0f : miterlimit;
	}

	/**
	 * Converts the 'linecap' property to the appropriate BasicStroke constant.
	 * 
	 * @param v the CSS value describing the linecap property
	 */
	public static int convertStrokeLinecap(Value v) {
		String s = v.getIdentifierValue();
		switch (s.charAt(0)) {
		case 'b':
			return BasicStroke.CAP_BUTT;
		case 'r':
			return BasicStroke.CAP_ROUND;
		case 's':
			return BasicStroke.CAP_SQUARE;
		default:
			throw new IllegalArgumentException("Linecap argument is not an appropriate CSS value");
		}
	}

	/**
	 * Converts the 'linejoin' property to the appropriate BasicStroke constant.
	 * 
	 * @param v the CSS value describing the linejoin property
	 */
	public static int convertStrokeLinejoin(Value v) {
		String s = v.getIdentifierValue();
		switch (s.charAt(0)) {
		case 'm':
			return BasicStroke.JOIN_MITER;
		case 'r':
			return BasicStroke.JOIN_ROUND;
		case 'b':
			return BasicStroke.JOIN_BEVEL;
		default:
			throw new IllegalArgumentException("Linejoin argument is not an appropriate CSS value");
		}
	}

	/////////////////////////////////////////////////////////////////////////
	// Paint utility methods
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the value of the alpha component (0 &lt;= result &lt;= 1).
	 * 
	 * @param v the value that declares the alpha component, either a percentage or
	 *          a number in the range (0 &lt;= v &lt;= 1).
	 * @return the value in the range (0 &lt;= v &lt;= 1).
	 */
	private static float resolveAlphaComponent(Value v) {
		float f;
		switch (v.getCSSUnit()) {
		case CSSUnit.CSS_PERCENTAGE:
			f = v.getFloatValue();
			f = (f > 100f) ? 100f : (f < 0f) ? 0f : f;
			return f * 0.01f;
		case CSSUnit.CSS_NUMBER:
			f = v.getFloatValue();
			f = (f > 1f) ? 1f : (f < 0f) ? 0f : f;
			return f;
		default:
			throw new IllegalArgumentException("Color alpha argument is not an appropriate CSS value");
		}
	}

	/**
	 * Returns the opacity represented by the specified CSSValue.
	 * 
	 * @param v the value that represents the opacity
	 * @return the opacity between 0 and 1
	 */
	public static float convertOpacity(Value v) {
		float r = v.getFloatValue();
		return (r < 0f) ? 0f : (r > 1.0f) ? 1.0f : r;
	}

}
