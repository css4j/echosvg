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

public interface SVGFECompositeElement extends SVGElement, SVGFilterPrimitiveStandardAttributes {
	// Composite Operators
	public static final short SVG_FECOMPOSITE_OPERATOR_UNKNOWN = 0;
	public static final short SVG_FECOMPOSITE_OPERATOR_OVER = 1;
	public static final short SVG_FECOMPOSITE_OPERATOR_IN = 2;
	public static final short SVG_FECOMPOSITE_OPERATOR_OUT = 3;
	public static final short SVG_FECOMPOSITE_OPERATOR_ATOP = 4;
	public static final short SVG_FECOMPOSITE_OPERATOR_XOR = 5;
	public static final short SVG_FECOMPOSITE_OPERATOR_ARITHMETIC = 6;

	public SVGAnimatedString getIn1();

	public SVGAnimatedString getIn2();

	public SVGAnimatedEnumeration getOperator();

	public SVGAnimatedNumber getK1();

	public SVGAnimatedNumber getK2();

	public SVGAnimatedNumber getK3();

	public SVGAnimatedNumber getK4();
}
