/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

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

import io.sf.carte.echosvg.ext.awt.image.CompositeRule;
import io.sf.carte.echosvg.ext.awt.image.PadMode;
import io.sf.carte.echosvg.ext.awt.image.renderable.CompositeRable8Bit;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.PadRable8Bit;
import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for the &lt;feBlend&gt; element.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGFeBlendElementBridge extends AbstractSVGFilterPrimitiveElementBridge {

	/**
	 * Constructs a new bridge for the &lt;feBlend&gt; element.
	 */
	public SVGFeBlendElementBridge() {
	}

	/**
	 * Returns 'feBlend'.
	 */
	@Override
	public String getLocalName() {
		return SVG_FE_BLEND_TAG;
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

		// 'mode' attribute - default is 'normal'
		CompositeRule rule = convertMode(filterElement, ctx);

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

		// build the blend filter
		List<Filter> srcs = new ArrayList<>(2);
		srcs.add(in2);
		srcs.add(in);

		Filter filter = new CompositeRable8Bit(srcs, rule, true);
		// handle the 'color-interpolation-filters' property
		handleColorInterpolationFilters(filter, filterElement);

		filter = new PadRable8Bit(filter, primitiveRegion, PadMode.ZERO_PAD);

		// update the filter Map
		updateFilterMap(filterElement, filter, filterMap);

		return filter;
	}

	/**
	 * Converts the 'mode' of the specified feBlend filter primitive.
	 *
	 * @param filterElement the filter feBlend element
	 */
	protected static CompositeRule convertMode(Element filterElement, BridgeContext ctx) {
		String rule = filterElement.getAttributeNS(null, SVG_MODE_ATTRIBUTE);
		if (rule.length() == 0) {
			return CompositeRule.OVER;
		}
		if (SVG_NORMAL_VALUE.equals(rule)) {
			return CompositeRule.OVER;
		}
		if (SVG_MULTIPLY_VALUE.equals(rule)) {
			return CompositeRule.MULTIPLY;
		}
		if (SVG_SCREEN_VALUE.equals(rule)) {
			return CompositeRule.SCREEN;
		}
		if (SVG_DARKEN_VALUE.equals(rule)) {
			return CompositeRule.DARKEN;
		}
		if (SVG_LIGHTEN_VALUE.equals(rule)) {
			return CompositeRule.LIGHTEN;
		}
		throw new BridgeException(ctx, filterElement, ERR_ATTRIBUTE_VALUE_MALFORMED,
				new Object[] { SVG_MODE_ATTRIBUTE, rule });
	}
}
