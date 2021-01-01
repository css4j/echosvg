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
package io.sf.carte.echosvg.dom.svg;

import org.w3c.dom.Attr;

/**
 * This interface should be implemented by all the attribute values
 * objects that must be updated when the attribute node is modified.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface LiveAttributeValue {

    /**
     * Called when an Attr node has been added.
     */
    void attrAdded(Attr node, String newv);

    /**
     * Called when an Attr node has been modified.
     */
    void attrModified(Attr node, String oldv, String newv);

    /**
     * Called when an Attr node has been removed.
     */
    void attrRemoved(Attr node, String oldv);
}
