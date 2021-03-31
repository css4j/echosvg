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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * Used to represent an SVG Composite. This can be achieved with to values: an
 * SVG opacity and a filter
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGCompositeDescriptor implements SVGDescriptor, SVGSyntax {
	private Element def;
	private String opacityValue;
	private String filterValue;

	public SVGCompositeDescriptor(String opacityValue, String filterValue) {
		this.opacityValue = opacityValue;
		this.filterValue = filterValue;
	}

	public SVGCompositeDescriptor(String opacityValue, String filterValue, Element def) {
		this(opacityValue, filterValue);
		this.def = def;
	}

	public String getOpacityValue() {
		return opacityValue;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public Element getDef() {
		return def;
	}

	@Override
	public Map<String, String> getAttributeMap(Map<String, String> attrMap) {
		if (attrMap == null)
			attrMap = new HashMap<>();

		attrMap.put(SVG_OPACITY_ATTRIBUTE, opacityValue);
		attrMap.put(SVG_FILTER_ATTRIBUTE, filterValue);

		return attrMap;
	}

	@Override
	public List<Element> getDefinitionSet(List<Element> defSet) {
		if (defSet == null)
			defSet = new LinkedList<>();

		if (def != null)
			defSet.add(def);

		return defSet;
	}
}
