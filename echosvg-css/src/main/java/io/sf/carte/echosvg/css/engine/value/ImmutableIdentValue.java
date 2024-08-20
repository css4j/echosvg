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

import org.w3c.api.DOMTypeException;
import org.w3c.dom.DOMException;

/**
 * Immutable identifier values.
 *
 * @author See Git history.
 * @version $Id$
 */
class ImmutableIdentValue extends IdentValue {

	/**
	 * Creates a new immutable IdentValue.
	 */
	public ImmutableIdentValue(String s) {
		super(s);
	}

	@Override
	public void setValue(String value) throws DOMTypeException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Immutable value.");
	}

	@Override
	public IdentValue clone() {
		return new IdentValue(value);
	}

}
