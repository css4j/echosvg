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

package io.sf.carte.echosvg.extension.svg;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.anim.dom.SVGOMElement;
import io.sf.carte.echosvg.bridge.Bridge;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.BridgeException;
import io.sf.carte.echosvg.bridge.CSSUtilities;
import io.sf.carte.echosvg.bridge.CursorManager;
import io.sf.carte.echosvg.bridge.SVGAElementBridge;
import io.sf.carte.echosvg.bridge.SVGTextElementBridge;
import io.sf.carte.echosvg.bridge.SVGUtilities;
import io.sf.carte.echosvg.bridge.TextNode;
import io.sf.carte.echosvg.bridge.TextUtilities;
import io.sf.carte.echosvg.bridge.UnitProcessor;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;
import io.sf.carte.echosvg.dom.util.XLinkSupport;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.gvt.text.GVTAttributedCharacterIterator;
import io.sf.carte.echosvg.gvt.text.TextPaintInfo;
import io.sf.carte.echosvg.gvt.text.TextPath;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Bridge class for the &lt;flowText&gt; element.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class EchoSVGFlowTextElementBridge extends SVGTextElementBridge implements EchoSVGExtConstants {

	public static final AttributedCharacterIterator.Attribute FLOW_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_PARAGRAPH;

	public static final AttributedCharacterIterator.Attribute FLOW_EMPTY_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_EMPTY_PARAGRAPH;

	public static final AttributedCharacterIterator.Attribute FLOW_LINE_BREAK = GVTAttributedCharacterIterator.TextAttribute.FLOW_LINE_BREAK;

	public static final AttributedCharacterIterator.Attribute FLOW_REGIONS = GVTAttributedCharacterIterator.TextAttribute.FLOW_REGIONS;

	public static final AttributedCharacterIterator.Attribute PREFORMATTED = GVTAttributedCharacterIterator.TextAttribute.PREFORMATTED;

	/**
	 * Constructs a new bridge for the &lt;flowText&gt; element.
	 */
	public EchoSVGFlowTextElementBridge() {
	}

	/**
	 * Returns the SVG namespace URI.
	 */
	@Override
	public String getNamespaceURI() {
		return BATIK_12_NAMESPACE_URI;
	}

	/**
	 * Returns 'flowText'.
	 */
	@Override
	public String getLocalName() {
		return BATIK_EXT_FLOW_TEXT_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new EchoSVGFlowTextElementBridge();
	}

	/**
	 * Returns false as text is not a container.
	 */
	@Override
	public boolean isComposite() {
		return false;
	}

	@Override
	protected GraphicsNode instantiateGraphicsNode() {
		return new FlowExtTextNode();
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
	protected void addContextToChild(BridgeContext ctx, Element e) {
		if (getNamespaceURI().equals(e.getNamespaceURI())) {
			String ln = e.getLocalName();
			if (ln.equals(BATIK_EXT_FLOW_PARA_TAG) || ln.equals(BATIK_EXT_FLOW_REGION_BREAK_TAG)
					|| ln.equals(BATIK_EXT_FLOW_LINE_TAG) || ln.equals(BATIK_EXT_FLOW_SPAN_TAG) || ln.equals(SVG_A_TAG)
					|| ln.equals(SVG_TREF_TAG)) {
				((SVGOMElement) e).setSVGContext(new EchoSVGFlowContentBridge(ctx, this, e));
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
	 * Creates the attributed string which represents the given text element
	 * children.
	 *
	 * @param ctx     the bridge context to use
	 * @param element the text element
	 */
	@Override
	protected AttributedString buildAttributedString(BridgeContext ctx, Element element) {
		List<RegionInfo> rgns = getRegions(ctx, element);
		AttributedString ret = getFlowDiv(ctx, element);
		if (ret == null)
			return ret;

		ret.addAttribute(FLOW_REGIONS, rgns, 0, 1);
		return ret;
	}

	/**
	 * Adds glyph position attributes to an AttributedString.
	 */
	@Override
	protected void addGlyphPositionAttributes(AttributedString as, Element element, BridgeContext ctx) {
		if (element.getNodeType() != Node.ELEMENT_NODE)
			return;
		String eNS = element.getNamespaceURI();
		if ((!eNS.equals(getNamespaceURI())) && (!eNS.equals(SVG_NAMESPACE_URI)))
			return;
		if (element.getLocalName() != BATIK_EXT_FLOW_TEXT_TAG) {
			// System.out.println("Elem: " + element);
			super.addGlyphPositionAttributes(as, element, ctx);
			return;
		}

		for (Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;
			String nNS = n.getNamespaceURI();
			if ((!getNamespaceURI().equals(nNS)) && (!SVG_NAMESPACE_URI.equals(nNS))) {
				continue;
			}
			Element e = (Element) n;
			String ln = e.getLocalName();
			if (ln.equals(BATIK_EXT_FLOW_DIV_TAG)) {
				// System.out.println("D Elem: " + e);
				super.addGlyphPositionAttributes(as, e, ctx);
				return;
			}
		}
	}

	@Override
	protected void addChildGlyphPositionAttributes(AttributedString as, Element element, BridgeContext ctx) {
		// Add Paint attributres for children of text element
		for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String cNS = child.getNamespaceURI();
			if ((!getNamespaceURI().equals(cNS)) && (!SVG_NAMESPACE_URI.equals(cNS))) {
				continue;
			}
			String ln = child.getLocalName();
			if (ln.equals(BATIK_EXT_FLOW_PARA_TAG) || ln.equals(BATIK_EXT_FLOW_REGION_BREAK_TAG)
					|| ln.equals(BATIK_EXT_FLOW_LINE_TAG) || ln.equals(BATIK_EXT_FLOW_SPAN_TAG) || ln.equals(SVG_A_TAG)
					|| ln.equals(SVG_TREF_TAG)) {
				addGlyphPositionAttributes(as, (Element) child, ctx);
			}
		}
	}

	/**
	 * Adds painting attributes to an AttributedString.
	 */
	@Override
	protected void addPaintAttributes(AttributedString as, Element element, TextNode node, TextPaintInfo parentPI,
			BridgeContext ctx) {
		if (element.getNodeType() != Node.ELEMENT_NODE)
			return;
		String eNS = element.getNamespaceURI();
		if ((!eNS.equals(getNamespaceURI())) && (!eNS.equals(SVG_NAMESPACE_URI)))
			return;
		if (element.getLocalName() != BATIK_EXT_FLOW_TEXT_TAG) {
			// System.out.println("Elem: " + element);
			super.addPaintAttributes(as, element, node, parentPI, ctx);
			return;
		}

		for (Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (!getNamespaceURI().equals(n.getNamespaceURI()))
				continue;
			Element e = (Element) n;
			String ln = e.getLocalName();
			if (ln.equals(BATIK_EXT_FLOW_DIV_TAG)) {
				// System.out.println("D Elem: " + e);
				super.addPaintAttributes(as, e, node, parentPI, ctx);
				return;
			}
		}
	}

	@Override
	protected void addChildPaintAttributes(AttributedString as, Element element, TextNode node, TextPaintInfo parentPI,
			BridgeContext ctx) {
		// Add Paint attributres for children of text element
		for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String cNS = child.getNamespaceURI();
			if ((!getNamespaceURI().equals(cNS)) && (!SVG_NAMESPACE_URI.equals(cNS))) {
				continue;
			}
			String ln = child.getLocalName();
			if (ln.equals(BATIK_EXT_FLOW_PARA_TAG) || ln.equals(BATIK_EXT_FLOW_REGION_BREAK_TAG)
					|| ln.equals(BATIK_EXT_FLOW_LINE_TAG) || ln.equals(BATIK_EXT_FLOW_SPAN_TAG) || ln.equals(SVG_A_TAG)
					|| ln.equals(SVG_TREF_TAG)) {
				Element childElement = (Element) child;
				TextPaintInfo pi = getTextPaintInfo(childElement, node, parentPI, ctx);
				addPaintAttributes(as, childElement, node, pi, ctx);
			}
		}
	}

	protected AttributedString getFlowDiv(BridgeContext ctx, Element element) {
		for (Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (!getNamespaceURI().equals(n.getNamespaceURI()))
				continue;
			Element e = (Element) n;

			String ln = n.getLocalName();
			if (ln.equals(BATIK_EXT_FLOW_DIV_TAG)) {
				return gatherFlowPara(ctx, e);
			}
		}
		return null;
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
		for (Node n = div.getFirstChild(); n != null; n = n.getNextSibling()) {

			if (n.getNodeType() != Node.ELEMENT_NODE || !getNamespaceURI().equals(n.getNamespaceURI())) {
				continue;
			}
			Element e = (Element) n;

			String ln = e.getLocalName();
			if (ln.equals(BATIK_EXT_FLOW_PARA_TAG)) {
				fillAttributedStringBuffer(ctx, e, true, null, null, asb, lnLocs);

				paraElems.add(e);
				paraEnds.add(asb.length());
			} else if (ln.equals(BATIK_EXT_FLOW_REGION_BREAK_TAG)) {
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

		// Note: The Working Group (in conjunction with XHTML working
		// group) has decided that multiple line elements collapse.
		int prevLN = 0;
		for (Integer lnLoc : lnLocs) {
			int nextLN = lnLoc;
			if (nextLN == prevLN)
				continue;

			ret.addAttribute(FLOW_LINE_BREAK, new Object(), prevLN, nextLN);
			// System.out.println("Attr: [" + prevLN + "," + nextLN + "]");
			prevLN = nextLN;
		}

		int start = 0;
		int end;
		List<MarginInfo> emptyPara = null;
		for (int i = 0; i < paraElems.size(); i++, start = end) {
			Element elem = paraElems.get(i);
			end = paraEnds.get(i);
			if (start == end) {
				if (emptyPara == null)
					emptyPara = new LinkedList<>();
				emptyPara.add(makeMarginInfo(elem));
				continue;
			}
			// System.out.println("Para: [" + start + ", " + end + "]");
			ret.addAttribute(FLOW_PARAGRAPH, makeMarginInfo(elem), start, end);
			if (emptyPara != null) {
				ret.addAttribute(FLOW_EMPTY_PARAGRAPH, emptyPara, start, end);
				emptyPara = null;
			}
		}

		return ret;
	}

	protected List<RegionInfo> getRegions(BridgeContext ctx, Element element) {
		List<RegionInfo> ret = new LinkedList<>();
		for (Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {

			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (!getNamespaceURI().equals(n.getNamespaceURI()))
				continue;

			Element e = (Element) n;

			String ln = e.getLocalName();
			if (!BATIK_EXT_FLOW_REGION_TAG.equals(ln))
				continue;

			// our default alignment is to the top of the flow rect.
			float verticalAlignment = 0.0f;
			String verticalAlignmentAttribute = e.getAttribute(EXT_VERTICAL_ALIGN_ATTRIBUTE);

			if ((verticalAlignmentAttribute != null) && (verticalAlignmentAttribute.length() > 0)) {
				if (EXT_ALIGN_TOP_VALUE.equals(verticalAlignmentAttribute)) {
					verticalAlignment = 0.0f;
				} else if (EXT_ALIGN_MIDDLE_VALUE.equals(verticalAlignmentAttribute)) {
					verticalAlignment = 0.5f;
				} else if (EXT_ALIGN_BOTTOM_VALUE.equals(verticalAlignmentAttribute)) {
					verticalAlignment = 1.0f;
				}
			}

			gatherRegionInfo(ctx, e, verticalAlignment, ret);
		}

		return ret;
	}

	protected void gatherRegionInfo(BridgeContext ctx, Element rgn, float verticalAlign, List<RegionInfo> regions) {

		for (Node n = rgn.getFirstChild(); n != null; n = n.getNextSibling()) {

			if (n.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (!getNamespaceURI().equals(n.getNamespaceURI()))
				continue;
			Element e = (Element) n;
			String ln = n.getLocalName();
			if (ln.equals(SVGConstants.SVG_RECT_TAG)) {
				UnitProcessor.Context uctx;
				uctx = UnitProcessor.createContext(ctx, e);

				RegionInfo ri = buildRegion(uctx, e, verticalAlign);
				if (ri != null)
					regions.add(ri);
			}
		}
	}

	protected RegionInfo buildRegion(UnitProcessor.Context uctx, Element e, float verticalAlignment) {
		String s;

		// 'x' attribute - default is 0
		s = e.getAttribute(EXT_X_ATTRIBUTE);
		float x = 0;
		if (s.length() != 0) {
			x = UnitProcessor.svgHorizontalCoordinateToUserSpace(s, EXT_X_ATTRIBUTE, uctx);
		}

		// 'y' attribute - default is 0
		s = e.getAttribute(EXT_Y_ATTRIBUTE);
		float y = 0;
		if (s.length() != 0) {
			y = UnitProcessor.svgVerticalCoordinateToUserSpace(s, EXT_Y_ATTRIBUTE, uctx);
		}

		// 'width' attribute - required
		s = e.getAttribute(EXT_WIDTH_ATTRIBUTE);
		float w;
		if (s.length() != 0) {
			w = UnitProcessor.svgHorizontalLengthToUserSpace(s, EXT_WIDTH_ATTRIBUTE, uctx);
		} else {
			throw new BridgeException(ctx, e, ERR_ATTRIBUTE_MISSING, new Object[] { EXT_WIDTH_ATTRIBUTE, s });
		}
		// A value of zero disables rendering of the element
		if (w == 0) {
			return null;
		}

		// 'height' attribute - required
		s = e.getAttribute(EXT_HEIGHT_ATTRIBUTE);
		float h;
		if (s.length() != 0) {
			h = UnitProcessor.svgVerticalLengthToUserSpace(s, EXT_HEIGHT_ATTRIBUTE, uctx);
		} else {
			throw new BridgeException(ctx, e, ERR_ATTRIBUTE_MISSING, new Object[] { EXT_HEIGHT_ATTRIBUTE, s });
		}
		// A value of zero disables rendering of the element
		if (h == 0) {
			return null;
		}

		return new RegionInfo(x, y, w, h, verticalAlignment);
	}

	/**
	 * Fills the given AttributedStringBuffer.
	 */
	protected void fillAttributedStringBuffer(BridgeContext ctx, Element element, boolean top, Integer bidiLevel,
			Map<Attribute, Object> initialAttributes, AttributedStringBuffer asb, List<Integer> lnLocs) {
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

		if (top)
			endLimit = 0;
		if (preserve)
			endLimit = asb.length();

		Map<Attribute, Object> map = initialAttributes == null ? new HashMap<>() : new HashMap<>(initialAttributes);
		initialAttributes = getAttributeMap(ctx, element, null, bidiLevel, map);
		Object o = map.get(TextAttribute.BIDI_EMBEDDING);
		Integer subBidiLevel = bidiLevel;
		if (o != null) {
			subBidiLevel = (Integer) o;
		}

		for (Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {

			if (preserve) {
				prevEndsWithSpace = false;
			} else {
				if (asb.length() == 0) {
					prevEndsWithSpace = true;
				} else {
					prevEndsWithSpace = (asb.getLastChar() == ' ');
				}
			}

			switch (n.getNodeType()) {
			case Node.ELEMENT_NODE:
				// System.out.println("Element: " + n);
				if ((!getNamespaceURI().equals(n.getNamespaceURI()))
						&& (!SVG_NAMESPACE_URI.equals(n.getNamespaceURI())))
					break;

				nodeElement = (Element) n;

				String ln = n.getLocalName();

				if (ln.equals(BATIK_EXT_FLOW_LINE_TAG)) {
					int before = asb.length();
					fillAttributedStringBuffer(ctx, nodeElement, false, subBidiLevel, initialAttributes, asb, lnLocs);
					// System.out.println("Line: " + asb.length() +
					// " - '" + asb + "'");
					lnLocs.add(asb.length());
					if (asb.length() != before) {
						initialAttributes = null;
					}
				} else if (ln.equals(BATIK_EXT_FLOW_SPAN_TAG) || ln.equals(SVG_ALT_GLYPH_TAG)) {
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
						HashMap<Attribute, Object> m = initialAttributes == null ? new HashMap<>()
								: new HashMap<>(initialAttributes);
						getAttributeMap(ctx, nodeElement, null, bidiLevel, m);
						asb.append(s, m);
						int trefEnd = asb.length() - 1;
						TextPaintInfo tpi;
						tpi = elemTPI.get(nodeElement);
						tpi.startChar = trefStart;
						tpi.endChar = trefEnd;
						initialAttributes = null;
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
		Map<Attribute, Object> initialMap = super.getAttributeMap(ctx, element, textPath, bidiLevel, result);
		String s;
		s = element.getAttribute(EXT_PREFORMATTED_ATTRIBUTE);
		if (s.length() != 0) {
			if (s.equals("true")) {
				result.put(PREFORMATTED, Boolean.TRUE);
			}
		}
		return initialMap;
	}

	protected void checkMap(Map<Attribute, ?> attrs) {
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

	protected static final GVTAttributedCharacterIterator.TextAttribute TEXTPATH = GVTAttributedCharacterIterator.TextAttribute.TEXTPATH;

	protected static final GVTAttributedCharacterIterator.TextAttribute ANCHOR_TYPE = GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE;

	protected static final GVTAttributedCharacterIterator.TextAttribute LETTER_SPACING = GVTAttributedCharacterIterator.TextAttribute.LETTER_SPACING;

	protected static final GVTAttributedCharacterIterator.TextAttribute WORD_SPACING = GVTAttributedCharacterIterator.TextAttribute.WORD_SPACING;

	protected static final GVTAttributedCharacterIterator.TextAttribute KERNING = GVTAttributedCharacterIterator.TextAttribute.KERNING;

	public static class LineBreakInfo {

		int breakIdx;
		float lineAdvAdj;
		boolean relative;

		/**
		 * @param breakIdx   The character after which to break.
		 * @param lineAdvAdj The line advance adjustment.
		 * @param relative   If true lineAdvAdj must be multiplied by the line height.
		 */
		public LineBreakInfo(int breakIdx, float lineAdvAdj, boolean relative) {
			this.breakIdx = breakIdx;
			this.lineAdvAdj = lineAdvAdj;
			this.relative = relative;
		}

		public int getBreakIdx() {
			return breakIdx;
		}

		public boolean isRelative() {
			return relative;
		}

		public float getLineAdvAdj() {
			return lineAdvAdj;
		}

	}

	public MarginInfo makeMarginInfo(Element e) {
		String s;
		float top = 0, right = 0, bottom = 0, left = 0;

		s = e.getAttribute(EXT_MARGIN_ATTRIBUTE);
		try {
			if (s.length() != 0) {
				float f = Float.parseFloat(s);
				top = right = bottom = left = f;
			}
		} catch (NumberFormatException nfe) {
			/* nothing */ }

		s = e.getAttribute(EXT_TOP_MARGIN_ATTRIBUTE);
		try {
			if (s.length() != 0) {
				float f = Float.parseFloat(s);
				top = f;
			}
		} catch (NumberFormatException nfe) {
			/* nothing */ }
		s = e.getAttribute(EXT_RIGHT_MARGIN_ATTRIBUTE);
		try {
			if (s.length() != 0) {
				float f = Float.parseFloat(s);
				right = f;
			}
		} catch (NumberFormatException nfe) {
			/* nothing */ }
		s = e.getAttribute(EXT_BOTTOM_MARGIN_ATTRIBUTE);
		try {
			if (s.length() != 0) {
				float f = Float.parseFloat(s);
				bottom = f;
			}
		} catch (NumberFormatException nfe) {
			/* nothing */ }
		s = e.getAttribute(EXT_LEFT_MARGIN_ATTRIBUTE);
		try {
			if (s.length() != 0) {
				float f = Float.parseFloat(s);
				left = f;
			}
		} catch (NumberFormatException nfe) {
			/* nothing */ }

		float indent = 0;
		s = e.getAttribute(EXT_INDENT_ATTRIBUTE);
		try {
			if (s.length() != 0) {
				float f = Float.parseFloat(s);
				indent = f;
			}
		} catch (NumberFormatException nfe) {
			/* nothing */ }

		int justification = MarginInfo.JUSTIFY_START;
		s = e.getAttribute(EXT_JUSTIFICATION_ATTRIBUTE);
		try {
			if (s.length() != 0) {
				if (EXT_JUSTIFICATION_START_VALUE.equals(s)) {
					justification = MarginInfo.JUSTIFY_START;
				} else if (EXT_JUSTIFICATION_MIDDLE_VALUE.equals(s)) {
					justification = MarginInfo.JUSTIFY_MIDDLE;
				} else if (EXT_JUSTIFICATION_END_VALUE.equals(s)) {
					justification = MarginInfo.JUSTIFY_END;
				} else if (EXT_JUSTIFICATION_FULL_VALUE.equals(s)) {
					justification = MarginInfo.JUSTIFY_FULL;
				}
			}
		} catch (NumberFormatException nfe) {
			/* nothing */ }

		String ln = e.getLocalName();
		boolean rgnBr = ln.equals(BATIK_EXT_FLOW_REGION_BREAK_TAG);
		return new MarginInfo(top, right, bottom, left, indent, justification, rgnBr);
	}

	/**
	 * Bridge class for flow text children that contain text.
	 */
	protected class EchoSVGFlowContentBridge extends AbstractTextChildTextContent {

		/**
		 * Creates a new FlowContentBridge.
		 */
		public EchoSVGFlowContentBridge(BridgeContext ctx, SVGTextElementBridge parent, Element e) {
			super(ctx, parent, e);
		}

	}

}
