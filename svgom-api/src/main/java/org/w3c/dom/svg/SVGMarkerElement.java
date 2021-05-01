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

public interface SVGMarkerElement
		extends SVGElement, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, SVGFitToViewBox {
	// Marker Unit Types
	public static final short SVG_MARKERUNITS_UNKNOWN = 0;
	public static final short SVG_MARKERUNITS_USERSPACEONUSE = 1;
	public static final short SVG_MARKERUNITS_STROKEWIDTH = 2;
	// Marker Orientation Types
	public static final short SVG_MARKER_ORIENT_UNKNOWN = 0;
	public static final short SVG_MARKER_ORIENT_AUTO = 1;
	public static final short SVG_MARKER_ORIENT_ANGLE = 2;

	public SVGAnimatedLength getRefX();

	public SVGAnimatedLength getRefY();

	public SVGAnimatedEnumeration getMarkerUnits();

	public SVGAnimatedLength getMarkerWidth();

	public SVGAnimatedLength getMarkerHeight();

	public SVGAnimatedEnumeration getOrientType();

	public SVGAnimatedAngle getOrientAngle();

	public void setOrientToAuto();

	public void setOrientToAngle(SVGAngle angle);
}
