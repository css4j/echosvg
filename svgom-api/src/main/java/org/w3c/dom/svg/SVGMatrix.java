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

public interface SVGMatrix {
	public float getA();

	public void setA(float a) throws DOMException;

	public float getB();

	public void setB(float b) throws DOMException;

	public float getC();

	public void setC(float c) throws DOMException;

	public float getD();

	public void setD(float d) throws DOMException;

	public float getE();

	public void setE(float e) throws DOMException;

	public float getF();

	public void setF(float f) throws DOMException;

	public SVGMatrix multiply(SVGMatrix secondMatrix);

	public SVGMatrix inverse() throws SVGException;

	public SVGMatrix translate(float x, float y);

	public SVGMatrix scale(float scaleFactor);

	public SVGMatrix scaleNonUniform(float scaleFactorX, float scaleFactorY);

	public SVGMatrix rotate(float angle);

	public SVGMatrix rotateFromVector(float x, float y) throws SVGException;

	public SVGMatrix flipX();

	public SVGMatrix flipY();

	public SVGMatrix skewX(float angle);

	public SVGMatrix skewY(float angle);
}
