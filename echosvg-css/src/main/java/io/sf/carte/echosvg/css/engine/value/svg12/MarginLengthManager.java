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

package io.sf.carte.echosvg.css.engine.value.svg12;

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.LengthManager;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a factory for the 'margin-*' properties values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class MarginLengthManager extends LengthManager {

    protected String  prop;

    public MarginLengthManager(String prop) {
        this.prop = prop;
    }
    
    /**
     * Implements {@link ValueManager#isInheritedProperty()}.
     */
    @Override
    public boolean isInheritedProperty() {
        return true;
    }

    /**
     * Implements {@link ValueManager#isAnimatableProperty()}.
     */
    @Override
    public boolean isAnimatableProperty() {
        return true;
    }

    /**
     * Implements {@link ValueManager#isAdditiveProperty()}.
     */
    @Override
    public boolean isAdditiveProperty() {
        return true;
    }

    /**
     * Implements {@link ValueManager#getPropertyType()}.
     */
    @Override
    public int getPropertyType() {
        return SVGTypes.TYPE_LENGTH_OR_INHERIT;
    }

    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    @Override
    public String getPropertyName() {
        return prop;
    }
    
    /**
     * Implements {@link ValueManager#getDefaultValue()}.
     */
    @Override
    public Value getDefaultValue() {
        return ValueConstants.NUMBER_0;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    @Override
    public Value createValue(LexicalUnit lu, CSSEngine engine)
        throws DOMException {
        if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.INHERIT) {
            return ValueConstants.INHERIT_VALUE;
        }
        return super.createValue(lu, engine);
    }


    /**
     * Indicates the orientation of the property associated with
     * this manager.
     */
    @Override
    protected int getOrientation() {
        // Margins are always wrt to block width, event for top/bottom.
        return HORIZONTAL_ORIENTATION;
    }
}
