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
import org.w3c.dom.Node;

/**
 * This class implements the {@link org.w3c.dom.Element} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GenericElement extends AbstractElement {
    private static final long serialVersionUID = 1L;

    /**
     * The node name.
     */
    protected String nodeName;

    /**
     * Is this element immutable?
     */
    protected boolean readonly;

    /**
     * Creates a new Element object.
     */
    protected GenericElement() {
    }

    /**
     * Creates a new Element object.
     * @param name  The element name for validation purposes.
     * @param owner The owner document.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: if name contains invalid characters,
     */
    public GenericElement(String name, AbstractDocument owner)
        throws DOMException {
        super(name, owner);
        nodeName = name;
    }

    /**
     * Sets the name of this node.
     */
    @Override
    public void setNodeName(String v) {
        nodeName = v;
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
     * @return {@link #nodeName}
     */
    @Override
    public String getNodeName() {
        return nodeName;
    }

    // ExtendedNode //////////////////////////////////////////////////
    
    /**
     * Tests whether this node is readonly.
     */
    @Override
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Sets this node readonly attribute.
     */
    @Override
    public void setReadonly(boolean v) {
        readonly = v;
    }

    /**
     * Exports this node to the given document.
     */
    @Override
    protected Node export(Node n, AbstractDocument d) {
        super.export(n, d);
        GenericElement ge = (GenericElement)n;
        ge.nodeName = nodeName;
        return n;
    }

    /**
     * Deeply exports this node to the given document.
     */
    @Override
    protected Node deepExport(Node n, AbstractDocument d) {
        super.deepExport(n, d);
        GenericElement ge = (GenericElement)n;
        ge.nodeName = nodeName;
        return n;
    }

    /**
     * Copy the fields of the current node into the given node.
     * @param n a node of the type of this.
     */
    @Override
    protected Node copyInto(Node n) {
        GenericElement ge = (GenericElement)super.copyInto(n);
        ge.nodeName = nodeName;
        return n;
    }

    /**
     * Deeply copy the fields of the current node into the given node.
     * @param n a node of the type of this.
     */
    @Override
    protected Node deepCopyInto(Node n) {
        GenericElement ge = (GenericElement)super.deepCopyInto(n);
        ge.nodeName = nodeName;
        return n;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new GenericElement();
    }
}
