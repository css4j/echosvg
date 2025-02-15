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

/**
 * This singleton class represents the 'unset' value.
 *
 */
public final class UnsetValue extends KeywordValue {

	private static final long serialVersionUID = 1L;

	/**
	 * The only instance of this class.
	 */
	private static final UnsetValue INSTANCE = new UnsetValue();

	/**
	 * Creates a new UnsetValue object.
	 */
	private UnsetValue() {
	}

	@Override
	public String getValue() {
		return "unset";
	}

	@Override
	public Type getPrimitiveType() {
		return Type.UNSET;
	}

	@Override
	public UnsetValue clone() {
		return INSTANCE;
	}

	/**
	 * Get the instance of {@code unset}.
	 * 
	 * @return the instance of {@code unset}.
	 */
	public static Value getInstance() {
		return INSTANCE;
	}

}
