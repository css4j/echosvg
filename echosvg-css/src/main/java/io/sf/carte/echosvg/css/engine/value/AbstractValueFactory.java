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

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class provides a base implementation for the value factories.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class AbstractValueFactory {

	/**
	 * Returns the name of the property handled.
	 */
	public abstract String getPropertyName();

	/**
	 * Resolves an URI.
	 */
	protected static String resolveURI(ParsedURL base, String value) {
		return new ParsedURL(base, value).toString();
	}

	/**
	 * Creates a DOM exception, given an invalid identifier.
	 */
	protected DOMException createInvalidIdentifierDOMException(String ident) {
		Object[] p = { getPropertyName(), ident };
		String s = Messages.formatMessage("invalid.identifier", p);
		return new DOMException(DOMException.SYNTAX_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid lexical unit type.
	 */
	protected DOMException createInvalidLexicalUnitDOMException(LexicalUnit.LexicalType type) {
		Object[] p = { getPropertyName(), type.toString() };
		String s = Messages.formatMessage("invalid.lexical.unit", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid float type.
	 */
	protected DOMException createInvalidFloatTypeDOMException(short t) {
		Object[] p = { getPropertyName(), (int) t };
		String s = Messages.formatMessage("invalid.float.type", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid float value.
	 */
	protected DOMException createInvalidFloatValueDOMException(float f) {
		Object[] p = { getPropertyName(), f };
		String s = Messages.formatMessage("invalid.float.value", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid string type.
	 */
	protected DOMException createInvalidStringTypeDOMException(Type t) {
		Object[] p = { getPropertyName(), t };
		String s = Messages.formatMessage("invalid.string.type", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	protected DOMException createMalformedLexicalUnitDOMException() {
		Object[] p = { getPropertyName() };
		String s = Messages.formatMessage("malformed.lexical.unit", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	protected DOMException createDOMException() {
		Object[] p = { getPropertyName() };
		String s = Messages.formatMessage("invalid.access", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

}
