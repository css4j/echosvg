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
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.URIValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the SVGPaint property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGPaintManager extends SVGColorManager {
    
    /**
     * Creates a new SVGPaintManager.
     */
    public SVGPaintManager(String prop) {
        super(prop);
    }

    /**
     * Creates a new SVGPaintManager.
     * @param prop The property name.
     * @param v The default value.
     */
    public SVGPaintManager(String prop, Value v) {
        super(prop, v);
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
        return SVGTypes.TYPE_PAINT;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    @Override
    public Value createValue(LexicalUnit lu, CSSEngine engine)
        throws DOMException {
        switch (lu.getLexicalUnitType()) {
        case IDENT:
            if (lu.getStringValue().equalsIgnoreCase
                (CSSConstants.CSS_NONE_VALUE)) {
                return ValueConstants.NONE_VALUE;
            }
            // Fall through
        default:
            return super.createValue(lu, engine);
        case URI:
        }
        String value = lu.getStringValue();
        String uri = resolveURI(engine.getCSSBaseURI(), value);
        lu = lu.getNextLexicalUnit();
        if (lu == null) {
            return new URIValue(value, uri);
        }

        ListValue result = new ListValue(' ');
        result.append(new URIValue(value, uri));

        if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.IDENT) {
            if (lu.getStringValue().equalsIgnoreCase
                (CSSConstants.CSS_NONE_VALUE)) {
                result.append(ValueConstants.NONE_VALUE);
                return result;
            }
        }
        Value v = super.createValue(lu, engine);
        if (v.getCssValueType() == CSSValue.CSS_CUSTOM) {
            ListValue lv = (ListValue)v;
            for (int i = 0; i < lv.getLength(); i++) {
                result.append(lv.item(i));
            }
        } else {
            result.append(v);
        }
        return result;
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
        if (value == ValueConstants.NONE_VALUE) {
            return value;
        }
        if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
            ListValue lv = (ListValue)value;
            Value v = lv.item(0);
            if (v.getPrimitiveType() == CSSPrimitiveValue.CSS_URI) {
                v = lv.item(1);
                if (v == ValueConstants.NONE_VALUE) {
                    return value;
                }
                Value t = super.computeValue(elt, pseudo, engine, idx, sm, v);
                if (t != v) {
                    ListValue result = new ListValue(' ');
                    result.append(lv.item(0));
                    result.append(t);
                    if (lv.getLength() == 3) {
                        result.append(lv.item(1));
                    }
                    return result;
                }
                return value;
            }
        }
        return super.computeValue(elt, pseudo, engine, idx, sm, value);
    }
}
