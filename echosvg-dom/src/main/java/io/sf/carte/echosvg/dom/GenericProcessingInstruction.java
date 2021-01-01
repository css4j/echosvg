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

import org.w3c.dom.Node;

/**
 * This class implements the {@link
 * org.w3c.dom.ProcessingInstruction} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public class GenericProcessingInstruction
    extends AbstractProcessingInstruction {
    private static final long serialVersionUID = 1L;

    /**
     * The target.
     */
    protected String target;

    /**
     * Is this node immutable?
     */
    protected boolean readonly;

    /**
     * Creates a new ProcessingInstruction object.
     */
    protected GenericProcessingInstruction() {
    }

    /**
     * Creates a new ProcessingInstruction object.
     */
    public GenericProcessingInstruction(String           target,
                                        String           data,
                                        AbstractDocument owner) {
        ownerDocument = owner;
        setTarget(target);
        setData(data);
    }

    /**
     * Sets the node name.
     */
    @Override
    public void setNodeName(String v) {
        setTarget(v);
    }

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
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.ProcessingInstruction#getTarget()}.
     * @return {@link #target}.
     */
    @Override
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target value.
     */
    public void setTarget(String v) {
        target = v;
    }

    /**
     * Exports this node to the given document.
     */
    @Override
    protected Node export(Node n, AbstractDocument d) {
        GenericProcessingInstruction p;
        p = (GenericProcessingInstruction)super.export(n, d);
        p.setTarget(getTarget());
        return p;
    }

    /**
     * Deeply exports this node to the given document.
     */
    @Override
    protected Node deepExport(Node n, AbstractDocument d) {
        GenericProcessingInstruction p;
        p = (GenericProcessingInstruction)super.deepExport(n, d);
        p.setTarget(getTarget());
        return p;
    }

    /**
     * Copy the fields of the current node into the given node.
     * @param n a node of the type of this.
     */
    @Override
    protected Node copyInto(Node n) {
        GenericProcessingInstruction p;
        p = (GenericProcessingInstruction)super.copyInto(n);
        p.setTarget(getTarget());
        return p;
    }

    /**
     * Deeply copy the fields of the current node into the given node.
     * @param n a node of the type of this.
     */
    @Override
    protected Node deepCopyInto(Node n) {
        GenericProcessingInstruction p;
        p = (GenericProcessingInstruction)super.deepCopyInto(n);
        p.setTarget(getTarget());
        return p;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new GenericProcessingInstruction();
    }
}
