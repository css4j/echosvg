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

import java.awt.Graphics2D;

/**
 * Interface for Rable's that can more efficently represent there action as a
 * paint method instead of a RenderedImage when going to a Graphics2D anyways.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface PaintRable {

	/**
	 * Should perform the equivilent action as createRendering followed by drawing
	 * the RenderedImage.
	 *
	 * @param g2d The Graphics2D to draw to.
	 * @return true if the paint call succeeded, false if for some reason the paint
	 *         failed (in which case a createRendering should be used).
	 */
	boolean paintRable(Graphics2D g2d);
}
