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

package io.sf.carte.echosvg.bridge.svg12;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

import io.sf.carte.doc.style.css.CSSValue;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.echosvg.anim.dom.SVGOMElement;
import io.sf.carte.echosvg.anim.dom.SVGOMFlowRegionElement;
import io.sf.carte.echosvg.anim.dom.XBLEventSupport;
import io.sf.carte.echosvg.bridge.Bridge;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.CSSUtilities;
import io.sf.carte.echosvg.bridge.CursorManager;
import io.sf.carte.echosvg.bridge.FlowTextNode;
import io.sf.carte.echosvg.bridge.GVTBuilder;
import io.sf.carte.echosvg.bridge.SVGAElementBridge;
import io.sf.carte.echosvg.bridge.SVGTextElementBridge;
import io.sf.carte.echosvg.bridge.SVGUtilities;
import io.sf.carte.echosvg.bridge.TextNode;
import io.sf.carte.echosvg.bridge.TextUtilities;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.svg12.SVG12ValueConstants;
import io.sf.carte.echosvg.dom.AbstractNode;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;
import io.sf.carte.echosvg.dom.util.XLinkSupport;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.gvt.CompositeGraphicsNode;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.gvt.flow.BlockInfo;
import io.sf.carte.echosvg.gvt.flow.RegionInfo;
import io.sf.carte.echosvg.gvt.flow.TextLineBreaks;
import io.sf.carte.echosvg.gvt.font.GVTFont;
import io.sf.carte.echosvg.gvt.text.GVTAttributedCharacterIterator;
import io.sf.carte.echosvg.gvt.text.TextPaintInfo;
import io.sf.carte.echosvg.gvt.text.TextPath;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVG12CSSConstants;
import io.sf.carte.echosvg.util.SVG12Constants;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Bridge class for the &lt;flowRoot&gt; element.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGFlowRootElementBridge extends SVG12TextElementBridge {

	public static final AttributedCharacterIterator.Attribute FLOW_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_PARAGRAPH;

	public static final AttributedCharacterIterator.Attribute FLOW_EMPTY_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_EMPTY_PARAGRAPH;

	public static final AttributedCharacterIterator.Attribute FLOW_LINE_BREAK = GVTAttributedCharacterIterator.TextAttribute.FLOW_LINE_BREAK;

	public static final AttributedCharacterIterator.Attribute FLOW_REGIONS = GVTAttributedCharacterIterator.TextAttribute.FLOW_REGIONS;

	public static final AttributedCharacterIterator.Attribute LINE_HEIGHT = GVTAttributedCharacterIterator.TextAttribute.LINE_HEIGHT;

	public static final GVTAttributedCharacterIterator.TextAttribute TEXTPATH = GVTAttributedCharacterIterator.TextAttribute.TEXTPATH;

	public static final GVTAttributedCharacterIterator.TextAttribute ANCHOR_TYPE = GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE;

	public static final GVTAttributedCharacterIterator.TextAttribute LETTER_SPACING = GVTAttributedCharacterIterator.TextAttribute.LETTER_SPACING;

	public static final GVTAttributedCharacterIterator.TextAttribute WORD_SPACING = GVTAttributedCharacterIterator.TextAttribute.WORD_SPACING;

	public static final GVTAttributedCharacterIterator.TextAttribute KERNING = GVTAttributedCharacterIterator.TextAttribute.KERNING;

	/**
	 * Map of flowRegion elements to their graphics nodes.
	 */
	protected Map<Node, GraphicsNode> flowRegionNodes;

	protected TextNode textNode;

	@Override
	protected TextNode getTextNode() {
		return textNode;
	}

	/**
	 * Listener for flowRegion changes.
	 */
	protected RegionChangeListener regionChangeListener;

	/**
	 * Constructs a new bridge for the &lt;flowRoot&gt; element.
	 */
	public SVGFlowRootElementBridge() {
	}

	/**
	 * Returns the SVG namespace URI.
	 */
	@Override
	public String getNamespaceURI() {
		return SVGConstants.SVG_NAMESPACE_URI;
	}

	/**
	 * Returns 'flowRoot'.
	 */
	@Override
	public String getLocalName() {
		return SVG12Constants.SVG_FLOW_ROOT_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGFlowRootElementBridge();
	}

	/**
	 * Returns false as text is not a container.
	 */
	@Override
	public boolean isComposite() {
		return false;
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

		// 'transform'
		String s = e.getAttributeNS(null, SVG_TRANSFORM_ATTRIBUTE);
		if (s.length() != 0) {
			cgn.setTransform(SVGUtilities.convertTransform(e, SVG_TRANSFORM_ATTRIBUTE, s, ctx));
		}
		// 'visibility'
		cgn.setVisible(CSSUtilities.convertVisibility(e));

		// 'text-rendering' and 'color-rendering'
		RenderingHints hints = null;
		hints = CSSUtilities.convertColorRendering(e, hints);
		hints = CSSUtilities.convertTextRendering(e, hints);
		if (hints != null) {
			cgn.setRenderingHints(hints);
		}

		// first child holds the flow region nodes
		CompositeGraphicsNode cgn2 = new CompositeGraphicsNode();
		cgn.add(cgn2);

		// second child is the text node
		FlowTextNode tn = (FlowTextNode) instantiateGraphicsNode();
		tn.setLocation(getLocation(ctx, e));

		// specify the text painter to use
		if (ctx.getTextPainter() != null) {
			tn.setTextPainter(ctx.getTextPainter());
		}
		textNode = tn;
		cgn.add(tn);

		associateSVGContext(ctx, e, cgn);

		// traverse the children to add SVGContext
		Node child = getFirstChild(e);
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				addContextToChild(ctx, (Element) child);
			}
			child = getNextSibling(child);
		}

		return cgn;
	}

	/**
	 * Creates the graphics node for this element.
	 */
	@Override
	protected GraphicsNode instantiateGraphicsNode() {
		return new FlowTextNode();
	}

	/**
	 * Returns the text node location In this case the text node may have serveral
	 * effective locations (one for each flow region). So it always returns 0,0.
	 *
	 * @param ctx the bridge context to use
	 * @param e   the text element
	 */
	@Override
	protected Point2D getLocation(BridgeContext ctx, Element e) {
		return new Point2D.Float(0, 0);
	}

	@Override
	protected boolean isTextElement(Element e) {
		if (!SVG_NAMESPACE_URI.equals(e.getNamespaceURI()))
			return false;
		String nodeName = e.getLocalName();
		return (nodeName.equals(SVG12Constants.SVG_FLOW_DIV_TAG) || nodeName.equals(SVG12Constants.SVG_FLOW_LINE_TAG)
				|| nodeName.equals(SVG12Constants.SVG_FLOW_PARA_TAG)
				|| nodeName.equals(SVG12Constants.SVG_FLOW_REGION_BREAK_TAG)
				|| nodeName.equals(SVG12Constants.SVG_FLOW_SPAN_TAG));
	}

	@Override
	protected boolean isTextChild(Element e) {
		if (!SVG_NAMESPACE_URI.equals(e.getNamespaceURI()))
			return false;
		String nodeName = e.getLocalName();
		return (nodeName.equals(SVGConstants.SVG_A_TAG) || nodeName.equals(SVG12Constants.SVG_FLOW_LINE_TAG)
				|| nodeName.equals(SVG12Constants.SVG_FLOW_PARA_TAG)
				|| nodeName.equals(SVG12Constants.SVG_FLOW_REGION_BREAK_TAG)
				|| nodeName.equals(SVG12Constants.SVG_FLOW_SPAN_TAG));
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
		CompositeGraphicsNode cgn = (CompositeGraphicsNode) node;

		// build flowRegion shapes
		boolean isStatic = !ctx.isDynamic();
		if (isStatic) {
			flowRegionNodes = new HashMap<>();
		} else {
			regionChangeListener = new RegionChangeListener();
		}
		CompositeGraphicsNode cgn2 = (CompositeGraphicsNode) cgn.get(0);
		GVTBuilder builder = ctx.getGVTBuilder();
		for (Node n = getFirstChild(e); n != null; n = getNextSibling(n)) {
			if (n instanceof SVGOMFlowRegionElement) {
				for (Node m = getFirstChild(n); m != null; m = getNextSibling(m)) {
					if (m.getNodeType() != Node.ELEMENT_NODE) {
						continue;
					}
					GraphicsNode gn = builder.build(ctx, (Element) m);
					if (gn != null) {
						cgn2.add(gn);
						if (isStatic) {
							flowRegionNodes.put(m, gn);
						}
					}
				}

				if (!isStatic) {
					AbstractNode an = (AbstractNode) n;
					XBLEventSupport es = (XBLEventSupport) an.initializeEventSupport();
					es.addImplementationEventListenerNS(SVG_NAMESPACE_URI, "shapechange", regionChangeListener, false);
				}
			}
		}

		// build text node
		GraphicsNode tn = cgn.get(1);
		super.buildGraphicsNode(ctx, e, tn);

		// Drop references once static build is completed.
		flowRegionNodes = null;
	}

	@Override
	protected void computeLaidoutText(BridgeContext ctx, Element e, GraphicsNode node) {
		super.computeLaidoutText(ctx, getFlowDivElement(e), node);
	}

	/**
	 * Add to the element children of the node, a <code>SVGContext</code> to support
	 * dynamic update. This is recursive, the children of the nodes are also
	 * traversed to add to the support elements their context
	 *
	 * @param ctx a <code>BridgeContext</code> value
	 * @param e   an <code>Element</code> value
	 *
	 * @see io.sf.carte.echosvg.dom.svg.SVGContext
	 * @see io.sf.carte.echosvg.bridge.BridgeUpdateHandler
	 */
	@Override
	protected void addContextToChild(BridgeContext ctx, Element e) {
		if (SVG_NAMESPACE_URI.equals(e.getNamespaceURI())) {
			String ln = e.getLocalName();
			if (ln.equals(SVG12Constants.SVG_FLOW_DIV_TAG) || ln.equals(SVG12Constants.SVG_FLOW_LINE_TAG)
					|| ln.equals(SVG12Constants.SVG_FLOW_PARA_TAG) || ln.equals(SVG12Constants.SVG_FLOW_SPAN_TAG)) {
				((SVGOMElement) e).setSVGContext(new FlowContentBridge(ctx, this, e));
			}
		}

		// traverse the children to add SVGContext
		Node child = getFirstChild(e);
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				addContextToChild(ctx, (Element) child);
			}
			child = getNextSibling(child);
		}
	}

	/**
	 * From the <code>SVGContext</code> from the element children of the node.
	 *
	 * @param ctx the <code>BridgeContext</code> for the document
	 * @param e   the <code>Element</code> whose subtree's elements will have threir
	 *            <code>SVGContext</code>s removed
	 *
	 * @see io.sf.carte.echosvg.dom.svg.SVGContext
	 * @see io.sf.carte.echosvg.bridge.BridgeUpdateHandler
	 */
	@Override
	protected void removeContextFromChild(BridgeContext ctx, Element e) {
		if (SVG_NAMESPACE_URI.equals(e.getNamespaceURI())) {
			String ln = e.getLocalName();
			if (ln.equals(SVG12Constants.SVG_FLOW_DIV_TAG) || ln.equals(SVG12Constants.SVG_FLOW_LINE_TAG)
					|| ln.equals(SVG12Constants.SVG_FLOW_PARA_TAG) || ln.equals(SVG12Constants.SVG_FLOW_SPAN_TAG)) {
				((AbstractTextChildBridgeUpdateHandler) ((SVGOMElement) e).getSVGContext()).dispose();
			}
		}

		Node child = getFirstChild(e);
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				removeContextFromChild(ctx, (Element) child);
			}
			child = getNextSibling(child);
		}
	}

	/**
	 * Creates the attributed string which represents the given text element
	 * children.
	 *
	 * @param ctx     the bridge context to use
	 * @param element the text element
	 */
	@Override
	protected AttributedString buildAttributedString(BridgeContext ctx, Element element) {
		if (element == null)
			return null;

		List<RegionInfo> rgns = getRegions(ctx, element);
		AttributedString ret = getFlowDiv(ctx, element);
		if (ret == null)
			return ret;
		ret.addAttribute(FLOW_REGIONS, rgns, 0, 1);
		TextLineBreaks.findLineBrk(ret);
		// dumpACIWord(ret);
		return ret;
	}

	protected void dumpACIWord(AttributedString as) {
		if (as == null)
			return;

		StringBuilder chars = new StringBuilder();
		StringBuilder brkStr = new StringBuilder();
		AttributedCharacterIterator aci = as.getIterator();
		AttributedCharacterIterator.Attribute WORD_LIMIT = TextLineBreaks.WORD_LIMIT;

		for (char ch = aci.current(); ch != CharacterIterator.DONE; ch = aci.next()) {

			chars.append(ch).append(' ').append(' ');
			int w = (Integer) aci.getAttribute(WORD_LIMIT);
			brkStr.append(w).append(' ');
			if (w < 10) {
				// for small values append another ' '
				brkStr.append(' ');
			}
		}
		System.out.println(chars.toString());
		System.out.println(brkStr.toString());
	}

	protected Element getFlowDivElement(Element elem) {
		String eNS = elem.getNamespaceURI();
		if (!eNS.equals(SVG_NAMESPACE_URI))
			return null;

		String nodeName = elem.getLocalName();
		if (nodeName.equals(SVG12Constants.SVG_FLOW_DIV_TAG))
			return elem;

		if (!nodeName.equals(SVG12Constants.SVG_FLOW_ROOT_TAG))
			return null;

		for (Node n = getFirstChild(elem); n != null; n = getNextSibling(n)) {
			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;

			String nNS = n.getNamespaceURI();
			if (!SVG_NAMESPACE_URI.equals(nNS))
				continue;

			Element e = (Element) n;
			String ln = e.getLocalName();
			if (ln.equals(SVG12Constants.SVG_FLOW_DIV_TAG))
				return e;
		}
		return null;
	}

	protected AttributedString getFlowDiv(BridgeContext ctx, Element element) {
		Element flowDiv = getFlowDivElement(element);
		if (flowDiv == null)
			return null;

		return gatherFlowPara(ctx, flowDiv);
	}

	protected AttributedString gatherFlowPara(BridgeContext ctx, Element div) {
		TextPaintInfo divTPI = new TextPaintInfo(ctx);
		// Set some basic props so we can get bounds info for complex paints.
		divTPI.visible = true;
		divTPI.setFillPaint(Color.black);
		elemTPI.put(div, divTPI);

		AttributedStringBuffer asb = new AttributedStringBuffer();
		List<Integer> paraEnds = new ArrayList<>();
		List<Element> paraElems = new ArrayList<>();
		List<Integer> lnLocs = new ArrayList<>();
		for (Node n = getFirstChild(div); n != null; n = getNextSibling(n)) {

			if (n.getNodeType() != Node.ELEMENT_NODE || !getNamespaceURI().equals(n.getNamespaceURI())) {
				continue;
			}
			Element e = (Element) n;

			String ln = e.getLocalName();
			if (ln.equals(SVG12Constants.SVG_FLOW_PARA_TAG)) {
				fillAttributedStringBuffer(ctx, e, true, null, null, asb, lnLocs);

				paraElems.add(e);
				paraEnds.add(asb.length());
			} else if (ln.equals(SVG12Constants.SVG_FLOW_REGION_BREAK_TAG)) {
				fillAttributedStringBuffer(ctx, e, true, null, null, asb, lnLocs);

				paraElems.add(e);
				paraEnds.add(asb.length());
			}
		}
		divTPI.startChar = 0;
		divTPI.endChar = asb.length() - 1;

		// Layer in the PARAGRAPH/LINE_BREAK Attributes so we can
		// break up text chunks.
		AttributedString ret = asb.toAttributedString();
		if (ret == null)
			return null;

		// Note: The Working Group (in conjunction with XHTML working
		// group) has decided that multiple line elements collapse.
		int prevLN = 0;
		for (Integer lnLoc : lnLocs) {
			int nextLN = lnLoc;
			if (nextLN == prevLN)
				continue;

			// System.out.println("Attr: [" + prevLN + "," + nextLN + "]");
			ret.addAttribute(FLOW_LINE_BREAK, new Object(), prevLN, nextLN);
			prevLN = nextLN;
		}

		int start = 0;
		int end;
		List<BlockInfo> emptyPara = null;
		for (int i = 0; i < paraElems.size(); i++, start = end) {
			Element elem = paraElems.get(i);
			end = paraEnds.get(i);
			if (start == end) {
				if (emptyPara == null)
					emptyPara = new LinkedList<>();
				emptyPara.add(makeBlockInfo(ctx, elem));
				continue;
			}
			// System.out.println("Para: [" + start + ", " + end + "]");
			ret.addAttribute(FLOW_PARAGRAPH, makeBlockInfo(ctx, elem), start, end);
			if (emptyPara != null) {
				ret.addAttribute(FLOW_EMPTY_PARAGRAPH, emptyPara, start, end);
				emptyPara = null;
			}
		}

		return ret;
	}

	/**
	 * Returns a list of Shapes that define the flow regions.
	 */
	protected List<RegionInfo> getRegions(BridgeContext ctx, Element element) {
		// Element comes in as flowDiv element we want flowRoot.
		element = (Element) element.getParentNode();
		List<RegionInfo> ret = new LinkedList<>();
		for (Node n = getFirstChild(element); n != null; n = getNextSibling(n)) {

			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (!SVGConstants.SVG_NAMESPACE_URI.equals(n.getNamespaceURI()))
				continue;

			Element e = (Element) n;
			String ln = e.getLocalName();
			if (!SVG12Constants.SVG_FLOW_REGION_TAG.equals(ln))
				continue;

			// our default alignment is to the top of the flow rect.
			float verticalAlignment = 0.0f;

			gatherRegionInfo(ctx, e, verticalAlignment, ret);
		}

		return ret;
	}

	protected void gatherRegionInfo(BridgeContext ctx, Element rgn, float verticalAlign, List<RegionInfo> regions) {

		boolean isStatic = !ctx.isDynamic();
		for (Node n = getFirstChild(rgn); n != null; n = getNextSibling(n)) {

			if (n.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			GraphicsNode gn = isStatic ? flowRegionNodes.get(n) : ctx.getGraphicsNode(n);
			Shape s = gn.getOutline();
			if (s == null) {
				continue;
			}

			AffineTransform at = gn.getTransform();
			if (at != null) {
				s = at.createTransformedShape(s);
			}
			regions.add(new RegionInfo(s, verticalAlign));
		}
	}

	protected int startLen;

	/**
	 * Fills the given AttributedStringBuffer.
	 */
	protected void fillAttributedStringBuffer(BridgeContext ctx, Element element, boolean top, Integer bidiLevel,
			Map<Attribute, ?> initialAttributes, AttributedStringBuffer asb, List<Integer> lnLocs) {
		// 'requiredFeatures', 'requiredExtensions', 'systemLanguage' &
		// 'display="none".
		if ((!SVGUtilities.matchUserAgent(element, ctx.getUserAgent())) || (!CSSUtilities.convertDisplay(element))) {
			return;
		}

		String s = XMLSupport.getXMLSpace(element);
		boolean preserve = s.equals(SVG_PRESERVE_VALUE);
		boolean prevEndsWithSpace;
		Element nodeElement = element;
		int elementStartChar = asb.length();

		if (top) {
			endLimit = startLen = asb.length();
		}
		if (preserve) {
			endLimit = startLen;
		}

		Map<Attribute, Object> map = initialAttributes == null ? new HashMap<>() : new HashMap<>(initialAttributes);
		initialAttributes = getAttributeMap(ctx, element, null, bidiLevel, map);
		Object o = map.get(TextAttribute.BIDI_EMBEDDING);
		Integer subBidiLevel = bidiLevel;
		if (o != null) {
			subBidiLevel = (Integer) o;
		}

		int lineBreak = -1;
		if (lnLocs.size() != 0) {
			lineBreak = lnLocs.get(lnLocs.size() - 1);
		}

		for (Node n = getFirstChild(element); n != null; n = getNextSibling(n)) {

			if (preserve) {
				prevEndsWithSpace = false;
			} else {
				int len = asb.length();
				if (len == startLen) {
					prevEndsWithSpace = true;
				} else {
					prevEndsWithSpace = (asb.getLastChar() == ' ');
					int idx = lnLocs.size() - 1;
					if (!prevEndsWithSpace && (idx >= 0)) {
						Integer i = lnLocs.get(idx);
						if (i == len) {
							prevEndsWithSpace = true;
						}
					}
				}
			}

			switch (n.getNodeType()) {
			case Node.ELEMENT_NODE:
				// System.out.println("Element: " + n);
				if (!SVG_NAMESPACE_URI.equals(n.getNamespaceURI()))
					break;

				nodeElement = (Element) n;

				String ln = n.getLocalName();

				if (ln.equals(SVG12Constants.SVG_FLOW_LINE_TAG)) {
					int before = asb.length();
					fillAttributedStringBuffer(ctx, nodeElement, false, subBidiLevel, initialAttributes, asb, lnLocs);
					// System.out.println("Line: " + asb.length() +
					// " - '" + asb + "'");
					lineBreak = asb.length();
					lnLocs.add(lineBreak);
					if (before != lineBreak) {
						initialAttributes = null;
					}
				} else if (ln.equals(SVG12Constants.SVG_FLOW_SPAN_TAG) || ln.equals(SVGConstants.SVG_ALT_GLYPH_TAG)) {
					int before = asb.length();
					fillAttributedStringBuffer(ctx, nodeElement, false, subBidiLevel, initialAttributes, asb, lnLocs);
					if (asb.length() != before) {
						initialAttributes = null;
					}
				} else if (ln.equals(SVG_A_TAG)) {
					if (ctx.isInteractive()) {
						NodeEventTarget target = (NodeEventTarget) nodeElement;
						UserAgent ua = ctx.getUserAgent();
						SVGAElementBridge.CursorHolder ch;
						ch = new SVGAElementBridge.CursorHolder(CursorManager.DEFAULT_CURSOR);
						target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_CLICK,
								new SVGAElementBridge.AnchorListener(ua, ch), false, null);

						target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOVER,
								new SVGAElementBridge.CursorMouseOverListener(ua, ch), false, null);

						target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOUT,
								new SVGAElementBridge.CursorMouseOutListener(ua, ch), false, null);
					}
					int before = asb.length();
					fillAttributedStringBuffer(ctx, nodeElement, false, subBidiLevel, initialAttributes, asb, lnLocs);
					if (asb.length() != before) {
						initialAttributes = null;
					}
				} else if (ln.equals(SVG_TREF_TAG)) {
					String uriStr = XLinkSupport.getXLinkHref((Element) n);
					Element ref = ctx.getReferencedElement((Element) n, uriStr);
					if (ref == null) {
						continue; // Missing reference
					}
					s = TextUtilities.getElementContent(ref);
					s = normalizeString(s, preserve, prevEndsWithSpace);
					if (s.length() != 0) {
						int trefStart = asb.length();
						Map<Attribute, Object> m = new HashMap<>();
						getAttributeMap(ctx, nodeElement, null, bidiLevel, m);
						asb.append(s, m);
						int trefEnd = asb.length() - 1;
						TextPaintInfo tpi;
						tpi = elemTPI.get(nodeElement);
						tpi.startChar = trefStart;
						tpi.endChar = trefEnd;
					}
				}
				break;

			case Node.TEXT_NODE:
			case Node.CDATA_SECTION_NODE:
				s = n.getNodeValue();
				s = normalizeString(s, preserve, prevEndsWithSpace);
				if (s.length() != 0) {
					asb.append(s, map);
					if (preserve) {
						endLimit = asb.length();
					}
					initialAttributes = null;
				}
			}
		}

		if (top) {
			boolean strippedSome = false;
			while ((endLimit < asb.length()) && (asb.getLastChar() == ' ')) {
				int idx = lnLocs.size() - 1;
				int len = asb.length();
				if (idx >= 0) {
					Integer i = lnLocs.get(idx);
					if (i >= len) {
						i = len - 1;
						lnLocs.set(idx, i);
						idx--;
						while (idx >= 0) {
							i = lnLocs.get(idx);
							if (i < len - 1)
								break;
							lnLocs.remove(idx);
							idx--;
						}
					}
				}
				asb.stripLast();
				strippedSome = true;
			}
			if (strippedSome) {
				for (TextPaintInfo tpi : elemTPI.values()) {
					if (tpi.endChar >= asb.length()) {
						tpi.endChar = asb.length() - 1;
						if (tpi.startChar > tpi.endChar)
							tpi.startChar = tpi.endChar;
					}
				}
			}
		}

		int elementEndChar = asb.length() - 1;
		TextPaintInfo tpi = elemTPI.get(element);
		tpi.startChar = elementStartChar;
		tpi.endChar = elementEndChar;
	}

	@Override
	protected Map<Attribute, Object> getAttributeMap(BridgeContext ctx, Element element, TextPath textPath,
			Integer bidiLevel, Map<Attribute, Object> result) {
		Map<Attribute, Object> inheritingMap = super.getAttributeMap(ctx, element, textPath, bidiLevel, result);

		float fontSize = TextUtilities.convertFontSize(element);
		float lineHeight = getLineHeight(ctx, element, fontSize);
		result.put(LINE_HEIGHT, lineHeight);

		return inheritingMap;
	}

	protected void checkMap(Map<Attribute, Object> attrs) {
		if (attrs.containsKey(TEXTPATH)) {
			return; // Problem, unsupported attr
		}

		if (attrs.containsKey(ANCHOR_TYPE)) {
			return; // Problem, unsupported attr
		}

		if (attrs.containsKey(LETTER_SPACING)) {
			return; // Problem, unsupported attr
		}

		if (attrs.containsKey(WORD_SPACING)) {
			return; // Problem, unsupported attr
		}

		if (attrs.containsKey(KERNING)) {
			return; // Problem, unsupported attr
		}
	}

	int marginTopIndex = -1;
	int marginRightIndex = -1;
	int marginBottomIndex = -1;
	int marginLeftIndex = -1;
	int indentIndex = -1;
	int textAlignIndex = -1;
	int lineHeightIndex = -1;

	protected void initCSSPropertyIndexes(Element e) {
		CSSEngine eng = CSSUtilities.getCSSEngine(e);
		marginTopIndex = eng.getPropertyIndex(CSSConstants.CSS_MARGIN_TOP_PROPERTY);
		marginRightIndex = eng.getPropertyIndex(CSSConstants.CSS_MARGIN_RIGHT_PROPERTY);
		marginBottomIndex = eng.getPropertyIndex(CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY);
		marginLeftIndex = eng.getPropertyIndex(CSSConstants.CSS_MARGIN_LEFT_PROPERTY);
		indentIndex = eng.getPropertyIndex(SVG12CSSConstants.CSS_INDENT_PROPERTY);
		textAlignIndex = eng.getPropertyIndex(SVG12CSSConstants.CSS_TEXT_ALIGN_PROPERTY);
		lineHeightIndex = eng.getLineHeightIndex();
	}

	public BlockInfo makeBlockInfo(BridgeContext ctx, Element element) {
		if (marginTopIndex == -1)
			initCSSPropertyIndexes(element);

		Value v;
		v = CSSUtilities.getComputedStyle(element, marginTopIndex);
		float top = v.getFloatValue();

		v = CSSUtilities.getComputedStyle(element, marginRightIndex);
		float right = v.getFloatValue();

		v = CSSUtilities.getComputedStyle(element, marginBottomIndex);
		float bottom = v.getFloatValue();

		v = CSSUtilities.getComputedStyle(element, marginLeftIndex);
		float left = v.getFloatValue();

		v = CSSUtilities.getComputedStyle(element, indentIndex);
		float indent = v.getFloatValue();

		v = CSSUtilities.getComputedStyle(element, textAlignIndex);
		if (v.getCssValueType() == CSSValue.CssType.KEYWORD) {
			// inherit, unset, revert
			v = CSSUtilities.getComputedStyle(element, SVGCSSEngine.DIRECTION_INDEX);
			if (v.isIdentifier(CSSConstants.CSS_LTR_VALUE))
				v = SVG12ValueConstants.START_VALUE;
			else
				v = SVG12ValueConstants.END_VALUE;
		}
		int textAlign;
		if (v != null && v.getPrimitiveType() == Type.IDENT) {
			String s = v.getIdentifierValue();
			if (s == CSSConstants.CSS_START_VALUE)
				textAlign = BlockInfo.ALIGN_START;
			else if (s == CSSConstants.CSS_CENTER_VALUE || s == CSSConstants.CSS_MIDDLE_VALUE)
				textAlign = BlockInfo.ALIGN_MIDDLE;
			else if (s == CSSConstants.CSS_END_VALUE)
				textAlign = BlockInfo.ALIGN_END;
			else
				textAlign = BlockInfo.ALIGN_FULL;
		} else {
			textAlign = BlockInfo.ALIGN_FULL;
		}

		Map<Attribute, Object> fontAttrs = new HashMap<>(20);
		List<GVTFont> fontList = getFontList(ctx, element, fontAttrs);
		Float fs = (Float) fontAttrs.get(TextAttribute.SIZE);
		float fontSize = fs;
		float lineHeight = getLineHeight(ctx, element, fontSize);

		String ln = element.getLocalName();
		boolean rgnBr;
		rgnBr = ln.equals(SVG12Constants.SVG_FLOW_REGION_BREAK_TAG);
		return new BlockInfo(top, right, bottom, left, indent, textAlign, lineHeight, fontList, fontAttrs,
				rgnBr);
	}

	private float getLineHeight(BridgeContext ctx, Element element, float fontSize) {
		if (lineHeightIndex == -1) {
			initCSSPropertyIndexes(element);
		}

		Value v = CSSUtilities.getComputedStyle(element, lineHeightIndex);

		float lineHeight = v.getFloatValue();
		if (v.getUnitType() == CSSUnit.CSS_NUMBER) {
			lineHeight *= fontSize;
		}

		return lineHeight;
	}

	/**
	 * Bridge class for flow text children that contain text.
	 */
	protected class FlowContentBridge extends AbstractTextChildTextContent {

		/**
		 * Creates a new FlowContentBridge.
		 */
		public FlowContentBridge(BridgeContext ctx, SVGTextElementBridge parent, Element e) {
			super(ctx, parent, e);
		}

	}

	/**
	 * svg:shapechange listener for flowRegion elements.
	 */
	protected class RegionChangeListener implements EventListener {

		/**
		 * Handles the svg:shapechange event.
		 */
		@Override
		public void handleEvent(Event evt) {
			// the flowRegion geometry may have changed, so relayout text
			laidoutText = null;
			computeLaidoutText(ctx, e, getTextNode());
		}

	}

}
