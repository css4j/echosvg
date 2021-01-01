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
import org.w3c.dom.css.CSSValue;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.LengthManager;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'enable-background' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class EnableBackgroundManager extends LengthManager {
    
    /**
     * The length orientation.
     */
    protected int orientation;

    /**
     * Implements {@link ValueManager#isInheritedProperty()}.
     */
    @Override
    public boolean isInheritedProperty() {
        return false;
    }

    /**
     * Implements {@link ValueManager#isAnimatableProperty()}.
     */
    @Override
    public boolean isAnimatableProperty() {
        return false;
    }

    /**
     * Implements {@link ValueManager#isAdditiveProperty()}.
     */
    @Override
    public boolean isAdditiveProperty() {
        return false;
    }

    /**
     * Implements {@link ValueManager#getPropertyType()}.
     */
    @Override
    public int getPropertyType() {
        return SVGTypes.TYPE_ENABLE_BACKGROUND_VALUE;
    }

    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    @Override
    public String getPropertyName() {
        return CSSConstants.CSS_ENABLE_BACKGROUND_PROPERTY;
    }
    
    /**
     * Implements {@link ValueManager#getDefaultValue()}.
     */
    @Override
    public Value getDefaultValue() {
        return SVGValueConstants.ACCUMULATE_VALUE;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    @Override
    public Value createValue(LexicalUnit lu, CSSEngine engine)
        throws DOMException {
        switch (lu.getLexicalUnitType()) {
        case INHERIT:
            return ValueConstants.INHERIT_VALUE;

        default:
            throw createInvalidLexicalUnitDOMException
                (lu.getLexicalUnitType());

        case IDENT:
            String id = lu.getStringValue().toLowerCase().intern();
            if (id == CSSConstants.CSS_ACCUMULATE_VALUE) {
                return SVGValueConstants.ACCUMULATE_VALUE;
            }
            if (id != CSSConstants.CSS_NEW_VALUE) {
                throw createInvalidIdentifierDOMException(id);
            }
            ListValue result = new ListValue(' ');
            result.append(SVGValueConstants.NEW_VALUE);
            lu = lu.getNextLexicalUnit();
            if (lu == null) {
                return result;
            }
            result.append(super.createValue(lu, engine));
            for (int i = 1; i < 4; i++) {
                lu = lu.getNextLexicalUnit();
                if (lu == null){
                    throw createMalformedLexicalUnitDOMException();
                }
                result.append(super.createValue(lu, engine));
            }
            return result;
        }
    }

    /**
     * Implements {@link
     * ValueManager#createStringValue(short,String,CSSEngine)}.
     */
    @Override
    public Value createStringValue(short type, String value,
                                   CSSEngine engine) {
        if (type != CSSPrimitiveValue.CSS_IDENT) {
            throw createInvalidStringTypeDOMException(type);
        }
        if (!value.equalsIgnoreCase(CSSConstants.CSS_ACCUMULATE_VALUE)) {
            throw createInvalidIdentifierDOMException(value);
        }
        return SVGValueConstants.ACCUMULATE_VALUE;
    }

    /**
     * Implements {@link ValueManager#createFloatValue(short,float)}.
     */
    @Override
    public Value createFloatValue(short unitType, float floatValue)
        throws DOMException {
        throw createDOMException();
    }

    /**
     * Implements {@link
     * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
     */
    @Override
    public Value computeValue(CSSStylableElement elt,
                              String pseudo,
                              CSSEngine engine,
                              int idx,
                              StyleMap sm,
                              Value value) {
        if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
            ListValue lv = (ListValue)value;
            if (lv.getLength() == 5) {
                Value lv1 = lv.item(1);
                orientation = HORIZONTAL_ORIENTATION;
                Value v1 = super.computeValue(elt, pseudo, engine,
                                              idx, sm, lv1);
                Value lv2 = lv.item(2);
                orientation = VERTICAL_ORIENTATION;
                Value v2 = super.computeValue(elt, pseudo, engine,
                                              idx, sm, lv2);
                Value lv3 = lv.item(3);
                orientation = HORIZONTAL_ORIENTATION;
                Value v3 = super.computeValue(elt, pseudo, engine,
                                              idx, sm, lv3);
                Value lv4 = lv.item(4);
                orientation = VERTICAL_ORIENTATION;
                Value v4 = super.computeValue(elt, pseudo, engine,
                                              idx, sm, lv4);

                if (lv1 != v1 || lv2 != v2 ||
                    lv3 != v3 || lv4 != v4) {
                    ListValue result = new ListValue(' ');
                    result.append(lv.item(0));
                    result.append(v1);
                    result.append(v2);
                    result.append(v3);
                    result.append(v4);
                    return result;
                }
            }
        }
        return value;
    }

    /**
     * Indicates the orientation of the property associated with
     * this manager.
     */
    @Override
    protected int getOrientation() {
        return orientation;
    }

}
