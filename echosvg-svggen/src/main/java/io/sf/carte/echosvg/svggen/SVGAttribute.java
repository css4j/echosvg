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

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an SVG attribute and provides convenience methods to determine
 * whether or not the attribute applies to a given element type.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAttribute {

	/**
	 * Set of Element tags to which the attribute does or does not apply.
	 */
	private Set<String> applicabilitySet;

	/**
	 * Controls the semantic of applicabilitySet. If true, then the applicabilitySet
	 * contains the elments to which the attribute applies. If false, the Set
	 * contains the elements to which the attribute does not apply.
	 */
	private boolean isSetInclusive;

	/**
	 * @param applicabilitySet Set of Element tags (Strings) to which the attribute
	 *                         applies
	 * @param isSetInclusive   defines whether elements in applicabilitySet define
	 *                         the list of elements to which the attribute applies
	 *                         or to which it does not apply
	 */
	public SVGAttribute(Set<String> applicabilitySet, boolean isSetInclusive) {
		if (applicabilitySet == null)
			applicabilitySet = new HashSet<>();

		this.applicabilitySet = applicabilitySet;
		this.isSetInclusive = isSetInclusive;
	}

	/**
	 * @param tag the tag of the Element to which the attribute could apply.
	 * @return true if the attribute applies to the given Element
	 */
	public boolean appliesTo(String tag) {
		boolean tagInMap = applicabilitySet.contains(tag);
		if (isSetInclusive)
			return tagInMap;
		else
			return !tagInMap;
	}
}
