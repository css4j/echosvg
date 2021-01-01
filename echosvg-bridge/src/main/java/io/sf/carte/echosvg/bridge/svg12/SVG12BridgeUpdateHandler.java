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
package io.sf.carte.echosvg.bridge.svg12;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.bridge.BridgeUpdateHandler;

/**
 * A BridgeUpdateHandler interface for SVG 1.2 specific events.  This is
 * for XBL event notification.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface SVG12BridgeUpdateHandler extends BridgeUpdateHandler {

    /**
     * Invoked when a bindable element's binding has changed.
     */
    void handleBindingEvent(Element bindableElement, Element shadowTree);

    /**
     * Invoked when the xblChildNodes property has changed because a
     * descendant xbl:content element has updated its selected nodes.
     */
    void handleContentSelectionChangedEvent(ContentSelectionChangedEvent csce);
}
