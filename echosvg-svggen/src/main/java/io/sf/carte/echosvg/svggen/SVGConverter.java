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

import java.util.List;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.ext.awt.g2d.GraphicContext;

/**
 * Defines the interface for classes that are able to convert part or all of a
 * GraphicContext.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 * @see io.sf.carte.echosvg.ext.awt.g2d.GraphicContext
 */
public interface SVGConverter extends SVGSyntax {

	/**
	 * Converts part or all of the input GraphicContext into a set of
	 * attribute/value pairs and related definitions
	 *
	 * @param gc GraphicContext to be converted
	 * @return descriptor of the attributes required to represent some or all of the
	 *         GraphicContext state, along with the related definitions
	 * @see io.sf.carte.echosvg.svggen.SVGDescriptor
	 */
	SVGDescriptor toSVG(GraphicContext gc);

	/**
	 * @return set of definitions referenced by the attribute values created by the
	 *         implementation since its creation. The return value should never be
	 *         null. If no definition is needed, an empty set should be returned.
	 */
	List<Element> getDefinitionSet();

}
