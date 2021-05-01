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

public interface SVGComponentTransferFunctionElement extends SVGElement {
	// Component Transfer Types
	public static final short SVG_FECOMPONENTTRANSFER_TYPE_UNKNOWN = 0;
	public static final short SVG_FECOMPONENTTRANSFER_TYPE_IDENTITY = 1;
	public static final short SVG_FECOMPONENTTRANSFER_TYPE_TABLE = 2;
	public static final short SVG_FECOMPONENTTRANSFER_TYPE_DISCRETE = 3;
	public static final short SVG_FECOMPONENTTRANSFER_TYPE_LINEAR = 4;
	public static final short SVG_FECOMPONENTTRANSFER_TYPE_GAMMA = 5;

	public SVGAnimatedEnumeration getType();

	public SVGAnimatedNumberList getTableValues();

	public SVGAnimatedNumber getSlope();

	public SVGAnimatedNumber getIntercept();

	public SVGAnimatedNumber getAmplitude();

	public SVGAnimatedNumber getExponent();

	public SVGAnimatedNumber getOffset();
}
