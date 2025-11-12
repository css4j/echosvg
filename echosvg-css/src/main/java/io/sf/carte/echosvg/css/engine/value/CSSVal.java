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

import java.io.IOException;

import org.w3c.css.om.typed.CSSStyleValue;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSValueSyntax;
import io.sf.carte.doc.style.css.CSSValueSyntax.Match;
import io.sf.carte.util.SimpleWriter;

/**
 * A gateway value between CSS Typed OM and an internal representation.
 *
 * @author See Git history.
 * @version $Id$
 */
public interface CSSVal extends io.sf.carte.doc.style.css.CSSValue, CSSStyleValue {

	/**
	 * If this value is a list or contains components, the number of
	 * <code>CSSStyleValue</code>s in the list. The range of valid values of the
	 * indices is <code>0</code> to <code>length-1</code> inclusive.
	 * 
	 * @return the number of components, or {@code 0} if this value is not a list
	 *         nor does it contain components.
	 */
	int getLength();

	/**
	 * If this value is a list, give the item corresponding to the requested index.
	 * If there is no item at such index, return {@code null} If this object is not
	 * a list and the index is {@code 0}, return itself.
	 * 
	 * @param index the index on the list.
	 * @return the item, or {@code null} if there is no item on that index.
	 */
	default CSSVal item(int index) {
		return index == 0 ? this : null;
	}

	/**
	 * Convenience method that either returns an identifier or throws an exception.
	 * 
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain an identifier value.
	 */
	String getIdentifierValue() throws DOMException;

	/**
	 * If this value can be used where a string is expected, get the value.
	 * 
	 * @return the string value, without the commas.
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain a String.
	 */
	String getStringValue() throws DOMException;

	/**
	 * Convenience method that either returns a String or URI or throws an exception.
	 * 
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain a String nor a URI value.
	 */
	String getURIValue() throws DOMException;

	/**
	 * Convenience method that either returns the float value, if the value is
	 * numeric, or throws an exception.
	 * 
	 * @return the float value.
	 */
	float getFloatValue() throws DOMException;

	@Override
	default void writeCssText(SimpleWriter wri) throws IOException {
		wri.write(getCssText());
	}

	@Override
	default Match matches(CSSValueSyntax syntax) {
		return Match.PENDING;
	}

	/**
	 * Create and return a copy of this object.
	 * <p>
	 * If this object is unmodifiable, the clone will be modifiable.
	 * </p>
	 * 
	 * @return a modifiable copy of this object.
	 */
	@Override
	CSSVal clone();

}
