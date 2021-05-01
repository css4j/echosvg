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

public interface SVGPathSegList {
	public int getNumberOfItems();

	public void clear() throws DOMException;

	public SVGPathSeg initialize(SVGPathSeg newItem) throws DOMException, SVGException;

	public SVGPathSeg getItem(int index) throws DOMException;

	public SVGPathSeg insertItemBefore(SVGPathSeg newItem, int index) throws DOMException, SVGException;

	public SVGPathSeg replaceItem(SVGPathSeg newItem, int index) throws DOMException, SVGException;

	public SVGPathSeg removeItem(int index) throws DOMException;

	public SVGPathSeg appendItem(SVGPathSeg newItem) throws DOMException, SVGException;
}
