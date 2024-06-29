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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.ext.awt.image.ARGBChannel;
import io.sf.carte.echosvg.ext.awt.image.PadMode;
import io.sf.carte.echosvg.ext.awt.image.renderable.DisplacementMapRable8Bit;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.PadRable;
import io.sf.carte.echosvg.ext.awt.image.renderable.PadRable8Bit;
import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for the &lt;feDisplacementMap&gt; element.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGFeDisplacementMapElementBridge extends AbstractSVGFilterPrimitiveElementBridge {

	/**
	 * Constructs a new bridge for the &lt;feDisplacementMap&gt; element.
	 */
	public SVGFeDisplacementMapElementBridge() {
	}

	/**
	 * Returns 'feDisplacementMap'.
	 */
	@Override
	public String getLocalName() {
		return SVG_FE_DISPLACEMENT_MAP_TAG;
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

		// 'scale' attribute - default is 0
		float scale = convertNumber(filterElement, SVG_SCALE_ATTRIBUTE, 0, ctx);

		// 'xChannelSelector' attribute - default is 'A'
		ARGBChannel xChannelSelector = convertChannelSelector(filterElement, SVG_X_CHANNEL_SELECTOR_ATTRIBUTE,
				ARGBChannel.A, ctx);

		// 'yChannelSelector' attribute - default is 'A'
		ARGBChannel yChannelSelector = convertChannelSelector(filterElement, SVG_Y_CHANNEL_SELECTOR_ATTRIBUTE,
				ARGBChannel.A, ctx);

		// 'in' attribute
		Filter in = getIn(filterElement, filteredElement, filteredNode, inputFilter, filterMap, ctx);
		if (in == null) {
			return null; // disable the filter
		}

		// 'in2' attribute - required
		Filter in2 = getIn2(filterElement, filteredElement, filteredNode, inputFilter, filterMap, ctx);
		if (in2 == null) {
			return null; // disable the filter
		}

		Rectangle2D defaultRegion;
		defaultRegion = (Rectangle2D) in.getBounds2D().clone();
		defaultRegion.add(in2.getBounds2D());
		// get filter primitive chain region
		Rectangle2D primitiveRegion = SVGUtilities.convertFilterPrimitiveRegion(filterElement, filteredElement,
				filteredNode, defaultRegion, filterRegion, ctx);

		PadRable pad = new PadRable8Bit(in, primitiveRegion, PadMode.ZERO_PAD);

		// build the displacement map filter
		List<Filter> srcs = new ArrayList<>(2);
		srcs.add(pad);
		srcs.add(in2);
		Filter displacementMap = new DisplacementMapRable8Bit(srcs, scale, xChannelSelector, yChannelSelector);

		// handle the 'color-interpolation-filters' property
		handleColorInterpolationFilters(displacementMap, filterElement);

		PadRable filter = new PadRable8Bit(displacementMap, primitiveRegion, PadMode.ZERO_PAD);

		// update the filter Map
		updateFilterMap(filterElement, filter, filterMap);

		return filter;
	}

	/**
	 * Returns the channel for the specified feDisplacementMap filter primitive
	 * attribute, considering the specified attribute name.
	 *
	 * @param filterElement the feDisplacementMap filter primitive element
	 * @param attrName      the name of the channel attribute
	 * @param ctx           the BridgeContext to use for error information
	 */
	protected static ARGBChannel convertChannelSelector(Element filterElement, String attrName,
			ARGBChannel defaultChannel, BridgeContext ctx) {

		String s = filterElement.getAttributeNS(null, attrName);
		if (s.length() == 0) {
			return defaultChannel;
		}
		if (SVG_A_VALUE.equals(s)) {
			return ARGBChannel.A;
		}
		if (SVG_R_VALUE.equals(s)) {
			return ARGBChannel.R;
		}
		if (SVG_G_VALUE.equals(s)) {
			return ARGBChannel.G;
		}
		if (SVG_B_VALUE.equals(s)) {
			return ARGBChannel.B;
		}
		throw new BridgeException(ctx, filterElement, ERR_ATTRIBUTE_VALUE_MALFORMED, new Object[] { attrName, s });
	}

}
