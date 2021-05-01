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
import org.w3c.dom.events.EventTarget;

public interface SVGTextContentElement
		extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, EventTarget {
	// lengthAdjust Types
	public static final short LENGTHADJUST_UNKNOWN = 0;
	public static final short LENGTHADJUST_SPACING = 1;
	public static final short LENGTHADJUST_SPACINGANDGLYPHS = 2;

	public SVGAnimatedLength getTextLength();

	public SVGAnimatedEnumeration getLengthAdjust();

	public int getNumberOfChars();

	public float getComputedTextLength();

	public float getSubStringLength(int charnum, int nchars) throws DOMException;

	public SVGPoint getStartPositionOfChar(int charnum) throws DOMException;

	public SVGPoint getEndPositionOfChar(int charnum) throws DOMException;

	public SVGRect getExtentOfChar(int charnum) throws DOMException;

	public float getRotationOfChar(int charnum) throws DOMException;

	public int getCharNumAtPosition(SVGPoint point);

	public void selectSubString(int charnum, int nchars) throws DOMException;
}
