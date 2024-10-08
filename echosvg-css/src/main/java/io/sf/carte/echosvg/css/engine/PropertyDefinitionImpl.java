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
package io.sf.carte.echosvg.css.engine;

import io.sf.carte.doc.style.css.CSSValueSyntax;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.value.PropertyDefinition;

/**
 * Implementation of a property definition.
 */
class PropertyDefinitionImpl implements PropertyDefinition {

	private final String name;

	boolean inherits = true;

	private CSSValueSyntax syntax;

	private LexicalUnit initialValue;

	PropertyDefinitionImpl(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean inherits() {
		return inherits;
	}

	void setInherits(boolean inherits) {
		this.inherits = inherits;
	}

	@Override
	public LexicalUnit getInitialValue() {
		return initialValue;
	}

	void setInitialValue(LexicalUnit initialValue) {
		this.initialValue = initialValue;
	}

	@Override
	public CSSValueSyntax getSyntax() {
		return syntax;
	}

	void setSyntax(CSSValueSyntax syntax) {
		this.syntax = syntax;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(48);
		buf.append("@property ").append(name).append(" {\n");
		buf.append("  syntax: \"").append(syntax.toString()).append("\";\n");
		buf.append("  inherits: ").append(Boolean.toString(inherits)).append(";\n");
		if (initialValue != null) {
			buf.append("  initial-value: ").append(initialValue.toString()).append(";\n");
		}
		buf.append("}\n");
		return buf.toString();
	}

}
