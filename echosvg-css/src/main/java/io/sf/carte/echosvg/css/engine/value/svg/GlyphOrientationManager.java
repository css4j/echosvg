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

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

import io.sf.carte.doc.style.css.CSSUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'glyph-orientation' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class GlyphOrientationManager extends AbstractValueManager {
    
    /**
     * Implements {@link
     * io.sf.carte.echosvg.css.engine.value.ValueManager#isInheritedProperty()}.
     */
    public boolean isInheritedProperty() {
        return true;
    }

    /**
     * Implements {@link ValueManager#isAnimatableProperty()}.
     */
    public boolean isAnimatableProperty() {
        return false;
    }

    /**
     * Implements {@link ValueManager#isAdditiveProperty()}.
     */
    public boolean isAdditiveProperty() {
        return false;
    }

    /**
     * Implements {@link ValueManager#getPropertyType()}.
     */
    public int getPropertyType() {
        return SVGTypes.TYPE_ANGLE;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    public Value createValue(LexicalUnit lu, CSSEngine engine)
        throws DOMException {
        switch (lu.getLexicalUnitType()) {
        case INHERIT:
            return SVGValueConstants.INHERIT_VALUE;

        case DIMENSION:
            switch (lu.getCssUnit()) {
            case CSSUnit.CSS_DEG:
                return new FloatValue(CSSPrimitiveValue.CSS_DEG,
                        lu.getFloatValue());
            case CSSUnit.CSS_RAD:
                return new FloatValue(CSSPrimitiveValue.CSS_RAD,
                        lu.getFloatValue());
            case CSSUnit.CSS_GRAD:
                return new FloatValue(CSSPrimitiveValue.CSS_GRAD,
                        lu.getFloatValue());
            case CSSUnit.CSS_TURN:
                return new FloatValue(CSSPrimitiveValue.CSS_DEG,
                        lu.getFloatValue() * 360f);
            }

            // For SVG angle properties unit defaults to 'deg'.
        case INTEGER:
            { 
                int n = lu.getIntegerValue();
                return new FloatValue(CSSPrimitiveValue.CSS_DEG, n);
            }
        case REAL:
            { 
                float n = lu.getFloatValue();
                return new FloatValue(CSSPrimitiveValue.CSS_DEG, n);
            }
        }
    
        throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
    }

    /**
     * Implements {@link ValueManager#createFloatValue(short,float)}.
     */
    public Value createFloatValue(short type, float floatValue)
        throws DOMException {
        switch (type) {
        case CSSPrimitiveValue.CSS_DEG:
        case CSSPrimitiveValue.CSS_GRAD:
        case CSSPrimitiveValue.CSS_RAD:
            return new FloatValue(type, floatValue);
        }
        throw createInvalidFloatValueDOMException(floatValue);
    }
}