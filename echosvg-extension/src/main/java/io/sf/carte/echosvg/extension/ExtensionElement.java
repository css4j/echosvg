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
package io.sf.carte.echosvg.extension;

import io.sf.carte.echosvg.anim.dom.SVGOMElement;
import io.sf.carte.echosvg.dom.AbstractDocument;

/**
 * This class implements the basic features an element must have in order
 * to be usable as a foreign element within an SVGOMDocument.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class ExtensionElement 
    extends SVGOMElement {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new Element object.
     */
    protected ExtensionElement() {
    }

    /**
     * Creates a new Element object.
     * @param name The element name, for validation purposes.
     * @param owner The owner document.
     */
    protected ExtensionElement(String name, AbstractDocument owner) {
        super(name, owner);
    }

    /**
     * Tests whether this node is readonly.
     */
    @Override
    public boolean isReadonly() {
        return false;
    }

    /**
     * Sets this node readonly attribute.
     */
    @Override
    public void setReadonly(boolean v) {
    }
}
