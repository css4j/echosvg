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
import org.w3c.css.om.typed.CSSKeywordValue;

/**
 * A CSS-wide non-primitive keyword value: 'inherit', 'unset', 'revert'.
 *
 */
abstract class KeywordValue extends AbstractValue implements CSSKeywordValue {

	/**
	 * Creates a new UnsetValue object.
	 */
	protected KeywordValue() {
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return getValue();
	}

	@Override
	public void setValue(String value) throws DOMTypeException {
		throw new DOMTypeException("Not supported.");
	}

	/**
	 * A code defining the type of the value.
	 */
	@Override
	public CssType getCssValueType() {
		return CssType.KEYWORD;
	}

}
