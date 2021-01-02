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

import java.awt.Paint;
import java.awt.geom.Rectangle2D;

/**
 * Fills the input image with a given paint
 *
 * @author <a href="mailto:dean@w3.org">Dean Jackson</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public interface FloodRable extends Filter {
	/**
	 * Set the flood paint.
	 * 
	 * @param paint the flood paint to use when filling
	 */
	void setFloodPaint(Paint paint);

	/**
	 * Get the flood paint.
	 * 
	 * @return The current flood paint for the filter
	 */
	Paint getFloodPaint();

	/**
	 * Sets the flood region
	 * 
	 * @param floodRegion region to flood with floodPaint
	 */
	void setFloodRegion(Rectangle2D floodRegion);

	/**
	 * Get the flood region
	 */
	Rectangle2D getFloodRegion();
}
