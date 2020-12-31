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

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import io.sf.carte.echosvg.xml.XMLUtilities;

/**
 * This class implements the {@link org.w3c.dom.DOMImplementation}.
 
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class GenericDOMImplementation extends AbstractDOMImplementation {

    /**
     * The default instance of this class.
     */
    protected static final DOMImplementation DOM_IMPLEMENTATION =
        new GenericDOMImplementation();

    /**
     * Creates a new GenericDOMImplementation object.
     */
    public GenericDOMImplementation() {
    }

    /**
     * Returns the default instance of this class.
     */
    public static DOMImplementation getDOMImplementation() {
        return DOM_IMPLEMENTATION;
    }

    // DOMImplementation //////////////////////////////////////////////////////

    /**
     * <b>DOM</b>: Implements {@link
     * DOMImplementation#createDocument(String,String,DocumentType)}.
     */
    public Document createDocument(String namespaceURI,
                                   String qualifiedName,
                                   DocumentType doctype) throws DOMException {
        Document result = new GenericDocument(doctype, this);
        result.appendChild(result.createElementNS(namespaceURI,
                                                  qualifiedName));
        return result;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * DOMImplementation#createDocumentType(String,String,String)}.
     */
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
}
