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
 * Base class for modification handlers.
 */
public abstract class AbstractValueModificationHandler implements ValueModificationHandler {

	/**
	 * Creates a new AbstractValueModificationHandler.
	 */
	protected AbstractValueModificationHandler() {
		super();
	}

	/**
	 * Returns the associated value.
	 */
	protected abstract Value getValue();

	/**
	 * Called when the value has changed.
	 * 
	 * @param v the value that changed.
	 */
	@Override
	public void valueChanged(Value v) throws DOMException {
		propertyTextChanged();
	}

	/**
	 * To trigger a full property set.
	 */
	protected void propertyTextChanged() throws DOMException {
		setPropertyText(getValue().getCssText());
	}

	/**
	 * To trigger a full property reparse.
	 * 
	 * @param text the property serialization.
	 */
	public abstract void setPropertyText(String text) throws DOMException;

}
