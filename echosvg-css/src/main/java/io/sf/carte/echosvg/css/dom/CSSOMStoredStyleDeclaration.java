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
package io.sf.carte.echosvg.css.dom;

import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.StyleDeclaration;
import io.sf.carte.echosvg.css.engine.StyleDeclarationProvider;
import io.sf.carte.echosvg.css.engine.value.Value;

/**
 * A class for SVG style declarations that store their properties in a
 * {@link io.sf.carte.echosvg.css.engine.StyleDeclaration}.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id$
 */
public abstract class CSSOMStoredStyleDeclaration
    extends CSSOMSVGStyleDeclaration
    implements CSSOMStyleDeclaration.ValueProvider,
               CSSOMStyleDeclaration.ModificationHandler,
               StyleDeclarationProvider {

    /**
     * The object storing the properties.
     */
    protected StyleDeclaration declaration;

    /**
     * Creates a new CSSOMStoredStyleDeclaration.
     */
    public CSSOMStoredStyleDeclaration(CSSEngine eng) {
        super(null, null, eng);
        valueProvider = this;
        setModificationHandler(this);
    }

    /**
     * Returns the object storing the properties of this style declaration.
     */
    @Override
    public StyleDeclaration getStyleDeclaration() {
        return declaration;
    }

    /**
     * Sets the object storing the properties of this style declaration.
     */
    @Override
    public void setStyleDeclaration(StyleDeclaration sd) {
        declaration = sd;
    }

    // ValueProvider /////////////////////////////////////////////////////////

    /**
     * Returns the current value associated with this object.
     */
    @Override
    public Value getValue(String name) {
        int idx = cssEngine.getPropertyIndex(name);
        for (int i = 0; i < declaration.size(); i++) {
            if (idx == declaration.getIndex(i)) {
                return declaration.getValue(i);
            }
        }
        return null;
    }

    /**
     * Tells whether the given property is important.
     */
    @Override
    public boolean isImportant(String name) {
        int idx = cssEngine.getPropertyIndex(name);
        for (int i = 0; i < declaration.size(); i++) {
            if (idx == declaration.getIndex(i)) {
                return declaration.getPriority(i);
            }
        }
        return false;
    }

    /**
     * Returns the text of the declaration.
     */
    @Override
    public String getText() {
        return declaration.toString(cssEngine);
    }

    /**
     * Returns the length of the declaration.
     */
    @Override
    public int getLength() {
        return declaration.size();
    }

    /**
     * Returns the value at the given.
     */
    @Override
    public String item(int idx) {
        return cssEngine.getPropertyName(declaration.getIndex(idx));
    }
}
