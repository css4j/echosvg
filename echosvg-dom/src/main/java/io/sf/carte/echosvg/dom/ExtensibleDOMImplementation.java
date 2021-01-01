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
package io.sf.carte.echosvg.dom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.css.DOMImplementationCSS;
import org.w3c.dom.css.ViewCSS;

import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.util.DoublyIndexedTable;
import io.sf.carte.echosvg.util.Service;
import io.sf.carte.echosvg.xml.XMLUtilities;

/**
 * This class implements the {@link org.w3c.dom.DOMImplementation} interface.
 * It allows the user to extend the set of elements supported by a
 * Document, directly or through the Service API (see
 * {@link io.sf.carte.echosvg.util.Service}).
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class ExtensibleDOMImplementation
    extends AbstractDOMImplementation
    implements DOMImplementationCSS,
               StyleSheetFactory {

    private static final long serialVersionUID = 1L;

    /**
     * The custom elements factories.
     */
    protected DoublyIndexedTable<String,String> customFactories;

    /**
     * The custom value managers.
     */
    protected List<ValueManager> customValueManagers;

    /**
     * The custom shorthand value managers.
     */
    protected List<ShorthandManager> customShorthandManagers;

    /**
     * Creates a new DOMImplementation.
     */
    public ExtensibleDOMImplementation() {

        for (DomExtension de : getDomExtensions()) {
            de.registerTags(this);
        }
    }

    /**
     * Allows the user to register a new element factory.
     */
    public void registerCustomElementFactory(String namespaceURI,
                                             String localName,
                                             ElementFactory factory) {
        if (customFactories == null) {
            customFactories = new DoublyIndexedTable<>();
        }
        customFactories.put(namespaceURI, localName, factory);
    }

    /**
     * Allows the user to register a new CSS value manager.
     */
    public void registerCustomCSSValueManager(ValueManager vm) {
        if (customValueManagers == null) {
            customValueManagers = new LinkedList<>();
        }
        customValueManagers.add(vm);
    }

    /**
     * Allows the user to register a new shorthand CSS value manager.
     */
    public void registerCustomCSSShorthandManager(ShorthandManager sm) {
        if (customShorthandManagers == null) {
            customShorthandManagers = new LinkedList<>();
        }
        customShorthandManagers.add(sm);
    }

    /**
     * Creates new CSSEngine and attach it to the document.
     */
    public CSSEngine createCSSEngine(AbstractStylableDocument doc,
                                     CSSContext ctx) {
        Parser p = new CSSParser();

        ValueManager[] vms;
        if (customValueManagers == null) {
            vms = new ValueManager[0];
        } else {
            vms = new ValueManager[customValueManagers.size()];
            Iterator<ValueManager> it = customValueManagers.iterator();
            int i = 0;
            while (it.hasNext()) {
                vms[i++] = it.next();
            }
        }

        ShorthandManager[] sms;
        if (customShorthandManagers == null) {
            sms = new ShorthandManager[0];
        } else {
            sms = new ShorthandManager[customShorthandManagers.size()];
            Iterator<ShorthandManager> it = customShorthandManagers.iterator();
            int i = 0;
            while (it.hasNext()) {
                sms[i++] = it.next();
            }
        }

        CSSEngine result = createCSSEngine(doc, ctx, p, vms, sms);
        doc.setCSSEngine(result);
        return result;
    }

    public abstract CSSEngine createCSSEngine(AbstractStylableDocument doc,
                                              CSSContext               ctx,
                                              Parser                   p,
                                              ValueManager     []      vms,
                                              ShorthandManager []      sms);

    /**
     * Creates a ViewCSS.
     */
    public abstract ViewCSS createViewCSS(AbstractStylableDocument doc);

    /**
     * Implements the behavior of Document.createElementNS() for this
     * DOM implementation.
     */
    public Element createElementNS(AbstractDocument document,
                                   String           namespaceURI,
                                   String           qualifiedName) {
        if (namespaceURI != null && namespaceURI.length() == 0) {
            namespaceURI = null;
        }
        if (namespaceURI == null)
            return new GenericElement(qualifiedName.intern(), document);

        if (customFactories != null) {
            String name = DOMUtilities.getLocalName(qualifiedName);
            ElementFactory cef;
            cef = (ElementFactory)customFactories.get(namespaceURI, name);
            if (cef != null) {
                return cef.create(DOMUtilities.getPrefix(qualifiedName),
                                  document);
            }
        }
        return new GenericElementNS(namespaceURI.intern(),
                                    qualifiedName.intern(),
                                    document);
    }

    /**
     * <b>DOM</b>: Implements DOMImplementation#createDocumentType(String,String,String).
     */
    @Override
    public DocumentType createDocumentType(String qualifiedName,
                                           String publicId,
                                           String systemId) {

        if (qualifiedName == null) {
            qualifiedName = "";
        }
        int test = XMLUtilities.testXMLQName(qualifiedName);
        if ((test & XMLUtilities.IS_XML_10_NAME) == 0) {
            throw new DOMException
                (DOMException.INVALID_CHARACTER_ERR,
                 formatMessage("xml.name",
                               new Object[] { qualifiedName }));
        }
        if ((test & XMLUtilities.IS_XML_10_QNAME) == 0) {
            throw new DOMException
                (DOMException.INVALID_CHARACTER_ERR,
                 formatMessage("invalid.qname",
                               new Object[] { qualifiedName }));
        }
        return new GenericDocumentType(qualifiedName, publicId, systemId);
    }

    // The element factories /////////////////////////////////////////////////

    /**
     * This interface represents a factory for elements.
     */
    public interface ElementFactory {
        /**
         * Creates an instance of the associated element type.
         */
        Element create(String prefix, Document doc);
    }

    // Service /////////////////////////////////////////////////////////

    protected static List<DomExtension> extensions = null;

    protected static synchronized List<DomExtension> getDomExtensions() {
        if (extensions != null)
            return extensions;

        extensions = new LinkedList<>();

        Iterator<Object> iter = Service.providers(DomExtension.class);

        while (iter.hasNext()) {
            DomExtension de = (DomExtension)iter.next();
            float priority  = de.getPriority();
            ListIterator<DomExtension> li = extensions.listIterator();
            for (;;) {
                if (!li.hasNext()) {
                    li.add(de);
                    break;
                }
                DomExtension lde = li.next();
                if (lde.getPriority() > priority) {
                    li.previous();
                    li.add(de);
                    break;
                }
            }
        }

        return extensions;
    }
}
