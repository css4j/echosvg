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

public interface SVGFETurbulenceElement extends SVGElement, SVGFilterPrimitiveStandardAttributes {
	// Turbulence Types
	public static final short SVG_TURBULENCE_TYPE_UNKNOWN = 0;
	public static final short SVG_TURBULENCE_TYPE_FRACTALNOISE = 1;
	public static final short SVG_TURBULENCE_TYPE_TURBULENCE = 2;
	// Stitch Options
	public static final short SVG_STITCHTYPE_UNKNOWN = 0;
	public static final short SVG_STITCHTYPE_STITCH = 1;
	public static final short SVG_STITCHTYPE_NOSTITCH = 2;

	public SVGAnimatedNumber getBaseFrequencyX();

	public SVGAnimatedNumber getBaseFrequencyY();

	public SVGAnimatedInteger getNumOctaves();

	public SVGAnimatedNumber getSeed();

	public SVGAnimatedEnumeration getStitchTiles();

	public SVGAnimatedEnumeration getType();
}
