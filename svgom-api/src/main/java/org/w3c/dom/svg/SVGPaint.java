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

public interface SVGPaint extends SVGColor {
	// Paint Types
	public static final short SVG_PAINTTYPE_UNKNOWN = 0;
	public static final short SVG_PAINTTYPE_RGBCOLOR = 1;
	public static final short SVG_PAINTTYPE_RGBCOLOR_ICCCOLOR = 2;
	public static final short SVG_PAINTTYPE_NONE = 101;
	public static final short SVG_PAINTTYPE_CURRENTCOLOR = 102;
	public static final short SVG_PAINTTYPE_URI_NONE = 103;
	public static final short SVG_PAINTTYPE_URI_CURRENTCOLOR = 104;
	public static final short SVG_PAINTTYPE_URI_RGBCOLOR = 105;
	public static final short SVG_PAINTTYPE_URI_RGBCOLOR_ICCCOLOR = 106;
	public static final short SVG_PAINTTYPE_URI = 107;

	public short getPaintType();

	public String getUri();

	public void setUri(String uri);

	public void setPaint(short paintType, String uri, String rgbColor, String iccColor) throws SVGException;
}
