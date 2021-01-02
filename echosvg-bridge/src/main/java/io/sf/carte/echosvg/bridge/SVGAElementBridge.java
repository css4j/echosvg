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

import java.awt.Cursor;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGAElement;

import io.sf.carte.echosvg.anim.dom.SVGOMAElement;
import io.sf.carte.echosvg.anim.dom.SVGOMAnimationElement;
import io.sf.carte.echosvg.anim.dom.SVGOMDocument;
import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.events.AbstractEvent;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Bridge class for the &lt;a&gt; element.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAElementBridge extends SVGGElementBridge {

	protected AnchorListener al;
	protected CursorMouseOverListener bl;
	protected CursorMouseOutListener cl;

	/**
	 * Constructs a new bridge for the &lt;a&gt; element.
	 */
	public SVGAElementBridge() {
	}

	/**
	 * Returns 'a'.
	 */
	@Override
	public String getLocalName() {
		return SVG_A_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGAElementBridge();
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

		super.buildGraphicsNode(ctx, e, node);

		if (ctx.isInteractive()) {
			NodeEventTarget target = (NodeEventTarget) e;
			CursorHolder ch = new CursorHolder(CursorManager.DEFAULT_CURSOR);

			al = new AnchorListener(ctx.getUserAgent(), ch);
			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_CLICK, al, false, null);
			ctx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_CLICK, al, false);

			bl = new CursorMouseOverListener(ctx.getUserAgent(), ch);
			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOVER, bl, false, null);
			ctx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOVER, bl, false);

			cl = new CursorMouseOutListener(ctx.getUserAgent(), ch);
			target.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOUT, cl, false, null);
			ctx.storeEventListenerNS(target, XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOUT, cl, false);
		}
	}

	@Override
	public void dispose() {
		NodeEventTarget target = (NodeEventTarget) e;
		if (al != null) {
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_CLICK, al, false);
			al = null;
		}
		if (bl != null) {
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOVER, bl, false);
			bl = null;
		}
		if (cl != null) {
			target.removeEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, SVG_EVENT_MOUSEOUT, cl, false);
			cl = null;
		}
		super.dispose();
	}

	/**
	 * Returns true as the &lt;a&gt; element is a container.
	 */
	@Override
	public boolean isComposite() {
		return true;
	}

	public static class CursorHolder {
		Cursor cursor = null;

		public CursorHolder(Cursor c) {
			cursor = c;
		}

		public void holdCursor(Cursor c) {
			cursor = c;
		}

		public Cursor getCursor() {
			return cursor;
		}
	}

	/**
	 * To handle a click on an anchor.
	 */
	public static class AnchorListener implements EventListener {
		protected UserAgent userAgent;
		protected CursorHolder holder;

		public AnchorListener(UserAgent ua, CursorHolder ch) {
			userAgent = ua;
			holder = ch;
		}

		@Override
		public void handleEvent(Event evt) {
			if (!(evt instanceof AbstractEvent))
				return;
			final AbstractEvent ae = (AbstractEvent) evt;

			List<Runnable> l = ae.getDefaultActions();
			if (l != null) {
				for (Runnable o : l) {
					if (o instanceof AnchorDefaultActionable)
						return; // only one anchor in default list...
				}
			}

			SVGAElement elt = (SVGAElement) evt.getCurrentTarget();
			ae.addDefaultAction(new AnchorDefaultActionable(elt, userAgent, holder));
		}
	}

	public static class AnchorDefaultActionable implements Runnable {

		protected SVGOMAElement elt;
		protected UserAgent userAgent;
		protected CursorHolder holder;

		public AnchorDefaultActionable(SVGAElement e, UserAgent ua, CursorHolder ch) {
			elt = (SVGOMAElement) e;
			userAgent = ua;
			holder = ch;
		}

		@Override
		public void run() {
			userAgent.setSVGCursor(holder.getCursor());
			String href = elt.getHref().getAnimVal();
			ParsedURL purl = new ParsedURL(elt.getBaseURI(), href);
			SVGOMDocument doc = (SVGOMDocument) elt.getOwnerDocument();
			ParsedURL durl = doc.getParsedURL();
			if (purl.sameFile(durl)) {
				String frag = purl.getRef();
				if (frag != null && frag.length() != 0) {
					Element refElt = doc.getElementById(frag);
					if (refElt instanceof SVGOMAnimationElement) {
						SVGOMAnimationElement aelt = (SVGOMAnimationElement) refElt;
						float t = aelt.getHyperlinkBeginTime();
						if (Float.isNaN(t)) {
							aelt.beginElement();
						} else {
							doc.getRootElement().setCurrentTime(t);
						}
						return;
					}
				}
			}
			userAgent.openLink(elt);
		}
	}

	/**
	 * To handle a mouseover on an anchor and set the cursor.
	 */
	public static class CursorMouseOverListener implements EventListener {

		protected UserAgent userAgent;
		protected CursorHolder holder;

		public CursorMouseOverListener(UserAgent ua, CursorHolder ch) {
			userAgent = ua;
			holder = ch;
		}

		@Override
		public void handleEvent(Event evt) {
			if (!(evt instanceof AbstractEvent))
				return;
			final AbstractEvent ae = (AbstractEvent) evt;

			List<Runnable> l = ae.getDefaultActions();
			if (l != null) {
				for (Runnable o : l) {
					if (o instanceof MouseOverDefaultActionable)
						return; // only one anchor in default list...
				}
			}

			Element target = (Element) ae.getTarget();
			SVGAElement elt = (SVGAElement) ae.getCurrentTarget();

			ae.addDefaultAction(new MouseOverDefaultActionable(target, elt, userAgent, holder));
		}
	}

	public static class MouseOverDefaultActionable implements Runnable {

		protected Element target;
		protected SVGAElement elt;
		protected UserAgent userAgent;
		protected CursorHolder holder;

		public MouseOverDefaultActionable(Element t, SVGAElement e, UserAgent ua, CursorHolder ch) {
			target = t;
			elt = e;
			userAgent = ua;
			holder = ch;
		}

		@Override
		public void run() {
			// Only modify the cursor if the target's cursor property is
			// 'auto'. Note that we do not need to check the value of
			// anchor element as the target's cursor value is resulting
			// from the CSS cascade which has accounted for inheritance.
			// This means that our behavior is to set the cursor to a
			// hand cursor for any content on which the cursor property is
			// 'auto' inside an anchor element. If, for example, the
			// content was:
			// <a cusor="wait">
			// <g cursor="auto">
			// <rect />
			// </g>
			// </a>
			//
			// The cursor on the inside rect will be set to the hand cursor and
			// not the wait cursor
			//
			if (CSSUtilities.isAutoCursor(target)) {
				holder.holdCursor(CursorManager.DEFAULT_CURSOR);
				// The target's cursor value is 'auto': use the hand cursor
				userAgent.setSVGCursor(CursorManager.ANCHOR_CURSOR);
			}

			//
			// In all cases, display the href in the userAgent
			//
			if (elt != null) {
				String href = elt.getHref().getAnimVal();
				userAgent.displayMessage(href);
			}
		}
	}

	/**
	 * To handle a mouseout on an anchor and set the cursor.
	 */
	public static class CursorMouseOutListener implements EventListener {

		protected UserAgent userAgent;
		protected CursorHolder holder;

		public CursorMouseOutListener(UserAgent ua, CursorHolder ch) {
			userAgent = ua;
			holder = ch;
		}

		@Override
		public void handleEvent(Event evt) {
			if (!(evt instanceof AbstractEvent))
				return;
			final AbstractEvent ae = (AbstractEvent) evt;

			List<Runnable> l = ae.getDefaultActions();
			if (l != null) {
				for (Runnable o : l) {
					if (o instanceof MouseOutDefaultActionable)
						return; // only one anchor in default list...
				}
			}

			SVGAElement elt = (SVGAElement) evt.getCurrentTarget();
			ae.addDefaultAction(new MouseOutDefaultActionable(elt, userAgent, holder));
		}
	}

	public static class MouseOutDefaultActionable implements Runnable {

		protected SVGAElement elt;
		protected UserAgent userAgent;
		protected CursorHolder holder;

		public MouseOutDefaultActionable(SVGAElement e, UserAgent ua, CursorHolder ch) {
			elt = e;
			userAgent = ua;
			holder = ch;
		}

		@Override
		public void run() {
			// No need to set the cursor on out events: this is taken care of
			// by the BridgeContext(?)

			// Hide the href in the userAgent
			if (elt != null) {
				userAgent.displayMessage("");
			}
		}
	}
}
