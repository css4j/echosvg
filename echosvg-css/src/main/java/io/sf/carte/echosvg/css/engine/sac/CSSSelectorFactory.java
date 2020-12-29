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
package io.sf.carte.echosvg.css.engine.sac;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

/**
 * This class implements the {@link org.w3c.css.sac.SelectorFactory} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class CSSSelectorFactory implements SelectorFactory {

    /**
     * The instance of this class.
     */
    public static final SelectorFactory INSTANCE = new CSSSelectorFactory();

    /**
     * This class does not need to be instantiated.
     */
    protected CSSSelectorFactory() {
    }

    /**
     * <b>SAC</b>: Implements {@link
     * SelectorFactory#createConditionalSelector(SimpleSelector,Condition)}.
     */
    public ConditionalSelector createConditionalSelector
        (SimpleSelector selector,
         Condition condition)
        throws CSSException {
        return new CSSConditionalSelector(selector, condition);
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.SelectorFactory#createAnyNodeSelector()}.
     */
    public SimpleSelector createAnyNodeSelector() throws CSSException {
        throw new CSSException("Not implemented in CSS2");
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.SelectorFactory#createRootNodeSelector()}.
     */
    public SimpleSelector createRootNodeSelector() throws CSSException {
        throw new CSSException("Not implemented in CSS2");
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.SelectorFactory#createNegativeSelector(SimpleSelector)}.
     */
    public NegativeSelector createNegativeSelector(SimpleSelector selector)
        throws CSSException {
        throw new CSSException("Not implemented in CSS2");
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.SelectorFactory#createElementSelector(String,String)}.
     */
    public ElementSelector createElementSelector(String namespaceURI,
                                                 String tagName)
        throws CSSException {
        return new CSSElementSelector(namespaceURI, tagName);
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.SelectorFactory#createTextNodeSelector(String)}.
     */
    public CharacterDataSelector createTextNodeSelector(String data)
        throws CSSException {
        throw new CSSException("Not implemented in CSS2");
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.SelectorFactory#createCDataSectionSelector(String)}.
     */
    public CharacterDataSelector createCDataSectionSelector(String data)
        throws CSSException {
        throw new CSSException("Not implemented in CSS2");
    }

    /**
     * <b>SAC</b>: Implements {@link
     * SelectorFactory#createProcessingInstructionSelector(String,String)}.
     */
    public ProcessingInstructionSelector createProcessingInstructionSelector
        (String target,
         String data) throws CSSException {
        throw new CSSException("Not implemented in CSS2");
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.SelectorFactory#createCommentSelector(String)}.
     */
    public CharacterDataSelector createCommentSelector(String data)
        throws CSSException {
        throw new CSSException("Not implemented in CSS2");
    }

    /**
     * <b>SAC</b>: Implements {@link
     * SelectorFactory#createPseudoElementSelector(String,String)}.
     */
    public ElementSelector createPseudoElementSelector(String namespaceURI,
                                                       String pseudoName)
        throws CSSException {
        return new CSSPseudoElementSelector(namespaceURI, pseudoName);
    }

    /**
     * <b>SAC</b>: Implements {@link
     * SelectorFactory#createDescendantSelector(Selector,SimpleSelector)}.
     */
    public DescendantSelector createDescendantSelector
        (Selector parent,
         SimpleSelector descendant)
        throws CSSException {
        return new CSSDescendantSelector(parent, descendant);
    }

    /**
     * <b>SAC</b>: Implements {@link
     * SelectorFactory#createChildSelector(Selector,SimpleSelector)}.
     */
    public DescendantSelector createChildSelector(Selector parent,
                                                  SimpleSelector child)
        throws CSSException {
        return new CSSChildSelector(parent, child);
    }

    /**
     * <b>SAC</b>: Implements {@link
     * SelectorFactory#createDirectAdjacentSelector(short,Selector,SimpleSelector)}.
     */
    public SiblingSelector createDirectAdjacentSelector
        (short          nodeType,
         Selector       child,
         SimpleSelector directAdjacent)
        throws CSSException {
        return new CSSDirectAdjacentSelector(nodeType, child,
                                               directAdjacent);
    }
}
