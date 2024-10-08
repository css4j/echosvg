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
 * This singleton class represents the 'inherit' value.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public final class InheritValue extends KeywordValue {

	/**
	 * The only instance of this class.
	 */
	public static final InheritValue INSTANCE = new InheritValue();

	/**
	 * Creates a new InheritValue object.
	 */
	InheritValue() {
	}

	@Override
	public String getValue() {
		return "inherit";
	}

	@Override
	public Type getPrimitiveType() {
		return Type.INHERIT;
	}

	@Override
	public InheritValue clone() {
		return INSTANCE;
	}

	/**
	 * Get the instance of {@code inherit}.
	 * 
	 * @return the instance of {@code inherit}.
	 */
	public static Value getInstance() {
		return INSTANCE;
	}

}
