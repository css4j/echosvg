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

import java.util.Objects;

import org.w3c.css.om.typed.CSSCounterValue;
import org.w3c.dom.DOMException;

/**
 * Abstract class for counter() and counters() functions.
 *
 */
abstract class AbstractCounterValue extends ComponentValue implements CSSCounterValue {

	private static final long serialVersionUID = 1L;

	private String identifier;
	private Value listStyle = null;

	protected AbstractCounterValue() {
		super();
	}

	AbstractCounterValue(String identifier, Value listStyle) {
		super();
		this.identifier = identifier;
		this.listStyle = listStyle;
	}

	@Override
	public String getName() {
		return identifier;
	}

	public void setName(String identifier) {
		this.identifier = identifier;
		if (handler != null) {
			handler.valueChanged(this);
		}
	}

	@Override
	public Value getCounterStyle() {
		return listStyle;
	}

	void setCounterStyle(AbstractValue listStyle) {
		componentize(listStyle);
		this.listStyle = listStyle;
		if (handler != null) {
			handler.valueChanged(this);
		}
	}

	@Override
	public String getSeparator() {
		return "";
	}

	@Override
	public Value item(int index) {
		if (index == 0) {
			return getCounterStyle();
		}
		return null;
	}

	void set(int index, Value component) throws DOMException {
		if (index == 0) {
			setCounterStyle((AbstractValue) component);
		}
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((listStyle == null) ? decimalHashCode() : listStyle.hashCode());
		return result;
	}

	private int decimalHashCode() {
		int result = 31 * CssType.TYPED.hashCode() + Type.IDENT.hashCode();
		result = 31 * result + "decimal".hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		AbstractCounterValue other = (AbstractCounterValue) obj;
		if (identifier == null) {
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) {
			return false;
		}

		return Objects.equals(listStyle, other.listStyle);
	}

	static void quoteSeparator(String separator, StringBuilder buf) {
		if (!separator.contains("'")) {
			buf.append('\'').append(separator).append('\'');
		} else {
			buf.append('"').append(separator).append('"');
		}
	}

}
