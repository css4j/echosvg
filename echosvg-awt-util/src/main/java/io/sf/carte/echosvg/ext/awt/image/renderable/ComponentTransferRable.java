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

import io.sf.carte.echosvg.ext.awt.image.ComponentTransferFunction;

/**
 * Defines the interface expected from a component transfer operation.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface ComponentTransferRable extends FilterColorInterpolation {

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
	 * Returns the transfer function for the alpha channel
	 */
	ComponentTransferFunction getAlphaFunction();

	/**
	 * Sets the transfer function for the alpha channel
	 */
	void setAlphaFunction(ComponentTransferFunction alphaFunction);

	/**
	 * Returns the transfer function for the red channel
	 */
	ComponentTransferFunction getRedFunction();

	/**
	 * Sets the transfer function for the red channel
	 */
	void setRedFunction(ComponentTransferFunction redFunction);

	/**
	 * Returns the transfer function for the green channel
	 */
	ComponentTransferFunction getGreenFunction();

	/**
	 * Sets the transfer function for the green channel
	 */
	void setGreenFunction(ComponentTransferFunction greenFunction);

	/**
	 * Returns the transfer function for the blue channel
	 */
	ComponentTransferFunction getBlueFunction();

	/**
	 * Sets the transfer function for the blue channel
	 */
	void setBlueFunction(ComponentTransferFunction blueFunction);
}
