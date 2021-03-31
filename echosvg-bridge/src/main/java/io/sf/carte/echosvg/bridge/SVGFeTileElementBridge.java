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

import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.TileRable8Bit;
import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for the &lt;feTile&gt; element.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGFeTileElementBridge extends AbstractSVGFilterPrimitiveElementBridge {

	/**
	 * Constructs a new bridge for the &lt;feTile&gt; element.
	 */
	public SVGFeTileElementBridge() {
	}

	/**
	 * Returns 'feTile'.
	 */
	@Override
	public String getLocalName() {
		return SVG_FE_TILE_TAG;
	}

	/**
	 * Creates a <code>Filter</code> primitive according to the specified
	 * parameters.
	 *
	 * @param ctx             the bridge context to use
	 * @param filterElement   the element that defines a filter
	 * @param filteredElement the element that references the filter
	 * @param filteredNode    the graphics node to filter
	 *
	 * @param inputFilter     the <code>Filter</code> that represents the current
	 *                        filter input if the filter chain.
	 * @param filterRegion    the filter area defined for the filter chain the new
	 *                        node will be part of.
	 * @param filterMap       a map where the mediator can map a name to the
	 *                        <code>Filter</code> it creates. Other
	 *                        <code>FilterBridge</code>s can then access a filter
	 *                        node from the filterMap if they know its name.
	 */
	@Override
	public Filter createFilter(BridgeContext ctx, Element filterElement, Element filteredElement,
			GraphicsNode filteredNode, Filter inputFilter, Rectangle2D filterRegion, Map<String, Filter> filterMap) {

		// Get the tiled region. For feTile, the default for the
		// filter primitive subregion is the parent filter region.
		Rectangle2D defaultRegion = filterRegion;
		Rectangle2D primitiveRegion = SVGUtilities.convertFilterPrimitiveRegion(filterElement, filteredElement,
				filteredNode, defaultRegion, filterRegion, ctx);

		// 'in' attribute
		Filter in = getIn(filterElement, filteredElement, filteredNode, inputFilter, filterMap, ctx);
		if (in == null) {
			return null; // disable the filter
		}

		Filter filter = new TileRable8Bit(in, primitiveRegion, in.getBounds2D(), false);

		// handle the 'color-interpolation-filters' property
		handleColorInterpolationFilters(filter, filterElement);

		// update the filter Map
		updateFilterMap(filterElement, filter, filterMap);

		return filter;
	}
}
