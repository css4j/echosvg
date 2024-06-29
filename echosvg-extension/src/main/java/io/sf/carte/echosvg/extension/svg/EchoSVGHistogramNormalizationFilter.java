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
package io.sf.carte.echosvg.extension.svg;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.FilterColorInterpolation;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface EchoSVGHistogramNormalizationFilter extends FilterColorInterpolation {

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
	 * Returns the trim percent for this normalization.
	 */
	float getTrim();

	/**
	 * Sets the trim percent for this normalization.
	 */
	void setTrim(float trim);

}
