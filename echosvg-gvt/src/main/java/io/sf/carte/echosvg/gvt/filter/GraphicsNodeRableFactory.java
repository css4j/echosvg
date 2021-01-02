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
package io.sf.carte.echosvg.gvt.filter;

import io.sf.carte.echosvg.gvt.GraphicsNode;

/**
 * This interface lets <code>GraphicsNode</code> create instances of
 * <code>GraphicsNodeRable</code> appropriate for the filter module
 * implementation.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface GraphicsNodeRableFactory {
	/**
	 * Returns a <code>GraphicsNodeRable</code> initialized with the input
	 * <code>GraphicsNode</code>.
	 */
	GraphicsNodeRable createGraphicsNodeRable(GraphicsNode node);
}
