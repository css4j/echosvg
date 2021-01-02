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
package io.sf.carte.echosvg.extension.svg;

import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.bridge.AbstractSVGFilterPrimitiveElementBridge;
import io.sf.carte.echosvg.bridge.Bridge;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.BridgeException;
import io.sf.carte.echosvg.bridge.SVGUtilities;
import io.sf.carte.echosvg.ext.awt.image.PadMode;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.PadRable8Bit;
import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for a histogram normalization element.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas Deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class EchoSVGHistogramNormalizationElementBridge extends AbstractSVGFilterPrimitiveElementBridge
		implements EchoSVGExtConstants {

	/**
	 * Constructs a new bridge for the &lt;histogramNormalization&gt; element.
	 */
	public EchoSVGHistogramNormalizationElementBridge() {
		/* nothing */ }

	/**
	 * Returns the SVG namespace URI.
	 */
	@Override
	public String getNamespaceURI() {
		return BATIK_EXT_NAMESPACE_URI;
	}

	/**
	 * Returns 'histogramNormalization'.
	 */
	@Override
	public String getLocalName() {
		return BATIK_EXT_HISTOGRAM_NORMALIZATION_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new EchoSVGHistogramNormalizationElementBridge();
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

		// 'in' attribute
		Filter in = getIn(filterElement, filteredElement, filteredNode, inputFilter, filterMap, ctx);
		if (in == null) {
			return null; // disable the filter
		}

		// The default region is the union of the input sources
		// regions unless 'in' is 'SourceGraphic' in which case the
		// default region is the filterChain's region
		Filter sourceGraphics = filterMap.get(SVG_SOURCE_GRAPHIC_VALUE);
		Rectangle2D defaultRegion;
		if (in == sourceGraphics) {
			defaultRegion = filterRegion;
		} else {
			defaultRegion = in.getBounds2D();
		}

		Rectangle2D primitiveRegion = SVGUtilities.convertFilterPrimitiveRegion(filterElement, filteredElement,
				filteredNode, defaultRegion, filterRegion, ctx);

		float trim = 1;
		String s = filterElement.getAttributeNS(null, EXT_TRIM_ATTRIBUTE);

		if (s.length() != 0) {
			try {
				trim = SVGUtilities.convertSVGNumber(s);
			} catch (NumberFormatException nfEx) {
				throw new BridgeException(ctx, filterElement, nfEx, ERR_ATTRIBUTE_VALUE_MALFORMED,
						new Object[] { EXT_TRIM_ATTRIBUTE, s });
			}
		}

		if (trim < 0)
			trim = 0;
		else if (trim > 100)
			trim = 100;

		Filter filter = in;
		filter = new EchoSVGHistogramNormalizationFilter8Bit(filter, trim / 100);

		filter = new PadRable8Bit(filter, primitiveRegion, PadMode.ZERO_PAD);

		// update the filter Map
		updateFilterMap(filterElement, filter, filterMap);

		// handle the 'color-interpolation-filters' property
		handleColorInterpolationFilters(filter, filterElement);

		return filter;
	}

	/**
	 * Stolen from AbstractSVGFilterPrimitiveElementBridge. Converts on the
	 * specified filter primitive element, the specified attribute that represents
	 * an integer and with the specified default value.
	 *
	 * @param filterElement the filter primitive element
	 * @param attrName      the name of the attribute
	 * @param defaultValue  the default value of the attribute
	 * @param ctx           the BridgeContext to use for error information
	 */
	protected static int convertSides(Element filterElement, String attrName, int defaultValue, BridgeContext ctx) {
		String s = filterElement.getAttributeNS(null, attrName);
		if (s.length() == 0) {
			return defaultValue;
		} else {
			int ret = 0;
			try {
				ret = SVGUtilities.convertSVGInteger(s);
			} catch (NumberFormatException nfEx) {
				throw new BridgeException(ctx, filterElement, nfEx, ERR_ATTRIBUTE_VALUE_MALFORMED,
						new Object[] { attrName, s });
			}

			if (ret < 3)
				throw new BridgeException(ctx, filterElement, ERR_ATTRIBUTE_VALUE_MALFORMED,
						new Object[] { attrName, s });
			return ret;
		}
	}
}
