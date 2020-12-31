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
package io.sf.carte.echosvg.css.engine.value.css2;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'font-weight' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class FontWeightManager extends IdentifierManager {

    /**
     * The identifier values.
     */
    protected static final StringMap values = new StringMap();
    static {
        values.put(CSSConstants.CSS_ALL_VALUE,
                   ValueConstants.ALL_VALUE);
        values.put(CSSConstants.CSS_BOLD_VALUE,
                   ValueConstants.BOLD_VALUE);
        values.put(CSSConstants.CSS_BOLDER_VALUE,
                   ValueConstants.BOLDER_VALUE);
        values.put(CSSConstants.CSS_LIGHTER_VALUE,
                   ValueConstants.LIGHTER_VALUE);
        values.put(CSSConstants.CSS_NORMAL_VALUE,
                   ValueConstants.NORMAL_VALUE);
    }

    /**
     * Implements {@link ValueManager#isInheritedProperty()}.
     */
    public boolean isInheritedProperty() {
        return true;
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
        return false;
    }

    /**
     * Implements {@link ValueManager#getPropertyType()}.
     */
    public int getPropertyType() {
        return SVGTypes.TYPE_FONT_WEIGHT_VALUE;
    }

    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    public String getPropertyName() {
        return CSSConstants.CSS_FONT_WEIGHT_PROPERTY;
    }

    /**
     * Implements {@link ValueManager#getDefaultValue()}.
     */
    public Value getDefaultValue() {
        return ValueConstants.NORMAL_VALUE;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    public Value createValue(LexicalUnit lu, CSSEngine engine)
        throws DOMException {
        if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.INTEGER) {
            int i = lu.getIntegerValue();
            switch (i) {
            case 100:
                return ValueConstants.NUMBER_100;
            case 200:
                return ValueConstants.NUMBER_200;
            case 300:
                return ValueConstants.NUMBER_300;
            case 400:
                return ValueConstants.NUMBER_400;
            case 500:
                return ValueConstants.NUMBER_500;
            case 600:
                return ValueConstants.NUMBER_600;
            case 700:
                return ValueConstants.NUMBER_700;
            case 800:
                return ValueConstants.NUMBER_800;
            case 900:
                return ValueConstants.NUMBER_900;
            }
            throw createInvalidFloatValueDOMException(i);
        }
        return super.createValue(lu, engine);
    }

    /**
     * Implements {@link ValueManager#createFloatValue(short,float)}.
     */
    public Value createFloatValue(short type, float floatValue)
        throws DOMException {
        if (type == CSSPrimitiveValue.CSS_NUMBER) {
            int i = (int)floatValue;
            if (floatValue == i) {
                switch (i) {
                case 100:
                    return ValueConstants.NUMBER_100;
                case 200:
                    return ValueConstants.NUMBER_200;
                case 300:
                    return ValueConstants.NUMBER_300;
                case 400:
                    return ValueConstants.NUMBER_400;
                case 500:
                    return ValueConstants.NUMBER_500;
                case 600:
                    return ValueConstants.NUMBER_600;
                case 700:
                    return ValueConstants.NUMBER_700;
                case 800:
                    return ValueConstants.NUMBER_800;
                case 900:
                    return ValueConstants.NUMBER_900;
                }
            }
        }
        throw createInvalidFloatValueDOMException(floatValue);
    }

    /**
     * Implements {@link
     * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
     */
    public Value computeValue(CSSStylableElement elt,
                              String pseudo,
                              CSSEngine engine,
                              int idx,
                              StyleMap sm,
                              Value value) {
        if (value == ValueConstants.BOLDER_VALUE) {
            sm.putParentRelative(idx, true);

            CSSContext ctx = engine.getCSSContext();
            CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
            float fw;
            if (p == null) {
                fw = 400;
            } else {
                Value v = engine.getComputedStyle(p, pseudo, idx);
                fw = v.getFloatValue();
            }
            return createFontWeight(ctx.getBolderFontWeight(fw));
        } else if (value == ValueConstants.LIGHTER_VALUE) {
            sm.putParentRelative(idx, true);

            CSSContext ctx = engine.getCSSContext();
            CSSStylableElement p = CSSEngine.getParentCSSStylableElement(elt);
            float fw;
            if (p == null) {
                fw = 400;
            } else {
                Value v = engine.getComputedStyle(p, pseudo, idx);
                fw = v.getFloatValue();
            }
            return createFontWeight(ctx.getLighterFontWeight(fw));
        } else if (value == ValueConstants.NORMAL_VALUE) {
            return ValueConstants.NUMBER_400;
        } else if (value == ValueConstants.BOLD_VALUE) {
            return ValueConstants.NUMBER_700;
        }
        return value;
    }

    /**
     * Returns the CSS value associated with the given font-weight.
     */
    protected Value createFontWeight(float f) {
        switch ((int)f) {
        case 100:
            return ValueConstants.NUMBER_100;
        case 200:
            return ValueConstants.NUMBER_200;
        case 300:
            return ValueConstants.NUMBER_300;
        case 400:
            return ValueConstants.NUMBER_400;
        case 500:
            return ValueConstants.NUMBER_500;
        case 600:
            return ValueConstants.NUMBER_600;
        case 700:
            return ValueConstants.NUMBER_700;
        case 800:
            return ValueConstants.NUMBER_800;
        default: // 900
            return ValueConstants.NUMBER_900;
        }
    }

    /**
     * Implements {@link IdentifierManager#getIdentifiers()}.
     */
    public StringMap getIdentifiers() {
        return values;
    }
}
