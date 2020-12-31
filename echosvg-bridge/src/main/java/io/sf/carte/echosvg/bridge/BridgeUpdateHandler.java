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
package io.sf.carte.echosvg.bridge;

import org.w3c.dom.events.MutationEvent;

import io.sf.carte.echosvg.anim.dom.AnimatedLiveAttributeValue;
import io.sf.carte.echosvg.css.engine.CSSEngineEvent;

/**
 * Interface for objects interested in being notified of updates.
 * 
 * @author <a href="mailto:vincent.hardy@apache.org">Vincent Hardy</a>
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id$
 */
public interface BridgeUpdateHandler {
    
    /**
     * Invoked when an MutationEvent of type 'DOMAttrModified' is fired.
     */
    void handleDOMAttrModifiedEvent(MutationEvent evt);

    /**
     * Invoked when an MutationEvent of type 'DOMNodeInserted' is fired.
     */
    void handleDOMNodeInsertedEvent(MutationEvent evt);

    /**
     * Invoked when an MutationEvent of type 'DOMNodeRemoved' is fired.
     */
    void handleDOMNodeRemovedEvent(MutationEvent evt);

    /**
     * Invoked when an MutationEvent of type 'DOMCharacterDataModified' 
     * is fired.
     */
    void handleDOMCharacterDataModified(MutationEvent evt);

    /**
     * Invoked when an CSSEngineEvent is fired.
     */
    void handleCSSEngineEvent(CSSEngineEvent evt);

    /**
     * Invoked when the animated value of an animated attribute has changed.
     */
    void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue alav);

    /**
     * Invoked when an 'other' animation value has changed.
     */
    void handleOtherAnimationChanged(String type);

    /**
     * Disposes this BridgeUpdateHandler and releases all resources.
     */
    void dispose();
}
