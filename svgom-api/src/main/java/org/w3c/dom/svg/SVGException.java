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

public abstract class SVGException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SVGException(short code, String message) {
		super(message);
		this.code = code;
	}

	public short code;
	// ExceptionCode
	public static final short SVG_WRONG_TYPE_ERR = 0;
	public static final short SVG_INVALID_VALUE_ERR = 1;
	public static final short SVG_MATRIX_NOT_INVERTABLE = 2;
}
