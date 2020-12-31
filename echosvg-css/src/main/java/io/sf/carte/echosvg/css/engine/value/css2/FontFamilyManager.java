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
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.AbstractValueManager;
import io.sf.carte.echosvg.css.engine.value.ListValue;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.StringValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a factory for the 'font-family' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class FontFamilyManager extends AbstractValueManager {

    /**
     * The default value.
     */
    protected static final ListValue DEFAULT_VALUE = new ListValue();
    static {
        DEFAULT_VALUE.append
            (new StringValue(CSSPrimitiveValue.CSS_STRING,
                             "Arial"));
        DEFAULT_VALUE.append
            (new StringValue(CSSPrimitiveValue.CSS_STRING,
                             "Helvetica"));
        DEFAULT_VALUE.append
            (new StringValue(CSSPrimitiveValue.CSS_IDENT,
                             CSSConstants.CSS_SANS_SERIF_VALUE));
    }

    /**
     * The identifier values.
     */
    protected static final StringMap values = new StringMap();
    static {
        values.put(CSSConstants.CSS_CURSIVE_VALUE,
                   ValueConstants.CURSIVE_VALUE);
        values.put(CSSConstants.CSS_FANTASY_VALUE,
                   ValueConstants.FANTASY_VALUE);
        values.put(CSSConstants.CSS_MONOSPACE_VALUE,
                   ValueConstants.MONOSPACE_VALUE);
        values.put(CSSConstants.CSS_SERIF_VALUE,
                   ValueConstants.SERIF_VALUE);
        values.put(CSSConstants.CSS_SANS_SERIF_VALUE,
                   ValueConstants.SANS_SERIF_VALUE);
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
        return false;
    }

    /**
     * Implements {@link ValueManager#getPropertyType()}.
     */
    @Override
    public int getPropertyType() {
        return SVGTypes.TYPE_FONT_FAMILY_VALUE;
    }

    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    @Override
    public String getPropertyName() {
        return CSSConstants.CSS_FONT_FAMILY_PROPERTY;
    }

    /**
     * Implements {@link ValueManager#getDefaultValue()}.
     */
    @Override
    public Value getDefaultValue() {
        return DEFAULT_VALUE;
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
        case STRING:
        }
        ListValue result = new ListValue();
        for (;;) {
            switch (lu.getLexicalUnitType()) {
            case STRING:
                result.append(new StringValue(CSSPrimitiveValue.CSS_STRING,
                                              lu.getStringValue()));
                lu = lu.getNextLexicalUnit();
                break;

            case IDENT:
                StringBuffer sb = new StringBuffer(lu.getStringValue());
                lu = lu.getNextLexicalUnit();
                if (lu != null && isIdentOrNumber(lu)) {
                    do {
                        sb.append(' ');
                        switch (lu.getLexicalUnitType()) {
                        case IDENT:
                            sb.append(lu.getStringValue());
                            break;
                        case INTEGER:
                            //Some font names contain integer values but are not quoted!
                            //Example: "Univers 45 Light"
                            sb.append(Integer.toString(lu.getIntegerValue()));
                        default:
                            break;
                        }
                        lu = lu.getNextLexicalUnit();
                    } while (lu != null && isIdentOrNumber(lu));
                    result.append(new StringValue(CSSPrimitiveValue.CSS_STRING,
                                                  sb.toString()));
                } else {
                    String id = sb.toString();
                    String s = id.toLowerCase().intern();
                    Value v = (Value)values.get(s);
                    result.append((v != null)
                                  ? v
                                  : new StringValue
                                        (CSSPrimitiveValue.CSS_STRING, id));
                }
                break;
                default:
            }
            if (lu == null)
                return result;
            if (lu.getLexicalUnitType() != LexicalUnit.LexicalType.OPERATOR_COMMA)
                throw createInvalidLexicalUnitDOMException
                    (lu.getLexicalUnitType());
            lu = lu.getNextLexicalUnit();
            if (lu == null)
                throw createMalformedLexicalUnitDOMException();
        }
    }

    private boolean isIdentOrNumber(LexicalUnit lu) {
        LexicalType type = lu.getLexicalUnitType();
        switch (type) {
        case IDENT:
        case INTEGER:
            return true;
        default:
            return false;
        }
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
        if (value == DEFAULT_VALUE) {
            CSSContext ctx = engine.getCSSContext();
            value = ctx.getDefaultFontFamily();
        }
        return value;
    }
}
