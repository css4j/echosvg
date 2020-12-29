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
package io.sf.carte.echosvg.css.engine.value.svg;

import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.SVGTypes;

import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class provides a manager for the '*-opacity' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class OpacityManager extends AbstractValueManager {
    
    /**
     * Whether the managed property is inherited.
     */
    protected boolean inherited;

    /**
     * The managed property name.
     */
    protected String property;

    /**
     * Creates a new OpacityManager.
     */
    public OpacityManager(String prop, boolean inherit) {
        property = prop;
        inherited = inherit;
    }

    /**
     * Implements {@link ValueManager#isInheritedProperty()}.
     */
    public boolean isInheritedProperty() {
        return inherited;
    }

    /**
     * Implements {@link ValueManager#isAnimatableProperty()}.
     */
    public boolean isAnimatableProperty() {
        return true;
    }

    /**
     * Implements {@link ValueManager#isAdditiveProperty()}.
     */
    public boolean isAdditiveProperty() {
        return true;
    }

    /**
     * Implements {@link ValueManager#getPropertyType()}.
     */
    public int getPropertyType() {
        return SVGTypes.TYPE_NUMBER_OR_INHERIT;
    }

    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    public String getPropertyName() {
        return property;
    }
    
    /**
     * Implements {@link ValueManager#getDefaultValue()}.
     */
    public Value getDefaultValue() {
        return SVGValueConstants.NUMBER_1;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    public Value createValue(LexicalUnit lu, CSSEngine engine)
        throws DOMException {
        switch (lu.getLexicalUnitType()) {
        case LexicalUnit.SAC_INHERIT:
            return SVGValueConstants.INHERIT_VALUE;

        case LexicalUnit.SAC_INTEGER:
            return new FloatValue(CSSPrimitiveValue.CSS_NUMBER,
                                  lu.getIntegerValue());

        case LexicalUnit.SAC_REAL:
            return new FloatValue(CSSPrimitiveValue.CSS_NUMBER,
                                  lu.getFloatValue());
        }
        throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
    }

    /**
     * Implements {@link ValueManager#createFloatValue(short,float)}.
     */
    public Value createFloatValue(short type, float floatValue)
        throws DOMException {
        if (type == CSSPrimitiveValue.CSS_NUMBER) {
            return new FloatValue(type, floatValue);
        }
        throw createInvalidFloatTypeDOMException(type);
    }
}
