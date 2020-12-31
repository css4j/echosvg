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

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.URIValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides a manager for the 'cursor' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class CursorManager extends AbstractValueManager {

    /**
     * The identifier values.
     */
    protected static final StringMap values = new StringMap();
    static {
        values.put(CSSConstants.CSS_AUTO_VALUE,
                   ValueConstants.AUTO_VALUE);
        values.put(CSSConstants.CSS_CROSSHAIR_VALUE,
                   ValueConstants.CROSSHAIR_VALUE);
        values.put(CSSConstants.CSS_DEFAULT_VALUE,
                   ValueConstants.DEFAULT_VALUE);
        values.put(CSSConstants.CSS_E_RESIZE_VALUE,
                   ValueConstants.E_RESIZE_VALUE);
        values.put(CSSConstants.CSS_HELP_VALUE,
                   ValueConstants.HELP_VALUE);
        values.put(CSSConstants.CSS_MOVE_VALUE,
                   ValueConstants.MOVE_VALUE);
        values.put(CSSConstants.CSS_N_RESIZE_VALUE,
                   ValueConstants.N_RESIZE_VALUE);
        values.put(CSSConstants.CSS_NE_RESIZE_VALUE,
                   ValueConstants.NE_RESIZE_VALUE);
        values.put(CSSConstants.CSS_NW_RESIZE_VALUE,
                   ValueConstants.NW_RESIZE_VALUE);
        values.put(CSSConstants.CSS_POINTER_VALUE,
                   ValueConstants.POINTER_VALUE);
        values.put(CSSConstants.CSS_S_RESIZE_VALUE,
                   ValueConstants.S_RESIZE_VALUE);
        values.put(CSSConstants.CSS_SE_RESIZE_VALUE,
                   ValueConstants.SE_RESIZE_VALUE);
        values.put(CSSConstants.CSS_SW_RESIZE_VALUE,
                   ValueConstants.SW_RESIZE_VALUE);
        values.put(CSSConstants.CSS_TEXT_VALUE,
                   ValueConstants.TEXT_VALUE);
        values.put(CSSConstants.CSS_W_RESIZE_VALUE,
                   ValueConstants.W_RESIZE_VALUE);
        values.put(CSSConstants.CSS_WAIT_VALUE,
                   ValueConstants.WAIT_VALUE);
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
        return SVGTypes.TYPE_CURSOR_VALUE;
    }

    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    public String getPropertyName() {
        return CSSConstants.CSS_CURSOR_PROPERTY;
    }

    /**
     * Implements {@link ValueManager#getDefaultValue()}.
     */
    public Value getDefaultValue() {
        return ValueConstants.AUTO_VALUE;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    public Value createValue(LexicalUnit lu, CSSEngine engine)
        throws DOMException {
        ListValue result = new ListValue();
        switch (lu.getLexicalUnitType()) {
        case INHERIT:
            return ValueConstants.INHERIT_VALUE;

        case URI:
            do {
                result.append(new URIValue(lu.getStringValue(),
                                           resolveURI(engine.getCSSBaseURI(),
                                                      lu.getStringValue())));
                lu = lu.getNextLexicalUnit();
                if (lu == null) {
                    throw createMalformedLexicalUnitDOMException();
                }
                if (lu.getLexicalUnitType() !=
                        LexicalUnit.LexicalType.OPERATOR_COMMA) {
                    throw createInvalidLexicalUnitDOMException
                        (lu.getLexicalUnitType());
                }
                lu = lu.getNextLexicalUnit();
                if (lu == null) {
                    throw createMalformedLexicalUnitDOMException();
                }
            } while (lu.getLexicalUnitType() == LexicalUnit.LexicalType.URI);
            if (lu.getLexicalUnitType() != LexicalUnit.LexicalType.IDENT) {
                throw createInvalidLexicalUnitDOMException
                    (lu.getLexicalUnitType());
            }
            // Fall through...

        case IDENT:
            String s = lu.getStringValue().toLowerCase().intern();
            Object v = values.get(s);
            if (v == null) {
                throw createInvalidIdentifierDOMException(lu.getStringValue());
            }
            result.append((Value)v);
            lu = lu.getNextLexicalUnit();
            break;
        default:
            break;
        }
        if (lu != null) {
            throw createInvalidLexicalUnitDOMException
                (lu.getLexicalUnitType());
        }
        return result;
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
        if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
            ListValue lv = (ListValue)value;
            int len = lv.getLength();
            ListValue result = new ListValue(' ');
            for (int i=0; i<len; i++) {
                Value v = lv.item(0);
                if (v.getPrimitiveType() == CSSPrimitiveValue.CSS_URI) {
                    // Reveal the absolute value as the cssText now.
                    result.append(new URIValue(v.getStringValue(),
                                               v.getStringValue()));
                } else {
                    result.append(v);
                }
            }
            return result;
        }
        return super.computeValue(elt, pseudo, engine, idx, sm, value);
    }

}
