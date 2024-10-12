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
 * This singleton class represents the 'revert' value.
 *
 */
public final class RevertValue extends KeywordValue {

	/**
	 * The only instance of this class.
	 */
	private static final RevertValue INSTANCE = new RevertValue();

	/**
	 * Creates a new UnsetValue object.
	 */
	private RevertValue() {
	}

	@Override
	public String getValue() {
		return "revert";
	}

	@Override
	public Type getPrimitiveType() {
		return Type.REVERT;
	}

	@Override
	public RevertValue clone() {
		return INSTANCE;
	}

	/**
	 * Get the instance of {@code revert}.
	 * 
	 * @return the instance of {@code revert}.
	 */
	public static Value getInstance() {
		return INSTANCE;
	}

}
