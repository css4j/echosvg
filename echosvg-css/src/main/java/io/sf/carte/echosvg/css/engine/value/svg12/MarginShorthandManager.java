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
import io.sf.carte.echosvg.css.engine.value.AbstractValueFactory;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.SVG12CSSConstants;

/**
 * This class represents an object which provide support for the
 * 'margin' shorthand property.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class MarginShorthandManager
    extends AbstractValueFactory
    implements ShorthandManager {

    public MarginShorthandManager() { }
    
    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    public String getPropertyName() {
        return SVG12CSSConstants.CSS_MARGIN_PROPERTY;
    }
    
    /**
     * Implements {@link ShorthandManager#isAnimatableProperty()}.
     */
    public boolean isAnimatableProperty() {
        return true;
    }

    /**
     * Implements {@link ShorthandManager#isAdditiveProperty()}.
     */
    public boolean isAdditiveProperty() {
        return false;
    }

    /**
     * Implements {@link ShorthandManager#setValues(CSSEngine,ShorthandManager.PropertyHandler,LexicalUnit,boolean)}.
     */
    public void setValues(CSSEngine eng,
                          ShorthandManager.PropertyHandler ph,
                          LexicalUnit lu,
                          boolean imp)
        throws DOMException {
        if (lu.getLexicalUnitType() == LexicalUnit.LexicalType.INHERIT)
            return;

        LexicalUnit []lus  = new LexicalUnit[4];
        int cnt=0;
        while (lu != null) {
            if (cnt == 4)
                throw createInvalidLexicalUnitDOMException
                    (lu.getLexicalUnitType());
            lus[cnt++] = lu;
            lu = lu.getNextLexicalUnit();
        }
        switch (cnt) {
        case 1: lus[3] = lus[2] = lus[1] = lus[0]; break;
        case 2: lus[2] = lus[0];  lus[3] = lus[1]; break;
        case 3: lus[3] = lus[1]; break;
        default:
        }

        ph.property(SVG12CSSConstants.CSS_MARGIN_TOP_PROPERTY,    lus[0], imp);
        ph.property(SVG12CSSConstants.CSS_MARGIN_RIGHT_PROPERTY,  lus[1], imp);
        ph.property(SVG12CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY, lus[2], imp);
        ph.property(SVG12CSSConstants.CSS_MARGIN_LEFT_PROPERTY,   lus[3], imp);
    }
}
