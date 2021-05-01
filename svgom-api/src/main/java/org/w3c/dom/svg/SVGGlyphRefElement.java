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

public interface SVGGlyphRefElement extends SVGElement, SVGURIReference, SVGStylable {
	public String getGlyphRef();

	public void setGlyphRef(String glyphRef) throws DOMException;

	public String getFormat();

	public void setFormat(String format) throws DOMException;

	public float getX();

	public void setX(float x) throws DOMException;

	public float getY();

	public void setY(float y) throws DOMException;

	public float getDx();

	public void setDx(float dx) throws DOMException;

	public float getDy();

	public void setDy(float dy) throws DOMException;
}
