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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.ext.awt.image.CompositeRule;
import io.sf.carte.echosvg.ext.awt.image.PadMode;
import io.sf.carte.echosvg.ext.awt.image.renderable.CompositeRable8Bit;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.renderable.PadRable8Bit;
import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for the &lt;feMerge&gt; element.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGFeMergeElementBridge
    extends AbstractSVGFilterPrimitiveElementBridge {

    /**
     * Constructs a new bridge for the &lt;feMerge&gt; element.
     */
    public SVGFeMergeElementBridge() {}

    /**
     * Returns 'feMerge'.
     */
    @Override
    public String getLocalName() {
        return SVG_FE_MERGE_TAG;
    }

    /**
     * Creates a <code>Filter</code> primitive according to the specified
     * parameters.
     *
     * @param ctx the bridge context to use
     * @param filterElement the element that defines a filter
     * @param filteredElement the element that references the filter
     * @param filteredNode the graphics node to filter
     * @param inputFilter the <code>Filter</code> that represents the current
     *        filter input if the filter chain.
     * @param filterRegion the filter area defined for the filter chain
     *        the new node will be part of.
     * @param filterMap a map where the mediator can map a name to the
     *        <code>Filter</code> it creates. Other <code>FilterBridge</code>s
     *        can then access a filter node from the filterMap if they
     *        know its name.
     */
    @Override
    public Filter createFilter(BridgeContext ctx,
                               Element filterElement,
                               Element filteredElement,
                               GraphicsNode filteredNode,
                               Filter inputFilter,
                               Rectangle2D filterRegion,
                               Map<String, Filter> filterMap) {

        List<Filter> srcs = extractFeMergeNode(filterElement,
                                       filteredElement,
                                       filteredNode,
                                       inputFilter,
                                       filterMap,
                                       ctx);

        if (srcs == null) {
            return null; // <!> FIXME: no subelement found, result unspecified
        }

        if (srcs.size() == 0) {
            return null; // <!> FIXME: no subelement found, result unspecified
        }

        // the default region is the input sources regions union
        Iterator<Filter> iter = srcs.iterator();
        Rectangle2D defaultRegion = 
            (Rectangle2D)iter.next().getBounds2D().clone();

        while (iter.hasNext()) {
            defaultRegion.add(iter.next().getBounds2D());
        }

        // get filter primitive chain region
        Rectangle2D primitiveRegion
            = SVGUtilities.convertFilterPrimitiveRegion(filterElement,
                                                        filteredElement,
                                                        filteredNode,
                                                        defaultRegion,
                                                        filterRegion,
                                                        ctx);

        Filter filter = new CompositeRable8Bit(srcs, CompositeRule.OVER, true);

        // handle the 'color-interpolation-filters' property
        handleColorInterpolationFilters(filter, filterElement);

        filter = new PadRable8Bit(filter, primitiveRegion, PadMode.ZERO_PAD);

        // update the filter Map
        updateFilterMap(filterElement, filter, filterMap);

        return filter;
    }


    /**
     * Returns a list of Filter objects that represents the feMergeNode of
     * the specified feMerge filter element.
     *
     * @param filterElement the feMerge filter element
     * @param filteredElement the filtered element
     * @param filteredNode the filtered graphics node
     * @param inputFilter the <code>Filter</code> that represents the current
     *        filter input if the filter chain.
     * @param filterMap the filter map that contains named filter primitives
     * @param ctx the bridge context
     */
    protected static List<Filter> extractFeMergeNode(Element filterElement,
                                             Element filteredElement,
                                             GraphicsNode filteredNode,
                                             Filter inputFilter,
                                             Map<String, Filter> filterMap,
                                             BridgeContext ctx) {

        List<Filter> srcs = null;
        for (Node n = filterElement.getFirstChild();
             n != null;
             n = n.getNextSibling()) {

            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element e = (Element)n;
            Bridge bridge = ctx.getBridge(e);
            if (bridge == null ||
                !(bridge instanceof SVGFeMergeNodeElementBridge)) {
                continue;
            }
            Filter filter =  ((SVGFeMergeNodeElementBridge)bridge).createFilter
                (ctx,
                 e,
                 filteredElement,
                 filteredNode,
                 inputFilter,
                 filterMap);
            if (filter != null) {
                if (srcs == null) {
                    srcs = new LinkedList<>();
                }
                srcs.add(filter);
            }
        }
        return srcs;
    }

    /**
     * Bridge class for the &lt;feMergeNode&gt; element.
     */
    public static class SVGFeMergeNodeElementBridge
            extends AnimatableGenericSVGBridge {

        /**
         * Constructs a new bridge for the &lt;feMergeNode&gt; element.
         */
        public SVGFeMergeNodeElementBridge() {}

        /**
         * Returns 'feMergeNode'.
         */
        @Override
        public String getLocalName() {
            return SVG_FE_MERGE_NODE_TAG;
        }

        /**
         * Creates a <code>Filter</code> according to the specified parameters.
         *
         * @param ctx the bridge context to use
         * @param filterElement the element that defines a filter
         * @param filteredElement the element that references the filter
         * @param filteredNode the graphics node to filter
         * @param inputFilter the <code>Filter</code> that represents the current
         *        filter input if the filter chain.
         * @param filterMap a map where the mediator can map a name to the
         *        <code>Filter</code> it creates. Other <code>FilterBridge</code>s
         *        can then access a filter node from the filterMap if they
         *        know its name.
         */
        public Filter createFilter(BridgeContext ctx,
                                   Element filterElement,
                                   Element filteredElement,
                                   GraphicsNode filteredNode,
                                   Filter inputFilter,
                                   Map<String, Filter> filterMap) {
            return getIn(filterElement,
                         filteredElement,
                         filteredNode,
                         inputFilter,
                         filterMap,
                         ctx);
        }
    }
}
