/*
 * Copyright (c) 2010 World Wide Web Consortium,
 *
 * (Massachusetts Institute of Technology, European Research Consortium for
 * Informatics and Mathematics, Keio University). All Rights Reserved. This
 * work is distributed under the W3C(r) Software License [1] in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.w3.org/Consortium/Legal/2002/copyright-software-20021231
 */

package org.w3c.dom.svg;

import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.RGBColor;

public interface SVGColor extends CSSValue {
	// Color Types
	public static final short SVG_COLORTYPE_UNKNOWN = 0;
	public static final short SVG_COLORTYPE_RGBCOLOR = 1;
	public static final short SVG_COLORTYPE_RGBCOLOR_ICCCOLOR = 2;
	public static final short SVG_COLORTYPE_CURRENTCOLOR = 3;

	public short getColorType();

	public RGBColor getRGBColor();

	public SVGICCColor getICCColor();

	public void setRGBColor(String rgbColor) throws SVGException;

	public void setRGBColorICCColor(String rgbColor, String iccColor) throws SVGException;

	public void setColor(short colorType, String rgbColor, String iccColor) throws SVGException;
}
