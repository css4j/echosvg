/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.Node;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.util.SVG12Constants;

/**
 * This class implements a multiImage extension to SVG.
 *
 * The 'multiImage' element is similar to the 'image' element (supports all the
 * same attributes and properties) except.
 * <ol>
 * <li>It has two addtional attributes: 'pixel-width' and 'pixel-height' which
 * are the maximum width and height of the image referenced by the xlink:href
 * attribute.</li>
 * <li>It can contain a child element 'subImage' which has two attributes,
 * pixel-width, pixel-height. It holds SVG content to be rendered.</li>
 * <li>It can contain a child element 'subImageRef' which has only three
 * attributes, pixel-width, pixel-height and xlink:href. The image displayed is
 * the smallest image such that pixel-width and pixel-height are greater than or
 * equal to the required image size for display.</li>
 * </ol>
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMMultiImageElement extends SVGStylableElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new SVG MultiImageElement object.
	 */
	protected SVGOMMultiImageElement() {
	}

	/**
	 * Creates a new SVG MultiImageElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMMultiImageElement(String prefix, AbstractDocument owner) {
		super(prefix, owner);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG12Constants.SVG_MULTI_IMAGE_TAG;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMMultiImageElement();
	}

}
