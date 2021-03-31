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
package io.sf.carte.echosvg.svggen;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository of SVG attribute descriptions, accessible by name.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAttributeMap {
	/**
	 * Map of attribute name to SVGAttribute objects
	 */
	private static Map<String, SVGAttribute> attrMap = new HashMap<>();

	/**
	 * @param attrName SVG name of the requested attribute
	 * @return attribute with requested name
	 */
	public static SVGAttribute get(String attrName) {
		return attrMap.get(attrName);
	}
}
