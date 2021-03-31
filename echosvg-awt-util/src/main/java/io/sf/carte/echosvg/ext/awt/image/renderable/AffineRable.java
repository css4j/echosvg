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
package io.sf.carte.echosvg.ext.awt.image.renderable;

import java.awt.geom.AffineTransform;

/**
 * Adjusts the input images coordinate system by a general Affine transform
 *
 * @author <a href="mailto:Thomas.DeWeeese@Kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface AffineRable extends Filter {
	/**
	 * Returns the source to be offset.
	 */
	Filter getSource();

	/**
	 * Sets the source to be offset.
	 * 
	 * @param src image to offset.
	 */
	void setSource(Filter src);

	/**
	 * Set the affine.
	 * 
	 * @param affine the new Affine transform for the filter.
	 */
	void setAffine(AffineTransform affine);

	/**
	 * Get the current affine.
	 * 
	 * @return The current affine transform for the filter.
	 */
	AffineTransform getAffine();
}
