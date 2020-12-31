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

import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.SVGTypes;

/**
 * This class provides a manager for the 'display' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class DisplayManager extends IdentifierManager {

    /**
     * The identifier values.
     */
    protected static final StringMap values = new StringMap();
    static {
        values.put(CSSConstants.CSS_BLOCK_VALUE,
                   ValueConstants.BLOCK_VALUE);
        values.put(CSSConstants.CSS_COMPACT_VALUE,
                   ValueConstants.COMPACT_VALUE);
        values.put(CSSConstants.CSS_INLINE_VALUE,
                   ValueConstants.INLINE_VALUE);
        values.put(CSSConstants.CSS_INLINE_TABLE_VALUE,
                   ValueConstants.INLINE_TABLE_VALUE);
        values.put(CSSConstants.CSS_LIST_ITEM_VALUE,
                   ValueConstants.LIST_ITEM_VALUE);
        values.put(CSSConstants.CSS_MARKER_VALUE,
                   ValueConstants.MARKER_VALUE);
        values.put(CSSConstants.CSS_NONE_VALUE,
                   ValueConstants.NONE_VALUE);
        values.put(CSSConstants.CSS_RUN_IN_VALUE,
                   ValueConstants.RUN_IN_VALUE);
        values.put(CSSConstants.CSS_TABLE_VALUE,
                   ValueConstants.TABLE_VALUE);
        values.put(CSSConstants.CSS_TABLE_CAPTION_VALUE,
                   ValueConstants.TABLE_CAPTION_VALUE);
        values.put(CSSConstants.CSS_TABLE_CELL_VALUE,
                   ValueConstants.TABLE_CELL_VALUE);
        values.put(CSSConstants.CSS_TABLE_COLUMN_VALUE,
                   ValueConstants.TABLE_COLUMN_VALUE);
        values.put(CSSConstants.CSS_TABLE_COLUMN_GROUP_VALUE,
                   ValueConstants.TABLE_COLUMN_GROUP_VALUE);
        values.put(CSSConstants.CSS_TABLE_FOOTER_GROUP_VALUE,
                   ValueConstants.TABLE_FOOTER_GROUP_VALUE);
        values.put(CSSConstants.CSS_TABLE_HEADER_GROUP_VALUE,
                   ValueConstants.TABLE_HEADER_GROUP_VALUE);
        values.put(CSSConstants.CSS_TABLE_ROW_VALUE,
                   ValueConstants.TABLE_ROW_VALUE);
        values.put(CSSConstants.CSS_TABLE_ROW_GROUP_VALUE,
                   ValueConstants.TABLE_ROW_GROUP_VALUE);
    }

    /**
     * Implements {@link
     * io.sf.carte.echosvg.css.engine.value.ValueManager#isInheritedProperty()}.
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
        return SVGTypes.TYPE_IDENT;
    }

    /**
     * Implements {@link
     * io.sf.carte.echosvg.css.engine.value.ValueManager#getPropertyName()}.
     */
    @Override
    public String getPropertyName() {
        return CSSConstants.CSS_DISPLAY_PROPERTY;
    }

    /**
     * Implements {@link
     * io.sf.carte.echosvg.css.engine.value.ValueManager#getDefaultValue()}.
     */
    @Override
    public Value getDefaultValue() {
        return ValueConstants.INLINE_VALUE;
    }

    /**
     * Implements {@link IdentifierManager#getIdentifiers()}.
     */
    @Override
    public StringMap getIdentifiers() {
        return values;
    }
}
