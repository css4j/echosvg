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

import io.sf.carte.echosvg.anim.dom.SVGOMDocument;
import io.sf.carte.echosvg.anim.dom.XBLEventSupport;
import io.sf.carte.echosvg.anim.dom.XBLOMShadowTreeElement;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.BridgeUpdateHandler;
import io.sf.carte.echosvg.bridge.DocumentLoader;
import io.sf.carte.echosvg.bridge.ScriptingEnvironment;
import io.sf.carte.echosvg.bridge.URIResolver;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.AbstractNode;
import io.sf.carte.echosvg.dom.events.EventSupport;
import io.sf.carte.echosvg.dom.events.NodeEventTarget;
import io.sf.carte.echosvg.dom.xbl.NodeXBL;
import io.sf.carte.echosvg.dom.xbl.XBLManager;
import io.sf.carte.echosvg.script.Interpreter;
import io.sf.carte.echosvg.script.InterpreterPool;
import io.sf.carte.echosvg.util.SVGConstants;
import io.sf.carte.echosvg.constants.XMLConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;

/**
 * Bridge context for SVG 1.2 documents.  This is primarily for dispatching
 * XBL events to bridges and for handling resource documents.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id$
 */
public class SVG12BridgeContext extends BridgeContext {

    /**
     * The BindingListener for XBL binding events.
     */
    protected XBLBindingListener bindingListener;

    /**
     * The ContentSelectionChangedListener for xbl:content element events.
     */
    protected XBLContentListener contentListener;

    /**
     * The EventTarget that has the mouse capture.
     */
    protected EventTarget mouseCaptureTarget;

    /**
     * Whether the mouse capture event target will receive events
     * that do not intersect with its geometry.
     */
    protected boolean mouseCaptureSendAll;

    /**
     * Whether the mouse capture will be released on mouse up.
     */
    protected boolean mouseCaptureAutoRelease;

    /**
     * Constructs a new bridge context.
     * @param userAgent the user agent
     */
    public SVG12BridgeContext(UserAgent userAgent) {
        super(userAgent);
    }

    /**
     * Constructs a new bridge context.
     * @param userAgent the user agent
     * @param loader document loader
     */
    public SVG12BridgeContext(UserAgent userAgent,
                              DocumentLoader loader) {
        super(userAgent, loader);
    }

    /**
     * Constructs a new bridge context.
     * @param userAgent the user agent
     * @param interpreterPool the interpreter pool
     * @param documentLoader document loader
     */
    public SVG12BridgeContext(UserAgent userAgent,
                              InterpreterPool interpreterPool,
                              DocumentLoader documentLoader) {
        super(userAgent, interpreterPool, documentLoader);
    }

    /**
     * Returns a new URIResolver object.
     */
    public URIResolver createURIResolver(SVGDocument doc, DocumentLoader dl) {
        return new SVG12URIResolver(doc, dl);
    }

    /**
     * Adds the GVT listener for AWT event support.
     */
    public void addGVTListener(Document doc) {
        SVG12BridgeEventSupport.addGVTListener(this, doc);
    }

    /**
     * Disposes this BridgeContext.
     */
    public void dispose() {
        clearChildContexts();

        synchronized (eventListenerSet) {
            // remove all listeners added by Bridges
            for (Object anEventListenerSet : eventListenerSet) {
                EventListenerMememto m = (EventListenerMememto) anEventListenerSet;
                NodeEventTarget et = m.getTarget();
                EventListener el = m.getListener();
                boolean uc = m.getUseCapture();
                String t = m.getEventType();
                boolean in = m.getNamespaced();
                if (et == null || el == null || t == null) {
                    continue;
                }
                if (m instanceof ImplementationEventListenerMememto) {
                    String ns = m.getNamespaceURI();
                    Node nde = (Node) et;
                    AbstractNode n = (AbstractNode) nde.getOwnerDocument();
                    if (n != null) {
                        XBLEventSupport es;
                        es = (XBLEventSupport) n.initializeEventSupport();
                        es.removeImplementationEventListenerNS(ns, t, el, uc);
                    }
                } else if (in) {
                    String ns = m.getNamespaceURI();
                    et.removeEventListenerNS(ns, t, el, uc);
                } else {
                    et.removeEventListener(t, el, uc);
                }
            }
        }

        if (document != null) {
            removeDOMListeners();
            removeBindingListener();
        }

        if (animationEngine != null) {
            animationEngine.dispose();
            animationEngine = null;
        }

        for (Object o : interpreterMap.values()) {
            Interpreter interpreter = (Interpreter) o;
            if (interpreter != null)
                interpreter.dispose();
        }
        interpreterMap.clear();

        if (focusManager != null) {
            focusManager.dispose();
        }
    }

