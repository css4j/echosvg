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
package io.sf.carte.echosvg.bridge;

/**
 * Bridge class for the &lt;hkern&gt; element.
 *
 * @author <a href="mailto:dean.jackson@cmis.csiro.au">Dean Jackson</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGHKernElementBridge extends SVGKernElementBridge {

	/**
	 * Constructs a new bridge for the &lt;hkern&gt; element.
	 */
	public SVGHKernElementBridge() {
	}

	/**
	 * Returns 'hkern'.
	 */
	@Override
	public String getLocalName() {
		return SVG_HKERN_TAG;
	}

}
