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

import java.awt.Paint;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * Bridge class for vending <code>Paint</code> objects.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @version $Id$
 */
public interface PaintBridge extends Bridge {

    /**
     * Creates a <code>Paint</code> according to the specified parameters.
     *
     * @param ctx the bridge context to use
     * @param paintElement the element that defines a Paint
     * @param paintedElement the element referencing the paint
     * @param paintedNode the graphics node on which the Paint will be applied
     * @param opacity the opacity of the Paint to create
     */
    Paint createPaint(BridgeContext ctx,
                      Element paintElement,
                      Element paintedElement,
                      GraphicsNode paintedNode,
                      float opacity);

}
