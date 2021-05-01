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

public interface SVGPathSegArcRel extends SVGPathSeg {
	public float getX();

	public void setX(float x) throws DOMException;

	public float getY();

	public void setY(float y) throws DOMException;

	public float getR1();

	public void setR1(float r1) throws DOMException;

	public float getR2();

	public void setR2(float r2) throws DOMException;

	public float getAngle();

	public void setAngle(float angle) throws DOMException;

	public boolean getLargeArcFlag();

	public void setLargeArcFlag(boolean largeArcFlag) throws DOMException;

	public boolean getSweepFlag();

	public void setSweepFlag(boolean sweepFlag) throws DOMException;
}
