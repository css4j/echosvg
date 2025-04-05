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

import java.util.Locale;

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.CSSEngine;

/**
 * This class provides a manager for the property with support for identifier
 * values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class IdentifierManager extends AbstractValueManager {

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case IDENT:
			String s = lu.getStringValue().toLowerCase(Locale.ROOT).intern();
			Object v = getIdentifiers().get(s);
			if (v == null) {
				throw createInvalidIdentifierDOMException(lu.getStringValue());
			}
			return (Value) v;

		case INHERIT:
			return ValueConstants.INHERIT_VALUE;

		case UNSET:
			return UnsetValue.getInstance();

		case REVERT:
			return RevertValue.getInstance();

		case INITIAL:
			return getDefaultValue();

		case VAR:
		case ATTR:
			return createLexicalValue(lu);

		default:
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}
	}

	protected Value createIdentValue(String value, CSSEngine engine) throws DOMException {
		Object v = getIdentifiers().get(value.toLowerCase(Locale.ROOT).intern());
		if (v == null) {
			throw createInvalidIdentifierDOMException(value);
		}
		return (Value) v;
	}

	/**
	 * Returns the map that contains the name/value mappings for each possible
	 * identifiers.
	 */
	public abstract StringMap getIdentifiers();

}
