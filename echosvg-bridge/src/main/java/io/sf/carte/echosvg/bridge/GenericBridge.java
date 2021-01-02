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

import org.w3c.dom.Element;

/**
 * A tagging interface that bridges for elements child of
 * <code>GraphicsNodeBridge</code> should implement.
 *
 * @author <a href="mailto:vincent.hardy@apache.org">Vincent Hardy</a>
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface GenericBridge extends Bridge {

	/**
	 * Invoked to handle an <code>Element</code> for a given
	 * <code>BridgeContext</code>. For example, see the
	 * <code>SVGTitleElementBridge</code>.
	 *
	 * @param ctx the bridge context to use
	 * @param e   the element being handled
	 */
	void handleElement(BridgeContext ctx, Element e);
}
