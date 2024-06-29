/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.bridge;

/**
 * A tagging interface that all bridges must implement. A bridge is responsible
 * for creating and maintaining an appropriate object according to an Element.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface Bridge {

	/**
	 * Returns the namespace URI of the element this <code>Bridge</code> is
	 * dedicated to.
	 */
	String getNamespaceURI();

	/**
	 * Returns the local name of the element this <code>Bridge</code> is dedicated
	 * to.
	 */
	String getLocalName();

	/**
	 * Returns a new instance of this bridge.
	 */
	Bridge getInstance();

}
