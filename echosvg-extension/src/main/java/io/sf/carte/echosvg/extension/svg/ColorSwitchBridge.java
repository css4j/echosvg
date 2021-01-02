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

import java.awt.Color;
import java.awt.Paint;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.bridge.AbstractSVGBridge;
import io.sf.carte.echosvg.bridge.Bridge;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.PaintBridge;
import io.sf.carte.echosvg.bridge.SVGUtilities;
import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for the "color switch" extension element.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas Deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ColorSwitchBridge extends AbstractSVGBridge implements PaintBridge, EchoSVGExtConstants {

	/**
	 * Constructs a new bridge for the &lt;batik:colorSwitch&gt; element.
	 */
	public ColorSwitchBridge() {
		/* nothing */ }

	/**
	 * Returns the SVG namespace URI.
	 */
	@Override
	public String getNamespaceURI() {
		return BATIK_EXT_NAMESPACE_URI;
	}

	/**
	 * Returns 'colorSwitch'.
	 */
	@Override
	public String getLocalName() {
		return BATIK_EXT_COLOR_SWITCH_TAG;
	}

	/**
	 * Creates a <code>Paint</code> according to the specified parameters.
	 *
	 * @param ctx            the bridge context to use
	 * @param paintElement   the element that defines a Paint
	 * @param paintedElement the element referencing the paint
	 * @param paintedNode    the graphics node on which the Paint will be applied
	 * @param opacity        the opacity of the Paint to create
	 */
	@Override
	public Paint createPaint(BridgeContext ctx, Element paintElement, Element paintedElement, GraphicsNode paintedNode,
			float opacity) {
		Element clrDef = null;
		for (Node n = paintElement.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ((n.getNodeType() != Node.ELEMENT_NODE))
				continue;
			Element ref = (Element) n;
			if ( // (ref instanceof SVGTests) &&
			SVGUtilities.matchUserAgent(ref, ctx.getUserAgent())) {
				clrDef = ref;
				break;
			}
		}

		if (clrDef == null)
			return Color.black;

		Bridge bridge = ctx.getBridge(clrDef);
		if (bridge == null || !(bridge instanceof PaintBridge))
			return Color.black;

		return ((PaintBridge) bridge).createPaint(ctx, clrDef, paintedElement, paintedNode, opacity);
	}
}
