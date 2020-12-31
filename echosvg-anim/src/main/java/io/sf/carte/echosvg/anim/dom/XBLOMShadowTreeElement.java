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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.svg.IdContainer;
import io.sf.carte.echosvg.dom.xbl.XBLShadowTreeElement;

/**
 * This class implements the xbl:shadowTree element.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id$
 */
public class XBLOMShadowTreeElement
        extends XBLOMElement
        implements XBLShadowTreeElement, IdContainer {

    /**
     * Creates a new XBLOMShadowTreeElement.
     */
    protected XBLOMShadowTreeElement() {
    }

    /**
     * Creates a new XBLOMShadowTreeElement.
     * @param prefix The namespace prefix.
     * @param owner  The owner document.
     */
    public XBLOMShadowTreeElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    @Override
    public String getLocalName() {
        return XBL_SHADOW_TREE_TAG;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new XBLOMShadowTreeElement();
    }

    // XBLShadowTreeElement //////////////////////////////////////////////////

    /**
     * Returns the Element that has an ID attribute with the given value.
     */
    @Override
    public Element getElementById(String elementId) {
        return getElementById(elementId, this);
    }

    protected Element getElementById(String elementId, Node n) {
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) n;
            if (e.getAttributeNS(null, "id").equals(elementId)) {
                return (Element) n;
            }
        }
        for (Node m = n.getFirstChild(); m != null; m = m.getNextSibling()) {
            Element result = getElementById(elementId, m);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    // CSSNavigableNode //////////////////////////////////////////////////////

    /**
     * Returns the parent of the imported element, from the CSS
     * point of view.
     */
    @Override
    public Node getCSSParentNode() {
        return ownerDocument.getXBLManager().getXblBoundElement(this);
    }
}
