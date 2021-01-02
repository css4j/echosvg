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
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;

/**
 * This class provides an abstract implementation of the ValueManager interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractValueManager extends AbstractValueFactory implements ValueManager {

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	@Override
	public Value createFloatValue(short unitType, float floatValue) throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link ValueManager#createStringValue(short,String,CSSEngine)}.
	 */
	@Override
	public Value createStringValue(short type, String value, CSSEngine engine) throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm,
			Value value) {

		if ((value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)
				&& (value.getPrimitiveType() == CSSPrimitiveValue.CSS_URI)) {
			// Reveal the absolute value as the cssText now.
			return new URIValue(value.getStringValue(), value.getStringValue());
		}
		return value;
	}
}