    /**
     * Adds a BindingListener to the XBLManager for the document, so that
     * XBL binding events can be passed on to the BridgeUpdateHandlers.
     */
    public void addBindingListener() {
        AbstractDocument doc = (AbstractDocument) document;
        DefaultXBLManager xm = (DefaultXBLManager) doc.getXBLManager();
        if (xm != null) {
            bindingListener = new XBLBindingListener();
            xm.addBindingListener(bindingListener);
            contentListener = new XBLContentListener();
            xm.addContentSelectionChangedListener(contentListener);
        }
    }

    /**
     * Removes the BindingListener from the XBLManager.
     */
    public void removeBindingListener() {
        AbstractDocument doc = (AbstractDocument) document;
        XBLManager xm = doc.getXBLManager();
        if (xm instanceof DefaultXBLManager) {
            DefaultXBLManager dxm = (DefaultXBLManager) xm;
            dxm.removeBindingListener(bindingListener);
            dxm.removeContentSelectionChangedListener(contentListener);
        }
    }

    /**
     * Adds EventListeners to the DOM and CSSEngineListener to the
     * CSSEngine to handle any modifications on the DOM tree or style
     * properties and update the GVT tree in response.  This overriden
     * method adds implementation event listeners, so that mutations in
     * shadow trees can be caught.
     */
    public void addDOMListeners() {
        SVGOMDocument doc = (SVGOMDocument)document;
        XBLEventSupport evtSupport
            = (XBLEventSupport) doc.initializeEventSupport();

        domAttrModifiedEventListener
            = new EventListenerWrapper(new DOMAttrModifiedEventListener());
        evtSupport.addImplementationEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMAttrModified",
             domAttrModifiedEventListener, true);

