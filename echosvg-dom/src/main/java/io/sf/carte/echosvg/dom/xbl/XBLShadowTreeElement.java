/*
 * Copyright (c) 2005 World Wide Web Consortium,
 *
 * (Massachusetts Institute of Technology, European Research Consortium for
 * Informatics and Mathematics, Keio University). All Rights Reserved. This
 * work is distributed under the W3C(r) Software License [1] in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.w3.org/Consortium/Legal/2002/copyright-software-20021231
 *
 * Modifications:
 *
 * September 10, 2005
 *   Placed interface in io.sf.carte.echosvg.dom.xbl for the time being.
 *   Added javadocs.
 */
package io.sf.carte.echosvg.dom.xbl;

import org.w3c.dom.Element;

/**
 * The interface for xbl:shadowTree elements. Eventually will move to
 * org.w3c.dom.xbl (or some such package).
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface XBLShadowTreeElement extends Element {

	/**
	 * Returns the Element that has an ID attribute with the given value.
	 */
	Element getElementById(String elementId);
}
