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

public interface SVGTransform {
	// Transform Types
	public static final short SVG_TRANSFORM_UNKNOWN = 0;
	public static final short SVG_TRANSFORM_MATRIX = 1;
	public static final short SVG_TRANSFORM_TRANSLATE = 2;
	public static final short SVG_TRANSFORM_SCALE = 3;
	public static final short SVG_TRANSFORM_ROTATE = 4;
	public static final short SVG_TRANSFORM_SKEWX = 5;
	public static final short SVG_TRANSFORM_SKEWY = 6;

	public short getType();

	public SVGMatrix getMatrix();

	public float getAngle();

	public void setMatrix(SVGMatrix matrix);

	public void setTranslate(float tx, float ty);

	public void setScale(float sx, float sy);

	public void setRotate(float angle, float cx, float cy);

	public void setSkewX(float angle);

	public void setSkewY(float angle);
}
