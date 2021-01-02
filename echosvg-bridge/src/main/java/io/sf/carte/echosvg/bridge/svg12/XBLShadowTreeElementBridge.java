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
package io.sf.carte.echosvg.bridge.svg12;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

import io.sf.carte.echosvg.bridge.AbstractGraphicsNodeBridge;
import io.sf.carte.echosvg.bridge.Bridge;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.GVTBuilder;
import io.sf.carte.echosvg.bridge.SVGUtilities;
import io.sf.carte.echosvg.gvt.CompositeGraphicsNode;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.XBLConstants;

/**
 * Bridge class for the &lt;xbl:shadowTree&gt; element.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class XBLShadowTreeElementBridge extends AbstractGraphicsNodeBridge {

	/**
	 * Constructs a new bridge for the &lt;xbl:shadowTree&gt; element.
	 */
	public XBLShadowTreeElementBridge() {
	}

	/**
	 * Returns 'shadowTree'.
	 */
	@Override
	public String getLocalName() {
		return XBLConstants.XBL_SHADOW_TREE_TAG;
	}

	/**
	 * Returns the XBL namespace.
	 */
	@Override
	public String getNamespaceURI() {
		return XBLConstants.XBL_NAMESPACE_URI;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new XBLShadowTreeElementBridge();
	}

	/**
	 * Creates a <code>GraphicsNode</code> according to the specified parameters.
	 *
	 * @param ctx the bridge context to use
	 * @param e   the element that describes the graphics node to build
	 * @return a graphics node that represents the specified element
	 */
	@Override
	public GraphicsNode createGraphicsNode(BridgeContext ctx, Element e) {
		// 'requiredFeatures', 'requiredExtensions' and 'systemLanguage'
		if (!SVGUtilities.matchUserAgent(e, ctx.getUserAgent())) {
			return null;
		}

		CompositeGraphicsNode cgn = new CompositeGraphicsNode();

		associateSVGContext(ctx, e, cgn);

		return cgn;
	}

	/**
	 * Creates a <code>CompositeGraphicsNode</code>.
	 */
	@Override
	protected GraphicsNode instantiateGraphicsNode() {
		// Not needed, since createGraphicsNode is overridden
		return null;
	}

	/**
	 * Builds using the specified BridgeContext and element, the specified graphics
	 * node.
	 *
	 * @param ctx  the bridge context to use
	 * @param e    the element that describes the graphics node to build
	 * @param node the graphics node to build
	 */
	@Override
	public void buildGraphicsNode(BridgeContext ctx, Element e, GraphicsNode node) {
		initializeDynamicSupport(ctx, e, node);
	}

	/**
	 * Returns true if the graphics node has to be displayed, false otherwise.
	 */
	@Override
	public boolean getDisplay(Element e) {
		return true;
	}

	/**
	 * Returns true as the &lt;xbl:template&gt; element is a container.
	 */
	@Override
	public boolean isComposite() {
		return true;
	}

	// BridgeUpdateHandler implementation //////////////////////////////////

	/**
	 * Invoked when an MutationEvent of type 'DOMNodeInserted' is fired.
	 */
	@Override
	public void handleDOMNodeInsertedEvent(MutationEvent evt) {
		if (evt.getTarget() instanceof Element) {
			handleElementAdded((CompositeGraphicsNode) node, e, (Element) evt.getTarget());
		}
	}

	/**
	 * Invoked when an MutationEvent of type 'DOMNodeInserted' is fired.
	 */
	public void handleElementAdded(CompositeGraphicsNode gn, Node parent, Element childElt) {
		// build the graphics node
		GVTBuilder builder = ctx.getGVTBuilder();
		GraphicsNode childNode = builder.build(ctx, childElt);
		if (childNode == null) {
			return; // the added element is not a graphic element
		}

		// Find the index where the GraphicsNode should be added
		int idx = -1;
		for (Node ps = childElt.getPreviousSibling(); ps != null; ps = ps.getPreviousSibling()) {
			if (ps.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element pse = (Element) ps;
			GraphicsNode psgn = ctx.getGraphicsNode(pse);
			while ((psgn != null) && (psgn.getParent() != gn)) {
				// In some cases the GN linked is
				// a child (in particular for images).
				psgn = psgn.getParent();
			}
			if (psgn == null)
				continue;
			idx = gn.indexOf(psgn);
			if (idx == -1)
				continue;
			break;
		}
		// insert after prevSibling, if
		// it was -1 this becomes 0 (first slot)
		idx++;
		gn.add(idx, childNode);
	}
}
