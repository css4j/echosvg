/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.ext.awt.image.renderable;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;

import org.apache.xmlgraphics.java2d.color.ICCColorSpaceWithIntent;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.rendered.CachableRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.ProfileRed;

/**
 * Implements the interface expected from a color matrix operation
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ProfileRable extends AbstractRable {

	private ICCColorSpaceWithIntent colorSpace;

	/**
	 * Instances should be built through the static factory methods
	 */
	public ProfileRable(Filter src, ICCColorSpaceWithIntent colorSpace) {
		super(src);
		this.colorSpace = colorSpace;
	}

	/**
	 * Sets the source of the blur operation
	 */
	public void setSource(Filter src) {
		init(src, null);
	}

	/**
	 * Returns the source of the blur operation
	 */
	public Filter getSource() {
		return (Filter) getSources().get(0);
	}

	/**
	 * Sets the ColorSpace of the Profile operation
	 */
	public void setColorSpace(ICCColorSpaceWithIntent colorSpace) {
		touch();
		this.colorSpace = colorSpace;
	}

	/**
	 * Returns the ColorSpace of the Profile operation
	 */
	public ICCColorSpaceWithIntent getColorSpace() {
		return colorSpace;
	}

	@Override
	public RenderedImage createRendering(RenderContext rc) {
		//
		// Get source's rendered image
		//
		RenderedImage srcRI = getSource().createRendering(rc);

		if (srcRI == null)
			return null;

		CachableRed srcCR = GraphicsUtil.wrap(srcRI);
		return new ProfileRed(srcCR, colorSpace);
	}
}
