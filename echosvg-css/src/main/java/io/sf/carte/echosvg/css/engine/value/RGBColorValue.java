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

/**
 * This class represents RGB colors.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RGBColorValue extends AbstractValue {

	/**
	 * The red component.
	 */
	protected Value red;

	/**
	 * The green component.
	 */
	protected Value green;

	/**
	 * The blue component.
	 */
	protected Value blue;

	/**
	 * Creates a new RGBColorValue.
	 */
	public RGBColorValue(Value r, Value g, Value b) {
		red = r;
		green = g;
		blue = b;
	}

	/**
	 * The type of the value.
	 */
	@Override
	public short getPrimitiveType() {
		return CSSPrimitiveValue.CSS_RGBCOLOR;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return "rgb(" + red.getCssText() + ", " + green.getCssText() + ", " + blue.getCssText() + ')';
	}

	/**
	 * Implements {@link Value#getRed()}.
	 */
	@Override
	public Value getRed() throws DOMException {
		return red;
	}

	/**
	 * Implements {@link Value#getGreen()}.
	 */
	@Override
	public Value getGreen() throws DOMException {
		return green;
	}

	/**
	 * Implements {@link Value#getBlue()}.
	 */
	@Override
	public Value getBlue() throws DOMException {
		return blue;
	}

	/**
	 * Returns a printable representation of the color.
	 */
	@Override
	public String toString() {
		return getCssText();
	}
}
