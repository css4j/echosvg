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
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.dom.DOMException;

/**
 * Manage the modification of value components.
 */
public interface ComponentHandler extends ValueModificationHandler {

	/**
	 * Notify that a component of this value changed.
	 * 
	 * @param c the modified value.
	 */
	void componentChanged(Value c);

	/**
	 * Notify that a component was added to this value.
	 * 
	 * @param c the new value.
	 */
	void componentAdded(Value c);

	/**
	 * Notify that a component was removed from this value.
	 * 
	 * @param c the removed value.
	 */
	void componentRemoved(Value c);

	/**
	 * Called when a list value has changed.
	 */
	void listValueChanged(int index, Value value) throws DOMException;

	/**
	 * Get the value that owns the components (a list, a color, etc).
	 * 
	 * @return the main value.
	 */
	Value getValue();

}
