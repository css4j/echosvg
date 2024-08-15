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
 * counters() function.
 *
 */
class CountersValue extends AbstractCounterValue {

	private String separator = "";

	public CountersValue() {
		super();
	}

	CountersValue(String identifier, Value listStyle, String separator) {
		super(identifier, listStyle);
		this.separator = separator;
	}

	@Override
	public CountersValue clone() {
		return new CountersValue(getName(), getCounterStyle(), getSeparator());
	}

	@Override
	public Type getPrimitiveType() {
		return Type.COUNTERS;
	}

	@Override
	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	@Override
	public String getCssText() {
		StringBuilder buf = new StringBuilder();
		buf.append("counter(").append(getName());
		Value style = getCounterStyle();
		String listStyle;
		if (style != null && !"decimal".equalsIgnoreCase(listStyle = style.getCssText())) {
			buf.append(',').append(' ').append(listStyle);
		}
		buf.append(')');
		return buf.toString();
	}

	@Override
	public CountersValue getCounterValue() throws DOMException {
		return this;
	}

}
