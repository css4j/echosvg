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
package io.sf.carte.echosvg.util.gui.resource;

import javax.swing.Action;

/**
 * This interface represents an object which maps actions to strings
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface ActionMap {
	/**
	 * Returns the action associated with the given string or null on error
	 * 
	 * @param key the key mapped with the action to get
	 * @throws MissingListenerException if the action is not found
	 */
	Action getAction(String key) throws MissingListenerException;
}
