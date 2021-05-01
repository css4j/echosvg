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

import org.w3c.dom.DOMException;

public interface SVGAngle {
	// Angle Unit Types
	public static final short SVG_ANGLETYPE_UNKNOWN = 0;
	public static final short SVG_ANGLETYPE_UNSPECIFIED = 1;
	public static final short SVG_ANGLETYPE_DEG = 2;
	public static final short SVG_ANGLETYPE_RAD = 3;
	public static final short SVG_ANGLETYPE_GRAD = 4;

	public short getUnitType();

	public float getValue();

	public void setValue(float value) throws DOMException;

	public float getValueInSpecifiedUnits();

	public void setValueInSpecifiedUnits(float valueInSpecifiedUnits) throws DOMException;

	public String getValueAsString();

	public void setValueAsString(String valueAsString) throws DOMException;

	public void newValueSpecifiedUnits(short unitType, float valueInSpecifiedUnits);

	public void convertToSpecifiedUnits(short unitType);
}
