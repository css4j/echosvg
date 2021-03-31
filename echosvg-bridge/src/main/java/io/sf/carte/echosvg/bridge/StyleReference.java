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

import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * A style reference represents a graphics node, CSS property pair. It describes
 * which GraphicsNode and which property of this GraphicsNode should be updated
 * when a style element (for example a filter) changes due to a modification of
 * the DOM.
 *
 * @author <a href="mailto:etissandier@ilog.fr">Emmanuel Tissandier</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class StyleReference {

	private GraphicsNode node;
	private String styleAttribute;

	/**
	 * Creates a new <code>StyleReference</code>.
	 * 
	 * @param node           the graphics node impacted.
	 * @param styleAttribute the name of the style attribute that is impacted.
	 */
	public StyleReference(GraphicsNode node, String styleAttribute) {
		this.node = node;
		this.styleAttribute = styleAttribute;
	}

	/**
	 * Returns the graphics node.
	 */
	public GraphicsNode getGraphicsNode() {
		return node;
	}

	/**
	 * Returns the style attribute
	 */
	public String getStyleAttribute() {
		return styleAttribute;
	}
}
