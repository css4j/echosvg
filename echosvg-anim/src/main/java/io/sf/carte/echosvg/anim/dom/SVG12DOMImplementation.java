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
package io.sf.carte.echosvg.anim.dom;

import java.net.URL;
import java.util.HashMap;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;

import io.sf.carte.doc.style.css.nsac.InputSource;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.SVG12CSSEngine;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.AbstractNode;
import io.sf.carte.echosvg.dom.AbstractStylableDocument;
import io.sf.carte.echosvg.dom.GenericElement;
import io.sf.carte.echosvg.dom.events.DocumentEventSupport;
import io.sf.carte.echosvg.dom.events.EventSupport;
import io.sf.carte.echosvg.dom.svg12.SVGOMWheelEvent;
import io.sf.carte.echosvg.dom.svg12.XBLOMShadowTreeEvent;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVG12Constants;
import io.sf.carte.echosvg.util.SVGConstants;
import io.sf.carte.echosvg.util.XBLConstants;

/**
 * This class implements the {@link DOMImplementation} interface.
 * It provides support the SVG 1.2 documents.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVG12DOMImplementation
    extends    SVGDOMImplementation {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new SVGDOMImplementation object.
     */
    public SVG12DOMImplementation() {
        factories = svg12Factories;
        registerFeature("CSS",            "2.0");
        registerFeature("StyleSheets",    "2.0");
        registerFeature("SVG",            new String[] {"1.0", "1.1", "1.2"});
        registerFeature("SVGEvents",      new String[] {"1.0", "1.1", "1.2"});
    }

    @Override
    public CSSEngine createCSSEngine(AbstractStylableDocument doc,
                                     CSSContext               ctx,
                                     Parser              p,
                                     ValueManager     [] vms,
                                     ShorthandManager [] sms) {
        ParsedURL durl = ((SVGOMDocument)doc).getParsedURL();
        CSSEngine result = new SVG12CSSEngine(doc, durl, p, vms, sms, ctx);

        URL url = getClass().getResource("resources/UserAgentStyleSheet.css");
        if (url != null) {
            ParsedURL purl = new ParsedURL(url);
            InputSource is = new InputSource(purl.toString());
            result.setUserAgentStyleSheet
                (result.parseStyleSheet(is, purl, "all"));
        }

        return result;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * DOMImplementation#createDocument(String,String,DocumentType)}.
     */
    @Override
    public Document createDocument(String namespaceURI,
                                   String qualifiedName,
                                   DocumentType doctype)
        throws DOMException {
        SVGOMDocument result = new SVG12OMDocument(doctype, this);
        result.setIsSVG12(true);
        // BUG 32108: return empty document if qualifiedName is null.
        if (qualifiedName != null)
            result.appendChild(result.createElementNS(namespaceURI,
                                                      qualifiedName));
        return result;
    }

    /**
     * Implements the behavior of Document.createElementNS() for this
     * DOM implementation.
     */
    @Override
    public Element createElementNS(AbstractDocument document,
                                   String           namespaceURI,
                                   String           qualifiedName) {
        if (namespaceURI == null)
            return new GenericElement(qualifiedName.intern(), document);

        String name = DOMUtilities.getLocalName(qualifiedName);
        String prefix = DOMUtilities.getPrefix(qualifiedName);
        if (SVGConstants.SVG_NAMESPACE_URI.equals(namespaceURI)) {
            ElementFactory ef = factories.get(name);
            if (ef != null) {
                return ef.create(prefix, document);
            }
        } else if (XBLConstants.XBL_NAMESPACE_URI.equals(namespaceURI)) {
            ElementFactory ef = xblFactories.get(name);
            if (ef != null) {
                return ef.create(prefix, document);
            }
        }

        if (customFactories != null) {
            ElementFactory cef;
            cef = (ElementFactory)customFactories.get(namespaceURI, name);
            if (cef != null) {
                return cef.create(prefix, document);
            }
        }

        return new BindableElement(prefix, document, namespaceURI, name);
    }

    /**
     * Creates an DocumentEventSupport object suitable for use with
     * this implementation.
     */
    @Override
    public DocumentEventSupport createDocumentEventSupport() {
        DocumentEventSupport result =  super.createDocumentEventSupport();
        result.registerEventFactory("WheelEvent",
                                    new DocumentEventSupport.EventFactory() {
                                        @Override
                                        public Event createEvent() {
                                            return new SVGOMWheelEvent();
                                        }
                                    });
        result.registerEventFactory("ShadowTreeEvent",
                                    new DocumentEventSupport.EventFactory() {
                                        @Override
                                        public Event createEvent() {
                                            return new XBLOMShadowTreeEvent();
                                        }
                                    });
        return result;
    }

    /**
     * Creates an EventSupport object for the given node.
     */
    @Override
    public EventSupport createEventSupport(AbstractNode n) {
        return new XBLEventSupport(n);
    }

    // The element factories /////////////////////////////////////////////////

    /**
     * The SVG element factories.
     */
    protected static HashMap<String, ElementFactory> svg12Factories = new HashMap<String, ElementFactory>(svg11Factories);

    static {
        svg12Factories.put(SVG12Constants.SVG_FLOW_DIV_TAG,
                           new FlowDivElementFactory());

        svg12Factories.put(SVG12Constants.SVG_FLOW_LINE_TAG,
                           new FlowLineElementFactory());

        svg12Factories.put(SVG12Constants.SVG_FLOW_PARA_TAG,
                           new FlowParaElementFactory());

        svg12Factories.put(SVG12Constants.SVG_FLOW_REGION_BREAK_TAG,
                           new FlowRegionBreakElementFactory());

        svg12Factories.put(SVG12Constants.SVG_FLOW_REGION_TAG,
                           new FlowRegionElementFactory());

        svg12Factories.put(SVG12Constants.SVG_FLOW_REGION_EXCLUDE_TAG,
                           new FlowRegionExcludeElementFactory());

        svg12Factories.put(SVG12Constants.SVG_FLOW_ROOT_TAG,
                           new FlowRootElementFactory());

        svg12Factories.put(SVG12Constants.SVG_FLOW_SPAN_TAG,
                           new FlowSpanElementFactory());

        svg12Factories.put(SVG12Constants.SVG_HANDLER_TAG,
                           new HandlerElementFactory());

        svg12Factories.put(SVG12Constants.SVG_MULTI_IMAGE_TAG,
                           new MultiImageElementFactory());

        svg12Factories.put(SVG12Constants.SVG_SOLID_COLOR_TAG,
                           new SolidColorElementFactory());

        svg12Factories.put(SVG12Constants.SVG_SUB_IMAGE_TAG,
                           new SubImageElementFactory());

        svg12Factories.put(SVG12Constants.SVG_SUB_IMAGE_REF_TAG,
                           new SubImageRefElementFactory());

    }

    /**
     * To create a 'flowDiv' element.
     */
    protected static class FlowDivElementFactory
        implements ElementFactory {
        public FlowDivElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowDivElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'flowLine' element.
     */
    protected static class FlowLineElementFactory
        implements ElementFactory {
        public FlowLineElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowLineElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'flowPara' element.
     */
    protected static class FlowParaElementFactory
        implements ElementFactory {
        public FlowParaElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowParaElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'flowRegionBreak' element.
     */
    protected static class FlowRegionBreakElementFactory
        implements ElementFactory {
        public FlowRegionBreakElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowRegionBreakElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'flowRegion' element.
     */
    protected static class FlowRegionElementFactory
        implements ElementFactory {
        public FlowRegionElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowRegionElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'flowRegion' element.
     */
    protected static class FlowRegionExcludeElementFactory
        implements ElementFactory {
        public FlowRegionExcludeElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowRegionExcludeElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'flowRoot' element.
     */
    protected static class FlowRootElementFactory
        implements ElementFactory {
        public FlowRootElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowRootElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'flowSpan' element.
     */
    protected static class FlowSpanElementFactory
        implements ElementFactory {
        public FlowSpanElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMFlowSpanElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'handler' element.
     */
    protected static class HandlerElementFactory
        implements ElementFactory {
        public HandlerElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMHandlerElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'multiImage' element.
     */
    protected static class MultiImageElementFactory
        implements ElementFactory {
        public MultiImageElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMMultiImageElement
                (prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'solidColor' element.
     */
    protected static class SolidColorElementFactory
        implements ElementFactory {
        public SolidColorElementFactory() {
        }
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMSolidColorElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'subImage' element.
     */
    protected static class SubImageElementFactory
        implements ElementFactory {
        public SubImageElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMSubImageElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'SubImageRef' element.
     */
    protected static class SubImageRefElementFactory
        implements ElementFactory {
        public SubImageRefElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new SVGOMSubImageRefElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * The XBL element factories.
     */
    protected static HashMap<String, ElementFactory> xblFactories = new HashMap<String, ElementFactory>();

    static {
        xblFactories.put(XBLConstants.XBL_XBL_TAG,
                         new XBLXBLElementFactory());

        xblFactories.put(XBLConstants.XBL_DEFINITION_TAG,
                         new XBLDefinitionElementFactory());

        xblFactories.put(XBLConstants.XBL_TEMPLATE_TAG,
                         new XBLTemplateElementFactory());

        xblFactories.put(XBLConstants.XBL_CONTENT_TAG,
                         new XBLContentElementFactory());

        xblFactories.put(XBLConstants.XBL_HANDLER_GROUP_TAG,
                         new XBLHandlerGroupElementFactory());

        xblFactories.put(XBLConstants.XBL_IMPORT_TAG,
                         new XBLImportElementFactory());

        xblFactories.put(XBLConstants.XBL_SHADOW_TREE_TAG,
                         new XBLShadowTreeElementFactory());
    }

    /**
     * To create a 'xbl:xbl' element.
     */
    protected static class XBLXBLElementFactory implements ElementFactory {
        public XBLXBLElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new XBLOMXBLElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'xbl:definition' element.
     */
    protected static class XBLDefinitionElementFactory
            implements ElementFactory {
        public XBLDefinitionElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new XBLOMDefinitionElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'xbl:template' element.
     */
    protected static class XBLTemplateElementFactory
            implements ElementFactory {
        public XBLTemplateElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new XBLOMTemplateElement(prefix, (AbstractDocument) doc);
        }
    }

    /**
     * To create a 'xbl:content' element.
     */
    protected static class XBLContentElementFactory
            implements ElementFactory {
        public XBLContentElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new XBLOMContentElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'xbl:handlerGroup' element.
     */
    protected static class XBLHandlerGroupElementFactory
            implements ElementFactory {
        public XBLHandlerGroupElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new XBLOMHandlerGroupElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'xbl:import' element.
     */
    protected static class XBLImportElementFactory
            implements ElementFactory {
        public XBLImportElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new XBLOMImportElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * To create a 'xbl:shadowTree' element.
     */
    protected static class XBLShadowTreeElementFactory
            implements ElementFactory {
        public XBLShadowTreeElementFactory() {}
        /**
         * Creates an instance of the associated element type.
         */
        @Override
        public Element create(String prefix, Document doc) {
            return new XBLOMShadowTreeElement(prefix, (AbstractDocument)doc);
        }
    }

    /**
     * The default instance of this class.
     */
    protected static final DOMImplementation DOM_IMPLEMENTATION =
        new SVG12DOMImplementation();

    /**
     * Returns the default instance of this class.
     */
    public static DOMImplementation getDOMImplementation() {
        return DOM_IMPLEMENTATION;
    }
}