        domNodeInsertedEventListener
            = new EventListenerWrapper(new DOMNodeInsertedEventListener());
        evtSupport.addImplementationEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMNodeInserted",
             domNodeInsertedEventListener, true);

        domNodeRemovedEventListener
            = new EventListenerWrapper(new DOMNodeRemovedEventListener());
        evtSupport.addImplementationEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMNodeRemoved",
             domNodeRemovedEventListener, true);

        domCharacterDataModifiedEventListener =
                new EventListenerWrapper(new DOMCharacterDataModifiedEventListener());
        evtSupport.addImplementationEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMCharacterDataModified",
             domCharacterDataModifiedEventListener, true);

        animatedAttributeListener = new AnimatedAttrListener();
        doc.addAnimatedAttributeListener(animatedAttributeListener);
        
        focusManager = new SVG12FocusManager(document);

        CSSEngine cssEngine = doc.getCSSEngine();
        cssPropertiesChangedListener = new CSSPropertiesChangedListener();
        cssEngine.addCSSEngineListener(cssPropertiesChangedListener);
    }

    /**
     * Adds EventListeners to the input document to handle the cursor 
     * property.
     * This is not done in the addDOMListeners method because 
     * addDOMListeners is only used for dynamic content whereas 
     * cursor support is provided for all content.
     * Also note that it is very important that the listeners be
     * registered for the capture phase as the 'default' behavior
     * for cursors is handled by the BridgeContext during the 
     * capture phase and the 'custom' behavior (handling of 'auto'
     * on anchors, for example), is handled during the bubbling phase.
     */
    public void addUIEventListeners(Document doc) {
        EventTarget evtTarget = (EventTarget)doc.getDocumentElement();
        AbstractNode n = (AbstractNode) evtTarget;
        XBLEventSupport evtSupport
            = (XBLEventSupport) n.initializeEventSupport();

        EventListener domMouseOverListener
            = new EventListenerWrapper(new DOMMouseOverEventListener());
        evtSupport.addImplementationEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             SVGConstants.SVG_EVENT_MOUSEOVER,
             domMouseOverListener, true);
        storeImplementationEventListenerNS
            (evtTarget,
             XMLConstants.XML_EVENTS_NAMESPACE_URI,
             SVGConstants.SVG_EVENT_MOUSEOVER,
             domMouseOverListener, true);

        EventListener domMouseOutListener
            = new EventListenerWrapper(new DOMMouseOutEventListener());
        evtSupport.addImplementationEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             SVGConstants.SVG_EVENT_MOUSEOUT,
             domMouseOutListener, true);
        storeImplementationEventListenerNS
            (evtTarget,
             XMLConstants.XML_EVENTS_NAMESPACE_URI,
             SVGConstants.SVG_EVENT_MOUSEOUT,
             domMouseOutListener, true);
    }

    public void removeUIEventListeners(Document doc) {
        EventTarget evtTarget = (EventTarget)doc.getDocumentElement();
        AbstractNode n = (AbstractNode) evtTarget;
        XBLEventSupport es = (XBLEventSupport) n.initializeEventSupport();

        synchronized (eventListenerSet) {
            for (Object anEventListenerSet : eventListenerSet) {
                EventListenerMememto elm = (EventListenerMememto) anEventListenerSet;
                NodeEventTarget et = elm.getTarget();
                if (et == evtTarget) {
                    EventListener el = elm.getListener();
                    boolean uc = elm.getUseCapture();
                    String t = elm.getEventType();
                    boolean in = elm.getNamespaced();
                    if (et == null || el == null || t == null) {
                        continue;
                    }
                    if (elm instanceof ImplementationEventListenerMememto) {
                        String ns = elm.getNamespaceURI();
                        es.removeImplementationEventListenerNS(ns, t, el, uc);
                    } else if (in) {
                        String ns = elm.getNamespaceURI();
                        et.removeEventListenerNS(ns, t, el, uc);
                    } else {
                        et.removeEventListener(t, el, uc);
                    }
                }
            }
        }
    }

    /**
     * Removes event listeners from the DOM and CSS engine.
     */
    protected void removeDOMListeners() {
        SVGOMDocument doc = (SVGOMDocument)document;

        doc.removeEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMAttrModified",
             domAttrModifiedEventListener, true);
        doc.removeEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMNodeInserted",
             domNodeInsertedEventListener, true);
        doc.removeEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMNodeRemoved",
             domNodeRemovedEventListener, true);
        doc.removeEventListenerNS
            (XMLConstants.XML_EVENTS_NAMESPACE_URI,
             "DOMCharacterDataModified",
             domCharacterDataModifiedEventListener, true);
        
        doc.removeAnimatedAttributeListener(animatedAttributeListener);

        CSSEngine cssEngine = doc.getCSSEngine();
        if (cssEngine != null) {
            cssEngine.removeCSSEngineListener
                (cssPropertiesChangedListener);
            cssEngine.dispose();
            doc.setCSSEngine(null);
        }
    }

    /**
     * Adds to the eventListenerSet the specified implementation event
     * listener registration.
     */
    protected void storeImplementationEventListenerNS(EventTarget t,
                                                      String ns,
                                                      String s,
                                                      EventListener l,
                                                      boolean b) {
        synchronized (eventListenerSet) {
            ImplementationEventListenerMememto m
                = new ImplementationEventListenerMememto(t, ns, s, l, b, this);
            eventListenerSet.add(m);
        }
    }

    public BridgeContext createSubBridgeContext(SVGOMDocument newDoc) {
        CSSEngine eng = newDoc.getCSSEngine();
        if (eng != null) {
            return (BridgeContext)newDoc.getCSSEngine().getCSSContext();
        }

        BridgeContext subCtx = super.createSubBridgeContext(newDoc);
        if (isDynamic() && subCtx.isDynamic()) {
            setUpdateManager(subCtx, updateManager);
            if (updateManager != null) {
                ScriptingEnvironment se;
                if (newDoc.isSVG12()) {
                    se = new SVG12ScriptingEnvironment(subCtx);
                } else {
                    se = new ScriptingEnvironment(subCtx);
                }
                se.loadScripts();
                se.dispatchSVGLoadEvent();
                if (newDoc.isSVG12()) {
                    DefaultXBLManager xm =
                        new DefaultXBLManager(newDoc, subCtx);
                    setXBLManager(subCtx, xm);
                    newDoc.setXBLManager(xm);
                    xm.startProcessing();
                }
            }
        }
        return subCtx;
    }

    /**
     * Starts mouse capture.
     */
    public void startMouseCapture(EventTarget target, boolean sendAll,
                                  boolean autoRelease) {
        mouseCaptureTarget = target;
        mouseCaptureSendAll = sendAll;
        mouseCaptureAutoRelease = autoRelease;
    }

    /**
     * Stops mouse capture.
     */
    public void stopMouseCapture() {
        mouseCaptureTarget = null;
    }

    /**
     * A class used to store an implementation EventListener added to the DOM.
     */
    protected static class ImplementationEventListenerMememto
            extends EventListenerMememto {

        /**
         * Creates a new ImplementationEventListenerMememto.
         */
        public ImplementationEventListenerMememto(EventTarget t,
                                                  String s,
                                                  EventListener l,
                                                  boolean b,
                                                  BridgeContext c) {
            super(t, s, l, b, c);
        }

        /**
         * Creates a new ImplementationEventListenerMememto.
         */
        public ImplementationEventListenerMememto(EventTarget t,
                                                  String n,
                                                  String s,
                                                  EventListener l,
                                                  boolean b,
                                                  BridgeContext c) {
            super(t, n, s, l, b, c);
        }
    }

    /**
     * Wrapper for DOM event listeners so that they will see only
     * original events (i.e., not retargetted).
     */
    protected static class EventListenerWrapper implements EventListener {

        /**
         * The wrapped listener.
         */
        protected EventListener listener;

        /**
         * Creates a new EventListenerWrapper.
         */
        public EventListenerWrapper(EventListener l) {
            listener = l;
        }

        /**
         * Handles the event.
         */
        public void handleEvent(Event evt) {
            listener.handleEvent(EventSupport.getUltimateOriginalEvent(evt));
        }

        /**
         * String representation of this listener wrapper.
         */
        public String toString() {
            return super.toString() + " [wrapping " + listener.toString() + "]";
        }
    }

    /**
     * The BindingListener.
     */
    protected class XBLBindingListener implements BindingListener {
        
        /**
         * Invoked when the specified bindable element's binding has changed.
         */
        public void bindingChanged(Element bindableElement,
                                   Element shadowTree) {
            BridgeUpdateHandler h = getBridgeUpdateHandler(bindableElement);
            if (h instanceof SVG12BridgeUpdateHandler) {
                SVG12BridgeUpdateHandler h12 = (SVG12BridgeUpdateHandler) h;
                try {
                    h12.handleBindingEvent(bindableElement, shadowTree);
                } catch (Exception e) {
                    userAgent.displayError(e);
                }
            }
        }
    }

    /**
     * The ContentSelectionChangedListener.
     */
    protected class XBLContentListener
            implements ContentSelectionChangedListener {
        
        /**
         * Invoked after an xbl:content element has updated its selected
         * nodes list.
         * @param csce the ContentSelectionChangedEvent object
         */
        public void contentSelectionChanged(ContentSelectionChangedEvent csce) {
            Element e = (Element) csce.getContentElement().getParentNode();
            if (e instanceof XBLOMShadowTreeElement) {
                e = ((NodeXBL) e).getXblBoundElement();
            }
            BridgeUpdateHandler h = getBridgeUpdateHandler(e);
            if (h instanceof SVG12BridgeUpdateHandler) {
                SVG12BridgeUpdateHandler h12 = (SVG12BridgeUpdateHandler) h;
                try {
                    h12.handleContentSelectionChangedEvent(csce);
                } catch (Exception ex) {
                    userAgent.displayError(ex);
                }
            }
        }
    }
}
