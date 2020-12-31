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
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.dom.css.CSSValue;

/**
 * This singleton class represents the 'inherit' value.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class InheritValue extends AbstractValue {
    /**
     * The only instance of this class.
     */
    public static final InheritValue INSTANCE = new InheritValue();

    /**
     * Creates a new InheritValue object.
     */
    protected InheritValue() {
    }

    /**
     *  A string representation of the current value.
     */
    @Override
    public String getCssText() {
        return "inherit";
    }

    /**
     * A code defining the type of the value.
     */
    @Override
    public short getCssValueType() {
        return CSSValue.CSS_INHERIT;
    }

    /**
     * Returns a printable representation of this object.
     */
    @Override
    public String toString() {
        return getCssText();
    }
}
